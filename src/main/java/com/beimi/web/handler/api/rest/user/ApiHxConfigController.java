package com.beimi.web.handler.api.rest.user;

import com.beimi.core.BMDataContext;
import com.beimi.util.cache.CacheHelper;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.HxConfigRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by fanling on 2018/2/8.
 */

@RestController
@RequestMapping("/api/updatehxconfig")
public class ApiHxConfigController {

    @Autowired
    private HxConfigRepository hxConfigRes;

    @RequestMapping
    public ResponseEntity<PcData> updatehxconfig (@Valid String hxtoken) {
        PcData pcData = null;
        if (!StringUtils.isBlank(hxtoken)) {
            List<HxConfig> hxConfigList = hxConfigRes.findAll();
            HxConfig hxConfig;
            if (hxConfigList.size() > 0){
                hxConfig = hxConfigList.get(0);
                hxConfig.setToken(hxtoken);
            }else{
                hxConfig = new HxConfig();
                hxConfig.setToken(hxtoken);
            }
            hxConfigRes.save(hxConfig);
            CacheHelper.getSystemCacheBean().put(BMDataContext.HX_TOKEN,hxtoken,BMDataContext.SYSTEM_ORGI);
            pcData = new PcData("200", "更新成功",null);
        }else {
            pcData = new PcData("203", "token为空",null);
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }

}
