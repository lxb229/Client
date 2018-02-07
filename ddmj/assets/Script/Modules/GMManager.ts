import UDManager from "./UDManager";
import { MJ_Suit } from "./Protocol";

/**
 * 游戏管理类
 * 
 * @export
 * @class GMManager
 */
export default class GMManager {
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
        for (var i = 0; i < list.length; i++) {
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
            for (var i = 0; i < seats.length; i++) {
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
    getCardIdsByCardId(cardIds1: number[], cardIds2: number[]): number[] {
        let list = [];
        for (var i = 0; i < cardIds1.length; i++) {
            let card1 = this.getCardById(cardIds1[i]);
            for (var j = 0; j < cardIds2.length; j++) {
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
            for (var i = 0; i < seats.length; i++) {
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
     * 获取是否是死觉
     * 
     * @export
     * @param {CardAttrib} card 
     */
    getDieTing(card: CardAttrib) {
        let tempList = [];
        if (this.mjGameData && this.mjGameData.seats) {
            let seats = this.mjGameData.seats;
            for (var i = 0; i < seats.length; i++) {
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
        for (var i = 0; i < tempList.length; i++) {
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
        var diff = arr1.concat(arr2).sort((a, b) => {
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
