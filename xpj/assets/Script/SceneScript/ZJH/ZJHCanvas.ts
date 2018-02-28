
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import ZJH_Player from './ZJH_Player';
import ZJH_Table from './ZJH_Table';
import Game_DealScript from './Game_DealScript';
import * as ZJH_Help from './ZJH_Help';
@ccclass
export default class ZJHCanvas extends cc.Component {
    /**
     * 动作节点
     * 
     * @type {cc.Node}
     * @memberof ZJHCanvas
     */
    @property(cc.Node)
    node_action: cc.Node = null;
    /**
     * 玩家界面节点
     * 
     * @type {cc.Node}
     * @memberof ZJHCanvas
     */
    @property(cc.Node)
    node_player: cc.Node = null;
    /**
     * 桌子界面节点
     * 
     * @type {cc.Node}
     * @memberof ZJHCanvas
     */
    @property(cc.Node)
    node_table: cc.Node = null;
    /**
     * 游戏场景的遮罩层
     * @type {cc.Node}
     * @memberof ZJHCanvas
     */
    @property(cc.Node)
    node_mask: cc.Node = null;
    /**
     *牌节点
     * @type {cc.Prefab}
     * @memberof ZJHCanvas
     */
    @property(cc.Prefab)
    card_prefab: cc.Prefab = null;
    /**
     * 翻牌动画
     * @type {cc.Prefab}
     * @memberof ZJHCanvas
     */
    @property(cc.Prefab)
    fanpai_prefab: cc.Prefab = null;
    /**
     * 比牌预设
     * @type {cc.Prefab}
     * @memberof ZJHCanvas
     */
    @property(cc.Prefab)
    bpPrefab: cc.Prefab = null;
    /**
     * 比牌失败预设
     * @type {cc.Prefab}
     * @memberof ZJHCanvas
     */
    @property(cc.Prefab)
    bpsbPrefab: cc.Prefab = null;
    /**
     * 牌抖动的预设
     * @type {cc.Prefab}
     * @memberof ZJHCanvas
     */
    @property(cc.Prefab)
    spPrefab: cc.Prefab = null;
    /**
     * 倒计时的预设 
     * @type {cc.Prefab[]} 0=自己 1=别人
     * @memberof ZJHCanvas
     */
    @property([cc.Prefab])
    timeDown_prefab_list: cc.Prefab[] = [];
    /**
     * 动作脚本
     * @memberof ZJHCanvas
     */
    _dealScript: Game_DealScript = null;
    /**
     * 玩家脚本
     * @memberof ZJHCanvas
     */
    _playerScript: ZJH_Player = null;

    /**
     * 桌子脚本
     * @memberof ZJHCanvas
     */
    _tableScript: ZJH_Table = null;

    /**
     * 是否结算
     * @type {boolean}
     * @memberof ZJHCanvas
     */
    _isShowResult: boolean = false;
    /**
     * 是否发牌
     * @type {boolean}
     * @memberof ZJHCanvas
     */
    _isFPAction: boolean = false;

