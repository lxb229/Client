package cn.lcg.backstage.sdk.mq;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import cn.lcg.backstage.sdk.config.ActiveMQConfig;

public abstract class SendDataBase<T> implements SendData<T> {

	protected static Logger logger = LoggerFactory.getLogger(SendDataBase.class);
	protected static String clientId;
	protected String modelName = null;
	protected String queueName = null;
	protected Gson gson = new Gson();
	protected ActivemqUtil activemq;

	protected void init() {
		clientId = ActiveMQConfig.getClientID() + "_";
		activemq = new ActivemqUtil(clientId, ActiveMQConfig.getMqURI(), ActiveMQConfig.getUid(),
				ActiveMQConfig.getPwd(), queueName);
	}

	public boolean send(T data) {
		if (data == null) {
			return false;
		}

		try {
			String strData = gson.toJson(data);
			activemq.sendMessage(strData);
			return true;
		} catch (JMSException e) {
			logger.error("发送" + modelName + "数据失败：" + e.getMessage(), e);
			return false;
		}
	}

	public void close() {
		if (activemq != null) {
			activemq.shutdown();
		}
	}
}
