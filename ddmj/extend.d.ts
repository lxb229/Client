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
    ddUrl: string;
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
     * 报牌语言,0是关闭不报牌,1是四川话,2是普通话(默认四川话)
     */
    language: number;

    /**
     * 是否开启背景音乐(默认开启)
     */
    isMusic: boolean;

    /**
     * 是否开启音效(默认开启)
     */
    isEffect: boolean;

    /**
     * 是否开启语音(默认开启)
     */
    isSound: boolean;
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
 * 游戏场景中的音效
 * 
 * @interface gameAudioURL
 */
declare interface gameAudioURL {
    /**
     * 拼接的路径
     * 
     * @type {string}
     * @memberof gameAudioURL
     */
    path: string;
    /**
     * 完整路径
     * 
     * @type {string}
     * @memberof gameAudioURL
     */
    url: string;
}

/**
 * 单个RuleContentItemAttrib属性:
 * 
 * @interface RuleContentItemAttrib
 */
declare interface RuleContentItemAttrib {
    /**
     * 规则Id
     * 
     * @type {number}
     * @memberof RuleContentItemAttrib
     */
    ruleId: number;
    /**
     * 规则名称
     * 
     * @type {string}
     * @memberof RuleContentItemAttrib
     */
    ruleName: string;
    /**
     * 初始选中状态(0=未选中,1=选中)
     * @type {number}
     * @memberof RuleContentItemAttrib
     */
    state: number;
}
/**
 * 单个RuleContentItem属性:
 * 
 * @interface RuleContentItem
 */
declare interface RuleContentItem {
    /**
     * 单选多选(0=单选,1=多选)
     * 
     * @type {number}
     * @memberof RuleContentItem
     */
    ridio: number;
    /**
     * 规则列表
     * 
     * @type {RuleContentItemAttrib[]}
     * @memberof RuleContentItem
     */
    ruleContentItemAttribs: RuleContentItemAttrib[];
}
/**
 * 单个RuleContent属性:
 * 
 * @interface RuleContent
 */
declare interface RuleContent {
    /**
     * 规则名称
     * 
     * @type {string}
     * @memberof RuleContent
     */
    ruleName: string;
    /**
     * 规则子项组
     * 
     * @type {RuleContentItem[]}
     * @memberof RuleContent
     */
    ruleContentItems: RuleContentItem[];
}

/**
 * 房间单条配置数据
 * 
 * @interface RuleCfgVo
 */
declare interface RuleCfgVo {
    /**
     * 条目Id
     * 
     * @type {number}
     * @memberof RuleCfgVo
     */
    itemId: number;
    /**
     * 条目名称
     * 
     * @type {string}
     * @memberof RuleCfgVo
     */
    itemName: string;
    /**
     * 规则内容
     * 
     * @type {RuleContent[]}
     * @memberof RuleCfgVo
     */
    ruleContents: RuleContent[];
}


declare interface CreateRoom {
    /**
     * 俱乐部id
     * 
     * @type {string}
     * @memberof CreateRoom
     */
    corpsId: string,
    /**
     * 游戏id
     * 
     * @type {number}
     * @memberof CreateRoom
     */
    roomItemId: number,
    /**
     * 配置规则
     * 
     * @type {number[]}
     * @memberof CreateRoom
     */
    rules: number[]
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
     * 座位玩家Id
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
     * 
     * @type {string}
     * @memberof SeatVo
     */
    nick: string;

