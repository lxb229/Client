"use strict";
cc._RF.push(module, '4aa833VOuZLW4JuTW8lYHyO', 'GameResult');
// Script/SceneScript/Game/GameResult.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var NewClass = /** @class */ (function (_super) {
    __extends(NewClass, _super);
    function NewClass() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 参加局数
         * @type {cc.Label}
         * @memberof NewClass
         */
        _this.lbl_join = null;
        /**
         * 胜率
         * @type {cc.Label}
         * @memberof NewClass
         */
        _this.lbl_winRate = null;
        /**
         * 最大押注
         * @type {cc.Label}
         * @memberof NewClass
         */
        _this.lbl_maxBet = null;
        /**
         * 盈利
         * @type {cc.Label}
         * @memberof NewClass
         */
        _this.lbl_profit = null;
        /**
         * 申请积分
         * @type {cc.Label}
         * @memberof NewClass
         */
        _this.lbl_applyMoney = null;
        /**
         * 结算
         * @type {cc.Label}
         * @memberof NewClass
         */
        _this.lbl_settlement = null;
        return _this;
    }
    NewClass.prototype.onLoad = function () {
        this.lbl_join.string = '';
        this.lbl_maxBet.string = '';
        this.lbl_winRate.string = '';
        this.lbl_applyMoney.string = '';
        this.lbl_profit.string = '';
        this.lbl_settlement.string = '';
    };
    NewClass.prototype.updateData = function (data) {
        this.lbl_join.string = data.gameNum;
        this.lbl_maxBet.string = data.maxBetMoney;
        this.lbl_winRate.string = Number(data.winRate) * 100 + '%';
        this.lbl_applyMoney.string = data.buyTotalMoney;
        this.lbl_profit.string = data.maxWinMoney;
        this.lbl_settlement.string = data.winMoney;
    };
    /**
     * 返回到大厅
     * @memberof NewClass
     */
    NewClass.prototype.click_btn_out = function () {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    };
    __decorate([
        property(cc.Label)
    ], NewClass.prototype, "lbl_join", void 0);
    __decorate([
        property(cc.Label)
    ], NewClass.prototype, "lbl_winRate", void 0);
    __decorate([
        property(cc.Label)
    ], NewClass.prototype, "lbl_maxBet", void 0);
    __decorate([
        property(cc.Label)
    ], NewClass.prototype, "lbl_profit", void 0);
    __decorate([
        property(cc.Label)
    ], NewClass.prototype, "lbl_applyMoney", void 0);
    __decorate([
        property(cc.Label)
    ], NewClass.prototype, "lbl_settlement", void 0);
    NewClass = __decorate([
        ccclass
    ], NewClass);
    return NewClass;
}(cc.Component));
exports.default = NewClass;

cc._RF.pop();