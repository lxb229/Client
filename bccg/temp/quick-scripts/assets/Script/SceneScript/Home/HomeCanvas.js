(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Home/HomeCanvas.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, '7f0faPnyzRIkZKF3vOVP7Iq', 'HomeCanvas', __filename);
// Script/SceneScript/Home/HomeCanvas.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var HomeCanvas = /** @class */ (function (_super) {
    __extends(HomeCanvas, _super);
    function HomeCanvas() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 头像
         *
         * @type {cc.Sprite}
         * @memberof HomeCanvas
         */
        _this.spr_head = null;
        /**
         * 用户昵称
         *
         * @type {cc.Label}
         * @memberof HomeCanvas
         */
        _this.lab_name = null;
        /**
         * 玩家id
         *
         * @type {cc.Label}
         * @memberof HomeCanvas
         */
        _this.lab_id = null;
        /**
         * 设置界面
         *
         * @type {cc.Prefab}
         * @memberof HomeCanvas
         */
        _this.pre_setting = null;
        /**
         * 创建界面
         *
         * @type {cc.Prefab}
         * @memberof HomeCanvas
        */
        _this.pre_create = null;
        /**
         * 加入房间
         *
         * @type {cc.Prefab}
         * @memberof HomeCanvas
         */
        _this.pre_join = null;
        /**
         * 公告父节点
         *
         * @type {cc.Node}
         * @memberof HomeCanvas
         */
        _this.noticeNode = null;
        /**
         * 公告内容
         * @type {cc.Node}
         * @memberof HomeCanvas
         */
        _this.lblNoticeCT = null;
        /**
         * 跑马灯节点
         *
         * @type {cc.Node}
         * @memberof HomeCanvas
         */
        _this.msgLayout = null;
        /**
         * 是否在跑公告
         *
         * @type {boolean}
         * @memberof HomeCanvas
         */
        _this._isRunNotice = false;
        return _this;
    }
    HomeCanvas.prototype.onLoad = function () {
        cc.log(dd.ud_manager.account_mine);
    };
    HomeCanvas.prototype.update = function (dt) {
        return __awaiter(this, void 0, void 0, function () {
            var _a, error_1;
            return __generator(this, function (_b) {
                switch (_b.label) {
                    case 0:
                        if (!(dd.ud_manager && dd.ud_manager.account_mine)) return [3 /*break*/, 4];
                        if (!dd.ud_manager.account_mine.roleAttribVo) return [3 /*break*/, 4];
                        this.lab_name.string = dd.utils.getStringBySize(dd.ud_manager.account_mine.roleAttribVo.nick, 12);
                        this.lab_id.string = dd.ud_manager.account_mine.roleAttribVo.starNO;
                        _b.label = 1;
                    case 1:
                        _b.trys.push([1, 3, , 4]);
                        _a = this.spr_head;
                        return [4 /*yield*/, dd.img_manager.loadURLImage(dd.ud_manager.account_mine.roleAttribVo.headImg)];
                    case 2:
                        _a.spriteFrame = _b.sent();
                        return [3 /*break*/, 4];
                    case 3:
                        error_1 = _b.sent();
                        cc.log('获取头像错误');
                        return [3 /*break*/, 4];
                    case 4: return [2 /*return*/];
                }
            });
        });
    };
    /**
     * 更新公告显示位置（跑灯效果）
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.checkPos = function () {
        if (this._isRunNotice) {
            var widget = this.msgLayout.getComponent(cc.Widget);
            if (widget.left < 0 && Math.abs(widget.left) >= this.msgLayout.width) {
                this.msgLayout.removeAllChildren();
                widget.left = this.noticeNode.width;
                this._isRunNotice = false;
                this.noticeNode.active = false;
            }
            else {
                widget.left -= 1;
            }
        }
        else {
            if (dd.ud_manager && dd.ud_manager.noticeList && dd.ud_manager.noticeList.length > 0) {
                this.createNotice(dd.ud_manager.noticeList.shift());
            }
            else {
                var np = {
                    content: '抵制不良游戏, 拒绝盗版游戏。 注意自我保护, 谨防受骗上当。 适度游戏益脑, 沉迷游戏伤身。 合理安排时间, 享受健康生活！',
                    type: 2,
                    color: [150, 150, 150]
                };
                var notice = {
                    msgId: '0',
                    contents: [np],
                };
                this.createNotice(notice);
            }
        }
    };
    /**
     * 创建公告消息
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.createNotice = function (data) {
        this.msgLayout.removeAllChildren();
        for (var i = 0; i < data.contents.length; i++) {
            var msgData = data.contents[i];
            var tNode = new cc.Node('notice');
            tNode.color = new cc.Color(msgData.color[0], msgData.color[1], msgData.color[2]);
            var lbl = tNode.addComponent(cc.Label);
            lbl.fontSize = 30;
            lbl.lineHeight = 30;
            lbl.overflow = cc.Label.Overflow.NONE;
            lbl.horizontalAlign = cc.Label.HorizontalAlign.CENTER;
            lbl.verticalAlign = cc.Label.VerticalAlign.CENTER;
            lbl.string = msgData.content;
            // lbl.string = '大王叫我来巡山，我来牌馆转一转。胡着我的牌，数着赢的钱，生活充满节奏感。';
            this.msgLayout.addChild(tNode);
        }
        this._isRunNotice = true;
        this.noticeNode.active = true;
    };
    /**
     * 点击设置按钮
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_setting = function () {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        var setNode = cc.instantiate(this.pre_setting);
        setNode.parent = dd.ui_manager.getRootNode();
    };
    /**
     * 点击公告按钮
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_notice = function () {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
    };
    /**
     * 点击生涯按钮
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_career = function () {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
    };
    /**
     * 点击商店按钮
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_store = function () {
    };
    /**
     * 点击创建房间
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_create = function () {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        var createNode = cc.instantiate(this.pre_create);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_GET_CFG, '', function (flag, content) {
            if (flag === 0) {
                var cfg = content;
                createNode.getComponent('Create').init(cfg);
                createNode.parent = dd.ui_manager.getRootNode();
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('获取房间配置消息发送超时');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    /**
     * 点击加入房间
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_join = function () {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        var joinNode = cc.instantiate(this.pre_join);
        joinNode.parent = dd.ui_manager.getRootNode();
    };
    /**
     * 点击我的房间
     *
     * @memberof HomeCanvas
     */
    HomeCanvas.prototype.click_mine = function () {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
    };
    __decorate([
        property(cc.Sprite)
    ], HomeCanvas.prototype, "spr_head", void 0);
    __decorate([
        property(cc.Label)
    ], HomeCanvas.prototype, "lab_name", void 0);
    __decorate([
        property(cc.Label)
    ], HomeCanvas.prototype, "lab_id", void 0);
    __decorate([
        property(cc.Prefab)
    ], HomeCanvas.prototype, "pre_setting", void 0);
    __decorate([
        property(cc.Prefab)
    ], HomeCanvas.prototype, "pre_create", void 0);
    __decorate([
        property(cc.Prefab)
    ], HomeCanvas.prototype, "pre_join", void 0);
    __decorate([
        property(cc.Node)
    ], HomeCanvas.prototype, "noticeNode", void 0);
    __decorate([
        property(cc.Label)
    ], HomeCanvas.prototype, "lblNoticeCT", void 0);
    __decorate([
        property(cc.Node)
    ], HomeCanvas.prototype, "msgLayout", void 0);
    HomeCanvas = __decorate([
        ccclass
    ], HomeCanvas);
    return HomeCanvas;
}(cc.Component));
exports.default = HomeCanvas;

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
        //# sourceMappingURL=HomeCanvas.js.map
        