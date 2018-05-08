
const { ccclass, property } = cc._decorator;

import MJ_Card from './MJ_Card';
import * as dd from './../../Modules/ModuleManager';
import { MJ_Act_Type, MJ_Game_Type } from '../../Modules/Protocol';
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

    @property([cc.SpriteFrame])
    icon_gang_list: cc.SpriteFrame[] = [];

    /**
     * 调用的父节点
     * 
     * @type {any}
     * @memberof MJ_Gang
     */
    _target = null;
    /**
     * 杠牌列表
     * @type {GangData[]}
     * @memberof MJ_Gang
     */
    _gangList: GangData[] = [];
    MJ_Gang_Touch_Start = (event: cc.Event.EventTouch) => {
        // let cardNode: cc.Node = event.currentTarget;
        // cardNode.color = cc.Color.GRAY;
        event.stopPropagation();
    };
    MJ_Gang_Touch_End = (event: cc.Event.EventTouch) => {
        let cardNode: cc.Node = event.currentTarget;
        if (cardNode) {
            // cardNode.color = cc.Color.WHITE;
            this.sendGangInfo(cardNode.tag);
            this.node.removeFromParent(true);
            this.node.destroy();
        }
        event.stopPropagation();
    };
    /**
     * 发送杠牌消息
     * @param {number} tag 
     * @memberof MJ_Gang
     */
    sendGangInfo(tag: number) {
        switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
            case MJ_Game_Type.GAME_TYPE_LSMJ://乐山麻将
                dd.gm_manager.getGMTarget().sendLSGangBreakCard(MJ_Act_Type.ACT_INDEX_GANG, this._gangList[tag], null);
                break;
            default:
                dd.gm_manager.getGMTarget().sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, this._gangList[tag].cardId, null);
                break;
        }
    }
    onLoad() {
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
        let result: GangData[] = [];
        if (gList && gList.length > 0) {
            for (let i = 0; i < gList.length; i++) {
                let isRepeat = this.getIsRepeat(gList[i], result);
                if (!isRepeat) {
                    result.push(gList[i]);
                }
            }
        }
        result.sort((a, b) => {
            return a.cardId - b.cardId;
        });
        this._gangList = result;

        if (result && result.length > 0) {
            for (let i = 0; i < result.length; i++) {
                if (i === 0) {
                    this.updateCardData(i, result[0], this.node_card);
                } else {
                    let cardNode = cc.instantiate(this.node_card);
                    this.updateCardData(i, result[i], cardNode);
                    cardNode.on("touchstart", this.MJ_Gang_Touch_Start, this);
                    cardNode.on("touchend", this.MJ_Gang_Touch_End, this);
                    cardNode.parent = this.nodeLayout;
                }
            }
        }
    }
    /**
     * 获取是否重复
     * @param {GangData} gangInfo 
     * @param {GangData[]} result 
     * @returns 
     * @memberof MJ_Gang
     */
    getIsRepeat(gangInfo: GangData, result: GangData[]) {
        let card1 = Math.floor(gangInfo.cardId / 10);
        for (let j = 0; j < result.length; j++) {
            let card2 = Math.floor(result[j].cardId / 10);
            if (card1 === card2) {
                return true;
            }
        }
        return false;
    }
    /**
     * 刷新牌数据
     * 
     * @param {number} cardId 牌数据
     * @param {cc.Node} cardNode 牌节点
     * @memberof MJ_Gang
     */
    updateCardData(index: number, gangData: GangData, cardNode: cc.Node) {
        let cardImg = cardNode.getChildByName('cardImg');
        let mask = cardNode.getChildByName('mask');
        let icon_gang = cardNode.getChildByName('icon_gang');
        cardNode.tag = index;
        if (cardImg) {
            let csf: cc.SpriteFrame = dd.gm_manager.getGMTarget().getMJCardSF(gangData.cardId);
            cardImg.getComponent(cc.Sprite).spriteFrame = csf;
        }

        icon_gang.getComponent(cc.Sprite).spriteFrame = this.icon_gang_list[gangData.isAnGang - 1];
        if (mask) {
            mask.active = false;
        }
    }
}
