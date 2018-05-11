package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.dao.CashApplyDao;
import com.guse.four_one_nine.dao.model.CashApply;

/** 
* @ClassName: CashApplyService
* @Description: 提现申请管理
* @author: wangkai
* @date: 2018年1月9日 下午4:59:13 
*  
*/
@Service
public class CashApplyService {
	@Autowired
	CashApplyDao cashApplyDao;
	
	/**
	 * 新增提现申请记录
	 * 
	 * @param apply
	 */
	public void addCashApply(CashApply apply){
		cashApplyDao.addCashApply(apply);
	}
}
