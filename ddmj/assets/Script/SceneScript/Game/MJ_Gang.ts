
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

    @property(cc.Sprite)
    icon_gang: cc.Sprite = null;
    /**
     * 牌父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Gang
     */
    @property(cc.Node)
    nodeLayout: cc.Node = null;

    @property([cc.SpriteFrame])
    icon_gang_list: cc.SpriteFrame[] = [];
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
        if (cardNode) {
            cardNode.color = cc.Color.WHITE;
            this._canvasTarget.sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, cardNode.tag, null);
            this.node.removeFromParent(true);
            this.node.destroy();
        }
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
     * @param {number[]} gList 
     * @memberof MJ_Gang
     */
    initData(gList: GangData[], target) {
        this._target = target;
        if (gList && gList.length > 0) {
            this.updateCardData(gList[0], this.node_card);
            if (gList.length > 1) {
                for (var i = 1; i < gList.length; i++) {
                    let cardNode = cc.instantiate(this.node_card);
                    this.updateCardData(gList[i], cardNode);
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
     * @param {number} cardId 牌数据
     * @param {cc.Node} cardNode 牌节点
     * @memberof MJ_Gang
     */
    updateCardData(gangData: GangData, cardNode: cc.Node) {
        let cardImg = cardNode.getChildByName('cardImg');
        let mask = cardNode.getChildByName('mask');
        cardNode.tag = gangData.cardId;
        if (cardImg) {
            let csf: cc.SpriteFrame = this._canvasTarget.getMJCardSF(gangData.cardId);
            cardImg.getComponent(cc.Sprite).spriteFrame = csf;
        }
        this.icon_gang.spriteFrame = gangData.isAnGang === 1 ? this.icon_gang_list[0] : this.icon_gang_list[1];
        if (mask) {
            mask.active = false;
        }
    }
}
