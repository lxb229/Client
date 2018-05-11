package com.guse.platform.vo.doudou;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemLuckVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class NoticeVo implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5084500896182405942L;
	
	//columns START
	/** 公告操作命令码(1：添加公告 2：修改公告 3：删除公告) */
	private Integer cmd ;
	/** 公告GmId */
	private Integer noticeId;
	/** 模板Id */
	private Integer templateId;
	/** 模板参数字符串(参数间以逗号分隔) */
	private String noticeParams;
	/** 公告开始时间(毫秒时间) */
	private Long startTime;
	/** 公告有效时长(秒) */
	private Integer vialdTime;
	/** 公告重复频率时间(秒,0=只播一次) */
	private Integer repate;
	//columns END
	
	public Integer getCmd() {
		return cmd;
	}

	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}

	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getNoticeParams() {
		return noticeParams;
	}

	public void setNoticeParams(String noticeParams) {
		this.noticeParams = noticeParams;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getVialdTime() {
		return vialdTime;
	}

	public void setVialdTime(Integer vialdTime) {
		this.vialdTime = vialdTime;
	}

	public Integer getRepate() {
		return repate;
	}

	public void setRepate(Integer repate) {
		this.repate = repate;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getNoticeId())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NoticeVo == false) return false;
		if(this == obj) return true;
		NoticeVo other = (NoticeVo)obj;
		return new EqualsBuilder()
			.append(getNoticeId(),other.getNoticeId())
			.isEquals();
	}
	
}

