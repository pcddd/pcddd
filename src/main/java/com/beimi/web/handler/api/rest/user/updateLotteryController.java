package com.beimi.web.handler.api.rest.user;


import com.beimi.core.BMDataContext;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.BjLotteryResESRepository;
import com.beimi.web.service.repository.es.JNDLotteryResESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import com.beimi.web.service.repository.jpa.BetTypeGroupRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/updateLottery")
public class updateLotteryController {

    @Autowired
    private JNDLotteryResESRepository jndLotteryResESRepository;

    @Autowired
    private BjLotteryResESRepository bjLotteryResESRepository;

    @Autowired
    private BetGameDetailESRepository betGameDetailESRepository;

    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private PlayUserRepository playUserRes ;

    @Autowired
    private BetTypeGroupRepository betTypeGroupRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PcData> updateLottery(@Valid int type,@Valid int curNo,@Valid String curRes,@Valid String nextDrawTime){
        if (type == 1){
            //北京
            Lottery lottery = getCurLottery(type);
            Lottery newLottery = null;
            if (lottery == null){
                //1正常 2封盘 3停售
                newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
                System.out.println(newLottery.toString());
                CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_BJ_LOTTERY,newLottery,BMDataContext.SYSTEM_ORGI);
                bjLotteryResESRepository.save(newLottery);
                return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
            }else{
                if (curNo > lottery.getCurNo()){
                    //1正常 2封盘 3停售
                    newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
                    System.out.println(newLottery.toString());
                    try {
                        doBetSetttleAccounts(1,curRes,curNo);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_BJ_LOTTERY,newLottery,BMDataContext.SYSTEM_ORGI);
                    bjLotteryResESRepository.deleteAll();
                    bjLotteryResESRepository.save(newLottery);
                    return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
                }
            }
//            Iterator<Lottery> iterator = bjLotteryResESRepository.findAll().iterator();
//            if (iterator.hasNext()){
//                Lottery lottery = bjLotteryResESRepository.findAll().iterator().next();
//                int no = lottery.getCurNo();
//                if (curNo > no){
//                    //1正常 2封盘 3停售
//                    newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
//                    System.out.println(newLottery.toString());
//                    bjLotteryResESRepository.deleteAll();
//                    bjLotteryResESRepository.save(newLottery);
//                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_BJ_PERIOD,curNo,BMDataContext.SYSTEM_ORGI);
//                    try {
//                        doBetSetttleAccounts(1,lottery.getCurRes(),lottery.getCurNo());
//                    } catch (Exception e){
//                        System.out.println(e.getMessage());
//                    }
//                    return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
//                }
//            }else{
//                CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_BJ_PERIOD,curNo,BMDataContext.SYSTEM_ORGI);
//                newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
//                bjLotteryResESRepository.save(newLottery);
//                System.out.println(newLottery.toString());
//                return new ResponseEntity<>(new PcData("200","更新成功",null), HttpStatus.OK);
//            }
        }else{
            //加拿大
            Lottery lottery = getCurLottery(type);
            Lottery newLottery = null;
            if (lottery == null){
                //1正常 2封盘 3停售
                newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
                System.out.println(newLottery.toString());
                CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_JND_LOTTERY,newLottery,BMDataContext.SYSTEM_ORGI);
                jndLotteryResESRepository.save(newLottery);
                return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
            }else{
                if (curNo > lottery.getCurNo()){
                    //1正常 2封盘 3停售
                    newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
                    System.out.println(newLottery.toString());
                    try {
                        doBetSetttleAccounts(2,curRes,curNo);
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_JND_LOTTERY,newLottery,BMDataContext.SYSTEM_ORGI);
                    jndLotteryResESRepository.deleteAll();
                    jndLotteryResESRepository.save(newLottery);
                    return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
                }
            }

//            Iterator<Lottery> iterator = jndLotteryResESRepository.findAll().iterator();
//            Lottery newLottery = null;
//            if (iterator.hasNext()){
//                Lottery lottery = jndLotteryResESRepository.findAll().iterator().next();
//                int no = lottery.getCurNo();
//                if (curNo > no){
//                    //1正常 2封盘 3停售
//                    newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
//                    System.out.println(newLottery.toString());
//                    jndLotteryResESRepository.deleteAll();
//                    jndLotteryResESRepository.save(newLottery);
//                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_JND_PERIOD,curNo,BMDataContext.SYSTEM_ORGI);
//                    try{
//                        doBetSetttleAccounts(2,lottery.getCurRes(),lottery.getCurNo());
//                    }catch (Exception e){
//                        System.out.println(e.getMessage());
//                    }
//                    return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
//                }
//            }else{
//                CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_JND_PERIOD,curNo,BMDataContext.SYSTEM_ORGI);
//                newLottery = new Lottery(curNo,curRes,0,0,nextDrawTime);
//                jndLotteryResESRepository.save(newLottery);
//                System.out.println(newLottery.toString());
//                return new ResponseEntity<>(new PcData("200","更新成功",null), HttpStatus.OK);
//            }
        }
        return new ResponseEntity<>(new PcData("201","更新失败",null), HttpStatus.OK);
    }

    private Lottery getCurLottery(int type){
        Lottery lottery = null;
        if (type == 1){
            lottery = (Lottery) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_BJ_LOTTERY, BMDataContext.SYSTEM_ORGI);
            if (lottery == null){
                Iterator<Lottery> iterator = bjLotteryResESRepository.findAll().iterator();
                if (iterator.hasNext()){
                    lottery = iterator.next();
                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_BJ_LOTTERY,lottery,BMDataContext.SYSTEM_ORGI);
                }
            }

        }else{
            lottery = (Lottery) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_JND_LOTTERY, BMDataContext.SYSTEM_ORGI);
            if (lottery == null){
                Iterator<Lottery> iterator = jndLotteryResESRepository.findAll().iterator();
                if (iterator.hasNext()){
                    lottery = iterator.next();
                    CacheHelper.getSystemCacheBean().put(BMDataContext.BET_TYPE_JND_LOTTERY,lottery,BMDataContext.SYSTEM_ORGI);
                }
            }
        }

        return lottery;
    }

    private void doBetSetttleAccounts(int type,String resno,int periods) throws Exception{
        String green = "1,4,7,10,16,19,22,25";
        String blue = "2,5,8,11,17,20,23,26";
        List<String> list = new ArrayList<String>();
        if (StringUtils.isNotEmpty(resno)) {
            String[] numStr = resno.split("=");
            if (numStr.length < 2){
                throw new Exception("doBetSetttleAccounts error: resno str err");
            }
            String[] nums = numStr[0].split(",");
            Integer sum = Integer.parseInt(numStr[1]);
            if (sum <= 4) {
                list.add("极小");
            } else if (sum <= 13) {
                list.add("小");
            } else if (sum >= 23) {
                list.add("极大");
            } else if (sum >= 14) {
                list.add("大");
            }

            if (sum % 2 != 0) {
                list.add("单");
            } else {
                list.add("双");
            }

            list.add(list.get(0) + list.get(1));

            if (sum % 3 == 0) {
                list.add("红");
            } else if (green.contains(String.valueOf(sum))) {
                list.add("绿");
            } else if (blue.contains(String.valueOf(sum))){
                list.add("蓝");
            }

            if (nums[0].equals(nums[1]) && nums[1].equals(nums[2])) {
                list.add("豹子");
            }
            list.add(sum.toString());
            List<BetGameDetail> betGameDetailList =betGameDetailESRepository.findByTypeAndPeriods(type,periods);
            if (betGameDetailList.size()!=0){
                PlayUser playUser=null;
                for (BetGameDetail betGameDetail : betGameDetailList){
                    playUser = playUserRes.findByToken(betGameDetail.getTokenId());
                    if (playUser!=null) {
                        GameBetType gameBetType = betTypeGroupRepository.findById(betGameDetail.getLotterTypeId());
                        if (list.contains(gameBetType.getName())) {
                            playUser.setDiamonds(betGameDetail.getDiamonds() * Integer.parseInt(gameBetType.getValue()) + playUser.getDiamonds());
                        } else {
                            playUser.setDiamonds(playUser.getDiamonds() - betGameDetail.getDiamonds());
                        }
//                        playUserESRes.save(playUser);
                        UKTools.published(playUser , playUserESRes , playUserRes , BMDataContext.UserDataEventType.SAVE.toString());
                    }
                }
//                if (playUser!=null) {
//                    gameDetailESRes.deleteAll();
//                }
            }

        }
    }
}
