const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_Card extends cc.Component {
    /**
     * 牌
     * @type {cc.Sprite}
     * @memberof Game_Card
     */
    @property(cc.Sprite)
    card_img: cc.Sprite = null;
    /**
     * 牌背
     * @type {cc.Node}
     * @memberof Game_Card
     */
    @property(cc.Node)
    backNode: cc.Node = null;
    /**
     * 牌ID
     * @type {number}
     * @memberof Game_Card
     */
    _cardId: number = 0;
    onLoad() {

    }
    /**
     * 初始化牌数据
     * 
     * @param {number} cardId  牌数据
     * @param {cc.SpriteFrame} cardSF 牌图片
     * @param {boolean} isShow 是否显示
     * @memberof Game_Card
     */
    initData(cardId: number, cardSF: cc.SpriteFrame, isShow: boolean) {
        this._cardId = cardId;
        this.card_img.spriteFrame = cardSF;
        if (this._cardId > 1) {
            this.backNode.active = !isShow;
        } else {
            this.backNode.active = false;
        }
    }
}
