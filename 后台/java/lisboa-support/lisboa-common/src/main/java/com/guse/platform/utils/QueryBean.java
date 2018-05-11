package com.guse.platform.utils;

import java.util.Date;


import com.guse.platform.utils.date.DateUtils;

public class QueryBean implements java.io.Serializable {
	
	private static final long serialVersionUID = -8574271957843088757L;
	
	/** 开始日期*/
    private Date            startDate;
    /** 结束日期*/
    private Date            endDate;
    /**
     * 比较日期 00:00:00 - 23:59:59
     */
    private Date  contrastDateStart;
    private Date  contrastDateEnd;

    /**
     * 查询某天日期00:00:00 - 23:59:59
     */
    private Date  somedayDateStart;
    private Date  somedayDateEnd;
    
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		if(endDate!=null){
			endDate = DateUtils.StrToDate(DateUtils.DateToStr(endDate, DateUtils.format)+" 23:59:59", DateUtils.sdf);
		}
		this.endDate = endDate;
	}
	public Date getSomedayDateStart() {
		return somedayDateStart;
	}
	public void setSomedayDateStart(Date somedayDateStart) {
		this.somedayDateStart = somedayDateStart;
	}
	public Date getSomedayDateEnd() {
		return somedayDateEnd;
	}
	public void setSomedayDateEnd(Date somedayDateEnd) {
		this.somedayDateEnd = somedayDateEnd;
	}
	public Date getContrastDateStart() {
		return contrastDateStart;
	}
	public void setContrastDateStart(Date contrastDateStart) {
		this.contrastDateStart = contrastDateStart;
	}
	public Date getContrastDateEnd() {
		return contrastDateEnd;
	}
	public void setContrastDateEnd(Date contrastDateEnd) {
		this.contrastDateEnd = contrastDateEnd;
	}
	
}
