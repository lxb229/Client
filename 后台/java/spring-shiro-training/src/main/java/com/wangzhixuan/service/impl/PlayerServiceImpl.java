package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.LoginLog;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.vo.PlayerVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.BeanUtils;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.commons.utils.StringUtils;
import com.wangzhixuan.mapper.PlayerMapper;
import com.wangzhixuan.service.ILoginLogService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {
	private static Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
	@Autowired
	private ILoginLogService loginLogService;
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private PropertyConfigurer configurer;
	private Gson gson = new Gson();
	
	
	@Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Player> list = playerMapper.selectPlayerPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
        
    }
	
	@Override
	public void taskPlayer(SystemTask task) {
		String content = "["+task.getJsonContent()+"]";
		List<PlayerVo> taskList = gson.fromJson(content, new TypeToken<List<PlayerVo>>(){}.getType());
		if(taskList != null && taskList.size() > 0 ) {
			Player player = new Player();
			BeanUtils.copyProperties(taskList.get(0), player);
			JSONObject json = JSONObject.parseObject(task.getJsonContent());
			Player systemPlayer = this.selectPlayerBy(player.getPlayId());
				
			boolean success = false;
			if(systemPlayer != null) {
				systemPlayer.setPlayStatus(player.getPlayStatus());
				systemPlayer.setPlayNick(player.getPlayNick());
				success = this.updateById(systemPlayer);
			} else {
				player.setCreateTime(new Date(new Long(json.getLongValue("createTime"))));
				success = this.insert(player);
				// 玩家注册时同时生成一条登录日志
				LoginLog loginLog = new LoginLog();
				loginLog.setPlayerId(player.getPlayId());
				loginLog.setLoginType(1);
				loginLog.setCreateTime(player.getCreateTime());
				success = loginLogService.insertLoginLog(loginLog);
			}
			if(success) {
				task.setTaskStatus(1);
			} else {
				task.setTaskStatus(2);
			}
			task.setTaskNum(task.getTaskNum()+1);
			taskService.updateById(task);
		}
	}

	@Override
	public Result deletePlayer(Player player) {
		Result res = new Result();
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_mall_set_proxy_list";
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+player.getPlayId()+"20"+player.getPlayStatus());
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
           } else {
        	   	res.setMsg(obj.getString("content"));
           		return res; 
           }
           
       } else {
    	   res.setMsg("返回结果超时!");
    	   return res;
       }
		boolean updateResult = this.updateById(player);
		res.setSuccess(updateResult);
		res.setMsg("操作成功");
 	   	return res;
	}

	@Override
	public Player selectPlayerBy(String playerId) {
		Map<String, Object> map = new HashMap<>();
		map.put("play_id", playerId);
		List<Player> playerList = this.selectByMap(map);
		if(playerList != null && playerList.size() > 0) {
			return playerList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> selectPlayer() {
		Map<String, Object> map = playerMapper.selectPlayer();
		BigDecimal allPlayer = new BigDecimal(map.get("allPlayer").toString());
		/*二日存留率*/
		BigDecimal twoDays = new BigDecimal(map.get("twoDays").toString());
		if(twoDays.compareTo(new BigDecimal(0)) == 1) {
			BigDecimal twoDayRate = twoDays.multiply(new BigDecimal(100)).divide(allPlayer, 2, BigDecimal.ROUND_HALF_UP);
			map.put("twoDayRate", twoDayRate);
		} else {
			map.put("twoDayRate", "0.00");
		}
		/*三日存留率*/
		BigDecimal threeDays = new BigDecimal(map.get("threeDays").toString());
		if(threeDays.compareTo(new BigDecimal(0)) == 1) {
			BigDecimal threeDayRate = threeDays.multiply(new BigDecimal(100)).divide(allPlayer, 2, BigDecimal.ROUND_HALF_UP);
			map.put("threeDayRate", threeDayRate);
		} else {
			map.put("threeDayRate", "0.00");
		}
		/*七日存留率*/
		BigDecimal sevenDays = new BigDecimal(map.get("sevenDays").toString());
		if(threeDays.compareTo(new BigDecimal(0)) == 1) {
			BigDecimal sevenDayRate = sevenDays.multiply(new BigDecimal(100)).divide(allPlayer, 2, BigDecimal.ROUND_HALF_UP);
			map.put("sevenDayRate", sevenDayRate);
		} else {
			map.put("sevenDayRate", "0.00");
		}
		
		return map;
	}

}
