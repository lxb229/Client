package com.palmjoys.yf1b.act.framework.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

@Entity
@Memcached
public class CommonCfgEntity implements IEntity<Integer>{
	@Id
	private int recordId;
	//官方WX
	@Column(nullable = false)
	private String wxNO;
	
	public static CommonCfgEntity valueOf (int recordId){
		CommonCfgEntity retEntity = new CommonCfgEntity();
		retEntity.recordId = recordId;
		retEntity.wxNO = "WX88888888";
		
		return retEntity;
	}
	
	@Override
	public Integer getId() {
		return recordId;
	}

	public String getWxNO() {
		return wxNO;
	}
	
	@Enhance
	public void setWxNO(String wxNO) {
		this.wxNO = wxNO;
	}

}
