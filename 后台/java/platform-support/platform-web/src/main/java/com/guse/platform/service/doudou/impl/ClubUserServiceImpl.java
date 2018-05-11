package com.guse.platform.service.doudou.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.dao.doudou.ClubUserMapper;
import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.service.doudou.ClubService;
import com.guse.platform.service.doudou.ClubUserService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.entity.doudou.Club;
import com.guse.platform.entity.doudou.ClubUser;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.system.Users;

/**
 * club_user
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class ClubUserServiceImpl extends BaseServiceImpl<ClubUser, java.lang.Integer> implements ClubUserService{

	private static Logger logger = LoggerFactory.getLogger(ClubUserServiceImpl.class);

	@Autowired
	private UsersService userService;
	@Autowired
	private SystemTaskMapper taskMapper;
	@Autowired
	private ClubUserMapper  clubUserMapper;
	@Autowired
	private ClubService clubService;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(clubUserMapper);
	}

	@Override
	public Result<Integer> saveOrUpdateClubUser(ClubUser clubUser) {
		ValidataBean validata = clubUser.validateModel();
		if (!validata.isFlag()) {
			return new Result<Integer>(00000, validata.getMsg());
		}
		Integer result = null;
		if (null != clubUser.getCuId()) {
			result = clubUserMapper.updateByIdSelective(clubUser);
		} else {
			result = clubUserMapper.insert(clubUser);
		}
		return new Result<Integer>(result);
	}

	@Override
	public ClubUser getClubUserBy(Integer clubId, Integer userId) {
		
		return clubUserMapper.getClubUserBy(clubId ,userId);
	}

	@Override
	public Result<Integer> deleteClubUser(Integer cuId) {
		if(null == cuId){
			return new Result<Integer>(00000,"删除俱乐部玩家失败，参数异常！");
		}
		return new Result<Integer>(clubUserMapper.deleteById(cuId));
	}

	@Override
	public void taskClubUsers(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getJsonContent());
		Result<Integer> success = processingClubUsers(obj);
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
	public Result<Integer> processingClubUsers(JSONObject obj) {
		if(obj == null) {
			return new Result<Integer>(00000,"MQ数据异常!");
		} else {
			Club club = clubService.getClubByAccount(obj.getString("club_id"));
			if(club == null) {
				return new Result<Integer>(00000,"俱乐部玩家，未查询到俱乐部");
			}
			Users users = userService.getUserByAccountId(Long.parseLong(obj.getString("user_id")));
			if(users == null) {
				return new Result<Integer>(00000,"俱乐部玩家，未查询到玩家");
			}
			ClubUser clubUser = null;
			Result<Integer> result = null;
			int cmd = obj.getIntValue("cmd");
			switch (cmd) {
			// 加入俱乐部
				case 1:
					clubUser = new ClubUser();
					clubUser.setClubId(club.getCid());
					clubUser.setCuUserId(users.getUserId());
					clubUser.setCuType("0");
					clubUser.setCuCreateTime(new Date(new Long(obj.getString("operate_time"))));
					clubUser.setCuState("1");
					result = this.saveOrUpdateClubUser(clubUser);
					break;
				// 退出俱乐部
				case 2:
					clubUser = this.getClubUserBy(club.getCid(), users.getUserId());
					if(clubUser == null) {
						return new Result<Integer>(00000,"俱乐部玩家，cmd=2，未查询到俱乐部玩家");
					}
					result = this.deleteClubUser(clubUser.getCuId());
					break;
				// 踢出俱乐部
				case 3:
					clubUser = this.getClubUserBy(club.getCid(), users.getUserId());
					if(clubUser == null) {
						return new Result<Integer>(00000,"俱乐部玩家，cmd=3，未查询到俱乐部玩家");
					}
					result = this.deleteClubUser(clubUser.getCuId());
					break;
				default:
					break;	
			}
			return result;
		}
	}
}
