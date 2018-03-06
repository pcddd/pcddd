package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.HttpUtils;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.BetTypeGroupRepository;
import com.beimi.web.service.repository.jpa.HxConfigRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
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
 * Created by fanling on 2018/2/8.
 */

@RestController
@RequestMapping("/api/dobet")
public class ApiBetController {

    @Autowired
    private PlayUserRepository playUserRes;

    @Autowired
    private BetGameDetailESRepository betGameDetailESRes;

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private HxConfigRepository hxConfigRes;

    @RequestMapping
    public ResponseEntity<PcData> doBet( @Valid String token,@Valid int type,@Valid String roomId,@Valid int roomlevel,@Valid int goldcoins,
                                        @Valid String lotterTypeId,@Valid int periods) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                PlayUser  playUser = playUserRes.findByToken(userToken.getId());
                if (playUser != null) {
                    PcddPeriods pcddPeriods;

                    if (type == 1)
                        pcddPeriods = (PcddPeriods)CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_BJ_LOTTERY,BMDataContext.SYSTEM_ORGI);
                    else
                        pcddPeriods = (PcddPeriods)CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_JND_LOTTERY,BMDataContext.SYSTEM_ORGI);

                    if (pcddPeriods == null || periods < pcddPeriods.getPeriods()){
                        return new ResponseEntity<>(new PcData("201","当期期数不可用",null), HttpStatus.OK);
                    }

                    if (playUser.getGoldcoins() < goldcoins){
                        return new ResponseEntity<>(new PcData("201","元宝不足，请先充值",null), HttpStatus.OK);
                    }

                    BetLevelTypeInfo betLevelTypeInfo = (BetLevelTypeInfo)CacheHelper.getBetValueCacheBean().getCacheObject(lotterTypeId,BMDataContext.SYSTEM_ORGI);
                    if (betLevelTypeInfo != null){
                        GameBetType GameBetType = (GameBetType)CacheHelper.getPcGameLevelBetTypeCacheBean().getCacheObject(betLevelTypeInfo.getBettypeid(),BMDataContext.SYSTEM_ORGI);
                        if (GameBetType!=null) {
                            BetGameDetail betGameDetail = betGameDetailESRes.findByUserIdAndPeriodsAndLevel(playUser.getId(),periods,roomlevel);
                            if (betGameDetail == null){
                                betGameDetail =new BetGameDetail();
                                betGameDetail.setUserId(playUser.getId());//玩家id
                                betGameDetail.setType(type);
                                betGameDetail.setOrgi("beimi");
                                betGameDetail.setLevel(roomlevel);
                                betGameDetail.setStatus(0);
                                PcBetEntity pcBetEntity = new PcBetEntity();
                                pcBetEntity.setGoldcoins(goldcoins);//下注金额
                                pcBetEntity.setBetLotterTypeId(lotterTypeId);//投注类型
                                pcBetEntity.setBetLotterName(GameBetType.getName());
                                pcBetEntity.setCreateTime(new Date().getTime());
                                pcBetEntity.setOrgi(type);
                                pcBetEntity.setPeriods(periods);
                                ArrayList<PcBetEntity> pcBetEntityList = new ArrayList<>();
                                pcBetEntityList.add(pcBetEntity);
                                betGameDetail.setPcBetEntityList(pcBetEntityList);
                                betGameDetail.setPeriods(periods);//期数
                            }else{
                                ArrayList<PcBetEntity> pcBetEntityList = betGameDetail.getPcBetEntityList();
                                PcBetEntity pcBetEntity = new PcBetEntity();
                                pcBetEntity.setGoldcoins(goldcoins);//下注金额
                                pcBetEntity.setBetLotterTypeId(lotterTypeId);//投注类型
                                pcBetEntity.setBetLotterName(GameBetType.getName());
                                pcBetEntity.setCreateTime(new Date().getTime());
                                pcBetEntity.setOrgi(type);
                                pcBetEntity.setPeriods(periods);
                                pcBetEntityList.add(pcBetEntity);
                                betGameDetail.setPcBetEntityList(pcBetEntityList);
                            }
                            betGameDetailESRes.save(betGameDetail);
                            HttpUtils.getInstance().postBetMes(roomId,getHxToken(), "",lotterTypeId, playUser.getUsername(), periods,
                                    GameBetType.getName(),goldcoins,1);
                            playUser.setGoldcoins(playUser.getGoldcoins() - goldcoins);
                            playUserRes.save(playUser);
                            pcData = new PcData("200", null, "下注成功");
                        }else{
                            pcData = new PcData("201", "下注类型错误", null);
                        }
                    } else{
                        pcData = new PcData("201", "下注类型错误", null);
                    }
                } else {
                    pcData = new PcData("201", "没有找到此用户",null);
                }
            }else {
                pcData = new PcData("202", "token失效,请重新登陆",null );
            }
        }
        else {
            pcData = new PcData("203", "token为空",null);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
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
