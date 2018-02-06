(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Game/PlayerUI.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, '7225d3+Uv1LLotYPhHPTmpu', 'PlayerUI', __filename);
// Script/SceneScript/Game/PlayerUI.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var PlayerUI = /** @class */ (function (_super) {
    __extends(PlayerUI, _super);
    function PlayerUI() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 头像
         *
         * @type {cc.Sprite}
         * @memberof PlayerUI
         */
        _this.headImg = null;
        /**
         * 名称
         * @type {cc.Label}
         * @memberof PlayerUI
         */
        _this.lab_nick = null;
        /**
         * 携带金币
         * @type {cc.Label}
         * @memberof PlayerUI
         */
        _this.lab_gold = null;
        /**
         * 下注金币
         * @type {cc.Label}
         * @memberof PlayerUI
         */
        _this.lab_bet = null;
        /**
         * 牌型
         * @type {cc.Label}
         * @memberof PlayerUI
         */
        _this.lab_cardType = null;
        /**
         * 倒计时
         * @type {cc.Sprite}
         * @memberof PlayerUI
         */
        _this.timer = null;
        /**
         * 庄节点
         * @type {cc.Node}
         * @memberof PlayerUI
         */
        _this.deal = null;
        /**
         * straddle节点
         * @type {cc.Node}
         * @memberof PlayerUI
         */
        _this.straddle = null;
        /**
         * 下注积分节点
         * @type {cc.Node}
         * @memberof PlayerUI
         */
        _this.board = null;
        /**
         * 小牌节点
         * @type {cc.Node}
         * @memberof PlayerUI
         */
        _this.minCard = null;
        /**
         * 两张手牌的节点集合 0 、1为手牌   2、3为看牌时候翻牌的节点
         *
         * @type {cc.Node[]}
         * @memberof PlayerUI
         */
        _this.handCards = [];
        /**
         * straddle表态按钮
         *
         * @type {cc.Node}
         * @memberof PlayerUI
         */
        _this.btn_straddle = null;
        /**
         * 亮牌按钮
         * @type {cc.Node}
         * @memberof PlayerUI
         */
        _this.btn_lookCard = null;
        /**
         * 当前节点绑定的座位对象
         *
         * @type {SeatData}
         * @memberof PlayerUI
         */
        _this.seatData = null;
        /**
         * 当前表态已经用掉的时间
         *
         * @type {number}
         * @memberof PlayerUI
         */
        _this.timeCount = -1;
        /**
         * 是否已创建了手牌
         *
         * @type {boolean}
         * @memberof PlayerUI
         */
        _this.isCreateCard = false;
        return _this;
    }
    PlayerUI.prototype.onLoad = function () {
        cc.systemEvent.on('updatePlayerUI', this.updatePlayerUI, this);
    };
    PlayerUI.prototype.updatePlayerUI = function () {
        if (!this.seatData)
            return;
        this.seatData = dd.gm_manager.getSeatDataByAccount(this.seatData.accountId);
        var tableData = dd.gm_manager.getTableData();
        if (this.seatData && tableData) {
            //如果结算的时候,最后那个亮牌的人是自己
            if (tableData.showCardSeatIndex !== -1 && tableData.showCardSeatIndex === this.seatData.seatIndex
                && tableData.gameState === dd.game_state.STATE_TABLE_OVER_ONCE) {
                this.btn_lookCard.active = true;
                var seq = cc.sequence(cc.scaleTo(0.4, 0.8), cc.scaleTo(0.4, 0.9).easing(cc.easeElasticOut(0.4)));
                var action = cc.repeatForever(seq);
                this.btn_lookCard.runAction(action);
            }
            else {
                this.btn_lookCard.stopAllActions();
                this.btn_lookCard.active = false;
            }
        }
        else {
            this.btn_lookCard.stopAllActions();
            this.btn_lookCard.active = false;
        }
    };
    PlayerUI.prototype.updateData = function (data, index) {
        this.seatData = data;
        this.setLeftOrRight(this.straddle, 100, index);
        this.setLeftOrRight(this.deal, 100, index);
        this.setLeftOrRight(this.board, 100, index);
        this.setCenter(this.minCard, -50, index);
        this.setLeftOrRight(this.lab_bet.node, 32, index);
    };
    /**
     * 设置距离左边或右边的距离
     *
     * @param {cc.Node} node
     * @param {number} len
     * @param {number} index
     * @memberof PlayerUI
     */
    PlayerUI.prototype.setLeftOrRight = function (node, len, index) {
        var widget = node.getComponent(cc.Widget);
        if (index > 4) {
            widget.isAlignRight = true;
            widget.isAbsoluteRight = true;
            widget.isAlignLeft = false;
            widget.right = len;
        }
        else {
            widget.isAlignLeft = true;
            widget.isAbsoluteLeft = true;
            widget.isAlignRight = false;
            widget.left = len;
        }
    };
    /**
     * 控制节点水平居中的偏移
     *
     * @param {cc.Node} node
     * @param {number} len
     * @param {number} index
     * @memberof PlayerUI
     */
    PlayerUI.prototype.setCenter = function (node, len, index) {
        var widget = node.getComponent(cc.Widget);
        widget.isAlignHorizontalCenter = true;
        widget.isAbsoluteHorizontalCenter = true;
        if (index > 4) {
            widget.horizontalCenter = -len;
        }
        else {
            widget.horizontalCenter = len;
        }
    };
    PlayerUI.prototype.update = function (dt) {
        return __awaiter(this, void 0, void 0, function () {
            var _this = this;
            var tableData, _a;
            return __generator(this, function (_b) {
                switch (_b.label) {
                    case 0:
                        if (!(dd && dd.gm_manager)) return [3 /*break*/, 2];
                        tableData = dd.gm_manager.getTableData();
                        if (!(this.seatData && tableData)) return [3 /*break*/, 2];
                        this.deal.active = (tableData.bankerIndex === this.seatData.seatIndex) ? true : false;
                        //这个玩家是否参加游戏
                        if (this.seatData.bGamed === 1) {
                            switch (this.seatData.btResult) {
                                case 1:
                                    this.lab_nick.string = '弃  牌';
                                    break;
                                case 2:
                                    this.lab_nick.string = '过  牌';
                                    break;
                                case 3:
                                    this.lab_nick.string = '跟  注';
                                    break;
                                case 4:
                                    this.lab_nick.string = '加  注';
                                    break;
                                case 5:
                                    this.lab_nick.string = 'ALL IN';
                                    break;
                                default: {
                                    if (this.seatData.btState === 0 && this.seatData.seatIndex === tableData.btIndex
                                        && tableData.gameState < dd.game_state.STATE_TABLE_OVER_ONCE) {
                                        this.lab_nick.string = '思考中...';
                                    }
                                    else {
                                        this.lab_nick.string = dd.utils.getStringBySize(this.seatData.nick, 12);
                                    }
                                    break;
                                }
                            }
                        }
                        else {
                            this.lab_nick.string = '等待下局';
                        }
                        //如果在庄家定大小盲阶段
                        if (tableData.gameState === dd.game_state.STATE_TABLE_BETBLIND) {
                            if (this.seatData.straddleFlag === 1) {
                                this.lab_nick.string = 'straddle';
                            }
                            else {
                                this.lab_nick.string = dd.utils.getStringBySize(this.seatData.nick, 12);
                            }
                        }
                        this.lab_gold.string = this.seatData.currMoney;
                        this.board.active = Number(this.seatData.betMoney) > 0 ? true : false;
                        if (this.board.active) {
                            this.lab_bet.string = this.seatData.betMoney;
                        }
                        this.straddle.active = this.seatData.straddleFlag === 1 ? true : false;
                        //在订庄家大小盲注的时候，才显示 straddle
                        if (tableData.gameState === dd.game_state.STATE_TABLE_BETBLIND) {
                            this.btn_straddle.active = (this.seatData.straddle === 1) && dd.gm_manager.isMineSeat(this.seatData) ? true : false;
                        }
                        else {
                            this.btn_straddle.active = false;
                        }
                        if (this.seatData.btResult === 1 || dd.gm_manager.isMineSeat(this.seatData) || tableData.gameState < dd.game_state.STATE_TABLE_OUTCARD_1) {
                            //自己和弃牌的人不显示小牌
                            this.minCard.active = false;
                            //自己和别人，小于发牌的阶段，都不显示牌，不管是亮牌还是手牌
                            if (tableData.gameState < dd.game_state.STATE_TABLE_OUTCARD_1) {
                                this.handCards.forEach(function (card) {
                                    if (card.childrenCount > 0) {
                                        card.destroyAllChildren();
                                    }
                                }, this);
                            }
                        }
                        else {
                            this.minCard.active = true;
                        }
                        //判断当前是否该作为表态(排除straddle)
                        if (this.seatData.btState === 0 && tableData.btIndex === this.seatData.seatIndex) {
                            if (tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_1
                                || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_2
                                || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_3
                                || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_4) {
                                if (this.timeCount === -1) {
                                    //表态已经用了的时间
                                    this.timeCount = Number(tableData.actTotalTime) - (Number(tableData.actTime) - Number(tableData.svrTime));
                                    if (this.timeCount < 0) {
                                        this.timeCount = 0;
                                    }
                                    if (this.timeCount > Number(tableData.actTotalTime)) {
                                        this.timeCount = Number(tableData.actTotalTime);
                                    }
                                }
                                this.timeCount += dt * 1000;
                                if (this.timeCount > Number(tableData.actTotalTime)) {
                                    this.timeCount = Number(tableData.actTotalTime);
                                }
                                this.timer.fillRange = -this.timeCount / Number(tableData.actTotalTime);
                                //播放倒计时
                                if (this.timer.fillRange === -0.8) {
                                    dd.mp_manager.playTime();
                                }
                            }
                            else {
                                this.timeCount = -1;
                                this.timer.fillRange = 0;
                            }
                        }
                        else {
                            this.timeCount = -1;
                            this.timer.fillRange = 0;
                        }
                        if (dd.gm_manager.isMineSeat(this.seatData)) {
                            if (tableData.gameState > dd.game_state.STATE_TABLE_OUTCARD_1 && this.seatData.btResult !== 1) {
                                this.lab_cardType.string = dd.gm_manager.getCardType(this.seatData.cardType);
                            }
                            else {
                                this.lab_cardType.string = '';
                            }
                            if (tableData.gameState > dd.game_state.STATE_TABLE_OUTCARD_1 && !dd.gm_manager.isCreateMineCard && this.seatData.btResult !== 1) {
                                dd.gm_manager.isCreateMineCard = true;
                                this.seatData.handCards.forEach(function (id, index) {
                                    var card = _this.handCards[index];
                                    if (card.childrenCount < 1) {
                                        var cardNode = new cc.Node();
                                        cardNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(id);
                                        cardNode.parent = card;
                                    }
                                }, this);
                            }
                            if (tableData.gameState < dd.game_state.STATE_TABLE_OUTCARD_1 || this.seatData.btResult === 1) {
                                if (dd.gm_manager.isCreateMineCard) {
                                    dd.gm_manager.isCreateMineCard = false;
                                    this.handCards.forEach(function (card) {
                                        if (card.childrenCount > 0) {
                                            card.destroyAllChildren();
                                        }
                                    }, this);
                                }
                            }
                            //如果不为 结算阶段，不显示亮牌按钮
                            if (tableData.gameState !== dd.game_state.STATE_TABLE_OVER_ONCE) {
                                this.btn_lookCard.stopAllActions();
                                this.btn_lookCard.active = false;
                            }
                        }
                        else {
                            this.lab_cardType.string = '';
                            this.btn_lookCard.active = false;
                        }
                        //显示头像的部分，因为是异步，所以要放到最后
                        _a = this.headImg;
                        return [4 /*yield*/, dd.img_manager.loadURLImage(this.seatData.headImg)];
                    case 1:
                        //显示头像的部分，因为是异步，所以要放到最后
                        _a.spriteFrame = _b.sent();
                        _b.label = 2;
                    case 2: return [2 /*return*/];
                }
            });
        });
    };
    /**
     * 亮牌按钮
     * @memberof PlayerUI
     */
    PlayerUI.prototype.click_btn_lookCard = function () {
        dd.mp_manager.playButton();
        this.btn_lookCard.active = false;
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_SHOW_CARD_BT, msg);
    };
    /**
     * 闭眼盲注表态
     * @memberof PlayerUI
     */
    PlayerUI.prototype.click_btn_straddle = function () {
        dd.mp_manager.playButton();
        this.btn_lookCard.active = false;
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId,
            seatIndex: this.seatData.seatIndex
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_STRADDLE_BT, msg);
    };
    __decorate([
        property(cc.Sprite)
    ], PlayerUI.prototype, "headImg", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerUI.prototype, "lab_nick", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerUI.prototype, "lab_gold", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerUI.prototype, "lab_bet", void 0);
    __decorate([
        property(cc.Label)
    ], PlayerUI.prototype, "lab_cardType", void 0);
    __decorate([
        property(cc.Sprite)
    ], PlayerUI.prototype, "timer", void 0);
    __decorate([
        property(cc.Node)
    ], PlayerUI.prototype, "deal", void 0);
    __decorate([
        property(cc.Node)
    ], PlayerUI.prototype, "straddle", void 0);
    __decorate([
        property(cc.Node)
    ], PlayerUI.prototype, "board", void 0);
    __decorate([
        property(cc.Node)
    ], PlayerUI.prototype, "minCard", void 0);
    __decorate([
        property([cc.Node])
    ], PlayerUI.prototype, "handCards", void 0);
    __decorate([
        property(cc.Node)
    ], PlayerUI.prototype, "btn_straddle", void 0);
    __decorate([
        property(cc.Node)
    ], PlayerUI.prototype, "btn_lookCard", void 0);
    PlayerUI = __decorate([
        ccclass
    ], PlayerUI);
    return PlayerUI;
}(cc.Component));
exports.default = PlayerUI;

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
        //# sourceMappingURL=PlayerUI.js.map
        