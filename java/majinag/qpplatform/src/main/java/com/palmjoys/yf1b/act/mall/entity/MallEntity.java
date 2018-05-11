package com.palmjoys.yf1b.act.mall.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.mall.model.MallItemAttrib;
import com.palmjoys.yf1b.act.mall.model.MallProxyAttrib;

@Entity
@Memcached
public class MallEntity implements IEntity<Integer>, Lifecycle{
	@Id
	private Integer cfgId;
	//商品列表Json
	@Lob
	@Column(nullable = false)
	private String mallListJson;
	//代理商配置列表Json
	@Lob
	@Column(nullable = false)
	private String proxyListJson;
	
	//商品列表
	@Transient
	private List<MallItemAttrib> mallList;
	
	//代理商配置列表
	@Transient
	private List<MallProxyAttrib> proxyList;
	

	public static MallEntity valueOf(int cfgId){
		MallEntity retEntity = new MallEntity();
		retEntity.cfgId = cfgId;
		retEntity.mallListJson = null;
		retEntity.mallList = new ArrayList<>();
		
		retEntity.proxyListJson = null;
		retEntity.proxyList = new ArrayList<>();
		
		return retEntity;
	}
	
	@Override
	public Integer getId() {
		return cfgId;
	}
	
	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(mallListJson)){
			mallList = new ArrayList<>();
		}else{
			mallList = JsonUtils.string2Collection(mallListJson, List.class, MallItemAttrib.class);
		}
		
		if(StringUtils.isBlank(proxyListJson)){
			proxyList = new ArrayList<>();
		}else{
			proxyList = JsonUtils.string2Collection(proxyListJson, List.class, MallProxyAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		mallListJson = JsonUtils.object2String(mallList);
		proxyListJson = JsonUtils.object2String(proxyList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	public List<MallItemAttrib> getMallList() {
		return mallList;
	}

	@Enhance
	public void setMallList(List<MallItemAttrib> mallList) {
		this.mallList = mallList;
	}

	public List<MallProxyAttrib> getProxyList() {
		return proxyList;
	}

	@Enhance
	public void setProxyList(List<MallProxyAttrib> proxyList) {
		this.proxyList = proxyList;
	}

	

}
