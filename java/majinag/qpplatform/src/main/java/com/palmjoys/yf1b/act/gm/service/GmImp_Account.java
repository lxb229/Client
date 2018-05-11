package com.palmjoys.yf1b.act.gm.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.network.handler.JettyRequestHandler;
import org.treediagram.nina.network.model.Result;

import com.palmjoys.yf1b.act.cooltime.manger.CheckResetTimeManager;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeConfigType;
import com.palmjoys.yf1b.act.cooltime.model.CoolTimeResult;
import com.palmjoys.yf1b.act.framework.account.entity.AccountEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AccountLuckyEntity;
import com.palmjoys.yf1b.act.framework.account.entity.AuthenticationEntity;
import com.palmjoys.yf1b.act.framework.account.manager.AccountLuckyManager;
import com.palmjoys.yf1b.act.framework.account.manager.AccountManager;
import com.palmjoys.yf1b.act.framework.account.manager.AuthenticationManager;
import com.palmjoys.yf1b.act.gm.model.GMDefine;
import com.palmjoys.yf1b.act.hotprompt.manager.HotPromptManager;
import com.palmjoys.yf1b.act.hotprompt.model.HotPromptDefine;
import com.palmjoys.yf1b.act.task.entity.TaskEntity;
import com.palmjoys.yf1b.act.task.manager.TaskManager;
import com.palmjoys.yf1b.act.task.model.TaskStatisticsAttrib;
import com.palmjoys.yf1b.act.wallet.manager.WalletManager;
import com.palmjoys.yf1b.act.wallet.service.WalletService;

@Service
public class GmImp_Account implements JettyRequestHandler{
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private WalletManager walletManager;
	@Autowired
	private AccountLuckyManager accountLuckyManager;
	@Autowired
	private WalletService walletService;
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private HotPromptManager hotPromptManager;
	@Autowired
	private CheckResetTimeManager resetTimeManager;

	@Override
	public String getPath() {
		return GMDefine.GM_CMD_ACCOUNT;
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		int err = GMDefine.GM_ERR_GM_CMD;
		Object retObj = null;
		try{
			String sCmd = request.getParameter("cmd");
			int nCmd = Integer.parseInt(sCmd);
			switch(nCmd){
			case 1://冻结或解冻帐号
				retObj = modfiyLockState(baseRequest, request);
				break;
			case 2://修改帐号类型
				retObj = modfiyAccountType(baseRequest, request);
				break;
			case 3://修改帮定的手机号
				retObj = modfiyBindedPhone(baseRequest, request);
				break;
			case 4://修改实名认证信息
				retObj = modfiyAuthentication(baseRequest, request);
				break;
			case 5://增加或减少房卡
				retObj = modfiyWallet(baseRequest, request);
				break;
			case 6://设置幸运值
				retObj = mofiyPlayerLucky(baseRequest, request);
				break;
			case 7://赠送房卡
				retObj = giverRoomCard(baseRequest, request);
				break;
			}			
		}catch(Exception e){
			err = GMDefine.GM_ERR_SVR_EXCEPTION;
		}
		
		if(null == retObj){
			retObj = Result.valueOfError(err, GMDefine.Err2Msg(err), null);
		}
		String retStr = JsonUtils.object2String(retObj);
		byte []retBytes = null;
		try{
			retBytes = retStr.getBytes("utf8");
		}catch(Exception e){
		}
		return retBytes;
	}
	
