package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
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
    public ResponseEntity<PcData> getUserInfo(HttpServletRequest request) {
        PcData resu=null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRepository.findByToken(userToken.getId());
        HashMap hashMap = new HashMap();
        hashMap.put("account",StringUtils.isBlank(playUser.getUsername()) ? "" : playUser.getUsername());
        hashMap.put("user_photo",StringUtils.isBlank(playUser.getHeadportrait()) ? "" : playUser.getHeadportrait());
        hashMap.put("mobile",StringUtils.isBlank(playUser.getMobile()) ? "" : playUser.getMobile());
        hashMap.put("nick_name",StringUtils.isBlank(playUser.getNickname()) ? "" : playUser.getNickname());
        hashMap.put("personal_sign",StringUtils.isBlank(playUser.getPersonalword()) ? "" : playUser.getPersonalword());
        hashMap.put("real_name",StringUtils.isBlank(playUser.getReal_name()) ? "" : playUser.getReal_name());
        hashMap.put("bank_name",StringUtils.isBlank(playUser.getBank_name()) ? "" : playUser.getBank_name());
        hashMap.put("bank_no",StringUtils.isBlank(playUser.getBank_no()) ? "" : playUser.getBank_no());
        hashMap.put("open_card_address",StringUtils.isBlank(playUser.getOpen_card_address()) ? "" : playUser.getOpen_card_address());
        hashMap.put("level",playUser.getLevel());
        hashMap.put("level_name",playUser.getLevel_name());
        hashMap.put("point",playUser.getGoldcoins());
        hashMap.put("withdrawals_password",StringUtils.isNotEmpty(playUser.getWithdrawals_password()) ? playUser.getWithdrawals_password() : "");
        resu=new PcData( "200",MessageEnum.USER_REGISTER_SUCCESS , hashMap);
        return new ResponseEntity<>(resu,HttpStatus.OK);
    }
}
