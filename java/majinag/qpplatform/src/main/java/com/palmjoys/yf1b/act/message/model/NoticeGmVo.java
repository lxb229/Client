package com.palmjoys.yf1b.act.message.model;

public class NoticeGmVo {
	//公告Id
	public String msgId;
	//公告内容
	public String content;
	//公告生效时间
	public String startTime;
	//公告失效时间
	public String endTime;
	//循环时间(秒,0=只发一次)
	public int intervalTime;
	//公告模板Id
	public int noticeTemplateId;
}
