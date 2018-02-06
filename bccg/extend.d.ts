declare module cc {
    export let RenderTexture: any;
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
    ddUrl: string;
}

/**
 * 微信通过code获取到的数据结构
 * 
 * @interface TokenInfo
 */
declare interface TokenInfo {
    /**
     * 接口调用凭证
     * 
     * @type {string}
     * @memberof TokenInfo
     */
    access_token: string;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     * 
     * @type {number}
     * @memberof TokenInfo
     */
    expires_in: number;
    /**
     * 用户刷新access_token
     * 
     * @type {string}
     * @memberof TokenInfo
     */
    refresh_token: string;
    /**
     * 授权用户唯一标识
     * 
     * @type {string}
     * @memberof TokenInfo
     */
    openid: string;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     * 
     * @type {string}
     * @memberof TokenInfo
     */
    scope: string;
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
/**
 * 角色属性数据
 * 
 * @interface RoleAttribVo
 */
declare interface RoleAttribVo {
    /**
     * 明星号
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    starNO: string;
    /**
     * 角色头像
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    headImg: string;
    /**
     * 性别(0=保密码,1=男,2=女)
     * 
     * @type {number}
     * @memberof RoleAttribVo
     */
    sex: number;
    /**
     * 角色呢称
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    nick: string;
    /**
     * 帐号状态(0=正常,-1=冻结)
     * 
     * @type {number}
     * @memberof RoleAttribVo
     */
    state: number;
    /**
     * 帐号注册时间
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    createTime: string;
    /**
     * 注册IP
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    createIP: string;
    /**
     * 最后在线时间
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    inTime: string;
    /**
     * 最后登录IP
     * 
     * @type {string}
     * @memberof RoleAttribVo
     */
    loginIP: string;
}
/**
 * 钱包属性数据
 * 
 * @interface WalletVo
 */
declare interface WalletVo {
    /**
     * 房卡
     * 
     * @type {string}
     * @memberof WalletVo
     */
    roomCard: string;
    /**
     * 金币
     * 
     * @type {string}
     * @memberof WalletVo
     */
    goldMoney: string;
    /**
     * 钻石
     * 
     * @type {string}
     * @memberof WalletVo
     */
    diamond: string;
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
 * 游戏服数据
 * 
 * @interface GameDataAttrib
 */
declare interface GameDataAttrib {
    /**
     * 客户端与服务器ping值
     * 
     * @type {number}
     * @memberof GameDataAttrib
     */
    pingVal: number;
    /**
     * 所在游戏Id(0=无游戏)
     * 
     * @type {number}
     * @memberof GameDataAttrib
     */
    gameId: number;
    /**
     * 所在桌子Id(0=无桌子)
     * 
     * @type {number}
     * @memberof GameDataAttrib
     */
    tableId: number;
    /**
     * 在线状态(0=不在线,1=在线)
     * 
     * @type {number}
     * @memberof GameDataAttrib
     */
    onLine: number;
}
/**
 * 帐号认证相关数据
 * 
 * @interface AuthenticationVo
 */
declare interface AuthenticationVo {
    /**
     * 姓名
     * 
     * @type {string}
     * @memberof AuthenticationVo
     */
    name: string;
    /**
     * 身份证号
     * 
     * @type {string}
     * @memberof AuthenticationVo
     */
    cardId: string;
    /**
     * 帮定的手机号
     * 
     * @type {string}
     * @memberof AuthenticationVo
     */
    phone: string;
}
/**
 * 账号属性
 * 
 * @interface AccountData
 */
declare interface AccountData {
    /**
     * 帐号Id
     * 
     * @type {string}
     * @memberof AccountData
     */
    accountId: string;
    /**
     * 游客或微信唯一uuid
     * 
     * @type {string}
     * @memberof AccountData
     */
    uuid: string;
    /**
     * 角色属性数据
     * 
     * @type {RoleAttribVo}
     * @memberof AccountData
     */
    roleAttribVo: RoleAttribVo;
    /**
     * 钱包数据
     * 
     * @type {WalletVo}
     * @memberof AccountData
     */
    walletVo: WalletVo;
    /**
     * 游戏数据
     * 
     * @type {GameDataAttrib}
     * @memberof AccountData
     */
    gameDataAttribVo: GameDataAttrib;
    /**
     * 帐号认证相关数据
     * 
     * @type {AuthenticationVo}
     * @memberof AccountData
     */
    authenticationVo: AuthenticationVo;
}
/**
 * 创建房间积分信息
 * 
 * @interface ChipAttrib
 */
declare interface ChipAttrib {
    /**
     * 小盲
     * 
     * @type {number}
     * @memberof ChipAttrib
     */
    small: number;
	/**
     * 大盲
     * 
     * @type {number}
     * @memberof ChipAttrib
     */
    big: number;
	/**
     * 最小加入
     * 
     * @type {number}
     * @memberof ChipAttrib
     */
    join: number;
}
/**
 * 创建房间的总配置信息
 * 
 * @interface CreateCfg
 */
declare interface CreateCfg {
    /**
     * 积分配置
     * 
     * @type {ChipAttrib}
     * @memberof CreateCfg
     */
    chips: ChipAttrib[];
    /**
     * 桌子时间配置(单位分钟)
     * 
     * @type {number[]}
     * @memberof CreateCfg
     */
    vaildTimes: number[];
}
/**
 * 参与过游戏未解散的桌子信息
 * 
 * @interface JoinedTableItem
 */
declare interface JoinedTableItem {
    /**
     * 桌子Id
     * 
     * @type {number}
     * @memberof JoinedTableItem
     */
    tableId: number;
    /**
     * 桌子创建玩家
     * 
     * @type {string}
     * @memberof JoinedTableItem
     */
    createPlayer: string;
    /**
     * 头像
     * 
     * @type {string}
     * @memberof JoinedTableItem
     */
    headImg: string;
    /**
     * 呢称
     * 
     * @type {string}
     * @memberof JoinedTableItem
     */
    nick: string;
    /**
     * 小盲注
     * 
     * @type {number}
     * @memberof JoinedTableItem
     */
    small: number;
    /**
     * 大盲注
     * 
     * @type {number}
     * @memberof JoinedTableItem
     */
    big: number;
    /**
     * 最小座下积分
     * 
     * @type {number}
     * @memberof JoinedTableItem
     */
    minJoin: number;
    /**
     * 桌子解散剩余时间(毫秒数)
     * 
     * @type {string}
     * @memberof JoinedTableItem
     */
    vaildTime: string;
    /**
     * 当前座位人数
     * 
     * @type {number}
     * @memberof JoinedTableItem
     */
    currPlayer: number;
    /**
     * 总座位数
     * 
     * @type {number}
     * @memberof JoinedTableItem
     */
    seatNum: number;
}
/**
 * 公告活动
 * 
 * @interface ActivityItemAttrib
 */
declare interface ActivityItemAttrib {
    /**
     * 一级URL
     * 
     * @type {string}
     * @memberof ActivityItemAttrib
     */
    currUrl: string;
    /**
     * 点击后指向的URL
     * 
     * @type {string}
     * @memberof ActivityItemAttrib
     */
    openUrl: string;
}
/**
 * 生涯统计历史数据
 * 
 * @interface CareerHistory
 */
declare interface CareerHistory {
    /**
     * 桌子记录Id
     * @type {string}
     * @memberof CareerHistory
     */
    recordId: string;
    /**
     * 房主帐号Id
     * 
     * @type {string}
     * @memberof CareerHistory
     */
    accountId: string;
    /**
     * 头像
     * 
     * @type {string}
     * @memberof CareerHistory
     */
    headImg: string;
    /**
     * 呢称
     * 
     * @type {string}
     * @memberof CareerHistory
     */
    nick: string;
    /**
     * 房间名称
     * 
     * @type {string}
     * @memberof CareerHistory
     */
    tableName: string;
    /**
     * 记录时间
     * 
     * @type {string}
     * @memberof CareerHistory
     */
    recordTime: string;
    /**
     * 总输赢积分
     * 
     * @type {string}
     * @memberof CareerHistory
     */
    winMoney: string;
}
/**
 * 生涯统计
 * 
 * @interface CareerData
 */
declare interface CareerData {
    /**
     * 获胜率
     * 
     * @type {string}
     * @memberof CareerData
     */
    winRate: string;
    /**
     * 入局率
     * 
     * @type {string}
     * @memberof CareerData
     */
    seatDown: string;
    /**
     * 亮牌率
     * 
     * @type {string}
     * @memberof CareerData
     */
    showCard: string;
    /**
     * 加注率
     * 
     * @type {string}
     * @memberof CareerData
     */
    addChip: string;
    /**
     * 弃牌率
     * 
     * @type {string}
     * @memberof CareerData
     */
    dropCards: string;
    /**
     * 全下率
     * 
     * @type {string}
     * @memberof CareerData
     */
    fullBet: string;
    /**
     * 历史数据
     * 
     * @type {CareerHistory[]}
     * @memberof CareerData
     */
    historyList: CareerHistory[];
}
/**
 * 牌桌大对象
 * 
 * @interface TableData
 */
declare interface TableData {
    /**
     * 桌子创建时间
     * 
     * @type {string}
     * @memberof TableData
     */
    createTime: string;
    /**
     * 桌子到期时间
     * 
     * @type {string}
     * @memberof TableData
     */
    vaildTime: string;
    /**
     * 桌子Id
     * 
     * @type {number}
     * @memberof TableData
     */
    tableId: number;
    /**
     * 桌子创建玩家
     * 
     * @type {string}
     * @memberof TableData
     */
    createPlayer: string;
    /**
     * 桌子名称
     * 
     * @type {string}
     * @memberof TableData
     */
    tableName: string;
    /**
     * 小盲注
     * 
     * @type {number}
     * @memberof TableData
     */
    smallBlind: number;
    /**
     * 大盲注
     * 
     * @type {number}
     * @memberof TableData
     */
    bigBlind: number;
    /**
     * 最小加入积分
     * 
     * @type {number}
     * @memberof TableData
     */
    joinChip: number;
    /**
     * 积分购入上限
     * 
     * @type {number}
     * @memberof TableData
     */
    buyMaxChip: number;
    /**
     * 桌子底牌
     * 
     * @type {number[]}
     * @memberof TableData
     */
    tableHandCards: number[];
    /**
     * 座位列表
     * 
     * @type {SeatData[]}
     * @memberof TableData
     */
    seats: SeatData[];
    /**
     * 桌子当前游戏状态
     * 
     * @type {GameState}
     * @memberof TableData
     */
    gameState: GameState;
    /**
     * 动作总时间(毫秒)
     * 
     * @type {number}
     * @memberof TableData
     */
    actTotalTime: number;
    /**
     * 服务器时间
     * 
     * @type {string}
     * @memberof TableData
     */
    svrTime: string;
    /**
     * 动作到期时间
     * 
     * @type {string}
     * @memberof TableData
     */
    actTime: string;
    /**
     * 本轮桌子最大下注额
     * @type {string}
     * @memberof TableData
     */
    maxBetMoney: string;
    /**
     * 当前表态座位
     * 
     * @type {number}
     * @memberof TableData
     */
    btIndex: number;
    /**
     * 上一个表态座位
     * 
     * @type {number}
     * @memberof TableData
     */
    prevBtIndex: number;
    /**
     * 亮牌座位(-1=无亮牌座位)
     * @type {number}
     * @memberof TableData
     */
    showCardSeatIndex: number;
    /**
     * 本局游戏庄家位置
     * 
     * @type {number}
     * @memberof TableData
     */
    bankerIndex: number;
    /**
     * 桌子是否开始运行
     * 
     * @type {number}
     * @memberof TableData
     */
    start: number;
    /**
     * 桌子游戏局数
     * 
     * @type {number}
     * @memberof TableData
     */
    gameNum: number;
    /**
     * 小盲位置
     * 
     * @type {number}
     * @memberof TableData
     */
    smallSeatIndex: number;
    /**
     * 大盲位置
     * 
     * @type {number}
     * @memberof TableData
     */
    bigSeatIndex: number;
    /**
     * 所有池子总金额
     * @type {string}
     * @memberof TableData
     */
    poolMoneys: string;
    /**
     * 池子列表(第一个为主池,其余为边池)
     * 
     * @type {string[]}
     * @memberof TableData
     */
    pools: string[];
    /**
     * 单局结算数据
     * 
     * @type {SettlementOnceVo[]}
     * @memberof TableData
     */
    settlementOnceList: SettlementOnceVo[];
    /**
     * 购买保险状态数据
     * 
     * @type {InsuranceStateAttrib}
     * @memberof TableData
     */
    insuranceStateAttrib: InsuranceStateAttrib;
}

declare interface InsuranceCfgAttrib {
    /**
     * 牌数
     * @type {number}
     * @memberof InsuranceCfgAttrib
     */
    cardNum: number;
    /**
     *赔率
     * @type {string}
     * @memberof InsuranceCfgAttrib
     */
    rate: string;
}
declare interface InsuranceSeatAttrib {
    /**
     * 座位下标
     * @type {number}
     * @memberof InsuranceSeatAttrib
     */
    seatIndex: number;
    /**
     * 玩家Id
     * @type {string}
     * @memberof InsuranceSeatAttrib
     */
    accountId: string;
    /**
     * 呢称
     * @type {string}
     * @memberof InsuranceSeatAttrib
     */
    nick: string;
    /**
     * 手牌
     * @type {number[]}
     * @memberof InsuranceSeatAttrib
     */
    handCards: number[];
    /**
     * 反超牌个数
     * @type {number}
     * @memberof InsuranceSeatAttrib
     */
    cardNum: number;
}
/**
 * 保险状态数据
 * @interface InsuranceStateAttrib
 */
declare interface InsuranceStateAttrib {
    /**
     * 保险的赔率配置
     * @type {InsuranceCfgAttrib}
     * @memberof InsuranceStateAttrib
     */
    insuranceRateList: InsuranceCfgAttrib[];
    /**
     *座位数据列表
     * @type {InsuranceSeatAttrib}
     * @memberof InsuranceStateAttrib
     */
    insuranceSeatList: InsuranceSeatAttrib[];
    /**
     * 桌子底牌
     * @type {number[]}
     * @memberof InsuranceStateAttrib
     */
    tableHandCards: number[];
    /**
     * 可以反超的牌列表
     * @type {number[]}
     * @memberof InsuranceStateAttrib
     */
    winCardList: number[];
    /**
     * 当前购买保险的玩家
     * @type {string}
     * @memberof InsuranceStateAttrib
     */
    accountId: string;
    /**
     *主池
     * @type {string}
     * @memberof InsuranceStateAttrib
     */
    poolMoney: string;
    /**
     * 主池中我下注金额
     * @type {string}
     * @memberof InsuranceStateAttrib
     */
    betMoney: string;
    /**
     * 玩家已购买的保险积分数
     * @type {string}
     * @memberof InsuranceStateAttrib
     */
    buyedNum: string;
}



/**
 * 单局结算数据
 * 
 * @interface SettlementOnceVo
 */
declare interface SettlementOnceVo {
    /**
     * 座位下标
     * 
     * @type {number}
     * @memberof SettlementOnceVo
     */
    seatIndex: number;
    /**
     * 输赢分数
     * 
     * @type {string}
     * @memberof SettlementOnceVo
     */
    winMoney: string;
}
/**
 * 总结算数据
 * @interface SettlementVo
 */
declare interface SettlementVo {
    /**
     * 游戏局数
     * @type {string}
     * @memberof SettlementVo
     */
    gameNum: string;
    /**
     *胜率
     * @type {string}
     * @memberof SettlementVo
     */
    winRate: string;
    /**
     * 单次最大下注积分数
     * @type {string}
     * @memberof SettlementVo
     */
    maxBetMoney: string;
    /**
     * 单局最大赢取积分数
     * @type {string}
     * @memberof SettlementVo
     */
    maxWinMoney: string;
    /**
     * 总购买积分数
     * @type {string}
     * @memberof SettlementVo
     */
    buyTotalMoney: string;
    /**
     * 结算输赢积分数
     * @type {string}
     * @memberof SettlementVo
     */
    winMoney: string;

}

/**
 * 座位属性
 * 
 * @interface SeatData
 */
declare interface SeatData {
    /**
     * 座位下标
     * 
     * @type {number}
     * @memberof SeatData
     */
    seatIndex: number;
    /**
     * 帐号Id(“0”=座位无人)
     * 
     * @type {string}
     * @memberof SeatData
     */
    accountId: string;
    /**
     * 明星号
     * 
     * @type {string}
     * @memberof SeatData
     */
    starNO: string;
    /**
     * 呢称
     * 
     * @type {string}
     * @memberof SeatData
     */
    nick: string;
    /**
     * 头像
     * 
     * @type {string}
     * @memberof SeatData
     */
    headImg: string;
    /**
     * 手牌
     * 
     * @type {number[]}
     * @memberof SeatData
     */
    handCards: number[];
    /**
     * 当前积分
     * 
     * @type {string}
     * @memberof SeatData
     */
    currMoney: string;
    /**
     * 当前总下注积分
     * 
     * @type {string}
     * @memberof SeatData
     */
    betMoney: string;
    /**
     * 座位表态状态(-1=放弃,0=等待,1=已表态)
     * 
     * @type {number}
     * @memberof SeatData
     */
    btState: number;
    /**
     * 座位表态结果(1=弃牌,2=过牌,3=跟注,4=加注,5=全下, btState=-1也表示弃牌)
     * 
     * @type {number}
     * @memberof SeatData
     */
    btResult: number;
    /**
     * 是否参加本局游戏(1=参加游戏)
     * 
     * @type {number}
     * @memberof SeatData
     */
    bGamed: number;
    /**
     * 当前牌的牌型
     * 
     * @type {CardType}
     * @memberof SeatData
     */
    cardType: CardType;
    /**
     * 闭眼盲注开启状态(1=可以闭眼盲表态)
     * 
     * @type {number}
     * @memberof SeatData
     */
    straddle: number;
    /**
     * 闭眼盲注标志(1=已表态闭眼盲)
     * 
     * @type {number}
     * @memberof SeatData
     */
    straddleFlag: number;
}
/**
 * 保险数据对象
 * 
 * @interface SafeData
 */
declare interface SafeData {
    /**
     * 保险的赔率配置
     * 
     * @type {InsuranceCfgAttrib[]}
     * @memberof SafeData
     */
    insuranceRateList: InsuranceCfgAttrib[];
    /**
     * 座位数据列表
     * 
     * @type {InsuranceSeatAttrib[]}
     * @memberof SafeData
     */
    insuranceSeatList: InsuranceSeatAttrib[];
    /**
     * 桌子底牌
     * 
     * @type {number[]}
     * @memberof SafeData
     */
    tableHandCards: number[];
    /**
     * 可以反超的牌列表
     * 
     * @type {number[]}
     * @memberof SafeData
     */
    winCardList: number[];
    /**
     * 当前购买保险的玩家
     * 
     * @type {string}
     * @memberof SafeData
     */
    accountId: string;
    /**
     * 主池
     * 
     * @type {string}
     * @memberof SafeData
     */
    poolMoney: string;
    /**
     * 主池中我下注金额
     * 
     * @type {string}
     * @memberof SafeData
     */
    betMoney: string;
    /**
     * 玩家已购买的保险积分数
     * 
     * @type {string}
     * @memberof SafeData
     */
    buyedNum: string;
}
/**
 * 保险的赔率属性
 * 
 * @interface InsuranceCfgAttrib
 */
declare interface InsuranceCfgAttrib {
    /**
     * 牌数
     * 
     * @type {number}
     * @memberof InsuranceCfgAttrib
     */
    cardNum: number;
    /**
     * 赔率
     * 
     * @type {string}
     * @memberof InsuranceCfgAttrib
     */
    rate: string;
}
/**
 * 保险座位数据
 * 
 * @interface InsuranceSeatAttrib
 */
declare interface InsuranceSeatAttrib {
    /**
     * 座位下标
     * 
     * @type {number}
     * @memberof InsuranceSeatAttrib
     */
    seatIndex: number;
    /**
     * 玩家Id
     * 
     * @type {string}
     * @memberof InsuranceSeatAttrib
     */
    accountId: string;
    /**
     * 呢称
     * 
     * @type {string}
     * @memberof InsuranceSeatAttrib
     */
    nick: string;
    /**
     * 手牌
     * 
     * @type {number[]}
     * @memberof InsuranceSeatAttrib
     */
    handCards: number[];
    /**
     * 反超牌个数
     * 
     * @type {number}
     * @memberof InsuranceSeatAttrib
     */
    cardNum: number;
}
/**
 * 扑克属性
 * 
 * @interface CardData
 */
declare interface CardData {
    /**
     * 扑克id
     * 
     * @type {number}
     * @memberof CardData
     */
    id: number;
    /**
     * 扑克花色(Id/15)
     * 
     * @type {Suit}
     * @memberof CardData
     */
    suit: Suit;
    /**
     * 扑克点数(Id%15)A为14
     * 
     * @type {number}
     * @memberof CardData
     */
    point: number;
}
/**
 * 申购记录对象
 * 
 * @interface DzpkerOrderItem
 */
declare interface DzpkerOrderItem {
    /**
     * 记录号
     * 
     * @type {string}
     * @memberof DzpkerOrderItem
     */
    itemId: string;
    /**
     * 购买玩家明星号
     * 
     * @type {string}
     * @memberof DzpkerOrderItem
     */
    starNO: string;
    /**
     * 购买玩家呢称
     * 
     * @type {string}
     * @memberof DzpkerOrderItem
     */
    nick: string;
    /**
     * 购买玩家头像
     * 
     * @type {string}
     * @memberof DzpkerOrderItem
     */
    headImg: string;
    /**
     * 购买积分数
     * 
     * @type {string}
     * @memberof DzpkerOrderItem
     */
    chipNum: string;
}
/**
 * 上局回顾对象
 * 
 * @interface PrevRecord
 */
declare interface PrevRecord {
    /**
     * 桌子底牌
     * 
     * @type {number[]}
     * @memberof PrevRecord
     */
    tableCards: number[];
    /**
     * 参与游戏的所有玩家数据
     * 
     * @type {TablePrevFightRecordItem[]}
     * @memberof PrevRecord
     */
    items: TablePrevFightRecordItem[];
}
/**
 * 上一局参与游戏的所有玩家数据
 * 
 * @interface TablePrevFightRecordItem
 */
declare interface TablePrevFightRecordItem {
    /**
     * 手牌
     * 
     * @type {number[]}
     * @memberof TablePrevFightRecordItem
     */
    handCards: number[];
    /**
     * 玩家Id
     * 
     * @type {string}
     * @memberof TablePrevFightRecordItem
     */
    starNO: string;
    /**
     * 头像
     * 
     * @type {string}
     * @memberof TablePrevFightRecordItem
     */
    headImg: string;
    /**
     * 呢称
     * 
     * @type {string}
     * @memberof TablePrevFightRecordItem
     */
    nick: string;
    /**
     * 输赢分数
     * 
     * @type {string}
     * @memberof TablePrevFightRecordItem
     */
    score: string;
    /**
     * 亮牌状态 1=显示牌 0=显示牌背
     * @type {number}
     * @memberof TablePrevFightRecordItem
     */
    showCardState: number;
}
/**
 * 个人盈亏对象
 * 
 * @interface MineRecord
 */
declare interface MineRecord {
    /**
     * 当前积分
     * 
     * @type {string}
     * @memberof MineRecord
     */
    currMoney: string;
    /**
     * 总购买积分
     * 
     * @type {string}
     * @memberof MineRecord
     */
    buyTotalMoney: string;
    /**
     * 输赢信息集合
     * 
     * @type {WinScoreDetailsItem[]}
     * @memberof MineRecord
     */
    items: WinScoreDetailsItem[];
}
/**
 * 单局输赢信息
 * 
 * @interface WinScoreDetailsItem
 */
declare interface WinScoreDetailsItem {
    /**
     * 局数
     * 
     * @type {number}
     * @memberof WinScoreDetailsItem
     */
    gameNum: number;
    /**
     * 输赢分数
     * 
     * @type {string}
     * @memberof WinScoreDetailsItem
     */
    winScore: string;
}
/**
 * 牌桌所有用户数据对象
 * 
 * @interface AllUser
 */
declare interface AllUser {
    /**
     * 保险分数
     * 
     * @type {string}
     * @memberof AllUser
     */
    insuranceScore: string;
    /**
     * 玩家列表
     * 
     * @type {TablePlayerListItem[]}
     * @memberof AllUser
     */
    items: TablePlayerListItem[];
}
/**
 * 房内玩家属性
 * 
 * @interface TablePlayerListItem
 */
declare interface TablePlayerListItem {
    /**
     * 玩家Id
     * 
     * @type {string}
     * @memberof TablePlayerListItem
     */
    starNO: string;
    /**
     * 头像
     * 
     * @memberof TablePlayerListItem
     */
    headImg: string;
    /**
     * 呢称
     * 
     * @memberof TablePlayerListItem
     */
    nick: string;
    /**
     * 总游戏局数
     * 
     * @memberof TablePlayerListItem
     */
    gameNum: string;
    /**
     * 当前积分数
     * 
     * @memberof TablePlayerListItem
     */
    currMoney: string;
    /**
     * 总输赢积分
     * 
     * @memberof TablePlayerListItem
     */
    winMoney: string;
}
/**
 * 热点消息对象
 * 
 * @interface HotPromptAttrib
 */
declare interface HotPromptAttrib {
    /**
     * 热点索引KEY
     * 
     * @type {HotKey}
     * @memberof HotPromptAttrib
     */
    hotKey: HotKey;
    /**
     * 热点值(0=无)
     * 
     * @type {number}
     * @memberof HotPromptAttrib
     */
    hotVal: number;
}
