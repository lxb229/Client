package com.guse.four_one_nine.dao.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Activity {
	private Long id;
	private String activity_name;
	private String activity_cover;
	private Integer mark_top;
	private Integer type;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date start_time;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date end_time;
	private Integer activity_status;
	private Date create_time;
	private String creater;
	private Integer restrict_no;
	private String activity_content;
	private Integer participants_no;
	private Integer interview_no;
	private Integer criticism_no;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getActivity_cover() {
		return activity_cover;
	}

	public void setActivity_cover(String activity_cover) {
		this.activity_cover = activity_cover;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public Integer getActivity_status() {
		return activity_status;
	}

	public void setActivity_status(Integer activity_status) {
		this.activity_status = activity_status;
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

	public Integer getRestrict_no() {
		return restrict_no;
	}

	public void setRestrict_no(Integer restrict_no) {
		this.restrict_no = restrict_no;
	}

	public String getActivity_content() {
		return activity_content;
	}

	public void setActivity_content(String activity_content) {
		this.activity_content = activity_content;
	}

	public Integer getParticipants_no() {
		return participants_no;
	}

	public void setParticipants_no(Integer participants_no) {
		this.participants_no = participants_no;
	}

	public Integer getInterview_no() {
		return interview_no;
	}

	public void setInterview_no(Integer interview_no) {
		this.interview_no = interview_no;
	}

	public Integer getCriticism_no() {
		return criticism_no;
	}

	public void setCriticism_no(Integer criticism_no) {
		this.criticism_no = criticism_no;
	}

	public Integer getMark_top() {
		return mark_top;
	}

	public void setMark_top(Integer mark_top) {
		this.mark_top = mark_top;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
