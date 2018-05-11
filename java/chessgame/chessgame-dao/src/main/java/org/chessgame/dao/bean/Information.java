package org.chessgame.dao.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 信息表(对象)
 * @author 不能
 *
 */
public class Information implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519342293041696915L;
	
	/**
	 * 信息类型 
	 * 1：帮助 2：消息 3：公告 4：推送通知
	 */
	private int i_type;
	
	/**
	 * 信息内容
	 */
	private String content;
	
	/**
	 * 信息创建时间
	 */
	private Date create_time;
	
	/**
	 * 信息状态 
	 * 1：展示 0：不展示
	 */
	private int status;

	public int getI_type() {
		return i_type;
	}

	public void setI_type(int i_type) {
		this.i_type = i_type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
