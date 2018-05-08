const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import MJ_Card from './MJ_Card';
import { MJ_Game_Type } from '../../Modules/Protocol';

@ccclass
export default class Game_Over_Item extends cc.Component {
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
     * 南充麻将的飘
     * @type {cc.Label}
     * @memberof Game_Over_Item
     */
    @property(cc.Label)
    lblPiao: cc.Label = null;
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
     * 躺牌的父节点
     * @type {cc.Node}
     * @memberof Game_Over_Item
     */
    @property(cc.Node)
    node_tang: cc.Node = null;
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
     * 各个玩家的输赢列表
     * @type {cc.Label[]}
     * @memberof Game_Over_Item
     */
    @property([cc.RichText])
    lbl_player_list: cc.RichText[] = [];

    onLoad() {

    }

    /**
     * 刷新item信息
     * 
     * @memberof Game_Over_Item
     */
    async updateItem(index: number, data: SettlementOnceVo) {

        this.lblName.string = dd.utils.getStringBySize(data.nick, 12);
        if (data.accountId === dd.ud_manager.mineData.accountId) {
            this.lblName.node.color = cc.Color.YELLOW;
        }
        this.lblScore.node.color = data.score > 0 ? cc.Color.RED : cc.Color.GREEN;
        this.lblScore.string = data.score > 0 ? ('+' + data.score) : ('' + data.score);
        this.lblDes.string = data.huPaiDesc;
        this.lblFan.string = data.rate + '番';
        if (data.huPaiIndex > 0) {
            this.lblHu.node.active = true;
            this.lblHu.string = data.huPaiIndex + '胡';
        } else {
            this.lblHu.node.active = false;
        }
        this.node_banker.active = data.banker === 0 ? false : true;

        if (data.seatScore) {
            let pScore = [];
            //南充麻将
            if (dd.gm_manager.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_NCMJ) {
                this.lblPiao.node.parent.active = true;
                this.lblPiao.string = 'x' + data.myPiaoNum;
                this.lblHu.node.active = false;
                data.seatScore.forEach((sScore, sIndex) => {
                    let str = '玩家<index>:  <color=#56000E><piao>  </c><bai>  <color><socre></c>';
                    if (sIndex !== index) {
                        str = str.replace('<index>', (sIndex + 1).toString());
                        str = str.replace('<color>', (sScore.score > 0 ? '<color=#FF0000>' : '<color=#00FF00>'));
                        str = str.replace('<socre>', (sScore.score > 0 ? ('+' + sScore.score) : ('' + sScore.score)));
                        str = str.replace('<piao>', (sScore.winPiaoNum > 0 ? ('飘+' + sScore.winPiaoNum) : ('飘' + sScore.winPiaoNum)));
                        switch (sScore.tangNum) {
                            case 0:
                                str = str.replace('<bai>', '(无摆)');
                                break;
                            case 1:
                                str = str.replace('<bai>', '(单摆)');
                                break;
                            case 2:
                                str = str.replace('<bai>', '(双摆)');
                                break;
                            default:
                                str = str.replace('<br/><bai>', '');
                                break;
                        }
                        pScore.push(str);
                    }
                });
            } else {
                this.lblPiao.node.parent.active = false;
                data.seatScore.forEach((sScore, sIndex) => {
                    let str = '玩家<index>:  <color=#56000E><number>番  </c><color><socre>  </c><tang><baojiao>';
                    if (sIndex !== index) {
                        str = str.replace('<index>', (sIndex + 1).toString());
                        str = str.replace('<number>', sScore.totalFanNum.toString());
                        str = str.replace('<color>', (sScore.score > 0 ? '<color=#FF0000>' : '<color=#00FF00>'));
                        str = str.replace('<socre>', (sScore.score > 0 ? ('+' + sScore.score) : ('' + sScore.score)));
                        switch (sScore.tangNum) {
                            case 0:
                                str = str.replace('<tang>', '(无躺)');
                                break;
                            case 1:
                                str = str.replace('<tang>', '(单躺)');
                                break;
                            case 2:
                                str = str.replace('<tang>', '(双躺)');
                                break;
                            default:
                                str = str.replace('<br/><tang>', '');
                                break;
                        }
                        switch (sScore.baoJiaoNum) {
                            case 0:
                                str = str.replace('<baojiao>', '(无叫)');
                                break;
                            case 1:
                                str = str.replace('<baojiao>', '(单叫)');
                                break;
                            case 2:
                                str = str.replace('<baojiao>', '(双叫)');
                                break;
                            default:
                                str = str.replace('<br/><baojiao>', '');
                                break;
                        }
                        pScore.push(str);
                    }
                });
            }
            for (let i = 0; i < this.lbl_player_list.length; i++) {
                if (i < data.seatScore.length - 1 && pScore[i]) {
                    this.lbl_player_list[i].node.active = true;
                    this.lbl_player_list[i].string = pScore[i];
                } else {
                    this.lbl_player_list[i].node.active = false;
                    this.lbl_player_list[i].string = '';
                }
            }
        }

        this.node_group.removeAllChildren();

        if (data.dianGangCards) {
            for (var i = 0; i < data.dianGangCards.length; i++) {
                dd.gm_manager.getGMTarget().showGroupCard(3, data.dianGangCards[i], 0, this.node_group);
            }
        }
        if (data.baGangCards) {
            for (var i = 0; i < data.baGangCards.length; i++) {
                dd.gm_manager.getGMTarget().showGroupCard(1, data.baGangCards[i], 0, this.node_group);
            }
        }
        if (data.anGangCards) {
            for (var i = 0; i < data.anGangCards.length; i++) {
                dd.gm_manager.getGMTarget().showGroupCard(2, data.anGangCards[i], 0, this.node_group);
            }
        }
        if (data.pengCards) {
            for (var i = 0; i < data.pengCards.length; i++) {
                dd.gm_manager.getGMTarget().showGroupCard(0, data.pengCards[i], 0, this.node_group);
            }
        }
        //如果没有杠牌和碰牌
        if (this.node_group.childrenCount === 0) {
            this.node_group.active = false;
        }
        let handCards = data.handCards;
        let seatInfo = dd.gm_manager.getSeatById(data.accountId);
        if (seatInfo && seatInfo.tangCardList && seatInfo.tangCardList.length > 0) {
            this.node_tang.active = true;
            this.node_tang.removeAllChildren();
            let tangCardList = seatInfo.tangCardList.sort((a, b) => {
                return a - b;
            });
            let mymjScript = dd.ui_manager.getCanvasNode().getComponent('MYMJScene');
            for (var i = 0; i < tangCardList.length; i++) {
                mymjScript.showTangCard(0, tangCardList[i], this.node_tang);
            }
            handCards = dd.gm_manager.getDiffAToB(data.handCards, tangCardList);
        } else {
            this.node_tang.active = false;
        }
        if (handCards && handCards.length > 0) {
            this.node_hand.active = true;
            this.node_hand.removeAllChildren();
            handCards = dd.gm_manager.getSortCardByCardIds(handCards);
            for (var i = 0; i < handCards.length; i++) {
                dd.gm_manager.getGMTarget().showMineCard(handCards[handCards.length - 1 - i], this.node_hand, false, (cardNode: cc.Node) => {
                    cardNode.scale = 0.8;
                    let mcm: MJ_Card = cardNode.getComponent('MJ_Card');
                    mcm.showMask(false);
                });
            }
        } else {
            this.node_hand.active = false;
        }

        if (data.huCards) {
            this.node_hu.active = true;
            this.node_hu.removeAllChildren();
            for (var i = 0; i < data.huCards.length; i++) {
                dd.gm_manager.getGMTarget().showMineCard(data.huCards[i], this.node_hu, false, (cardNode: cc.Node) => {
                    cardNode.scale = 0.8;
                    let mcm: MJ_Card = cardNode.getComponent('MJ_Card');
                    mcm.showMask(false);
                });
            }
        } else {
            this.node_hu.active = false;
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
