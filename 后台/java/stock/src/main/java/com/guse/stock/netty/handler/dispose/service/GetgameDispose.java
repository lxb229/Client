package com.guse.stock.netty.handler.dispose.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guse.stock.common.JSONUtils;
import com.guse.stock.dao.IGameDao;
import com.guse.stock.netty.handler.dispose.AbstractDispose;

/** 
* @ClassName: GetgameDispose 
* @Description: 获取游戏
* @author Fily GUSE
* @date 2017年8月28日 下午5:46:18 
*  
*/
@Component
public class GetgameDispose extends AbstractDispose {

	/** 
	* @Fields pt : 协议号
	*/
	public Integer pt = 3;
	
	@Autowired
	IGameDao gameDao;
	
	@Override
	public void processing() {
		// 获取业务参数
		JSONObject data = JSONUtils.toJSONObject(msg.getData());
		int page_size = data.getInt("page_size");
		int page_num = data.getInt("page_num");
		page_size = page_size<5 ? 5 : page_size; // 最少每页5条
		page_num = page_num<0 ? 1 : page_num; // 从第一页开始
		
		// 获取全部条数
		int total = gameDao.findAllCount();
		
		// 计算当前页数
		int pages = total/page_size + (total%page_size>0 ? 1: 0);
		pages = pages==0 ? 1 : pages;
		page_num = page_num>pages ? pages : page_num;
		// 开始条数
		int start = (page_num-1) * page_size;
		
		// 查询游戏信息
		List<Map<String, Object>> list = gameDao.findAll(start, page_size);
		
		// 封装返回结果
		data.clear();
		data.put("total", total);
		data.put("page_num", page_num);
		data.put("page_size", page_size);
		data.put("games", JSONUtils.toJSONString(list));
		msg.setData(data.toString());

	}

}
