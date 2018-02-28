package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.BetGameDetail;
import com.beimi.web.model.PcBetEntity;
import com.beimi.web.model.PcData;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/betRecord")
public class ApiBetRecordController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private BetGameDetailESRepository betGameDetailESRes;

    @RequestMapping
    public ResponseEntity<PcData> betRecord(@Valid int type,@Valid String token,@Valid int page,@Valid int pagesize) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)) {
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                List<BetGameDetail> betGameDetailList = betGameDetailESRes.findByTypeAndTokenId(type,token,
                        new PageRequest(page - 1, pagesize, new Sort(Sort.Direction.DESC, "periods")));
                if (betGameDetailList == null)
                    betGameDetailList = new ArrayList<>();

                List<PcBetEntity> pcBetEntityList = new ArrayList<>();
                for (int i=0;i<betGameDetailList.size();i++){
                    pcBetEntityList.addAll(betGameDetailList.get(i).getPcBetEntityList());
                }

                HashMap hashMap = new HashMap();
                hashMap.put("data",pcBetEntityList);
                pcData = new PcData("200", "成功", hashMap);
            }else{
                pcData = new PcData("201", "token失效,请重新登陆", null);
            }
        }else{
            pcData = new PcData("203", "token为空",null);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
