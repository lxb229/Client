package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Mahjong;
import com.wangzhixuan.model.MahjongPlayer;
import com.wangzhixuan.model.SystemTask;
import com.wangzhixuan.commons.result.Result;
import com.wangzhixuan.mapper.MahjongPlayerMapper;
import com.wangzhixuan.service.IMahjongPlayerService;
import com.wangzhixuan.service.IMahjongService;
import com.wangzhixuan.service.ISystemTaskService;
import com.alibaba.fastjson.JSONObject;
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
 * 麻将馆玩家 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
@Service
public class MahjongPlayerServiceImpl extends ServiceImpl<MahjongPlayerMapper, MahjongPlayer> implements IMahjongPlayerService {

	private static Logger logger = LoggerFactory.getLogger(MahjongPlayerServiceImpl.class);
	@Autowired
	private ISystemTaskService taskService;
	@Autowired
	private IMahjongService mahjongService;
	
	@Override
	public void taskMahjongPlayer(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getTaskContent());
		Result success = processingMahjongPlayer(obj);
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
	public MahjongPlayer selectMahjongPlayerBy(String mahjongNo, String startNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("mahjong_no", mahjongNo);
		map.put("start_no", startNo);
		List<MahjongPlayer> playerList = this.selectByMap(map);
		if(playerList != null && playerList.size() > 0) {
			return playerList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Result processingMahjongPlayer(JSONObject obj) {
		MahjongPlayer mahjongPlayer = null;
		Mahjong mahjong = null;
		boolean success = false;
		Result result = null;
		if(obj == null) {
			return new Result(false,"数据异常!");
		} else {
			int cmd = obj.getIntValue("cmd");
			switch (cmd) {
				/**成员增加*/ 
				case 1:
					/**设置麻将馆属性*/
					mahjongPlayer = new MahjongPlayer();
					mahjongPlayer.setMahjongNo(obj.getString("mahjong_no"));
					mahjongPlayer.setStartNo(obj.getString("start_no"));
					mahjongPlayer.setCreateTime(obj.getDate("create_time"));
					mahjongPlayer.setType(1);
					mahjongPlayer.setLiveness(0);
					mahjongPlayer.setDonate(0);
					mahjongPlayer.setIntegral(0);
					mahjongPlayer.setStatus(1);
					success = this.insert(mahjongPlayer);
					if(success) {
						result = new Result("ok");
					} else {
						result = new Result(false, "增加麻将馆成员失败");
					}
					/**更新麻将馆人数*/
					mahjong = mahjongService.selectMahjongBy(obj.getString("mahjong_no"));
					if(mahjong != null) {
						mahjong.setPeopleNumber(mahjong.getPeopleNumber()+1);
						mahjongService.updateById(mahjong);
					}
					break;
				/**成员踢出*/ 
				case 2:
					mahjongPlayer = this.selectMahjongPlayerBy(obj.getString("mahjong_no"), obj.getString("start_no"));
					if(mahjongPlayer != null) {
						success = this.deleteById(mahjongPlayer.getId());
						if(success) {
							result = new Result("ok");
						} else {
							result = new Result(false, "踢出麻将馆成员失败");
						}
					} else {
						return new Result(false,"cmd=2,未查询到麻将馆成员");
					}
					/**更新麻将馆人数*/
					mahjong = mahjongService.selectMahjongBy(obj.getString("mahjong_no"));
					if(mahjong != null) {
						mahjong.setPeopleNumber(mahjong.getPeopleNumber()-1);
						mahjongService.updateById(mahjong);
					}
					break;	
				/**成员退出*/ 
				case 3:
					mahjongPlayer = this.selectMahjongPlayerBy(obj.getString("mahjong_no"), obj.getString("start_no"));
					if(mahjongPlayer != null) {
						success = this.deleteById(mahjongPlayer.getId());
						if(success) {
							result = new Result("ok");
						} else {
							result = new Result(false, "麻将馆成员退出失败");
						}
					} else {
						return new Result(false,"cmd=3,未查询到麻将馆成员");
					}
					/**更新麻将馆人数*/
					mahjong = mahjongService.selectMahjongBy(obj.getString("mahjong_no"));
					if(mahjong != null) {
						mahjong.setPeopleNumber(mahjong.getPeopleNumber()-1);
						mahjongService.updateById(mahjong);
					}
					break;	
				default:
					
					break;
			}
		   return result;
		}
	}
	
}
