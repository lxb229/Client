package com.wangzhixuan.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class CommodityImageVo implements Serializable {
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
     * 商品
     */
	private String commodityName;
    /**
     * 图片类型(1=icon图片 2=详情图片)
     */
	private Integer imageType;
    /**
     * 图片地址
     */
	private String imageUrl;
    /**
     * GM操作人
     */
	private Integer userId;
    /**
     * 创建时间
     */
	private Date createTime;
	
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

	public Integer getImageType() {
		return imageType;
	}

	public void setImageType(Integer imageType) {
		this.imageType = imageType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}