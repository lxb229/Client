package com.guse.apple_reverse.dao.model;

public class AppleQueryServers {

	private int id;
	private String ip; //IP地址
	private int port; //端口
	private int status; //状态：0.闲置 1.繁忙 2.维护中
	public static int STATUS_IDLE = 0;
	public static int STATUS_BUSY = 1;
	public static int STATUS_MAINTAIN = 2;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
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
