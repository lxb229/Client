package com.guse.platform.service.doudou.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.dao.doudou.UserRoomcardsMapper;
import com.guse.platform.service.doudou.SystemPropLogService;
import com.guse.platform.service.doudou.UserRoomcardsService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.entity.doudou.SystemPropLog;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.doudou.UserRoomcards;
import com.guse.platform.entity.system.Users;

/**
 * user_roomcards
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class UserRoomcardsServiceImpl extends BaseServiceImpl<UserRoomcards, java.lang.Integer> implements UserRoomcardsService{
	
	private static Logger logger = LoggerFactory.getLogger(UserRoomcardsServiceImpl.class);
	
	@Autowired
	private UserRoomcardsMapper  userRoomcardsMapper;
	@Autowired
	private SystemTaskMapper taskMapper;
	@Autowired
	private UsersService usersService;
	@Autowired
	private SystemPropLogService propLogService;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(userRoomcardsMapper);
	}

	@Override
	public Result<Integer> saveOrUpdateRoomcard(UserRoomcards roomcards) {
		if(roomcards.getNewRoomcards() == null) {
			roomcards.setNewRoomcards(0);
		}
		if(roomcards.getBuyRoomcards() == null) {
			roomcards.setBuyRoomcards(0);
		}
		if(roomcards.getUseRoomcards() == null) {
			roomcards.setUseRoomcards(0);
		}
		if(roomcards.getGivenRoomcards() == null) {
			roomcards.setGivenRoomcards(0);
		}
		if(roomcards.getSendOutRoomcards() == null) {
			roomcards.setSendOutRoomcards(0);
		}
		if(roomcards.getReceiveRoomcards() == null) {
			roomcards.setReceiveRoomcards(0);
		}
		if(roomcards.getConsumptionAmount() == null) {
			roomcards.setConsumptionAmount(0.00D);
		}
		if(roomcards.getEarningsAmount() == null) {
			roomcards.setEarningsAmount(0.00D);
		}
		
		ValidataBean validata = roomcards.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != roomcards.getUrcId()){
			result = userRoomcardsMapper.updateByIdSelective(roomcards);
		}else{
			roomcards.setCreateTime(new Date());
			result = userRoomcardsMapper.insert(roomcards);
		}
		return new Result<Integer>(result);
	}

	@Override
	public List<UserRoomcards> getRoomcardByUser(Integer userId) {
		return userRoomcardsMapper.getRoomcardByUser(userId);
	}

	@Override
	public void taskRoomCards(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getJsonContent());
		Result<Integer> success = processingRoomCards(obj);
		if(success.isOk()) {
			task.setTaskStatus(1);
		} else {
			logger.info(success.getErrorMsg());
			task.setTaskStatus(2);
		}
		task.setTaskNum(task.getTaskNum()+1);
		taskMapper.updateByIdSelective(task);
		
	}

	@Override
	public Result<Integer> processingRoomCards(JSONObject obj) {
		Users users = null;
		Result<Integer> result = null;
		if(obj == null) {
			return new Result<Integer>(00000,"MQ数据异常!");
		} else {
			int cmd = obj.getIntValue("cmd");
			users = usersService.getUserByStartNo(Long.parseLong(obj.getString("account_id")),obj.getString("start_no"));
			if(users == null) {
				return new Result<Integer>(00000,"房卡数据，玩家未查询到");
			}
			Users operateUser = null;
			if(Long.parseLong(obj.getString("operate_id")) > 0) {
				operateUser = usersService.getUserByAccountId(Long.parseLong(obj.getString("operate_id")));
			} else {
				operateUser = usersService.selectUserDetial(1).getData();
			}
			// 更新用户房卡数量
			List<UserRoomcards> list  = this.getRoomcardByUser(users.getUserId());
			UserRoomcards roomcards = null;
			int roomcard_num = Math.abs(Integer.parseInt(obj.getString("roomcard_num")));
			if(list != null && list.size() > 0) {
				roomcards = list.get(0);
			} else {
				roomcards = new UserRoomcards();
				roomcards.setCreateTime(new Date());
				roomcards.setNewRoomcards(0);
				roomcards.setBuyRoomcards(0);
				roomcards.setUseRoomcards(0);
				roomcards.setGivenRoomcards(0);
				roomcards.setSendOutRoomcards(0);
				roomcards.setConsumptionAmount(0D);
			}
			// 生成道具增加记录
			SystemPropLog propLog = new SystemPropLog();
			propLog.setCreateTime(new Date());
			propLog.setSplAmount(Integer.parseInt(obj.getString("roomcard_num")));
				
			switch (cmd) {
				// 房卡赠送
			   	case 1:
					if(Integer.parseInt(obj.getString("roomcard_num")) > 0 ) {
						propLog.setSplContent(users.getNick()+"获赠房卡"+roomcard_num+"张");
						roomcards.setGivenRoomcards(roomcards.getGivenRoomcards()+roomcard_num);
					} else {
						propLog.setSplContent(operateUser.getNick()+"送出房卡"+roomcard_num+"张");
						roomcards.setUseRoomcards(roomcards.getUseRoomcards()+roomcard_num);
						roomcards.setSendOutRoomcards(roomcards.getSendOutRoomcards()+roomcard_num);
					}
					propLog.setSplOprtuser(operateUser.getUserId());
					propLog.setSplType(4);
					propLog.setSplTime(obj.getDate("operate_time"));
					result = propLogService.saveOrUpdatePropLog(propLog);
					if (!result.isOk()) {
			    		return result;
			    	}
					roomcards.setUserId(users.getUserId());
					roomcards.setNewRoomcards(roomcards.getNewRoomcards()+Integer.parseInt(obj.getString("roomcard_num")));
					result = this.saveOrUpdateRoomcard(roomcards);
					break;
				// 游戏消耗/系统退卡
			   	case 2:
			   		if(Integer.parseInt(obj.getString("roomcard_num")) > 0 ) {
						propLog.setSplContent(operateUser.getNick()+"退给"+users.getNick()+"房卡"+roomcard_num+"张");
						roomcards.setGivenRoomcards(roomcards.getGivenRoomcards()+roomcard_num);
					} else {
						propLog.setSplContent(operateUser.getNick()+"消耗"+users.getNick()+"房卡"+roomcard_num+"张");
						roomcards.setUseRoomcards(roomcards.getUseRoomcards()+roomcard_num);
					}
					propLog.setSplOprtuser(operateUser.getUserId());
					propLog.setSplType(5);
					propLog.setSplTime(obj.getDate("operate_time"));
					result = propLogService.saveOrUpdatePropLog(propLog);
					if (!result.isOk()) {
			    		return result;
			    	}
					roomcards.setUserId(users.getUserId());
					roomcards.setNewRoomcards(roomcards.getNewRoomcards()+Integer.parseInt(obj.getString("roomcard_num")));
					result = this.saveOrUpdateRoomcard(roomcards);
					break;
			   	default:
			   		break;
			}
		}
		return result;
	}
}
