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
     * 帐号注册
     * account [String] 帐号名称
     * password [String] 密码
     * headImg [String] 头像
     * nick [String] 呢称
     * sex [int] 性别
     */
    ACCOUNT_REGISTER = 102,

    /**
     * 帐号密码方式登录
     * account [String] 帐号名称
     * password [String] 密码
     */
    ACCOUNT_LOGIN_PASSWORD = 103,

    /**
     * 游客登录
     * uuid [String] 微信或游客唯一uuid
     */
    ACCOUNT_LOGIN_TOURIST = 104,

    /**
     * 微信登录
     * uuid [String] 微信或游客唯一uuid
     * headImg [String] 头像
     * nick [String] 呢称
     * sex [int] 性别
     */
    ACCOUNT_LOGIN_WX = 105,

    /**
     * 帐号Id登录
     * accountId [String] 数据库唯 一Id
     */
    ACCOUNT_LOGIN_ACCOUNTID = 106,

    /**
     * 登出
     * accountId [String] 帐号Id
     */
    ACCOUNT_LOGIN_OUT = 107,

    /**
     * 通过帐号获取角色信息
     * accountId [String] 帐号Id
     */
    ACCOUNT_ROLE_ACCOUNTID = 108,

    /**
     * 通过明星号获取角色信息
     * starNO [String] 明星号
     */
    ACCOUNT_ROLE_STARNO = 109,

    /**
     * 客户端上报与服务器ping值
     * ping [int] ping数值
     */
    ACCOUNT_PING = 110,

    /**
     * 帐号实名认证
     * name [String] 姓名
     * cardId [String] 身份证
     */
    ACCOUNT_AUTHENTICATION = 111,

    /**
     * 手机绑定
     * phone [String] 手机号
     * vaildCode [String] 验证码
     */
    ACCOUNT_PHONE_BIND = 112,

    /**
     * 获取手机短信效验码
     * phone [String] 手机号
     */
    ACCOUNT_PHONE_GET_SMSCODE = 113,

    /**
     * 赠送房卡
     * givePlayer [String] 赠送玩家StarNO
     * giveNum [int] 赠送数量
     */
    WALLET_ROOMCARD_GIVE = 201,

    /**
     * 获取房卡赠送记录
     */
    WALLET_ROOMCARD_RECORD = 202,

    /**
     * 获取所有邮件列表
     */
    MAIL_GET_MAIL_LIST = 301,

    /**
     * 查看邮件
     * mailId [int] 邮件唯一Id
     */
    MAIL_VIEW_MAIL_ITEM = 302,

    /**
     * 发送聊天信息
     * tableId [int] 桌子Id
     * type [int] 聊天内容类型
     * content [int] 聊天内容Id
     */
    CHAT_SEND = 501,

    /**
     * 获取活动信息列表
     */
    ACTIVITY_GET_ACTIVITY_LIST = 601,

    /**
     * 获取红点数据
     * accountId [String] 帐号Id
     */
    HOTPROMPT_GET_HOTDATA = 701,

    /**
     * 获取商城信息列表
     */
    MALL_GET_MALL_LIST = 801,

    /**
     * 购买商城物品
     * itemId [int] 物品配置Id
     */
    MALL_BUY_ITEM = 802,

    /**
     * 充值指定数量金币
     * rmb [int] 人民币数量
     * goldmoney [int] 金币数量
     */
    MALL_CHARGE_GOLDMONEY = 803,

    /**
     * 充值指定数量钻石
     * rmb [int] 人民币数量
     * diamond [int] 钻石数量
     */
    MALL_CHARGE_DIAMOND = 804,

    /**
     * 查询战绩
     * type [int] 查询类型(1=个人,2=俱乐部)
     * query [String] 查询参数(个人=玩家帐号Id,俱乐部=俱乐部Id)
     */
    REPLAY_QUERY_RECORD = 1001,

    /**
     * 查询详细战绩数据
     * tableId [int] 桌子Id
     */
    REPLAY_QUERY_DETAILED_RECORD = 1002,

    /**
     * 获取桌子创建配置
     */
    DZPKER_TABLE_GET_CFG = 1201,

    /**
     * 创建桌子
     * tableName [String] 桌子名称
     * small [int] 小盲注
     * big [int] 大盲注
     * minJoin [int] 最小座下
     * vaildTime [int] 游戏时长(单位:分钟)
     * insurance [int] 是否开启保险(0=未开,1=开启)
     * straddle [int] 是否开启闭眼盲注(0=未开,1=开启)
     * buyMax [int] 单次积分购买上限
     */
    DZPKER_TABLE_CREATE = 1202,

    /**
     * 房主点击运行桌子
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_START_RUN = 1203,

    /**
     * 加入指定桌子
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_JOIN = 1204,

    /**
     * 退出指定桌子
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_LEAVE = 1205,

    /**
     * 座下指定位置
     * tableId [int] 桌子Id
     * seatIndex [int] 座位下标
     */
    DZPKER_SEAT_DOWN = 1206,

    /**
     * 从座位上站起
     * tableId [int] 桌子Id
     * seatIndex [int] 座位下标
     */
    DZPKER_SEAT_UP = 1207,

    /**
     * 购买积分
     * tableId [int] 桌子Id
     * chipNum [int] 积分数量
     */
    DZPKER_BUY_CHIP = 1208,

    /**
     * 正常游戏表态
     * tableId [int] 桌子Id
     * bt [int] 表态(1=弃牌,2=过牌,3=跟注,4=加注,5=全下)
     * btVal [int] 表态值
     */
    DZPKER_TABLE_BT = 1209,

    /**
     * 房主获取积分购买申请列表
     */
    DZPKER_QUERY_BUYCHIP_LIST = 1210,

    /**
     * 房主处理积分购买申请条目
     * itemId [String] 购买申请Id
     * bt [int] 处理表态(1=同意,2=拒绝)
     */
    DZPKER_TRANS_BUYCHIP_ITEM = 1211,

    /**
     * 闭眼盲注表态
     * tableId [int] 桌子Id
     * seatIndex [int] 座位下标
     */
    DZPKER_STRADDLE_BT = 1212,

    /**
     * 获取参与过游戏的未解散的桌子
     */
    DZPKER_GET_FIGHTED_TABLE_LIST = 1213,

    /**
     * 获取桌子上一局游戏信息
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_PREV_FIGHT = 1214,

    /**
     * 获取桌子所有玩家数据信息
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_PLAYER_LIST = 1215,

    /**
     * 获取桌子个人赢亏数据
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_PLAYER_ONCE_WINSCORE = 1216,

    /**
     * 购买保险
     * tableId [int] 桌子Id
     * bt [int] 0=不买,1=购买
     * buyCards [[I] 购买的牌数据
     * buyMoney [int] 购买积分
     * payMoney [int] 赔付积分
     */
    DZPKER_TABLE_BUY_INSURANCE = 1217,

    /**
     * 获取生涯历史数据
     */
    DZPKER_TABLE_GET_CAREE_INFO = 1218,

    /**
     * 结算后亮牌表态
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_SHOW_CARD_BT = 1219,

    /**
     * 获取指定桌子数据
     * tableId [int] 桌子Id
     */
    DZPKER_TABLE_GET_TABLE_INFO = 1220,

    /**
     * 获取指定桌子所有参与过游戏的玩家输赢明细
     * recordId [String] 桌子记录Id
     */
    DZPKER_TABLE_GET_ALL_WIN_SCORE_INFO = 1221,

    /**
     * 推送消息(玩家被踢下线)
     */
    ACCOUNT_KICK_OFFLINE_NOTIFY = 10001,

    /**
     * 推送消息(钱包数据变化)
     */
    WALLET_WALLET_NOTIFY = 20001,

    /**
     * 推送消息(公告数据)
     */
    MESSAGE_NOTICE_NOTIFY = 40001,

    /**
     * 推送消息(聊天信息通知)
     */
    CHAT_SEND_NOTIFY = 50001,

    /**
     * 推送消息(红点提示数据)
     */
    HOTPROMPT_HOTDATA_NOTIFY = 70001,

    /**
     * 购买或充值成功通知
     */
    MALL_CHARGE_NOTIFY = 80001,

    /**
     * 推送消息(游戏桌子状态变化通知)
     */
    DZPKER_TABLE_STATE_NOTIFY = 120001,

    /**
     * 推送消息(游戏座位数据变化通知)
     */
    DZPKER_SEAT_STATE_NOTIFY = 120002,

    /**
     * 推送消息(游戏桌子表态通知)
     */
    DZPKER_TABLE_BT_NOTIFY = 120003,

    /**
     * 推送消息(游戏桌子总结算数据通知)
     */
    DZPKER_TABLE_SETTLEMENT_NOTIFY = 120004,

    /**
     * 推送消息(保险结算结果数据通知)
     */
    DZPKER_TABLE_SETTLEMENT_INSURANCE_NOTIFY = 120005,

    /**
     * 推送消息(积分购买成功通知)
     */
    DZPKER_TABLE_BUY_CHIP_SUCESS_NOTIFY = 120006,

    /**
     * 推送消息(玩家亮牌通知)
     */
    DZPKER_TABLE_SHOW_CARD_NOTIFY = 120007,
    /**
     * 推送消息(闭眼盲注表态通知)
     */
    DZPKER_TABLE_STRADDL_BT_NOTIFY = 120008,

    /**
     * 推送消息(玩家从座位上被踢起通知)
     */
    DZPKER_TABLE_SEAT_KICK_NOTIFY = 120009
}


