package com.beimi.util;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static HttpUtils mHttpUtils;
    private OkHttpClient client;
    //超时时间
    public static final int TIMEOUT=1000*6;

    //json请求
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    public static HttpUtils getInstance(){
       if (mHttpUtils == null){
           mHttpUtils = new HttpUtils();
       }
       return mHttpUtils;
    }

    private HttpUtils() {
        initOkhttp();
    }

    private void initOkhttp(){
        client = new OkHttpClient();
        client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(TIMEOUT,TimeUnit.SECONDS).readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private OkHttpClient getOkClient(){
        if (client == null)
            initOkhttp();
        return client;
    }


    /**
     * post请求  json数据为body
     *
     */
    public void postJson(String url,String token,String json,final HttpCallBack callBack){
        RequestBody body = RequestBody.create(JSON,json);
        final Request request = new Request.Builder().url(url).addHeader("Authorization",token)
                .post(body).build();
        OnStart(callBack);

        enqueue(request,callBack);
    }
    private void enqueue(Request request,final HttpCallBack callBack){
        getOkClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OnError(callBack, e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callBack, response.body().string());
                } else {
                    OnError(callBack, response.message());
                }
            }
        });
    }
    /**
     * post请求  map是body
     *
     * @param url
     * @param map
     * @param callBack
     */
    public void postMap(String url, Map<String,String> map, final HttpCallBack callBack){
        FormBody.Builder builder=new FormBody.Builder();

        //遍历map
        if(map!=null){
            for (Map.Entry<String,String> entry : map.entrySet()){
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        OnStart(callBack);

        enqueue(request,callBack);
    }
    /**
     * get 请求
     *
     * @param url
     * @param callBack
     */
    public void getJson(String url,final HttpCallBack callBack){
        Request request = new Request.Builder().url(url).build();
        OnStart(callBack);
        enqueue(request,callBack);

    }
    private void OnStart(HttpCallBack callBack){
        if(callBack!=null){
            callBack.onstart();
        }
    }
    private void onSuccess(final HttpCallBack callBack,final String data){
        if(callBack!=null){
            callBack.onSusscess(data);
        }
    }
    private void OnError(final HttpCallBack callBack,final String msg){
        if(callBack!=null){
            callBack.onError(msg);
        }
    }
    public static abstract class HttpCallBack{
        //开始
        public void onstart(){};
        //成功回调
        public abstract void onSusscess(String data);
        //失败
        public void onError(String meg){}
    }

    /**
     * 进入房间
     * @param hxToken
     * @param username
     */
    public void postJoinRoomMes(String hxToken,String username){
        String json = "{\"target_type\" : \"chatrooms\",\"target\" : [\"42187441111041\"],\"msg\" : {\"type\" : \"txt\",\"msg\" : \"{notice_type:1,nick_name:"+ username +
                ",level:1}\"},\"from\" : \"" + username + "\"} ";
        postJson(HxService.MESSAGE,
                hxToken,
                json,
                new HttpUtils.HttpCallBack() {
                    @Override
                    public void onSusscess(String data) {
//                        System.out.println(data);
                    }

                    @Override
                    public void onError(String meg) {
                        super.onError(meg);
                    }
                });
    }


    /**
     *  下注
     */
    public void postBetMes(String hxToken,String user_photo,String nick_name,int game_count,String game_type,int point,int level){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"target_type\" : \"chatrooms\",\"target\" : [\"42187441111041\"],\"msg\" : {\"type\" : \"txt\",\"msg\" : \"{notice_type:2,nick_name:")
                .append(nick_name)
//                .append(",user_photo:")
//                .append(user_photo)
                .append(",game_count:")
                .append(game_count)
                .append(",game_type:")
                .append(game_type)
                .append(",point:")
                .append(point)
                .append(",level:")
                .append(level)
                .append("}\"},\"from\" : \"")
                .append(nick_name)
                .append("\"} ");

        postJson(HxService.MESSAGE,
                hxToken,
                stringBuilder.toString(),
                new HttpUtils.HttpCallBack() {
                    @Override
                    public void onSusscess(String data) {
//                        System.out.println(data);
                    }

                    @Override
                    public void onError(String meg) {
                        super.onError(meg);
                    }
                });
    }

    /**
     * 开奖信息
     */
    public void postOpenLotteryMes(String hxToken,int game_count,String ext_content,long create_time){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"target_type\":\"chatrooms\",\"target\":[\"42187441111041\"],\"msg\":{\"type\":\"txt\",\"msg\":\"{notice_type:3,game_count:")
                .append(game_count)
                .append(",level:1,ext_content:\\\"")
                .append(ext_content)
                .append("\\\",create_time:")
                .append(create_time)
                .append("}\"},\"from\":\"")
                .append("admin")
                .append("\"} ");

        postJson(HxService.MESSAGE,
                hxToken,
                stringBuilder.toString(),
                new HttpUtils.HttpCallBack() {
                    @Override
                    public void onSusscess(String data) {
//                        System.out.println(data);
                    }

                    @Override
                    public void onError(String meg) {
                        super.onError(meg);
                    }
                });

    }

    /**
     * 开盘
     */
    public void postOpenBetMes(String hxToken,String game_count,String extraStr){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"target_type\" : \"chatrooms\",\"target\" : [\"42187441111041\"],\"msg\" : {\"type\" : \"txt\",\"msg\" : \"{notice_type:5,game_count:")
                .append(game_count)
                .append(",ext_content:")
                .append(extraStr)
                .append("}\"},\"from\" : \"")
                .append("admin")
                .append("\"} ");

        postJson(HxService.MESSAGE,
                hxToken,
                stringBuilder.toString(),
                new HttpUtils.HttpCallBack() {
                    @Override
                    public void onSusscess(String data) {
//                        System.out.println(data);
                    }

                    @Override
                    public void onError(String meg) {
                        super.onError(meg);
                    }
                });

    }
    /**
     * 封盘
     */
    public void postCloseBetMes(String hxToken,String game_count){



        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"target_type\" : \"chatrooms\",\"target\" : [\"42187441111041\"],\"msg\" : {\"type\" : \"txt\",\"msg\" : \"{notice_type:4,game_count:")
                .append(game_count)
                .append("}\"},\"from\" : \"")
                .append("admin")
                .append("\"} ");

        postJson(HxService.MESSAGE,
                hxToken,
                stringBuilder.toString(),
                new HttpUtils.HttpCallBack() {
                    @Override
                    public void onSusscess(String data) {
//                        System.out.println(data);
                    }

                    @Override
                    public void onError(String meg) {
                        super.onError(meg);
                    }
                });

    }





}