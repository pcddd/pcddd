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
import org.springframework.web.bind.annotation.RequestMethod;
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
public class ApiPcLoginController {
    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private PlayUserClientESRepository playUserClientRes ;

    @Autowired
    private PlayUserRepository playUserRes ;

    @Autowired
    private TokenESRepository tokenESRes ;

    @SuppressWarnings("rawtypes")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PcData> caiGuest(HttpServletRequest request , @Valid String username ,@Valid String password) {
//        PlayUserClient playUserClient = null ;
        PcData playerResultData=null;
        PlayUser playUser=playUserRes.findByUsername(username);
        Token userToken = null ;
//        playUserClient = playUserClientRes.findByUsername(username);
        if (playUser!=null) {
            String ip = UKTools.getIpAddr(request);
            IP ipdata = IPTools.getInstance().findGeography(ip);
            userToken = new Token();
            userToken.setIp(ip);
            userToken.setRegion(ipdata.getProvince() + ipdata.getCity());
            userToken.setId(UKTools.getUUID());

            userToken.setUserid(playUser.getId());
            userToken.setCreatetime(new Date());
            userToken.setOrgi(playUser.getOrgi());
            AccountConfig config = CacheConfigTools.getGameAccountConfig(BMDataContext.SYSTEM_ORGI);
            if (config != null && config.getExpdays() > 0) {
                userToken.setExptime(new Date(System.currentTimeMillis() + 60 * 60 * 24 * config.getExpdays() * 1000));//默认有效期 ， 7天
            } else {
                userToken.setExptime(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 7 * 1000));//默认有效期 ， 7天
            }
            userToken.setLastlogintime(new Date());
            userToken.setUpdatetime(new Date(0));

            tokenESRes.save(userToken);
            playUser.setToken(userToken.getId());
            UserInfo userInfo = new UserInfo();
            userInfo.token = userToken.getId();
            userInfo.id = playUser.getId();
            userInfo.account = playUser.getUsername();
            userInfo.nick_name = playUser.getNickname();
            userInfo.point = playUser.getGoldcoins();
            userInfo.personal_sign = playUser.getPersonalword();
            playUser.setToken(userToken.getId());
            CacheHelper.getApiUserCacheBean().put(userToken.getId(), userToken, userToken.getOrgi());
            CacheHelper.getApiUserCacheBean().put(playUser.getId(), playUser, userToken.getOrgi());
            if (!playUser.getPassword().equals(UKTools.md5(password))) {
                playerResultData = new PcData("201", "密码错误", null);
            } else {
                playerResultData = new PcData( playUser != null ? "200" : "201", playUser != null ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_REGISTER_FAILD_USERNAME, userInfo);
                UKTools.published(playUser , playUserESRes , playUserRes , BMDataContext.UserDataEventType.SAVE.toString());
            }
        }else {
            playerResultData = new PcData("203", "用户未注册", null);
        }
        return new ResponseEntity<>(playerResultData, HttpStatus.OK);
    }
}
