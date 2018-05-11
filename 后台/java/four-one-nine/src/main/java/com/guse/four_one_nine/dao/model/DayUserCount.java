package com.guse.four_one_nine.dao.model;

import java.util.Date;

/** 
* @ClassName: DayUserCount 
* @Description: 用户信息日统计
* @author Fily GUSE
* @date 2018年1月4日 下午3:57:15 
*  
*/
public class DayUserCount {
	
	/*主键*/
	private Long id;
	/*统计日期*/
	private Date date;
	/*当天注册数*/
	private Integer register_num;
	/*当前安装量*/
	private Integer appinstaller_num;
	/*认证用户数*/
	private Integer real_num;
	/*卖家认证数*/
	private Integer seller_num;
	/*活跃用户数*/
	private Integer live_num;
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
	public Integer getRegister_num() {
		return register_num;
	}
	public void setRegister_num(Integer register_num) {
		this.register_num = register_num;
	}
	public Integer getReal_num() {
		return real_num;
	}
	public void setReal_num(Integer real_num) {
		this.real_num = real_num;
	}
	public Integer getSeller_num() {
		return seller_num;
	}
	public void setSeller_num(Integer seller_num) {
		this.seller_num = seller_num;
	}
	public Integer getLive_num() {
		return live_num;
	}
	public void setLive_num(Integer live_num) {
		this.live_num = live_num;
	}
	public Integer getAppinstaller_num() {
		return appinstaller_num;
	}
	public void setAppinstaller_num(Integer appinstaller_num) {
		this.appinstaller_num = appinstaller_num;
	}
}
