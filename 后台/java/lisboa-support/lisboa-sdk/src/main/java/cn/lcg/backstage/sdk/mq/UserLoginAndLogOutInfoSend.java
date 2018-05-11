package cn.lcg.backstage.sdk.mq;

import com.guse.platform.utils.UserLoginAndLogOutInfo;

/**
 * 用户登录登出明细消息发送类
 * @author yanhua
 */
public class UserLoginAndLogOutInfoSend  extends SendDataBase<UserLoginAndLogOutInfo> {

	public UserLoginAndLogOutInfoSend() {
		queueName = "User_Login_And_Logout_Info";
		modelName = "用户登录登出明细";
		init();
	}

}
