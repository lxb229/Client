
package com.guse.platform.controller.doudou;
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
import com.guse.platform.dao.doudou.SystemAgencyMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.City;
import com.guse.platform.entity.doudou.SystemAgency;
import com.guse.platform.entity.doudou.SystemCash;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemCashService;


@Controller
@RequestMapping("/systemCashController")
public class SystemCashController extends BaseController {
	
	@Autowired
	private SystemCashService cashService;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private SystemAgencyMapper agencyMapper;
	@Autowired
	private UsersMapper usersMapper;
	
	
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(SystemCash cash,PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	String orderType = "sc_Amount";
		int sortType = cash.getCityId();
		if(sortType == 1) {
			orderType = "sc_mouth";
		} else if(sortType == 2) {
			orderType = "sc_Amount";
		}
		int sortStyle  = cash.getScId(); 
		if(sortStyle == 1 ) {
			orderType += " asc ";
		} else {
			orderType += " desc ";
		}
    	
    	Result<PageResult<SystemCash>> result = null;
		result = cashService.queryPageList(pageBean, cash, orderType, user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemCash systemCash = result.getData().getList().get(i);
    		City city = cityMapper.selectById(systemCash.getCityId());
    		systemCash.setCity(city);
    		
    		SystemAgency agency = agencyMapper.getAgencyForCityId(systemCash.getCityId());
    		systemCash.setAgency(agency);
    		
    		Users agencyUser = usersMapper.selectByPrimaryKey(agency.getSaUserId());
    		systemCash.setAgencyUser(agencyUser);
    		
    		
    		result.getData().getList().set(i, systemCash);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 审核提现
     * @return
     */
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @ResponseBody
    public Object audit(SystemCash cash) throws Exception {
    	Result<Integer> result = cashService.updateCash(cash);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 处理提现
     * @return
     */
    @RequestMapping(value = "/cash", method = RequestMethod.POST)
    @ResponseBody
    public Object cash(Integer scId) throws Exception {
    	SystemCash cash = cashService.getCashById(scId);
    	cash.setScState(3);
    	Result<Integer> result = cashService.updateCash(cash);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
}
