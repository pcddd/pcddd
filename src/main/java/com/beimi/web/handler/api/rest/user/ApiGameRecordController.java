package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import javafx.scene.control.Pagination;
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
@RequestMapping("/api/gameRecord")
public class ApiGameRecordController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private BetGameDetailESRepository betGameDetailESRes;

    @Autowired
    private PlayUserRepository playUserRes;

    @RequestMapping
    public ResponseEntity<PcData> gameRecord(HttpServletRequest request,@Valid long daytime, @Valid int page, @Valid int pagesize) {
        PcData pcData = null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser == null){
            pcData = new PcData("201", "找不到用户", null);
        }else{
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            calendar.setTimeInMillis(daytime);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            List<BetGameDetail> betGameDetailList = betGameDetailESRes.findByUserIdAndCreatetime(playUser.getId(),calendar.getTimeInMillis(),
                    new PageRequest(page-1,pagesize,new Sort(Sort.Direction.DESC, "createtime")));

            List<PcBetEntity> pcBetEntityList = new ArrayList<>();
            for (int i=0;i<betGameDetailList.size();i++){
                pcBetEntityList.addAll(betGameDetailList.get(i).getPcBetEntityList());
            }
            Collections.sort(pcBetEntityList,new Comparator<PcBetEntity>() {
                @Override
                public int compare(PcBetEntity o1, PcBetEntity o2) {
                    if (o1.getCreateTime().getTime() < o2.getCreateTime().getTime())
                        return 1;
                    return -1;
                }
            });

            HashMap hashMap = new HashMap();
            hashMap.put("data",pcBetEntityList);
            pcData = new PcData("200", "成功", hashMap);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
