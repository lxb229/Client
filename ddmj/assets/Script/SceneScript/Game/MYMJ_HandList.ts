const { ccclass, property } = cc._decorator;

import MJ_Card from './MJ_Card';
import MJ_Card_Group from './MJ_Card_Group';
import MJCanvas from './MJCanvas';
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState, MJ_Act_State } from '../../Modules/Protocol';

@ccclass
export default class MJ_HandList extends cc.Component {
    /**
     * 手牌节点
     * 
     * @type {cc.Node}
     * @memberof MJ_HandList
     */
    @property(cc.Node)
    node_hand: cc.Node = null;

    /**
     * 开始点击的位置
     * 
     * @type {cc.Vec2}
     * @memberof MJ_HandList
     */
    _firstPos: cc.Vec2 = cc.p(0, 0);
    /**
     * 结束点击的位置
     * 
     * @type {cc.Vec2}
     * @memberof MJ_HandList
     */
    _endPos: cc.Vec2 = cc.p(0, 0);
    /**
     * canvas脚本
     * 
     * @type {MJCanvas}
     * @memberof MJ_HandList
     */
    _canvasTarget: MJCanvas = null;

    /**
     * 自己的脚本
     * 
     * @memberof MJ_HandList
     */
    _mineScript = null;
    /**
    * 手牌节点列表
    * 
    * @type {cc.Node[]}
    * @memberof MJ_Play
    */
    _hand_card_list: cc.Node[] = [];

    /**
     * 座位信息
     * 
     * @type {SeatVo}
     * @memberof MJ_HandList
     */
    _seatInfo: SeatVo = null;
    /**
     * 
     * 点击选中的牌
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    _selectCard: cc.Node = null;

    _selectCardPos: cc.Vec2 = cc.v2(0, 0);
    _selectCardZIndex: number = 0;
    /**
     * 移动偏移量
     * @type {number}
     * @memberof MJ_HandList
     */
    _moveDelta: number = 0;
    /**
     * 
     * 显示换三张的牌的状态 0未显示 1已显示
     * @type {number}
     * @memberof MJ_HandList
     */
    _swapState: number = 0;

    /**
     * 出牌可以听牌的列表
     * 
     * @memberof MJ_HandList
     */
    _tingsList: number[] = [];
    /**
     * _tingsList 的对应列表，打出对应的那张牌，可以胡的牌的列表
     * 
     * @memberof MJ_HandList
     */
    _huList = [];

