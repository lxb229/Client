"use strict";
cc._RF.push(module, '480e9n9QFpCYbV1K6aSg7Jo', 'Setting');
// Script/SceneScript/Home/Setting.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var Setting = /** @class */ (function (_super) {
    __extends(Setting, _super);
    function Setting() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 音效开关
         *
         * @type {cc.Toggle}
         * @memberof Setting
         */
        _this.toggle = null;
        /**
         * 版本号
         *
         * @type {cc.Label}
         * @memberof Setting
         */
        _this.lab_ver = null;
        /**
         * 是否已经完成onLoad
         *
         * @type {boolean}
         * @memberof Setting
         */
        _this.isInit = false;
        return _this;
    }
    Setting.prototype.onLoad = function () {
        if (dd.mp_manager.sw) {
            this.toggle.check();
        }
        else {
            this.toggle.uncheck();
        }
        this.lab_ver.string = dd.config.version;
        dd.ui_manager.hideLoading();
        this.isInit = true;
    };
    /**
     * 点击音效开关
     *
     * @memberof Setting
     */
    Setting.prototype.click_toggle = function () {
        if (this.isInit) {
            dd.mp_manager.playButton();
        }
        dd.mp_manager.sw = this.toggle.isChecked;
        cc.sys.localStorage.setItem('sw', JSON.stringify({ sw: this.toggle.isChecked }));
    };
    /**
     * 点击返回大厅
     *
     * @memberof Setting
     */
    Setting.prototype.click_back = function () {
        dd.mp_manager.playButton();
        this.node.destroy();
    };
    /**
     * 点击注销登录
     *
     * @memberof Setting
     */
    Setting.prototype.click_out = function () {
        dd.ui_manager.showLoading('正在注销,请稍后');
        dd.mp_manager.playButton();
        var obj = { 'accountId': dd.ud_manager.account_mine.accountId };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_LOGIN_OUT, msg, function (flag, content) {
            if (flag === 0) {
                dd.ws_manager.disconnect(function () {
                    var db = cc.sys.localStorage;
                    if (db.getItem('TokenInfo')) {
                        db.removeItem('TokenInfo');
                    }
                    dd.destroy();
                    cc.sys.garbageCollect();
                    cc.game.restart();
                });
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('注销消息发送超时');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    __decorate([
        property(cc.Toggle)
    ], Setting.prototype, "toggle", void 0);
    __decorate([
        property(cc.Label)
    ], Setting.prototype, "lab_ver", void 0);
    Setting = __decorate([
        ccclass
    ], Setting);
    return Setting;
}(cc.Component));
exports.default = Setting;

cc._RF.pop();