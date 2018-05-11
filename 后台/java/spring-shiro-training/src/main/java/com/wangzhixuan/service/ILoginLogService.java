package com.wangzhixuan.service;

import com.wangzhixuan.model.LoginLog;
import com.wangzhixuan.model.SystemTask;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 玩家登陆日志 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
public interface ILoginLogService extends IService<LoginLog> {

	/**
	 *  登录日志将影响玩家的登录次数、连续登录天数、上次登录时间、2日存留、3日存留、7存留
	 */
	boolean dataAnalysis(LoginLog loginLog);
	/**
	 * 登录日志将影响峰值、在线人数
	 * @param loginLog
	 */
	boolean dayPeak(LoginLog loginLog);
	/**
	 * 新增登录日志
	 * @param loginLog
	 */
	boolean insertLoginLog(LoginLog loginLog);
	/**
	 * 获取连续登录天数数据
	 * @param playId
	 * @param createTime
	 * @return
	 */
	List<Map<String, Object>> getContinuousLogin(String playId, Date createTime);
	
	/**
	 * 处理任务中的玩家数据
	 * @param task
	 */
	void taskLoginLog(SystemTask task);
}
