import { Protocol } from './../../Modules/Protocol';
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class LoginCanvas extends cc.Component {
    @property(cc.Node)
    node_login: cc.Node = null;
    @property(cc.Node)
    node_reg: cc.Node = null;
    /**
     * 手机号输入框
     * 
     * @type {cc.EditBox}
     * @memberof LoginCanvas
     */
    @property(cc.EditBox)
    edit_phone: cc.EditBox = null;
    /**
     * 验证码输入框
     * 
     * @type {cc.EditBox}
     * @memberof LoginCanvas
     */
    @property(cc.EditBox)
    edit_verfi: cc.EditBox = null;
    /**
     * 登录按钮
     * 
     * @type {cc.Button}
     * @memberof LoginCanvas
     */
    @property(cc.Button)
    btn_login: cc.Button = null;

    /**
     * 获取验证码按钮
     * 
     * @type {cc.Button}
     * @memberof LoginCanvas
     */
    @property(cc.Button)
    btn_verfi: cc.Button = null;
    /**
     * 获取验证码上的label，获取验证码倒计时
     * 
     * @type {cc.Label}
     * @memberof LoginCanvas
     */
    @property(cc.Label)
    lbl_verfi: cc.Label = null;
    /**
     * 注册手机号
     * @type {cc.EditBox}
     * @memberof LoginCanvas
     */
    @property(cc.EditBox)
    edit_reg_phone: cc.EditBox = null;
    /**
     * 注册密码
     * @type {cc.EditBox}
     * @memberof LoginCanvas
     */
    @property(cc.EditBox)
    edit_reg_pwd: cc.EditBox = null;
    /**
     * 注册确认密码
     * @type {cc.EditBox}
     * @memberof LoginCanvas
     */
    @property(cc.EditBox)
    edit_reg_pwd2: cc.EditBox = null;

    _isDownTime: boolean = false;
    _downTime: number = 0;
    _cd: number = 1;

    async onLoad() {
        if (dd.ui_manager.showLoading('正在连接服务器，请稍后')) {
            let connectState = await this.connectWS();
        }
        this.showLogin(0);
    }
    /**
     * 显示登录界面
     * @param {number} type 0=登录 1=注册
     * @memberof LoginCanvas
     */
    showLogin(type: number) {
        this.node_login.active = type === 0 ? true : false;
        this.node_reg.active = type === 1 ? true : false;
        if (type === 0) {
            if (cc.sys.localStorage.getItem('phone')) {
                this.edit_phone.string = cc.sys.localStorage.getItem('phone');
            }
            if (cc.sys.localStorage.getItem('password')) {
                this.edit_verfi.string = cc.sys.localStorage.getItem('password');
            }
        } else {
            this.edit_reg_phone.string = '';
            this.edit_reg_pwd.string = '';
            this.edit_reg_pwd2.string = '';
        }
    }
    /**
     * 连接游戏服务器
     * 
     * @returns {Promise<boolean>} 
     * @memberof LoginCanvas
     */
    async connectWS(): Promise<boolean> {
        try {
            await dd.ws_manager.connect(dd.config.wsUrl);//连接服务器
            dd.ui_manager.hideLoading();
            return true;
        } catch (err) {
            cc.log(err);
            let yes: btn_obj = {
                lbl_name: '确定',
                callback: () => {
                    this.onLoad();
                }
            }
            dd.ui_manager.showAlert('连接服务器失败，请确认您的网络后点击确定按钮重新连接！', '错误提示', yes);
            return false;
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
        this.lbl_verfi.string = this._downTime + 's';
        if (dd.ui_manager.showLoading()) {
            let obj = { 'phone': phone };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_GET_SMS_CODE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
            dd.ui_manager.hideLoading();
        }
    }
    /**
     *登录手机号
     * 
     * @param {string} phone 手机号
     * @param {string} code 验证码
     * @memberof Auth_phone
     */
    sendLogin(phone: string, code: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'phone': phone, 'code': code };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_LOGIN_PHONE, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//登录服务器成功
                    cc.sys.localStorage.setItem('phone', phone);
                    cc.sys.localStorage.setItem('password', code);
                    dd.ud_manager.mineData = content as UserData;
                    dd.ws_manager.setLoginState(true);
                    this.turnToHome();
                } else {//登录服务器失败
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }
    /**
     *登录手机号
     * 
     * @param {string} phone 手机号
     * @param {string} code  密码
     * @memberof Auth_phone
     */
    sendRegister(phone: string, code: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'phone': phone, 'code': code };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_REGISTER, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//登录服务器成功
                    cc.sys.localStorage.setItem('phone', phone);
                    cc.sys.localStorage.setItem('password', code);
                    dd.ud_manager.mineData = content as UserData;
                    dd.ws_manager.setLoginState(true);
                    this.turnToHome();
                } else {//登录服务器失败
                    dd.ui_manager.showTip(content);
                }
            });
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
            dd.ui_manager.showTip("*请输入有效的手机号");
            return false;
        }
        return true;
    }
    /**
     * 登录
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    click_btn_login() {
        if (!this.btn_login.interactable) return;
        dd.mp_manager.playButton();
        let phoneStr = this.edit_phone.string.trim();
        let verfiStr = this.edit_verfi.string.trim();
        if (phoneStr === '' || phoneStr.length === 0) {
            dd.ui_manager.showTip('*请输入有效的手机号');
            return;
        }
        if (verfiStr === '' || verfiStr.length !== 6) {
            dd.ui_manager.showTip('*请输入6位密码');
            return;
        }
        this.sendLogin(phoneStr, verfiStr);
    }
    /**
     * 登录游戏
     * 
     * @memberof LoginCanvas
     */
    turnToHome(): void {
        if (dd.ui_manager.showLoading())
            cc.director.loadScene('HomeScene');
    }

    update(dt) {
        if (this._isDownTime) {
            this._cd -= dt;
            if (this._cd <= 0) {
                this._cd = 1;
                this._downTime--;
                this.lbl_verfi.string = this._downTime + 's';
                if (this._downTime < 0) {
                    this.btn_verfi.interactable = true;
                    this.lbl_verfi.string = '获取验证码';
                }
            }
        } else {
            this.btn_verfi.interactable = true;
            this.lbl_verfi.string = '获取验证码';
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
        let phoneStr = this.edit_phone.string.trim();
        if (phoneStr === '' || phoneStr.length === 0) {
            dd.ui_manager.showTip('*请输入有效的手机号');
            return;
        }
        if (this.checkMobile(phoneStr)) {
            this.sendVaildCode(phoneStr);
        }
    }
    /**
     * 返回到登录界面
     * @memberof LoginCanvas
     */
    click_btn_return() {
        dd.mp_manager.playButton();
        this.showLogin(0);
    }

    /**
     * 跳转到注册界面
     * @memberof LoginCanvas
     */
    click_btn_turnToReg() {
        dd.mp_manager.playButton();
        this.showLogin(1);
    }
    /**
     * 注册
     * @memberof LoginCanvas
     */
    click_btn_reg() {
        let phoneRegStr = this.edit_reg_phone.string.trim();
        if (phoneRegStr === '' || phoneRegStr.length === 0) {
            dd.ui_manager.showTip('*请输入有效的手机号');
            return;
        }
        let pwdRegStr = this.edit_reg_pwd.string.trim();
        if (pwdRegStr === '' || pwdRegStr.length !== 6) {
            dd.ui_manager.showTip('*请输入6位密码');
            return;
        }
        let pwdRegStr2 = this.edit_reg_pwd2.string.trim();
        if (pwdRegStr2 !== pwdRegStr) {
            dd.ui_manager.showTip('*两次密码输入不一致');
            return;
        }
        //正则表达式检验
        if (this.checkMobile(phoneRegStr)) {
            this.sendRegister(phoneRegStr, pwdRegStr);
        }
    }
}
