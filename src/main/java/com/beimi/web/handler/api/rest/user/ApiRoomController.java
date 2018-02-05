package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.GameRoom;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.ResultData;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by fanling on 2018/2/4.
 */
@RestController
@RequestMapping("/api/room")
public class ApiRoomController {
    @Autowired
    private GameRoomRepository playRoomRes;

    @RequestMapping
    public ResponseEntity<ResultData> register(@Valid String code) {
        List<GameRoom> roominfo=playRoomRes.findByCode(code);
        ResultData resu=new ResultData(roominfo.size() != 0, roominfo.size() != 0?"200":"201", roominfo.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_GAMEROOM, roominfo);
        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
