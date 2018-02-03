package com.beimi.web.handler.api.rest.user;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.beimi.core.BMDataContext;
import com.beimi.util.*;
import com.beimi.web.model.*;
import com.beimi.web.service.repository.es.TokenESRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beimi.web.handler.Handler;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;

/**
 * Created by fanling on 2018/2/2.
 */
@RestController
@RequestMapping("/api/caiRegister")
public class ApiController extends Handler{
	   @Autowired
	    private PlayUserESRepository playUserESRes;

	    @Autowired
	    private PlayUserRepository playUserRes ;

	   @Autowired
	   private TokenESRepository tokenESRes ;

	    @RequestMapping
	    public ResponseEntity<ResultData> register(HttpServletRequest request , @Valid PlayUser player) {
			String ip = UKTools.getIpAddr(request);
	        player = register(player,ip) ;
			ResultData resu=new ResultData(player != null, player!=null?"200":"201", player!=null ? MessageEnum.USER_REGISTER_SUCCESS : MessageEnum.USER_REGISTER_FAILD_USERNAME, player);
			return new ResponseEntity<>(resu, HttpStatus.OK);
	    }
	    /**
	     * 注册用户
	     * @param player
	     * @return
	     */
	    public PlayUser register(PlayUser player, String ip){
	        if(player!= null && !StringUtils.isBlank(player.getPassword())){
	            if(StringUtils.isBlank(player.getUsername())){
	                player.setUsername("Guest_"+ Base62.encode(UKTools.getUUID().toLowerCase()));
	            }
	            if(!StringUtils.isBlank(player.getPassword())){
	                player.setPassword(UKTools.md5(player.getPassword()));
	            }else{
	                player.setPassword(UKTools.md5(RandomKey.genRandomNum(6)));//随机生成一个6位数的密码 ，备用
	            }
	            player.setCreatetime(new Date());
	            player.setUpdatetime(new Date());
	            player.setLastlogintime(new Date());
				Token userToken=null;
				userToken=tokenESRes.findById(player.getId());
				if (userToken==null) {
					userToken = new Token();
					userToken.setIp(ip);
					userToken.setId(UKTools.getUUID());
					userToken.setUserid(player.getId());
					userToken.setCreatetime(new Date());
					userToken.setOrgi(player.getOrgi());
					AccountConfig config = CacheConfigTools.getGameAccountConfig(BMDataContext.SYSTEM_ORGI);
					if (config != null && config.getExpdays() > 0) {
						userToken.setExptime(new Date(System.currentTimeMillis() + 60 * 60 * 24 * config.getExpdays() * 1000));//默认有效期 ， 7天
					} else {
						userToken.setExptime(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 7 * 1000));//默认有效期 ， 7天
					}
					userToken.setLastlogintime(new Date());
					userToken.setUpdatetime(new Date(0));

					tokenESRes.save(userToken);
				}
				int users = playUserESRes.countByUsername(player.getUsername()) ;
	            if(users == 0){
					UKTools.published(player , playUserESRes , playUserRes);
	            }else{
	                player = null ;
	            }
	        }
	        return player ;
	    }
}
