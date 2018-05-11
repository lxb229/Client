package com.guse.apple_reverse.service.query;

import java.util.List;

import com.guse.apple_reverse.netty.handler.session.Message;

/** 
* @ClassName: ListeningQuery 
* @Description: 查询监听接口
* @author Fily GUSE
* @date 2017年12月27日 下午4:35:31 
*  
*/
public interface ListeningQuery {

	/** 
	* @Title: queryFail 
	* @Description: 调用客服端失败或者消息本来就失败 ，自己实现处理方式
	* @param @param msg
	* @return void 
	* @throws 
	*/
	public void queryFail(Message msg);
	
	/** 
	* @Title: markQuery 
	* @Description: 查询信息标记 
	* @param @param msg
	* @param @param markTime
	* @return void 
	* @throws 
	*/
	public void markQuery(Message msg, Long markTime);
	
	/** 
	* @Title: getQuerys 
	* @Description: 获取监听列表 
	* @param @return
	* @return List<QueryBean> 
	* @throws 
	*/
	public List<QueryBean> getQuerys();
}
