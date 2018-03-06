package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.MessageEnum;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.BetLevelRepository;
import com.beimi.web.service.repository.jpa.GameBetFatherTypeRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fanling on 2018/2/4.
 */
@RestController
@RequestMapping("/api/bettype")
public class ApiBetTypeController {

    @Autowired
    private BetLevelRepository betLevelRepository;

    @Autowired
    private GameBetFatherTypeRepository gameBetFatherTypeRepository;

    @Autowired
    private TokenESRepository tokenESRes ;

    @RequestMapping
    public ResponseEntity<PcData> bettype(@Valid String token,@Valid String orgi,@Valid String roomtype) {
        PcData resu=null;
        if(!StringUtils.isBlank(token)){
            Token userToken = tokenESRes.findById(token);
            if(userToken != null){
                if (!StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){

                    List<GameBetFatherType> gameBetFatherTypeList = gameBetFatherTypeRepository.findAll();
                    List res = new ArrayList();
                    for (int i=0;i<gameBetFatherTypeList.size();i++){
                        GameBetFatherType gameBetFatherType = gameBetFatherTypeList.get(i);
                        if (gameBetFatherType!=null || gameBetFatherType.getId()!=null){
                            List<BetLevelTypeInfo> betLevelTypeInfoList = betLevelRepository.findByTypeAndOrgiAndRoomtype(gameBetFatherType.getId(),orgi,roomtype);
                            List<BetLevelTypeInfoClient>  betLevelTypeInfoClientList = new ArrayList<>();
                            for (int j=0; j<betLevelTypeInfoList.size();j++){
                                BetLevelTypeInfo betLevelTypeInfo = betLevelTypeInfoList.get(j);
                                GameBetType gameBetType = (GameBetType)CacheHelper.getPcGameLevelBetTypeCacheBean().getCacheObject(betLevelTypeInfo.getBettypeid(), BMDataContext.SYSTEM_ORGI);
                                BetLevelTypeInfoClient betLevelTypeInfoClient = new BetLevelTypeInfoClient(betLevelTypeInfo,gameBetType.getName(),gameBetType.getRescode());
                                betLevelTypeInfoClientList.add(betLevelTypeInfoClient);
                            }
                            res.add(betLevelTypeInfoClientList);
                        }
                    }

                    HashMap hashMap = new HashMap();
                    hashMap.put("bets",res);
                    resu=new PcData( res.size() != 0?"200":"201", res.size() != 0 ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_FAILD_GAMEROOM,
                            hashMap);
                    return new ResponseEntity<>(resu,HttpStatus.OK);
                }else{
                    tokenESRes.delete(userToken);
                }
            }
        }
        return new ResponseEntity<>( new PcData("201",MessageEnum.USER_TOKEN, resu),HttpStatus.OK);
    }
}
