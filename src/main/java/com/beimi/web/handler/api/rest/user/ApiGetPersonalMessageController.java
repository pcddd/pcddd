package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PersonalMesRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import com.beimi.web.service.repository.jpa.SystemMesRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/getPersonalMes")
public class ApiGetPersonalMessageController {

    @Autowired
    PersonalMesRepository personalMesRepository;

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private PlayUserRepository playUserRes;

    @RequestMapping
    public ResponseEntity<PcData> getPersonalMes(HttpServletRequest request) {
        PcData resu=null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser == null){
            resu=new PcData( "201", "查无此人",
                    null);
        }else{
            List<PersonalMesModel> personalMesModels = personalMesRepository.findByUserid(playUser.getId());
            HashMap hashMap = new HashMap();
            hashMap.put("data",personalMesModels);
            resu=new PcData( personalMesModels.size() != 0?"200":"201", personalMesModels.size() != 0 ? "成功": "暂无消息",
                    hashMap);
        }

        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
