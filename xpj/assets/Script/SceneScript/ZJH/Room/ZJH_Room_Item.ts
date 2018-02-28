import { utils } from './../../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ZJH_Room_Item extends cc.Component {
    /**
     * 等级场
     * @type {cc.Label}
     * @memberof ZJH_Room_Item
     */
    @property(cc.Label)
    lblGrade: cc.Label = null;
    /**
     * 进入限制
     * @type {cc.Label}
     * @memberof ZJH_Room_Item
     */
    @property(cc.Label)
    lblLimit: cc.Label = null;
    /**
     * 底分
     * @type {cc.Label}
     * @memberof ZJH_Room_Item
     */
    @property(cc.Label)
    lblBase: cc.Label = null;
    /**
     * 封顶上限
     * @type {cc.Label}
     * @memberof ZJH_Room_Item
     */
    @property(cc.Label)
    lblMax: cc.Label = null;
    /**
     * 底板图片
     * @type {cc.Sprite}
     * @memberof ZJH_Room_Item
     */
    @property(cc.Sprite)
    img_board: cc.Sprite = null;
    /**
     * item数据
     * 
     * @memberof ZJH_Room_Item
     */
    _itemData: RoomCfgItem = null;
    /**
     * 点击回调函数
     * @memberof ZJH_Room_Item
     */
    _cb = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            if (this._cb) {
                this._cb(this._itemData);
            }
            event.stopPropagation();
        }, this);
    }

    /**
     * 刷新数据
     * @param {any} data 
     * @param {any} target 父节点场景脚本
     * @param {any} cb 
     * @memberof ZJH_Room_Item
     */
    updateItem(data: RoomCfgItem, index: number, target, cb) {
        this._itemData = data;
        this._cb = cb;

        if (data) {
            this.img_board.spriteFrame = target.board_img_list[index];
            this.lblGrade.string = utils.getShowNumberString(data.joinLimit);
            this.lblBase.string = utils.getShowNumberString(data.joinLimit) + '金币准入';
            this.lblLimit.string = '底注' + utils.getShowNumberString(data.baseScore) + '金币';
            this.lblMax.string = '单注封顶' + utils.getShowNumberString(data.onceMax) + '金币';
        }
    }
}
