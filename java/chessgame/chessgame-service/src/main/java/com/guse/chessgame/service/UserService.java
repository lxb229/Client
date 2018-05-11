package com.guse.chessgame.service;

import java.util.List;

import org.chessgame.dao.bean.Users;
import org.chessgame.dao.vo.UserVo;

public interface UserService {
	
	/**
	 * 保存用户
	 * @param uid 用户唯一标识
	 * @param utype 登录类型
	 * @param nickname 昵称
	 * @param head_portrait 头像
	 * @param sex 性别
	 * @param create_ip 注册IP
	 * @return 对象在数据库中的ObjectId
	 */
	public String saveUser(String uid, String nickname, String head_portrait, int sex, String create_ip);
	
	/**
	 * 保存用户
	 * @param user 用户对象
	 * @return 对象在数据库中的ObjectId
	 */
	public String saveUser(Users user);
	
	/**
	 * 根据uid获取用户对象
	 * @param uid 用户唯一标识
	 * @return
	 */
	public Users getUserByUid(String uid);
	
	/**
	 * 根据搜索条件查询符合条件的用户对象集合
	 * @param userVo 搜索条件
	 * @return 符合条件的用户对象集合
	 */
	public List<Users> getUsersBySeach(UserVo userVo);
	
	/**
	 * 更新用户信息
	 * @param user 用户最新信息
	 * @return 修改的数据量
	 */
	public int updateUser(Users user);
	
	/**
	 * 删除用户(假删除，将用户状态设置为无效)
	 * @param user 用户对象
	 * @return 更新数量
	 */
	public int deleteUser(Users user);
	
}