    /**
     * 性别
     * @type {number}
     * @memberof SeatVo
     */
    sex: number;
    /**
     * 在线状态(0=离线,1=在线)
     * @type {number}
     * @memberof SeatVo
     */
    onLine: number;
    /**
     * 手牌长度
     * @type {number}
     * @memberof SeatVo
     */
    handCardsLen: number;
    /**
     * 座位暗杠的牌
     * @type {number[]}
     * @memberof SeatVo
     */
    anGangCards: number[][];
    /**
     * 座位明杠的牌
     * @type {number[]}
     * @memberof SeatVo
     */
    baGangCards: number[][];
    /**
     * 座位碰的牌
     * @type {number[]}
     * @memberof SeatVo
     */
    pengCards: number[][];
    /**
     * 玩家打出别人不要的牌
     * @type {number[]}
     * @memberof SeatVo
     */
    outUnUseCards: number[];
    /**
     * 玩家当前已胡的牌
     * @type {number[]}
     * @memberof SeatVo
     */
    huCards: number[];
    /**
     * 当前分数
     * @type {number}
     * @memberof SeatVo
     */
    score: number;
    /**
     *表态状态(-1=放弃,0=等待,1=已表态) 
     * @type {number}
     * @memberof SeatVo
     */
    btState: number;
    /**
     * 缺花色(1=万,2=筒,3=条)
     * @type {number}
     * @memberof SeatVo
     */
    unSuit: number;
    /**
     * 胡牌方式(0=未胡牌,1=自摸 ,2=点炮,3=抢杠胡,4=自摸杠上花,5=点杠上花胡,6=点杠上炮,7=查叫) 
     * @type {number}
     * @memberof SeatVo
     */
    huPaiType: number;
    /**
     * 胡牌顺序 
     * @type {number}
     * @memberof SeatVo
     */
    huPaiIndex: number;
    /**
     * 杠牌类型(0=非杠,1=自摸巴杠,2=自摸暗杠,3=点杠)
     * 
     * @type {number}
     * @memberof SeatVo
     */
    gangType: number;
    //以下数据只能自已可见,别人不可见
    /**
     * 玩家当前手牌
     * @type {number[]}
     * @memberof SeatVo
     */
    handCards: number[];

    /**
     * 换牌数据
     * @type {number[]}
     * @memberof SeatVo
     */
    swapCards: number[];

    /**
     * 摸的牌
     * @type {number}
     * @memberof SeatVo
     */
    moPaiCard: number;
    /**
     * 打出的牌
     * @type {number}
     * @memberof SeatVo
     */
    outCard: number;
    /**
     * 胡杠碰的牌
     * @type {number}
     * @memberof SeatVo
     */
    breakCard: number;
    /**
     *胡杠碰躺吃状态 
     * @type {number[]}
     * @memberof SeatVo
     */
    breakCardState: number[];
    /**
     * 躺牌状态(0=未躺牌,1=已躺牌)
     * @type {number}
     * @memberof SeatVo
     */
    tangCardState: number;
    /**
     *躺的牌
     * @type {number[]}
     * @memberof SeatVo
     */
    tangCardList: number[];
    /**
     * 报叫状态(-1=无报叫状态,0=等待报叫,1=已报叫)
     * @type {number}
     * @memberof SeatVo
     */
    baojiaoState: number;
}


/**
 * 单局结算数据
 * 
 * @interface SettlementOnceVo
 */
declare interface SettlementOnceVo {
    /**
     * 玩家Id
     * @type {string}
     * @memberof SeatVo
     */
    accountId: string;
    /**
    * 头像 
    * @type {string}
    * @memberof SettlementAllVo
    */
    headImg: string;
    /**
     * 呢称
     * @type {string}
     * @memberof SettlementAllVo
     */
    nick: string;
    /**
     *庄家标志(0=非庄家,1=是庄家) 
     * @type {number}
     * @memberof SettlementAllVo
     */
    banker: number;
    /**
     *玩家当前手牌
     * @type {number[]}
     * @memberof SettlementAllVo
     */
    handCards: number[];
    /**
     * 座位暗杠的牌
     * @type {number[]}
     * @memberof SettlementAllVo
     */
    anGangCards: number[][];
    /**
     *座位巴杠的牌
     * @type {number[]}
     * @memberof SettlementAllVo
     */
    baGangCards: number[][];
    /**
     * 座位点杠的牌
     * @type {number[]}
     * @memberof SettlementAllVo
     */
    dianGangCards: number[][];
    /**
     * 座位碰的牌
     * @type {number[]}
     * @memberof SettlementAllVo
     */
    pengCards: number[][];
    /**
     * 玩家当前已胡的牌
     * @type {number[]}
     * @memberof SettlementAllVo
     */
    huCards: number[];
    /**
     *翻数 
     * @type {number}
     * @memberof SettlementAllVo
     */
    rate: number;
    /**
     * 本局分数
     * @type {number}
     * @memberof SettlementAllVo
     */
    score: number;
    /**
     * 胡牌顺序(0=未胡牌)
     * @type {number}
     * @memberof SettlementAllVo
     */
    huPaiIndex: number;
    /**
     *胡牌描述
     * @type {string}
     * @memberof SettlementAllVo
     */
    huPaiDesc: string;
    /**
     * 座位单局结算分数
     * @type {SettementSeatScore[]}
     * @memberof SettlementOnceVo
     */
    seatScore: SettementSeatScore[];
}
declare interface SettementSeatScore {
    /**
     * 分数
     * @type {number}
     * @memberof SettementSeatScore
     */
    score: number;
    /**
     * 躺个数 -1=游戏中没有躺规则 0=无躺 1=单躺 2=双躺
     * @type {number}
     * @memberof SettementSeatScore
     */
    tangNum: number;
    /**
     * 报叫个数(-1=无报叫的游戏规则)  0=无叫 1=单叫 2=双叫
     * @type {number}
     * @memberof SettementSeatScore
     */
    baoJiaoNum: number;
    /**
     * 总翻数
     * @type {number}
     * @memberof SettementSeatScore
     */
    totalFanNum: number;
    /**
     * 飞数量
     * @type {number}
     * @memberof SettementSeatScore
     */
    flyNum: number;
}

