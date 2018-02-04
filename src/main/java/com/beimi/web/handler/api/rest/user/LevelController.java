package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.ResultData;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by fanling on 2018/2/4.
 */
@RestController
@RequestMapping("/api/Level")
public class LevelController {
    @Autowired
    private GamePlaywayRepository gamePlayRes;

    @RequestMapping
    public ResponseEntity<ResultData> level(@Valid String orgi){
        List<GamePlayway> list = gamePlayRes.findByOrgi(orgi, new Sort(Sort.Direction.ASC, "sortindex"));
        ResultData resu=new ResultData(list.size() != 0, list.size() != 0?"200":"201", list.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_PLAYWAY, list);

        return new ResponseEntity<>(resu, HttpStatus.OK);
    };
}
