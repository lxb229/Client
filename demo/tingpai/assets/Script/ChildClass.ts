import MJCard from "./MJCard";
const { ccclass, property } = cc._decorator;

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

        let arr = [
            new MJCard(1, 1),
            new MJCard(2, 1),
            new MJCard(3, 1),
            new MJCard(7, 1),
            new MJCard(8, 1),
            new MJCard(9, 1),
            new MJCard(3, 3),
            new MJCard(3, 3),
            new MJCard(5, 3),
            new MJCard(5, 3)
        ];
        let start = Date.now();
        let list = this.getTingPai(arr);
        let diff = Date.now() - start;
        let points = list.map((item) => {
            return item.point + item.suit * 10;
        }, this);
        this.lbl.string = diff + '|' + points.join(',');
        cc.log(list);
        let outs = [
            new MJCard(3, 3),
            new MJCard(3, 3),
            new MJCard(5, 3),
            new MJCard(5, 3)
        ];



        cc.log(this.getTingByTang(arr, outs));
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
        for (let i = 0; i < 12; i += 2) {
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
            tings.forEach((card: MJCard) => {
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
}


