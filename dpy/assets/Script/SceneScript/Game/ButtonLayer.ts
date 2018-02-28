const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class ButtonLayer extends cc.Component {

    /**
    * 表态按钮界面
    * @type {cc.Node}
    * @memberof PlayerLayer
    */
    @property(cc.Node)
    nodeBetBT: cc.Node = null;
    /**
     * 离开房间按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    leave: cc.Node = null;
    /**
     * 站起按钮(坐下后可见)
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    stand: cc.Node = null;
    /**
     * 分享按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    share: cc.Node = null;
    /**
     * 折叠按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    arrow: cc.Node = null;
    /**
     * 上局回顾按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    history: cc.Node = null;
    /**
     * 个人盈亏按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    win: cc.Node = null;
    /**
     * 房内用户按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    player: cc.Node = null;
    /**
     * 表情按钮(坐下后可见)
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    expression: cc.Node = null;
    /**
     * 申购按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    buyBet: cc.Node = null;
    /**
     * 申购列表按钮
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    dotNode: cc.Node = null;
    /**
     * 申购数的显示(大于才显示,包括它的父节点)
     * 
     * @type {cc.Label}
     * @memberof ButtonLayer
     */
    @property(cc.Label)
    lab_dot: cc.Label = null;
    /**
     * 表情面板
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    bq_board: cc.Node = null;
    /**
     * 上局回顾面板
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    reviewLayer: cc.Node = null;
    /**
     * 个人盈亏面板
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    settlementLayer: cc.Node = null;
    /**
     * 房内用户面板
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    playersLayer: cc.Node = null;
    /**
     * 申购列表面板
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    emailLayer: cc.Node = null;
    /**
     * 左边框
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    left_board: cc.Node = null;
    /**
     * 表情关闭按钮图片
     * 
     * @type {cc.SpriteFrame}
     * @memberof ButtonLayer
     */
    @property(cc.SpriteFrame)
    bq_close: cc.SpriteFrame = null;
    /**
     * 开始按钮(只有当有两个或两个以上的人坐下且游戏没有开始的时候显示)
     * 
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    startNode: cc.Node = null;
    /**
     * 等待游戏开始的节点
     * @type {cc.Node}
     * @memberof ButtonLayer
     */
    @property(cc.Node)
    waitNode: cc.Node = null;
    /**
     * 申购记录预制
     * 
     * @type {cc.Prefab}
     * @memberof ButtonLayer
     */
    @property(cc.Prefab)
    emailItem: cc.Prefab = null;
    /**
     * 上局回顾预制
     * 
     * @type {cc.Prefab}
     * @memberof ButtonLayer
     */
    @property(cc.Prefab)
    reviewItem: cc.Prefab = null;
    /**
     * 个人盈亏预制
     * 
     * @type {cc.Prefab}
     * @memberof ButtonLayer
     */
    @property(cc.Prefab)
    settlementItem: cc.Prefab = null;
    /**
     * 房内玩家预制
     * 
     * @type {cc.Prefab}
     * @memberof ButtonLayer
     */
    @property(cc.Prefab)
    playerItem: cc.Prefab = null;
    /**
     * 申购面板预制
     * 
     * @type {cc.Prefab}
     * @memberof ButtonLayer
     */
    @property(cc.Prefab)
    pre_buy: cc.Prefab = null;
    /**
     * 折叠筐展开状态
     * 
     * @type {boolean}
     * @memberof ButtonLayer
     */
    isOpen: boolean = false;
    /**
     * 表情框展示状态
     * 
     * @type {boolean}
     * @memberof ButtonLayer
     */
    bq_isShow: boolean = false;
    /**
     * 左边框展示状态
     * 
     * @type {boolean}
     * @memberof ButtonLayer
     */
    left_isShow: boolean = false;

    onLoad() {
        cc.systemEvent.on('betBT', this.showBetBTLayer, this);

        this.isOpen = false;
        this.change();
        this.initBQ();
        if (dd.gm_manager.isMineCreater()) {
            this.dotNode.active = true;
            this.updateOrderCount();
        } else {
            this.dotNode.active = false;
        }
        //重连进入游戏的时候刷新
        this.showBetBTLayer();
        dd.gm_manager.btnScript = this;
    }
    onDestroy() {
        cc.systemEvent.off('betBT', this.showBetBTLayer, this);
    }
    /**
   * 显示表态按钮界面
   * @memberof PlayerLayer
   */
    showBetBTLayer() {
        let mineSeat = dd.gm_manager.getMineSeat();
        let tableData = dd.gm_manager.getTableData();
        if (mineSeat && tableData) {
            //在表态阶段显示表态按钮
            if (tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_1 || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_2
                || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_3 || tableData.gameState === dd.game_state.STATE_TABLE_BET_BT_4) {
                //如果表态座位是自己，并且自己是 ‘等待表态’ 状态
                if (tableData.btIndex === mineSeat.seatIndex && mineSeat.btState === 0) {
                    this.nodeBetBT.active = true;
                    let betBT = this.nodeBetBT.getComponent('BetBTLayer');
                    betBT.init();
                    this.closeAll();
                    dd.mp_manager.playBet();
                } else {
                    this.nodeBetBT.active = false;
                }
            } else {
                this.nodeBetBT.active = false;
            }
        } else {
            this.nodeBetBT.active = false;
        }
    }
    /**
     * 更新申购订单数量
     * 
     * @param {(isOk: boolean) => void} [callback] 
     * @memberof ButtonLayer
     */
    updateOrderCount() {
        let obj = {
            accountId: dd.ud_manager.account_mine.accountId
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.HOTPROMPT_GET_HOTDATA, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                let hots = content as HotPromptAttrib[];
                hots.forEach((hot: HotPromptAttrib) => {
                    if (hot.hotKey === dd.hot_key.HOT_KEY_ORDER) {
                        if (dd.gm_manager.orderCount > -1 && dd.gm_manager.orderCount < hot.hotVal) {
                            dd.mp_manager.playMsg();
                        }
                        dd.gm_manager.orderCount = hot.hotVal;
                    }
                }, this);
            }
        });
    }

    /**
     * 初始化表情
     * 
     * @memberof ButtonLayer
     */
    initBQ() {
        dd.img_manager.bqSpriteFrames.forEach((spriteFrame: cc.SpriteFrame) => {
            let node = new cc.Node();
            node.width = 80;
            node.height = 80;
            node.tag = Number(spriteFrame.name);
            node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                cc.log('发送表情');
                cc.log(event.getCurrentTarget());
                let obj = {
                    tableId: dd.gm_manager.getTableData().tableId,
                    type: 1,
                    content: event.getCurrentTarget().tag
                };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.CHAT_SEND, msg, (flag: number, content?: any) => {
                    if (flag !== 0) {
                        dd.ui_manager.showTip(content);
                    }
                });
                this.closeAll();
            }, this);
            let spriteNode = new cc.Node();
            spriteNode.addComponent(cc.Sprite).spriteFrame = spriteFrame;
            spriteNode.parent = node;
            node.parent = this.bq_board;
        });
        let closeNode = new cc.Node();
        closeNode.width = 80;
        closeNode.height = 80;
        closeNode.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            this.hideBQBoard();
        }, this);
        let sprite = new cc.Node();
        sprite.addComponent(cc.Sprite).spriteFrame = this.bq_close;
        sprite.parent = closeNode;
        closeNode.parent = this.bq_board;
    }
    /**
     * 显示表情面板
     * 
     * @returns 
     * @memberof ButtonLayer
     */
    showBQBoard() {
        if (this.bq_isShow) return;
        this.bq_isShow = true;
        this.bq_board.stopAllActions();
        let endY = -this.node.height / 2 + this.bq_board.height / 2;
        let len = endY - this.bq_board.y;
        let rate = len / this.bq_board.height;
        this.runMoveAction(this.bq_board, rate, cc.p(0, endY));
    }
    /**
     * 隐藏表情面板
     * 
     * @returns 
     * @memberof ButtonLayer
     */
    hideBQBoard() {
        if (!this.bq_isShow) return;
        this.bq_isShow = false;
        this.bq_board.stopAllActions();
        let endY = -this.node.height / 2 - this.bq_board.height / 2;
        let len = this.bq_board.y - endY;
        let rate = len / this.bq_board.height;
        this.runMoveAction(this.bq_board, rate, cc.p(0, endY));
    }
    /**
     * 显示左侧边框
     * 
     * @returns 
     * @memberof ButtonLayer
     */
    showLFBoard() {
        if (this.left_isShow) return;
        this.left_isShow = true;
        this.left_board.stopAllActions();
        let endX = -this.node.width / 2 + this.left_board.width / 2;
        let len = endX - this.left_board.x;
        let rate = len / this.left_board.width;
        this.runMoveAction(this.left_board, rate, cc.p(endX, 0));
    }
    /**
     * 隐藏左侧边框
     * 
     * @returns 
     * @memberof ButtonLayer
     */
    hideLFBoard() {
        if (!this.left_isShow) return;
        this.left_isShow = false;
        this.left_board.stopAllActions();
        let endX = -this.node.width / 2 - this.left_board.width / 2;
        let len = this.left_board.x - endX;
        let rate = len / this.left_board.width;
        this.runMoveAction(this.left_board, rate, cc.p(endX, 0), () => {
            //隐藏左边框中的所有节点
            this.showNode();
        });
    }
    /**
     * 执行节点移动动画
     * 
     * @param {cc.Node} node 
     * @param {number} rate 
     * @param {cc.Vec2} point 
     * @param {Function} [endAction] 
     * @memberof ButtonLayer
     */
    runMoveAction(node: cc.Node, rate: number, point: cc.Vec2, endAction?: Function) {
        let moveAction = cc.moveTo(0.3 * rate, point);
        let callback = cc.callFunc(() => {
            node.setPosition(point);
            if (endAction) {
                endAction();
            }
        }, this);
        node.runAction(cc.sequence(moveAction, callback));
    }
    /**
     * 点击折叠按钮
     * 
     * @param {cc.Event.EventCustom} event 
     * @param {string} [data] 
     * @memberof ButtonLayer
     */
    click_arrow(event: cc.Event.EventCustom, data?: string) {
        dd.mp_manager.playButton();
        this.isOpen = !this.isOpen;
        this.change();
        this.hideBQBoard();
        this.hideLFBoard();
    }
    /**
     * 关闭所有折叠按钮,表情和侧边栏
     * 
     * @memberof ButtonLayer
     */
    closeAll() {
        dd.mp_manager.playButton();
        this.isOpen = false;
        this.change();
        this.hideBQBoard();
        this.hideLFBoard();
    }
    /**
     * 改变右下角折叠按钮的状态
     * 
     * @memberof ButtonLayer
     */
    change() {
        this.arrow.rotation = this.isOpen ? 180 : 0;
        this.history.active = this.isOpen;
        this.win.active = this.isOpen;
        this.player.active = this.isOpen;
        this.leave.active = this.isOpen;
        this.stand.active = (this.isOpen && dd.gm_manager.getMineSeat()) ? true : false;
        this.share.active = (this.isOpen && dd.config.wxState === 0) ? true : false;
        this.expression.active = (this.isOpen && dd.gm_manager.getMineSeat()) ? true : false;;
        this.buyBet.active = this.isOpen;
    }
    /**
     * 控制左边框内容显示(不传为全隐藏)
     * 
     * @param {cc.Node} [node] 需要显示的节点
     * @memberof ButtonLayer
     */
    showNode(node?: cc.Node) {
        this.reviewLayer.active = (this.reviewLayer === node) ? true : false;
        this.settlementLayer.active = (this.settlementLayer === node) ? true : false;
        this.playersLayer.active = (this.playersLayer === node) ? true : false;
        this.emailLayer.active = (this.emailLayer === node) ? true : false;
    }
    /**
     * 点击上局回顾
     * 
     * @memberof ButtonLayer
     */
    click_history() {
        this.closeAll();
        this.showNode(this.reviewLayer);
        //清理上次的数据
        let sv = cc.find('sv', this.reviewLayer).getComponent(cc.ScrollView);
        sv.scrollToTop();
        sv.content.destroyAllChildren();
        let cardLayout = cc.find('top/cardLayout', this.reviewLayer);
        cardLayout.destroyAllChildren();
        //获取本次的数据
        let obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_PREV_FIGHT, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                //显示本次数据
                let data: PrevRecord = content;

                if (data.items && data.items.length > 0) {
                    for (let i = 0; i < 5; i++) {
                        let cardId = 0;
                        if (data.tableCards && data.tableCards.length > i) {
                            cardId = data.tableCards[i];
                        }
                        let cardNode = new cc.Node();
                        let sprite = cardNode.addComponent(cc.Sprite);
                        sprite.spriteFrame = dd.img_manager.getPokerSpriteFrameById(cardId);
                        sprite.sizeMode = cc.Sprite.SizeMode.CUSTOM;
                        cardNode.width = 50;
                        cardNode.height = 70;
                        cardNode.parent = cardLayout;
                    }

                    data.items.forEach((item: TablePrevFightRecordItem) => {
                        let itemNode = cc.instantiate(this.reviewItem);
                        itemNode.getComponent('ReviewItem').init(item);
                        itemNode.parent = sv.content;
                    }, this);
                }
                //移动边框到显示区域
                this.showLFBoard();
            } else if (flag === -1) {
                dd.ui_manager.showTip('上局回顾消息发送超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 点击个人盈亏
     * 
     * @memberof ButtonLayer
     */
    click_win() {
        this.closeAll();
        this.showNode(this.settlementLayer);
        //清理上次的数据
        let sv = cc.find('sv', this.settlementLayer).getComponent(cc.ScrollView);
        sv.scrollToTop();
        sv.content.destroyAllChildren();
        //获取本次的数据
        let obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_PLAYER_ONCE_WINSCORE, msg, async (flag: number, content?: any) => {
            if (flag === 0) {
                //显示本次数据
                let data: MineRecord = content;
                let infoNode = cc.find('info', this.settlementLayer);
                cc.find('head/mask/img', infoNode).getComponent(cc.Sprite).spriteFrame = await dd.img_manager.loadURLImage(dd.ud_manager.account_mine.roleAttribVo.headImg);
                cc.find('nick', infoNode).getComponent(cc.Label).string = dd.utils.getStringBySize(dd.ud_manager.account_mine.roleAttribVo.nick, 12);
                cc.find('gold', infoNode).getComponent(cc.Label).string = data.currMoney;
                cc.find('bet', infoNode).getComponent(cc.Label).string = data.buyTotalMoney;
                if (data.items && data.items.length > 0) {
                    data.items.forEach((item: WinScoreDetailsItem) => {
                        let itemNode = cc.instantiate(this.settlementItem);
                        cc.find('index', itemNode).getComponent(cc.Label).string = '第' + item.gameNum + '局';
                        cc.find('gold', itemNode).getComponent(cc.Label).string = item.winScore;
                        itemNode.parent = sv.content;
                    }, this);
                }
                //移动边框到显示区域
                this.showLFBoard();
            } else if (flag === -1) {
                dd.ui_manager.showTip('个人盈亏消息发送超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 点击房内用户
     * 
     * @memberof ButtonLayer
     */
    click_player() {
        this.closeAll();
        this.showNode(this.playersLayer);
        //清理上次的数据
        let sv = cc.find('sv', this.playersLayer).getComponent(cc.ScrollView);
        sv.scrollToTop();
        sv.content.destroyAllChildren();
        //获取本次的数据
        let obj = {
            tableId: dd.gm_manager.getTableData().tableId
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_PLAYER_LIST, msg, async (flag: number, content?: any) => {
            if (flag === 0) {
                //显示本次数据
                let data: AllUser = content;
                let str = Number(data.insuranceScore) > 0 ? ('+' + data.insuranceScore) : (data.insuranceScore);
                cc.find('top/gold', this.playersLayer).getComponent(cc.Label).string = str;
                if (data.items && data.items.length > 0) {
                    data.items.forEach((item: TablePlayerListItem) => {
                        let playerNode = cc.instantiate(this.playerItem);
                        playerNode.getComponent('PlayerItem').init(item);
                        playerNode.parent = sv.content;
                    }, this);
                }
                //移动边框到显示区域
                this.showLFBoard();
            } else if (flag === -1) {
                dd.ui_manager.showTip('房内用户消息发送超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 点击表情
     * 
     * @memberof ButtonLayer
     */
    click_expression() {
        this.closeAll();
        this.showBQBoard();
    }
    /**
     * 点击离开房间
     * 
     * @memberof ButtonLayer
     */
    click_leave() {
        this.closeAll();
        if (dd.ui_manager.showLoading('正在离开桌子,请稍后')) {
            let obj = {
                tableId: dd.gm_manager.getTableData().tableId
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_LEAVE, msg, (flag: number, content?: any) => {
                if (flag === 0) {
                    cc.director.loadScene('HomeScene');
                } else if (flag === -1) {
                    dd.ui_manager.showTip('退出桌子消息发送超时!');
                } else {
                    dd.ui_manager.showTip(content);
                }
            });
        } else {
            dd.ui_manager.showTip('游戏中不能离开桌子!');
        }
    }
    /**
     * 点击站起
     * 
     * @memberof ButtonLayer
     */
    click_stand() {
        this.closeAll();
        let mineSeat = dd.gm_manager.getMineSeat();
        // if (dd.gm_manager.canStandOrLeave()) {
        let obj = {
            tableId: dd.gm_manager.getTableData().tableId,
            seatIndex: mineSeat.seatIndex
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_SEAT_UP, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                dd.ui_manager.showTip('离开座位');
            } else if (flag === -1) {
                dd.ui_manager.showTip('站起消息发送超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
        // } else {
        //     dd.ui_manager.showTip('游戏中不能离开座位!');
        // }
    }

    /**
     * 点击分享
     * 
     * @memberof ButtonLayer
     */
    click_share() {
        this.closeAll();
        dd.js_call_native.wxShare(dd.config.cd.ddUrl, '德扑约', '我在德扑约房间:' + dd.gm_manager.getTableData().tableId + '中,约吗?');
    }
    /**
     * 点击红点
     * 
     * @memberof ButtonLayer
     */
    click_dot() {
        this.closeAll();
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_QUERY_BUYCHIP_LIST, '', (flag: number, content?: any) => {
            if (flag === 0) {
                let datas = content.items as DzpkerOrderItem[];
                if (datas && datas.length > 0) {
                    this.showNode(this.emailLayer);
                    let sv = this.emailLayer.getComponent(cc.ScrollView);
                    sv.content.destroyAllChildren();
                    datas.forEach((order: DzpkerOrderItem) => {
                        let itemNode = cc.instantiate(this.emailItem);
                        itemNode.getComponent('EmailItem').init(order, this.doDot);
                        itemNode.parent = sv.content;
                    }, this);
                    sv.scrollToTop();
                    this.showLFBoard();
                } else {
                    dd.ui_manager.showTip('当前没有申购记录!');
                }
            } else if (flag === -1) {
                dd.ui_manager.showTip('获取申购数据超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 处理申购点击事件
     * 
     * @param {number} bt 
     * @param {string} id 
     * @param {cc.Node} node 
     * @memberof ButtonLayer
     */
    doDot(bt: number, id: string, node: cc.Node) {
        let obj = {
            itemId: id,
            bt: bt
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TRANS_BUYCHIP_ITEM, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                if (bt === 1) {
                    dd.ui_manager.showTip('已同意该申请!');
                } else {
                    dd.ui_manager.showTip('已忽略该申请!');
                }
                node.destroy();
            } else if (flag === -1) {
                dd.ui_manager.showTip('处理申购记录超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 点击申购按钮
     * 
     * @memberof ButtonLayer
     */
    click_buy() {
        this.closeAll();
        cc.instantiate(this.pre_buy).parent = this.node;
    }
    /**
     * 点击游戏开始按钮
     * 
     * @memberof ButtonLayer
     */
    click_start() {
        dd.ui_manager.showLoading('正在准备开始游戏');
        this.closeAll();
        dd.ws_manager.sendMsg(
            dd.protocol.DZPKER_TABLE_START_RUN,
            JSON.stringify({ tableId: dd.gm_manager.getTableData().tableId }),
            (flag: number, content?: any) => {
                if (flag === 0) {
                    dd.ui_manager.showTip('游戏开始');
                } else {
                    dd.ui_manager.showTip('游戏启动失败!');
                }
            }
        );
    }
    update(dt: number) {
        if (dd && dd.gm_manager && dd.gm_manager.getTableData()) {
            if (this.dotNode.active) {//申购记录数量显示的控制
                if (dd.gm_manager.orderCount > 0) {
                    this.lab_dot.node.parent.active = true;
                    this.lab_dot.string = dd.gm_manager.orderCount.toString();
                } else {
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
                    } else {
                        this.startNode.getComponent(cc.Button).interactable = false;
                    }
                } else {
                    this.waitNode.active = true;
                }
            } else {
                this.startNode.active = false;
                this.waitNode.active = false;
            }
        }
    }

}
