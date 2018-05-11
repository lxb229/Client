package com.palmjoys.yf1b.act.dzpker.entity;

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

import com.palmjoys.yf1b.act.dzpker.model.ChipAttrib;
import com.palmjoys.yf1b.act.dzpker.model.InsuranceCfgAttrib;

@Entity
@Memcached
public class DzpkerCfgEntity implements IEntity<Integer>, Lifecycle{
	@Id
	private int cfgId;
	//筹码配置Json
	@Lob
	@Column(nullable = false)
	private String chipsListJson;
	//桌子过期时间配置
	@Lob
	@Column(nullable = false)
	private String vildTimeListJson;
	//单次筹码购买上限配置
	@Lob
	@Column(nullable = false)
	private String buyChipListJson;
	//保险赔率配置
	@Lob
	@Column(nullable = false)
	private String insuranceListJson;
	
	//筹码配置列表
	@Transient
	private List<ChipAttrib> chipsList;
	//桌子过期时间列表
	@Transient
	private List<Integer> vildTimeList;
	//单次筹码购买上限列表
	@Transient
	private List<Integer> buyChipList;
	//保险赔率列表
	@Transient
	private List<InsuranceCfgAttrib> insuranceList;
	

	public static DzpkerCfgEntity valueOf(int id){
		DzpkerCfgEntity retEntity = new DzpkerCfgEntity();
		retEntity.cfgId = id;
		retEntity.chipsListJson = null;
		retEntity.chipsList = new ArrayList<>();
		retEntity.vildTimeListJson = null;
		retEntity.vildTimeList = new ArrayList<>();
		retEntity.buyChipListJson = null;
		retEntity.buyChipList = new ArrayList<>();
		retEntity.insuranceListJson = null;
		retEntity.insuranceList = new ArrayList<>();
		
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
		if(StringUtils.isBlank(this.chipsListJson)){
			this.chipsList = new ArrayList<>();
		}else{
			this.chipsList = JsonUtils.string2Collection(this.chipsListJson, List.class, ChipAttrib.class);
		}
		if(StringUtils.isBlank(this.vildTimeListJson)){
			this.vildTimeList = new ArrayList<>();
		}else{
			this.vildTimeList = JsonUtils.string2Collection(this.vildTimeListJson, List.class, Integer.class);
		}
		if(StringUtils.isBlank(this.buyChipListJson)){
			this.buyChipList = new ArrayList<>();
		}else{
			this.buyChipList = JsonUtils.string2Collection(this.buyChipListJson, List.class, Integer.class);
		}
		if(StringUtils.isBlank(this.insuranceListJson)){
			this.insuranceList = new ArrayList<>();
		}else{
			this.insuranceList = JsonUtils.string2Collection(this.insuranceListJson, List.class, InsuranceCfgAttrib.class);
		}
	}
	
	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		this.chipsListJson = JsonUtils.object2String(this.chipsList);
		this.vildTimeListJson = JsonUtils.object2String(this.vildTimeList);
		this.buyChipListJson = JsonUtils.object2String(this.buyChipList);
		this.insuranceListJson = JsonUtils.object2String(this.insuranceList);
		return false;
	}
	
	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}

	public List<ChipAttrib> getChipsList() {
		return chipsList;
	}

	@Enhance
	public void setChipsList(List<ChipAttrib> chipsList) {
		this.chipsList = chipsList;
	}

	public List<Integer> getVildTimeList() {
		return vildTimeList;
	}

	@Enhance
	public void setVildTimeList(List<Integer> vildTimeList) {
		this.vildTimeList = vildTimeList;
	}

	public List<Integer> getBuyChipList() {
		return buyChipList;
	}

	@Enhance
	public void setBuyChipList(List<Integer> buyChipList) {
		this.buyChipList = buyChipList;
	}

	public List<InsuranceCfgAttrib> getInsuranceList() {
		return insuranceList;
	}

	@Enhance
	public void setInsuranceList(List<InsuranceCfgAttrib> insuranceList) {
		this.insuranceList = insuranceList;
	}

}
