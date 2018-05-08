import UDManager from "./UDManager";
import { MJ_Suit, MJ_Game_Type, MJ_GameState } from "./Protocol";
import MJ_Game_Mine from '../SceneScript/Game/MJ_Game_Mine';
import MJCanvas from '../SceneScript/Game/MJCanvas';
/**
 * 将对数据
 */
declare interface JDData {
    /**
     * 组成将对的牌
     */
    card: number,
    /**
     * 需要花费鬼牌的张数(0,1,2)
     */
    count: number
}
/**
 * 游戏管理类
 * 
 * @export
 * @class GMManager
 */
export default class GMManager {
    private wans: number[] = [];
    private tongs: number[] = [];
    private tiaos: number[] = [];
    private static _instance: GMManager = null;
    private constructor() { }
    /**
     * 获取GMManager单例对象
     * 
     * @static
     * @returns {GMManager} 
     * @memberof GMManager
     */
    static getInstance(): GMManager {
        if (GMManager._instance === null) {
            GMManager._instance = new GMManager();
            //初始化牌
            for (let i = 1; i < 4; i++) {
                for (let j = 1; j < 10; j++) {
                    let cardId = i * 10 + j;
                    switch (i) {
                        case 1: GMManager._instance.wans.push(cardId); break;
                        case 2: GMManager._instance.tongs.push(cardId); break;
                        case 3: GMManager._instance.tiaos.push(cardId); break;
                        default: break;
                    }
                }
            }
        }
        return GMManager._instance;
    }
    /**
     * 麻将游戏数据
     * 
     * @type {MJGameData}
     * @memberof GMManager
     */
    mjGameData: MJGameData = null;
    /**
     * 是否是重播麻将记录 0否 1是
     * 
     * @type {number}
     * @memberof GMManager
     */
    replayMJ: number = 0;

    /**
     * 牌的点击触摸id(不是牌的id)
     * @type {cc.Touch}
     * @memberof GMManager
     */
    touchTarget: cc.Touch = null;

    /**
     * 麻将记录数据列表
     * 
     * @type {MJGameData[]}
     * @memberof GMManager
     */
    replayDataList: ReplayData[] = [];
    /**
     * 战绩记录的id
     * @type {string}
     * @memberof GMManager
     */
    replayRecordId: string = '0';
    /**
     * 是否重播暂停
     * 
     * @type {boolean}
     * @memberof GMManager
     */
    isReplayPause: boolean = false;
    /**
     * 游戏自己手牌的脚本
     * @type {MJ_Game_Mine}
     * @memberof GMManager
     */
    _minScript: MJ_Game_Mine = null;
    /**
     * 游戏canvas脚本
     * @type {MJCanvas}
     * @memberof GMManager
     */
    _gmScript: MJCanvas = null;
    /**
     * 设置
     * @param {any} target 
     * @memberof GMManager
     */
    setGMTarget(target) {
        this._gmScript = target;
    }
    /**
     * 获取游戏场景的脚本
     * @returns 
     * @memberof GMManager
     */
    getGMTarget() {
        if (!this._gmScript) {
            this._gmScript = cc.director.getScene().getChildByName('Canvas').getComponent('MJCanvas');
        }
        return this._gmScript;
    }
    /**
     * 刷新游戏数据
     * @param {MJGameData} tableData 
     * @param {boolean} [isComing=false] 是否进入游戏
     * @param {number} [replayMJ=0] 是否重播游戏 0否 1是
     * @memberof GMManager
     */
    setTableData(tableData: MJGameData, isComing: boolean = false, replayMJ: number = 0) {
        this.mjGameData = tableData;
        if (isComing) {
            this.mjGameData.seats = this.sortSeatList(this.mjGameData.seats);
            this.replayMJ = replayMJ;
        }
        cc.systemEvent.emit('MJ_GamePush');
    }

    /**
     * 刷新桌子状态数据
     * @memberof GMManager
     */
    setStateData(data: any) {
        cc.log('------桌子状态推送数据------' + data.state);
        cc.log(data);
        if (!this.mjGameData) return;
        let tableData = this.mjGameData;
        tableData.tableBaseVo.gameState = data.state;
        tableData.tableBaseVo.svrTime = data.svrTime;
        tableData.tableBaseVo.actTime = data.stateVaildTime;
        tableData.tableBaseVo.currGameNum = data.currGameNum;
        tableData.tableBaseVo.tableCardNum = data.tableCardNum;
        tableData.tableBaseVo.btIndex = data.btIndex;
        tableData.tableBaseVo.prevBtIndex = data.prevBtIndex;
        tableData.tableBaseVo.tangCanHuList = data.tangCanHuList;
        tableData.breakSeats = null;
        tableData.breakState = 0;

        let stateData = data.stateData;
        //游戏状态
        switch (tableData.tableBaseVo.gameState) {
            case MJ_GameState.STATE_TABLE_READY://准备阶段，刷新庄家位置
                tableData.tableBaseVo.bankerIndex = stateData.bankerIndex;
                break;
            case MJ_GameState.STATE_TABLE_FAPAI://发牌阶段，刷新每个座位的牌
                stateData.seats.forEach((seatInfo) => {
                    for (let i = 0; i < tableData.seats.length; i++) {
                        let seat = tableData.seats[i];
                        if (seat.seatIndex === seatInfo.seatIndex) {
                            tableData.seats[i].handCardsLen = seatInfo.handCardsLen;
                            tableData.seats[i].handCards = seatInfo.handCards;
                            break;
                        }
                    }
                });
                break;
            case MJ_GameState.STATE_TABLE_SWAPCARD://换牌阶段,刷新每个座位的表态状态
            case MJ_GameState.STATE_TABLE_DINGQUE://定缺阶段,刷新每个座位的表态状态
            case MJ_GameState.STATE_TABLE_PIAOPAI://漂牌阶段,刷新每个座位的漂牌状态
                stateData.seats.forEach((seatInfo) => {
                    for (let i = 0; i < tableData.seats.length; i++) {
                        let seat = tableData.seats[i];
                        if (seat.seatIndex === seatInfo.seatIndex) {
                            tableData.seats[i].btState = seatInfo.btState;
                            break;
                        }
                    }
                });
                break;
            case MJ_GameState.STATE_TABLE_DESTORY://解散桌子阶段,刷新每个座位的表态状态
                tableData.tableBaseVo.destoryQuestPlayer = stateData.destoryQuestPlayer;
                stateData.seats.forEach((seatInfo) => {
                    for (let i = 0; i < tableData.seats.length; i++) {
                        let seat = tableData.seats[i];
                        if (seat.seatIndex === seatInfo.seatIndex) {
                            tableData.seats[i].btState = seatInfo.btState;
                            break;
                        }
                    }
                });
                break;
            case MJ_GameState.STATE_TABLE_ZHUAPAI://摸牌阶段，刷新摸牌位置的摸牌
                for (let i = 0; i < tableData.seats.length; i++) {
                    let seat = tableData.seats[i];
                    if (seat.seatIndex === stateData.seatIndex) {
                        tableData.seats[i].moPaiCard = stateData.moPaiCard;
                        tableData.seats[i].btState = stateData.btState;
                        break;
                    }
                }
                break;
            case MJ_GameState.STATE_TABLE_OUTCARD://出牌阶段，刷新出牌位置
                tableData.tableBaseVo.btIndex = stateData.seatIndex;
                for (let i = 0; i < tableData.seats.length; i++) {
                    let seat = tableData.seats[i];
                    if (seat.seatIndex === stateData.seatIndex) {
                        tableData.seats[i].btState = stateData.btState;
                        break;
                    }
                }
                break;
            case MJ_GameState.STATE_TABLE_BREAKCARD://胡碰杠躺阶段，刷新胡碰杠列表数据 和 牌
                stateData.seats.forEach((seatInfo) => {
                    for (let i = 0; i < tableData.seats.length; i++) {
                        let seat = tableData.seats[i];
                        if (seat.seatIndex === seatInfo.seatIndex) {
                            tableData.seats[i].breakCardState = seatInfo.breakCardState;
                            tableData.seats[i].breakCard = seatInfo.breakCard;
                            tableData.seats[i].btState = seatInfo.btState;
                            break;
                        }
                    }
                });
                break;
            case MJ_GameState.STATE_TABLE_OVER_ONCE://单局阶段阶段，刷新单局结算数据
                tableData.tableBaseVo.nextGame = stateData.nextGame;
                tableData.settlementOnce = stateData.settlementOnce;
                break;
            case MJ_GameState.STATE_TABLE_OVER_ALL://总阶段阶段，刷新总结算数据
                tableData.settlementAll = stateData.settlementAll;
                break;
            case MJ_GameState.STATE_TABLE_BAOJIAO://报叫阶段，刷新报叫状态数据(-1=无报叫状态,0=等待报叫,1=已报叫)
                stateData.seats.forEach((seatInfo) => {
                    for (let i = 0; i < tableData.seats.length; i++) {
                        let seat = tableData.seats[i];
                        if (seat.seatIndex === seatInfo.seatIndex) {
                            tableData.seats[i].baojiaoState = seatInfo.baojiaoState;
                            break;
                        }
                    }
                });
                break;
            default:
                break;
        }
        this.setTableData(tableData, false);
    }

