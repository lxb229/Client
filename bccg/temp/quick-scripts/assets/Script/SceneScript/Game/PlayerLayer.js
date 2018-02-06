(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Game/PlayerLayer.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, 'c7ad3y5yEFB44lZyiF3iCra', 'PlayerLayer', __filename);
// Script/SceneScript/Game/PlayerLayer.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var PlayerLayer = /** @class */ (function (_super) {
    __extends(PlayerLayer, _super);
    function PlayerLayer() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
         * 玩家预制
         *
         * @type {cc.Prefab}
         * @memberof PlayerLayer
         */
        _this.pre_player = null;
        /**
         * 坐位节点数组
         *
         * @type {cc.Node[]}
         * @memberof PlayerLayer
         */
        _this.seatNodes = [];
        /**
         * 公共牌列表
         * @type {cc.Node[]}
         * @memberof PlayerLayer
         */
        _this.pCardNodes = [];
        /**
         * 扑克牌预制
         *
         * @type {cc.Prefab}
         * @memberof PlayerLayer
         */
        _this.pre_card = null;
        /**
         * 积分
         * @type {cc.Prefab}
         * @memberof PlayerLayer
         */
        _this.chip_prefab = null;
        return _this;
    }
    PlayerLayer.prototype.onLoad = function () {
        var _this = this;
        cc.systemEvent.on('updatePlayer', this.updatePlayer, this);
        cc.systemEvent.on('doFPAction', this.doFPAction, this);
        cc.systemEvent.on('btResult', this.showBTResult, this);
        cc.systemEvent.on('lookCard', this.showLookCard, this);
        cc.systemEvent.on('chatPush', this.showChat, this);
        this.seatNodes.forEach(function (seatNode) {
            seatNode.getChildByName('empty').on(cc.Node.EventType.TOUCH_END, _this.click_empty, _this);
        }, this);
        this.updatePlayer();
        dd.gm_manager.playerScript = this;
    };
    PlayerLayer.prototype.onDestroy = function () {
        cc.systemEvent.off('updatePlayer', this.updatePlayer, this);
        cc.systemEvent.off('doFPAction', this.doFPAction, this);
        cc.systemEvent.off('btResult', this.showBTResult, this);
        cc.systemEvent.off('lookCard', this.showLookCard, this);
        cc.systemEvent.off('chatPush', this.showChat, this);
    };
    PlayerLayer.prototype.update = function () {
        var _this = this;
        if (dd.gm_manager && dd.gm_manager.getTableData()) {
            var tabelData = dd.gm_manager.getTableData();
            if (tabelData.gameState === dd.game_state.STATE_TABLE_OUTCARD_2) {
                if (dd.gm_manager.publicCardNum !== 3) {
                    dd.gm_manager.publicCardNum = 3;
                    //创建3张公共牌
                    this.showPublicCardAction(0, 3);
                }
            }
            else if (tabelData.gameState === dd.game_state.STATE_TABLE_OUTCARD_3) {
                if (dd.gm_manager.publicCardNum !== 4) {
                    dd.gm_manager.publicCardNum = 4;
                    //创建第4张公共牌
                    this.showPublicCardAction(3, 4);
                    this.updatePublicCard(3);
                }
            }
            else if (tabelData.gameState === dd.game_state.STATE_TABLE_OUTCARD_4) {
                if (dd.gm_manager.publicCardNum !== 5) {
                    dd.gm_manager.publicCardNum = 5;
                    //创建第5张公共牌
                    this.showPublicCardAction(4, 5);
                    this.updatePublicCard(4);
                }
            }
            else {
                this.updatePublicCard(tabelData.tableHandCards.length);
                //单局结算
                if (tabelData.gameState === dd.game_state.STATE_TABLE_OVER_ONCE) {
                    if (!dd.gm_manager.isShowGameOver) {
                        dd.gm_manager.isShowGameOver = true;
                        this.showOthersCard();
                        setTimeout(function () {
                            _this.showGameOver();
                        }, 1000);
                    }
                }
            }
        }
    };
    /**
     * 显示聊天
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showChat = function (content) {
        var data = content.detail;
        var seatInfo = dd.gm_manager.getSeatDataByAccount(data.accountId);
        if (seatInfo) {
            var playerNode = this.getNodeBySeatId(seatInfo.seatIndex);
            if (playerNode) {
                var node = new cc.Node();
                node.width = 80;
                node.height = 80;
                var spriteNode = new cc.Node();
                spriteNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.bqSpriteFrames[data.content - 1];
                spriteNode.parent = node;
                node.parent = playerNode;
                node.scale = 0;
                var action = cc.scaleTo(0.5, 1);
                action.easing(cc.easeElasticOut(0.4));
                var seq = cc.sequence(action, cc.delayTime(1), cc.callFunc(function (target, data) {
                    target.removeFromParent(true);
                    target.destroy();
                }, this));
                node.runAction(seq);
            }
        }
    };
    /**
     * 更新坐位上的玩家
     *
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.updatePlayer = function () {
        var mineSeat = dd.gm_manager.getMineSeat();
        if (mineSeat) {
            dd.gm_manager.startIndex = mineSeat.seatIndex;
        }
        var tempIndex = dd.gm_manager.startIndex;
        for (var i = 0; i < this.seatNodes.length; i++) {
            var seatData = dd.gm_manager.getSeatDataByIndex(tempIndex);
            var seatNode = this.seatNodes[i];
            var emptyNode = seatNode.getChildByName('empty');
            var playerNode = seatNode.getChildByName('Player');
            if (seatData.accountId === '0') {
                emptyNode.active = true;
                if (playerNode) {
                    playerNode.destroy();
                }
            }
            else {
                emptyNode.active = false;
                if (!playerNode) {
                    playerNode = cc.instantiate(this.pre_player);
                }
                playerNode.getComponent('PlayerUI').updateData(seatData, i);
                playerNode.parent = seatNode;
                if (i === 0 && mineSeat) {
                    playerNode.x = -150;
                }
                else {
                    playerNode.x = 0;
                }
            }
            tempIndex++;
            if (tempIndex > 8) {
                tempIndex = 0;
            }
        }
    };
    /**
     * 播放发牌动画(如果自己参与战斗发牌给自己后会执行翻牌动画)
     *
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.doFPAction = function () {
        var _this = this;
        var nodeList = [];
        var dataList = [];
        var tabelData = dd.gm_manager.getTableData();
        var dealIndex = tabelData.bankerIndex;
        for (var i = 0; i < tabelData.seats.length; i++) {
            var seatData = tabelData.seats[dealIndex];
            if (seatData.bGamed === 1) {
                var nodeIndex = seatData.seatIndex - dd.gm_manager.startIndex;
                if (nodeIndex < 0) {
                    nodeIndex = 9 + nodeIndex;
                }
                var playerNode = this.seatNodes[nodeIndex].getChildByName('Player');
                if (playerNode) {
                    nodeList.push(playerNode);
                    dataList.push(seatData);
                }
            }
            dealIndex++;
            if (dealIndex > 8) {
                dealIndex = 0;
            }
        }
        var dTime = (Number(tabelData.actTime) - Number(tabelData.svrTime)) / 1000;
        var eTime = dTime / nodeList.length / 2;
        if (eTime < 0.1) {
            eTime = 0.1;
        }
        cc.log('发每张手牌的时间' + eTime);
        var index = 0;
        var _loop_1 = function (i) {
            var _loop_2 = function (j) {
                var playerNode = nodeList[j];
                if (playerNode) {
                    var data_1 = dataList[j];
                    var endPos = playerNode.parent.getPosition().add(playerNode.getPosition());
                    var isMine_1 = playerNode.x === 0 ? false : true;
                    var actionNode = new cc.Node();
                    actionNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
                    actionNode.scale = 0.5;
                    actionNode.setPosition(0, 0);
                    actionNode.parent = this_1.node;
                    var delayAction = cc.delayTime(eTime * index);
                    var audioCallBack = cc.callFunc(function (target) {
                        dd.mp_manager.playDeal();
                    }, this_1);
                    var moveAction = cc.moveTo(eTime, endPos);
                    var scaleAction = cc.scaleTo(eTime, 1);
                    var rotateAction = cc.rotateBy(eTime, 720);
                    var spawnAction = cc.spawn(moveAction, scaleAction, rotateAction);
                    var callback = cc.callFunc(function (target) {
                        target.destroy();
                        if (isMine_1 && playerNode && playerNode.isValid) {
                            dd.gm_manager.isCreateMineCard = true;
                            //播放翻牌动画
                            var fpNode = cc.instantiate(_this.pre_card);
                            fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(data_1.handCards[i]);
                            fpNode.parent = playerNode.getChildByName('card' + (i + 1));
                            fpNode.getComponent(cc.Animation).play();
                            dd.mp_manager.playFlop();
                        }
                    }, this_1);
                    actionNode.runAction(cc.sequence(delayAction, audioCallBack, spawnAction, callback));
                    index++;
                }
            };
            for (var j = 0; j < nodeList.length; j++) {
                _loop_2(j);
            }
        };
        var this_1 = this;
        for (var i = 0; i < 2; i++) {
            _loop_1(i);
        }
    };
    /**
     * 显示结算时候的玩家看牌
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showLookCard = function () {
        var tabelData = dd.gm_manager.getTableData();
        if (tabelData && tabelData.showCardSeatIndex !== -1) {
            var seatInfo = dd.gm_manager.getSeatDataByIndex(tabelData.showCardSeatIndex);
            //不是自己才翻牌
            if (!dd.gm_manager.isMineSeat(seatInfo)) {
                var playerNode = this.getNodeBySeatId(tabelData.showCardSeatIndex);
                if (playerNode) {
                    for (var i = 0; i < 2; i++) {
                        //播放翻牌动画
                        var fpNode = cc.instantiate(this.pre_card);
                        fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(seatInfo.handCards[i]);
                        fpNode.parent = playerNode.getChildByName('card' + (i + 3));
                        fpNode.getComponent(cc.Animation).play();
                        dd.mp_manager.playFlop();
                    }
                }
            }
        }
    };
    /**
     * 显示公共牌的发牌动作
     * @param {number} sIndex
     * @param {number} eIndex 结束牌索引
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showPublicCardAction = function (sIndex, eIndex) {
        var _this = this;
        cc.log('显示公共牌的发牌动作');
        var tabelData = dd.gm_manager.getTableData();
        var index = 0;
        var dTime = (Number(tabelData.actTime) - Number(tabelData.svrTime)) / 1000;
        var eTime = dTime / (eIndex - sIndex);
        if (eTime < 0.1) {
            eTime = 0.1;
        }
        cc.log('发每张公共牌的时间' + eTime);
        var _loop_3 = function (i) {
            var pCardNode = this_2.pCardNodes[i];
            var oldCardNode = pCardNode.getChildByName('FPCard');
            if (oldCardNode) {
                oldCardNode.destroy();
            }
            var endPos = pCardNode.parent.getPosition().add(pCardNode.getPosition());
            var actionNode = new cc.Node();
            actionNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
            actionNode.scale = 0.5;
            actionNode.setPosition(0, 0);
            actionNode.parent = this_2.node;
            var delayAction = cc.delayTime(eTime * index);
            var moveAction = cc.moveTo(eTime, endPos);
            var scaleAction = cc.scaleTo(eTime, 1);
            var rotateAction = cc.rotateBy(eTime, 720);
            var spawnAction = cc.spawn(moveAction, scaleAction, rotateAction);
            var callback = cc.callFunc(function (target) {
                target.destroy();
                //播放翻牌动画
                var fpNode = cc.instantiate(_this.pre_card);
                fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(tabelData.tableHandCards[i]);
                fpNode.parent = pCardNode;
                fpNode.getComponent(cc.Animation).play();
                dd.mp_manager.playFlop();
            }, this_2);
            actionNode.runAction(cc.sequence(delayAction, spawnAction, callback));
            dd.mp_manager.playDeal();
            index++;
        };
        var this_2 = this;
        for (var i = sIndex; i < eIndex; i++) {
            _loop_3(i);
        }
    };
    /**
     * 刷新公共牌
     * @param {number} pIndex 刷新公共牌的数量
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.updatePublicCard = function (pIndex) {
        var _this = this;
        var tabelData = dd.gm_manager.getTableData();
        if (tabelData.gameState < dd.game_state.STATE_TABLE_OUTCARD_2) {
            for (var i = 0; i < this.pCardNodes.length; i++) {
                this.pCardNodes[i].removeAllChildren();
            }
        }
        else {
            tabelData.tableHandCards.forEach(function (id, index) {
                if (index < pIndex) {
                    var pNode = _this.pCardNodes[index];
                    var cardNode = pNode.getChildByName('FPCard');
                    if (!cardNode) {
                        cardNode = cc.instantiate(_this.pre_card);
                        cardNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(id);
                        cardNode.getChildByName('back').active = false;
                        cardNode.parent = pNode;
                    }
                    else {
                        var isPlayAnim = cardNode.getComponent(cc.Animation).getAnimationState('fpAction').isPlaying;
                        if (!isPlayAnim) {
                            cardNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(id);
                            cardNode.getChildByName('back').active = false;
                        }
                    }
                }
            }, this);
        }
    };
    /**
     * 显示表态人的表态结果
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showBTResult = function () {
        var tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            var prevBtIndex = tabelData.prevBtIndex;
            var prevSeat = dd.gm_manager.getSeatDataByIndex(prevBtIndex);
            if (prevSeat) {
                switch (prevSeat.btResult) {
                    case 1://弃牌
                        dd.mp_manager.playFold();
                        this.showDiscardAction(prevSeat);
                        break;
                    case 2://过牌
                        dd.mp_manager.playCheck();
                        break;
                    case 3://跟注
                        dd.mp_manager.playCall();
                        break;
                    case 4:
                        dd.mp_manager.playRaise();
                        break;
                    case 5://全下
                        dd.mp_manager.playAllin();
                        break;
                    default:
                        break;
                }
            }
        }
    };
    /**
     * 显示弃牌动作
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showDiscardAction = function (seatData) {
        var playerNode = this.getNodeBySeatId(seatData.seatIndex);
        if (playerNode) {
            var sPos = playerNode.parent.getPosition().add(playerNode.getPosition());
            var actionNode = new cc.Node();
            actionNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
            actionNode.setPosition(sPos);
            actionNode.parent = this.node;
            var moveAction = cc.moveTo(0.5, cc.p(0, 0));
            var scaleAction = cc.scaleTo(0.5, 0.5);
            var rotateAction = cc.rotateBy(0.5, 720);
            var spawnAction = cc.spawn(moveAction, scaleAction, rotateAction);
            var callback = cc.callFunc(function (target) {
                target.destroy();
            }, this);
            actionNode.runAction(cc.sequence(spawnAction, callback));
            dd.mp_manager.playFold();
        }
    };
    /**
     * 显示下注的动作
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showFlyBetAction = function (seatData, cb) {
        if (cb === void 0) { cb = null; }
        var seats = dd.gm_manager.getTableData().seats;
        var canvasTarget = dd.ui_manager.getCanvasNode().getComponent('GameCanvas');
        var ePos = canvasTarget.pool.parent.getPosition().add(canvasTarget.pool.getPosition());
        var playerNode = this.getNodeBySeatId(seatData.seatIndex);
        if (playerNode) {
            var pos = playerNode.parent.getPosition().add(playerNode.getPosition());
            this.flyBetAction(pos, ePos, '', this.node, cb);
        }
    };
    /**
     * 飞积分
     * @param {cc.Vec2} sPos 开始位置
     * @param {cc.Vec2} ePos 结束位置
     * @param {string} money 金币
     * @param {cc.Node} parentNode 父节点
     * @param {any} cb  回调函数
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.flyBetAction = function (sPos, ePos, money, parentNode, cb) {
        if (cb === void 0) { cb = null; }
        var chipNode = cc.instantiate(this.chip_prefab);
        chipNode.getChildByName('lblBet').getComponent(cc.Label).string = money;
        chipNode.setPosition(sPos);
        chipNode.parent = this.node;
        var moveAction = cc.moveTo(cc.pDistance(ePos, sPos) / 2500, ePos);
        var callback = cc.callFunc(function (target) {
            target.destroy();
            if (cb) {
                cb();
            }
        }, this);
        chipNode.runAction(cc.sequence(moveAction, callback));
    };
    /**
     * 根据座位id获取节点索引
     * @param {number} seatIndex 座位号
     * @returns
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.getNodeBySeatId = function (seatIndex) {
        var nodeIndex = seatIndex - dd.gm_manager.startIndex;
        if (nodeIndex < 0) {
            nodeIndex = 9 + nodeIndex;
        }
        var playerNode = this.seatNodes[nodeIndex].getChildByName('Player');
        return playerNode;
    };
    /**
     * 显示游戏单局结算飞积分
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showGameOver = function () {
        var canvasTarget = dd.ui_manager.getCanvasNode().getComponent('GameCanvas');
        var ePos = canvasTarget.pool.parent.getPosition().add(canvasTarget.pool.getPosition());
        var tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            dd.mp_manager.playOver();
            for (var i = 0; i < tabelData.settlementOnceList.length; i++) {
                var data = tabelData.settlementOnceList[i];
                var playerNode = this.getNodeBySeatId(data.seatIndex);
                if (playerNode) {
                    if (Number(data.winMoney) > 0) {
                        var pos = playerNode.parent.getPosition().add(playerNode.getPosition());
                        //播放飞积分的动画
                        this.flyBetAction(ePos, pos, data.winMoney, this.node);
                    }
                    //播放飞数字的动画
                    this.showWinOrLostAction(data.winMoney, playerNode);
                }
            }
        }
    };
    /**
     * 游戏结算时，显示其他人的牌
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showOthersCard = function () {
        var tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            var players = []; //游戏结算时，剩余的在游戏中的玩家数量
            for (var i = 0; i < tabelData.seats.length; i++) {
                var seat = tabelData.seats[i];
                if (seat.bGamed === 1 && seat.btResult !== 1) {
                    players.push(seat);
                }
            }
            //如果剩余的玩家数量大于1，就翻开所有剩余玩家的牌
            if (players.length > 1) {
                for (var i = 0; i < players.length; i++) {
                    var seat = players[i];
                    //不是自己才翻牌
                    if (!dd.gm_manager.isMineSeat(seat)) {
                        var playerNode = this.getNodeBySeatId(seat.seatIndex);
                        if (playerNode) {
                            for (var j = 0; j < seat.handCards.length; j++) {
                                //播放翻牌动画
                                var fpNode = cc.instantiate(this.pre_card);
                                fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(seat.handCards[j]);
                                fpNode.parent = playerNode.getChildByName('card' + (j + 3));
                                fpNode.getComponent(cc.Animation).play();
                            }
                        }
                    }
                }
            }
        }
    };
    /**
     * 显示输赢金币动作
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showWinOrLost = function () {
        var tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            for (var i = 0; i < tabelData.settlementOnceList.length; i++) {
                var data = tabelData.settlementOnceList[i];
                var playerNode = this.getNodeBySeatId(data.seatIndex);
                if (playerNode) {
                    this.showWinOrLostAction(data.winMoney, playerNode);
                }
            }
        }
    };
    /**
     * 显示输赢金币动作
     * @param {number} money
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.showWinOrLostAction = function (money, parent) {
        var actionNode = new cc.Node();
        var lbl = actionNode.addComponent(cc.Label);
        lbl.fontSize = 40;
        lbl.string = Number(money) > 0 ? ('+' + money) : money;
        actionNode.parent = parent;
        actionNode.setPosition(cc.p(0, 0));
        actionNode.color = Number(money) > 0 ? cc.Color.RED : cc.Color.GREEN;
        var move = cc.moveBy(1, cc.p(0, 100));
        var callback = cc.callFunc(function (target) {
            target.destroy();
        }, this);
        actionNode.runAction(cc.sequence(move, callback));
    };
    /**
     * 点击空位
     *
     * @param {cc.Event.EventTouch} event
     * @memberof PlayerLayer
     */
    PlayerLayer.prototype.click_empty = function (event) {
        if (dd.gm_manager.getMineSeat())
            return;
        dd.ui_manager.showLoading('正在申请坐下');
        dd.mp_manager.playButton();
        var seatIndex = Number(event.getCurrentTarget().parent.name) + dd.gm_manager.startIndex;
        if (seatIndex > 8) {
            seatIndex -= 9;
        }
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId,
            seatIndex: seatIndex
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_SEAT_DOWN, msg, function (flag, content) {
            if (flag === 0) {
                dd.ui_manager.showTip('入座成功!');
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('坐下消息发送超时');
            }
            else {
                // dd.ui_manager.showTip(content);
                dd.gm_manager.btnScript.click_buy();
                dd.ui_manager.hideLoading();
            }
        });
    };
    __decorate([
        property(cc.Prefab)
    ], PlayerLayer.prototype, "pre_player", void 0);
    __decorate([
        property([cc.Node])
    ], PlayerLayer.prototype, "seatNodes", void 0);
    __decorate([
        property([cc.Node])
    ], PlayerLayer.prototype, "pCardNodes", void 0);
    __decorate([
        property(cc.Prefab)
    ], PlayerLayer.prototype, "pre_card", void 0);
    __decorate([
        property(cc.Prefab)
    ], PlayerLayer.prototype, "chip_prefab", void 0);
    PlayerLayer = __decorate([
        ccclass
    ], PlayerLayer);
    return PlayerLayer;
}(cc.Component));
exports.default = PlayerLayer;

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
        //# sourceMappingURL=PlayerLayer.js.map
        