
package com.guse.platform.controller.doudou;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.platform.common.base.AjaxResponse;
import com.guse.platform.common.base.BaseController;
import com.guse.platform.common.base.Constant;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.page.PageBean;
import com.guse.platform.common.page.PageResult;
import com.guse.platform.dao.doudou.CityMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.City;
import com.guse.platform.entity.doudou.SystemAgency;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemAgencyService;


@Controller
@RequestMapping("/systemAgencyController")
public class SystemAgencyController extends BaseController {
	@Autowired
	private SystemAgencyService agencyService;
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private CityMapper cityMapper;
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(SystemAgency agency,PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	String orderType = "agency_end";
		int sortType = agency.getSaCityId();
		if(sortType == 1) {
			orderType = "agency_state";
		} else if(sortType == 2) {
			orderType = "agency_end";
		}
		int sortStyle  = agency.getSaUserId(); 
		if(sortStyle == 1 ) {
			orderType += " asc ";
		} else {
			orderType += " desc ";
		}
    	
    	Result<PageResult<SystemAgency>> result = null;
		result = agencyService.queryPageList(pageBean, agency, orderType, user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemAgency systemAgency = result.getData().getList().get(i);
    		City city = cityMapper.selectById(systemAgency.getSaCityId());
    		systemAgency.setCity(city);
    		
    		Users agencyUser = usersMapper.selectByPrimaryKey(systemAgency.getSaUserId());
    		systemAgency.setUsers(agencyUser);
    		
    		result.getData().getList().set(i, systemAgency);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/getAgencyListForCityId", method = RequestMethod.POST)
    @ResponseBody
    public Object getAgencyListForCityId(Integer cityId) throws Exception {
    	Result<List<SystemAgency>> result = agencyService.getAgencyListForCityId(cityId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().size(); i++) {
    		SystemAgency systemAgency = result.getData().get(i);
    		Users users = usersMapper.selectByPrimaryKey(systemAgency.getSaUserId());
    		systemAgency.setUsers(users);
    		result.getData().set(i, systemAgency);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 增加
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateAgency", method = RequestMethod.POST)
    @ResponseBody
    public Object saveOrUpdateAgency(SystemAgency agency) throws Exception {
    	Result<Integer> result = agencyService.saveOrUpdateAgency(agency);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(Integer agencyId) throws Exception {
    	Result<Integer> result = agencyService.deleteAgency(agencyId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
