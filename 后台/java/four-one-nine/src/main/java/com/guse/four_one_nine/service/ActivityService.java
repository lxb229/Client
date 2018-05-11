package com.guse.four_one_nine.service;

import java.util.ArrayList;
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
import com.guse.four_one_nine.dao.ActivityApplyDao;
import com.guse.four_one_nine.dao.ActivityDao;
import com.guse.four_one_nine.dao.ActivityInfoDao;
import com.guse.four_one_nine.dao.ActivityVisitDao;
import com.guse.four_one_nine.dao.model.Activity;
import com.guse.four_one_nine.dao.model.ActivityInfo;
import com.guse.four_one_nine.dao.model.User;

/** 
* @ClassName: ActivityService 
* @Description: 活动服务类
* @author Fily GUSE
* @date 2018年1月9日 下午2:52:12 
*  
*/
@Service
public class ActivityService {
	public final static Logger logger = LoggerFactory.getLogger(ActivityService.class);
	
	@Autowired
	ActivityDao dao;
	@Autowired
	ActivityInfoDao infoDao;
	@Autowired
	ActivityApplyDao applyDao;
	@Autowired
	ActivityVisitDao visitDao;
	@Autowired
	AppInformService appService;
	
	/** 
	* @Title: attract 
	* @Description: 总参与人数和访问量 
	* @param @return
	* @return int 
	* @throws 
	*/
	public Map<String, Integer> getAttract() {
		
		return dao.findAttract();
	}
	
	/** 
	* @Title: delete 
	* @Description: 批量删除活动 
	* @param @param ids
	* @param @return
	* @return String 
	* @throws 
	*/
	public void delete(String ids, ResponseAjax response) {
		// 通知app
		String [] id = ids.split(",");
		for(String i : id) {
			try {
				pushActivityDelete(Integer.parseInt(i));
			} catch (NumberFormatException e) {
				response.setFailure("提交数据异常");
				return;
			} catch (Exception e) {
				response.setFailure(e.getMessage());
				return;
			}
		}
		if(response.isSuccess()) {
			dao.delete(ids);
		}
	}
	
	/** 
	* @Title: publish 
	* @Description: 发布活动 
	* @param @param act
	* @param @param infos
	* @param @return
	* @return String 
	* @throws 
	*/
	@Transactional
	public void publish(Activity act, String[] channel_name, String[] channel_url, ResponseAjax response) {
		// 保存活动信息
		dao.addActivity(act);
		// 保存渠道连接
		List<ActivityInfo> infoList = new ArrayList<ActivityInfo>();
		if(channel_name != null && channel_name.length > 0) {
			
			for(int r = 0; r < channel_name.length; r ++) {
				if(StringUtils.isNotBlank(channel_name[r]) 
						&& StringUtils.isNotBlank(channel_url[r])) {
					ActivityInfo info = new ActivityInfo();
					info.setActivty_id(act.getId());
					info.setChannel_name(channel_name[r]);
					info.setChannel_url(channel_url[r]);
					infoDao.addActivityInfo(info);
					infoList.add(info);
				}
			}
		}
		// 推送app
		try {
			pushActivityPublish(act, infoList);
		} catch (Exception e) {
			response.setFailure(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}
	
	/** 
	* @Title: getActivity 
	* @Description: 查询活动信息 
	* @param @param id
	* @param @return
	* @return Activity 
	* @throws 
	*/
	public Activity getActivity(int id) {
		
		return dao.getById(id);
	}
	
	/** 
	* @Title: getChannel 
	* @Description: 获取活动渠道连接 
	* @param @param id
	* @param @return
	* @return List<ActivityInfo> 
	* @throws 
	*/
	public List<ActivityInfo> getChannel(int id) {
		
		return infoDao.findByActivityId(id);
	}
	
	/** 
	* @Title: getApplyers 
	* @Description: 获取活动参与人员信息 
	* @param @param id
	* @param @return
	* @return List<ActivityApply> 
	* @throws 
	*/
	public List<User> getApplyers(int id) {
		
		return applyDao.findByActivityId(id);
	}
	
	/** 
	* @Title: getFiveActive 
	* @Description: 7天活跃人数 
	* @param @param id
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	public List<Map<String, Integer>> getWeekActive(int id) {
		
		return visitDao.findWeekActive(id);
	}
	
	/** 
	* @Title: ratio 
	* @Description: 参与人员占比 
	* @param @param id
	* @param @return
	* @return List<Map<String,Integer>> 
	* @throws 
	*/
	public List<Map<String, String>> ratio(int id) {
		List<Map<String, String>> sources = applyDao.ratio(id);
		if(sources == null || sources.isEmpty()) {
			return null;
		}
		int total = 0; // 总数
		for(Map<String, String> map : sources) {
			total += Integer.parseInt(map.get("num"));
		}
		
		// 计算占比
		for(Map<String, String> map : sources) {
			map.put("ratio", ((Double.parseDouble(map.get("num"))/(double)total)*100) + "");
		}
		return sources;
	}

	
	/************************************************
	 ***************** 推送app数据组装 ******************
	 ***********************************************/
	/** 
	* @Description: 发布活动 
	* @param @param act 活动信息
	* @param @param infoList 渠道连接
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushActivityPublish(Activity act, List<ActivityInfo> infoList) throws Exception{
		String business = "gs-cloud-web-moc/activity/publish";
		JSONObject params = new JSONObject();
		params.put("id", act.getId());
		params.put("name", act.getActivity_name());
		params.put("icon", act.getActivity_cover());
		params.put("mark_top", act.getMark_top());
		params.put("type", act.getType());
		params.put("start_time", DateUtil.formatCurrentDate(act.getStart_time()));
		params.put("end_time", DateUtil.formatCurrentDate(act.getEnd_time()));
		params.put("max_players", act.getParticipants_no());
		params.put("content", act.getActivity_content());
		if(infoList.size() > 0) {
			params.put("place_link", JSONUtil.toJSONString(infoList));
		}
		params.put("publisher", act.getCreater());
		params.put("publishtime", DateUtil.formatCurrentDate(act.getCreate_time()));
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
	
	/** 
	* @Description: 删除活动 
	* @param @param id 活动标识
	* @param @throws Exception
	* @return void 
	* @throws 
	*/
	private void pushActivityDelete(int id) throws Exception{
		String business = "gs-cloud-web-moc/activity/delete";
		JSONObject params = new JSONObject();
		params.put("activity_id", id);
		
		params = appService.sendMessage(business, params);
		if(params.getInt("code") != 0) {
			String msg = params.getString("msg");
			logger.error("推送消息到app出现异常,接口：[{}], 异常提示：[{}], 接口参数:[{}]", business, msg, params.toString());
			throw new Exception(msg);
		}
	}
}
