package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.PlayerLottery;
import com.wangzhixuan.model.vo.PlayerLotteryVo;
import com.wangzhixuan.model.vo.SilverCommodityVo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.JsonUtils;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.PlayerLotteryMapper;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.IPlayerLotteryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class PlayerLotteryServiceImpl extends ServiceImpl<PlayerLotteryMapper, PlayerLottery> implements IPlayerLotteryService {

	@Autowired
	private ICommodityService commodityService;
	@Override
	public PlayerLottery getPlayerLotteryBy(String startNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("start_no", startNo);
		List<PlayerLottery> playerLotteryList = this.selectByMap(map);
		if(playerLotteryList != null && playerLotteryList.size() > 0) {
			return playerLotteryList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result insertPlayerLottery(String startNo) {
		if(StringUtils.isBlank(startNo)) {
			return new Result(false, "玩家明星号为空");
		}
		List<SilverCommodityVo> lotteryList = commodityService.getRandomJackpot();
		if(lotteryList != null && lotteryList.size() > 0) {
			PlayerLottery playerLottery = new PlayerLottery();
			playerLottery.setStartNo(startNo);
			playerLottery.setLotteryJson(setLotteryJson(lotteryList));
			
			boolean success = this.insert(playerLottery);
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "新增玩家奖项失败");
			}
			
		}
		return null;
	}

	@Override
	public Result refreshPlayerLottery(String startNo) {
		if(StringUtils.isBlank(startNo)) {
			return new Result(false, "玩家明星号为空");
		}
		PlayerLottery playerLottery = getPlayerLotteryBy(startNo);
		if(playerLottery == null) {
			return insertPlayerLottery(startNo);
		} else {
			List<SilverCommodityVo> lotteryList = commodityService.getRandomJackpot();
			if(lotteryList != null && lotteryList.size() > 0) {
				playerLottery.setLotteryJson(setLotteryJson(lotteryList));
				boolean success = this.updateById(playerLottery);
				if(success) {
					return new Result("ok");
				} else {
					return new Result(false, "刷新玩家奖项失败");
				}
			}
		}
		return null;
	}

	@Override
	public String setLotteryJson(List<SilverCommodityVo> list) {
		List<PlayerLotteryVo> voList = new ArrayList<>();
		
		/**将list集合混乱排序*/
		Collections.shuffle(list);
		
		PlayerLotteryVo lotteryVo = null;
		for (int i = 0; i < list.size(); i++) {
			lotteryVo = new PlayerLotteryVo();
			lotteryVo.setIndex(i+1);
			lotteryVo.setId(list.get(i).getCommodity());
			lotteryVo.setItemName(list.get(i).getCommodityName());
			lotteryVo.setCommodityLv(list.get(i).getAwardLv());
			voList.add(lotteryVo);
		}
		
		
		return JsonUtils.toJson(voList);
	}
	
}
