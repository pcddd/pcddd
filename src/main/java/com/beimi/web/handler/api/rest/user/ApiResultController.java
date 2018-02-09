package com.beimi.web.handler.api.rest.user;

import com.beimi.util.Tools;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.GameDetailESRepository;
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

    @Autowired
    private GameDetailESRepository gameDetailESRes;
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
            List<GameDetail> list1=gameDetailESRes.findByPeriods(periods);
            if (list1.size()!=0){
                PlayUser playUser=null;
                for (GameDetail gameDetail :list1) {
                         playUser = playUserESRes.findById(gameDetail.getUserId());
                       if (playUser!=null) {
                           TypeGroup typeGroup = typeGroupRes.findById(gameDetail.getLotterType());
                           if (list.contains(typeGroup.getName())) {
                               playUser.setGoldcoins(gameDetail.getDiamonds() * Integer.parseInt(typeGroup.getValue()) + playUser.getGoldcoins());
                           } else {
                               playUser.setGoldcoins(playUser.getGoldcoins() - gameDetail.getDiamonds());
                           }
                           playUserESRes.save(playUser);
                       }
                    }
                    if (playUser!=null) {
                        gameDetailESRes.deleteAll();
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
