const { ccclass, property } = cc._decorator;

import MJ_Card from './MJ_Card';
import MJ_Card_Group from './MJ_Card_Group';
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState, MJ_Act_State, MJ_Suit, MJ_Act_Type, MJ_Game_Type } from '../../Modules/Protocol';

@ccclass
export default class MJ_Play extends cc.Component {
    /**
     * 牌节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_mine_card: cc.Node = null;

    /**
     * 提示的文本
     * 
     * @type {cc.Label}
     * @memberof MJ_Play
     */
    @property(cc.Label)
    lblTip: cc.Label = null;

    /**
     * 换三张的按钮
     * 
     * @type {cc.Button}
     * @memberof MJ_Play
     */
    @property(cc.Button)
    btn_swap: cc.Button = null;

    /**
     * 定缺的按钮父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_lack: cc.Node = null;

    /**
     * 碰杠胡按钮表态的父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_state: cc.Node = null;
    /**
     * 推荐文字
     * 
     * @type {cc.SpriteFrame}
     * @memberof MJ_Play
     */
    @property(cc.SpriteFrame)
    img_tj: cc.SpriteFrame = null;
    /**
     *定缺节点列表
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Play
     */
    @property([cc.Node])
    node_dq_list: cc.Node[] = [];
    /**
     * 胡、杠、碰、吃、躺、过的节点列表
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Play
     */
    @property([cc.Node])
    node_state_list: cc.Node[] = [];

    /**
     * 当前玩家的信息
     * 
     * @type {SeatVo}
     * @memberof MJ_Play
     */
    _seatInfo: SeatVo = null;

    /**
     * 换牌数组
     * 
     * @type {number[]}
     * @memberof MJ_Play
     */
    _swapCardList: number[] = [];

    /**
     * 手牌列表脚本
     * 
     * @type {MJ_HandList}
     * @memberof MJ_Play
     */
    _handList = null;

    onLoad() {
        dd.gm_manager._minScript = this;
        this.getHandTarget();
    }

    /**
     * 初始化界面
     * 
     * @memberof MJ_Play
     */
    initData() {
        this._swapCardList.length = 0;

        this.btn_swap.interactable = false;
        this.lblTip.node.active = false;

        this.btn_swap.node.active = false;
        this.node_state.active = false;
        this.node_lack.active = false;
    }
    /**
     * 获取手牌的脚本
     * @memberof MJ_Play
     */
    getHandTarget() {
        if (dd.gm_manager && dd.gm_manager.mjGameData) {
            switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
                case MJ_Game_Type.GAME_TYPE_MYMJ:
                    this._handList = this.node_mine_card.getComponent('MYMJ_HandList');
                    break;
                case MJ_Game_Type.GAME_TYPE_LSMJ:
                    this._handList = this.node_mine_card.getComponent('LSMJ_HandList');
                    break;
                case MJ_Game_Type.GAME_TYPE_NCMJ:
                    this._handList = this.node_mine_card.getComponent('NCMJ_HandList');
                    break;
                default:
                    this._handList = this.node_mine_card.getComponent('MJ_HandList');
                    break;
            }
        }

