package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.*;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.PlayUserClientESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by fanling on 2018/2/2.
 */
@RestController
@RequestMapping("/api/caiGuest")
public class GuestController {
    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private PlayUserClientESRepository playUserClientRes ;

    @Autowired
    private PlayUserRepository playUserRes ;

    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<ResultData> caiGuest(HttpServletRequest request , @Valid String token) {
        PlayUserClient playUserClient = null ;
        Token userToken = null ;
        if(!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token) ;
            if(userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
                //返回token， 并返回游客数据给游客
                playUserClient = playUserClientRes.findById(userToken.getUserid()) ;
                if(playUserClient!=null){
                    playUserClient.setToken(userToken.getId());
                }
            }else{
                if(userToken!=null){
                    tokenESRes.delete(userToken);
                    userToken = null ;
                }
            }
        }
        String ip = UKTools.getIpAddr(request);
        IP ipdata = IPTools.getInstance().findGeography(ip);
        playUserClient.setToken(userToken.getId());
        CacheHelper.getApiUserCacheBean().put(userToken.getId(),userToken, userToken.getOrgi());
        CacheHelper.getApiUserCacheBean().put(playUserClient.getId(),playUserClient, userToken.getOrgi());
        ResultData playerResultData = new ResultData( playUserClient!=null , playUserClient != null ? MessageEnum.USER_REGISTER_SUCCESS: MessageEnum.USER_REGISTER_FAILD_USERNAME , "200",playUserClient , userToken) ;
        /**
         * 根据游戏配置 ， 选择 返回的 玩法列表
         */
        return new ResponseEntity<>(playerResultData, HttpStatus.OK);
    }
}
