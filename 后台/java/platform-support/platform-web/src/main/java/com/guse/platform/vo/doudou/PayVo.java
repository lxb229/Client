package com.guse.platform.vo.doudou;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * SystemLuckVo
 * @author nbin
 * @date 2017年7月18日 下午2:04:29 
 * @version V1.0
 */
public class PayVo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7830492733186810404L;
	//columns START
	
	/** 商户订单号 */
	private String out_trade_no;
	/** 商品名称 */
	private String subject;
	/** 付款金额 */
	private double total_fee;
	/** 商品描述 */
	private String body ;
	/**微信支付二维码*/
	private String payWXImage;
	
	//columns END

	public String getOut_trade_no() {
		return out_trade_no;
	}
	
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public double getTotal_fee() {
		return total_fee;
	}
	
	public void setTotal_fee(double total_fee) {
		this.total_fee = total_fee;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public String getPayWXImage() {
		return payWXImage;
	}

	public void setPayWXImage(String payWXImage) {
		this.payWXImage = payWXImage;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getOut_trade_no())
			.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PayVo == false) return false;
		if(this == obj) return true;
		PayVo other = (PayVo)obj;
		return new EqualsBuilder()
			.append(getOut_trade_no(),other.getOut_trade_no())
			.isEquals();
	}
	
}

