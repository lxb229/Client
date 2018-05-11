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
 * 每日峰值表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@TableName("day_peak")
public class DayPeak extends Model<DayPeak> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 天数日期
     */
	@TableField("day_date")
	private Date dayDate;
    /**
     * 峰值
     */
	private Integer peak;
    /**
     * 峰值时间
     */
	@TableField("peak_time")
	private Date peakTime;
    /**
     * 在线人数
     */
	@TableField("online_num")
	private Integer onlineNum;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDayDate() {
		return dayDate;
	}

	public void setDayDate(Date dayDate) {
		this.dayDate = dayDate;
	}

	public Integer getPeak() {
		return peak;
	}

	public void setPeak(Integer peak) {
		this.peak = peak;
	}

	public Date getPeakTime() {
		return peakTime;
	}

	public void setPeakTime(Date peakTime) {
		this.peakTime = peakTime;
	}

	public Integer getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
