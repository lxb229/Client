package com.guse.platform.service.doudou.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.AlipayConfig;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.PayOrderUtil;
import com.guse.platform.dao.doudou.SystemCashMapper;
import com.guse.platform.dao.doudou.SystemOrderMapper;
import com.guse.platform.service.doudou.SystemAgencyService;
import com.guse.platform.service.doudou.SystemOrderService;
import com.guse.platform.entity.doudou.SystemAgency;
import com.guse.platform.entity.doudou.SystemCash;
import com.guse.platform.entity.doudou.SystemOrder;
import com.guse.platform.entity.system.Users;

/**
 * system_order
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemOrderServiceImpl extends BaseServiceImpl<SystemOrder, java.lang.Integer> implements SystemOrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(SystemOrderServiceImpl.class);
	
	@Autowired
	private SystemOrderMapper  systemOrderMapper;
	@Autowired
	private SystemCashMapper  systemCashMapper;
	@Autowired
	private SystemAgencyService agencyService;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemOrderMapper);
	}
	
	@Override
	public Result<Integer> saveOrUpdateOrder(SystemOrder order) {
		ValidataBean validata = order.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != order.getSoId()){
			result = systemOrderMapper.updateByIdSelective(order);
		}else{
			order.setCreateTime(new Date());
			result = systemOrderMapper.insert(order);
		}
		return new Result<Integer>(result);
	}

	@Override
	public String randomOrder() {
	
		List<String> yearList = new ArrayList<String>();
		yearList.add("FK");
		yearList.add("SC");
		yearList.add("JB");
		
		Calendar now = Calendar.getInstance();
		String year = yearList.get(now.get(Calendar.YEAR) - 2017);
		String month = now.get(Calendar.MONTH)+1 > 9 ? Integer.toString(now.get(Calendar.MONTH)+1) : "0"+(now.get(Calendar.MONTH)+1);
		String day = now.get(Calendar.DAY_OF_MONTH) > 9 ? Integer.toString(now.get(Calendar.DAY_OF_MONTH)) : "0"+now.get(Calendar.DAY_OF_MONTH);
		String hour = now.get(Calendar.HOUR_OF_DAY) > 9 ? Integer.toString(now.get(Calendar.HOUR_OF_DAY)) : "0"+now.get(Calendar.HOUR_OF_DAY);
		String minute = now.get(Calendar.MINUTE) > 9 ? Integer.toString(now.get(Calendar.MINUTE)) : "0"+now.get(Calendar.MINUTE);
		String second = now.get(Calendar.SECOND) > 9 ? Integer.toString(now.get(Calendar.SECOND)) : "0"+now.get(Calendar.SECOND);
		Random random=new Random();
		String randomNumber = Integer.toString(random.nextInt(90000)+10000); 
		String orderNum = year+month+day+hour+minute+second+randomNumber;
		return orderNum;
				
	}

	@Override
	public Result<Map<String, Object>> countAmount(SystemOrder order, Users user) {
		// 统计订单数据
		Map<String, Object> orderMap = systemOrderMapper.countOrder(order);
		// 如果登录用户为代理或者管理员统计提现数据
		List<Map<String, Object>> agencyList = systemOrderMapper.getCashList(order);
		agencyList = getAgencyMap( agencyList);
		Map<String, Object> agencyMap = getAgencyCount(agencyList);
		if(user.getRoleId() == 1 || user.getRoleId() == 12) {
			orderMap.put("agencyRole", 1);
			orderMap.put("agencyNum", agencyMap.get("agencyNum"));
			orderMap.put("agencyAmount", agencyMap.get("agencyAmount"));
		} else {
			orderMap.put("agencyRole", 0);
			orderMap.put("agencyNum", 0);
			orderMap.put("agencyAmount", 0);
		}
		return new Result<Map<String, Object>>(orderMap);
	}

	@Override
	public Result<Integer> cash(SystemOrder order,Users user) {
		List<Map<String, Object>> agencyList = systemOrderMapper.getCashList(order);
		agencyList = getAgencyMap( agencyList);
		Integer result = null;
		for (int i = 0; i < agencyList.size(); i++) {
			Map<String, Object> agencyMap = agencyList.get(i);
			SystemCash cash = new SystemCash();
			cash.setCityId(Integer.parseInt(agencyMap.get("id").toString()));
			cash.setScAmount(new BigDecimal(agencyMap.get("agencyAmount").toString()).doubleValue() );
			cash.setScMouth(agencyMap.get("mouth").toString());
			cash.setScState(0);
			cash.setCreateTime(new Date());
			result = systemCashMapper.insert(cash);
			// 更新房卡订单提现状态
			updateCashState(agencyMap.get("mouth").toString(), Integer.parseInt(agencyMap.get("id").toString()));
		}
		return new Result<Integer>(result);
	}

	@Override
	public Double getRadio(SystemAgency agency,Double amount) {
		if(amount < 100000) {
			return agency.getSaLessOne();
		} else if(amount > 100000 && amount < 200000) {
			return agency.getSaOneToTwo();
		} else if(amount > 200000 && amount < 300000) {
			return agency.getSaTwoToThree();
		} else if(amount > 300000 && amount < 400000) {
			return agency.getSaThreeToFour();
		} else if(amount > 400000) {
			return agency.getSaGreaterFoure();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getAgencyMap( List<Map<String, Object>> map) {
		for (int i = 0; i < map.size(); i++) {
			Map<String, Object> agencyMap = map.get(i);
			SystemAgency agency = agencyService.getAgencyForCityId(Integer.parseInt(agencyMap.get("id").toString()));
			if(agency != null) {
				double amount = (double) agencyMap.get("agencyAmount");
				double radio = getRadio(agency, amount);
				BigDecimal agencyAmount = new BigDecimal(amount).multiply(new BigDecimal(radio)).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
				agencyMap.put("agencyAmount", agencyAmount);
			} else {
				agencyMap.put("agencyAmount", "0");
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getAgencyCount(List<Map<String, Object>> map) {
		Map<String, Object> agencyCount = new HashMap<>();
		BigDecimal agencyAmount = new BigDecimal("0");
		for (int i = 0; i < map.size(); i++) {
			agencyAmount = agencyAmount.add(new BigDecimal(map.get(i).get("agencyAmount").toString()));
		}
		agencyCount.put("agencyNum", map.size());
		agencyCount.put("agencyAmount", agencyAmount);
		return agencyCount;
	}

	@Override
	public Result<Integer> updateCashState(String mouth, int cityId) {
		Integer result  = systemOrderMapper.updateCashState(mouth, cityId);
		return new Result<Integer>(result);
	}

	@Override
	public Result<Integer> seachStatus(String orderNo) {
		SystemOrder order = systemOrderMapper.getOrderByNo(orderNo);
		if(order.getPayState() != 1) {
			return new Result<Integer>(100000,"待支付");
		}
		return  new Result<Integer>(order.getPayState());
	}

	@Override
	public Result<String> phoneNotifyUrl(Map<String, String> paramsStr) {
		logger.info("支付宝支付结果通知"+paramsStr);
        try {
            //验证签名
            boolean flag = AlipaySignature.rsaCheckV1(paramsStr, AlipayConfig.alipay_public_key, AlipayConfig.charset, "RSA2");
            if(flag){
                String out_trade_no = paramsStr.get("out_trade_no");   //商户订单号
                String sucess = PayOrderUtil.payOrder(out_trade_no);
                if(StringUtils.isNoneBlank(sucess) && sucess.equals("购买成功")) {
                	return new Result<String>(sucess);
                } else {
                	return new Result<String>(00000,sucess);
                }
            } else {
            	return new Result<String>(00000,"验签失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
		return new Result<String>(00000,"回调错误");
	}

	@Override
	public Result<String> phoneReturnUrl(Map<String, String> paramsStr) {
		return phoneNotifyUrl(paramsStr);
	}

}
