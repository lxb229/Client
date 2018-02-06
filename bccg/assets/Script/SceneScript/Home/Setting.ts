const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Setting extends cc.Component {
    /**
     * 音效开关
     * 
     * @type {cc.Toggle}
     * @memberof Setting
     */
    @property(cc.Toggle)
    toggle: cc.Toggle = null;
    /**
     * 版本号
     * 
     * @type {cc.Label}
     * @memberof Setting
     */
    @property(cc.Label)
    lab_ver: cc.Label = null;
    /**
     * 是否已经完成onLoad
     * 
     * @type {boolean}
     * @memberof Setting
     */
    isInit: boolean = false;

    onLoad() {
        if (dd.mp_manager.sw) {
            this.toggle.check();
        } else {
            this.toggle.uncheck();
        }
        this.lab_ver.string = dd.config.version;
        dd.ui_manager.hideLoading();
        this.isInit = true;
    }
    /**
     * 点击音效开关
     * 
     * @memberof Setting
     */
    click_toggle() {
        if (this.isInit) {
            dd.mp_manager.playButton();
        }
        dd.mp_manager.sw = this.toggle.isChecked;
        cc.sys.localStorage.setItem('sw', JSON.stringify({ sw: this.toggle.isChecked }));
    }
    /**
     * 点击返回大厅
     * 
     * @memberof Setting
     */
    click_back() {
        dd.mp_manager.playButton();
        this.node.destroy();
    }
    /**
     * 点击注销登录
     * 
     * @memberof Setting
     */
    click_out() {
        dd.ui_manager.showLoading('正在注销,请稍后');
        dd.mp_manager.playButton();
        let obj = { 'accountId': dd.ud_manager.account_mine.accountId };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_LOGIN_OUT, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                dd.ws_manager.disconnect(() => {
                    let db = cc.sys.localStorage;
                    if (db.getItem('TokenInfo')) {
                        db.removeItem('TokenInfo');
                    }
                    dd.destroy();
                    cc.sys.garbageCollect();
                    cc.game.restart();
                });
            } else if (flag === -1) {
                dd.ui_manager.showTip('注销消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
}
