package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 系统任务表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-10
 */
@TableName("system_task")
public class SystemTask extends Model<SystemTask> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 数据类型
     */
	@TableField("task_cmd")
	private Integer taskCmd;
    /**
     * 任务json内容
     */
	@TableField("json_content")
	private String jsonContent;
    /**
     * 任务状态0：待处理 1：处理成功 2：处理失败
     */
	@TableField("task_status")
	private Integer taskStatus;
    /**
     * 任务处理次数
     */
	@TableField("task_num")
	private Integer taskNum;
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

	public Integer getTaskCmd() {
		return taskCmd;
	}

	public void setTaskCmd(Integer taskCmd) {
		this.taskCmd = taskCmd;
	}

	public String getJsonContent() {
		return jsonContent;
	}

	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
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
