
const { ccclass, property } = cc._decorator;

import MJCanvas from './MJCanvas';
import MJ_Card from './MJ_Card';
import * as dd from './../../Modules/ModuleManager';
import { MJ_Act_Type } from '../../Modules/Protocol';
@ccclass
export default class MJ_Gang extends cc.Component {

    /**
     * 牌节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Gang
     */
    @property(cc.Node)
    node_card: cc.Node = null;

    /**
     * 牌父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Gang
     */
    @property(cc.Node)
    nodeLayout: cc.Node = null;

    /**
     * canvas脚本
     * 
     * @memberof MJ_Table
     */
    _canvasTarget: MJCanvas = null;

    /**
     * 调用的父节点
     * 
     * @type {any}
     * @memberof MJ_Gang
     */
    _target = null;

    MJ_Gang_Touch_Start = (event: cc.Event.EventTouch) => {
        let cardNode: cc.Node = event.currentTarget;
        cardNode.color = cc.Color.GRAY;
        event.stopPropagation();
    };
    MJ_Gang_Touch_End = (event: cc.Event.EventTouch) => {
        let cardNode: cc.Node = event.currentTarget;
        cardNode.color = cc.Color.WHITE;
        let hcs: MJ_Card = cardNode.getComponent('MJ_Card');
        this._canvasTarget.sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, hcs._cardId, null);
        this.node.removeFromParent(true);
        this.node.destroy();
        event.stopPropagation();
    };
    onLoad() {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this._target.node_state.active = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);

        this.node_card.on("touchstart", this.MJ_Gang_Touch_Start, this);
        this.node_card.on("touchend", this.MJ_Gang_Touch_End, this);
    }

    /**
     * 初始化数据
     * 
     * @param {number[]} cardIds 
     * @memberof MJ_Gang
     */
    initData(cardIds: number[], target) {
        this._target = target;
        if (cardIds && cardIds.length > 0) {
            this.updateCardData(cardIds[0], this.node_card);
            if (cardIds.length > 1) {
                for (var i = 1; i < cardIds.length; i++) {
                    let cardNode = cc.instantiate(this.node_card);
                    this.updateCardData(cardIds[i], cardNode);
                    cardNode.on("touchstart", this.MJ_Gang_Touch_Start, this);
                    cardNode.on("touchend", this.MJ_Gang_Touch_End, this);
                    cardNode.parent = this.nodeLayout;
                }
            }
        }
    }

    /**
     * 刷新牌数据
     * 
     * @param {number} card 牌数据
     * @param {cc.Node} CardNode 牌节点
     * @memberof MJ_Gang
     */
    updateCardData(Card: number, CardNode: cc.Node) {
        let hcs: MJ_Card = CardNode.getComponent('MJ_Card');
        let csf: cc.SpriteFrame = this._canvasTarget.getMJCardSF(Card);
        hcs.initData(Card, csf);
    }
}
