package com.guse.chessgame.service.impl;

import java.util.Date;
import java.util.Random;

import org.chessgame.dao.bean.LoginLog;
import org.chessgame.dao.bean.Players;
import org.chessgame.dao.bean.Users;

import com.guse.chessgame.service.LoginLogService;
import com.guse.chessgame.service.LoginService;
import com.guse.chessgame.service.PlayerService;
import com.guse.chessgame.service.UserService;

public class LoginServiceImpl implements LoginService {

	
	UserService userService = new UserServiceImpl();
	PlayerService playerService = new PlayerServiceImpl();
	LoginLogService logService = new LoginLogServiceImpl();
	
	public Users login(String uid, String nickname,
			String head_portrait, int sex, String login_ip,
			int login_type) {
		/**根据uid获取用户对象*/
		Users user = userService.getUserByUid(uid);
		/**如果根据uid能获取用户对象说明用户已经存在，直接返回*/
		if(user != null) {
		} else {
			/**反之，不能根据uid获取用户对象则需要将用户信息先进行保存，早重新获取*/
			userService.saveUser(uid, nickname, head_portrait, sex, login_ip);
			user = userService.getUserByUid(uid);
			/**保存用户之后，还需要生成玩家对象*/
			Players palyer = new Players();
			palyer.setPlayer_serial_no(playerService.getPlayerSequence());
			palyer.setUser(user);
			palyer.setRoomcard_amount(0);
			playerService.savePalyer(palyer);
			
		}
		/**生成登录日志*/
		LoginLog log = new LoginLog();
		log.setUid(uid);
		log.setLogin_time(new Date());
		log.setLogin_ip(login_ip);
		log.setLogin_type(login_type);
		log.setLogin_result(1);
		log.setLogin_key(this.setLoginKey());
		logService.saveLoginLog(log);
		
		return user;
	}

	public static void main(String[] args) {
		LoginService login = new LoginServiceImpl();
//		Users user = login.login("59953cad1a3458904ed85rty", "游客heheda", "", 1, "192.168.10.202",1);
//		System.out.println(user.getUid()+"  "+user.getNickname()+"  "+user.getCreate_ip()+"  "+user.getStatus());
		System.out.println(login.setLoginKey());
	}

	public String setLoginKey() {
		Random random = new Random();
		int loginKey = random.nextInt(90000000)+10000000;
		return Integer.toString(loginKey);
	}
}
