package com.guse.apple_reverse_client.netty.handler;

/** 
* @ClassName: QueryLine 
* @Description: 查询线路
* @author Fily GUSE
* @date 2017年11月23日 下午5:28:24 
*  
*/
public class QueryLine {
	
	private int port; //端口
	private int status = STATUS_IDLE; //状态：0.闲置 1.繁忙 
	public static int STATUS_IDLE = 0;
	public static int STATUS_BUSY = 1;
	
	public QueryLine(int port) {
		this.port = port;
	}

	/*  get/set  */
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
