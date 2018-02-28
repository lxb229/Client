/**
 * 通信协议枚举
 *
 * @export
 * @enum {number}
 */
export enum Protocol {

    /**
     * 心跳消息
     */
    ACCOUNT_HEART = 101,

    /**
     * 游客登录
     * uuid [String] 微信或游客唯一uuid
     */
    ACCOUNT_LOGIN_TOURIST = 102,

    /**
     * 微信登录
     * uuid [String] 微信或游客唯一uuid
     * headImg [String] 头像
     * nick [String] 呢称
     * sex [int] 性别
     */
    ACCOUNT_LOGIN_WX = 103,

    /**
     * 登出
     * accountId [String] 帐号Id
     */
    ACCOUNT_LOGIN_OUT = 104,

    /**
     * 通过帐号获取角色信息
     * accountId [String] 帐号Id
     */
    ACCOUNT_ROLE_ACCOUNTID = 105,

    /**
     * 通过明星号获取角色信息
     * starNO [String] 明星号
     */
    ACCOUNT_ROLE_STARNO = 106,

    /**
     * 客户端上报与服务器ping值
     * ping [int] ping数值
     */
    ACCOUNT_PING = 107,

    /**
     * 手机登录
     * phone [String] 手机号
     * code [String] 验证码
     */
    ACCOUNT_LOGIN_PHONE = 108,

    /**
     * 获取手机短信验证码
     * phone [String] 手机号码
     */
    ACCOUNT_GET_SMS_CODE = 109,

    /**
     * 手机号注册
     * phone [String] 手机号
     * code [String] 密码
     */
    ACCOUNT_REGISTER = 110,

    /**
     * 客户信息
     */
    ACCOUNT_CUSTOMER_SERVICE = 111,

    /**
     * 获取所有邮件列表
     */
    MAIL_MAILLIST = 201,

    /**
     * 查看邮件
     * mailId [int] 邮件唯一Id
     */
    MAIL_MAILVIEW = 202,

    /**
     * 获取游戏场列表
     * gameType [int] 游戏类型,1=牛牛,2=金花
     */
    ZJH_GET_ROOM_LIST = 501,

    /**
     * 获取桌子列表
     * cfgId [int] 游戏场Id
     */
    ZJH_GET_TABLE_LIST = 502,

    /**
     * 快速加入游戏桌子
     * cfgId [int] 游戏场Id
     */
    ZJH_QUICK_JOIN = 503,

    /**
     * 输入桌子号加入桌子
     * tableId [int] 桌子Id
     * type [int] 1=私人桌子
     */
    ZJH_JION_TABLEID = 504,

    /**
     * 创建桌子
     * cfgId [int] 游戏场Id
     */
    ZJH_TABLE_CREATE = 505,

    /**
     * 强制离开桌子
     * tableId [int] 桌子Id
     */
    ZJH_TABLE_LEAVE = 506,

    /**
     * 座位表态
     * tableId [int] 桌子Id
     * seatIndex [int] 座位下标(0-N)
     * bt [int] 表态状态(0=放弃,1=看牌,2=比牌,3=全押,4=跟注,5=加注)
     * btVal [int] 表态结果值
     */
    ZJH_TABLE_BET = 507,

    /**
     * 牛牛游戏叫庄家
     * tableId [int] 桌子Id
     * bt [int] 表态结果(0=不叫,1=叫庄家)
     */
    ZJH_TABLE_CALL_BANKER = 508,

    /**
     * 游戏点准备开始游戏
     * tableId [int] 桌子Id
     */
    ZJH_TABLE_NN_GAME_READY = 509,

    /**
     * 赠送房卡
     * givePlayer [String] 赠送玩家StarNO
     * giveNum [int] 赠送数量
     */
    WALLET_ROOMCARD_GIVE = 801,

    /**
     * 获取房卡赠送记录
     */
    WALLET_ROOMCARD_RECORD = 802,

    /**
     * 获取兑换比例
     */
    ORDER_GET_EXCHANAGE_PERCENT = 901,

    /**
     * 充值金币
     * rmb [int] 人民币数量
     * payType [int] 支付类型(1=WX,2=支付宝)
     */
    ORDER_CHARGE_RMB2GOLDMONEY = 902,

    /**
     * 金币提现人民币
     * goldMoney [int] 金币数量
     */
    ORDER_CHARGE_GOLDMONEY2RMB = 903,

    /**
     * 提现订单查询
     */
    ORDER_CHARGE_GOLDMONEY2RMB_QUERY = 904,

    /**
     * 推送消息(红点提示数据)
     */
    ACCOUNT_NOTIFY_HOT_PROMPT = 10001,

    /**
     * 推送消息(玩家钱包数据)
     */
    ACCOUNT_NOTIFY_WALLET = 10002,

    /**
     * 公告推送消息
     */
    MESSAGE_NOTICE_NOTIFY = 30001,

    /**
     * 推送消息(游戏状态变化)
     */
    ZJH_GAMESTATE_CHANAGE_NOTIFY = 50001,

    /**
     * 推送消息(座位数据变化)
     */
    ZJH_SEAT_CHANAGE_NOTIFY = 50002,

    /**
     * 推送消息(下注数据变化)
     */
    ZJH_TABLE_BET_NOTIFY = 50003,

    /**
     * 推送消息(玩家被踢出座位)
     */
    ZJH_TABLE_KICK_PLAYER_NOTIFY = 50004,

    /**
     * 推送消息(玩家看牌数据)
     */
    ZJH_TABLE_LOOKCARD_NOTIFY = 50005,

    /**
     * 推送消息(牛牛叫庄数据)
     */
    ZJH_TABLE_NN_CALLBANKER_NOTIFY = 50006,

    /**
     * 推送消息(牛牛游戏准备数据)
     */
    ZJH_TABLE_NN_GAMEREADY_NOTIFY = 50007,

    /**
     * 推送消息(充值成功)
     */
    ORDER_RMB2GOLDMONEY_NOTIFY = 90001
}