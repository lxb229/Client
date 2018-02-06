import { ud_manager, ui_manager, mp_manager } from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Item extends cc.Component {

    @property(cc.Node)
    node_odd: cc.Node = null;           //特殊背景

    @property(cc.Label)
    lblClubName: cc.Label = null;      //俱乐部名称

    @property(cc.Label)
    lblClubId: cc.Label = null;        //俱乐部Id

    @property(cc.Label)
    lblClubState: cc.Label = null;        //俱乐部状态

    @property(cc.Label)
    lblClubNum: cc.Label = null;       //俱乐部人数

    @property(cc.Label)
    lblClubMoney: cc.Label = null;     //俱乐部房卡数量

    @property(cc.Sprite)
    img_use: cc.Sprite = null;        //房卡使用开关

    @property(cc.Button)
    btn_use: cc.Button = null;

    _itemData: CorpsVoInner = null;                            //俱乐部信息数据
    _cb = null;                             //item点击回调
    _target = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            mp_manager.playButton();
            if (this._cb) {
                this._cb(this._itemData);
            }
            event.stopPropagation();
        }, this);
    }

    updateItem(index: number, data: CorpsVoInner, cb, target) {
        this._itemData = data;
        this._cb = cb;
        this._target = target;

        this.lblClubName.string = data.corpsName;
        this.lblClubId.string = 'ID:' + data.corpsId;
        this.lblClubState.string = data.corpsState === 0 ? '' : '(已冻结)';
        this.lblClubNum.string = data.memberNum + '';
        this.lblClubMoney.string = data.roomCard + '';
        this.node_odd.active = index % 2 === 0 ? true : false;
        //如果群主是自己
        if (data.createPlayer === ud_manager.mineData.accountId) {
            this.btn_use.node.active = true;
            this.img_use.spriteFrame = data.state === 0 ? this._target.club_use_on_off_list[1] : this._target.club_use_on_off_list[0];
        } else {
            this.btn_use.node.active = false;
            this.img_use.spriteFrame = data.state === 0 ? this._target.club_use_on_off_list[3] : this._target.club_use_on_off_list[2];
        }
    }

    click_check_use() {
        mp_manager.playButton();
        if (this._itemData.state === 1) {
            ui_manager.showAlert('关闭后此群不在消耗房卡，<br/>群成员将不能再创建房间，<br/>是否关闭？', '解散俱乐部', {
                lbl_name: '是',
                callback: () => {
                    this._target.sendUseRoomCard(this._itemData.corpsId, 0);
                    this.img_use.spriteFrame = this._target.club_use_on_off_list[1];
                }
            }, {
                    lbl_name: '否',
                    callback: () => {
                        this.img_use.spriteFrame = this._target.club_use_on_off_list[0];
                    }
                }, 1);
        } else {
            this._target.sendUseRoomCard(this._itemData.corpsId, 1);
            this.img_use.spriteFrame = this._target.club_use_on_off_list[0];
        }
    }
}
