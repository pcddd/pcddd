package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.HttpUtils;
import com.beimi.util.MessageEnum;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.GameRoomRepository;
import com.beimi.web.service.repository.jpa.HxConfigRepository;
import com.beimi.web.service.repository.jpa.PcRoomRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


/**
 * Created by fanling on 2018/2/7.
 */
@RestController
@RequestMapping("/api/joinAndLeaveRoom")
public class ApiJoinAndLeaveRoomController {
    @Autowired
    private TokenESRepository tokenESRes ;

    @Autowired
    private PlayUserRepository playUserRes ;

    @Autowired
    private PcRoomRepository playRoomRes;

    @Autowired
    private HxConfigRepository hxConfigRes;

    @RequestMapping
    public ResponseEntity<PcData> joinAndLeaveRoom(HttpServletRequest request, @Valid String roomid, boolean inOrOut){
        PcData resu=null;
        String message="";
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PcRoomInfo pcRoomInfo = playRoomRes.findByRoomid(roomid);
        if (pcRoomInfo == null)
            return new ResponseEntity<>(new PcData("200","房间不存在",null ), HttpStatus.OK);
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser == null)
            return new ResponseEntity<>(new PcData("200","没有找到用户",null ), HttpStatus.OK);
        if(inOrOut){
            //加入
            HttpUtils.getInstance().postJoinRoomMes(roomid,getHxToken(),playUser.getUsername());
            CacheHelper.getRoomMappingCacheBean().put(userToken.getUserid(), roomid, "beimi");//玩家加入房间
            message="玩家已加入房间";
        }else{
            if (CacheHelper.getRoomMappingCacheBean().getCacheObject(userToken.getUserid(),"beimi")!=null){
                CacheHelper.getRoomMappingCacheBean().delete(userToken.getUserid(),"beimi");//玩家离开房间
                message="玩家已离开房间";
            }
        }
        resu=new PcData("200",message,null );

        return new ResponseEntity<>(resu, HttpStatus.OK);
    }

    private String getHxToken(){
        String hxtoken = (String)CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.HX_TOKEN,BMDataContext.SYSTEM_ORGI);
        if (StringUtils.isBlank(hxtoken)){
            List<HxConfig> hxConfigList = hxConfigRes.findAll();
            if (hxConfigList.size() > 0)
                hxtoken = hxConfigList.get(0).getToken();
        }
        return hxtoken;
    }
}
