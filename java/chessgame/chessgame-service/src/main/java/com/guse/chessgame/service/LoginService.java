package com.guse.chessgame.service;

import org.chessgame.dao.bean.Users;

public interface LoginService {
	
	/**
	 * 用户登录
	 * @param uid 用户唯一标识
	 * @param nickname 昵称
	 * @param head_portrait 头像
	 * @param sex 性别
	 * @param login_ip 登录IP
	 * @param login_type 登录类型 1：iOS 2：Android 
	 * @return 登录用户
	 */
	public Users login(String uid, String nickname, String head_portrait, int sex, String login_ip, int login_type);
	
	/**
	 * 生成8位数加解密Key
	 * @return 加解密key
	 */
	public String setLoginKey();

}
