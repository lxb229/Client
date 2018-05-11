package com.wangzhixuan.test.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.wangzhixuan.service.ILotteryService;
import com.wangzhixuan.test.base.BaseTest;

/**
 * 用户service
 * @author nbin
 * @date 2017年7月18日 上午10:30:06 
 * @version V1.0
 */
public class LotteryServiceTest extends BaseTest {
	@Autowired
	private ILotteryService lotteryService;
	/*
	 * 分页查询用户
	 */
	@Test
	public void selectLottery(){
		
//		List<Integer> lvList = new ArrayList<>();
//		lvList.add(8);
//		lvList.add(9);
//		lvList.add(10);
//		Result result = lotteryService.emptyLottery(lvList);
//		System.out.println(JSONObject.toJSONString(result));
		
//		List<Integer> awardList = lotteryService.getAwardLottery(5, 0, 0, 30, new ArrayList<>());
		List<Integer> awardList = lotteryService.getLuckyLottery(1, 0, 30, new ArrayList<>());
		System.out.println(awardList.size());
		for (int i = 0; i < awardList.size(); i++) {
			System.out.println(awardList.get(i));
		}
		
//		Result result = lotteryService.setNewLottery();
//		System.out.println(JSONObject.toJSONString(result));
		
//		Lottery lottery = lotteryService.getLvLottery(3);
//
//		System.out.println(JSONObject.toJSONString(lottery));
		
//		List<Integer> lvList = new ArrayList<>();
//		lvList.add(8);
//		lvList.add(9);
//		lvList.add(10);
//		List<Lottery> list = lotteryService.getWishLvLottery(lvList);
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(JSONObject.toJSONString(list.get(i)));
//		}
		
		
//		List<Lottery> list = lotteryService.getNewLottery();
//		
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(JSONObject.toJSONString(list.get(i)));
//		}
//		
//		list = lotteryService.getRemainingLottery();
//		
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(JSONObject.toJSONString(list.get(i)));
//		}
//		
//		Map<String, Object> map = new HashMap<>();
//		List<Lottery> lotteryList = lotteryService.selectByMap(map);
//		
//		for (int i = 0; i < lotteryList.size(); i++) {
//			System.out.println(JSONObject.toJSONString(lotteryList.get(i)));
//		}
	}
	
}
