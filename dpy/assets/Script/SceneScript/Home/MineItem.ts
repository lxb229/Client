const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class MineItem extends cc.Component {
    /**
     * 房主头像
     * 
     * @type {cc.Sprite}
     * @memberof MineItem
     */
    @property(cc.Sprite)
    head: cc.Sprite = null;
    /**
     * 房主昵称
     * 
     * @type {cc.Label}
     * @memberof MineItem
     */
    @property(cc.Label)
    nick: cc.Label = null;
    /**
     * 大小盲注
     * 
     * @type {cc.Label}
     * @memberof MineItem
     */
    @property(cc.Label)
    blind: cc.Label = null;
    /**
     * 最小买入
     * 
     * @type {cc.Label}
     * @memberof MineItem
     */
    @property(cc.Label)
    min: cc.Label = null;
    /**
     * 剩余时间
     * 
     * @type {cc.Label}
     * @memberof MineItem
     */
    @property(cc.Label)
    time: cc.Label = null;
    /**
     * 现有人数
     * 
     * @type {cc.Label}
     * @memberof MineItem
     */
    @property(cc.Label)
    count: cc.Label = null;
    /**
     * 我的牌局对象
     * 
     * @type {JoinedTableItem}
     * @memberof MineItem
     */
    data: JoinedTableItem = null;
    /**
     * 点击节点触发的回调方法
     * 
     * @type {Function}
     * @memberof MineItem
     */
    callback: Function = null;

    async updateItem(data: JoinedTableItem, callback?: Function) {
        this.data = data;
        this.callback = callback;
        if (this.data) {
            this.nick.string = dd.utils.getStringBySize(data.nick, 12);
            this.blind.string = data.small + '/' + data.big;
            this.min.string = data.minJoin.toString();
            this.count.string = data.currPlayer + '/' + data.seatNum;
            this.head.spriteFrame = await dd.img_manager.loadURLImage(data.headImg);
        }
    }

    update(dt: number) {
        if (this.data) {
            let endTime = Number(this.data.vaildTime);
            if (endTime > 0) {
                endTime -= dt * 1000;
                if (endTime < 0) {
                    endTime = 0;
                }
                this.data.vaildTime = endTime.toString();
                this.time.string = dd.utils.getCountDownString(Number(this.data.vaildTime));
            } else {
                this.data = null;
                this.node.destroy();
            }
        }
    }
    /**
     * 点击当前节点
     * 
     * @memberof MineItem
     */
    click_item() {
        if (this.callback) {
            this.callback(this.data);
        }
    }
}
