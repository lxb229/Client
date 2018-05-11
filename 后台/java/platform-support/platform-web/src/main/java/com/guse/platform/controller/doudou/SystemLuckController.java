
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
import com.guse.platform.entity.doudou.SystemLuck;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemLuckService;


@Controller
@RequestMapping("/systemLuckController")
public class SystemLuckController extends BaseController {
	
	@Autowired
	private SystemLuckService luckService;
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
    public @ResponseBody Object queryPagelist(SystemLuck luck,PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PageResult<SystemLuck>> result = null;
		result = luckService.queryPageList(pageBean, luck, "id", user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemLuck systemLuck = result.getData().getList().get(i);
    		Users users = usersMapper.selectByPrimaryKey(systemLuck.getUserId());
    		systemLuck.setUsers(users);
    		result.getData().getList().set(i, systemLuck);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 增加/更新
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdate(SystemLuck luck) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	if(luck.getCreateId() == null) {
    		luck.setCreateId(user.getUserId());
    	}
    	Result<Integer> result = luckService.saveOrUpdateLuck(luck);
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
    public Object delete(Integer luckId) throws Exception {
    	Result<Integer> result = luckService.deleteLuck(luckId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
