package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import com.beimi.web.service.repository.jpa.TypeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by fanling on 2018/2/8.
 */

@RestController
@RequestMapping("/api/addGold")
public class AddGoldController {

    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private PlayUserRepository playUserRes ;

    @RequestMapping
    public ResponseEntity<PcData> addGold(@Valid PlayUser playUser, @Valid Token token) {
        PcData pcData=null;
        playUser = playUserRes.findById(token.getId());
        if (playUser!=null){
            playUser.setDiamonds(playUser.getDiamonds());
            playUser.setPeriods(playUser.getPeriods());
            playUser.setLotterType(playUser.getLotterType());
            playUserESRes.save(playUser);
//            playUserRes.save(playUser);
            pcData=new PcData(true,"200","保存成功");
        }
        else {
            pcData=new PcData(true,"201","保存失败");
        }
        return null;
    }
}
