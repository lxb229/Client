package com.guse.platform.controller.system;

import java.util.Date;

import javax.print.DocFlavor.STRING;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import com.guse.platform.common.utils.encrypt.QEncodeUtil;
import com.guse.platform.entity.system.Users;
import com.guse.platform.service.system.UsersService;
import com.guse.platform.vo.system.AccountVo;
import com.google.gson.Gson;

/**
 * 用户
 * @author nbin
 * @version 1.0
 * @CreateDate 2017-7-13 - 上午11:34:26
 */
@Controller
@RequestMapping("/system/users")
public class UsersController extends BaseController {
	
	@Autowired
	private UsersService usersService;
	
	private Gson gson = new Gson();
	
	
	/**
     * 列表
     * @return
     */
    @RequestMapping(value = "/queryPagelist", method = RequestMethod.POST)
    public @ResponseBody Object queryPagelist(Users user,PageBean pageBean) {
    	Result<PageResult<Users>> result = null;
		try {
			result = usersService.queryPageListByUsers(user, pageBean);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    
    /**
     * 列表
     * @return
     */
    @RequestMapping(value = "/queryAccounList", method = RequestMethod.POST)
    public @ResponseBody Object queryAccounList(Users user,PageBean pageBean) {
    	Result<PageResult<AccountVo>> result = null;
		try {
			result = usersService.queryAccountList(user, pageBean);
		} catch (Exception e) {
			return new AjaxResponse(Constant.CODE_ERROR, e.getMessage());
		}
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	
    	return new AjaxResponse(result.getData());
    }
    
    
    /**
     * 增加/减少金币
     * @return
     */
    @RequestMapping(value = "/updateRoomcard", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRoomcard(AccountVo accountVo) throws Exception {
    	Result<Integer> result = usersService.updateRoomcard(accountVo);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value = "/updateUserPass", method = RequestMethod.POST)
    @ResponseBody
    public Object updateUserPass(AccountVo accountVo) throws Exception {
    	Result<Integer> result = usersService.updateUserPass(accountVo);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
    /**
     * 用户详情
     * @Title: selectUserDetail 
     * @param @param userId
     * @param @return
     * @param @throws Exception 
     * @return Object
     */
    @RequestMapping(value = "/selectUserDetail", method = RequestMethod.POST)
    public @ResponseBody Object selectUserDetail(Integer userId) throws Exception {
    	Result<Users> result =usersService.selectUserDetial(userId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
    	return new AjaxResponse(result.getData());
    }
	
    /**
     * 新增/更新
     * @return
     */
    @RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object add(Users user) {
    	Result<Integer> result = null;
    	AjaxResponse ajax = AjaxResponse.success();
		try {
			result = usersService.saveOrUpdateUser(user);
			ajax.setData(result.getData());
		} catch (Exception e) {
			ajax = AjaxResponse.error(e.getMessage());
		}
    	if (!result.isOk()) {
    		ajax = AjaxResponse.error(result.getErrorMsg());
    	}
        return ajax;
    }
    
    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ResponseBody
    public Object modifyPwd(String oldPwd,String newPwd) {
    	Result<Integer> result = null;
    	AjaxResponse ajax = AjaxResponse.success();
		try {
			if(StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)) {
				return AjaxResponse.error("新/旧密码不能为空！");
			}
			Users user = new Users();
			Users loginUser = (Users)getSession().getAttribute(Constant.SESSION_LOGIN_USER);
			if(loginUser == null) {
				return AjaxResponse.error("用户未登录或者登录超时，请登录！");
			}
//			String oldPwdPass = QEncodeUtil.aesEncrypt(oldPwd, Constant.AES_KEY);
			String oldPwdPass = QEncodeUtil.md5Encrypt(oldPwd);
			if(!loginUser.getLoginPass().equals(oldPwdPass)){
				return AjaxResponse.error("旧密码输入不正确！");
			}
			user.setUserId(loginUser.getUserId());
//			user.setLoginPass(QEncodeUtil.aesEncrypt(newPwd, Constant.AES_KEY));
			user.setLoginPass(QEncodeUtil.md5Encrypt(newPwd));
			result = usersService.saveOrUpdateUser(user);
	    	if (!result.isOk()) {
	    		ajax = AjaxResponse.error(result.getErrorMsg());
	    	} else {
//	    		loginUser.setLoginPass(QEncodeUtil.aesEncrypt(newPwd, Constant.AES_KEY));
	    		user.setLoginPass(QEncodeUtil.md5Encrypt(newPwd));
	    		getSession().setAttribute(Constant.SESSION_LOGIN_USER,loginUser);
			}
			ajax.setData(result.getData());
		} catch (Exception e) {
			ajax = AjaxResponse.error(e.getMessage());
		}
        return ajax;
    }
    
    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody Object delete(Integer userId) throws Exception {
    	Result<Integer> result = usersService.delectUser(userId);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
        return new AjaxResponse(result.getData());
    }
    
    
    /**
     * 启用禁用
     * @return
     */
    @RequestMapping(value = "/enableDisable", method = RequestMethod.POST)
    public @ResponseBody Object enable(Users user) throws Exception {
    	Result<Integer> result = usersService.enableDisableUser(user);
    	if (!result.isOk()) {
    		return new AjaxResponse(Constant.CODE_ERROR, result.getErrorMsg());
    	}
        return new AjaxResponse(result.getData());
    }
    
    /**
     * 设置用户角色
     * @return
     */
    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
    @ResponseBody
    public Object setRole(Users user) {
    	AjaxResponse ajax = new AjaxResponse();
    	try {
        	Result<Integer> result = usersService.setUserRole(user.getUserId(), user.getRoleId());
        	if (!result.isOk()) {
        		ajax.setCode(Constant.CODE_ERROR);
        		ajax.setMsg(result.getErrorMsg());
        	} else {
        		ajax.setCode(Constant.CODE_SUCCESS);
        		ajax.setData(result.getData());
			}
		} catch (Exception e) {
			ajax.setCode(Constant.CODE_ERROR);
    		ajax.setMsg(e.getMessage());
		}
        return ajax;
    }    
}
