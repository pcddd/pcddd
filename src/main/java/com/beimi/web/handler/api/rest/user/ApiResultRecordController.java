package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.PcddPeriodsESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/recordResult")
public class ApiResultRecordController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private PcddPeriodsESRepository pcddPeriodsESRepository;

    @RequestMapping
    public ResponseEntity<PcData> recordResult(@Valid int type,@Valid String token,@Valid int page,@Valid int pagesize) {
        PcData pcData = null;
        Token userToken = null;
        if (!StringUtils.isBlank(token)) {
            userToken = tokenESRes.findById(token);
            if (userToken != null && !StringUtils.isBlank(userToken.getId()) && userToken.getExptime() != null && userToken.getExptime().after(new Date())) {
                List<PcddPeriods> pcddPeriodsList = pcddPeriodsESRepository.findByType(type,
                        new PageRequest(page - 1, pagesize, new Sort(Sort.Direction.DESC, "periods")));
                if (pcddPeriodsList == null)
                    pcddPeriodsList = new ArrayList<>();
                HashMap hashMap = new HashMap();
                hashMap.put("data",pcddPeriodsList);
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
