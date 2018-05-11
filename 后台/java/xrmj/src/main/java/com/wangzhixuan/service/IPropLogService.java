package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.model.PropLog;
import com.wangzhixuan.model.SystemOrder;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.RedPacketVo;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public interface IPropLogService extends IService<PropLog> {
	
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 处理任务中的游戏数据
	 * @param task
	 */
	void taskPropLog(SystemTask task);
	
	/**
	 * 处理游戏数据
	 * @param obj 游戏数据对象
	 * @return
	 */
	Result processingPropLog(SystemTask task);
	
	/**
	 * 处理游戏数据-创建房间
	 * @param obj
	 * @return
	 */
	Result createRoom(String startNo);
	
	/**
	 * 处理游戏数据-消耗房卡
	 * @param obj
	 * @return
	 */
	Result useRoomcard(String startNo, String amount, Date createTime);
	
	/**
	 * 处理游戏数据-参与牌局的玩家
	 * @param obj
	 * @return
	 */
	Result joinParty(List<String> playerList);
	
	/**
	 * 根据购买订单生成道具日志
	 * @param order 购买订单
	 * @return
	 */
	Result insertPropLog(SystemOrder order);
	
	/**
	 * 根据领取红包生成道具日志
	 * @param order 购买订单
	 * @return
	 */
	Result insertPropLog(RedPacketVo vo);
	
	/**
	 * 根据游戏消耗生成道具日志
	 * @param startNo 玩家明星号
	 * @param amount 消耗数量
	 * @param moneyType 消耗货币单位
	 * @return
	 */
	Result insertPropLogByGame(String startNo, Integer amount, Integer moneyType, Date time);
	
	/**
	 * 根据捐赠房卡生成道具日志
	 * @param startNo 玩家明星号
	 * @param amount 捐赠数量
	 * @param createTime 捐赠时间
	 * @param type 0=馆主添加 1=成员捐赠
	 * @return
	 */
	Result insertPropLogBydonate(String startNo, Integer amount, Date createTime, Integer type);
	
	/**
	 * 根据银币消耗生成道具日志
	 * @param startNo 玩家明星号
	 * @param amount 消耗数量
	 * @param type 消耗类型 1=刷新抽奖消耗 2=银币抽奖消耗
	 * @return
	 */
	Result insertPropLogUseSilver(String startNo, Integer amount, Integer type);
	/**
	 * 根据金币消耗生成道具日志
	 * @param startNo 玩家明星号
	 * @param amount 消耗数量
	 * @return
	 */
	Result insertPropLogUseGold(String startNo, Integer amount);
	
}
