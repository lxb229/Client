package com.guse.platform.common.base;

/**
 * 统一常量
 * @author nbin
 * @date 2017年7月17日 上午9:35:40 
 * @version V1.0
 */
public class Constant {
	
	
	public final static String 	AES_KEY = "guse123";
	
    public final static String  AES_INIT_PASS      = "lztb123";
	
    /**
     * session 标示
     */
    public final static String 	USER_TOKEN_KEY             = "ab_user_token";
    /**
     * 用户基本信息
     */
    public final static String 	USER_INFO_KEY              = "ab_user_info";
    
    /**
     * 
     */
    public final static String  SESSION_LOGIN_USER      = "loginUser";
    
    
    
    /** 启用状态*/
    public final static int DISABLE = 1;
    
    /** 禁用状态*/
    public final static int ENABLE = 0;
    
	public static String CODE_SUCCESS = "00000";
	public static String CODE_ERROR = "11111";
	public static String MSG_ERROR = "操作失败！";
	public static String MSG_SUCCESS = "操作成功！";
	
	
}
