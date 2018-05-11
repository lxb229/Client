package com.palmjoys.yf1b.act.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.core.time.DateUtils;
import org.treediagram.nina.network.model.Result;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.condition.manager.ConditionCheckManager;
import com.palmjoys.yf1b.act.condition.model.ConditionAttrib;
import com.palmjoys.yf1b.act.condition.model.ConditionCheckResult;
import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeConfigType;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;
import com.palmjoys.yf1b.act.framework.utils.HttpClientUtils;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.message.service.MessageService;
import com.palmjoys.yf1b.act.reward.manager.RewardManager;
import com.palmjoys.yf1b.act.reward.model.RewardVo;
import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.manager.TaskManager;
import com.palmjoys.yf1b.act.task.model.RewareGoldItemDetailVo;
import com.palmjoys.yf1b.act.task.model.RewareGoldMoneyItem;
import com.palmjoys.yf1b.act.task.model.RewareGoldMoneyVo;
import com.palmjoys.yf1b.act.task.model.RewareSilverVo;
import com.palmjoys.yf1b.act.task.model.RewareSuccessGmNotifyVo;
import com.palmjoys.yf1b.act.task.model.RewareSuccessPlayerNotifyVo;
import com.palmjoys.yf1b.act.task.model.TaskAttrib;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;
import com.palmjoys.yf1b.act.task.model.TaskVo;
import com.palmjoys.yf1b.act.task.resource.TaskConfig;
import com.palmjoys.yf1b.act.task.resource.WelfareRewareConfig;
import com.palmjoys.yf1b.act.wallet.entity.WalletEntity;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class TaskServiceImp implements TaskService{
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private CheckResetTimeManager resetTimeManager;
	@Autowired
	private ConditionCheckManager conditionCheckManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private MessageService messageService;
	@Autowired
	private RewardManager rewardManager;
	@Static
	private Storage<Integer, TaskConfig> taskConfigs;
	@Static
	private Storage<Integer, WelfareRewareConfig> welfareRewareCfgs;
	
	@Override
	public Object task_get_welfare_list(Long accountId) {
		TaskVo retVo = new TaskVo();
		TaskEntity taskEntity = taskManager.loadOrCreate(accountId);
		List<TaskAttrib> welfareList = taskEntity.getWelfareList();
		boolean bChanage = false;
		for(TaskAttrib taskAttrib : welfareList){
			TaskConfig taskCfg = taskConfigs.get(taskAttrib.taskId, false);
			if(null == taskCfg){
				continue;
			}
			int total = 0;
			int complete = 0;
			ConditionCheckResult checkResult = conditionCheckManager.checkCondition(accountId, taskCfg.getFinshCondition());
			if(taskAttrib.state == TaskManager.TASK_COMPLETE_STATE_NONE){
				if(checkResult.complete >= checkResult.total){
					taskAttrib.state = TaskManager.TASK_COMPLETE_STATE_FINSH;
					bChanage = true;
				}
			}
			total = (int) checkResult.total;
			complete = (int) checkResult.complete;
			
			if(complete > total){
				complete = total;
			}
			
			retVo.addTaskItem(taskCfg.getType(), taskCfg.getId(), taskAttrib.state, 
					taskCfg.getFinshDesc(),	taskCfg.getRewardDesc(), total, complete, taskCfg.getSort());
		}
		if(bChanage){
			taskEntity.setWelfareList(welfareList);
		}
		retVo.dayLuckValue = taskEntity.getDayLuckValue();
		retVo.sortWelfare();
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object task_get_daytask_list(Long accountId) {
		CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(accountId, CoolTimeConfigType.PLAYERTIME_RESET_DAYTASK, true);
		if(coolTimeResult.bReset){
			taskManager.resetDaytask(accountId);
		}
		TaskVo retVo = new TaskVo();
		TaskEntity taskEntity = taskManager.loadOrCreate(accountId);
		List<TaskAttrib> dayTaskList = taskEntity.getDayTaskList();
		boolean bChanage = false;
		for(TaskAttrib taskAttrib : dayTaskList){
			TaskConfig taskCfg = taskConfigs.get(taskAttrib.taskId, false);
			if(null == taskCfg){
				continue;
			}
			int total = 0;
			int complete = 0;
			ConditionCheckResult checkResult = conditionCheckManager.checkCondition(accountId, taskCfg.getFinshCondition());
			if(taskAttrib.state == TaskManager.TASK_COMPLETE_STATE_NONE){
				if(checkResult.complete >= checkResult.total){
					taskAttrib.state = TaskManager.TASK_COMPLETE_STATE_FINSH;
					bChanage = true;
				}
			}
			total = (int) checkResult.total;
			complete = (int) checkResult.complete;
			
			if(complete > total){
				complete = total;
			}
			if(taskAttrib.state != TaskManager.TASK_COMPLETE_STATE_NONE){
				complete = total;
			}
			
			retVo.addTaskItem(taskCfg.getType(), taskCfg.getId(), taskAttrib.state, 
					taskCfg.getFinshDesc(),	taskCfg.getRewardDesc(), total, complete, taskCfg.getSort());
		}
		if(bChanage){
			taskEntity.setDayTaskList(dayTaskList);
		}
		retVo.dayLuckValue = taskEntity.getDayLuckValue();
		retVo.sortDayTask();
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object task_get_welfare_reware(Long accountId, int taskId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		TaskConfig taskConfig = taskConfigs.get(taskId, false);
		if(null == taskConfig){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "指定任务不存在", null);
		}
		if(taskConfig.getType() != 2){
			//不是福利任务
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		RewardVo rewardRetVo = null;
		
		String err = "接口参数错误";
		RewareSuccessGmNotifyVo rewardVo = null;
		WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
		walletEntity.lock();
		try{
			do{
				TaskAttrib findTask = null;
				TaskConfig prevTaskCfg = null;
				TaskEntity taskEntity = taskManager.loadOrCreate(accountId);
				List<TaskAttrib> taskList = taskEntity.getWelfareList();				
				for(TaskAttrib taskAttrib : taskList){
					if(taskAttrib.taskId == taskId){
						findTask = taskAttrib;
					}else{
						TaskConfig theConfig = taskConfigs.get(taskAttrib.taskId, false);
						if(null != theConfig && theConfig.getSort() == taskConfig.getSort()-1){
							prevTaskCfg = theConfig;
						}
					}
				}
				
				if(null == findTask){
					err = "指定任务不存在";
					break;
				}
				
				if(findTask.state != TaskManager.TASK_COMPLETE_STATE_FINSH){
					err = "任务未达到奖励领取条件";
					break;
				}
				int useNum = 0;
				if(null == prevTaskCfg){
					ConditionAttrib[] conditions = taskConfig.getFinshCondition();
					useNum = conditions[0].condition[0];
				}else{
					ConditionAttrib[] conditions = taskConfig.getFinshCondition();
					ConditionAttrib[] prevConditions = prevTaskCfg.getFinshCondition();
					useNum = conditions[0].condition[0] - prevConditions[0].condition[0];
				}
				
				long round = taskEntity.getRound();
				String rewareAddr = welfareRewareCfgs.get(1, false).getWelfareAddr();
				Object []params = new Object[4];
				params[0] = String.valueOf(accountEntity.getStarNO());
				params[1] = String.valueOf(taskConfig.getSort());
				params[2] = String.valueOf(round);
				params[3] = String.valueOf(useNum);
				
				String sParams = JsonUtils.object2String(params);
				String retStr = HttpClientUtils.executeByPost(rewareAddr, sParams);
				if(null == retStr || retStr.isEmpty()){
					err = "任务奖励领取失败";
					break;
				}
				
				try{
					rewardVo = JsonUtils.string2Object(retStr, RewareSuccessGmNotifyVo.class);
				}catch(Exception e){
					err = "任务奖励领取失败";
					break;
				}
				
				findTask.state = TaskManager.TASK_COMPLETE_STATE_GET;
				boolean bReset = true;
				for(TaskAttrib taskAttrib : taskList){
					if(taskAttrib.state != TaskManager.TASK_COMPLETE_STATE_GET){
						bReset = false;
						break;
					}
				}
				if(bReset){
					//所有红包都领取完了,需要重置所有红包状态和领取的计数
					TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
					taskStatistics.totalCreateTable = 0;
					taskEntity.setTaskStatistics(taskStatistics);
					for(TaskAttrib taskAttrib : taskList){
						taskAttrib.state = TaskManager.TASK_COMPLETE_STATE_NONE;
					}
					taskEntity.setRound(taskEntity.getRound() + 1);
				}
				taskEntity.setWelfareList(taskList);
				
				err = "";
			}while(false);	
		}finally{
			walletEntity.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		//发放奖励
		rewardRetVo = rewardManager.rewareGive(accountId, rewardVo.attachment, rewardVo);
		
		//检查是否发放公告
		int limit = welfareRewareCfgs.get(1, false).getNoticeLimit();
		long goldMoneyNum = rewardRetVo.getRewardNum("WALLET", 10003);
		if(goldMoneyNum > limit){
			//发送全服公告
			int templateId = 1002;
			String []params = new String[3];
			params[0] = accountEntity.getHeadImg();
			params[1] = accountEntity.getNick();
			params[2] = String.valueOf(goldMoneyNum);
			long currTime = DateUtils.getTime(-1, -1, -1, -1, -1, -1);
			
			messageService.message_notice_New(templateId, 0, params, String.valueOf(currTime), "60", 0);
		}
		
		return Result.valueOfSuccess(rewardRetVo);
	}

	@Override
	public Object task_get_task_reware(Long accountId, int taskId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		TaskConfig taskConfig = taskConfigs.get(taskId, false);
		if(null == taskConfig){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "指定任务不存在", null);
		}
		if(taskConfig.getType() != 1){
			//不是每日任务
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		
		RewardVo rewardRetVo = null;
		List<GameObject> rewareLists = new ArrayList<>();
		String err = "接口参数错误";
		WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
		walletEntity.lock();
		try{
			do{
				TaskEntity taskEntity = taskManager.loadOrCreate(accountId);
				TaskAttrib findTask = null;
				List<TaskAttrib> taskList = taskEntity.getDayTaskList();
						
				for(TaskAttrib taskAttrib : taskList){
					if(taskAttrib.taskId == taskId){
						findTask = taskAttrib;
						break;
					}
				}
				
				if(null == findTask){
					err = "指定任务不存在";
					break;
				}
				if(findTask.state != TaskManager.TASK_COMPLETE_STATE_FINSH){
					err = "未达到任务奖励领取条件";
					break;
				}				
				//获取任务奖励
				GameObject []rewares = taskConfig.getRewares();
				for(GameObject reware : rewares){
					rewareLists.add(reware);
				}
				
				findTask.state = TaskManager.TASK_COMPLETE_STATE_GET;
				taskEntity.setDayTaskList(taskList);
				
				err = "";
			}while(false);
		}finally{
			walletEntity.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		//发放奖励
		rewardRetVo = rewardManager.rewareGive(accountId, rewareLists, null);
		
		//检查红点更新
		if(hotPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_DAYTASK)>0){
			hotPromptManager.notifyHotPrompt(accountId);
		}
		
		return Result.valueOfSuccess(rewardRetVo);
	}

	@Override
	public Object task_shar_fight_score(Long accountId) {
		TaskEntity taskEntity = taskManager.loadOrCreate(accountId);
		TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
		taskStatistics.daySharScoreNum++;
		taskEntity.setTaskStatistics(taskStatistics);
		
		if(hotPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_DAYTASK) > 0){
			hotPromptManager.notifyHotPrompt(accountId);
		}
		
		return Result.valueOfSuccess();
	}

	@Override
	public Object task_silver_reware_info(Long accountId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}		
		RewareSilverVo retVo = null;
		String silverMoneyAddr = welfareRewareCfgs.get(1, false).getSilverMoneyAddr();
		int cmd = 1;
		Object []params = new Object[2];
		params[0] = String.valueOf(cmd);
		params[1] = String.valueOf(accountEntity.getStarNO());
		
		String sParams = JsonUtils.object2String(params);
		String retStr = HttpClientUtils.executeByPost(silverMoneyAddr, sParams);
		if(null == retStr || retStr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		try{
			retVo = JsonUtils.string2Object(retStr, RewareSilverVo.class);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		taskManager.setSilverRewareRefshCost(retVo.refshCost);
		taskManager.setSilverRewareDrawCost(retVo.drawCost);
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object task_silver_reware_refsh(Long accountId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		int refshCost = taskManager.getSilverRewareRefshCost();
		int silverMoney = walletManager.getSilverMoney(accountId);
		if(silverMoney < refshCost){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "刷新奖励银币不足", null);
		}
		
		RewareSilverVo retVo = null;
		String silverMoneyAddr = welfareRewareCfgs.get(1, false).getSilverMoneyAddr();
		int cmd = 2;
		Object []params = new Object[2];
		params[0] = String.valueOf(cmd);
		params[1] = String.valueOf(accountEntity.getStarNO());
		
		String sParams = JsonUtils.object2String(params);
		String retStr = HttpClientUtils.executeByPost(silverMoneyAddr, sParams);
		if(null == retStr || retStr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		try{
			retVo = JsonUtils.string2Object(retStr, RewareSilverVo.class);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		taskManager.setSilverRewareRefshCost(retVo.refshCost);
		taskManager.setSilverRewareDrawCost(retVo.drawCost);
		
		//扣除银币
		walletManager.addSilverMoney(accountId, 0-refshCost);
		walletManager.sendWalletNotify(accountId);
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object task_silver_reware_get(Long accountId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		int drawCost = taskManager.getSilverRewareDrawCost();
		RewareSuccessGmNotifyVo rewardVo = null;
		String err = "接口参数错误";
		WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
		walletEntity.lock();
		try{
			do{
				int silverMoney = walletEntity.getSilverMoney();
				if(silverMoney < drawCost){
					err = "抽取奖励银币不足";
					break;
				}
				
				String silverMoneyAddr = welfareRewareCfgs.get(1, false).getSilverMoneyAddr();
				int cmd = 3;
				Object []params = new Object[2];
				params[0] = String.valueOf(cmd);
				params[1] = String.valueOf(accountEntity.getStarNO());
				
				String sParams = JsonUtils.object2String(params);
				String retStr = HttpClientUtils.executeByPost(silverMoneyAddr, sParams);
				if(null == retStr || retStr.isEmpty()){
					err = "奖励领取失败";
					break;
				}
				try{
					rewardVo = JsonUtils.string2Object(retStr, RewareSuccessGmNotifyVo.class);
				}catch(Exception e){
					err = "奖励领取失败";
					break;
				}
				//扣除银币
				walletEntity.setSilverMoney(silverMoney-drawCost);
				
				//发送钱包变化消息
				walletManager.sendWalletNotify(accountId);
				err = "";
			}while(false);
		}finally{
			walletEntity.unLock();
		}		
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		//统计任务数据
		TaskEntity taskEntity = taskManager.loadOrCreate(accountId);
		TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
		taskStatistics.dayLuckRewardNum++;
		taskEntity.setTaskStatistics(taskStatistics);
		
		if(rewardVo.winning > 0){
			//抽中了发放奖励
			rewardManager.rewareGive(accountId, rewardVo.attachment, rewardVo);
		}		
		
		//检测红点
		long hotVal = hotPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_DAYTASK);
		hotVal += hotPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_MAIL);
		if(hotVal > 0){
			hotPromptManager.notifyHotPrompt(accountId);
		}
		
		RewareSuccessPlayerNotifyVo notifyVo = new RewareSuccessPlayerNotifyVo();
		notifyVo.rewareIndex = rewardVo.rewareIndex;
		notifyVo.winning = rewardVo.winning;
		notifyVo.refshCost = rewardVo.refshCost;
		notifyVo.drawCost = rewardVo.drawCost;
		notifyVo.rewareList = rewardVo.rewareList;
		
		taskManager.setSilverRewareRefshCost(rewardVo.refshCost);
		taskManager.setSilverRewareDrawCost(rewardVo.drawCost);
		
		return Result.valueOfSuccess(notifyVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object task_silver_reware_all_item(Long accountId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		String silverMoneyAddr = welfareRewareCfgs.get(1, false).getSilverMoneyAddr();
		int cmd = 4;
		Object []params = new Object[2];
		params[0] = String.valueOf(cmd);
		params[1] = String.valueOf(accountEntity.getStarNO());
				
		String sParams = JsonUtils.object2String(params);
		String retStr = HttpClientUtils.executeByPost(silverMoneyAddr, sParams);
		if(null == retStr || retStr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		List<String> retVo = null;
		try{
			retVo = JsonUtils.string2Collection(retStr, List.class, String.class);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		return Result.valueOfSuccess(retVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object task_gold_reware_info(Long accountId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		String goldMoneyAddr = welfareRewareCfgs.get(1, false).getGoldMoneyAddr();
		int cmd = 1;
		Object []params = new Object[6];
		params[0] = String.valueOf(cmd);
		params[1] = String.valueOf(accountEntity.getStarNO());
		params[2] = String.valueOf(accountEntity.getStarNO());
		params[3] = String.valueOf(accountEntity.getStarNO());
		params[4] = String.valueOf(accountEntity.getStarNO());
		params[5] = String.valueOf(accountEntity.getStarNO());
		
		String sParams = JsonUtils.object2String(params);
		String retStr = HttpClientUtils.executeByPost(goldMoneyAddr, sParams);
		if(null == retStr || retStr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		List<RewareGoldMoneyItem> itemList = null;
		try{
			itemList = JsonUtils.string2Collection(retStr, List.class, RewareGoldMoneyItem.class);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		taskManager.setGoldItemList(itemList);
		
		RewareGoldMoneyVo retVo = new RewareGoldMoneyVo();
		retVo.itemList = itemList;
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object task_gold_reware_item_info(Long accountId, int itemId) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		String goldMoneyAddr = welfareRewareCfgs.get(1, false).getGoldMoneyAddr();
		int cmd = 2;
		Object []params = new Object[6];
		params[0] = String.valueOf(cmd);
		params[1] = String.valueOf(accountEntity.getStarNO());
		params[2] = String.valueOf(itemId);
		params[3] = String.valueOf(accountEntity.getStarNO());
		params[4] = String.valueOf(accountEntity.getStarNO());
		params[5] = String.valueOf(accountEntity.getStarNO());
		
		String sParams = JsonUtils.object2String(params);
		String retStr = HttpClientUtils.executeByPost(goldMoneyAddr, sParams);
		if(null == retStr || retStr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}
		
		RewareGoldItemDetailVo retVo = new RewareGoldItemDetailVo();
		try{
			retVo = JsonUtils.string2Object(retStr, RewareGoldItemDetailVo.class);
		}catch(Exception e){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "获取奖励信息失败", null);
		}		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object task_gold_reware_exchange(Long accountId, int itemId, String name, String phone, String addr) {
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		if(null == name || name.isEmpty() 
				|| phone == null || phone.isEmpty() 
				|| addr == null || addr.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "兑换金币豪礼接收者姓名或地址或联系电话信息有误", null);
		}
		
		List<RewareGoldMoneyItem> goldItemList = taskManager.getGoldItemList();
		if(null == goldItemList){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "金币豪礼兑换失败", null);
		}
		int costGoldMoney = -1;
		for(RewareGoldMoneyItem item : goldItemList){
			if(item.itemId == itemId){
				costGoldMoney = item.goldMoney;
				break;
			}
		}
		
		if(costGoldMoney <= 0){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "金币豪礼兑换失败", null);
		}
		
		String err = "接口参数错误";
		RewareSuccessGmNotifyVo rewardVo = null;
		WalletEntity walletEntity = walletManager.loadOrCreate(accountId);
		walletEntity.lock();
		try{
			int goldMoney = walletEntity.getGoldMoney();
			do{
				if(goldMoney < costGoldMoney){
					err = "兑换金币豪礼金币不足";
					break;
				}
				
				String goldMoneyAddr = welfareRewareCfgs.get(1, false).getGoldMoneyAddr();
				int cmd = 3;
				Object []params = new Object[6];
				params[0] = String.valueOf(cmd);
				params[1] = String.valueOf(accountEntity.getStarNO());
				params[2] = String.valueOf(itemId);
				params[3] = String.valueOf(name);
				params[4] = String.valueOf(phone);
				params[5] = String.valueOf(addr);
				
				String sParams = JsonUtils.object2String(params);
				String retStr = HttpClientUtils.executeByPost(goldMoneyAddr, sParams);
				if(null == retStr || retStr.isEmpty()){
					err = "金币豪礼兑换失败";
					break;
				}				
				try{
					rewardVo = JsonUtils.string2Object(retStr, RewareSuccessGmNotifyVo.class);
				}catch(Exception e){
					err = "金币豪礼兑换失败";
					break;
				}
				
				//扣除金币
				walletEntity.setGoldMoney(goldMoney-costGoldMoney);
				walletManager.sendWalletNotify(accountId);
				err = "";
			}while(false);
		}finally{
			walletEntity.unLock();
		}
		
		if(err.isEmpty() == false){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, err, null);
		}
		
		//发放奖励
		RewardVo rewardRetVo = rewardManager.rewareGive(accountId, rewardVo.attachment, rewardVo);
		
		//检测红点
		if(hotPromptManager.updateHotPrompt(accountId, HotPromptDefine.HOT_KEY_MAIL) > 0){
			hotPromptManager.notifyHotPrompt(accountId);
		}
		
		return Result.valueOfSuccess(rewardRetVo);
	}

}
