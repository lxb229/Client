import { wxLogin } from './../../Modules/JSCallNative';
import { Protocol } from './../../Modules/Protocol';
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class LoginCanvas extends cc.Component {
    /**
     * 用户协议复选框
     * 
     * @type {cc.Toggle}
     * @memberof LoginCanvas
     */
    @property(cc.Toggle)
    checkToggle: cc.Toggle = null;
    /**
     * 微信按钮
     * 
     * @type {cc.Button}
     * @memberof LoginCanvas
     */
    @property(cc.Button)
    btn_wx: cc.Button = null;
    /**
     * 游客按钮
     * 
     * @type {cc.Button}
     * @memberof LoginCanvas
     */
    @property(cc.Button)
    btn_yk: cc.Button = null;
    /**
     * 用户协议
     * @type {cc.Node}
     * @memberof LoginCanvas
     */
    @property(cc.Node)
    node_yhxy: cc.Node = null;
    /**
     * 微信登录结果回调
     * 
     * @memberof LoginCanvas
     */
    cb_login: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        dd.ui_manager.hideLoading();
        let detail: CB_Login = event.detail;
        if (detail.flag === 1) {//成功
            let userInfo: UserInfo = detail.data as UserInfo;
            userInfo.headimgurl = dd.utils.getHeadImgUrl(userInfo.headimgurl);
            this.wsLogin(dd.protocol.ACCOUNT_LOGIN_WX, userInfo);
        } else {//失败
            dd.ui_manager.showTip(detail.data as string);
        }
    }
    /**
     * ccc组件释放的生命周期回调
     * 
     * @memberof LoginCanvas
     */
    onDestroy() {
        if (cc.sys.isNative && cc.sys.isMobile) {//手机端
            cc.systemEvent.off('cb_login', this.cb_login, this);
        }
    }
    async onLoad() {
        this.node_yhxy.active = false;
        dd.ui_manager.fixIPoneX(this.node);
        if (dd.config.wxState === 0) {
            this.btn_wx.node.active = true;
            this.btn_yk.node.active = false;
        } else {
            this.btn_wx.node.active = false;
            this.btn_yk.node.active = true;
        }
        if (dd.ui_manager.showLoading('正在连接服务器，请稍后')) {
            let connectState = await this.connectWS();
            if (cc.sys.isNative && cc.sys.isMobile && connectState) {
                //注册微信登录回调
                cc.systemEvent.on('cb_login', this.cb_login, this);
                //微信自动登录
                await this.aotuLogin();
            }
        }
        dd.mp_manager.playBackGround();
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
     * 微信自动登录
     * 
     * @returns {Promise<void>} 
     * @memberof LoginCanvas
     */
    async aotuLogin(): Promise<void> {
        let db = cc.sys.localStorage;
        if (db.getItem('TokenInfo')) {//验证是否有授权过
            try {
                let data: TokenInfo = JSON.parse(db.getItem('TokenInfo'));
                //刷新token过期时间
                let url_refresh = 'https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=' + dd.config.app_id + '&grant_type=refresh_token&refresh_token=' + data.refresh_token;
                let response_refresh = await fetch(url_refresh);
                if (response_refresh.ok) {//请求成功
                    let newToken: TokenInfo = await response_refresh.json();
                    db.setItem('TokenInfo', JSON.stringify(newToken));
                    let url_userInfo = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + newToken.access_token + '&openid=' + newToken.openid;
                    let response_userInfo = await fetch(url_userInfo);
                    if (response_userInfo.ok) {//获取用户信息成功
                        let userInfo = await response_userInfo.json();
                        userInfo.headimgurl = dd.utils.getHeadImgUrl(userInfo.headimgurl);
                        this.wsLogin(dd.protocol.ACCOUNT_LOGIN_WX, userInfo);
                    } else {//获取用户信息失败
                        dd.ui_manager.showTip('微信用户信息获取失败，请重新授权登录');
                    }
                } else {//授权过期需要重新授权
                    dd.ui_manager.showTip('微信授权过期，请重新授权登录');
                }
            } catch (err) {
                cc.log(err);
                dd.ui_manager.showTip('微信请求异常，请重新授权登录');
            }
        }
    }
    /**
     * 微信登录
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    click_btn_wx() {
        if (!this.checkToggle.isChecked) {
            dd.ui_manager.showTip('您没有同意用户协议不可进入游戏，请先同意用户协议！');
            return;
        }
        if (dd.ui_manager.showLoading('正在拉取微信授权，请稍后')) {
            setTimeout(() => {
                dd.mp_manager.playButton();
                dd.js_call_native.wxLogin();
            }, 1000);
        }
    }
    /**
     * ws登录请求
     * 
     * @param {Protocol} msgId 协议号
     * @param {UserInfo} [info] 微信获取的数据对象
     * @memberof LoginCanvas
     */
    wsLogin(msgId: Protocol, info?: UserInfo): void {
        if (dd.ui_manager.showLoading('正在登录中,请稍后')) {
            let obj: any = {};
            if (info) {
                obj.uuid = info.unionid;
                obj.headImg = info.headimgurl;
                obj.nick = info.nickname;
                obj.sex = info.sex;
            } else {
                obj.uuid = this.getGuestAccount();
            }
            dd.config.voiceState = dd.js_call_native.initVoice(obj.uuid, dd.config.voice_id, dd.config.voice_key);
            if (dd.config.voiceState !== 0) {
                dd.ui_manager.showTip('语音初始化失败,正在重试');
            }
            dd.ws_manager.sendMsg(
                msgId,
                JSON.stringify(obj),
                (flag: number, content?: any) => {
                    if (flag === 0) {//登录服务器成功
                        dd.ud_manager.mineData = content as UserData;
                        dd.ws_manager.setLoginState(true);
                        cc.director.loadScene('HomeScene');
                    } else {//登录服务器失败
                        dd.ui_manager.hideLoading();
                        dd.ui_manager.showTip(content);
                    }
                });
        }
    }
    /**
     * 游客登录
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    click_btn_yk() {
        if (!this.checkToggle.isChecked) {
            dd.ui_manager.showTip('您没有同意用户协议不可进入游戏，请先同意用户协议！');
            return;
        }
        dd.mp_manager.playButton();
        this.wsLogin(dd.protocol.ACCOUNT_LOGIN_TOURIST);
    }
    /**
     * 获取uuid，第一次运行创建uuid
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    getGuestAccount(): string {
        let db = cc.sys.localStorage;
        let uuid = db.getItem('uuid');
        if (uuid && uuid.length === 32) {
            return uuid;
        } else {
            uuid = this.createUUID(32, 16);
            db.setItem('uuid', uuid);
            return uuid;
        }
    }
    /**
     * 创建UUID
     * 
     * @param {number} len UUID长度
     * @param {number} radix 输出的进制（2,8,10,16）
     * @returns {string} 返回对应进制下制定长度的字符串
     * @memberof LoginCanvas
     */
    createUUID(len: number, radix: number): string {
        let chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        let uuid = [], i;
        radix = radix || chars.length;
        if (len) {
            for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
        } else {
            let r;
            uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
            uuid[14] = '4';
            for (i = 0; i < 36; i++) {
                if (!uuid[i]) {
                    r = 0 | Math.random() * 16;
                    uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                }
            }
        }
        return uuid.join('');
    }
    /**
     * 点击用户协议的复选框事件
     * 
     * @memberof LoginCanvas
     */
    click_yhxy_toggle() {
        dd.mp_manager.playButton();
        if (this.checkToggle.isChecked) {
            this.checkToggle.uncheck();
            this.node_yhxy.active = false;
        } else {
            this.checkToggle.check();
            this.node_yhxy.active = true;
        }
    }
    /**
     * 退出用户协议
     * @memberof LoginCanvas
     */
    click_yhxy_out() {
        dd.mp_manager.playButton();
        this.node_yhxy.active = false;
    }
}
