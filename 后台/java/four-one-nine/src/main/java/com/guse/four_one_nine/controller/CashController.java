package com.guse.four_one_nine.controller;

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
import com.guse.four_one_nine.service.CashService;
import com.guse.four_one_nine.service.util.AbstractSearchService;

/** 
* @ClassName: CashController 
* @Description: 提现管理 适配器
* @author Fily GUSE
* @date 2018年1月8日 上午11:35:29 
*  
*/
@Controller
@Scope("prototype")
@RequestMapping("cash")
public class CashController extends AbstractSearchService{
	
	@Autowired
	CashService cashService;

	/** 
	* @Title: index 
	* @Description: 界面跳转 
	* @param @param model
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/")
	public String page(ModelMap model) {
		// 统计信息
		model.addAttribute("count", cashService.countCash());
		return "withdrawals-list";
	}
	
	
	/** 
	* @Title: list 
	* @Description: 列表表格信息 
	* @param 
	* @return void 
	* @throws 
	*/
	@RequestMapping("/list")
	@ResponseBody
	public ResponsePage list(RequestPage page) {
		String params = "1=1";
		if(StringUtils.isNotBlank(page.getText())) {
			params += " and(u.nick_name like '%"+page.getText()+"%' "
					+ "or t.account like '%"+page.getText()+"%' "
					+ ")";
		}
		if(page.getType() != null) {
			switch (page.getType()) {
			case 1:
				params += " and t.status=1";
				break;
			case 2: 
				params += " and t.status=2";
				break;
			case 3:
				params += " and t.status=0";
				break;
			default:
				break;
			}
		}
		return searchPage("cash_apply t LEFT JOIN `user` u ON t.user_id = u.user_id", params, page);
	}
	@Override
	public String queryField() {
		return "t.id,u.head_picture picture, u.nick_name name, t.phone, t.account_type, t.account,t.money, t.apply_time, t.`status`";
	}
	
	/** 
	* @Title: audit 
	* @Description: 审核提现 (批量)
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/audit")
	@ResponseBody
	public ResponseAjax audit(String ids, int result) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(ids)) {
			response.setFailure("请选择需要审核的申请信息");
			return response;
		}
		
		if(response.isSuccess()) {
			cashService.audit(ids, result, response);
		}
		return response;
	}
	
	/** 
	* @Title: remit 
	* @Description: 打款提醒 
	* @param @return
	* @return String 
	* @throws 
	*/
	@RequestMapping("/remit")
	@ResponseBody
	public ResponseAjax remit(String ids) {
		ResponseAjax response = new ResponseAjax();
		if(StringUtils.isBlank(ids)) {
			response.setFailure("请选择已打款的申请信息");
			return response;
		}
		
		if(response.isSuccess()) {
			cashService.remit(ids, response);
		}
		
		return response;
	}
}
