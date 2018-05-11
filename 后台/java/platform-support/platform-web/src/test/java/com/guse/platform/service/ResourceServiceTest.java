package com.guse.platform.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.guse.platform.TestBase;
import com.guse.platform.common.base.Result;
import com.guse.platform.entity.system.Resource;
import com.guse.platform.service.system.ResourceService;
/**
 * 资源管理
 * @author nbin
 * @date 2017年7月19日 下午4:11:14 
 * @version V1.0
 */
public class ResourceServiceTest extends TestBase {

	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 新建更新
	 * @Title: saveOrUpdateResource 
	 * @param  
	 * @return void
	 */
	@Test
	public void saveOrUpdateResource(){
		Resource resource = new Resource();
		resource.setSrName("启禁用户");
		resource.setSrGroup(4);
		resource.setSrUrl("/system/users/enableDisable");
		Result<Integer> result = resourceService.saveOrUpdateResource(resource);
		System.out.println(JSON.toJSONString(result));
	}
	/**
	 * 删除
	 * @Title: deleteResource 
	 * @param  
	 * @return void
	 */
	@Test
	public void deleteResource(){
		
	}
	
	/**
	 * 详细
	 * @Title: selectResourceDetial 
	 * @param  
	 * @return void
	 */
	@Test
	public void selectResourceDetial(){
		
	}
	
	/**
	 * 获得一个菜单下的资源列表
	 * @Title: getResourceListForMenuId 
	 * @param  
	 * @return void
	 */
	@Test
	public void getResourceListForMenuId(){
		Result<List<Resource>> result = resourceService.getResourceListForMenuId(4);
		System.out.println(JSON.toJSONString(result));
	}
	
}
