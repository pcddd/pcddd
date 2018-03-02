package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.GameBetFatherTypeRepository;
import com.beimi.web.service.repository.jpa.GameBetTypeRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fanling on 2018/2/4.
 */
@RestController
@RequestMapping("/api/getUserInfo")
public class ApiGetUserInfoController {
    @Autowired
    private PlayUserRepository playUserRepository;

    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<PcData> getUserInfo(@Valid String token) {
        PcData resu=null;
        if(!StringUtils.isBlank(token)){
            Token userToken = userToken = tokenESRes.findById(token);
            if(userToken != null){
                if (!StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
                    PlayUser playUser = playUserRepository.findByToken(token);
                    HashMap hashMap = new HashMap();
                    hashMap.put("nick_name",StringUtils.isBlank(playUser.getNickname()) ? "" : playUser.getNickname());
                    hashMap.put("personal_sign",StringUtils.isBlank(playUser.getPersonalword()) ? "" : playUser.getPersonalword());
                    hashMap.put("level",playUser.getLevel());
                    hashMap.put("level_name",playUser.getLevel_name());
                    hashMap.put("point",playUser.getGoldcoins());
                    resu=new PcData( "200",MessageEnum.USER_REGISTER_SUCCESS , hashMap);
                    return new ResponseEntity<>(resu,HttpStatus.OK);
                }else{
                    tokenESRes.delete(userToken);
                }
            }
        }
        return new ResponseEntity<>( new PcData("201",MessageEnum.USER_TOKEN, resu),HttpStatus.OK);
    }
}
