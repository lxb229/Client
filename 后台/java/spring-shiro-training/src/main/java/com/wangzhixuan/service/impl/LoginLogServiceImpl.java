package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.DayPeak;
import com.wangzhixuan.model.LoginLog;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.LoginLogVo;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.mapper.LoginLogMapper;
import com.wangzhixuan.service.IDayPeakService;
import com.wangzhixuan.service.ILoginLogService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家登陆日志 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

	@Autowired
	private IPlayerService playerService;
	@Autowired
	private IDayPeakService dayPeakService;
	@Autowired
	private LoginLogMapper loginLogMapper;
	@Autowired
	private ISystemTaskService taskService;
	
	private Gson gson = new Gson();
	
	@Override
	public boolean insertLoginLog(LoginLog loginLog) {
		boolean success = this.insert(loginLog);
		if(success) {
			/**更新玩家的登录次数、连续登录天数、上次登录时间、2日存留、3日存留、7存留*/
			success = this.dataAnalysis(loginLog);
		}
		if(success) {
			/**更新峰值、在线人数*/
			success = this.dayPeak(loginLog);
		}
		return success;
		
	}
	
	@Override
	public boolean dataAnalysis(LoginLog loginLog) {
		Player player = playerService.selectPlayerBy(loginLog.getPlayerId());
		if(player != null) {
			// 如果是登录日志
			if(loginLog.getLoginType() == 1) {
				player.setLoginNum(player.getLoginNum()+1);
				player.setLastLoginTime(loginLog.getCreateTime());
				// 获取连续登录天数
				List<Map<String, Object>> maxDays = getContinuousLogin(player.getPlayId(), null);
				if(maxDays != null && maxDays.size() > 0) {
					int maxday = Integer.parseInt(maxDays.get(0).get("lianxu_days").toString());
					player.setLoginDays(maxday);
				}
				// 获取用户2日、3日、7日留存
				List<Map<String, Object>> keepDays = getContinuousLogin(player.getPlayId(), player.getCreateTime());
				if(keepDays != null && keepDays.size() > 0) {
					int keepday = Integer.parseInt(keepDays.get(0).get("lianxu_days").toString());
					if(keepday >= 2) {
						player.setTwoDaysKeep(1);
					}
					if(keepday >= 3) {
						player.setThreeDaysKeep(1);
					}
					if(keepday >= 7) {
						player.setSevenDaysKeep(1);
					}
				}
			}
			playerService.updateById(player);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean dayPeak(LoginLog loginLog) {
		// 获取今日峰值数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s = sdf.format(loginLog.getCreateTime());
		List<DayPeak> dayPeakList = null;
		DayPeak dayPeak = null;
		try {
			Date date =  sdf.parse(s);
			Map<String, Object> dayPeakMap = new HashMap<>();
			dayPeakMap.put("day_date", date);
			dayPeakList = dayPeakService.selectByMap(dayPeakMap);
			/**已经存在了今日峰值数据*/
			if(dayPeakList != null && dayPeakList.size() > 0) {
				dayPeak = dayPeakList.get(0);
				/**如果为登录日志，在线人数+1，同时判断在线人数是否大于历史峰值*/
				if(loginLog.getLoginType() == 1) {
					dayPeak.setOnlineNum(dayPeak.getOnlineNum()+1);
					if(dayPeak.getOnlineNum() > dayPeak.getPeak()) {
						dayPeak.setPeak(dayPeak.getOnlineNum());
						dayPeak.setPeakTime(loginLog.getCreateTime());
					}
					/**如果为登出日志，在线人数-1*/
				} else {
					dayPeak.setOnlineNum(dayPeak.getOnlineNum()-1);
				}
				return dayPeakService.updateById(dayPeak);
			/**还未生成今日的峰值数据*/
			} else {
				dayPeak = new DayPeak();
				dayPeak.setDayDate(date);
				if(loginLog.getLoginType() == 1) {
					dayPeak.setOnlineNum(1);
					dayPeak.setPeak(1);
					dayPeak.setPeakTime(loginLog.getCreateTime());
				} else {
					dayPeak.setOnlineNum(0);
					dayPeak.setPeak(0);
					dayPeak.setPeakTime(loginLog.getCreateTime());
				}
				return dayPeakService.insert(dayPeak);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<Map<String, Object>> getContinuousLogin(String playId, Date createTime) {
		return loginLogMapper.getContinuousLogin(playId, createTime);
	}

	@Override
	public void taskLoginLog(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<LoginLogVo> taskList = gson.fromJson(content, new TypeToken<List<LoginLogVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			LoginLog loginLog = new LoginLog();
			BeanUtils.copyProperties(taskList.get(0), loginLog);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			loginLog.setCreateTime(new Date(new Long(json.getLongValue("createTime"))));
			boolean success = this.insertLoginLog(loginLog);
			if(success) {
				task.setTaskStatus(1);
			} else {
				task.setTaskStatus(2);
			}
			task.setTaskNum(task.getTaskNum()+1);
			taskService.updateById(task);
		}
	}

}
