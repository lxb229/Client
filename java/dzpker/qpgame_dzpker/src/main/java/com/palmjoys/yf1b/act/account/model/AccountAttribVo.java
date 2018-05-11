package com.palmjoys.yf1b.act.account.model;

import com.palmjoys.yf1b.act.wallet.model.WalletVo;

public class AccountAttribVo {
	//帐号Id
	public String accountId;
	//游客或微信唯一uuid
	public String uuid;
	//角色属性数据
	public RoleAttribVo roleAttribVo;
	//钱包数据
	public WalletVo walletVo;
	//游戏数据
	public GameDataAttrib gameDataAttribVo;
	//帐号认证相关数据
	public AuthenticationVo authenticationVo;
	
	public AccountAttribVo(){
		this.roleAttribVo = new RoleAttribVo();
		this.walletVo = new WalletVo();
		this.gameDataAttribVo = new GameDataAttrib();
		this.authenticationVo = new AuthenticationVo();
	}
}
