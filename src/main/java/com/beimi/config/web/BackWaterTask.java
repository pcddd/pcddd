package com.beimi.config.web;

import com.beimi.web.model.BackWaterMondel;
import com.beimi.web.model.BetGameDetail;
import com.beimi.web.model.GamePlayway;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.jpa.BackWaterRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import org.apache.http.util.TextUtils;
import org.apache.lucene.util.SparseFixedBitSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class BackWaterTask {

    @Autowired
    private BetGameDetailESRepository betGameDetailESRes;

    @Autowired
    private BackWaterRepository backWaterRepository;

    @Autowired
    private GamePlaywayRepository gamePlaywayRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void doBackWaterTask(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE,-1);

        for (int level=1;level < 4; level++){
            List<BackWaterMondel> backWaterMondelList = new ArrayList<>();
            /**
             * 找出一天内某一个级别房下所有的投注记录
             */
            List<BetGameDetail> betGameDetailList = betGameDetailESRes.findByLevelAndCreatetime(level,calendar.getTimeInMillis());
            for (BetGameDetail betGameDetail : betGameDetailList){
                /**
                 * 收集玩家一天内某个级别房下的所有回水记录
                 */
                BackWaterMondel backWaterMondel = new BackWaterMondel();
                backWaterMondel.setGametype(betGameDetail.getType());
                backWaterMondel.setRoomtype(betGameDetail.getLevel());
                backWaterMondel.setUserid(betGameDetail.getUserId());
                backWaterMondel.setTotalbetgold(betGameDetail.getTotalbetgold());
                backWaterMondel.setTotalwingold(betGameDetail.getTotalwingold());
//                int totallosegold = betGameDetail.getTotalbetgold() - betGameDetail.getTotalwingold();
//                backWaterMondel.setTotallosegold(totallosegold > 0 ? totallosegold : 0);
                backWaterMondel.setCombinationgold(betGameDetail.getCombinationGold());

                int tag = backWaterMondelList.indexOf(backWaterMondel);
                if (tag != -1){
                    //已经存在
                    BackWaterMondel rhs = backWaterMondelList.get(tag);
                    rhs.setTotalbetgold(backWaterMondel.getTotalbetgold() + rhs.getTotalbetgold());
                    rhs.setTotalwingold(backWaterMondel.getTotalwingold() + rhs.getTotalwingold());
//                    rhs.setTotallosegold( backWaterMondel.getTotallosegold() + rhs.getTotallosegold());
                    rhs.setCombinationgold(backWaterMondel.getCombinationgold() + rhs.getCombinationgold());
                }else{
                    backWaterMondelList.add(backWaterMondel);
                }
            }
            /**
             * 计算回水
             */
            for (BackWaterMondel waterMondel : backWaterMondelList){
                long totallosegold = waterMondel.getTotalbetgold() - waterMondel.getTotalwingold();
                if(totallosegold > 0){
                    waterMondel.setTotallosegold(totallosegold);
                    calculateBackWater(waterMondel);
                }else{
                    waterMondel.setTotallosegold(0);
                }
            }
            if (backWaterMondelList.size() > 0){
                IterablerList iterablerList = new IterablerList(backWaterMondelList);
                backWaterRepository.save(iterablerList);
            }
        }
    }

    private void calculateBackWater(BackWaterMondel backWaterMondel) {
        GamePlayway gamePlayway = gamePlaywayRepository.findByGametypeAndRoomtype(backWaterMondel.getGametype(),backWaterMondel.getRoomtype());
        if (gamePlayway == null)
            return;
        String section_1 = gamePlayway.getSection_1();
        String section_2 = gamePlayway.getSection_2();
        String section_3 = gamePlayway.getSection_3();
        if (!TextUtils.isEmpty(section_3)){
            if (playBackWater(backWaterMondel,section_3,gamePlayway.getComposability()))
                return;
        }

        if (!TextUtils.isEmpty(section_2)){
            if (playBackWater(backWaterMondel,section_2,gamePlayway.getComposability()))
                return;
        }

        if (!TextUtils.isEmpty(section_1)){
            playBackWater(backWaterMondel,section_1,gamePlayway.getComposability());
        }
    }

    private boolean playBackWater(BackWaterMondel backWaterMondel,String section,int composability){
        String[] waterArr = section.split("-");
        if (backWaterMondel.getTotallosegold() > Integer.parseInt(waterArr[0])){
            long cp = Math.round(1.0 * backWaterMondel.getCombinationgold()/backWaterMondel.getTotallosegold() * 100);
            if (cp >= composability){
                backWaterMondel.setWatergold(Math.round(backWaterMondel.getTotallosegold() * Integer.parseInt(waterArr[2]) * 0.01));
                backWaterMondel.setBill((int)cp);
                backWaterMondel.setStatus(0);
                return true;
            }else{
                backWaterMondel.setWatergold(Math.round(backWaterMondel.getTotallosegold() * Integer.parseInt(waterArr[2]) * 0.01));
                backWaterMondel.setBill(composability);
                backWaterMondel.setStatus(2);
            }
        }
        return false;
    }

    class IterablerList<T> implements Iterable<T>{
        private List<T> list = null;

        public IterablerList(List<T> t){
            this.list = t ;
        }
        @Override
        public Iterator<T> iterator() {
            // TODO Auto-generated method stub
            return new Iterator<T>() {
                private Integer index = 0;
                @Override
                public boolean hasNext() {
                    // TODO Auto-generated method stub
                    return index<list.size();
                }

                @Override
                public T next() {
                    // TODO Auto-generated method stub
                    return list.get(index++);
                }

                @Override
                public void remove() {
                    // TODO Auto-generated method stub
                }
            };
        }
    }


}
