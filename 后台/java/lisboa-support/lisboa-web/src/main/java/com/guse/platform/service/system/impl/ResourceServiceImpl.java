package com.guse.platform.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.dao.system.ResourceMapper;
import com.guse.platform.entity.system.Resource;
import com.guse.platform.service.system.ResourceService;


/**
 * 
 * @author nbin
 * @version 1.0
 * @CreateDate 201-7-26 - 上午11:34:26
 */
@Service("resourceService")
public class ResourceServiceImpl  implements ResourceService{
	
	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public Result<Integer> saveOrUpdateResource(Resource resource) {
		ValidataBean validata = resource.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		Integer result = null;
		if(null != resource.getSrId()){
			result = resourceMapper.updateByPrimaryKeySelective(resource);
		}else{
			result = resourceMapper.insert(resource);
		}
		return new Result<Integer>(result);
	}

	@Override
	public Result<Integer> deleteResource(Integer resourceId) {
		if(null == resourceId){
			return new Result<Integer>(00000,"删除资源失败，参数异常！");
		}
		return new Result<Integer>(resourceMapper.deleteByPrimaryKey(resourceId));
	}

	@Override
	public Result<Resource> selectResourceDetial(Integer resourceId) {
		if(null == resourceId){
			return new Result<Resource>(00000,"获取资源详细信息失败，参数异常！");
		}
		return new Result<Resource>(this.resourceMapper.selectByPrimaryKey(resourceId));
	}
	
	@Override
	public Result<List<Resource>> getResourceListForMenuId(Integer menuId) {
		if(null == menuId){
			return new Result<List<Resource>>(00000,"获取菜单下的资源列表失败，参数异常！");
		}
		return new Result<List<Resource>>(this.resourceMapper.selectResourceListByMenuId(menuId));
	}

	
}
