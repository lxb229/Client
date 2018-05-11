package com.guse.platform.common.utils;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.platform.common.base.Result;
import com.guse.platform.dao.doudou.SystemOrderMapper;
import com.guse.platform.dao.doudou.SystemProductMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.SystemOrder;
import com.guse.platform.entity.doudou.SystemProduct;
import com.guse.platform.entity.doudou.SystemPropLog;
import com.guse.platform.entity.doudou.UserRoomcards;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemOrderService;
import com.guse.platform.service.doudou.SystemPropLogService;
import com.guse.platform.service.doudou.UserRoomcardsService;
import com.guse.platform.vo.doudou.AccountVo;
@Component  
public class PayOrderUtil {
	
	@Autowired
	private SystemOrderMapper  systemOrderMapper;
	@Autowired
	private SystemOrderService  systemOrderService;
	@Autowired
	private SystemProductMapper  systemProductMapper;
	@Autowired
	private UsersMapper  usersMapper;
	@Autowired
	private SystemPropLogService propLogServicer;
	@Autowired
	private UserRoomcardsService roomcardsServicer;
	@Autowired
	private GameGMUtil gameGMUtil;
	
	private static PayOrderUtil payOrderUtil;  
	  
    public void setUserInfo(SystemOrderMapper systemOrderMapper, SystemOrderService  systemOrderService ,
    		SystemProductMapper systemProductMapper, UsersMapper usersMapper,
    		SystemPropLogService propLogServicer, UserRoomcardsService roomcardsServicer, 
    		GameGMUtil gameGMUtil) {  
        this.systemOrderMapper = systemOrderMapper; 
        this.systemOrderService = systemOrderService;
        this.systemProductMapper = systemProductMapper;  
        this.usersMapper = usersMapper;  
        this.propLogServicer = propLogServicer;  
        this.roomcardsServicer = roomcardsServicer;  
        this.gameGMUtil = gameGMUtil;  
        
    }  
      
    @PostConstruct  
    public void init() {  
    	payOrderUtil = this;  
    	payOrderUtil.systemOrderMapper = this.systemOrderMapper;  
    	payOrderUtil.systemOrderService = this.systemOrderService;
    	payOrderUtil.systemProductMapper = systemProductMapper;  
    	payOrderUtil.usersMapper = usersMapper;  
    	payOrderUtil.propLogServicer = propLogServicer;  
    	payOrderUtil.roomcardsServicer = roomcardsServicer;  
    	payOrderUtil.gameGMUtil = gameGMUtil;  
  
    } 
	
	public static String payOrder(String out_trade_no) {
		SystemOrder order = payOrderUtil.systemOrderMapper.getOrderByNo(out_trade_no);
		SystemProduct product = payOrderUtil.systemProductMapper.selectById(order.getProductId());
		Users user = payOrderUtil.usersMapper.selectByPrimaryKey(order.getUserId());
		// 更改订单状态
		order.setPayTime(new Date());
		order.setPayState(1);
		Result<Integer> result = payOrderUtil.systemOrderService.saveOrUpdateOrder(order);
		if (!result.isOk()) {
    		return result.getErrorMsg();
    	}
		// 生成道具增加记录
		SystemPropLog propLog = new SystemPropLog();
		propLog.setCreateTime(new Date());
		propLog.setSplAmount(product.getSpAmount());
		propLog.setSplContent(user.getNick()+"购买房卡"+product.getSpAmount()+"张");
		propLog.setSplOprtuser(user.getUserId());
		propLog.setSplType(1);
		propLog.setSplTime(new Date());
		result = payOrderUtil.propLogServicer.saveOrUpdatePropLog(propLog);
		if (!result.isOk()) {
    		return result.getErrorMsg();
    	}
		// 更新用户房卡数量
		List<UserRoomcards> list  = payOrderUtil.roomcardsServicer.getRoomcardByUser(user.getUserId());
		UserRoomcards roomcards = null;
		if(list != null && list.size() > 0) {
			roomcards = list.get(0);
		} else {
			roomcards = new UserRoomcards();
			roomcards.setCreateTime(new Date());
			roomcards.setNewRoomcards(0);
			roomcards.setBuyRoomcards(0);
			roomcards.setConsumptionAmount(0D);
		}
		roomcards.setUserId(user.getUserId());
		roomcards.setNewRoomcards(roomcards.getNewRoomcards()+product.getSpAmount());
		roomcards.setBuyRoomcards(roomcards.getBuyRoomcards()+product.getSpAmount());
		roomcards.setConsumptionAmount(roomcards.getConsumptionAmount()+product.getSpPrice());
		result = payOrderUtil.roomcardsServicer.saveOrUpdateRoomcard(roomcards);
		if (!result.isOk()) {
    		return result.getErrorMsg();
    	}
		
		AccountVo accountVo = new AccountVo();
		accountVo.setCmd(5);
		accountVo.setStarNo(user.getStarNo());
		accountVo.setMoney(product.getSpAmount());
		Result<Integer> success = payOrderUtil.gameGMUtil.accountService(accountVo);
		if(!success.isOk()) {
			return success.getErrorMsg();
		}
		return "购买成功";
	}
}
