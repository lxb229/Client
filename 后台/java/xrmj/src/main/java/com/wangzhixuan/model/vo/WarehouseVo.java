package com.wangzhixuan.model.vo;

import java.io.Serializable;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class WarehouseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	 /**
     * 表主键id
     */
	private Integer id;
    /**
     * 商品
     */
	private Integer commodity;
	/**
     * 商品名称
     */
	private String commodityName;
    /**
     * 可用数量
     */
	private Integer usableAmount;
    /**
     * 冻结待出库数量
     */
	private Integer outboundAmount;
    /**
     * 总数量
     */
	private Integer allAmount;
    /**
     * 已使用数量
     */
	private Integer useAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCommodity() {
		return commodity;
	}

	public void setCommodity(Integer commodity) {
		this.commodity = commodity;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getUsableAmount() {
		return usableAmount;
	}

	public void setUsableAmount(Integer usableAmount) {
		this.usableAmount = usableAmount;
	}

	public Integer getOutboundAmount() {
		return outboundAmount;
	}

	public void setOutboundAmount(Integer outboundAmount) {
		this.outboundAmount = outboundAmount;
	}

	public Integer getAllAmount() {
		return allAmount;
	}

	public void setAllAmount(Integer allAmount) {
		this.allAmount = allAmount;
	}

	public Integer getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(Integer useAmount) {
		this.useAmount = useAmount;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}