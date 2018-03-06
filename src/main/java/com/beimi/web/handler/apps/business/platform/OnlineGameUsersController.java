package com.beimi.web.handler.apps.business.platform;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.beimi.util.UKTools;
import com.beimi.web.model.BetGameDetail;
import com.beimi.web.model.PcBetEntity;
import com.beimi.web.service.repository.es.BetGameDetailESRepository;
import freemarker.template.utility.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	private PlayUserESRepository playUserESRepository;
	
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
		/*Page<BetGameDetail> page=betGameDetailESRes.findByOrgi("beimi",new PageRequest(super.getP(request), super.getPs(request)));
		for (BetGameDetail betGameDetail){

		}*/
		map.addAttribute("playersList", betGameDetailESRes.findByOrgi("beimi",new PageRequest(super.getP(request), super.getPs(request)))) ;
		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}

	@RequestMapping({"/gameusers/search"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView search(ModelMap map , HttpServletRequest request , @Valid String username,@Valid String periods){
		int per=0;
		if(StringUtils.isNotEmpty(periods) && StringUtils.isNotEmpty(username)){
			per=Integer.parseInt(periods);
			map.addAttribute("playersList", betGameDetailESRes.findByUsernameAndPeriods(username,per,new PageRequest(super.getP(request), super.getPs(request)))) ;
		}else if (StringUtils.isNotEmpty(periods)){
			per=Integer.parseInt(periods);
			map.addAttribute("playersList", betGameDetailESRes.findByPeriods(per,new PageRequest(super.getP(request), super.getPs(request)))) ;
		}else if (StringUtils.isNotEmpty(username)){
			map.addAttribute("playersList", betGameDetailESRes.findByUsername(username,new PageRequest(super.getP(request), super.getPs(request)))) ;
		}
		PlayUser playUser=playUserESRepository.findByUsername(username);
		map.addAttribute("playersList", betGameDetailESRes.findByUserIdAndPeriods(playUser.getId(),per,new PageRequest(super.getP(request), super.getPs(request)))) ;

		return request(super.createAppsTempletResponse("/apps/business/platform/game/online/index"));
	}
	
	@RequestMapping({"/gameusers/edit"})
	@Menu(type="platform", subtype="onlinegameusers")
	public ModelAndView edit(ModelMap map , HttpServletRequest request , @Valid String username){
		BetGameDetail betGameDetail=betGameDetailESRes.findByUsername(username);
		ArrayList<PcBetEntity> pcBetEntityList= betGameDetail.getPcBetEntityList();
		map.addAttribute("pcBetEntityList", pcBetEntityList) ;
		return request(super.createRequestPageTempletResponse("/apps/business/platform/game/online/edit"));
	}
	
	@RequestMapping("/gameusers/update")
    @Menu(type = "admin" , subtype = "gameusers")
    public ModelAndView update(HttpServletRequest request ,@Valid PlayUser players) {
		PlayUser playUser = playUserESRepository.findById(players.getId()) ;
		if(playUser!=null){
			playUser.setDisabled(players.isDisabled());
			playUser.setGoldcoins(players.getGoldcoins());
			playUser.setDiamonds(players.getDiamonds());
			playUser.setUpdatetime(new Date());
			UKTools.published(players,playUserESRepository,playUserRes);
		}
    	return request(super.createRequestPageTempletResponse("redirect:/apps/platform/online/gameusers.html"));
    }
}
