package com.guse.platform.service.doudou.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.dao.doudou.OrderMapper;
import com.guse.platform.service.doudou.OrderService;
import com.guse.platform.vo.doudou.OrderVo;
import com.guse.platform.entity.doudou.Order;
import com.guse.platform.entity.system.Users;

/**
 * order
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, java.lang.Integer> implements OrderService{
	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private OrderMapper  orderMapper;
	@Autowired
	private  PropertyConfigurer configurer;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(orderMapper);
	}

	@Override
	public Result<PageResult<OrderVo>> queryOrderList(OrderVo orderVo, PageBean pageBean, String cmd) throws UnsupportedEncodingException {
		if (pageBean == null) {
			logger.info("获取用户列表失败，pageBean is null ！");
			return new Result<PageResult<OrderVo>>(00000, "获取购买订单列表失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		int start = (pageBean.getPageNo()-1)*pageBean.getPageSize();
		int num = pageBean.getPageSize();
		int sortStyle  = orderVo.getSortStyle() == 0 ? 2 : orderVo.getSortStyle();
		int sortType = orderVo.getSortCondition() == 0 ? 2 : orderVo.getSortCondition();
		int[] match = {0,0,0,0};
		List<Object> matchParam = new ArrayList<>();
		if(orderVo != null) {
			if(StringUtils.isNotBlank(orderVo.getOrderId())){
				match[0] = 1;
				matchParam.add(orderVo.getOrderId());
			}
			if(StringUtils.isNotBlank(orderVo.getStarNO())) {
				match[1] = 1;
				matchParam.add(orderVo.getStarNO());
			}
			if(orderVo.getOrderStart() != null && orderVo.getOrderEnd() != null ) {
				match[2] = 1;
				matchParam.add(orderVo.getOrderStart().getTime());
				matchParam.add(orderVo.getOrderEnd().getTime());
			} else if(orderVo.getOrderStart() != null && orderVo.getOrderEnd() == null ) {
				match[2] = 1;
				matchParam.add(orderVo.getOrderStart().getTime());
				matchParam.add(new Date().getTime());
			} else if(orderVo.getOrderStart() == null && orderVo.getOrderEnd() != null ) {
				try {
					match[2] = 1;
					DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					Date startDate = dateFormat2.parse("2017-11-30 00:00:00");
					matchParam.add(startDate.getTime());
					matchParam.add(orderVo.getOrderEnd().getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				} 
			}
			if(orderVo.getState() != null && orderVo.getState() != 99) {
				match[3] = 1;
				matchParam.add(orderVo.getState());
			}
		}
		String matchStr = URLEncoder.encode(JSON.toJSONString(match), "UTF-8");
		String matchParamStr = URLEncoder.encode(JSON.toJSONString(matchParam), "UTF-8");
		lisboaAddress = lisboaAddress+cmd+"%20"+start+"%20"+num+"%20"+sortStyle+"%20"+sortType+"%20"+matchStr+"%20"+matchParamStr;
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success == 0) {
            	String content = obj.getString("content");
            	JSONObject listObj = JSONObject.parseObject(content);
            	int totalNum = listObj.getIntValue("totalNum");
            	Long count = new Long((long)totalNum);
        		if (count <= 0) {
        			return new Result<PageResult<OrderVo>>(new PageResult<OrderVo>().initNullPage());
        		}
        		
        		List<OrderVo> list = gson.fromJson(listObj.getString("items"), new TypeToken<List<OrderVo>>(){}.getType()); 
        		for (int i = 0; i < list.size(); i++) {
        			OrderVo order = list.get(i);
        			order.setTheCreateTime(new Date(new Long(order.getCreateTime())));
        			order.setTheTransTime(new Date(new Long(order.getTransTime())));
            		list.set(i, order);
            	}
        		PageResult<OrderVo> pageResult = null;
        		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
        		pageResult.setList(list);
        		return new Result<PageResult<OrderVo>>(pageResult);
        		
            } else {
            	return new Result<PageResult<OrderVo>>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<PageResult<OrderVo>>(00000,"返回结果超时!");
        }
	}

	@Override
	public Result<Integer> orderStart(OrderVo orderVo, Users user) {
		if (orderVo == null) {
			logger.info("开始处理订单失败，orderVo is null ！");
			return new Result<Integer>(00000, "开始处理订单失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_order_order_handle_start";
		String orderId = orderVo.getOrderId();
		int transPlayer = user.getUserId(); 
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+orderId+"%20"+transPlayer);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
	       		return new Result<Integer>(success);
           } else {
           		return new Result<Integer>(00000,obj.getString("content")); 
           }
           
       } else {
       	return new Result<Integer>(00000,"返回结果超时!");
       }
	}

	@Override
	public Result<Integer> orderComplete(OrderVo orderVo, Users user) {
		if (orderVo == null) {
			logger.info("订单处理结果失败，orderVo is null ！");
			return new Result<Integer>(00000, "订单处理结果失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_order_order_handle_complete";
		String orderId = orderVo.getOrderId();
		int transPlayer = user.getUserId(); 
		int resu = orderVo.getState();
		String reMarks = orderVo.getReMarks();
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+orderId+"%20"+transPlayer+"%20"+resu+"%20"+reMarks);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
	       		return new Result<Integer>(success);
           } else {
           		return new Result<Integer>(00000,obj.getString("content")); 
           }
           
       } else {
       	return new Result<Integer>(00000,"返回结果超时!");
       }
	}
	
}
