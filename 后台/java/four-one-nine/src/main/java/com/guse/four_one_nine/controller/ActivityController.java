package com.guse.four_one_nine.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guse.four_one_nine.controller.page.RequestPage;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.controller.page.ResponsePage;
import com.guse.four_one_nine.dao.model.Activity;
import com.guse.four_one_nine.service.ActivityService;
import com.guse.four_one_nine.service.util.AbstractSearchService;


/** 
* @ClassName: ActivityController 
* @Description: 活动相关适配器
* @author Fily GUSE
* @date 2018年1月9日 下午2:36:45 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("activity")
public class ActivityController extends AbstractSearchService {
	
	@Autowired
	ActivityService service;
	
	/** 
	* @Title: page 
	* @Description: 活动管理页面 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/")
	public String page(ModelMap model) {
		
		// 总参与人数和访问量
		model.addAttribute("attract", service.getAttract());
		
		return "activity-list";
	}
	
	
	/** 
	* @Title: search 
	* @Description: 管理页面列表查询 
	* @param @param page
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	@RequestMapping("/list")
	@ResponseBody
	public ResponsePage search(RequestPage page) {
		// 查询条件
		String params = "delete_mark = 1";
		if(StringUtils.isNotBlank(page.getText())) {
			params += " and t.activity_name like '%"+page.getText().trim()+"%'";
		}
		params += " ORDER BY t.id DESC";
		return searchPage("`activity` t", params, page);
	}
	@Override
	public String queryField() {
		return "t.id, t.activity_name name, t.participants_no participants, t.interview_no interview, t.criticism_no criticism, t.create_time";
	}
	
	/** 
	* @Title: delete 
	* @Description: 删除活动 
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/del")
	@ResponseBody
	public ResponseAjax delete(String ids) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(ids)) {
			response.setFailure("未选择活动");
		} 
		if(response.isSuccess()){
			service.delete(ids, response);
		}
		return response;
	}

	/** 
	* @Title: publishPage 
	* @Description: 发布活动界面 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/publish_activity")
	public String publishPage(ModelMap model) {
		
		return "activity-add";
	}
	
	/** 
	* @Title: publish 
	* @Description: 发布新活动 
	* @param @param act
	* @param @param info
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/publish")
	@ResponseBody
	public ResponseAjax publish(Activity act, String[] channel_name, String []channel_url, HttpSession session) {
		ResponseAjax response = new ResponseAjax();
		if(act == null) {
			response.setFailure("提交信息异常");
		} else {
			// 验证信息
			if(StringUtils.isBlank(act.getActivity_name())) {
				response.setFailure("未填写活动名称");
				return response;
			}
			if(act.getType() == null) {
				response.setFailure("请选择活动类型");
				return response;
			}
			if(StringUtils.isBlank(act.getActivity_cover())) {
				response.setFailure("未选择活动封面");
				return response;
			}
			if(act.getActivity_content().equals("<p><br></p>")) {
				response.setFailure("未填写活动内容");
				return response;
			}
			if(act.getStart_time() == null || act.getEnd_time() == null || act.getStart_time().getTime() > act.getEnd_time().getTime()) {
				response.setFailure("未选择正确活动时间");
				return response;
			}
			
			if(response.isSuccess()) {
				act.setCreater(session.getAttribute("login_name").toString());
				act.setCreate_time(new Date());
				service.publish(act, channel_name, channel_url, response);
			}
		}
		return response;
	}
	
	/** 
	* @Title: activityInfo 
	* @Description: 活动详情界面 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/activity_info")
	public String activityInfo(ModelMap model, Integer id) {
		Activity act = service.getActivity(id);
		// 获取活动详情
		model.addAttribute("activity", act);
		// 获取渠道连接
		model.addAttribute("channel", service.getChannel(id));
		// 参与活动用户
		model.addAttribute("applyers", service.getApplyers(id));
		// 活动时长
		model.addAttribute("duration", (int)(act.getEnd_time().getTime() - act.getStart_time().getTime()) / (1000*3600*24));
		// 最近5天活跃人员信息
		List<Map<String, Integer>> active = service.getWeekActive(id);
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		String activeTime = sp.format(new Date());
		int activeNum = 0;
		int trend = 0; // -1倒退，0持平，1增长
		if(active != null && !active.isEmpty()) {
			if(active.size() == 1) {
				trend = 1;
			} else {
				if(active.get(0).get("users") > active.get(0).get("users")) {
					trend = 1;
				} else if(active.get(0).get("users") < active.get(0).get("users")) {
					trend = -1;
				}
			}
		}
		// 当天时间
		model.addAttribute("activeTime", activeTime);
		// 活跃人数
		model.addAttribute("activeNum", activeNum);
		// 活跃趋势
		model.addAttribute("trend", trend);
		// 活动是否结束
		model.addAttribute("isOver", new Date().getTime() - act.getEnd_time().getTime());
		
		// 活动参与用户(新增用户数)
		model.addAttribute("toDayApply", act.getParticipants_no());
		// 人员分布占比
		model.addAttribute("ratio", service.ratio(id));
		
		return "activity-details";
	}
	
}
