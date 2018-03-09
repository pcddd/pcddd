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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beimi.web.handler.Handler;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;

/**
 * Created by fanling on 2018/2/2.
 */
@RestController
@RequestMapping("/api/caiRegister")
public class ApiPcRegisterController extends Handler{
	   @Autowired
	   private PlayUserESRepository playUserESRes;

	    @Autowired
	   private PlayUserRepository playUserRes ;

	   @Autowired
	   private TokenESRepository tokenESRes ;

	@SuppressWarnings("rawtypes")
	@RequestMapping(method = RequestMethod.POST)
	    public ResponseEntity<PcData> register(HttpServletRequest request , @Valid PlayUser player) {
			String ip = UKTools.getIpAddr(request);
	        player = register(player,ip) ;
			PcData resu=new PcData(player!=null?"200":"201", player!=null ? MessageEnum.USER_REGISTER_SUCCESS :"注册失败", player);
			return new ResponseEntity<>(resu, HttpStatus.OK);
	    }
	    /**
	     * 注册用户
	     * @param player
	     * @return
	     */
	    public PlayUser register(PlayUser player, String ip){
	        if(player!= null && !StringUtils.isBlank(player.getPassword()) && StringUtils.isNotEmpty(player.getUsername())){
				player.setPassword(UKTools.md5(player.getPassword()));
	            player.setCreatetime(new Date());
	            player.setUpdatetime(new Date());
				player.setGoldcoins(10000);
	            player.setLastlogintime(new Date());
				player.setNickname(player.getUsername());
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
				PlayUser users = playUserRes.findByUsername(player.getUsername()) ;
	            if(users == null){
					UKTools.published(player , playUserESRes , playUserRes);
	            }else{
	                player = null ;
	            }
				return player;
	        }
	        return null ;
	    }
}
