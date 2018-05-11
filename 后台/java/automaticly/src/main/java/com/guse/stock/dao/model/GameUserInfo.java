package com.guse.stock.dao.model;

import org.apache.ibatis.type.Alias;

/** 
* @ClassName: GameUserInfo 
* @Description: 游戏关联信息
* @author Fily GUSE
* @date 2017年9月27日 下午5:46:05 
*  
*/
@Alias("gameUserInfo")
public class GameUserInfo {
	
	// 主键
	private Long id;
	// 用户编号
	private Long user_id;
	// 游戏编号
	private Long game_id;
	// qq
	private String qq;
	// 微信号
	private String weixin;
	// 游戏信息
	private String info;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGame_id() {
		return game_id;
	}
	public void setGame_id(Long game_id) {
		this.game_id = game_id;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
}
