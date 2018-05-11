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
 * 玩家幸运值
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-18
 */
@TableName("player_luck")
public class PlayerLuck extends Model<PlayerLuck> {

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
     * 幸运值
     */
	private Integer luck;
    /**
     * 幸运开始时间
     */
	@TableField("luck_start")
	private Date luckStart;
    /**
     * 幸运结束时间
     */
	@TableField("luck_end")
	private Date luckEnd;
    /**
     * 幸运总次数
     */
	@TableField("luck_count")
	private Integer luckCount;
    /**
     * 幸运剩余次数
     */
	@TableField("luck_remain")
	private Integer luckRemain;
    /**
     * 幸运状态
     */
	@TableField("luck_status")
	private Integer luckStatus;
    /**
     * 创建人
     */
	@TableField("user_id")
	private Integer userId;
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
		if(luck > 100 || luck < 0){
			vb.setMsg("幸运值在0-100间");
			return vb;
		}
		if(luckStart == null) {
			vb.setMsg("幸运开始时间不能为空");
			return vb;
		}
		if(luckEnd == null) {
			vb.setMsg("幸运结束时间不能为空");
			return vb;
		}
		if(luckStart.getTime() > luckEnd.getTime()) {
			vb.setMsg("结束时间必须小于开始时间");
			return vb;
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

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public Integer getLuck() {
		return luck;
	}

	public void setLuck(Integer luck) {
		this.luck = luck;
	}

	public Date getLuckStart() {
		return luckStart;
	}

	public void setLuckStart(Date luckStart) {
		this.luckStart = luckStart;
	}

	public Date getLuckEnd() {
		return luckEnd;
	}

	public void setLuckEnd(Date luckEnd) {
		this.luckEnd = luckEnd;
	}

	public Integer getLuckCount() {
		return luckCount;
	}

	public void setLuckCount(Integer luckCount) {
		this.luckCount = luckCount;
	}

	public Integer getLuckRemain() {
		return luckRemain;
	}

	public void setLuckRemain(Integer luckRemain) {
		this.luckRemain = luckRemain;
	}

	public Integer getLuckStatus() {
		return luckStatus;
	}

	public void setLuckStatus(Integer luckStatus) {
		this.luckStatus = luckStatus;
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
	protected Serializable pkVal() {
		return this.id;
	}

}
