package com.guse.four_one_nine.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.guse.four_one_nine.common.DateUtil;
import com.guse.four_one_nine.common.JSONUtil;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.ServerOrderDao;
import com.guse.four_one_nine.dao.UnionDao;
import com.guse.four_one_nine.dao.UnionUserDao;
import com.guse.four_one_nine.dao.model.Union;
import com.guse.four_one_nine.dao.model.UnionUser;

/** 
* @ClassName: UnionService 
* @Description: 工会服务类
* @author Fily GUSE
* @date 2018年1月13日 下午4:40:55 
*  
*/
@Service
public class UnionService {
	public final static Logger logger = LoggerFactory.getLogger(UnionService.class);
	
	@Autowired
	UnionDao dao;
	@Autowired
	ServerOrderDao orderDao;
	@Autowired
	UnionUserDao uuDao;
	@Autowired
	AppInformService appService;
	
	/** 
	* @Description: 根据id获取工会信息 
	* @param @param id
	* @param @return
	* @return Union 
	* @throws 
	*/
	public Union getUnion(long id) {
		
		return dao.getById(id);
	}
	
	/** 
	* @Description: 获取工会成员列表 
	* @param @param id
	* @param @return
	* @return List<UnionUser> 
	* @throws 
	*/
	public List<Map<String, Object>> getUnionUsers(long id) {
	
		return uuDao.findByUnionId(id);
	}
	
	/** 
	* @Description: 删除工会 
	* @param @param ids
	* @param @param response
	* @return void 
	* @throws 
	*/
	public void delete(String ids, ResponseAjax response) {
		// 获取审核数据
		List<Union> list = dao.findByIds(ids);
		String[] id = ids.split(",");
		if(list.size() == id.length) {
			int status = list.get(0).getStatus();
			// 判断所有内容都是同一个状态
			for(Union union : list) {
				if(union.getStatus() != status) {
					response.setFailure("数据异常"); return;
				}
			}
			// 通知app
			for(Union union : list) {
				try {
					pushUnionAction(union, UNIONACTION_DEL);
				} catch (Exception e) {
					response.setFailure(e.getMessage());
					return;
				}
			}
			
			// 修改状态
			dao.updateDelete(ids);
			// 清除工会成员
			uuDao.deleteByUnion(ids);
		} else {
			response.setFailure("提交数据异常");
		}
	}
	
	/** 
	* @Description: 统计工会收入 
	* @param @param id
	* @return void 
	* @throws 
	*/
	public Map<String, Integer> incomeCount(Long id){
		return orderDao.countByUnion(id);
	}
	
	/** 
	* @Description: 设置职位 
	* @param @param position
	* @param @param userId
	* @param @return
	* @return String 
	* @throws 
	*/
	public void setPosition(long id, Integer position, Long userId, ResponseAjax response) {
		// 通知app
		Union union = new Union();
		union.setUnion_id(id);
		union.setClo(userId);
		try {
			pushUnionAction(union, UNIONACTION_UPD);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			return;
		}
		
		dao.updateClo(id, userId);
	}

