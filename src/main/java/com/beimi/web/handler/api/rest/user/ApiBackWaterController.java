package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.BackWaterRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/backWater")
public class ApiBackWaterController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private BackWaterRepository backWaterRepository;

    @Autowired
    private PlayUserRepository playUserRes;

    @RequestMapping
    public ResponseEntity<PcData> backWater(HttpServletRequest request,@Valid int roomtype, @Valid int page, @Valid int pagesize) {
        PcData pcData = null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser == null){
            pcData = new PcData("201", "找不到用户", null);
        }else{
            List<BackWaterMondel> backWaterMondelList = backWaterRepository.findByUseridAndRoomtypeAndWatergoldGreaterThan(playUser.getId(),roomtype,0,
                    new PageRequest(page-1,pagesize,new Sort(Sort.Direction.DESC,"createtime")));
            HashMap hashMap = new HashMap();
            hashMap.put("data",backWaterMondelList);
            pcData = new PcData("200", "成功", hashMap);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
