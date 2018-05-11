package com.guse.four_one_nine.dao.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/** 
* @ClassName: DayDealCount 
* @Description: 交易信息日统计
* @author Fily GUSE
* @date 2018年1月4日 下午3:53:29 
*  
*/
@Alias("dayDealCount")
public class DayDealCount {
	/*主键*/
	private Long id;
	/*统计时间*/
	private Date date;
	/*平台流水*/
	private Integer platform_statements_count;
	/*新增服务*/
	private Integer newly_server_count;
	/*成交量*/
	private Integer order_num;
	/*交易金额*/
	private Integer order_count;
	/*单笔最大金额*/
	private Integer order_max;
	/*提现次数*/
	private Integer cash_num;
	/*提现金额*/
	private Integer cash_count;
	/*最大一笔提现金额*/
	private Integer cash_max;
	/*打款数*/
	private Integer remit_num;
	/*累计打款金额*/
	private Integer remit_count;
	/*最大打款金额*/
	private Integer remit_max;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getPlatform_statements_count() {
		return platform_statements_count;
	}
	public void setPlatform_statements_count(Integer platform_statements_count) {
		this.platform_statements_count = platform_statements_count;
	}
	public Integer getNewly_server_count() {
		return newly_server_count;
	}
	public void setNewly_server_count(Integer newly_server_count) {
		this.newly_server_count = newly_server_count;
	}
	public Integer getOrder_num() {
		return order_num;
	}
	public void setOrder_num(Integer order_num) {
		this.order_num = order_num;
	}
	public Integer getOrder_count() {
		return order_count;
	}
	public void setOrder_count(Integer order_count) {
		this.order_count = order_count;
	}
	public Integer getOrder_max() {
		return order_max;
	}
	public void setOrder_max(Integer order_max) {
		this.order_max = order_max;
	}
	public Integer getCash_num() {
		return cash_num;
	}
	public void setCash_num(Integer cash_num) {
		this.cash_num = cash_num;
	}
	public Integer getCash_count() {
		return cash_count;
	}
	public void setCash_count(Integer cash_count) {
		this.cash_count = cash_count;
	}
	public Integer getCash_max() {
		return cash_max;
	}
	public void setCash_max(Integer cash_max) {
		this.cash_max = cash_max;
	}
	public Integer getRemit_num() {
		return remit_num;
	}
	public void setRemit_num(Integer remit_num) {
		this.remit_num = remit_num;
	}
	public Integer getRemit_count() {
		return remit_count;
	}
	public void setRemit_count(Integer remit_count) {
		this.remit_count = remit_count;
	}
	public Integer getRemit_max() {
		return remit_max;
	}
	public void setRemit_max(Integer remit_max) {
		this.remit_max = remit_max;
	}
}
