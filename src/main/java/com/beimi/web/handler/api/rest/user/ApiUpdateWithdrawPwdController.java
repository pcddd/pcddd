package com.beimi.web.handler.api.rest.user;

import com.beimi.web.model.PcData;
import com.beimi.web.model.PlayUser;
import com.beimi.web.model.Token;
import com.beimi.web.service.repository.es.TokenESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/updateWithdrawPwd")
public class ApiUpdateWithdrawPwdController {

    @Autowired
    private TokenESRepository tokenESRes;

    @Autowired
    private PlayUserRepository playUserRes;

    @RequestMapping
    public ResponseEntity<PcData> updateWithdrawPwd(HttpServletRequest request, @Valid String oldwithdrawpwd, @Valid String newwithdrawpwd) {
        PcData pcData = null;
        Token userToken = tokenESRes.findById(request.getHeader("token"));
        PlayUser playUser = playUserRes.findByToken(userToken.getId());
        if (playUser == null){
            pcData = new PcData("201", "找不到用户", null);
        }else{
            if (StringUtils.isNotEmpty(playUser.getWithdrawals_password())){
                if (playUser.getWithdrawals_password().equals(oldwithdrawpwd)){
                    playUser.setWithdrawals_password(newwithdrawpwd);
                    playUserRes.save(playUser);
                    pcData = new PcData("200", "设置成功", null);
                }else{
                    pcData = new PcData("201", "原密码不正确", null);
                }
            }else{
                pcData = new PcData("201", "还未设置提现密码", null);
            }
        }
        return new ResponseEntity<>(pcData, HttpStatus.OK);
    }
}
