package com.guse.platform.service.doudou.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.GameGMUtil;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.service.doudou.PresentService;
import com.guse.platform.service.doudou.SystemPropLogService;
import com.guse.platform.service.doudou.UserRoomcardsService;
import com.guse.platform.vo.doudou.AccountVo;
import com.guse.platform.entity.doudou.SystemPropLog;
import com.guse.platform.entity.doudou.UserRoomcards;
import com.guse.platform.entity.system.Users;

/**
 * system_cash
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class PresentServiceImpl extends BaseServiceImpl<Users, java.lang.Integer> implements PresentService{
	private static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
	@Autowired
	private UsersMapper  usersMapper;
	@Autowired
	private GameGMUtil gameGMUtil;
	@Autowired
	private SystemPropLogService propLogService;
	@Autowired
	private UserRoomcardsService roomcardService;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(usersMapper);
	}

	@Override
	public Result<PageResult<Users>> queryByStarNO(Users users, PageBean pageBean, Users user) {
		if (pageBean == null) {
			logger.info("获取俱乐部列表失败，pageBean is null ！");
			return new Result<PageResult<Users>>(00000, "获取玩家失败！");
		}
		Users playerUser = null;
		if(users != null && StringUtils.isNotBlank(users.getStarNo())) {
			playerUser = usersMapper.getUserBy(Integer.parseInt(users.getStarNo()));
		}
		/**获取玩家当前房卡库存*/
		List<UserRoomcards> roomcardList = roomcardService.getRoomcardByUser(user.getUserId());
		if(playerUser != null ) {
			if(roomcardList != null && roomcardList.size() > 0) {
				playerUser.setAccountType(roomcardList.get(0).getNewRoomcards());
			} else {
				playerUser.setAccountType(0);
			}
		}
		
		Long count = 0L; 
		if(playerUser != null) {
			count = 1L; 
		}
		if (count <= 0) {
			return new Result<PageResult<Users>>(new PageResult<Users>().initNullPage());
		}
		List<Users> list = new ArrayList<>();
		list.add(playerUser);
		PageResult<Users> pageResult = null;
		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
		pageResult.setList(list);
		return new Result<PageResult<Users>>(pageResult);
	}

	@Override
	public Result<Integer> present(AccountVo vo, Users user) {
		if(user.getStarNo().equals(vo.getStarNo())) {
			return new Result<Integer>(00000,"不能自己赠送给自己");
		}
		
		/**当管理员或者客服赠送房卡时，管理员和客服没有明星号。改为直接增加房卡*/
		if(user.getRoleId() == 1 || user.getRoleId() == 11) {
			return presentBySystem(vo, user);
		} else {
			return presentByPlayer(vo, user);
		}
		
	}

	@Override
	public Result<Integer> presentBySystem(AccountVo vo, Users user) {
		/**调用游戏服务器接口*/
		vo.setCmd(5);
		vo.setMoney(vo.getScAmount());
		Result<Integer> success = gameGMUtil.accountService(vo);
		if(!success.isOk()) {
			return success;
		}
		/** 生成道具增加记录*/
		Users presentUser = usersMapper.getUserBy(Integer.parseInt(vo.getStarNo()));
		SystemPropLog propLog = new SystemPropLog();
		propLog.setCreateTime(new Date());
		propLog.setSplAmount(vo.getScAmount());
		propLog.setSplContent(user.getNick()+"发放给"+presentUser.getNick()+"房卡"+vo.getScAmount()+"张");
		propLog.setSplOprtuser(user.getUserId());
		propLog.setSplType(2);
		propLog.setSplTime(new Date());
		success = propLogService.saveOrUpdatePropLog(propLog);
		if (!success.isOk()) {
    		return new Result<Integer>(00000,success.getErrorMsg());
    	}
		
		/**更新用户房卡数量*/
		List<UserRoomcards> list  = roomcardService.getRoomcardByUser(presentUser.getUserId());
		UserRoomcards roomcards = null;
		if(list != null && list.size() > 0) {
			roomcards = list.get(0);
		} else {
			roomcards = new UserRoomcards();
			roomcards.setCreateTime(new Date());
			roomcards.setNewRoomcards(0);
			roomcards.setBuyRoomcards(0);
			roomcards.setConsumptionAmount(0D);
			roomcards.setGivenRoomcards(0);
		}
		roomcards.setUserId(presentUser.getUserId());
		roomcards.setGivenRoomcards(roomcards.getGivenRoomcards()+vo.getScAmount());
		roomcards.setNewRoomcards(roomcards.getNewRoomcards()+vo.getScAmount());
		success = roomcardService.saveOrUpdateRoomcard(roomcards);
		if (!success.isOk()) {
			return new Result<Integer>(00000,success.getErrorMsg());
    	} else {
    		return success;
    	}
	}

	@Override
	public Result<Integer> presentByPlayer(AccountVo vo, Users user) {
		List<UserRoomcards> presentlist  = roomcardService.getRoomcardByUser(user.getUserId());
		UserRoomcards presentRoom = null;
		if(presentlist == null || presentlist.size() <=0 ) {
			return new Result<Integer>(00000,"您暂时未获得房卡");
		}
		presentRoom = presentlist.get(0);
		if(presentRoom.getNewRoomcards() < vo.getScAmount()) {
			return new Result<Integer>(00000,"您的房卡数量不足");
		}
		/**调用游戏服务器接口*/
		vo.setCmd(7);
		vo.setStarNO1(user.getStarNo());
		vo.setStarNO2(vo.getStarNo());
		Result<Integer> success = gameGMUtil.accountService(vo);
		if(!success.isOk()) {
			return success;
		}
		
		/**生成道具增减记录*/
		Users presentUser = usersMapper.getUserBy(Integer.parseInt(vo.getStarNo()));
		SystemPropLog propLog = new SystemPropLog();
		propLog.setCreateTime(new Date());
		propLog.setSplAmount(vo.getScAmount());
		propLog.setSplContent(presentUser.getNick()+"获赠房卡"+vo.getScAmount()+"张");
		propLog.setSplOprtuser(user.getUserId());
		propLog.setSplType(4);
		propLog.setSplTime(new Date());
		success = propLogService.saveOrUpdatePropLog(propLog);
		if (!success.isOk()) {
    		return new Result<Integer>(00000,success.getErrorMsg());
    	}
		SystemPropLog presentPropLog = new SystemPropLog();
		presentPropLog.setCreateTime(new Date());
		presentPropLog.setSplAmount(-vo.getScAmount());
		presentPropLog.setSplContent(user.getNick()+"送出房卡"+vo.getScAmount()+"张");
		presentPropLog.setSplOprtuser(user.getUserId());
		presentPropLog.setSplType(4);
		presentPropLog.setSplTime(new Date());
		success = propLogService.saveOrUpdatePropLog(presentPropLog);
		if (!success.isOk()) {
    		return new Result<Integer>(00000,success.getErrorMsg());
    	}
		
		/**更新用户房卡数量*/
		List<UserRoomcards> list  = roomcardService.getRoomcardByUser(presentUser.getUserId());
		UserRoomcards roomcards = null;
		if(list != null && list.size() > 0) {
			roomcards = list.get(0);
		} else {
			roomcards = new UserRoomcards();
			roomcards.setCreateTime(new Date());
			roomcards.setNewRoomcards(0);
			roomcards.setBuyRoomcards(0);
			roomcards.setConsumptionAmount(0D);
			roomcards.setGivenRoomcards(0);
		}
		roomcards.setUserId(presentUser.getUserId());
		roomcards.setGivenRoomcards(roomcards.getGivenRoomcards()+vo.getScAmount());
		roomcards.setNewRoomcards(roomcards.getNewRoomcards()+vo.getScAmount());
		success = roomcardService.saveOrUpdateRoomcard(roomcards);
		if (!success.isOk()) {
			return new Result<Integer>(00000,success.getErrorMsg());
    	}
		
		presentRoom.setUserId(user.getUserId());
		presentRoom.setUseRoomcards(presentRoom.getUseRoomcards()+vo.getScAmount());
		presentRoom.setSendOutRoomcards(presentRoom.getSendOutRoomcards()+vo.getScAmount());
		presentRoom.setNewRoomcards(presentRoom.getNewRoomcards()-vo.getScAmount());
		success = roomcardService.saveOrUpdateRoomcard(presentRoom);
		if (!success.isOk()) {
			return new Result<Integer>(00000,success.getErrorMsg());
    	} else {
    		return success;
    	}
	}
}
