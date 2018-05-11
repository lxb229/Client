package com.guse.four_one_nine.service.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.four_one_nine.controller.page.RequestPage;
import com.guse.four_one_nine.controller.page.ResponsePage;
import com.guse.four_one_nine.dao.util.SearchDao;

/** 
* @ClassName: AbstractSearchService 
* @Description: 抽象列表查询服务类
* @author Fily GUSE
* @date 2018年1月8日 下午4:52:33 
*  
*/
@Service
public abstract class AbstractSearchService {
	
	@Autowired
	SearchDao dao;
	
	/** 
	* @Title: search 
	* @Description: 执行查询 
	* @param @param page
	* @param @param tableName
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	public ResponsePage searchPage(String tableName, String params, RequestPage reqPage) {
		return executeQuery(tableName, params, reqPage, queryField());
	}
	
	/** 
	* @Title: search 
	* @Description: 执行查询 
	* @param @param page
	* @param @param tableName
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	public ResponsePage searchPage(String tableName, String params, RequestPage reqPage, String fields) {
		return executeQuery(tableName, params, reqPage, fields);
	}
	// 查询字段
	public abstract String queryField();
	
	/** 
	* @Description: 执行分页查询 
	* @param @param tableName
	* @param @param params
	* @param @param reqPage
	* @param @param fields
	* @param @return
	* @return ResponsePage 
	* @throws 
	*/
	private ResponsePage executeQuery(String tableName, String params, RequestPage reqPage, String fields) {
		ResponsePage page = new ResponsePage();
		// 查询条件
		if(StringUtils.isBlank(params)) {
			params = "1=1";
		}
		// 查询总条数
		page.setTotal(dao.searchAll(tableName, params));
		// 设置分页
		if(page.getTotal() > 0) {
			Integer index = reqPage.getPage();
			Integer showNum = reqPage.getShowNum();
			// 查询列
			String field = fields;
			String limit = "";
			// 计算分页
			if(index != null) {
				// 页码数
				int pages = (page.getTotal()/showNum) + (page.getTotal()%showNum > 0 ? 1 : 0);
				page.setPages(pages);
				// 查询页码矫正
				if(index <= 0) {
					index = 1;
				}
				if(index > page.getPages()) {
					index = page.getPages();
				}
				page.setNowPage(index);
				limit = "limit " + (index-1)*showNum + "," +showNum;
			}
			// 执行查询
			page.setData(dao.search(tableName, field, params, limit));
		}
		return page;
	}

}
