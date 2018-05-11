package com.wangzhixuan.service;

import com.wangzhixuan.commons.result.PageInfo;
import com.wangzhixuan.model.Notice;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-20
 */
public interface INoticeService extends IService<Notice> {
	/**
	 * 页面查询
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);
	
	/**
	 * 增加公告
	 * @param notice 公告对象
	 * @return 是否增加成功
	 */
	boolean insertNotice(Notice notice);
	
	/**
	 * 删除公告
	 * @param notice 公告对象
	 * @return
	 */
	boolean deleteNotice(Notice notice);
	
	/**
	 * 设置公告URL通知游戏服务器
	 * @param noticeList
	 * @return
	 */
	boolean setActivitityUrl(List<Notice> noticeList) throws UnsupportedEncodingException; 
}
