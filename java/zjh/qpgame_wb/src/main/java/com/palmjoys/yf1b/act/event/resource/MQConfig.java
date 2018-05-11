package com.palmjoys.yf1b.act.event.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("event")
public class MQConfig {
	@ResourceId
	private int id;
	//消费者Id
	private String producerId;
	//阿里云身份验证
	private String accessKey;
	//阿里云身份验证
	private String secretKey;
	//TCP接入域名
	private String onSAddr;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProducerId() {
		return producerId;
	}
	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getOnSAddr() {
		return onSAddr;
	}
	public void setOnSAddr(String onSAddr) {
		this.onSAddr = onSAddr;
	}
}
