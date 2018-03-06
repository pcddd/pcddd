package com.beimi.web.handler.api.rest.user;

import com.beimi.util.MessageEnum;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PersonalMesRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import com.beimi.web.service.repository.jpa.SystemMesRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/getSystemMes")
public class ApiGetSystemMessageController {


    @Autowired
    SystemMesRepository systemMesRepository;
    @Autowired
    private TokenESRepository tokenESRes;

    @RequestMapping
    public ResponseEntity<PcData> getSystemMes(@Valid String token) {
        PcData resu=null;
        if(!StringUtils.isBlank(token)){
            Token userToken = tokenESRes.findById(token);
            if(userToken != null){
                if (!StringUtils.isBlank(userToken.getUserid()) && userToken.getExptime()!=null && userToken.getExptime().after(new Date())){

                    List<SysMesModel> sysMesModels = systemMesRepository.findAll();

                    HashMap hashMap = new HashMap();
                    hashMap.put("data",sysMesModels);
                    resu=new PcData( sysMesModels.size() != 0?"200":"201", sysMesModels.size() != 0 ? "成功": "暂无消息",
                            hashMap);
                    return new ResponseEntity<>(resu, HttpStatus.OK);
                }else{
                    tokenESRes.delete(userToken);
                }
            }
        }
        return new ResponseEntity<>( new PcData("201",MessageEnum.USER_TOKEN, resu),HttpStatus.OK);
    }
}
