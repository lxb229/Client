import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Table_Item extends cc.Component {

    /**
     * 游戏状态
     * 
     * @type {cc.Sprite}
     * @memberof Club_Table_Item
     */
    @property(cc.Sprite)
    img_state: cc.Sprite = null;
    /**
     * 头像
     * @type {[cc.Sprite]}
     * @memberof Club_Table_Item
     */
    @property([cc.Sprite])
    headImg_list: cc.Sprite[] = [];

    @property(cc.Label)
    lblGameNum: cc.Label = null;

    _itemData: CorpsTableInner = null;       //俱乐部信息数据
    _cb = null;                             //item点击回调
    _target = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            if (this._cb) {
                this._cb(this._itemData);
            }
            event.stopPropagation();
        }, this);
    }

    updateItem(data: CorpsTableInner, cb, target) {
        this._itemData = data;
        this._cb = cb;
        this._target = target;

        this.showInfo();
    }

    showInfo() {
        let player = 0;
        for (var i = 0; i < this.headImg_list.length; i++) {
            let seatInfo = this._itemData.seats[i];
            if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '' && seatInfo.accountId !== '0') {
                player++;
                this.showHead(i, seatInfo);
            } else {
                this.headImg_list[i].node.active = false;
            }
        }

        if (player >= this.headImg_list.length) {
            this.lblGameNum.string = '8/10局';
            this.img_state.spriteFrame = this._target.table_state_list[0];
        } else {
            this.lblGameNum.string = '';
            this.img_state.spriteFrame = this._target.table_state_list[1];
        }
    }

    async showHead(index: number, seatInfo) {
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(seatInfo.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        if (this.headImg_list[index]) {
            this.headImg_list[index].node.active = true;
            this.headImg_list[index].spriteFrame = headSF;
        }
    }
}
