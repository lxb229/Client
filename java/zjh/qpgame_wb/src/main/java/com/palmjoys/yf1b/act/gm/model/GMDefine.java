package com.palmjoys.yf1b.act.gm.model;


public class GMDefine {
	private static final int GM_ERR_BASE = 0;
	
	//GM命令定义
	//GM程序后台管理命令
	public static final String GM_CMD_GMMANAGER = "/gm/gmManager";
	//GM帮会命令
	public static final String GM_CMD_CHARGE = "/gm/charge";
		
	
	//GM操作错误定义
	//成功
	public static final int GM_ERR_SUCESS = 0;
	//服务器异常
	public static final int GM_ERR_SVR_EXCEPTION = GM_ERR_BASE-1;
	//不能识别的GM指令
	public static final int GM_ERR_GM_CMD = GM_ERR_BASE-2;
	//GM指令参数错误
	public static final int GM_ERR_GM_PARAM = GM_ERR_BASE-3;
	
	
	public static String Err2Msg(int err){
		String sErr = "";
		switch(err){
		case GMDefine.GM_ERR_SUCESS:
			break;
		case GMDefine.GM_ERR_SVR_EXCEPTION:
			sErr = "服务器未知异常错误";
			break;
		case GMDefine.GM_ERR_GM_CMD:
			sErr = "不能识别的GM指令";
			break;
		case GMDefine.GM_ERR_GM_PARAM:
			sErr = "GM指令参数错误";
			break;
		default:
			sErr = "未知错误";
			break;
		}		
		return sErr;
	}
}
