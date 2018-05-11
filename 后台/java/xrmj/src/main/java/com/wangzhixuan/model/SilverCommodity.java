package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 银币抽奖商品
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-08
 */
@TableName("silver_commodity")
public class SilverCommodity extends Model<SilverCommodity> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商品
     */
	private Integer commodity;
    /**
     * 商品等级
     */
	@TableField("award_lv")
	private Integer awardLv;
    /**
     * 邮件内容
     */
	@TableField("email_content")
	private String emailContent;
    /**
     * 上架时间
     */
	@TableField("issued_time")
	private Date issuedTime;
    /**
     * 下架时间
     */
	@TableField("soldout_time")
	private Date soldoutTime;


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

	public Integer getAwardLv() {
		return awardLv;
	}

	public void setAwardLv(Integer awardLv) {
		this.awardLv = awardLv;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}

	public Date getSoldoutTime() {
		return soldoutTime;
	}

	public void setSoldoutTime(Date soldoutTime) {
		this.soldoutTime = soldoutTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
