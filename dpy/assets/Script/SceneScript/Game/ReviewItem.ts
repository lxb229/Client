const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class ReviewItem extends cc.Component {
    /**
     * 头像
     * 
     * @type {cc.Sprite}
     * @memberof ReviewItem
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 手牌1
     * 
     * @type {cc.Sprite}
     * @memberof ReviewItem
     */
    @property(cc.Sprite)
    card1: cc.Sprite = null;
    /**
     * 手牌2
     * 
     * @type {cc.Sprite}
     * @memberof ReviewItem
     */
    @property(cc.Sprite)
    card2: cc.Sprite = null;
    /**
     * 昵称
     * 
     * @type {cc.Label}
     * @memberof ReviewItem
     */
    @property(cc.Label)
    nick: cc.Label = null;
    /**
     * 输赢量
     * 
     * @type {cc.Label}
     * @memberof ReviewItem
     */
    @property(cc.Label)
    gold: cc.Label = null;

    async init(data: TablePrevFightRecordItem) {
        this.nick.string = dd.utils.getStringBySize(data.nick, 12);
        this.gold.string = data.score;
        //如果是自己
        if (data.starNO === dd.ud_manager.account_mine.roleAttribVo.starNO) {
            if (data.handCards && data.handCards.length > 0) {
                this.card1.spriteFrame = dd.img_manager.getPokerSpriteFrameById(data.handCards[0]);
                this.card2.spriteFrame = dd.img_manager.getPokerSpriteFrameById(data.handCards[1]);
            } else {
                this.card1.spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
                this.card2.spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
            }
        } else {
            if (data.handCards && data.handCards.length > 0 && data.showCardState === 1) {
                this.card1.spriteFrame = dd.img_manager.getPokerSpriteFrameById(data.handCards[0]);
                this.card2.spriteFrame = dd.img_manager.getPokerSpriteFrameById(data.handCards[1]);
            } else {
                this.card1.spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
                this.card2.spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
            }
        }
        this.headImg.spriteFrame = await dd.img_manager.loadURLImage(data.headImg);
    }
}
