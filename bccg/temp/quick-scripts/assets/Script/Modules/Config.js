(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/Modules/Config.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, '54ad3SofyNO7b64ujzlGWLN', 'Config', __filename);
// Script/Modules/Config.ts

Object.defineProperty(exports, "__esModule", { value: true });
/**
 * 检测app版本号的请求地址
 */
exports.checkUrl = 'http://27.50.49.181:8080/checkVer?ver=';
/**
 * ws连接地址
 */
// export const wsUrl: string = 'ws://27.50.49.181:30000';
// export const wsUrl: string = 'ws://192.168.12.156:30000';
/**
 * 微信id和key
 */
exports.app_id = 'wx80fecf07fad576ad';
exports.secret = 'e669f8c640bfcaab88640d0a7fecae05';
/**
 * 微信初始化状态 0 = 成功 其它失败
 */
exports.wxState = -1;
/**
 * 资源版本号
 */
exports.version = '1.0.0';
/**
 * 服务器获取的版本信息
 */
exports.cd = null;

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
        //# sourceMappingURL=Config.js.map
        