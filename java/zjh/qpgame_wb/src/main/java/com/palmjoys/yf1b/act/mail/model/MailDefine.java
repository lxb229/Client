package com.palmjoys.yf1b.act.mail.model;

import com.palmjoys.yf1b.act.framework.annotation.SocketCode;
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
	// 查看邮件
	int MAIL_COMMAND_VIEW = MAIL_COMMAND_BASE + 2;

	// error id
	@SocketCode("邮件不存在")
	int MAIL_ERROR_UNEXIST = MAIL_ERROR_BASE - 1;
	@SocketCode("不存在的接收人")
	int MAIL_ERROR_RECVUSER_UNEXIST = MAIL_ERROR_BASE - 2;
	@SocketCode("邮件标题不能为空")
	int MAIL_ERROR_TITLE_EMPTY = MAIL_ERROR_BASE - 3;
	@SocketCode("邮件内容不能为空")
	int MAIL_ERROR_COMMENT_EMPTY = MAIL_ERROR_BASE - 4;
}
