
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
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.SystemFeedback;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemFeedbackService;


@Controller
@RequestMapping("/systemFeedbackController")
public class SystemFeedbackController extends BaseController {
	
	@Autowired
	private SystemFeedbackService feedbackService;
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
    public @ResponseBody Object queryPagelist(SystemFeedback feedback,PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	String orderType = "create_time";
		int sortType = feedback.getUserId();
		if(sortType == 1) {
			orderType = "id";
		} else if(sortType == 2) {
			orderType = "create_time";
		}
		int sortStyle  = feedback.getId(); 
		if(sortStyle == 1 ) {
			orderType += " asc ";
		} else {
			orderType += " desc ";
		}
    	
    	Result<PageResult<SystemFeedback>> result = null;
		result = feedbackService.queryPageList(pageBean, feedback, orderType , user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemFeedback systemFeedback = result.getData().getList().get(i);
    		Users users = usersMapper.selectByPrimaryKey(systemFeedback.getUserId());
    		systemFeedback.setUsers(users);
    		result.getData().getList().set(i, systemFeedback);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 增加/更新
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdate(SystemFeedback feedback) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<Integer> result = feedbackService.saveOrUpdateFeedback(feedback ,user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
