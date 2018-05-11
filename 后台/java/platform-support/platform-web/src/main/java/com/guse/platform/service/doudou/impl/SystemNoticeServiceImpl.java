package com.guse.platform.service.doudou.impl;

import java.util.Date;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.common.base.Result;
import com.guse.platform.common.base.ValidataBean;
import com.guse.platform.common.utils.GameGMUtil;
import com.guse.platform.dao.doudou.SystemNoticeMapper;
import com.guse.platform.service.doudou.SystemNoticeService;
import com.guse.platform.vo.doudou.NoticeVo;
import com.guse.platform.entity.doudou.SystemNotice;

/**
 * system_notice
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class SystemNoticeServiceImpl extends BaseServiceImpl<SystemNotice, java.lang.Integer> implements SystemNoticeService{

	@Autowired
	private SystemNoticeMapper  systemNoticeMapper;
	@Autowired
	private GameGMUtil  gameGMUtil;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(systemNoticeMapper);
	}
	
	@Override
	public Result<Integer> saveOrUpdateProduct(SystemNotice notice) {
		ValidataBean validata = notice.validateModel();
		if(!validata.isFlag()){
			return new Result<Integer>(00000,validata.getMsg());
		}
		NoticeVo noticeVo = new NoticeVo();
		noticeVo.setTemplateId(notice.getSnTemplateId());
		noticeVo.setNoticeParams(notice.getSnContent());
		noticeVo.setStartTime(notice.getSnStartTime().getTime());
		Integer vialdTime = (int) ((notice.getSnEndTime().getTime()-notice.getSnStartTime().getTime())/1000);
		noticeVo.setVialdTime(vialdTime);
		noticeVo.setRepate(notice.getSnIntervalTime());
		
		noticeVo.setCmd(1);
		noticeVo.setNoticeId(systemNoticeMapper.getNextNoticeId());
		
		Result<Integer> gmSuccess = gameGMUtil.noticeService(noticeVo);
		if(!gmSuccess.isOk()) {
			return gmSuccess;
		}
		
		notice.setCreateTime(new Date());
		Integer success = systemNoticeMapper.insert(notice);
		return new Result<Integer>(success);
	}

	@Override
	public Result<Integer> deleteNotice(SystemNotice notice) {
		if(null == notice){
			return new Result<Integer>(00000,"删除公告失败，参数异常！");
		}
		NoticeVo noticeVo = new NoticeVo();
		noticeVo.setCmd(3);
		noticeVo.setNoticeId(notice.getSnId());
		Result<Integer> gmSuccess = gameGMUtil.noticeService(noticeVo);
		if(!gmSuccess.isOk()) {
			return gmSuccess;
		}
		
		return new Result<Integer>(systemNoticeMapper.deleteById(notice.getSnId()));
	}

}