    /**
     * 刷新座位数据
     * @param {*} data 
     * @param {number} state 1=座位数据 2=定缺数据 3=出牌数据 4=解散数据 5=报叫数据 6=漂牌数据
     * @memberof GMManager
     */
    setSeatData(data: any, state: number) {
        cc.log('------座位推送数据------');
        cc.log(data);
        if (!this.mjGameData || !this.mjGameData.seats) return;
        let seats = this.mjGameData.seats;
        for (let i = 0; i < seats.length; i++) {
            if (seats[i].seatIndex === data.seatIndex) {
                switch (state) {
                    case 1://座位推送数据
                        seats[i] = data;
                        break;
                    case 2://定缺推送数据
                        seats[i].btState = data.btState;
                        seats[i].unSuit = data.unSuit;
                        break;
                    case 3://出牌推送数据
                        seats[i] = data;
                        break;
                    case 4://桌子解散表态推送数据
                        seats[i].btState = data.btState;
                        break;
                    case 5://报叫推送数据
                        seats[i].baojiaoState = data.baojiaoState;
                        break;
                    case 6://飘牌推送数据
                        seats[i].btState = data.btState;
                        seats[i].piaoNum = data.piaoNum;
                        break;
                    default:
                        cc.log('state错误');
                        break;
                }
                break;
            }
        }
        this.mjGameData.seats = seats;
        this.setTableData(this.mjGameData);
    }
    /**
     * 刷新换牌推送数据
     * @param {*} data 
     * @memberof GMManager
     */
    setSwapCardData(data: any) {
        cc.log('------换牌推送------' + new Date());
        cc.log(data);
        if (!this.mjGameData || !this.mjGameData.seats) return;
        this.mjGameData.tableBaseVo.swapCardType = data.swapCardType;
        let seats = this.mjGameData.seats;
        data.seats.forEach((seatInfo) => {
            for (let i = 0; i < seats.length; i++) {
                if (seats[i].seatIndex === seatInfo.seatIndex) {
                    seats[i].btState = seatInfo.btState;
                    seats[i].handCardsLen = seatInfo.handCardsLen;
                    seats[i].handCards = seatInfo.handCards;
                    seats[i].swapCards = seatInfo.swapCards;
                    break;
                }
            }
        });
        this.mjGameData.seats = seats;
        this.setTableData(this.mjGameData);
    }
    /**
     * 刷新胡杠碰表态结果推送数据
     * @param {*} data 
     * @memberof GMManager
     */
    setBreakCardData(data: any) {
        cc.log('------胡杠碰表态结果------');
        cc.log(data);
        if (!this.mjGameData) return;
        this.mjGameData.breakSeats = data.breakSeats;
        this.mjGameData.breakState = data.breakState;
        let seats = this.mjGameData.seats;
        data.seats.forEach((seatInfo) => {
            for (let i = 0; i < seats.length; i++) {
                if (seats[i].seatIndex === seatInfo.seatIndex) {
                    seats[i] = seatInfo;
                    break;
                }
            }
        });
        this.mjGameData.seats = seats;
        this.setTableData(this.mjGameData);
    }
    /**
     * 根据游戏id，跳转到游戏场景
     * @memberof GMManager
     */
    turnToGameScene(tableData: MJGameData, replayMJ: number) {
        this.mjGameData = tableData;
        this.replayMJ = replayMJ;
        if (this.mjGameData && this.mjGameData.tableBaseVo) {
            switch (this.mjGameData.tableBaseVo.cfgId) {
                case MJ_Game_Type.GAME_TYPE_XZDD:
                    cc.director.loadScene('XZDD4Scene');
                    break;
                case MJ_Game_Type.GAME_TYPE_SRLF:
                case MJ_Game_Type.GAME_TYPE_SRSF:
                    cc.director.loadScene('XZDD3Scene');
                    break;
                case MJ_Game_Type.GAME_TYPE_LRLF:
                    cc.director.loadScene('XZDD2Scene');
                    break;
                case MJ_Game_Type.GAME_TYPE_MYMJ:
                    cc.director.loadScene('MYMJScene');
                    break;
                case MJ_Game_Type.GAME_TYPE_ZGMJ:
                    if (this.mjGameData.seats.length === 3) {
                        cc.director.loadScene('ZG3MJScene');
                    } else if (this.mjGameData.seats.length === 4) {
                        cc.director.loadScene('ZG4MJScene');
                    } else { }
                    break;
                case MJ_Game_Type.GAME_TYPE_LSMJ:
                    cc.director.loadScene('LSMJScene');
                    break;
                case MJ_Game_Type.GAME_TYPE_NCMJ:
                    cc.director.loadScene('NCMJScene');
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 返回该玩家表态剩余时间(秒)
     * 
     * @export
     * @param {any} sTime 开始时间(毫秒)
     * @param {any} eTime 结束时间(毫秒)
     * @returns 
     */
    getDiffTime(sTime, eTime) {
        let s = Number(sTime);
        let e = Number(eTime);
        let diff = Math.floor((e - s) / 1000);
        if (diff < 0) diff = 0;
        return diff;
    }

    /**
     * 根据玩家accountId获取座位号
     * 
     * @param {string} accountId 
     * @returns {SeatVo} 
     */
    getSeatById(accountId: string): SeatVo {
        let seatData: SeatVo = null;
        if (this.mjGameData && this.mjGameData.seats) {
            for (let i = 0; i < this.mjGameData.seats.length; i++) {
                if (this.mjGameData.seats[i].accountId === accountId) {
                    seatData = this.mjGameData.seats[i];
                    break;
                }
            }
        }
        return seatData;
    }

    /**
     * 根据座位id获取位置索引 
     * 
     * @export
     * @param {number} seatId   seatId位-1的时候，根据accountId返回
     * @param {string} [accountId=''] 
     * @returns 
     */
    getIndexBySeatId(seatId: number, accountId: string = ''): number {
        if (this.mjGameData && this.mjGameData.seats) {
            let seats = this.mjGameData.seats;
            for (let i = 0; i < seats.length; i++) {
                let seat: SeatVo = seats[i];
                if (seatId === -1) {
                    if (seat.accountId !== '0' && seat.accountId === accountId) {
                        return i;
                    }
                } else {
                    if (seat.seatIndex === seatId) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    /**
     * 获取是否在胡杠碰的状态
     * 
     * @returns 
     * @memberof MJ_Play
     */
    getIsBreakCardState(seatInfo: SeatVo): boolean {
        let isBreakCS = false;
        if (seatInfo.breakCard > 0 && seatInfo.breakCardState && seatInfo.breakCardState.length > 0) {
            for (var i = 0; i < seatInfo.breakCardState.length; i++) {
                if (seatInfo.breakCardState[i] === 1) {
                    isBreakCS = true;
                    break;
                }
            }
        }
        return isBreakCS;
    }
    /**
     * 对服务器传过来的三位cardId（花色，点数，索引） 转换为两位的cardId(花色 ，点数),以便于进行计算
     * @param {number[]} cardIds 
     * @memberof GMManager
     */
    getSplitCards(cardIds: number[]): number[] {
        cardIds.sort();
        let tempList = cardIds.map((cardId: number) => {
            return Math.floor(cardId / 10);
        });
        return tempList;
    }
    /**
     * 根据数组切割手牌
     * @export
     * @param {any} cardIdList 牌列表
     * @returns 
     */
    getSplitList(cards: number[]): SortCardData {
        let scData: SortCardData = {
            gangList: [],
            pengList: [],
            duiList: []
        };
        if (!cards || !(cards instanceof Array)) return scData;
        for (let i = 0; i < cards.length; i++) {
            //如果第一张和第二张的(花色和点数都相同)
            if (cards[i + 1] && cards[i] === cards[i + 1]) {
                //如果第二张和第三张的(花色和点数都相同)
                if (cards[i + 2] && cards[i + 2] === cards[i + 1]) {
                    //如果第四张和第三张的(花色和点数都相同)
                    if (cards[i + 3] && cards[i + 2] === cards[i + 3]) {
                        //四张点数相同
                        scData.gangList.unshift(cards[i]);
                        i = i + 3;
                    } else {
                        //三张点数相同
                        scData.pengList.unshift(cards[i]);
                        i = i + 2;
                    }
                } else {
                    //两张点数相同
                    scData.duiList.unshift(cards[i]);
                    i = i + 1;
                }
            } else {
                //单牌
            }
        }
        return scData;
    }
    /**
     * 根据牌唯一Id列表获取排序后的列表
     * 
     * @export
     * @returns {number[]} 
     */
    getSortCardByCardIds(cardIds: number[], unsuit: MJ_Suit = MJ_Suit.SUIT_TYPE_NAN): number[] {
        if (!cardIds || !(cardIds instanceof Array)) return [];
        let wanArray: number[] = [];
        let tongArray: number[] = [];
        let tiaoArray: number[] = [];
        cardIds.forEach((cardId: number) => {
            if (cardId > 100 && cardId <= 200) {
                wanArray.push(cardId);
            } else if (cardId > 200 && cardId <= 300) {
                tongArray.push(cardId);
            } else if (cardId > 300 && cardId <= 400) {
                tiaoArray.push(cardId);
            } else {
                cc.log('牌唯一Id错误');
            }
        });
        wanArray.sort((a, b) => {
            return b - a;
        });
        tongArray.sort((a, b) => {
            return b - a;
        });
        tiaoArray.sort((a, b) => {
            return b - a;
        });

        let array = [];
        switch (unsuit) {//根据定缺排序
            case MJ_Suit.SUIT_TYPE_NAN:
                array.push(...tiaoArray, ...tongArray, ...wanArray);
                break;
            case MJ_Suit.SUIT_TYPE_WAN:
                array.push(...wanArray, ...tiaoArray, ...tongArray);
                break;
            case MJ_Suit.SUIT_TYPE_TONG:
                array.push(...tongArray, ...tiaoArray, ...wanArray);
                break;
            case MJ_Suit.SUIT_TYPE_TIAO:
                array.push(...tiaoArray, ...tongArray, ...wanArray);
                break;
            default:
                break;
        }
        return array;
    }

    /**
     * 获取定缺的花色
     * 
     * @export
     * @param {number} [type=0] 0=最少的花色 1=最多的花色
     * @param {number[]} cardIds 牌id列表
     */
    getDingQueSuit(cardIds: number[], type: number = 0): number {
        if (!cardIds || !(cardIds instanceof Array)) -1;
        let wanArray: number[] = [];
        let tongArray: number[] = [];
        let tiaoArray: number[] = [];
        cardIds.forEach((cardId: number) => {
            if (cardId > 100 && cardId <= 200) {
                wanArray.push(cardId);
            } else if (cardId > 200 && cardId <= 300) {
                tongArray.push(cardId);
            } else if (cardId > 300 && cardId <= 400) {
                tiaoArray.push(cardId);
            } else {
                cc.log('牌唯一Id错误');
            }
        });
        let array = [{ cards: tiaoArray, dq: 2 }, { cards: tongArray, dq: 1 }, { cards: wanArray, dq: 0 }];
        array.sort((a, b) => {
            return a.cards.length - b.cards.length;
        });
        if (array[0].cards.length === array[1].cards.length) {
            let cards_0 = this.getSplitCards(array[0].cards);
            let cards_1 = this.getSplitCards(array[1].cards);
            //移除一个对子牌之后，再移除3同和3连
            this.remove3TL(cards_0);
            this.remove3TL(cards_1);
            this.remove2Tong(cards_0);
            this.remove2Tong(cards_1);
            if (cards_0.length >= cards_1.length) {
                return array[0].dq;
            } else {
                return array[1].dq;
            }
        }
        return array[0].dq;
    }

    /**
     * 对座位列表进行排序,让自己的座位始终在最下方
     * 
     * @param {any} seatList 
     * @returns 
     */
    sortSeatList(seats: SeatVo[]) {
        if (!this.mjGameData) return null;
        let mySeat: SeatVo = this.getSeatById(UDManager.getInstance().mineData.accountId);
        if (mySeat) {
            let tempList: SeatVo[] = [];
            seats.forEach((seatInfo: SeatVo) => {
                let index = 0;
                if (mySeat.seatIndex > seatInfo.seatIndex) {
                    index = seats.length - (mySeat.seatIndex - seatInfo.seatIndex);
                } else {
                    index = Math.abs(mySeat.seatIndex - seatInfo.seatIndex);
                }
                tempList[index] = seatInfo;
            });
            return tempList;
        }
        return seats;
    };

    /**
     * 获取手牌中是否有碰牌中的牌,使其可以杠
     * @memberof GMManager
     */
    private getGangByPeng(pengCards: number[][], cards: number[]): number[] {
        //重组碰牌数组，只取碰的什么牌
        let tempList = pengCards.map((pengCard: number[]) => {
            return Math.floor(pengCard[0] / 10);
        });
        let list = [];
        tempList.forEach(card => {
            for (let i = 0; i < cards.length; i++) {
                if (card === cards[i]) {
                    list.push(card);
                }
            }
        });
        return list;
    }

    /**
    * 获取自己是否打完了定缺的牌
    * 
    * @returns {boolean} 
    * @memberof MJ_Play
    */
    getIsUnSuit(list: number[], unSuit: number, gui?: number): boolean {
        //如果在定缺之前的时候,还没有定缺，都显示打完定缺
        if (this.mjGameData.tableBaseVo.gameState <= MJ_GameState.STATE_TABLE_DINGQUE
            && unSuit === MJ_Suit.SUIT_TYPE_NAN) return true;
        //是否打完打缺
        let min = unSuit * 100;
        let max = min + 100;
        let isHasUnSuit = list.some((id: number) => {
            if (gui) {
                let guiMin = gui * 10;
                let guiMax = guiMin + 4;
                if (id > guiMin && id <= guiMax) return false;
                else return (id > min && id <= max);
            } else {
                return (id > min && id <= max);
            }
        }, this);
        return !isHasUnSuit;
    }

    /**
     * 获取杠牌
     * @param {number[]} cards 
     * @param {SeatVo} seatInfo 
     * @returns 返回的列表是两位数的cardid
     * @memberof GMManager
     */
    getGangList(cards: number[], seatInfo: SeatVo) {
        let gList = [];
        //拷贝和重组牌数组
        let tempCards = this.getSplitCards(cards);
        //获取自己的手牌中，是否有杠牌
        let scData: SortCardData = this.getSplitList(tempCards);
        let agList = this.sortGang(scData.gangList, 2);
        let bgList = [];
        //如果自己有碰牌，看看自己的手牌中是否有碰牌的牌，连起来就有4张牌，也可以杠牌
        if (seatInfo.pengCards && seatInfo.pengCards.length > 0) {
            //获取手牌中是否存在 已经碰牌的牌，如果有，可以杠
            let pList = this.getGangByPeng(seatInfo.pengCards, tempCards);
            bgList = this.sortGang(pList, 1);
        }
        gList = gList.concat(agList, bgList);
        gList.sort();
        return gList;
    }
    /**
     * 
     * @memberof GMManager
     */
    /**
     * 获取乐山麻将，在幺鸡任用的情况下，返回 排除幺鸡的数据，和幺鸡的数量
     * 还要排除 定缺的牌
     * @param {number[]} cards 
     * @param {MJ_Suit} unSuit 
     * @returns 
     * @memberof GMManager
     */
    private getLSExcludeYaoJi(cards: number[], unSuit: MJ_Suit, gui: number = 31) {
        let obj = {
            list: [],
            yaojinum: 0,
        };
        let min = unSuit * 10;
        let max = min + 10;
        cards.forEach(cardId => {
            //如果是幺鸡(花色3，点数1) suit = cardId / 100  point = cardId / 10 % 10
            if (cardId === gui) {
                obj.yaojinum++;
            } else {
                //如果不是幺鸡，并也不是定缺的牌
                if (!(cardId > min && cardId <= max)) {
                    obj.list.push(cardId);
                }
            }
        });
        return obj;
    }

    /**
     * 获取乐山麻将的杠牌
     * @param {number[]} cards 牌 = 手牌 + 摸牌
     * @param {SeatVo} seatInfo 玩家的座位
     * @returns 返回的列表是两位数的cardid
     * @memberof GMManager
     */
    getLSGangList(cards: number[], seatInfo: SeatVo) {
        //拷贝和重组牌数组
        let tempCards = this.getSplitCards(cards);
        //如果是乐山麻将
        let obj = this.getLSExcludeYaoJi(tempCards, seatInfo.unSuit);
        let gangObj = {
            agList: [],//暗杠
            bgList: [],//巴杠
            yj_agList: [],//有幺鸡暗杠
            yj_bgList: [],//有幺鸡巴杠
        };
        //获取自己的手牌中，是否有杠牌
        let scData: SortCardData = this.getSplitList(obj.list);
        gangObj.agList = scData.gangList;
        //如果自己有碰牌，看看自己的手牌中是否有碰牌的牌，连起来就有4张牌，也可以杠牌
        if (seatInfo.pengCards && seatInfo.pengCards.length > 0) {
            //获取手牌中是否存在 已经碰牌的牌，如果有，可以杠
            let pList = this.getGangByPeng(seatInfo.pengCards, obj.list);
            gangObj.bgList = pList;
        }
        /**************上面得到的是正常情况下，可以杠牌的牌*******************/
        //如果乐山麻将开启了 幺鸡任用 的打法
        if (this.mjGameData.tableBaseVo.yaojiReplace === 1) {
            if (obj.yaojinum > 0) {
                //如果自己有碰牌
                if (seatInfo.pengCards && seatInfo.pengCards.length > 0) {
                    seatInfo.pengCards.forEach((peng: number[]) => {
                        gangObj.yj_bgList.push(Math.floor(peng[0] / 10));
                    });
                }
            }
            switch (obj.yaojinum) {
                case 1://一张幺鸡，
                    //手牌中有三张一样的
                    if (scData.pengList && scData.pengList.length > 0) {
                        gangObj.yj_agList = gangObj.yj_agList.concat(scData.pengList);
                    }
                    break;
                case 2://二张幺鸡，
                    //手牌中有三张一样的
                    if (scData.pengList && scData.pengList.length > 0) {
                        gangObj.yj_agList = gangObj.yj_agList.concat(scData.pengList);
                    }
                    //手牌中有二张一样的
                    if (scData.duiList && scData.duiList.length > 0) {
                        gangObj.yj_agList = gangObj.yj_agList.concat(scData.duiList);
                    }
                    break;
                case 3://三张幺鸡，
                    gangObj.yj_agList = obj.list;
                    break;
                default:
                    break;
            }
        }
        let gangList = [];
        let anGangList = this.sortGang(gangObj.agList, 2, false);
        let baGangList = this.sortGang(gangObj.bgList, 1, false);
        let yj_anGangList = this.sortGang(gangObj.yj_agList, 2, true);
        let yj_baGangList = this.sortGang(gangObj.yj_bgList, 1, true);
        gangList = gangList.concat(anGangList, baGangList, yj_anGangList, yj_baGangList);
        return gangList;
    }
    /**
     * 设置杠牌的信息
     * 
     * @param {number[]} list 
     * @param {number} isAnGang 是否是暗杠 1=巴杠 2=暗杠 3=直杠
     * @param {boolean} [isUseYaoJi=false]  是否使用幺鸡
     * @returns 
     * @memberof GMManager
     */
    sortGang(list: number[], isAnGang: number, isUseYaoJi: boolean = false) {
        let sGang = [];
        if (list && list.length > 0) {
            list.forEach((card: number) => {
                let cardId = card > 100 ? card : (card * 10 + 1);
                let gObj: GangData = {
                    cardId: cardId, //由两位数 转 3位数
                    isAnGang: isAnGang, //1=巴杠 2=暗杠 3=直杠
                    isUseYaoJi: isUseYaoJi,
                };
                sGang.push(gObj);
            });
        }
        return sGang;
    }

    /**
     * 获取是否是死觉
     * @param {number} card  牌是由花色和点数组成的两位数
     * @returns true = 是死觉 false = 非死觉
     * @memberof GMManager
     */
    getDieTing(card: number) {
        let tempList = [];
        if (this.mjGameData && this.mjGameData.seats) {
            let seats = this.mjGameData.seats;
            seats.forEach((seatInfo: SeatVo) => {
                //如果有打出的牌
                if (seatInfo.outCard) {
                    tempList = tempList.concat(seatInfo.outCard);
                }
                //如果这个玩家是自己
                if (seatInfo.accountId === UDManager.getInstance().mineData.accountId) {
                    //加入手牌
                    if (seatInfo.handCards) {
                        tempList = tempList.concat(seatInfo.handCards);
                    }
                    //加入摸牌
                    if (seatInfo.moPaiCard) {
                        tempList.push(seatInfo.moPaiCard);
                    }
                }
            });

            tempList = this.getSplitCards(tempList);
            let num = 0;
            tempList.forEach(cardId => {
                if (cardId === card) {
                    num++;
                }
            });
            if (num >= 4) {
                return true;
            } else {
                return false;
            }
        }
    }
    /**
     * 是否是乐山麻将，并开启幺鸡任用
     * @returns 
     * @memberof GMManager
     */
    getLSMJ_IsYaoJi_ByCardId(cardId: number) {
        //如果是乐山麻将
        if (this.mjGameData && this.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_LSMJ
            && this.mjGameData.tableBaseVo.yaojiReplace === 1
            && Math.floor(cardId / 10) === 31) {
            return true;
        }
        return false;
    }
    /**
     * 从a数组中去除B数组中的元素
     * @memberof GMManager
     */
    getDiffAToB(cardIds1: number[], cardIds2: number[]) {
        let list = [];
        cardIds1.forEach(cardid => {
            if (cardIds2.indexOf(cardid) === -1) {
                list.push(cardid);
            }
        });
        return list;
    }
    /***************************************麻将躺牌算法*************************************************************************** */
    /**
     * 根据所选躺拍获取指定听牌，如果选择了多余的牌不能获取到听牌
     * 
     * @param {number[]} cards 手牌
     * @param {number[]} outs 躺出的牌
     * @returns {number[]} 
     * @memberof GMManager
     */
    getTingByTang(handCards: number[], outCards: number[], unSuit: MJ_Suit): number[] {
        if (outCards.length % 3 === 0) return [];
        let tings = this.getTingPai(handCards.slice(), unSuit);
        let cards = this.getSplitCards(handCards);
        let outs = this.getSplitCards(outCards);
        if (tings.length === 0) return [];
        tings.sort();
        //当有定缺时，胡牌花色为2门，当无定缺时,胡牌花色可以为3门
        let maxSuit = unSuit === MJ_Suit.SUIT_TYPE_NAN ? 3 : 2;
        if (outs.length % 3 === 1) {//选出添加一张听牌到躺牌数组中必定满足能够胡牌的
            let result = [];
            if (outs.length === 1) {
                let temp_cards = cards.slice(0);
                //移除数组中的outs[0]
                temp_cards.splice(temp_cards.indexOf(outs[0]), 1);
                //判断是否是6对牌
                if (this.checkAllDui(6, temp_cards)) {
                    result = outs;
                } else {
                    temp_cards.sort();
                    //再移除3同和3连
                    this.remove3TL(temp_cards);
                    if (temp_cards.length === 0) {
                        result = outs;
                    }
                }
                return result;
            } else {//>=4张躺牌
                tings.forEach((card: number) => {
                    let temp_cards = outs.slice(0);
                    temp_cards.push(card);
                    if (this.canHuPaiByGui(temp_cards, maxSuit)) {
                        let new_cards = cards.slice(0);
                        outs.forEach(card => {
                            //移除数组中的card
                            new_cards.splice(new_cards.indexOf(card), 1);
                        });
                        new_cards.sort();
                        //再移除3同和3连
                        this.remove3TL(new_cards);
                        if (new_cards.length === 0) {
                            result.push(card);
                        }
                    }
                }, this);
                //校验是否选择了多余项
                if (this.canRemoveK(outs, result, true, maxSuit)
                    || this.canRemoveS(outs, result, true, maxSuit)
                    || this.canRemoveD(outs, result, false, maxSuit)) {
                    return [];
                } else
                    return result;
            }
        }
        if (outs.length % 3 === 2) {//选出添加一张听牌到躺牌数组中必定满足能够凑成3个一样的牌或者3个同色的顺子
            if (outs.length === 8 || outs.length === 11) return [];
            let result = [];
            tings.forEach((card: number) => {
                let temp_cards = outs.slice(0);
                temp_cards.push(card);
                temp_cards.sort();
                //再移除3同和3连
                this.remove3TL(temp_cards);
                if (temp_cards.length === 0) {
                    let new_cards = cards.slice(0);
                    outs.forEach(card => {
                        //移除数组中的card
                        new_cards.splice(new_cards.indexOf(card), 1);
                    });
                    if (this.canHuPaiByGui(new_cards, maxSuit)) {
                        result.push(card);
                    }
                }
            }, this);
            if (outs.length === 2) {
                if (result.length > 0) {
                    if (outs[0] === outs[1]) {
                        return [];
                    }
                }
                return result;
            } else {
                //校验是否选择了多余项
                if (this.canRemoveK(outs, result, false, maxSuit)
                    || this.canRemoveS(outs, result, false, maxSuit)) {
                    return [];
                } else
                    return result;
            }
        }
    }
    /**
     * 移除数组中的对象
     * 
     * @param {number[]} cards 
     * @param {number} card 
     * @memberof GMManager
     */
    private removeCard(cards: number[], card: number) {
        for (let i = 0; i < cards.length; i++) {
            if (cards[i] === card) {
                cards.splice(i, 1);
                break;
            }
        }
    }
    /**
     * 判断数组对应元素是否相同
     * 
     * @param {number[]} arr1 
     * @param {number[]} arr2 
     * @returns {boolean} 
     * @memberof GMManager
     */
    private isSameArray(arr1: number[], arr2: number[]): boolean {
        if (arr1.length !== arr2.length) return false;
        arr1.sort();
        arr2.sort();
        for (let i = 0; i < arr1.length; i++) {
            let card1 = arr1[i];
            let card2 = arr2[i];
            if (card1 !== card2) {
                return false
            }
        }
        return true;
    }
    /**
     * 根据起始位置找到构成顺子的3张牌的下标
     * 
     * @param {number[]} cards 
     * @param {number} start 
     * @returns 
     * @memberof GMManager
     */
    private getIndexs(cards: number[], start: number) {
        let card1 = cards[start];
        let point = Math.floor(card1 / 10 % 10);
        if (point > 7) return null;
        let card2: number = card1 + 1;
        let card3: number = card1 + 2;
        let index2 = -1;
        let index3 = -1;
        for (let i = start; i < cards.length; i++) {
            if (index2 === -1 && cards[i] === card2) {
                index2 = i;
            }
            if (index3 === -1 && cards[i] === card3) {
                index3 = i;
            }
            if (index2 > -1 && index3 > -1) {
                break;
            }
        }
        if (index2 > -1 && index3 > -1) {
            return [start, index2, index3];
        } else {
            return null;
        }
    }
    /**
     * 去多余项后的数组听牌集合和原始的听牌集合是否相同
     * 
     * @param {number[]} newCards 
     * @param {number[]} result 
     * @param {boolean} checkHu 
     * @returns 
     * @memberof GMManager
     */
    private checkLen(newCards: number[], result: number[], checkHu: boolean, maxSuit: number) {
        let count = 0;
        result.forEach((card: number) => {
            let temp = newCards.slice(0);
            temp.push(card);
            if (checkHu) {
                if (this.canHuPaiByGui(temp, maxSuit)) {
                    count++;
                }
            } else {
                temp.sort();
                this.remove3TL(temp);
                if (temp.length === 0) {
                    count++;
                }
            }
        }, this);
        if (count === result.length) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 是否能够去除多余的3个一样的牌后还能满足和原始听牌一样
     * 
     * @param {number[]} cards 
     * @param {number[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof GMManager
     */
    private canRemoveK(cards: number[], result: number[], checkHu: boolean = true, maxSuit: number = 2): boolean {
        let res = false;
        for (let i = 0; i < cards.length - 2; i++) {
            if (this.isSameArray([cards[i], cards[i], cards[i]], [cards[i], cards[i + 1], cards[i + 2]])) {
                let newCards = cards.slice(0);
                newCards.splice(i, 3);
                if (this.checkLen(newCards, result, checkHu, maxSuit)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }
    /**
     * 是否能够去除3个顺子牌后还能满足和原始听牌一样
     * 
     * @param {number[]} cards 
     * @param {number[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof GMManager
     */
    private canRemoveS(cards: number[], result: number[], checkHu: boolean = true, maxSuit: number = 2): boolean {
        let res = false;
        for (let i = 0; i < cards.length; i++) {
            let indexs = this.getIndexs(cards, i);
            let newCards = [];
            if (indexs && indexs.length === 3) {
                for (let j = 0; j < cards.length; j++) {
                    if (indexs.indexOf(j) === -1) {
                        newCards.push(cards[j]);
                    }
                }
                if (this.checkLen(newCards, result, checkHu, maxSuit)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }
    /**
     * 是否能够去除一个对子后还能满足和原始听牌一样
     * 
     * @param {number[]} cards 
     * @param {number[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof GMManager
     */
    canRemoveD(cards: number[], result: number[], checkHu: boolean = true, maxSuit: number = 2): boolean {
        let res = false;
        for (let i = 0; i < cards.length - 1; i++) {
            if (cards[i] === cards[i + 1]) {
                let newCards = cards.slice(0);
                newCards.splice(i, 2);
                if (this.checkLen(newCards, result, checkHu, maxSuit)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }
    /****************************************************躺牌算法************************************************************** */


    /***********************************听牌算法(赖子)****************************************** */
    /**
    * 获取可以胡的牌，即听牌
    * 
    * @param {number[]} cards 手牌
    * @param {MJ_Suit} unSuit 定缺的花色
    * @param {number} [gui] 鬼牌
    * @returns {number[]} 
    * @memberof GMManager
    */
    getTingPai(cards: number[], unSuit: MJ_Suit, gui?: number): number[] {
        let results: number[] = [];
        if (cards.length % 3 !== 1) return results;
        let temp_cards = this.getSplitCards(cards);
        if (gui) { let guis = this.getSameCards(temp_cards, gui); }
        let list = this.splistToListBySuit(temp_cards);
        //当有定缺时，胡牌花色为2门，当无定缺时,胡牌花色可以为3门
        let maxSuit = unSuit === MJ_Suit.SUIT_TYPE_NAN ? 3 : 2;
        if (list.length > maxSuit || list.length <= 0) return results;
        let checkList: number[] = this.getAllCheck(unSuit, gui);
        checkList.forEach((card) => {
            let isGui = false;
            if (gui) {
                isGui = card === Math.floor(gui / 10) ? true : false;
            }
            if (!isGui) {
                let temps = this.getSplitCards(cards);
                temps.push(card);
                if (this.canHuPaiByGui(temps, maxSuit, gui)) {
                    results.push(card);
                }
            }
        }, this);
        return results;
    }

    /**
     * 判断是否可以胡牌(带鬼)
     * 
     * @param {number[]} cards 
     * @param {number} gui 
     * @returns {boolean} 
     * @memberof GMManager
     */
    canHuPaiByGui(cards: number[], maxSuit: number, gui?: number): boolean {
        if (cards.length % 3 !== 2) return false;
        cards.sort();
        let temp_cards = cards.slice(0);
        let guis = [];
        if (gui) { guis = this.getSameCards(temp_cards, gui); }
        let list = this.splistToListBySuit(temp_cards);
        //判断花色是否超出
        if (list.length > maxSuit || list.length <= 0) return false;
        //判断是否是7对
        if (this.checkAllDui(7, temp_cards, guis)) return true;
        //判断剩余的牌
        let res = this.checkRemainingIsCanHu(list, guis);
        return res;
    }
    /**
     * 判断剩余的牌是否能胡牌
     * @private
     * @param {number[][]} list 
     * @param {number[]} guis 
     * @returns 
     * @memberof GMManager
     */
    private checkRemainingIsCanHu(list: number[][], guis: number[] = []) {
        let res = false;
        switch (list.length) {
            case 1:
                res = this.checkRemaining(list[0], guis);
                break;
            case 2:
                if (guis.length > 0) {
                    res = this.checkRemaining(list[0], guis, list[1]);
                    if (!res) {
                        res = this.checkRemaining(list[1], guis, list[0]);
                    }
                } else {
                    //因为有两种花色,所以将牌可能在两种花色中的任意一种,
                    //包含将对的花色加上添加的鬼牌满足3*n+2,不包含将对的花色加上添加的鬼牌满足3*n
                    if (list[0].length % 3 === 2) {
                        res = this.checkRemaining(list[0], guis, list[1]);
                    } else {
                        res = this.checkRemaining(list[1], guis, list[0]);
                    }
                }
                break;
            case 3:
                if (guis.length > 0) {
                    res = this.checkRemaining(list[0], guis, list[1], list[2]);
                    if (!res) {
                        res = this.checkRemaining(list[1], guis, list[0], list[2]);
                    }
                    if (!res) {
                        res = this.checkRemaining(list[2], guis, list[0], list[1]);
                    }
                } else {
                    //包含将对的花色加上添加的鬼牌满足3*n+2,不包含将对的花色加上添加的鬼牌满足3*n
                    if (list[0].length % 3 === 2) {
                        res = this.checkRemaining(list[0], guis, list[1], list[2]);
                    } else if (list[1].length % 3 === 2) {
                        res = this.checkRemaining(list[1], guis, list[0], list[2]);
                    } else {
                        res = this.checkRemaining(list[2], guis, list[0], list[1]);
                    }
                }
                break;
            default:
                break;
        }
        return res;
    }
    /**
     * 判断duiNum对，是否都是对子牌
     * @private
     * @param {number} duiNum 几对牌(7对、6对)
     * @param {number[]} cards 牌
     * @param {number[]} guis 鬼牌
     * @returns {boolean} 
     * @memberof GMManager
     */
    private checkAllDui(duiNum: number, cards: number[], guis: number[] = []): boolean {
        if ((cards.length + guis.length) !== duiNum * 2) return false;
        let temps = cards.slice(0);
        this.remove2Tong(temps);
        let diffLen = guis.length - temps.length;
        if (diffLen >= 0 && diffLen % 2 === 0) {
            return true;
        }
        return false;
    }
    /**
     * 判断7对
     * 
     * @param {number[]} nums 
     * @returns {boolean} 
     * @memberof GMManager
     */
    private checkQiDui2(nums: number[]): boolean {
        if (nums.length !== 14) return false;
        for (let i = 0; i <= 12; i += 2) {
            if (nums[i] !== nums[i + 1]) {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断移除将牌后剩余的牌是否满足顺子和克子
     * 
     *说明:因为有多种花色,所以将牌可能在每个花色中的任意一种,
     *包含将对的花色加上添加的鬼牌满足3*n+2,不包含将对的花色加上添加的鬼牌满足3*n
     * @private
     * @param {number[]} cardList1 牌列表1
     * @param {number[]} guis 鬼牌列表
     * @param {number[]} [cardList2] 牌列表2
     * @param {number[]} [cardList3] 牌列表3
     * @returns {boolean} true or false
     * @memberof GMManager
     */
    private checkRemaining(cardList1: number[], guis: number[], cardList2?: number[], cardList3?: number[]): boolean {
        let jds = this.extractJD(cardList1, guis);
        let res = false;
        for (let i = 0; i < jds.length; i++) {
            let jdObj = jds[i];
            let cards_0 = cardList1.slice(0);
            let count = jdObj.count;
            //不是两个鬼牌组成的将对,需要先把牌集合中的相同牌移除
            if (count !== 2) {
                for (let j = 0; j < cards_0.length; j++) {
                    //从手牌中移除这个 对子牌
                    if (jdObj.card === cards_0[j]) {
                        cards_0.splice(j, 1);
                        j--;
                        count++;
                    }
                    //对子牌是2张牌
                    if (count === 2) {
                        break;
                    }
                }
            }
            //移除一个对子牌之后，再移除3同和3连
            this.remove3TL(cards_0);
            let needCount = this.remove2LT(cards_0) + jdObj.count;
            if (needCount <= guis.length) {
                if (cardList2) {
                    let lastCount = guis.length - needCount;
                    let cards_2 = cardList2.slice(0);
                    this.remove3TL(cards_2);
                    let count2 = this.remove2LT(cards_2);
                    if (count2 <= lastCount) {
                        if (cardList3) {
                            lastCount = guis.length - needCount;
                            let cards_3 = cardList3.slice(0);
                            this.remove3TL(cards_3);
                            let count3 = this.remove2LT(cards_3);
                            if (count3 <= lastCount) {
                                res = true;
                                break;
                            }
                        } else {
                            res = true;
                            break;
                        }
                    }
                } else {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }
    /**
     * 按花色拆分成多个数组
     * 
     * @param {number[]} cards 
     * @returns {number[][]} 
     * @memberof GMManager
     */
    private splistToListBySuit(cards: number[]): number[][] {
        let res: number[][] = [];
        let wanArray: number[] = [];
        let tongArray: number[] = [];
        let tiaoArray: number[] = [];
        cards.forEach((cardId: number) => {
            if (cardId > 10 && cardId <= 20) {
                wanArray.push(cardId);
            } else if (cardId > 20 && cardId <= 30) {
                tongArray.push(cardId);
            } else if (cardId > 30 && cardId <= 40) {
                tiaoArray.push(cardId);
            } else {
                cc.log('牌唯一Id错误');
            }
        });
        if (wanArray.length > 0) {
            res.push(wanArray);
        }
        if (tongArray.length > 0) {
            res.push(tongArray);
        }
        if (tiaoArray.length > 0) {
            res.push(tiaoArray);
        }
        return res;
    }
    /**
     * 获取除去定缺后所有的牌,排除鬼牌
     * @param {MJ_Suit} unSuit 定缺的花色
     * @param {number} gui 游戏的癞子牌
     * @returns {number[]} 
     * @memberof GMManager
     */
    private getAllCheck(unSuit: MJ_Suit, gui: number): number[] {
        let res = [];
        switch (unSuit) {
            case MJ_Suit.SUIT_TYPE_WAN: res.push(...this.tongs, ...this.tiaos); break;
            case MJ_Suit.SUIT_TYPE_TONG: res.push(...this.wans, ...this.tiaos); break;
            case MJ_Suit.SUIT_TYPE_TIAO: res.push(...this.wans, ...this.tongs); break;
            case MJ_Suit.SUIT_TYPE_NAN: res.push(...this.wans, ...this.tongs, ...this.tiaos); break;
            default: break;
        }
        //如果鬼存在的话， 需要从数组中排除
        if (gui) {
            for (let i = 0; i < res.length; i++) {
                if (gui === res[i]) {
                    res.splice(i, 1);
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 在相同花色的数组中抽取将对
     * 
     * @param {number[]} cards 
     * @returns 
     * @memberof GMManager
     */
    private extractJD(cards: number[], guis: number[]) {
        let res = [];
        let suit = Math.floor(cards[0] / 100);
        let dui_2 = [];//对牌
        let dui_1 = [];//单牌
        cards.sort();
        for (let i = 0; i < cards.length; i++) {
            if (cards[i] === cards[i + 1] && dui_2.indexOf(cards[i]) === -1) {//对牌
                dui_2.push(cards[i]);
            }
            if (dui_1.indexOf(cards[i]) === -1 && dui_2.indexOf(cards[i]) === -1) {//单牌
                dui_1.push(cards[i]);
            }
        }
        dui_2.forEach((card: number) => {
            let obj: JDData = {
                card: card,
                count: 0
            }
            res.push(obj);
        });
        if (guis.length > 0) {//有鬼
            dui_1.forEach((card: number) => {
                let obj: JDData = {
                    card: card,
                    count: 1
                }
                res.push(obj);
            });
        }
        if (guis.length > 1) {//有多只鬼
            let obj: JDData = {
                card: guis[0],
                count: 2
            }
            res.push(obj);
        }
        return res;
    }
    /**
     * 移除所有两张相同的牌
     * @param {number[]} cards 
     * @returns {number[][]} 返回对子集合
     * @memberof GMManager
     */
    private remove2Tong(cards: number[]): number[][] {
        let res: number[][] = [];
        if (cards.length < 2) return res;
        for (let i = 0; i < cards.length - 1; i++) {
            if (cards[i] === cards[i + 1]) {
                res.push(cards.splice(i, 2));
                i--;
            }
        }
        return res;
    }
    /**
     * 从左到右依次移除3同和3连,先3同后3连
     * 
     * @param {number[]} cards 
     * @memberof GMManager
     */
    private remove3TL(cards: number[]) {
        let dans: number[] = [];
        while (cards.length > 2) {
            let card1 = cards[0];
            let card2 = cards[1];
            let card3 = cards[2];
            //直接判断 第二张牌 和 第三张牌 是否和第一张牌相同
            if (card1 === card2 && card2 === card3) {
                cards.splice(0, 3);
            } else {
                let index2 = cards.indexOf(card1 + 1);
                let index3 = cards.indexOf(card1 + 2);
                //如果相连的第二张牌 或 第三张牌 存在，就是3连
                if (index2 > -1 && index3 > -1) {
                    cards.splice(index3, 1);
                    cards.splice(index2, 1);
                    cards.splice(0, 1);
                } else {
                    dans.push(cards.splice(0, 1)[0]);
                }
            }
        }
        cards.splice(0, 0, ...dans);
    }
    /**
     * 移除2连和2同,优先移除2连如果不满足再检验移除2同
     * 
     * @param {number[]} cards 
     * @returns {number} 返回凑整这些牌需要多少个鬼
     * @memberof GMManager
     */
    private remove2LT(cards: number[]): number {
        let danCount = 0;
        let duiCount = 0;
        let lianCount = 0;
        while (cards.length > 1) {
            let card1 = cards[0];
            let index2 = card1 + 1;
            let index3 = card1 + 2;
            //如果相连的第二张牌 或 第三张牌 存在，就是3连
            if (index2 > -1 || index3 > -1) {
                let index = index2 > -1 ? index2 : index3;
                cards.splice(index, 1);
                cards.splice(0, 1);
                lianCount++;
            } else {
                //如果相连 的第二张牌 和第一张牌 相同，就是2同(对子)
                let card2 = cards[1];
                if (card1 === card2) {
                    cards.splice(0, 2)
                    duiCount++;
                } else {//如果不是3连或2同，就是单牌
                    cards.splice(0, 1);
                    danCount++;
                }
            }
        }
        if (cards.length === 1) {
            danCount++;
        }
        return danCount * 2 + duiCount + lianCount;
    }
    /**
     * 找出数组中包含指定对象的集合
     * 
     * @param {number[]} cards 
     * @param {number} card 
     * @returns {number[]} 
     * @memberof GMManager
     */
    private getSameCards(cards: number[], card: number): number[] {
        let removes: number[] = [];
        for (let i = 0; i < cards.length; i++) {
            if (cards[i] === card) {
                removes.push(...cards.splice(i, 1));
                i--;
            }
        }
        return removes;
    }
    /************************************赖子听牌算法****************************************** */

    /**
     * 清空单例对象
     * 
     * @memberof GMManager
     */
    destroySelf(): void {
        this._minScript = null;
        this._gmScript = null;
        this.mjGameData = null;
        this.replayMJ = 0;
        this.replayDataList.length = 0;
        this.isReplayPause = false;
        this.touchTarget = null;
        this.replayRecordId = '0';
    }
}
