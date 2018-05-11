package com.guse.platform.service.doudou.impl;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alipay.utils.AlipayUtil;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.PayOrderUtil;
import com.guse.platform.dao.doudou.SystemProductMapper;
import com.guse.platform.service.doudou.SystemOrderService;
import com.guse.platform.service.doudou.SystemProductService;
import com.guse.platform.vo.doudou.PayVo;
import com.weixin.entity.WXPayResult;
import com.weixin.paytest.AliPayTest;
import com.weixin.paytest.WeixinPayTest;
import com.weixin.utils.HttpXmlUtils;
import com.weixin.utils.JdomParseXmlUtils;
import com.weixin.utils.WXImageUtil;
import com.weixin.utils.WXSignUtils;
import com.guse.platform.entity.doudou.SystemOrder;
import com.guse.platform.entity.doudou.SystemProduct;
import com.guse.platform.entity.system.Users;

/**
 * system_product
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemProductServiceImpl extends BaseServiceImpl<SystemProduct, java.lang.Integer> implements SystemProductService{
	private static Logger logger = LoggerFactory.getLogger(SystemProductServiceImpl.class);
	@Autowired
	private SystemProductMapper  systemProductMapper;
	
	@Autowired
	private SystemOrderService orderServicer;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemProductMapper);
	}
	

	@Override
	public Result<List<SystemProduct>> queryListProduct(SystemProduct product) {
		List<SystemProduct> productList = systemProductMapper.select(product);
		return new Result<List<SystemProduct>>(productList);
	}

	@Override
	public Result<List<SystemProduct>> queryListStore(SystemProduct product) {
		List<SystemProduct> productList = systemProductMapper.selectStore(product);
		return new Result<List<SystemProduct>>(productList);
	}
	
	@Override
	public Result<Integer> saveOrUpdateProduct(SystemProduct product) {
		ValidataBean validata = product.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != product.getSpId()){
			//更新用户不可以更改登录名
			result = systemProductMapper.updateByIdSelective(product);
		}else{
			//产品名称不能重复
			List<SystemProduct> existRoles = systemProductMapper.getProductByName(product.getSpName());
			if(CollectionUtils.isNotEmpty(existRoles)){
				return new Result<Integer>(00000,"产品已存在！");
			}
			product.setCreateTime(new Date());
			result = systemProductMapper.insert(product);
		}
		return new Result<Integer>(result);
	}

	@Override
	public Result<Integer> deleteProduct(Integer spId) {
		if(null == spId){
			return new Result<Integer>(00000,"删除产品失败，参数异常！");
		}
		return new Result<Integer>(systemProductMapper.deleteById(spId));
	}

	@Override
	public Result<PayVo> buyProduct(Integer productId, Users user) {
		// 获取产品
		SystemProduct product = systemProductMapper.selectById(productId);
		// 生成订单
		SystemOrder order = new SystemOrder();
		order.setCreateTime(new Date());
		order.setPayPrice(product.getSpPrice());
		order.setPayState(0);
		order.setPayType(1);
		order.setProductId(productId);
		order.setSoAmounts(product.getSpPrice());
		order.setSoNo(orderServicer.randomOrder());
		order.setUserId(user.getUserId());
		order.setCashState(0);
		Result<Integer> result = orderServicer.saveOrUpdateOrder(order);
		if (!result.isOk()) {
    		return new Result<PayVo>(00000,result.getErrorMsg());
    	}
		
		PayVo payVo = new PayVo();
		payVo.setOut_trade_no(order.getSoNo());
		payVo.setSubject(product.getSpName());
		payVo.setTotal_fee(product.getSpPrice());
		payVo.setBody(product.getSpName());
		return new Result<PayVo>(payVo);
		 
	}
	
	@Override
	public Result<PayVo> buyProductByWX(Integer productId, Users user) {
		// 获取产品
		SystemProduct product = systemProductMapper.selectById(productId);
		// 生成订单
		SystemOrder order = new SystemOrder();
		order.setCreateTime(new Date());
		order.setPayPrice(product.getSpPrice());
		order.setPayState(0);
		order.setPayType(2);
		order.setProductId(productId);
		order.setSoAmounts(product.getSpPrice());
		order.setSoNo(orderServicer.randomOrder());
		order.setUserId(user.getUserId());
		order.setCashState(0);
		Result<Integer> result = orderServicer.saveOrUpdateOrder(order);
		if (!result.isOk()) {
    		return new Result<PayVo>(00000,result.getErrorMsg());
    	}
		// 微信下订单
		WXImageUtil util = new WXImageUtil();
		int totalAmount = (int) (product.getSpPrice()*100);
		String code_url = util.getWeixinPayUrl(product.getSpName(), product.getSpName(), totalAmount, order.getSoNo());
//		// 获取微信二维码
//		String image_url = util.generateQrcode(code_url);
		
		PayVo payVo = new PayVo();
		payVo.setOut_trade_no(order.getSoNo());
		payVo.setPayWXImage(code_url);
		return new Result<PayVo>(payVo);
		
	}
	
	@Override
	public Result<PayVo> phoneBuyProductByWX(String ip, Integer productId, Users user) {
		// 获取产品
		SystemProduct product = systemProductMapper.selectById(productId);
		// 生成订单
		SystemOrder order = new SystemOrder();
		order.setCreateTime(new Date());
		order.setPayPrice(product.getSpPrice());
		order.setPayState(0);
		order.setPayType(2);
		order.setProductId(productId);
		order.setSoAmounts(product.getSpPrice());
		order.setSoNo(orderServicer.randomOrder());
		order.setUserId(user.getUserId());
		order.setCashState(0);
		Result<Integer> result = orderServicer.saveOrUpdateOrder(order);
		if (!result.isOk()) {
    		return new Result<PayVo>(00000,result.getErrorMsg());
    	}
		
		WeixinPayTest payTest = new WeixinPayTest();
		int totalAmount = (int) (product.getSpPrice()*100);
		String url = payTest.getWeixinPagePayUrl(ip, product.getSpName(), product.getSpName(), totalAmount, order.getSoNo());
		PayVo payVo = new PayVo();
		payVo.setOut_trade_no(order.getSoNo());
		payVo.setPayWXImage(url);
		return new Result<PayVo>(payVo);
	}
	
	@Override
	public Result<PayVo> phoneBuyProductByAli(Integer productId, Users user) {
		// 获取产品
		SystemProduct product = systemProductMapper.selectById(productId);
		// 生成订单
		SystemOrder order = new SystemOrder();
		order.setCreateTime(new Date());
		order.setPayPrice(product.getSpPrice());
		order.setPayState(0);
		order.setPayType(1);
		order.setProductId(productId);
		order.setSoAmounts(product.getSpPrice());
		order.setSoNo(orderServicer.randomOrder());
		order.setUserId(user.getUserId());
		order.setCashState(0);
		Result<Integer> result = orderServicer.saveOrUpdateOrder(order);
		if (!result.isOk()) {
    		return new Result<PayVo>(00000,result.getErrorMsg());
    	}
		
		AliPayTest payTest = new AliPayTest();
		String url = payTest.getAlipayPagePayUrl(product.getSpName(), product.getSpName(), product.getSpPrice().toString(), order.getSoNo());
		PayVo payVo = new PayVo();
		payVo.setOut_trade_no(order.getSoNo());
		payVo.setPayWXImage(url);
		return new Result<PayVo>(payVo);
	}
	

	@Override
	public String WeixinNotifyUrl(String paramsStr) {
		 logger.info("222----[微信回调]接收到的报文---" + paramsStr);
         if (!StringUtils.isEmpty(paramsStr)) {
             WXPayResult wxPayResult = JdomParseXmlUtils.getWXPayResult(paramsStr);
             if ("SUCCESS".equalsIgnoreCase(wxPayResult.getReturn_code())) {
                 SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                 
                 parameters.put("appid", wxPayResult.getAppid());
                 parameters.put("attach", wxPayResult.getAttach());
                 parameters.put("bank_type", wxPayResult.getBank_type());
                 parameters.put("cash_fee", wxPayResult.getCash_fee());
                 parameters.put("device_info", wxPayResult.getDevice_info());
                 parameters.put("fee_type", wxPayResult.getFee_type());
                 parameters.put("is_subscribe", wxPayResult.getIs_subscribe());
                 parameters.put("mch_id", wxPayResult.getMch_id());
                 parameters.put("nonce_str", wxPayResult.getNonce_str());
                 parameters.put("openid", wxPayResult.getOpenid());
                 parameters.put("out_trade_no", wxPayResult.getOut_trade_no());
                 parameters.put("result_code", wxPayResult.getResult_code());
                 parameters.put("return_code", wxPayResult.getReturn_code());
                 parameters.put("time_end", wxPayResult.getTime_end());
                 parameters.put("total_fee", wxPayResult.getTotal_fee());
                 parameters.put("trade_type", wxPayResult.getTrade_type());
                 parameters.put("transaction_id", wxPayResult.getTransaction_id());
 
                 //反校验签名
                 String sign = WXSignUtils.createSign("UTF-8", parameters);
 
                 logger.info("sign = "+sign+"  wxPayResult.getSign()="+wxPayResult.getSign());
 
                 if (sign.equals(wxPayResult.getSign())) {
                     //修改订单的状态
                     String attach = wxPayResult.getAttach();
                     logger.info("微信支付订单号："+attach);
                     String result = PayOrderUtil.payOrder(attach);
                     if(!result.equals("购买成功")) {
                    	 return (HttpXmlUtils.backWeixin("FAIL", "购买失败"));
                     }
 
                     return (HttpXmlUtils.backWeixin("SUCCESS", "OK"));
                 } else {
                     return (HttpXmlUtils.backWeixin("FAIL", "签名失败"));
                 }
             } else {
                 return (HttpXmlUtils.backWeixin("FAIL", wxPayResult.getReturn_msg()));
 
                 //System.out.println("---------微信支付返回Fail----------" + wxPayResult.getReturn_msg());
             }
         }
         return (HttpXmlUtils.backWeixin("FAIL", "paramsStr is null"));
	}


	@Override
	public Result<PayVo> buyProductByAli(Integer productId, Users user) {
		// 获取产品
		SystemProduct product = systemProductMapper.selectById(productId);
		// 生成订单
		SystemOrder order = new SystemOrder();
		order.setCreateTime(new Date());
		order.setPayPrice(product.getSpPrice());
		order.setPayState(0);
		order.setPayType(1);
		order.setProductId(productId);
		order.setSoAmounts(product.getSpPrice());
		order.setSoNo(orderServicer.randomOrder());
		order.setUserId(user.getUserId());
		order.setCashState(0);
		Result<Integer> result = orderServicer.saveOrUpdateOrder(order);
		if (!result.isOk()) {
    		return new Result<PayVo>(00000,result.getErrorMsg());
    	}
		
		// 微信下订单
		AlipayUtil util = new AlipayUtil();
		String code_url = util.getAlipayPagePayUrl(order.getSoNo(), product.getSpName(), product.getSpName(),  Double.toString(product.getSpPrice()), product.getSpName());
		
		PayVo payVo = new PayVo();
		payVo.setSubject(code_url);
		return new Result<PayVo>(payVo);
	}
	
	@Override
	public String AlipayNotifyUrl(String paramsStr) {
		// TODO Auto-generated method stub
		return null;
	}

}
