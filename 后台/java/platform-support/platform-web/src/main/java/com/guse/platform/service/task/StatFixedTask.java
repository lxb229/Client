package com.guse.platform.service.task;

import org.apache.tools.ant.types.resources.comparators.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.platform.utils.date.DateUtils;
import com.google.gson.Gson;

/**
 * 执行凌晨统计任务
 * @author liyang
 *
 */
@Component
public class StatFixedTask {
	private static final Logger logger = LoggerFactory.getLogger(StatFixedTask.class);
	@Autowired
	private GameActivityRotorStatisticsTask gameActivityRotorStatisticsTask;
	
	/**
	 * 执行任务
	 */
	public void excute() {
		Gson gson = new Gson();
		logger.info("StatFixedTask.excute()任务开始：>>>>开始时间：{}",gson.toJson(new Date()));
		long startDate = System.currentTimeMillis();
		//大转盘统计入库
		gameActivityRotorStatisticsTask.save();
		//结束时间
		logger.info("StatFixedTask.excute()任务结束：>>>>结束时间：{}",gson.toJson(new Date()));
	    Long endDate = System.currentTimeMillis();
	    Float costTime = (endDate - startDate) / 1000f / 60;
	    logger.info("StatFixedTask[" + this.getClass().getName() + ".excute] end run at[{}]",
	    		gson.toJson(new Date()) + " \nCost: " + costTime + "分钟");
	}

}
