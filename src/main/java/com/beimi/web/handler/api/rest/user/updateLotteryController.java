package com.beimi.web.handler.api.rest.user;


import com.beimi.core.BMDataContext;
import com.beimi.util.HttpUtils;
import com.beimi.util.UKTools;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.*;
import com.beimi.web.service.repository.jpa.HxConfigRepository;
import com.beimi.web.service.repository.jpa.PcddPeriodsRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/updateLottery")
public class updateLotteryController {

    @Autowired
    private BetGameDetailESRepository betGameDetailESRepository;

    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private PlayUserRepository playUserRes ;

//    @Autowired
//    private BetTypeGroupRepository betTypeGroupRepository;

    @Autowired
    private PcddPeriodsESRepository pcddPeriodsESRepository;

    @Autowired
    private PcddPeriodsRepository pcddPeriodsRepository;

    @Autowired
    private HxConfigRepository hxConfigRes;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PcData> updateLottery(@Valid int type,@Valid int curNo,@Valid String curRes,@Valid String nextDrawTime){
        PcddPeriods pcddPeriods = getCurLottery(type,curNo);
        PcddPeriods newPcddPeriods;
        int intervalSec,status;
        String BM_TAG;
        if (type == 1){
            intervalSec = 300;
            BM_TAG = BMDataContext.BET_TYPE_BJ_LOTTERY;
        }else{
            intervalSec = 210;
            BM_TAG = BMDataContext.BET_TYPE_JND_LOTTERY;
        }
        if (pcddPeriods == null || curNo >= pcddPeriods.getPeriods()) {
            status = getStatus(intervalSec, nextDrawTime);
        }else{
            return new ResponseEntity<>(new PcData("更新失败","200",null), HttpStatus.OK);
        }
        if (pcddPeriods == null){
            //1正常 2封盘 3停售
            newPcddPeriods = new PcddPeriods(type,curNo,status,curRes,nextDrawTime);
            System.out.println(newPcddPeriods.toString());
            CacheHelper.getSystemCacheBean().put(BM_TAG,newPcddPeriods,BMDataContext.SYSTEM_ORGI);
            UKTools.published(pcddPeriods,pcddPeriodsESRepository,pcddPeriodsRepository);
            return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
        }else{
            if (curNo > pcddPeriods.getPeriods()){
                newPcddPeriods = new PcddPeriods(type,curNo,status,curRes,nextDrawTime);
                System.out.println(newPcddPeriods.toString());
                try {
                    doBetSetttleAccounts(type,curRes,curNo,newPcddPeriods);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                CacheHelper.getSystemCacheBean().put(BM_TAG,newPcddPeriods,BMDataContext.SYSTEM_ORGI);
                UKTools.published(newPcddPeriods,pcddPeriodsESRepository,pcddPeriodsRepository);
                //通知客户端开奖结果
                HttpUtils.getInstance().postOpenLotteryMes(type,getHxToken(), curNo,curRes,new Date().getTime());
                return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
            }else if (pcddPeriods.getStatus() != status){
                pcddPeriods.setStatus(status);
                //通知客户端
                postMes(type,status,curNo);
                CacheHelper.getSystemCacheBean().put(BM_TAG,pcddPeriods,BMDataContext.SYSTEM_ORGI);
                System.out.println(pcddPeriods.toString());
                UKTools.published(pcddPeriods,pcddPeriodsESRepository,pcddPeriodsRepository);
                return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new PcData("201","更新失败",null), HttpStatus.OK);
    }

    private PcddPeriods getCurLottery(int type,int periods){
        PcddPeriods pcddPeriods;
        if (type == 1){
            pcddPeriods = (PcddPeriods) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_BJ_LOTTERY, BMDataContext.SYSTEM_ORGI);
        }else{
            pcddPeriods = (PcddPeriods) CacheHelper.getSystemCacheBean().getCacheObject(BMDataContext.BET_TYPE_JND_LOTTERY, BMDataContext.SYSTEM_ORGI);
        }
        if (pcddPeriods == null){
            List<PcddPeriods> pcddPeriodsList = pcddPeriodsESRepository.findByTypeAndPeriods(type,periods);
            if (pcddPeriodsList.size() > 0)
                pcddPeriods = pcddPeriodsList.get(0);
        }

        return pcddPeriods;
    }

    private void doBetSetttleAccounts(int type,String resno,int periods,PcddPeriods pcddPeriods) throws Exception{
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
            if (sum <= 13){
                list.add("小");

                if (sum % 2 != 0) {
                    list.add("单");
                } else {
                    list.add("双");
                }

                if (sum <= 4) {
                    list.add("极小");
                }

            }else{
                list.add("大");

                if (sum % 2 != 0) {
                    list.add("单");
                } else {
                    list.add("双");
                }

                if (sum >= 23) {
                    list.add("极大");
                }
            }
//            list.add(list.get(0) + list.get(1));

            if (sum % 3 == 0) {
                list.add("红");
                pcddPeriods.setColorid(1);
            } else if (green.contains(String.valueOf(sum))) {
                list.add("绿");
                pcddPeriods.setColorid(2);
            } else if (blue.contains(String.valueOf(sum))){
                list.add("蓝");
                pcddPeriods.setColorid(3);
            }else{
                pcddPeriods.setColorid(4);
            }

            if (nums[0].equals(nums[1]) && nums[1].equals(nums[2])) {
                list.add("豹子");
            }

            list.add(sum.toString());

            Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
            Matcher m = p.matcher(list.toString());
            if (m.find()){
                pcddPeriods.setResname(m.group().substring(1, m.group().length()-1));
            }

            List<BetGameDetail> betGameDetailList =betGameDetailESRepository.findByTypeAndPeriods(type,periods);
            if (betGameDetailList.size()!=0){
                PlayUser playUser=null;
                for (BetGameDetail betGameDetail : betGameDetailList){
                    if (betGameDetail.getStatus() == 1)
                        continue;
                    playUser = playUserRes.findById(betGameDetail.getUserId());
                    if (playUser!=null) {
                        List<PcBetEntity> pcBetEntityList = betGameDetail.getPcBetEntityList();
                        int totalgold = 0;
                        for (int i=0;i<pcBetEntityList.size();i++){
                            int gold = 0;
                            PcBetEntity pcBetEntity = pcBetEntityList.get(i);
                            GameBetType gameBetType = (GameBetType)CacheHelper.getSystemCacheBean().getCacheObject(pcBetEntity.getBetLotterTypeId(),BMDataContext.SYSTEM_ORGI);
//                            GameBetType gameBetType = betTypeGroupRepository.findById(pcBetEntity.getLotterTypeId());
                            if (list.contains(gameBetType.getName())) {
                                gold = pcBetEntity.getGoldcoins() * Integer.parseInt(gameBetType.getValue());
                                totalgold += gold;
                            } else {
                                gold = -1 * pcBetEntity.getGoldcoins();
                                totalgold += gold;
                            }
                            pcBetEntity.setGetGold(gold);
                            pcBetEntity.setLotterName(list.toString());
                            pcBetEntity.setIsWin(gold > 0 ? 1 : 0);
                            pcBetEntity.setRealResult(resno);
                        }
                        if (totalgold > 0){
                            playUser.setGoldcoins(playUser.getGoldcoins() + totalgold);
                            UKTools.published(playUser , playUserESRes , playUserRes , BMDataContext.UserDataEventType.SAVE.toString());
                        }
                        betGameDetail.setStatus(1);
                        betGameDetailESRepository.save(betGameDetail);
                    }
                }
//                if (playUser!=null) {
//                    gameDetailESRes.deleteAll();
//                }
            }

        }
    }
    /**
     * 状态
     */
    private int getStatus(int intervalSec,String nextDrawTime){
        long nexttime = Integer.parseInt(nextDrawTime) - new Date().getTime()/1000;
//        int intervalSec = type == 1 ? 300 : 200;
        int status;
        if (nexttime < 0){
            if(nexttime < -intervalSec){
                //停盘
                status = 3;
            }else{
                //客户端显示?,?,? 同时进行下一期下注
                status = 4;
            }
        }else if (nexttime > 20){
            //正常下注
            status = 1;
        }else{
            //提前20s封盘
            status = 2;
        }

        return status;
    }

    /**
     * 通知客户端
     */
    private void postMes(int type,int status,int periods){
       switch (status){
           case 1:  //正常下注
               HttpUtils.getInstance().postOpenBetMes(type,getHxToken(),String.valueOf(periods+1),"祝您好运连连，多多赢利");
               break;
           case 2:  //提前20s封盘
               HttpUtils.getInstance().postCloseBetMes(type,getHxToken(),String.valueOf(periods+1));
               break;
           case 3:  //停盘
               HttpUtils.getInstance().postCloseBetMes(type,getHxToken(),String.valueOf(periods+1));
               break;
           case 4:  //客户端显示?,?,? 同时进行下一期下注
               HttpUtils.getInstance().postOpenBetMes(type,getHxToken(),String.valueOf(periods+2),"祝您好运连连，多多赢利");
               break;
       }
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
