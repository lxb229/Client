package com.guse.platform.service.doudou.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.GameGMUtil;
import com.guse.platform.dao.doudou.SystemLuckMapper;
import com.guse.platform.dao.doudou.SystemTaskMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.service.doudou.SystemLuckService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.vo.doudou.AccountVo;
import com.guse.platform.entity.doudou.SystemLuck;
import com.guse.platform.entity.doudou.SystemTask;
import com.guse.platform.entity.system.Users;

/**
 * system_luck
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemLuckServiceImpl extends BaseServiceImpl<SystemLuck, java.lang.Integer> implements SystemLuckService{
	
	private static Logger logger = LoggerFactory.getLogger(SystemLuckServiceImpl.class);

	@Autowired
	private SystemTaskMapper taskMapper;
	@Autowired
	private SystemLuckMapper  systemLuckMapper;
	@Autowired
	private GameGMUtil gameGMUtil;
	@Autowired
	private UsersService usersService;
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemLuckMapper);
	}

	@Override
	public Result<Integer> saveOrUpdateLuck(SystemLuck luck) {
		Users users = usersMapper.getUserBy(luck.getUserId());
		luck.setUserId(users.getUserId());
		luck.setLuckState(1);
		if(luck.getLuckRemain() == null) {
			luck.setLuckRemain(luck.getLuckCount());
		}
		ValidataBean validata = luck.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != luck.getId()){
			result = systemLuckMapper.updateByIdSelective(luck);
		}else{
			//一个人只能有一个幸运
			List<SystemLuck> existRoles = systemLuckMapper.getLuckByUserId(luck.getUserId());
			if(CollectionUtils.isNotEmpty(existRoles)){
				return new Result<Integer>(00000,"玩家已存在幸运值！");
			}
			luck.setCreateTime(new Date());
			result = systemLuckMapper.insert(luck);
		}
		
		Users user = usersService.selectUserDetial(luck.getUserId()).getData();
		AccountVo accountVo = new AccountVo();
		accountVo.setCmd(6);
		accountVo.setStarNo(user.getStarNo());
		accountVo.setLuck(luck.getLuck());
		accountVo.setLuckStart(luck.getLuckStart().getTime());
		accountVo.setLuckEnd(luck.getLuckEnd().getTime());
		accountVo.setLuckNum(luck.getLuckCount());
		Result<Integer> success = gameGMUtil.accountService(accountVo);
		if(!success.isOk()) {
			return success;
		}
		return new Result<Integer>(result);
	}

	@Override
	public Result<Integer> deleteLuck(Integer luckId) {
		if(null == luckId){
			return new Result<Integer>(00000,"删除幸运玩家失败，参数异常！");
		}
		SystemLuck luck = systemLuckMapper.selectById(luckId);
		Users user = usersService.selectUserDetial(luck.getUserId()).getData();
		AccountVo accountVo = new AccountVo();
		accountVo.setCmd(6);
		accountVo.setStarNo(user.getStarNo());
		accountVo.setLuck(0);
		accountVo.setLuckStart(new Date().getTime());
		accountVo.setLuckEnd(new Date().getTime());
		accountVo.setLuckNum(0);
		Result<Integer> success = gameGMUtil.accountService(accountVo);
		if(!success.isOk()) {
			return success;
		}
		return new Result<Integer>(systemLuckMapper.deleteById(luckId));
	}

	@Override
	public void taskUserLuck(SystemTask task) {
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(task.getJsonContent());
		Result<Integer> success = processingUserLuck(obj);
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
	public Result<Integer> processingUserLuck(JSONObject obj) {
		if(obj == null) {
			return new Result<Integer>(00000,"MQ数据异常!");
		} else {
			Users user = usersMapper.getUserBy(Integer.parseInt(obj.getString("starNO")));
			if(user == null) {
				return new Result<Integer>(00000,"玩家幸运值，未查询到玩家");
			}
			//查询玩家对应的幸运值
			List<SystemLuck> existRoles = systemLuckMapper.getLuckByUserId(user.getUserId());
			if(existRoles == null || existRoles.size() <= 0) {
				return new Result<Integer>(00000,"玩家幸运值，未查询到幸运值");
			}
			SystemLuck luck = existRoles.get(0);
			luck.setLuckRemain(luck.getLuckRemain()-1);
			int result = systemLuckMapper.updateByIdSelective(luck);
			return new Result<Integer>(result);
		}
	}
}
