(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Login/LoginCanvas.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, '8e008YDPPZHhq89FtAO4ztd', 'LoginCanvas', __filename);
// Script/SceneScript/Login/LoginCanvas.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var LoginCanvas = /** @class */ (function (_super) {
    __extends(LoginCanvas, _super);
    function LoginCanvas() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 微信登录按钮
         *
         * @type {cc.Button}
         * @memberof LoginCanvas
         */
        _this.btn_wechat = null;
        /**
         * 快速登录按钮
         *
         * @type {cc.Button}
         * @memberof LoginCanvas
         */
        _this.btn_quick = null;
        _this.tog_yhxy = null;
        _this.yhxy_layer = null;
        /**
         * 微信登录结果回调
         *
         * @memberof LoginCanvas
         */
        _this.cb_login = function (event) {
            var detail = event.detail;
            if (detail.flag === 1) {
                var userInfo = detail.data;
                userInfo.headimgurl = dd.utils.getHeadImgUrl(userInfo.headimgurl);
                _this.wsLogin(dd.protocol.ACCOUNT_LOGIN_WX, userInfo);
            }
            else {
                dd.ui_manager.showTip(detail.data);
            }
        };
        return _this;
    }
    /**
     * ccc组件释放的生命周期回调
     *
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.onDestroy = function () {
        if (dd.config.wxState === 0) {
            cc.systemEvent.off('cb_login', this.cb_login, this);
        }
    };
    LoginCanvas.prototype.onLoad = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            var errMsg_1;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        if (cc.sys.localStorage.getItem('isRead')) {
                            this.tog_yhxy.check();
                        }
                        else {
                            this.tog_yhxy.uncheck();
                        }
                        this.yhxy_layer.active = false;
                        this.btn_quick.node.active = true;
                        this.btn_wechat.node.active = false;
                        dd.ui_manager.showLoading();
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 6, , 7]);
                        return [4 /*yield*/, dd.ws_manager.connect(dd.config.wsUrl)];
                    case 2:
                        _a.sent(); //连接服务器
                        if (!(dd.config.wxState === 0)) return [3 /*break*/, 4];
                        this.btn_quick.node.active = false;
                        this.btn_wechat.node.active = true;
                        //注册微信登录回调
                        cc.systemEvent.on('cb_login', this.cb_login, this);
                        //微信自动登录
                        return [4 /*yield*/, this.aotuLogin()];
                    case 3:
                        //微信自动登录
                        _a.sent();
                        return [3 /*break*/, 5];
                    case 4:
                        dd.ui_manager.hideLoading();
                        _a.label = 5;
                    case 5: return [3 /*break*/, 7];
                    case 6:
                        errMsg_1 = _a.sent();
                        dd.ui_manager.showAlert(2, errMsg_1, function () {
                            _this.onLoad();
                        });
                        return [3 /*break*/, 7];
                    case 7: return [2 /*return*/];
                }
            });
        });
    };
    /**
     * 微信自动登录
     *
     * @returns {Promise<void>}
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.aotuLogin = function () {
        return __awaiter(this, void 0, void 0, function () {
            var db, data, url_refresh, response_refresh, newToken, url_userInfo, response_userInfo, userInfo, err_1;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        db = cc.sys.localStorage;
                        if (!db.getItem('TokenInfo')) return [3 /*break*/, 12];
                        _a.label = 1;
                    case 1:
                        _a.trys.push([1, 10, , 11]);
                        data = JSON.parse(db.getItem('TokenInfo'));
                        url_refresh = 'https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=' + dd.config.app_id + '&grant_type=refresh_token&refresh_token=' + data.refresh_token;
                        return [4 /*yield*/, fetch(url_refresh)];
                    case 2:
                        response_refresh = _a.sent();
                        if (!response_refresh.ok) return [3 /*break*/, 8];
                        return [4 /*yield*/, response_refresh.json()];
                    case 3:
                        newToken = _a.sent();
                        db.setItem('TokenInfo', JSON.stringify(newToken));
                        url_userInfo = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + newToken.access_token + '&openid=' + newToken.openid;
                        return [4 /*yield*/, fetch(url_userInfo)];
                    case 4:
                        response_userInfo = _a.sent();
                        if (!response_userInfo.ok) return [3 /*break*/, 6];
                        return [4 /*yield*/, response_userInfo.json()];
                    case 5:
                        userInfo = _a.sent();
                        userInfo.headimgurl = dd.utils.getHeadImgUrl(userInfo.headimgurl);
                        this.wsLogin(dd.protocol.ACCOUNT_LOGIN_WX, userInfo);
                        return [3 /*break*/, 7];
                    case 6:
                        dd.ui_manager.showTip('微信用户信息获取失败，请重新授权登录');
                        _a.label = 7;
                    case 7: return [3 /*break*/, 9];
                    case 8:
                        dd.ui_manager.showTip('微信授权过期，请重新授权登录');
                        _a.label = 9;
                    case 9: return [3 /*break*/, 11];
                    case 10:
                        err_1 = _a.sent();
                        dd.ui_manager.showTip('微信请求异常，请重新授权登录');
                        return [3 /*break*/, 11];
                    case 11: return [3 /*break*/, 13];
                    case 12:
                        dd.ui_manager.hideLoading();
                        _a.label = 13;
                    case 13: return [2 /*return*/];
                }
            });
        });
    };
    /**
     * 微信登录
     *
     * @returns
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.click_wx = function () {
        if (!this.tog_yhxy.isChecked) {
            dd.ui_manager.showTip('请先阅读游戏声明！');
            return;
        }
        if (!dd.ui_manager.showLoading('准备跳往微信授权登录'))
            return;
        setTimeout(function () {
            dd.js_call_native.wxLogin();
        }, 1000);
    };
    /**
     * ws登录请求
     *
     * @param {number} msgId 协议号
     * @param {UserInfo} [info] 微信获取的数据对象
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.wsLogin = function (msgId, info) {
        var _this = this;
        dd.mp_manager.playButton();
        var obj = {};
        if (info) {
            obj.uuid = info.unionid;
            obj.headImg = info.headimgurl;
            obj.nick = info.nickname;
            obj.sex = info.sex;
        }
        else {
            obj.uuid = this.getGuestAccount();
        }
        dd.ws_manager.sendMsg(msgId, JSON.stringify(obj), function (flag, content) {
            if (flag === 0) {
                dd.ud_manager.account_mine = content;
                dd.ws_manager.setLoginState(true);
                if (dd.ud_manager.account_mine.gameDataAttribVo.tableId !== 0) {
                    _this.getTableData(0);
                }
                else {
                    setTimeout(function () {
                        cc.director.loadScene('HomeScene');
                    }, 1000);
                }
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('登录消息超时');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    /**
     * 游客登录
     *
     * @returns
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.click_quick = function () {
        if (!this.tog_yhxy.isChecked) {
            dd.ui_manager.showTip('请先阅读游戏声明！');
            return;
        }
        if (!dd.ui_manager.showLoading('正在登录中,请稍后'))
            return;
        this.wsLogin(dd.protocol.ACCOUNT_LOGIN_TOURIST);
    };
    /**
     * 获取当前最新的桌子数据
     *
     * @param {number} count 请求次数
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.getTableData = function (count) {
        var _this = this;
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_GET_TABLE_INFO, JSON.stringify({ tableId: dd.ud_manager.account_mine.gameDataAttribVo.tableId }), function (flag, content) {
            if (flag === 0) {
                dd.gm_manager.setTableData(content, 1);
                cc.director.loadScene('GameScene', function () {
                    dd.ui_manager.showTip('返回牌局成功!');
                });
            }
            else {
                count++;
                if (count > 3) {
                    dd.ui_manager.showAlert(2, '连接服务器失败,请确认网络后重新启动游戏!', function () {
                        cc.game.end();
                    });
                }
                else {
                    _this.getTableData(count);
                }
            }
        });
    };
    /**
     * 获取uuid，第一次运行创建uuid
     *
     * @returns
     * @memberof LoginCanvas
     */
    LoginCanvas.prototype.getGuestAccount = function () {
        var db = cc.sys.localStorage;
        var uuid = db.getItem('uuid');
        if (uuid && uuid.length === 32) {
            return uuid;
        }
        else {
            uuid = dd.utils.createUUID(32, 16);
            db.setItem('uuid', uuid);
            return uuid;
        }
    };
    LoginCanvas.prototype.click_Tog = function () {
        dd.mp_manager.playButton();
        cc.sys.localStorage.setItem('isRead', true);
        this.yhxy_layer.active = true;
        this.tog_yhxy.check();
    };
    LoginCanvas.prototype.click_close = function () {
        dd.mp_manager.playButton();
        this.yhxy_layer.active = false;
    };
    __decorate([
        property(cc.Button)
    ], LoginCanvas.prototype, "btn_wechat", void 0);
    __decorate([
        property(cc.Button)
    ], LoginCanvas.prototype, "btn_quick", void 0);
    __decorate([
        property(cc.Toggle)
    ], LoginCanvas.prototype, "tog_yhxy", void 0);
    __decorate([
        property(cc.Node)
    ], LoginCanvas.prototype, "yhxy_layer", void 0);
    LoginCanvas = __decorate([
        ccclass
    ], LoginCanvas);
    return LoginCanvas;
}(cc.Component));
exports.default = LoginCanvas;

cc._RF.pop();
        }
        if (CC_EDITOR) {
            __define(__module.exports, __require, __module);
        }
        else {
            cc.registerModuleFunc(__filename, function () {
                __define(__module.exports, __require, __module);
            });
        }
        })();
        //# sourceMappingURL=LoginCanvas.js.map
        