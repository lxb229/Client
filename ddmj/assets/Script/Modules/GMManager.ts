import UDManager from "./UDManager";
import { MJ_Suit, MJ_Game_Type } from "./Protocol";
import MJ_Game_Mine from '../SceneScript/Game/MJ_Game_Mine';

/**
 * 将对数据
 */
declare interface JDData {
    /**
     * 组成将对的牌
     */
    card: CardAttrib,
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
    private wans: CardAttrib[] = [];
    private tongs: CardAttrib[] = [];
    private tiaos: CardAttrib[] = [];
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
                    let card: CardAttrib = { suit: i, point: j, cardId: (i - 1) * 36 + (j - 1) * 4 + 1 };
                    switch (i) {
                        case 1: GMManager._instance.wans.push(card); break;
                        case 2: GMManager._instance.tongs.push(card); break;
                        case 3: GMManager._instance.tiaos.push(card); break;
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

    _minScript: MJ_Game_Mine = null;
    /**
     * 刷新游戏桌子对象
     * 
     * @param {TableData} tableData 
     * @memberof GMManager
     */
    setTableData(tableData: MJGameData) {
        this.mjGameData = tableData;
        cc.systemEvent.emit('MJ_GamePush');
    }
    /**
     * 设置报叫的数据消息
     * @param {*} content 
     * @memberof GMManager
     */
    setBaoJiaoData(content: any) {
        //报叫表态座位 seatIndex;
        //报叫结果(0=不报叫,1=报叫)  boaojiaoResult;
        //桌子座位数据 seats;
        this.mjGameData.seats = content.seats;
        cc.systemEvent.emit('MJ_GamePush');
    }
    /**
     * 深度拷贝对象，通过Json转换的过程来实现
     * @param {any} obj 
     * @returns 
     * @memberof GMManager
     */
    private deepCopy(obj) {
        if (obj instanceof Object || obj instanceof Array) {
            return JSON.parse(JSON.stringify(obj));
        } else {
            return obj;
        }
    }
    /**
     * 根据定缺排序
     * 
     * @export
     * @param {number[]} wanArray 万
     * @param {number[]} tongArray 筒
     * @param {number[]} tiaoArray 条
     * @param {MJ_Suit} [unsuit=MJ_Suit.SUIT_TYPE_NAN] 
     */
    private sortCardByUnSuit(wanArray: number[], tongArray: number[], tiaoArray: number[], unsuit: MJ_Suit = MJ_Suit.SUIT_TYPE_NAN) {
        let array = [];
        switch (unsuit) {
            case MJ_Suit.SUIT_TYPE_NAN:
                array = [tiaoArray, tongArray, wanArray];
                break;
            case MJ_Suit.SUIT_TYPE_WAN:
                array = [wanArray, tiaoArray, tongArray];
                break;
            case MJ_Suit.SUIT_TYPE_TONG:
                array = [tongArray, tiaoArray, wanArray];
                break;
            case MJ_Suit.SUIT_TYPE_TIAO:
                array = [tiaoArray, tongArray, wanArray];
                break;
            default:
                break;
        }
        return array;
    }
    /**
     * 根据游戏id，跳转到游戏场景
     * @memberof GMManager
     */
    turnToGameScene() {
        if (this.mjGameData && this.mjGameData.tableBaseVo) {
            switch (this.mjGameData.tableBaseVo.cfgId) {
                case MJ_Game_Type.GAME_TYPE_XZDD:
                    cc.director.loadScene('MJScene');
                    break;
                case MJ_Game_Type.GAME_TYPE_SRLF:
                case MJ_Game_Type.GAME_TYPE_SRSF:
                    cc.director.loadScene('SRMJScene');
                    break;
                case MJ_Game_Type.GAME_TYPE_LRLF:
                    cc.director.loadScene('LRMJScene');
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
                default:
                    break;
            }
        }
    }

    /**
     * 根据数组切割手牌
     * @export
     * @param {any} cardIdList 牌列表
     * @returns 
     */
    getSplitList(cardIdList: number[]): SortCardData {
        let scData: SortCardData = {
            gangList: [],
            pengList: [],
            duiList: []
        };
        if (!cardIdList) return scData;
        if (!(cardIdList instanceof Array)) return scData;

        let list: number[] = this.deepCopy(cardIdList);
        list.sort((a, b) => {
            return a - b;
        });
        for (let i = 0; i < list.length; i++) {
            //第一张和第二张的的唯一Id相邻
            if (list[i + 1]) {
                let card1: CardAttrib = this.getCardById(list[i]);
                let card2: CardAttrib = this.getCardById(list[i + 1]);
                //如果第一张和第二张的(花色和点数都相同)
                if (card1.suit === card2.suit && card1.point === card2.point) {
                    //第二张和第三张的唯一Id相邻
                    if (list[i + 2]) {
                        let card3: CardAttrib = this.getCardById(list[i + 2]);
                        //如果第二张和第三张的(花色和点数都相同)
                        if (card2.suit === card3.suit && card3.point === card2.point) {
                            //第四张和第三张的唯一Id相邻
                            if (list[i + 3] && list[i + 2] + 1 === list[i + 3]) {
                                let card4: CardAttrib = this.getCardById(list[i + 3]);
                                //如果第四张和第三张的(花色和点数都相同)
                                if (card3.suit === card4.suit && card3.point === card4.point) {
                                    //四张点数相同
                                    scData.gangList.unshift(list[i]);
                                    i = i + 3;
                                } else {
                                    //三张点数相同
                                    scData.pengList.unshift(list[i]);
                                    i = i + 2;
                                }
                            } else {
                                //三张点数相同
                                scData.pengList.unshift(list[i]);
                                i = i + 2;
                            }
                        } else {
                            //两张点数相同
                            scData.duiList.unshift(list[i]);
                            i = i + 1;
                        }
                    } else {
                        //两张点数相同
                        scData.duiList.unshift(list[i]);
                        i = i + 1;
                    }
                }
            } else {
                //单牌
            }
        }
        return scData;
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
     * 根据牌唯一id获取牌数据
     * 
     * @export
     * @param {number} cardId 
     * @returns {CardAttrib} 
     */
    getCardById(cardId: number): CardAttrib {
        let card: CardAttrib = {
            cardId: cardId,
            suit: Math.floor((cardId - 1) / 36) + 1,
            point: Math.floor(((cardId - 1) % 36) / 4) + 1,
        };
        return card;
    }
    /**
     * 根据花色点数生成牌的对象
     * @param {number} suit 
     * @param {number} point 
     * @returns 
     * @memberof GMManager
     */
    getCardBySP(suit: number, point: number): CardAttrib {
        let card: CardAttrib = {
            suit: suit,
            point: point,
            cardId: (suit - 1) * 36 + (point - 1) * 4 + 1
        };
        return card;
    }
    /**
     * 根据牌唯一Id列表获取排序后的列表
     * 
     * @export
     * @returns {number[]} 
     */
    getSortCardByCardIds(cardIds: number[], unsuit: MJ_Suit = MJ_Suit.SUIT_TYPE_NAN): number[] {
        if (!cardIds) return [];
        if (!(cardIds instanceof Array)) return [];
        let wanArray: number[] = [];
        let tongArray: number[] = [];
        let tiaoArray: number[] = [];
        cardIds.forEach((cardId: number) => {
            if (cardId > 0 && cardId <= 36) {
                wanArray.push(cardId);
            } else if (cardId > 36 && cardId <= 72) {
                tongArray.push(cardId);
            } else if (cardId > 72 && cardId <= 108) {
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
        let array = this.sortCardByUnSuit(wanArray, tongArray, tiaoArray, unsuit);
        return [...array[0], ...array[1], ...array[2]];
    }

    /**
     * 获取定缺的花色
     * 
     * @export
     * @param {number} [type=0] 0=最少的花色 1=最多的花色
     * @param {number[]} cardIds 牌id列表
     */
    getDingQueSuit(cardIds: number[], type: number = 0): number {
        if (!cardIds) -1;
        if (!(cardIds instanceof Array)) -1;
        let wanArray: number[] = [];
        let tongArray: number[] = [];
        let tiaoArray: number[] = [];
        cardIds.forEach((cardId: number) => {
            if (cardId > 0 && cardId <= 36) {
                wanArray.push(cardId);
            } else if (cardId > 36 && cardId <= 72) {
                tongArray.push(cardId);
            } else if (cardId > 72 && cardId <= 108) {
                tiaoArray.push(cardId);
            } else {
                cc.log('牌唯一Id错误');
            }
        });
        if (wanArray.length === 0) {
            return 0;
        }
        if (tongArray.length === 0) {
            return 1;
        }
        if (tiaoArray.length === 0) {
            return 2;
        }
        //排序
        let array = [tiaoArray, tongArray, wanArray];
        array.sort((a, b) => {
            if (type === 0) {
                return a.length - b.length;
            } else {
                return b.length - a.length;
            }
        });
        //取出第一个牌，获取目标类型
        let tCardId = array[0][0];
        if (tCardId > 0 && tCardId <= 36) {
            return 0;
        } else if (tCardId > 36 && tCardId <= 72) {
            return 1;
        } else if (tCardId > 72 && tCardId <= 108) {
            return 2;
        } else {
            cc.log('牌唯一Id错误');
            return -1;
        }
    }

    /**
     * 对座位列表进行排序,让自己的座位始终在最下方
     * 
     * @param {any} seatList 
     * @returns 
     */
    sortSeatList(seats) {
        if (!this.mjGameData) return null;
        let mySeat: SeatVo = this.getSeatById(UDManager.getInstance().mineData.accountId);
        if (mySeat) {
            let tempList: SeatVo[] = [];
            for (let i = 0; i < seats.length; i++) {
                let seatInfo: SeatVo = seats[i];
                if (seatInfo) {
                    let index = 0;
                    if (mySeat.seatIndex > seatInfo.seatIndex) {
                        index = seats.length - (mySeat.seatIndex - seatInfo.seatIndex);
                    } else {
                        index = Math.abs(mySeat.seatIndex - seatInfo.seatIndex);
                    }
                    tempList[index] = seatInfo;
                }
            }
            return tempList;
        }
        return null;
    };

    /**
     * 根据cardIds1中的cardId查找cardIds2中是否存在
     * 
     * @export
     * @param {number[]} cardIds1 碰牌
     * @param {number[]} cardIds2 手牌
     */
    getCardIdsByCardId(cardIds1: number[][], cardIds2: number[]): number[] {
        let list = [];
        for (let i = 0; i < cardIds1.length; i++) {
            let card1 = this.getCardById(cardIds1[i][0]);
            for (let j = 0; j < cardIds2.length; j++) {
                let card2 = this.getCardById(cardIds2[j]);
                if (card1.suit === card2.suit && card1.point === card2.point) {
                    list.push(cardIds2[j]);
                    break;
                }
            }
        }
        return list;
    }
    /**
     * 获取杠牌
     * @param {number[]} cards 
     * @param {SeatVo} seatInfo 
     * @memberof GMManager
     */
    getGangList(cards: number[], seatInfo: SeatVo) {
        let gList = [];
        //获取自己的手牌中，是否有杠牌
        let scData: SortCardData = this.getSplitList(cards);
        let agList = this.sortGang(scData.gangList, 2);
        let bgList = [];
        //如果自己有碰牌，看看自己的手牌中是否有碰牌的牌，连起来就有4张牌，也可以杠牌
        if (seatInfo.pengCards && seatInfo.pengCards.length > 0) {
            //获取手牌中是否存在 已经碰牌的牌，如果有，可以杠
            let pList = this.getCardIdsByCardId(seatInfo.pengCards, cards);
            bgList = this.sortGang(pList, 1);
        }
        gList = gList.concat(agList, bgList);
        gList.sort((a, b) => {
            return a.cardId - b.cardId;
        });
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
    getLSExcludeYaoJi(cards: number[], unSuit: MJ_Suit) {
        let obj = {
            list: [],
            yaojinum: 0,
        };
        cards.forEach((cardId: number) => {
            let card = this.getCardById(cardId);
            //如果是幺鸡
            if (card.suit === MJ_Suit.SUIT_TYPE_TIAO && card.point === 1) {
                obj.yaojinum++;
                //如果是乐山麻将，并未使用了幺鸡任用,也不是定缺的牌
                if (this.mjGameData.tableBaseVo.yaojiReplace === 0 && card.suit !== unSuit) {
                    obj.list.push(cardId);
                }
            } else {
                //如果不是幺鸡，并也不是定缺的牌
                if (card.suit !== unSuit) {
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
     * @returns 
     * @memberof GMManager
     */
    getLSGangList(cards: number[], seatInfo: SeatVo) {
        //如果是乐山麻将
        let obj = this.getLSExcludeYaoJi(cards, seatInfo.unSuit);
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
            let pList = this.getCardIdsByCardId(seatInfo.pengCards, obj.list);
            gangObj.bgList = pList;
        }
        /**************上面得到的是正常情况下，可以杠牌的牌*******************/
        //如果乐山麻将开启了 幺鸡任用 的打法
        if (this.mjGameData.tableBaseVo.yaojiReplace === 1) {
            if (obj.yaojinum > 0) {
                //如果自己有碰牌
                if (seatInfo.pengCards && seatInfo.pengCards.length > 0) {
                    seatInfo.pengCards.forEach((cards: number[]) => {
                        gangObj.yj_bgList.push(cards[0]);
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
     * @param {number[]} list 
     * @param {boolean} isUseYaoJi 是否使用幺鸡
     * @param {boolean} isAnGang  是否是暗杠 1=巴杠 2=暗杠 3=直杠
     * @returns 
     * @memberof GMManager
     */
    sortGang(list: number[], isAnGang: number, isUseYaoJi: boolean = false) {
        let sGang = [];
        if (list && list.length > 0) {
            list.forEach((cardId: number) => {
                let gObj: GangData = {
                    cardId: cardId,
                    isAnGang: isAnGang,
                    isUseYaoJi: isUseYaoJi,
                };
                sGang.push(gObj);
            });
        }
        return sGang;
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
            this.mjGameData.seats.forEach((seat: SeatVo) => {
                if (seat.accountId === accountId) {
                    seatData = seat;
                }
            }, this);
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
     * 获取游戏手牌张数
     * @memberof GMManager
     */
    getGameCardNum() {
        let gameCardNum = 13;
        let index = this.mjGameData.tableBaseVo.ruleShowDesc.indexOf('张手牌');
        if (index !== -1) {
            let num = this.mjGameData.tableBaseVo.ruleShowDesc.substring(index - 1, index);
            gameCardNum = Number(num);
        }
        return gameCardNum;
    }
    /**
     * 获取是否是死觉
     * 
     * @export
     * @param {CardAttrib} card 
     */
    getDieTing(card: CardAttrib) {
        let tempList = [];
        if (this.mjGameData && this.mjGameData.seats) {
            let seats = this.mjGameData.seats;
            for (let i = 0; i < seats.length; i++) {
                let seat = seats[i];
                if (seat.outCard) {
                    tempList = tempList.concat(seat.outCard);
                }
                if (seat.accountId === UDManager.getInstance().mineData.accountId) {
                    if (seat.handCards) {
                        tempList = tempList.concat(seat.handCards);
                    }
                    if (seat.moPaiCard) {
                        tempList.push(seat.moPaiCard);
                    }
                }
            }
        }
        let num = 0;
        for (let i = 0; i < tempList.length; i++) {
            let tempCard = this.getCardById(tempList[i]);
            if (card.suit === tempCard.suit && card.point === tempCard.point) {
                num++;
            }
        }

        if (num >= 4) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 获取两个数组中 不同的的数据
     * @param {any} arr1 数组1
     * @param {any} arr2 数组2
     * @returns 
     * @memberof GMManager
     */
    getDiffArr(arr1, arr2) {
        let diff = arr1.concat(arr2).sort((a, b) => {
            return a - b;//从小到大排序
        }).filter((valu, index, arr) => {
            //计算不同的项放入diff
            return arr.indexOf(valu) === arr.lastIndexOf(valu);
        });
        return diff;
    }
    /**
     * 获取两个数组中 相同的的数据
     * @param {any} arr1 数组1
     * @param {any} arr2 数组2
     * @memberof GMManager
     */
    getSameArr(arr1, arr2) {
        let same = arr1.filter((valu, index, arr) => {
            return arr2.includes(valu);
        });
        same.sort((a, b) => {
            return a - b;
        });
        same = this.getNoRepeat(same);
        return same;
    }
    /**
     * 去掉数组中重复的值
     * @param {any} arr 
     * @returns 
     * @memberof GMManager
     */
    getNoRepeat(arr) {
        let result = [];
        for (let i = 0; i < arr.length; i++) {
            if (result.indexOf(arr[i]) === -1) {
                result.push(arr[i]);
            }
        }
        return result;
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
    /**
     * 是否是乐山麻将，并开启幺鸡任用
     * @returns 
     * @memberof GMManager
     */
    getLSMJ_IsYaoJi_ByCardId(cardId: number) {
        let card = this.getCardById(cardId);
        //如果是乐山麻将
        if (this.mjGameData && this.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_LSMJ
            && this.mjGameData.tableBaseVo.yaojiReplace === 1
            && card.suit === MJ_Suit.SUIT_TYPE_TIAO && card.point === 1) {
            return true;
        }
        return false;
    }
    /***************************************麻将算法*************************************************************************** */
    /**
     * 获取可以胡的牌，即听牌
     * 
     * @param {CardAttrib[]} cards 
     * @returns {CardAttrib[]} 
     * @memberof ChildClass
     */
    getTingPai(cards: CardAttrib[]): CardAttrib[] {
        let results: CardAttrib[] = [];
        let checkList: CardAttrib[] = [];
        let suits = this.getSuits(cards);
        if (suits.length > 2) return [];
        suits.forEach((suit) => {
            switch (suit) {
                case 1: checkList = checkList.concat(this.wans); break;
                case 2: checkList = checkList.concat(this.tongs); break;
                case 3: checkList = checkList.concat(this.tiaos); break;
                default: break;
            }
        }, this);
        checkList.forEach((card) => {
            let temps = cards.slice(0);
            temps.push(card);
            if (this.canHuPai(temps)) {
                results.push(card);
            }
        }, this);
        return results;
    }

    /**
     * 根据所选躺拍获取指定听牌，如果选择了多余的牌不能获取到听牌
     * 
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib[]} outs 
     * @returns {CardAttrib[]} 
     * @memberof ChildClass
     */
    getTingByTang(cards: CardAttrib[], outs: CardAttrib[]): CardAttrib[] {
        if (outs.length % 3 === 0) return [];
        this.sortCards(cards);
        this.sortCards(outs);
        let tings = this.getTingPai(cards);
        this.sortCards(tings);
        if (outs.length % 3 === 1) {//选出添加一张听牌到躺牌数组中必定满足能够胡牌的
            let result = [];
            if (outs.length === 1) {
                let newCards = cards.slice(0);
                this.removeCard(newCards, outs[0]);
                let nums = newCards.map((card) => {
                    return card.suit * 10 + card.point;
                }, this);
                nums.sort();
                if (this.checkSixDui(nums)) {
                    result = outs;
                } else {
                    if (this.checkRemaining(nums)) {
                        result = outs;
                    }
                }
                return result;
            } else {//>=4张躺牌
                tings.forEach((card: CardAttrib) => {
                    let temp = outs.slice(0);
                    temp.push(card);
                    if (this.canHuPai(temp)) {
                        result.push(card);
                    }
                }, this);
                //校验是否选择了多余项
                if (this.canRemoveK(outs, result, true) || this.canRemoveS(outs, result, true) || this.canRemoveD(outs, result, false)) {
                    return [];
                } else
                    return result;
            }
        }
        if (outs.length % 3 === 2) {//选出添加一张听牌到躺牌数组中必定满足能够凑成3个一样的牌或者3个同色的顺子
            if (outs.length === 8 || outs.length === 11) return [];
            let result = [];
            tings.forEach((card: CardAttrib) => {
                let temp = outs.slice(0);
                temp.push(card);
                let nums = temp.map((card) => {
                    return card.suit * 10 + card.point;
                }, this);
                nums.sort();
                if (this.checkRemaining(nums)) {
                    result.push(card);
                }
            }, this);
            if (outs.length === 2) {
                if (result.length > 0) {
                    if (this.isSameCard(outs[0], outs[1])) {
                        return [];
                    }
                }
                return result;
            } else {
                //校验是否选择了多余项
                if (this.canRemoveK(outs, result, false) || this.canRemoveS(outs, result, false)) {
                    return [];
                } else
                    return result;
            }
        }
    }

    /**
     * 获取乐山可以胡的牌，即听牌
     * 
     * @param {CardAttrib[]} cards 
     * @returns {CardAttrib[]} 
     * @memberof ChildClass
     */
    getLSTingPai(cards: CardAttrib[]): CardAttrib[] {
        let results: CardAttrib[] = [];
        return results;
    }
    /**
     * 判断是否可以胡牌
     * 
     * @param {CardAttrib[]} cards 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    private canHuPai(cards: CardAttrib[]): boolean {
        if (cards.length % 3 !== 2) return false;
        if (this.getSuits(cards).length > 2) return false;
        if (cards.length === 2) {
            if (cards[0].point === cards[1].point && cards[0].suit === cards[1].suit) return true;
            else return false;
        } else {//5,8,11,14
            let nums = cards.map((card) => {
                return card.suit * 10 + card.point;
            }, this);
            nums.sort();
            if (this.checkQiDui(nums)) return true;
            let dups = this.getDuplicate(nums);
            for (let i = 0; i < dups.length; i++) {
                let num = dups[i];
                let temps = nums.slice(0);
                for (let i = 0; i < 2; i++) {
                    let index = temps.indexOf(num);
                    temps.splice(index, 1);
                }
                if (this.checkRemaining(temps)) return true;
            }
            return false;
        }
    }

    /**
     * 检查花色
     * 
     * @param {CardAttrib[]} cards 
     * @returns {number} 
     * @memberof ChildClass
     */
    private getSuits(cards: CardAttrib[]): number[] {
        let suits: number[] = [];
        cards.forEach((card) => {
            if (suits.indexOf(card.suit) === -1) {
                suits.push(card.suit);
            }
        }, this);
        return suits;
    }

    /**
     * 判断移除将牌后剩余的牌是否满足顺子和克子，通过递归移除法验证，当剩余牌为0是返回能胡牌，反之则不能胡牌
     * 余牌数量不为0是必定是3的倍数，余牌是排序过的从小到大
     * 
     * @param {number[]} nums 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    private checkRemaining(nums: number[]): boolean {
        if (nums.length === 0) return true;
        if (nums[0] === nums[1] && nums[1] === nums[2]) {
            let temps = nums.slice(3);
            return this.checkRemaining(temps);
        } else {
            if (nums.indexOf(nums[0] + 1) !== -1 && nums.indexOf(nums[0] + 2) !== -1) {
                let temps = nums.slice(0);
                let remove = 0, index = 0;
                for (let i = 0; i < 3; i++) {
                    remove = temps.splice(index, 1)[0];
                    index = temps.indexOf(remove + 1);
                }
                return this.checkRemaining(temps);
            }
            return false;
        }
    }

    /**
     * 找出可以当将牌的重复项
     * 
     * @param {number[]} nums 
     * @returns {number[]} 
     * @memberof ChildClass
     */
    private getDuplicate(nums: number[]): number[] {
        let result: number[] = [];
        nums.forEach((num) => {
            if (nums.indexOf(num) !== nums.lastIndexOf(num) && result.indexOf(num) === -1)
                result.push(num);
        })
        return result;
    }

    /**
    * 判断6对
    * 
    * @param {number[]} nums 
    * @returns {boolean} 
    * @memberof ChildClass
    */
    checkSixDui(nums: number[]): boolean {
        if (nums.length !== 12) return false;
        for (let i = 0; i < 10; i += 2) {
            if (nums[i] !== nums[i + 1]) {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断7对
     * 
     * @param {number[]} nums 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    private checkQiDui(nums: number[]): boolean {
        if (nums.length !== 14) return false;
        for (let i = 0; i <= 12; i += 2) {
            if (nums[i] !== nums[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 按照花色和点数排序
     * 
     * @param {CardAttrib[]} cards 
     * @memberof ChildClass
     */
    private sortCards(cards: CardAttrib[]) {
        cards.sort((a: CardAttrib, b: CardAttrib) => {
            if (a.suit > b.suit) {
                return 1;
            } else if (a.suit < b.suit) {
                return -1;
            } else {
                return a.point - b.point;
            }
        });
    }
    /**
     * 判断元素是否相同
     * 
     * @param {CardAttrib} card1 
     * @param {CardAttrib} card2 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    isSameCard(card1: CardAttrib, card2: CardAttrib): boolean {
        if (card1.suit === card2.suit && card1.point === card2.point) {
            return true;
        } else return false;
    }
    /**
     * 移除数组中的对象
     * 
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib} card 
     * @memberof ChildClass
     */
    removeCard(cards: CardAttrib[], card: CardAttrib) {
        for (let i = 0; i < cards.length; i++) {
            if (this.isSameCard(cards[i], card)) {
                cards.splice(i, 1);
                break;
            }
        }
    }
    /**
     * 判断数组对应元素是否相同
     * 
     * @param {CardAttrib[]} arr1 
     * @param {CardAttrib[]} arr2 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    isSameArray(arr1: CardAttrib[], arr2: CardAttrib[]): boolean {
        if (arr1.length !== arr2.length) return false;
        this.sortCards(arr2);
        this.sortCards(arr1);
        for (let i = 0; i < arr1.length; i++) {
            let card1 = arr1[i];
            let card2 = arr2[i];
            if (!this.isSameCard(card1, card2)) {
                return false
            }
        }
        return true;
    }
    /**
     * 根据起始位置找到构成顺子的3张牌的下标
     * 
     * @param {CardAttrib[]} cards 
     * @param {number} start 
     * @returns 
     * @memberof ChildClass
     */
    private getIndexs(cards: CardAttrib[], start: number) {
        let card1 = cards[start];
        if (card1.point > 7) return null;
        let card2: CardAttrib = this.getCardBySP(card1.suit, card1.point + 1);
        let card3: CardAttrib = this.getCardBySP(card1.suit, card1.point + 2);
        let index2 = -1;
        let index3 = -1;
        for (let i = start; i < cards.length; i++) {
            if (index2 === -1 && this.isSameCard(cards[i], card2)) {
                index2 = i;
            }
            if (index3 === -1 && this.isSameCard(cards[i], card3)) {
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
     * @param {CardAttrib[]} newCards 
     * @param {CardAttrib[]} result 
     * @param {boolean} checkHu 
     * @returns 
     * @memberof ChildClass
     */
    private checkLen(newCards: CardAttrib[], result: CardAttrib[], checkHu: boolean) {
        let count = 0;
        result.forEach((card: CardAttrib) => {
            let temp = newCards.slice(0);
            temp.push(card);
            if (checkHu) {
                if (this.canHuPai(temp)) {
                    count++;
                }
            } else {
                let nums = temp.map((card) => {
                    return card.suit * 10 + card.point;
                }, this);
                nums.sort();
                if (this.checkRemaining(nums)) {
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
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    private canRemoveK(cards: CardAttrib[], result: CardAttrib[], checkHu: boolean = true): boolean {
        let res = false;
        for (let i = 0; i < cards.length - 2; i++) {
            if (this.isSameArray([cards[i], cards[i], cards[i]], [cards[i], cards[i + 1], cards[i + 2]])) {
                let newCards = cards.slice(0);
                newCards.splice(i, 3);
                if (this.checkLen(newCards, result, checkHu)) {
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
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    private canRemoveS(cards: CardAttrib[], result: CardAttrib[], checkHu: boolean = true): boolean {
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
                if (this.checkLen(newCards, result, checkHu)) {
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
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canRemoveD(cards: CardAttrib[], result: CardAttrib[], checkHu: boolean = true): boolean {
        let res = false;
        for (let i = 0; i < cards.length - 1; i++) {
            if (this.isSameCard(cards[i], cards[i + 1])) {
                let newCards = cards.slice(0);
                newCards.splice(i, 2);
                if (this.checkLen(newCards, result, checkHu)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }
    /****************************************************正常听牌算法************************************************************** */


    /***********************************赖子听牌算法****************************************** */
    /**
     * 移除所有两张相同的牌
     * 
     * @param {CardAttrib[]} cards 
     * @returns {CardAttrib[][]} 返回对子集合
     * @memberof ChildClass
     */
    private remove2Tong(cards: CardAttrib[]): CardAttrib[][] {
        let res: CardAttrib[][] = [];
        if (cards.length < 2) return res;
        for (let i = 0; i < cards.length - 1; i++) {
            if (this.isSameCard(cards[i], cards[i + 1])) {
                res.push(cards.splice(i, 2));
                i--;
            }
        }
        return res;
    }
    /**
     * 按花色拆分成多个数组
     * 
     * @param {CardAttrib[]} cards 
     * @returns {CardAttrib[][]} 
     * @memberof ChildClass
     */
    private splistToListBySuit(cards: CardAttrib[]): CardAttrib[][] {
        let res: CardAttrib[][] = [];
        let list1: CardAttrib[] = [];
        let list2: CardAttrib[] = [];
        let list3: CardAttrib[] = [];
        cards.forEach((card: CardAttrib) => {
            switch (card.suit) {
                case 1:
                    list1.push(card);
                    break;
                case 2:
                    list2.push(card);
                    break;
                case 3:
                    list3.push(card);
                    break;
                default:
                    break;
            }
        }, this)
        if (list1.length > 0) {
            res.push(list1);
        }
        if (list2.length > 0) {
            res.push(list2);
        }
        if (list3.length > 0) {
            res.push(list3);
        }
        return res;
    }
    /**
     * 在相同花色的数组中抽取将对
     * 
     * @param {CardAttrib[]} cards 
     * @returns 
     * @memberof ChildClass
     */
    private extractJD(cards: CardAttrib[], guis: CardAttrib[]) {
        let res = [];
        let suit = cards[0].suit;
        let dui_2 = [];//对牌
        let dui_1 = [];//单牌
        let points = cards.map((card: CardAttrib) => {
            return card.point;
        });
        points.forEach((point: number) => {
            if (points.indexOf(point) !== points.lastIndexOf(point) && dui_2.indexOf(point) === -1) {//对牌
                dui_2.push(point);
            }
            if (dui_1.indexOf(point) === -1 && dui_2.indexOf(point) === -1) {//单牌
                dui_1.push(point);
            }
        });
        dui_2.forEach((point: number) => {
            let obj: JDData = {
                card: { suit: suit, point: point, cardId: (suit - 1) * 36 + (point - 1) * 4 + 1 },
                count: 0
            }
            res.push(obj);
        });
        if (guis.length > 0) {//有鬼
            dui_1.forEach((point: number) => {
                let obj: JDData = {
                    card: { suit: suit, point: point, cardId: (suit - 1) * 36 + (point - 1) * 4 + 1 },
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
     * 在集合中找到指定对象
     * 
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib} card 
     * @returns 返回对象下标,没找到返回-1
     * @memberof ChildClass
     */
    findCard(cards: CardAttrib[], card: CardAttrib) {
        let index = -1;
        for (let i = 0; i < cards.length; i++) {
            if (this.isSameCard(cards[i], card)) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * 从左到右依次移除3同和3连,先3同后3连
     * 
     * @param {CardAttrib[]} cards 
     * @memberof ChildClass
     */
    remove3TL(cards: CardAttrib[]) {
        let dans: CardAttrib[] = [];
        while (cards.length > 2) {
            let card1 = cards[0];
            let card2 = cards[1];
            let card3 = cards[2];
            if (this.isSameCard(card1, card2) && this.isSameCard(card2, card3)) {
                cards.splice(0, 3);
            } else {
                let point = card1.point;
                let index2 = this.findCard(cards, this.getCardBySP(card1.suit, card1.point + 1));
                let index3 = this.findCard(cards, this.getCardBySP(card1.suit, card1.point + 2));
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
     * @param {CardAttrib[]} cards 
     * @returns {number} 返回凑整这些牌需要多少个鬼
     * @memberof ChildClass
     */
    remove2LT(cards: CardAttrib[]): number {
        let danCount = 0;
        let duiCount = 0;
        let lianCount = 0;
        while (cards.length > 1) {
            let card1 = cards[0];
            let index2 = this.findCard(cards, this.getCardBySP(card1.suit, card1.point + 1));
            let index3 = this.findCard(cards, this.getCardBySP(card1.suit, card1.point + 2));
            if (index2 > -1 || index3 > -1) {
                let index = index2 > -1 ? index2 : index3;
                cards.splice(index, 1);
                cards.splice(0, 1);
                lianCount++;
            } else {
                let card2 = cards[1];
                if (this.isSameCard(card1, card2)) {
                    cards.splice(0, 2)
                    duiCount++;
                } else {
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
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib} card 
     * @returns {CardAttrib[]} 
     * @memberof ChildClass
     */
    getSameCards(cards: CardAttrib[], card: CardAttrib): CardAttrib[] {
        let removes: CardAttrib[] = [];
        for (let i = 0; i < cards.length; i++) {
            if (this.isSameCard(cards[i], card)) {
                removes.push(...cards.splice(i, 1));
                i--;
            }
        }
        return removes;
    }
    /**
     * 判断是否可以胡牌(带鬼)
     * 
     * @param {CardAttrib[]} cards 
     * @param {CardAttrib} gui 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canHuPaiByGui(cards: CardAttrib[], gui: CardAttrib): boolean {
        if (cards.length % 3 !== 2) return false;
        this.sortCards(cards);
        let temp_cards = cards.slice(0);
        let guis = this.getSameCards(temp_cards, gui);
        let list = this.splistToListBySuit(temp_cards);
        if (list.length > 2 || list.length <= 0) return false;
        if (temp_cards.length + guis.length === 14) {//判断是否是7对
            let temps = temp_cards.slice(0);
            this.remove2Tong(temps);
            let diffLen = guis.length - temps.length;
            if (diffLen >= 0 && diffLen % 2 === 0) {
                return true;
            }
        }
        if (list.length === 1) {
            //因为只有一种花色,所以将牌必然在其中
            let jds = this.extractJD(list[0], guis);
            let res = false;
            for (let i = 0; i < jds.length; i++) {
                let jdObj = jds[i];
                let cards_0 = list[0].slice(0);
                let count = jdObj.count;
                if (count !== 2) {//不是两个鬼牌组成的将对,需要先把牌集合中的相同牌移除
                    for (let j = 0; j < cards_0.length; j++) {
                        if (this.isSameCard(jdObj.card, cards_0[j])) {
                            cards_0.splice(j, 1);
                            j--;
                            count++;
                        }
                        if (count === 2) {
                            break;
                        }
                    }
                }
                this.remove3TL(cards_0);
                let needCount = this.remove2LT(cards_0) + jdObj.count;
                if (needCount <= guis.length) {
                    res = true;
                    break;
                }
            }
            return res;
        }
        else {
            //list.length === 2
            //因为有两种花色,所以将牌可能在两种花色中的任意一种,包含将对的花色加上添加的鬼牌满足3*n+2,不包含将对的花色加上添加的鬼牌满足3*n
            //当将对在list[0]中
            let jds = this.extractJD(list[0], guis);
            let res_1 = false;
            for (let i = 0; i < jds.length; i++) {
                let jdObj = jds[i];
                let cards_1 = list[0].slice(0);
                let count = jdObj.count;
                if (count !== 2) {//不是两个鬼牌组成的将对,需要先把牌集合中的相同牌移除
                    for (let j = 0; j < cards_1.length; j++) {
                        if (this.isSameCard(jdObj.card, cards_1[j])) {
                            cards_1.splice(j, 1);
                            j--;
                            count++;
                        }
                        if (count === 2) {
                            break;
                        }
                    }
                }
                this.remove3TL(cards_1);
                let count1 = this.remove2LT(cards_1) + jdObj.count;
                if (count1 <= guis.length) {
                    let lastCount = guis.length - count1;
                    let cards_2 = list[1].slice(0);
                    this.remove3TL(cards_2);
                    let count2 = this.remove2LT(cards_2);
                    if (count2 <= lastCount) {
                        res_1 = true;
                        break;
                    }
                }
            }
            if (res_1) {
                return res_1;
            } else {
                //当将对在list[1]中
                let jds = this.extractJD(list[1], guis);
                let res_2 = false;
                for (let i = 0; i < jds.length; i++) {
                    let jdObj = jds[i];
                    let cards_2 = list[1].slice(0);
                    let count = jdObj.count;
                    if (count !== 2) {//不是两个鬼牌组成的将对,需要先把牌集合中的相同牌移除
                        for (let j = 0; j < cards_2.length; j++) {
                            if (this.isSameCard(jdObj.card, cards_2[j])) {
                                cards_2.splice(j, 1);
                                j--;
                                count++;
                            }
                            if (count === 2) {
                                break;
                            }
                        }
                    }
                    this.remove3TL(cards_2);
                    let count2 = this.remove2LT(cards_2) + jdObj.count;
                    if (count2 <= guis.length) {
                        let lastCount = guis.length - count2;
                        let cards_1 = list[0].slice(0);
                        this.remove3TL(cards_1);
                        let count1 = this.remove2LT(cards_1);
                        if (count1 <= lastCount) {
                            res_2 = true;
                            break;
                        }
                    }
                }
                return res_2;
            }
        }
    }
    /**
     * 获取除去定缺后所有的牌,排除鬼牌
     * 
     * @param {CardAttrib} gui 
     * @param {number} suit 
     * @returns {CardAttrib[]} 
     * @memberof ChildClass
     */
    getAllCheck(gui: CardAttrib, suit: number): CardAttrib[] {
        let res = [];
        switch (suit) {
            case 1: res.push(...this.tongs, ...this.tiaos); break;
            case 2: res.push(...this.wans, ...this.tiaos); break;
            case 3: res.push(...this.wans, ...this.tongs); break;
            default: break;
        }
        for (let i = 0; i < res.length; i++) {
            if (this.isSameCard(gui, res[i])) {
                res.splice(i, 1);
                break;
            }
        }
        return res;
    }
    /**
     * 获取可以胡的牌，即听牌
     * 
     * @param {CardAttrib[]} cards 移除要打的牌,剩余的手牌
     * @param {number} 定缺的花色
     * @returns {CardAttrib[]} 
     * @memberof ChildClass
     */
    getTingPaiByGui(cards: CardAttrib[], gui: CardAttrib, suit: number): CardAttrib[] {
        let results: CardAttrib[] = [];
        if (cards.length % 3 !== 1) return results;
        this.sortCards(cards);
        let temp_cards = cards.slice(0);
        let guis = this.getSameCards(temp_cards, gui);
        let list = this.splistToListBySuit(temp_cards);
        if (list.length > 2 || list.length <= 0) return results;
        let checkList: CardAttrib[] = this.getAllCheck(gui, suit);
        checkList.forEach((card) => {
            if (!this.isSameCard(card, gui)) {
                let temps = cards.slice(0);
                temps.push(card);
                if (this.canHuPaiByGui(temps, gui)) {
                    results.push(card);
                }
            }
        }, this);
        return results;
    }
    /************************************赖子听牌算法****************************************** */

    /**
     * 清空单例对象
     * 
     * @memberof GMManager
     */
    destroySelf(): void {
        this.mjGameData = null;
        this.replayMJ = 0;
        this.replayDataList.length = 0;
        this.isReplayPause = false;
        this.touchTarget = null;
        this.replayRecordId = '0';
    }
}
