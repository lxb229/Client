package com.palmjoys.yf1b.act.message.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.treediagram.nina.core.lang.JsonUtils;
import org.treediagram.nina.memcache.IEntity;
import org.treediagram.nina.memcache.annotation.Enhance;
import org.treediagram.nina.memcache.annotation.Memcached;

import com.palmjoys.yf1b.act.message.model.NoticeParamAttrib;

@Entity
@Memcached
@NamedQueries({
	@NamedQuery(name=NoticeEntity.NQ_NOTICEMAX_UID, query="SELECT max(A.noticeId) from NoticeEntity AS A")
})
public class NoticeEntity implements IEntity<Long>, Lifecycle{
	public final static String NQ_NOTICEMAX_UID = "nq_noticemax_uid";
	//公告Id
	@Id
	private Long noticeId;
	//公告GM方唯一Id
	@Column(nullable = false)
	private int notice_gm_Id;
	//公告生效时间
	@Column(nullable = false)
	private long startTime;
	//公告结束时间
	@Column(nullable = false)
	private long endTime;
	//循环时间(秒,0=只发一次)
	@Column(nullable = false)
	private int intervalTime;
	//消息下次推送时间
	@Column(nullable = false)
	private long nextTime;
	//公告模板Id
	@Column(nullable = false)
	private int noticeTemplateId;
	//公告内容Json
	@Lob
	@Column(nullable = false)
	private String contentJson;
	
	@Transient
	private List<NoticeParamAttrib> contentList;
	
	public static NoticeEntity valueOf(Long Id){
		NoticeEntity retEntity = new NoticeEntity();
		retEntity.noticeId = Id;
		retEntity.notice_gm_Id = 0;
		retEntity.startTime = 0;
		retEntity.endTime = 0;
		retEntity.intervalTime = 0;
		retEntity.nextTime = 0;
		retEntity.contentJson = null;
		retEntity.contentList = new ArrayList<NoticeParamAttrib>();
		
		return retEntity;
	}
		
	@Override
	public Long getId() {
		return noticeId;
	}

	public Long getNoticeId() {
		return noticeId;
	}

	@Enhance
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	
	public int getNotice_gm_Id() {
		return notice_gm_Id;
	}

	@Enhance
	public void setNotice_gm_Id(int notice_gm_Id) {
		this.notice_gm_Id = notice_gm_Id;
	}

	public long getStartTime() {
		return startTime;
	}

	@Enhance
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	@Enhance
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	@Enhance
	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public long getNextTime() {
		return nextTime;
	}

	@Enhance
	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public int getNoticeTemplateId() {
		return noticeTemplateId;
	}

	@Enhance
	public void setNoticeTemplateId(int noticeTemplateId) {
		this.noticeTemplateId = noticeTemplateId;
	}

	public String getContentJson() {
		return contentJson;
	}

	@Enhance
	public void setContentJson(String contentJson) {
		this.contentJson = contentJson;
	}

	public List<NoticeParamAttrib> getContentList() {
		return contentList;
	}

	@Enhance
	public void setContentList(List<NoticeParamAttrib> contentList) {
		this.contentList = contentList;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		if(StringUtils.isBlank(contentJson)){
			contentList = new ArrayList<NoticeParamAttrib>();
		}else{
			contentList = JsonUtils.string2Collection(contentJson, List.class, NoticeParamAttrib.class);
		}
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		contentJson = JsonUtils.object2String(contentList);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		return onSave(arg0);
	}
	
}
