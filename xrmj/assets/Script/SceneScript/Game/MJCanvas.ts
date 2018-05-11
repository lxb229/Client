
const { ccclass, property } = cc._decorator;

import MJ_Card from './MJ_Card';
import MJ_Card_Group from './MJ_Card_Group';

import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState, MJ_Suit, MJ_Act_Type, MJ_Game_Type, MJ_Act_State } from '../../Modules/Protocol';
@ccclass
export default class MJCanvas extends cc.Component {
    /**
     * 桌子界面节点
     * 
     * @type {cc.Node}
     * @memberof MJCanvas
     */
    @property(cc.Node)
    node_table: cc.Node = null;

    /**
     * 游戏界面节点
     * 
     * @type {cc.Node}
     * @memberof MJCanvas
     */
    @property(cc.Node)
    node_game: cc.Node = null;

    /**
     * 重播游戏界面节点
     * 
     * @type {cc.Node}
     * @memberof MJCanvas
     */
    @property(cc.Node)
    node_replay: cc.Node = null;

    /**
     * 角色信息预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    role_prefab: cc.Prefab = null;
    /**
     * 聊天预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    chat_prefab: cc.Prefab = null;
    /**
     * 设置预设
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    setting_prefab: cc.Prefab = null;
    /**
     * 聊天气泡预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    chat_show_prefab: cc.Prefab = null;

    /**
     * 解散房间的预设
     * 
     * @type {cc.Prefab}
     * @memberof MJ_Table
     */
    @property(cc.Prefab)
    disband_prefab: cc.Prefab = null;

    /**
     * 每局游戏结束的预设
     * 
     * @type {cc.Prefab}
     * @memberof MJ_Table
     */
    @property(cc.Prefab)
    game_over_prefab: cc.Prefab = null;

    /**
     * 左右两边玩家打出的牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    mj_card_left_prefab: cc.Prefab = null;

    /**
     * 上玩家打出的牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    mj_card_top_prefab: cc.Prefab = null;
    /**
     * 下玩家打出的牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    mj_card_table_prefab: cc.Prefab = null;

    /**
     * 玩家自己的手牌预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    mj_card_mine_prefab: cc.Prefab = null;
    /**
     * 多个杠牌的时候，选中杠牌的预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    mj_card_gang_prefab: cc.Prefab = null;

    /**
     *显示听牌，可以胡什么牌的预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    mj_ting_prefab: cc.Prefab = null;
    /**
     * 换三张动作预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    act_swap_prefab: cc.Prefab = null;

    /**
     * 刮风动作预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    act_gf_prefab: cc.Prefab = null;

    /**
     * 下雨动作预设
     * 
     * @type {cc.Prefab}
     * @memberof MJCanvas
     */
    @property(cc.Prefab)
    act_xy_prefab: cc.Prefab = null;
    /**
     * 杠/碰牌的预设列表 0下 1左 2上 3右
     * 
     * @type {cc.Prefab[]}
     * @memberof MJCanvas
     */
    @property([cc.Prefab])
    mj_card_group_Prefab: cc.Prefab[] = [];
    /**
     * 打缺的图片列表
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof MJCanvas
     */
    @property([cc.SpriteFrame])
    unSuit_list: cc.SpriteFrame[] = [];

    /**
     * 麻将的文字图片列表
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof MJCanvas
     */
    @property({
        // default: [],
        type: [cc.SpriteFrame],
        tooltip: '0=自摸\n 1=胡\n 2=天胡\n 3=地胡\n 4=杠上花\n 5=杠上炮\n 6=碰\n 7=杠\n 8=抢杠\n 9=点炮\n 10=一炮多响\n 11=海底捞月\n 12=呼叫转移\n'
            + '13=流局\n 14=游戏结束\n'
    })
    mj_text_list: cc.SpriteFrame[] = [];

    _node_chat: cc.Node = null;
    _node_setting: cc.Node = null;
    /**
     * 换三张动作节点
     * 
     * @type {cc.Node}
     * @memberof MJCanvas
     */
    _act_swap: cc.Node = null;

