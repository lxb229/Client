package com.guse.stock.netty.handler.dispose.service;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.guse.stock.common.HttpUtil;
import com.guse.stock.common.JSONUtils;
import com.guse.stock.netty.handler.ISession;
import com.guse.stock.netty.handler.MessageCode;
import com.guse.stock.netty.handler.dispose.AbstractDispose;
import com.guse.stock.netty.handler.dispose.UserInfo;

/**
 * @ClassName: LoginDispose
 * @Description: 登录
 * @author Fily GUSE
 * @date 2017年8月28日 下午4:49:40
 * 
 */
@Component
public class LoginDispose extends AbstractDispose {

	/**
	 * @Fields pt : 协议号
	 */
	public Integer pt = 100;

	@Override
	public void loginCheck() {
//		System.out.println("登录接口不用验证登录信息");
	}

	@Override
	public void processing() {

		// 解析业务数据
		JSONObject data = JSONUtils.toJSONObject(msg.getData());
		String username = data.getString("username"); // 用户名
		String password = data.getString("password"); // 密码
		String device_number = data.getString("device_number"); // 设备号
		String version = data.getString("version"); // 版本

		// 调用php登录
		String url = "login";
		JSONObject param = new JSONObject();
		param.put("username", username);
		param.put("password", password);
		param.put("device_number", device_number);
//		param.put("type", version);
		
		try {
			JSONObject json = HttpUtil.phpApi(url, param);
			if(json.getInt("code") != 0) {
				MessageCode code = MessageCode.getMessageCodeByCode(json.getInt("code"));
				msg.setError(code);
				return ;
			}
			// 获取消息参数信息
			json = JSONUtils.toJSONObject(json.getString("data"));
			handler.userInfo = new UserInfo();
			handler.userInfo.uid = json.getLong("uid");
			handler.userInfo.hash = json.getString("hash");
			handler.userInfo.device_number = param.getString("device_number");
			Object pid = json.get("pid");
			if(pid == null || Integer.parseInt(pid.toString()) == 0) {
				pid = handler.userInfo.uid;
			}
			handler.userInfo.pid = Long.parseLong(pid.toString());
			handler.userInfo.name = json.getString("name");
			// 保存全部信息到登录对象
			handler.userInfo.other = json;
			
			// 设置返回数据
			data.clear();
			data.put("uid", handler.userInfo.uid);
			data.put("name", handler.userInfo.name);
			msg.setData(data.toString());
			
			// 更新用户信息到管道
			ISession.updateUser(channel, handler.userInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg.setError(MessageCode.SERVERERROR);
		}
	}

}
