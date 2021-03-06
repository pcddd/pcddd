package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.PcddPeriodsESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by fanling on 2018/2/7.
 */
@RestController
@RequestMapping("/api/lottery")
public class ApiLotteryController {

    @Autowired
    private PcddPeriodsESRepository pcddPeriodsESRepository;

    @Autowired
    private TokenESRepository tokenESRes ;

    @Autowired
    private PlayUserRepository playUserRes ;

    @RequestMapping
    public ResponseEntity<PcData> Lottery(HttpServletRequest request,@Valid int type){
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser==null)
            return new ResponseEntity<>(new PcData("201","未知用户",null), HttpStatus.OK);

        PcddPeriods pcddPeriods = getCurLottery(type);
        PcData resu;
        int intervalSec;
        // type  1北京 2加拿大
        if (type == 1){
            intervalSec = 300;
        }else{
            intervalSec = 210;
        }
        if (pcddPeriods != null){
            LotteryClient lottery = new LotteryClient(pcddPeriods.getPeriods(),
                    pcddPeriods.getRes(),pcddPeriods.getResname(),pcddPeriods.getStatus(),0,pcddPeriods.getOpentime(),pcddPeriods.getColorid());
            long nexttime = Integer.parseInt(pcddPeriods.getOpentime()) - new Date().getTime()/1000;
            if (nexttime < 0){
                lottery.setNextOpenSec(0);
                if(nexttime < -intervalSec){
                    //停盘
                    lottery.setStauts(3);
                }else{
                    //客户端显示?,?,? 同时进行下一期下注
                    lottery.setStauts(4);
                    lottery.setNextOpenSec(intervalSec + nexttime - 15);
                    lottery.setCurNo(lottery.getCurNo()+1);
                    lottery.setCurRes("?,?,?=?");
                }
            }else if (nexttime > 15){
                //
                lottery.setStauts(1);
                lottery.setNextOpenSec(nexttime - 15);
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
    }

    private PcddPeriods getCurLottery(int type){
        PcddPeriods pcddPeriods = null;
        if (type == 1){
            pcddPeriods = (PcddPeriods) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_BJ_LOTTERY, BMDataContext.SYSTEM_ORGI);
            if (pcddPeriods == null){
                Iterator<PcddPeriods> iterator = pcddPeriodsESRepository.findByType(type,new Sort(Sort.Direction.DESC, "periods")).iterator();
                if (iterator.hasNext()){
                    pcddPeriods = iterator.next();
                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_BJ_LOTTERY,pcddPeriods,BMDataContext.SYSTEM_ORGI);
                }
            }
        }else{
            pcddPeriods = (PcddPeriods) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_JND_LOTTERY, BMDataContext.SYSTEM_ORGI);
            if (pcddPeriods == null){
                Iterator<PcddPeriods> iterator = pcddPeriodsESRepository.findByType(type,new Sort(Sort.Direction.DESC, "periods")).iterator();
                if (iterator.hasNext()){
                    pcddPeriods = iterator.next();
                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_JND_LOTTERY,pcddPeriods,BMDataContext.SYSTEM_ORGI);
                }
            }
        }
        return pcddPeriods;
    }
}
