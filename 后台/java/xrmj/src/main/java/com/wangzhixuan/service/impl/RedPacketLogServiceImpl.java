package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Jackpot;
import com.wangzhixuan.model.Lottery;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerWish;
import com.wangzhixuan.model.RedPacketLog;
import com.wangzhixuan.model.vo.RedPacketLogVo;
import com.wangzhixuan.model.vo.RedPacketVo;
import com.wangzhixuan.commons.base.Constant;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.mapper.RedPacketLogMapper;
import com.wangzhixuan.service.IJackpotService;
import com.wangzhixuan.service.ILotteryService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPlayerWishService;
import com.wangzhixuan.service.IRedPacketLogService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 红包抽奖记录 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-27
 */
@Service
public class RedPacketLogServiceImpl extends ServiceImpl<RedPacketLogMapper, RedPacketLog> implements IRedPacketLogService {

	@Autowired
	private RedPacketLogMapper redPacketMapper;
	@Autowired
	private IPlayerWishService wishService;
	@Autowired
	private IJackpotService jackpotService;
	@Autowired
	private ILotteryService lotteryService;
	@Autowired
	private IPlayerMoneyService playerMoneyService;
	
	//数据访问操作锁
	private Lock _lock = new ReentrantLock();
	
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<RedPacketLogVo> list = redPacketMapper.selectRedPacketLogVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public RedPacketVo alreadyRedPacket(String startNo, int redPacketNo, Integer round, Integer receiveNumber) {
		RedPacketLog redPacket = redPacketMapper.getAlreadyRed(startNo, redPacketNo, round);
		if(redPacket != null) {
			RedPacketVo vo = new RedPacketVo();
			vo.setRedNo(redPacket.getRedPacketNo());
			vo.setMoneyType(redPacket.getType());
			vo.setAmount(redPacket.getAmount());
			vo.setStartNo(redPacket.getStartNo());
			vo.setCreateTime(redPacket.getCreateTime());
			return vo;
		} else {
			return null;
		}
	}
	
	@Override
	public RedPacketVo receiveRedPacket(String startNo, int redPacketNo, Integer round, Integer receiveNumber) {
		/**判断这个红包是否已经生成，只是网络通信失败未领取*/
		RedPacketVo vo = null;
		if(receiveNumber > 0) {
			PlayerWish wish = wishService.getPlayerWishBy(startNo);
			vo = new RedPacketVo();
			vo.setRedNo(redPacketNo);
			vo.setStartNo(startNo);
			vo.setCreateTime(new Date());
			/**领取红包-保底金额*/
			BigDecimal minimum = getMinimum(receiveNumber);
			/**领取红包-众筹金额*/
			BigDecimal crowdfunding = getCrowdfunding(wish, receiveNumber);
			BigDecimal amount = minimum.add(crowdfunding);
			Random random=new Random();
			int maran = random.nextInt(100);
			/**领取的红包金额小于1，必定给银币;领取的红包金额大于于1，50%的几率给银币，50%的几率给金币*/
			if(amount.compareTo(new BigDecimal("1")) != -1 && maran > 50) {
				vo.setMoneyType(Constant.MONEY_SILVER);
				amount = amount.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_DOWN);
				vo.setAmount(amount.intValue());
			} else {
				vo.setMoneyType(Constant.MONEY_GOLD);
				vo.setAmount(amount.intValue());
			}
			/**保底奖池-保底金额直接从保底奖池中扣除*/
			/**众筹奖池-众筹奖池需要待领取数量确定后，由领取数量-保底金额=众筹金额*/
			amount=vo.getMoneyType()==2?new BigDecimal(vo.getAmount()).setScale(4, BigDecimal.ROUND_HALF_DOWN):new BigDecimal(vo.getAmount()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN);
			crowdfunding = amount.subtract(minimum).setScale(4, BigDecimal.ROUND_HALF_DOWN);
			/**生成红包领取日志*/
			RedPacketLog log = new RedPacketLog();
			log.setStartNo(startNo);
			log.setRoundNumber(round);
			log.setRedPacketNo(redPacketNo);
			log.setLotteryNumber(receiveNumber);
			log.setMinimumAmount(minimum);
			log.setCrowdfundingAmount(crowdfunding);
			log.setType(vo.getMoneyType());
			log.setAmount(vo.getAmount());
			log.setWish(wish.getWish());
			log.setCreateTime(vo.getCreateTime());
			boolean success  = this.insert(log);
			/**更新奖池*/
			if(success) {
				success = jackpotService.consumeJackpot(minimum, receiveNumber, crowdfunding, vo).isSuccess();
			}
			/**更新玩家货币*/
			if(success) {
				success = playerMoneyService.updatePlayerMoney(vo).isSuccess();
			}
			/**更新玩家祝福值-如果一轮红包领取完，重置玩家有效牌局次数*/
			if(success) {
				success = wishService.redPacketWish(startNo, round).isSuccess();
			}
		}
		return vo;
	}

	@Override
	public BigDecimal getMinimum(int receiveNumber) {
		Jackpot jackpot = null;
		_lock.lock();
		BigDecimal minimum = new BigDecimal("0");
		jackpot = jackpotService.getJackpot();
		if(jackpot.getMinimum().compareTo(new BigDecimal("0")) != 0) {
			minimum = new BigDecimal(jackpot.getGetNumber()).multiply(new BigDecimal(receiveNumber)).divide(jackpot.getMinimum(), 4, BigDecimal.ROUND_HALF_DOWN);
		}
		_lock.unlock();
		return minimum;
	}

	@Override
	public BigDecimal getCrowdfunding(PlayerWish wish, int receiveNumber) {
		Jackpot jackpot = null;
		List<Integer> lotteryList = null;
		BigDecimal crowdfunding = new BigDecimal("0");
		Lottery lottery = null;
		_lock.lock();
		jackpot = jackpotService.getJackpot();
		/**祝福值为100的玩家，另一套抽奖规则*/
		if(wish.getWishLv() == 6) {
			lotteryList = lotteryService.getLuckyLottery(1, 0, receiveNumber, new ArrayList<>());
		} else {
			lotteryList = lotteryService.getAwardLottery(wish.getWishLv(), 0, 0, receiveNumber, new ArrayList<>());
		}
		/**获取本次抽奖可获取概率金额的百分比*/
		if(lotteryList != null && lotteryList.size() > 0) {
			for (int i = 0; i < lotteryList.size(); i++) {
				lottery = lotteryService.getLvLottery(lotteryList.get(i));
				crowdfunding.add(lottery.getPercentage());
			}
		}
		/**计算出本次概率抽奖总的金额*/ 
		crowdfunding = crowdfunding.multiply(new BigDecimal(jackpot.getBonus())).setScale(4, BigDecimal.ROUND_HALF_DOWN);
		_lock.unlock();
		return crowdfunding;
	}

	@Override
	public List<RedPacketLog> getRedByRound(String startNo, Integer round) {
		return redPacketMapper.getRedByRound(startNo, round);
	}

}
