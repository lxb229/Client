package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Lottery;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.LotteryMapper;
import com.wangzhixuan.service.ILotteryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 百轮抽奖 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryMapper, Lottery> implements ILotteryService {

	@Autowired
	private LotteryMapper lotteryMapper;
	
	

	@Override
	public List<Integer> getLuckyLottery(Integer awardsLv, int number, Integer receiveNumber, List<Integer> awardList) {
		List<Lottery> lotteryList = getAwardsLvLottery(awardsLv+number);
		Integer residue = getLotteryResidue(lotteryList);
		/**如果当前祝福的剩余百轮抽奖满足抽奖次数刚好满足抽奖次数，直接全部领取*/
		if(residue == receiveNumber) {
			awardList.addAll(randomLottery(lotteryList));
			/**如果当前祝福的剩余百轮抽奖满足抽奖次数，直接在从中进行抽奖*/
		} else if(residue > receiveNumber) {
			/**从这些百轮抽奖中进行抽奖，并且返回抽奖结果*/
			awardList.addAll(randomLottery(lotteryList, receiveNumber));
		} else {
			awardList.addAll(randomLottery(lotteryList));
			/**第10次查询，重置奖池*/
			if(number == 9) {
				setNewLottery();
				getLuckyLottery(1, 0, receiveNumber-residue, awardList);
				/**继续往更低一级抽奖查询*/
			}   else {
				number+=1;
				getLuckyLottery(awardsLv, number, receiveNumber-residue, awardList);
			}
		}
		return awardList;
	}
	
	@Override
	public List<Integer> getAwardLottery(Integer wishLv, Integer type, int number, Integer receiveNumber,
			List<Integer> awardList) {
		List<Lottery> lotteryList = getWishLvLottery(wishLv+type);
		Integer residue = getLotteryResidue(lotteryList);
		/**如果当前祝福的剩余百轮抽奖满足抽奖次数刚好满足抽奖次数，直接全部领取*/
		if(residue == receiveNumber) {
			awardList.addAll(randomLottery(lotteryList));
			/**如果当前祝福的剩余百轮抽奖满足抽奖次数，直接在从中进行抽奖*/
		} else if(residue > receiveNumber) {
			/**从这些百轮抽奖中进行抽奖，并且返回抽奖结果*/
			awardList.addAll(randomLottery(lotteryList, receiveNumber));
		} else {
			awardList.addAll(randomLottery(lotteryList));
			/**第5次查询，重置奖池*/
			if(number == 4) {
				setNewLottery();
				getAwardLottery(wishLv, 0, 0, receiveNumber-residue, awardList);
			/**查询到最低一级抽奖，则开始往上查询抽奖*/
			} else if(wishLv+type == 1) {
				number+=1;
				getAwardLottery(wishLv, 1, number, receiveNumber-residue, awardList);
			/**继续往更低一级抽奖查询*/
			} else if(type <= 0) {
				type-=1;
				number+=1;
				getAwardLottery(wishLv, type, number, receiveNumber-residue, awardList);
			/**继续往更高一级抽奖查询*/
			}  else {
				type+=1;
				number+=1;
				getAwardLottery(wishLv, type, number, receiveNumber-residue, awardList);
			}
		}
		return awardList;
	}
	

	@Override
	public List<Lottery> getWishLvLottery(Integer wishLv) {
		List<Integer> lvList = new ArrayList<>();
		switch (wishLv) {
			case 1:
				lvList.add(8);
				lvList.add(9);
				lvList.add(10);
				break;
			case 2:
				lvList.add(7);
				lvList.add(8);
				lvList.add(9);
				break;
			case 3:
				lvList.add(5);
				lvList.add(6);
				lvList.add(7);
				break;
			case 4:
				lvList.add(4);
				lvList.add(5);
				lvList.add(6);
				break;
			case 5:
				lvList.add(1);
				lvList.add(2);
				lvList.add(3);
				lvList.add(4);
				break;
			default:
				break;
		}
		return getWishLvLottery(lvList);
	}
	
	@Override
	public Result setNewLottery() {
		int success = lotteryMapper.setNewLottery();
		if(success >= 0) {
			return new Result("ok");
		} else {
			return new Result(false, "重置百轮抽奖失败");
		}
	}

	@Override
	public Result emptyLottery(List<Integer> idList) {
		int success = lotteryMapper.emptyLottery(idList);
		if(success >= 0) {
			return new Result("ok");
		} else {
			return new Result(false, "清空指定id集合奖项失败");
		}
	}

	@Override
	public Result updateLottery(List<Integer> lvList) {
		List<Integer> copyList = new ArrayList<>();
		/**先将集合去重*/
		for(int i=0; i<lvList.size(); i++){
            if(!copyList.contains(lvList.get(i))){   //查看新集合中是否有指定的元素，如果没有则加入
            	copyList.add(lvList.get(i));
            }
        }
		int success = 0;
		/**获取每一个奖项出现的次数*/
		for (int i = 0; i < copyList.size(); i++) {
			Integer number = Collections.frequency(lvList, copyList.get(i));
			success = lotteryMapper.lessenLottery(copyList.get(i), number);
		}
		if(success >= 0) {
			return new Result("ok");
		} else {
			return new Result(false, "减少指定等级奖品数量失败");
		}
	}
	
	@Override
	public List<Integer> randomLottery(List<Lottery> list, Integer receiveNumber) {
		List<Integer> randomList = new ArrayList<>();
		for (int i = 0; i < receiveNumber; i++) {
			Random random = new Random();
			Integer randomNo = list.size();
			Integer randomNumber = random.nextInt(randomNo);
			Lottery lottery = list.get(randomNumber);
			if(lottery.getResidue() > 0) {
				randomList.add(list.get(randomNumber).getAwardsLv());
				lottery.setResidue(lottery.getResidue()-1);
				list.set(randomNumber, lottery);
			} else {
				list.remove(randomNumber);
				i--;
			}
		}
		/**更新已经领取的奖项*/
		List<Integer> copyList = randomList;
		updateLottery(copyList);
		return randomList;
	}

	@Override
	public Integer getLotteryResidue(List<Lottery> list) {
		Integer residue = 0;
		if(list != null && list.size() >0) {
			for (int i = 0; i < list.size(); i++) {
				residue += list.get(i).getResidue();
			}
		}
		return residue;
	}

	@Override
	public List<Integer> randomLottery(List<Lottery> list) {
		List<Integer> awardList = new ArrayList<>();
		if(list != null && list.size() >0) {
			List<Integer> emptyList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).getResidue(); j++) {
					awardList.add(list.get(i).getAwardsLv());
				}
				emptyList.add(list.get(i).getId());
			}
			/**清空这些奖项*/
			emptyLottery(emptyList);
		}
		return awardList;
	}
	
	@Override
	public List<Lottery> getWishLvLottery(List<Integer> lvList) {
		return lotteryMapper.getWishLvLottery(lvList);
	}
	
	@Override
	public List<Lottery> getAwardsLvLottery(Integer awardsLv) {
		return lotteryMapper.getAwardsLvLottery(awardsLv);
	}

	@Override
	public Lottery getLvLottery(Integer awardsLv) {
		return lotteryMapper.getLvLottery(awardsLv);
	}

}
