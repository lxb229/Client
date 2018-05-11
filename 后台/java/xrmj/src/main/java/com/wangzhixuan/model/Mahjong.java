package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.wangzhixuan.commons.utils.StringUtils;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 麻将馆
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
public class Mahjong extends Model<Mahjong> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 麻将馆明星号
     */
	@TableField("mahjong_no")
	private String mahjongNo;
    /**
     * 麻将馆名称
     */
	@TableField("mahjong_name")
	private String mahjongName;
    /**
     * 麻将馆微信
     */
	@TableField("mahjong_wechat")
	private String mahjongWechat;
    /**
     * 馆主明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 活跃度
     */
	private Integer liveness;
    /**
     * 模式(1=共享房卡模式）
     */
	private Integer pattern;
    /**
     * 人数
     */
	@TableField("people_number")
	private Integer peopleNumber;
    /**
     * 可用房卡数
     */
	@TableField("usable_amount")
	private Integer usableAmount;
    /**
     * 捐赠房卡数
     */
	@TableField("donate_amount")
	private Integer donateAmount;
    /**
     * 消耗房卡数
     */
	@TableField("consume_amount")
	private Integer consumeAmount;
    /**
     * 麻将馆状态(1=启用 0=禁用)
     */
	private Integer status;
    /**
     * 推荐状态(0=未推荐 1=已推荐)
     */
	private Integer recommend;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(id == null){
			if(StringUtils.isBlank(mahjongNo)){
				vb.setMsg("麻将馆明星号为空");
				return vb;
			}
			if(StringUtils.isBlank(mahjongName)) {
				vb.setMsg("麻将馆名称为空");
				return vb;
			}
			if(StringUtils.isBlank(startNo)) {
				vb.setMsg("馆主明星号为空");
				return vb;
			}
		}
		vb.setFlag(true);
	    return vb; 
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMahjongNo() {
		return mahjongNo;
	}

	public void setMahjongNo(String mahjongNo) {
		this.mahjongNo = mahjongNo;
	}

	public String getMahjongName() {
		return mahjongName;
	}

	public void setMahjongName(String mahjongName) {
		this.mahjongName = mahjongName;
	}

	public String getMahjongWechat() {
		return mahjongWechat;
	}

	public void setMahjongWechat(String mahjongWechat) {
		this.mahjongWechat = mahjongWechat;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public Integer getLiveness() {
		return liveness;
	}

	public void setLiveness(Integer liveness) {
		this.liveness = liveness;
	}

	public Integer getPattern() {
		return pattern;
	}

	public void setPattern(Integer pattern) {
		this.pattern = pattern;
	}

	public Integer getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(Integer peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

	public Integer getUsableAmount() {
		return usableAmount;
	}

	public void setUsableAmount(Integer usableAmount) {
		this.usableAmount = usableAmount;
	}

	public Integer getDonateAmount() {
		return donateAmount;
	}

	public void setDonateAmount(Integer donateAmount) {
		this.donateAmount = donateAmount;
	}

	public Integer getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(Integer consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
