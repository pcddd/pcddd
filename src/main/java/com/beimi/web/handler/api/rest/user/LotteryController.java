package com.beimi.web.handler.api.rest.user;

import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BjLotteryResESRepository;
import com.beimi.web.service.repository.es.JNDLotteryResESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by fanling on 2018/2/7.
 */
@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    @Autowired
    private BjLotteryResESRepository bjLotteryResESRepository;

    @Autowired
    private JNDLotteryResESRepository jndLotteryResESRepository;

    @Autowired
    private TokenESRepository tokenESRes ;

    @Autowired
    private PlayUserRepository playUserRes ;

    @RequestMapping
    public ResponseEntity<PcData> Lottery(@Valid String token,@Valid int type){
        // type  1北京 2加拿大
        Token userToken=null;
        PlayUser playUser = null;
        if(!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token) ;
            playUser = playUserRes.findByToken(token);
            if(userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date()) &&
                playUser != null){
                PcData resu=null;
                int intervalSec;
                Iterator<Lottery> iterator = null;
                if (type == 1){
                    iterator = bjLotteryResESRepository.findAll().iterator();
                    intervalSec = 300;
                }else{
                    iterator = jndLotteryResESRepository.findAll().iterator();
                    intervalSec = 210;
                }
                if (iterator.hasNext()){
                    Lottery lottery = iterator.next();
                    long nexttime = Integer.parseInt(lottery.getNextTime()) - new Date().getTime()/1000;
                    if (nexttime < 0){
                        lottery.setNextOpenSec(0);
                        if(nexttime < -intervalSec){
                            //停盘
                            lottery.setStauts(3);
                        }else{
                            //客户端显示?,?,? 同时进行下一期下注，此时不断请求新的彩果
                            lottery.setStauts(4);
                            lottery.setNextOpenSec(intervalSec + nexttime - 20);
                            lottery.setCurNo(lottery.getCurNo()+1);
                            lottery.setCurRes("?,?,?=?");
                        }
                    }else if (nexttime > 20){
                        //
                        lottery.setStauts(1);
                        lottery.setNextOpenSec(nexttime - 20);
                    }else{
                        //提前20s封盘
                        lottery.setStauts(2);
                        lottery.setNextOpenSec(0);
                    }
                    lottery.setCurPoint(playUser.getGoldcoins());
                    resu=new PcData("200","请求成功",lottery);
                }else{
                    resu=new PcData("201","暂无信息",null);
                }
                return new ResponseEntity<>(resu, HttpStatus.OK);
            }else{
                //过期
                if(userToken!=null){
                    tokenESRes.delete(userToken);
                }
                return new ResponseEntity<>(new PcData("201","登录已失效，请重新登录",null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new PcData("201","登录已失效，请重新登录",null), HttpStatus.OK);
    }
}
