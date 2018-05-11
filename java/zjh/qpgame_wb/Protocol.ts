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
     * 获取所有邮件列表
     */
    MAIL_MAILLIST = 201,

    /**
     * 查看邮件
     * mailId [int] 邮件唯一Id
     */
    MAIL_MAILVIEW = 202,

    /**
     * 获取我的帮会列表
     */
    CORPS_GET_CORPS_LIST = 401,

    /**
     * 创建帮会
     * corpsName [String] 帮会名称(具有唯一性)
     */
    CORPS_CREATE = 402,

    /**
     * 获取帮会所有游戏桌子
     * corpsId [String] 帮会Id
     */
    CORPS_TABLE_LIST = 403,

    /**
     * 获取帮会所有成员信息
     * corpsId [String] 帮会Id
     */
    CORPS_MEMBER_LIST = 404,

    /**
     * 设置房卡使用状态
     * corpsId [String] 帮会Id
     * state [int] 0=关闭,1=打开
     */
    CORPS_SET_ROOMCARD_STATE = 405,

    /**
     * 解散帮会
     * corpsId [String] 帮会Id
     */
    CORPS_DESTORY = 406,

    /**
     * 成员退出帮会
     * corpsId [String] 帮会Id
     */
    CORPS_EXIT = 407,

    /**
     * 添加帮会成员
     * corpsId [String] 帮会Id
     * starNO [String] 添加的玩家明星号
     */
    CORPS_ADD_MEMBER = 408,

    /**
     * 踢出帮会成员
     * corpsId [String] 帮会Id
     * starNO [String] 踢出的玩家明星号
     */
    CORPS_KICK_MEMBER = 409,

    /**
     * 申请加入帮会
     * corpsId [String] 帮会Id
     */
    CORPS_QUEST_JOIN = 410,

    /**
     * 帮会同意或拒绝玩家加入帮会
     * corpsId [String] 帮会Id
     * starNO [String] 玩家明星号
     * bt [int] 表态(0=拒绝,1=同意)
     */
    CORPS_QUEST_JOIN_BT = 411,

    /**
     * 获取帮会加入申请列表
     * corpsId [String] 帮会Id
     */
    CORPS_GET_QUESTJOIN_LIST = 412,

    /**
     * 获取麻将条目配置
     */
    MAJIANG_GET_RULECFG = 501,

    /**
     * 创建桌子
     * corpsId [String] 帮会Id,0=无帮会
     * roomItemId [int] 桌子配置条目Id
     * rules [List] 桌子规则
     */
    MAJIANG_ROOM_CREATE = 502,

    /**
     * 加入桌子
     * tableId [int] 桌子号
     */
    MAJIANG_ROOM_JOIN = 503,

    /**
     * 退出桌子
     * tableId [int] 桌子号
     */
    MAJIANG_ROOM_LEAV = 504,

    /**
     * 获取桌子信息
     * tableId [int] 桌子号
     */
    MAJIANG_ROOM_GET_ROOMINFO = 505,

    /**
     * 换牌表态
     * tableId [int] 桌子号
     * cardIds [[B] 要换的牌
     */
    MAJIANG_ROOM_SWAP_CARD = 506,

    /**
     * 定缺表态
     * tableId [int] 桌子号
     * bt [int] 表态结果(1=万,2=筒,3=条)
     */
    MAJIANG_ROOM_DINQUE = 507,

    /**
     * 出牌表态
     * tableId [int] 桌子号
     * cardId [byte] 要出的牌
     */
    MAJIANG_ROOM_OUT_CARD = 508,

    /**
     * 胡杠碰吃表态
     * tableId [int] 桌子号
     * bt [int] 表态类型(0=胡,1=杠,2=碰,3=吃,4=过)
     * cardId [byte] 表态的牌
     */
    MAJIANG_ROOM_OTHERBREAK_CARD = 509,

    /**
     * 申请解散桌子
     * tableId [int] 桌子号
     */
    MAJIANG_ROOM_QUEST_DELETE = 510,

    /**
     * 解散桌子表态
     * tableId [int] 桌子号
     * bt [int] 表态结果(1=拒绝,2=同意)
     */
    MAJIANG_ROOM_DELETE_BT = 511,

    /**
     * 点击下一局游戏
     */
    MAJIANG_ROOM_NEXT_GAME = 512,

    /**
     * 发送聊天信息
     * tableId [int] 桌子Id
     * type [int] 聊天内容类型
     * content [int] 聊天内容Id
     */
    CHAT_SEND = 601,

    /**
     * 查询战绩
     * type [int] 查询类型(1=个人,2=俱乐部)
     * query [String] 查询参数(个人=玩家帐号Id,俱乐部=俱乐部Id)
     */
    REPLAY_QUERY_RECORD = 701,

    /**
     * 查询详细战绩数据
     * tableId [int] 桌子Id
     */
    REPLAY_QUERY_DETAILED_RECORD = 702,

    /**
     * 群主删除战绩数据
     * tableId [int] 桌子Id
     */
    REPLAY_DELETE_RECORD = 703,

    /**
     * 玩家实名认证
     * name [String] 姓名
     * cardId [String] 身份证
     */
    REPLAY_REALNAME_AUTHENTICATION = 704,

    /**
     * 获取手机短信码
     * phone [String] 手机号
     */
    REPLAY_PHONE_GET_SMSCODE = 705,

    /**
     * 手机帮定
     * phone [String] 手机号
     * vaildCode [String] 验证码
     */
    REPLAY_PHONE_BIND = 706,

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
     * 推送消息(俱乐部解散通知)
     */
    CORPS_DESTORY_NOTIFY = 40001,

    /**
     * 推送消息(俱乐部成员添加通知)
     */
    CORPS_ADD_MEMBER_NOTIFY = 40002,

    /**
     * 推送消息(俱乐部成员移除通知)
     */
    CORPS_KIC_KMEMBER_NOTIFY = 40003,

    /**
     * 推送消息(游戏状态变化)
     */
    MAJIANG_ROOM_GAMESTATE_NOTIFY = 50001,

    /**
     * 推送消息(座位数据变化)
     */
    MAJIANG_ROOM_SEAT_NOTIFY = 50002,

    /**
     * 推送消息(定缺数据通知)
     */
    MAJIANG_ROOM_DINQUE_NOTIFY = 50003,

    /**
     * 推送消息(换牌数据通知)
     */
    MAJIANG_ROOM_SWAPCARD_NOTIFY = 50004,

    /**
     * 推送消息(出牌数据通知)
     */
    MAJIANG_ROOM_OUTCARD_NOTIFY = 50005,

    /**
     * 推送消息(胡杠碰吃过表态通知)
     */
    MAJIANG_ROOM_BREAKCARD_NOTIFY = 50006,

    /**
     * 推送消息(桌子已解散通知)
     */
    MAJIANG_ROOM_DESTORY_NOTIFY = 50007,

    /**
     * 推送消息(桌子解散表态通知)
     */
    MAJIANG_ROOM_DESTORY_BT_NOTIFY = 50008,

    /**
     * 推送消息(聊天信息通知)
     */
    CHAT_SEND_NOTIFY = 60001
}