	private Object modfiyLockState(Request baseRequest, HttpServletRequest request){
		String starNO = request.getParameter("starNO");
		if(null == starNO){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		String state = request.getParameter("state");
		int nState = Integer.parseInt(state);
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);		
		if(null == accountEntity){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		accountEntity.setState(nState);
		return Result.valueOfSuccess();
	}
	
	private Object modfiyAccountType(Request baseRequest, HttpServletRequest request){
		String starNO = request.getParameter("starNO");
		String type = request.getParameter("type");
		String wxNO = request.getParameter("wxNO");
		int nType = Integer.parseInt(type);
		if((nType!=0 && nType!=1) || (nType==1 && (wxNO==null || wxNO.isEmpty()))){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);		
		if(null == accountEntity){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		accountEntity.setAccountType(nType);
		if(nType == 1){
			AuthenticationEntity authenticationEntity = authenticationManager.loadOrCreate(accountEntity.getId());
			authenticationEntity.setWxNO(wxNO);
		}
		
		return Result.valueOfSuccess();
	}
	
	private Object modfiyBindedPhone(Request baseRequest, HttpServletRequest request){
		String starNO = request.getParameter("starNO");
		String phone = request.getParameter("phone");
		if(null==phone || null==starNO || phone.length() != 11){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);		
		if(null == accountEntity){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		AuthenticationEntity authenticationEntity = authenticationManager.loadOrCreate(accountEntity.getId());
		authenticationEntity.setPhone(phone);
		return Result.valueOfSuccess();
	}
	
	private Object modfiyAuthentication(Request baseRequest, HttpServletRequest request){
		String starNO = request.getParameter("starNO");
		String name = request.getParameter("name");
		String cardId = request.getParameter("cardId");
		if(null==name || null==starNO || name.length() < 2
				|| null==cardId || cardId.length() != 18){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);		
		if(null == accountEntity){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		AuthenticationEntity authenticationEntity = authenticationManager.loadOrCreate(accountEntity.getId());
		authenticationEntity.setCardId(cardId);
		authenticationEntity.setName(name);
		
		return Result.valueOfSuccess();
	}

	private Object modfiyWallet(Request baseRequest, HttpServletRequest request){
		String starNO = request.getParameter("starNO");
		String type = request.getParameter("type");
		String money = request.getParameter("money");
		if(null==money || null==starNO || null==type){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		int nMoney = Integer.parseInt(money);
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);		
		if(null == accountEntity){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		int nType = Integer.parseInt(type);
		if(nType <1 || nType>5){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		int rs = 0;
		switch(nType){
		case 1://房卡
			rs = walletManager.addRoomCard(accountEntity.getId(), nMoney);
			break;
		case 2://体验用房卡
			rs = walletManager.addReplaceCard(accountEntity.getId(), nMoney);
			break;
		case 3://钻石
			rs = walletManager.addDiamond(accountEntity.getId(), nMoney);
			break;
		case 4://金币
			rs = walletManager.addGoldMoney(accountEntity.getId(), nMoney);
			break;
		case 5://银币
			rs = walletManager.addSilverMoney(accountEntity.getId(), nMoney);
			break;
		}		
		
		if(rs < 0){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		
		if(nMoney > 0 && nType==1){
			//增加充值统计记录
			TaskEntity taskEntity = taskManager.loadOrCreate(accountEntity.getId());
			TaskStatisticsAttrib taskStatistics = taskEntity.getTaskStatistics();
			CoolTimeResult coolTimeResult = resetTimeManager.checkPlayerResetTime(accountEntity.getId(), CoolTimeConfigType.PLAYERTIME_RESET_DAYTASK, true);
			if(coolTimeResult.bReset){
				taskStatistics.reset();
			}
			taskStatistics.dayChargeCard += nMoney;
			taskEntity.setTaskStatistics(taskStatistics);
			
			if(hotPromptManager.updateHotPrompt(accountEntity.getId(), HotPromptDefine.HOT_KEY_DAYTASK) > 0){
				hotPromptManager.notifyHotPrompt(accountEntity.getId());
			}
		}
		return Result.valueOfSuccess();
	}
	
	private Object mofiyPlayerLucky(Request baseRequest, HttpServletRequest request){
		String starNO = request.getParameter("starNO");
		String luck = request.getParameter("luck");
		String luckStartTime = request.getParameter("luckStart");
		String luckEndTime = request.getParameter("luckEnd");
		String luckNum = request.getParameter("luckNum");
		if(null==luck || null==starNO || null==luckStartTime 
			|| null==luckEndTime || null==luckNum){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		int nLuck = Integer.parseInt(luck);
		AccountEntity accountEntity = accountManager.findOf_starNO(starNO);		
		if(null == accountEntity){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		AccountLuckyEntity entity = accountLuckyManager.loadOrCreate(accountEntity.getId());
		entity.setLuck(nLuck);
		entity.setLuckStartTime(Long.parseLong(luckStartTime));
		entity.setLuckEndTime(Long.parseLong(luckEndTime));
		entity.setLuckTotalNum(Integer.parseInt(luckNum));
		entity.setLuckUsedNum(0);
		return Result.valueOfSuccess();
	}
	
	private Object giverRoomCard(Request baseRequest, HttpServletRequest request){
		String starNO1 = request.getParameter("starNO1");
		String starNO2 = request.getParameter("starNO2");
		String num = request.getParameter("num");
		int nNum = Integer.parseInt(num);
		if(nNum <= 0){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		
		AccountEntity accountEntity1 = accountManager.findOf_starNO(starNO1);	
		AccountEntity accountEntity2 = accountManager.findOf_starNO(starNO2);	
		if(null == accountEntity1 || null == accountEntity2){
			return Result.valueOfError(GMDefine.GM_ERR_GM_PARAM,
					GMDefine.Err2Msg(GMDefine.GM_ERR_GM_PARAM), null);
		}
		return walletService.wallet_roomcard_give(accountEntity1.getId(), starNO2, nNum);
	}
	
}
