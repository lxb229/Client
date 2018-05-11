package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.PlayerLuck;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.vo.AccountVo;
import com.wangzhixuan.model.vo.PlayerLuckVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.GameGMUtils;
import com.wangzhixuan.mapper.PlayerLuckMapper;
import com.wangzhixuan.service.IPlayerLuckService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 玩家幸运值 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-18
 */
@Service
public class PlayerLuckServiceImpl extends ServiceImpl<PlayerLuckMapper, PlayerLuck> implements IPlayerLuckService {
	private static Logger logger = LoggerFactory.getLogger(PlayerLuckServiceImpl.class);
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private GameGMUtils gameGMUtils;
	@Autowired
	private PlayerLuckMapper playerLuckMapper;
	@Autowired
	private IPlayerService playerService;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Player> page = new Page<Player>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<PlayerLuckVo> list = playerLuckMapper.selectPlayerLuckVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}
	
	@Override
	public Result insertPlayerLuck(PlayerLuck playerLuck) {
		playerLuck.setLuckStatus(1);
		playerLuck.setLuckRemain(playerLuck.getLuckCount());
		ValidataBean validata = playerLuck.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		Player player = playerService.selectPlayerBy(playerLuck.getStartNo());
		if(player == null) {
			return new Result(false, "玩家明星号错误");
		}
		PlayerLuck luck = this.selectPlayerLuckBy(playerLuck.getStartNo());
		if(luck != null) {
			return new Result(false, "该玩家已有幸运值");
		}
		boolean success = this.insert(playerLuck);
		/**通知游戏服务器*/
		if(success) {
			AccountVo accountVo = new AccountVo();
        	accountVo.setCmd(6);
        	accountVo.setStarNo(player.getStartNo());
        	accountVo.setLuck(playerLuck.getLuck());
        	accountVo.setLuckStart(playerLuck.getLuckStart().getTime());
        	accountVo.setLuckEnd(playerLuck.getLuckEnd().getTime());
        	accountVo.setLuckNum(playerLuck.getLuckCount());
        	return gameGMUtils.accountService(accountVo);
		} else {
			return null;
		}
	}
	
	@Override
	public Result updatePlayerLuck(PlayerLuck playerLuck) {
		playerLuck.setLuckRemain(playerLuck.getLuckCount());
		ValidataBean validata = playerLuck.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		Player player = playerService.selectPlayerBy(playerLuck.getStartNo());
		if(player == null) {
			return new Result(false, "玩家明星号错误");
		}
		boolean success = this.updateById(playerLuck);
		/**通知游戏服务器*/
		if(success) {
			AccountVo accountVo = new AccountVo();
        	accountVo.setCmd(6);
        	accountVo.setStarNo(player.getStartNo());
        	accountVo.setLuck(playerLuck.getLuck());
        	accountVo.setLuckStart(playerLuck.getLuckStart().getTime());
        	accountVo.setLuckEnd(playerLuck.getLuckEnd().getTime());
        	accountVo.setLuckNum(playerLuck.getLuckCount());
        	return gameGMUtils.accountService(accountVo);
		} else {
			return null;
		}
	}
	
	@Override
	public Result deletePlayerLuck(Integer id) {
		PlayerLuck playerLuck = this.selectById(id);
		if(playerLuck != null) {
			/**将一个玩家的幸运值置为无效*/
			playerLuck.setLuckStatus(0);
			boolean success = this.updateById(playerLuck);
			/**通知游戏服务器*/
			if(success) {
				AccountVo accountVo = new AccountVo();
	        	accountVo.setCmd(6);
	        	accountVo.setStarNo(playerLuck.getStartNo());
	        	accountVo.setLuck(playerLuck.getLuck());
	        	accountVo.setLuckStart(playerLuck.getLuckStart().getTime());
	        	accountVo.setLuckEnd(playerLuck.getLuckEnd().getTime());
	        	accountVo.setLuckNum(0);
	        	return gameGMUtils.accountService(accountVo);
			} else {
				return null;
			}
		} else {
			return new Result(false, "查询数据出错");
		}
	}

	@Override
	public void taskUseLuck(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingPlayerLuck(obj);
		if(success != null && success.isSuccess()) {
			task.setTaskStatus(1);
		} else {
			logger.info(success.getMsg());
			task.setTaskStatus(2);
		}
		task.setTaskNum(task.getTaskNum()+1);
		taskService.updateById(task);
		
	}

	@Override
	public Result processingPlayerLuck(JSONObject obj) {
		if(obj == null) {
			return new Result(false,"MQ数据异常!");
		} else {
			Player player = playerService.selectPlayerBy(obj.getString("starNO"));
			if(player == null) {
				return new Result(false,"玩家幸运值，未查询到玩家");
			}
			/**查询玩家对应的幸运值*/
			PlayerLuck playerLuck = this.selectPlayerLuckBy(obj.getString("starNO"));
			if(playerLuck == null) {
				return new Result(false,"玩家幸运值，未查询到幸运值");
			}
			playerLuck.setLuckRemain(playerLuck.getLuckRemain()-1);
			boolean success = this.updateById(playerLuck);
			if(success) {
				return new Result("OK");
			} else {
				return new Result(false, "消耗幸运值失败");
			}
		}
	}

	@Override
	public PlayerLuck selectPlayerLuckBy(String startNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("start_no", startNo);
		List<PlayerLuck> playerList = this.selectByMap(map);
		if(playerList != null && playerList.size() > 0) {
			return playerList.get(0);
		} else {
			return null;
		}
	}
	
}
