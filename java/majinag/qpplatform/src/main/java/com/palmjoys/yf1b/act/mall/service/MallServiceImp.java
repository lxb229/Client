package com.palmjoys.yf1b.act.mall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeConfigType;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.event.manager.EventTriggerManager;
import com.palmjoys.yf1b.act.event.model.EventAttrib;
import com.palmjoys.yf1b.act.event.model.EventDefine;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.model.AccountDefine;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.mall.entity.MallEntity;
import com.palmjoys.yf1b.act.mall.manager.MallManager;
import com.palmjoys.yf1b.act.mall.model.MallVo;
import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.manager.TaskManager;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;

@Service
public class MallServiceImp implements MallService{
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private MallManager mallManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private EventTriggerManager eventTriggerManager;
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private CheckResetTimeManager resetTimeManager;

	@Override
	public Object mall_itemlist(Long accountId) {
		MallVo retVo = new MallVo();
		MallEntity mallEntity = mallManager.loadOrCreate();
		retVo.mallItems.addAll(mallEntity.getMallList());
		retVo.proxyItems.addAll(mallEntity.getProxyList());
		retVo.sortMallItem();
		
		return Result.valueOfSuccess(retVo);
	}

	@Override
	public Object mall_item_buy(Long accountId, int itemId) {
		return Result.valueOfSuccess();
	}

	@Override
	public Object mall_item_buy_ok(Long accountId, String order_no, String pay_price, String pay_time, int num) {
		if(num <= 0 || null==order_no || order_no.isEmpty() || pay_price==null || pay_price.isEmpty() || pay_time==null || pay_time.isEmpty()){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "接口参数错误", null);
		}
		AccountEntity accountEntity = accountManager.load(accountId);
		if(null == accountEntity){
			return Result.valueOfError(AccountDefine.ACCOUNT_ERROR_COMMON_CODE, "玩家帐号异常", null);
		}
		
		walletManager.addRoomCard(accountId, num);
		
		EventAttrib eventAttrib = new EventAttrib(EventDefine.EVENT_APP_BUY);
		eventAttrib.addEventParam(order_no);
		eventAttrib.addEventParam(accountEntity.getStarNO());
		eventAttrib.addEventParam(pay_price);
		eventAttrib.addEventParam(pay_time);
		eventAttrib.addEventParam(num);
		
		eventTriggerManager.triggerEvent(eventAttrib);
		
		//增加充值统计记录
		TaskEntity taskEntity = taskManager.loadOrCreate(accountEntity.getId());
		TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
		CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(accountEntity.getId(), CoolTimeConfigType.PLAYERTIME_RESET_DAYTASK, true);
		if(coolTimeResult.bReset){
			taskStatistics.reset();
		}
		taskStatistics.dayChargeCard += num;
		taskEntity.setTaskStatistics(taskStatistics);
		
		if(hotPromptManager.updateHotPrompt(accountEntity.getId(), HotPromptDefine.HOT_KEY_DAYTASK) > 0){
			hotPromptManager.notifyHotPrompt(accountEntity.getId());
		}
		
		return Result.valueOfSuccess();
	}

}
