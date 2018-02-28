declare module cc {
    export var RenderTexture: any;
    export interface Node {
        _sgNode: any;
    }
}

declare let gl: any;

declare interface checkData {
    msg: string;
    type: number;
    apkUrl: string;
    ipaUrl: string;
}

/**
 * 按钮对象
 * 
 * @interface btn_obj
 */
declare interface btn_obj {
    /**
     * 按钮名字
     * 
     * @type {string}
     * @memberof btn_obj
     */
    lbl_name: string;
    /**
     * 按钮响应的回调
     * 
     * @memberof btn_obj
     */
    callback: (event?: cc.Event.EventTouch) => void;
}

/**
 * 音频配置对象
 * 
 * @interface audioSetting
 */
declare interface audioSetting {
    /**
     * 是否开启背景音乐(默认开启)
     */
    isMusic: boolean;

    /**
     * 是否开启音效(默认开启)
     */
    isEffect: boolean;
}

/**
 * 快速发言对象
 * 
 * @interface quicklySpeak 
 */
declare interface quicklySpeak {
    /**
     * 消息id
     * 
     * @type {number}
     * @memberof quicklySpeak
     */
    id: number;
    /**
     * 消息内容
     * 
     * @type {string}
     * @memberof quicklySpeak
     */
    msg: string;
}

/**
 *  CardAttrib属性:
 * 
 * @interface CardAttrib
 */
declare interface CardAttrib {
    /**
     * 牌的唯一Id
     * 
     * @type {number}
     * @memberof CardAttrib
     */
    cardId: number;
    /**
     * 花色
     * @type {number}
     * @memberof CardAttrib
     */
    suit: number;
    /**
     * 点数
     * @type {number}
     * @memberof CardAttrib
     */
    point: number;
}
/**
 * SeatVo属性
 * 
 * @interface SeatVo
 */
declare interface SeatVo {
    /**
     * 座位下标
     * @type {number}
     * @memberof SeatVo
     */
    seatIndex: number;
    /**
     * 玩家Id(0=无人)
     * @type {string}
     * @memberof SeatVo
     */
    accountId: string;
    /**
     * 头像
     * @type {string}
     * @memberof SeatVo
     */
    headImg: string;
    /**
     * 呢称
     * @type {string}
     * @memberof SeatVo
     */
    nick: string;
    /**
     * 明星号
     * @type {string}
     * @memberof SeatVo
     */
    starNO: string;
    /**
     * 座位金币
     * @type {number}
     * @memberof SeatVo
     */
    money: number;
    /**
     * 座位手牌
     * @type {number[]}
     * @memberof SeatVo
     */
    handCards: number[];
    /**
     * 表态状态(-1=放弃,0=等待,1=表态)
     * @type {number}
     * @memberof SeatVo
     */
    btState: number;
    /**
     * 下注金额
     * @type {number}
     * @memberof SeatVo
     */
    betMoney: number;
    /**
     * 表态结果(0=弃牌,1=看牌,2=比牌,3=全押,4=跟注,5=加注)
     * @type {number}
     * @memberof SeatVo
     */
    btVal: number;
    /**
     * 看牌标志(0=未看牌,1=已看牌)
     * @type {number}
     * @memberof SeatVo
     */
    looked: number;

    /**
     * 是否参与游戏
     * @type {number} 0 = 未参加游戏 1=参加游戏
     * @memberof SeatVo
     */
    bGamed: number;
}
/**
 * SettlementItemNN属性:
 * @interface SettlementItemNN
 */
declare interface SettlementItemNN {
    /**
     * 玩家账号id
     * 
     * @type {string}
     * @memberof SettlementItemNN
     */
    accountId: string;
    /**
     * 庄家标识(1=庄家)
     * @type {number}
     * @memberof SettlementItemNN
     */
    banker: number;
    /**
     *呢称
     * @type {string}
     * @memberof SettlementItemNN
     */
    nick: string;
    /**
     * 牛点数
     * @type {string}
     * @memberof SettlementItemNN
     */
    nnDesc: string;
    /**
     * 输赢分数
     * @type {number}
     * @memberof SettlementItemNN
     */
    score: number;
}
/**
 * SettlementItemZJH 属性:
 * @interface SettlementItemZJH
 */
declare interface SettlementItemZJH {
    /**
     * 座位
     * @type {number}
     * @memberof SettlementItemZJH
     */
    seatIndex: number;
    /**
     * 输赢分数(正的赢,负的输)
     * @type {number}
     * @memberof SettlementItemZJH
     */
    score: number;
}
/**
 * 游戏数据
 * 
 * @interface GameData
 */
