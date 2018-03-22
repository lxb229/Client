const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import MJ_Game_Mine from './MJ_Game_Mine';
import MJ_Game_Others from './MJ_Game_Others';
import MJ_Card from './MJ_Card';
import MJ_Card_Group from './MJ_Card_Group';
import { MJ_GameState, MJ_Act_State, MJ_Act_Type, MJ_Game_Type } from '../../Modules/Protocol';

declare interface CardConfig {
    /**
     * 每行牌的数量
     * @type {number}
     * @memberof CardConfig
     */
    row: number,
    /**
     * 牌的缩放比例
     * @type {number}
     * @memberof CardConfig
     */
    cScale: number,
    /**
     * 自己的牌的xy间隔
     * @type {cc.Vec2}
     * @memberof CardConfig
     */
    minSpacing: cc.Vec2,
    /**
     * 右边的牌的xy间隔
     * @type {cc.Vec2}
     * @memberof CardConfig
     */
    rightSpacing: cc.Vec2
}

@ccclass
export default class MJ_Game extends cc.Component {

    /**
     * 表态时间
     * 
     * @type {cc.Label}
     * @memberof MJ_Game
     */
    @property(cc.Label)
    lblTime: cc.Label = null;

    /**
     * 游戏数据
     * 
     * @type {cc.RichText}
     * @memberof MJ_Game
     */
    @property(cc.RichText)
    lblGameInfo: cc.RichText = null;
    /**
     * 轮到该谁表态的节点列表
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Game
     */
    @property([cc.Node])
    node_state_list: cc.Node[] = [];

    /**
     *  玩家座位索引(对应的玩家节点列表的方向)  0自己 3左边 2上边 1右边
     * @type {number[]}
     * @memberof MJ_Game
     */
    @property({
        type: [cc.Integer],
        tooltip: '玩家座位索引(对应的玩家节点列表的方向)，\n0自己\n 1右边\n 2上边\n 3左边'
    })
    sId_list: number[] = [];
    /**
     * 玩家打牌的节点列表
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Game
     */
    @property([cc.Node])
    node_player_list: cc.Node[] = [];
    /**
     * 杠、碰的父节点容器
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_group_list: cc.Node[] = [];
    /**
     * 躺牌的父节点
     * @type {cc.Node[]}
     * @memberof MJ_Game
     */
    @property(cc.Node)
    node_tang_list: cc.Node[] = [];
    /**
     * 打出的牌的父节点容器
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_playOut_list: cc.Node[] = [];

    /**
     * 动作、文字显示的父节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_act_list: cc.Node[] = [];

    /**
     * 胡牌的节点(牌的父节点)
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_hu_list: cc.Node[] = [];

    /**
     * 胡牌文字图片的父节点容器
     * 
     * @type {cc.Node}
     * @memberof MJ_Play
     */
    @property(cc.Node)
    node_img_hu_list: cc.Node[] = [];

    /**
     * 
     * 当前时间
     * @type {string}
     * @memberof MJ_Table
     */
    _nowTime: number = 0;

    /**
     * 刷新时间
     * 
     * @type {number}
     * @memberof MJ_Table
     */
    _cdTime: number = 0;

    onLoad() {
    }

    /**
     * 初始化界面
     * 
     * @memberof MJ_Play
     */
    initData() {
        for (var i = 0; i < this.node_img_hu_list.length; i++) {
            this.node_img_hu_list[i].active = false;
        }
        for (var i = 0; i < this.node_group_list.length; i++) {
            this.node_group_list[i].removeAllChildren();
        }
        for (var i = 0; i < this.node_tang_list.length; i++) {
            this.node_tang_list[i].removeAllChildren();
        }
        for (var i = 0; i < this.node_act_list.length; i++) {
            this.node_act_list[i].removeAllChildren();
        }
        for (var i = 0; i < this.node_playOut_list.length; i++) {
            this.node_playOut_list[i].removeAllChildren();
        }
        for (var i = 0; i < this.node_hu_list.length; i++) {
            this.node_hu_list[i].removeAllChildren();
        }
    }

