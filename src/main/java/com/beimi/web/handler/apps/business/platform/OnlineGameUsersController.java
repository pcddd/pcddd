package com.beimi.web.handler.apps.business.platform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.beimi.web.model.BetGameDetail;
import com.beimi.web.model.PcBetEntity;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

	@RequestMapping({"/gameusers"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView online(ModelMap map , HttpServletRequest request , @Valid String id){
		map.addAttribute("playersList", betGameDetailESRes.findByOrgiAndType("beimi",1,new PageRequest(super.getP(request), super.getPs(request)))) ;
		map.addAttribute("type",1);
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}

	@RequestMapping({"/gameusers/type"})
	@Menu(type="platform", subtype="onlinegame")
	public ModelAndView type(ModelMap map , HttpServletRequest request , @Valid String id){
		map.addAttribute("playersList", betGameDetailESRes.findByOrgiAndType("beimi",2,new PageRequest(super.getP(request), super.getPs(request)))) ;
		map.addAttribute("type",2);
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}

	@RequestMapping({"/gameusers/search"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView search(ModelMap map , HttpServletRequest request , @Valid String username,@Valid String periods,@Valid String type){
		int per=0;
		if(!"".equals(periods)&&!"".equals(username)){
			per=Integer.parseInt(periods);
			map.addAttribute("playersList", betGameDetailESRes.findByUsernameAndPeriodsAndType(username,per,Integer.parseInt(type),new PageRequest(super.getP(request), super.getPs(request)))) ;
		}else if (!"".equals(periods)){
			per=Integer.parseInt(periods);
			map.addAttribute("playersList", betGameDetailESRes.findByPeriodsAndType(per,Integer.parseInt(type),new PageRequest(super.getP(request), super.getPs(request)))) ;
		}else if (!"".equals(username)){
			map.addAttribute("playersList", betGameDetailESRes.findByUsernameAndType(username,Integer.parseInt(type),new PageRequest(super.getP(request), super.getPs(request)))) ;
		}

		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}
	
	@RequestMapping({"/gameusers/indexs"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView indexs(ModelMap map , HttpServletRequest request , @Valid String username){
	    BetGameDetail betGameDetail=betGameDetailESRes.findByUsername(username);
		ArrayList<PcBetEntity> pcBetEntityList= betGameDetail.getPcBetEntityList();
		map.addAttribute("pcBetEntityList",pcBetEntityList);
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/indexs"));
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