/**
 * 总结算数据
 * 
 * @interface SettlementAllVo
 */
declare interface SettlementAllVo {
    /**
     * 玩家Id
     * @type {string}
     * @memberof SeatVo
     */
    accountId: string;
    /**
     * 头像
     * @type {string}
     * @memberof SettlementAllVo
     */
    headImg: string;
    /**
     * 呢称
     * @type {string}
     * @memberof SettlementAllVo
     */
    nick: string;
    /**
     * 明星号
     * @type {string}
     * @memberof SettlementAllVo
     */
    starNO: string;
    /**
     * 自摸次数 
     * @type {number}
     * @memberof SettlementAllVo
     */
    zimo: number;
    /**
     *别人点我炮次数
     * @type {number}
     * @memberof SettlementAllVo
     */
    otherHuPai: number;
    /**
     * 我点炮次数
     * @type {number}
     * @memberof SettlementAllVo
     */
    dianPao: number;
    /**
     * 暗杠次数
     * @type {number}
     * @memberof SettlementAllVo
     */
    angang: number;
    /**
     *明杠次数
     * @type {number}
     * @memberof SettlementAllVo
     */
    mimang: number;
    /**
     * 查叫次数
     * @type {number}
     * @memberof SettlementAllVo
     */
    chajiao: number;
    /**
     * 总分数
     * @type {number}
     * @memberof SettlementAllVo
     */
    score: number;
}


/**
 * 桌子基础数据,所有人都可见
 * 
 * @interface TableBaseVo
 */
declare interface TableBaseVo {
    /**
     * 游戏ID 1=血战 2=三人两方 3=三人三方 4两人两方 5绵阳麻将 6自贡麻将 乐山麻将
     * @type {number}
     * @memberof TableBaseVo
     */
    cfgId: number;
    /**
     * 俱乐部id
     * @type {string}
     * @memberof TableBaseVo
     */
    corpsId: string;
    /**
    * 桌子Id
    * @type {number}
    * @memberof MJGameData
    */
    tableId: number;
    /**
     * 桌子类型 0=一般房,1=聊天房
     * 
     * @type {number}
     * @memberof MJGameData
     */
    tableChatType: number;
    /**
     * 桌子创建玩家Id
     * @type {string}
     * @memberof MJGameData
     */
    createPlayer: string;
    /**
     * 庄家位置
     * @type {number}
     * @memberof MJGameData
     */
    bankerIndex: number;
    /**
     * 当前局数
     * @type {number}
     * @memberof MJGameData
     */
    currGameNum: number;
    /**
     * 最大游戏局数
     * @type {number}
     * @memberof MJGameData
     */
    maxGameNum: number;
    /**
     * 当前桌子牌张数
     * @type {number}
     * @memberof MJGameData
     */
    tableCardNum: number;
    /**
     * 服务器当前时间
     * @type {string}
     * @memberof MJGameData
     */
    svrTime: string;
    /**
     * 当前动作时间
     * @type {string}
     * @memberof MJGameData
     */
    actTime: string;
    /**
     * 桌子游戏状态
     * @type {number}
     * @memberof MJGameData
     */
    gameState: number;
    /**
     * 表态座位下标
     * @type {number}
     * @memberof MJGameData
     */
    btIndex: number;
    /**
     *上个表态座位下标
     * @type {number}
     * @memberof MJGameData
     */
    prevBtIndex: number;
    /**
     * 桌子规则描述文本
     * @type {string}
     * @memberof MJGameData
     */
    ruleShowDesc: string;
    /**
     * 换牌方式(默认=0)(0=[0->1,1->2,2->3,3->0],1=[0->3,3->2,2->1,1->0],2=[0->2,1->3])
     * @memberof TableBaseVo
     */
    swapCardType: number;
    /**
     * 解散桌子的申请人
     * @type {string}
     * @memberof TableBaseVo
     */
    destoryQuestPlayer: string;
    /**
     * 是否有下一局(1=有,0=无)
     * @type {number}
     * @memberof TableBaseVo
     */
    nextGame: number;
    /**
     * 听牌是否提示 0不提示 1提示
     * @type {number}
     * @memberof TableBaseVo
     */
    tingTips: number;
    /**
     * 是否幺鸡任用(0=不替换,1=任用)
     * @type {number}
     * @memberof TableBaseVo
     */
    yaojiReplace: number;
}

