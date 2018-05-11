package com.guse.platform.service.doudou.impl;


import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
//
import org.springframework.stereotype.Service;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.service.doudou.MQService;
import com.guse.platform.service.doudou.SystemTaskService;
import com.guse.platform.entity.doudou.City;

/**
 * city
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class MQServiceImpl extends BaseServiceImpl<City, java.lang.Integer> implements MQService{

	@Autowired
	private SystemTaskService taskService;
	
	@Override
	public void startConsumer() {
		 Properties properties = new Properties();
	        // 您在控制台创建的 Consumer ID
	        properties.put(PropertyKeyConst.ConsumerId, "CID_OFFILINE");
	        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.AccessKey, "HL1xvpOHB9Lj8r7j");
	        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.SecretKey, "H92GUKOpNEkNYdJrMorWvhSj5BHVKn");
	        // 设置 TCP 接入域名（此处以公共云生产环境为例）
	        properties.put(PropertyKeyConst.ONSAddr,
	          "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
	          // 集群订阅方式 (默认)
	          // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
	          // 广播订阅方式
	          // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
	        Consumer consumer = ONSFactory.createConsumer(properties);
	        // MQ-订阅用户信息					   
	        consumer.subscribe("OFFILINE_USER", "UserMessage", new MessageListener() { //订阅多个Tag
	            public Action consume(Message message, ConsumeContext context) {
	                System.out.println("Receive: " + message);
	            	try {
						String body = new String(message.getBody(),"UTF-8");
			            taskService.operateUser(body);
	            	} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                return Action.CommitMessage;
	            }
	        });
	        // MQ-订阅房卡信息
	        consumer.subscribe("OFFILINE_ROOMCARD", "RoomCardMessage", new MessageListener() { //订阅全部Tag
	        	public Action consume(Message message, ConsumeContext context) {
	                System.out.println("Receive: " + message);
	            	try {
						String body = new String(message.getBody(),"UTF-8");
						taskService.operateRoomCard(body);
	            	} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
	                return Action.CommitMessage;
	            }
	
	        });
	        // MQ-订阅俱乐部信息
	        consumer.subscribe("OFFILINE_CLUB", "ClubMessage", new MessageListener() { //订阅全部Tag
	        	public Action consume(Message message, ConsumeContext context) {
	                System.out.println("Receive: " + message);
	            	try {
						String body = new String(message.getBody(),"UTF-8");
						taskService.operateClub(body);
	            	} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
	                return Action.CommitMessage;
	            }
	
	        });
	        // MQ-订阅俱乐部玩家信息
	        consumer.subscribe("OFFILINE_CLUBUSER", "ClubUserMessage", new MessageListener() { //订阅全部Tag
	        	public Action consume(Message message, ConsumeContext context) {
	                System.out.println("Receive: " + message);
	            	try {
						String body = new String(message.getBody(),"UTF-8");
						taskService.operateClubUser(body);
	            	} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
	                return Action.CommitMessage;
	            }
	
	        });
	        consumer.start();
	        System.out.println("Consumer Started");
	}

}