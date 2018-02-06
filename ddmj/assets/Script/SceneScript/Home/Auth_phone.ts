const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Auth_phone extends cc.Component {

    /**
     * 手机绑定的节点
     * 
     * @type {cc.Node}
     * @memberof Auth_phone
     */
    @property(cc.Node)
    node_auth_phone: cc.Node = null;
    /**
     * 已经手机绑定的节点
     * 
     * @type {cc.Node}
     * @memberof Auth_phone
     */
    @property(cc.Node)
    node_bind_phone: cc.Node = null;
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
     * 手机号
     * @type {cc.Label}
     * @memberof Auth_phone
     */
    @property(cc.Label)
    lblPhone: cc.Label = null;

    /**
     * 获取验证码的按钮
     * 
     * @type {cc.Button}
     * @memberof Auth_phone
     */
    @property(cc.Button)
    btn_verfi: cc.Button = null;

    _isDownTime: boolean = false;
    _downTime: number = 0;
    _cd: number = 1;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);

        //如果已经绑定手机号
        if (dd.ud_manager.mineData.phone && dd.ud_manager.mineData.phone !== '') {
            this.lblPhone.string = dd.ud_manager.mineData.phone;
            this.showAuthPhone(1);
        } else {
            this.showAuthPhone(0);
        }
    }

    update(dt) {
        if (this._isDownTime) {
            this._cd -= dt;
            if (this._cd <= 0) {
                this._cd = 1;
                this._downTime--;
                this.lblVerfi.string = this._downTime + 's';
                if (this._downTime < 0) {
                    this.btn_verfi.interactable = true;
                    this.lblVerfi.string = '获取验证码';
                }
            }
        } else {
            this.btn_verfi.interactable = true;
            this.lblVerfi.string = '获取验证码';
        }
    }

    /**
     * 显示手机绑定界面
     * @param {number} type 0未绑定 1已绑定
     * @memberof Auth_phone
     */
    showAuthPhone(type: number) {
        this._isDownTime = false;
        this.node_auth_phone.active = type === 0 ? true : false;
        this.node_bind_phone.active = type === 1 ? true : false;
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
                    this.lblPhone.string = dd.ud_manager.mineData.phone;
                    this.showAuthPhone(1);
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
        this._isDownTime = true;
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
        if (phoneStr === '' || phoneStr.length === 0) {
            this.showError(0, '*请输入有效的手机号');
            return;
        }
        if (this.checkMobile(phoneStr)) {
            this.sendVaildCode(phoneStr);
        }
    }

    /**
     * 手机号验证
     * @param {string} sMobile 
     * @returns 
     * @memberof LoginCanvas
     */
    checkMobile(sMobile: string) {
        if (!(/^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/.test(sMobile))) {
            this.showError(0, '*请输入有效的手机号');
            return false;
        }
        return true;
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
        this.lblMsg.node.setPositionY(pos.y - 50);
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
        if (phoneStr === '' || phoneStr.length === 0) {
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
     * 已绑定
     * 
     * @memberof Auth_phone
     */
    click_btn_alBind() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }

    /**
     * 退出
     * 
     * @memberof Auth_phone
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
