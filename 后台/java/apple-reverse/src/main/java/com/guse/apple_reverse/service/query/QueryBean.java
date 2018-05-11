package com.guse.apple_reverse.service.query;

import com.guse.apple_reverse.netty.handler.session.Message;

/** 
* @ClassName: QueryBean 
* @Description: 查询对象
* @author Fily GUSE
* @date 2017年12月27日 下午4:32:08 
*  
*/
public class QueryBean {
	
	private int id; // 标识
	private Message msg; // 查询对象
	private Long queryTime; // 查询时间
	private boolean respond = false; // 是否回执。默认没有回执
	private int queryNum = 1; // 查询次数,默认1
	private boolean result = false; // 是否返回结果，默认没有
	public QueryBean(int id, Message msg, Long queryTime) {
		this.id = id;
		this.msg = msg;
		this.queryTime = queryTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Message getMsg() {
		return msg;
	}
	public void setMsg(Message msg) {
		this.msg = msg;
	}
	public Long getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(Long queryTime) {
		this.queryTime = queryTime;
	}
	public boolean isRespond() {
		return respond;
	}
	public void setRespond(boolean respond) {
		this.respond = respond;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public int getQueryNum() {
		return queryNum;
	}
	public void setQueryNum(int queryNum) {
		this.queryNum = queryNum;
	}
}
