package com.wangzhixuan.model;

import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhixuan.wang
 * @since 2017-12-18
 */
public class Wechat extends Model<Wechat> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
	private Integer id;
	private String wechat;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