    /**
     * 申请解散房间节点
     * 
     * @type {cc.Node}
     * @memberof MJCanvas
     */
    _node_disband: cc.Node = null;
    /**
     * 
     * 麻将的牌图片列表 1~9万 10~18筒 19~27条 
     * @type {cc.SpriteFrame[]}
     * @memberof MJCanvas
     */
    @property({
        type: [cc.SpriteFrame],
        tooltip: ' 1~9万\n 10~18筒\n 19~27条'
    })
    mj_sf_list: cc.SpriteFrame[] = [];

    /**
     * 
     * 推送消息(聊天信息通知) 回调函数
     * @memberof MJCanvas
     */
    MJ_ChatPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        this.showChatInfo(data);
    };
    /**
     * 微信分享回调
     * 
     * @memberof MJ_Table
     */
    wxShareCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        if (data === 0) {
            dd.ui_manager.showTip('好友邀请发送成功！');
        } else {
            dd.ui_manager.showTip('好友邀请发送失败！');
        }
    };
    /**
     * 游戏界面脚本
     * 
     * @type {}
     * @memberof MJCanvas
     */
    _mjGame = null;
    /**
     * 桌子界面脚本
     * 
     * @type {MJ_Table}
     * @memberof MJCanvas
     */
    _mjTable = null;

    _role: cc.Node = null;
    _game_over: cc.Node = null;
    _node_gang: cc.Node = null;
    _node_ting: cc.Node = null;

    onLoad() {
        dd.gm_manager.setGMTarget(this);
        this._mjGame = this.node_game.getComponent('MJ_Game');
        this._mjTable = this.node_table.getComponent('MJ_Table');

        dd.mp_manager.audioSetting.isMusic = false;
        dd.mp_manager.saveMPSetting();
        dd.mp_manager.stopBackGround();
        this.bindOnPush();

        //添加一个全局的点击事件
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            if (dd.gm_manager.touchTarget) return;
            //对自己的牌进行选中
            if (!dd.gm_manager._minScript) {
                dd.gm_manager._minScript = this._mjGame.node_player_list[0].getComponent('MJ_Game_Mine');
            }
            dd.gm_manager._minScript.unSelectCard();
            this.showTSCard(-1);

            this.showTingPai(false);
            if (dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_SWAPCARD) {//如果是换牌
                dd.gm_manager._minScript._swapCardList.length = 0;
                dd.gm_manager._minScript.btn_swap.interactable = false;
            }
        }, this);
    }

    start() {
        //是否是重播游戏记录
        if (dd.gm_manager.replayMJ === 0) {
            this.node_replay.active = false;
            if (dd.ui_manager.showLoading('正在加载桌子信息')) {
                let obj = { 'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId, 'password': '0' };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                    if (flag === 0) {//成功
                        dd.gm_manager.setTableData(content as MJGameData, true);
                    } else if (flag === -1) {//超时

                    } else {//失败,content是一个字符串
                        dd.ui_manager.showAlert(content, '温馨提示', {
                            lbl_name: '确定',
                            callback: () => {
                                this._mjTable.quitGame();
                            }
                        });
                    }
                    dd.ui_manager.hideLoading();
                });
            }
        } else {
            this.node_replay.active = true;
            this.showMJInfo();
        }
    }

    onDestroy() {
        this.bindOffPush();
        dd.mp_manager.destroyGame();
    }
    /**
     * 绑定游戏push
     * 
     * @memberof MJCanvas
     */
    bindOnPush(): void {
        //推送消息(游戏状态变化)
        cc.systemEvent.on('MJ_GamePush', this.showMJInfo, this);
        //推送消息(聊天信息通知)
        cc.systemEvent.on('MJ_ChatPush', this.MJ_ChatPush, this);
        cc.systemEvent.on('cb_share', this.wxShareCallBack, this);
    }

    /**
     * 解除绑定游戏push
     * 
     * @memberof MJCanvas
     */
    bindOffPush() {
        //推送消息(游戏状态变化)
        cc.systemEvent.off('MJ_GamePush', this.showMJInfo, this);
        //推送消息(聊天信息通知)
        cc.systemEvent.off('MJ_ChatPush', this.MJ_ChatPush, this);
        cc.systemEvent.off('cb_share', this.wxShareCallBack, this);
    }

    /**
     * 显示麻将信息
     * 
     * @memberof MJCanvas
     */
    showMJInfo() {
        cc.log('------游戏数据----' + dd.gm_manager.mjGameData.tableBaseVo.gameState);
        cc.log('时间：' + new Date());
        cc.log(dd.gm_manager.mjGameData);
        this._mjTable.showTableInfo();
        //如果在空闲状态，就不显示打游戏界面
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_IDLE) {
            this.node_game.active = false;
        } else {
            this.node_game.active = true;
        }
        //如果在换牌阶段，并且换牌结束，播放动作的时候，不进行刷新游戏数据，需要动作结束
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_SWAPCARD
            && dd.gm_manager.mjGameData.tableBaseVo.swapCardType >= 0) {
            //两人麻将，就直接跳过,原因是：两人麻将没有定缺，换三张后直接进入了出牌阶段，没有时间来对牌进行整理
            if (dd.gm_manager.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_LRLF) {
                this._mjGame.showGameInfo();
            }
        } else {
            this._mjGame.showGameInfo();
        }
        this.showSwapAct();

        //游戏状态
        switch (dd.gm_manager.mjGameData.tableBaseVo.gameState) {
            case MJ_GameState.STATE_TABLE_READY:
                break;
            case MJ_GameState.STATE_TABLE_BREAKCARD:
                //处理胡杠碰吃过表态结果
                //处理播放动作和播放语音
                this._mjGame.showBreakStates();
                break;
            case MJ_GameState.STATE_TABLE_OVER_ONCE:
                this.showGameOver();
                break;
            case MJ_GameState.STATE_TABLE_OVER_ALL:
                this._mjTable.quitGame(1);
                break;
            default:
                break;
        }

        this.showDisband();
    }
    /**
     *  显示麻将的换三张动作
     * @memberof MJCanvas
     */
    showSwapAct() {
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_SWAPCARD) {
            if (!this._act_swap || !this._act_swap.isValid) {
                this._act_swap = cc.instantiate(this.act_swap_prefab);
                this._act_swap.setPosition(cc.p(0, 12));
                this._act_swap.parent = dd.ui_manager.getRootNode();
            }
            let swapAct = this._act_swap.getComponent('MJ_ActionSwap');
            swapAct.showSwapCard(dd.gm_manager.mjGameData, dd.gm_manager.mjGameData.tableBaseVo.cfgId);
        } else {
            if (this._act_swap && this._act_swap.isValid) {
                let swapAct = this._act_swap.getComponent('MJ_ActionSwap');
                if (!swapAct._isAct) {
                    this._act_swap.removeFromParent(true);
                    this._act_swap.destroy();
                    this._act_swap = null;
                }
            }
        }
    }

    /**
     * 换牌表态
     * 
     * @param {number[]} cardIds 
     * @memberof MJCanvas
     */
    sendSwap(cardIds: number[]) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'cardIds': JSON.stringify(cardIds),
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_SWAP_CARD, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                cc.log(content);
                dd.ui_manager.showTip(content);
            }
        });
    }

    /**
     * 定缺表态
     * 
     * @memberof MJCanvas
     */
    sendDinQue(dq: MJ_Suit) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'bt': dq
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_DINQUE, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                cc.log(content);
                dd.ui_manager.showTip(content);
            }
        });
    }

    /**
     * 出牌表态
     * 
     * @param {number} cardId 
     * @memberof MJCanvas
     */
    sendOutCard(cardId: number) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'cardId': JSON.stringify(cardId)
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_OUT_CARD, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
                this.showMJInfo();
            }
        });
    }

    /**
     * 胡杠碰吃表态
     * 
     * @param {MJ_Act_Type} actType 表态类型
     * @param {number} cardId 表态牌对象
     *  @param {cc.Node} node_state 表态节点
     * @memberof MJCanvas
     */
    sendOtherBreakCard(actType: MJ_Act_Type, cardId: number, node_state?: cc.Node) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'bt': actType,
            'cardId': JSON.stringify(cardId)
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_OTHERBREAK_CARD, msg, (flag: number, content?: any) => {
            if (node_state && node_state.isValid) {
                if (flag === 0) {
                    node_state.active = false;
                } else {
                    dd.ui_manager.showTip(content);
                    node_state.active = true;
                }
            }
        });
    }
    /**
     * 胡杠碰吃表态
     * bt [int] 表态类型(0=胡,1=杠,2=碰,3=吃,4=过)
     * cardId [byte] 表态的牌(目标牌,如幺鸡当3条用,表态牌为3条)
     * gangType [int] 杠类型(1=巴杠,2=暗杠 3=直杠)
     * replace [int] 表态的牌是否是幺鸡
     *  @param {cc.Node} node_state 表态节点
     * @memberof MJCanvas
     */
    sendLSGangBreakCard(actType: MJ_Act_Type, gangObj: GangData, node_state?: cc.Node) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'bt': actType,
            'cardId': JSON.stringify(gangObj.cardId),
            'gangType': gangObj.isAnGang,
            'replace': 0
        };
        //只要是巴杠，并且是 使用幺鸡 去巴杠的，就要替换牌
        if (!gangObj.isAnGang && gangObj.isUseYaoJi) {
            obj.replace = 1;
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_OTHERBREAK_CARD_LSMJ_BT, msg, (flag: number, content?: any) => {
            if (node_state && node_state.isValid) {
                if (flag === 0) {
                    node_state.active = false;
                } else {
                    dd.ui_manager.showTip(content);
                    node_state.active = true;
                }
            }
        });
    }

    /**
     * 获取cardId麻将牌的图片
     * 
     * @param {number} cardId 
     * @memberof MJCanvas
     */
    getMJCardSF(cardId: number): cc.SpriteFrame {
        let suit = cardId > 100 ? Math.floor(cardId / 100) : Math.floor(cardId / 10);
        let point = cardId > 100 ? Math.floor(cardId / 10) % 10 : cardId % 10;
        let index = (suit - 1) * 9 + (point - 1);
        let sf: cc.SpriteFrame = null;
        if (index >= 0 && index < this.mj_sf_list.length) {
            sf = this.mj_sf_list[index];
        } else {
            cc.log('索引错误');
        }
        return sf;
    }

    /**
     * 创建打出的牌
     * 
     * @param {number} type  0下边  1右边  2上边  3左边
     * @param {number} cardId 牌数据
     * @param {cc.Node} parentNode  父节点
     * @param {(mjCardNode: cc.Node) => void} [initCB=null] 创建完成的回调
     * @memberof MJCanvas
     */
    showPlayOutCard(type: number, cardId: number, parentNode: cc.Node, initCB: (mjCardNode: cc.Node) => void = null) {
        let cardNode: cc.Node = null;
        switch (type) {
            case 0:
                cardNode = cc.instantiate(this.mj_card_table_prefab);
                break;
            case 1:
            case 3:
                cardNode = cc.instantiate(this.mj_card_left_prefab);
                break;
            case 2:
                cardNode = cc.instantiate(this.mj_card_top_prefab);
                break;
            default:
        }

        if (cardNode) {
            cardNode.tag = cardId;
            let mc: MJ_Card = cardNode.getComponent('MJ_Card');
            if (cardId > 0) {
                let cardSF = this.getMJCardSF(cardId);
                mc.initData(cardId, cardSF);
            }
            //如果是右边，需要修正一下参数，因为用的是同一个预设
            if (type === 1) {
                mc.setFixCard(cc.p(-1, 1), cc.p(-1, 1));
            }
            cardNode.parent = parentNode;
            if (initCB) {
                initCB(cardNode);
            }
        }
    }

    /**
     *  根据类型创建杠/碰牌的节点
     * 
     * @param {number} type 0碰牌 1巴杠 2暗杠 3直杠
     * @param {number} cards 牌数据列表
     * @param {number} seatId 座位号 0自己 3左边 2上边 1右边
     * @param {cc.Node} parentNode 父节点
     * @memberof MJCanvas
     */
    showGroupCard(type: number, cards: number[], seatId: number, parentNode: cc.Node, initCB: (mcgNode: cc.Node) => void = null) {
        if (cards && cards.length > 0) {
            let mj_card_group: cc.Node = cc.instantiate(this.mj_card_group_Prefab[seatId]);
            let mcg: MJ_Card_Group = mj_card_group.getComponent('MJ_Card_Group');
            let csf_list = [];
            cards.forEach(cardId => {
                let csf = this.getMJCardSF(cardId);
                csf_list.push(csf);
            });
            mcg.initData(type, cards, csf_list);
            mj_card_group.parent = parentNode;
            if (initCB) {
                initCB(mj_card_group);
            }
        }
    }

    /**
     * 显示自己的手牌
     * 
     * @param {number} cardId 牌数据
     * @param {cc.Node} parentNode 牌父节点
     * @param {boolean} [isShow=false] 是否显示背面
     * @param {(mcgNode: cc.Node) => void} [initCB=null] 回调函数
     * @memberof MJCanvas
     */
    showMineCard(cardId: number, parentNode: cc.Node, isShow: boolean = false, initCB: (mcgNode: cc.Node) => void = null) {
        let mj_card_mine: cc.Node = cc.instantiate(this.mj_card_mine_prefab);
        mj_card_mine.tag = cardId;
        let mcm: MJ_Card = mj_card_mine.getComponent('MJ_Card');
        if (cardId > 0) {
            let cardSF = this.getMJCardSF(cardId);
            mcm.initData(cardId, cardSF, isShow);
        }
        mj_card_mine.parent = parentNode;
        if (initCB) {
            initCB(mj_card_mine);
        }
    }

    /**
    * 显示每个人打出的麻将
    * 
    * @param {number} cardId 牌数据
    * @param {cc.Node} parentNode 父节点
    * @param {SeatVo} seatInfo 座位信息
    * @memberof MJCanvas
    */
    showOutActMJ(cardId: number, parentNode: cc.Node, seatInfo: SeatVo) {
        let mj_card_mine: cc.Node = cc.instantiate(this.mj_card_mine_prefab);
        mj_card_mine.tag = cardId;
        let mcm: MJ_Card = mj_card_mine.getComponent('MJ_Card');
        if (cardId) {
            let cardSF = this.getMJCardSF(cardId);
            mcm.initData(cardId, cardSF);
        }
        //如果是乐山麻将
        if (dd.gm_manager.mjGameData.tableBaseVo.cfgId === MJ_Game_Type.GAME_TYPE_LSMJ) {
            let clon = mj_card_mine.getChildByName('clon');
            if (clon) clon.active = false;
        }
        mj_card_mine.parent = parentNode;
        mj_card_mine.scale = 0.1;
        mj_card_mine.opacity = 0;
        let spwan1 = cc.spawn(cc.scaleTo(0.1, 1), cc.fadeIn(0.1));
        let spwan2 = cc.spawn(cc.scaleTo(0.1, 0.1), cc.fadeOut(0.1));
        let seq = cc.sequence(spwan1, cc.delayTime(1), spwan2, cc.callFunc((target: cc.Node, data?: any) => {
            target.removeFromParent(true);
            target.destroy();
        }, this));
        mj_card_mine.runAction(seq);

        //播放音效
        let suit = Math.floor(cardId / 100);
        let point = Math.floor(cardId / 10) % 10;
        dd.mp_manager.playPokerSound(dd.mp_manager.audioSetting.language, suit, seatInfo.sex, point);
    }

    /**
     * 显示多个可以杠的牌
     * 
     * @param {number[]} cards 
     * @memberof MJCanvas
     */
    showMoreGang(gList: GangData[], target) {
        this._node_gang = cc.instantiate(this.mj_card_gang_prefab);
        this._node_gang.parent = dd.ui_manager.getRootNode();
        let mjGang = this._node_gang.getComponent('MJ_Gang');
        mjGang.initData(gList, target);
    }

    /**
     * 显示听牌，可以胡什么牌的预设
     * @param {boolean} isShow 
     * @param {number[]} [cards] 牌数组是花色和点数组成的 两位数
     * @memberof MJCanvas
     */
    showTingPai(isShow: boolean, cards?: number[], ) {
        let rootNode = dd.ui_manager.getRootNode();
        if (isShow) {
            if (!this._node_ting || !this._node_ting.isValid) {
                this._node_ting = cc.instantiate(this.mj_ting_prefab);
                this._node_ting.parent = rootNode;
            }
            let mjTing = this._node_ting.getComponent('MJ_Ting');
            mjTing.initData(cards);
        } else {
            if (this._node_ting && this._node_ting.isValid) {
                this._node_ting.removeFromParent(true);
                this._node_ting.destroy();
                this._node_ting = null;
            }
        }
    }
    /**
     * 显示麻将的表态文字动作
     * 
     * @param {cc.SpriteFrame} actSf 
     * @param {cc.Node} parentNode 
     * @memberof MJCanvas
     */
    showTxtAct(actSf: cc.SpriteFrame, parentNode: cc.Node) {
        let node: cc.Node = new cc.Node('mjTxtAct');
        let sf: cc.Sprite = node.addComponent(cc.Sprite);
        sf.spriteFrame = actSf;
        node.scale = 2;
        node.opacity = 0;
        node.setPosition(cc.p(0, 26));
        node.parent = dd.ui_manager.getRootNode();

        let spawn = cc.spawn(cc.fadeIn(0.3), cc.scaleTo(0.5, 1));
        let seq = cc.sequence(spawn, cc.delayTime(0.5), cc.callFunc((target: cc.Node, data?: any) => {
            target.removeFromParent(true);
            target.destroy();
        }, this));
        node.runAction(seq);
    }

    /**
     * 显示刮风动作
     * 
     * @param {cc.Node} parentNode 
     * @memberof MJCanvas
     */
    showGFAct(parentNode: cc.Node) {
        let act_gf = cc.instantiate(this.act_gf_prefab);
        act_gf.parent = parentNode;
    }
    /**
     * 显示下雨动作
     * 
     * @param {cc.Node} parentNode 
     * @memberof MJCanvas
     */
    showXYAct(parentNode: cc.Node) {
        let act_xy = cc.instantiate(this.act_xy_prefab);
        act_xy.parent = parentNode;
    }
    /**
     * 显示游戏聊天界面
     * 
     * @memberof MJCanvas
     */
    showChat() {
        dd.mp_manager.playAlert();
        if (!this._node_chat || !this._node_chat.isValid) {
            this._node_chat = cc.instantiate(this.chat_prefab);
            this._node_chat.parent = dd.ui_manager.getRootNode();
        }
    }

    /**
     * 显示游戏聊天信息
     * @param {any} type 
     * @memberof MJCanvas
     */
    showChatInfo(data: ChatData) {
        if (!data) return;
        let pos: cc.Vec2 = this._mjTable.getPlayPosById(data.accountId);
        if (data.type === 0) {
            let seatInfo = dd.gm_manager.getSeatById(data.accountId);
            dd.mp_manager.playQuicklySound(seatInfo.sex, data.content);
            let quickList = dd.mp_manager.quicklyList;
            let chat_show = cc.instantiate(this.chat_show_prefab);
            let fx = 1;
            if (pos.x > 0) {
                fx = -1;
            }
            chat_show.scaleX = fx * 0.1;
            chat_show.scaleY = 0.1;
            pos.y += 80;
            chat_show.setPosition(pos);
            let showscript = chat_show.getComponent('Game_Chat_Show');
            showscript.showChat(quickList[data.content - 1].msg, fx);
            chat_show.parent = dd.ui_manager.getRootNode();

            let action = cc.sequence(cc.scaleTo(0.1, 1 * fx, 1), cc.delayTime(1), cc.callFunc((target: cc.Node, data?: any) => {
                target.removeFromParent(true);
                target.destroy();
            }, this));
            chat_show.runAction(action);
        } else {
            let bqNode: cc.Node = new cc.Node('bqNode');
            let sf: cc.Sprite = bqNode.addComponent(cc.Sprite);
            let bqSF = dd.img_manager.chatSpriteFrames[data.content];
            sf.spriteFrame = bqSF;
            bqNode.scale = 0;
            pos.x = pos.x > 0 ? pos.x - 80 : pos.x + 80;
            bqNode.setPosition(cc.p(pos.x, pos.y + 50));
            bqNode.parent = dd.ui_manager.getRootNode();

            let action = cc.scaleTo(0.5, 1);
            action.easing(cc.easeElasticOut(0.4));
            let seq = cc.sequence(action, cc.delayTime(0.5), cc.callFunc((target: cc.Node, data?: any) => {
                target.removeFromParent(true);
                target.destroy();
            }, this));
            bqNode.runAction(seq);
        }
    }

    /**
     * 显示游戏设置
     * 
     * @memberof MJCanvas
     */
    showSetting() {
        dd.mp_manager.playAlert();
        if (!this._node_setting || !this._node_setting.isValid) {
            this._node_setting = cc.instantiate(this.setting_prefab);
            this._node_setting.parent = dd.ui_manager.getRootNode();
        }
    }

    /**
     * 显示游戏解散房间界面
     * 
     * @memberof MJCanvas
     */
    showDisband() {
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_DESTORY) {
            if (!this._node_disband || !this._node_disband.isValid) {
                this._node_disband = cc.instantiate(this.disband_prefab);
                this._node_disband.zIndex = 9;
                this._node_disband.parent = dd.ui_manager.getRootNode();
            }
            let script = this._node_disband.getComponent('Game_Disband');
            script.initData();
            this.unShowPopup();
        } else {
            if (this._node_disband && this._node_disband.isValid) {
                this._node_disband.removeFromParent(true);
                this._node_disband.destroy();
                this._node_disband = null;
            }
        }
    }

    /**
     * 不显示弹框
     * 
     * @memberof MJCanvas
     */
    unShowPopup() {
        if (this._node_setting && this._node_setting.isValid) {
            this._node_setting.removeFromParent(true);
            this._node_setting.destroy();
            this._node_setting = null;
        }
        if (this._node_chat && this._node_chat.isValid) {
            this._node_chat.removeFromParent(true);
            this._node_chat.destroy();
            this._node_chat = null;
        }
    }

    /**
     * 显示每局游戏结束
     * 
     * @memberof MJCanvas
     */
    showGameOver() {
        if (dd.gm_manager && dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.settlementOnce) {
            if (!this._game_over || !this._game_over.isValid) {
                this._game_over = cc.instantiate(this.game_over_prefab);
                let go = this._game_over.getComponent('Game_Over');
                go.initData(dd.gm_manager.mjGameData.settlementOnce);
                this._game_over.zIndex = 8;
                this._game_over.parent = dd.ui_manager.getRootNode();
            }
        }
    }

    /**
     * 显示角色信息
     * 
     * @memberof MJCanvas
     */
    showRoleInfo(accountId: string) {
        if (dd.ui_manager.showLoading()) {
            dd.mp_manager.playAlert();
            let obj = { 'accountId': accountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_ACCOUNTID, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let roleInfo = content as UserData;
                    if (!this._role || !this._role.isValid) {
                        this._role = cc.instantiate(this.role_prefab);
                        let roleScript = this._role.getComponent('Role');
                        roleScript.showInfo(roleInfo);
                        this._role.parent = dd.ui_manager.getRootNode();
                    }
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }

    /**
     * 跳转调用,显示桌子上和选出的牌一样的牌
     * 
     * @param {number} cardId 
     * @memberof MJCanvas
     */
    showTSCard(cardId: number) {
        this._mjGame.showTableSelectCard(cardId);
    }
}
