package com.palmjoys.yf1b.act.notice.resource;

import java.util.List;

import org.treediagram.nina.resource.annotation.ResourceId;
import org.treediagram.nina.resource.annotation.ResourceType;

import com.palmjoys.yf1b.act.notice.model.NoticeParamAttrib;

@ResourceType("notice")
public class NoticeTemplateConfig {
	@ResourceId
	private int modelId;
	//模板模式
	private List<NoticeParamAttrib> style;
	//有效时长(秒)
	private int unvalidTime;
	//重复频率(秒)
	private int repeat;
	
	public int getModelId() {
		return modelId;
	}
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}
	public List<NoticeParamAttrib> getStyle() {
		return style;
	}
	public void setStyle(List<NoticeParamAttrib> style) {
		this.style = style;
	}
	public int getUnvalidTime() {
		return unvalidTime;
	}
	public void setUnvalidTime(int unvalidTime) {
		this.unvalidTime = unvalidTime;
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	
	
}
