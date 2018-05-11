package com.wangzhixuan.service.impl;

import com.wangzhixuan.model.Notice;
import com.wangzhixuan.model.vo.NoticeVo;
import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.commons.utils.HttpClientUtil;
import com.wangzhixuan.commons.utils.PropertyConfigurer;
import com.wangzhixuan.mapper.NoticeMapper;
import com.wangzhixuan.service.INoticeService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-20
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {
	
	private static Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class); 
	@Autowired
	private NoticeMapper noticeMapper;
	@Autowired
	private PropertyConfigurer configurer;
	
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Notice> page = new Page<Notice>(pageInfo.getNowpage(), pageInfo.getSize());
        page.setOrderByField(pageInfo.getSort());
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<Notice> list = noticeMapper.selectNoticePage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public boolean insertNotice(Notice notice) {
		boolean success = this.insert(notice);
		List<Notice> list = noticeMapper.selectAllNotice();
		try {
			success = this.setActivitityUrl(list);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean deleteNotice(Notice notice) {
		boolean success = false;
		this.deleteById(notice.getId());
		List<Notice> list = noticeMapper.selectAllNotice();
		try {
			success = this.setActivitityUrl(list);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean setActivitityUrl(List<Notice> noticeList) throws UnsupportedEncodingException {
		
		String lisboaAddress = configurer.getProperty("ddmjAddress");
		String cmd = "gm_activity_set_activitity_url";
		List<NoticeVo> activityList = new ArrayList<>();
		for (int i = 0; i < noticeList.size(); i++) {
			Notice notice = noticeList.get(i);
			NoticeVo activityVo = new NoticeVo();
			activityVo.setCurrUrl(notice.getTitleImg());
			activityVo.setOpenUrl(notice.getContentImg());
			activityList.add(activityVo);
		}
		String jsonNotice = URLEncoder.encode(JSON.toJSONString(activityList), "UTF-8");
		String result = null;
		try {
			if(StringUtils.isEmpty(lisboaAddress)){
	        	throw new RuntimeException("空地址");
	        }
			result = HttpClientUtil.httpGet(lisboaAddress+cmd+"%20"+jsonNotice);
		} catch (Exception e) {
			logger.error("获取接口地址异常{}",e);
			return false;
		}
		if (result != null && result != "") {
			 //JSON格式转换
           JSONObject obj = JSONObject.parseObject(result);
           int success = obj.getIntValue("code");
           if(success == 0) {
	       		return true;
           } else {
        	   logger.error(obj.getString("content"));
        	   return false;
           }
           
       } else {
    	   logger.error("返回结果超时!");
    	   return false;
       }
	}
	
}
