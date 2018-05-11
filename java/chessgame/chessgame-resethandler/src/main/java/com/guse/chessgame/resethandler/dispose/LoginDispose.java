package com.guse.chessgame.resethandler.dispose;

import com.guse.chessgame.common.util.AesCBC;

import net.sf.json.JSONObject;

/** 
* @ClassName: LoginDispose 
* @Description: 处理登录信息
* @author Fily GUSE
* @date 2017年8月20日 上午12:50:49 
*  
*/
public class LoginDispose extends AbstractDispose {
	

	/**
	 * 根据玩家id，从数据库获取加密key信息
	 */
	@Override
	void processing() {
		
		// 获取登录玩家加密key
		String key = "87473920";
//		// 添加到通道上下文中
//		AttributeKey<String> LOGIN_KEY = AttributeKey.valueOf("netty.channel");
//		PlayerInfo player = new PlayerInfo();
//		
//		AttributeKey<PlayerInfo> attr = channel.parent().attr(PLAYER_ATTR);
//		attr.setIfAbsent(player);
		
		JSONObject obj = new JSONObject();
		obj.put("name", "贤良");
		obj.put("sex", "1");
		obj.put("carNum", 17);
		obj.put("emailNum", 0);
		msg.setData(obj.toString());
		
		try {
			// 返回信息加密
			String data = AesCBC.getInstance().encrypt(obj.toString(), key).replaceAll("[\\s*\t\n\r]", "");
			msg.setData(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
