package com.guse.four_one_nine.dao.model;

import java.util.Date;

/**
 * @ClassName: Classify
 * @Description: 服务分类实体
 * @author: wangkai
 * @date: 2018年1月8日 下午1:51:48
 * 
 */
public class Classify {
	private Long id;
	private String classify_name;
	private String classify_code;
	private Long status;
	private String creater;
	private Date create_time;

	public Long getId() {
		return id;
	}

	public String getClassify_code() {
		return classify_code;
	}

	public void setClassify_code(String classify_code) {
		this.classify_code = classify_code;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassify_name() {
		return classify_name;
	}

	public void setClassify_name(String classify_name) {
		this.classify_name = classify_name;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