/**
 * 游戏状态
 * 
 * @enum {number}
 */
export enum GameState {
    /**
     * 空闲状态
     */
    STATE_TABLE_IDLE = 0,
    /**
     * 准备状态
     */
    STATE_TABLE_READY = 1,
    /**
     * 定庄家下大小盲注
     */
    STATE_TABLE_BETBLIND = 2,
    /**
     * 第一轮发手牌
     */
    STATE_TABLE_OUTCARD_1 = 3,
    /**
     * 第一轮下注表态
     */
    STATE_TABLE_BET_BT_1 = 4,
    /**
     * 第二轮发三张公用牌
     */
    STATE_TABLE_OUTCARD_2 = 5,
    /**
     * 第二轮下注表态
     */
    STATE_TABLE_BET_BT_2 = 6,
    /**
     * 第三轮发一张公用牌
     */
    STATE_TABLE_OUTCARD_3 = 7,
    /**
     * 第三轮下注表态
     */
    STATE_TABLE_BET_BT_3 = 8,
    /**
     * 第四轮发一张公用牌
     */
    STATE_TABLE_OUTCARD_4 = 9,
    /**
     * 第四轮下注表态
     */
    STATE_TABLE_BET_BT_4 = 10,
    /**
     * 保险购买状态
     */
    STATE_TABLE_BUY_INSURANCE = 11,
    /**
     * 单局结算
     */
    STATE_TABLE_OVER_ONCE = 12,
    /**
     * 总结算
     */
    STATE_TABLE_OVER_ALL = 13,
    /**
     * 新一轮积分结算
     */
    STATE_TABLE_NEW_ROUND_BET = 14
}

