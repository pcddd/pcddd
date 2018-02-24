package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
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
    public ResponseEntity<PcData> roomlist(@Valid String roomtype,@Valid String token,@Valid String orgi) {
        PcData resu=null;
        if(!StringUtils.isBlank(token)){
            Token userToken = userToken = tokenESRes.findById(token);
            if(userToken != null){
                if (!StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){
                    List<GameRoom> roominfo = playRoomRes.findByRoomtypeAndOrgi(roomtype,orgi);
                    resu=new PcData( roominfo.size() != 0?"200":"201", roominfo.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_GAMEROOM,
                            new ListContainer(roominfo));
                    return new ResponseEntity<>(resu,HttpStatus.OK);
                }else{
                    tokenESRes.delete(userToken);
                }
            }
        }
        return new ResponseEntity<>( new PcData("201",MessageEnum.USER_TOKEN, resu),HttpStatus.OK);
    }
}
