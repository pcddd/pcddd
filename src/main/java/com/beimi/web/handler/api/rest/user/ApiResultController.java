package com.beimi.web.handler.api.rest.user;

import com.beimi.util.Tools;
import com.beimi.web.model.Lottery;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.ResultData;
import com.beimi.web.model.TypeGroup;
import com.beimi.web.service.repository.es.LotteryResESRepository;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.GamePlaywayRepository;
import com.beimi.web.service.repository.jpa.TypeGroupRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Created by fanling on 2018/2/7.
 */
@RestController
@RequestMapping("/api/result")
public  class ApiResultController {
    @Autowired
    private PlayUserESRepository playUserESRes;

    @Autowired
    private TypeGroupRepository typeGroupRes;
    /**
     *
     * @param prenum  中奖号码
     * @param periods 期数
     * @return
     */
    @RequestMapping
    public List<String> result(@Valid String prenum,@Valid String periods) {
            String green = "1,4,7,10,16,19,22,25";
            String blue = "2,5,8,11,17,20,23,26";
            List<String> list = new ArrayList<String>();
        if (StringUtils.isNotEmpty(prenum)&&StringUtils.isNotEmpty(periods)) {
            char[] num = prenum.toCharArray();
            Integer sum = sum(prenum);
            if (sum <= 4) {
                list.add("极小");
            } else if (sum <= 13) {
                list.add("小");
            } else if (sum >= 14) {
                list.add("大");
            } else if (sum >= 23) {
                list.add("极大");
            }
            if (sum % 2 != 0) {
                list.add("单");
            } else {
                list.add("双");
            }
            list.add(list.get(0) + list.get(1));
            if (sum % 3 == 0) {
                list.add("红");
            } else if (green.contains(sum.toString())) {
                list.add("绿");
            } else if (blue.contains(sum.toString())) {
                list.add("蓝");
            }
            if (num[0] == num[1] && num[1] == num[2]) {
                list.add("豹子");
            }
            list.add(sum.toString());
            List<PlayUser> list1=playUserESRes.findByPeriods(periods);
            if (list1.size()!=0){
                for (PlayUser playUser :list1) {
                    if (list.contains(playUser.getLotterType())){
                        TypeGroup typeGroup=typeGroupRes.findByName(playUser.getLotterType());
                        playUser.setGoldcoins(playUser.getDiamonds()*Integer.parseInt(typeGroup.getValue())+playUser.getGoldcoins());
                    }else {
                        playUser.setGoldcoins(playUser.getGoldcoins()-playUser.getDiamonds());
                    }
                    playUser.setDiamonds(0);
                    playUser.setPeriods("");
                    playUser.setLotterType("");
                    playUserESRes.save(playUser);
                }

            }

        }
        return list;
    }
    public static int sum(String value) {
        String strings[] = value.split("\\D+");
        int sum = 0;
        for (String s : strings) {
            if (!s.equals("")) {
                sum += Integer.parseInt(s);
            }
        }
        return sum;
    }
}
