package com.guse.platform.vo.doudou;

import java.io.Serializable;
/**
 * 注册阶段分析
 * @author nbin
 * @date 2017年8月23日 上午10:26:46 
 * @version V1.0
 */
public class RemainRegisterVo implements Serializable {
	
	private static final long serialVersionUID = 1637707955203829082L;
	
	/** 激活、加载、新增注册、新增玩家、新增有效用户、游客角色*/
	private String itemsType;
	/** 完成人数*/
	private Integer completeUserNum;
	/** 流失人数*/
	private Integer lossUserNum;
	/** 流失率*/
	private Double lossRate;
	public String getItemsType() {
		return itemsType;
	}
	public void setItemsType(String itemsType) {
		this.itemsType = itemsType;
	}
	public Integer getCompleteUserNum() {
		return completeUserNum;
	}
	public void setCompleteUserNum(Integer completeUserNum) {
		this.completeUserNum = completeUserNum;
	}
	public Integer getLossUserNum() {
		return lossUserNum;
	}
	public void setLossUserNum(Integer lossUserNum) {
		this.lossUserNum = lossUserNum;
	}
	public Double getLossRate() {
		return lossRate;
	}
	public void setLossRate(Double lossRate) {
		this.lossRate = lossRate;
	}
	
	
}
