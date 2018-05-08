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

    @property(cc.Label)
    lblSex: cc.Label = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
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
        switch (roleInfo.sex) {
            case 1:
                this.lblSex.string = '性别:男';
                break;
            case 2:
                this.lblSex.string = '性别:女';
                break;
            default:
                this.lblSex.string = '性别:保密';
                break;
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