    wans: CardAttrib[] = [];
    tongs: CardAttrib[] = [];
    tiaos: CardAttrib[] = [];
    _moPaiCardId: number = -1;
    /**
     * 自己是否能出牌
     * @type {boolean}
     * @memberof MJ_HandList
     */
    _isCanPlay: boolean = false;
    /**
     * 是佛打完定缺的牌
     * @type {boolean}
     * @memberof MJ_HandList
     */
    _isUnSuit: boolean = false;
    /**
     * 触摸选中这张牌
     * 
     * @returns 
     * @memberof MJ_HandList
     */
    setSelectCard(cardNode: cc.Node) {
        this._canvasTarget.showTingPai(false);
        if (cardNode && cardNode.isValid) {
            cardNode.color = cc.Color.WHITE;
            let hcs: MJ_Card = cardNode.getComponent('MJ_Card');

            //如果是换牌阶段
            if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_SWAPCARD) {
                if (hcs._isSelect) {//当前牌处于选中状态
                    hcs.showSelectCard(false);
                    //从数组中减去
                    this._mineScript.selectSwapCard(hcs._cardId, -1);
                } else {
                    let isSelect = this._mineScript.selectSwapCard(hcs._cardId, 1);
                    hcs.showSelectCard(isSelect);
                }
            } else {
                //如果这张牌不能打
                if (hcs._isShowMask) {
                    cc.log('不能打这张牌');
                    hcs.showSelectCard(false);
                } else {
                    //绵阳麻将 == 如果正在躺牌，
                    let isTang = this._mineScript.getIsShowHui();
                    if (hcs._isSelect) {//当前牌处于选中状态
                        //绵阳麻将 == 如果正在躺牌，就收回选中的牌
                        if (isTang) {
                            hcs.showSelectCard(false);
                            return;
                        }
                        //如果拖动牌，移动距离大于80,但是距离小于150，就不打出这张牌
                        if (this._moveDelta > 80) {
                            let d = cc.pDistance(this._firstPos, this._endPos);
                            let dy = this._endPos.y - this._firstPos.y;
                            if (d > 150 && dy > 80) {
                                cc.log('打出这张牌,拖动距离：' + d + ';拖动高度：' + dy);
                                if (this._seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
                                    this._canvasTarget.sendOutCard(hcs._cardId);
                                    //移除节点
                                    this.deleteCardByNode(cardNode);
                                }
                            } else {
                                cc.log('选中这张牌');
                                this.selectCardByCardId(hcs._cardId);
                                this._canvasTarget.showTSCard(hcs._cardId);
                            }
                        } else {
                            cc.log('打出这张牌');
                            if (this._seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
                                this._canvasTarget.sendOutCard(hcs._cardId);
                                //移除节点
                                this.deleteCardByNode(cardNode);
                            } else {
                                hcs.showSelectCard(false);
                            }
                        }
                    } else {
                        //如果当前牌没有选中
                        //绵阳麻将 == 如果正在躺牌，
                        if (isTang) {
                            //如果显示悔按钮状态下，就选中牌，并显示躺界面
                            cc.log('选中这张牌');
                            let tangObj: TangCfg = this.selectTangOutCard(hcs._cardId);
                            this._canvasTarget.showTSCard(hcs._cardId);
                            this._mineScript.showTangNode(true, tangObj);
                        } else {
                            let d = cc.pDistance(this._firstPos, this._endPos);
                            let dy = this._endPos.y - this._firstPos.y;
                            if (d > 200 && dy > 80) {
                                cc.log('打出这张牌,拖动距离：' + d + ';拖动高度：' + dy);
                                if (this._seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
                                    this._canvasTarget.sendOutCard(hcs._cardId);
                                    //移除节点
                                    this.deleteCardByNode(cardNode);
                                }
                            } else {
                                cc.log('选中这张牌');
                                this.selectCardByCardId(hcs._cardId);
                                this._canvasTarget.showTSCard(hcs._cardId);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 点击开始
     * 
     * @memberof MJ_HandList
     */
    touchBegan = (event: cc.Event.EventTouch) => {
        event.stopPropagation();
        if (event.getTouches().length > 1) return;
        if (dd.gm_manager.touchTarget || !this._isCanPlay) {
            dd.gm_manager.touchTarget = null;
            if (this._selectCard) {
                this._selectCard.setPosition(this._selectCardPos);
                this._selectCard.zIndex = this._selectCardZIndex;
                this._selectCard = null;
            }
            return;
        }
        dd.gm_manager.touchTarget = event.touch;
        let touches = event.getTouches();
        this._firstPos = touches[0].getLocation();
        let cardNode = this.getCardNodeByTouch(this._firstPos);
        if (cardNode) {
            this._selectCardPos = cardNode.getPosition();
            let hcs: MJ_Card = cardNode.getComponent('MJ_Card');
            if (!hcs._isShowMask) {
                this._selectCard = cardNode;
                this._selectCard.color = cc.Color.GRAY;
                //设置层级数据
                this._selectCardZIndex = this._selectCard.zIndex;
                this._selectCard.zIndex = 99;
            } else {
                this._selectCard = null;
            }
        }
    }

    /**
     * 点击移动
     * 
     * @memberof MJ_HandList
     */
    touchMoved = (event: cc.Event.EventTouch) => {
        event.stopPropagation();
        if (event.getTouches().length > 1) return;
        if (dd.gm_manager.touchTarget === event.touch) {
            if (this.node.isValid) {
                if (this._selectCard) {
                    if (this._isCanPlay) {
                        let touches = event.getTouches();
                        this._endPos = touches[0].getLocation();

                        let delta = touches[0].getDelta();
                        let cPos = this._selectCard.getPosition();
                        let pos = cc.pAdd(delta, cPos);
                        this._selectCard.setPosition(pos);
                        let dx = Math.abs(delta.x);
                        let dy = Math.abs(delta.y);
                        this._moveDelta += Math.sqrt(dx * dx + dy * dy);
                    }
                }
            }
        }
    }

    /**
     * 点击结束
     * 
     * @memberof MJ_HandList
     */
    touchEnd = (event: cc.Event.EventTouch) => {
        event.stopPropagation();
        if (event.getTouches().length > 1) return;
        if (dd.gm_manager.touchTarget === event.touch) {
            if (this._selectCard) {
                //重置这张牌的位置和层级
                this._selectCard.setPosition(this._selectCardPos);
                this._selectCard.zIndex = this._selectCardZIndex;
                //如果自己能出牌
                if (this._isCanPlay) {
                    let touches = event.getTouches();
                    this._endPos = touches[0].getLocation();
                    this.setSelectCard(this._selectCard);
                    this._moveDelta = 0;
                }
                this._selectCard = null;
            }
            dd.gm_manager.touchTarget = null;
        }
    }

    /**
     * 添加节点点击事件
     * 
     * @memberof MJ_HandList
     */
    onTouchEvent() {
        this.node.on(cc.Node.EventType.TOUCH_START, this.touchBegan, this);
        this.node.on(cc.Node.EventType.TOUCH_CANCEL, this.touchEnd, this);
        this.node.on(cc.Node.EventType.TOUCH_END, this.touchEnd, this);
        this.node.on(cc.Node.EventType.TOUCH_MOVE, this.touchMoved, this);
    }

    onLoad() {
        for (let i = 1; i < 4; i++) {
            for (let j = 1; j < 10; j++) {
                let card: CardAttrib = {
                    cardId: 0,
                    suit: i,
                    point: j
                };
                switch (i) {
                    case 1: this.wans.push(card); break;
                    case 2: this.tongs.push(card); break;
                    case 3: this.tiaos.push(card); break;
                    default: break;
                }
            }
        }
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent("MJCanvas");
        this.onTouchEvent();
    }

    initData() {
        if (!this._canvasTarget) {
            this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        }
        this._hand_card_list.length = 0;
        this.node_hand.removeAllChildren();
        this._moPaiCardId = -1;
    }

    /**
     * 获取是否可以出牌
     * @memberof MJ_HandList
     */
    getIsCP() {
        let isCP = false;
        //绵阳麻将 == 如果该玩家躺牌了，也不能出牌
        if (this._seatInfo.tangCardState === 0) {
            switch (dd.gm_manager.mjGameData.tableBaseVo.gameState) {
                case MJ_GameState.STATE_TABLE_SWAPCARD:
                    if (this._seatInfo.swapCards) {
                        isCP = false;
                    } else {
                        isCP = true;
                    }
                    break;
                case MJ_GameState.STATE_TABLE_OUTCARD:
                    //如果表态人是自己
                    if (this._seatInfo.btState === MJ_Act_State.ACT_STATE_WAIT &&
                        this._seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
                        isCP = true;
                    } else {
                        isCP = false;
                    }
                    break;
                case MJ_GameState.STATE_TABLE_BREAKCARD:
                    let isHui = this._mineScript.getIsShowHui();
                    if (isHui) {
                        isCP = true;
                    } else {
                        isCP = false;
                    }
                    break;
                default:
                    break;
            }
        }
        return isCP;
    }

    /**
     * 刷新手牌列表
     * 
     * @param {SeatVo} seatInfo 
     * @memberof MJ_HandList
     */
    updateHandList(seatInfo: SeatVo, target) {
        if (!this._canvasTarget) {
            this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        }
        //如果在准备阶段，就初始化数据
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState <= MJ_GameState.STATE_TABLE_READY) {
            this.initData();
        }
        this._mineScript = target;
        this._seatInfo = seatInfo;
        this._isCanPlay = this.getIsCP();

        //移动其他手牌
        let handCards = [];
        if (this._seatInfo.handCards) {
            handCards = this._seatInfo.handCards;
        }
        //如果是自己表态，并且 摸牌存在，合并到手牌数组中
        if (dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex && this._seatInfo.moPaiCard > 0) {
            handCards = handCards.concat(this._seatInfo.moPaiCard);
            this._moPaiCardId = this._seatInfo.moPaiCard;
        }

        //绵阳麻将 == 如果该玩家躺牌了并存在躺牌，就要把手牌中的躺牌去重 
        if (this._seatInfo.tangCardState === 1 && this._seatInfo.tangCardList) {
            handCards = dd.gm_manager.getDiffAToB(handCards, this._seatInfo.tangCardList);
            handCards.sort((a, b) => {
                return b - a;
            });
        }
        this._isUnSuit = this.getIsUnSuit(handCards);

        //绵阳麻将 == 如果正在躺牌，就不刷新手牌了
        let isTang = this._mineScript.getIsShowHui();
        if (!isTang) {
            //如果是重播，就刷新手牌
            if (dd.gm_manager.replayMJ === 1) {
                this.showReplayHandCards(handCards, this._moPaiCardId, this._isUnSuit);
            } else {
                //如果不是出牌阶段,移除
                if (dd.gm_manager.mjGameData.tableBaseVo.gameState !== MJ_GameState.STATE_TABLE_OUTCARD) {
                    this._canvasTarget.showTingPai(false);
                }
                this.showHandCard(handCards, this._moPaiCardId, this._isUnSuit);
                this.deleteNotCard(handCards);
                //桌子上有无听牌提示
                // if (dd.gm_manager.mjGameData.tableBaseVo.tingTips === 1) {
                this.showTingCard(handCards, this._isUnSuit);
                // }
                this.showSwapCards();
            }
        }

        //是否是绵阳麻将
        //如果是自己表态，并且 摸牌存在，合并到手牌数组中
        if (dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex && this._seatInfo.moPaiCard > 0) {
            this.node_hand.width = 85 * (handCards.length - 1) + 10;
        } else {
            this.node_hand.width = 85 * handCards.length + 10;
        }
        cc.log('手牌的数量-----' + handCards.length + '-----手牌节点的宽度：' + this.node_hand.width);
    }

    /**
     * 显示换三张的牌
     * 
     * @memberof MJ_HandList
     */
    showSwapCards() {
        //如果在定缺阶段,有换三张的牌,
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_DINGQUE) {
            if (this._swapState === 0) {
                this._swapState = 1;
                if (this._seatInfo.swapCards && this._seatInfo.swapCards.length > 0 && this._seatInfo.unSuit < 1) {
                    for (var i = 0; i < this._seatInfo.swapCards.length; i++) {
                        for (var j = 0; j < this._hand_card_list.length; j++) {
                            let cardNode = this._hand_card_list[j];
                            if (cardNode.tag === this._seatInfo.swapCards[i]) {
                                let hcs: MJ_Card = cardNode.getComponent('MJ_Card');
                                hcs.showSelectCard(true);
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            this._swapState = 0;
        }
    }


    /**
     * 显示听牌
     * @param {number[]} handCards 
     * @param {boolean} isUnSuit 是否打完定缺的牌
     * @memberof MJ_HandList
     */
    showTingCard(handCards: number[], isUnSuit: boolean) {
        this._tingsList.length = 0;
        this._huList.length = 0;
        let isShow = false;

        //只有在 （自己表态）、（游戏出牌）的条件下，才会显示听牌
        if (dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex
            && this._seatInfo.btState === MJ_Act_State.ACT_STATE_WAIT) {
            if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_OUTCARD
                || dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_BREAKCARD) {
                isShow = true;
            }
        }

        for (var i = 0; i < this._hand_card_list.length; i++) {
            let hcn: cc.Node = this._hand_card_list[i];
            let hcs: MJ_Card = hcn.getComponent('MJ_Card');
            if (isShow) {
                let tings = this.getTingsByCardId(handCards, hcn.tag);
                //如果有听牌的话
                if (tings && tings.length > 0) {
                    this._tingsList.push(hcn.tag);
                    this._huList.push(tings);
                    //如果打完定缺的牌，显示标识
                    if (isUnSuit) {
                        hcs.showBS(true, 1, -1);
                    } else {
                        let card: CardAttrib = dd.gm_manager.getCardById(hcs._cardId);
                        //如果未打完定缺的牌，并且，这张牌是定缺的牌，显示标识
                        if (card.suit === this._seatInfo.unSuit) {
                            hcs.showBS(true, 1, -1);
                        } else {
                            //如果不是定缺的牌，就不显示
                            hcs.showBS(false, 1, -1);
                        }
                    }
                }
            } else {
                hcs.showBS(false, 1, -1);
            }
        }
    }
    /**
     * 根据是否躺牌显示手牌,用来切换点躺牌按钮之后，只亮可以听牌的牌，而点击悔之后，就显示回正常
     * @param {boolean} isShowTang 
     * @memberof MJ_HandList
     */
    showHandCardsByTang(isShowTang: boolean) {
        this._isCanPlay = isShowTang;
        for (var i = 0; i < this._hand_card_list.length; i++) {
            let cardNode = this._hand_card_list[i];
            if (cardNode) {
                let hcs: MJ_Card = cardNode.getComponent('MJ_Card');
                //如果没有定缺的牌了
                if (this._isUnSuit) {
                    if (isShowTang) {
                        //标识不存在 或 不显示躺牌
                        if (!hcs.bsNode.active) {
                            hcs.showMask(true);
                        } else {
                            hcs.showMask(false);
                        }
                    } else {
                        hcs.showMask(false);
                    }
                } else {
                    let card: CardAttrib = dd.gm_manager.getCardById(hcs._cardId);
                    if (card.suit === this._seatInfo.unSuit) {
                        hcs.showMask(false);
                    } else {
                        hcs.showMask(true);
                    }
                }
            }
        }
    }
    /**
     * 显示重播时候的手牌
     * 
     * @param {number[]} handCards 手牌列表
     * @param {number} [moPaiCard]   摸牌
     * @param {boolean} isUnSuit 是否打完定缺
     * @memberof MJ_HandList
     */
    showReplayHandCards(handCards: number[], moPaiCard: number, isUnSuit: boolean) {
        this.node_hand.removeAllChildren();
        for (var i = 0; i < handCards.length; i++) {
            let index = i;
            let cardId = handCards[i];
            //如果是摸牌,并且轮到自己表态，摸牌存在,就在摸牌位置创建摸牌
            if (cardId === moPaiCard && dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex && this._seatInfo.moPaiCard > 0) {
                this._canvasTarget.showMineCard(this._seatInfo.moPaiCard, this.node_hand, false, (cardNode: cc.Node) => {
                    cardNode.setPosition(cardNode.width, 0);
                    this.showUnSuit(cardNode, isUnSuit);
                });
            } else {
                this._canvasTarget.showMineCard(cardId, this.node_hand, false, (cardNode: cc.Node) => {
                    let ePos = cc.p((index * (-cardNode.width) - cardNode.width / 2), 0);
                    cardNode.setPosition(ePos);
                    this.showUnSuit(cardNode, isUnSuit);
                });
            }
        }
    }
    /**
     * 显示手牌
     * 
     * @param {number[]} handCards 手牌列表
     * @param {number} [moPaiCard]   摸牌
     * @param {boolean} isUnSuit 是否打完定缺
     * @memberof MJ_HandList
     */
    showHandCard(handCards: number[], moPaiCard: number, isUnSuit: boolean) {
        let isMyBreakState = false;
        let isAllSwap = false;
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_BREAKCARD) {
            isMyBreakState = this.getIsMyBreakState();
        } else if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_SWAPCARD) {
            //在换三张阶段，获取是否是自己确定换三张
            isAllSwap = this.getIsAllSwap();
        } else { }

        for (var i = 0; i < handCards.length; i++) {
            let index = i;
            let cardId = handCards[i];

            let hnc: cc.Node = this.getCardNodeByCardId(cardId, i);
            if (cardId !== moPaiCard) {
                if (hnc) {
                    //如果拍存在，修正牌的数据
                    this.fixCardNode(hnc, index, isMyBreakState, isAllSwap, isUnSuit);
                } else {
                    this._canvasTarget.showMineCard(cardId, this.node_hand, false, (cardNode: cc.Node) => {
                        let ePos = cc.p((index * (-cardNode.width) - cardNode.width / 2), 0);
                        cardNode.setPosition(ePos);
                        this._hand_card_list.push(cardNode);
                        this.showUnSuit(cardNode, isUnSuit);
                    });
                }
            } else {
                this.showMPCard(index, cardId, hnc, isUnSuit);
            }
        }
    }

    /**
     * 修正牌节数据
     * 
     * @memberof MJ_HandList
     */
    fixCardNode(hnc: cc.Node, index: number, isMyBreakState: boolean, isAllSwap: boolean, isUnSuit: boolean) {
        //如果自己出牌
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_OUTCARD) {
            //只有在 （自己表态）、（自己已经表态）、（游戏出牌）的条件下，才会做动作
            if (dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex
                && this._seatInfo.btState !== MJ_Act_State.ACT_STATE_WAIT) {
                //移动牌的位置
                this.moveCardAct(index, hnc);
            } else {
                //如果是重播
                if (dd.gm_manager.replayMJ === 1) {
                    //移动牌的位置
                    this.moveCardAct(index, hnc);
                }
            }
        } else if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_BREAKCARD) {//胡碰杠
            //在杠碰胡阶段，如果自己有杠碰胡，说明牌面有变化，需要移动牌的位置
            if (isMyBreakState) {
                //移动牌的位置
                this.moveCardAct(index, hnc);
            }
        } else if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_SWAPCARD) {//换三张
            //如果所有人都换牌了
            if (isAllSwap) {
                //重置牌的位置
                this.moveCardAct(index, hnc, false);
            } else {
                //如果自己换牌了，就移动下位置
                if (this._seatInfo.swapCards) {
                    //移动牌的位置
                    this.moveCardAct(index, hnc);
                }
                //如果自己还没有换牌，就不需要移动位置
            }
        } else if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_DINGQUE) {//定缺
            //如果定缺了
            if (this._seatInfo.unSuit && this._seatInfo.unSuit > 0) {
                //重置牌的位置
                this.moveCardAct(index, hnc, false);
            } else {
                //如果刚进入定缺阶段
                if (this._swapState === 0) {
                    //重置牌的位置
                    this.moveCardAct(index, hnc, false);
                }
            }
        } else {
            //如果是重播
            if (dd.gm_manager.replayMJ === 1) {
                //移动牌的位置
                this.moveCardAct(index, hnc);
            }
        }
        this.showUnSuit(hnc, isUnSuit);
    }


    /**
     * 获取自己是否有碰杠胡的状态
     * 
     * @returns {boolean} 
     * @memberof MJ_HandList
     */
    getIsMyBreakState(): boolean {
        let breakSeats = dd.gm_manager.mjGameData.breakSeats;
        if (breakSeats) {
            for (var i = 0; i < breakSeats.length; i++) {
                if (breakSeats[i] === this._seatInfo.seatIndex) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取是否所有人都换牌表态了
     * 
     * @memberof MJ_HandList
     */
    getIsAllSwap() {
        let seats = dd.gm_manager.mjGameData.seats;
        for (var i = 0; i < seats.length; i++) {
            if (seats[i] && seats[i].accountId !== '0' && seats[i].btState === MJ_Act_State.ACT_STATE_WAIT) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示位置
     * 
     * @param {number} index 插入位置
     * @param {number} cardId 牌的唯一Id
     * @param {cc.Node} mpCard 摸牌节点
     * @param {boolean} isUnSuit 是否打完定缺
     * @memberof MJ_HandList
     */
    showMPCard(index: number, cardId: number, mpCard: cc.Node, isUnSuit: boolean) {
        //如果是自己表态
        if (dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex) {
            //如果自己的表态状态在（等待表态），并且有（摸牌），显示摸牌
            if (this._seatInfo.btState === MJ_Act_State.ACT_STATE_WAIT && this._seatInfo.moPaiCard > 0) {
                if (!mpCard) {
                    this._canvasTarget.showMineCard(this._seatInfo.moPaiCard, this.node_hand, false, (cardNode: cc.Node) => {
                        cardNode.setPosition(cardNode.width, 0);
                        this._hand_card_list.push(cardNode);
                        this.showUnSuit(cardNode, isUnSuit);
                    });
                } else {
                    mpCard.setPosition(mpCard.width, 0);
                }
            } else {
                if (mpCard) {//如果自己已经摸牌表态了，就把摸得这张牌插入到手牌中
                    //只有在 （自己表态）、（自己已经表态）、（游戏出牌）的条件下，才会做动作
                    if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_OUTCARD
                        || dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_BREAKCARD) {
                        this.showUnSuit(mpCard, isUnSuit);
                        if (this._seatInfo.btState !== MJ_Act_State.ACT_STATE_WAIT) {
                            cc.log('-----移动摸牌---' + this._seatInfo);
                            //计算最终的位置进行插牌
                            let ePos = cc.p((index * (-mpCard.width) - mpCard.width / 2), 0);
                            mpCard.setPosition(ePos);
                            mpCard.scale = 0;
                            mpCard.runAction(cc.scaleTo(0.2, 1));
                            this._moPaiCardId = -1;
                        }
                    } else {
                        this.deleteCardNodeByCardId(cardId);
                        //移除节点
                        this.deleteCardByNode(mpCard);
                    }
                }
            }
        }
    }

    /**
     * 移动牌到目标位置
     * 
     * @param {number} index 目标索引位置
     * @param {cc.Node} cardNode 牌节点
     * @memberof MJ_HandList
     */
    moveCardAct(index: number, cardNode: cc.Node, isAct: boolean = true) {
        cardNode.stopAllActions();
        let ePos = cc.p((index * (-cardNode.width) - cardNode.width / 2), 0);
        let nPos = cardNode.getPosition();
        if (ePos.x !== nPos.x || ePos.y !== nPos.y) {
            if (isAct) {
                let d = cc.pDistance(nPos, ePos);
                cardNode.runAction(cc.moveTo(d / 1000, ePos));
            } else {
                cardNode.setPosition(ePos);
            }
        }
    }
    /**
     * 插牌动作
     * 
     * @param {number} index 插入位置
     * @param {cc.Node} cardNode 牌节点
     * @param {cc.Vec2} sPos 起始位置
     * @memberof MJ_HandList
     */
    insertCardAct(index: number, cardNode: cc.Node, sPos: cc.Vec2) {
        let ePos = cc.p((index * (-cardNode.width) - cardNode.width / 2), 0);
        let d = cc.pDistance(sPos, ePos);
        let mTime = d / 1500;
        let action = null;
        if (index > 0) {
            let dTime = 0.3;
            let addH = 0;
            let action1 = cc.spawn(cc.rotateTo(dTime, 20), cc.moveTo(dTime, cc.p(sPos.x, sPos.y + cardNode.height + addH)));
            let move = cc.moveTo(mTime, cc.p(ePos.x, ePos.y + cardNode.height + addH));
            action = cc.sequence(action1, move, cc.rotateTo(0.12, 0), cc.moveTo(dTime, ePos));
        } else {
            action = cc.moveTo(mTime, cc.p(ePos.x, ePos.y));
        }
        cardNode.runAction(action);
    }
    /**
     * 删除不要的牌节点
     * 
     * @memberof MJ_HandList
     */
    deleteNotCard(handCards: number[]) {
        for (var i = 0; i < this._hand_card_list.length; i++) {
            let cardNode = this._hand_card_list[i];
            let index = this.getIndexByCardId(cardNode.tag, handCards);
            if (index === -1) {
                cc.log('---删除---' + cardNode.tag);
                this._hand_card_list.splice(i, 1);
                //移除节点
                cardNode.removeFromParent(true);
                cardNode.destroy();
                //因为数组减1,所以下标不变
                i -= 1;
            }
        }
    }
    /**
     * 根据节点删除该节点,并从数组中移除
     * @param {cc.Node} cardNode 
     * @memberof MJ_HandList
     */
    deleteCardByNode(tagetNode: cc.Node) {
        for (var i = 0; i < this._hand_card_list.length; i++) {
            let cardNode = this._hand_card_list[i];
            if (tagetNode === cardNode) {
                this._hand_card_list.splice(i, 1);
                //移除节点
                cardNode.removeFromParent(true);
                cardNode.destroy();
                break;
            }
        }
    }
    /**
     * 根据cardId删除牌节点
     * 
     * @param {number} cardId 
     * @memberof MJ_HandList
     */
    deleteCardNodeByCardId(cardId: number) {
        for (var i = 0; i < this._hand_card_list.length; i++) {
            let hnc: cc.Node = this._hand_card_list[i];
            if (hnc.tag === cardId) {
                //移除节点
                hnc.removeFromParent(true);
                hnc.destroy();
                this._hand_card_list.splice(i, 1);
                break;
            }
        }
    }
    /**
     * 显示定缺
     * 
     * @param {boolean} isUnSuit 
     * @memberof MJ_HandList
     */
    showUnSuit(cardNode: cc.Node, isUnSuit: boolean) {
        if (cardNode) {
            let hcs: MJ_Card = cardNode.getComponent('MJ_Card');
            //如果没有定缺的牌了
            if (isUnSuit) {
                hcs.showMask(false);
            } else {
                //如果还有定缺的牌，就先打定缺的牌
                let card: CardAttrib = dd.gm_manager.getCardById(hcs._cardId);
                if (card.suit === this._seatInfo.unSuit) {
                    hcs.showMask(false);
                } else {
                    hcs.showMask(true);
                }
            }
        }
    }

    /**
     * 根据cardId获取牌节点
     * 
     * @returns {cc.Node} 
     * @memberof MJ_HandList
     */
    getCardNodeByCardId(cardId: number, index: number = 0): cc.Node {
        let cNode: cc.Node = null;
        if (cardId > 0) {
            for (var i = 0; i < this._hand_card_list.length; i++) {
                let hnc: cc.Node = this._hand_card_list[i];
                if (hnc.tag === cardId) {
                    cNode = hnc;
                    break;
                }
            }
        }
        return cNode;
    }

    /**
     * 根据cardId获取手牌位置
     * @param {number} cardId  节点的cardId
     * @param {number[]} handCards  手牌 + 摸牌列表
     * @returns {number} 
     * @memberof MJ_HandList
     */
    getIndexByCardId(cardId: number, handCards: number[]): number {
        for (var i = 0; i < handCards.length; i++) {
            if (handCards[i] === cardId) {
                return i;
            }
        }
        return -1;
    }
    /**
     * 根据触摸点获取牌节点
     * 
     * @param {cc.Vec2} touch 
     * @memberof MJ_HandList
     */
    getCardNodeByTouch(touch: cc.Vec2): cc.Node {
        let cNode = null;
        for (let i = 0; i < this._hand_card_list.length; i++) {
            let cardNode: cc.Node = this._hand_card_list[i];
            if (cardNode.isValid) {
                let box = cardNode.getBoundingBoxToWorld();
                if (cc.rectContainsPoint(box, touch)) {//触点在这张牌上
                    cNode = cardNode;
                    break;
                }
            }
        }
        return cNode;
    }

    /**
     * 根据cardId选中牌
     * 
     * @param {number} cardId 
     * @memberof MJ_HandList
     */
    selectCardByCardId(cardId: number) {
        for (var i = 0; i < this._hand_card_list.length; i++) {
            let hnc: cc.Node = this._hand_card_list[i];
            let hcs: MJ_Card = hnc.getComponent('MJ_Card');
            if (hcs._cardId === cardId) {
                hcs.showSelectCard(true);

                //桌子上有无听牌提示
                if (dd.gm_manager.mjGameData.tableBaseVo.tingTips === 1) {
                    //查找听牌的下标，如果下标不为-1，表示打出这张牌，可以听牌
                    let index = this._tingsList.indexOf(cardId);
                    if (index !== -1) {
                        //显示听牌的界面
                        cc.log('显示胡牌');
                        this._canvasTarget.showTingPai(true, this._huList[index]);
                    }
                }
            } else {
                hcs.showSelectCard(false);
            }
        }
    }

    /**
     * 根据cardId选中牌,并返回听牌
     * 
     * @param {number} cardId 
     * @memberof MJ_HandList
     */
    selectTangOutCard(cardId: number) {
        let obj: TangCfg = {
            outcard: cardId,
            hucards: [],
            cardIds: [],
            btcards: []
        };
        for (var i = 0; i < this._hand_card_list.length; i++) {
            let hnc: cc.Node = this._hand_card_list[i];
            let hcs: MJ_Card = hnc.getComponent('MJ_Card');
            if (hcs._cardId === cardId) {
                hcs.showSelectCard(true);
                //查找听牌的下标，如果下标不为-1，表示打出这张牌，可以听牌
                let index = this._tingsList.indexOf(cardId);
                if (index !== -1) {
                    //显示听牌的界面
                    cc.log('返回的听牌');
                    let hucards = this._huList[index].map((card: CardAttrib) => {
                        return card.cardId;
                    });
                    obj.hucards = hucards;
                }
            } else {
                obj.cardIds.push(hcs._cardId);
                hcs.showSelectCard(false);
            }
        }
        return obj;
    }

    /**
    * 获取自己是否打完了定缺的牌
    * 
    * @returns {boolean} 
    * @memberof MJ_Play
    */
    getIsUnSuit(list: number[]): boolean {
        //是否打完打缺
        let isUnSuit = true;
        for (var i = 0; i < list.length; i++) {
            let card: CardAttrib = dd.gm_manager.getCardById(list[i]);
            if (card.suit === this._seatInfo.unSuit) {
                isUnSuit = false;
                break;
            }
        }
        return isUnSuit;
    }
    /**
     * 根据cardId计算，打出这张牌是否可以听牌（胡牌）,返回可以 胡的牌的数组
     * 
     * @param {number[]} cardIds 手牌列表 = 手牌 + 摸牌(如果存在)
     * @param {number} cardId 
     * @returns 
     * @memberof MJ_HandList
     */
    getTingsByCardId(cardIds: number[], cardId: number) {
        let hands = cardIds.slice(0);
        let index = hands.indexOf(cardId);
        hands.splice(index, 1);
        let cards = hands.map((cardId) => {
            return dd.gm_manager.getCardById(cardId);
        }, this);
        let tings = dd.gm_manager.getTingPai(cards);
        if (tings.length > 0) {//有听牌需要显示
            return tings;
        }
        return null;
    }
    /**
     * 根据cardId计算，打出这张牌是否可以躺牌,返回可以躺的牌的数组
     * @param {number[]} cardIds 
     * @param {number} cardId 
     * @returns 
     * @memberof MJ_HandList
     */
    getTangsByCardId(cardIds: number[], cardId: number) {
        let hands = cardIds.slice(0);
        let index = hands.indexOf(cardId);
        hands.splice(index, 1);
        let cards = hands.map((cardId) => {
            return dd.gm_manager.getCardById(cardId);
        }, this);
        let tings = dd.gm_manager.getTingPai(cards);
        if (tings.length > 0) {//有听牌需要显示
            return cards;
        }
        return null;
    }

    /**
     * 播放插牌的动作
     * 
     * @param {cc.Vec2} ePos 
     * @memberof MJ_HandList
     */
    playCardAct(cardNode: cc.Node, ePos: cc.Vec2) {
        let sPos = cardNode.parent.convertToWorldSpaceAR(cardNode.getPosition());
        sPos = this.node_hand.convertToNodeSpaceAR(sPos);
        cardNode.setPosition(sPos);
        cardNode.parent = this.node_hand;
        ePos.x = Math.floor(ePos.x / cardNode.width) * (cardNode.width) + cardNode.width / 2;
        let dTime = 0.3;
        let d = cc.pDistance(sPos, ePos);
        let mTime = d / 1500;
        let addH = 0;
        let action1 = cc.spawn(cc.rotateTo(dTime, 20), cc.moveTo(dTime, cc.p(sPos.x, sPos.y + cardNode.height + addH)));
        let move = cc.moveTo(mTime, cc.p(ePos.x, ePos.y + cardNode.height + addH));
        let seq = cc.sequence(action1, move, cc.rotateTo(0.12, 0), cc.moveTo(dTime, ePos), cc.callFunc((target: cc.Node, data?: any) => {
            target.removeFromParent(true);
            target.destroy();
        }, this));
        cardNode.runAction(seq);
    }
}
