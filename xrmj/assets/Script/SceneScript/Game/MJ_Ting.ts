
const { ccclass, property } = cc._decorator;

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
    }

    /**
     * 初始化数据
     * 
     * @param {number[]} cardIds
     * @memberof MJ_Ting
     */
    initData(cardIds: number[]) {
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
    updateCardData(card: number, CardNode: cc.Node) {
        let cardImg = CardNode.getChildByName('cardImg');
        let mask = CardNode.getChildByName('mask');
        if (cardImg) {
            let csf: cc.SpriteFrame = dd.gm_manager.getGMTarget().getMJCardSF(card);
            cardImg.getComponent(cc.Sprite).spriteFrame = csf;
        }
        if (mask) {
            let isShowMask = dd.gm_manager.getDieTing(card);
            mask.active = isShowMask;
        }
    }
}