    /**
     * 游戏状态推送消息函数
     * 
     * @memberof MJCanvas
     */
    ZJH_GamePush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        dd.gm_manager.zjhGameData = data as GameData;
        this.showZJHInfo();
    };

    /**
     * 推送消息(座位数据变化) 回调函数
     * 
     * @memberof MJCanvas
     */
    ZJH_SeatPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        dd.gm_manager.zjhGameData = data as GameData;
        this.showZJHInfo();
    };
    /**
     * 推送消息(下注数据变化) 回调函数
     * 
     * @memberof MJCanvas
     */
    ZJH_BetPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        cc.log('下注push');
        cc.log(data);
        dd.gm_manager.zjhGameData = data.tableVo as GameData;
        this.showZJHInfo();

        let seatInfo = ZJH_Help.getSeatBySeatId(data.btIndex);
        if (seatInfo && seatInfo.btState !== ZJH_Help.ZJH_BT_State.ACT_STATE_WAIT && seatInfo.bGamed === 1) {
            let index = ZJH_Help.getIndexBySeatId(seatInfo.seatIndex);
            switch (seatInfo.btVal) {
                case ZJH_Help.ZJH_Act_State.BT_VAL_DROP://弃牌
                    dd.mp_manager.playZJH('state_qp')
                    this.showQPAction(seatInfo.seatIndex);
                    break;
                case ZJH_Help.ZJH_Act_State.BT_VAL_LOOCK://看牌
                    dd.mp_manager.playZJH('state_kp');
                    break;
                case ZJH_Help.ZJH_Act_State.BT_VAL_COMPARAE://比牌

                    break;
                case ZJH_Help.ZJH_Act_State.BT_VAL_BETALL://全下
                    dd.mp_manager.playZJH('state_allin')
                    this._tableScript.playGoldAction(this._playerScript.getPosByIndex(index), data.betMoney);
                    break;
                case ZJH_Help.ZJH_Act_State.BT_VAL_BETSAME://跟注
                    dd.mp_manager.playZJH('state_gz')
                    this._tableScript.playGoldAction(this._playerScript.getPosByIndex(index), data.betMoney);
                    break;
                case ZJH_Help.ZJH_Act_State.BT_VAL_BETADD://加注
                    dd.mp_manager.playZJH('state_jz')
                    this._tableScript.playGoldAction(this._playerScript.getPosByIndex(index), data.betMoney);
                    break;
                default:
            }
        }
    };

    /**
     * 推送消息(玩家被踢出座位) 回调函数
     * 
     * @memberof MJCanvas
     */
    ZJH_KickPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        let str = '';
        if (data === 1) {
            str = '您的金币不足,被踢出房间！'
        } else if (data === 3) {//长时间为准备
            str = '您长时间未准备，已被踢出房间！'
        }
        if (dd.ui_manager.showLoading()) {
            dd.ud_manager.mineData.tableId = 0;
            cc.director.loadScene('HomeScene', () => {
                dd.ui_manager.showTip(str);
                dd.gm_manager.destroySelf();
            });
        }
    };

    /**
     * 推送消息(玩家看牌数据)
     * 
     * @memberof MJCanvas
     */
    ZJH_LookPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        cc.log('玩家看牌push');
        cc.log(data);
        let seatInfo = ZJH_Help.getSeatBySeatId(data.btIndex);
        dd.mp_manager.playZJH('state_kp');
        //如果是自己，显示翻牌
        if (seatInfo.accountId === dd.ud_manager.mineData.accountId) {
            this._playerScript.showFPActionBySeatId(data.btIndex);
        }
        dd.gm_manager.zjhGameData = data.tableVo as GameData;
        this.showZJHInfo();
    };
    /**
     * //推送消息(玩家房卡数据变化)
     * @memberof ZJHCanvas
     */
    WalletPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        let wallet = data as Wallet;
        let zjhGameData = dd.gm_manager.zjhGameData;
        if (zjhGameData) {
            for (var i = 0; i < zjhGameData.seats.length; i++) {
                if (dd.ud_manager.mineData.accountId === zjhGameData.seats[i].accountId) {
                    dd.gm_manager.zjhGameData.seats[i].money = wallet.roomCard;
                    break;
                }
            }
            this._playerScript.showPlayerInfo();
        }
    };
    /**
     * //推送消息(准备)
     * @memberof ZJHCanvas
     */
    readyPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        dd.gm_manager.zjhGameData = data as GameData;
        this.showZJHInfo();
    };
    /**
      * 绑定游戏push
      * 
      * @memberof MJCanvas
      */
    bindOnPush(): void {
        //推送消息(游戏状态变化)
        cc.systemEvent.on('GamePush', this.ZJH_GamePush);
        //推送消息(座位数据变化)
        cc.systemEvent.on('SeatPush', this.ZJH_SeatPush);
        //推送消息(下注数据变化)
        cc.systemEvent.on('BetPush', this.ZJH_BetPush);
        //推送消息(玩家被踢出座位)
        cc.systemEvent.on('KickPush', this.ZJH_KickPush);
        //推送消息(玩家看牌数据)
        cc.systemEvent.on('LookPush', this.ZJH_LookPush);
        //推送消息(玩家房卡数据变化)
        cc.systemEvent.on('WalletPush', this.WalletPush);
        //准备
        cc.systemEvent.on('Ready', this.readyPush);
    }

    /**
     * 解除绑定游戏push
     * 
     * @memberof MJCanvas
     */
    bindOffPush() {
        //推送消息(游戏状态变化)
        cc.systemEvent.off('GamePush', this.ZJH_GamePush);
        //推送消息(座位数据变化)
        cc.systemEvent.off('SeatPush', this.ZJH_SeatPush);
        //推送消息(下注数据变化)
        cc.systemEvent.off('BetPush', this.ZJH_BetPush);
        //推送消息(玩家被踢出座位)
        cc.systemEvent.off('KickPush', this.ZJH_KickPush);
        //推送消息(玩家看牌数据)
        cc.systemEvent.off('LookPush', this.ZJH_LookPush);
        //推送消息(玩家房卡数据变化)
        cc.systemEvent.off('WalletPush', this.WalletPush);
        //准备
        cc.systemEvent.off('Ready', this.readyPush);
    }

    onLoad() {

        this._dealScript = this.node_action.getComponent('Game_DealScript');
        this._playerScript = this.node_player.getComponent('ZJH_Player');
        this._tableScript = this.node_table.getComponent('ZJH_Table');
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this._tableScript.showAddReturnToState();
            event.stopPropagation();
        }, this);
        this.bindOnPush();
    }
    onDestroy() {
        this.bindOffPush();
    }
    start() {
        if (dd.ui_manager.showLoading('正在加载桌子信息')) {
            //获取房间信息
            let obj = { 'tableId': dd.gm_manager.zjhGameData.tableId, 'type': 0 };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_JION_TABLEID, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    dd.gm_manager.zjhGameData = content as GameData;
                    this.showZJHInfo();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示', {
                        lbl_name: '确定',
                        callback: () => {
                            if (dd.ui_manager.showLoading()) {
                                dd.ud_manager.mineData.tableId = 0;
                                cc.director.loadScene('HomeScene', () => {
                                    dd.gm_manager.destroySelf();
                                });
                            }
                        }
                    });

                }
                cc.log(content);
            });
        }
    }

    /**
     * 
     * 
     * @param {ZJH_Help.ZJH_Act_State} type 
     * @param {number} data  //看牌 = 0 比牌=别人的座位号 押注=金额
     * @memberof ZJHCanvas
     */
    sendBetInfo(type: ZJH_Help.ZJH_Act_State, data: number) {
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        if (mySeat) {
            let obj = {
                'tableId': dd.gm_manager.zjhGameData.tableId,
                'seatIndex': mySeat.seatIndex,
                'bt': type,
                'btVal': data,         //看牌 = 0 比牌=别人的座位号 押注=金额
            };
            //如果是下注，
            if (type === ZJH_Help.ZJH_Act_State.BT_VAL_BETSAME || type === ZJH_Help.ZJH_Act_State.BT_VAL_BETADD) {

                //如果下注的钱大于单注上限，
                if (obj.btVal > dd.gm_manager.zjhGameData.onceMax) {
                    obj.btVal = dd.gm_manager.zjhGameData.onceMax;
                }
                //如果下注的钱大于自己的钱，那么下注的钱就是自己身上所有的钱
                if (obj.btVal > mySeat.money) {
                    obj.btVal = mySeat.money;
                }
                //如果下注的钱 大于等于 自己身上所有的钱，就是全押
                if (mySeat.money <= obj.btVal) {
                    obj.bt = ZJH_Help.ZJH_Act_State.BT_VAL_BETALL;
                }
                obj.btVal = obj.btVal + mySeat.betMoney;
            }

            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_BET, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log('下注成功');
                } else if (flag === -1) {//超时
                    cc.log(content);
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }

    /**
    * 发送准备
    * 
    * @memberof MJCanvas
    */
    sendReadyGame() {
        // if (dd.ui_manager.showLoading()) {
        let obj = {
            'tableId': dd.gm_manager.zjhGameData.tableId,
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_NN_GAME_READY, msg, (flag: number, content?: any) => {
            dd.ui_manager.hideLoading();
            if (flag === 0) {//成功
            } else if (flag === -1) {//超时
                dd.ui_manager.showTip('准备消息发送超时,请重试!');
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
            }
        });
        // }
    }
    /**
     * 退出桌子
     * 
     * @memberof MJCanvas
     */
    sendOutGame() {
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'tableId': dd.gm_manager.zjhGameData.tableId,
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_LEAVE, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.sendGetRoomInfo();
                } else if (flag === -1) {//超时
                    cc.log(content);
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '错误提示', {
                        lbl_name: '确定',
                        callback: () => {
                            this.sendGetRoomInfo();
                        }
                    });
                }
            });
        }
    }
    /**
     * 获取房间列表
     * 
     * @memberof ZJHCanvas
     */
    sendGetRoomInfo() {
        if (dd.ui_manager.showLoading('正在获取房间列表,请稍后')) {
            let obj = { 'gameType': 2 };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_GET_ROOM_LIST, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    let items = content.items as RoomCfgItem[];
                    dd.ud_manager.mineData.tableId = 0;
                    cc.director.loadScene('ZJHRoomScene', () => {
                        dd.gm_manager.destroySelf();
                        dd.ui_manager.getCanvasNode().getComponent('ZJH_RoomCanvas').init(items);
                    });
                } else if (flag === -1) {//超时
                    if (dd.ui_manager.showLoading()) {
                        dd.ud_manager.mineData.tableId = 0;
                        cc.director.loadScene('HomeScene', () => {
                            dd.gm_manager.destroySelf();
                        });
                    }
                } else {//失败,content是一个字符串
                    if (dd.ui_manager.showLoading()) {
                        dd.ud_manager.mineData.tableId = 0;
                        cc.director.loadScene('HomeScene', () => {
                            dd.gm_manager.destroySelf();
                        });
                    }
                }
            });
        }
    }

    /**
     * 显示扎金花信息
     * 
     * @memberof ZJHCanvas
     */
    showZJHInfo() {
        cc.log('------游戏状态----' + dd.gm_manager.zjhGameData.gameState);
        cc.log(dd.gm_manager.zjhGameData);
        //座位排序
        dd.gm_manager.zjhGameData.seats = ZJH_Help.sortSeatList(dd.gm_manager.zjhGameData.seats);
        this._playerScript.showPlayerInfo();
        this._tableScript.showTableInfo();
        this.showMask();

        //动作
        let action = dd.gm_manager.zjhGameData.gameState;
        if (action !== ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_OVER) {
            this._isShowResult = false;
        }
        if (action !== ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_FAPAI) {
            this._isFPAction = false;
        }
        switch (action) {
            case ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_READY: {//准备
                break;
            }
            case ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_BASESCORE: {//下底注
                //开始游戏的音效
                dd.mp_manager.playStart(() => {
                    //开始游戏，每个在玩玩家下底注
                    let obj = this._playerScript.getFPPosList();
                    this._dealScript.showDeal(obj.posList, (index: number) => {
                        this._tableScript.playGoldAction(this._playerScript.getPosByIndex(index), dd.gm_manager.zjhGameData.baseScore);
                    });
                });
                break;
            }
            case ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_FAPAI: {//发牌
                let dTime = (Number(dd.gm_manager.zjhGameData.actTime) - Number(dd.gm_manager.zjhGameData.svrTime)) / 1000;
                //剩余时间大于0
                if (dTime > 3) {
                    dd.mp_manager.playZJH('fapai', () => {
                        this.showFaPai();
                    });
                }
                break;
            }
            case ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_BET: {//表态
                break;
            }
            case ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_COMPARE: {//两个人比牌
                let dTime = (Number(dd.gm_manager.zjhGameData.actTime) - Number(dd.gm_manager.zjhGameData.svrTime)) / 1000;
                if (dTime > 3) {
                    dd.mp_manager.playZJH('state_bp');
                    this.showBPAction();
                }
                break;
            }
            case ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_OVER: {
                if (!this._isShowResult) {
                    this._isShowResult = true;
                    this._playerScript.showAllFPAction();
                    //显示结算动画
                    this.showWinAction();
                }
                break;
            }
            default:
                break;
        }
    }
    /**
     * 显示发牌动画
     * 
     * @memberof ZJHCanvas
     */
    showFaPai() {
        if (this._isFPAction) return;
        this._isFPAction = true;
        let obj = this._playerScript.getFPPosList();
        this._dealScript.showDealFP(cc.v2(0, 240), obj.posList, (index: number, roll: number) => {
            this._playerScript.showPokerByIndex(obj.playerIndexList[index], roll, 0, false);
        }, null, 3);
    }

    /**
     * 显示翻牌牌动画
     * @param {number} cardId 牌数据
     * @param {cc.Node} parentNode 牌父节点
     * @param {*} [cb] 回调函数
     * @memberof ZJHCanvas
     */
    showFanPai(cardId: number, parentNode: cc.Node, cb?: any) {
        let fanpai = cc.instantiate(this.fanpai_prefab);
        let fpScript = fanpai.getComponent('Game_ActionFP');
        fpScript.initData(cardId, dd.img_manager.getCardSpriteFrameById(cardId), cb);
        fanpai.parent = parentNode;
    }
    /**
     *显示牌信息 
     * @param {number} cardId 牌数据
     * @param {cc.Node} parentNode 牌父节点
     * @param {boolean} [isShow=true]  是否显示
     * @memberof ZJHCanvas
     */
    showCard(cardId: number, parentNode: cc.Node, isShow: boolean = true) {
        let node_card = cc.instantiate(this.card_prefab);
        let card_script = node_card.getComponent('Game_Card');
        if (cardId > -1) {
            let pokerImg = dd.img_manager.getCardSpriteFrameById(cardId);
            card_script.initData(cardId, pokerImg, isShow);
        }
        node_card.parent = parentNode;
    }

    /**
     * 显示倒计时
     * @param {number} type 
     * @param {number} sTime 
     * @param {number} cTime 
     * @param {boolean} isPlayEfc 
     * @param {cc.Node} parentNode 
     * @memberof ZJHCanvas
     */
    showSJDAction(type: number, sTime: number, cTime: number, isPlayEfc: boolean, parentNode: cc.Node) {
        let tdNode = null;
        if (type === 0) {
            tdNode = cc.instantiate(this.timeDown_prefab_list[0]);
        } else {
            tdNode = cc.instantiate(this.timeDown_prefab_list[1]);
        }
        let tdScript = tdNode.getComponent('Game_TimeDown');
        tdScript.initData(sTime, cTime, isPlayEfc, parentNode);
    }

    /**
     * 显示比牌动作
     * @memberof ZJHCanvas
     */
    showBPAction() {
        let bpAction = cc.instantiate(this.bpPrefab);
        bpAction.parent = dd.ui_manager.getRootNode();
        dd.mp_manager.playZJH('bp');
    }

    /**
     * 显示比牌失败动作
     * @param {any} pos 
     * @param {any} parentNode 
     * @memberof ZJHCanvas
     */
    showBPSBAction(pos, parentNode) {
        dd.mp_manager.playZJH('kd');
        let bpsbNode = cc.instantiate(this.bpsbPrefab);
        bpsbNode.setPosition(pos);
        bpsbNode.parent = parentNode;
    }

    /**
     *显示弃牌动作
     * @param {Number} seatId 
     * @returns 
     * @memberof ZJHCanvas
     */
    showQPAction(seatId: number, cb: any = null) {
        if (seatId < 0 || seatId > 5) {
            cc.log('弃牌索引错误');
            return;
        }
        let index = ZJH_Help.getIndexBySeatId(seatId);
        let pos = this._playerScript.getPosByIndex(index);
        this._dealScript.showDealDiscard(pos, cb);

        //获取自己的座位信息
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        //如果是自己
        if (mySeat && mySeat.seatIndex === seatId) {
            this._playerScript.showFPActionBySeatId(seatId);
        }
    }

    /**
     * 创建一个抖动的动作
     * @param {number} seatId 
     * @param {cc.Vec2} pos 
     * @memberof ZJH_Table
     */
    showShakeAction(seatId: number, pos: cc.Vec2) {
        let pokerShake = cc.instantiate(this.spPrefab);
        pokerShake.setPosition(pos);
        let shakeScript = pokerShake.getComponent("ZJH_PokerShake");
        shakeScript.initData(seatId, this);
        pokerShake.parent = this.node_mask;
        let anim = pokerShake.getComponent(cc.Animation);
        anim.play();
    }

    onTouchMaskUp = (event: cc.Event.EventTouch) => {
        this.showMask(false);
        event.stopPropagation();
    }
    /**
     * 显示游戏场景的遮罩
     * @param {boolean} isShow  是否显示
     * @memberof ZJHCanvas
     */
    showMask(isShow: boolean = false) {
        this.node_mask.active = isShow;

        if (isShow) {
            this.node.on("touchend", this.onTouchMaskUp, this);
        } else {
            this.node_mask.removeAllChildren(true);
            this.node_mask.off('touchend', this.onTouchMaskUp, this);
        }
    }
    /**
     * 显示选择比牌的界面
     * @memberof ZJHCanvas
     */
    showChooseBPAction() {
        this.showMask(true);
        this._playerScript.showPokerShakeAction();
    }
    /**
     * 显示赢家动画
     * @memberof ZJHCanvas
     */
    showWinAction() {
        if (dd.gm_manager.zjhGameData.ZJHOverItems) {
            let winlist = dd.gm_manager.zjhGameData.ZJHOverItems;
            let winIndex = 0;
            let winGold = 0;
            for (var i = 0; i < winlist.length; i++) {
                let gold = winlist[i].score;
                if (gold > winGold) {
                    winGold = gold;
                    winIndex = winlist[i].seatIndex;
                }
            }
            let index = ZJH_Help.getIndexBySeatId(winIndex);
            let pos = this._playerScript.getPosByIndex(index);
            this._tableScript.playWinGoldAction(pos, () => {
                for (var i = 0; i < winlist.length; i++) {
                    let nGold = winlist[i].score;
                    let nIndex = ZJH_Help.getIndexBySeatId(winlist[i].seatIndex);
                    let ePos = this._playerScript.getPosByIndex(nIndex);
                    let color = nGold > 0 ? cc.Color.RED : cc.Color.GREEN;
                    let str = dd.utils.getShowNumberString(nGold);
                    if (nGold > 0) {
                        str = '+' + str;
                    }
                    this.craeteLalelAction(str, ePos, color);
                }
            });
        }
    }

    /**
     * 创建文本动作
     * @param {string} str 文本
     * @param {cc.Vec2} pos 坐标点
     * @param {cc.Color} color 颜色
     * @param {cc.Node} parentNode  父节点
     * @memberof ZJHCanvas
     */
    craeteLalelAction(str: string, pos: cc.Vec2, color: cc.Color) {
        let lblNode = new cc.Node();
        lblNode.color = color;
        lblNode.setPosition(pos);
        let lbl = lblNode.addComponent(cc.Label);
        lbl.fontSize = 50;
        lbl.lineHeight = 50;
        lbl.string = str;
        lblNode.parent = dd.ui_manager.getRootNode();
        let seq = cc.sequence(cc.moveTo(2, cc.v2(pos.x, pos.y + 80)), cc.callFunc(() => {
            lblNode.removeFromParent(true);
            lblNode.destroy();
        }));
        lblNode.runAction(seq);
    }
}
