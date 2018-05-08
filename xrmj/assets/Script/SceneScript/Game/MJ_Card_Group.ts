import * as dd from './../../Modules/ModuleManager';
import { MJ_Game_Type, MJ_Suit } from '../../Modules/Protocol';
const { ccclass, property } = cc._decorator;

@ccclass
export default class MJ_Card_Group extends cc.Component {

    @property([cc.Node])
    node_mj_list: cc.Node[] = [];

    @property([cc.Node])
    node_card_list: cc.Node[] = [];

    @property([cc.Sprite])
    img_card_list: cc.Sprite[] = [];

    @property([cc.Node])
    node_back_list: cc.Node[] = [];

    @property([cc.SpriteFrame])
    icon_gang_list: cc.SpriteFrame[] = [];
    /**
      * 麻将数据
      * 
      * @type {number}
      * @memberof MJ_Card
      */
    _cards: number[] = [];
    onLoad() {

    }

    /**
     * 初始化杠/碰 牌数据
     * 
     * @param {number} cards  牌数据列表
     * @param {cc.SpriteFrame} cardSF_list 图片列表
     * @param {number} type  类型 0:碰 1:吧杠 2:暗杠 3:直杠
     * @memberof MJ_Card_Group
     */
    initData(type: number, cards: number[], cardSF_list: cc.SpriteFrame[]) {
        this._cards = cards;
        this.node_mj_list.forEach((mjNode: cc.Node, i: number) => {
            //前三张牌
            if (i < 3) {
                mjNode.active = true;
                //暗杠不显示前三张牌，碰 或 其他的杠都要显示
                this.node_back_list[i].active = type === 2 ? true : false;
                this.node_card_list[i].active = type === 2 ? false : true;
            } else {
                //如果是杠牌，第四张牌显示，如果不是，则不显示
                if (type !== 0) {
                    mjNode.active = true;
                    //第四张牌，暗杠显示，其他杠显示
                    this.node_back_list[i].active = false;
                    this.node_card_list[i].active = true;
                } else {
                    mjNode.active = false;
                }
            }
            this.img_card_list[i].spriteFrame = cardSF_list[i];

            //如果是乐山麻将
            if (dd.gm_manager.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_LSMJ) {
                let isLSYaoJi = dd.gm_manager.getLSMJ_IsYaoJi_ByCardId(cards[i]);
                let clon = this.node_card_list[i].getChildByName('clon');
                if (isLSYaoJi) {
                    if (clon) clon.active = true;
                } else {
                    if (clon) clon.active = false;
                }
                if (dd.gm_manager.mjGameData.tableBaseVo.yaojiReplace === 1) {
                    //如果是乐山麻将，开启幺鸡任用，就都显示出牌
                    this.node_back_list[i].active = false;
                    this.node_card_list[i].active = true;
                }
            }
        });

        //如果是乐山麻将
        if (dd.gm_manager.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_LSMJ) {
            let icon_gang = this.node.getChildByName('icon_gang');
            if (icon_gang) {
                if (dd.gm_manager.mjGameData.tableBaseVo.yaojiReplace === 1) {
                    icon_gang.active = true;
                    icon_gang.getComponent(cc.Sprite).spriteFrame = this.icon_gang_list[type - 1];
                } else {
                    icon_gang.active = false;
                }
            }
        }
    }
}
