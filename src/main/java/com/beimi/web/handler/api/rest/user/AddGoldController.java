package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.GameDetailESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import com.beimi.web.service.repository.jpa.TypeGroupRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by fanling on 2018/2/8.
 */

@RestController
@RequestMapping("/api/addGold")
public class AddGoldController {

    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private GameDetailESRepository gameDetailESRes;

    @Autowired
    private TokenESRepository tokenESRes;

    @RequestMapping
    public ResponseEntity<PcData> addGold(@Valid int diamonds,@Valid String lotterType,@Valid String periods, @Valid String token) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                PlayUser  playUser = playUserESRes.findById(userToken.getUserid());
                if (playUser != null) {
                    GameDetail gameDetail=new GameDetail();
                    gameDetail.setUserId(userToken.getUserid());//玩家id
                    gameDetail.setDiamonds(diamonds);//下注金额
                    gameDetail.setPeriods(periods);//期数
                    gameDetail.setLotterType(lotterType);//投注类型
                    if (playUser.getGoldcoins()>gameDetail.getDiamonds()) {
                        gameDetailESRes.save(gameDetail);
                        pcData = new PcData("保存成功","200");
                    }else {
                        pcData=new PcData("余额不足","204");
                    }
                } else {
                    pcData = new PcData("没有找到此用户", "201");
                }
            }else {
                pcData = new PcData("token失效", "202" );
            }
        }
        else {
            pcData = new PcData("token为空", "203" );
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }

}
