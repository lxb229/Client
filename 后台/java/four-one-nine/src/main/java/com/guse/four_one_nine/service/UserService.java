package com.guse.four_one_nine.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.app.service.DayUserCountService;
import com.guse.four_one_nine.controller.page.ResponseAjax;
import com.guse.four_one_nine.dao.DayUserCountDao;
import com.guse.four_one_nine.dao.ServerOrderDao;
import com.guse.four_one_nine.dao.UserDao;
import com.guse.four_one_nine.dao.model.DayUserCount;
import com.guse.four_one_nine.dao.model.User;

/** 
* @ClassName: UserService 
* @Description: 用户服务类
* @author Fily GUSE
* @date 2018年1月12日 下午6:03:04 
*  
*/
@Service("userService")
public class UserService {
	public final static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserDao dao;
	@Autowired
	DayUserCountDao countDao;
	@Autowired
	DayUserCountService userCountService;
	@Autowired
	ServerOrderDao orderDao;
	@Autowired
	AppInformService appService;
	
	/** 
	* @Description: 获取用户信息 
	* @param @return
	* @return Map<String,Integer> 
	* @throws 
	*/
	public Map<String, Integer> getCount() {
		
		return countDao.count();
	}
	
	/** 
	* @Description: 获取今日统计信息 
	* @param @return
	* @return DayUserCount 
	* @throws 
	*/
	public DayUserCount getToDayCount() {
		
		return userCountService.findToDay();
	}
	
	/** 
	* @Description: 获取用户信息 
	* @param @param id
	* @param @return
	* @return User 
	* @throws 
	*/
	public User getUser(long id) {
		
		return dao.getUser(id);
	}
	
	/** 
	* @Description: 获取用户消费金额 
	* @param @param id
	* @param @return
	* @return Integer 
	* @throws 
	*/
	public Integer getExpenditure(long id) {
		
		return orderDao.countUserExpenditure(id);
	}
	
	/** 
	* @Description: 冻结，解冻用户 
	* @param @param ids
	* @param @param result
	* @param @param session
	* @return void 
	* @throws 
	*/
	public void freeze(String ids, int result, ResponseAjax response) {
		// 获取审核数据
		List<User> list = dao.findByIds(ids);
		String[] id = ids.split(",");
		if(list.size() == id.length) {
			int status = list.get(0).getStatus();
			// 判断所有内容都是同一个状态
			for(User user : list) {
				if(user.getStatus() != status) {
					response.setFailure("数据状态不一致"); return;
				}
			}
			if(result == status) {
				response.setFailure("当前状态不能操作"); return;
			}

			// 推送app
			for(User user : list) {
				try {
					pushInterdictAction(user.getUser_id(), status == 0 ? INTERDICTACTIONOPERATION_UNFREEZE : INTERDICTACTIONOPERATION_FREEZE);
				} catch (Exception e) {
					response.setFailure(e.getMessage());
					return;
				}
			}
			// 修改状态
			if(response.isSuccess()) {
				dao.freeze(ids, result);
			}
		} else {
			response.setFailure("提交数据异常");
		}
	}

	/** 
	* @Description: 重置密码 
	* @param @param id
	* @param @param response
	* @return void 
	* @throws 
	*/
	public void resetPwd(long id, ResponseAjax response) {
		// 推送app
		try {
			pushResetPwd(id, RESETPWD_OPERATION);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
		}
	}
	
	/** 
	* @Description: 删除封面图片 
	* @param @param id
	* @param @param picture
	* @param @param response
	* @return void 
	* @throws 
	*/
	public void delCover(long id, String picture, ResponseAjax response) {
		User user = getUser(id);
		if(StringUtils.isBlank(user.getCover_picture()) 
				|| user.getCover_picture().indexOf(picture) == -1) {
			response.setFailure("信息不存在");
			return ;
		}
		// 通知app
		try {
			pushDeleteCover(id, picture);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			return;
		}
		
		// 数据库删除图片
		String cover = new String();
		for(String str : user.getCover_picture().split(",")) {
			if(!str.equals(picture)) {
				cover += StringUtils.isBlank(cover) ? "" : ",";
				cover += str;
			}
		}
		user.setCover_picture(cover);
		dao.updateUser(user);
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
	private void pushDeleteCover(long userId, String coverPicture) throws Exception{
		String business = "gs-cloud-web-moc/user/deleteCover";
		JSONObject params = new JSONObject();
		params.put("user_id", userId);
		params.put("cover_picture", coverPicture);
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	
	/** 
	* @Description: 冻结、解冻 用户 
	* @param @param userId 用户标识
	* @param @param operation 操作
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private static final int INTERDICTACTIONOPERATION_FREEZE = 1;
	private static final int INTERDICTACTIONOPERATION_UNFREEZE = 2;
	private void pushInterdictAction(long userId, int operation) throws Exception{
		String business = "gs-cloud-web-moc/user/updateStatus";
		JSONObject params = new JSONObject();
		params.put("user_id", userId);
		params.put("operation", operation);
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	
	/** 
	* @Description: 重置用户密码 
	* @param @param userId 用户密码
	* @param @param operation 新密码
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private static final String RESETPWD_OPERATION = "88888888";
	private void pushResetPwd(long userId, String operation) throws Exception{
		String business = "gs-cloud-web-moc/user/resetPwd";
		JSONObject params = new JSONObject();
		params.put("user_id", userId);
		params.put("operation", operation);
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
}
