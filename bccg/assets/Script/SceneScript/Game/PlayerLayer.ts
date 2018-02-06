const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class PlayerLayer extends cc.Component {
    /**
     * 玩家预制
     * 
     * @type {cc.Prefab}
     * @memberof PlayerLayer
     */
    @property(cc.Prefab)
    pre_player: cc.Prefab = null;
    /**
     * 坐位节点数组
     * 
     * @type {cc.Node[]}
     * @memberof PlayerLayer
     */
    @property([cc.Node])
    seatNodes: cc.Node[] = [];
    /**
     * 公共牌列表
     * @type {cc.Node[]}
     * @memberof PlayerLayer
     */
    @property([cc.Node])
    pCardNodes: cc.Node[] = [];
    /**
     * 扑克牌预制
     * 
     * @type {cc.Prefab}
     * @memberof PlayerLayer
     */
    @property(cc.Prefab)
    pre_card: cc.Prefab = null;
    /**
     * 积分
     * @type {cc.Prefab}
     * @memberof PlayerLayer
     */
    @property(cc.Prefab)
    chip_prefab: cc.Prefab = null;
    onLoad() {
        cc.systemEvent.on('updatePlayer', this.updatePlayer, this);
        cc.systemEvent.on('doFPAction', this.doFPAction, this);
        cc.systemEvent.on('btResult', this.showBTResult, this);
        cc.systemEvent.on('lookCard', this.showLookCard, this);
        cc.systemEvent.on('chatPush', this.showChat, this);

        this.seatNodes.forEach((seatNode: cc.Node) => {
            seatNode.getChildByName('empty').on(cc.Node.EventType.TOUCH_END, this.click_empty, this);
        }, this);

        this.updatePlayer();
        dd.gm_manager.playerScript = this;
    }

    onDestroy() {
        cc.systemEvent.off('updatePlayer', this.updatePlayer, this);
        cc.systemEvent.off('doFPAction', this.doFPAction, this);
        cc.systemEvent.off('btResult', this.showBTResult, this);
        cc.systemEvent.off('lookCard', this.showLookCard, this);
        cc.systemEvent.off('chatPush', this.showChat, this);
    }

    update() {
        if (dd.gm_manager && dd.gm_manager.getTableData()) {
            let tabelData = dd.gm_manager.getTableData();
            if (tabelData.gameState === dd.game_state.STATE_TABLE_OUTCARD_2) {
                if (dd.gm_manager.publicCardNum !== 3) {
                    dd.gm_manager.publicCardNum = 3;
                    //创建3张公共牌
                    this.showPublicCardAction(0, 3);
                }
            } else if (tabelData.gameState === dd.game_state.STATE_TABLE_OUTCARD_3) {
                if (dd.gm_manager.publicCardNum !== 4) {
                    dd.gm_manager.publicCardNum = 4;
                    //创建第4张公共牌
                    this.showPublicCardAction(3, 4);
                    this.updatePublicCard(3);
                }
            } else if (tabelData.gameState === dd.game_state.STATE_TABLE_OUTCARD_4) {
                if (dd.gm_manager.publicCardNum !== 5) {
                    dd.gm_manager.publicCardNum = 5;
                    //创建第5张公共牌
                    this.showPublicCardAction(4, 5);
                    this.updatePublicCard(4);
                }
            } else {
                this.updatePublicCard(tabelData.tableHandCards.length);
                //单局结算
                if (tabelData.gameState === dd.game_state.STATE_TABLE_OVER_ONCE) {
                    if (!dd.gm_manager.isShowGameOver) {
                        dd.gm_manager.isShowGameOver = true;
                        this.showOthersCard();
                        setTimeout(() => {
                            this.showGameOver();
                        }, 1000);
                    }
                }
            }
        }
    }
    /**
     * 显示聊天
     * @memberof PlayerLayer
     */
    showChat(content: any) {
        let data = content.detail;
        let seatInfo = dd.gm_manager.getSeatDataByAccount(data.accountId);
        if (seatInfo) {
            let playerNode = this.getNodeBySeatId(seatInfo.seatIndex);
            if (playerNode) {
                let node = new cc.Node();
                node.width = 80;
                node.height = 80;
                let spriteNode = new cc.Node();
                spriteNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.bqSpriteFrames[data.content - 1];
                spriteNode.parent = node;
                node.parent = playerNode;
                node.scale = 0;

                let action = cc.scaleTo(0.5, 1);
                action.easing(cc.easeElasticOut(0.4));
                let seq = cc.sequence(action, cc.delayTime(1), cc.callFunc((target: cc.Node, data?: any) => {
                    target.removeFromParent(true);
                    target.destroy();
                }, this));
                node.runAction(seq);
            }
        }

    }
    /**
     * 更新坐位上的玩家
     * 
     * @memberof PlayerLayer
     */
    updatePlayer() {
        let mineSeat = dd.gm_manager.getMineSeat();
        if (mineSeat) {
            dd.gm_manager.startIndex = mineSeat.seatIndex;
        }
        let tempIndex = dd.gm_manager.startIndex;
        for (let i = 0; i < this.seatNodes.length; i++) {
            let seatData = dd.gm_manager.getSeatDataByIndex(tempIndex);
            let seatNode = this.seatNodes[i];
            let emptyNode = seatNode.getChildByName('empty');
            let playerNode = seatNode.getChildByName('Player');
            if (seatData.accountId === '0') {//座位没人
                emptyNode.active = true;
                if (playerNode) {
                    playerNode.destroy();
                }
            } else {//座位有人
                emptyNode.active = false;
                if (!playerNode) {
                    playerNode = cc.instantiate(this.pre_player);
                }
                playerNode.getComponent('PlayerUI').updateData(seatData, i);
                playerNode.parent = seatNode;
                if (i === 0 && mineSeat) {//是自己
                    playerNode.x = -150;
                } else {
                    playerNode.x = 0;
                }
            }
            tempIndex++;
            if (tempIndex > 8) {
                tempIndex = 0;
            }
        }
    }
    /**
     * 播放发牌动画(如果自己参与战斗发牌给自己后会执行翻牌动画)
     * 
     * @memberof PlayerLayer
     */
    doFPAction() {
        let nodeList: cc.Node[] = [];
        let dataList: SeatData[] = [];
        let tabelData = dd.gm_manager.getTableData();
        let dealIndex = tabelData.bankerIndex;
        for (let i = 0; i < tabelData.seats.length; i++) {
            let seatData = tabelData.seats[dealIndex];
            if (seatData.bGamed === 1) {
                let nodeIndex = seatData.seatIndex - dd.gm_manager.startIndex;
                if (nodeIndex < 0) {
                    nodeIndex = 9 + nodeIndex;
                }
                let playerNode = this.seatNodes[nodeIndex].getChildByName('Player');
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
        let dTime = (Number(tabelData.actTime) - Number(tabelData.svrTime)) / 1000;
        let eTime = dTime / nodeList.length / 2;
        if (eTime < 0.1) {
            eTime = 0.1;
        }
        cc.log('发每张手牌的时间' + eTime);
        let index = 0;
        for (let i = 0; i < 2; i++) {
            for (let j = 0; j < nodeList.length; j++) {
                let playerNode = nodeList[j];
                if (playerNode) {
                    let data = dataList[j];
                    let endPos = playerNode.parent.getPosition().add(playerNode.getPosition());
                    let isMine = playerNode.x === 0 ? false : true;
                    let actionNode = new cc.Node();
                    actionNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
                    actionNode.scale = 0.5;
                    actionNode.setPosition(0, 0);
                    actionNode.parent = this.node;
                    let delayAction = cc.delayTime(eTime * index);
                    let audioCallBack = cc.callFunc((target: cc.Node) => {
                        dd.mp_manager.playDeal();
                    }, this);
                    let moveAction = cc.moveTo(eTime, endPos);
                    let scaleAction = cc.scaleTo(eTime, 1);
                    let rotateAction = cc.rotateBy(eTime, 720);
                    let spawnAction = cc.spawn(moveAction, scaleAction, rotateAction);
                    let callback = cc.callFunc((target: cc.Node) => {
                        target.destroy();

                        if (isMine && playerNode && playerNode.isValid) {
                            dd.gm_manager.isCreateMineCard = true;
                            //播放翻牌动画
                            let fpNode = cc.instantiate(this.pre_card);
                            fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(data.handCards[i]);
                            fpNode.parent = playerNode.getChildByName('card' + (i + 1));
                            fpNode.getComponent(cc.Animation).play();
                            dd.mp_manager.playFlop();
                        }
                    }, this);
                    actionNode.runAction(cc.sequence(delayAction, audioCallBack, spawnAction, callback));
                    index++;
                }
            }
        }
    }
    /**
     * 显示结算时候的玩家看牌
     * @memberof PlayerLayer
     */
    showLookCard() {
        let tabelData = dd.gm_manager.getTableData();
        if (tabelData && tabelData.showCardSeatIndex !== -1) {
            let seatInfo = dd.gm_manager.getSeatDataByIndex(tabelData.showCardSeatIndex);
            //不是自己才翻牌
            if (!dd.gm_manager.isMineSeat(seatInfo)) {
                let playerNode = this.getNodeBySeatId(tabelData.showCardSeatIndex);
                if (playerNode) {
                    for (let i = 0; i < 2; i++) {
                        //播放翻牌动画
                        let fpNode = cc.instantiate(this.pre_card);
                        fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(seatInfo.handCards[i]);
                        fpNode.parent = playerNode.getChildByName('card' + (i + 3));
                        fpNode.getComponent(cc.Animation).play();
                        dd.mp_manager.playFlop();
                    }
                }
            }
        }

    }

    /**
     * 显示公共牌的发牌动作
     * @param {number} sIndex 
     * @param {number} eIndex 结束牌索引
     * @memberof PlayerLayer
     */
    showPublicCardAction(sIndex: number, eIndex: number) {
        cc.log('显示公共牌的发牌动作');
        let tabelData = dd.gm_manager.getTableData();
        let index = 0;
        let dTime = (Number(tabelData.actTime) - Number(tabelData.svrTime)) / 1000;
        let eTime = dTime / (eIndex - sIndex);
        if (eTime < 0.1) {
            eTime = 0.1;
        }
        cc.log('发每张公共牌的时间' + eTime);
        for (let i = sIndex; i < eIndex; i++) {
            let pCardNode = this.pCardNodes[i];
            let oldCardNode = pCardNode.getChildByName('FPCard');
            if (oldCardNode) {
                oldCardNode.destroy();
            }
            let endPos = pCardNode.parent.getPosition().add(pCardNode.getPosition());
            let actionNode = new cc.Node();
            actionNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
            actionNode.scale = 0.5;
            actionNode.setPosition(0, 0);
            actionNode.parent = this.node;
            let delayAction = cc.delayTime(eTime * index);
            let moveAction = cc.moveTo(eTime, endPos);
            let scaleAction = cc.scaleTo(eTime, 1);
            let rotateAction = cc.rotateBy(eTime, 720);
            let spawnAction = cc.spawn(moveAction, scaleAction, rotateAction);
            let callback = cc.callFunc((target: cc.Node) => {
                target.destroy();

                //播放翻牌动画
                let fpNode = cc.instantiate(this.pre_card);
                fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(tabelData.tableHandCards[i]);
                fpNode.parent = pCardNode;
                fpNode.getComponent(cc.Animation).play();
                dd.mp_manager.playFlop();
            }, this);
            actionNode.runAction(cc.sequence(delayAction, spawnAction, callback));
            dd.mp_manager.playDeal();
            index++;
        }
    }
    /**
     * 刷新公共牌
     * @param {number} pIndex 刷新公共牌的数量
     * @memberof PlayerLayer
     */
    updatePublicCard(pIndex: number) {
        let tabelData = dd.gm_manager.getTableData();
        if (tabelData.gameState < dd.game_state.STATE_TABLE_OUTCARD_2) {
            for (let i = 0; i < this.pCardNodes.length; i++) {
                this.pCardNodes[i].removeAllChildren();
            }
        } else {
            tabelData.tableHandCards.forEach((id: number, index: number) => {
                if (index < pIndex) {
                    let pNode = this.pCardNodes[index];
                    let cardNode = pNode.getChildByName('FPCard');
                    if (!cardNode) {
                        cardNode = cc.instantiate(this.pre_card);
                        cardNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(id);
                        cardNode.getChildByName('back').active = false;
                        cardNode.parent = pNode;
                    } else {
                        let isPlayAnim = cardNode.getComponent(cc.Animation).getAnimationState('fpAction').isPlaying;
                        if (!isPlayAnim) {
                            cardNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(id);
                            cardNode.getChildByName('back').active = false;
                        }
                    }

                }
            }, this);
        }
    }
    /**
     * 显示表态人的表态结果
     * @memberof PlayerLayer
     */
    showBTResult() {
        let tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            let prevBtIndex = tabelData.prevBtIndex;
            let prevSeat = dd.gm_manager.getSeatDataByIndex(prevBtIndex);
            if (prevSeat) {
                switch (prevSeat.btResult) {//座位表态结果(1=弃牌,2=过牌,3=跟注,4=加注,5=全下
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
    }
    /**
     * 显示弃牌动作
     * @memberof PlayerLayer
     */
    showDiscardAction(seatData: SeatData) {
        let playerNode = this.getNodeBySeatId(seatData.seatIndex);
        if (playerNode) {
            let sPos = playerNode.parent.getPosition().add(playerNode.getPosition());
            let actionNode = new cc.Node();
            actionNode.addComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(0);
            actionNode.setPosition(sPos);
            actionNode.parent = this.node;
            let moveAction = cc.moveTo(0.5, cc.p(0, 0));
            let scaleAction = cc.scaleTo(0.5, 0.5);
            let rotateAction = cc.rotateBy(0.5, 720);
            let spawnAction = cc.spawn(moveAction, scaleAction, rotateAction);
            let callback = cc.callFunc((target: cc.Node) => {
                target.destroy();
            }, this);
            actionNode.runAction(cc.sequence(spawnAction, callback));
            dd.mp_manager.playFold();
        }
    }
    /**
     * 显示下注的动作
     * @memberof PlayerLayer
     */
    showFlyBetAction(seatData: SeatData, cb: any = null) {
        let seats = dd.gm_manager.getTableData().seats;
        let canvasTarget = dd.ui_manager.getCanvasNode().getComponent('GameCanvas');
        let ePos = canvasTarget.pool.parent.getPosition().add(canvasTarget.pool.getPosition());
        let playerNode = this.getNodeBySeatId(seatData.seatIndex);
        if (playerNode) {
            let pos = playerNode.parent.getPosition().add(playerNode.getPosition());
            this.flyBetAction(pos, ePos, '', this.node, cb);
        }
    }
    /**
     * 飞积分
     * @param {cc.Vec2} sPos 开始位置
     * @param {cc.Vec2} ePos 结束位置
     * @param {string} money 金币
     * @param {cc.Node} parentNode 父节点
     * @param {any} cb  回调函数
     * @memberof PlayerLayer
     */
    flyBetAction(sPos: cc.Vec2, ePos: cc.Vec2, money: string, parentNode: cc.Node, cb: any = null) {
        let chipNode = cc.instantiate(this.chip_prefab);
        chipNode.getChildByName('lblBet').getComponent(cc.Label).string = money;
        chipNode.setPosition(sPos);
        chipNode.parent = this.node;
        let moveAction = cc.moveTo(cc.pDistance(ePos, sPos) / 2500, ePos);
        let callback = cc.callFunc((target: cc.Node) => {
            target.destroy();
            if (cb) {
                cb();
            }
        }, this);
        chipNode.runAction(cc.sequence(moveAction, callback));
    }
    /**
     * 根据座位id获取节点索引
     * @param {number} seatIndex 座位号
     * @returns 
     * @memberof PlayerLayer
     */
    getNodeBySeatId(seatIndex: number) {
        let nodeIndex = seatIndex - dd.gm_manager.startIndex;
        if (nodeIndex < 0) {
            nodeIndex = 9 + nodeIndex;
        }
        let playerNode = this.seatNodes[nodeIndex].getChildByName('Player');
        return playerNode;
    }
    /**
     * 显示游戏单局结算飞积分
     * @memberof PlayerLayer
     */
    showGameOver() {
        let canvasTarget = dd.ui_manager.getCanvasNode().getComponent('GameCanvas');
        let ePos = canvasTarget.pool.parent.getPosition().add(canvasTarget.pool.getPosition());
        let tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            dd.mp_manager.playOver();
            for (let i = 0; i < tabelData.settlementOnceList.length; i++) {
                let data = tabelData.settlementOnceList[i];
                let playerNode = this.getNodeBySeatId(data.seatIndex);
                if (playerNode) {
                    if (Number(data.winMoney) > 0) {
                        let pos = playerNode.parent.getPosition().add(playerNode.getPosition());
                        //播放飞积分的动画
                        this.flyBetAction(ePos, pos, data.winMoney, this.node);
                    }
                    //播放飞数字的动画
                    this.showWinOrLostAction(data.winMoney, playerNode);
                }

            }
        }
    }
    /**
     * 游戏结算时，显示其他人的牌
     * @memberof PlayerLayer
     */
    showOthersCard() {
        let tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            let players: SeatData[] = [];//游戏结算时，剩余的在游戏中的玩家数量
            for (let i = 0; i < tabelData.seats.length; i++) {
                let seat = tabelData.seats[i];
                if (seat.bGamed === 1 && seat.btResult !== 1) {
                    players.push(seat);
                }
            }
            //如果剩余的玩家数量大于1，就翻开所有剩余玩家的牌
            if (players.length > 1) {
                for (let i = 0; i < players.length; i++) {
                    let seat = players[i];
                    //不是自己才翻牌
                    if (!dd.gm_manager.isMineSeat(seat)) {
                        let playerNode = this.getNodeBySeatId(seat.seatIndex);
                        if (playerNode) {
                            for (let j = 0; j < seat.handCards.length; j++) {
                                //播放翻牌动画
                                let fpNode = cc.instantiate(this.pre_card);
                                fpNode.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(seat.handCards[j]);
                                fpNode.parent = playerNode.getChildByName('card' + (j + 3));
                                fpNode.getComponent(cc.Animation).play();
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 显示输赢金币动作
     * @memberof PlayerLayer
     */
    showWinOrLost() {
        let tabelData = dd.gm_manager.getTableData();
        if (tabelData) {
            for (let i = 0; i < tabelData.settlementOnceList.length; i++) {
                let data = tabelData.settlementOnceList[i];
                let playerNode = this.getNodeBySeatId(data.seatIndex);
                if (playerNode) {
                    this.showWinOrLostAction(data.winMoney, playerNode);
                }
            }
        }
    }
    /**
     * 显示输赢金币动作
     * @param {number} money 
     * @memberof PlayerLayer
     */
    showWinOrLostAction(money: string, parent: cc.Node) {
        let actionNode = new cc.Node();
        let lbl = actionNode.addComponent(cc.Label);
        lbl.fontSize = 40;
        lbl.string = Number(money) > 0 ? ('+' + money) : money;
        actionNode.parent = parent;
        actionNode.setPosition(cc.p(0, 0));
        actionNode.color = Number(money) > 0 ? cc.Color.RED : cc.Color.GREEN;
        let move = cc.moveBy(1, cc.p(0, 100));
        let callback = cc.callFunc((target: cc.Node) => {
            target.destroy();
        }, this);
        actionNode.runAction(cc.sequence(move, callback));
    }
    /**
     * 点击空位
     * 
     * @param {cc.Event.EventTouch} event 
     * @memberof PlayerLayer
     */
    click_empty(event: cc.Event.EventTouch) {
        if (dd.gm_manager.getMineSeat()) return;
        dd.ui_manager.showLoading('正在申请坐下');
        dd.mp_manager.playButton();
        let seatIndex = Number(event.getCurrentTarget().parent.name) + dd.gm_manager.startIndex;
        if (seatIndex > 8) {
            seatIndex -= 9;
        }
        let obj = {
            tableId: dd.gm_manager.getTableData().tableId,
            seatIndex: seatIndex
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_SEAT_DOWN, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                dd.ui_manager.showTip('入座成功!');
            } else if (flag === -1) {
                dd.ui_manager.showTip('坐下消息发送超时');
            } else {
                // dd.ui_manager.showTip(content);
                dd.gm_manager.btnScript.click_buy();
                dd.ui_manager.hideLoading();
            }
        });
    }
}
