package com.guse.platform.service.doudou.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.GameGMUtil;
import com.guse.platform.dao.doudou.SystemAgencyMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.service.doudou.SystemAgencyService;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.vo.doudou.AccountVo;
import com.guse.platform.entity.doudou.SystemAgency;
import com.guse.platform.entity.system.Users;

/**
 * system_agency
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemAgencyServiceImpl extends BaseServiceImpl<SystemAgency, java.lang.Integer> implements SystemAgencyService{

	@Autowired
	private SystemAgencyMapper  systemAgencyMapper;
	@Autowired
	private GameGMUtil gameGMUtil;
	@Autowired
	private UsersService usersService;
	@Autowired
	private UsersMapper  usersMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemAgencyMapper);
	}

	@Override
	public Result<Integer> saveOrUpdateAgency(SystemAgency agency) {
		Users users = usersMapper.getUserBy(agency.getSaUserId());
		if(users == null) {
			return new Result<Integer>(00000,"查无此人");
		}
		if(StringUtils.isBlank(users.getWxNo())) {
			return new Result<Integer>(00000,"代理需先设置微信号！");
		}
		agency.setSaUserId(users.getUserId());
		ValidataBean validata = agency.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		//一个区域只能有一个代理
		List<SystemAgency> existRoles = systemAgencyMapper.selectAgencyListByCityId(agency.getSaCityId());
		if(CollectionUtils.isNotEmpty(existRoles)){
			return new Result<Integer>(00000,"代理已存在！");
		}
		/**通知服务器修改用户类型*/
		Users user = usersService.selectUserDetial(agency.getSaUserId()).getData();
		AccountVo accountVo = new AccountVo();
		accountVo.setCmd(2);
		accountVo.setStarNo(user.getStarNo());
		accountVo.setType(1);
		accountVo.setWxNO(user.getWxNo());
		Result<Integer> success = gameGMUtil.accountService(accountVo);
		if(!success.isOk()) {
			return success;
		}
		
		//更改用户权限为代理 
		users.setRoleId(12);
		Integer result = null;
		if(agency.getAgencyState() == null) {
			agency.setAgencyState(1);
		}
		usersMapper.updateByPrimaryKeySelective(users);
		if(null != agency.getSaId()){
//			SystemAgency oldAgency = systemAgencyMapper.selectById(agency.getSaId());
			success = gmDeleteAgency(agency);
			result = systemAgencyMapper.updateByIdSelective(agency);
			if(!success.isOk()) {
				return success;
			}
		}else{
			agency.setSaCreateTime(new Date());
			result = systemAgencyMapper.insert(agency);
			
		}
		
		
		return new Result<Integer>(result);
	}

	@Override
	public Result<List<SystemAgency>> getAgencyListForCityId(Integer cityId) {
		if(null == cityId){
			return new Result<List<SystemAgency>>(00000,"获取区域下的代理列表失败，参数异常！");
		}
		return new Result<List<SystemAgency>>(systemAgencyMapper.selectAgencyListByCityId(cityId));
	}

	@Override
	public Result<Integer> deleteAgency(Integer saId) {
		if(null == saId){
			return new Result<Integer>(00000,"删除代理失败，参数异常！");
		}
		SystemAgency agency = systemAgencyMapper.selectById(saId);
		Result<Integer> success = gmDeleteAgency(agency);
		if(!success.isOk()) {
			return success;
		}
		return new Result<Integer>(systemAgencyMapper.deleteById(saId));
	}

	@Override
	public SystemAgency getAgencyForCityId(Integer cityId) {
		
		return systemAgencyMapper.getAgencyForCityId(cityId);
	}

	@Override
	public Result<Integer> gmDeleteAgency(SystemAgency agency) {
		//查询该代理是否还代理其他区域，如果没有则取消用户代理权限
		List<SystemAgency> agencyList = systemAgencyMapper.selectAgencyOut(agency);
		if(agencyList != null && agencyList.size() > 0) {
		} else {
			Users user = usersService.selectUserDetial(agency.getSaUserId()).getData();
			AccountVo accountVo = new AccountVo();
			accountVo.setCmd(2);
			accountVo.setStarNo(user.getStarNo());
			accountVo.setType(0);
			Result<Integer> success = gameGMUtil.accountService(accountVo);
			if(!success.isOk()) {
				return success;
			}
		}
		return new Result<Integer>(agencyList.size());
	}
	
	
}
