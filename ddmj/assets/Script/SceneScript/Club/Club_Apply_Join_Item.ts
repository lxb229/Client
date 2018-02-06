
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Club_Apply_Join_Item extends cc.Component {

    @property(cc.Label)
    lblName: cc.Label = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;

    _roleInfo: UserData = null;
    _canvasScript = null;
    onLoad() {

    }
    /**
     * 显示信息
     * 
     * @memberof Club_Role
     */
    async updateItem(data, target) {
        this._roleInfo = data;
        this._canvasScript = target;
        this.lblName.string = data.nick;
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(data.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.headImg.spriteFrame = headSF;
    }

    click_btn_refuse() {
        dd.mp_manager.playButton();
        this._canvasScript.sendApplyAnwser(this._roleInfo.starNO, 0);
    }

    click_btn_agree() {
        dd.mp_manager.playButton();
        this._canvasScript.sendApplyAnwser(this._roleInfo.starNO, 1);
    }
}
