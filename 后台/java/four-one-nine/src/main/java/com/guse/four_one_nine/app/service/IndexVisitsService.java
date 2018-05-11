package com.guse.four_one_nine.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.app.model.IndexVisits;
import com.guse.four_one_nine.dao.IndexVisitsDao;

/** 
* @ClassName: IndexVisitsService 
* @Description: 活动模块统计
* @author wangkai
* @date 2018年2月4日 下午2:03:43 
*  
*/
@Service
public class IndexVisitsService {
	
	@Autowired
	IndexVisitsDao indexVisitsDao;
	
	/** 
	* @Title: indexVisitsCount 
	* @Description: 活动模块信息统计 
	* @param @param order
	* @return void 
	* @throws 
	*/
	public void indexVisitsCount(IndexVisits indexVisits) {
		
		IndexVisits old_indexVisits = indexVisitsDao.getToIndexVisits();
		//故事接龙访问量（次数）
		indexVisits.setStory_solitaire_visits(old_indexVisits.getStory_solitaire_visits()+indexVisits.getStory_solitaire_visits());

		//交易访问量（次数）
		indexVisits.setTransaction_visits(old_indexVisits.getTransaction_visits()+indexVisits.getTransaction_visits());

		//涂鸦访问量（次数）
		indexVisits.setGraffiti_visits(old_indexVisits.getGraffiti_visits()+indexVisits.getGraffiti_visits());

		//日签访问量（次数）
		indexVisits.setDay_sign_visits(old_indexVisits.getDay_sign_visits()+indexVisits.getDay_sign_visits());

		//活动访问量（次数）
		indexVisits.setActivities_visits(old_indexVisits.getActivities_visits()+indexVisits.getActivities_visits());

		//海报访问量（次数）
		indexVisits.setPosters_visits(old_indexVisits.getPosters_visits()+indexVisits.getPosters_visits());

		
		indexVisitsDao.save(indexVisits);
	}
}
