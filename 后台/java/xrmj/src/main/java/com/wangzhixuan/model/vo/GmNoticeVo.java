package com.wangzhixuan.model.vo;

import java.io.Serializable;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 通知游戏服务器的公告Vo对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class GmNoticeVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//columns START
	/** 
	 * 公告操作命令码
	 * 1：添加公告 
	 * 2：修改公告 
	 * 3：删除公告
	 */
	private Integer cmd ;
	/** 公告GmId */
	private Integer noticeId;
	/** 模板Id */
	private Integer templateId;
	/** 模板参数字符串(参数间以&分隔) */
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
		return JsonUtils.toJson(this);
	}
}