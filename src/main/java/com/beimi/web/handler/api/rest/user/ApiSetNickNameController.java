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

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PcData> setNickName(HttpServletRequest request,@Valid String nickname, @Valid String personalword) {
        PcData pcData = null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        if (StringUtils.isNotEmpty(nickname) && StringUtils.isNotEmpty(personalword)){
            PlayUser playUser = playUserRepository.findByToken(userToken.getId());
            playUser.setNickname(nickname);
            playUser.setPersonalword(personalword);
            playUserRepository.save(playUser);
            pcData = new PcData("200", "成功", null);
        }else{
            pcData = new PcData("200", "请输入正确的昵称和签名", null);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
