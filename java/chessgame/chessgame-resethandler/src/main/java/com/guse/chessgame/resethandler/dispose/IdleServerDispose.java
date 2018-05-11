package com.guse.chessgame.resethandler.dispose;

import net.sf.json.JSONObject;

public class IdleServerDispose extends AbstractDispose {

	@Override
	void processing() {
		// 收到服务器心跳包
		System.out.println("server idle......");
		System.out.println(JSONObject.fromObject(msg).toString());
		
		// 服务器心跳包不需要返回信息
		if_respond = false;

	}

}
