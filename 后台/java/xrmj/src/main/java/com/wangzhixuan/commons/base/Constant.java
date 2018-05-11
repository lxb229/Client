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
	
	
	/**任务-玩家数据*/
	public static Integer TASK_PLAYER = 1;
	/**任务-订单数据*/
	public static Integer TASK_ORDER = 2;
	/**任务-游戏数据*/
	public static Integer TASK_GAMEPLAYER = 3;
	/**任务-玩家祝福值数据*/
	public static Integer TASK_PLAYERWISH = 4;
	/**任务-麻将馆数据*/
	public static Integer TASK_MAHJONG = 5;
	/**任务-麻将馆成员数据*/
	public static Integer TASK_MAHJONGPLAYER = 6;
	/**任务-麻将馆房卡数据*/
	public static Integer TASK_MAHJONGCARD = 7;
	/**任务-麻将馆战斗数据*/
	public static Integer TASK_MAHJONGCOMBAT = 8;
	/**任务-玩家消耗幸运值数据*/
	public static Integer TASK_USERLUCK = 9;
	
	/**货币-房卡*/
	public static Integer MONEY_ROOMCARD = 1;
	/**货币-金币*/
	public static Integer MONEY_GOLD = 2;
	/**货币-银币*/
	public static Integer MONEY_SILVER = 3;
	
	/**一轮红包领取最大牌局数*/
	public static Integer REDPACK_NUMBER = 35;
	/**一轮红包领取最大个数*/
	public static Integer REDPACK_CEILING = 6;
	
//	public static Integer TOP_UP_TASK_SCORE = 20;/**满足当日充值任务房卡数量增加的祝福值数量*/
//	public static Integer ROOMCARD_NUMBER_SCORE = 20;/**满足账号拥有多少房卡增加的祝福值数量*/
//	public static Integer CREATEROOM_SCORE = 20;/**达到指定创建房间数量增加的祝福值数量*/
//	public static Integer WINNER_SCORE = 20;/**达到大赢家条件增加的祝福值数量*/
//	public static Integer ONESELF_SCORE = 20;/**达到自摸条件增加的祝福值数量*/
	
	/**祝福值等级-祝福值小于20*/
	public static Integer WISHLV_1 = 1;
	/**祝福值等级-祝福值大于20,小于40*/
	public static Integer WISHLV_2 = 2;
	/**祝福值等级-祝福值大于40,小于60*/
	public static Integer WISHLV_3 = 3;
	/**祝福值等级-祝福值大于60,小于80*/
	public static Integer WISHLV_4 = 4;
	/**祝福值等级-祝福值大于80,小于100*/
	public static Integer WISHLV_5 = 5;
	/**祝福值等级-祝福值等于100*/
	public static Integer WISHLV_6 = 6;
	
//	public static Integer RECEIVE_1=2;/**一号红包的抽奖次数*/
//	public static Integer RECEIVE_2=3;/**二号红包的抽奖次数*/
//	public static Integer RECEIVE_3=4;/**三号红包的抽奖次数*/
//	public static Integer RECEIVE_4=5;/**四号红包的抽奖次数*/
//	public static Integer RECEIVE_5=6;/**五号红包的抽奖次数*/
//	public static Integer RECEIVE_6=15;/**六号红包的抽奖次数*/
	
	/**生成订单编号*/
	public static Integer RANDOM_ORDER = 1;
	/**生成商品编号*/
	public static Integer RANDOM_COMMODITY = 2;
	/**生成兑换码编号*/
	public static Integer RANDOM_REDEEM_CODE = 3;
	/**生成入库单编号*/
	public static Integer RANDOM_WAREHOUSE_IN = 4;
	/**生成出库单编号*/
	public static Integer RANDOM_WAREHOUSE_OUT = 5;
	
	/**银币抽奖-每次抽奖银币消耗*/
	public static Integer DRAWCOST = 100;
	/**银币抽奖-每次刷新银币消耗*/
	public static Integer REFSHCOST = 10;
	
	/**银币消耗-刷新抽奖奖品*/
	public static Integer SILVER_REFSHCOST = 1; 
	/**银币消耗-抽奖*/
	public static Integer SILVER_DRAWCOST = 2; 
	
}
