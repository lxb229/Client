package com.wangzhixuan.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.wangzhixuan.commons.utils.StringUtils;

import java.util.Date;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 玩家
 * </p>
 *
 * @author zhixuan.wang
 * @since 2018-03-23
 */
public class Player extends Model<Player> {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 玩家明星号
     */
	@TableField("start_no")
	private String startNo;
    /**
     * 头像
     */
	@TableField("head_img")
	private String headImg;
    /**
     * 性别
     */
	private Integer sex;
    /**
     * 玩家昵称
     */
	private String nick;
    /**
     * 玩家账号状态(0=正常 1=冻结)
     */
	private Integer status;
    /**
     * 注册ip
     */
	@TableField("create_ip")
	private String createIp;
    /**
     * 账号类型(0=普通玩家 1=群主)
     */
	private Integer type;
    /**
     * 电话号码
     */
	private String phone;
    /**
     * 真实姓名
     */
	@TableField("real_name")
	private String realName;
    /**
     * 身份证号码
     */
	@TableField("card_no")
	private String cardNo;
    /**
     * 注册时间
     */
	@TableField("create_time")
	private Date createTime;
	
	/**
	 * 验证
	 */
	public ValidataBean validateModel() {
		ValidataBean vb = new ValidataBean();
		if(id == null){
			 if(StringUtils.isBlank(startNo)){
				 vb.setMsg("明星号为空");
		         return vb;
			 }
			 if(status == null) {
				 vb.setMsg("账号状态为空");
		         return vb;
			 }
			 if(type == null) {
				 vb.setMsg("账号类型为空");
		         return vb;
			 }
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

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
