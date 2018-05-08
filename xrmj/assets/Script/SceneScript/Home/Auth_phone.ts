import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Auth_phone extends cc.Component {
    /**
     * 手机输入框
     * 
     * @type {cc.EditBox}
     * @memberof Auth_phone
     */
    @property(cc.EditBox)
    eb_phone: cc.EditBox = null;

    /**
     * 验证码输入框
     * 
     * @type {cc.EditBox}
     * @memberof Auth_phone
     */
    @property(cc.EditBox)
    eb_verfi: cc.EditBox = null;

    /**
     * 验证码的文本
     * 
     * @type {cc.Label}
     * @memberof Auth_phone
     */
    @property(cc.Label)
    lblVerfi: cc.Label = null;

    /**
     * 提示信息
     * @type {cc.Label}
     * @memberof Auth_phone
     */
    @property(cc.Label)
    lblMsg: cc.Label = null;
    /**
     * 获取验证码的按钮
     * 
     * @type {cc.Button}
     * @memberof Auth_phone
     */
    @property(cc.Button)
    btn_verfi: cc.Button = null;

    _downTime: number = 0;
    _cd: number = 1;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        this.lblMsg.string = '';
    }

    update(dt) {
        if (this._downTime > 0) {
            this._cd -= dt;
            if (this._cd <= 0) {
                this._cd = 1;
                this._downTime--;
                this.lblVerfi.string = this._downTime + 's';
                if (this._downTime <= 0) {
                    this.btn_verfi.interactable = true;
                    this.lblVerfi.string = '获取验证码';
                }
            }
        }
    }

    /**
     * 玩家手机绑定
     * 
     * @param {string} phone 手机号
     * @param {string} vaildCode 验证码
     * @memberof Auth_phone
     */
    sendBindPhone(phone: string, vaildCode: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'phone': phone, 'vaildCode': vaildCode };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_PHONE_BIND, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ud_manager.mineData.phone = content;
                    dd.ui_manager.showAlert('绑定成功！', '温馨提示');
                    this.node.removeFromParent(true);
                    this.node.destroy();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    this.lblMsg.string = content;
                }
            });
            dd.ui_manager.hideLoading();
        }
    }
    /**
     * 发送验证码
     * 
     * @param {string} phone 手机号
     * @memberof Auth_phone
     */
    sendVaildCode(phone: string) {
        this.btn_verfi.interactable = false;
        this._downTime = 60;
        this._cd = 1;
        this.lblVerfi.string = this._downTime + 's';
        if (dd.ui_manager.showLoading()) {
            let obj = { 'phone': phone };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_PHONE_GET_SMSCODE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ud_manager.mineData.phone = content;
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    this.lblMsg.string = content;
                }
            });
            dd.ui_manager.hideLoading();
        }
    }
    /**
     * 点击获取验证码
     * 
     * @returns 
     * @memberof Auth_phone
     */
    click_btn_verfi() {
        dd.mp_manager.playButton();
        if (!this.btn_verfi.interactable) {
            return;
        }
        let phoneStr = this.eb_phone.string.trim();
        if (!dd.utils.checkMobile(phoneStr))
            return dd.ui_manager.showTip('*请输入有效手机号');
        this.sendVaildCode(phoneStr);
    }
    /**
     * 显示错误
     * 
     * @param {number} type 
     * @param {string} str 
     * @memberof Auth_phone
     */
    showError(type: number, str: string) {
        this.lblMsg.node.opacity = 255;
        this.lblMsg.string = str;
        let pos = type === 0 ? this.eb_phone.node.parent.getPosition() : this.eb_verfi.node.parent.getPosition();
        this.lblMsg.node.setPositionY(pos.y - 60);
        let action = cc.sequence(cc.delayTime(2), cc.fadeOut(1), cc.callFunc((target: cc.Node, data?: any) => {
            target.opacity = 255;
            target.getComponent(cc.Label).string = '';
        }, this));
        this.lblMsg.node.runAction(action);
    }
    /**
     * 绑定手机点击事件
     * 
     * @memberof Auth_phone
     */
    click_btn_bind() {
        dd.mp_manager.playButton();
        let phoneStr = this.eb_phone.string.trim();
        let verfiStr = this.eb_verfi.string.trim();
        if (!dd.utils.checkMobile(phoneStr)) {
            this.showError(0, '*请输入有效的手机号');
            return;
        }
        if (verfiStr === '') {
            this.showError(1, '*请输入验证码');
            return;
        }
        this.sendBindPhone(phoneStr, verfiStr);
    }
    /**
     * 退出
     * 
     * @memberof Auth_phone
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
