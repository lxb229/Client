package com.wangzhixuan.commons.base;

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
	
	
	public static Integer TASK_PLAYER = 1;/**任务-玩家数据*/
	public static Integer TASK_ROOM = 2;/**任务-房间数据*/
	public static Integer TASK_LOGINLOG = 3;/**任务-登录日志数据*/
	public static Integer TASK_JOINROOM = 4;/**任务-进入房间数据*/
	public static Integer TASK_JOINPARTY = 5;/**任务-入局数据*/
	public static Integer TASK_JETTONLOG = 6;/**任务-申请筹码数据*/
	public static Integer TASK_BETLOG = 7;/**任务-下注记录数据*/
	public static Integer TASK_INSURANCELOG = 8;/**任务-保险记录数据*/
	public static Integer TASK_INSURANCEPROFIT = 9;/**任务-保险盈亏数据*/
	public static Integer TASK_PLAYERPROFIT = 10;/**任务-玩家盈亏数据*/
	public static Integer TASK_ROOMDISAPPEAR = 11;/**任务-房间解散数据*/
	
}
