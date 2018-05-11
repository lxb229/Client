package com.wangzhixuan.mapper;

import com.wangzhixuan.model.Notice;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
  * 公告表 Mapper 接口
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-20
 */
public interface NoticeMapper extends BaseMapper<Notice> {
	
	List<Notice> selectNoticePage(Pagination page, Map<String, Object> params);
	
	List<Notice> selectAllNotice();
	
}