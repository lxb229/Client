package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.PlayerMoney;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.vo.RedPacketVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.PlayerMoneyMapper;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家账号余额 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class PlayerMoneyServiceImpl extends ServiceImpl<PlayerMoneyMapper, PlayerMoney> implements IPlayerMoneyService {

	@Override
	public Result insertPlayerMoney(String startNo) {
		boolean success = false;
		if(StringUtils.isNotBlank(startNo)) {
			PlayerMoney money = new PlayerMoney();
			money.setStartNo(startNo);
			money.setRoomCard(0);
			money.setGoldCoin(0);
			money.setSilverCoin(0);
			money.setAppPay(new BigDecimal("0.00"));
			money.setOfflinePay(new BigDecimal("0.00"));
			money.setWebPay(new BigDecimal("0.00"));
			money.setWechatPay(new BigDecimal("0.00"));
			money.setUseRoomCard(0);
			money.setAllGold(0);
			money.setAllSilver(0);
			success = this.insert(money);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "保存失败");
		}
	}
	
	@Override
	public Result playerMoneyByGame(String startNo, Integer amount, Integer moneyType) {
		PlayerMoney playerMoney = getPlayerMoneyBy(startNo);
		if(playerMoney != null && StringUtils.isNotBlank(startNo) && amount != null && moneyType != null) {
			switch (moneyType) {
				case 1:
					playerMoney.setRoomCard(playerMoney.getRoomCard()-amount);
					playerMoney.setUseRoomCard(playerMoney.getUseRoomCard()+amount);
					break;
				case 2:
					playerMoney.setGoldCoin(playerMoney.getGoldCoin()-amount);
					break;
				case 3:
					playerMoney.setSilverCoin(playerMoney.getSilverCoin()-amount);
					break;
				default:
					break;
			}
			return updatePlayerMoney(playerMoney);
		}
		
		return new Result(false, "游戏消耗-更新玩家货币失败");
	}
	
	@Override
	public Result updatePlayerMoney(SystemOrder order) {
		if(order != null) {
			PlayerMoney playerMoney = getPlayerMoneyBy(order.getStartNo());
			playerMoney.setRoomCard(playerMoney.getRoomCard()+order.getRoomcardAmount());
			switch (order.getPurchaseType()) {
				case 1:
					playerMoney.setOfflinePay(playerMoney.getOfflinePay().add(order.getPayPrice()));
					break;
				case 2:
					playerMoney.setAppPay(playerMoney.getAppPay().add(order.getPayPrice()));
					break;
				case 3:
					playerMoney.setWebPay(playerMoney.getWebPay().add(order.getPayPrice()));
					break;
				case 4:
					playerMoney.setWebPay(playerMoney.getWebPay().add(order.getPayPrice()));
					break;
				default:
					break;
			}
			return updatePlayerMoney(playerMoney);
		}
		return new Result(false, "订单-更新玩家货币失败");
	}
	
	@Override
	public Result updatePlayerMoney(RedPacketVo vo) {
		if(vo != null) {
			PlayerMoney playerMoney = getPlayerMoneyBy(vo.getStartNo());
			switch (vo.getMoneyType()) {
				case 2:
					playerMoney.setGoldCoin(playerMoney.getGoldCoin()+vo.getAmount());
					playerMoney.setAllGold(playerMoney.getAllGold()+vo.getAmount());
					break;
				case 3:
					playerMoney.setSilverCoin(playerMoney.getSilverCoin()+vo.getAmount());
					playerMoney.setAllSilver(playerMoney.getAllGold()+vo.getAmount());
					break;
				default:
					break;
			}
			return updatePlayerMoney(playerMoney);
		}
		return new Result(false, "领取红包-更新玩家货币失败");
	}

	@Override
	public Result updatePlayerMoney(PlayerMoney money) {
		boolean success = this.updateById(money);
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "更新失败");
		}
	}

	@Override
	public PlayerMoney getPlayerMoneyBy(String startNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("start_no", startNo);
		List<PlayerMoney> moneyList = this.selectByMap(map);
		if(moneyList != null && moneyList.size() > 0) {
			return moneyList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result playerUseSilver(String startNo, Integer amount) {
		Result result = null;
		if(StringUtils.isNotBlank(startNo)) {
			/**获取玩家货币*/
			PlayerMoney playerMoney = this.getPlayerMoneyBy(startNo);
			/**判断当前拥有银币是否大于消耗数量*/
			if(playerMoney != null && playerMoney.getSilverCoin() > amount) {
				result = playerMoneyByGame(startNo, amount, Constant.MONEY_SILVER);
			} else {
				result = new Result(false, "银币数量不足");
			}
		} else {
			result = new Result(false, "玩家明星号错误");
		}
		return result;
	}

	@Override
	public Result playerUserGold(String startNo, Integer amount) {
		Result result = null;
		if(StringUtils.isNotBlank(startNo)) {
			/**获取玩家货币*/
			PlayerMoney playerMoney = this.getPlayerMoneyBy(startNo);
			/**判断当前拥有金币是否大于消耗数量*/
			if(playerMoney != null && playerMoney.getGoldCoin() > amount) {
				result = playerMoneyByGame(startNo, amount, Constant.MONEY_GOLD);
			} else {
				result = new Result(false, "金币数量不足");
			}
		} else {
			result = new Result(false, "玩家明星号错误");
		}
		return result;
	}

}
