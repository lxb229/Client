
const { ccclass, property } = cc._decorator;
import MJ_Card from './MJ_Card';

import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState, MJ_Suit, MJ_Act_Type } from '../../Modules/Protocol';

@ccclass
export default class MYMJScene extends cc.Component {


    /**
     * 左右两边玩家打出的躺牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MYMJScene
     */
    @property(cc.Prefab)
    mj_card_left_tang_prefab: cc.Prefab = null;

    /**
     * 上玩家打出的躺牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MYMJScene
     */
    @property(cc.Prefab)
    mj_card_top_tang_prefab: cc.Prefab = null;
    /**
     * 下玩家打出的躺牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MYMJScene
     */
    @property(cc.Prefab)
    mj_card_table_tang_prefab: cc.Prefab = null;
    onLoad() {

    }

    /**
     * 创建躺牌
     * 
     * @param {number} type  0下边  1右边  2上边  3左边
     * @param {number} cardId 牌数据
     * @param {cc.Node} parentNode  父节点
     * @param {(mjCardNode: cc.Node) => void} [initCB=null] 创建完成的回调
     * @memberof MYMJScene
     */
    showTangCard(type: number, cardId: number, parentNode: cc.Node, initCB: (mjCardNode: cc.Node) => void = null) {
        let cardNode: cc.Node = null;
        switch (type) {
            case 0:
                cardNode = cc.instantiate(this.mj_card_table_tang_prefab);
                break;
            case 1:
            case 3:
                cardNode = cc.instantiate(this.mj_card_left_tang_prefab);
                break;
            case 2:
                cardNode = cc.instantiate(this.mj_card_top_tang_prefab);
                break;
            default:
        }
        if (cardNode) {
            cardNode.tag = cardId;
            let cardImg: cc.Node = cardNode.getChildByName('img_card');
            if (cardId > 0 && cardImg) {
                let cardSF = dd.gm_manager.getGMTarget().getMJCardSF(cardId);
                cardImg.getComponent(cc.Sprite).spriteFrame = cardSF;
                //如果是右边，需要修正一下参数，因为用的是同一个预设
                if (type === 1) {
                    cardImg.scaleX = -1 * cardImg.scaleX;
                    cardImg.scaleY = -1 * cardImg.scaleY;
                }
            }
            cardNode.parent = parentNode;
            if (initCB) {
                initCB(cardNode);
            }
        }
    }
}
