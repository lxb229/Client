(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/SceneScript/Game/ButtonLayer.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, 'be2447zq/dBz6b7SeH2ez8V', 'ButtonLayer', __filename);
// Script/SceneScript/Game/ButtonLayer.ts

Object.defineProperty(exports, "__esModule", { value: true });
var _a = cc._decorator, ccclass = _a.ccclass, property = _a.property;
var dd = require("./../../Modules/ModuleManager");
var ButtonLayer = /** @class */ (function (_super) {
    __extends(ButtonLayer, _super);
    function ButtonLayer() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        /**
        * 表态按钮界面
        * @type {cc.Node}
        * @memberof PlayerLayer
        */
        _this.nodeBetBT = null;
        /**
         * 离开房间按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.leave = null;
        /**
         * 站起按钮(坐下后可见)
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.stand = null;
        /**
         * 分享按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.share = null;
        /**
         * 折叠按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.arrow = null;
        /**
         * 上局回顾按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.history = null;
        /**
         * 个人盈亏按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.win = null;
        /**
         * 房内用户按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.player = null;
        /**
         * 表情按钮(坐下后可见)
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.expression = null;
        /**
         * 申购按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.buyBet = null;
        /**
         * 申购列表按钮
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.dotNode = null;
        /**
         * 申购数的显示(大于才显示,包括它的父节点)
         *
         * @type {cc.Label}
         * @memberof ButtonLayer
         */
        _this.lab_dot = null;
        /**
         * 表情面板
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.bq_board = null;
        /**
         * 上局回顾面板
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.reviewLayer = null;
        /**
         * 个人盈亏面板
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.settlementLayer = null;
        /**
         * 房内用户面板
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.playersLayer = null;
        /**
         * 申购列表面板
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.emailLayer = null;
        /**
         * 左边框
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.left_board = null;
        /**
         * 表情关闭按钮图片
         *
         * @type {cc.SpriteFrame}
         * @memberof ButtonLayer
         */
        _this.bq_close = null;
        /**
         * 开始按钮(只有当有两个或两个以上的人坐下且游戏没有开始的时候显示)
         *
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.startNode = null;
        /**
         * 等待游戏开始的节点
         * @type {cc.Node}
         * @memberof ButtonLayer
         */
        _this.waitNode = null;
        /**
         * 申购记录预制
         *
         * @type {cc.Prefab}
         * @memberof ButtonLayer
         */
        _this.emailItem = null;
        /**
         * 上局回顾预制
         *
         * @type {cc.Prefab}
         * @memberof ButtonLayer
         */
        _this.reviewItem = null;
        /**
         * 个人盈亏预制
         *
         * @type {cc.Prefab}
         * @memberof ButtonLayer
         */
        _this.settlementItem = null;
        /**
         * 房内玩家预制
         *
         * @type {cc.Prefab}
         * @memberof ButtonLayer
         */
        _this.playerItem = null;
        /**
         * 申购面板预制
         *
         * @type {cc.Prefab}
         * @memberof ButtonLayer
         */
        _this.pre_buy = null;
        /**
         * 折叠筐展开状态
         *
         * @type {boolean}
         * @memberof ButtonLayer
         */
        _this.isOpen = false;
        /**
         * 表情框展示状态
         *
         * @type {boolean}
         * @memberof ButtonLayer
         */
        _this.bq_isShow = false;
        /**
         * 左边框展示状态
         *
         * @type {boolean}
         * @memberof ButtonLayer
         */
        _this.left_isShow = false;
        return _this;
    }
    ButtonLayer.prototype.onLoad = function () {
        cc.systemEvent.on('betBT', this.showBetBTLayer, this);
        this.isOpen = false;
        this.change();
        this.initBQ();
        if (dd.gm_manager.isMineCreater()) {
            this.dotNode.active = true;
            this.updateOrderCount();
        }
        else {
            this.dotNode.active = false;
        }
        //重连进入游戏的时候刷新
        this.showBetBTLayer();
        dd.gm_manager.btnScript = this;
    };
    ButtonLayer.prototype.onDestroy = function () {
        cc.systemEvent.off('betBT', this.showBetBTLayer, this);
    };
    /**
   * 显示表态按钮界面
   * @memberof PlayerLayer
   */
    ButtonLayer.prototype.showBetBTLayer = function () {
        var mineSeat = dd.gm_manager.getMineSeat();
        var tableData = dd.gm_manager.getTableData();
        if (mineSeat && tableData) {
            //在表态阶段显示表态按钮
            if (tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_1 || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_2
                || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_3 || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_4) {
                //如果表态座位是自己，并且自己是 ‘等待表态’ 状态
                if (tableData.btIndex === mineSeat.seatIndex && mineSeat.btState === 0) {
                    this.nodeBetBT.active = true;
                    var betBT = this.nodeBetBT.getComponent('BetBTLayer');
                    betBT.init();
                    this.closeAll();
                    dd.mp_manager.playBet();
                }
                else {
                    this.nodeBetBT.active = false;
                }
            }
            else {
                this.nodeBetBT.active = false;
            }
        }
        else {
            this.nodeBetBT.active = false;
        }
    };
    /**
     * 更新申购订单数量
     *
     * @param {(isOk: boolean) => void} [callback]
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.updateOrderCount = function () {
        var _this = this;
        var obj = {
            accountId: dd.ud_manager.account_mine.accountId
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.HOTPROMPT_GET_HOTDATA, msg, function (flag, content) {
            if (flag === 0) {
                var hots = content;
                hots.forEach(function (hot) {
                    if (hot.hotKey === dd.hot_key.HOT_KEY_ORDER) {
                        if (dd.gm_manager.orderCount > -1 && dd.gm_manager.orderCount < hot.hotVal) {
                            dd.mp_manager.playMsg();
                        }
                        dd.gm_manager.orderCount = hot.hotVal;
                    }
                }, _this);
            }
        });
    };
    /**
     * 初始化表情
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.initBQ = function () {
        var _this = this;
        dd.img_manager.bqSpriteFrames.forEach(function (spriteFrame) {
            var node = new cc.Node();
            node.width = 80;
            node.height = 80;
            node.tag = Number(spriteFrame.name);
            node.on(cc.Node.EventType.TOUCH_END, function (event) {
                cc.log('发送表情');
                cc.log(event.getCurrentTarget());
                var obj = {
                    tableId: dd.gm_manager.getTableData().tableId,
                    type: 1,
                    content: event.getCurrentTarget().tag
                };
                var msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.CHAT_SEND, msg, function (flag, content) {
                    if (flag !== 0) {
                        dd.ui_manager.showTip(content);
                    }
                });
                _this.closeAll();
            }, _this);
            var spriteNode = new cc.Node();
            spriteNode.addComponent(cc.Sprite).spriteFrame = spriteFrame;
            spriteNode.parent = node;
            node.parent = _this.bq_board;
        });
        var closeNode = new cc.Node();
        closeNode.width = 80;
        closeNode.height = 80;
        closeNode.on(cc.Node.EventType.TOUCH_END, function (event) {
            _this.hideBQBoard();
        }, this);
        var sprite = new cc.Node();
        sprite.addComponent(cc.Sprite).spriteFrame = this.bq_close;
        sprite.parent = closeNode;
        closeNode.parent = this.bq_board;
    };
    /**
     * 显示表情面板
     *
     * @returns
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.showBQBoard = function () {
        if (this.bq_isShow)
            return;
        this.bq_isShow = true;
        this.bq_board.stopAllActions();
        var endY = -this.node.height / 2 + this.bq_board.height / 2;
        var len = endY - this.bq_board.y;
        var rate = len / this.bq_board.height;
        this.runMoveAction(this.bq_board, rate, cc.p(0, endY));
    };
    /**
     * 隐藏表情面板
     *
     * @returns
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.hideBQBoard = function () {
        if (!this.bq_isShow)
            return;
        this.bq_isShow = false;
        this.bq_board.stopAllActions();
        var endY = -this.node.height / 2 - this.bq_board.height / 2;
        var len = this.bq_board.y - endY;
        var rate = len / this.bq_board.height;
        this.runMoveAction(this.bq_board, rate, cc.p(0, endY));
    };
    /**
     * 显示左侧边框
     *
     * @returns
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.showLFBoard = function () {
        if (this.left_isShow)
            return;
        this.left_isShow = true;
        this.left_board.stopAllActions();
        var endX = -this.node.width / 2 + this.left_board.width / 2;
        var len = endX - this.left_board.x;
        var rate = len / this.left_board.width;
        this.runMoveAction(this.left_board, rate, cc.p(endX, 0));
    };
    /**
     * 隐藏左侧边框
     *
     * @returns
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.hideLFBoard = function () {
        var _this = this;
        if (!this.left_isShow)
            return;
        this.left_isShow = false;
        this.left_board.stopAllActions();
        var endX = -this.node.width / 2 - this.left_board.width / 2;
        var len = this.left_board.x - endX;
        var rate = len / this.left_board.width;
        this.runMoveAction(this.left_board, rate, cc.p(endX, 0), function () {
            //隐藏左边框中的所有节点
            _this.showNode();
        });
    };
    /**
     * 执行节点移动动画
     *
     * @param {cc.Node} node
     * @param {number} rate
     * @param {cc.Vec2} point
     * @param {Function} [endAction]
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.runMoveAction = function (node, rate, point, endAction) {
        var moveAction = cc.moveTo(0.3 * rate, point);
        var callback = cc.callFunc(function () {
            node.setPosition(point);
            if (endAction) {
                endAction();
            }
        }, this);
        node.runAction(cc.sequence(moveAction, callback));
    };
    /**
     * 点击折叠按钮
     *
     * @param {cc.Event.EventCustom} event
     * @param {string} [data]
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_arrow = function (event, data) {
        dd.mp_manager.playButton();
        this.isOpen = !this.isOpen;
        this.change();
        this.hideBQBoard();
        this.hideLFBoard();
    };
    /**
     * 关闭所有折叠按钮,表情和侧边栏
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.closeAll = function () {
        dd.mp_manager.playButton();
        this.isOpen = false;
        this.change();
        this.hideBQBoard();
        this.hideLFBoard();
    };
    /**
     * 改变右下角折叠按钮的状态
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.change = function () {
        this.arrow.rotation = this.isOpen ? 180 : 0;
        this.history.active = this.isOpen;
        this.win.active = this.isOpen;
        this.player.active = this.isOpen;
        this.leave.active = this.isOpen;
        this.stand.active = (this.isOpen && dd.gm_manager.getMineSeat()) ? true : false;
        this.share.active = (this.isOpen && dd.config.wxState === 0) ? true : false;
        this.expression.active = (this.isOpen && dd.gm_manager.getMineSeat()) ? true : false;
        ;
        this.buyBet.active = this.isOpen;
    };
    /**
     * 控制左边框内容显示(不传为全隐藏)
     *
     * @param {cc.Node} [node] 需要显示的节点
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.showNode = function (node) {
        this.reviewLayer.active = (this.reviewLayer === node) ? true : false;
        this.settlementLayer.active = (this.settlementLayer === node) ? true : false;
        this.playersLayer.active = (this.playersLayer === node) ? true : false;
        this.emailLayer.active = (this.emailLayer === node) ? true : false;
    };
    /**
     * 点击上局回顾
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_history = function () {
        var _this = this;
        this.closeAll();
        this.showNode(this.reviewLayer);
        //清理上次的数据
        var sv = cc.find('sv', this.reviewLayer).getComponent(cc.ScrollView);
        sv.scrollToTop();
        sv.content.destroyAllChildren();
        var cardLayout = cc.find('top/cardLayout', this.reviewLayer);
        cardLayout.destroyAllChildren();
        //获取本次的数据
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_PREV_FIGHT, msg, function (flag, content) {
            if (flag === 0) {
                //显示本次数据
                var data = content;
                if (data.items && data.items.length > 0) {
                    for (var i = 0; i < 5; i++) {
                        var cardId = 0;
                        if (data.tableCards && data.tableCards.length > i) {
                            cardId = data.tableCards[i];
                        }
                        var cardNode = new cc.Node();
                        var sprite = cardNode.addComponent(cc.Sprite);
                        sprite.spriteFrame = dd.img_manager.getPokerSpriteFrameById(cardId);
                        sprite.sizeMode = cc.Sprite.SizeMode.CUSTOM;
                        cardNode.width = 50;
                        cardNode.height = 70;
                        cardNode.parent = cardLayout;
                    }
                    data.items.forEach(function (item) {
                        var itemNode = cc.instantiate(_this.reviewItem);
                        itemNode.getComponent('ReviewItem').init(item);
                        itemNode.parent = sv.content;
                    }, _this);
                }
                //移动边框到显示区域
                _this.showLFBoard();
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('上局回顾消息发送超时!');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    /**
     * 点击个人盈亏
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_win = function () {
        var _this = this;
        this.closeAll();
        this.showNode(this.settlementLayer);
        //清理上次的数据
        var sv = cc.find('sv', this.settlementLayer).getComponent(cc.ScrollView);
        sv.scrollToTop();
        sv.content.destroyAllChildren();
        //获取本次的数据
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_PLAYER_ONCE_WINSCORE, msg, function (flag, content) { return __awaiter(_this, void 0, void 0, function () {
            var _this = this;
            var data, infoNode, _a;
            return __generator(this, function (_b) {
                switch (_b.label) {
                    case 0:
                        if (!(flag === 0)) return [3 /*break*/, 2];
                        data = content;
                        infoNode = cc.find('info', this.settlementLayer);
                        _a = cc.find('head/mask/img', infoNode).getComponent(cc.Sprite);
                        return [4 /*yield*/, dd.img_manager.loadURLImage(dd.ud_manager.account_mine.roleAttribVo.headImg)];
                    case 1:
                        _a.spriteFrame = _b.sent();
                        cc.find('nick', infoNode).getComponent(cc.Label).string = dd.utils.getStringBySize(dd.ud_manager.account_mine.roleAttribVo.nick, 12);
                        cc.find('gold', infoNode).getComponent(cc.Label).string = data.currMoney;
                        cc.find('bet', infoNode).getComponent(cc.Label).string = data.buyTotalMoney;
                        if (data.items && data.items.length > 0) {
                            data.items.forEach(function (item) {
                                var itemNode = cc.instantiate(_this.settlementItem);
                                cc.find('index', itemNode).getComponent(cc.Label).string = '第' + item.gameNum + '局';
                                cc.find('gold', itemNode).getComponent(cc.Label).string = item.winScore;
                                itemNode.parent = sv.content;
                            }, this);
                        }
                        //移动边框到显示区域
                        this.showLFBoard();
                        return [3 /*break*/, 3];
                    case 2:
                        if (flag === -1) {
                            dd.ui_manager.showTip('个人盈亏消息发送超时!');
                        }
                        else {
                            dd.ui_manager.showTip(content);
                        }
                        _b.label = 3;
                    case 3: return [2 /*return*/];
                }
            });
        }); });
    };
    /**
     * 点击房内用户
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_player = function () {
        var _this = this;
        this.closeAll();
        this.showNode(this.playersLayer);
        //清理上次的数据
        var sv = cc.find('sv', this.playersLayer).getComponent(cc.ScrollView);
        sv.scrollToTop();
        sv.content.destroyAllChildren();
        //获取本次的数据
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_PLAYER_LIST, msg, function (flag, content) { return __awaiter(_this, void 0, void 0, function () {
            var _this = this;
            var data, str;
            return __generator(this, function (_a) {
                if (flag === 0) {
                    data = content;
                    str = Number(data.insuranceScore) > 0 ? ('+' + data.insuranceScore) : (data.insuranceScore);
                    cc.find('top/gold', this.playersLayer).getComponent(cc.Label).string = str;
                    if (data.items && data.items.length > 0) {
                        data.items.forEach(function (item) {
                            var playerNode = cc.instantiate(_this.playerItem);
                            playerNode.getComponent('PlayerItem').init(item);
                            playerNode.parent = sv.content;
                        }, this);
                    }
                    //移动边框到显示区域
                    this.showLFBoard();
                }
                else if (flag === -1) {
                    dd.ui_manager.showTip('房内用户消息发送超时!');
                }
                else {
                    dd.ui_manager.showTip(content);
                }
                return [2 /*return*/];
            });
        }); });
    };
    /**
     * 点击表情
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_expression = function () {
        this.closeAll();
        this.showBQBoard();
    };
    /**
     * 点击离开房间
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_leave = function () {
        this.closeAll();
        if (dd.ui_manager.showLoading('正在离开桌子,请稍后')) {
            var obj = {
                tableId: dd.gm_manager.getTableData().tableId
            };
            var msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_LEAVE, msg, function (flag, content) {
                if (flag === 0) {
                    cc.director.loadScene('HomeScene');
                }
                else if (flag === -1) {
                    dd.ui_manager.showTip('退出桌子消息发送超时!');
                }
                else {
                    dd.ui_manager.showTip(content);
                }
            });
        }
        else {
            dd.ui_manager.showTip('游戏中不能离开桌子!');
        }
    };
    /**
     * 点击站起
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_stand = function () {
        this.closeAll();
        var mineSeat = dd.gm_manager.getMineSeat();
        // if (dd.gm_manager.canStandOrLeave()) {
        var obj = {
            tableId: dd.gm_manager.getTableData().tableId,
            seatIndex: mineSeat.seatIndex
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_SEAT_UP, msg, function (flag, content) {
            if (flag === 0) {
                dd.ui_manager.showTip('离开座位');
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('站起消息发送超时!');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
        // } else {
        //     dd.ui_manager.showTip('游戏中不能离开座位!');
        // }
    };
    /**
     * 点击分享
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_share = function () {
        this.closeAll();
        dd.js_call_native.wxShare(dd.config.cd.ddUrl, '德扑约', '我在德扑约房间:' + dd.gm_manager.getTableData().tableId + '中,约吗?');
    };
    /**
     * 点击红点
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_dot = function () {
        var _this = this;
        this.closeAll();
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_QUERY_BUYCHIP_LIST, '', function (flag, content) {
            if (flag === 0) {
                var datas = content.items;
                if (datas && datas.length > 0) {
                    _this.showNode(_this.emailLayer);
                    var sv_1 = _this.emailLayer.getComponent(cc.ScrollView);
                    sv_1.content.destroyAllChildren();
                    datas.forEach(function (order) {
                        var itemNode = cc.instantiate(_this.emailItem);
                        itemNode.getComponent('EmailItem').init(order, _this.doDot);
                        itemNode.parent = sv_1.content;
                    }, _this);
                    sv_1.scrollToTop();
                    _this.showLFBoard();
                }
                else {
                    dd.ui_manager.showTip('当前没有申购记录!');
                }
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('获取申购数据超时!');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    /**
     * 处理申购点击事件
     *
     * @param {number} bt
     * @param {string} id
     * @param {cc.Node} node
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.doDot = function (bt, id, node) {
        var obj = {
            itemId: id,
            bt: bt
        };
        var msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TRANS_BUYCHIP_ITEM, msg, function (flag, content) {
            if (flag === 0) {
                if (bt === 1) {
                    dd.ui_manager.showTip('已同意该申请!');
                }
                else {
                    dd.ui_manager.showTip('已忽略该申请!');
                }
                node.destroy();
            }
            else if (flag === -1) {
                dd.ui_manager.showTip('处理申购记录超时!');
            }
            else {
                dd.ui_manager.showTip(content);
            }
        });
    };
    /**
     * 点击申购按钮
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_buy = function () {
        this.closeAll();
        cc.instantiate(this.pre_buy).parent = this.node;
    };
    /**
     * 点击游戏开始按钮
     *
     * @memberof ButtonLayer
     */
    ButtonLayer.prototype.click_start = function () {
        dd.ui_manager.showLoading('正在准备开始游戏');
        this.closeAll();
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_START_RUN, JSON.stringify({ tableId: dd.gm_manager.getTableData().tableId }), function (flag, content) {
            if (flag === 0) {
                dd.ui_manager.showTip('游戏开始');
            }
            else {
                dd.ui_manager.showTip('游戏启动失败!');
            }
        });
    };
    ButtonLayer.prototype.update = function (dt) {
        if (dd && dd.gm_manager && dd.gm_manager.getTableData()) {
            if (this.dotNode.active) {
                if (dd.gm_manager.orderCount > 0) {
                    this.lab_dot.node.parent.active = true;
                    this.lab_dot.string = dd.gm_manager.orderCount.toString();
                }
                else {
                    this.lab_dot.string = '';
                    this.lab_dot.node.parent.active = false;
                }
                //如果没有申购了，就回收
                if (this.left_isShow && this.emailLayer.active && this.emailLayer.getComponent(cc.ScrollView).content.childrenCount <= 0) {
                    this.closeAll();
                }
            }
            //如果桌子未运行
            if (dd.gm_manager.getTableData().start !== 1) {
                //如果是自己
                if (dd.gm_manager.isMineCreater()) {
                    this.startNode.active = true;
                    //如果桌子上的人数大于1 
                    if (dd.gm_manager.getSeatDownCount() > 1) {
                        this.startNode.getComponent(cc.Button).interactable = true;
                    }
                    else {
                        this.startNode.getComponent(cc.Button).interactable = false;
                    }
                }
                else {
                    this.waitNode.active = true;
                }
            }
            else {
                this.startNode.active = false;
                this.waitNode.active = false;
            }
        }
    };
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "nodeBetBT", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "leave", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "stand", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "share", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "arrow", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "history", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "win", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "player", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "expression", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "buyBet", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "dotNode", void 0);
    __decorate([
        property(cc.Label)
    ], ButtonLayer.prototype, "lab_dot", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "bq_board", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "reviewLayer", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "settlementLayer", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "playersLayer", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "emailLayer", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "left_board", void 0);
    __decorate([
        property(cc.SpriteFrame)
    ], ButtonLayer.prototype, "bq_close", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "startNode", void 0);
    __decorate([
        property(cc.Node)
    ], ButtonLayer.prototype, "waitNode", void 0);
    __decorate([
        property(cc.Prefab)
    ], ButtonLayer.prototype, "emailItem", void 0);
    __decorate([
        property(cc.Prefab)
    ], ButtonLayer.prototype, "reviewItem", void 0);
    __decorate([
        property(cc.Prefab)
    ], ButtonLayer.prototype, "settlementItem", void 0);
    __decorate([
        property(cc.Prefab)
    ], ButtonLayer.prototype, "playerItem", void 0);
    __decorate([
        property(cc.Prefab)
    ], ButtonLayer.prototype, "pre_buy", void 0);
    ButtonLayer = __decorate([
        ccclass
    ], ButtonLayer);
    return ButtonLayer;
}(cc.Component));
exports.default = ButtonLayer;

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
        //# sourceMappingURL=ButtonLayer.js.map
        