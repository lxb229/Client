package cn.lcg.backstage.sdk.mq;

import com.guse.platform.utils.UserInfoType;

/**
 * 用户信息消息发送类
 * @author yanhua
 */
public class UserInfoSend extends SendDataBase<UserInfoType> {

	public UserInfoSend() {
		queueName = "Operation_Base_User";
		modelName = "用户信息";
		init();
	}

}
