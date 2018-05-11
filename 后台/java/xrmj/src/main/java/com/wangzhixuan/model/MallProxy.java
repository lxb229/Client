package com.wangzhixuan.model;

import com.wangzhixuan.commons.utils.StringUtils;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;


/**
 * <p>
 * 商城代理微信
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-04-19
 */
@TableName("mall_proxy")
public class MallProxy extends Model<MallProxy> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	private Integer id;
    /**
     * 代理类型
     */
	private String proxyType;
    /**
     * 代理微信号
     */
	private String wxNO;
    /**
     * 代理备注
     */
	private String proxyDesc;

	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		 if(StringUtils.isBlank(proxyType)){
			 vb.setMsg("请填写代理商类型！");
	         return vb;
		 }
		 if(StringUtils.isBlank(wxNO)) {
			 vb.setMsg("请填写代理商WX号！");
	         return vb;
		 }
		 if(StringUtils.isBlank(proxyDesc)) {
			 vb.setMsg("请填写代理商备注！");
	         return vb;
		 }
		 vb.setFlag(true);
	     return vb; 
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public String getWxNO() {
		return wxNO;
	}

	public void setWxNO(String wxNO) {
		this.wxNO = wxNO;
	}

	public String getProxyDesc() {
		return proxyDesc;
	}

	public void setProxyDesc(String proxyDesc) {
		this.proxyDesc = proxyDesc;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
