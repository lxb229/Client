
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
import com.guse.platform.entity.doudou.SystemPropLog;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemPropLogService;
import com.guse.platform.service.system.UsersService;


@Controller
@RequestMapping("/systemPropLogController")
public class SystemPropLogController extends BaseController {
	
	@Autowired
	private SystemPropLogService propLogService;
	@Autowired
	private UsersService usersService;
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(SystemPropLog propLog,PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	String orderType = "spl_amount";
		int sortType = propLog.getSplOprtuser();
		if(sortType == 1) {
			orderType = "spl_amount";
		} else if(sortType == 2) {
			orderType = "spl_time";
		}
		int sortStyle  = propLog.getSplAmount(); 
		if(sortStyle == 1 ) {
			orderType += " asc ";
		} else {
			orderType += " desc ";
		}
    	
    	Result<PageResult<SystemPropLog>> result = null;
		result = propLogService.queryPageList(pageBean, propLog, orderType,user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemPropLog spl = result.getData().getList().get(i);
    		Users users = usersService.selectUserDetial(spl.getSplOprtuser()).getData();
    		spl.setOprtuser(users);
    		result.getData().getList().set(i, spl);
    	}
    	return new AjaxResponse(result.getData());
    }
}
