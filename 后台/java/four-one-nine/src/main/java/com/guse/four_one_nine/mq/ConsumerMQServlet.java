package com.guse.four_one_nine.mq;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;


import com.guse.four_one_nine.app.model.ActivityApply;
import com.guse.four_one_nine.app.model.ActivityComment;
import com.guse.four_one_nine.app.model.ActivityVisit;
import com.guse.four_one_nine.app.model.CashApply;
import com.guse.four_one_nine.app.model.OrderStatus;
import com.guse.four_one_nine.app.model.SensitiveUse;
import com.guse.four_one_nine.app.model.ServerComment;
import com.guse.four_one_nine.app.model.ServerInfo;
import com.guse.four_one_nine.app.model.ServerLike;
import com.guse.four_one_nine.app.model.ServerOrder;
import com.guse.four_one_nine.app.model.ServerUpdate;
import com.guse.four_one_nine.app.model.SettlementInfo;
import com.guse.four_one_nine.app.model.TemplateUse;
import com.guse.four_one_nine.app.model.UserCertification;
import com.guse.four_one_nine.app.model.UserInfo;
import com.guse.four_one_nine.app.model.UserLogin;
import com.guse.four_one_nine.app.model.UserMerchantsCertification;
import com.guse.four_one_nine.app.model.UserUpdate;
import com.guse.four_one_nine.app.service.ActivityApplyService;
import com.guse.four_one_nine.app.service.ActivityCommentService;
import com.guse.four_one_nine.app.service.ActivityVisitService;
import com.guse.four_one_nine.app.service.AppInstalledService;
import com.guse.four_one_nine.app.service.CashApplyService;
import com.guse.four_one_nine.app.service.DayDealCountService;
import com.guse.four_one_nine.app.service.DayUserCountService;
import com.guse.four_one_nine.app.service.LoginLoggerService;
import com.guse.four_one_nine.app.service.OrderSettlementService;
import com.guse.four_one_nine.app.service.SensitiveAppService;
import com.guse.four_one_nine.app.service.ServerAppService;
import com.guse.four_one_nine.app.service.ServerCommentService;
import com.guse.four_one_nine.app.service.ServerLikeService;
import com.guse.four_one_nine.app.service.ServerOrderService;
import com.guse.four_one_nine.app.service.TemplateAppService;
import com.guse.four_one_nine.app.service.UserAppService;
import com.guse.four_one_nine.dao.model.AppInstalled;
import com.guse.four_one_nine.dao.model.LoginLogger;
import com.guse.four_one_nine.dao.model.OrderSettlement;
import com.guse.four_one_nine.dao.model.Sensitive;
import com.guse.four_one_nine.dao.model.Server;
import com.guse.four_one_nine.dao.model.Template;
import com.guse.four_one_nine.dao.model.User;


/** 
* @ClassName: ConsumerMQServlet
* @Description: MQ消息订阅
* @author: wangkai
* @date: 2018年1月9日 下午3:01:04 
*  
*/

