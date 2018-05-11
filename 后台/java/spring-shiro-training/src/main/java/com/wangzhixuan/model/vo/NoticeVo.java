package com.wangzhixuan.model.vo;

import com.wangzhixuan.commons.utils.JsonUtils;
import java.io.Serializable;

/**
 * <p>
 * 公告表
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-01-20
 */
public class NoticeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标题图片
     */
	private String currUrl;
    /**
     * 内容图片
     */
	private String openUrl;

	
	public String getCurrUrl() {
		return currUrl;
	}


	public void setCurrUrl(String currUrl) {
		this.currUrl = currUrl;
	}


	public String getOpenUrl() {
		return openUrl;
	}


	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}
	
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
	
}
