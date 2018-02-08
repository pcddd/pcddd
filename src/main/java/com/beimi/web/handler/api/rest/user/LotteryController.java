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

   /* public Lottery parseLotteryJson(String lotteryJson){
        try{
            JSONObject jsonObject = JSON.parseObject(lotteryJson);
            return new Lottery(jsonObject.getString("no"),jsonObject.getString("preno"),
                    jsonObject.getString("prenum"),getPatternStr(jsonObject.getString("endtime")),
                    jsonObject.getString("endtimeString"),
                    getPatternStr(jsonObject.getString("now")));


        }catch (Exception e){
            System.out.println("（解析 http://pckai.cc/api/latest?lotteryId=3&code=bjkl8 异常 ）" + e);
            e.printStackTrace();
        }

        return null;
    }

    private String getPatternStr(String str){
        Pattern p = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher m = p.matcher(str);
        while(m.find()) {
            return m.group();
        }
        return null;
    }

    *//**
     * 获取json数据
     * @param url 链接
     * @return
     *//*
    public String SendGet(String url){
        // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try
        {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null)
            {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line;
            }
        } catch (Exception e)
        {
            System.out.println("（http://pckai.cc/api/latest?lotteryId=3&code=bjkl8 请求异常）" + e);
            e.printStackTrace();
        }
        // 使用finally来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            } catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        return result;
    }*/
}
