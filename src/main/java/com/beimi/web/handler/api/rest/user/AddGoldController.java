package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
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
                    playUser.setDiamonds(playUser.getDiamonds()+diamonds);//下注金额
                    playUser.setPeriods(periods);//期数
                    playUser.setLotterType(lotterType);//投注类型
                    if (playUser.getGoldcoins()>playUser.getDiamonds()) {
                        playUserESRes.save(playUser);
                        pcData = new PcData(true, "200", "保存成功");
                    }else {
                        pcData=new PcData(false,"204","余额不足");
                    }
                } else {
                    pcData = new PcData(true, "201", "没有找到此用户");
                }
            }else {
                pcData = new PcData(true, "202", "token失效");
            }
        }
        else {
            pcData = new PcData(true, "203", "token为空");
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }

}
