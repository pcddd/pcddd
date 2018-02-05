package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/room")
public class ApiRoomController {
    @Autowired
    private GameRoomRepository playRoomRes;

    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<ResultData> register(@Valid String code,@Valid String token) {
        Token userToken = null ;
        List<GameRoom> roominfo=null;
        if(!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token) ;
            if(userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
                //返回token， 并返回游客数据给游客
                roominfo=playRoomRes.findByCode(code);
            }else{
                if(userToken!=null){
                    tokenESRes.delete(userToken);
                    userToken = null ;
                }
            }
        }
        ResultData resu=new ResultData(roominfo.size() != 0, roominfo.size() != 0?"200":"201", roominfo.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_GAMEROOM, roominfo);
        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
