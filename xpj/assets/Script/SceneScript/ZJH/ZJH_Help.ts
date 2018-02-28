
import GMManager from '../../Modules/GMManager';
import UDManager from '../../Modules/UDManager';

/**
 * 动作表态 枚举
 * @export
 * @enum {number} 不要= -1 等待表态 = 0  已表态 = 1
 */
export enum ZJH_BT_State {
    //不要
    ACT_STATE_DROP = -1,
    //等待表态
    ACT_STATE_WAIT = 0,
    //已表态
    ACT_STATE_BT = 1,
}
/**
 *金花游戏状态定义
 * @export
 * @enum {number}
 */
export enum ZJH_Game_State {
    /**
     * 空闲
     */
    STATE_TABLE_IDLE = 0,
    /**
     * 准备
     */
    STATE_TABLE_ZJH_READY = 1,
    /**
     * 下底注
     */
    STATE_TABLE_ZJH_BASESCORE = 2,
    /**
     * 发牌
     */
    STATE_TABLE_ZJH_FAPAI = 3,
    /**
     * 下注表态
     */
    STATE_TABLE_ZJH_BET = 4,
    /**
     * 比牌
     */
    STATE_TABLE_ZJH_COMPARE = 5,
    /**
     * 结算
     */
    STATE_TABLE_ZJH_OVER = 6
}

/**
 * 定义表态状态值
 * 
 * @export
 * @enum {number} 
 */
export enum ZJH_Act_State {
    /**
     * 弃牌
     */
    BT_VAL_DROP = 0,
    /**
     * 看牌
     */
    BT_VAL_LOOCK = 1,
    /**
     * 比牌
     */
    BT_VAL_COMPARAE = 2,
    /**
     * 全下
     */
    BT_VAL_BETALL = 3,
    /**
     * 跟注
     */
    BT_VAL_BETSAME = 4,
    /**
     * 加注
     */
    BT_VAL_BETADD = 5
}

/**
 * 对座位列表进行排序,让自己的座位始终在最下方
 * 
 * @param {any} seatList 
 * @returns 
 */
export function sortSeatList(seats) {
    if (!GMManager.getInstance().zjhGameData) return null;
    let mySeat: SeatVo = getSeatById(UDManager.getInstance().mineData.accountId);
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
 * 根据玩家accountId获取座位号
 * 
 * @param {string} accountId 
 * @returns {SeatVo} 
 */
export function getSeatById(accountId: string): SeatVo {
    if (!GMManager.getInstance().zjhGameData || !GMManager.getInstance().zjhGameData.seats) return null;
    for (var i = 0; i < GMManager.getInstance().zjhGameData.seats.length; i++) {
        let seatInfo: SeatVo = GMManager.getInstance().zjhGameData.seats[i];
        if (seatInfo.accountId === accountId) {
            return seatInfo;
        }
    }
    return null;
}

/**
 * 根据玩家SeatId获取座位号
 * 
 * @param {string} seatById 
 * @returns {SeatVo} 
 */
export function getSeatBySeatId(seatById: Number): SeatVo {
    if (!GMManager.getInstance().zjhGameData || !GMManager.getInstance().zjhGameData.seats) return null;
    for (var i = 0; i < GMManager.getInstance().zjhGameData.seats.length; i++) {
        let seatInfo: SeatVo = GMManager.getInstance().zjhGameData.seats[i];
        if (seatInfo.seatIndex === seatById) {
            return seatInfo;
        }
    }
    return null;
}


/**
 * 根据座位id获取位置索引 
 * 
 * @export
 * @param {number} seatId   seatId位-1的时候，根据accountId返回
 * @param {string} [accountId=''] 
 * @returns 
 */
export function getIndexBySeatId(seatId: number, accountId: string = ''): number {
    if (GMManager.getInstance().zjhGameData && GMManager.getInstance().zjhGameData.seats) {
        let seats = GMManager.getInstance().zjhGameData.seats;
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

export function getNowPlayer(): SeatVo[] {
    let seatList: SeatVo[] = [];
    if (GMManager.getInstance().zjhGameData && GMManager.getInstance().zjhGameData.seats) {
        let seats = GMManager.getInstance().zjhGameData.seats;
        for (var i = 0; i < seats.length; i++) {
            let seat: SeatVo = seats[i];
            if (seat.accountId !== '' && seat.accountId !== '0'
                && seat.bGamed === 1 && seat.btState !== ZJH_BT_State.ACT_STATE_DROP) {
                seatList.push(seat);
            }
        }
    }
    return seatList;
}

/**
 * 根据数字获取每个区间的 数量
 * @export
 * @param {any} num 
 * @returns 
 */
export function getEveryIntervalNum(num) {
    num = Number(num);
    let level = [100, 50, 20, 10, 5, 2, 1];
    let ChipList = [0, 0, 0, 0, 0, 0, 0];
    for (let i = 0; i < 7; i++) {
        ChipList[i] = Math.floor(num / level[i]);
        if (num % level[i] === 0) break;
        num = num % level[i];
    }
    return ChipList;
}

/**
 * 根据数据获取指定筹码区域的个数,其他区域为0个
 * @export
 * @param {number} num 
 * @param {number} speNum 
 * @returns 
 */
export function getOneIntervalNum(num: number, speNum: number) {
    num = Number(num);
    let ChipList = [];
    let b5 = speNum === 100 ? Math.floor(num / 100) : 0;           //100
    ChipList.push(b5);
    let b4 = speNum === 50 ? Math.floor(num / 50) : 0;      //50
    ChipList.push(b4);
    let b2 = speNum === 20 ? Math.floor(num / 20) : 0;     //20
    ChipList.push(b2);
    let b1 = speNum === 10 ? Math.floor(num / 10) : 0;     //10
    ChipList.push(b1);
    let n4 = speNum === 5 ? Math.floor(num / 5) : 0;         //5
    ChipList.push(n4);
    let n2 = speNum === 2 ? Math.floor(num / 2) : 0;         //2
    ChipList.push(n2);
    let n1 = speNum === 1 ? Math.floor(num / 1) : 0;          //1
    ChipList.push(n1);
    return ChipList;
}

