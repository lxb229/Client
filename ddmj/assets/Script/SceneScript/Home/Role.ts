const { ccclass, property } = cc._decorator;
import *  as dd from './../../Modules/ModuleManager';
@ccclass
export default class Role extends cc.Component {

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

    @property([cc.SpriteFrame])
    sex_img: cc.SpriteFrame[] = [];
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    /**
     * 显示信息
     * 
     * @memberof Role
     */
    async showInfo(roleInfo: UserData) {
        this.lblName.string = roleInfo.nick;
        this.lblIP.string = 'IP: ' + roleInfo.clientIP;
        this.lblID.string = 'ID: ' + roleInfo.starNO;
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
}