/**
 * 血战到底的数据
 * 
 * @interface MJGameData
 */
declare interface MJGameData {
    tableBaseVo: TableBaseVo;
    /**
     * 座位数据
     * @type {SeatVo[]}
     * @memberof MJGameData
     */
    seats: SeatVo[];
    /**
     * 胡杠碰吃打断处理结果状态(0=胡,1=杠,2=碰,3=吃,4=过)
     * @type {number}
     * @memberof MJGameData
     */
    breakState: number;
    /**
     * 胡杠碰吃响应座位下标
     * @type {number[]}
     * @memberof MJGameData
     */
    breakSeats: number[];
    /**
     * 单局结算数据
     * @type {SettlementOnceVo[]}
     * @memberof MJGameData
     */
    settlementOnce: SettlementOnceVo[];
    /**
     * 总结算数据
     * @type {SettlementAllVo[]}
     * @memberof MJGameData
     */
    settlementAll: SettlementAllVo[];
}

/**
 * 牌列表排序后的数据
 * 
 * @interface SortCardData
 */
declare interface SortCardData {
    /**
     * 碰牌列表
     * 
     * @type {number[]}
     * @memberof SortCardData
     */
    pengList: number[];
    /**
     * 杠牌列表
     * 
     * @type {number[]}
     * @memberof SortCardData
     */
    gangList: number[];
    /**
     * 对子列表
     * 
     * @type {number[]}
     * @memberof SortCardData
     */
    duiList: number[];
}
/**
 * 杠牌对象数据
 * @interface GangData
 */
declare interface GangData {
    cardId: number, //杠的牌
    isAnGang: number, //1=巴杠,2=暗杠或直杠
    isUseYaoJi: boolean,//是否使用幺鸡
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

declare interface ReplayData {
    /**
     * 帧开始时间
     * @type {string}
     * @memberof ReplayData
     */
    startTime: string;
    /**
     * 帧开始时间
     * @type {MJGameData}
     * @memberof ReplayData
     */
    frameData: MJGameData;
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
     * 热点索引KEY(1=邮件 2俱乐部申请成员数量)
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
declare interface MailVo {
    /**
     * 邮件Id
     * 
     * @type {number}
     * @memberof MailVo
     */
    mailId: number;