        //如果获取不到的话
        if (!this._handList) {
            let sceneName = cc.director.getScene().name;
            switch (name) {
                case 'MYMJScene':
                    this._handList = this.node_mine_card.getComponent('MYMJ_HandList');
                    break;
                case 'LSMJScene':
                    this._handList = this.node_mine_card.getComponent('LSMJ_HandList');
                    break;
                case 'NCMJScene':
                    this._handList = this.node_mine_card.getComponent('NCMJ_HandList');
                    break;
                default:
                    this._handList = this.node_mine_card.getComponent('MJ_HandList');
                    break;
            }
        }
    }
    /**
     * 刷新打牌的信息
     * 
     * @memberof MJ_Play
     */
    updatePlay(seatInfo: SeatVo) {
        if (!this._handList) {
            this.getHandTarget();
        }
        this._seatInfo = seatInfo;

        //如果在定缺阶段之前，排序就是 万筒条
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState < MJ_GameState.STATE_TABLE_DINGQUE) {
            this._seatInfo.handCards = dd.gm_manager.getSortCardByCardIds(this._seatInfo.handCards);
        } else if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_DINGQUE) {
            //如果在定缺阶段，未定缺时排序 万筒条 ，已定缺时排序 打缺的牌在最右边
            if (this._seatInfo.btState !== MJ_Act_State.ACT_STATE_WAIT) {
                this._seatInfo.handCards = dd.gm_manager.getSortCardByCardIds(this._seatInfo.handCards, this._seatInfo.unSuit);
                for (var i = 0; i < this.node_dq_list.length; i++) {
                    this.node_dq_list[i].scale = 1;
                    this.node_dq_list[i].removeAllChildren();
                    this.node_dq_list[i].stopAllActions();
                }
            } else {
                this._seatInfo.handCards = dd.gm_manager.getSortCardByCardIds(this._seatInfo.handCards);
                this.showDQAction();
            }
        } else if (dd.gm_manager.mjGameData.tableBaseVo.gameState > MJ_GameState.STATE_TABLE_DINGQUE) {
            //如果大于了定缺阶段，都需要先打定缺的牌
            this._seatInfo.handCards = dd.gm_manager.getSortCardByCardIds(this._seatInfo.handCards, this._seatInfo.unSuit);
        } else {
        }

        //如果在准备阶段，就初始化数据
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState <= MJ_GameState.STATE_TABLE_READY) {
            this.initData();
        }

        //如果在小于定缺阶段
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState <= MJ_GameState.STATE_TABLE_DINGQUE) {
            if (this._seatInfo.swapCards) {
                this._swapCardList = this._seatInfo.swapCards;
            }
        }

        //刷新手牌
        this._handList.updateHandList(this._seatInfo, this);
        this.showPlayState();
    }

    /**
     * 取消选中牌
     * 
     * @memberof MJ_Play
     */
    unSelectCard() {
        this._handList.selectCardByCardId(-1);
    }

    /**
     * 选中换三张的牌
     * 
     * @param {number} cardId 
     * @param {number} isSelect -1否 1是
     * @returns {boolean} 
     * @memberof MJ_Play
     */
    selectSwapCard(cardId: number, isSelect: number): boolean {
        if (isSelect < 0) {//是否选中
            //从数组中减去
            let sIndex = this._swapCardList.indexOf(cardId);
            if (sIndex !== -1) {
                this._swapCardList.splice(sIndex, 1);
            }
            if (this._swapCardList.length < 3) {
                this.btn_swap.interactable = false;
            }
        } else {
            if (this._swapCardList.length < 3) {
                //选出这张牌
                this._swapCardList.push(cardId);
                if (this._swapCardList.length === 3) {
                    this.btn_swap.interactable = true;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 座位状态的显示
     * 
     * @memberof MJ_Play
     */
    showPlayState() {
        switch (dd.gm_manager.mjGameData.tableBaseVo.gameState) {
            case MJ_GameState.STATE_TABLE_SWAPCARD: {//游戏换牌状态
                this.node_state.active = false;
                this.node_lack.active = false;

                this.lblTip.node.active = true;
                if (this._seatInfo.btState === 0) {
                    this.lblTip.string = '请选择花色一样的三张牌交换...';
                    this.btn_swap.node.active = true;
                    if (this._swapCardList.length === 3) {
                        this.btn_swap.interactable = true;
                    } else {
                        this.btn_swap.interactable = false;
                    }
                } else {
                    this.lblTip.string = '已选牌...';
                    this.btn_swap.node.active = false;
                }
                break;
            }
            case MJ_GameState.STATE_TABLE_DINGQUE: {//游戏定缺状态
                this.node_state.active = false;
                this.btn_swap.node.active = false;

                this.lblTip.node.active = true;
                if (this._seatInfo.btState === 0) {
                    this.lblTip.string = '请选择一门花色定缺...';
                    this.node_lack.active = true;
                } else {
                    this.lblTip.string = '已定缺...';
                    this.node_lack.active = false;
                }
                break;
            }
            case MJ_GameState.STATE_TABLE_BREAKCARD: {//杠碰吃胡牌表态状态
                this.lblTip.node.active = false;
                this.showBreakState();
                break;
            }
            case MJ_GameState.STATE_TABLE_PIAOPAI: {//飘表态状态
                this.lblTip.node.active = true;
                if (this._seatInfo.piaoNum === 0) {
                    this.lblTip.string = '请选择飘...';
                } else {
                    this.lblTip.string = '已选飘...';
                }
                break;
            }
            default:
                this.lblTip.node.active = false;
                this.node_state.active = false;
                this.node_lack.active = false;
                break;
        }
        //绵阳麻将 == 如果不为躺阶段，就不显示躺节点
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState !== MJ_GameState.STATE_TABLE_BREAKCARD) {
            this.unShowTang();
        }
        //自贡麻将 == 显示报叫节点
        this.showBaoJiao();
        //南充麻将 == 显示飘节点
        this.showPiaoNode();
    }

    /**
     * 显示胡杠碰吃过 状态
     * 
     * @param {MJ_Act_Type} breakState 
     * @memberof MJ_Play
     */
    showBreakState() {
        //如果自己有（胡杠碰吃过的状态）
        let isBreakCS = this.getIsBreakCardState();
        if (isBreakCS) {
            //如果自己在(等待表态)
            if (this._seatInfo.btState === MJ_Act_State.ACT_STATE_WAIT) {
                //正在显示绵阳麻将的躺界面
                let isTang = this.getIsShowHui();
                if (!isTang) {
                    this.node_state.active = true;
                    for (var i = 0; i < this._seatInfo.breakCardState.length; i++) {
                        if (this.node_state_list[i]) {
                            if (this._seatInfo.breakCardState[i] === 1) {
                                this.node_state_list[i].active = true;
                            } else {
                                this.node_state_list[i].active = false;
                            }
                        }
                    }

                }
                switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
                    case MJ_Game_Type.GAME_TYPE_MYMJ:
                        //绵阳麻将 如果该玩家已经躺了，并且有 胡的状态,那么不显示 胡按钮
                        if (this._seatInfo.tangCardState === 1 && this._seatInfo.breakCardState[0] === 1) {
                            this.node_state.active = false;
                        }
                        break;
                    case MJ_Game_Type.GAME_TYPE_NCMJ:
                        //南充麻将 如果有 胡的状态,那么不显示 胡按钮
                        if (this._seatInfo.breakCardState[0] === 1) {
                            //如果该玩家已经躺了
                            if (this._seatInfo.tangCardState === 1) {
                                this.node_state.active = false;
                            } else {
                                //如果桌子上的牌少于12张，也不显示 胡按钮
                                if (dd.gm_manager.mjGameData.tableBaseVo.handCardNum < 12) {
                                    this.node_state.active = false;
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
                this.node_state.active = false;
            }
        } else {
            this.node_state.active = false;
        }
    }

    /**
     * 获取是否在胡杠碰的状态
     * 
     * @returns 
     * @memberof MJ_Play
     */
    getIsBreakCardState() {
        let isBreakCS = false;
        for (var i = 0; i < this._seatInfo.breakCardState.length; i++) {
            if (this._seatInfo.breakCardState[i] === 1) {
                isBreakCS = true;
                break;
            }
        }
        return isBreakCS;
    }

    /**
     * 自己定缺按钮的点击事件
     * 
     * @memberof MJ_Play
     */
    click_btn_lack(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0': {//万
                cc.log('万');
                dd.gm_manager.getGMTarget().sendDinQue(MJ_Suit.SUIT_TYPE_WAN);
                break;
            }
            case '1': {//筒
                cc.log('筒');
                dd.gm_manager.getGMTarget().sendDinQue(MJ_Suit.SUIT_TYPE_TONG);
                break;
            }
            case '2': {//条
                cc.log('条');
                dd.gm_manager.getGMTarget().sendDinQue(MJ_Suit.SUIT_TYPE_TIAO);
                break;
            }
            default:
                break;
        }
        this.unSelectCard();
    }

    /**
     * 
     * 换三张
     * @memberof MJ_Play
     */
    click_btn_swap() {
        dd.mp_manager.playButton();
        if (this._swapCardList.length !== 3) {
            dd.ui_manager.showTip('请选择花色一样的三张牌交换');
            return;
        }
        let isAllSame = true;
        let card1 = Math.floor(this._swapCardList[0] / 100);
        let card2 = Math.floor(this._swapCardList[1] / 100);
        let card3 = Math.floor(this._swapCardList[2] / 100);
        if (card1 === card2 && card2 === card3) {
            dd.gm_manager.getGMTarget().sendSwap(this._swapCardList);
        } else {
            dd.ui_manager.showTip('请选择花色一样的三张牌交换');
        }
    }

    /**
     * 胡杠碰过的表态
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof MJ_Play
     */
    click_btn_state(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0': {//胡
                this.dealBreakCard(MJ_Act_Type.ACT_INDEX_HU, this._seatInfo.breakCard);
                break;
            }
            case '1': {//杠
                this.dealGangBeakCard();
                break;
            }
            case '2': {//碰
                this.dealBreakCard(MJ_Act_Type.ACT_INDEX_PENG, this._seatInfo.breakCard);
                break;
            } case '3': {//吃
                this.dealBreakCard(MJ_Act_Type.ACT_INDEX_CHI, this._seatInfo.breakCard);
                break;
            } case '4': {//过
                this.dealBreakCard(MJ_Act_Type.ACT_INDEX_DROP, this._seatInfo.breakCard);
                break;
            } case '5': {//绵阳麻将 躺
                this.showHuiBtn();
                break;
            }
            default:
                break;
        }
    }
    /**
     * 处理胡碰杠吃过
     * @param {MJ_Act_Type} actType 
     * @param {number} cardId 
     * @memberof MJ_Play
     */
    dealBreakCard(actType: MJ_Act_Type, cardId: number) {
        switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
            case MJ_Game_Type.GAME_TYPE_LSMJ://乐山麻将
                let obj: GangData = {
                    cardId: cardId,
                    isAnGang: 1,
                    isUseYaoJi: false,
                };
                dd.gm_manager.getGMTarget().sendLSGangBreakCard(actType, obj, this.node_state);
                break;
            default:
                dd.gm_manager.getGMTarget().sendOtherBreakCard(actType, cardId, this.node_state);
                break;
        }
    }
    /**
     * 处理杠牌
     * @memberof MJ_Play
     */
    dealGangBeakCard() {
        switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
            case MJ_Game_Type.GAME_TYPE_MYMJ://绵阳麻将
                //绵阳麻将，如果已经躺牌的话,就只能杠摸的那张牌
                if (this._seatInfo.tangCardState === 1) {
                    dd.gm_manager.getGMTarget().sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, this._seatInfo.breakCard, this.node_state);
                    break;
                }
                this.baseGang();
                break;
            case MJ_Game_Type.GAME_TYPE_LSMJ://乐山麻将
                this.lsGang();
                break;
            default:
                this.baseGang();
                break;
        }
    }

    /**
     * 基本的杠牌
     * @memberof MJ_Play
     */
    baseGang() {
        //如果表态是自己,
        if (this._seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
            let list = this._seatInfo.handCards;
            //如果(摸牌)存在，并且是(自己摸牌)，就要把摸得牌算进去
            if (this._seatInfo.moPaiCard && dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex) {
                list = list.concat(this._seatInfo.moPaiCard);
            }
            let gList: GangData[] = dd.gm_manager.getGangList(list, this._seatInfo);
            if (gList.length > 0) {//如果自己有杠牌,表示是自己 摸牌摸到的杠(摸到的暗杠 + 摸到的巴杠)
                //如果自己手中有多张杠牌,就显示 选择杠牌界面
                if (gList.length > 1) {
                    dd.gm_manager.getGMTarget().showMoreGang(gList, this);
                    this.node_state.active = false;
                } else {//如果只有一个杠，就直接杠这张牌
                    dd.gm_manager.getGMTarget().sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, gList[0].cardId, this.node_state);
                }
            } else {//如果自己手中没有杠，就直接 杠别人打出来的那张牌
                dd.gm_manager.getGMTarget().sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, this._seatInfo.breakCard, this.node_state);
            }
        } else {//如果没有轮到自己摸牌时的表态，是杠别人打出来的那张牌
            dd.gm_manager.getGMTarget().sendOtherBreakCard(MJ_Act_Type.ACT_INDEX_GANG, this._seatInfo.breakCard, this.node_state);
        }
    }
    /**
     * 乐山麻将的杠牌
     * @memberof MJ_Play
     */
    lsGang() {
        //如果表态是自己,
        if (this._seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
            let list = this._seatInfo.handCards;
            //如果(摸牌)存在，并且是(自己摸牌)，就要把摸得牌算进去
            if (this._seatInfo.moPaiCard && dd.gm_manager.mjGameData.tableBaseVo.btIndex === this._seatInfo.seatIndex) {
                list = list.concat(this._seatInfo.moPaiCard);
            }
            let gList = dd.gm_manager.getLSGangList(list, this._seatInfo);
            if (gList.length > 0) {//如果自己有杠牌,表示是自己 摸牌摸到的杠(摸到的暗杠 + 摸到的巴杠)
                //如果自己手中有多张杠牌,就显示 选择杠牌界面
                if (gList.length > 1) {
                    dd.gm_manager.getGMTarget().showMoreGang(gList, this);
                    this.node_state.active = false;
                } else {//如果只有一个杠，就直接杠这张牌
                    dd.gm_manager.getGMTarget().sendLSGangBreakCard(MJ_Act_Type.ACT_INDEX_GANG, gList[0], this.node_state);
                }
            } else {//如果自己手中没有杠，就直接 杠摸的那张牌
                let obj: GangData = {
                    cardId: this._seatInfo.breakCard,
                    isAnGang: 2,
                    isUseYaoJi: false,
                };
                dd.gm_manager.getGMTarget().sendLSGangBreakCard(MJ_Act_Type.ACT_INDEX_GANG, obj, this.node_state);
            }
        } else {//如果没有轮到自己摸牌时的表态，是杠别人打出来的那张牌
            let obj: GangData = {
                cardId: this._seatInfo.breakCard,
                isAnGang: 1,
                isUseYaoJi: false,
            };
            dd.gm_manager.getGMTarget().sendLSGangBreakCard(MJ_Act_Type.ACT_INDEX_GANG, obj, this.node_state);
        }
    }
    /**
     * 显示推荐定缺的动作
     * 
     * @memberof MJ_Play
     */
    showDQAction() {
        let type = dd.gm_manager.getDingQueSuit(this._seatInfo.handCards);
        if (type !== -1) {
            let node_dq = this.node_dq_list[type];
            node_dq.stopAllActions();
            let seq = cc.sequence(cc.scaleTo(0.4, 0.8), cc.scaleTo(0.4, 1).easing(cc.easeElasticOut(0.4)));
            let action = cc.repeatForever(seq);
            node_dq.runAction(action);

            let node_tj = new cc.Node('tj');
            let sp = node_tj.addComponent(cc.Sprite);
            sp.spriteFrame = this.img_tj;
            node_tj.setPosition(cc.p(30, 30));
            node_tj.parent = node_dq;
        }
    }

    /**
     * 绵阳麻将的躺 和 悔 的按钮事件
     * @memberof MJ_Play
     */
    click_btn_hui(event, type: string) {
        dd.mp_manager.playButton();
        this.node_state.active = true;
        this._handList.showHandCardsByTang(false);
        let btn_hui = this.node.getChildByName('btn_hui');
        if (btn_hui) btn_hui.active = false;
    }
    /**
     * 显示绵阳麻将躺节点,进行选躺牌
     * @param {boolean} isShow  是否显示躺节点
     * 
     * @memberof MJ_Play
     */
    showTangNode(isShow: boolean, tangObj: TangCfg = null) {
        let canvas = dd.ui_manager.getCanvasNode();
        let node_tang = canvas.getChildByName('Tang');
        if (node_tang) {
            node_tang.active = isShow;
            if (isShow) {
                let mjTang = node_tang.getComponent('MJ_Tang');
                mjTang.initData(tangObj, (flag) => {
                    if (this.node_state && this.node_state.isValid) {
                        if (flag === 0) {
                            this.node_state.active = false;
                        } else {
                            this.node_state.active = true;
                        }
                    }
                });
            }
        }
    }
    /**
     * 不显示躺
     * @memberof MJ_Play
     */
    unShowTang() {
        let canvas = dd.ui_manager.getCanvasNode();
        let node_tang = canvas.getChildByName('Tang');
        if (node_tang) node_tang.active = false;
        let btn_hui = this.node.getChildByName('btn_hui');
        if (btn_hui) btn_hui.active = false;
    }
    /**
     * 点击躺之后，显示悔按钮
     * @memberof MJ_Play
     */
    showHuiBtn() {
        //悔状态下显示手牌
        this._handList.showHandCardsByTang(true);
        this.node_state.active = false;
        let btn_hui = this.node.getChildByName('btn_hui');
        if (btn_hui) btn_hui.active = true;
    }
    /**
     * 获取是否显示悔按钮
     * @memberof MJ_Play
     */
    getIsShowHui() {
        let btn_hui = this.node.getChildByName('btn_hui');
        if (btn_hui && btn_hui.active) return true;
        return false;
    }

    /**
     * 胡杠碰过的表态
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof MJ_Play
     */
    click_btn_baojiao(event, type: string) {
        dd.mp_manager.playButton();
        let node_baojiao = this.node.getChildByName('node_baojiao');
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'btVal': type,
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_BAOJIAO_BT, msg, (flag: number, content?: any) => {
            if (node_baojiao && node_baojiao.isValid) {
                if (flag === 0) {
                    node_baojiao.active = false;
                } else {
                    node_baojiao.active = true;
                }
            }
        });
        if (node_baojiao && node_baojiao.isValid) node_baojiao.active = false;
    }
    /**
     * 自贡麻将 === 显示报叫
     * @memberof MJ_Play
     */
    showBaoJiao() {
        let node_baojiao = this.node.getChildByName('node_baojiao');
        if (node_baojiao && this._seatInfo) {
            if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_BAOJIAO
                && this._seatInfo.baojiaoState === 0) {
                node_baojiao.active = true;
            } else {
                node_baojiao.active = false;
            }
        }
    }
    /**
     * 南充麻将飘按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof MJ_Play
     */
    click_btn_piao(event, type: string) {
        dd.mp_manager.playButton();
        let node_piao = this.node.getChildByName('node_piao');
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'btVal': type,
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_NCMJ_PIAOPAI_BT, msg, (flag: number, content?: any) => {
            if (node_piao && node_piao.isValid) {
                if (flag === 0) {
                    node_piao.active = false;
                } else {
                    node_piao.active = true;
                }
            }
        });
        if (node_piao && node_piao.isValid) node_piao.active = false;
    }
    /**
     * 南充麻将 === 显示飘
     * @memberof MJ_Play
     */
    showPiaoNode() {
        let node_piao = this.node.getChildByName('node_piao');
        if (node_piao && this._seatInfo) {
            //如果在飘的阶段，并且还未飘
            if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_PIAOPAI
                && this._seatInfo.btState === MJ_Act_State.ACT_STATE_WAIT) {
                node_piao.active = true;
                for (let i = 1; i < 6; i++) {
                    let btn_piao = node_piao.getChildByName('btn_piao' + i);
                    if (btn_piao) {
                        if (i <= dd.gm_manager.mjGameData.tableBaseVo.maxPiaoPaiNum) {
                            btn_piao.active = true;
                        } else {
                            btn_piao.active = false;
                        }
                    }
                }
            } else {
                node_piao.active = false;
            }
        }
    }
}