    /**
 * 显示游戏桌子信息
 * 
 * @memberof MJ_Game
 */
    showTableState() {
        this._nowTime = dd.gm_manager.getDiffTime(dd.gm_manager.mjGameData.tableBaseVo.svrTime, dd.gm_manager.mjGameData.tableBaseVo.actTime);
        this.lblTime.string = this._nowTime + '';
        let pIndex = -1;
        //大于定缺的时候，显示游戏信息
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState > MJ_GameState.STATE_TABLE_DINGQUE
            && dd.gm_manager.mjGameData.tableBaseVo.gameState !== MJ_GameState.STATE_TABLE_PIAOPAI
            && dd.gm_manager.mjGameData.tableBaseVo.gameState !== MJ_GameState.STATE_TABLE_BAOJIAO) {
            this.lblGameInfo.node.parent.active = true;
            this.lblGameInfo.string = '<color=#4ecab1>剩余 </c><color=#ffc600>' + dd.gm_manager.mjGameData.tableBaseVo.tableCardNum
                + '</c><color=#4ecab1> 张</c><color=#4ecab1>   第 </c><color=#ffc600>'
                + dd.gm_manager.mjGameData.tableBaseVo.currGameNum + '/' + dd.gm_manager.mjGameData.tableBaseVo.maxGameNum + '</c><color=#4ecab1> 局</c>';
            pIndex = dd.gm_manager.getIndexBySeatId(dd.gm_manager.mjGameData.tableBaseVo.btIndex);
        } else {
            this.lblGameInfo.node.parent.active = false;
        }
        for (var i = 0; i < this.node_state_list.length; i++) {
            if (pIndex === i) {
                this.node_state_list[i].active = true;
            } else {
                this.node_state_list[i].active = false;
            }
        }
    }

    update(dt) {
        if (dd.gm_manager && !dd.gm_manager.isReplayPause) {
            if (this._nowTime > 0) {
                this._cdTime -= dt;
                if (this._cdTime < 0) {
                    this._cdTime = 1;
                    this._nowTime--;
                    this.lblTime.string = this._nowTime + '';
                    //如果小于3s，每一秒都要振动一下
                    if (this._nowTime <= 2) {
                        this.showPhoneVibration();
                    }
                }
            }
        }
    }
    /**
     * 显示手机振动
     * 
     * @memberof MJ_Game
     */
    showPhoneVibration() {
        if (dd.gm_manager && dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.tableBaseVo) {
            let gameState = dd.gm_manager.mjGameData.tableBaseVo.gameState;
            if (MJ_GameState.STATE_TABLE_OVER_ALL !== gameState && MJ_GameState.STATE_TABLE_OVER_ONCE !== gameState
                && MJ_GameState.STATE_TABLE_DESTORY !== gameState && MJ_GameState.STATE_TABLE_READY !== gameState) {
                let mySeatInfo = dd.gm_manager.getSeatById(dd.ud_manager.mineData.accountId);
                if (mySeatInfo) {
                    //如果为胡碰杠阶段，就判断是否是自己胡碰杠的表态
                    if (MJ_GameState.STATE_TABLE_FAPAI === gameState) {
                        dd.js_call_native.phoneVibration();
                    } else if (MJ_GameState.STATE_TABLE_BREAKCARD === gameState) {
                        let isBreakCS = false;
                        for (var i = 0; i < mySeatInfo.breakCardState.length; i++) {
                            if (mySeatInfo.breakCardState[i] === 1) {
                                isBreakCS = true;
                                break;
                            }
                        }
                        if (isBreakCS) {
                            dd.js_call_native.phoneVibration();
                        }
                    } else {
                        //如果不为胡碰杠阶段，就判断是否是自己打牌表态
                        if (mySeatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex) {
                            dd.js_call_native.phoneVibration();
                        }
                    }
                }
            }
        }
    }

    /**
     * 显示游戏信息
     * 
     * @memberof MJ_Game
     */
    showGameInfo() {
        //如果在准备阶段，就初始化数据
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState <= MJ_GameState.STATE_TABLE_READY) {
            this.initData();
        }
        this.showTableState();
        this.showPlayCard();
    }

    /**
     * 显示打牌信息
     * 
     * @memberof MJ_Game
     */
    showPlayCard() {
        for (var i = 0; i < this.node_player_list.length; i++) {
            let seatInfo = dd.gm_manager.mjGameData.seats[i];
            if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '' && seatInfo.accountId !== '0') {
                if (i === 0) {
                    let mjMine: MJ_Game_Mine = this.node_player_list[i].getComponent('MJ_Game_Mine');
                    mjMine.updatePlay(seatInfo);
                } else {
                    let mjOthers: MJ_Game_Others = this.node_player_list[i].getComponent('MJ_Game_Others');
                    mjOthers.updatePlay(seatInfo);
                }
                this.showGroupCard(i, seatInfo);
                this.shwoTangCard(i, seatInfo);
                this.showSeatPlayOutCard(i, seatInfo);
                this.showPlayOutAct(i, seatInfo);
                this.showHuPai(i, seatInfo);
            }
        }
    }

    /**
     * 显示桌子上已经打出的牌中，是否有选中的这张牌
     * 
     * @memberof MJ_Game
     */
    showTableSelectCard(cardId: number) {
        let node_out_list = [];
        for (let sId = 0; sId < this.node_playOut_list.length; sId++) {
            let node_playOut = this.node_playOut_list[sId];
            if (node_playOut) {
                node_out_list = node_out_list.concat(node_playOut.children);
            }
        }
        let card = Math.floor(cardId / 10);
        for (var i = 0; i < node_out_list.length; i++) {
            let outCard: cc.Node = node_out_list[i];
            let oCard = Math.floor(outCard.tag / 10);
            let ocs = outCard.getComponent('MJ_Card');
            if (oCard === card) {
                ocs.showLight(true);
            } else {
                ocs.showLight(false);
            }
        }
    }
    /**
     * 显示绵阳麻将每个玩家的躺牌
     * @param {number} index 座位索引
     * @param {SeatVo} seatInfo 座位信息
     * @memberof MJ_Game
     */
    shwoTangCard(index: number, seatInfo: SeatVo) {
        let node_tang = this.node_tang_list[index];
        if (node_tang) {
            if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '0'
                && seatInfo.tangCardList && seatInfo.tangCardList.length > 0) {
                node_tang.active = true;
                let sId = this.sId_list[index];
                node_tang.removeAllChildren();

                let mymjScript = dd.ui_manager.getCanvasNode().getComponent('MYMJScene');
                let tangCardList = seatInfo.tangCardList;
                if (sId === 3) {//you边
                    tangCardList.sort((a, b) => {
                        return a - b;
                    });
                }
                //显示躺牌
                for (let i = 0; i < tangCardList.length; i++) {
                    mymjScript.showTangCard(sId, tangCardList[i], node_tang);
                }
            } else {
                node_tang.active = false;
            }
        }
    }

    /**
     * 显示每个玩家的碰 、 杠牌
     * @param {number} index 座位索引
     * @param {SeatVo} seatInfo 座位信息
     * @memberof MJ_Play
     */
    showGroupCard(index: number, seatInfo: SeatVo) {
        let node_group = this.node_group_list[index];
        if (node_group) {
            if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '' && seatInfo.accountId !== '0') {
                let sId = this.sId_list[index];
                //显示碰杠牌
                node_group.removeAllChildren();
                if (seatInfo.pengCards) {
                    for (var i = 0; i < seatInfo.pengCards.length; i++) {
                        dd.gm_manager._gmScript.showGroupCard(0, seatInfo.pengCards[i], sId, node_group);
                    }
                }
                if (seatInfo.baGangCards) {
                    for (var i = 0; i < seatInfo.baGangCards.length; i++) {
                        dd.gm_manager._gmScript.showGroupCard(1, seatInfo.baGangCards[i], sId, node_group);
                    }
                }
                if (seatInfo.anGangCards) {
                    for (var i = 0; i < seatInfo.anGangCards.length; i++) {
                        dd.gm_manager._gmScript.showGroupCard(2, seatInfo.anGangCards[i], sId, node_group);
                    }
                }
                if (seatInfo.dianGangCards) {
                    for (var i = 0; i < seatInfo.dianGangCards.length; i++) {
                        dd.gm_manager._gmScript.showGroupCard(3, seatInfo.dianGangCards[i], sId, node_group);
                    }
                }
            }
        }
    }

    /**
     * 创建打出的牌
     * @param {number} index 
     * @param {cc.Node} node_playOut 
     * @param {number} sId 
     * @param {SeatVo} seatInfo 
     * @param {CardConfig} cardConfig 牌节点的配置
     * @memberof MJ_Game
     */
    createOutCard(index: number, node_playOut: cc.Node, sId: number, seatInfo: SeatVo, cardConfig: CardConfig) {
        dd.gm_manager._gmScript.showPlayOutCard(sId, seatInfo.outUnUseCards[index], node_playOut, (cardNode: cc.Node) => {
            let cSize = cc.v2(cardNode.width * cardConfig.cScale, cardNode.height * cardConfig.cScale);
            cardNode.width = cardNode.width * cardConfig.cScale;
            cardNode.height = cardNode.height * cardConfig.cScale;
            if (sId === 0) {
                let px = index % cardConfig.row * (cardNode.width + cardConfig.minSpacing.x) + cardNode.width / 2;
                let py = Math.floor(index / cardConfig.row) * (cardNode.height + cardConfig.minSpacing.y) + cardNode.height / 2;
                cardNode.setPosition(cc.p(px, py));
                cardNode.zIndex = 99 - index;
            } else if (sId === 1) {
                let px = -Math.floor(index / cardConfig.row) * (cardNode.width + cardConfig.rightSpacing.x) - cardNode.width / 2;
                let py = index % cardConfig.row * (cardNode.height + cardConfig.rightSpacing.y) + cardNode.height / 2;
                cardNode.setPosition(cc.p(px, py));
                cardNode.zIndex = 99 - index;
            } else { }

            //如果刚刚是这个玩家表态，最后一张牌亮(显示刚刚打出的牌亮起来)
            if (seatInfo.outCard && index === seatInfo.outUnUseCards.length - 1
                && seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.prevBtIndex
                && !dd.gm_manager.mjGameData.breakSeats) {
                let ocs: MJ_Card = cardNode.getComponent('MJ_Card');
                switch (sId) {
                    case 0:
                        ocs.showBS(true, 1, -1);
                        break;
                    case 1:
                        ocs.showBS(true);
                        break;
                    case 2:
                        ocs.showBS(true, -1, 1);
                        break;
                    case 3:
                        ocs.showBS(true);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    /**
    * 显示打出的牌
    * @param {number} index 索引
    * @param {SeatVo} seatInfo 座位信息
    * @memberof MJ_Play
   */
    showSeatPlayOutCard(index: number, seatInfo: SeatVo) {
        let node_playOut = this.node_playOut_list[index];
        if (node_playOut) {
            if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '' && seatInfo.accountId !== '0') {
                let sId = this.sId_list[index];
                node_playOut.removeAllChildren();
                if (seatInfo.outUnUseCards) {
                    let cardConfig: CardConfig = {
                        row: 10,
                        cScale: 1,
                        minSpacing: cc.v2(0, -10),
                        rightSpacing: cc.v2(0, -11)
                    };
                    switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
                        case MJ_Game_Type.GAME_TYPE_XZDD://血战到底
                        case MJ_Game_Type.GAME_TYPE_MYMJ://绵阳麻将
                        case MJ_Game_Type.GAME_TYPE_ZGMJ://自贡麻将
                            cardConfig.row = 10;
                            cardConfig.cScale = 1;
                            cardConfig.minSpacing = cc.v2(0, -10);
                            cardConfig.rightSpacing = cc.v2(0, -11);
                            break;
                        case MJ_Game_Type.GAME_TYPE_SRLF://三人麻将（二方）
                        case MJ_Game_Type.GAME_TYPE_SRSF://三人麻将（三方）
                            cardConfig.row = 15;
                            cardConfig.cScale = 0.75;
                            cardConfig.minSpacing = cc.v2(2, -6);
                            cardConfig.rightSpacing = cc.v2(1, -8);
                            break;
                        case MJ_Game_Type.GAME_TYPE_LRLF://二人麻将（二方）
                            cardConfig.row = 16;
                            cardConfig.cScale = 1;
                            cardConfig.minSpacing = cc.v2(0, -11);
                            break;
                        default:
                    }
                    //刷新打出的牌数据
                    for (var i = 0; i < seatInfo.outUnUseCards.length; i++) {
                        this.createOutCard(i, node_playOut, sId, seatInfo, cardConfig);
                    }
                }
            }
        }
    }

    /**
     * 显示刚刚打出的牌的动作
     * @param {number} index 索引
     * @param {SeatVo} seatInfo 座位信息
     * @memberof MJ_Game
     */
    showPlayOutAct(index: number, seatInfo: SeatVo) {
        let node_act = this.node_act_list[index];
        if (node_act) {
            if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '' && seatInfo.accountId !== '0') {
                //轮到这个玩家在（出牌阶段）的（表态）时候，并且 这个玩家（已经表态）以及 存在(刚刚打出的牌)
                if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_OUTCARD) {
                    if (seatInfo.seatIndex === dd.gm_manager.mjGameData.tableBaseVo.btIndex
                        && seatInfo.btState === MJ_Act_State.ACT_STATE_BT
                        && seatInfo.outCard) {
                        dd.gm_manager._gmScript.showOutActMJ(seatInfo.outCard, node_act, seatInfo);
                        dd.mp_manager.playOut();
                    }
                }
            }
        }
    }

    /**
     * 显示胡牌
     * @param {number} index 座位索引
     * @param {SeatVo} seatInfo 座位信息
     * @memberof MJ_Play
     */
    showHuPai(index: number, seatInfo: SeatVo) {
        let node_hu = this.node_hu_list[index];
        let node_img_hu = this.node_img_hu_list[index];
        if (node_hu) {
            let sId = this.sId_list[index];
            node_hu.removeAllChildren();
            //如果玩家不在（未胡牌）的状态，表示胡牌了，并且 （胡牌）存在
            if (seatInfo.huPaiType !== 0 && seatInfo.huCards) {
                for (var i = 0; i < seatInfo.huCards.length; i++) {
                    dd.gm_manager._gmScript.showPlayOutCard(sId, seatInfo.huCards[i], node_hu, (cardNode: cc.Node) => {
                        let cs: MJ_Card = cardNode.getComponent('MJ_Card');
                        cs.showLight(true);
                    });
                }
                if (node_img_hu) {
                    //显示文字图片
                    node_img_hu.active = true;
                    node_img_hu.getComponent(cc.Sprite).spriteFrame = seatInfo.huPaiType === 1 ? dd.gm_manager._gmScript.mj_text_list[0] : dd.gm_manager._gmScript.mj_text_list[1];
                }
            } else {
                //不显示文字图片
                node_img_hu.active = false;
            }
        }
    }

    /**
     * 显示 胡杠碰状态的表态结果
     * 
     * @memberof MJ_Game
     */
    showBreakStates() {
        if (dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.breakSeats) {
            let breakSeats = dd.gm_manager.mjGameData.breakSeats;
            let playIndex = 0;
            //胡杠碰吃打断处理结果状态 breakState (0=胡,1=杠,2=碰,3=吃,4=过)
            for (var i = 0; i < breakSeats.length; i++) {
                //获取座位的索引
                let sId = dd.gm_manager.getIndexBySeatId(breakSeats[i]);
                if (sId !== -1 && this.node_player_list[sId]) {
                    //根据索引，获取座位信息
                    let seatInfo = dd.gm_manager.mjGameData.seats[sId];
                    if (seatInfo && seatInfo.accountId && seatInfo.accountId !== '' && seatInfo.accountId !== '0') {
                        this.showBreakStateAct(dd.gm_manager.mjGameData.breakState, sId, seatInfo, playIndex);
                        playIndex++;
                    }
                }
            }
        }
    }

    /**
     * 显示胡杠碰吃过 状态的动作
     * @param {MJ_Act_Type} breakState  (0=胡,1=杠,2=碰,3=吃,4=过)
     * @param {number} sId 座位索引
     * @param {SeatVo} seatInfo 座位信息
     * @param {number} playIndex 播放索引，只有等于0 的时候才播放音效，因为在循环中，多次调用回造成 1在播放-释放的时候 2调用播放，但是被1释放了，崩溃
     * @memberof MJ_Play
     */
    showBreakStateAct(breakState: MJ_Act_Type, sId: number, seatInfo: SeatVo, playIndex: number) {
        let node_act = this.node_act_list[sId];
        if (node_act) {
            let sf: cc.SpriteFrame = null;
            switch (breakState) {
                case MJ_Act_Type.ACT_INDEX_HU:
                    sf = this.getHuPaiSF(seatInfo);
                    if (playIndex === 0) {
                        if (seatInfo.huPaiType === 1) {
                            dd.mp_manager.playPokerSound(dd.mp_manager.audioSetting.language, 4, seatInfo.sex, 4);
                        } else {
                            dd.mp_manager.playPokerSound(dd.mp_manager.audioSetting.language, 4, seatInfo.sex, 1);
                        }
                    }
                    break;
                case MJ_Act_Type.ACT_INDEX_GANG:
                    sf = dd.gm_manager._gmScript.mj_text_list[7];
                    if (playIndex === 0) {
                        dd.mp_manager.playPokerSound(dd.mp_manager.audioSetting.language, 4, seatInfo.sex, 2);
                    }
                    switch (seatInfo.gangType) {
                        case 1://自摸巴杠
                            dd.gm_manager._gmScript.showGFAct(node_act);
                            break;
                        case 2://暗杠
                            dd.gm_manager._gmScript.showXYAct(node_act);
                            break;
                        case 3://点杠
                            dd.gm_manager._gmScript.showXYAct(node_act);
                            break;
                        default:
                            break;
                    }
                    break;
                case MJ_Act_Type.ACT_INDEX_PENG:
                    sf = dd.gm_manager._gmScript.mj_text_list[6];
                    if (playIndex === 0) {
                        dd.mp_manager.playPokerSound(dd.mp_manager.audioSetting.language, 4, seatInfo.sex, 3);
                    }
                    break;
                case MJ_Act_Type.ACT_INDEX_TANG:
                    dd.mp_manager.playTangSound(seatInfo.sex);
                    break;
                default:
                    break;
            }
            dd.gm_manager._gmScript.showTxtAct(sf, node_act);
        }
    }

    /**
     * 获取胡牌类型的图片
     * 
     * @returns 
     * @param {SeatVo} seatInfo 座位信息
     * @memberof MJ_Play
     */
    getHuPaiSF(seatInfo: SeatVo): cc.SpriteFrame {
        //'0=自摸\n 1=胡\n 2=天胡\n 3=地胡\n 4=杠上花\n 5=杠上炮\n 6=碰\n 7=杠\n 8=抢杠\n 9=点炮\n 10=一炮多响\n 11=海底捞月\n 12=呼叫转移\n''13=流局\n 14=游戏结束\n'
        //胡牌方式(0=未胡牌,1=自摸 ,2=点炮,3=抢杠胡,4=自摸杠上花,5=点杠上花胡,6=点杠上炮,7=查叫) 
        let huPaiSF = null;
        if (dd.gm_manager.mjGameData.breakSeats.length > 1) {
            huPaiSF = dd.gm_manager._gmScript.mj_text_list[10];
        } else {
            switch (seatInfo.huPaiType) {
                case 0:
                    huPaiSF = null;
                    break;
                case 1:
                    huPaiSF = dd.gm_manager._gmScript.mj_text_list[0];
                    break;
                case 2:
                    huPaiSF = dd.gm_manager._gmScript.mj_text_list[9];
                    break;
                case 3:
                    huPaiSF = dd.gm_manager._gmScript.mj_text_list[8];
                    break;
                case 4:
                    huPaiSF = dd.gm_manager._gmScript.mj_text_list[4];
                    break;
                case 5:
                    huPaiSF = dd.gm_manager._gmScript.mj_text_list[4];
                    break;
                case 6:
                    huPaiSF = dd.gm_manager._gmScript.mj_text_list[5];
                    break;
                default:
                    break;
            }
        }
        return huPaiSF;
    }

}