    /**
     * 发送人帐号Id(0=系统邮件)
     * 
     * @type {string}
     * @memberof MailVo
     */
    senderId: string;
    /**
     * 发送人呢称
     * 
     * @type {string}
     * @memberof MailVo
     */
    senderNickeName: string;
    /**
     * 邮件标题
     * 
     * @type {string}
     * @memberof MailVo
     */
    title: string;
    /**
     * 邮件内容
     * 
     * @type {string}
     * @memberof MailVo
     */
    content: string;
    /**
     * 接收时间
     * 
     * @type {string}
     * @memberof MailVo
     */
    recvTime: string;

}

/**
 * 战绩座位数据
 * 
 * @interface RecordSeatVo
 */
declare interface RecordSeatVo {
    /**
     * 帐号Id
     * @type {string}
     * @memberof RecordSeatVo
     */
    accountId: string;
    /**
     * 呢称
     * @type {string}
     * @memberof RecordSeatVo
     */
    nick: string;
    /**
     * 分数
     * @type {number}
     * @memberof RecordSeatVo
     */
    score: number;

}
/**
 * 战绩项数据
 * 
 * @interface RecordItemVo
 */
declare interface RecordItemVo {
    /**
     * 记录Id
     * @type {string}
     * @memberof RecordItemVo
     */
    recordId: string;
    /**
     * 桌子Id
     * @type {number}
     * @memberof RecordItemVo
     */
    tableId: number;
    /**
     * 记录时间
     * @type {string}
     * @memberof RecordItemVo
     */
    recordTime: string;
    /**
     * 座位数据 
     * @type {RecordSeatVo[]}
     * @memberof RecordItemVo
     */
    seats: RecordSeatVo[];
}

/**
 * 战绩数据
 * 
 * @interface RecordVo
 */
declare interface RecordVo {
    /**
     * 列表项数
     * @type {number}
     * @memberof RecordVo
     */
    itemNum: number;
    /**
     * 项列表
     * @type {RecordItemVo[]}
     * @memberof RecordVo
     */
    items: RecordItemVo[];
}

/**
 * 详细战绩项数据
 * 
 * @interface RecordDetailedItemVo
 */
declare interface RecordDetailedItemVo {
    /**
     *局数 
     * @type {number}
     * @memberof RecordDetailedItemVo
     */
    gameNum: number;
    /**
     *记录时间
     * @type {string}
     * @memberof RecordDetailedItemVo
     */
    recordTime: string;
    /**
     *分数列表
     * @type {number[]}
     * @memberof RecordDetailedItemVo
     */
    scores: number[];
    /**
     *录像文件
     * @type {string}
     * @memberof RecordDetailedItemVo
     */
    recordFile: string;
}

/**
 * 详细战绩数据
 * 
 * @interface RecordDetailedVo
 */
declare interface RecordDetailedVo {
    /**
    * 桌子Id
    * @type {number}
    * @memberof RecordDetailedVo
    */
    tableId: number;
    /**
     * 座位玩家呢称
     * @type {string[]}
     * @memberof RecordDetailedVo
     */
    nicks: string[];
    /**
     *列表数据
     * @type {RecordDetailedItemVo[]}
     * @memberof RecordDetailedVo
     */
    items: RecordDetailedItemVo[]
}

/**
 * 俱乐部信息
 * 
 * @interface CorpsVoInner
 */
declare interface CorpsVoInner {
    /**
     * 帮会名称
     * @type {string}
     * @memberof CorpsDetailed
     */
    corpsName: string;
    /**
     * 帮会Id
     * @type {string}
     * @memberof CorpsVoInner
     */
    corpsId: string;
    /**
     * 群主Id
     * @type {string}
     * @memberof CorpsVoInner
     */
    createPlayer: string;
    /**
     * 帮主呢称
     * @type {string}
     * @memberof CorpsVoInner
     */
    nick: string;
    /**
     * 帮会创建时间
     * @type {string}
     * @memberof CorpsVoInner
     */
    createTime: string;
    /**
     *帮会人数
     * @type {number}
     * @memberof CorpsVoInner
     */
    memberNum: number;
    /**
     *帮会房卡数
     * @type {number}
     * @memberof CorpsVoInner
     */
    roomCard: number;
    /**
     *房卡状态(0=关闭,1=开放)
     * @type {number}
     * @memberof CorpsVoInner
     */
    state: number;
    /**
     * 帮会状态(0=正常,-1=已冻结)
     * @type {number}
     * @memberof CorpsVoInner
     */
    corpsState: number;
}
/**
 * 帮会详细信息
 * @interface CorpsDetailed
 */
declare interface CorpsDetailed {
    /**
     * 帮会名称
     * @type {string}
     * @memberof CorpsDetailed
     */
    corpsName: string;
    /**
     * 帮会Id
     * @type {string}
     * @memberof CorpsVoInner
     */
    corpsId: string;
    /**
     * 帮会状态(0=正常,-1=已冻结)
     * @type {number}
     * @memberof CorpsVoInner
     */
    corpsState: number;
    /**
     * 帮会桌子列表
     * @type {CorpsTableInner[]}
     * @memberof CorpsDetailed
     */
    tables: CorpsTableInner[];
    /**
     * 帮会成员列表
     * @type {CorpsMemberVoInner[]}
     * @memberof CorpsDetailed
     */
    members: CorpsMemberVoInner[];

}

/**
 * 桌子玩家信息
 * 
 * @interface CorpsTablePlayer
 */
declare interface CorpsTablePlayer {
    /**
     *帐号Id(0=无人座位)
     * @type {string}
     * @memberof CorpsTablePlayer
     */
    accountId: string;
    /**
     * 呢称 
     * @type {string}
     * @memberof CorpsTablePlayer
     */
    nick: string;
    /**
     *网络ping值 
     * @type {number}
     * @memberof CorpsTablePlayer
     */
    pingVal: number;
    /**
     * 角色头像
     * 
     * @type {string}
     * @memberof UserData
     */
    headImg: string;
}
/**
 * 俱乐部游戏桌子数据
 * 
 * @interface CorpsTableInner
 */
declare interface CorpsTableInner {
    /**
     * 桌子号
     * @type {number}
     * @memberof CorpsTableInner
     */
    tableId: number;
    /**
     * 桌子规则描述
     * @type {string}
     * @memberof CorpsTableInner
     */
    ruleShowDesc: string;
    /**
     * 玩家列表
     * 
     * @type {CorpsTablePlayer[]}
     * @memberof CorpsTableInner
     */
    seats: CorpsTablePlayer[];
}

/**
 * 俱乐部成员信息
 * 
 * @interface CorpsMemberVoInner
 */
declare interface CorpsMemberVoInner {
    /**
     *呢称
     * @type {string}
     * @memberof CorpsMemberVoInner
     */
    nick: string;
    /**
     * 角色头像
     * 
     * @type {string}
     * @memberof UserData
     */
    headImg: string;
    /**
     *明星号唯一Id
     * @type {string}
     * @memberof CorpsMemberVoInner
     */
    starNO: string;
    /**
     * 群主标识(1=群主)
     * @type {number}
     * @memberof CorpsMemberVoInner
     */
    flag: number;
    /**
     * 加入时间
     * @type {string}
     * @memberof CorpsMemberVoInner
     */
    joinTime: string;
    /**
     *消耗房卡
     * @type {number}
     * @memberof CorpsMemberVoInner
     */
    costCard: number;
    /**
     *最后在线时间
     * @type {string}
     * @memberof CorpsMemberVoInner
     */
    lastTime: string;
    /**
     * 游戏状态(0=离线,1=在线,2=游戏中) 
     * @type {number}
     * @memberof CorpsMemberVoInner
     */
    state: number;
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
 * 商城商品列表
 * @interface StoreGoods
 */
declare interface StoreGoods {
    /**
     * 商品列表
     * @type {MallItem[]}
     * @memberof StoreGoods
     */
    mallItems: MallItem[];
    /**
     * 代理商列表
     * @type {MallProxyItem[]}
     * @memberof StoreGoods
     */
    proxyItems: MallProxyItem[];
}
/**
 * MallItem属性
 * @interface MallItem
 */
declare interface MallItem {
    /**
     * 商品Id
     * @type {number}
     * @memberof MallItem
     */
    itemId: number;
    /**
     * 售价
     * @type {number}
     * @memberof MallItem
     */
    sellPrice: number;
    /**
     * 房卡数
     * @type {number}
     * @memberof MallItem
     */
    roomCardNum: number;
}
/**
 * MallProxyItem属性
 * @interface MallProxyItem
 */
declare interface MallProxyItem {
    /**
     * 代理商类型
     * @type {sring}
     * @memberof MallProxyItem
     */
    proxyType: sring;
    /**
     * 代理商备注
     * @type {sring}
     * @memberof MallProxyItem
     */
    proxyDesc: sring;
    /**
     * 代理商WX号
     * @type {sring}
     * @memberof MallProxyItem
     */
    wxNO: sring;
}
/**
 * ios商品属性
 * 
 * @interface Product
 */
declare interface Product {
    /**
     * 标题
     * 
     * @type {string}
     * @memberof Product
     */
    title: string;
    /**
     * 说明描述
     * 
     * @type {string}
     * @memberof Product
     */
    description: string;
    /**
     * 价格
     * 
     * @type {string}
     * @memberof Product
     */
    price: string;
    /**
     * 商品ID
     * 
     * @type {string}
     * @memberof Product
     */
    productid: string;
}
/**
 * 躺牌的信息
 * @interface TangCfg
 */
declare interface TangCfg {
    /**
     * 躺牌后要打出的牌
     * @type {number}
     * @memberof TangInfo
     */
    outcard: number,
    /**
     * 躺牌后胡的牌
     * @type {number[]}
     * @memberof TangInfo
     */
    hucards: number[],
    /**
     * 除了要打出的牌，剩余的手牌
     * @type {number[]}
     * @memberof TangInfo
     */
    cardIds: number[],
    /**
     * 表态的牌
     * @type {number[]}
     * @memberof TangInfo
     */
    btcards: number[]
}



