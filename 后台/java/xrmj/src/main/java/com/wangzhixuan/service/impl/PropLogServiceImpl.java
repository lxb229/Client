package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PropLog;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.GamePlayerVo;
import com.wangzhixuan.model.vo.PropLogVo;
import com.wangzhixuan.model.vo.RedPacketVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.PropLogMapper;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.service.IOperatingStatisticsService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IPlayerWishService;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.service.ISystemTaskService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class PropLogServiceImpl extends ServiceImpl<PropLogMapper, PropLog> implements IPropLogService {

	private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private PropLogMapper propLogMapper;
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IJackpotService jackpotService;
	@Autowired
	private IPlayerMoneyService playerMoneyService;
	@Autowired
	private IOperatingStatisticsService operatingService;
	@Autowired
	private IPlayerWishService wishService;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<PropLogVo> list = propLogMapper.selectPropLogVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	
	@Override
	public void taskPropLog(SystemTask task) {
		Result success = processingPropLog(task);
		if(success != null && success.isSuccess()) {
			task.setTaskStatus(1);
		} else {
			logger.info(success.getMsg());
			task.setTaskStatus(2);
		}
		task.setTaskNum(task.getTaskNum()+1);
		taskService.updateById(task);
	}

	@Override
	public Result processingPropLog(SystemTask task) {
		Result result = null;
		GamePlayerVo vo = null;
		if(task == null) {
			return new Result(false,"数据异常!");
		} else {
			String content = "["+task.getTaskContent()+"]";
			List<GamePlayerVo> voList = gson.fromJson(content, new TypeToken<List<GamePlayerVo>>(){}.getType());
			if(voList != null && voList.size() > 0) {
				vo = voList.get(0);
			}
			if(vo != null) {
				// 创建房间
				if(StringUtils.isNotBlank(vo.getCreate_start_no())) {
					result = createRoom(vo.getCreate_start_no());
				}
				if(result != null && result.isSuccess() && vo.getAmount() != null && StringUtils.isNotBlank(vo.getConsume_start_no())) {
					// 房卡消耗
					Date createTime = new Date();
					if(StringUtils.isNotBlank(vo.getCreate_time())) {
						createTime = new Date(new Long(vo.getCreate_time()));
					}
					result = useRoomcard(vo.getConsume_start_no(), vo.getAmount(), createTime);
				}
				if(result != null && result.isSuccess() && vo.getPlayerList() != null) {
					// 牌局参与玩家
					result = joinParty(vo.getPlayerList());
				}
			}
			return result;
		}
	}

	@Override
	public Result createRoom(String startNo) {
		/**创建房间-更新玩家祝福值*/
		return wishService.wishCreateRoom(startNo);
	}

	@Override
	public Result useRoomcard(String startNo, String useAmount, Date createTime) {
		/**消耗房卡数量*/
		int amount = Integer.parseInt(useAmount);
		/**玩家*/
		Player player = playerService.selectPlayerBy(startNo);
		Result result = null;
		if(player != null) {
			/**生成道具增加日志*/
			result = insertPropLogByGame(player.getStartNo(), amount, Constant.MONEY_ROOMCARD, createTime);
			/**消耗的房卡注入保底奖池*/
			if(result != null && result.isSuccess()) {
				result = jackpotService.supplementJackpot(amount);
			}
			/**更新玩家的货币*/
			if(result != null && result.isSuccess()) {
				result = playerMoneyService.playerMoneyByGame(player.getStartNo(), amount, Constant.MONEY_ROOMCARD);
			}
			/**更新运营统计*/
			if(result != null && result.isSuccess()) {
				result = operatingService.operatingByGame(amount);
			}
		}
		return result;
	}

	@Override
	public Result joinParty(List<String> playerList) {
		Result result = null;
		for (int i = 0; i < playerList.size(); i++) {
			/**更新玩家祝福值*/
			result = wishService.wishJoinParty(playerList.get(i));
			/**更新奖池领取条件总和*/
			if(result != null && result.isSuccess()) {
				result = jackpotService.wishJoinParty();
			}
		}
		return result;
	}

	@Override
	public Result insertPropLog(SystemOrder order) {
		PropLog propLog = new PropLog();
		boolean success = false;
		if(order != null) {
			propLog.setStartNo(order.getStartNo());
			propLog.setMoneyType(Constant.MONEY_ROOMCARD);
			propLog.setAmount(order.getRoomcardAmount());
			propLog.setLogType(1);
			propLog.setCreateTime(new Date());
			String purchaseType = "";
			switch (order.getPurchaseType()) {
				case 1:
					purchaseType = "线下购买";
					break;
				case 2:
					purchaseType = "APP内购";
					break;
				case 3:
					purchaseType = "网页购买";
					break;
				case 4:
					purchaseType = "公众号购买";
					break;
				default:
					break;
			}
			Player player = playerService.selectPlayerBy(order.getStartNo());
			String content = player.getNick()+"通过"+purchaseType+"购买房卡"+order.getRoomcardAmount()+"张";
			propLog.setContent(content);
			success = this.insert(propLog);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "订单-增加道具日志失败");
		}
	}
	
	@Override
	public Result insertPropLog(RedPacketVo vo) {
		PropLog propLog = new PropLog();
		boolean success = false;
		if(vo != null) {
			propLog.setStartNo(vo.getStartNo());
			propLog.setMoneyType(vo.getMoneyType());
			propLog.setAmount(vo.getAmount());
			propLog.setLogType(3);
			propLog.setCreateTime(new Date());
			String moneyType = "";
			switch (vo.getMoneyType()) {
				case 2:
					moneyType = "金币";
					break;
				case 3:
					moneyType = "银币";
					break;
				default:
					break;
			}
			Player player = playerService.selectPlayerBy(vo.getStartNo());
			String content = player.getNick()+"领取"+vo.getRedNo()+"号红包，获得"+moneyType+"枚";
			propLog.setContent(content);
			success = this.insert(propLog);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "领取红包-增加道具日志失败");
		}
	}
	
	

	@Override
	public Result insertPropLogByGame(String startNo, Integer amount, Integer moneyType, Date time) {
		PropLog propLog = new PropLog();
		boolean success = false;
		if(StringUtils.isNotBlank(startNo) && amount != null && moneyType != null) {
			propLog.setStartNo(startNo);
			propLog.setMoneyType(moneyType);
			propLog.setAmount(amount);
			propLog.setLogType(2);
			propLog.setCreateTime(time);
			Player player = playerService.selectPlayerBy(startNo);
			String mType = "";
			switch (moneyType) {
				case 1:
					mType="房卡";
					break;
				case 2:
					mType="金币";
					break;
				case 3:
					mType="银币";
					break;
				default:
					break;
			}
			String content = player.getNick()+"通过游戏消耗"+mType+amount;
			propLog.setContent(content);
			success = this.insert(propLog);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "订单-增加道具日志失败");
		}
	}
	

	@Override
	public Result insertPropLogBydonate(String startNo, Integer amount, Date createTime, Integer type) {
		PropLog propLog = new PropLog();
		boolean success = false;
		if(StringUtils.isNotBlank(startNo) && amount != null && type != null) {
			propLog.setStartNo(startNo);
			propLog.setMoneyType(Constant.MONEY_ROOMCARD);
			propLog.setAmount(amount);
			propLog.setLogType(2);
			propLog.setCreateTime(createTime);
			Player player = playerService.selectPlayerBy(startNo);
			String mType = "";
			switch (type) {
				case 0:
					mType="添加";
					break;
				case 1:
					mType="捐赠";
					break;
				default:
					break;
			}
			String content = player.getNick()+mType+"房卡"+amount+"张";
			propLog.setContent(content);
			success = this.insert(propLog);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "订单-增加道具日志失败");
		}
	}


	@Override
	public Result insertPropLogUseSilver(String startNo, Integer amount, Integer type) {
		PropLog propLog = new PropLog();
		boolean success = false;
		if(StringUtils.isNotBlank(startNo) && amount != null && type != null) {
			propLog.setStartNo(startNo);
			propLog.setMoneyType(Constant.MONEY_SILVER);
			propLog.setAmount(amount);
			propLog.setCreateTime(new Date());
			Player player = playerService.selectPlayerBy(startNo);
			String mType = "";
			switch (type) {
				/**刷新抽奖消耗*/
				case 1:
					propLog.setLogType(4);
					mType="刷新抽奖";
					break;
				/**银币抽奖消耗*/
				case 2:
					propLog.setLogType(5);
					mType="银币抽奖";
					break;
				default:
					break;
			}
			String content = player.getNick()+"通过"+mType+"消耗银币"+amount+"个";
			propLog.setContent(content);
			success = this.insert(propLog);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "银币消耗-增加道具日志失败");
		}
	}

	@Override
	public Result insertPropLogUseGold(String startNo, Integer amount) {
		PropLog propLog = new PropLog();
		boolean success = false;
		if(StringUtils.isNotBlank(startNo) && amount != null) {
			propLog.setStartNo(startNo);
			propLog.setMoneyType(Constant.MONEY_GOLD);
			propLog.setAmount(amount);
			propLog.setCreateTime(new Date());
			propLog.setLogType(6);
			Player player = playerService.selectPlayerBy(startNo);
			String content = player.getNick()+"通过金币兑换消耗金币"+amount+"个";
			propLog.setContent(content);
			success = this.insert(propLog);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "金币消耗-增加道具日志失败");
		}
	}

}
