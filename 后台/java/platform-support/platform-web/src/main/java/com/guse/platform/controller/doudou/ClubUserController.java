
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
import com.guse.platform.entity.doudou.ClubUser;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.ClubUserService;


@Controller
@RequestMapping("/clubUserController")
public class ClubUserController extends BaseController {
	
	@Autowired
	ClubUserService clubUserService;
	@Autowired
	UsersMapper usersMapper;
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(ClubUser clubUser, PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PageResult<ClubUser>> result = null;
		try {
			result = clubUserService.queryPageList(pageBean, clubUser, null, user);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		ClubUser sysClubUser = result.getData().getList().get(i);
    		Users users = usersMapper.selectByPrimaryKey(sysClubUser.getCuUserId());
    		sysClubUser.setUsers(users);
    		result.getData().getList().set(i, sysClubUser);
    	}
    	return new AjaxResponse(result.getData());
    }
}
