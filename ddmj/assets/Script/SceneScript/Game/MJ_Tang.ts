
const { ccclass, property } = cc._decorator;

import MJCanvas from './MJCanvas';
import MJ_Card from './MJ_Card';
import * as dd from './../../Modules/ModuleManager';
/**
 * 绵阳麻将的躺
 * @export
 * @class MJ_Tang
 * @extends {cc.Component}
 */
@ccclass
export default class MJ_Tang extends cc.Component {

    /**
     * 牌节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Tang
     */
    @property(cc.Node)
    node_card: cc.Node = null;

    /**
     * 牌父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Tang
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
     * @memberof MJ_Tang
     */
    _target = null;

    MJ_Tang_Touch_Start = (event: cc.Event.EventTouch) => {
        let cardNode: cc.Node = event.currentTarget;
        cardNode.color = cc.Color.GRAY;
        event.stopPropagation();
    };
    MJ_Tang_Touch_End = (event: cc.Event.EventTouch) => {
        let cardNode: cc.Node = event.currentTarget;
        cardNode.color = cc.Color.WHITE;

        let toggle = cardNode.getComponent(cc.Toggle);
        if (toggle) toggle.isChecked = !toggle.isChecked;
        event.stopPropagation();
    };
    onLoad() {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        this.node.on("touchend", (event: cc.Event.EventTouch) => {

            event.stopPropagation();
        }, this);

        this.node_card.on("touchstart", this.MJ_Tang_Touch_Start, this);
        this.node_card.on("touchend", this.MJ_Tang_Touch_End, this);
    }

    /**
     * 初始化数据
     * 
     * @param {number[]} cardIds 
     * @memberof MJ_Tang
     */
    initData(cardIds: number[], target) {
        this._target = target;
        if (cardIds && cardIds.length > 0) {
            cardIds.sort((a, b) => {
                return b - a;
            });
            this.updateCardData(cardIds[0], this.node_card);
            if (cardIds.length > 1) {
                for (var i = 1; i < cardIds.length; i++) {
                    let cardNode = cc.instantiate(this.node_card);
                    this.updateCardData(cardIds[i], cardNode);
                    cardNode.on("touchstart", this.MJ_Tang_Touch_Start, this);
                    cardNode.on("touchend", this.MJ_Tang_Touch_End, this);
                    cardNode.parent = this.nodeLayout;
                }
            }
        }
    }

    /**
     * 刷新牌数据
     * 
     * @param {number} cardId 牌数据
     * @param {cc.Node} CardNode 牌节点
     * @memberof MJ_Tang
     */
    updateCardData(cardId: number, CardNode: cc.Node) {
        CardNode.tag = cardId;
        let cardImg = CardNode.getChildByName('cardImg');
        let mask = CardNode.getChildByName('mask');
        if (cardImg) {
            let csf: cc.SpriteFrame = this._canvasTarget.getMJCardSF(cardId);
            cardImg.getComponent(cc.Sprite).spriteFrame = csf;
        }
        let toggle = CardNode.getComponent(cc.Toggle);
        if (toggle) toggle.isChecked = false;
    }
    /**
     * 取消
     * @memberof MJ_Tang
     */
    click_btn_cancel() {
        dd.mp_manager.playButton();
    }
    /**
     * 确定
     * @memberof MJ_Tang
     */
    click_btn_ok() {
        dd.mp_manager.playButton();
        let tangList = [];
        this.nodeLayout.children.forEach(cardNode => {
            let toggle = cardNode.getComponent(cc.Toggle);
            if (toggle && toggle.isChecked) {
                tangList.push(cardNode.tag);
            }
        });
        cc.log('要躺的牌的列表' + tangList);
    }
}
