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
     * 帐号Id登录
     * accountId [String] 数据库唯 一Id
     */
    ACCOUNT_LOGIN_ACCOUNTID = 108,

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
     * 删除邮件
     * mailIds [[I] 邮件Id列表
     */
    MAIL_MAILDELETE = 203,

    /**
     * 发送邮件
     * title [String] 邮件标题
     * content [String] 邮件内容
     * starNO [String] 邮件接收人
     * attachments [GameObject;] 邮件附件列表
     */
    MAIL_MAILSEND = 204,

    /**
     * 领取邮件附件
     * mailId [int] 邮件唯一Id
     * param [String] 领取参数
     */
    MAIL_GET_ATTACH = 205,

    /**
     * 获取帮会列表
     * type [int] 类型(1=已加入,2=推荐,3=所有公开)
     */
    CORPS_GET_CORPS_LIST = 401,

    /**
     * 创建帮会
     * corpsName [String] 帮会名称(具有唯一性)
     * wxNO [String] 馆主微信号
     */
    CORPS_CREATE = 402,

    /**
     * 搜索帮会指定帮会
     * query [String] 搜索KEY
     */
    CORPS_CORPS_SEARCH = 403,

    /**
     * 申请加入帮会
     * corpsId [String] 帮会Id
     * reson [String] 加入理由
     */
    CORPS_QUEST_JOIN = 404,

    /**
     * 捐赠房卡
     * corpsId [String] 帮会Id
     * cardNum [int] 房卡数量
     */
    CORPS_CORPS_GIVECARD = 405,

    /**
     * 获取帮会所有游戏桌子
     * corpsId [String] 帮会Id
     */
    CORPS_TABLE_LIST = 406,

    /**
     * 获取帮会所有成员信息
     * corpsId [String] 帮会Id
     * type [int] 类型(1=成员列表,2=申请加入列表,3=黑名单)
     */
    CORPS_MEMBER_LIST = 407,

    /**
     * 踢出帮会成员
     * corpsId [String] 帮会Id
     * kickAccountId [String] 踢出的玩家帐号Id
     * type [int] 类型(0=不加入黑名单,1=加入黑名单)
     */
    CORPS_KICK_MEMBER = 408,

    /**
     * 帮会同意或拒绝玩家加入帮会
     * corpsId [String] 帮会Id
     * btAccountId [String] 玩家帐号Id
     * bt [int] 表态(0=拒绝,1=同意)
     * reson [String] 拒绝原因
     */
    CORPS_QUEST_JOIN_BT = 409,

    /**
     * 解锁黑名单玩家
     * corpsId [String] 帮会Id
     * btAccountId [String] 解锁的玩家帐号Id
     */
    CORPS_MEMBER_BLACKLIST_UNLOCK = 410,

    /**
     * 设置房卡使用状态
     * corpsId [String] 帮会Id
     * state [int] 0=关闭,1=打开
     */
    CORPS_SET_ROOMCARD_STATE = 411,

    /**
     * 帮会转让
     * corpsId [String] 帮会Id
     * btStarNO [String] 转让的明星号
     */
    CORPS_ZHUANRANG_PLAYER = 412,

    /**
     * 解散帮会
     * corpsId [String] 帮会Id
     */
    CORPS_DESTORY = 413,

    /**
     * 成员退出帮会
     * corpsId [String] 帮会Id
     */
    CORPS_EXIT = 414,

    /**
     * 修改帮会公告
     * corpsId [String] 帮会Id
     * notice [String] 帮会公告内容
     */
    CORPS_MODFIY_NOTICE = 415,

    /**
     * 修改帮会微信号
     * corpsId [String] 帮会Id
     * wxno [String] 新的微信号
     */
    CORPS_MODFIY_WXNO = 416,

    /**
     * 修改帮会可见状态
     * corpsId [String] 帮会Id
     * hidde [int] 0=可见,1=隐藏
     */
    CORPS_MODFIY_HIDDE = 417,

    /**
     * 获取帮会成员排行榜
     * corpsId [String] 帮会Id
     * type [int] 1=活跃度排行,2=战绩排行,3=房卡捐献排行
     */
    CORPS_MEMBER_RANKINFO = 418,

    /**
     * 获取帮会详细信息
     * corpsId [String] 帮会Id
     */
    CORPS_GET_CORPS_DETAILED = 419,

    /**
     * 离开帮会场景
     * corpsId [String] 帮会Id
     */
    CORPS_LEAVE_SENCE = 420,

    /**
     * 帮主邀请加入帮会
     * corpsId [String] 帮会Id
     * btStarNO [String] 邀请玩家明星号
     */
    CORPS_YAOQING_JOIN = 421,

    /**
     * 获取麻将条目配置
     * corpsId [String] 帮会Id,0=无帮会
     */
    MAJIANG_GET_RULECFG = 501,

    /**
     * 创建桌子
     * corpsId [String] 帮会Id,0=无帮会
     * roomItemId [int] 桌子配置条目Id
     * rules [List] 桌子规则
     * password [String] 桌子密码,0=无密码
     */
    MAJIANG_ROOM_CREATE = 502,

    /**
     * 加入桌子
     * tableId [int] 桌子号
     * password [String] 桌子密码
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
     * cardIds [[I] 要换的牌
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
     * cardId [int] 要出的牌
     */
    MAJIANG_ROOM_OUT_CARD = 508,

    /**
     * 胡杠碰吃表态
     * tableId [int] 桌子号
     * bt [int] 表态类型(0=胡,1=杠,2=碰,3=吃,4=过)
     * cardId [int] 表态的牌
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
     * 躺牌表态
     * tableId [int] 桌子号
     * btcards [[I] 表态的牌
     * hucards [[I] 躺牌后胡的牌
     * outcard [int] 躺牌后要打出的牌
     */
    MAJIANG_ROOM_TANG_CARD_BT = 513,

    /**
     * 报叫表态
     * tableId [int] 桌子号
     * btVal [int] 表态的结果(0=不报叫,1=报叫)
     */
    MAJIANG_ROOM_BAOJIAO_BT = 514,

    /**
     * 乐山麻将胡杠碰吃表态
     * tableId [int] 桌子号
     * bt [int] 表态类型(0=胡,1=杠,2=碰,3=吃,4=过)
     * cardId [int] 表态的牌(目标牌,如幺鸡当3条用,表态牌为3条)
     * gangType [int] 杠类型(1=巴杠,2=暗杠或直杠)
     * replace [int] 表态的牌是否是幺鸡
     */
    MAJIANG_ROOM_OTHERBREAK_CARD_LSMJ_BT = 515,

    /**
     * 南充麻将漂牌表态
     * tableId [int] 桌子号
     * btVal [int] 表态的结果(漂几个)
     */
    MAJIANG_ROOM_NCMJ_PIAOPAI_BT = 516,

    /**
     * 获取桌子密码标识
     * tableId [int] 桌子号
     */
    MAJIANG_ROOM_PASSWORD_STATE = 517,

    /**
     * 设置托管
     * tableId [int] 桌子号
     * bt [int] 0=取消托管,1=设置托管
     */
    MAJIANG_ROOM_TRUSTEESHIP = 518,

    /**
     * 发送聊天信息
     * tableId [int] 桌子Id
     * type [int] 聊天内容类型
     * content [int] 聊天内容Id
     */
    CHAT_SEND = 601,

    /**
     * 查询战绩
     * type [int] 查询类型(1=个人,2=麻将馆)
     * query [String] 查询参数(个人=玩家帐号Id,麻将馆=麻将馆Id)
     */
    REPLAY_QUERY_RECORD = 701,

    /**
     * 查询详细战绩数据
     * recordId [String] 记录Id
     */
    REPLAY_QUERY_DETAILED_RECORD = 702,

    /**
     * 馆主删除战绩数据
     * recordId [String] 记录Id
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
     * 获取所有商品列表
     */
    MALL_ITEMLIST = 901,

    /**
     * 购买指定商品
     * itemId [int] 商品Id
     */
    MALL_ITEM_BUY = 902,

    /**
     * 商品购买成功
     * order_no [String] 订单号
     * pay_price [String] 付款金额
     * pay_time [String] 付款时间
     * num [int] 房卡数量
     */
    MALL_ITEM_BUY_OK = 903,

    /**
     * 获取红包福利列表
     */
    TASK_GET_WELFARE_LIST = 1001,

    /**
     * 获取每日任务列表
     */
    TASK_GET_DAYTASK_LIST = 1002,

    /**
     * 获取红包福利奖励
     * taskId [int] 任务Id
     */
    TASK_GET_WELFARE_REWARE = 1003,

    /**
     * 获取任务奖励
     * taskId [int] 任务Id
     */
    TASK_GET_TASK_REWARE = 1004,

    /**
     * 分享一次战绩
     */
    TASK_SHAR_FIGHT_SCORE = 1005,

    /**
     * 获取银币抽奖界面信息
     */
    TASK_SILVER_REWARE_INFO = 1006,

    /**
     * 银币抽奖刷新物品列表
     */
    TASK_SILVER_REWARE_REFSH = 1007,

    /**
     * 银币抽奖抽取奖品
     */
    TASK_SILVER_REWARE_GET = 1008,

    /**
     * 获取银币所有奖励物品列表
     */
    TASK_SILVER_REWARE_ALL_ITEM = 1009,

    /**
     * 获取金币兑换界面信息
     */
    TASK_GOLD_REWARE_INFO = 1010,

    /**
     * 获取金币物品详情
     * itemId [int] 物品Id
     */
    TASK_GOLD_REWARE_ITEM_INFO = 1011,

    /**
     * 金币兑换物品
     * itemId [int] 要兑换的物品Id
     * name [String] 物品接收者名称
     * phone [String] 物品接收者联系电话
     * addr [String] 物品发送地址
     */
    TASK_GOLD_REWARE_EXCHANGE = 1012,

    /**
     * 推送消息(红点提示数据)
     */
    ACCOUNT_NOTIFY_HOT_PROMPT = 10001,

    /**
     * 推送消息(玩家钱包数据)
     */
    ACCOUNT_NOTIFY_WALLET = 10002,

    /**
     * 推送消息(帐号被踢下线通知)
     */
    ACCOUNT_NOTIFY_KICK_ACCOUNT = 10003,

    /**
     * 公告推送消息
     */
    MESSAGE_NOTICE_NOTIFY = 30001,

    /**
     * 推送消息(麻将馆解散通知)
     */
    CORPS_DESTORY_NOTIFY = 40001,

    /**
     * 推送消息(麻将馆成员添加通知)
     */
    CORPS_ADD_MEMBER_NOTIFY = 40002,

    /**
     * 推送消息(麻将馆成员移除通知)
     */
    CORPS_KICK_KMEMBER_NOTIFY = 40003,

    /**
     * 推送消息(帮会房间创建)
     */
    CORPS_TABLE_CREATE_NOTIFY = 40004,

    /**
     * 推送消息(帮会房间数据更改)
     */
    CORPS_TABLE_DATA_CHANAGE_NOTIFY = 40005,

    /**
     * 推送消息(帮会转让通知)
     */
    CORPS_ZHANRANG_CORPS_NOTIFY = 40006,

    /**
     * 推送消息(帮会微信公告修改通知)
     */
    CORPS_NOTICE_MODFIY_NOTIFY = 40007,

    /**
     * 推送消息(帮会钱包变化通知)
     */
    CORPS_WALLET_MODFIY_NOTIFY = 40008,

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
     * 推送消息(座位报叫表态通知)
     */
    MAJIANG_ROOM_BAOJIAO_BT_NOTIFY = 50009,

    /**
     * 推送消息(南充麻将漂牌数据通知)
     */
    MAJIANG_ROOM_NCMJ_PIAOPAI_BT_NOTIFY = 50010,

    /**
     * 推送消息(聊天信息通知)
     */
    CHAT_SEND_NOTIFY = 60001
}