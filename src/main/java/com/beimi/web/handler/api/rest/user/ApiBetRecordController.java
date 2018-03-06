package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/betRecord")
public class ApiBetRecordController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private BetGameDetailESRepository betGameDetailESRes;

    @Autowired
    private PlayUserRepository playUserRes;

    @RequestMapping
    public ResponseEntity<PcData> betRecord(@Valid int type,@Valid String token,@Valid int page,@Valid int pagesize) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)) {
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                PlayUser playUser = playUserRes.findByToken(token);
                if (playUser == null){
                    pcData = new PcData("201", "找不到用户", null);
                }else{
                    List<BetGameDetail> betGameDetailList = betGameDetailESRes.findByTypeAndUserId(type,playUser.getId(),
                            new PageRequest(page - 1, pagesize, new Sort(Sort.Direction.DESC, "periods")));
                    if (betGameDetailList == null)
                        betGameDetailList = new ArrayList<>();
                    List<PcBetEntity> pcBetEntityList = new ArrayList<>();
                    for (int i=0;i<betGameDetailList.size();i++){
                        pcBetEntityList.addAll(betGameDetailList.get(i).getPcBetEntityList());
                    }
                    Collections.sort(pcBetEntityList,new Comparator<PcBetEntity>() {
                        @Override
                        public int compare(PcBetEntity o1, PcBetEntity o2) {
                            if (o1.getCreateTime() < o2.getCreateTime())
                                return 1;
                            return -1;
                        }
                    });
                    HashMap hashMap = new HashMap();
                    hashMap.put("data",pcBetEntityList);
                    pcData = new PcData("200", "成功", hashMap);
                }
            }else{
                pcData = new PcData("201", "token失效,请重新登陆", null);
            }
        }else{
            pcData = new PcData("203", "token为空",null);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
