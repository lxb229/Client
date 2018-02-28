const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import ZJH_PlayerUI from './ZJH_PlayerUI';
import * as ZJH_Help from './ZJH_Help';
@ccclass
export default class ZJH_Player extends cc.Component {
    /**
     * 玩家节点坐标
     * 
     * @type {cc.Node[]}
     * @memberof ZJH_Player
     */
    @property(cc.Node)
    node_player_list: cc.Node[] = [];

    _canvansScript = null;
    onLoad() {
        this._canvansScript = dd.ui_manager.getCanvasNode().getComponent('ZJHCanvas');
    }
    /**
     * 重置玩家信息
     * 
     * @memberof ZJH_Player
     */
    resetPlayerInfo() {
        for (var i = 0; i < this.node_player_list.length; i++) {
            let player = this.node_player_list[i];
            if (player) {
                let playerUI: ZJH_PlayerUI = player.getComponent('ZJH_PlayerUI');
                playerUI.resetSeat();
            }
        }
    }
    /**
     * 显示玩家信息
     * @memberof ZJH_Player
     */
    showPlayerInfo() {
        let seats = dd.gm_manager.zjhGameData.seats;
        if (seats && seats.length) {
            for (var i = 0; i < seats.length; i++) {
                let seat = seats[i];
                let player = this.node_player_list[i];
                if (player) {
                    if (seat && seat.accountId !== '' && seat.accountId !== '0') {
                        player.active = true;
                        let playerUI: ZJH_PlayerUI = player.getComponent('ZJH_PlayerUI');
                        playerUI.initData(seat);
                    } else {
                        player.active = false;
                    }
                }
            }
        }
    }

    /**
     * 获取发牌的坐标列表 和 索引列表
     * 
     * @memberof NewClass
     */
    getFPPosList() {
        let obj = {
            'posList': [],
            'playerIndexList': []
        };
        for (var i = 0; i < dd.gm_manager.zjhGameData.seats.length; i++) {
            let seat = dd.gm_manager.zjhGameData.seats[i];
            if (seat && seat.accountId !== '' && seat.accountId !== '0'
                && seat.bGamed === 1
                && seat.btState !== ZJH_Help.ZJH_BT_State.ACT_STATE_DROP) {
                let player = this.node_player_list[i];
                if (player) {
                    let playerUI = player.getComponent('ZJH_PlayerUI');
                    let cPos = playerUI.getCardBoardPos();
                    let pos = this.getWorldPos(player, cPos);
                    obj.posList.push(pos);
                    obj.playerIndexList.push(i);
                }
            }
        }
        return obj;
    }

    /**
     * 根据index来显示玩家的牌信息
     * @param {number} index 座位玩家索引
     * @param {number} pokerId 牌索引
     * @param {number} [type=0] 0=显示牌 1=显示翻牌
     * @param {boolean} [isShow=true] 是否显示牌
     * @memberof ZJH_Player
     */
    showPokerByIndex(index: number, pokerId: number, type: number = 0, isShow: boolean = true) {
        let player = this.node_player_list[index];
        if (player) {
            let playerUI: ZJH_PlayerUI = player.getComponent('ZJH_PlayerUI');
            playerUI.showPokerById(pokerId, type, isShow);
        }
    }

    /**
     * 获取世界坐标 node:父节点 pos:坐标
     * @param {any} node 
     * @param {any} pos 
     * @returns 
     * @memberof ZJH_Player
     */
    getWorldPos(node, pos) {
        let targetP = node.convertToWorldSpaceAR(pos);
        //依然是以屏幕左下角为起点,所以要减去一半
        targetP.x = targetP.x - this.node.width / 2;
        targetP.y = targetP.y - this.node.height / 2;
        return targetP;
    }
    /**
     * 根据座位索引，获取座位的坐标点
     * 
     * @memberof ZJH_Player
     */
    getPosByIndex(index: number) {
        if (index < 0 || index >= this.node_player_list.length) {
            cc.log('索引错误');
            return cc.p(0, 0);
        }
        let player = this.node_player_list[index];
        if (player) {
            return this.getWorldPos(this.node, player.getPosition());
        }
        return cc.p(0, 0);
    }
    /**
     * 显示所有人的牌
     * @memberof ZJH_Player
     */
    showAllFPAction() {
        for (var i = 0; i < dd.gm_manager.zjhGameData.seats.length; i++) {
            let seat = dd.gm_manager.zjhGameData.seats[i];
            if (seat && seat.accountId !== '' && seat.accountId !== '0' && seat.bGamed === 1) {
                this.showFPActionBySeatId(seat.seatIndex);
            }
        }
    }
    /**
     *  根据座位id显示翻牌动作
     * @param {number} seatId   //需要计算的座位id
     * @memberof ZJH_Player
     */
    showFPActionBySeatId(seatId: number) {
        let index = ZJH_Help.getIndexBySeatId(seatId);
        let player = this.node_player_list[index];
        if (player) {
            let script = player.getComponent("ZJH_PlayerUI");
            script.showSeatFPAction();
        }
    }

