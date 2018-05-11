package com.palmjoys.yf1b.act.order.manager;

import org.treediagram.nina.memcache.service.Filter;

import com.palmjoys.yf1b.act.order.entity.BuyOrderEntity;
import com.palmjoys.yf1b.act.order.entity.SellOrderEntity;

public class OrderFilterManager {
	private static OrderFilterManager _instance = new OrderFilterManager();
	
	public static OrderFilterManager Instance(){
		return _instance;
	}
	
	public BuyOrderFilter_orderId createFileter_BuyOrderFilter_orderId(long orderId){
		return new BuyOrderFilter_orderId(orderId);
	}
	
	public BuyOrderFilter_starNO createFilter_BuyOrderFilter_starNO(String starNO){
		return new BuyOrderFilter_starNO(starNO);
	}
	
	public SellOrderFilter_orderId createFilter_SellOrderFilter_orderId(long orderId){
		return new SellOrderFilter_orderId(orderId);
	}
	
	public SellOrderFilter_starNO createFilter_SellOrderFilter_starNO(String starNO){
		return new SellOrderFilter_starNO(starNO);
	}
		
	public class BuyOrderFilter_orderId implements Filter<BuyOrderEntity>{
		private long orderId;
		public BuyOrderFilter_orderId(long orderId){
			this.orderId = orderId;
		}
		
		@Override
		public boolean isExclude(BuyOrderEntity entity) {
			if(this.orderId == entity.getId().longValue()){
				return false;
			}
			return true;
		}
	}
	
	public class BuyOrderFilter_starNO implements Filter<BuyOrderEntity>{
		private String starNO;
		public BuyOrderFilter_starNO(String starNO){
			this.starNO = starNO;
		}
		
		@Override
		public boolean isExclude(BuyOrderEntity entity) {
			if(this.starNO.equalsIgnoreCase(entity.getStarNO())){
				return false;
			}
			return true;
		}
	}
	
	public class SellOrderFilter_orderId implements Filter<SellOrderEntity>{
		private long orderId;
		public SellOrderFilter_orderId(long orderId){
			this.orderId = orderId;
		}

		@Override
		public boolean isExclude(SellOrderEntity entity) {
			if(this.orderId == entity.getId().longValue()){
				return false;
			}
			return true;
		}
	}
	
	public class SellOrderFilter_starNO implements Filter<SellOrderEntity>{
		private String starNO;

		public SellOrderFilter_starNO(String starNO){
			this.starNO = starNO;
		}
		
		@Override
		public boolean isExclude(SellOrderEntity entity) {
			if(this.starNO.equalsIgnoreCase(entity.getStarNO())){
				return false;
			}
			return true;
		}
	}

}
