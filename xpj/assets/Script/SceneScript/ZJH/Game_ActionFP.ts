const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_ActionFP extends cc.Component {
    /**
     * 牌节点
     * 
     * @type {cc.Node}
     * @memberof Game_ActionFP
     */
    @property(cc.Node)
    cardNode: cc.Node = null;

    @property(cc.Sprite)
    cardImg: cc.Sprite = null;
    /**
     * 回调函数
     * 
     * @memberof Game_ActionFP
     */
    _cb = null;
    /**
     * 牌数据
     * 
     * @memberof Game_ActionFP
     */
    _data = null;

    onLoad() {

    }

    /**
     *初始化翻牌的数据
     * @param {number} cardId 牌数据
     * @param {cc.SpriteFrame} cardImg 牌图片
     * @param {*} [cb] 翻牌的回调函数
     * @param {*} [data] 翻牌的回调数据
     * @memberof Game_ActionFP
     */
    initData(cardId: number, cardImg: cc.SpriteFrame, cb?: any, data?: any) {
        if (cardId > -1 && cardImg) {
            this.cardImg.spriteFrame = cardImg;
        }

        if (cb) {
            this._cb = cb;
        }
        if (data !== null || data !== undefined) {
            this._data = data;
        }
    }

    /**
     * 翻牌回调
     * 
     * @memberof Game_ActionFP
     */
    fpActionEnd() {
        if (this._cb) {
            if (this._data !== null || this._data !== undefined) {
                this._cb(this._data);
            } else {
                this._cb();
            }
        }
        // this.node.removeFromParent(true);
        // this.node.destroy();
    }
}
