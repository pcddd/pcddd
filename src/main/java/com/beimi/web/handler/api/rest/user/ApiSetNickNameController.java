package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/setNickName")
public class ApiSetNickNameController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private PlayUserRepository playUserRepository;

    @RequestMapping
    public ResponseEntity<PcData> setNickName(@Valid String token,@Valid String nickname,@Valid String personalword) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)) {
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                if (StringUtils.isNotEmpty(nickname) && StringUtils.isNotEmpty(personalword)){
                    PlayUser playUser = playUserRepository.findByToken(token);
                    playUser.setNickname(nickname);
                    playUser.setPersonalword(personalword);
                    playUserRepository.save(playUser);
                    pcData = new PcData("200", "成功", null);
                }else{
                    pcData = new PcData("200", "请输入正确的昵称和签名", null);
                }
            }else{
                pcData = new PcData("201", "token失效,请重新登陆", null);
            }
        }else{
            pcData = new PcData("203", "token为空",null);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
