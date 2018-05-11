package com.guse.stock.netty.handler.dispose.service;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.common.JSONUtils;
import com.guse.stock.dao.IGameDao;
import com.guse.stock.dao.IGameParDao;
import com.guse.stock.dao.IStockDao;
import com.guse.stock.dao.model.Game;
import com.guse.stock.dao.model.GamePar;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: CheckGearsDispose 
* @Description: 检查库存
* @author Fily GUSE
* @date 2017年8月28日 下午5:50:40 
*  
*/
@Component
public class CheckGearsDispose extends AbstractDispose {
	
	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 7;
	
	@Autowired
	IStockDao stockDao;
	@Autowired
	IGameDao gameDao;
	@Autowired
	IGameParDao gameParDao;

	@Override
	public void processing() {
		// 解析业务数据
		JSONObject data = JSONUtils.toJSONObject(msg.getData());
		String game_id = data.getString("game_id");		//游戏ID
		String identify = data.getString("identify");	//面值ID
		Long user_id = data.getLong("user_id");		//用户ID
		
		// 查询用户和登录用户不一致
		if(!handler.userInfo.getUid().equals(user_id)) {
			msg.setError(MessageCode.INVALIDDATA);
			return;
		}
		
		// 查询游戏信息
		Game game = gameDao.findByIdentify(game_id);
		if(game == null) {
			msg.setError(MessageCode.NOGAMEID);
			return ;
		}
		// 查询面值
		GamePar gamePar = gameParDao.findByIdentify(identify);
		if(gamePar == null) {
			msg.setError(MessageCode.NOPERID);
			return;
		}
		
		// 查询库存数
		int amount = stockDao.countStock(user_id, game.getGame_id(), gamePar.getPar_id());
		
		// 设置返回数据
		data.clear();
		data.put("amount", amount);
		msg.setData(JSONUtils.toJSONString(data));
	}

}
