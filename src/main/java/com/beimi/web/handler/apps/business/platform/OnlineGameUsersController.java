package com.beimi.web.handler.apps.business.platform;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.beimi.web.model.BetGameDetail;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.beimi.util.Menu;
import com.beimi.web.handler.Handler;
import com.beimi.web.model.PlayUser;
import com.beimi.web.service.repository.es.PlayUserESRepository;
import com.beimi.web.service.repository.jpa.PlayUserRepository;

@Controller
@RequestMapping("/apps/platform/online")
public class OnlineGameUsersController extends Handler{
	
	@Autowired
	private PlayUserESRepository playersRes;
	
	@Autowired
	private PlayUserRepository playUserRes ;

	@Autowired
	private BetGameDetailESRepository betGameDetailESRes;
	
	/*@RequestMapping({"/gameusers"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView online(ModelMap map , HttpServletRequest request , @Valid String id){
		map.addAttribute("playersList", playersRes.findByOrgi("beimi",new PageRequest(super.getP(request), super.getPs(request)))) ;
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}*/

	@RequestMapping({"/gameusers"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView online(ModelMap map , HttpServletRequest request , @Valid String id){
		map.addAttribute("playersList", betGameDetailESRes.findByOrgi("beimi",new PageRequest(super.getP(request), super.getPs(request)))) ;
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}

	@RequestMapping({"/gameusers/search"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView search(ModelMap map , HttpServletRequest request , @Valid String username,@Valid String periods){
		int per=0;
		if(!"".equals(periods)){
			per=Integer.parseInt(periods);
		}
		PlayUser playUser=playersRes.findByUsername(username);
		if (playUser!=null){
			username=playUser.getToken();
		}else {
			username="";
		}
		map.addAttribute("playersList", betGameDetailESRes.findByTokenIdAndPeriods(username,per,new PageRequest(super.getP(request), super.getPs(request)))) ;
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}
	
	@RequestMapping({"/gameusers/edit"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView edit(ModelMap map , HttpServletRequest request , @Valid String id){
		
		map.addAttribute("playUser", playersRes.findById(id)) ;
		
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/online/edit"));
	}
	
	@RequestMapping("/gameusers/update")
    @Menu(type = "admin" , subtype = "gameusers")
    public ModelAndView update(HttpServletRequest request ,@Valid PlayUser players) {
		PlayUser playUser = playersRes.findById(players.getId()) ;
		if(playUser!=null){
			playUser.setDisabled(players.isDisabled());
			playUser.setGoldcoins(players.getGoldcoins());
			playUser.setCards(players.getCards());
			playUser.setDiamonds(players.getDiamonds());
			playUser.setUpdatetime(new Date());
			playersRes.save(playUser) ;
			playUserRes.save(playUser) ;
		}
    	return request(super.createRequestPageTempletResponse("redirect:/apps/platform/online/gameusers.html"));
    }
}
