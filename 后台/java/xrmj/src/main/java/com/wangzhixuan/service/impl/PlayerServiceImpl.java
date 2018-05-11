package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.OperatingStatistics;
import com.wangzhixuan.model.Player;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.vo.AccountVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.commons.utils.GameGMUtils;
import com.wangzhixuan.mapper.PlayerMapper;
import com.wangzhixuan.service.IOperatingStatisticsService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPlayerService;
import com.wangzhixuan.service.IPlayerWishService;
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
	private GameGMUtils gameGMUtils;
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IPlayerMoneyService moneyService;
	@Autowired
	private IPlayerWishService wishService;
	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private IOperatingStatisticsService operatingService;
	
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
	public Result banOrEnable(Integer id, Integer status) {
		Player player = this.selectById(id);
        player.setStatus(status);
        boolean success = this.updateById(player);
        /**通知游戏服务器*/
        if(success) {
        	AccountVo accountVo = new AccountVo();
        	accountVo.setCmd(1);
        	accountVo.setStarNo(player.getStartNo());
        	accountVo.setState(player.getStatus());
        	return gameGMUtils.accountService(accountVo);
        }
		return null;
	}
	

	@Override
	public Result updatePlayer(Player player) {
        boolean success = this.updateById(player);
        /**通知游戏服务器*/
        if(success) {
        	player = this.selectById(player.getId());
        	AccountVo accountVo = new AccountVo();
        	accountVo.setCmd(3);
        	accountVo.setStarNo(player.getStartNo());
        	accountVo.setPhone(player.getPhone());
        	return gameGMUtils.accountService(accountVo);
        }
		return null;
	}


	@Override
	public void taskPlayer(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingPlayer(obj);
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
	public Result processingPlayer(JSONObject obj) {
		Player player = null;
		if(obj == null) {
			return new Result(false,"数据异常!");
		} else {
			int cmd = obj.getIntValue("cmd");
			switch (cmd) {
				// 玩家注册
				case 1:
					player = new Player();
					player.setStartNo(obj.getString("start_no"));
					player.setNick(obj.getString("nick"));
					player.setSex(Integer.parseInt(obj.getString("sex")));
					player.setHeadImg(obj.getString("head_img"));
					player.setStatus(Integer.parseInt(obj.getString("status")));
					player.setType(1);
					player.setCreateIp(obj.getString("create_ip"));
					player.setCreateTime(obj.getDate("create_time"));
					break;
				// 玩家信息修改
				case 2:
					player = this.selectPlayerBy(obj.getString("start_no"));
					if(player != null) {
						player.setNick(obj.getString("nick"));
						player.setSex(Integer.parseInt(obj.getString("sex")));
						player.setHeadImg(obj.getString("head_img"));
						player.setStatus(Integer.parseInt(obj.getString("status")));
					} else {
						return new Result(false,"cmd=2,未查询到玩家");
					}
					break;	
				// 玩家绑定手机
				case 3:
					player = this.selectPlayerBy(obj.getString("start_no"));
					if(player != null) {
						player.setPhone(obj.getString("phone"));
					} else {
						return new Result(false,"cmd=3;未查询到玩家");
					}
					break;	
				// 玩家实名认证
				case 4:
					player = this.selectPlayerBy(obj.getString("start_no"));
					if(player != null) {
						player.setRealName(obj.getString("real_name"));
						player.setCardNo(obj.getString("card_no"));
					} else {
						return new Result(false,"cmd=4;未查询到玩家");
					}
					break;	
				default:
					
					break;
			}
		   if(player == null) {
			   return new Result(false,"用户数据异常!");
		   } else {
			   return  this.saveOrUpdatePlayer(player);
		   }
		}
	}

	@Override
	public Player selectPlayerBy(String start_no) {
		Map<String, Object> map = new HashMap<>();
		map.put("start_no", start_no);
		List<Player> playerList = this.selectByMap(map);
		if(playerList != null && playerList.size() > 0) {
			return playerList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result saveOrUpdatePlayer(Player player) {
		ValidataBean validata = player.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		boolean success = false;
		if(player.getId() == null) {
			return this.insertPlayer(player);
		} else {
			success = this.updateById(player);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "处理玩家失败");
		}
	}
	
	@Override
	public Result insertPlayer(Player player) {
		Result result = null;
		boolean success = this.insert(player);
		/**更新运营数据*/
		if(success) {
			OperatingStatistics operating = operatingService.getOperating();
			operating.setPlayerAmount(operating.getPlayerAmount()+1);
			success = operatingService.updateById(operating);
		}
		/**设置玩家的货币*/
		if(success) {
			result = moneyService.insertPlayerMoney(player.getStartNo());
		}
		/**设置玩家的祝福值*/
		if(result != null && result.isSuccess()) {
			result = wishService.insertPlayerWish(player.getStartNo());
		}
		return result;
	}

}
