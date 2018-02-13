import UDManager from "./UDManager";
import { MJ_Suit } from "./Protocol";
import MJ_Game_Mine from '../SceneScript/Game/MJ_Game_Mine';
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
     * 根据花色点数算cardId
     * @param {number} suit 
     * @param {number} point 
     * @returns 
     * @memberof GMManager
     */
    getCardBySP(suit: number, point: number) {
        return (suit - 1) * 36 + (point - 1) * 4 + point % 4;
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
        var result: number[] = [];
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
        for (let i = 0; i < 12; i += 2) {
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
     * @param {MJCard[]} cards 
     * @param {MJCard} card 
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
        let card2: CardAttrib = { suit: card1.suit, point: card1.point + 1, cardId: (card1.suit) % 36 + (card1.point + 1) + (card1.suit - 1) * 4 };
        let card3: CardAttrib = { suit: card1.suit, point: card1.point + 2, cardId: (card1.suit) % 36 + (card1.point + 2) + (card1.suit - 1) * 4 };
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
    /****************************************************************************************************************** */
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
