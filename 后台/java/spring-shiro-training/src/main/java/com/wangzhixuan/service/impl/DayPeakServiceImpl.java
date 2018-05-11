package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.DayPeak;
import com.wangzhixuan.mapper.DayPeakMapper;
import com.wangzhixuan.service.IDayPeakService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 每日峰值表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Service
public class DayPeakServiceImpl extends ServiceImpl<DayPeakMapper, DayPeak> implements IDayPeakService {

	@Autowired
	private DayPeakMapper dayPeakMapper;
	
	@Override
	public DayPeak selectMaxDayPeak() {
		return dayPeakMapper.selectMaxDayPeak();
	}

	@Override
	public DayPeak selectDayPeakByTime(Date createTime) {
		return dayPeakMapper.selectDayPeakByTime(createTime);
	}

	@Override
	public Map<String, Object> selectPeakData() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取平均峰值
		Map<String, Object> peakData = dayPeakMapper.getMeanPeak();
		// 获取最大峰值
		DayPeak maxDayPeak = this.selectMaxDayPeak();
		if(maxDayPeak != null) {
			peakData.put("maxPeak", maxDayPeak.getPeak());
			peakData.put("maxTime", dateFormat.format(maxDayPeak.getPeakTime()));
		} else {
			peakData.put("maxPeak", "无");
			peakData.put("maxTime", "无");
		}
		// 获取昨日峰值
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date time=cal.getTime();
		DayPeak yesterdayPeak = this.selectDayPeakByTime(time);
		if(yesterdayPeak != null) {
			peakData.put("yesterdayPeak", yesterdayPeak.getPeak());
			peakData.put("yesterdayTime", dateFormat.format(yesterdayPeak.getPeakTime()));
		} else {
			peakData.put("yesterdayPeak", "无");
			peakData.put("yesterdayTime", "无");
		} 
		// 获取今日峰值
		DayPeak todayPeak = this.selectDayPeakByTime(new Date());
		if(todayPeak != null) {
			peakData.put("onlineNum", todayPeak.getOnlineNum());
		} else {
			peakData.put("onlineNum", 0);
		}
		
		return peakData;
	}
	
}
