(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Game/GameCanvas.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, '7e30d7OQjdA34HlcWZqyS4T', 'GameCanvas', __filename);
// Script/SceneScript/Game/GameCanvas.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var GameCanvans = /** @class */ (function (_super) {
    __extends(GameCanvans, _super);
    function GameCanvans() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 存放信息的父节点
         *
         * @type {cc.Node}
         * @memberof GameCanvans
         */
        _this.info = null;
        /**
         * 押注池预设
         * @type {cc.Prefab}
         * @memberof GameCanvans
         */
        _this.pool_prefab = null;
        /**
         * 下注池节点
         * @type {cc.Node}
         * @memberof GameCanvans
         */
        _this.pool = null;
        /**
         * 边池节点列表
         * @type {cc.Node}
         * @memberof GameCanvans
         */
        _this.sidePoolList = [];
        /**
         * 倒计时展示标签
         *
         * @type {cc.Label}
         * @memberof GameCanvans
         */
        _this.lba_time = null;
        /**
         * 底池
         * @type {cc.Label}
         * @memberof GameCanvans
         */
        _this.lbl_poolMoneys = null;
        /**
         * 保险预设
         * @type {cc.Prefab}
         * @memberof GameCanvans
         */
        _this.safe_prefab = null;
        _this._safe = null;
        return _this;
    }
    GameCanvans.prototype.onLoad = function () {
        cc.systemEvent.on('updateGame', this.showGameInfo, this);
        this.showInfo();
    };
    GameCanvans.prototype.onDestroy = function () {
        dd.gm_manager.clean();
        cc.systemEvent.off('updateGame', this.showPool, this);
    };
    /**
     * 显示桌子信息
     *
     * @memberof GameCanvans
     */
    GameCanvans.prototype.showInfo = function () {
        if (dd.gm_manager && dd.gm_manager.getTableData()) {
            var tableData = dd.gm_manager.getTableData();
            cc.find('layout2/name', this.info).getComponent(cc.Label).string = tableData.tableName;
            cc.find('layout2/id', this.info).getComponent(cc.Label).string = tableData.tableId.toString();
            cc.find('layout2/small', this.info).getComponent(cc.Label).string = tableData.smallBlind.toString();
            cc.find('layout2/big', this.info).getComponent(cc.Label).string = tableData.bigBlind.toString();
            this.showGameInfo();
        }
    };
    /**
     * 显示游戏信息
     * @memberof GameCanvans
     */
    GameCanvans.prototype.showGameInfo = function () {
        var _this = this;
        this.showSafe();
        if (dd.gm_manager && dd.gm_manager.getTableData()) {
            var tableData_1 = dd.gm_manager.getTableData();
            //如果是 新一轮积分结算 阶段，播放所有人的飞积分动作
            if (tableData_1.gameState === dd.game_state.STATE_TABLE_NEW_ROUND_BET) {
                //这里判断是否需要飞积分的动作
                var seats = tableData_1.seats;
                var index_1 = 0;
                for (var i = 0; i < seats.length; i++) {
                    var seat = seats[i];
                    if (seat.bGamed === 1 && Number(seat.betMoney) > 0) {
                        if (dd.gm_manager.playerScript) {
                            dd.gm_manager.playerScript.showFlyBetAction(seat, function () {
                                if (index_1 === 0) {
                                    _this.showPool(tableData_1.pools);
                                }
                            });
                        }
                    }
                }
            }
            else {
                if (tableData_1.gameState > dd.game_state.STATE_TABLE_BETBLIND && tableData_1.gameState < dd.game_state.STATE_TABLE_OVER_ONCE) {
                    if (tableData_1.gameState !== dd.game_state.STATE_TABLE_BET_BT_1
                        && tableData_1.gameState !== dd.game_state.STATE_TABLE_BET_BT_2
                        && tableData_1.gameState !== dd.game_state.STATE_TABLE_BET_BT_3
                        && tableData_1.gameState !== dd.game_state.STATE_TABLE_BET_BT_4) {
                        this.showPool(tableData_1.pools);
                    }
                }
                else {
                    this.sidePoolList.forEach(function (sidePool) {
                        sidePool.removeAllChildren();
                        sidePool.active = false;
                    });
                }
            }
        }
    };
    /**
     * 显示下注池
     * @memberof GameCanvans
     */
    GameCanvans.prototype.showPool = function (pools) {
        var _this = this;
        if (!pools || !pools[0])
            return;
        this.sidePoolList.forEach(function (sidePool) {
            sidePool.removeAllChildren();
        });
        pools.forEach(function (bet, index) {
            if (index < 4) {
                _this.sidePoolList[0].active = true;
                _this.creatSidePool(bet, _this.sidePoolList[0]);
            }
            else if (index >= 4 && index < 8) {
                _this.sidePoolList[1].active = true;
                _this.creatSidePool(bet, _this.sidePoolList[1]);
            }
            else {
                _this.sidePoolList[2].active = true;
                _this.creatSidePool(bet, _this.sidePoolList[2]);
            }
        });
    };
    GameCanvans.prototype.creatSidePool = function (bet, parentNode) {
        var sPool = cc.instantiate(this.pool_prefab);
        cc.find('lblBet', sPool).getComponent(cc.Label).string = bet;
        sPool.parent = parentNode;
    };
    /**
     * 显示保险
     * @memberof GameCanvans
     */
    GameCanvans.prototype.showSafe = function () {
        var tableData = dd.gm_manager.getTableData();
        if (tableData.gameState === dd.game_state.STATE_TABLE_BUY_INSURANCE) {
            if (!this._safe || !this._safe.isValid) {
                this._safe = cc.instantiate(this.safe_prefab);
                this._safe.parent = this.node;
            }
        }
        else {
            if (this._safe && this._safe.isValid) {
                this._safe.removeFromParent();
                this._safe.destroy();
            }
        }
    };
    GameCanvans.prototype.update = function (dt) {
        if (dd && dd.gm_manager) {
            //倒计时标签控制
            if (dd.gm_manager.countDownTime > 0) {
                dd.gm_manager.countDownTime -= dt * 1000;
            }
            if (dd.gm_manager.countDownTime < 0) {
                cc.log('房间时间到了,系统回收房间');
                dd.gm_manager.countDownTime = 0;
            }
            this.lba_time.string = dd.utils.getCountDownString(dd.gm_manager.countDownTime);
            if (dd.gm_manager.getTableData()) {
                if (Number(dd.gm_manager.getTableData().poolMoneys) > 0) {
                    this.lbl_poolMoneys.node.parent.active = true;
                    this.lbl_poolMoneys.string = dd.gm_manager.getTableData().poolMoneys;
                }
                else {
                    this.lbl_poolMoneys.node.parent.active = false;
                }
            }
        }
    };
    __decorate([
        property(cc.Node)
    ], GameCanvans.prototype, "info", void 0);
    __decorate([
        property(cc.Prefab)
    ], GameCanvans.prototype, "pool_prefab", void 0);
    __decorate([
        property(cc.Node)
    ], GameCanvans.prototype, "pool", void 0);
    __decorate([
        property([cc.Node])
    ], GameCanvans.prototype, "sidePoolList", void 0);
    __decorate([
        property(cc.Label)
    ], GameCanvans.prototype, "lba_time", void 0);
    __decorate([
        property(cc.Label)
    ], GameCanvans.prototype, "lbl_poolMoneys", void 0);
    __decorate([
        property(cc.Prefab)
    ], GameCanvans.prototype, "safe_prefab", void 0);
    GameCanvans = __decorate([
        ccclass
    ], GameCanvans);
    return GameCanvans;
}(cc.Component));
exports.default = GameCanvans;

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
        //# sourceMappingURL=GameCanvas.js.map
        