declare interface GameData {
    /**
     * 桌子Id
     * @type {number}
     * @memberof GameData
     */
    tableId: number;
    /**
     * 桌子类型(1=牛牛,2=金花)
     * @type {number}
     * @memberof GameData
     */
    gameType: number;
    /**
     * 游戏状态
     * @type {number}
     * @memberof GameData
     */
    gameState: number;
    /**
     * 庄家位置
     * @type {string}
     * @memberof GameData
     */
    bankerId: string;
    /**
     *当前表态座位下标
     * @type {number}
     * @memberof GameData
     */
    btIndex: number;
    /**
     * 服务器时间
     * @type {string}
     * @memberof GameData
     */
    svrTime: string;
    /**
     * 动作到期时间
     * @type {string}
     * @memberof GameData
     */
    actTime: string;
    /**
     * 结算列表(牛牛)
     * @type {SettlementItemNN[]}
     * @memberof GameData
     */
    NNOverItems: SettlementItemNN[];
    /**
     * 结算列表(ZJH)
     * @type {SettlementItemZJH[]}
     * @memberof GameData
     */
    ZJHOverItems: SettlementItemZJH[];
    /**
     * 座位数据
     * @type {SeatVo[]}
     * @memberof GameData
     */
    seats: SeatVo[];
    /**
     * 总压注金额
     * @type {number}
     * @memberof GameData
     */
    totalBetMoney: number;
    /**
     * 底分
     * @type {number}
     * @memberof GameData
     */
    baseScore: number;
    /**
     * 单注押注上限
     * @type {number}
     * @memberof GameData
     */
    onceMax: number;
    /**
     * 轮数
     * @type {number}
     * @memberof GameData
     */
    roundNum: number;
    /**
     * 动作总时间(毫秒)
     * @type {number}
     * @memberof GameData
     */
    actTotalTime: number;
    /**
     * 比牌发起座位
     * @type {number}
     * @memberof GameData
     */
    compareSrcIndex: number;
    /**
     * 比牌目标座位
     * @type {number}
     * @memberof GameData
     */
    compareDstIndex: number;
    /**
     *比牌结果(-1=源小于目标,0=相同,1=源大于目标)
     * @type {number}
     * @memberof GameData
     */
    compareResult: number;
	/**
     * 金花桌子上个下注玩家的座位下标
     * @type {number}
     * @memberof GameData
     */
    prevSeatIndex: number;
    /**
     * 桌子上不看牌需下注金额
     * @type {number}
     * @memberof GameData
     */
    unLookBetMoney: number;
    /**
     * 桌子上看牌需下注金额
     * @type {number}
     * @memberof GameData
     */
    lookBetMoney: number;
}

declare interface ChatData {
    /**
     * 
     * 发言玩家Id
     * @type {string}
     * @memberof ChatData
     */
    accountId: string;
    /**
     * 聊天内容类型
     * 
     * @type {number}
     * @memberof ChatData
     */
    type: number,
    /**
     * 
     * 聊天内容
     * @type {number}
     * @memberof ChatData
     */
    content: number
}

/**
 * 玩家钱包
 * 
 * @interface Wallet
 */
declare interface Wallet {
    /**
     * 房卡数量
     * 
     * @type {number}
     * @memberof UserData
     */
    roomCard: number;
}

/**
 * 大厅红点提示数据
 * 
 * @interface HotTip
 */
declare interface HotTip {
    /**
     * 热点索引KEY(1=邮件)
     * 
     * @type {number} 1=邮件
     * @memberof HotTip
     */
    hotKey: number;
    /**
     * 热点值(0=无,1=有)
     * 
     * @type {number}0=无,1=有
     * @memberof HotTip
     */
    hotVal: number
}

/**
 * 单个公告数据
 * 
 * @interface NoticeParamAttrib
 */
declare interface NoticeParamAttrib {
    /**
     * 公告内容
     * 
     * @type {string}
     * @memberof NoticeParamAttrib
     */
    content: string;
    /**
     * 显示颜色(RGB)
     * 
     * @type {number[]}
     * @memberof NoticeParamAttrib
     */
    color: number[];
    /**
     * 公告类型(0=不变参,1=玩家对像,2=字符串,3=物品)
     * 
     * @type {number}
     * @memberof NoticeParamAttrib
     */
    type: number
}
/**
 * 大厅公告数据
 * 
 * @interface NoticeNotify
 */
declare interface NoticeNotify {
    /**
     * 公告Id
     * 
     * @type {string}
     * @memberof NoticeNotify
     */
    msgId: string;
    /**
     * 公告内容
     * 
     * @type {NoticeParamAttrib}
     * @memberof NoticeNotify
     */
    contents: NoticeParamAttrib[]
}

/**
 * 微信获取用户信息返回的数据
 * 
 * @interface UserInfo
 */
declare interface UserInfo {
    /**
     * 普通用户的标识，对当前开发者帐号唯一
     * 
     * @type {string}
     * @memberof UserInfo
     */
    openid: string;
    /**
     * 普通用户昵称
     * 
     * @type {string}
     * @memberof UserInfo
     */
    nickname: string;
    /**
     * 普通用户性别，1为男性，2为女性
     * 
     * @type {number}
     * @memberof UserInfo
     */
    sex: number;
    /**
     * 普通用户个人资料填写的省份
     * 
     * @type {string}
     * @memberof UserInfo
     */
    province: string;
    /**
     * 普通用户个人资料填写的城市
     * 
     * @type {string}
     * @memberof UserInfo
     */
    city: string;
    /**
     * 国家，如中国为CN
     * 
     * @type {string}
     * @memberof UserInfo
     */
    country: string;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640* 640正方形头像），用户没有头像时该项为空
     * 
     * @type {string}
     * @memberof UserInfo
     */
    headimgurl: string;
    /**
     * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     * 
     * @type {string[]}
     * @memberof UserInfo
     */
    privilege: string[];
    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     * 
     * @type {string}
     * @memberof UserInfo
     */
    unionid: string;
}
/**
 * 登录回调全局事件通知需要传的对象
 * 
 * @interface CB_Login
 */
