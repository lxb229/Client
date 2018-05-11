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
                this.aotuLogin();
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
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading('正在拉取微信授权，请稍后')) {
            setTimeout(() => {
                dd.js_call_native.wxLogin();
            }, 1000);
        }
    }
    /**
     * 微信自动登录
     * 
     * @returns {Promise<void>} 
     * @memberof LoginCanvas
     */
    async aotuLogin(): Promise<void> {
        if (dd.config.wxState !== 0) {
            cc.log('未安装微信');
            dd.ui_manager.hideLoading();
            return;
        }
        let db = cc.sys.localStorage;
        let tokenStr = db.getItem('TokenInfo');
        if (tokenStr) {//验证是否有授权过
            try {
                let data: TokenInfo = JSON.parse(tokenStr);
                //刷新token过期时间
                let url_refresh = 'https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=' + dd.config.app_id + '&grant_type=refresh_token&refresh_token=' + data.refresh_token;
                let newStr = await dd.ud_manager.xmlHttp(url_refresh);
                let newToken: TokenInfo = JSON.parse(newStr);
                db.setItem('TokenInfo', newStr);
                let url_userInfo = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + newToken.access_token + '&openid=' + newToken.openid;
                let infoStr = await dd.ud_manager.xmlHttp(url_userInfo);
                let userInfo = JSON.parse(infoStr);
                userInfo.headimgurl = dd.utils.getHeadImgUrl(userInfo.headimgurl);
                this.wsLogin(dd.protocol.ACCOUNT_LOGIN_WX, userInfo);
            } catch (errMsg) {
                dd.ui_manager.hideLoading();
                dd.ui_manager.showTip('微信自动登录失败:' + errMsg);
            }
        } else {
            cc.log('未微信授权登录，请重新授权登录');
            dd.ui_manager.hideLoading();
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
                obj.uuid = dd.ud_manager.getGuestAccount();
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
            this.node_yhxy.active = true;
        }
    }
    /**
     * 同意用户协议
     * @memberof LoginCanvas
     */
    click_yhxy_agree() {
        dd.mp_manager.playButton();
        this.checkToggle.check();
        this.node_yhxy.active = false;
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
