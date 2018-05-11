package com.palmjoys.yf1b.act.notice.service;

public interface NoticeService {
	/**
	 * 获取公告列表
	 * */
	public Object notice_List();
	
	/**
	 * 查询指定公告
	 * noticeId 公告Id
	 * */
	public Object notice_query_content(String noticeId);
	
	/**
	 * 修改公告
	 * noticeId 公告Id
	 * params 公告变参内容
	 * startTime 公告生效时间(毫秒时间戳)
	 * vialdTime 公告有效时长(秒)
	 * repate 公告重复频率时间(秒,0=只播一次)
	 * */
	public Object notice_modfiy(String noticeId, String []params, 
			String startTime, String vialdTime, int repate);
	
	/**
	 * 删除公告
	 * noticeId 公告Id
	 * */
	public Object notice_delete(String noticeId);
	
	/**
	 * 添加公告
	 * templateId 公告模板Id
	 * params 公告变参内容
	 * startTime 公告生效时间(毫秒时间戳)
	 * vialdTime 公告有效时长(秒)
	 * repate 公告重复频率时间(秒,0=只播一次)
	 * */
	public Object notice_new(int templateId, int exNoticeId, String []params, String startTime, 
			String vialdTime, int repate);
	
	/**
	 * 添加公告2
	 * templateId 公告模板Id
	 * params 公告变参内容
	 * */
	public Object notice_new2(int templateId, int exNoticeId, String []params);
}
