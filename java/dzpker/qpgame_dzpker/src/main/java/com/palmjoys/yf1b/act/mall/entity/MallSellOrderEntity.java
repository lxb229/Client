package com.palmjoys.yf1b.act.mall.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class MallSellOrderEntity implements IEntity<Long>{
	@Id
	private long orderId;

	@Override
	public Long getId() {
		return orderId;
	}

}
