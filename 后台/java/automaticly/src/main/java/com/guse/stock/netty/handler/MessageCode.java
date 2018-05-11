package com.guse.stock.netty.handler;

/** 
* @ClassName: MessageCode 
* @Description: 服务器回应消息 状态
* @author Fily GUSE
* @date 2017年9月1日 下午5:43:08 
*  
*/
public enum MessageCode {
	
		SUCCESS(0, "成功"),							NOTLOGIN(-1, "未登录"), 						UNKNOWNAGREEMENT(400,"未知协议"),
		INVALIDDATA(401,"非法数据"),					ENCRYPTERROR(402,"数据加密错误"),				DECODEERROR(403,"数据解密失败"),
		PARAMERROR(404, "请求参数异常"),
		SERVERERROR(500,"服务器异常"),					LOGINSERVERERROR(510, "登录服务器异常"),
		
		// 入库
		NULLCERTIFICATE(4101,"凭证为空"),				REPETITIONTRANCODE(4102,"凭证交易码重复"),		FORGECERTIFICATE(4103,"伪造凭证"),
		CERTIFICATEDECODE(4104,"解密失败"),			INVALIDCERTIFICATEDE(4105,"凭证无效"),
		
		
		NULLDATA(4001, "空数据"), 					DECODEDEFEAT(4002, "数据解密失败"),				NULLVERSION(4003, "版本号为空"),
		NULLNAMEORPWD(4004, "用户名或者密码为空"),		ERRORPASSWORD(4005, "密码错误"),				NOTUSER(4006, "用户不存在"),
		FORBIDDENUSER(4007,"用户被禁用"),				DELETEUSER(4008,"用户被删除"),					FORBIDDENOUTDISPOSE(4009,"禁止出库"),
		NOTONLINEUSER(4010,"用户不在线"),				NULLEQUIPMENT(4011,"设备号为空"),				SAVEERROREQUIPMENT(4012,"设备号保存失败"),
		UPDATEERROREQUIPMENT(4013,"设备号更新失败"),		AUTHORISEERROREQUIPMENT(4014,"设备号授权错误"),	NOTONLINEEQUIPMENT(4015,"设备不在线"),
		NOBUYPLUGIN(4016,"主账号未购买插件"),				PASSDATEPLUGIN(4017,"主账号插件过期"),			NULLGAMEID(4018,"游戏ID为空"),	
		SAVEERROGAMEID(4019,"保存游戏ID失败"),			NOGAMEID(4020,"游戏id不存在"),					NULLPERID(4021,"面值id为空"),
		SAVEERRORPERID(4022,"面值id保存失败"),			NOPERID(4023,"面值id不存在"),					
		SAVEERRORCERTIFICATE(4025,"凭证保存失败"),		NOCERTIFICATE(4026,"凭证不存在"),				REPETITIONCERTIFICATE(4027,"凭证重复"),
		SAVEERRORSTOCK(4028,"库存保存失败"),			NOSTOCK(4029,"库存不存在"),						SAVEERRORSTOCKLOG(4030,"入库记录保存失败"),
		SAVEERRORSTOCKUSELOG(4031,"出库记录保存失败"),	NULLORDERID(4032,"订单为空"),					NOORDERID(4033,"订单不存在"),
		NULLSTATUS(4034,"状态为空"),					ZEROSTATUS(4035,"状态为0"),					DBEQULSTATUS(4036,"状态与数据库状态一致"),	
		GETERRORSTOCKBYORDER(4037,"订单获取库存失败"),	UPDATEERRORSTOCK(4038,"更新库存失败"),			UPDATEERRORSTOCKUSELOG(4039,"更新出库记录失败"),
		OVERSTEPTHREE(4040,"回滚超3次"),				UNKNOWNSTATUS(4041,"未知状态"),				LOGOUTERROR(4042,"注销失败"),
		NULLUSER(4043,"用户为空"),						NULLSTOCK(4044,"库存id为空"),					NULLSTOCKCOUNTS(4045,"库存统计为空"),
		UPDATEERRORSTOCKCOUNTS(4046,"库存统计保存失败")
		
		;
	
	
	// 回应编号
	private int code;
	// 编号说明
	private String des;
	
	MessageCode(int code, String des) {
		this.code = code;
		this.des = des;
	}
	
	public static MessageCode getMessageCodeByCode(int code){
	    for(MessageCode msg : MessageCode.values()){
	      if(code == msg.getCode()){
	        return msg;
	      }
	    }
	    return MessageCode.SERVERERROR;
	  }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
