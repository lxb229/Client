package com.palmjoys.yf1b.act.mail.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketDefine;
import com.palmjoys.yf1b.act.framework.annotation.SocketModule;

@SocketDefine(value = "邮件模块")
public interface MailDefine {
	@SocketModule("邮件模块")
	int MAIL = 2;

	static final int MAIL_COMMAND_BASE = MAIL * 100;
	static final int MAIL_ERROR_BASE = (0 - MAIL) * 1000;
	static final int MAIL_COMMAND_BASE_NOTIFY = MAIL * 10000;

	// command id
	// 获取邮件列表
	int MAIL_COMMAND_MAILLIST = MAIL_COMMAND_BASE + 1;
	// 查看邮件详细信息
	int MAIL_COMMAND_VIEW = MAIL_COMMAND_BASE + 2;
	//删除邮件
	int MAIL_COMMAND_DELETE = MAIL_COMMAND_BASE + 3;
	//发送邮件
	int MAIL_COMMAND_SEND = MAIL_COMMAND_BASE + 4;
	//领取邮件附件
	int MAIL_COMMAND_GET_ATTACH = MAIL_COMMAND_BASE + 5;
}
