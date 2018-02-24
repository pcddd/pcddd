package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.PcData;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.ResultData;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
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

    @Autowired
    private PlayUserRepository playUserRes ;

    @RequestMapping
    public ResponseEntity<PcData> joinAndLeaveRoom(@Valid String roomid, boolean inOrOut, @Valid String token){
        Token userToken = null ;
        PcData resu=null;
        String message="";
        if(!StringUtils.isBlank(token)&&StringUtils.isNotEmpty(roomid)) {
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                if(inOrOut){
                    //加入
                    CacheHelper.getRoomMappingCacheBean().put(userToken.getUserid(), roomid, "beimi");//玩家加入房间
                    message="玩家已加入房间";
                }else{
                    if (CacheHelper.getRoomMappingCacheBean().getCacheObject(userToken.getUserid(),"beimi")!=null){
                        CacheHelper.getRoomMappingCacheBean().delete(userToken.getUserid(),"beimi");//玩家离开房间
                        message="玩家已离开房间";
                    }
                }
                resu=new PcData("200",message,null );
            } else {
                resu = new PcData("201",MessageEnum.USER_TOKEN, null);
            }
        }else{
            resu=new PcData("203","无效参数",null);
        }

        return new ResponseEntity<>(resu, HttpStatus.OK);
    }
}
