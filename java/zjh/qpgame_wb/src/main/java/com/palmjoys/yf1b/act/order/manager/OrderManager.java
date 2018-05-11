package com.palmjoys.yf1b.act.order.manager;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.treediagram.nina.core.codec.CryptoUtils;
import org.treediagram.nina.memcache.annotation.Inject;
import org.treediagram.nina.memcache.orm.Querier;
import org.treediagram.nina.memcache.service.EntityBuilder;
import org.treediagram.nina.memcache.service.EntityMemcache;
import org.treediagram.nina.resource.Storage;
import org.treediagram.nina.resource.annotation.Static;

import com.palmjoys.yf1b.act.order.entity.BuyOrderEntity;
import com.palmjoys.yf1b.act.order.entity.SellOrderEntity;
import com.palmjoys.yf1b.act.order.model.OrderPayVo;
import com.palmjoys.yf1b.act.order.model.PaySubmitAttrib;
import com.palmjoys.yf1b.act.order.resource.OrderConfig;

@Component
public class OrderManager {
	@Inject
	private EntityMemcache<Long, BuyOrderEntity> buyOrderCache;
	@Inject
	private EntityMemcache<Long, SellOrderEntity> sellOrderCache;
	@Autowired
	private Querier querier;
	@Static
	private Storage<Integer, OrderConfig> orderCfgs;
	
	
	//购买订单Id
	private AtomicLong buyOrderIdIndex;
	//出售订单Id
	private AtomicLong sellOrderIdIndex;
	//订单同步数据修改锁
	private Lock _lock = new ReentrantLock();
	
	//订单状态定义
	//等待处理状态
	public static final int STATE_ORDER_WAIT = 0;
	//正在处理
	public static final int STATE_ORDER_TRANSING = 1;
	//处理失败
	public static final int STATE_ORDER_FAIL = 2;
	//处理成功
	public static final int STATE_ORDER_SUCESS = 3;
	
	//订单支付类型定义
	//WX
	public static final int TYPE_PAY_WX = 1;
	//支付宝
	public static final int TYPE_PAY_ZFB = 2;
	
	@PostConstruct
	protected void init(){
		Long buyOrderId = null;
		Long sellOrderId = null;
		String querySql = "SELECT MAX(A.orderId) FROM BuyOrderEntity AS A";
		List<Object> retObjects = querier.listBySqlLimit(BuyOrderEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				buyOrderId = (Long) obj;
			}
		}
		if(null == buyOrderId){
			buyOrderIdIndex = new AtomicLong(10000000);
		}else{
			buyOrderIdIndex = new AtomicLong(buyOrderId.longValue()+1);
		}
		
		querySql = "SELECT MAX(A.orderId) FROM SellOrderEntity AS A";
		retObjects = querier.listBySqlLimit(SellOrderEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			if(null != obj){
				sellOrderId = (Long) obj;
			}
		}
		if(null == sellOrderId){
			sellOrderIdIndex = new AtomicLong(10000000);
		}else{
			sellOrderIdIndex = new AtomicLong(sellOrderId.longValue()+1);
		}
	}
	
	public void lock(){
		this._lock.lock();
	}
	
	public void unLock(){
		this._lock.unlock();
	}
	
	public BuyOrderEntity loadOrCreateBuyOrder(String starNO, int rmb, long goldMoney){
		long orderId = buyOrderIdIndex.incrementAndGet();
		return buyOrderCache.loadOrCreate(orderId, new EntityBuilder<Long, BuyOrderEntity>(){
			@Override
			public BuyOrderEntity createInstance(Long pk) {
				return BuyOrderEntity.valueOf(orderId, starNO, rmb, goldMoney);
			}
		});
	}
	
	public BuyOrderEntity findBuyOrder_orderId(long orderId){
		return buyOrderCache.load(orderId);
	}
	
	public SellOrderEntity loadOrCreateSellOrder(String starNO, int rmb, int goldMoney,
			int payType, String payAccount){
		long orderId = sellOrderIdIndex.incrementAndGet();
		return sellOrderCache.loadOrCreate(orderId, new EntityBuilder<Long, SellOrderEntity>(){
			@Override
			public SellOrderEntity createInstance(Long pk) {
				return SellOrderEntity.valueOf(orderId, starNO, rmb, goldMoney, payType, payAccount);
			}
		});
	}
	
	public SellOrderEntity findSellOrder_orderId(long orderId){
		return sellOrderCache.load(orderId);
	}
	
	public SellOrderEntity findSellOrderOf_starNO(String starNO){
		//先查表
		String querySql = "SELECT A.orderId FROM SellOrderEntity AS A WHERE A.starNO='" + starNO + "' AND A.state<="+OrderManager.STATE_ORDER_TRANSING;
		List<Object> retObjects = querier.listBySqlLimit(SellOrderEntity.class, Object.class, querySql, 0, 1);
		for(Object obj : retObjects){
			Long orderId = (Long) obj;
			return this.findSellOrder_orderId(orderId);
		}
		//后查缓存
		List<SellOrderEntity> retEntitys = sellOrderCache.getFinder().find(OrderFilterManager.Instance().createFilter_SellOrderFilter_starNO(starNO));
		for(SellOrderEntity entity : retEntitys){
			if(entity.getState() <= OrderManager.STATE_ORDER_TRANSING){
				return entity;
			}
		}
		return null;
	}
	
	//向第三方接口提交充值请求数据
	public OrderPayVo sendChargeData(BuyOrderEntity orderEntity){
		OrderPayVo retVo = new OrderPayVo();
		PaySubmitAttrib submitAttrib = new PaySubmitAttrib();
		try{
			int payId = orderCfgs.get(1, false).getPayType();
			//版本号
			submitAttrib.version = "1.0";
			//商户号
			submitAttrib.customerid = orderCfgs.get(payId, false).getSellerNO();
			//商户KEY
			submitAttrib.apikey = orderCfgs.get(payId, false).getSellerKEY();
			//订单号
			submitAttrib.sdorderno = ""+orderEntity.getId();
			//金额(元)
			submitAttrib.total_fee = ""+orderEntity.getRmb() + ".00";
			//支付方式
			if(orderEntity.getPayType() == 1){
				//WX
				submitAttrib.paytype = orderCfgs.get(payId, false).getWxPay();
			}else if(orderEntity.getPayType() == 2){
				//支付宝
				submitAttrib.paytype = orderCfgs.get(payId, false).getAliPay();
			}else if(orderEntity.getPayType() == 3){
				//银行
				submitAttrib.paytype = orderCfgs.get(payId, false).getAliPay();
			}
			//获取二维码
			submitAttrib.get_code = "0";
			//银行编码
			submitAttrib.bankcode = "";
			//支付成功异步回调服务器URL
			submitAttrib.notifyurl = orderCfgs.get(payId, false).getUbotzurl();
			//支付成功客户端同步URL(不参与签名)
			submitAttrib.returnurl = "http://pay.qimipay.com";
			//附加信息
			submitAttrib.remark = ""+orderEntity.getId();			 
			//MD5加密值
			String ubosign = submitAttrib.getSignParamStr();
			submitAttrib.sign = CryptoUtils.getMD5(ubosign.getBytes("utf8"));			
			
			String payUrl = orderCfgs.get(payId, false).getPayUrl() + "?" + submitAttrib.getParams();
			retVo.code = 0;
			retVo.msg = payUrl;
		}catch(Exception e){
			retVo.code = -1;
			retVo.msg = "server exception";
		}
		return retVo;
	}
	
}
