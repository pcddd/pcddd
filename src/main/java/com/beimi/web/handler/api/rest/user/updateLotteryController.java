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

@RestController
@RequestMapping("/api/updateLottery")
public class updateLotteryController {

    @Autowired
    private BetGameDetailESRepository betGameDetailESRepository;

    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private PlayUserRepository playUserRes ;

    @Autowired
    private BetTypeGroupRepository betTypeGroupRepository;

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
        if (curNo >= pcddPeriods.getPeriods()) {
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
                    doBetSetttleAccounts(type,curRes,curNo);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                CacheHelper.getSystemCacheBean().put(BM_TAG,newPcddPeriods,BMDataContext.SYSTEM_ORGI);
                UKTools.published(newPcddPeriods,pcddPeriodsESRepository,pcddPeriodsRepository);
                //通知客户端开奖结果
                HttpUtils.getInstance().postOpenLotteryMes(getHxToken(), curNo,curRes,new Date().getTime()/1000);
                return new ResponseEntity<>(new PcData("更新成功","200",null), HttpStatus.OK);
            }else if (pcddPeriods.getStatus() != status){
                pcddPeriods.setStatus(status);
                //通知客户端
                postMes(status,curNo);
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
            pcddPeriods = pcddPeriodsESRepository.findByTypeAndPeriods(type,periods);
        }

        return pcddPeriods;
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
    private void postMes(int status,int periods){
       switch (status){
           case 1:  //正常下注
               HttpUtils.getInstance().postOpenBetMes(getHxToken(),String.valueOf(periods),"");
               break;
           case 2:  //提前20s封盘
               HttpUtils.getInstance().postCloseBetMes(getHxToken(),String.valueOf(periods));
               break;
           case 3:  //停盘
               HttpUtils.getInstance().postCloseBetMes(getHxToken(),String.valueOf(periods));
               break;
           case 4:  //客户端显示?,?,? 同时进行下一期下注
               HttpUtils.getInstance().postOpenBetMes(getHxToken(),String.valueOf(periods),"");
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
