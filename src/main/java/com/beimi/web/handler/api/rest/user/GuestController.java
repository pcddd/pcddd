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
    public ResponseEntity<ResultData> caiGuest(HttpServletRequest request , @Valid String username) {
        PlayUserClient playUserClient = null ;
        Token userToken = null ;
        playUserClient = playUserClientRes.findByUsername(username);
        String ip = UKTools.getIpAddr(request);
        IP ipdata = IPTools.getInstance().findGeography(ip);
        if(userToken == null){
            userToken = new Token();
            userToken.setIp(ip);
            userToken.setRegion(ipdata.getProvince()+ipdata.getCity());
            userToken.setId(UKTools.getUUID());
            userToken.setUserid(playUserClient.getId());
            userToken.setCreatetime(new Date());
            userToken.setOrgi(playUserClient.getOrgi());
            AccountConfig config = CacheConfigTools.getGameAccountConfig(BMDataContext.SYSTEM_ORGI) ;
            if(config!=null && config.getExpdays() > 0){
                userToken.setExptime(new Date(System.currentTimeMillis()+60*60*24*config.getExpdays()*1000));//默认有效期 ， 7天
            }else{
                userToken.setExptime(new Date(System.currentTimeMillis()+60*60*24*7*1000));//默认有效期 ， 7天
            }
            userToken.setLastlogintime(new Date());
            userToken.setUpdatetime(new Date(0));

            tokenESRes.save(userToken) ;
        }
        playUserClient.setToken(userToken.getId());
        CacheHelper.getApiUserCacheBean().put(userToken.getId(),userToken, userToken.getOrgi());
        CacheHelper.getApiUserCacheBean().put(playUserClient.getId(),playUserClient, userToken.getOrgi());
        ResultData playerResultData = new ResultData( playUserClient!=null , playUserClient != null ? MessageEnum.USER_REGISTER_SUCCESS: MessageEnum.USER_REGISTER_FAILD_USERNAME ,playUserClient != null?"200":"201", playUserClient , userToken) ;
        return new ResponseEntity<>(playerResultData, HttpStatus.OK);
    }
}
