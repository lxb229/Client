const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class DetailItem extends cc.Component {
    /**
     * 房主头像
     * 
     * @type {cc.Sprite}
     * @memberof MineItem
     */
    @property(cc.Sprite)
    head: cc.Sprite = null;
    /**
     * 索引
     * 
     * @type {cc.Label}
     * @memberof DetailItem
     */
    @property(cc.Label)
    index: cc.Label = null;
    /**
     * 玩家昵称
     * 
     * @type {cc.Label}
     * @memberof DetailItem
     */
    @property(cc.Label)
    nick: cc.Label = null;
    /**
     * 玩家输赢值
     * 
     * @type {cc.Label}
     * @memberof DetailItem
     */
    @property(cc.Label)
    gold: cc.Label = null;
    /**
     * 游戏局数
     * 
     * @type {cc.Label}
     * @memberof DetailItem
     */
    @property(cc.Label)
    num: cc.Label = null;

    async updateItem(data: any, callback?: Function) {
        if (data) {
            let item: TablePlayerListItem = data.item;
            this.index.string = data.index + 1;
            this.nick.string = dd.utils.getStringBySize(item.nick, 12);
            this.gold.string = item.winMoney;
            this.num.string = item.gameNum;
            this.head.spriteFrame = await dd.img_manager.loadURLImage(item.headImg);
        }
    }
}
