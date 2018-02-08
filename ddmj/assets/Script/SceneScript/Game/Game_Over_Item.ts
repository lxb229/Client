const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import MJCanvas from './MJCanvas';
import MJ_Card from './MJ_Card';

@ccclass
export default class Game_Over_Item extends cc.Component {
    @property(cc.Node)
    node_light: cc.Node = null;
    /**
     * 头像
     * 
     * @type {cc.Sprite}
     * @memberof Game_Over_Item
     */
    @property(cc.Sprite)
    imgHead: cc.Sprite = null;

    /**
     * 名字
     * 
     * @type {cc.Label}
     * @memberof Game_Over_Item
     */
    @property(cc.Label)
    lblName: cc.Label = null;
    /**
     * 玩家打牌过程中的一些操作描述
     * 
     * @type {cc.Label}
     * @memberof Game_Over_Item
     */
    @property(cc.Label)
    lblDes: cc.Label = null;

    /**
     * 加减分数
     * 
     * @type {cc.Label}
     * @memberof Game_Over_Item
     */
    @property(cc.Label)
    lblScore: cc.Label = null;
    /**
     * 番数
     * 
     * @type {cc.Label}
     * @memberof Game_Over_Item
     */
    @property(cc.Label)
    lblFan: cc.Label = null;
    /**
     * hu次数
     * 
     * @type {cc.Label}
     * @memberof Game_Over_Item
     */
    @property(cc.Label)
    lblHu: cc.Label = null;

    /**
     * 庄家节点
     * 
     * @type {cc.Node}
     * @memberof Game_Over_Item
     */
    @property(cc.Node)
    node_banker: cc.Node = null;

    /**
     * 碰、杠牌的父节点
     * 
     * @type {cc.Node}
     * @memberof Game_Over_Item
     */
    @property(cc.Node)
    node_group: cc.Node = null;

    /**
     * 手牌牌的父节点
     * 
     * @type {cc.Node}
     * @memberof Game_Over_Item
     */
    @property(cc.Node)
    node_hand: cc.Node = null;

    /**
     * 胡牌的父节点
     * 
     * @type {cc.Node}
     * @memberof Game_Over_Item
     */
    @property(cc.Node)
    node_hu: cc.Node = null;
    /**
     * 玩家列表
     * @type {cc.Label[]}
     * @memberof Game_Over_Item
     */
    @property([cc.Label])
    lbl_player_list: cc.Label[] = [];
    /**
     * 各个玩家的输赢
     * @type {cc.Label[]}
     * @memberof Game_Over_Item
     */
    @property([cc.Label])
    lbl_score_list: cc.Label[] = [];
    /**
     * canvas脚本
     * 
     * @memberof MJ_Table
     */
    _canvasTarget: MJCanvas = null;
    onLoad() {

    }

    /**
     * 刷新item信息
     * 
     * @memberof Game_Over_Item
     */
    async updateItem(index: number, data: SettlementOnceVo) {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');

        this.lblName.string = data.nick;
        this.lblScore.string = data.score + '';
        this.lblDes.string = data.huPaiDesc;
        this.lblFan.string = data.rate + '番';
        if (data.huPaiIndex > 0) {
            this.lblHu.node.active = true;
            this.lblHu.string = data.huPaiIndex + '胡';
        } else {
            this.lblHu.node.active = false;
        }
        this.node_light.active = index % 2 === 0 ? true : false;
        this.node_banker.active = data.banker === 0 ? false : true;

        for (let i = 0; i < this.lbl_player_list.length; i++) {
            if (i !== index) {
                this.lbl_player_list[i].string = '玩家' + (i + 1);
            }
        }
        //显示各个玩家对这个玩家的输赢
        for (let i = 0; i < this.lbl_score_list.length; i++) {
            let wlScore = 1;
            if (wlScore > 0) {
                this.lbl_player_list[i].string = '+' + wlScore;
            } else {
                this.lbl_player_list[i].string = wlScore.toString();
            }
        }
        this.node_group.removeAllChildren();
        this.node_hand.removeAllChildren();
        this.node_hu.removeAllChildren();

        let minGang = [];
        if (data.baGangCards) {
            minGang = data.baGangCards;
        }
        if (data.dianGangCards) {
            minGang = minGang.concat(data.dianGangCards);
        }
        if (minGang) {
            for (var i = 0; i < minGang.length; i++) {
                this._canvasTarget.showGroupCard(1, minGang[i], 0, this.node_group);
            }
        }
        if (data.anGangCards) {
            for (var i = 0; i < data.anGangCards.length; i++) {
                this._canvasTarget.showGroupCard(2, data.anGangCards[i], 0, this.node_group);
            }
        }
        if (data.pengCards) {
            for (var i = 0; i < data.pengCards.length; i++) {
                this._canvasTarget.showGroupCard(0, data.pengCards[i], 0, this.node_group);
            }
        }
        //如果没有杠牌和碰牌
        if (this.node_group.childrenCount === 0) {
            this.node_group.active = false;
        }
        if (data.handCards) {
            let handCards = dd.gm_manager.getSortCardByCardIds(data.handCards);
            for (var i = 0; i < handCards.length; i++) {
                this._canvasTarget.showMineCard(handCards[handCards.length - 1 - i], this.node_hand, false, (cardNode: cc.Node) => {
                    cardNode.scale = 0.8;
                    let mcm: MJ_Card = cardNode.getComponent('MJ_Card');
                    mcm.showMask(false);
                });
            }
        }
        if (data.huCards) {
            for (var i = 0; i < data.huCards.length; i++) {
                this._canvasTarget.showMineCard(data.huCards[i], this.node_hu, false, (cardNode: cc.Node) => {
                    cardNode.scale = 0.8;
                    let mcm: MJ_Card = cardNode.getComponent('MJ_Card');
                    mcm.showMask(false);
                });
            }
        }

        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(data.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.imgHead.spriteFrame = headSF;
    }
}
