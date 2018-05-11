package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Mahjong;
import com.wangzhixuan.model.MahjongPlayer;
import com.wangzhixuan.model.MahjongRound;
import com.wangzhixuan.model.PlayerMoney;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.model.ValidataBean;
import com.wangzhixuan.model.vo.PlayerIntegralVo;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.MahjongMapper;
import com.wangzhixuan.service.IMahjongPlayerService;
import com.wangzhixuan.service.IMahjongRoundService;
import com.wangzhixuan.service.IMahjongService;
import com.wangzhixuan.service.IPlayerMoneyService;
import com.wangzhixuan.service.IPropLogService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 麻将馆 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
@Service
public class MahjongServiceImpl extends ServiceImpl<MahjongMapper, Mahjong> implements IMahjongService {
	
	private static Logger logger = LoggerFactory.getLogger(MahjongServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IMahjongPlayerService mahjongPlayerService;
	@Autowired
	private IPlayerMoneyService playerMoneyService;
	@Autowired
	private IPropLogService propLogService;
	@Autowired
	private IMahjongRoundService mahjongRoundService;
	
	@Override
	public void taskMahjong(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingMahjong(obj);
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
	public void taskMahjongCard(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingMahjongCard(obj);
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
	public void taskMahjongCombat(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingMahjongCombat(obj);
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
	public Result processingMahjong(JSONObject obj) {
		Mahjong mahjong = null;
		MahjongPlayer oldPlayer = null;
		MahjongPlayer newPlayer = null;
		if(obj == null) {
			return new Result(false,"数据异常!");
		} else {
			int cmd = obj.getIntValue("cmd");
			obj = obj.getJSONObject("cmdData");
			switch (cmd) {
				/**新建麻将馆*/ 
				case 1:
					/**设置麻将馆属性*/
					mahjong = new Mahjong();
					mahjong.setMahjongNo(obj.getString("mahjong_no"));
					mahjong.setMahjongName(obj.getString("mahjong_name"));
					mahjong.setMahjongWechat(obj.getString("mahjong_wechat"));
					mahjong.setStartNo(obj.getString("start_no"));
					mahjong.setUsableAmount(obj.getInteger("usable_amount"));
					mahjong.setCreateTime(obj.getDate("create_time"));
					
					mahjong.setLiveness(1);
					mahjong.setPattern(1);
					mahjong.setPeopleNumber(1);
					mahjong.setDonateAmount(0);
					mahjong.setConsumeAmount(0);
					mahjong.setStatus(1);
					mahjong.setRecommend(0);
					
					break;
				/**解散麻将馆*/ 
				case 2:
					mahjong = this.selectMahjongBy(obj.getString("mahjong_no"));
					if(mahjong != null) {
						mahjong.setStatus(0);//麻将馆设置为无效
					} else {
						return new Result(false,"cmd=2,未查询到麻将馆");
					}
					break;	
				/**转让麻将馆*/ 
				case 3:
					mahjong = this.selectMahjongBy(obj.getString("mahjong_no"));
					oldPlayer = mahjongPlayerService.selectMahjongPlayerBy(obj.getString("mahjong_no"), obj.getString("old_start_no"));
					newPlayer = mahjongPlayerService.selectMahjongPlayerBy(obj.getString("mahjong_no"), obj.getString("new_start_no"));
					if(mahjong != null && oldPlayer != null && newPlayer != null) {
						mahjong.setStartNo(obj.getString("new_start_no"));
						oldPlayer.setType(1);
						newPlayer.setType(0);
					} else {
						return new Result(false,"cmd=3;未查询到麻将馆、老馆主、新馆主");
					}
					break;	
				default:
					
					break;
			}
		   if(mahjong == null) {
			   return new Result(false,"用户数据异常!");
		   } else {
			   if(oldPlayer != null && newPlayer != null) {
				   mahjongPlayerService.updateById(oldPlayer);
				   mahjongPlayerService.updateById(newPlayer);
			   }
			   return  this.saveOrUpdateMahjong(mahjong);
		   }
		}
	}


	@Override
	public Result processingMahjongCard(JSONObject obj) {
		String type = "";
		Integer mType = 1;
		if(obj == null) {
			return new Result(false,"数据异常!");
		} else {
			int cmd = obj.getIntValue("cmd");
			switch (cmd) {
				/**馆主增加房卡*/ 
				case 1:
					type = "增加";
					mType = 0;
					break;
				/**成员捐赠房卡*/ 
				case 2:
					type = "捐赠";
					mType = 1;
					break;	
				default:
					
					break;
			}
			boolean success = false;
			Integer amount = obj.getInteger("amount");
			String mahjongNo = obj.getString("mahjong_no");
			String startNo = obj.getString("start_no");
			/**获取玩家货币*/
			PlayerMoney playerMoney = playerMoneyService.getPlayerMoneyBy(obj.getString("start_no"));
			if(playerMoney != null) {
				playerMoney.setRoomCard(playerMoney.getRoomCard()-amount);
				playerMoney.setUseRoomCard(playerMoney.getUseRoomCard()+amount);
				success = playerMoneyService.updateById(playerMoney);
			} else {
				return new Result(false, type+"-未查询到玩家货币");
			}
			/**获取麻将馆成员*/
			MahjongPlayer mahjongPlayer = mahjongPlayerService.selectMahjongPlayerBy(mahjongNo, startNo);
			if(mahjongPlayer != null && success) {
				mahjongPlayer.setDonate(mahjongPlayer.getDonate()+amount);
				success = mahjongPlayerService.updateById(mahjongPlayer);
			} else {
				return new Result(false, type+"-未查询到麻将馆成员");
			}
			/**获取麻将馆*/
			Mahjong mahjong = this.selectMahjongBy(obj.getString("mahjong_no"));
			if(mahjong != null && success) {
				mahjong.setUsableAmount(mahjong.getUsableAmount()+amount);
				mahjong.setDonateAmount(mahjong.getDonateAmount()+amount);
				success = this.updateById(mahjong);
			}
			/**生成道具日志*/
			if(success) {
				return propLogService.insertPropLogBydonate(startNo, amount, obj.getDate("create_time"), mType);
			} else {
				return null;
			}
		}
	}

	@Override
	public Result processingMahjongCombat(JSONObject obj) {
		if(obj == null) {
			return new Result(false,"数据异常!");
		} else {
			boolean success = false;
			Integer gameType = obj.getInteger("game_type");
			String mahjongNo = obj.getString("mahjong_no");
			Integer consume = obj.getInteger("consume");
			Integer liveness = obj.getInteger("liveness");
			/**获取麻将馆*/
			Mahjong mahjong = this.selectMahjongBy(mahjongNo);
			if(mahjong != null && consume != null) {
				/**减少麻将馆房卡*/
				mahjong.setUsableAmount(mahjong.getUsableAmount()-consume);
				mahjong.setConsumeAmount(mahjong.getConsumeAmount()+consume);
				/**更新麻将馆玩法类型轮数*/
				MahjongRound mahjongRound = mahjongRoundService.selectMahjongRoundBy(mahjongNo, gameType);
				if(mahjongRound == null) {
					/**麻将馆该玩法类型轮数为空，新增数据*/
					mahjongRound = new MahjongRound();
					mahjongRound.setMahjongNo(mahjongNo);
					mahjongRound.setGameType(gameType);
					mahjongRound.setRound(1);
					mahjongRoundService.insert(mahjongRound);
				} else {
					mahjongRound.setRound(mahjongRound.getRound()+1);
					mahjongRoundService.updateById(mahjongRound);
				}
				/**获取玩家集合*/
				List<PlayerIntegralVo> voList = gson.fromJson(obj.getString("list"), new TypeToken<List<PlayerIntegralVo>>(){}.getType());
				for (int i = 0; i < voList.size(); i++) {
					/**更新麻将馆玩家*/
					MahjongPlayer mahjongPlayer = mahjongPlayerService.selectMahjongPlayerBy(mahjongNo, voList.get(i).getStart_no());
					mahjongPlayer.setLiveness(mahjongPlayer.getLiveness()+liveness);
					mahjongPlayer.setIntegral(mahjongPlayer.getIntegral()+voList.get(i).getIntegral());
					mahjongPlayerService.updateById(mahjongPlayer);
					/**更新麻将馆活跃度*/
					mahjong.setLiveness(mahjong.getLiveness()+liveness);
				}
				success = this.updateById(mahjong);
			}
			if(success) {
				return new Result("ok");
			} else {
				return new Result(false, "处理麻将馆战斗失败");
			}
		}
	}
	
	@Override
	public Mahjong selectMahjongBy(String mahjongNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("mahjong_no", mahjongNo);
		List<Mahjong> playerList = this.selectByMap(map);
		if(playerList != null && playerList.size() > 0) {
			return playerList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result saveOrUpdateMahjong(Mahjong mahjong) {
		ValidataBean validata = mahjong.validateModel();
		if (!validata.isFlag()) {
			return new Result(false, validata.getMsg());
		}
		boolean success = false;
		if(mahjong.getId() == null) {
			return this.insertMahjong(mahjong);
		} else {
			success = this.updateById(mahjong);
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "处理麻将馆失败");
		}
	}

	@Override
	public Result insertMahjong(Mahjong mahjong) {
		boolean success = this.insert(mahjong);
		if(success) {
			/**生成麻将馆馆主*/
			MahjongPlayer mahjongPlayer = new MahjongPlayer();
			mahjongPlayer.setMahjongNo(mahjong.getMahjongNo());
			mahjongPlayer.setStartNo(mahjong.getStartNo());
			mahjongPlayer.setType(0);
			mahjongPlayer.setLiveness(0);
			mahjongPlayer.setDonate(mahjong.getUsableAmount());
			mahjongPlayer.setIntegral(0);
			mahjongPlayer.setStatus(1);
			mahjongPlayer.setCreateTime(mahjong.getCreateTime());
			success = mahjongPlayerService.insert(mahjongPlayer);
			
		}
		if(success) {
			return new Result("ok");
		} else {
			return new Result(false, "增加麻将馆失败");
		}
	}

}
