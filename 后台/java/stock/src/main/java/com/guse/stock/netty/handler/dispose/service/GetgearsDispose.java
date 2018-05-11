package com.guse.stock.netty.handler.dispose.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.common.JSONUtils;
import com.guse.stock.dao.IGameDao;
import com.guse.stock.dao.IGameParDao;
import com.guse.stock.dao.model.Game;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: GetgearsDispose 
* @Description: 获取档位
* @author Fily GUSE
* @date 2017年8月28日 下午5:47:07 
*  
*/
@Component
public class GetgearsDispose extends AbstractDispose {

	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 4;
	
	@Autowired
	IGameDao gameDao;
	@Autowired
	IGameParDao gameParDao;
	
	@Override
	public void processing() {
		// 获取业务参数
		JSONObject data = JSONUtils.toJSONObject(msg.getData());
		String game_id = data.getString("game_id");
		
		// 获取游戏信息
		Game game = gameDao.findByIdentify(game_id);
		if(game == null) {
			msg.setError(MessageCode.NOGAMEID);
			return ;
		}
		// 获取游戏档位信息
		List<Map<String, Object>> list = gameParDao.findByGame(game.getGame_id());
		System.out.println(JSONUtils.toJSONString(list));
		
		// 封装返回结果
		data.clear();
		data.put("game_par", JSONUtils.toJSONString(list));
		msg.setData(data.toString());

	}

}
