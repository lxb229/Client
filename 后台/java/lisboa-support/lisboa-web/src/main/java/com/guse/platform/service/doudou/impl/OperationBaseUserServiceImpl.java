package com.guse.platform.service.doudou.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.common.utils.DateUtils;
import com.guse.platform.dao.doudou.OperationBaseUserMapper;
import com.guse.platform.entity.doudou.OperationBaseUser;
import com.guse.platform.service.doudou.OperationBaseUserService;
import com.guse.platform.utils.redis.RedisMapToBean;

/**
 * operation_base_user
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class OperationBaseUserServiceImpl extends BaseServiceImpl<OperationBaseUser, java.lang.Long> implements OperationBaseUserService{

	@Autowired
	private OperationBaseUserMapper  operationBaseUserMapper;
	@Autowired
	private RedisMapToBean rm;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(operationBaseUserMapper);
	}

	@Override
	public Result<PageResult<OperationBaseUser>> queryHistoryPageList(PageBean pageBean, OperationBaseUser obu,String orderBy) {
		Long count = operationBaseUserMapper.countHistoryByParam(obu);
		if (count <= 0) {
             return new Result<PageResult<OperationBaseUser>>(new PageResult<OperationBaseUser>().initNullPage());
        }
		List<Map<String,Object>> listMap = operationBaseUserMapper.selectHistoryPageByParam(obu, new PageResult<OperationBaseUser>(pageBean.getPageNo(), pageBean.getPageSize(), count, orderBy));
		if(CollectionUtils.isEmpty(listMap)){
			 return new Result<PageResult<OperationBaseUser>>(new PageResult<OperationBaseUser>().initNullPage());
		}
		//转换
		List<OperationBaseUser> list = new ArrayList<>();
		for(Map<String,Object> map : listMap){
			OperationBaseUser obj = new OperationBaseUser();
			Map<String,String> newmap = new HashMap<>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if(entry.getKey().equals("obuLastLoginTime")){
					//未登录天数
					if(null!=entry.getValue()){
						Long days = DateUtils.getDaysByDate(new Date(), DateUtils.parseDate(entry.getValue()+"", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
						newmap.put("notLonginDay", days+"");
						continue;
					}
				}
				newmap.put(entry.getKey(), null != entry.getValue()? entry.getValue()+"" :"");
			}
			rm.mapToPojo(newmap, obj);
			list.add(obj);
		}
	    PageResult<OperationBaseUser> pageResult = null;
		pageResult = new PageResult<OperationBaseUser>(pageBean.getPageNo(), pageBean.getPageSize(),count ,"");
		pageResult.setList(list);
		return new Result<PageResult<OperationBaseUser>>(pageResult);
	}

	@Override
	public List<OperationBaseUser> selectHistory(OperationBaseUser obu) {
		List<Map<String,Object>> listMap = operationBaseUserMapper.selectHistory(obu);
		List<OperationBaseUser> list = new ArrayList<>();
		if(CollectionUtils.isEmpty(listMap)){
			 return list;
		}
		for(Map<String,Object> map : listMap){
			OperationBaseUser obj = new OperationBaseUser();
			Map<String,String> newmap = new HashMap<>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if(entry.getKey().equals("obuLastLoginTime")){
					//未登录天数
					if(null!=entry.getValue()){
						Long days = DateUtils.getDaysByDate(new Date(), DateUtils.parseDate(entry.getValue()+"", DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
						newmap.put("notLonginDay", days+"");
						continue;
					}
				}
				newmap.put(entry.getKey(), null != entry.getValue()? entry.getValue()+"" :"");
			}
			rm.mapToPojo(newmap, obj);
			list.add(obj);
		}
		return list;
	}
	
}
