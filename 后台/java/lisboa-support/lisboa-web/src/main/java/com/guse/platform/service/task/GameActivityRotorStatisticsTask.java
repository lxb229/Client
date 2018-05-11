package com.guse.platform.service.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.platform.common.utils.DateUtils;
import com.guse.platform.common.utils.JedisUtils;
import com.guse.platform.dao.doudou.GameActivityRotorStatisticsMapper;
import com.guse.platform.entity.doudou.GameActivityRotorStatistics;
import com.guse.platform.utils.redis.RedisKeys;
import com.guse.platform.utils.redis.RedisMapToBean;
/**
 * 大转盘统计入库
 * @author liyang
 *
 */
@Component
public class GameActivityRotorStatisticsTask {
	private static final Logger logger = LoggerFactory.getLogger(GameActivityRotorStatisticsTask.class);
	private static final int dbIndex = 0;
	@Autowired
	private JedisUtils          jedisUtils;
	@Autowired
	private RedisMapToBean rm;
	@Autowired
	private GameActivityRotorStatisticsMapper mapper;
	
	/**
	 * 存储最近三天数据
	 */
	public void save(){
		logger.info("GameActivityRotorStatisticsTask.save()");
		//获取最近三天的时间
		String[] last3DaysDates = DateUtils.last3DaysDates();
		for (int i = 0; i < last3DaysDates.length; i++) {
			String d = last3DaysDates[i];
			insertData(d);
		}
	}
	
	
	public void insertData(String date){
		//当前时间
		Date now = new Date();
		//存储对象
		GameActivityRotorStatistics saveModel  = getGameActivityRotorStatistics(date);
		//logger.info(JSON.toJSONString(operationPlayerNewFirstOnline));
		if(saveModel==null){
			logger.info("实时数据统计，该天{}数据为空！",date);
			return;
		}
		//查询参数
		GameActivityRotorStatistics param = new GameActivityRotorStatistics();
		param.setGarsDate(DateUtils.parseDate(date, DateUtils.DATE_FORMAT_YYYY_MM_DD));
		//先查询date天的统计数据
		List<GameActivityRotorStatistics> dateData = mapper.select(param);
		
		//如果不存在执行插入
		if(CollectionUtils.isEmpty(dateData)){
			//插入数据库
			saveModel.setGarsCreateTime(now);
			Object result = mapper.insert(saveModel);
			logger.info("{}该天数据插入结果:{}",date,result);
		}else{
			//更新
			saveModel.setGarsId(dateData.get(0).getGarsId());
			Object result = mapper.updateByIdSelective(saveModel);
			logger.info("{}该天数据更新结果:{}",date,result);
		}
	}
	
	//redis获取实时数据
	public GameActivityRotorStatistics getGameActivityRotorStatistics(String date){
		//时间key
		String dateKey = DateUtils.formatDate(date, DateUtils.DATE_FORMAT_YYYYMMDD);
		//存储对象
		GameActivityRotorStatistics saveModel  = new GameActivityRotorStatistics();
		//先从redis获取数据
		Map<String, String> redisData = jedisUtils.getmap(dbIndex,RedisKeys.GAME_ACTIVITY_ROTOR_STATISTICS+":"+dateKey);
		
		if(null==redisData || redisData.size()==0){
			logger.info("实时数据统计，该天{}数据为空！",date);
			return null;
		}else{
			 rm.mapToPojo(redisData, saveModel);
		}
		return saveModel;
	}
}