declare interface CB_Login {
    /**
     * 1标识data为UserInfo数据，0标识data是string为错误信息
     * 
     * @type {number}
     * @memberof CB_Login
     */
    flag: number;
    /**
     * 具体数据
     * 
     * @type {(UserInfo | string)}
     * @memberof CB_Login
     */
    data: UserInfo | string;
}

declare interface UserData {
    /**
     * 游客或微信唯一uuid
     * 
     * @type {string}
     * @memberof UserData
     */
    accountId: string;
    /**
     * 游客或微信唯一uuid
     * 
     * @type {string}
     * @memberof UserData
     */
    uuid: string;
    /**
     * 明星号
     * 
     * @type {string}
     * @memberof UserData
     */
    starNO: string;
    /**
     * 角色头像
     * 
     * @type {string}
     * @memberof UserData
     */
    headImg: string;
    /**
     * 性别(0=保密码,1=男,2=女)
     * 
     * @type {number}
     * @memberof UserData
     */
    sex: number;
    /**
     * 角色呢称
     * 
     * @type {string}
     * @memberof UserData
     */
    nick: string;
    /**
     * 角色房卡
     * 
     * @type {number}
     * @memberof UserData
     */
    roomCard: number;
    /**
     * 玩家的桌子Id
     * 
     * @type {number}
     * @memberof UserData
     */
    tableId: number;

    /**
     * 玩家IP
     * 
     * @type {string}
     * @memberof UserData
     */
    clientIP: string;
    /**
     * 帮定的手机号 
     * @type {string}
     * @memberof UserData
     */
    phone: string;
    /**
     *实名认证标识(1=已认证)
     * @type {number}
     * @memberof UserData
     */
    authenticationFlag: number;
}

/**
 * 用于检测消息超时，以及响应服务器的返回
 * 
 * @interface SendData
 */
declare interface SendData {
    /**
     * 协议号
     * 
     * @type {number}
     * @memberof SendData
     */
    msgId: number;
    /**
     * 时间戳
     * 
     * @type {number}
     * @memberof SendData
     */
    sendTime: number;
    /**
     * 回调方法，flag为1时服务器返回成功，为0时返回失败，为-1时消息超时
     * 
     * @memberof SendData
     */
    callback: (flag: number, content?: any) => void;
}

/**
 * 服务器返回对象
 * 
 * @interface S_Data
 */
declare interface S_Data {
    /**
     * 0是成功，负数表示错误
     * 
     * @type {number}
     * @memberof S_Data
     */
    code: number;
    /**
     * 服务器返回最终消息，错误是为错误信息，成功时为null或json字符串
     * 
     * @type {string}
     * @memberof S_Data
     */
    content?: string;
}

/**
 * 消息结构头对象
 * 
 * @interface MessageHeader
 */
declare interface MessageHeader {
    /**
     * 包头校验信息
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    hcheck: number;
    /**
     * 包体编码方式
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    code: number;
    /**
     * 32位标识
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    flag: number;
    /**
     * 识别标识
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    id: number;
    /**
     * 时间戳
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    timestamp: number;
    /**
     * 包体校验信息
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    bcheck: number;
    /**
     * 包体长度
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    blen: number;
    /**
     * 附件长度
     * 
     * @type {number}
     * @memberof MessageHeader
     */
    alen: number;
}

declare interface WalletGiveInner {
	/**
     * 赠送时间
     * @type {string}
     * @memberof WalletGiveInner
     */
    giveTime: string;

	/**
     *内容
     * @type {string}
     * @memberof WalletGiveInner
     */
    content: string;
}
/**
 * RoomCfgItem属性
 * @interface RoomCfgItem
 */
declare interface RoomCfgItem {
    /**
     * 等级场
     * @type {number}
     * @memberof RoomCfgItem
     */
    cfgId: number;
    /**
     * 底分
     * @type {number}
     * @memberof RoomCfgItem
     */
    baseScore: number;
    /**
     * 单注最高封顶
     * @type {number}
     * @memberof RoomCfgItem
     */
    onceMax: number;
    /**
     * 进入限制
     * @type {number}
     * @memberof RoomCfgItem
     */
    joinLimit: number;
}
declare interface TableCfgItem {
    /**
     * 桌子号
     * @type {number}
     * @memberof TableCfgItem
     */
    tableId: number;
    /**
     * 桌子人数
     * @type {number}
     * @memberof TableCfgItem
     */
    playerNum: number;
}