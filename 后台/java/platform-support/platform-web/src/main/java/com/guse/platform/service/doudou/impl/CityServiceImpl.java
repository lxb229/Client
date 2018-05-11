package com.guse.platform.service.doudou.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.utils.HttpClientUtil;
import com.guse.platform.dao.doudou.CityMapper;
import com.guse.platform.service.doudou.CityService;
import com.guse.platform.vo.doudou.CityTreeVo;
import com.guse.platform.entity.doudou.City;

/**
 * city
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class CityServiceImpl extends BaseServiceImpl<City, java.lang.Integer> implements CityService{

	@Autowired
	private CityMapper  cityMapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(cityMapper);
	}
	
	@Override
	public Result<List<CityTreeVo>> menusTree() {
		List<CityTreeVo> list = Lists.newArrayList();
		//设置默认根节点
		CityTreeVo rootMenu = new CityTreeVo();
		rootMenu.setId("0");
		rootMenu.setParent("#");
		rootMenu.setText("地区区域");
		list.add(rootMenu);
		//所有菜单
		List<City> menus = cityMapper.selectAllCityList();
		if(CollectionUtils.isEmpty(menus)){
			return new Result<List<CityTreeVo>>(list);
		}
		Map<Integer, List<City>> menusGroup = menusSortByGroupPid(menus);
		for(Map.Entry<Integer, List<City>> entry : menusGroup.entrySet()){
            for(City m : entry.getValue() ){
            	CityTreeVo menu = new CityTreeVo();
            	menu.setId(m.getId().toString());
            	menu.setParent(m.getPid().toString());
            	menu.setText(m.getName());
            	menu.setSort(m.getSort()==null?0:m.getSort());
            	list.add(menu);
            }
        }
		return new Result<List<CityTreeVo>>(list);
	}

	/**
	  * 分组排序根据父id
	  * @Title: menusSortByGroup 
	  * @param @param list
	  * @param @return 
	  * @return Map<Integer,List<Menus>>
	  */
	 public static Map<Integer, List<City>> menusSortByGroupPid(List<City> list){
      Map<Integer, List<City>> map = new HashMap<Integer, List<City>>();
      for(City menus : list) {
          List<City> staList = map.get(menus.getPid());
          if(staList==null){
              staList = new ArrayList<City>();
          }
          staList.add(menus);
          Collections.sort(staList, new Comparator<City>() {
              @Override
              public int compare(City o1, City o2) {
                  return o1.getSort().compareTo(o2.getSort());
              }
          });
          
          map.put(menus.getPid(), staList);
      }
      return map;
  }

	@Override
	public City getCityByIp(String ipAddress) {
		String cityService = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip="+ipAddress;
		String result = HttpClientUtil.httpGet(cityService);
		result = result.substring(result.indexOf("{"), result.indexOf("}")+1);
		//JSON格式转换
        JSONObject obj = JSONObject.parseObject(result);
        String cityName = obj.getString("city");
        City city = cityMapper.getCityByName(cityName);
		return city;
	}
	
}