package com.palmjoys.yf1b.act.framework.account.model;

import com.palmjoys.yf1b.act.wallet.model.WalletVo;

public class AccountAttribVo {
	//帐号Id
	public String accountId;
	//游客或微信唯一uuid
	public String uuid;
	//明星号
	public String starNO;
	//角色头像
	public String headImg;
	//性别(0=保密码,1=男,2=女)
	public int sex;
	//角色呢称
	public String nick;
	//角色钱包
	public WalletVo wallet;
	//角色桌子号
	public int tableId;
	//帮定的手机号
	public String phone;
	//实名认证标识(1=已认证)
	public int authenticationFlag;
	//官方WX号
	public String wxNO="";
}
