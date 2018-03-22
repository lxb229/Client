import MJCard from "./MJCard";
const { ccclass, property } = cc._decorator;
/**
 * 将对数据
 */
declare interface JDData {
    /**
     * 组成将对的牌
     */
    card: MJCard,
    /**
     * 需要花费鬼牌的张数(0,1,2)
     */
    count: number
}

@ccclass
export default class ChildClass extends cc.Component {
    @property(cc.Label)
    lbl: cc.Label = null;
    wans: MJCard[] = [];
    tongs: MJCard[] = [];
    tiaos: MJCard[] = [];
    onLoad() {
        for (let i = 1; i < 4; i++) {
            for (let j = 1; j < 10; j++) {
                let card = new MJCard(j, i);
                switch (i) {
                    case 1: this.wans.push(card); break;
                    case 2: this.tongs.push(card); break;
                    case 3: this.tiaos.push(card); break;
                    default: break;
                }
            }
        }



        // let objs = [];
        // for (let i = 0; i < 10000; i++) {
        //     let wan = [];
        //     let tong = [];
        //     let tiao = [];

        //     let arrs = [wan, tong, tiao];
        //     for (let i = 1; i < 4; i++) {
        //         for (let j = 1; j < 10; j++) {
        //             let card = new MJCard(j, i);
        //             switch (i) {
        //                 case 1: wan.push([card, card, card, card]); break;
        //                 case 2: tong.push([card, card, card, card]); break;
        //                 case 3: tiao.push([card, card, card, card]); break;
        //                 default: break;
        //             }
        //         }
        //     }
        //     let obj = {
        //         arr: [],
        //         hu: false,
        //         gui: Math.floor(Math.random() * 1000) % 4
        //     }
        //     let guiCount = obj.gui;
        //     while (guiCount > 0) {
        //         guiCount--;
        //         obj.arr.push(new MJCard(1, 3));
        //     }
        //     let tt = [2, 5, 8, 11, 14];
        //     let index = Math.floor(Math.random() * 1000) % 5;
        //     let lastCount = tt[index] - obj.arr.length;
        //     if (lastCount < 0) {
        //         lastCount += 3;
        //     }
        //     while (lastCount > 0) {
        //         let suit = Math.floor(Math.random() * 1000) % 3 + 1;
        //         let point = Math.floor(Math.random() * 1000) % 9 + 1;
        //         if (suit === 3 && point === 1) {
        //             point = Math.floor(Math.random() * 1000) % 8 + 2;
        //         }
        //         if (arrs[suit - 1][point - 1].length > 0) {
        //             obj.arr.push(arrs[suit - 1][point - 1].pop());
        //             lastCount--;
        //         }
        //     }
        //     objs.push(obj);
        // }

        let arr = [
            new MJCard(3, 2),
            new MJCard(4, 2),
            new MJCard(4, 2),
            new MJCard(4, 2),
            new MJCard(5, 2),
            new MJCard(6, 2),
            new MJCard(7, 2),
            new MJCard(7, 2),
            new MJCard(8, 2),
            new MJCard(9, 2)
        ];

        let start = Date.now();


        let cards = this.getTingByTang(arr,
            [new MJCard(7, 2),
            new MJCard(9, 2)]);

        cc.log(cards);

        // let ture_count = 0;
        // let rights = [];
        // let errors = [];
        // objs.forEach((obj) => {
        //     obj.hu = this.canHuPaiByGui(obj.arr, new MJCard(1, 3));
        //     if (obj.hu) {
        //         ture_count++;
        //         rights.push(obj);
        //     } else {
        //         errors.push(obj);
        //     }
        // }, this);

        // let hu = this.canHuPaiByGui(arr, new MJCard(1, 3));
        // let tings = this.getTingPaiByGui(arr, new MJCard(1, 3), 2);

        let diff = Date.now() - start;
        // this.lbl.string = diff + '|' + ture_count;

        // cc.log(errors);
        // cc.log('-------------------');
        // cc.log(rights.sort((a, b) => {
        //     return a.gui - b.gui;
        // }));

        // let tings = this.getTingPai(arr);
        // let list = this.getTingPai(arr);
        // let points = list.map((item) => {
        //     return item.point + item.suit * 10;
        // }, this);
        // this.lbl.string = diff + '|' + points.join(',');
        // cc.log(list);
    }

