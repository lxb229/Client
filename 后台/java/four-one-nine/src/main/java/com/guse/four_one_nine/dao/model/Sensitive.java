package com.guse.four_one_nine.dao.model;

import java.util.Date;

public class Sensitive {
	private Long id;
	private String word_group;
	private String replace_word;
	private Date update_time;
	private String updater;
	private Date create_time;
	private String creater;

	private Long count_no;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getCount_no() {
		return count_no;
	}

	public void setCount_no(Long count_no) {
		this.count_no = count_no;
	}

	public String getWord_group() {
		return word_group;
	}

	public void setWord_group(String word_group) {
		this.word_group = word_group;
	}

	public String getReplace_word() {
		return replace_word;
	}

	public void setReplace_word(String replace_word) {
		this.replace_word = replace_word;
	}

}
