package com.beimi.web.handler.api.rest.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beimi.util.MessageEnum;
import com.beimi.util.Tools;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.LotteryResESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanling on 2018/2/7.
 */
@RestController
@RequestMapping("/api/lottery")
public class LotteryController {

    @Autowired
    private LotteryResESRepository lotteryResESRepository;

    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<PcData> Lottery(@Valid String token){

        Token userToken=null;

        if(!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token) ;
            if(userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
                PcData resu=null;
                Iterator<Lottery> iterator = lotteryResESRepository.findAll().iterator();
                if (iterator.hasNext()){
                    Lottery lottery = iterator.next();
                    lottery.setNextOpenSec(Integer.parseInt(lottery.getNexTime()) - new Date().getTime()/1000);
                    resu=new PcData(true,"200","请求成功",lottery);
                }else{
                    resu=new PcData("暂无信息","201");
                }

                return new ResponseEntity<>(resu, HttpStatus.OK);
            }else{
                //过期
                if(userToken!=null){
                    tokenESRes.delete(userToken);
                    userToken = null ;
                }
                return new ResponseEntity<>(new PcData("登录已失效，请重新登录","201"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new PcData("登录已失效，请重新登录","201"), HttpStatus.OK);
    }
}
