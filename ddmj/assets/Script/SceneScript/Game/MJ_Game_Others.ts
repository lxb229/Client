const { ccclass, property } = cc._decorator;

import MJ_Card from './MJ_Card';
import MJ_Card_Group from './MJ_Card_Group';
import MJCanvas from './MJCanvas';
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState } from '../../Modules/Protocol';

@ccclass
export default class MJ_Play extends cc.Component {

    /**
     * 手牌的容器节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_hand: cc.Node = null;

    /**
     * 提示的文本
     * 
     * @type {cc.Label}
     * @memberof MJ_Play
     */
    @property(cc.Label)
    lblTip: cc.Label = null;

    /**
     * 手牌节点列表 0摸牌 1~13手牌
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Play
     */
    @property([cc.Node])
    hand_card_list: cc.Node[] = [];


    /**
     * canvas脚本
     * 
     * @type {MJCanvas}
     * @memberof MJ_Play
     */
    _canvasTarget: MJCanvas = null;

    /**
     * 当前玩家的信息
     * 
     * @type {SeatVo}
     * @memberof MJ_Play
     */
    _seatInfo: SeatVo = null;
    onLoad() {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
    }

    /**
     * 刷新打牌的信息
     * @param {SeatVo} seatInfo 座位信息
     * @memberof MJ_Play
     */
    updatePlay(seatInfo: SeatVo) {
        if (!this._canvasTarget) {
            this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        }
        this._seatInfo = seatInfo;
        this.showPlayState();
        this.showHandCard();
        this.showMPCard();
    }

    /**
    *  显示自己的牌 手牌 + 杠牌 + 碰牌
    * 
    * @memberof MJ_Play
    */
    showHandCard() {
        let handLen = this._seatInfo.handCardsLen;
        //绵阳麻将 == 如果该玩家躺牌了并存在躺牌，就要把手牌中的躺牌去重 
        if (this._seatInfo.tangCardState === 1 && this._seatInfo.tangCardList) {
            handLen -= this._seatInfo.tangCardList.length;
        }
        if (handLen > 0) {
            this.node_hand.active = true;
            //显示其他手牌
            for (var i = 1; i < this.hand_card_list.length; i++) {
                //如果这张牌大于了手牌的长度，就不显示了
                if (i > handLen) {
                    this.hand_card_list[i].active = false;
                } else {
                    this.hand_card_list[i].active = true;
                }
            }
        } else {
            this.node_hand.active = false;
        }
    }

    /**
     *  显示摸牌
     * 
     * @memberof MJ_Play
     */
    showMPCard() {
        //如果是自己表态，并且是摸牌状态，牌也存在，就显示
        if (dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex
            && this._seatInfo.moPaiCard
        ) {
            this.hand_card_list[0].active = true;
        } else {
            this.hand_card_list[0].active = false;
        }
    }

    /**
     * 座位状态的显示
     * 
     * @memberof MJ_Play
     */
    showPlayState() {
        switch (dd.gm_manager.mjGameData.tableBaseVo.gameState) {
            //如果在准备阶段，就初始化数据
            case MJ_GameState.STATE_TABLE_IDLE:
                this.hand_card_list[0].active = false;
                this.node_hand.active = false;
                break;
            case MJ_GameState.STATE_TABLE_READY: {//游戏准备状态
                this.hand_card_list[0].active = false;
                this.node_hand.active = false;
                this.lblTip.node.active = false;
                break;
            }
            case MJ_GameState.STATE_TABLE_SWAPCARD: {//游戏换牌状态
                this.lblTip.node.active = true;
                if (this._seatInfo.btState === 0) {
                    this.lblTip.string = '正在选牌...';
                } else {
                    this.lblTip.string = '已选牌...';
                }
                break;
            }
            case MJ_GameState.STATE_TABLE_DINGQUE: {//游戏定缺状态
                this.lblTip.node.active = true;
                if (this._seatInfo.btState === 0) {
                    this.lblTip.string = '定缺中...';
                } else {
                    this.lblTip.string = '已定缺...';
                }
                break;
            }
            default:
                this.lblTip.node.active = false;
                break;
        }
    }
}


