package com.guse.chessgame.service.impl;

import java.lang.reflect.InvocationTargetException;

import org.chessgame.dao.PlayerDao;
import org.chessgame.dao.UsersDao;
import org.chessgame.dao.bean.Players;
import org.chessgame.dao.bean.Users;

import com.guse.chessgame.service.PlayerService;

public class PlayerServiceImpl implements PlayerService {
	

	public String savePalyer(Players player) {
		PlayerDao playerDao = new PlayerDao();
		try {
			return playerDao.savePlayer(player);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			playerDao.closeDao();
		}
		return null;
	}

	public int updatePlayer(Players player) {
		PlayerDao playerDao = new PlayerDao();
		try {
			return playerDao.updatePlayer(player);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			playerDao.closeDao();
		}
		return 0;
	}

	public Players seachPalyer(String uid) {
		PlayerDao playerDao = new PlayerDao();
		UsersDao usersDao = new UsersDao();
		try {
			Users user = usersDao.seachUser(uid);
			return playerDao.seachPlayer(user);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getPlayerSequence() {
		PlayerDao playerDao = new PlayerDao();
		try {
			return playerDao.getPlayerSequence();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			playerDao.closeDao();
		}
		return 0;
	}

}
