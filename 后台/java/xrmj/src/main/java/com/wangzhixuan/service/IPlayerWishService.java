package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.PlayerWish;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.SystemTask;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家祝福值 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface IPlayerWishService extends IService<PlayerWish> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 处理任务中的玩家祝福值数据
	 * @param task
	 */
	void taskPlayerWish(SystemTask task);
	
	/**
	 * 处理玩家祝福值
	 * @param obj 玩家祝福值对象
	 * @return
	 */
	Result processingPlayerWish(Integer addWish, String start_no, Integer gold, Integer silver);
	
	/**
	 * 新增玩家祝福值
	 * @param money 玩家祝福值对象
	 * @return
	 */
	Result insertPlayerWish(String startNo);
	/**
	 * 更新玩家祝福值
	 * @param money 玩家祝福值对象
	 * @return
	 */
	Result updatePlayerWish(PlayerWish wish);
	
	/**
	 * 玩家参加牌局，更新玩家祝福值
	 * @param startNo
	 * @return
	 */
	Result wishJoinParty(String startNo);
	
	/**
	 * 玩家创建房间，更新玩家祝福值
	 * @param startNo
	 * @return
	 */
	Result wishCreateRoom(String startNo);
	
	/**
	 * 购买订单之后，更新玩家祝福值
	 * @param order 购买订单
	 * @return
	 */
	Result wishOrder(SystemOrder order);
	
	/**
	 * 根据玩家明星号获取玩家祝福值
	 * @param startNo
	 * @return
	 */
	PlayerWish getPlayerWishBy(String startNo);
	
	/**
	 * 根据红包领取个数判断一轮红包是否领取完成，领取完重置玩家有效牌局次数
	 * @param startNo 玩家明星号
	 * @param round 轮数
	 * @return
	 */
	Result redPacketWish(String startNo, Integer round);
	
	/**
	 * 清空玩家祝福值
	 * @return
	 */
	Result clearWish();
}
