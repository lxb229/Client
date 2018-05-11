
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
import com.guse.platform.dao.doudou.ClubMapper;
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.Club;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.ClubService;


@Controller
@RequestMapping("/clubController")
public class ClubController extends BaseController {
	
	@Autowired
	ClubService clubService;
	@Autowired
	UsersMapper usersMapper;
	@Autowired
	ClubMapper clubMapper;
	
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(Club club, PageBean pageBean) {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PageResult<Club>> result = null;
		try {
			result = clubService.queryPageListByClub(club, pageBean , user);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		Club sysClub = result.getData().getList().get(i);
    		Users users = usersMapper.selectByPrimaryKey(sysClub.getCuserId());
    		sysClub.setUsers(users);
    		result.getData().getList().set(i, sysClub);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 启用禁用
     * @return
     */
    @RequestMapping(value = "/enableDisable", method = RequestMethod.POST)
    public @ResponseBody Object enable(Club club) throws Exception {
    	int club_state = club.getCstate();
    	club = clubMapper.selectById(club.getCid());
    	club.setCstate(club_state);
    	Result<Integer> result = clubService.enableDisableClub(club);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
        return new AjaxResponse(result.getData());
    }
}
