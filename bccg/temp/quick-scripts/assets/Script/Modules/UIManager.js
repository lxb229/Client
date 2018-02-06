(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/Modules/UIManager.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, 'cbba0kC7dhEUobrKq4JhRCi', 'UIManager', __filename);
// Script/Modules/UIManager.ts

Object.defineProperty(exports, "__esModule", { value: true });
/**
 * 管理公共UI的类
 *
 * @export
 * @class UIManager
 */
var UIManager = /** @class */ (function () {
    function UIManager() {
        var _this = this;
        /**
         * 是否处于场景切换中
         *
         * @type {boolean}
         * @memberof UIManager
         */
        this.isLoadingScence = false;
        /**
         * loading 节点
         *
         * @private
         * @type {cc.Node}
         * @memberof UIManager
         */
        this.loadingNode = null;
        /**
         * tip 节点
         *
         * @private
         * @type {cc.Node}
         * @memberof UIManager
         */
        this.tipNode = null;
        /**
         * alert 节点
         *
         * @private
         * @type {cc.Node}
         * @memberof UIManager
         */
        this.alertNode = null;
        /**
         * alert节点里的确定按钮点击回调事件
         *
         * @private
         * @memberof UIManager
         */
        this.cb_yes = null;
        /**
         * alert节点里的取消按钮点击回调事件
         *
         * @private
         * @memberof UIManager
         */
        this.cb_no = null;
        /**
         * 场景切换前的回调
         *
         * @memberof UIManager
         */
        this.cb_before_scene_loading = function (event) {
            _this.isLoadingScence = true;
            _this.hideLoading();
        };
        this.cb_after_scene_loading = function (event) {
            _this.isLoadingScence = false;
        };
        /**
         * 游戏每帧刷新前的回调
         *
         * @memberof UIManager
         */
        this.cb_befor_update = function (event) {
        };
        /**
         * app切换到后台的回调
         *
         * @memberof UIManager
         */
        this.cb_app_hide = function (event) {
            if (!cc.game.isPaused()) {
                cc.game.pause();
            }
        };
        /**
         * app切换到前台的回调
         *
         * @memberof UIManager
         */
        this.cb_app_show = function (event) {
            if (cc.game.isPaused()) {
                cc.game.resume();
            }
        };
    }
    /**
     * 获取WSManager单例对象
     *
     * @static
     * @returns {UIManager}
     * @memberof UIManager
     */
    UIManager.getInstance = function () {
        if (UIManager._instance === null) {
            UIManager._instance = new UIManager();
        }
        return UIManager._instance;
    };
    /**
     * 初始化
     *
     * @memberof UIManager
     */
    UIManager.prototype.initUI = function () {
        return __awaiter(this, void 0, void 0, function () {
            var _a, _b, _c;
            return __generator(this, function (_d) {
                switch (_d.label) {
                    case 0:
                        //注册全局事件
                        cc.director.on(cc.Director.EVENT_BEFORE_SCENE_LOADING, this.cb_before_scene_loading, this);
                        cc.director.on(cc.Director.EVENT_AFTER_SCENE_LAUNCH, this.cb_after_scene_loading, this);
                        cc.director.on(cc.Director.EVENT_BEFORE_UPDATE, this.cb_befor_update, this);
                        cc.game.on(cc.game.EVENT_HIDE, this.cb_app_hide, this);
                        cc.game.on(cc.game.EVENT_SHOW, this.cb_app_show, this);
                        _a = this;
                        return [4 /*yield*/, this.loadPrefabToNode('Loading')];
                    case 1:
                        _a.loadingNode = _d.sent();
                        _b = this;
                        return [4 /*yield*/, this.loadPrefabToNode('Tip')];
                    case 2:
                        _b.tipNode = _d.sent();
                        _c = this;
                        return [4 /*yield*/, this.loadPrefabToNode('Alert')];
                    case 3:
                        _c.alertNode = _d.sent();
                        return [2 /*return*/];
                }
            });
        });
    };
    /**
     * 加载prefab,返回实例化节点Node
     *
     * @param {string} name
     * @returns {Promise<cc.Node>}
     * @memberof UIManager
     */
    UIManager.prototype.loadPrefabToNode = function (name) {
        var path = 'Prefab/' + name;
        return new Promise(function (resolve, reject) {
            cc.loader.loadRes(path, cc.Prefab, function (err, prefab) {
                if (err) {
                    reject(err.message);
                }
                else {
                    var node = cc.instantiate(prefab);
                    cc.loader.release(prefab);
                    resolve(node);
                }
            });
        });
    };
    /**
     * 显示Loading框
     *
     * @param {string} [msg='正在加载,请稍后']
     * @memberof UIManager
     */
    UIManager.prototype.showLoading = function (msg) {
        if (msg === void 0) { msg = '正在加载,请稍后'; }
        if (this.isLoadingScence)
            return false;
        if (this.loadingNode && this.loadingNode.isValid) {
            this.loadingNode.parent = this.getCanvasNode();
            this.loadingNode.setLocalZOrder(this.loadingNode.parent.childrenCount - 1);
            this.loadingNode.getComponent(cc.Animation).setCurrentTime(0);
            cc.find('layout/lbl_msg', this.loadingNode).getComponent(cc.Label).string = msg;
        }
        else {
            cc.error('loadingNode不存在');
        }
        return true;
    };
    /**
     * 隐藏loading框
     *
     * @memberof UIManager
     */
    UIManager.prototype.hideLoading = function () {
        if (this.loadingNode && this.loadingNode.parent) {
            this.loadingNode.removeFromParent();
        }
    };
    /**
     * 显示飘框提示
     *
     * @param {string} msg 提示内容
     * @param {number} [distance=400] 飘框需要移动的距离
     * @param {number} [time1=0.5]
     * @param {number} [time2=1]
     * @param {number} [time3=0.5]
     * @memberof UIManager
     */
    UIManager.prototype.showTip = function (msg, distance, time1, time2, time3) {
        if (distance === void 0) { distance = 400; }
        if (time1 === void 0) { time1 = 0.5; }
        if (time2 === void 0) { time2 = 1; }
        if (time3 === void 0) { time3 = 0.5; }
        this.hideLoading();
        if (this.tipNode) {
            var tip_1 = cc.instantiate(this.tipNode);
            tip_1.parent = this.getCanvasNode();
            tip_1.setLocalZOrder(tip_1.parent.childrenCount - 1);
            cc.find('layout/lbl_msg', tip_1).getComponent(cc.Label).string = msg;
            var layout = cc.find('layout', tip_1);
            layout.setPosition(0, -distance * 0.5);
            var action1 = cc.spawn(cc.moveTo(time1, cc.p(0, 0)), cc.fadeIn(time1));
            var action3 = cc.spawn(cc.moveTo(time3, cc.p(0, distance)), cc.fadeOut(time3));
            var endAction = cc.callFunc(function () {
                tip_1.removeFromParent();
                tip_1.destroy();
            }, this);
            layout.runAction(cc.sequence(action1, cc.delayTime(1), action3, endAction));
        }
        else {
            cc.error('tipNode不存在');
        }
    };
    /**
     * 显示弹出框
     *
     * @param {number} type 1是温馨提示,2是错误提示
     * @param {string} msg 文本内容(富文本)
     * @param {Function} [yes] 点击确定后的回调方法
     * @param {Function} [no] 点击取消后的回调方法
     * @memberof UIManager
     */
    UIManager.prototype.showAlert = function (type, msg, yes, no) {
        this.hideLoading();
        this.hideAlert();
        if (this.alertNode) {
            if (type === 1) {
                cc.find('box/wxts', this.alertNode).active = true;
                cc.find('box/cwts', this.alertNode).active = false;
            }
            else {
                cc.find('box/wxts', this.alertNode).active = false;
                cc.find('box/cwts', this.alertNode).active = true;
            }
            cc.find('box/board_msg/rt_msg', this.alertNode).getComponent(cc.RichText).string = msg;
            var btn_yes = cc.find('box/layout/btn_yes', this.alertNode);
            btn_yes.active = true;
            btn_yes.on(cc.Node.EventType.TOUCH_END, this.cb_click, this);
            if (yes)
                this.cb_yes = yes;
            var btn_no = cc.find('box/layout/btn_no', this.alertNode);
            if (no) {
                this.cb_no = no;
                btn_no.active = true;
                btn_no.on(cc.Node.EventType.TOUCH_END, this.cb_click, this);
            }
            else {
                btn_no.active = false;
            }
            this.alertNode.parent = this.getCanvasNode();
            this.alertNode.setLocalZOrder(this.alertNode.parent.childrenCount - 1);
        }
        else {
            cc.error('alertNode不存在');
        }
    };
    /**
     * alert框按钮点击事件
     *
     * @private
     * @param {cc.Event.EventTouch} event
     * @memberof UIManager
     */
    UIManager.prototype.cb_click = function (event) {
        var btn_yes = cc.find('box/layout/btn_yes', this.alertNode);
        var btn_no = cc.find('box/layout/btn_no', this.alertNode);
        if (btn_yes === event.currentTarget && this.cb_yes) {
            this.cb_yes();
        }
        if (btn_no === event.currentTarget && this.cb_no) {
            this.cb_no();
        }
        this.hideAlert();
    };
    /**
     * 隐藏Alert框
     *
     * @memberof UIManager
     */
    UIManager.prototype.hideAlert = function () {
        if (this.alertNode && this.alertNode.parent) {
            this.alertNode.removeFromParent();
        }
        cc.find('box/wxts', this.alertNode).active = false;
        cc.find('box/cwts', this.alertNode).active = false;
        cc.find('box/board_msg/rt_msg', this.alertNode).getComponent(cc.RichText).string = '';
        var btn_yes = cc.find('box/layout/btn_yes', this.alertNode);
        btn_yes.off(cc.Node.EventType.TOUCH_END, this.cb_click, this);
        this.cb_yes = null;
        btn_yes.active = false;
        var btn_no = cc.find('box/layout/btn_no', this.alertNode);
        btn_no.off(cc.Node.EventType.TOUCH_END, this.cb_click, this);
        btn_no.active = false;
        this.cb_no = null;
    };
    /**
     * 获取当前显示的Canvas节点
     *
     * @returns {cc.Node}
     * @memberof UIManager
     */
    UIManager.prototype.getCanvasNode = function () {
        return cc.director.getScene().getChildByName('Canvas');
    };
    /**
     * 获取当前场景下的RootNode节点
     *
     * @returns {cc.Node}
     * @memberof UIManager
     */
    UIManager.prototype.getRootNode = function () {
        return cc.find('RootNode', this.getCanvasNode());
    };
    /**
     * 清理销毁
     *
     * @memberof WSManager
     */
    UIManager.prototype.destroySelf = function () {
        //注销全局事件
        cc.director.off(cc.Director.EVENT_BEFORE_SCENE_LOADING, this.cb_before_scene_loading, this);
        cc.director.off(cc.Director.EVENT_BEFORE_UPDATE, this.cb_befor_update, this);
        cc.game.off(cc.game.EVENT_HIDE, this.cb_app_hide, this);
        cc.game.off(cc.game.EVENT_SHOW, this.cb_app_show, this);
        //清除loading框
        if (this.loadingNode) {
            this.hideLoading();
            this.loadingNode.destroy();
            this.loadingNode = null;
        }
        //清除tip框
        if (this.tipNode) {
            this.tipNode.destroy();
            this.tipNode = null;
        }
        //清除alert框
        if (this.alertNode) {
            this.hideAlert();
            this.alertNode.destroy();
            this.alertNode = null;
        }
        UIManager._instance = null;
    };
    UIManager._instance = null;
    return UIManager;
}());
exports.default = UIManager;

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
        //# sourceMappingURL=UIManager.js.map
        