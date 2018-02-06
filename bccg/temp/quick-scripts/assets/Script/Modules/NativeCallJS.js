(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/Modules/NativeCallJS.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, 'd0f79HLLThNZYwr8VFvo8ym', 'NativeCallJS', __filename);
// Script/Modules/NativeCallJS.ts

Object.defineProperty(exports, "__esModule", { value: true });
var Config = require("./Config");
/**
 * 微信登录-获取access_token
 *
 * @export
 * @param {string} code 登录授权获取到的code
 */
function getAccessToken(code) {
    return __awaiter(this, void 0, void 0, function () {
        var url, response, data, db, url_userInfo, response_userInfo, userInfo, err_1;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    _a.trys.push([0, 11, , 12]);
                    url = 'https://api.weixin.qq.com/sns/oauth2/access_token?appid=' + Config.app_id + '&secret=' + Config.secret + '&code=' + code + '&grant_type=authorization_code';
                    return [4 /*yield*/, fetch(url)];
                case 1:
                    response = _a.sent();
                    if (!response.ok) return [3 /*break*/, 9];
                    return [4 /*yield*/, response.json()];
                case 2:
                    data = _a.sent();
                    if (!data.access_token) return [3 /*break*/, 7];
                    db = cc.sys.localStorage;
                    db.setItem('TokenInfo', JSON.stringify(data));
                    url_userInfo = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + data.access_token + '&openid=' + data.openid;
                    return [4 /*yield*/, fetch(url_userInfo)];
                case 3:
                    response_userInfo = _a.sent();
                    if (!response_userInfo.ok) return [3 /*break*/, 5];
                    return [4 /*yield*/, response_userInfo.json()];
                case 4:
                    userInfo = _a.sent();
                    cc.systemEvent.emit('cb_login', { flag: 1, data: userInfo });
                    return [3 /*break*/, 6];
                case 5:
                    cc.systemEvent.emit('cb_login', { flag: 0, data: '获取用户信息失败' });
                    _a.label = 6;
                case 6: return [3 /*break*/, 8];
                case 7:
                    cc.systemEvent.emit('cb_login', { flag: 0, data: '获取token失败' });
                    _a.label = 8;
                case 8: return [3 /*break*/, 10];
                case 9:
                    cc.systemEvent.emit('cb_login', { flag: 0, data: '请求token失败' });
                    _a.label = 10;
                case 10: return [3 /*break*/, 12];
                case 11:
                    err_1 = _a.sent();
                    cc.systemEvent.emit('cb_login', { flag: 0, data: 'http请求异常' });
                    return [3 /*break*/, 12];
                case 12: return [2 /*return*/];
            }
        });
    });
}
exports.getAccessToken = getAccessToken;
/**
 * 微信登录-授权失败
 *
 * @export
 */
function loginError() {
    cc.systemEvent.emit('cb_login', { flag: 0, data: '授权失败' });
}
exports.loginError = loginError;
/**
 * 微信分享-结果回调
 *
 * @export
 * @param {number} result 分享结果,0为成功
 */
function shareCallback(result) {
    cc.systemEvent.emit('cb_share', result);
}
exports.shareCallback = shareCallback;

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
        //# sourceMappingURL=NativeCallJS.js.map
        