    /**
     * 根据自己的座位，计算seatId在界面上是第几个位置
     * @param {number} mySeatId //自己的座位id
     * @param {number} seatId   //需要计算的座位id
     * @returns 
     * @memberof ZJH_Player
     */
    getSeatIndexBySeatId(mySeatId: number, seatId: number) {
        //获取自己的座位信息
        let index = 0;
        if (mySeatId > seatId) {
            index = 5 - (mySeatId - seatId);
        } else {
            index = Math.abs(mySeatId - seatId);
        }
        return index;
    }


    /**
     * 显示牌的抖动动作
     */
    showPokerShakeAction() {
        let prevSeat = ZJH_Help.getSeatBySeatId(dd.gm_manager.zjhGameData.prevSeatIndex);
        //如果上一家的表态是全下，那么只能比牌 上一家
        if (prevSeat && prevSeat.btVal === ZJH_Help.ZJH_Act_State.BT_VAL_BETALL) {
            this.showShakeBySeat(prevSeat);
        } else {
            let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
            //自己当前 跟注 时，应该下注的金额
            let showMoney = mySeat.looked === 1 ? dd.gm_manager.zjhGameData.lookBetMoney : dd.gm_manager.zjhGameData.unLookBetMoney;
            //如果自己身上的钱不足 应该下注的钱，就只能 比牌下一家         
            if (mySeat.money <= showMoney) {
                let nextSeat = this.getNextBtSeat(dd.gm_manager.zjhGameData.btIndex);
                if (nextSeat) {
                    this.showShakeBySeat(nextSeat);
                }
            } else {
                for (var i = 0; i < dd.gm_manager.zjhGameData.seats.length; i++) {
                    let seat = dd.gm_manager.zjhGameData.seats[i];
                    if (seat && seat.accountId !== '' && seat.accountId !== '0' && seat.bGamed === 1
                        && seat.accountId !== dd.ud_manager.mineData.accountId
                        && seat.btVal !== ZJH_Help.ZJH_Act_State.BT_VAL_DROP
                        && seat.btState !== ZJH_Help.ZJH_BT_State.ACT_STATE_DROP) {
                        this.showShakeBySeat(seat);
                    }
                }
            }
        }
    }
    /**
     * 根据seat显示抖牌
     * @param {SeatVo} seat 
     * @memberof ZJH_Player
     */
    showShakeBySeat(seat: SeatVo) {
        let index = ZJH_Help.getIndexBySeatId(seat.seatIndex);
        let player = this.node_player_list[index];
        if (player) {
            let playerUI = player.getComponent('ZJH_PlayerUI');
            let cPos = playerUI.getCardBoardPos();
            let pos = this.getWorldPos(player, cPos);
            this._canvansScript.showShakeAction(seat.seatIndex, pos);
        }
    }
    /**
     * 获取下一家表态的人
     * @memberof ZJH_Player
     */
    getNextBtSeat(btIndex: number): SeatVo {
        let loop = 0;
        let nextSeat = null;
        let nextIndex = btIndex + 1;
        while (loop < this.node_player_list.length) {
            nextIndex = nextIndex % this.node_player_list.length;
            let seat = ZJH_Help.getSeatBySeatId(nextIndex);
            if (seat.accountId !== '' && seat.accountId !== '0' &&
                seat.bGamed === 1 && seat.btState !== ZJH_Help.ZJH_BT_State.ACT_STATE_DROP) {
                nextSeat = seat;
                break;
            }
            nextIndex++;
            loop++;
        }
        return nextSeat;
    }
}
