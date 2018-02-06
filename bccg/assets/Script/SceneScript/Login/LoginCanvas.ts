const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class LoginCanvas extends cc.Component {
    /**
     * 微信登录按钮
     * 
     * @type {cc.Button}
     * @memberof LoginCanvas
     */
    @property(cc.Button)
    btn_wechat: cc.Button = null;
    /**
     * 快速登录按钮
     * 
     * @type {cc.Button}
     * @memberof LoginCanvas
     */
    @property(cc.Button)
    btn_quick: cc.Button = null;

    @property(cc.Toggle)
    tog_yhxy: cc.Toggle = null;

    @property(cc.Node)
    yhxy_layer: cc.Node = null;
    /**
     * 微信登录结果回调
     * 
     * @memberof LoginCanvas
     */
    cb_login: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        let detail = event.detail;
        if (detail.flag === 1) {//成功
            let userInfo: UserInfo = detail.data;
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
        if (dd.config.wxState === 0) {
            cc.systemEvent.off('cb_login', this.cb_login, this);
        }
    }
    async onLoad() {
        if (cc.sys.localStorage.getItem('isRead')) {
            this.tog_yhxy.check();
        } else {
            this.tog_yhxy.uncheck();
        }
        this.yhxy_layer.active = false;
        this.btn_quick.node.active = true;
        this.btn_wechat.node.active = false;
        dd.ui_manager.showLoading();
        try {
            await dd.ws_manager.connect(dd.config.wsUrl);//连接服务器
            if (dd.config.wxState === 0) {
                this.btn_quick.node.active = false;
                this.btn_wechat.node.active = true;
                //注册微信登录回调
                cc.systemEvent.on('cb_login', this.cb_login, this);
                //微信自动登录
                await this.aotuLogin();
            } else {
                dd.ui_manager.hideLoading();
            }
        } catch (errMsg) {
            dd.ui_manager.showAlert(2, errMsg, () => {
                this.onLoad();
            });
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
                dd.ui_manager.showTip('微信请求异常，请重新授权登录');
            }
        } else {
            dd.ui_manager.hideLoading();
        }
    }
    /**
     * 微信登录
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    click_wx() {
        if (!this.tog_yhxy.isChecked) {
            dd.ui_manager.showTip('请先阅读游戏声明！');
            return;
        }
        if (!dd.ui_manager.showLoading('准备跳往微信授权登录')) return;
        setTimeout(() => {
            dd.js_call_native.wxLogin();
        }, 1000);
    }
    /**
     * ws登录请求
     * 
     * @param {number} msgId 协议号
     * @param {UserInfo} [info] 微信获取的数据对象
     * @memberof LoginCanvas
     */
    wsLogin(msgId: number, info?: UserInfo): void {
        dd.mp_manager.playButton();
        let obj: any = {};
        if (info) {
            obj.uuid = info.unionid;
            obj.headImg = info.headimgurl;
            obj.nick = info.nickname;
            obj.sex = info.sex;
        } else {
            obj.uuid = this.getGuestAccount();
        }
        dd.ws_manager.sendMsg(
            msgId,
            JSON.stringify(obj),
            (flag: number, content?: any) => {
                if (flag === 0) {//登录服务器成功
                    dd.ud_manager.account_mine = content as AccountData;
                    dd.ws_manager.setLoginState(true);
                    if (dd.ud_manager.account_mine.gameDataAttribVo.tableId !== 0) {
                        this.getTableData(0);
                    } else {
                        setTimeout(() => {
                            cc.director.loadScene('HomeScene');
                        }, 1000);
                    }
                } else if (flag === -1) {
                    dd.ui_manager.showTip('登录消息超时');
                } else {//登录服务器失败
                    dd.ui_manager.showTip(content);
                }
            });
    }
    /**
     * 游客登录
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    click_quick() {
        if (!this.tog_yhxy.isChecked) {
            dd.ui_manager.showTip('请先阅读游戏声明！');
            return;
        }
        if (!dd.ui_manager.showLoading('正在登录中,请稍后')) return;
        this.wsLogin(dd.protocol.ACCOUNT_LOGIN_TOURIST);
    }
    /**
     * 获取当前最新的桌子数据
     * 
     * @param {number} count 请求次数
     * @memberof LoginCanvas
     */
    getTableData(count: number) {
        dd.ws_manager.sendMsg(
            dd.protocol.DZPKER_TABLE_GET_TABLE_INFO,
            JSON.stringify({ tableId: dd.ud_manager.account_mine.gameDataAttribVo.tableId }),
            (flag: number, content?: any) => {
                if (flag === 0) {
                    dd.gm_manager.setTableData(content as TableData, 1);
                    cc.director.loadScene('GameScene', () => {
                        dd.ui_manager.showTip('返回牌局成功!');
                    });
                } else {
                    count++;
                    if (count > 3) {
                        dd.ui_manager.showAlert(2, '连接服务器失败,请确认网络后重新启动游戏!', () => {
                            cc.game.end();
                        });
                    } else {
                        this.getTableData(count);
                    }
                }
            });
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
            uuid = dd.utils.createUUID(32, 16);
            db.setItem('uuid', uuid);
            return uuid;
        }
    }

    click_Tog() {
        dd.mp_manager.playButton();
        cc.sys.localStorage.setItem('isRead', true);
        this.yhxy_layer.active = true;
        this.tog_yhxy.check();
    }

    click_close() {
        dd.mp_manager.playButton();
        this.yhxy_layer.active = false;
    }
}
