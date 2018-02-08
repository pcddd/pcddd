package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.Lottery;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.PlayUserClient;
import com.beimi.web.model.ResultData;
import com.beimi.web.service.repository.es.GameESRepository;
import com.beimi.web.service.repository.es.PlayUserClientESRepository;
import com.beimi.web.service.repository.jpa.PlayUserClientRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by fanling on 2018/2/8.
 */

@RestController
@RequestMapping("/api/addGold")
public class AddGoldController {

    @Autowired
    private PlayUserClientESRepository playUserClientEsRes ;

    @Autowired
    private GameESRepository gameESRepository;

    @Autowired
    private PlayUserRepository playUserRes ;

    @RequestMapping
    public ResponseEntity<ResultData> addGold(@Valid PlayUserClient playUserClient) {
        PlayUser playUser = playUserRes.findByUsername("111");
        if (playUserClient!=null){
            playUserClient.setGoldcoins(13);
            playUserClient.setPeriods("7874");
            playUserClient.setRate(45);
            playUserClientEsRes.deleteAll();
            playUserClientEsRes.save(playUserClient);
            playUserRes.save(playUser);
        }
        return null;
    }
}
