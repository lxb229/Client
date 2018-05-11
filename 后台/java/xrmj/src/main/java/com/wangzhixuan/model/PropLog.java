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
 * 
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
@TableName("prop_log")
public class PropLog extends Model<PropLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 货币类型(1=房卡 2=金币 3=银币)
     */
	@TableField("money_type")
	private Integer moneyType;
    /**
     * 数量
     */
	private Integer amount;
    /**
     * 日志类型(1=房卡购买 2=房卡游戏消耗 3=红包领取 4=刷新抽奖 5=银币抽奖 6=金币兑换)
     */
	@TableField("log_type")
	private Integer logType;
    /**
     * 详细说明
     */
	private String content;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getLogType() {
		return logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
