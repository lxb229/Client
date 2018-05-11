package com.guse.four_one_nine.controller;

import java.util.List;
import java.util.Map;

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
import com.guse.four_one_nine.dao.model.Union;
import com.guse.four_one_nine.service.UnionService;
import com.guse.four_one_nine.service.UserService;
import com.guse.four_one_nine.service.util.AbstractSearchService;

/** 
* @ClassName: UnionController 
* @Description: 工会管理 适配器
* @author Fily GUSE
* @date 2018年1月12日 上午11:26:42 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("union")
public class UnionController extends AbstractSearchService{
	
	@Autowired
	UnionService service;
	@Autowired
	UserService userService;
	
	/** 
	* @Title: indexPage 
	* @Description: 首页界面 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/")
	public String indexPage() {
		
		return "union-list";
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
		String params = "t.status=1";
		if(StringUtils.isNotBlank(page.getText())) {
			params += " and (t.union_name like '%"+page.getText()+"%' or u.nick_name like '%"+page.getText()+"%')";
		}
		String tableName = "`union` t "
				+ " LEFT JOIN `user` u ON u.user_id = t.clo "
				+ " LEFT JOIN (select union_id, count(1) user_num from union_user GROUP BY union_id) ur ON ur.union_id = t.union_id";
		params += " ORDER BY t.union_id";
		return searchPage(tableName, params, page);
	}
	@Override
	public String queryField() {
		return "t.union_id id, t.union_logo logo, t.union_name name, u.nick_name, IFNULL(ur.user_num, 0) user_num,t.create_time";
	}
	
	/** 
	* @Description: 删除工会 
	* @param @param ids
	* @param @return
	* @return ResponseAjax 
	* @throws 
	*/
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseAjax delete(String ids, HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser)session.getAttribute(SysUser.SESSION_NAME));
		if(StringUtils.isBlank(ids)) {
			response.setFailure("请选择工会");
			return response;
		}
		
		if(response.isSuccess()) {
			service.delete(ids, response);
		}
		return response;
	}
	
	/** 
	* @Title: publishPage 
	* @Description: 新建工会界面 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/publish-page")
	public String publishPage(Long unionId, ModelMap model) {
		if(unionId != null) {
			Union union = service.getUnion(unionId);
			// 工会信息
			model.addAttribute("union", union);
			// 会长
			model.addAttribute("clo_user", userService.getUser(union.getClo()));
			List<Map<String, Object>> unionUser = service.getUnionUsers(unionId);
			// 工会成员
			model.addAttribute("unionUser", unionUser);
		}
		return "union-publish";
	}
	
	/** 
	* @Description: 保存工会信息 
	* @param @param union
	* @param @param unionUserId
	* @param @return
	* @return ResponseAjax 
	* @throws 
	*/
	@RequestMapping("/save")
	@ResponseBody
	public ResponseAjax save(Union union, Long[] unionUserId, HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser)session.getAttribute(SysUser.SESSION_NAME));
		if(StringUtils.isBlank(union.getUnion_name())) {
			response.setFailure("工会名称不能未空");
			return response;
		}
		if(StringUtils.isBlank(union.getUnion_logo())) {
			response.setFailure("工会LOGO不能未空");
			return response;
		}
		if(union.getClo() == null) {
			response.setFailure("会长不能未空");
			return response;
		}
		if(unionUserId == null || unionUserId.length == 0) {
			response.setFailure("成员不能未空");
			return response;
		}
		// 验证会长属于成员
		boolean unUnionUser = true;
		for(Long userId : unionUserId) {
			if(userId.equals(union.getClo())) {
				unUnionUser = false;
				break;
			}
		}
		if(unUnionUser) {
			response.setFailure("会长必须从成员中选择");
			return response;
		}
		
		
		if(response.isSuccess()) {
			service.save(union, unionUserId, response);
		}
		return response;
	}
	
	/** 
	* @Description: 工会详情界面 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail/{id}")
	public String detailPage(@PathVariable("id")long id) {
		
		return "union-detail";
	}
	
	/** 
	* @Description: 工会详情-工会收益界面 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail/income/{id}")
	public String detailIncomePage(@PathVariable("id")long id, ModelMap model) {
		// 工会收入统计
		model.addAttribute("count", service.incomeCount(id));
		// 按照天统计收入信息
		
		return "union-detail-income";
	}
	
	/** 
	* @Description: 工会相关订单信息 
	* @param @param id
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/orders/{id}")
	@ResponseBody
	public ResponsePage orderSearch(@PathVariable("id")long id, RequestPage page) {
		// 搜索条件
		String params = "t.union_id = "+id+" ORDER BY t.buy_time DESC";
		// 查询表名
		String tableName = "server_order t LEFT JOIN `user` bu ON bu.user_id = t.buy_user "
				+ " LEFT JOIN `server` s ON s.id = t.server_id"
				+ " LEFT JOIN `user` pu ON pu.user_id = s.publish_user";
		// 查询字段
		String fileds = "t.id, bu.nick_name buy_name, s.`name` server_name, pu.nick_name publish_name, t.total, t.tip_money, t.`status` settle_status, t.buy_time , t.`status` order_status";
		return searchPage(tableName, params, page, fileds);
	}
	
	/** 
	* @Description: 工会详情-工会管理界面 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail/manage/{id}")
	public String detailManagePage(@PathVariable("id")long id, ModelMap model) {
		// 工会信息
		model.addAttribute("union", service.getUnion(id));
		
		return "union-detail-manage";
	}
	
	/** 
	* @Description: 工会成员 列表
	* @param @param id
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/users/{id}")
	@ResponseBody
	public ResponsePage usersSearch(@PathVariable("id")long id, RequestPage page) {
		// 搜索条件
		String params = "t.union_id = " + id + " ORDER BY t.create_time DESC";
		// 查询表名
		String fileds = "t.id,u.head_picture picture, u.nick_name, u.phone, IF(un.clo=t.user_id,'会长','成员') title , i.income, t.create_time";
		// 查询字段
		String tableName = "union_user t"
				+ " LEFT JOIN `union` un on un.union_id = t.union_id "
				+ " LEFT JOIN `user` u ON u.user_id = t.user_id"
				+ " LEFT JOIN (SELECT s.publish_user, IFNULL(SUM(t.total),0) income FROM server_order t LEFT JOIN `server` s ON s.id = t.server_id WHERE t.union_id = 1 GROUP BY s.publish_user) i ON i.publish_user = t.user_id ";
		return searchPage(tableName, params, page, fileds);
	}
	
	/** 
	* @Description: 设置职位 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/{id}/set_position")
	@ResponseBody
	public ResponseAjax setPosition(@PathVariable("id")long id, Integer position, Long userId, HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser)session.getAttribute(SysUser.SESSION_NAME));
		Union union = service.getUnion(id);
		if(union == null) {
			response.setFailure("工会不存在");
			return response;
		}
		List<Map<String, Object>> unionUser = service.getUnionUsers(id);
		if(unionUser == null || !unionUser.isEmpty()) {
			response.setFailure("工会成员异常");
			return response;
		}
		boolean unUnionUser = true;
		for(Map<String, Object> map : unionUser) {
			if(userId.equals(map.get("user_id"))) {
				unUnionUser = false;
				break;
			}
		}
		if(unUnionUser) {
			response.setFailure("用户不属于该工会");
			return response;
		}
		
		service.setPosition(id, position, userId, response);
		return response;
	}
}
