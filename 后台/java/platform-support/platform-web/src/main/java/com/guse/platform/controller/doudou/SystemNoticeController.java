
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
import com.guse.platform.dao.system.UsersMapper;
import com.guse.platform.entity.doudou.MallProxy;
import com.guse.platform.entity.doudou.SystemNotice;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.doudou.SystemNoticeService;


@Controller
@RequestMapping("/noticeController")
public class SystemNoticeController extends BaseController {
	
	@Autowired
	SystemNoticeService service;
	@Autowired
	UsersMapper usersMapper;
	@Autowired
	CityMapper cityMapper;
	/**
	 * 分页查询
	 * @Title: queryPagelist 
	 * @param @return
	 * @param @throws Exception 
	 * @return Object
	 */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(SystemNotice notice, PageBean pageBean) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	Result<PageResult<SystemNotice>> result = null;
		try {
			result = service.queryPageList(pageBean, notice, "sn_id", user);
		} catch (Exception e) {
			e.printStackTrace();
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	for (int i = 0; i < result.getData().getList().size(); i++) {
    		SystemNotice systemNotice = result.getData().getList().get(i);
    		Users users = usersMapper.selectByPrimaryKey(systemNotice.getCreateId());
    		systemNotice.setUsers(users);
    		result.getData().getList().set(i, systemNotice);
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 增加/更新
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdate(SystemNotice notice) throws Exception {
    	Users user   = (Users) getSession().getAttribute(Constant.SESSION_LOGIN_USER);
    	if(user == null) {
    		return new AjaxResponse(Constant.CODE_ERROR, "session失效，重新登录");
    	}
    	notice.setCreateId(user.getUserId());
    	Result<Integer> result = service.saveOrUpdateProduct(notice);
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
    public Object delete(SystemNotice notice) throws Exception {
    	Result<Integer> result = service.deleteNotice(notice);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
}
