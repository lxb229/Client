"use strict";
cc._RF.push(module, 'a0675wcy/NNNZT2/5xhcwfq', 'Join');
// Script/SceneScript/Home/Join.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var Join = /** @class */ (function (_super) {
    __extends(Join, _super);
    function Join() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 展示点击数字的标签数组
         *
         * @type {cc.Label[]}
         * @memberof Join
         */
        _this.nums = [];
        /**
         * 当前输入的数字对应的下标
         *
         * @type {number}
         * @memberof Join
         */
        _this.currentIndex = 0;
        return _this;
    }
    Join.prototype.onLoad = function () {
        var _this = this;
        this.nums.length = 0;
        this.currentIndex = 0;
        cc.find('layout', this.node).children.forEach(function (node) {
            var num = cc.find('num', node).getComponent(cc.Label);
            _this.nums.push(num);
        }, this);
        this.nums.sort(function (a, b) {
            return Number(a.node.parent.name) - Number(b.node.parent.name);
        });
        dd.ui_manager.hideLoading();
    };
    /**
     * 点击关闭按钮
     *
     * @memberof Join
     */
    Join.prototype.click_out = function () {
        dd.mp_manager.playButton();
        this.node.destroy();
    };
    /**
     * 点击按钮
     *
     * @param {cc.Event.EventTouch} event
     * @param {string} [data]
     * @memberof Join
     */
    Join.prototype.click_btn = function (event, data) {
        dd.mp_manager.playButton();
        var index = Number(data);
        if (index === 10) {
            if (this.currentIndex > 0) {
                this.currentIndex--;
                this.nums[this.currentIndex].string = '';
            }
            else {
                this.nums[0].string = '';
            }
        }
        else if (index === 11) {
            this.nums.forEach(function (label) {
                label.string = '';
            }, this);
            this.currentIndex = 0;
        }
        else {
            if (this.currentIndex < this.nums.length) {
                this.nums[this.currentIndex].string = data;
                this.currentIndex++;
                if (this.currentIndex === this.nums.length) {
                    if (!dd.ui_manager.showLoading('正在加入房间,请稍后'))
                        return;
                    var obj = { tableId: this.getNumber() };
                    var msg = JSON.stringify(obj);
                    dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_JOIN, msg, function (flag, content) {
                        if (flag === 0) {
                            dd.gm_manager.setTableData(content, 1);
                            cc.director.loadScene('GameScene', function () {
                                dd.ui_manager.showTip('加入房间成功');
                            });
                        }
                        else if (flag === -1) {
                            dd.ui_manager.showTip('加入房间消息发送超时');
                        }
                        else {
                            dd.ui_manager.showTip(content);
                        }
                    });
                }
            }
        }
    };
    /**
     * 获取所有标签所组成的数字
     *
     * @returns
     * @memberof Join
     */
    Join.prototype.getNumber = function () {
        var numStr = '';
        for (var i = 0; i < this.nums.length; i++) {
            numStr += this.nums[i].string;
        }
        return Number(numStr);
    };
    Join = __decorate([
        ccclass
    ], Join);
    return Join;
}(cc.Component));
exports.default = Join;

cc._RF.pop();