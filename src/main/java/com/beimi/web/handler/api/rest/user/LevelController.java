package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.model.ResultData;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by fanling on 2018/2/4.
 */
@RestController
@RequestMapping("/api/Level")
public class LevelController {
    @Autowired
    private GamePlaywayRepository gamePlayRes;

    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<ResultData> level(@Valid String orgi,@Valid String token){
        Token userToken = null ;
        List<GamePlayway> roomLevelInfo=null;
        if(!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token) ;
            if(userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
                //返回token， 并返回游客数据给游客
                 roomLevelInfo = gamePlayRes.findByOrgi(orgi, new Sort(Sort.Direction.ASC, "sortindex"));
            }else{
                if(userToken!=null){
                    tokenESRes.delete(userToken);
                    userToken = null ;
                }
            }
        }
        ResultData resu=new ResultData(roomLevelInfo.size() != 0, roomLevelInfo.size() != 0?"200":"201", roomLevelInfo.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_PLAYWAY, roomLevelInfo);

        return new ResponseEntity<>(resu, HttpStatus.OK);
    };
}
