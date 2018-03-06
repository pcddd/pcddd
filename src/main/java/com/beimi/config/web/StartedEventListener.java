package com.beimi.config.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.beimi.web.model.*;
import com.beimi.web.service.repository.jpa.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.beimi.core.BMDataContext;
import com.beimi.core.engine.game.GameEngine;
import com.beimi.util.cache.CacheHelper;

@Component
public class StartedEventListener implements ApplicationListener<ContextRefreshedEvent> {
	
//	@Resource
//	private GameEngine gameEngine ;
	
	private SysDicRepository sysDicRes;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	if(BMDataContext.getContext() == null){
    		BMDataContext.setApplicationContext(event.getApplicationContext());
    	}
//    	BMDataContext.setGameEngine(gameEngine);
    	
    	sysDicRes = event.getApplicationContext().getBean(SysDicRepository.class) ;
    	List<SysDic> sysDicList = sysDicRes.findAll() ;
    	
    	for(SysDic dic : sysDicList){
    		CacheHelper.getSystemCacheBean().put(dic.getId(), dic, dic.getOrgi());
			if(dic.getParentid().equals("0")){
				List<SysDic> sysDicItemList = new ArrayList<SysDic>();
				for(SysDic item : sysDicList){
					if(item.getDicid()!=null && item.getDicid().equals(dic.getId())){
						sysDicItemList.add(item) ;
					}
				}
				CacheHelper.getSystemCacheBean().put(dic.getCode(), sysDicItemList, dic.getOrgi());
			}
		}
    	/**
    	 * 加载系统全局配置
    	 */
    	SystemConfigRepository systemConfigRes = event.getApplicationContext().getBean(SystemConfigRepository.class) ;
    	SystemConfig config = systemConfigRes.findByOrgi(BMDataContext.SYSTEM_ORGI) ;
    	if(config != null){
    		CacheHelper.getSystemCacheBean().put("systemConfig", config, BMDataContext.SYSTEM_ORGI);
    	}

		/**
		 * 倍率缓存
		 */
		BetLevelRepository betLevelRepository = event.getApplicationContext().getBean(BetLevelRepository.class);
    	try{
			List<BetLevelTypeInfo> betLevelTypeInfoList = betLevelRepository.findAll() ;
			if(betLevelTypeInfoList.size() > 0){
				for(BetLevelTypeInfo betLevelTypeInfo : betLevelTypeInfoList){
					CacheHelper.getBetValueCacheBean().put(betLevelTypeInfo.getId(), betLevelTypeInfo, BMDataContext.SYSTEM_ORGI);
				}
			}
		}catch (Exception e){
    		e.printStackTrace();
		}
    	
    	GenerationRepository generationRes = event.getApplicationContext().getBean(GenerationRepository.class) ;
    	List<Generation> generationList = generationRes.findAll() ;
    	for(Generation generation : generationList){
    		CacheHelper.getSystemCacheBean().setAtomicLong(BMDataContext.ModelType.ROOM.toString(), generation.getStartinx());
    	}

    	//缓存环信房间字符串
		cacheHxRoomIdStr(event);
    	//缓存游戏投注类型
		cacheLevelBetInfo(event);
    }

	private void cacheLevelBetInfo(ContextRefreshedEvent event) {
		BetTypeGroupRepository betTypeGroupRepository = event.getApplicationContext().getBean(BetTypeGroupRepository.class) ;
		List<GameBetType> gameBetTypes = betTypeGroupRepository.findAll();
		for (int i=0; i<gameBetTypes.size();i++){
			GameBetType gameBetType = gameBetTypes.get(i);
			CacheHelper.getPcGameLevelBetTypeCacheBean().put(gameBetType.getId(),gameBetType,BMDataContext.SYSTEM_ORGI);
		}
	}

	private void cacheHxRoomIdStr(ContextRefreshedEvent event) {
		String hxRoomIds = "";
		String roomStrs = "[";
		String[] roomIdArr;
		PcRoomRepository pcRoomRepository = event.getApplicationContext().getBean(PcRoomRepository.class) ;
		List<PcRoomInfo> pcBjRoomInfoList = pcRoomRepository.findByOrgi("1");
		for(PcRoomInfo pcRoomInfo : pcBjRoomInfoList){
			if (StringUtils.isNotEmpty(pcRoomInfo.getRoomid())){
				hxRoomIds += pcRoomInfo.getRoomid()+",";
			}
		}
		roomIdArr = hxRoomIds.split(",");
		for (int i=0;i<roomIdArr.length;i++){
			if (StringUtils.isNotEmpty(roomIdArr[i])){
				roomStrs += "\"" + roomIdArr[i] + "\"";
			}
			if (i != roomIdArr.length-1){
				roomStrs += ",";
			}
		}
		roomStrs += "]";
		CacheHelper.getSystemCacheBean().put(BMDataContext.ROOMID_TYPE_BJ,roomStrs,BMDataContext.SYSTEM_ORGI);
		List<PcRoomInfo> pcJndRoomInfoList = pcRoomRepository.findByOrgi("2");

		hxRoomIds = "";
		roomStrs = "[";
		for(PcRoomInfo pcRoomInfo : pcJndRoomInfoList){
			if (StringUtils.isNotEmpty(pcRoomInfo.getRoomid())){
				hxRoomIds += pcRoomInfo.getRoomid()+",";
			}
		}
		System.out.println();
		roomIdArr = hxRoomIds.split(",");
		for (int i=0;i<roomIdArr.length;i++){
			if (StringUtils.isNotEmpty(roomIdArr[i])){
				roomStrs += "\"" + roomIdArr[i] + "\"";
			}
			if (i != roomIdArr.length-1){
				roomStrs += ",";
			}
		}
		roomStrs += "]";
		CacheHelper.getSystemCacheBean().put(BMDataContext.ROOMID_TYPE_JND,roomStrs,BMDataContext.SYSTEM_ORGI);
	}
}