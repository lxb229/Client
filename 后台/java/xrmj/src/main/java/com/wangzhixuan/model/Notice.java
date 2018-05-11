package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 游戏公告
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 公告内容(多参数用&分割)
     */
	private String content;
    /**
     * 公告模板
     */
	@TableField("template_id")
	private Integer templateId;
    /**
     * 公告开始时间
     */
	@TableField("notice_start")
	private Date noticeStart;
    /**
     * 公告结束时间
     */
	@TableField("notice_end")
	private Date noticeEnd;
    /**
     * 公告间隔时间(秒）
     */
	@TableField("interval_time")
	private Integer intervalTime;
    /**
     * GM操作人
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Date getNoticeStart() {
		return noticeStart;
	}

	public void setNoticeStart(Date noticeStart) {
		this.noticeStart = noticeStart;
	}

	public Date getNoticeEnd() {
		return noticeEnd;
	}

	public void setNoticeEnd(Date noticeEnd) {
		this.noticeEnd = noticeEnd;
	}

	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
