package com.beimi.web.handler.api.rest.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beimi.util.Tools;
import com.beimi.web.model.Lottery;
import com.beimi.web.model.PcData;
import com.beimi.web.model.ResultData;
import com.beimi.web.service.repository.es.LotteryResESRepository;
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

    @RequestMapping
    public ResponseEntity<PcData> Lottery(){
        PcData resu=null;
        Iterator<Lottery> iterator = lotteryResESRepository.findAll().iterator();
        if (iterator.hasNext()){
            resu=new PcData(true,"200","请求成功",iterator.next());
        }else{
            resu=new PcData("暂无信息","201");
        }
        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
