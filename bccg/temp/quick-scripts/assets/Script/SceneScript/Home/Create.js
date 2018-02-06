(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Home/Create.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, 'b5d05oHishEMbHMDcBuT83I', 'Create', __filename);
// Script/SceneScript/Home/Create.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var Create = /** @class */ (function (_super) {
    __extends(Create, _super);
    function Create() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 房间名称
         *
         * @type {cc.Label}
         * @memberof Create
         */
        _this.lab_name = null;
        /**
         * 大小盲注
         *
         * @type {cc.Label}
         * @memberof Create
         */
        _this.lab_blind = null;
        /**
         * 买入积分
         *
         * @type {cc.Label}
         * @memberof Create
         */
        _this.lab_bet = null;
        /**
         * 进度条
         *
         * @type {cc.Sprite}
         * @memberof Create
         */
        _this.spr_bar = null;
        /**
         * 拖动按钮
         *
         * @type {cc.Node}
         * @memberof Create
         */
        _this.nod_btn = null;
        /**
         * 时间单选容器
         *
         * @type {cc.Node}
         * @memberof Create
         */
        _this.nod_time = null;
        /**
         * 时间单选项
         *
         * @type {cc.Prefab}
         * @memberof Create
         */
        _this.pre_toggle = null;
        /**
         * 保险开关
         *
         * @type {cc.Toggle}
         * @memberof Create
         */
        _this.tog_safe = null;
        /**
         * straddie开关
         *
         * @type {cc.Toggle}
         * @memberof Create
         */
        _this.tog_straddie = null;
        /**
         * 单次购入上限
         *
         * @type {cc.Label}
         * @memberof Create
         */
        _this.lab_limit = null;
        /**
         * 创建配置信息
         *
         * @type {CreateCfg}
         * @memberof Create
         */
        _this.cfgData = null;
        /**
         * 选中的时间
         *
         * @type {number}
         * @memberof Create
         */
        _this.selectTime = null;
        /**
         * 选中的配置对象
         *
         * @type {ChipAttrib}
         * @memberof Create
         */
        _this.selectChip = null;
        return _this;
    }
    /**
     * 初始化数据
     *
     * @param {CreateCfg} cfg
     * @memberof Create
     */
    Create.prototype.init = function (cfg) {
        this.cfgData = cfg;
    };
    Create.prototype.onLoad = function () {
        var _this = this;
        this.lab_name.string = dd.utils.getStringBySize(dd.ud_manager.account_mine.roleAttribVo.nick, 12) + '的房间';
        this.updateBet(0);
        this.updatTime();
        this.updateLimit(0);
        this.spr_bar.node.parent.on(cc.Node.EventType.TOUCH_END, function (event) {
            dd.mp_manager.playButton();
            var pos = _this.spr_bar.node.convertToNodeSpaceAR(event.touch.getLocation());
            if (_this.cfgData.chips.length < 2) {
                _this.spr_bar.fillRange = 1;
                _this.nod_btn.x = _this.spr_bar.node.width;
                _this.updateBet(0);
                return;
            }
            var index = dd.utils.getClosestIndex(_this.spr_bar.node.width, _this.cfgData.chips.length - 1, pos.x);
            pos.x = dd.utils.getClosestNumber(_this.spr_bar.node.width, _this.cfgData.chips.length - 1, pos.x);
            _this.spr_bar.fillRange = pos.x / _this.spr_bar.node.width;
            _this.nod_btn.x = pos.x;
            _this.updateBet(index);
        }, this);
        this.nod_btn.on(cc.Node.EventType.TOUCH_MOVE, function (event) {
            var target = event.getCurrentTarget();
            var pos = target.parent.convertToNodeSpaceAR(event.touch.getLocation());
            if (pos.x < 0)
                pos.x = 0;
            if (pos.x > target.parent.width)
                pos.x = target.parent.width;
            target.x = pos.x;
            _this.spr_bar.fillRange = pos.x / target.parent.width;
            var index = dd.utils.getClosestIndex(target.parent.width, _this.cfgData.chips.length - 1, pos.x);
            _this.updateBet(index);
            event.stopPropagation();
        }, this);
        this.nod_btn.on(cc.Node.EventType.TOUCH_END, this.touch_fun, this);
        this.nod_btn.on(cc.Node.EventType.TOUCH_CANCEL, this.touch_fun, this);
        dd.ui_manager.hideLoading();
    };
    /**
     * 拖动条按钮点击结束和取消事件
     *
     * @param {cc.Event.EventTouch} event
     * @returns
     * @memberof Create
     */
    Create.prototype.touch_fun = function (event) {
        dd.mp_manager.playButton();
        var target = event.getCurrentTarget();
        var pos = target.parent.convertToNodeSpaceAR(event.touch.getLocation());
        if (this.cfgData.chips.length < 2) {
            this.spr_bar.fillRange = 1;
            this.nod_btn.x = target.parent.width;
            this.updateBet(0);
            return;
        }
        var index = dd.utils.getClosestIndex(target.parent.width, this.cfgData.chips.length - 1, pos.x);
        pos.x = dd.utils.getClosestNumber(target.parent.width, this.cfgData.chips.length - 1, pos.x);
        this.spr_bar.fillRange = pos.x / target.parent.width;
        target.x = pos.x;
        this.updateBet(index);
        event.stopPropagation();
    };
    /**
     * 更新下注积分
     *
     * @param {number} index
     * @returns
     * @memberof Create
     */
    Create.prototype.updateBet = function (index) {
        if (index < 0 || index > this.cfgData.chips.length - 1)
            return;
        var chip = this.cfgData.chips[index];
        if (chip) {
            this.lab_blind.string = chip.small + '/' + chip.big;
            this.lab_bet.string = chip.join.toString();
            this.selectChip = chip;
            this.lab_limit.string = this.lab_bet.string;
            this.lab_limit.node.tag = 0;
        }
        else {
            cc.log(index);
            cc.log(this.cfgData);
            cc.error('updateBet:Error');
        }
    };
    /**
     * 更新时间
     *
     * @memberof Create
     */
    Create.prototype.updatTime = function () {
        var _this = this;
        this.cfgData.vaildTimes.forEach(function (time, index) {
            var node = cc.instantiate(_this.pre_toggle);
            node.tag = time;
            if (index === 0) {
                node.getComponent(cc.Toggle).check();
                _this.selectTime = time;
            }
            var lab_time = cc.find('time', node).getComponent(cc.Label);
            if (time < 60) {
                lab_time.string = time + 'M';
            }
            else {
                lab_time.string = Math.floor(time / 60) + 'H';
            }
            node.on('toggle', function (event) {
                dd.mp_manager.playButton();
                var toggle = event.detail;
                _this.selectTime = toggle.node.tag;
            }, _this);
            node.parent = _this.nod_time;
        }, this);
    };
    /**
     * 更新单次购入上限
     *
     * @param {number} index
     * @memberof Create
     */
    Create.prototype.updateLimit = function (index) {
        if (index < 0)
            index = 0;
        if (index > 9)
            index = 9;
        this.lab_limit.string = (Number(this.lab_bet.string) * (index + 1)).toString();
        this.lab_limit.node.tag = index;
    };
    /**
     * 点击toggle按钮播放音效
     *
     * @returns
     * @memberof Create
     */
    Create.prototype.click_toggle = function () {
        dd.mp_manager.playButton();
    };
    /**
     * 点击增加上限
     *
     * @memberof Create
     */
    Create.prototype.upLimit = function () {
        dd.mp_manager.playButton();
        var index = this.lab_limit.node.tag;
        if (index < 9) {
            index++;
            this.updateLimit(index);
        }
    };
    /**
     * 点击减小上限
     *
     * @memberof Create
     */
    Create.prototype.downLimit = function () {
        dd.mp_manager.playButton();
        var index = this.lab_limit.node.tag;
        if (index > 0) {
            index--;
            this.updateLimit(index);
        }
    };
    /**
     * 点击关闭按钮
     *
     * @memberof Create
     */
    Create.prototype.click_out = function () {
        dd.mp_manager.playButton();
        this.node.destroy();
    };
    /**
     * 点击创建房间
     *
     * @memberof Create
     */
    Create.prototype.click_create = function () {
        if (!dd.ui_manager.showLoading('正在创建房间,请稍后'))
            return;
        dd.mp_manager.playButton();
        var obj = {
            tableName: this.lab_name.string,
            small: this.selectChip.small,
            big: this.selectChip.big,
            minJoin: this.selectChip.join,
            vaildTime: this.selectTime,
            insurance: this.tog_safe.isChecked ? 1 : 0,
            straddle: this.tog_straddie.isChecked ? 1 : 0,
            buyMax: Number(this.lab_limit.string)
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_CREATE, msg, function (flag, content) {
            if (flag === 0) {
                dd.gm_manager.setTableData(content, 1);
                cc.director.loadScene('GameScene', function () {
                    dd.ui_manager.showTip('创建房间成功');
                });
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('创建房间消息发送超时');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    __decorate([
        property(cc.Label)
    ], Create.prototype, "lab_name", void 0);
    __decorate([
        property(cc.Label)
    ], Create.prototype, "lab_blind", void 0);
    __decorate([
        property(cc.Label)
    ], Create.prototype, "lab_bet", void 0);
    __decorate([
        property(cc.Sprite)
    ], Create.prototype, "spr_bar", void 0);
    __decorate([
        property(cc.Node)
    ], Create.prototype, "nod_btn", void 0);
    __decorate([
        property(cc.Node)
    ], Create.prototype, "nod_time", void 0);
    __decorate([
        property(cc.Prefab)
    ], Create.prototype, "pre_toggle", void 0);
    __decorate([
        property(cc.Toggle)
    ], Create.prototype, "tog_safe", void 0);
    __decorate([
        property(cc.Toggle)
    ], Create.prototype, "tog_straddie", void 0);
    __decorate([
        property(cc.Label)
    ], Create.prototype, "lab_limit", void 0);
    Create = __decorate([
        ccclass
    ], Create);
    return Create;
}(cc.Component));
exports.default = Create;

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
        //# sourceMappingURL=Create.js.map
        