	/** 
	* @Description: 保存工会信息 
	* @param @param union
	* @param @param unionUserId
	* @param @param session
	* @return void 
	* @throws 
	*/
	@Transactional
	public void save(Union union, Long[] unionUserIds, ResponseAjax response) {
		// 验证工会成员不属于其他工会
		String ids = JSONUtil.toJSONArray(unionUserIds).toString();
		ids = ids.substring(1, ids.length() - 1);
		String param = " user_id in("+ids+")";
		if(union.getUnion_id() != null) {
			param += " and union_id <> "+union.getUnion_id();
		}
		List<UnionUser> existList = uuDao.existUnion(param);
		if(existList != null && existList.size() > 0) {
			response.setFailure("成员已存在于其他工会");
			return ;
		}
		
		String userIds = "";
		
		try {
		// 新建工会
		if(union.getUnion_id() == null) {
			union.setCreate_time(new Date());
			union.setCreater(response.getUser().getName());
			// 保存工会信息
			dao.addUnion(union);
			// 推送app
			pushUnionAction(union, UNIONACTION_ADD);
			
			// 成员信息
			userIds = "";
			for(Long userId : unionUserIds) {
				UnionUser uUser = new UnionUser();
				uUser.setUnion_id(union.getUnion_id());
				uUser.setUser_id(userId);
				uUser.setCreate_time(new Date());
				uUser.setCreater(response.getUser().getName());
				uuDao.addUnionUser(uUser);
				
				// 成员id 拼接
				userIds += (StringUtils.isNotBlank(userIds) ? "," : "");
				userIds += userId;
			}
			// 通知app
			pushUnionMemberAction(union.getUnion_id(), MEMBERACTION_ADD, userIds);
		} else {
			union.setUpdate_time(new Date());
			union.setUpdater(response.getUser().getName());
			dao.updateUnion(union);
			// 通知app
			pushUnionAction(union, UNIONACTION_UPD);
			
			// 处理工会原成员
			List<UnionUser> unionUsers = uuDao.findUnionUser(union.getUnion_id());
			// 删除修改后的成员
			userIds = "";
			for(UnionUser uu : unionUsers) {
				boolean isDel = true;
				for(long id : unionUserIds) {
					if(uu.getUser_id() == id) {
						isDel = false;
						break;
					}
				}
				if(isDel) {
					uuDao.deleteUnionUser(uu);
					userIds += StringUtils.isNotBlank(userIds) ? "," : "";
					userIds += uu.getUnion_id();
				}
			}
			// 通知app 删除成员
			pushUnionMemberAction(union.getUnion_id(), MEMBERACTION_DEL, userIds);
			
			// 添加新增成员
			userIds = "";
			for(long id : unionUserIds) {
				boolean isAdd = true;
				for(UnionUser uu : unionUsers) {
					if(uu.getUser_id() == id) {
						isAdd = false;
						break;
					}
				}
				if(isAdd) {
					UnionUser uUser = new UnionUser();
					uUser.setUnion_id(union.getUnion_id());
					uUser.setUnion_id(id);
					uUser.setCreate_time(new Date());
					uUser.setCreater(response.getUser().getName());
					uuDao.addUnionUser(uUser);
					
					// 记录成员编号
					userIds += StringUtils.isNotBlank(userIds) ? "," : "";
					userIds += uUser.getUnion_id();
				}
			}
			// 通知app 新增成员
			pushUnionMemberAction(union.getUnion_id(), MEMBERACTION_ADD, userIds);
		}
		} catch(Exception e) {
			e.printStackTrace();
			response.setFailure(e.getMessage());
			// 事物回滚
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}
	
	
	/************************************************
	 ***************** 推送app数据组装 ******************
	 ***********************************************/
	/** 
	* @Description: 推送 工会操作 
	* @param @param union 工会信息
	* @param @param action 操作类型， 1 新增，2 修改， 0 删除
	* @param @param response
	* @param @return
	* @return boolean 
	* @throws 
	*/
	private static final int UNIONACTION_ADD = 1;
	private static final int UNIONACTION_UPD = 2;
	private static final int UNIONACTION_DEL = 0;
	private void pushUnionAction(Union union, int action) throws Exception{
		String business = "gs-cloud-web-moc/user/unionAction";
		JSONObject params = new JSONObject();
		params.put("id", union.getUnion_id());
		params.put("action", action);
		if(StringUtils.isNotBlank(union.getUnion_name())) {
			params.put("name", union.getUnion_name());
		}
		if(StringUtils.isNotBlank(union.getUnion_logo())) {
			params.put("icon", union.getUnion_logo());
		}
		if(union.getClo() != null) {
			params.put("clo", union.getClo());
		}
		if(union.getIncome_ratio_clo() != null) {
			params.put("clo_ratio", union.getIncome_ratio_clo());
		}
		if(union.getCreate_time() != null) {
			params.put("create_date", DateUtil.formatCurrentDate(union.getCreate_time()));
		}
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	
	/** 
	* @Description: 推送工会成员信息 
	* @param @param unionId 工会标识
	* @param @param action 操作。1新增，0删除
	* @param @param userIds
	* @param @param response
	* @param @return
	* @return boolean 
	* @throws 
	*/
	private static final int MEMBERACTION_ADD = 1; // 新增操作
	private static final int MEMBERACTION_DEL = 0; // 删除操作
	private void pushUnionMemberAction(long unionId, int action, String userIds) throws Exception{
		if(StringUtils.isBlank(userIds)) {
			return;
		}
		String business = "gs-cloud-web-moc/user/unionMemberAction";
		JSONObject params = new JSONObject();
		params.put("union_id", unionId);
		params.put("action", action);
		params.put("user_id", userIds);
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}

}
