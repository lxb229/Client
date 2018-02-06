"use strict";
cc._RF.push(module, '412be3wOR5POL4lrvyl64N9', 'PlayerItem');
// Script/SceneScript/Game/PlayerItem.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var PlayerItem = /** @class */ (function (_super) {
    __extends(PlayerItem, _super);
    function PlayerItem() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 头像
         *
         * @type {cc.Sprite}
         * @memberof PlayerItem
         */
        _this.headImg = null;
        /**
         * 用户昵称
         *
         * @type {cc.Label}
         * @memberof PlayerItem
         */
        _this.nick = null;
        /**
         * 积分数
         *
         * @type {cc.Label}
         * @memberof PlayerItem
         */
        _this.gold = null;
        /**
         * 参与局数
         *
         * @type {cc.Label}
         * @memberof PlayerItem
         */
        _this.count = null;
        /**
         * 输赢量
         *
         * @type {cc.Label}
         * @memberof PlayerItem
         */
        _this.win = null;
        return _this;
    }
    PlayerItem.prototype.init = function (data) {
        return __awaiter(this, void 0, void 0, function () {
            var _a;
            return __generator(this, function (_b) {
                switch (_b.label) {
                    case 0:
                        _a = this.headImg;
                        return [4 /*yield*/, dd.img_manager.loadURLImage(data.headImg)];
                    case 1:
                        _a.spriteFrame = _b.sent();
                        this.nick.string = dd.utils.getStringBySize(data.nick, 12);
                        this.gold.string = data.currMoney;
                        this.count.string = data.gameNum;
                        this.win.string = data.winMoney;
                        return [2 /*return*/];
                }
            });
        });
    };
    __decorate([
        property(cc.Sprite)
    ], PlayerItem.prototype, "headImg", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerItem.prototype, "nick", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerItem.prototype, "gold", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerItem.prototype, "count", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerItem.prototype, "win", void 0);
    PlayerItem = __decorate([
        ccclass
    ], PlayerItem);
    return PlayerItem;
}(cc.Component));
exports.default = PlayerItem;

cc._RF.pop();