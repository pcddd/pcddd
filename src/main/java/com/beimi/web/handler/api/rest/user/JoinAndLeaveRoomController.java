package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.ResultData;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.TokenESRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by fanling on 2018/2/7.
 */
@RestController
@RequestMapping("/api/joinAndLeaveRoom")
public class JoinAndLeaveRoomController {
    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<ResultData> joinAndLeaveRoom(@Valid String roomid, @Valid String token){
        Token userToken = null ;
        ResultData resu=null;
        String message="";
        if(!StringUtils.isBlank(token)&&StringUtils.isNotEmpty(roomid)) {
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                if (CacheHelper.getRoomMappingCacheBean().getCacheObject(userToken.getUserid(),"beimi")!=null){
                    CacheHelper.getRoomMappingCacheBean().delete(userToken.getUserid(),"beimi");//玩家离开房间
                    message="玩家已离开房间";
                }else {
                    CacheHelper.getRoomMappingCacheBean().put(userToken.getUserid(), roomid, "beimi");//玩家加入房间
                    message="玩家已加入房间";
                }
                    resu=new ResultData(message,"200" );
            } else {
                resu = new ResultData(MessageEnum.USER_TOKEN, "201");
            }
        }else{
            resu=new ResultData("参数为空","203" );
        }

        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
