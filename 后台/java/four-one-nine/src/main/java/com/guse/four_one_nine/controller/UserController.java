package com.guse.four_one_nine.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.four_one_nine.controller.page.RequestPage;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.controller.page.ResponsePage;
import com.guse.four_one_nine.dao.model.SysUser;
import com.guse.four_one_nine.dao.model.User;
import com.guse.four_one_nine.service.UserService;
import com.guse.four_one_nine.service.util.AbstractSearchService;

/** 
* @ClassName: UserController 
* @Description: 用户管理 适配器
* @author Fily GUSE
* @date 2018年1月12日 上午11:04:03 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("user")
public class UserController extends AbstractSearchService{
	
	@Autowired
	UserService service;
	
	/** 
	* @Title: indePage 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/")
	public String indePage(Integer type, ModelMap model) {
		model.addAttribute("count", service.getCount());
		model.addAttribute("toDayCount", service.getToDayCount());
		model.addAttribute("type", type == null ? 0 : type);
		
		return "user-list";
	}
	
	
	/** 
	* @Title: search 
	* @Description: 列表搜索 
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/search")
	@ResponseBody
	public ResponsePage search(RequestPage page) {
		String params = "1=1";
		if(page.getType() != null) {
			switch (page.getType()) {
			case 1:
				params += " and t.seller_certification=1";
				break;
			case 2:
				params += " and t.`status` = 0";
				break;
			default:
				break;
			}
		}
		if(StringUtils.isNotBlank(page.getText())) {
			params += " and(t.nick_name like '%"+page.getText()+"%' or t.phone like '%"+page.getText()+"%')";
		}
		
		String tableName = "`user` t "
				+ " LEFT JOIN (SELECT user_id, COUNT(DISTINCT FROM_UNIXTIME(login_date,'%Y-%m-%d')) alive_day FROM login_logger GROUP BY user_id) l ON l.user_id = t.user_id"
				+ " LEFT JOIN (SELECT buy_user, SUM(total) total FROM server_order GROUP BY buy_user) s ON s.buy_user = t.user_id";
		return searchPage(tableName, params, page);
	}
	@Override
	public String queryField() {
		return "t.user_id id,t.head_picture picture,t.nick_name,t.phone,t.real_certification,IFNULL(l.alive_day,0) alive_day, IFNULL(s.total,0) total, t.registe_time, t.`status`";
	}
	
	
	/** 
	* @Description: 冻结解冻用户信息 
	* @param @param ids
	* @param @param result
	* @param @param session
	* @param @return
	* @return ResponseData 
	* @throws 
	*/
	@RequestMapping("/freeze")
	@ResponseBody
	public ResponseAjax freeze(String ids, int result, HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser)session.getAttribute(SysUser.SESSION_NAME));
		if(StringUtils.isBlank(ids)) {
			response.setFailure("请选择人员");
			return response;
		}
		
		if(response.isSuccess()) {
			service.freeze(ids, result, response);
		}
		return response;
	}
	
	/** 
	* @Title: detail 
	* @Description: 用户详情 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id")long id, ModelMap model) {
		User user = service.getUser(id);
		// 用户信息
		model.addAttribute("user", user);
		// 封面图片
		if(StringUtils.isNotBlank(user.getCover_picture())) {
			model.addAttribute("covers", user.getCover_picture().split(","));
		}
		model.addAttribute("expenditure", service.getExpenditure(id));
		
		return "user-detail";
	}
	
	/** 
	* @Description: 删除封面图片 
	* @param @param picture
	* @param @return
	* @return ResponseAjax 
	* @throws 
	*/
	@RequestMapping("/{id}/del_cover")
	@ResponseBody
	public ResponseAjax delCover(@PathVariable("id")long id, String picture) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(picture)) {
			response.setFailure("请选择图片");
			return response;
		}
		
		if(response.isSuccess()) {
			service.delCover(id, picture, response);
		}
		return response;
	}
	
	/** 
	* @Description: 重置密码 
	* @param @param session
	* @param @return
	* @return ResponseAjax 
	* @throws 
	*/
	@RequestMapping("/resetPwd/{id}")
	@ResponseBody
	public ResponseAjax resetPwd(@PathVariable("id")long id,HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser) session.getAttribute(SysUser.SESSION_NAME));
		service.resetPwd(id, response);
		
		return response;
	}
	
	/** 
	* @Description: 选择用户界面 
	* @param url 内容访问地址
	* @param callbackFunction 业务处理回调js函数
	* @param where 查询条件
	* @return String 
	* @throws 
	*/
	@RequestMapping("/choice_user_page")
	public String choiceUserPage(String callbackFunction, String where, ModelMap model) {
		model.addAttribute("callbackFunction", callbackFunction);
		model.addAttribute("where", where);
		
		return "choice_user";
	}
	
	/** 
	* @Title: search 
	* @Description: 列表搜索 
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/choice_user/search")
	@ResponseBody
	public ResponsePage choiceUserSearch(RequestPage page, String where) {
		String params = "1=1";
		if(StringUtils.isNotBlank(where)) {
			params += " and " + where;
		}
		if(StringUtils.isNotBlank(page.getText())) {
			params += " and(t.nick_name like '%"+page.getText()+"%' or t.phone like '%"+page.getText()+"%')";
		}
		
		String tableName = "`user` t ";
		String fields = "t.user_id, t.head_picture, t.nick_name, t.phone, t.seller_certification, t.registe_time";
		return searchPage(tableName, params, page, fields);
	}

}
