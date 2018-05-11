package com.guse.four_one_nine.mq;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;

public class ProducerTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //您在控制台创建的Producer ID
        properties.put(PropertyKeyConst.ProducerId, "PID_OFFILINE");
        // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey,"HL1xvpOHB9Lj8r7j");
        // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, "H92GUKOpNEkNYdJrMorWvhSj5BHVKn");
        //设置发送超时时间，单位毫秒
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 设置 TCP 接入域名（此处以公共云生产环境为例）
        properties.put(PropertyKeyConst.ONSAddr,
          "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
        producer.start();
        
        
        //循环发送消息
        for (int i = 1; i < 2; i++){
            
			try {
				Message msg = new Message( //
				    // Message所属的Topic
				    "four-one-nine-user",
				    // Message Tag 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
				    "activity-apply",
				    // Message Body 可以是任何二进制形式的数据， MQ不做任何干预，
				    // 需要Producer与Consumer协商好一致的序列化和反序列化方式
				    " {'activity_id':'22','user_id':'1023222','apply_time':'2017-07-14 11:11:11'}".getBytes("UTF-8"));
				// 设置代表消息的业务关键属性，请尽可能全局唯一。
				// 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发
				// 注意：不设置也不会影响消息正常收发
				msg.setKey("ORDERID_824_" + i);
				// 同步发送消息，只要不抛异常就是成功
				SendResult sendResult = producer.send(msg);
				System.out.println(sendResult);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
        
        // 在应用退出前，销毁Producer对象
        // 注意：如果不销毁也没有问题
        producer.shutdown();
    }
}