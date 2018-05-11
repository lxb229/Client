package com.palmjoys.yf1b.act.event.manager;

import java.util.Properties;

import org.springframework.stereotype.Component;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.palmjoys.yf1b.act.event.resource.MQConfig;

//事件数据发送到MQ队列
@Component
public class EventMQManager {
	@Static
	private Storage<Integer, MQConfig> mqCfgs;
	private Producer _producer = null;
	private boolean bInitProducer = false;
	private long eventDataId = 0;
	
	private boolean initProducer(){
		boolean bOK = false;
		try{
			Properties properties = new Properties();
	        //您在控制台创建的Producer ID
	        properties.put(PropertyKeyConst.ProducerId, mqCfgs.get(1, false).getProducerId());
	        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.AccessKey,  mqCfgs.get(1, false).getAccessKey());
	        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.SecretKey,  mqCfgs.get(1, false).getSecretKey());
	        //设置发送超时时间，单位毫秒
	        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
	        // 设置 TCP 接入域名（此处以公共云生产环境为例）
	        properties.put(PropertyKeyConst.ONSAddr, mqCfgs.get(1, false).getOnSAddr());
	        if(null != _producer){
	        	_producer.shutdown();
	        }
	        _producer = null;
	        
	        _producer = ONSFactory.createProducer(properties);
	        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
	        _producer.start();
	        
			bOK = true;
		}catch(Exception e){
			bOK = false;
		}
		return bOK;
	}
	
	public void resetMqInit(){
		mqCfgs.reload();
		this.bInitProducer = false;
	}
	
	public boolean sendMsg2MQ(String topic, String tag, String content){
		if(bInitProducer == false){
			bInitProducer = initProducer();
		}
		if(bInitProducer == false){
			return false;
		}
		
		boolean bOK = false;
		try{
			Message mqMsg = new Message(topic, tag, content.getBytes("UTF-8"));
			mqMsg.setKey("MQ_DATA_ID_" + eventDataId);
			eventDataId++;
			_producer.send(mqMsg);
			bOK = true;
		}catch(Exception e){
			bOK = false;
		}
		return bOK;
	}
}
