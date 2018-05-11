package com.guse.platform.service.system.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.common.utils.RightsHelper;
import com.guse.platform.common.utils.encrypt.QEncodeUtil;
import com.guse.platform.dao.doudou.GoldLogMapper;
import com.guse.platform.dao.system.MenusMapper;
import com.guse.platform.dao.system.ResourceMapper;
import com.guse.platform.dao.system.RolesMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.GoldLog;
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Resource;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.vo.system.AccountVo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * @author nbin
 * @version 1.0
 * @CreateDate 201-7-26 - 上午11:34:26
 */
@Service("userService")
public class UsersServiceImpl implements UsersService {

	private static Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
	private Gson gson = new Gson();
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private MenusMapper menusMapper;
	@Autowired
	private RolesMapper rolesMapper;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private  PropertyConfigurer configurer;
	@Autowired
	private GoldLogMapper  goldLogMapper;

	@Override
	public Result<Users> loginUser(String username, String password) {
		logger.info("用户登录：username={},password={}", username, password);
		Result<Users> result = new Result<Users>();
		if (StringUtils.isBlank(username)) {
			return new Result<Users>(00000, "用户名不能为空！");
		}
		if (StringUtils.isBlank(password)) {
			return new Result<Users>(00000, "密码不能为空！");
		}
		Users user = usersMapper.getUserByLoginName(username);
		if (null == user) {
			return new Result<Users>(00000, "用户不存在！");
		}
		if(user.getUserStatus() != 1) {
			return new Result<Users>(00000, "账号被禁用，不能登录，请联系管理员！");
		}
		try {
			String pwdPsss = QEncodeUtil.md5Encrypt(password);
			System.out.println("加密密码：" + pwdPsss);
			System.out.println("用户密码：" + user.getLoginPass());
			if (user.getLoginPass().equals(pwdPsss)) {
				result.setData(user);
			} else {
				return new Result<Users>(00000, "密码输入错误！");
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("用户登录密码转换失败！", e);
			return new Result<Users>(00000, "登录失败！");
		} catch (Exception e) {
			logger.error("用户登录失败:" + e.getMessage(), e);
			return new Result<Users>(00000, "登录失败！");
		}
		
		//超级管理员
		if(user.getLoginName().equals("admin")) {
			List<Menus> menus = this.menusMapper.selectAllMenusList();
			user.setMenusList(menus);
			List<Long> menusIdList = new ArrayList<Long>();
			if(menus != null) {
				for (Menus menu : menus) {
					menusIdList.add(Long.parseLong(menu.getMenuId()+""));
				}
			}
			List<Resource> resourceList = this.resourceMapper.selectResourceListByMenuIds(menusIdList);
			user.setResourceList(resourceList);
		}
		//普通用户
		else if(user.getRoleId() != null) {
			Roles role = this.rolesMapper.selectByPrimaryKey(user.getRoleId());
			List<Long> menusIdList = new ArrayList<Long>();
			//获取用户的菜单列表
			if(role != null && role.getMenuRights() != null) {
				List<Menus> menus = this.menusMapper.selectAllMenusList();
				if(menus != null) {
					user.setMenusList(new ArrayList<Menus>());
					for (Menus menus2 : menus) {
						if(RightsHelper.testRights(role.getMenuRights().toString(), menus2.getMenuId().toString())) {
							user.getMenusList().add(menus2);
							menusIdList.add(Long.parseLong(menus2.getMenuId()+""));
						}
					}
				}
			}
			//获取用户的资源列表
			if(role != null && role.getResourceRights() != null) {
				List<Resource> resourceList = this.resourceMapper.selectResourceListByMenuIds(menusIdList);
				if(resourceList != null) {
					user.setResourceList(new ArrayList<Resource>());
					for (Resource resource : resourceList) {
						if(RightsHelper.testRights(role.getResourceRights().toString(), resource.getSrId().toString())) {
							user.getResourceList().add(resource);
						}						
					}
				}
			}
		}
		
		//登录成功
		result.setData(user);
		result.setErrorCode(10000);
		
		return result;
	}

	@Override
	public Result<PageResult<Users>> queryPageListByUsers(Users user, PageBean pageBean) {
		
		
		if (pageBean == null) {
			logger.info("获取用户列表失败，pageBean is null ！");
			return new Result<PageResult<Users>>(00000, "获取用户列表失败！");
		}
		Long count = usersMapper.countByParam(user);
		if (count <= 0) {
			return new Result<PageResult<Users>>(new PageResult<Users>().initNullPage());
		}
		//
		List<Users> list = usersMapper.selectPageByParam(user,
				new PageResult<Users>(pageBean.getPageNo(), pageBean.getPageSize(), count, ""));
		PageResult<Users> pageResult = null;
		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
		pageResult.setList(list);
		return new Result<PageResult<Users>>(pageResult);
	}
	
	@Override
	public Result<PageResult<AccountVo>> queryAccountList(Users user, PageBean pageBean) {
		if (pageBean == null) {
			logger.info("获取用户列表失败，pageBean is null ！");
			return new Result<PageResult<AccountVo>>(00000, "获取用户列表失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "";
		if(user != null && user.getLoginName() != null && user.getLoginName().length() == 6 ) {
			String starNO = user.getLoginName();
			cmd="gm_account_query_by_starNO";
			lisboaAddress = lisboaAddress+cmd+"%20"+starNO;
		} else if(user != null && user.getLoginName() != null && user.getLoginName().length() == 11 ) {
			String phone = user.getLoginName();
			cmd="gm_account_query_by_phone";
			lisboaAddress = lisboaAddress+cmd+"%20"+phone;
		} else if(user == null || StringUtils.isBlank(user.getLoginName())) {
			int start = (pageBean.getPageNo()-1)*pageBean.getPageSize();
			int num = pageBean.getPageSize();
			int sortStyle  = user.getUserStatus();
			int sortType = user.getRoleId();
			cmd="gm_account_query_account_list";
			lisboaAddress = lisboaAddress+cmd+"%20"+start+"%20"+num+"%20"+sortStyle+"%20"+sortType;
		} else {
			return new Result<PageResult<AccountVo>>(new PageResult<AccountVo>().initNullPage());
		}
		
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success == 0) {
            	String content = obj.getString("content");
            	JSONObject listObj = JSONObject.parseObject(content);
            	int totalNum = listObj.getIntValue("totalNum");
            	Long count = new Long((long)totalNum);
        		if (count <= 0) {
        			return new Result<PageResult<AccountVo>>(new PageResult<AccountVo>().initNullPage());
        		}
        		
        		List<AccountVo> list = gson.fromJson(listObj.getString("items"), new TypeToken<List<AccountVo>>(){}.getType()); 
        		for (int i = 0; i < list.size(); i++) {
            		AccountVo accountVo = list.get(i);
            		accountVo.setTheLastTime(new Date(new Long(accountVo.getLastTime())));
            		list.set(i, accountVo);
            	}
        		PageResult<AccountVo> pageResult = null;
        		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, "");
        		pageResult.setList(list);
        		return new Result<PageResult<AccountVo>>(pageResult);
        		
            } else {
            	return new Result<PageResult<AccountVo>>(00000,obj.getString("content")); 
            }
            
        } else {
        	return new Result<PageResult<AccountVo>>(00000,"返回结果超时!");
        }
	}
	@Override
	public Result<Integer> updateRoomcard(AccountVo accountVo) {
		if (accountVo == null) {
			logger.info("增减金币失败，accountVo is null ！");
			return new Result<Integer>(00000, "增减金币失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_wallet_add_roomcard";
		String starNO = accountVo.getStarNO();
		int num = accountVo.getJbState()*accountVo.getJbAmount();
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+starNO+"%20"+num);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
        	   	//生成金币记录
        	   	GoldLog goldLog = new GoldLog();
        	   	goldLog.setStarNo(starNO);
        	   	goldLog.setTheEventTime(new Date());
        	   	goldLog.setScore(num);
        	   	goldLog.setGameType(99);// 系统增加减少金币
        	   	success = goldLogMapper.insert(goldLog); 
	       		return new Result<Integer>(success);
       		
           } else {
           	return new Result<Integer>(00000,obj.getString("content")); 
           }
           
       } else {
       	return new Result<Integer>(00000,"返回结果超时!");
       }
	}
	
	@Override
	public Result<Integer> updateUserPass(AccountVo accountVo) {
		if (accountVo == null) {
			logger.info("修改密码失败，accountVo is null ！");
			return new Result<Integer>(00000, "修改密码失败！");
		}
		String lisboaAddress = configurer.getProperty("lisboaAddress");
		String cmd = "gm_account_modfiy_password";
		String starNO = accountVo.getStarNO();
		String newPassword = accountVo.getLoginPass();
		if(StringUtils.isBlank(newPassword)) {
			return new Result<Integer>(00000,"新密码不能为空!");
		} else if(newPassword.trim().length() != 6) {
			return new Result<Integer>(00000,"密码必须为6位!");
		}
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+starNO+"%20"+newPassword);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
	       		return new Result<Integer>(success);
           } else {
           	return new Result<Integer>(00000,obj.getString("content")); 
           }
       } else {
       	return new Result<Integer>(00000,"返回结果超时!");
       }
	}
	
	@Override
	public Result<Integer> saveOrUpdateUser(Users user) {
		ValidataBean validata = user.validateModel();
		if (!validata.isFlag()) {
			return new Result<Integer>(00000, validata.getMsg());
		}
		if (null == user.getUserId()) {
			try {
				if(user.getLoginPass() == null) {
					user.setLoginPass(Constant.AES_INIT_PASS);
				}
				user.setLoginPass(QEncodeUtil.md5Encrypt(user.getLoginPass()));
			} catch (Exception e) {
				return new Result<Integer>(00000, "密码转换失败！");
			}
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
		}
		Integer result = null;
		if (null != user.getUserId()) {
			// 更新用户不可以更改登录名
			user.setLoginName(null);
			result = usersMapper.updateByPrimaryKeySelective(user);
		} else {
			// 新建用户登录名不能重复
			Users existUser = usersMapper.getUserByLoginName(user.getLoginName());
			if (existUser != null) {
				return new Result<Integer>(00000, "该用户已存在！");
			}
			result = usersMapper.insert(user);
		}
		return new Result<Integer>(result);
	}

	@Override
	public Result<Integer> delectUser(Integer userId) {
		if (null == userId) {
			return new Result<Integer>(00000, "删除用户失败，参数异常！");
		}
		Users users = usersMapper.selectByPrimaryKey(userId);
		if(users == null) {
			return new Result<Integer>(00000, "用户不存在！");
		}
		if(users.getLoginName().equals("admin")) {
			return new Result<Integer>(00000, "删除用户失败，超管不能删除！");
		}
		return new Result<Integer>(usersMapper.deleteByPrimaryKey(userId));
	}

	@Override
	public Result<Integer> enableDisableUser(Users user) {
		if (null == user || null == user.getUserId() || null == user.getUserStatus()) {
			return new Result<Integer>(00000, "启禁用用户失败！");
		}
		Users users = usersMapper.selectByPrimaryKey(user.getUserId());
		if(users == null) {
			return new Result<Integer>(00000, "用户不存在！");
		}
		if(users.getLoginName().equals("admin")) {
			return new Result<Integer>(00000, "超管用户不能操作！");
		}
		user.setUpdateTime(new Date());
		return new Result<Integer>(usersMapper.updateByPrimaryKeySelective(user));
	}

	@Override
	public Result<Users> selectUserDetial(Integer userId) {
		if (null == userId) {
			return new Result<Users>(00000, "获取用户信息失败，参数异常！");
		}
		return new Result<Users>(this.usersMapper.selectByPrimaryKey(userId));
	}

	@Override
	public Result<Integer> setUserRole(Integer userId, Integer roleId) {
		Result<Integer> result = new Result<Integer>();
		if (userId == null || roleId == null) {
			result.setErrorCode(00000);
			result.setErrorMsg("参数不能为空!");
			return result;
		}
		Users user = new Users();
		user.setUserId(userId);
		user.setRoleId(roleId);
		try {
			int oknum = usersMapper.updateByPrimaryKeySelective(user);
			result.setData(oknum);
		} catch (Exception e) {
			result.setErrorCode(00000);
			result.setErrorMsg("设置用户权限失败!");
			logger.error("设置用户权限失败：" + e.getMessage(), e);
		}
		return result;
	}

}
