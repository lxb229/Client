import { mp_manager } from "../../Modules/ModuleManager";

const { ccclass, property } = cc._decorator;

@ccclass
export default class MJ_Card extends cc.Component {

    /**
     * 牌图片
     * 
     * @type {cc.Sprite}
     * @memberof MJ_Card
     */
    @property(cc.Sprite)
    cardImg: cc.Sprite = null;

    /**
     * 标识
     * 
     * @type {cc.Node}
     * @memberof MJ_Card
     */
    @property(cc.Node)
    bsNode: cc.Node = null;

    /**
     * 牌亮光
     * 
     * @type {cc.Node}
     * @memberof MJ_Card
     */
    @property(cc.Node)
    light: cc.Node = null;
    /**
     * 牌背景
     * 
     * @type {cc.Node}
     * @memberof MJ_Card
     */
    @property(cc.Node)
    cardBack: cc.Node = null;

    /**
     * 牌遮罩(手牌有)
     * 
     * @type {cc.Node}
     * @memberof MJ_Card
     */
    @property(cc.Node)
    maskNode: cc.Node = null;

    /**
     * 是否显示了遮罩
     * 
     * @type {boolean}
     * @memberof MJ_Card
     */
    _isShowMask: boolean = false;
    /**
     * 是否被选中
     * 
     * @type {boolean}
     * @memberof MJ_Card
     */
    _isSelect: boolean = false;

    /**
     * 没有被选中的时候，普通状态的Y坐标
     * 
     * @type {number}
     * @memberof MJ_Card
     */
    _nomalY: number = 0;
    /**
     * 选中这个麻将后，选中状态的Y坐标
     * 
     * @type {number}
     * @memberof MJ_Card
     */
    _chooseY: number = 0;

    /**
     * 麻将数据唯一id
     * 
     * @type {number}
     * @memberof MJ_Card
     */
    _cardId: number = 0;
    onLoad() {
        this._nomalY = 0;
        this._chooseY = 36;
    }

    unuse() {
        this.node.active = true;
        this.node.stopAllActions();
        if (this.cardImg) {
            this.cardImg.node.active = false;
            this.cardImg.spriteFrame = null;
        }
        if (this.cardBack) {
            this.cardBack.active = false;
        }
        if (this.bsNode) {
            this.bsNode.active = false;
        }
        if (this.light) {
            this.light.active = false;
        }
        if (this.maskNode) {
            this.maskNode.active = false;
        }
        this.node.x = 0;
        this.node.y = 0;
        this.node.opacity = 255;
        this.node.scaleX = Math.abs(this.node.scaleX);
        this.node.scaleY = Math.abs(this.node.scaleY);
        this.cardImg.node.scaleX = Math.abs(this.cardImg.node.scaleX);
        this.cardImg.node.scaleY = Math.abs(this.cardImg.node.scaleY);
        this._isSelect = false;
        this._isShowMask = false;
        this._cardId = 0;
    }

    /**
     * 初始化麻将数据
     * 
     * @param {number} cardId 麻将数据
     * @param {cc.SpriteFrame} [cardSF=null] 麻将图片,默认null
     * @param {boolean} [isShow=false] 麻将是否显示背景图片(如果存在背景),默认false
     * @memberof MJ_Card
     */
    initData(cardId: number, cardSF: cc.SpriteFrame = null, isShow: boolean = false) {
        this._cardId = cardId;
        this.node.active = true;
        this.node.opacity = 255;

        if (this.cardBack) {
            this.cardBack.active = isShow;
        }
        if (this.cardImg) {
            this.cardImg.node.active = true;
            this.cardImg.spriteFrame = cardSF;
        }
        if (this.maskNode) {
            this.maskNode.active = false;
        }
        if (this.light) {
            this.light.active = isShow;
        }
        if (this.bsNode) {
            this.bsNode.active = false;
        }
    }

    /**
     * 修正card的显示方向
     * 
     * @param {cc.Vec2} nodefix  节点的修正
     * @param {cc.Vec2} imgfix   图片的修正
     * @memberof MJ_Card
     */
    setFixCard(nodefix: cc.Vec2, imgfix: cc.Vec2) {
        this.node.scaleX *= nodefix.x;
        this.node.scaleY *= nodefix.y;
        this.cardImg.node.scaleX *= imgfix.x;
        this.cardImg.node.scaleY *= imgfix.y;
    }

    /**
     * 是否显示遮罩
     * 
     * @param {boolean} isShowMask 
     * @memberof MJ_Card
     */
    showMask(isShowMask: boolean) {
        if (this.maskNode) {
            this._isShowMask = isShowMask;
            this.maskNode.active = isShowMask === true ? true : false;
        }
    }

    /**
     * 显示是否选中这个麻将
     * 
     * @param {boolean} isSelect 
     * @memberof MJ_Card
     */
    showSelectCard(isSelect: boolean) {
        this._isSelect = isSelect;
        if (isSelect) {
            this.node.y = this._chooseY;
            mp_manager.playSelect();
        } else {
            this.node.y = this._nomalY;
        }
    }

    /**
     * 是否显示标识
     * 
     * @param {boolean} isShow 
     * @param {number} fx 修正x的坐标
     * @param {number} fr 修正node的角度
     * @memberof MJ_Card
     */
    showBS(isShow: boolean, fx: number = 1, fr: number = 1) {
        if (this.bsNode) {
            this.bsNode.active = isShow;
            if (isShow) {
                this.bsNode.y = Math.abs(this.bsNode.y) * fx;
                this.bsNode.rotation = Math.abs(this.bsNode.rotation) * fr;
            }
        }
    }
    /**
     * 显示牌的光
     * 
     * @param {boolean} isShow 
     * @memberof MJ_Card
     */
    showLight(isShow: boolean) {
        if (this.light) {
            this.light.active = isShow;
        }
    }
}
