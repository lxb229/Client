package com.palmjoys.yf1b.act.replay.resource;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

@ResourceType("record")
public class RecordConfig {
	@ResourceId
	private int id;
	//回放数据提交地址
	private String replayUrl;
	//记录保存时间(小时)
	private int recordSaveTime;
	
	public String getReplayUrl() {
		return replayUrl;
	}
	public void setReplayUrl(String replayUrl) {
		this.replayUrl = replayUrl;
	}
	public int getRecordSaveTime() {
		return recordSaveTime;
	}
	public void setRecordSaveTime(int recordSaveTime) {
		this.recordSaveTime = recordSaveTime;
	}
	
	
}
