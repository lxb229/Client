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
 * 麻将馆玩家
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-17
 */
@TableName("mahjong_player")
public class MahjongPlayer extends Model<MahjongPlayer> {

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
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 类型(0=馆主  1=成员)
     */
	private Integer type;
    /**
     * 活跃度
     */
	private Integer liveness;
    /**
     * 捐赠数量
     */
	private Integer donate;
    /**
     * 积分
     */
	private Integer integral;
    /**
     * 状态(1=有效 0=无效)
     */
	private Integer status;
    /**
     * 加入时间
     */
	@TableField("create_time")
	private Date createTime;


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

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLiveness() {
		return liveness;
	}

	public void setLiveness(Integer liveness) {
		this.liveness = liveness;
	}

	public Integer getDonate() {
		return donate;
	}

	public void setDonate(Integer donate) {
		this.donate = donate;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
