package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerWish;
import com.wangzhixuan.model.RedPacketLog;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.PlayerWishVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.PlayerWishMapper;
import com.wangzhixuan.service.IPlayerWishService;
import com.wangzhixuan.service.IRedPacketLogService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家祝福值 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class PlayerWishServiceImpl extends ServiceImpl<PlayerWishMapper, PlayerWish> implements IPlayerWishService {
	@Autowired
	private IRedPacketLogService redPacketService;
	@Autowired
	private PlayerWishMapper playerWishMapper;

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<PlayerWishVo> list = playerWishMapper.selectPlayerWishVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}
	
	@Override
	public void taskPlayerWish(SystemTask task) {
		//JSON格式转换
//        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
//		Result success = processingPlayerWish(obj);
//		if(success != null && success.isSuccess()) {
//			task.setTaskStatus(1);
//		} else {
//			logger.info(success.getMsg());
//			task.setTaskStatus(2);
//		}
//		task.setTaskNum(task.getTaskNum()+1);
//		taskService.updateById(task);
		
	}

	@Override
	public Result processingPlayerWish(Integer wish, String start_no, Integer gold, Integer silver) {
		if(wish < 0 || StringUtils.isBlank(start_no)) {
			return new Result(false, "数据错误");
		}
		PlayerWish playWish = getPlayerWishBy(start_no);
		playWish.setWish(playWish.getWish()+wish);
		/***system**获取赠送的金币数量*/
		if(gold > 0) {
			
		}
		/***system**获取赠送的银币数量*/
		if(silver > 0) {
			
		}
		
		return  updatePlayerWish(playWish);
	}

	@Override
	public Result insertPlayerWish(String startNo) {
		boolean success = false;
		if(StringUtils.isNotBlank(startNo)) {
			PlayerWish wish = new PlayerWish();
			wish.setStartNo(startNo);
			wish.setWish(0);
			wish.setWishLv(1);
			wish.setCreatePartyNumber(0);
			wish.setBuyNumber(0);
			wish.setJoinNumber(0);
			success = this.insert(wish);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "保存失败");
		}
	}
	
	@Override
	public Result wishJoinParty(String startNo) {
		if(StringUtils.isNotBlank(startNo)) {
			PlayerWish wish = getPlayerWishBy(startNo);
			/**达到一轮最大有效牌局参与次数*/
			if(wish.getJoinNumber() < Constant.REDPACK_NUMBER) {
				wish.setJoinNumber(wish.getJoinNumber()+1);
				return updatePlayerWish(wish);
			} else {
				return new Result(false, "玩家达到一轮红包领取最大牌局数");
			}
		}
		return new Result(false, "参加牌局-更新玩家祝福失败");
	}
	

	@Override
	public Result wishCreateRoom(String startNo) {
		if(StringUtils.isNotBlank(startNo)) {
			PlayerWish wish = getPlayerWishBy(startNo);
			wish.setCreatePartyNumber(wish.getCreatePartyNumber()+1);
			return updatePlayerWish(wish);
		}
		return new Result(false, "创建房间-更新玩家祝福失败");
	}
	
	@Override
	public Result wishOrder(SystemOrder order) {
		if(order != null) {
			PlayerWish wish = getPlayerWishBy(order.getStartNo());
			wish.setBuyNumber(wish.getBuyNumber()+order.getRoomcardAmount());
			return updatePlayerWish(wish);
		}
		return new Result(false, "购买订单-更新玩家祝福失败");
	}

	@Override
	public Result updatePlayerWish(PlayerWish wish) {
		Integer playerwish = wish.getWish();
		/**设置玩家祝福值等级*/
		if(playerwish < 20) {
			wish.setWishLv(Constant.WISHLV_1);
		} else if(playerwish >= 20 && playerwish < 40) {
			wish.setWishLv(Constant.WISHLV_2);
		} else if(playerwish >= 40 && playerwish < 60) {
			wish.setWishLv(Constant.WISHLV_3);
		} else if(playerwish >= 60 && playerwish < 80) {
			wish.setWishLv(Constant.WISHLV_4);
		} else if(playerwish >= 80 && playerwish < 100) {
			wish.setWishLv(Constant.WISHLV_5);
		} else if(playerwish == 100) {
			wish.setWishLv(Constant.WISHLV_6);
		}
		boolean success = this.updateById(wish);
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "更新失败");
		}
	}

	@Override
	public PlayerWish getPlayerWishBy(String startNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("start_no", startNo);
		List<PlayerWish> wishList = this.selectByMap(map);
		if(wishList != null && wishList.size() > 0) {
			return wishList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result redPacketWish(String startNo, Integer round) {
		List<RedPacketLog> redList = redPacketService.getRedByRound(startNo, round);
		if(redList != null && redList.size() == Constant.REDPACK_CEILING) {
			PlayerWish wish = getPlayerWishBy(startNo);
			wish.setJoinNumber(0);
			return updatePlayerWish(wish);
		} else {
			return null;
		}
	}

	@Override
	public Result clearWish() {
		int success = playerWishMapper.clearWish();
		if(success >= 0) {
			return new Result("ok");
		} else {
			return new Result(false, "清空玩家祝福值失败");
		}
	}

}
