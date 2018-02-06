
const { ccclass, property } = cc._decorator;

import MJCanvas from './MJCanvas';
import MJ_Card from './MJ_Card';
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class MJ_Ting extends cc.Component {

    /**
     * 牌节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Ting
     */
    @property(cc.Node)
    node_card: cc.Node = null;

    /**
     * canvas脚本
     * 
     * @memberof MJ_Table
     */
    _canvasTarget: MJCanvas = null;

    /**
     * 牌节点列表
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Ting
     */
    _node_card_list: cc.Node[] = [];
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
    }

    /**
     * 初始化数据
     * 
     * @param {CardAttrib[]} cardIds (cardId不能用)
     * @memberof MJ_Ting
     */
    initData(cardIds: CardAttrib[]) {
        this._node_card_list.forEach(cardNode => {
            cardNode.removeFromParent(true);
            cardNode.destroy();
        });
        this._node_card_list.length = 0;
        if (cardIds && cardIds.length > 0) {
            this.updateCardData(cardIds[0], this.node_card);
            if (cardIds.length > 1) {
                for (var i = 1; i < cardIds.length; i++) {
                    let cardNode = cc.instantiate(this.node_card);
                    this.updateCardData(cardIds[i], cardNode);
                    cardNode.parent = this.node;
                    this._node_card_list.push(cardNode);
                }
            }
        }
    }

    /**
     * 刷新牌数据
     * 
     * @param {number} card 牌数据 (cardId不能用)
     * @param {cc.Node} CardNode 牌节点
     * @memberof MJ_Ting
     */
    updateCardData(Card: CardAttrib, CardNode: cc.Node) {
        let hcs: MJ_Card = CardNode.getComponent('MJ_Card');
        let cardId: number = (Card.suit - 1) * 36 + (Card.point - 1) * 4 + 1;
        let csf: cc.SpriteFrame = this._canvasTarget.getMJCardSF(cardId);
        hcs.initData(cardId, csf);
        let isShowMask = dd.gm_manager.getDieTing(Card);
        hcs.showMask(isShowMask);
    }
}
