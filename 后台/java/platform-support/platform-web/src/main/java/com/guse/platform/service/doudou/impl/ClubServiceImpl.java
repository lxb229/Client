package com.guse.platform.service.doudou.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.common.utils.PropertyConfigurer;
import com.guse.platform.dao.doudou.ClubMapper;
import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.service.doudou.ClubService;
import com.guse.platform.service.doudou.ClubUserService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.entity.doudou.Club;
import com.guse.platform.entity.doudou.ClubUser;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.system.Users;

/**
 * club
 * @author nbin
 * @version V1.0
 */
@Service
public class ClubServiceImpl extends BaseServiceImpl<Club, java.lang.Integer> implements ClubService{
	private static Logger logger = LoggerFactory.getLogger(ClubServiceImpl.class);

	@Autowired
	private  PropertyConfigurer configurer;
	@Autowired
	private ClubMapper  clubMapper;
	@Autowired
	private UsersService userService;
	@Autowired
	private SystemTaskMapper taskMapper;
	@Autowired
	private ClubUserService clubUserService;
	
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(clubMapper);
	}

	@Override
	public Result<PageResult<Club>> queryPageListByClub(Club club, PageBean pageBean,Users user) {
		if (pageBean == null) {
			logger.info("获取俱乐部列表失败，pageBean is null ！");
			return new Result<PageResult<Club>>(00000, "获取俱乐部列表失败！");
		}
		Long count = clubMapper.countByParam(club, user);
		if (count <= 0) {
			return new Result<PageResult<Club>>(new PageResult<Club>().initNullPage());
		}
		//
		String orderType = "c_create_time";
		int sortType = club.getCuserId();
		if(sortType == 1) {
			orderType = "id";
		} else if(sortType == 2) {
			orderType = "create_time";
		}
		int sortStyle  = club.getCid(); 
		if(sortStyle == 1 ) {
			orderType += " asc ";
		} else {
			orderType += " desc ";
		}
		
		List<Club> list = clubMapper.selectPageByParam(club,
				new PageResult<Club>(pageBean.getPageNo(), pageBean.getPageSize(), count, orderType), user);
		PageResult<Club> pageResult = null;
		pageResult = new PageResult<>(pageBean.getPageNo(), pageBean.getPageSize(), count, orderType);
		pageResult.setList(list);
		return new Result<PageResult<Club>>(pageResult);
	}
	
	@Override
	public Result<Integer> enableDisableClub(Club club) {
		if (null == club || null == club.getCid() || null == club.getCstate() || null == club.getCno()) {
			return new Result<Integer>(00000, "启禁用俱乐部失败！");
		}
		Result<Integer> result = manageGameClub(club);
		if(!result.isOk()) {
			return result;
		}
		return new Result<Integer>(clubMapper.updateByIdSelective(club));
	}
	
	@Override
	public Result<Integer> manageGameClub(Club club) {
		
		String serviceAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_corps_lock_corps";
		String cno = club.getCno();
		if(club.getCstate() == 0) {
			/**冻结俱乐部*/
			cmd = "gm_corps_lock_corps";
		} else {
			/**解冻俱乐部*/
			cmd = "gm_corps_unlock_corps";
		}
		serviceAddress += cmd+"%20"+cno;
		String result = null;
        try {
        	result = HttpClientUtil.httpGet(serviceAddress);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return new Result<Integer>(00000,"获取接口地址异常!"); 
		}
        logger.info("获取接口地址:{}",result);
        if (StringUtils.isNotBlank(result)) {
            //JSON格式转换
            JSONObject obj = JSONObject.parseObject(result);
            int success = obj.getIntValue("code");
            if(success >= 0) {
            	return new Result<Integer>(success);
            } else {
            	return new Result<Integer>(00000,obj.getString("content")); 
            }
        } else {
        	return new Result<Integer>(00000,"返回结果超时!");
        }
	}

	@Override
	public Club getClubByAccount(String accountId) {
		if ( accountId != null) {
			Club club = clubMapper.getClubByAccount(accountId);
			return club;
		}
		return null;
	}

	@Override
	public Result<Integer> saveOrUpdateClub(Club club) {
		ValidataBean validata = club.validateModel();
		if (!validata.isFlag()) {
			return new Result<Integer>(00000, validata.getMsg());
		}
		Integer result = null;
		if (null != club.getCid()) {
			result = clubMapper.updateByIdSelective(club);
		} else {
			result = clubMapper.insert(club);
		}
		return new Result<Integer>(result);
	}

	@Override
	public Integer getNextClubId() {
		
		return clubMapper.getNextClubId();
	}

	@Override
	public Result<Integer> gmDeleteClub(Club club) {
		//查询该群主是否还有其他俱乐部，如果没有则取消用户群主权限
		List<Club> agencyList = clubMapper.selectClubOut(club);
		if(agencyList != null && agencyList.size() > 0) {
		} else {
			Users user = userService.selectUserDetial(club.getCuserId()).getData();
			if(user.getRoleId() == 14) {
				/**更新玩家权限为普通成员*/
				user.setRoleId(15);
				userService.saveOrUpdateUser(user);
			}
		}
		return new Result<Integer>(agencyList.size());
	}

	@Override
	public void taskClubs(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getJsonContent());
		Result<Integer> success = processingClubs(obj);
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
	public Result<Integer> processingClubs(JSONObject obj) {
		if(obj == null) {
			return new Result<Integer>(00000,"MQ数据异常!");
		} else {
			Club club = null; 
			ClubUser clubUser = null;
			int cmd = obj.getIntValue("cmd");
			switch (cmd) {
			   // 创建俱乐部
				case 1:
					club = new Club();
					club.setCno(obj.getString("club_no"));
					club.setName(obj.getString("club_name"));
					Users users = userService.getUserByAccountId(Long.parseLong(obj.getString("create_id")));
					if(users == null) {
						return new Result<Integer>(00000,"俱乐部数据，cmd=1，未查询到玩家");
					} else if(users.getRoleId() == 15) {
						/**更新玩家状态为群主*/
						users.setRoleId(14);
						userService.saveOrUpdateUser(users);
					}
					club.setCuserId(users.getUserId());
					club.setCcreateTime(new Date(new Long(obj.getString("create_time"))));
					club.setCstate(1);
					// 生成群主的俱乐部成员
					clubUser = new ClubUser();
					clubUser.setClubId(this.getNextClubId());
					clubUser.setCuUserId(users.getUserId());
					clubUser.setCuType("1");
					clubUser.setCuCreateTime(new Date(new Long(obj.getString("create_time"))));
					clubUser.setCuState("1");
					
					break;
				// 解散俱乐部
				case 2:
					club = this.getClubByAccount(obj.getString("club_no"));
					if(club == null) {
						return new Result<Integer>(00000,"俱乐部数据，cmd=2，未查询到俱乐部");
					} else if(club != null ) {
						club.setCstate(-1);
						this.gmDeleteClub(club);
					} 
					break;
	
				default:
					break;
			}
			if(club == null) {
				return new Result<Integer>(00000,"俱乐部数据异常!");
			} else {
				Result<Integer> result = this.saveOrUpdateClub(club);
				if(!result.isOk()) {
					return result;
				}
				if(clubUser != null) {
					result = clubUserService.saveOrUpdateClubUser(clubUser);
				}
				return result;
			}
		}
	}

}
