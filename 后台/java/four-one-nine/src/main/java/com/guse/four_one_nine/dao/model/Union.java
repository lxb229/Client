package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class Union {
	private Long union_id;
	private String union_name;
	private String union_logo;
	private Long clo;
	private Integer income_ratio_clo;
	private Integer income_ratio_member;
	private Integer status = 1;
	private Date update_time;
	private String updater;
	private Date create_time;
	private String creater;

	public Long getUnion_id() {
		return union_id;
	}

	public void setUnion_id(Long union_id) {
		this.union_id = union_id;
	}

	public String getUnion_name() {
		return union_name;
	}

	public void setUnion_name(String union_name) {
		this.union_name = union_name;
	}

	public String getUnion_logo() {
		return union_logo;
	}

	public void setUnion_logo(String union_logo) {
		this.union_logo = union_logo;
	}

	public Long getClo() {
		return clo;
	}

	public void setClo(Long clo) {
		this.clo = clo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Integer getIncome_ratio_clo() {
		return income_ratio_clo;
	}

	public void setIncome_ratio_clo(Integer income_ratio_clo) {
		this.income_ratio_clo = income_ratio_clo;
	}

	public Integer getIncome_ratio_member() {
		return income_ratio_member;
	}

	public void setIncome_ratio_member(Integer income_ratio_member) {
		this.income_ratio_member = income_ratio_member;
	}

}
