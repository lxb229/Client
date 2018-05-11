package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.SilverJackpot;
import com.wangzhixuan.model.vo.LotteryVo;
import com.wangzhixuan.model.vo.LuckVo;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 银币抽奖奖池 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-02
 */
public interface ISilverJackpotService extends IService<SilverJackpot> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 处理抽奖的各种事件
	 * @return
	 */
	Object lottery(Integer cmd, String start_no);
	
	/**
	 * 获取参与抽奖的物品信息
	 * @param startNo 玩家明星号
	 * @return
	 */
	LotteryVo getLotteryBy(String startNo);
	
	/**
	 * 刷新一个玩家的奖项
	 * @param startNo
	 * @return
	 */
	LotteryVo refreshLotteryBy(String startNo);
	
	/**
	 * 玩家幸运抽奖
	 * @param startNo
	 * @return
	 */
	LuckVo luckyDraw(String startNo);
	
	/**
	 * 重置银币抽奖奖池
	 * @return
	 */
	Result setSilverJackot();
	
	/**
	 * 获取当前银币抽奖奖池剩余
	 * @return
	 */
	List<SilverJackpot> getSilverJackpotResidue();
	
	/**
	 * 从奖池中随机抽取一个奖项
	 * @param list 奖池剩余
	 * @return
	 */
	Integer randomSilverJackpot(List<SilverJackpot> list);
	
	/**
	 * 根据奖项等级获取对应的奖池
	 * @param lv 奖项等级
	 * @return
	 */
	SilverJackpot getSilverJackpotBy(Integer lv);
	
	/**
	 * 减少对应奖项等级的奖池一个数量
	 * @param lv 奖项等级
	 * @return
	 */
	void lessenSilverJackpot(Integer lv);

}
