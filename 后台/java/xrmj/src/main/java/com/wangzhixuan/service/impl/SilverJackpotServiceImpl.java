package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerLottery;
import com.wangzhixuan.model.SilverJackpot;
import com.wangzhixuan.model.vo.LotteryVo;
import com.wangzhixuan.model.vo.LuckVo;
import com.wangzhixuan.model.vo.PlayerLotteryVo;
import com.wangzhixuan.model.vo.RewareSilverItem;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.SilverJackpotMapper;
import com.wangzhixuan.service.ICommodityService;
import com.wangzhixuan.service.IPlayerLotteryService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.service.ISilverJackpotService;
import com.wangzhixuan.service.IWarehouseOutService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银币抽奖奖池 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
@Service
public class SilverJackpotServiceImpl extends ServiceImpl<SilverJackpotMapper, SilverJackpot> implements ISilverJackpotService {
	private static Logger logger = LoggerFactory.getLogger(SilverJackpotServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private SilverJackpotMapper silverJackpotMapper;
	@Autowired
	private ICommodityService commodityService;
	@Autowired
	private IPlayerLotteryService playerLotteryService;
	@Autowired
	private IWarehouseOutService warehouseOutService;
	@Autowired
	private IPlayerMoneyService playerMoneyService;
	@Autowired
	private IPropLogService propLogService;
	/**数据访问操作锁*/
	private Lock _lock = new ReentrantLock();

	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<SilverJackpot> list = silverJackpotMapper.selectSilverJackpotPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	
	@Override
	public Object lottery(Integer cmd, String start_no) {
		Object object = new Object();
		switch (cmd) {
			/**cmd=1获取参与抽奖的物品信息*/
			case 1:
				LotteryVo vo  = getLotteryBy(start_no);
				object = vo;
				break;
				/**cmd=2刷新参与抽奖的物品信息*/
			case 2:
				LotteryVo refreshVo = refreshLotteryBy(start_no);
				object = refreshVo;
				break;
				/**cmd=3抽取奖励*/
			case 3:
				LuckVo luckVo = luckyDraw(start_no);
				object = luckVo;
				break;
				/**cmd=4获取所奖品池物品列表*/
			case 4:
				List<String> commodityList = commodityService.getAllSilverCommodity();
				object = commodityList;
				break;
			default:
				break;
		}
		
		
		return object;
	}
	
	@Override
	public LotteryVo getLotteryBy(String startNo) {
		LotteryVo vo = new LotteryVo();
		vo.setDrawCost(Constant.DRAWCOST);
		vo.setRefshCost(Constant.REFSHCOST);
		PlayerLottery playerLottery = null;
		if(StringUtils.isNotBlank(startNo)) {
			playerLottery = playerLotteryService.getPlayerLotteryBy(startNo);
			/**如果玩家的当前对应抽奖项为，则生成抽奖项*/
			if(playerLottery == null) {
				Result result = playerLotteryService.insertPlayerLottery(startNo);
				if(result != null && result.isSuccess()) {
					/**新增玩家奖项之后，重新再次获取玩家奖项*/
					playerLottery = playerLotteryService.getPlayerLotteryBy(startNo);
				} else {
					logger.info(result.getMsg());
					return null;
				}
			}
			List<PlayerLotteryVo> voList = gson.fromJson(playerLottery.getLotteryJson(), new TypeToken<List<PlayerLotteryVo>>(){}.getType());
			List<RewareSilverItem> itemList = new ArrayList<>();
			for (int i = 0; i < voList.size(); i++) {
				RewareSilverItem item = new RewareSilverItem();
				item.setIndex(voList.get(i).getIndex());
				item.setItemName(voList.get(i).getItemName());
				itemList.add(item);
			}
			vo.setRewareList(itemList);
			return vo;
		}
		return null;
	}
	

	@Override
	public LotteryVo refreshLotteryBy(String startNo) {
		if(StringUtils.isNotBlank(startNo)) {
			_lock.lock();
//			/**扣除银币*/
//			Result result = playerMoneyService.playerUseSilver(startNo, Constant.REFSHCOST);
//			/**生成道具日志*/
//			if(result != null && result.isSuccess()) {
//				result = propLogService.insertPropLogUseSilver(startNo, Constant.REFSHCOST, Constant.SILVER_REFSHCOST);
//			} else {
//				logger.info(result.getMsg());
//				_lock.unlock();
//				return null;
//			}
			Result result = playerLotteryService.refreshPlayerLottery(startNo);
			/**刷新奖项*/
//			if(result != null && result.isSuccess()) {
//				result = playerLotteryService.refreshPlayerLottery(startNo);
//			} else {
//				_lock.unlock();
//				logger.info(result.getMsg());
//				return null;
//			}
			if(result != null && result.isSuccess()) {
				_lock.unlock();
				return getLotteryBy(startNo);
			} else {
				logger.info(result.getMsg());
				_lock.unlock();
				return null;
			}
		}
		return null;
	}
	

	@Override
	public LuckVo luckyDraw(String startNo) {
		_lock.lock();
//		/**扣除银币*/
//		Result result = playerMoneyService.playerUseSilver(startNo, Constant.DRAWCOST);
//		/**生成道具日志*/
//		if(result != null && result.isSuccess()) {
//			result = propLogService.insertPropLogUseSilver(startNo, Constant.DRAWCOST, Constant.SILVER_DRAWCOST);
//		} else {
//			logger.info(result.getMsg());
//			_lock.unlock();
//			return null;
//		}
		Result result = null;
		/**获取当前银币奖池剩余*/
		List<SilverJackpot> list = getSilverJackpotResidue();
		/**如果银币奖池为空，重置银币奖池*/
		if(list == null || list.size() <= 0) {
			result = setSilverJackot();
			if(result != null && result.isSuccess()) {
				list = getSilverJackpotResidue();
			} else {
				logger.info(result.getMsg());
				_lock.unlock();
				return null;
			}
		}
		/**从奖池中抽取一个奖项*/
		Integer awardLv = randomSilverJackpot(list); 
		LuckVo luckVo = warehouseOutService.outputCommodity(startNo, awardLv, new LuckVo());
		
		/**刷新玩家奖池*/
		playerLotteryService.refreshPlayerLottery(startNo);
		LotteryVo vo = getLotteryBy(startNo);
		luckVo.setRefshCost(vo.getRefshCost());
		luckVo.setDrawCost(vo.getDrawCost());
		luckVo.setRewareList(vo.getRewareList());
		_lock.unlock();
		return luckVo;
	}

	@Override
	public Result setSilverJackot() {
		int success = silverJackpotMapper.setSilverJackot();
		if(success >= 0) {
			return new Result("ok");
		} else {
			return new Result(false, "重置银币奖池失败");
		}
	}

	@Override
	public List<SilverJackpot> getSilverJackpotResidue() {
		return silverJackpotMapper.getSilverJackpotResidue();
	}

	@Override
	public Integer randomSilverJackpot(List<SilverJackpot> list) {
		/**获取当前奖池剩余商品寄回*/
		List<Integer> lvList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getResidue(); j++) {
				lvList.add(list.get(i).getAwardLv());
			}
		}
		if(lvList.size() > 0) {
			Random random = new Random();
			Integer randomNo = lvList.size();
			Integer randomNumber = random.nextInt(randomNo);
			Integer awardLv = lvList.get(randomNumber);
			return awardLv;
		}
		return 0;
	}

	@Override
	public void lessenSilverJackpot(Integer lv) {
		/**获取对应奖池*/
		SilverJackpot silverJackpot = getSilverJackpotBy(lv);
		if(silverJackpot != null) {
			if(silverJackpot.getResidue() > 0) {
				silverJackpot.setResidue(silverJackpot.getResidue()-1);
				this.updateById(silverJackpot);
			}
		}
	}

	@Override
	public SilverJackpot getSilverJackpotBy(Integer lv) {
		Map<String, Object> map = new HashMap<>();
		map.put("award_lv", lv);
		List<SilverJackpot> silverJackpotList = this.selectByMap(map);
		if(silverJackpotList != null && silverJackpotList.size() > 0) {
			return silverJackpotList.get(0);
		} else {
			return null;
		}
	}

}
