package com.palmjoys.yf1b.act.framework.common.manager;

import java.util.Collection;

import org.springframework.stereotype.Component;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.framework.common.resource.ForbiddenWordConfig;
import com.palmjoys.yf1b.act.framework.common.resource.GameObjectConfig;

@Component
public class CommonCfgManager {
	@Static
	private Storage<Integer, ForbiddenWordConfig> forbiddenWordCfgs;
	@Static
	private Storage<Integer, GameObjectConfig> gameObjectCfgs;
	
	/**
	 * 检测屏蔽词
	 * str 等待检测的字符串
	 * replace 是否替换掉屏蔽词
	 * 返回  null=有屏蔽词
	 * */
	public String checkForbiddenWord(String str, boolean replace){
		String retStr = str;
		Collection<ForbiddenWordConfig> cfgs = forbiddenWordCfgs.getAll();
		for(ForbiddenWordConfig cfg : cfgs){
			String strWord = cfg.getName();
			int index = str.indexOf(strWord);
			if(index >= 0){
				retStr = null;
				if(replace){
					String reStr = "";
					for(int i=0; i<strWord.length(); i++){
						reStr += "*";
					}					
					str = str.replace(strWord, reStr);
					retStr = str;
				}else{
					break;
				}
			}
		}
		
		return retStr;
	}
	
	public GameObjectConfig getGameObject(int gameObjectId){
		return gameObjectCfgs.get(gameObjectId, false);
	}
}
