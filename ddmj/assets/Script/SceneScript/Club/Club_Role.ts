const { ccclass, property } = cc._decorator;
import *  as dd from './../../Modules/ModuleManager';
@ccclass
export default class Club_Role extends cc.Component {

    @property(cc.Label)
    lblName: cc.Label = null;

    @property(cc.Label)
    lblIP: cc.Label = null;

    @property(cc.Label)
    lblID: cc.Label = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;

    @property(cc.Sprite)
    sexImg: cc.Sprite = null;

    @property(cc.Button)
    btn_kike: cc.Button = null;

    @property([cc.SpriteFrame])
    sex_img: cc.SpriteFrame[] = [];

    _roleInfo: UserData = null;
    _canvasScript = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this.node.active = false;
            event.stopPropagation();
        }, this);
    }

    /**
     * 显示信息
     * 
     * @memberof Club_Role
     */
    async showInfo(roleInfo: UserData, clubInfo: CorpsVoInner, target) {
        this._roleInfo = roleInfo;
        this._canvasScript = target;
        this.lblName.string = roleInfo.nick;
        this.lblIP.string = 'IP: ' + roleInfo.clientIP;
        this.lblID.string = 'ID: ' + roleInfo.starNO;

        if (clubInfo.createPlayer === dd.ud_manager.mineData.accountId) {
            if (roleInfo.starNO === dd.ud_manager.mineData.starNO) {
                this.btn_kike.node.active = false;
            } else {
                this.btn_kike.node.active = true;
            }
        } else {
            this.btn_kike.node.active = false;
        }
        if (roleInfo.sex > 0) {
            this.sexImg.node.active = true;
            this.sexImg.spriteFrame = this.sex_img[roleInfo.sex - 1];
        } else {
            this.sexImg.node.active = false;
        }
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(roleInfo.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.headImg.spriteFrame = headSF;
    }

    click_btn_kike() {
        dd.mp_manager.playButton();
        this._canvasScript.sendKikMember(this._roleInfo.starNO);
    }

    click_btn_cancel() {
        dd.mp_manager.playButton();
        this.node.active = false;
    }
}
