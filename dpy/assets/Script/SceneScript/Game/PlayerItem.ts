const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class PlayerItem extends cc.Component {
    /**
     * 头像
     * 
     * @type {cc.Sprite}
     * @memberof PlayerItem
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 用户昵称
     * 
     * @type {cc.Label}
     * @memberof PlayerItem
     */
    @property(cc.Label)
    nick: cc.Label = null;
    /**
     * 积分数
     * 
     * @type {cc.Label}
     * @memberof PlayerItem
     */
    @property(cc.Label)
    gold: cc.Label = null;
    /**
     * 参与局数
     * 
     * @type {cc.Label}
     * @memberof PlayerItem
     */
    @property(cc.Label)
    count: cc.Label = null;
    /**
     * 输赢量
     * 
     * @type {cc.Label}
     * @memberof PlayerItem
     */
    @property(cc.Label)
    win: cc.Label = null;

    async init(data: TablePlayerListItem) {
        this.headImg.spriteFrame = await dd.img_manager.loadURLImage(data.headImg);
        this.nick.string = dd.utils.getStringBySize(data.nick, 12);
        this.gold.string = data.currMoney;
        this.count.string = data.gameNum;
        this.win.string = data.winMoney;
    }
}
