const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Record extends cc.Component {
    /**
     * 房主头像
     * 
     * @type {cc.Sprite}
     * @memberof MineItem
     */
    @property(cc.Sprite)
    head: cc.Sprite = null;
    /**
     * 房间名称
     * 
     * @type {cc.Label}
     * @memberof Record
     */
    @property(cc.Label)
    room: cc.Label = null;
    /**
     * 房主昵称
     * 
     * @type {cc.Label}
     * @memberof Record
     */
    @property(cc.Label)
    nick: cc.Label = null;
    /**
     * 玩家输赢值
     * 
     * @type {cc.Label}
     * @memberof Record
     */
    @property(cc.Label)
    gold: cc.Label = null;
    /**
     * 结算时间
     * 
     * @type {cc.Label}
     * @memberof Record
     */
    @property(cc.Label)
    time: cc.Label = null;

    _data: CareerHistory = null;
    _cb: any = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            if (this._cb) {
                this._cb(this._data);
            }
        }, this);
    }
    async updateItem(data: CareerHistory, callback?: Function) {
        if (data) {
            this._data = data;
            this._cb = callback;
            this.nick.string = dd.utils.getStringBySize(data.nick, 12);
            this.time.string = dd.utils.getDateStringByTimestamp(data.recordTime);
            this.gold.string = data.winMoney;
            this.room.string = data.tableName;
            this.head.spriteFrame = await dd.img_manager.loadURLImage(data.headImg);
        }
    }
}
