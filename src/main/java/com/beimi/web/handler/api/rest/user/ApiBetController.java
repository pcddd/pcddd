package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.BetTypeGroupRepository;
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
    private BetTypeGroupRepository betTypeGroupRepository;

    @RequestMapping
    public ResponseEntity<PcData> doBet(@Valid int type,@Valid int diamonds,@Valid String lotterTypeId,@Valid int periods, @Valid String token) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)){
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                PlayUser  playUser = playUserRes.findByToken(userToken.getId());
                if (playUser != null) {
                    Lottery curLottery;
                    if (type == 1)
                        curLottery = (Lottery)CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_BJ_LOTTERY,BMDataContext.SYSTEM_ORGI);
                    else
                        curLottery = (Lottery)CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_JND_LOTTERY,BMDataContext.SYSTEM_ORGI);

                    if (curLottery == null || periods != curLottery.getCurNo()+1){
                        return new ResponseEntity<>(new PcData("201","当期期数不可用",null), HttpStatus.OK);
                    }

                    BetGameDetail betGameDetail =new BetGameDetail();
                    betGameDetail.setPlayUser(playUser);
                    betGameDetail.setTokenId(userToken.getId());//玩家id
                    betGameDetail.setType(type);
                    betGameDetail.setOrgi("beimi");
                    betGameDetail.setDiamonds(diamonds);//下注金额
                    betGameDetail.setPeriods(periods);//期数
                    betGameDetail.setLotterTypeId(lotterTypeId);//投注类型
                    GameBetType gameBetType = betTypeGroupRepository.findById(betGameDetail.getLotterTypeId());
                    betGameDetail.setLotterName(gameBetType.getName());//投注类型名称
                    if (playUser.getDiamonds()>= betGameDetail.getDiamonds()) {
                        betGameDetailESRes.save(betGameDetail);
                        pcData = new PcData("200","下注成功",null);
                    }else {
                        pcData=new PcData("204","余额不足",null);
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

}
