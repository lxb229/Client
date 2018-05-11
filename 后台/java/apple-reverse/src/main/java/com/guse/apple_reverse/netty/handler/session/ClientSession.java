package com.guse.apple_reverse.netty.handler.session;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/** 
* @ClassName: ClientSession 
* @Description: 客服端session信息
* @author Fily GUSE
* @date 2017年11月23日 下午7:30:06 
*  
*/
public class ClientSession {
	// 客服端名称
	private String name;
	// 线程数
	private int threadNum = 0;
	// 当前使用数
	private int useNum = 0;
	// 支持查询类型查询类型
	private Map<String, String> queryType;
	// 连接时间
	private String connectDate;
	// 自动切换IP间隔
	private Integer cIPTime;
	// 客服端当前时间
	private String nowDate;

	/** 
	* @Title: getQueryType 
	* @Description: 获取查询类型 
	* @param @param query_type
	* @param @return
	* @return String 
	* @throws 
	*/
	public boolean checkedQueryType(int query_type) {
		if(queryType != null && !queryType.isEmpty()) {
			return StringUtils.isNotBlank(queryType.get(query_type + ""));
		}
		return false;
	}
	
	/** 
	* @Title: getQueryType 
	* @Description: 获取查询类型 
	* @param @param query_type
	* @param @return
	* @return String 
	* @throws 
	*/
	public String findQueryTypeName(int query_type) {
		if(queryType != null && !queryType.isEmpty()) {
			return queryType.get(query_type + "");
		}
		return null;
	}

	/* get/set */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public int getUseNum() {
		return useNum;
	}
	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}
	public Map<String, String> getQueryType() {
		return queryType;
	}
	public void setQueryType(Map<String, String> queryType) {
		this.queryType = queryType;
	}
	public String getConnectDate() {
		return connectDate;
	}
	public void setConnectDate(String connectDate) {
		this.connectDate = connectDate;
	}
	public Integer getcIPTime() {
		return cIPTime;
	}
	public void setcIPTime(Integer cIPTime) {
		this.cIPTime = cIPTime;
	}
	public String getNowDate() {
		return nowDate;
	}
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
}
