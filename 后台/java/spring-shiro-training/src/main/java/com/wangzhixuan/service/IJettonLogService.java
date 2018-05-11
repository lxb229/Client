package com.wangzhixuan.service;

import com.wangzhixuan.model.JettonLog;
import com.wangzhixuan.model.SystemTask;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 筹码日志表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-18
 */
public interface IJettonLogService extends IService<JettonLog> {
	/**
	 * 处理任务中的申请筹码数据
	 * @param task
	 */
	void taskJettonLog(SystemTask task);
	/**
	 * 增加一条申请筹码记录
	 * @param joinRoomLog
	 * @return
	 */
	boolean insertJettonLog(JettonLog jettonLog);
	
	/**
	 * 增加玩家的筹码流水
	 * @param joinRoomLog
	 */
	boolean addPlayerJettonNum(JettonLog jettonLog);
	
	/**
	 * 增加房间的筹码数量
	 * @param joinRoomLog
	 */
	boolean addRoomJettonNum(JettonLog jettonLog);
	
	/**
	 * 增加玩家在房间中的明细
	 * @param joinRoomLog 
	 */
	boolean addPlayerRoom(JettonLog jettonLog);
	
	/**
	 * 获取筹码数据汇总
	 * @return
	 */
	Map<String, Object> selectJettonMap();
}
