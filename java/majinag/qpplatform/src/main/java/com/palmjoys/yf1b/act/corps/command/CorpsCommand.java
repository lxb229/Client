package com.palmjoys.yf1b.act.corps.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.console.ConsoleBean;
import org.treediagram.nina.console.ConsoleCommand;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.corps.entity.CorpsConfigEntity;
import com.palmjoys.yf1b.act.corps.entity.CorpsEntity;
import com.palmjoys.yf1b.act.corps.manager.CorpsCfgManager;
import com.palmjoys.yf1b.act.corps.manager.CorpsManager;
import com.palmjoys.yf1b.act.corps.model.CorpsCfgVo;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;

@Component
@ConsoleBean
public class CorpsCommand {
	@Autowired
	private CorpsManager corpsManager;
	@Autowired
	private CorpsCfgManager corpsCfgManager;
	
	/**
	 * 冻结或解冻帮会
	 * corpsId 帮会Id
	 * type 1=冻结,2=取消冻结
	 * */
	@ConsoleCommand(name = "gm_corps_set_lock_state", description = "冻结或解冻帮会")
	public Object gm_corps_set_lock_state(String corpsId, int type){
		if(null == corpsId || corpsId.isEmpty() || type<1 || type>2){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);	
		}
		CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
		if(null == corpsEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		if(type == 1){
			corpsEntity.setCorpsState(-1);
		}else{
			corpsEntity.setCorpsState(0);
		}
				
		return Result.valueOfSuccess();
	}
	
	/**
	 * 设置或取消帮会推荐
	 * corpsId 帮会Id
	 * type 1=推荐,2=取消推荐
	 * */
	@ConsoleCommand(name = "gm_corps_set_recommend_state", description = "设置或取消帮会推荐")
	public Object gm_corps_set_recommend_state(String corpsId, int type){
		if(null == corpsId || corpsId.isEmpty() || type<1 || type>2){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);	
		}
		CorpsEntity corpsEntity = corpsManager.findOf_CorpsId(corpsId);
		if(null == corpsEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆不存在", null);
		}
		CorpsConfigEntity corpsConfigEntity = corpsCfgManager.loadOrCreate();
		List<String> recommendCorpsList = corpsConfigEntity.getRecommendCorpsList();
		if(type == 1){
			//设置推荐
			if(recommendCorpsList.contains(corpsId) == false){
				recommendCorpsList.add(corpsId);
				corpsConfigEntity.setRecommendCorpsList(recommendCorpsList);
			}
		}else{
			//取消推荐
			recommendCorpsList.remove(corpsId);
			corpsConfigEntity.setRecommendCorpsList(recommendCorpsList);
		}
		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 获取帮会配置
	 * */
	@ConsoleCommand(name = "gm_corps_get_corps_cfg", description = "设置或取消帮会推荐")
	public Object gm_corps_get_corps_cfg(){		
		CorpsCfgVo vo = new CorpsCfgVo();
		CorpsConfigEntity cfgEntity = corpsCfgManager.loadOrCreate();
		vo.roomCardLimit = cfgEntity.getRoomCardLimit();
		vo.createMax = cfgEntity.getCreateMax();
		vo.maxMemberNum = cfgEntity.getMaxMemberNum();
		vo.maxJoinQuestNum = cfgEntity.getMaxJoinQuestNum();
		
		return Result.valueOfSuccess(vo);
	}
	
	/**
	 * 设置帮会配置
	 * cfgStr 配置数据
	 * */
	@ConsoleCommand(name = "gm_corps_set_corps_cfg", description = "设置或取消帮会推荐")
	public Object gm_corps_set_corps_cfg(String cfgStr){
		if(null == cfgStr || cfgStr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆配置数据错误", null);
		}
		try{
			CorpsCfgVo cfg = JsonUtils.string2Object(cfgStr, CorpsCfgVo.class);
			//效验参数
			if(cfg.roomCardLimit < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆创建房卡限制数量不能小于0", null);
			}
			if(cfg.createMax <= 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆创建限制数量不能小于1", null);
			}
			if(cfg.createMax > 100){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆创建数量最大不能超过100", null);
			}
			if(cfg.maxMemberNum <= 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员限制数量不能小于1", null);
			}
			if(cfg.maxMemberNum > 1000){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员限制数量最大不能超过1000", null);
			}
			if(cfg.maxJoinQuestNum <= 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员申请加入数量不能小于1", null);
			}
			if(cfg.maxJoinQuestNum > 1000){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员申请加入数量最大不能超过1000", null);
			}
			
			CorpsConfigEntity cfgEntity = corpsCfgManager.loadOrCreate();
			cfgEntity.setRoomCardLimit(cfg.roomCardLimit);
			cfgEntity.setCreateMax(cfg.createMax);
			cfgEntity.setMaxMemberNum(cfg.maxMemberNum);
			cfgEntity.setMaxJoinQuestNum(cfg.maxJoinQuestNum);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆配置数据错误", null);
		}
		
		return Result.valueOfSuccess();
	}
	
	/**
	 * 设置帮会配置
	 * cfgStr 配置数据
	 * */
	@ConsoleCommand(name = "gm_corps_set_corps_cfg2", description = "设置或取消帮会推荐")
	public Object gm_corps_set_corps_cfg2(int roomCardLimit, int createMax, int maxMemberNum, int maxJoinQuestNum){
		try{
			//效验参数
			if(roomCardLimit < 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆创建房卡限制数量不能小于0", null);
			}
			if(createMax <= 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆创建限制数量不能小于1", null);
			}
			if(createMax > 100){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆创建数量最大不能超过100", null);
			}
			if(maxMemberNum <= 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员限制数量不能小于1", null);
			}
			if(maxMemberNum > 1000){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员限制数量最大不能超过1000", null);
			}
			if(maxJoinQuestNum <= 0){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员申请加入数量不能小于1", null);
			}
			if(maxJoinQuestNum > 1000){
				return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆成员申请加入数量最大不能超过1000", null);
			}
			
			CorpsConfigEntity cfgEntity = corpsCfgManager.loadOrCreate();
			cfgEntity.setRoomCardLimit(roomCardLimit);
			cfgEntity.setCreateMax(createMax);
			cfgEntity.setMaxMemberNum(maxMemberNum);
			cfgEntity.setMaxJoinQuestNum(maxJoinQuestNum);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "麻将馆配置数据错误", null);
		}
		
		return Result.valueOfSuccess();
	}

}
