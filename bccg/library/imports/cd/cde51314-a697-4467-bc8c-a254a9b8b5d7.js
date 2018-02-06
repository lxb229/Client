"use strict";
cc._RF.push(module, 'cde51MUppdEZ7yMolSpuLXX', 'SVScript');
// Script/SVScript.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var SVScript = /** @class */ (function (_super) {
    __extends(SVScript, _super);
    function SVScript() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * item的预制,需要绑定item脚本,且和prefab同名
         *
         * @type {cc.Prefab}
         * @memberof SVScript
         */
        _this.itemPrefab = null;
        /**
         * 两个item之间的间隔
         *
         * @type {number}
         * @memberof SVScript
         */
        _this.spacing = 0;
        /**
         * 刷新间隔
         *
         * @type {number}
         * @memberof SVScript
         */
        _this.updateInterval = 0.2;
        /**
         * 存放item的数组
         *
         * @type {cc.Node[]}
         * @memberof SVScript
         */
        _this.itemNodes = [];
        /**
         * 用于创建item的数据集合
         *
         * @type {any[]}
         * @memberof SVScript
         */
        _this.datas = null;
        /**
         * item的尺寸
         *
         * @type {cc.Size}
         * @memberof SVScript
         */
        _this.itemSize = cc.size(0, 0);
        /**
         * item的回调函数
         *
         * @type {Function}
         * @memberof SVScript
         */
        _this.callback = null;
        /**
         * 计时器时间
         *
         * @type {number}
         * @memberof SVScript
         */
        _this.updateTimer = 0;
        /**
         * 上一次content坐标
         *
         * @type {number}
         * @memberof SVScript
         */
        _this.lastContentPosY = 0;
        /**
         * scrollview组件
         *
         * @type {cc.ScrollView}
         * @memberof SVScript
         */
        _this.sv = null;
        return _this;
    }
    /**
     * 实例化滚动控件
     *
     * @param {any[]} dataList 数据集合

     * @param {Function} cb 回调函数，可用于点击item回调，item通知sv数据变化等
     * @memberof SVScript
     */
    SVScript.prototype.init = function (dataList, cb) {
        if (!dataList || dataList.length < 1)
            return;
        if (!this.itemPrefab)
            return;
        this.sv = this.node.getComponent(cc.ScrollView);
        this.itemNodes.length = 0;
        if (this.updateInterval < 0)
            this.updateInterval = 0.2;
        this.datas = dataList;
        this.callback = cb;
        var tempItem = cc.instantiate(this.itemPrefab);
        this.itemSize = tempItem.getContentSize();
        tempItem.destroy();
        this.sv.content.height = this.datas.length * (this.itemSize.height + this.spacing);
        var count = Math.ceil(this.node.height / (this.itemSize.height + this.spacing)) * 2;
        for (var i = 0; i < count; i++) {
            if (i < this.datas.length) {
                var itemNode = cc.instantiate(this.itemPrefab);
                itemNode.tag = i;
                itemNode.setPosition(0, -itemNode.height * (0.5 + i) - this.spacing * (i + 1));
                itemNode.getComponent(this.itemPrefab.name).updateItem(this.datas[i], this.callback);
                this.sv.content.addChild(itemNode);
                this.itemNodes.push(itemNode);
            }
            else {
                break;
            }
        }
    };
    //获取item以滚动控件为坐标系的坐标
    SVScript.prototype.getPositionInView = function (item) {
        var worldPos = item.parent.convertToWorldSpaceAR(item.position);
        var viewPos = this.sv.content.parent.convertToNodeSpaceAR(worldPos);
        return viewPos;
    };
    SVScript.prototype.update = function (dt) {
        var _this = this;
        if (!this.datas || this.datas.length < 1)
            return;
        this.updateTimer += dt;
        if (this.updateTimer < this.updateInterval)
            return;
        this.updateTimer = 0;
        //判断滑动方向。
        var diff = this.sv.content.y - this.lastContentPosY;
        if (diff === 0)
            return;
        var offset = (this.itemSize.height + this.spacing) * this.itemNodes.length;
        this.itemNodes.forEach(function (item) {
            var viewPos = _this.getPositionInView(item);
            var script = item.getComponent(_this.itemPrefab.name);
            if (diff < 0) {
                if (viewPos.y < -offset / 2 && item.y + offset < 0) {
                    item.setPositionY(item.y + offset);
                    item.tag = item.tag - _this.itemNodes.length;
                    script.updateItem(_this.datas[item.tag], _this.callback);
                }
            }
            else {
                if (viewPos.y > offset / 2 && item.y - offset > -_this.sv.content.height) {
                    item.setPositionY(item.y - offset);
                    item.tag = item.tag + _this.itemNodes.length;
                    script.updateItem(_this.datas[item.tag], _this.callback);
                }
            }
        }, this);
        // 更新上次记录的Y坐标
        this.lastContentPosY = this.sv.content.y;
    };
    __decorate([
        property(cc.Prefab)
    ], SVScript.prototype, "itemPrefab", void 0);
    __decorate([
        property(cc.Integer)
    ], SVScript.prototype, "spacing", void 0);
    __decorate([
        property(cc.Integer)
    ], SVScript.prototype, "updateInterval", void 0);
    SVScript = __decorate([
        ccclass
    ], SVScript);
    return SVScript;
}(cc.Component));
exports.default = SVScript;

cc._RF.pop();