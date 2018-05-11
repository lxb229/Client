package com.wangzhixuan.model.vo;

import java.io.Serializable;
import com.wangzhixuan.commons.utils.JsonUtils;

/**
 * 红包领取返回对象
 * @description：RedVo
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class PlayerIntegralVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 玩家明星号
	 */
	private String start_no;
	/**
	 * 玩家积分
	 */
	private Integer integral;
	
	public String getStart_no() {
		return start_no;
	}

	public void setStart_no(String start_no) {
		this.start_no = start_no;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}