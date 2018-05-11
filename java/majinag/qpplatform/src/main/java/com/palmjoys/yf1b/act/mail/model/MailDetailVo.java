package com.palmjoys.yf1b.act.mail.model;

import java.util.List;

import com.palmjoys.yf1b.act.framework.gameobject.model.GameObject;

public class MailDetailVo {
	//邮件Id
	public int mailId;
	//邮件内容
	public String content;
	//邮件附件
	public List<GameObject> attachment;
	//邮件附件相关数据
	public String attachmentData;
	//邮件附件状态(-1=过期, 0=正常)
	public int attachmentState;
	//邮件附件领取状态(0=未领取,1=已领取)
	public int attachmentGetState;
}