export enum CardType {
    /**
     * 单牌
     */
    TYPE_CARD_NONE = 0,
    /**
     * 一对
     */
    TYPE_CARD_ONE_DOUBLE = 1,
    /**
     * 两对
     */
    TYPE_CARD_TWO_DOUBLE = 2,
    /**
     * 三条
     */
    TYPE_CARD_SAME_THREE = 3,
    /**
     * 顺子
     */
    TYPE_CARD_SHUN = 4,
    /**
     * 同花
     */
    TYPE_CARD_SAME_SUIT = 5,
    /**
     * 葫芦(3条+1对)
     */
    TYPE_CARD_GOURD = 6,
    /**
     * 四条
     */
    TYPE_CARD_SAME_FOUR = 7,
    /**
     * 同花顺
     */
    TYPE_CARD_SAME_SUIT_SHUN = 8,
    /**
     * 皇家同花顺
     */
    TYPE_CARD_GOLD_SAME_SUIT_SHUN = 9
}
/**
 * 扑克花色
 * 
 * @enum {number}
 */
export enum Suit {
    /**
     * 黑桃
     */
    TYPE_SUIT_BACK = 0,
    /**
     * 红桃
     */
    TYPE_SUIT_RED = 1,
    /**
     * 梅花
     */
    TYPE_SUIT_CLUB = 2,
    /**
     * 方片
     */
    TYPE_SUIT_BLOCK = 3,
}

/**
 * 红点消息类型
 * 
 * @enum {number}
 */
export enum HotKey {
    /**
     * 邮件
     */
    HOT_KEY_MAIL = 1,
    /**
     * 房主订单
     */
    HOT_KEY_ORDER = 2
}