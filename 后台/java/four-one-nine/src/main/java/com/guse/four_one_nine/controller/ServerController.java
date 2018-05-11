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
import com.guse.four_one_nine.dao.model.Server;
import com.guse.four_one_nine.dao.model.SysUser;
import com.guse.four_one_nine.service.ServerService;
import com.guse.four_one_nine.service.UserService;
import com.guse.four_one_nine.service.util.AbstractSearchService;

/** 
* @ClassName: ServerController 
* @Description: 服务管理 适配器
* @author Fily GUSE
* @date 2018年1月10日 上午11:38:07 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("/server")
public class ServerController extends AbstractSearchService{
	
	@Autowired
	ServerService service;
	@Autowired
	UserService userService;
	
	/** 
	* @Description: 服务分类界面
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/classify_page/")
	public String classifyPage(ModelMap model) {
		// 统计信息
		model.addAttribute("count", service.getCount());
		// 分类统计信息
		model.addAttribute("classifyCount", service.getClassifyCount());
		// 未分类的服务
		model.addAttribute("unClassify", service.getUnClassify());
		
		return "server-class";
	}
	
	/** 
	* @Title: addClassify 
	* @Description: 新建服务分类 
	* @param @param className
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/add_classify")
	@ResponseBody
	public ResponseAjax addClassify(String className,String classCode, HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser)session.getAttribute(SysUser.SESSION_NAME));
		if(StringUtils.isBlank(className)) {
			response.setFailure("分类名称必填"); return response;
		}
		if(StringUtils.isBlank(classCode)) {
			response.setFailure("分类编码必填"); return response;
		}
		
		if(response.isSuccess()) {
			service.addClassify(className, classCode, response);
		}
		return response;
	}	
	/** 
	* @Title: addClassify 
	* @Description: 新建服务归类 
	* @param @param className
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/add_serverClassify")
	@ResponseBody
	public ResponseAjax addServerClassify(String classCode,String ids, HttpSession session) {
		ResponseAjax response = new ResponseAjax((SysUser)session.getAttribute(SysUser.SESSION_NAME));
		if(StringUtils.isBlank(classCode)) {
			response.setFailure("分类必选"); return response;
		}if(StringUtils.isBlank(ids)) {
			response.setFailure("服务必选"); return response;
		}
		
		if(response.isSuccess()) {
			service.addServerClassify(classCode,ids, response);
		}
		return response;
	}
	
	/** 
	* @Description: 服务列表界面 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/list_page/")
	public String listPage(ModelMap model) {
		// 全部分类
		model.addAttribute("classifys", service.getClassifys());
		
		return "server-list";
	}
	
	/** 
	* @Title: list 
	* @Description: 服务列表 
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/list")
	@ResponseBody
	public ResponsePage searchg(RequestPage page) {
		// 查询条件
		String params = " t.status=1";
		if(StringUtils.isNotBlank(page.getText())) {
			params+= " and (t.`name` like '%"+page.getText()+"%'" + " or u.nick_name like '%"+page.getText()+"%')";
		}
		if(page.getType() != null) {
			// 
			switch(page.getType()) {
			 case -1: // 未分类
				 params += " and t.classify_id is null";
				 break;
			 case 0: // 全部
				 break;
			default:
				params += " and t.classify_id = " + page.getType();
				break;
			}
		}
		// 查询表名部分
		String tableName = "`server` t  "
		 		+ "LEFT JOIN classify c ON c.id = t.classify_id "
		 		+ "LEFT JOIN `user` u ON u.user_id = t.publish_user "
		 		+ "LEFT JOIN (SELECT COUNT(1)sell_num,SUM(tip_money) tip_money,server_id from server_order GROUP BY server_id) o ON o.server_id = t.id "
		 		+ "LEFT JOIN (select COUNT(1) comment_num, server_id FROM server_comment GROUP BY server_id) ct ON ct.server_id = t.id";
		return searchPage(tableName, params, page);
	}
	@Override
	public String queryField() {
		
		return "t.id, t.`name`, c.classify_name, u.nick_name, o.sell_num, t.price,ct.comment_num, o.tip_money, t.publish_time";
	}
	
	
	/** 
	* @Description: 服务详情界面 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail_page/{id}")
	public String detailPage(@PathVariable("id")int id, ModelMap model) {
		// 获取服务信息
		Server server = service.getService(id);
		model.addAttribute("server", server);
		// 发布者信息
		model.addAttribute("publish", userService.getUser(server.getPublish_user()));
		// 统计信息
		model.addAttribute("count", service.getServerCount(id));
		// 订单列表
		model.addAttribute("orders", service.getOrders(id));
		// 评价列表
		model.addAttribute("comments", service.getComments(id));
		
		return "server-detail";
	}
	
	/** 
	* @Description: 服务收支明细界面 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail/income/{id}")
	public String detailIncomePage(@PathVariable("id")int id) {
		
		return "server-detail-income";
	}
	
	/** 
	* @Description: 服务明细 收支明细列表 
	* @param @param id
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/orders/{id}")
	@ResponseBody
	public ResponsePage orderSearch(@PathVariable("id")long id, RequestPage page) {
		// 不分页
		page.setPage(null);
		// 搜索条件
		String params = "t.server_id = "+id+" ORDER BY t.buy_time DESC";
		// 查询表名
		String tableName = "server_order t LEFT JOIN `user` u ON u.user_id = t.buy_user ";
		// 查询字段
		String fileds = "t.id,u.head_picture picture, u.nick_name, t.total, t.tip_money, t.`status` income_status, t.buy_time, t.`status` order_status";
		return searchPage(tableName, params, page, fileds);
	}
	
	/** 
	* @Description: 服务评价界面 
	* @param @param id
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/detail/comment/{id}")
	public String detailCommentPage(@PathVariable("id")int id) {
		
		return "server-detail-comment";
	}
}
