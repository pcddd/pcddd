package com.beimi.web.handler.api.rest.user;

import com.beimi.util.UKTools;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/setBankInfo")
public class ApiSetBankInfoController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private PlayUserRepository playUserRes;

    @RequestMapping
    public ResponseEntity<PcData> setBankInfo(HttpServletRequest request, @Valid String realname, @Valid String bankname,
                                              @Valid String bankno, @Valid String bankaddress, @Valid String withdrawpwd) {
        PcData pcData = null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser == null){
            pcData = new PcData("201", "找不到用户", null);
        }else{
            if (playUser.getWithdrawals_password().equals(withdrawpwd)){
                playUser.setReal_name(realname);
                playUser.setBank_name(bankname);
                playUser.setBank_no(bankno);
                playUser.setOpen_card_address(bankaddress);
                playUser.setWithdrawals_password(withdrawpwd);
                playUserRes.save(playUser);
                pcData = new PcData("200", "绑定成功", null);
            }else{
                pcData = new PcData("201", "提现密码错误", null);
            }
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
