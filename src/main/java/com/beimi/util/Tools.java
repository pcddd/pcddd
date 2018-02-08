package com.beimi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beimi.web.handler.api.rest.user.ApiResultController;
import com.beimi.web.model.Lottery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanling on 2018/2/7.
 */
public class Tools {
    public static Lottery parseLotteryJson(String lotteryJson){
        try{
            JSONObject jsonObject = JSON.parseObject(lotteryJson);
            return new Lottery(jsonObject.getInteger("no"),jsonObject.getInteger("preno"),
                    jsonObject.getString("prenum"),getPatternStr(jsonObject.getString("endtime")),
                    jsonObject.getString("endtimeString"),
                    getPatternStr(jsonObject.getString("now")));


        }catch (Exception e){
            System.out.println("（解析 http://pckai.cc/api/latest?lotteryId=3&code=bjkl8 异常 ）" + e);
            e.printStackTrace();
        }

        return null;
    }

    public static String getPatternStr(String str){
        Pattern p = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher m = p.matcher(str);
        while(m.find()) {
            return m.group();
        }
        return null;
    }

    /**
     * 获取json数据
     * @param url 链接
     * @return
     */
    public static String SendGet(String url){
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
    }
}
