package com.guse.platform.dao.doudou;


import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.SystemNotice;


/**
 * system_notice
 * @see SystemNoticeMapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface SystemNoticeMapper extends  BaseMapper<SystemNotice, java.lang.Integer>{
	/**
	 * 获取公告下一个主键id
	 * @return
	 */
	Integer getNextNoticeId();
}
