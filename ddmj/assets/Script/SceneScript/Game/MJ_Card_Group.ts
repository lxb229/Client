const { ccclass, property } = cc._decorator;

@ccclass
export default class NewClass extends cc.Component {

    @property([cc.Node])
    node_card_list: cc.Node[] = [];

    @property([cc.Sprite])
    imgCardList: cc.Sprite[] = [];

    @property([cc.Node])
    node_back_list: cc.Node[] = [];

    /**
      * 麻将数据
      * 
      * @type {number}
      * @memberof MJ_Card
      */
    _cardId: number = 0;
    onLoad() {

    }

    /**
     * 初始化杠/碰 牌数据
     * 
     * @param {number} cardId  牌数据
     * @param {cc.SpriteFrame} cardSF 图片
     * @param {number} type  类型 0:碰 1:明杠 2:暗杠
     * @memberof NewClass
     */
    initData(type: number, cardId: number, cardSF: cc.SpriteFrame) {
        this._cardId = cardId;
        for (var i = 0; i < this.imgCardList.length; i++) {
            this.imgCardList[i].spriteFrame = cardSF;
        }

        for (var i = 0; i < this.node_back_list.length; i++) {
            if (i < 3) {
                if (type !== 0) {//如果是杠牌
                    this.node_back_list[i].active = type === 1 ? false : true;
                } else {
                    this.node_back_list[i].active = false;
                }
            } else {
                if (type !== 0) {//如果是杠牌
                    this.node_card_list[i].active = true;
                    this.node_back_list[i].active = false;
                } else {
                    this.node_card_list[i].active = false;
                }
            }
        }
    }
}
