const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class NNBattleCanvas extends cc.Component {
    /**
     * 桌子ID
     * 
     * @type {cc.Label}
     * @memberof NNBattleCanvas
     */
    @property(cc.Label)
    lbl_tableId: cc.Label = null;
    /**
     * 桌子中间显示的(下注额度显示)
     * 
     * @type {cc.Label}
     * @memberof NNBattleCanvas
     */
    @property(cc.Label)
    lbl_mid: cc.Label = null;
    /**
     * 上方玩家节点
     * 
     * @type {cc.Node}
     * @memberof NNBattleCanvas
     */
    @property(cc.Node)
    player_up: cc.Node = null;
    /**
     * 下方自己的节点
     * 
     * @type {cc.Node}
     * @memberof NNBattleCanvas
     */
    @property(cc.Node)
    player_down: cc.Node = null;
    /**
     * 结算面板
     * 
     * @type {cc.Node}
     * @memberof NNBattleCanvas
     */
    @property(cc.Node)
    result: cc.Node = null;
    /**
     * 胜利节点
     * 
     * @type {cc.Node}
     * @memberof NNBattleCanvas
     */
    @property(cc.Node)
    winNode: cc.Node = null;
    /**
     * 失败节点
     * 
     * @type {cc.Node}
     * @memberof NNBattleCanvas
     */
    @property(cc.Node)
    loseNode: cc.Node = null;

    @property(cc.Node)
    fpNode: cc.Node = null;

    @property(cc.Prefab)
    fpPrefab: cc.Prefab = null;

    @property([cc.SpriteFrame])
    niuSFs: cc.SpriteFrame[] = [];

    isRunAction: boolean = false;

    isKick: boolean = false;

    /**
     * 倒计时,不为0就做倒计时
     * 
     * @type {number}
     * @memberof NNBattleCanvas
     */
    diffTime: number = 0;

    needWait: boolean = false;

    updateUp(needUpdate: boolean = false) {
        if (dd.gm_manager.nnGameData.seats[0].accountId !== '0' && dd.gm_manager.nnGameData.seats[1].accountId !== '0') {
            let playerData: SeatVo = this.getSeatData(false);
            if (!this.player_up.active || needUpdate) {
                this.changePlayerInfo(playerData, this.player_up);
            }
            this.updatePlayer(playerData, this.player_up);
            this.player_up.active = true;
        } else {
            this.player_up.active = false;
        }
    }

    updateMid() {
        if (dd.gm_manager.nnGameData.totalBetMoney > 0 && dd.gm_manager.nnGameData.gameState > 1) {
            this.lbl_mid.string = '已下注: ' + dd.gm_manager.nnGameData.totalBetMoney;
        } else {
            this.lbl_mid.string = '';
        }
    }

    updateDown() {
        let playerData: SeatVo = this.getSeatData(true);
        this.updatePlayer(playerData, this.player_down);
    }

    changePlayerInfo(playerData: SeatVo, seatNode: cc.Node) {
        let playerNode: cc.Node = seatNode.getChildByName('player_board');
        playerNode.getChildByName('name').getComponent(cc.Label).string = playerData.nick;
        cc.find('layout/money', playerNode).getComponent(cc.Label).string = dd.utils.getShowNumberString(playerData.money);
        playerNode.getChildByName('head').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getHeadById(Number(playerData.headImg));
    }

    updatePlayer(playerData: SeatVo, seatNode: cc.Node) {
        let playerNode: cc.Node = seatNode.getChildByName('player_board');
        if (dd.gm_manager.nnGameData.bankerId === playerData.accountId) {
            if (dd.gm_manager.nnGameData.gameState > 2) {
                playerNode.getChildByName('zhuang').active = true;
            } else {
                playerNode.getChildByName('zhuang').active = false;
            }
        } else {
            playerNode.getChildByName('zhuang').active = false;
        }

        let timeNode = cc.find('player_board/time', seatNode);
        let niuNode = seatNode.getChildByName('niu');
        let infoNode = seatNode.getChildByName('lbl_info');
        let cardLayout = seatNode.getChildByName('cardLayout');
        //down才有的节点
        let btnLayout = seatNode.getChildByName('btnLayout');
        let jzLayout = seatNode.getChildByName('jzLayout');
        let btnReady = seatNode.getChildByName('btn_ready');

        switch (dd.gm_manager.nnGameData.gameState) {
            case 0: //空闲
                if (this.isRunAction) return;
                timeNode.active = false;
                niuNode.active = false;
                cardLayout.active = false;
                this.cleanCardLayout(cardLayout);
                if (playerData.accountId === dd.ud_manager.mineData.accountId) {//down
                    btnLayout.active = false;
                    jzLayout.active = false;
                    if (playerData.btState === 0) {
                        btnReady.active = true;
                        infoNode.active = false;
                    } else {
                        btnReady.active = false;
                        infoNode.active = true;
                        infoNode.getComponent(cc.Label).string = '已准备';
                    }
                } else {//up
                    infoNode.active = true;
                    if (playerData.btState === 0) {
                        infoNode.getComponent(cc.Label).string = '未准备';
                    } else {
                        infoNode.getComponent(cc.Label).string = '已准备';
                    }
                }
                break;
            case 1://游戏开始
                break;
            case 2: //叫庄
                niuNode.active = false;
                cardLayout.active = false;
                this.cleanCardLayout(cardLayout);
                if (playerData.seatIndex === dd.gm_manager.nnGameData.btIndex) {//当前坐位表态
                    timeNode.active = true;
                    timeNode.getChildByName('lbl_time').getComponent(cc.Label).string = Math.floor(this.diffTime / 1000).toString();
                    if (playerData.accountId === dd.ud_manager.mineData.accountId) {//down
                        btnReady.active = false;
                        btnLayout.active = false;
                        if (playerData.btState === 0) {
                            jzLayout.active = true;
                            infoNode.active = false;
                        } else {
                            jzLayout.active = false;
                            infoNode.active = true;
                            infoNode.getComponent(cc.Label).string = playerData.btState === 1 ? '叫庄' : '不叫';
                        }
                    } else {//up
                        infoNode.active = true;
                        if (playerData.btState === 0) {
                            infoNode.getComponent(cc.Label).string = '叫庄思考中...';
                        } else {
                            infoNode.getComponent(cc.Label).string = playerData.btState === 1 ? '叫庄' : '不叫';
                        }
                    }
                } else {
                    timeNode.active = false;
                    if (playerData.btState === 0) {
                        infoNode.active = false;
                    } else {
                        infoNode.getComponent(cc.Label).string = playerData.btState === 1 ? '叫庄' : '不叫';
                    }
                    if (playerData.accountId === dd.ud_manager.mineData.accountId) {//down
                        btnReady.active = false;
                        btnLayout.active = false;
                        jzLayout.active = false;
                    }
                }
                break;
            case 3: //押注
                niuNode.active = false;
                cardLayout.active = false;
                this.cleanCardLayout(cardLayout);
                if (playerData.seatIndex === dd.gm_manager.nnGameData.btIndex) {//当前坐位表态
                    timeNode.active = true;
                    timeNode.getChildByName('lbl_time').getComponent(cc.Label).string = Math.floor(this.diffTime / 1000).toString();
                    if (playerData.accountId === dd.ud_manager.mineData.accountId) {//down
                        btnReady.active = false;
                        jzLayout.active = false;
                        infoNode.active = false;
                        if (playerData.btState === 0) {
                            btnLayout.active = true;
                            let minMoney = dd.gm_manager.nnGameData.seats[0].money < dd.gm_manager.nnGameData.seats[1].money ? dd.gm_manager.nnGameData.seats[0].money : dd.gm_manager.nnGameData.seats[1].money;
                            let btn1 = btnLayout.getChildByName('btn1');
                            btn1.getChildByName('money').getComponent(cc.Label).string = Math.floor(minMoney / 36 * 1).toString();
                            let btn2 = btnLayout.getChildByName('btn2');
                            btn2.getChildByName('money').getComponent(cc.Label).string = Math.floor(minMoney / 36 * 2).toString();
                            let btn3 = btnLayout.getChildByName('btn3');
                            btn3.getChildByName('money').getComponent(cc.Label).string = Math.floor(minMoney / 36 * 3).toString();
                            let btn4 = btnLayout.getChildByName('btn4');
                            btn4.getChildByName('money').getComponent(cc.Label).string = Math.floor(minMoney / 36 * 4).toString();
                            let btn5 = btnLayout.getChildByName('btn5');
                            btn5.getChildByName('money').getComponent(cc.Label).string = Math.floor(minMoney / 36 * 5).toString();
                            let btn6 = btnLayout.getChildByName('btn6');
                            btn6.getChildByName('money').getComponent(cc.Label).string = Math.floor(minMoney / 36 * 6).toString();
                        } else {
                            btnLayout.active = false;
                        }
                    } else {//up
                        if (playerData.btState === 0) {
                            infoNode.active = true;
                            infoNode.getComponent(cc.Label).string = '押注思考中...';
                        } else {
                            infoNode.active = false;
                        }
                    }
                } else {
                    timeNode.active = false;
                    infoNode.active = false;
                    if (playerData.accountId === dd.ud_manager.mineData.accountId) {//down
                        btnReady.active = false;
                        btnLayout.active = false;
                        jzLayout.active = false;
                    }
                }
                break;
            case 4: //发牌
                // niuNode.active = true;
                cardLayout.active = true;
                timeNode.active = false;
                infoNode.active = false;
                if (playerData.accountId === dd.ud_manager.mineData.accountId) {//down
                    btnReady.active = false;
                    btnLayout.active = false;
                    jzLayout.active = false;
                }
                break;
            default: break;
        }
    }

    cleanCardLayout(layout: cc.Node) {
        for (let j = 1; j < 6; j++) {
            let cardNode = layout.getChildByName('card' + j);
            cardNode.removeAllChildren();
        }
    }

    clickBack() {
        if (this.needWait || this.isRunAction) return;
        dd.mp_manager.playButton();
        if (dd.gm_manager.nnGameData.gameState > 0 && dd.gm_manager.nnGameData.gameState < 4) {
            dd.ui_manager.showAlert('这把游戏还没结算不能逃跑哦!!!', '温馨提示');
        } else {
            if (dd.ui_manager.showLoading('正在加载,请稍后')) {
                this.needWait = true;
                if (this.isKick) {
                    this.getRoomInfo();
                } else {
                    let obj = { 'tableId': dd.gm_manager.nnGameData.tableId };
                    let msg = JSON.stringify(obj);
                    dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_LEAVE, msg, (flag: number, content?: any) => {
                        if (flag === 0) {//成功
                        } else if (flag === -1) {//超时
                            dd.ui_manager.showTip('退出消息发送超时,请重试!');
                            this.needWait = false;
                        } else {//失败,content是一个字符串
                            dd.ui_manager.showTip(content);
                            this.needWait = false;
                        }
                    });
                }
            }
        }
    }

    clickJZ(event: cc.Event.EventTouch, type: string) {
        if (this.needWait) return;
        dd.mp_manager.playButton();
        this.needWait = true;
        let jzLayout = this.player_down.getChildByName('jzLayout');
        let timeNode = cc.find('player_board/time', this.player_down);
        jzLayout.active = false;
        timeNode.active = false;
        let num = Number(type);
        let obj = {
            'tableId': dd.gm_manager.nnGameData.tableId,
            'bt': num
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_CALL_BANKER, msg, (flag: number, content?: any) => {
            this.needWait = false;
            if (flag === 0) {//成功

            } else if (flag === -1) {//超时
                dd.ui_manager.showTip('叫庄消息发送超时,请重试!');
                jzLayout.active = true;
                timeNode.active = true;
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
                jzLayout.active = true;
                timeNode.active = true;
            }
        });
    }

    clickReady() {
        if (this.needWait) return;
        dd.mp_manager.playButton();
        this.needWait = true;
        this.result.active = false;
        let btnReady = this.player_down.getChildByName('btn_ready');
        btnReady.active = false;
        let obj = { 'tableId': dd.gm_manager.nnGameData.tableId };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_NN_GAME_READY, msg, (flag: number, content?: any) => {
            this.needWait = false;
            if (flag === 0) {//成功

            } else if (flag === -1) {//超时
                dd.ui_manager.showTip('准备消息发送超时,请重试!');
                btnReady.active = true;
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
                btnReady.active = true;
            }
        });
    }

    clickBet(event: cc.Event.EventTouch, index: string) {
        if (this.needWait) return;
        dd.mp_manager.playButton();
        this.needWait = true;
        let btnLayout = this.player_down.getChildByName('btnLayout');
        let timeNode = cc.find('player_board/time', this.player_down);
        btnLayout.active = false;
        timeNode.active = false;
        let num = Number(index);
        let mineSeat = dd.gm_manager.nnGameData.seats[0].accountId === dd.ud_manager.mineData.accountId ? dd.gm_manager.nnGameData.seats[0] : dd.gm_manager.nnGameData.seats[1];
        let minMoney = dd.gm_manager.nnGameData.seats[0].money < dd.gm_manager.nnGameData.seats[1].money ? dd.gm_manager.nnGameData.seats[0].money : dd.gm_manager.nnGameData.seats[1].money;
        let obj = {
            'tableId': dd.gm_manager.nnGameData.tableId,
            'seatIndex': mineSeat.seatIndex,
            'bt': 1,
            'btVal': Math.floor(minMoney / 36 * num)
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_BET, msg, (flag: number, content?: any) => {
            this.needWait = false;
            if (flag === 0) {//成功

            } else if (flag === -1) {//超时
                dd.ui_manager.showTip('押注消息发送超时,请重试!');
                btnLayout.active = true;
                timeNode.active = true;
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
                btnLayout.active = true;
                timeNode.active = true;
            }
        });
    }

    changeDiffTime() {
        this.diffTime = Number(dd.gm_manager.nnGameData.actTime) - Number(dd.gm_manager.nnGameData.svrTime);
        if (this.diffTime < 0) {
            this.diffTime = 0;
        }
    }

    update(dt: number) {
        if (dd.gm_manager && dd.gm_manager.nnGameData) {
            if (this.diffTime > 0) {
                this.diffTime -= dt * 1000;
                if (this.diffTime < 0) {
                    this.diffTime = 0;
                }
            }
            this.updateUp();
            this.updateMid();
            this.updateDown();
        }
    }

    getSeatData(isMine: boolean) {
        if (isMine) {
            return dd.gm_manager.nnGameData.seats[0].accountId === dd.ud_manager.mineData.accountId ? dd.gm_manager.nnGameData.seats[0] : dd.gm_manager.nnGameData.seats[1];
        } else {
            return dd.gm_manager.nnGameData.seats[0].accountId === dd.ud_manager.mineData.accountId ? dd.gm_manager.nnGameData.seats[1] : dd.gm_manager.nnGameData.seats[0];
        }
    }

    fp(player1: cc.Node, player2: cc.Node) {
        let isMine: boolean = (player1 === this.player_down);
        let niu: cc.Node = null;
        let index: number = 0;
        this.playFP(player1, () => {
            this.playFP(player2, () => {
                this.runFP(player1, this.getSeatData(isMine).handCards, () => {
                    niu = player1.getChildByName('niu');
                    index = dd.card_manager.getNiuTypeByIds(this.getSeatData(isMine).handCards);
                    niu.getComponent(cc.Sprite).spriteFrame = this.niuSFs[index];
                    niu.active = true;
                    dd.mp_manager.playNN('bull' + index);
                    niu.runAction(cc.sequence(cc.scaleTo(0.5, 1.2), cc.scaleTo(0.5, 1), cc.delayTime(1), cc.callFunc(() => {
                        this.runFP(player2, this.getSeatData(!isMine).handCards, () => {
                            niu = player2.getChildByName('niu');
                            index = dd.card_manager.getNiuTypeByIds(this.getSeatData(!isMine).handCards);
                            niu.getComponent(cc.Sprite).spriteFrame = this.niuSFs[index];
                            niu.active = true;
                            dd.mp_manager.playNN('bull' + index);
                            niu.runAction(cc.sequence(cc.scaleTo(0.5, 1.2), cc.scaleTo(0.5, 1), cc.delayTime(1), cc.callFunc(() => {
                                if (dd.gm_manager.nnGameData.NNOverItems[0].score > 0) {
                                    dd.mp_manager.playNN('nn_zwin');
                                } else {
                                    dd.mp_manager.playNN('nn_xwin');
                                }
                                setTimeout(() => {
                                    this.showResult();
                                }, 1000);
                            }, this)));
                        });
                    }, this)));
                });
            });
        });
    }


    gamePush = (event: cc.Event.EventCustom) => {
        dd.gm_manager.nnGameData = event.detail as GameData;
        this.changeDiffTime();
        if (dd.gm_manager.nnGameData.gameState === 1) {//游戏开始
            dd.mp_manager.playNN('go');
        }
        if (dd.gm_manager.nnGameData.gameState === 2) {//开始叫庄
            dd.mp_manager.playNN('ding');
        }
        if (dd.gm_manager.nnGameData.gameState === 3) {//开始下注
            dd.mp_manager.playNN('startBet');
        }
        if (dd.gm_manager.nnGameData.gameState === 4) {//开始发牌
            this.isRunAction = true;
            if (dd.gm_manager.nnGameData.bankerId === dd.ud_manager.mineData.accountId) {
                this.fp(this.player_up, this.player_down);
            } else {
                this.fp(this.player_down, this.player_up);
            }
        }
    };

    showResult() {
        let datas = dd.gm_manager.nnGameData.NNOverItems as SettlementItemNN[];
        for (let i = 0; i < datas.length; i++) {
            let data = datas[i];
            let line = cc.find('board/table/line' + (i + 1), this.result);
            if (data.accountId === dd.ud_manager.mineData.accountId) {
                line.getChildByName('line_bg').active = true;
                let win = cc.find('board/win', this.result);
                let lose = cc.find('board/lose', this.result);
                if (data.score > 0) {
                    win.active = true;
                    lose.active = false;
                    dd.mp_manager.playWin();
                } else {
                    win.active = false;
                    lose.active = true;
                    dd.mp_manager.playLose();
                }
            } else {
                line.getChildByName('line_bg').active = false;
            }
            if (data.banker === 1) {
                line.getChildByName('zhuang').active = true;
            } else {
                line.getChildByName('zhuang').active = false;
            }
            line.getChildByName('lbl_name').getComponent(cc.Label).string = data.nick;
            line.getChildByName('lbl_point').getComponent(cc.Label).string = data.nnDesc;
            line.getChildByName('lbl_gold').getComponent(cc.Label).string = data.score.toString();
        }
        if (this.isKick) {
            cc.find('board/btnLayout/goon', this.result).active = false;
        }
        this.result.active = true;
        this.isRunAction = false;
        this.changePlayerInfo(this.getSeatData(true), this.player_down);
        this.changePlayerInfo(this.getSeatData(false), this.player_up);
    }

    seatPush = (event: cc.Event.EventCustom) => {
        if (this.isRunAction) {
            let data = event.detail;
            let id = setInterval(() => {
                if (!this.isRunAction) {
                    clearInterval(id);
                    dd.gm_manager.nnGameData = data as GameData;
                    this.updateUp(true);
                }
            }, 100);
        } else {
            dd.gm_manager.nnGameData = event.detail as GameData;
            this.updateUp(true);
        }
    };

    betPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        dd.gm_manager.nnGameData = data.tableVo as GameData;
        this.changeDiffTime();
        dd.mp_manager.playCoinMove();
    };

    kickPush = (event: cc.Event.EventCustom) => {
        dd.ud_manager.mineData.tableId = 0
        if (event.detail === 1) {
            cc.log('钱不够了!');
            this.isKick = true;
        } else {
            this.getRoomInfo();
        }
    };

    getRoomInfo() {
        let obj = { 'gameType': 1 };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_GET_ROOM_LIST, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
                let items = content.items as RoomCfgItem[];
                cc.director.loadScene('NNRoomScene', () => {
                    dd.gm_manager.nnGameData = null;
                    dd.ui_manager.getCanvasNode().getComponent('NNRoomCanvas').init(items);
                });
            } else if (flag === -1) {//超时
                dd.ui_manager.showTip('获取房间列表失败,请重试!');
                this.needWait = false;
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
                this.needWait = false;
            }
        });
    }

    callBanker = (event: cc.Event.EventCustom) => {
        dd.gm_manager.nnGameData = event.detail as GameData;
        this.changeDiffTime();
    };

    walletPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        let wallet = data as Wallet;
        let nnGameData = dd.gm_manager.nnGameData;
        if (nnGameData) {
            for (var i = 0; i < nnGameData.seats.length; i++) {
                if (dd.ud_manager.mineData.accountId === nnGameData.seats[i].accountId) {
                    dd.gm_manager.nnGameData.seats[i].money = wallet.roomCard;
                    break;
                }
            }
        }
    };

    readyPush = (event: cc.Event.EventCustom) => {
        if (this.isRunAction) {
            let data = event.detail;
            let id = setInterval(() => {
                if (!this.isRunAction) {
                    clearInterval(id);
                    dd.gm_manager.nnGameData = data as GameData;
                    this.changeDiffTime();
                    dd.mp_manager.playNN('ding');
                }
            }, 100);
        } else {
            dd.gm_manager.nnGameData = event.detail as GameData;
            this.changeDiffTime();
            dd.mp_manager.playNN('ding');
        }
    };

    playFP(seatNode: cc.Node, callback: Function) {
        let points = [];
        let cardList = [];
        let layout = seatNode.getChildByName('cardLayout');
        for (let j = 1; j < 6; j++) {
            let cardNode = layout.getChildByName('card' + j);
            cardList.push(cardNode);
            let worldPoint = cardNode.parent.convertToWorldSpaceAR(cardNode.getPosition());
            points.push(this.fpNode.convertToNodeSpaceAR(worldPoint));
        }
        let fp = this.fpNode.getComponent('Game_DealScript');
        fp.showDealFP(cc.v2(0, 0), points, (index: number, round: number) => {
            let parent = cardList[index];
            let node = new cc.Node();
            node.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getCardSpriteFrameById(1);
            node.width = parent.width;
            node.height = parent.height;
            node.setPosition(0, 0);
            node.parent = parent;
        }, () => {//发牌结束回调
            callback();
        });
    }

    runFP(seatNode: cc.Node, cards: number[], callback: Function) {
        let layout = seatNode.getChildByName('cardLayout');
        let objs = [];
        for (let j = 1; j < 6; j++) {
            let cardNode = layout.getChildByName('card' + j);
            let obj = { 'node': cardNode, 'cardId': cards[j - 1] };
            objs.push(obj);
        }
        this.runAction(objs, callback);
    }

    runAction(objs: { node: cc.Node, cardId: number }[], callback: Function) {
        dd.mp_manager.playFaPai();
        let obj = objs.shift();
        obj.node.removeAllChildren();
        let actionNode = cc.instantiate(this.fpPrefab);
        actionNode.parent = obj.node;
        let sf = dd.img_manager.getCardSpriteFrameById(obj.cardId);
        actionNode.getComponent('Game_ActionFP').initData(obj.cardId, sf, () => {
            if (objs.length > 0) {
                this.runAction(objs, callback);
            } else {
                callback();
            }
        });
    }

    async onLoad() {
        let index = dd.gm_manager.nnGameData.tableId % 50;
        this.lbl_tableId.string = '桌号: ' + (index === 0 ? 50 : index);
        //推送消息(游戏状态变化)
        cc.systemEvent.on('GamePush', this.gamePush);
        //推送消息(座位数据变化)
        cc.systemEvent.on('SeatPush', this.seatPush);
        //推送消息(下注数据变化)
        cc.systemEvent.on('BetPush', this.betPush);
        //推送消息(玩家被踢出座位)
        cc.systemEvent.on('KickPush', this.kickPush);
        //推送消息(叫庄)
        cc.systemEvent.on('CallBanker', this.callBanker);
        //推送消息(玩家房卡数据变化)
        cc.systemEvent.on('WalletPush', this.walletPush);
        //准备
        cc.systemEvent.on('Ready', this.readyPush);

        this.player_up.active = false;
        this.isRunAction = false;
        this.changePlayerInfo(this.getSeatData(true), this.player_down);
    }

    onDestroy() {
        //推送消息(游戏状态变化)
        cc.systemEvent.off('GamePush', this.gamePush);
        //推送消息(座位数据变化)
        cc.systemEvent.off('SeatPush', this.seatPush);
        //推送消息(下注数据变化)
        cc.systemEvent.off('BetPush', this.betPush);
        //推送消息(玩家被踢出座位)
        cc.systemEvent.off('KickPush', this.kickPush);
        //推送消息(玩家看牌数据)
        cc.systemEvent.off('CallBanker', this.callBanker);
        //推送消息(玩家房卡数据变化)
        cc.systemEvent.off('WalletPush', this.walletPush);
        //准备
        cc.systemEvent.off('Ready', this.readyPush);
    }
}