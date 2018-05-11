package com.guse.four_one_nine.mq;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

public class ConsumerTest {
    public static void main(String[] args) {
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
        consumer.subscribe("OFFILINE_MQ", "TagA||TagB||UserMessage", new MessageListener() { //订阅多个Tag
            public Action consume(Message message, ConsumeContext context) {
            	
                System.out.println("Receive: " + message);
            	try {
					System.out.println("body:"+new String(message.getBody(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                return Action.CommitMessage;
            }
        });
        //订阅另外一个Topic
//        consumer.subscribe("TopicTestMQ-Other", "*", new MessageListener() { //订阅全部Tag
//        	@Override
//        	public Action consume(Message message, ConsumeContext context) {
//                System.out.println("Receive: " + message);
//                return Action.CommitMessage;
//            }
//
//        });
        consumer.start();
        System.out.println("Consumer Started");
    }
}