    /**
     * 判断是否可以胡牌
     * 
     * @param {MJCard[]} cards 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canHuPai(cards: MJCard[]): boolean {
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
     * @param {MJCard[]} cards 
     * @returns {number} 
     * @memberof ChildClass
     */
    getSuits(cards: MJCard[]): number[] {
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
    checkRemaining(nums: number[]): boolean {
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
    getDuplicate(nums: number[]): number[] {
        var result: number[] = [];
        nums.forEach((num) => {
            if (nums.indexOf(num) !== nums.lastIndexOf(num) && result.indexOf(num) === -1)
                result.push(num);
        })
        return result;
    }

    /**
     * 判断7对
     * 
     * @param {number[]} nums 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    checkQiDui(nums: number[]): boolean {
        if (nums.length !== 14) return false;
        for (let i = 0; i <= 12; i += 2) {
            if (nums[i] !== nums[i + 1]) {
                return false;
            }
        }
        return true;
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
     * 获取可以胡的牌，即听牌
     * 
     * @param {MJCard[]} cards 
     * @returns {MJCard[]} 
     * @memberof ChildClass
     */
    getTingPai(cards: MJCard[]): MJCard[] {
        let results: MJCard[] = [];
        let checkList: MJCard[] = [];
        let suits = this.getSuits(cards);
        if (suits.length > 2) return [];
        suits.forEach((suit) => {
            switch (suit) {
                case 1: checkList.push(...this.wans); break;
                case 2: checkList.push(...this.tongs); break;
                case 3: checkList.push(...this.tiaos); break;
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
     * 按照花色和点数排序
     * 
     * @param {MJCard[]} cards 
     * @memberof ChildClass
     */
    sortCards(cards: MJCard[]) {
        cards.sort((a: MJCard, b: MJCard) => {
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
     * @param {MJCard} card1 
     * @param {MJCard} card2 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    isSameCard(card1: MJCard, card2: MJCard): boolean {
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
    removeCard(cards: MJCard[], card: MJCard) {
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
     * @param {MJCard[]} arr1 
     * @param {MJCard[]} arr2 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    isSameArray(arr1: MJCard[], arr2: MJCard[]): boolean {
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
     * @param {MJCard[]} cards 
     * @param {number} start 
     * @returns 
     * @memberof ChildClass
     */
    getIndexs(cards: MJCard[], start: number) {
        let card1 = cards[start];
        if (card1.point > 7) return null;
        let card2 = new MJCard(card1.point + 1, card1.suit);
        let card3 = new MJCard(card1.point + 2, card1.suit);
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
     * @param {MJCard[]} newCards 
     * @param {MJCard[]} result 
     * @param {boolean} checkHu 
     * @returns 
     * @memberof ChildClass
     */
    checkLen(newCards: MJCard[], result: MJCard[], checkHu: boolean) {
        let count = 0;
        result.forEach((card: MJCard) => {
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
     * @param {MJCard[]} cards 
     * @param {MJCard[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canRemoveK(cards: MJCard[], result: MJCard[], checkHu: boolean = true): boolean {
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
     * @param {MJCard[]} cards 
     * @param {MJCard[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canRemoveS(cards: MJCard[], result: MJCard[], checkHu: boolean = true): boolean {
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
     * @param {MJCard[]} cards 
     * @param {MJCard[]} result 
     * @param {boolean} [checkHu=true] 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canRemoveD(cards: MJCard[], result: MJCard[], checkHu: boolean = true): boolean {
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
    /**
     * 根据所选躺拍获取指定听牌，如果选择了多余的牌不能获取到听牌
     * 
     * @param {MJCard[]} cards 
     * @param {MJCard[]} outs 
     * @returns {MJCard[]} 
     * @memberof ChildClass
     */
    getTingByTang(cards: MJCard[], outs: MJCard[]): MJCard[] {
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
                tings.forEach((card: MJCard) => {
                    let temp = outs.slice(0);
                    temp.push(card);
                    if (this.canHuPai(temp)) {
                        let newCards = cards.slice(0);
                        outs.forEach((card) => {
                            this.removeCard(newCards, card);
                        }, this);
                        let nums = newCards.map((card) => {
                            return card.suit * 10 + card.point;
                        }, this);
                        nums.sort();
                        if (this.checkRemaining(nums)) {
                            result.push(card);
                        }
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
            tings.forEach((card: MJCard) => {
                let temp = outs.slice(0);
                temp.push(card);
                let nums = temp.map((card) => {
                    return card.suit * 10 + card.point;
                }, this);
                nums.sort();
                if (this.checkRemaining(nums)) {
                    let newCards = cards.slice(0);
                    outs.forEach((card) => {
                        this.removeCard(newCards, card);
                    }, this);
                    if (this.canHuPai(newCards)) {
                        result.push(card);
                    }
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
     * 移除所有两张相同的牌
     * 
     * @param {MJCard[]} cards 
     * @returns {MJCard[][]} 返回对子集合
     * @memberof ChildClass
     */
    remove2Tong(cards: MJCard[]): MJCard[][] {
        let res: MJCard[][] = [];
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
     * @param {MJCard[]} cards 
     * @returns {MJCard[][]} 
     * @memberof ChildClass
     */
    splistToListBySuit(cards: MJCard[]): MJCard[][] {
        let res: MJCard[][] = [];
        let list1: MJCard[] = [];
        let list2: MJCard[] = [];
        let list3: MJCard[] = [];
        cards.forEach((card: MJCard) => {
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
     * @param {MJCard[]} cards 
     * @returns 
     * @memberof ChildClass
     */
    extractJD(cards: MJCard[], guis: MJCard[]) {
        let res = [];
        let suit = cards[0].suit;
        let dui_2 = [];//对牌
        let dui_1 = [];//单牌
        let points = cards.map((card: MJCard) => {
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
                card: new MJCard(point, suit),
                count: 0
            }
            res.push(obj);
        });
        if (guis.length > 0) {//有鬼
            dui_1.forEach((point: number) => {
                let obj: JDData = {
                    card: new MJCard(point, suit),
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
     * @param {MJCard[]} cards 
     * @param {MJCard} card 
     * @returns 返回对象下标,没找到返回-1
     * @memberof ChildClass
     */
    findCard(cards: MJCard[], card: MJCard) {
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
     * @param {MJCard[]} cards 
     * @memberof ChildClass
     */
    remove3TL(cards: MJCard[]) {
        let dans: MJCard[] = [];
        while (cards.length > 2) {
            let card1 = cards[0];
            let card2 = cards[1];
            let card3 = cards[2];
            if (this.isSameCard(card1, card2) && this.isSameCard(card2, card3)) {
                cards.splice(0, 3);
            } else {
                let point = card1.point;
                let index2 = this.findCard(cards, new MJCard(card1.point + 1, card1.suit));
                let index3 = this.findCard(cards, new MJCard(card1.point + 2, card1.suit));
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
     * @param {MJCard[]} cards 
     * @returns {number} 返回凑整这些牌需要多少个鬼
     * @memberof ChildClass
     */
    remove2LT(cards: MJCard[]): number {
        let danCount = 0;
        let duiCount = 0;
        let lianCount = 0;
        while (cards.length > 1) {
            let card1 = cards[0];
            let index2 = this.findCard(cards, new MJCard(card1.point + 1, card1.suit));
            let index3 = this.findCard(cards, new MJCard(card1.point + 2, card1.suit));
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
     * @param {MJCard[]} cards 
     * @param {MJCard} card 
     * @returns {MJCard[]} 
     * @memberof ChildClass
     */
    getSameCards(cards: MJCard[], card: MJCard): MJCard[] {
        let removes: MJCard[] = [];
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
     * @param {MJCard[]} cards 
     * @param {MJCard} gui 
     * @returns {boolean} 
     * @memberof ChildClass
     */
    canHuPaiByGui(cards: MJCard[], gui: MJCard): boolean {
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
     * @param {MJCard} gui 
     * @param {number} suit 
     * @returns {MJCard[]} 
     * @memberof ChildClass
     */
    getAllCheck(gui: MJCard, suit: number): MJCard[] {
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
     * @param {MJCard[]} cards 移除要打的牌,剩余的手牌
     * @param {number} 定缺的花色
     * @returns {MJCard[]} 
     * @memberof ChildClass
     */
    getTingPaiByGui(cards: MJCard[], gui: MJCard, suit: number): MJCard[] {
        let results: MJCard[] = [];
        if (cards.length % 3 !== 1) return results;
        this.sortCards(cards);
        let temp_cards = cards.slice(0);
        let guis = this.getSameCards(temp_cards, gui);
        let list = this.splistToListBySuit(temp_cards);
        if (list.length > 2 || list.length <= 0) return results;
        let checkList: MJCard[] = this.getAllCheck(gui, suit);
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
}