public class ConsumerMQServlet implements ApplicationListener<ContextRefreshedEvent>{
	

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		startConsumer();
	}

	public final static Logger logger = LoggerFactory.getLogger(ConsumerMQServlet.class);
	public void startConsumer() {
		Properties properties = new Properties();
        // 您在控制台创建的 Consumer ID
        properties.put(PropertyKeyConst.ConsumerId, "CID_FOURONENINE");
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
        consumer.subscribe("four-one-nine-user", "*", new MessageListener() { //订阅是*的Tag
            public Action consume(Message message, ConsumeContext context) {
            	try {
            		String tag =message.getTag();
					String body = new String(message.getBody(),"UTF-8");
					logger.info("{},{}",tag, body);
					 //JSON格式转换
		            JSONObject obj = JSONObject.parseObject(body);
		            if(tag.equals("user")){
		                // MQ-订阅用户信息		
						operateUser(obj);			   
		            	
		            }else if(tag.equals("user-update")){
				        // MQ-订阅用户信息更新	
						operateUserUpdate(obj);
		            	
		            }else if(tag.equals("user-login")){
				        // MQ-订阅用户登录日志			
						operateUserLogin(obj);
		            	
		            }else if(tag.equals("user-certification")){
				        // MQ-订阅用户认证	
						operateUserCertification(obj);
		            	
		            }else if(tag.equals("user-merchants-certification")){
				        // MQ-订阅卖家认证					   
						operateUserMerchantsCertification(obj);
		            	
		            }else if(tag.equals("sensitive-use")){
				        // MQ-订阅敏感字使用信息					  
						operateSensitiveUse(obj);
		            	
		            }else if(tag.equals("template-use")){
				        // MQ-订阅模板使用信息	
						operateTemplateUse(obj);
		            	
		            }else if(tag.equals("activity-apply")){
				        // MQ-订阅活动报名用户信息	
						operateActivityApply(obj);
		            	
		            }else if(tag.equals("activity-visit")){
				        // MQ-订阅活动访问
						operateActivityVisit(obj);
		            	
		            }else if(tag.equals("activity-comment")){
				        // MQ-订阅活动评价	
						operateActivityComment(obj);
		            	
		            }else if(tag.equals("cash-apply")){
				        // MQ-订阅用户提现申请	
						operateCashApply(obj);
		            	
		            }else if(tag.equals("server-info")){
				        // MQ-订阅服务信息					   
						operateServerInfo(obj);
		            	
		            }else if(tag.equals("server-update")){
				        // MQ-订阅服务更新				   
						operateServerUpdate(obj);
		            	
		            }else if(tag.equals("server-order")){
				        // MQ-订阅服务订单				   
						operateServerOrder(obj);
		            	
		            }else if(tag.equals("order-status")){
				        // MQ-订阅服务订单状态更改					   
						operateOrderStatus(obj);
		            	
		            }else if(tag.equals("server-comment")){
				        // MQ-订阅服务评价					   
						operateServerComment(obj);
		            	
		            }else if(tag.equals("server-like")){
				        // MQ-订阅服务点赞					   
						operateServerLike(obj);
		            	
		            }else if(tag.equals("settlement-info")){
				        // MQ-订阅服务订单结算					   
						operateSettlementInfo(obj);
		            }else if(tag.equals("app-installed")){
				        // MQ-订阅装机量数据					   
		            	operateappInstalled(obj);
		            }
		            
					
            	} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                return Action.CommitMessage;
            }
        });
        
        consumer.start();
	}

	@Autowired 
	UserAppService userService;
	

	@Autowired 
	DayUserCountService countService;
	

	@Autowired 
	DayDealCountService dealCountService;
	
	/**
	 * @Description: 用户信息消费业务处理
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月14日
	
	 */
	public Action operateUser(JSONObject obj) {
		
		User user=new User();
		
		UserInfo userInfo = (UserInfo)JSONObject.toJavaObject(obj, UserInfo.class);
		
		user.setUser_id(userInfo.getId());
		user.setNick_name(userInfo.getNick_name());
		user.setPhone(userInfo.getPhone());
		user.setHead_picture(userInfo.getHead_picture());
		user.setCover_picture(userInfo.getCover_picture());
		user.setSex(userInfo.getSex());
		user.setAge(userInfo.getAge());
		user.setBirth_time(userInfo.getBirthday());
		user.setUser_source(userInfo.getSource());
		user.setIp(userInfo.getIp());
		user.setCity(userInfo.getRegister_site());
		user.setRegiste_time(new Date(userInfo.getRegister_date()));
		

		try {
			
			userService.addUser(user);
			
			countService.userRegister(user);
			
		} catch (Exception e) {
			// TODO: handle exception
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}


	/**
	 * @Description: 用户信息更新消费业务处理
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月14日
	 */
	public Action operateUserUpdate(JSONObject obj) {

		User user=new User();
		
		UserUpdate userUpdate = (UserUpdate)JSONObject.toJavaObject(obj, UserUpdate.class);
		
		user.setUser_id(userUpdate.getId());
		user.setNick_name(userUpdate.getNick_name());
		user.setPhone(userUpdate.getPhone());
		user.setHead_picture(userUpdate.getHead_picture());
		user.setCover_picture(userUpdate.getCover_picture());
		user.setSex(userUpdate.getSex());
		user.setAge(userUpdate.getAge());
		user.setBirth_time(userUpdate.getBirthday());

		try {
			userService.updateUser(user);
		}catch (Exception e){
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}

	@Autowired 
	LoginLoggerService loggerService;

	/**
	 * @Description: 用户登录信息
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月14日
	 */
	public Action operateUserLogin(JSONObject obj) {
		LoginLogger logger =new LoginLogger();
		
		UserLogin login =(UserLogin)JSONObject.toJavaObject(obj, UserLogin.class);
		logger.setUser_id(login.getUser_id());
		logger.setLogin_date(new Date(login.getLogin_date()));
		logger.setLogin_ip(login.getLogin_ip());
		logger.setLogin_device(login.getLogin_device());
		logger.setLogin_site(login.getLogin_site());

		

		try {
			loggerService.addLoginLogger(logger);
			countService.userLogin(logger);
		}catch (Exception e){
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
		
	}


	/**
	 * @Description: 用户认证
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月14日
	 */
	public Action operateUserCertification(JSONObject obj) {
		User user=new User();
		UserCertification certification = (UserCertification)JSONObject.toJavaObject(obj, UserCertification.class);
		user.setReal_certification(1);
		user.setUser_id(certification.getUser_id());
		try {
			userService.userCertification(user);
			countService.userReal(user);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}


	/**
	 * @Description: 用户卖家认证
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月14日
	 */
	public Action operateUserMerchantsCertification(JSONObject obj) {
		User user=new User();
		UserMerchantsCertification merchantsCertification = (UserMerchantsCertification)JSONObject.toJavaObject(obj, UserMerchantsCertification.class);
		user.setSeller_certification(1);
		user.setUser_id(merchantsCertification.getUser_id());
		try {
			userService.userMerchantsCertification(user);
			countService.userSeller(user);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}

	@Autowired 
	SensitiveAppService sensitiveService;

	/**
	 * @Description: 敏感字使用
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月14日
	 */
	public Action operateSensitiveUse(JSONObject obj) {
		Sensitive sensitive = new Sensitive();
		SensitiveUse sensitiveUse = (SensitiveUse)JSONObject.toJavaObject(obj, SensitiveUse.class);
		sensitive.setId(sensitiveUse.getId());
		
		try {
			sensitiveService.SensitiveCount(sensitive);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}

	@Autowired 
	TemplateAppService templateService;

	/**
	 * @Description: 模板统计
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateTemplateUse(JSONObject obj) {
		Template template = new Template();
		
		TemplateUse templateUse=(TemplateUse)JSONObject.toJavaObject(obj,TemplateUse.class);
		template.setId(templateUse.getId());
		template.setUpdate_time(new Date(templateUse.getTime()));
		templateService.templateCount(template);
		try {
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}

	@Autowired
	ActivityApplyService activityApplyService;
	/**
	 * @Description: 活动报名
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateActivityApply(JSONObject obj) {
		com.guse.four_one_nine.dao.model.ActivityApply apply = new com.guse.four_one_nine.dao.model.ActivityApply();
		ActivityApply applyActivityApply =(ActivityApply)JSONObject.toJavaObject(obj,ActivityApply.class);
		
		apply.setActivity_id(applyActivityApply.getActivity_id());
		apply.setUser_id(applyActivityApply.getUser_id());
		apply.setApply_time(new Date(applyActivityApply.getApply_time()));
		
		try {
			activityApplyService.addActivityApply(apply);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}


	@Autowired
	ActivityVisitService activityVisitService;
	/**
	 * @Description: 活动访问
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateActivityVisit(JSONObject obj) {

		com.guse.four_one_nine.dao.model.ActivityVisit visit = new com.guse.four_one_nine.dao.model.ActivityVisit();
		ActivityVisit activityVisit =(ActivityVisit)JSONObject.toJavaObject(obj,ActivityVisit.class);
		
		visit.setActivity_id(activityVisit.getActivity_id());
		visit.setUser_id(activityVisit.getUser_id());
		visit.setVisit_time(new Date(activityVisit.getVisit_time()));
		
		try {
			activityVisitService.addActivityVisit(visit);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}


	@Autowired
	ActivityCommentService activityCommentService;
	/**
	 * @Description: 活动评价
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateActivityComment(JSONObject obj) {

		com.guse.four_one_nine.dao.model.ActivityComment comment = new com.guse.four_one_nine.dao.model.ActivityComment();
		ActivityComment activityComment =(ActivityComment)JSONObject.toJavaObject(obj,ActivityComment.class);
		
		comment.setActivity_id(activityComment.getActivity_id());
		comment.setUser_id(activityComment.getUser_id());
		comment.setContent(activityComment.getContent());
		comment.setComment_time(new Date(activityComment.getComment_time()));
		
		try {
			activityCommentService.addActivityApply(comment);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}
	
	@Autowired
	CashApplyService applyService;
	/**
	 * @Description: 提现申请
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateCashApply(JSONObject obj) {
		
		com.guse.four_one_nine.dao.model.CashApply apply = new com.guse.four_one_nine.dao.model.CashApply();
		CashApply cashApply =(CashApply)JSONObject.toJavaObject(obj,CashApply.class);
		
		apply.setId(cashApply.getCash_id());
		apply.setUser_id(cashApply.getUser_id());
		apply.setMoney(cashApply.getMoney());
		apply.setPhone(cashApply.getPhone());
		apply.setAccount_type(cashApply.getAccount_type());
		apply.setAccount(cashApply.getAccount());
		apply.setApply_time(new Date(cashApply.getApply_time()));

		try {
			applyService.addCashApply(apply);
			dealCountService.cashCount(apply);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}

	@Autowired
	ServerAppService appService;
	/**
	 * @Description: TODO
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateServerInfo(JSONObject obj) {
		Server server = new Server();
		ServerInfo serverInfo = (ServerInfo)JSONObject.toJavaObject(obj, ServerInfo.class);
		
		server.setId(serverInfo.getServer_id());
		server.setPublish_user(serverInfo.getUser_id());
		server.setName(serverInfo.getName());
		server.setClassify_id(serverInfo.getClassify_id());
		server.setPrice(serverInfo.getPrice());
		server.setUnit(serverInfo.getUnit());
		server.setRemarks(serverInfo.getDescribe());
		server.setPicture(serverInfo.getPicture());
		server.setPublish_time(new Date(serverInfo.getPublish_time()));
		
		try {
			appService.addServer(server);
			dealCountService.serverCount(server);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;

	}


	/**
	 * @Description: 服务更新
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateServerUpdate(JSONObject obj) {
		Server server = new Server();
		ServerUpdate serverInfo = (ServerUpdate)JSONObject.toJavaObject(obj, ServerUpdate.class);
		
		server.setId(serverInfo.getServer_id());
		server.setPublish_user(serverInfo.getUser_id());
		server.setName(serverInfo.getName());
		server.setClassify_id(serverInfo.getClassify_id());
		server.setPrice(serverInfo.getPrice());
		server.setUnit(serverInfo.getUnit());
		server.setRemarks(serverInfo.getDescribe());
		server.setPicture(serverInfo.getPicture());
		server.setStatus(serverInfo.getStatus());
		
		try {
			appService.addServer(server);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
		
	}

	@Autowired
	ServerOrderService orderService;
	/**
	 * @Description: 服务订单
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateServerOrder(JSONObject obj) {
		com.guse.four_one_nine.dao.model.ServerOrder order = new com.guse.four_one_nine.dao.model.ServerOrder();
		ServerOrder serverOrder =(ServerOrder)JSONObject.toJavaObject(obj,ServerOrder.class);

		order.setId(serverOrder.getOrder_id());
		order.setServer_id(serverOrder.getServer_id());
		order.setUnion_id(serverOrder.getUnion_id());
		order.setPay_type(serverOrder.getPay_type());
		order.setBuy_user(serverOrder.getUser_id());
		order.setBuy_num(serverOrder.getBuy_num());
		order.setTotal(serverOrder.getTotal());
		order.setTip_money(serverOrder.getTip_money());
		order.setBuy_time(new Date(serverOrder.getBuy_time()));
		
		try {
			orderService.addServerOrder(order);
			dealCountService.countOrder(order);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
		
	}


	/**
	 * @Description: 订单变更
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateOrderStatus(JSONObject obj) {
		com.guse.four_one_nine.dao.model.ServerOrder order = new com.guse.four_one_nine.dao.model.ServerOrder();
		OrderStatus orderStatus = (OrderStatus)JSONObject.toJavaObject(obj,OrderStatus.class);
		
		order.setId(orderStatus.getOrder_id());
		order.setStatus(orderStatus.getAction());
		order.setFinish_time(new Date(orderStatus.getAlert_time()));
		try {
			orderService.updateServerOrder(order);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
		
	}

	@Autowired
	ServerCommentService commentService;
	/**
	 * @Description: 服务评价
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateServerComment(JSONObject obj) {
		com.guse.four_one_nine.dao.model.ServerComment serverComment = new com.guse.four_one_nine.dao.model.ServerComment();
		ServerComment comment = (ServerComment)JSONObject.toJavaObject(obj,ServerComment.class);
		serverComment.setServer_id(comment.getService_id());
		serverComment.setComment_user(comment.getUser_id());
		serverComment.setContent(comment.getContent());
		serverComment.setComment_time(new Date(comment.getComment_time()));
		try {
			commentService.addServerApply(serverComment);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}
	
	@Autowired
	ServerLikeService likeService;

	/**
	 * @Description: 服务点赞
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateServerLike(JSONObject obj) {

		com.guse.four_one_nine.dao.model.ServerLike serverLike = new com.guse.four_one_nine.dao.model.ServerLike();
		ServerLike like = (ServerLike)JSONObject.toJavaObject(obj,ServerLike.class);
		serverLike.setServer_id(like.getService_id());
		serverLike.setLike_user(like.getUser_id());
		serverLike.setLike_time(new Date(like.getLike_time()));
		
		try {
			likeService.addServerLike(serverLike);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}
	@Autowired
	OrderSettlementService orderSettlementService;
	/**
	 * @Description: 服务订单结算
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年1月15日
	 */
	public Action operateSettlementInfo(JSONObject obj) {
		OrderSettlement orderSettlement = new OrderSettlement();
		SettlementInfo settlementInfo = (SettlementInfo)JSONObject.toJavaObject(obj,SettlementInfo.class);
		
		orderSettlement.setOrder_id(settlementInfo.getOrder_id());
		orderSettlement.setSeller_income(settlementInfo.getSeller_income());
		orderSettlement.setUnion_id(settlementInfo.getUnion_id());
		orderSettlement.setClo_user(settlementInfo.getUnion_clo_id());
		orderSettlement.setClo_income(settlementInfo.getClo_income());
		orderSettlement.setSettlement_time(new Date(settlementInfo.getSettlement_time()));

		try {
			orderSettlementService.addOrderSettlement(orderSettlement);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}
	 
	@Autowired
	AppInstalledService appInstalledService;

	/**
	 * @Description: 装机量
	 * @param @param obj   
	 * @return void  
	 * @throws
	 * @author wangkai
	 * @date 2018年2月4日
	 */
	public Action operateappInstalled(JSONObject obj) {

		com.guse.four_one_nine.dao.model.AppInstalled appInstalled = new com.guse.four_one_nine.dao.model.AppInstalled();
		AppInstalled installed = (AppInstalled)JSONObject.toJavaObject(obj,AppInstalled.class);
		appInstalled.setPhone_brand(installed.getPhone_brand());
		appInstalled.setPhone_models(installed.getPhone_models());;
		appInstalled.setIemi(installed.getIemi());;
		appInstalled.setInstalled_time(installed.getInstalled_time());
		
		try {
			appInstalledService.addAppInstalled(appInstalled);
		} catch (Exception e) {
	        return Action.CommitMessage;
		}
        return Action.CommitMessage;
	}
}