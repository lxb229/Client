package com.guse.platform.service.system.impl;

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
import com.guse.platform.common.utils.GameGMUtil;
import com.guse.platform.common.utils.RightsHelper;
import com.guse.platform.common.utils.encrypt.QEncodeUtil;
import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.dao.system.MenusMapper;
import com.guse.platform.dao.system.ResourceMapper;
import com.guse.platform.dao.system.RolesMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.City;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.system.Menus;
import com.guse.platform.entity.system.Resource;
import com.guse.platform.entity.system.Roles;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.CityService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.vo.doudou.AccountVo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;


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
	private GameGMUtil gameGMUtil;
	@Autowired
	private SystemTaskMapper taskMapper;
	@Autowired
	private CityService cityService;

	@Override
	public Result<Users> loginUser(String username, String password) {
		logger.info("用户登录：username={},password={}", username, password);
		Result<Users> result = new Result<Users>();
		if (StringUtils.isBlank(username)) {
			return new Result<Users>(00000, "电话不能为空！");
		}
		if (StringUtils.isBlank(password)) {
			return new Result<Users>(00000, "验证码不能为空！");
		}
//		Users user = usersMapper.getUserByLoginName(username);
		Users user = usersMapper.getUserByPhone(username);
		if (null == user) {
			return new Result<Users>(00000, "用户不存在！");
		}
		if(user.getUserStatus() != 0) {
			return new Result<Users>(00000, "账号被禁用，不能登录，请联系管理员！");
		}
		try {
//			String pwdPsss = QEncodeUtil.md5Encrypt(password);
//			System.out.println("加密密码：" + pwdPsss);
//			System.out.println("用户密码：" + user.getLoginPass());
			System.out.println(user.getVerCode());
			System.out.println(password);
			if (Integer.parseInt(user.getVerCode()) == Integer.parseInt(password)) {
				long nowTime=System.currentTimeMillis();   //获取当前时间
				long sendTime = user.getSendTime().getTime();
				if((nowTime - sendTime) < 300000) {
					result.setData(user);
				} else {
					return new Result<Users>(00000, "验证码过期！");
				}
			} else {
				return new Result<Users>(00000, "验证码输入错误！");
			}
		} 
//		catch (UnsupportedEncodingException e) {
//			logger.info("用户登录密码转换失败！", e);
//			return new Result<Users>(00000, "登录失败！");
//		} 
		catch (Exception e) {
			logger.error("用户登录失败:" + e.getMessage(), e);
			return new Result<Users>(00000, "登录失败！");
		}
		
		//超级管理员
		if(user.getLoginName() != null && user.getLoginName().equals("admin")) {
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
	public Result<PageResult<Users>> queryPageListByUsers(Users user, PageBean pageBean, Users roleuser) {
		if (pageBean == null) {
			logger.info("获取用户列表失败，pageBean is null ！");
			return new Result<PageResult<Users>>(00000, "获取用户列表失败！");
		}
		Long count = usersMapper.countByParam(user, roleuser);
		if (count <= 0) {
			return new Result<PageResult<Users>>(new PageResult<Users>().initNullPage());
		}
		//
		String orderType = "create_time";
		int sortType = user.getRoleId();
		if(sortType == 1) {
			orderType = "create_time";
		} else if(sortType == 2) {
			orderType = "update_time";
		}
		String sortStyle  = user.getLoginPass(); 
		if(sortStyle.equals("1")) {
			orderType += " asc ";
		} else {
			orderType += " desc ";
		}
		List<Users> list = usersMapper.selectPageByParam(user,
				new PageResult<Users>(pageBean.getPageNo(), pageBean.getPageSize(), count, orderType),roleuser);
		PageResult<Users> pageResult = null;
		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, orderType);
		pageResult.setList(list);
		return new Result<PageResult<Users>>(pageResult);
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
			if(user.getCreateTime() == null) {
				user.setCreateTime(new Date());
			}
			user.setUpdateTime(new Date());
		}
		Users phoneUser = null;
		if(StringUtils.isNotBlank(user.getPhone())) {
			phoneUser = usersMapper.getUserByPhone(user.getPhone());
		}
		Integer result = null;
		if (null != user.getUserId()) {
			if(phoneUser != null && phoneUser.getUserId() != user.getUserId()) {
				return new Result<Integer>(00000, "电话号码重复！");
			}
			// 更新用户不可以更改登录名
			user.setLoginName(null);
			result = usersMapper.updateByPrimaryKeySelective(user);
		} else {
			// 新建用户电话号码不能重复，昵称可以重复
//			Users existUser = usersMapper.getUserByLoginName(user.getLoginName());
//			Users existUser = usersMapper.getUserByNick(user.getNick());
//			if (existUser != null) {
//				return new Result<Integer>(00000, "用户已存在！");
//			}
			if (phoneUser != null) {
				return new Result<Integer>(00000, "电话号码重复！");
			}
			user.setRoleId(15);
			user.setUserStatus(0);
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
		if(StringUtils.isNotBlank(users.getLoginName()) && users.getLoginName().equals("admin")) {
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
//		if(users.getLoginName().equals("admin")) {
//			return new Result<Integer>(00000, "超管用户不能操作！");
//		}
		user.setUpdateTime(new Date());
		Users userVo = selectUserDetial(user.getUserId()).getData();
		AccountVo accountVo = new AccountVo();
		accountVo.setCmd(1);
		accountVo.setStarNo(userVo.getStarNo());
		accountVo.setState(user.getUserStatus());
		Result<Integer> success = gameGMUtil.accountService(accountVo);
		if(!success.isOk()) {
			return success;
		}
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

	@Override
	public Users getUserByStartNo(Long accountId,String startNo) {
		if (startNo != null && accountId != null) {
			Users user = usersMapper.getUserByStartNo(accountId,startNo);
			return user;
		}
		return null;
	}

	@Override
	public Users getUserByAccountId(Long accountId) {
		if ( accountId != null) {
			Users user = usersMapper.getUserByAccountId(accountId);
			return user;
		}
		return null;
	}

	@Override
	public Result<Integer> sendVerCode(String phone, String smsCode) {
		Users users = usersMapper.getUserByPhone(phone);
		if(users == null) {
			return new Result<Integer>(00000,"电话号码绑定错误！");
		} else {
			users.setVerCode(smsCode);
			users.setSendTime(new Date());
			return this.saveOrUpdateUser(users);
		}
	}

	@Override
	public void taskUsers(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getJsonContent());
		Result<Integer> success = processingUser(obj);
		if(success.isOk()) {
			task.setTaskStatus(1);
		} else {
			logger.info(success.getErrorMsg());
			task.setTaskStatus(2);
		}
		task.setTaskNum(task.getTaskNum()+1);
		taskMapper.updateByIdSelective(task);
		
	}

	@Override
	public Result<Integer> processingUser(JSONObject obj) {
		Users users = null;
		if(obj == null) {
			return new Result<Integer>(00000,"MQ数据异常!");
		} else {
			int cmd = obj.getIntValue("cmd");
			switch (cmd) {
				// 玩家注册
				case 1:
					users = new Users();
					users.setRoleId(15);
					users.setAccountId(Long.parseLong(obj.getString("account_id")));
					users.setStarNo(obj.getString("start_no"));
					users.setNick(obj.getString("nick"));
					users.setSex(Integer.parseInt(obj.getString("sex")));
					users.setHeadImg(obj.getString("head_img"));
					users.setUserStatus(Integer.parseInt(obj.getString("user_status")));
					users.setCreateTime(obj.getDate("create_time"));
					users.setCreateIp(obj.getString("create_ip"));
					City city = cityService.getCityByIp(obj.getString("create_ip"));
					if(city != null) {
						users.setArea(city.getId());
					}
					break;
				// 玩家信息修改
				case 2:
					users = this.getUserByStartNo(Long.parseLong(obj.getString("account_id")),obj.getString("start_no"));
					if(users != null) {
						users.setNick(obj.getString("nick"));
						users.setSex(Integer.parseInt(obj.getString("sex")));
						users.setHeadImg(obj.getString("head_img"));
						users.setUserStatus(Integer.parseInt(obj.getString("user_status")));
					} else {
						return new Result<Integer>(00000,"cmd=2,未查询到玩家");
					}
					break;	
				// 玩家绑定手机
				case 3:
					users = this.getUserByStartNo(Long.parseLong(obj.getString("account_id")),obj.getString("start_no"));
					if(users != null) {
						users.setPhone(obj.getString("phone"));
					} else {
						return new Result<Integer>(00000,"cmd=3;未查询到玩家");
					}
					break;	
				// 玩家实名认证
				case 4:
					users = this.getUserByStartNo(Long.parseLong(obj.getString("account_id")),obj.getString("start_no"));
					if(users != null) {
						users.setRealName(obj.getString("real_name"));
						users.setCardNo(obj.getString("card_no"));
					} else {
						return new Result<Integer>(00000,"cmd=4;未查询到玩家");
					}
					break;	
				default:
					
					break;
			}
		   if(users == null) {
			   return new Result<Integer>(00000,"用户数据异常!");
		   } else {
			   return  this.saveOrUpdateUser(users);
		   }
		}
	}

}
