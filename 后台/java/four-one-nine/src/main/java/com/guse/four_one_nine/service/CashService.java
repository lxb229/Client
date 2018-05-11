package com.guse.four_one_nine.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.common.DateUtil;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.CashApplyDao;
import com.guse.four_one_nine.dao.DayDealCountDao;
import com.guse.four_one_nine.dao.model.CashApply;

/** 
* @ClassName: CashService 
* @Description: 提现服务类
* @author Fily GUSE
* @date 2018年1月8日 下午2:42:54 
*  
*/
@Service
public class CashService {
	public final static Logger logger = LoggerFactory.getLogger(CashService.class);

	@Autowired
	DayDealCountDao dealDao;
	@Autowired
	CashApplyDao cashDao;
	@Autowired
	AppInformService appService;
	
	/** 
	* @Title: countCash 
	* @Description: 提现统计信息 
	* @param 
	* @return void 
	* @throws 
	*/
	public Map<String, Integer> countCash() {
		Map<String, Integer> map = dealDao.countCash();
		return map;
	}

	
	
	/** 
	* @Title: audit 
	* @Description: 批量审核 
	* @param @param ids
	* @param @param result
	* @param @return
	* @return String 
	* @throws 
	*/
	public void audit(String ids, int result, ResponseAjax response) {
		// 获取审核数据
		List<CashApply> list = cashDao.findByIds(ids);
		String[] id = ids.split(",");
		if(list.size() == id.length) {
			int status = list.get(0).getStatus();
			// 判断所有内容都是同一个状态
			for(CashApply cash : list) {
				if(cash.getStatus() != status) {
					response.setFailure("数据状态不一致"); return;
				}
			}
			if(status == CashApply.STATUS_FAIL || status == CashApply.STATUS_REMIT) {
				response.setFailure("该状态不能审核"); return;
			}
			// 已审核成功的不能拒绝
			if(status == CashApply.STATUS_SUCCESS && result == 0) {
				response.setFailure("已审核通过的不能拒绝"); return;
			}
			// 计算下级状态,拒绝为0，成功：未审核改为2(同意)，已审核改为3(已打款)
			int next_status = result == 0 ? 0 : status==CashApply.STATUS_APPLYING?CashApply.STATUS_SUCCESS:CashApply.STATUS_REMIT;
			
			// 通知app审核
			for(String str : id) {
				try {
					pushCashAudit(Integer.parseInt(str), result == 1 ? CASHAUDITRESULT_PASS : CASHAUDITRESULT_REJECT);
				} catch (NumberFormatException e) {
					response.setFailure("提交数据异常");
					return;
				} catch (Exception e) {
					response.setFailure(e.getMessage());
					return;
				}
			}
			
			// 修改状态
			cashDao.audit(ids, next_status);
		} else {
			response.setFailure("提交数据异常");
		}
	}
	
	/** 
	* @Title: remit 
	* @Description: 打款通知 
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	public void remit(String ids, ResponseAjax response) {
		// 获取审核数据
		List<CashApply> list = cashDao.findByIds(ids);
		String[] id = ids.split(",");
		if(list.size() == id.length) {
			int status = list.get(0).getStatus();
			// 判断所有内容都是同一个状态
			for(CashApply cash : list) {
				if(cash.getStatus() != status) {
					response.setFailure("数据状态不一致"); return;
				}
			}
			if(status == CashApply.STATUS_SUCCESS) {
				response.setFailure("该状态不能标记打款"); return;
			}
			// 通知app审核
			for(CashApply cash : list) {
				try {
					pushCashRemit(cash);
				} catch (Exception e) {
					response.setFailure(e.getMessage());
					return;
				}
			}
			
			// 修改状态
			cashDao.remit(ids, 3);
		} else {
			response.setFailure("提交数据异常");
		}
	}
	
	/************************************************
	 ***************** 推送app数据组装 ******************
	 ***********************************************/
	/** 
	* @Description: 审核 
	* @param @param cashId 提现信息标识
	* @param @param result 审核结果
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private static final int CASHAUDITRESULT_PASS = 1;
	private static final int CASHAUDITRESULT_REJECT = 0;
	private void pushCashAudit(int cashId, int result) throws Exception{
		String business = "gs-cloud-web-moc/cash/audit";
		JSONObject params = new JSONObject();
		params.put("cash_id", cashId);
		params.put("reuslt", result);
		params.put("remark", "");
		params.put("audit_time", DateUtil.formatCurrentDate(new Date()));
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	/** 
	* @Description: 打款提醒 
	* @param @param cashId
	* @param @param cashId
	* @param @param cashId
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushCashRemit(CashApply cashApply) throws Exception{
		String business = "gs-cloud-web-moc/cash/remit";
		JSONObject params = new JSONObject();
		params.put("cash_id", cashApply.getId());
		params.put("phone", cashApply.getPhone());
		params.put("money", cashApply.getMoney()==null ? 0 : cashApply.getMoney());
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
}
