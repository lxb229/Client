
const { ccclass, property } = cc._decorator;

import MJCanvas from './MJCanvas';
import MJ_Card from './MJ_Card';
import * as dd from './../../Modules/ModuleManager';
import { MJ_Act_Type, MJ_Game_Type } from '../../Modules/Protocol';
/**
 * 绵阳麻将的躺
 * @export
 * @class MJ_Tang
 * @extends {cc.Component}
 */
@ccclass
export default class MJ_Tang extends cc.Component {

    /**
     * 牌父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Tang
     */
    @property(cc.Node)
    nodeLayout: cc.Node = null;

    @property(cc.Prefab)
    card_tang_prefab: cc.Prefab = null;
    /**
     * canvas脚本
     * 
     * @memberof MJ_Table
     */
    _canvasTarget: MJCanvas = null;

    /**
     * 调用的回调
     * 
     * @type {any}
     * @memberof MJ_Tang
     */
    _cb = null;

    _tangCfg: TangCfg = null;

    MJ_Tang_Touch_End = (event: cc.Event.EventTouch) => {
        let cardNode: cc.Node = event.currentTarget;

        let toggle = cardNode.getChildByName('toggle').getComponent(cc.Toggle);
        if (toggle) {
            toggle.isChecked = !toggle.isChecked;
        }
        event.stopPropagation();
    };
    onLoad() {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);
    }
    /**
     * 初始化数据
     * tableId [int] 桌子号
     * btcards [[B] 表态的牌
     * hucards [[B] 躺牌后胡的牌
     * outcard [byte] 躺牌后要打出的牌
     * @memberof MJCanvas
     */
    initData(tangCfg: TangCfg, cb) {
        this._cb = cb;
        this._tangCfg = tangCfg;
        if (tangCfg.cardIds && tangCfg.cardIds.length > 0) {
            tangCfg.cardIds.sort((a, b) => {
                return b - a;
            });
            this.nodeLayout.removeAllChildren();
            for (var i = 0; i < tangCfg.cardIds.length; i++) {
                this.updateCardData(tangCfg.cardIds[i]);
            }
        }
    }

    /**
     * 躺牌表态
     * tableId [int] 桌子号
     * btcards [[B] 表态的牌
     * hucards [[B] 躺牌后胡的牌
     * outcard [byte] 躺牌后要打出的牌
     * @memberof MJCanvas
     */
    sendTang(tangCfg: TangCfg) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'btcards': tangCfg.btcards,
            'hucards': tangCfg.hucards,
            'outcard': tangCfg.outcard,
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_TANG_CARD_BT, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
                this.node.active = false;
                dd.gm_manager._minScript.unShowTang();
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
            }
        });
    }

    /**
     * 刷新牌数据
     * 
     * @param {number} cardId 牌数据
     * @memberof MJ_Tang
     */
    updateCardData(cardId: number) {
        let cardNode = cc.instantiate(this.card_tang_prefab);
        cardNode.active = true;
        cardNode.setPosition(cc.p(0, 0));
        cardNode.tag = cardId;
        let cardImg = cardNode.getChildByName('cardImg');
        if (cardImg) {
            let csf: cc.SpriteFrame = this._canvasTarget.getMJCardSF(cardId);
            cardImg.getComponent(cc.Sprite).spriteFrame = csf;
        }
        let toggle = cardNode.getChildByName('toggle').getComponent(cc.Toggle);
        if (toggle) toggle.isChecked = false;
        cardNode.on("touchend", this.MJ_Tang_Touch_End, this);
        cardNode.parent = this.nodeLayout;
    }
    /**
     * 取消
     * @memberof MJ_Tang
     */
    click_btn_cancel() {
        dd.mp_manager.playButton();
        dd.gm_manager._minScript.unSelectCard();
        this.node.active = false;
    }
    /**
     * 确定
     * @memberof MJ_Tang
     */
    click_btn_ok() {
        dd.mp_manager.playButton();
        let tangList = [];
        this.nodeLayout.children.forEach(cardNode => {
            let toggle = cardNode.getChildByName('toggle').getComponent(cc.Toggle);
            if (toggle && toggle.isChecked) {
                tangList.push(cardNode.tag);
            }
        });
        if (tangList.length > 0) {
            cc.log('要躺的牌的列表' + tangList);
            let cards = this._tangCfg.cardIds.map((cardId) => {
                return dd.gm_manager.getCardById(cardId);
            }, this);
            let outcards = tangList.map((cardId) => {
                return dd.gm_manager.getCardById(cardId);
            }, this);
            let mySeat: SeatVo = dd.gm_manager.getSeatById(dd.ud_manager.mineData.accountId);
            let tangs = dd.gm_manager.getTingByTang(cards, outcards, mySeat.unSuit);
            let hucards = tangs.map((card: CardAttrib) => {
                return card.cardId;
            });
            cc.log('听牌列表' + this._tangCfg.hucards);
            cc.log('躺牌后胡牌列表' + hucards);
            if (tangs.length > 0) {
                this._tangCfg.hucards = hucards;
                this._tangCfg.btcards = tangList;
                this.sendTang(this._tangCfg);
            } else {
                this.nodeLayout.children.forEach(cardNode => {
                    let toggle = cardNode.getChildByName('toggle').getComponent(cc.Toggle);
                    if (toggle) {
                        toggle.isChecked = false;
                    }
                });
                dd.ui_manager.showTip('请选择正确的躺牌！');
            }
        } else {
            //南充麻将
            if (dd.gm_manager.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_NCMJ) {
                dd.ui_manager.showTip('请选择要摆的牌！');
            } else {
                dd.ui_manager.showTip('请选择要躺的牌！');
            }
        }
    }
}
