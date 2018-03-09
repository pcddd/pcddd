package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by fanling on 2018/2/4.
 */
@RestController
@RequestMapping("/api/Level")
public class ApiRoomLevelController {
    @Autowired
    private GamePlaywayRepository gamePlayRes;

    @RequestMapping
    public ResponseEntity<PcData> level(@Valid String orgi){
        PcData resu=null;
        List<GamePlayway> roomLevelInfo=null;
        roomLevelInfo = gamePlayRes.findByGametype(Integer.parseInt(orgi), new Sort(Sort.Direction.ASC, "roomtype"));
        resu=new PcData(roomLevelInfo.size() != 0?"200":"201", roomLevelInfo.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_PLAYWAY,
                new ListContainer<>(roomLevelInfo));
        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
