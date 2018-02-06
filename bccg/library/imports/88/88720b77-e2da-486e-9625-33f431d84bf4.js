"use strict";
cc._RF.push(module, '88720t34tpIbpYlM/Qx2Ev0', 'MPManager');
// Script/Modules/MPManager.ts

Object.defineProperty(exports, "__esModule", { value: true });
/**
 * 音频管理类
 *
 * @export
 * @class MPManager
 */
var MPManager = /** @class */ (function () {
    function MPManager() {
        this.url_allin = null;
        this.url_bet = null;
        this.url_call = null;
        this.url_check = null;
        this.url_deal = null;
        this.url_flop = null;
        this.url_fold = null;
        this.url_join = null;
        this.url_msg = null;
        this.url_over = null;
        this.url_raise = null;
        this.url_start = null;
        this.url_straddle = null;
        this.url_time = null;
        this.url_button = null;
        /**
         * 音效开关
         *
         * @private
         * @type {boolean}
         * @memberof MPManager
         */
        this.sw = true;
    }
    /**
     * 获取WSManager单例对象
     *
     * @static
     * @returns {MPManager}
     * @memberof MPManager
     */
    MPManager.getInstance = function () {
        if (MPManager._instance === null) {
            MPManager._instance = new MPManager();
        }
        return MPManager._instance;
    };
    /**
     * 初始化音频管理
     *
     * @returns {Promise<void>}
     * @memberof MPManager
     */
    MPManager.prototype.initMP = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _a, _b, _c, _d, _e, _f, _g, _h, _j, _k, _l, _m, _o, _p, _q;
            return __generator(this, function (_r) {
                switch (_r.label) {
                    case 0:
                        this.initSetting();
                        _a = this;
                        return [4 /*yield*/, this.loadFile('allin')];
                    case 1:
                        _a.url_allin = _r.sent();
                        _b = this;
                        return [4 /*yield*/, this.loadFile('bet')];
                    case 2:
                        _b.url_bet = _r.sent();
                        _c = this;
                        return [4 /*yield*/, this.loadFile('call')];
                    case 3:
                        _c.url_call = _r.sent();
                        _d = this;
                        return [4 /*yield*/, this.loadFile('check')];
                    case 4:
                        _d.url_check = _r.sent();
                        _e = this;
                        return [4 /*yield*/, this.loadFile('deal')];
                    case 5:
                        _e.url_deal = _r.sent();
                        _f = this;
                        return [4 /*yield*/, this.loadFile('flop')];
                    case 6:
                        _f.url_flop = _r.sent();
                        _g = this;
                        return [4 /*yield*/, this.loadFile('fold')];
                    case 7:
                        _g.url_fold = _r.sent();
                        _h = this;
                        return [4 /*yield*/, this.loadFile('join')];
                    case 8:
                        _h.url_join = _r.sent();
                        _j = this;
                        return [4 /*yield*/, this.loadFile('msg')];
                    case 9:
                        _j.url_msg = _r.sent();
                        _k = this;
                        return [4 /*yield*/, this.loadFile('over')];
                    case 10:
                        _k.url_over = _r.sent();
                        _l = this;
                        return [4 /*yield*/, this.loadFile('raise')];
                    case 11:
                        _l.url_raise = _r.sent();
                        _m = this;
                        return [4 /*yield*/, this.loadFile('start')];
                    case 12:
                        _m.url_start = _r.sent();
                        _o = this;
                        return [4 /*yield*/, this.loadFile('straddle')];
                    case 13:
                        _o.url_straddle = _r.sent();
                        _p = this;
                        return [4 /*yield*/, this.loadFile('time')];
                    case 14:
                        _p.url_time = _r.sent();
                        _q = this;
                        return [4 /*yield*/, this.loadFile('button')];
                    case 15:
                        _q.url_button = _r.sent();
                        return [2 /*return*/];
                }
            });
        });
    };
    /**
     * 加载音频文件
     *
     * @private
     * @param {string} name 音频名称(不带扩展名)
     * @returns {Promise<string>} 返回音频可用播放的路径
     * @memberof MPManager
     */
    MPManager.prototype.loadFile = function (name) {
        return new Promise(function (resolve, reject) {
            var path = 'Audio/' + name;
            cc.loader.loadRes(path, function (error, resource) {
                if (error) {
                    cc.error(error.message || error);
                    reject(error.message || error);
                }
                cc.loader.setAutoRelease(resource, false);
                resolve(resource);
            });
        });
    };
    /**
     * 初始化音频配置
     *
     * @memberof MPManager
     */
    MPManager.prototype.initSetting = function () {
        var db = cc.sys.localStorage;
        var sw = db.getItem('sw');
        if (sw) {
            this.sw = JSON.parse(sw).sw;
        }
        else {
            db.setItem('sw', JSON.stringify({ sw: this.sw }));
        }
    };
    /**
     * 播放allin音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playAllin = function () {
        if (this.sw && this.url_allin) {
            cc.audioEngine.play(this.url_allin, false, 1);
        }
    };
    /**
     * 播放提示当前表态玩家的音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playBet = function () {
        if (this.sw && this.url_bet) {
            cc.audioEngine.play(this.url_bet, false, 1);
        }
    };
    /**
     * 播放跟住音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playCall = function () {
        if (this.sw && this.url_call) {
            cc.audioEngine.play(this.url_call, false, 1);
        }
    };
    /**
     * 播放看牌(过牌)音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playCheck = function () {
        if (this.sw && this.url_check) {
            cc.audioEngine.play(this.url_check, false, 1);
        }
    };
    /**
     * 播放发牌音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playDeal = function () {
        if (this.sw && this.url_deal) {
            cc.audioEngine.play(this.url_deal, false, 1);
        }
    };
    /**
     * 播放翻盘音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playFlop = function () {
        if (this.sw && this.url_flop) {
            cc.audioEngine.play(this.url_flop, false, 1);
        }
    };
    /**
     * 播放弃牌音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playFold = function () {
        if (this.sw && this.url_fold) {
            cc.audioEngine.play(this.url_fold, false, 1);
        }
    };
    /**
     * 播放入座音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playJoin = function () {
        if (this.sw && this.url_join) {
            cc.audioEngine.play(this.url_join, false, 1);
        }
    };
    /**
     * 播放新消息音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playMsg = function () {
        if (this.sw && this.url_msg) {
            cc.audioEngine.play(this.url_msg, false, 1);
        }
    };
    /**
     * 播放游戏结束的音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playOver = function () {
        if (this.sw && this.url_over) {
            cc.audioEngine.play(this.url_over, false, 1);
        }
    };
    /**
     * 播放加注音效
     * @memberof MPManager
     */
    MPManager.prototype.playRaise = function () {
        if (this.sw && this.url_raise) {
            cc.audioEngine.play(this.url_raise, false, 1);
        }
    };
    /**
     * 播放游戏开始音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playStart = function () {
        if (this.sw && this.url_start) {
            cc.audioEngine.play(this.url_start, false, 1);
        }
    };
    /**
     * 播放闭眼盲音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playStraddle = function () {
        if (this.sw && this.url_straddle) {
            cc.audioEngine.play(this.url_straddle, false, 1);
        }
    };
    /**
     * 播放时间到的音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playTime = function () {
        if (this.sw && this.url_time) {
            cc.audioEngine.play(this.url_time, false, 1);
        }
    };
    /**
     * 播放按钮音效
     *
     * @memberof MPManager
     */
    MPManager.prototype.playButton = function () {
        if (this.sw && this.url_button) {
            cc.audioEngine.play(this.url_button, false, 1);
        }
    };
    /**
     * 释放销毁
     *
     * @memberof MPManager
     */
    MPManager.prototype.destroySelf = function () {
        cc.loader.release(this.url_allin);
        cc.loader.release(this.url_bet);
        cc.loader.release(this.url_call);
        cc.loader.release(this.url_check);
        cc.loader.release(this.url_deal);
        cc.loader.release(this.url_flop);
        cc.loader.release(this.url_fold);
        cc.loader.release(this.url_join);
        cc.loader.release(this.url_msg);
        cc.loader.release(this.url_over);
        cc.loader.release(this.url_raise);
        cc.loader.release(this.url_start);
        cc.loader.release(this.url_straddle);
        cc.loader.release(this.url_time);
        cc.loader.release(this.url_button);
        cc.audioEngine.uncacheAll();
        this.url_allin = null;
        this.url_bet = null;
        this.url_call = null;
        this.url_check = null;
        this.url_deal = null;
        this.url_flop = null;
        this.url_fold = null;
        this.url_join = null;
        this.url_msg = null;
        this.url_over = null;
        this.url_raise = null;
        this.url_start = null;
        this.url_straddle = null;
        this.url_time = null;
        this.url_button = null;
    };
    MPManager._instance = null;
    return MPManager;
}());
exports.default = MPManager;

cc._RF.pop();