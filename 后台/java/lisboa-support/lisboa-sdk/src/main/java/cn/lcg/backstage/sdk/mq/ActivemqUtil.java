package cn.lcg.backstage.sdk.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivemqUtil implements ExceptionListener {

	private static Logger logger = LoggerFactory.getLogger(ActivemqUtil.class);

	private String clientID;
	private String brokerUrl;
	private String userName;
	private String password;
	private String queue;
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private MessageListener messageListener;

	public ActivemqUtil(String clientID,String url) {
		this.clientID = clientID;
		this.brokerUrl = url;
		connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);
		((ActiveMQConnectionFactory)connectionFactory).setClientIDPrefix(this.clientID);
		init();
	}
	
	public ActivemqUtil(String clientID,String url,String queue) {
		this.clientID = clientID;
		this.brokerUrl = url;
		this.queue = queue;
		connectionFactory = new ActiveMQConnectionFactory(this.brokerUrl);
		((ActiveMQConnectionFactory)connectionFactory).setClientIDPrefix(this.clientID);
		init();
	}
	
	public ActivemqUtil(String clientID,String url,String user,String pwd) {
		this.clientID = clientID;
		this.brokerUrl = url;
		this.userName = user;
		this.password = pwd;
		connectionFactory = new ActiveMQConnectionFactory(this.userName,this.password,this.brokerUrl);
		((ActiveMQConnectionFactory)connectionFactory).setClientIDPrefix(this.clientID);
		init();
	}
	
	public ActivemqUtil(String clientID,String url,String user,String pwd,String queue) {
		this.clientID = clientID;
		this.brokerUrl = url;
		this.userName = user;
		this.password = pwd;
		this.queue = queue;
		connectionFactory = new ActiveMQConnectionFactory(this.userName,this.password,this.brokerUrl);
		((ActiveMQConnectionFactory)connectionFactory).setClientIDPrefix(this.clientID);
		init();
	}

	// 初始化
	private void init() {
		try {
			// 创建连接
			connection = connectionFactory.createConnection();
			ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
			prefetchPolicy.setQueuePrefetch(1);//activeMQ预取策略。不使用预读模式
			((ActiveMQConnection) connection).setPrefetchPolicy(prefetchPolicy);
			
			connection.setExceptionListener(this);
			connection.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			closeConnection();
		}
	}

	// 关闭会话
	private void closeSession() {
		if (session != null) {
			try {
				session.close();
				session = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 关闭连接
	private void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送消息，发送完成后关闭会话，需要手动关闭连接
	 * @param msg
	 * @throws JMSException
	 */
	public void sendMessage(String msg) throws JMSException{
		sendMessage(this.queue,msg);
	}
	
	/**
	 * 发送消息，发送完成后关闭会话，需要手动关闭连接
	 * @param queue
	 * @param msg
	 * @throws JMSException
	 */
	public void sendMessage(String queue,String msg) throws JMSException{
		try {
			session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
			Destination destination = session.createQueue(queue);
			MessageProducer producer = session.createProducer(destination);    
	        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
	        TextMessage message = session.createTextMessage(msg);  
	        producer.send(message);  
	        //session.commit();
		} finally {
			closeSession();
		}
	}
	
	public Message getMessage(long timeout) {
		return getMessage(this.queue, timeout);
	}

	/**
	 * 主动获取消息，获取后自动关闭会话
	 * @param queue 队列名称
	 * @param timeout 超时时间
	 * @return
	 */
	public Message getMessage(String queue, long timeout) {
		Message message = null;
		try {
			//创建非事物，客户端确认删除模式
			session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
			Destination destination = session.createQueue(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			message = consumer.receive(timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSession();
			//closeConnection();
		}
		return message;
	}

	/**
	 * 被动接收消息（监听模式），消息自动送达，需要手动关闭连接
	 * @param messageListener
	 * @throws Exception
	 */
	public void receiveMessage(MessageListener messageListener) throws JMSException {
		receiveMessage(this.queue, messageListener);
	}

	/**
	 * 被动接收消息（监听模式），消息自动送达，需要手动关闭连接
	 * @param queue 队列名称
	 * @param messageListener
	 * @throws Exception
	 */
	public void receiveMessage(String queue, MessageListener messageListener) throws JMSException {
		try {
			if(session != null) {
				throw new JMSException("已启动监听模式接收数据，不能重复启动！");
			}
			this.messageListener = messageListener;
			// 创建非事物，客户端确认删除模式
			session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
			Destination destination = session.createQueue(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(this.messageListener);
		} catch (JMSException e) {
			logger.error("接收（监听）消息失败：" + e.getMessage(), e);
			closeSession();
			closeConnection();

			throw e;
		}
	}

	/**
	 * 关闭连接
	 */
	public void shutdown(){
		closeSession();
		closeConnection();
	}
	
	@Override
	public void onException(JMSException ex) {
		logger.error("MQ发生错误：" + ex.getMessage(), ex);
	}

}
