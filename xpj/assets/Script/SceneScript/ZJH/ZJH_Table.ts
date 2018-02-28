
const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import Game_GoldBase from "./Game_GoldBase";
import * as ZJH_Help from './ZJH_Help';
@ccclass
export default class ZJH_Table extends Game_GoldBase {
    @property(cc.Toggle)
    toggle_gz: cc.Toggle = null;
    @property(cc.Label)
    lblRoomId: cc.Label = null;
    @property(cc.Label)
    lblOnceMax: cc.Label = null;
    @property(cc.Label)
    lblScore: cc.Label = null;
    @property(cc.Label)
    lblBetCount: cc.Label = null;
    @property(cc.Label)
    lblRound: cc.Label = null;
    @property(cc.Label)
    lblClockTime: cc.Label = null;
    @property(cc.Node)
    node_clock: cc.Node = null;
    @property(cc.Sprite)
    clock_pro: cc.Sprite = null;
    @property({
        type: [cc.Button],
        tooltip: '0=弃牌\n1=看牌\n2=比牌\n3=全押\n4=跟注\n5=加注'
    })
    btn_state_list: cc.Button[] = [];
    /**
     * 按钮节点(表态按钮的界面)
     * @type {cc.Node}
     * @memberof ZJH_Table
     */
    @property(cc.Node)
    node_btnLayer: cc.Node = null;
    /**
     *押注池
     * @type {cc.Node}
     * @memberof ZJH_Table
     */
    @property(cc.Node)
    betLayer: cc.Node = null;
    /**
     * 加注界面
     * @type {cc.Node}
     * @memberof ZJH_Table
     */
    @property(cc.Node)
    node_addLayer: cc.Node = null;
    /**
     * 自己的座位节点
     * @type {cc.Node}
     * @memberof ZJH_Table
     */
    @property(cc.Node)
    node_mySeat: cc.Node = null;
    /**
     * 准备按钮
     * @type {cc.Node}
     * @memberof ZJH_Table
     */
    @property(cc.Node)
    node_ready: cc.Node = null;
    /**
     * 加注label
     * @type {cc.Label[]}
     * @memberof ZJH_Table
     */
    @property([cc.Label])
    lblAddBetList: cc.Label[] = [];
    /**
     * 加注按钮
     * @type {cc.Button[]}
     * @memberof ZJH_Table
     */
    @property([cc.Button])
    btn_addBet_list: cc.Button[] = [];

    _canvansScript = null;
    /**
     * 准备倒计时
     * @type {number}
     * @memberof ZJH_Table
     */
    _clockTime: number = 0;
    _cd: number = 0;

    _clock_countTime: number = 0;
    _clock_pro_curTime: number = 0;
    onLoad() {
        this._canvansScript = dd.ui_manager.getCanvasNode().getComponent('ZJHCanvas');
    }

    /**
     * 初始化下注按钮
     * @memberof ZJH_Table
     */
    initBetBtn() {
        for (var i = 0; i < this.btn_state_list.length; i++) {
            this.btn_state_list[i].interactable = false;
        }
    }
    update(dt) {
        if (this._clockTime > 0) {
            this._cd -= dt;
            if (this._cd <= 0) {
                this._cd = 1;
                this.lblClockTime.string = this._clockTime + '';
                this._clockTime--;
                if (this._clockTime <= 5) {
                    dd.mp_manager.playTime();
                }
            }
        }
        if (this._clock_pro_curTime <= this._clock_countTime) {
            this._clock_pro_curTime += dt;
            let pro = this._clock_pro_curTime / this._clock_countTime;
            if (pro < 0) {
                pro = 0;
            } else if (pro > 1) {
                pro = 1;
            } else {
                pro = pro;
            }
            this.clock_pro.fillRange = 1 - pro;
        } else {
            this.clock_pro.fillRange = 0;
        }
    }

    /**
     * 显示桌子基本信息
     * 
     * @memberof ZJH_Table
     */
    showTableBase() {
        this.lblRoomId.string = '房间号 ' + dd.gm_manager.zjhGameData.tableId;
        this.lblBetCount.string = '押注池 ' + dd.utils.getShowNumberString(dd.gm_manager.zjhGameData.totalBetMoney);
        this.lblScore.string = '底分 ' + dd.utils.getShowNumberString(dd.gm_manager.zjhGameData.baseScore);
        this.lblRound.string = '轮数 ' + dd.gm_manager.zjhGameData.roundNum;
        this.lblOnceMax.string = dd.utils.getShowNumberString(dd.gm_manager.zjhGameData.onceMax);
    }
    /**
     * 显示桌子信息
     * 
     * @memberof ZJH_Table
     */
    showTableInfo() {
        this.showTableBase();
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        //动作
        let action = dd.gm_manager.zjhGameData.gameState;
        if (action === ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_READY || action === ZJH_Help.ZJH_Game_State.STATE_TABLE_IDLE) {
            this.removeAllEnemyNode(this.betLayer);
            //如果在准备阶段
            if (mySeat.btState === ZJH_Help.ZJH_BT_State.ACT_STATE_WAIT) {
                this.node_ready.active = true;
            } else {
                this.node_ready.active = false;
            }
            //只有在准备阶段才会显示倒计时
            if (action === ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_READY) {
                this._clockTime = (Number(dd.gm_manager.zjhGameData.actTime) - Number(dd.gm_manager.zjhGameData.svrTime)) / 1000 - 1;
                this._clock_countTime = Number(dd.gm_manager.zjhGameData.actTotalTime) / 1000;
                this._clock_pro_curTime = this._clock_countTime - this._clockTime;
                this.lblClockTime.string = this._clockTime + '';
                this._cd = 1;
                this.node_clock.active = true;
            } else {
                this.node_clock.active = false;
            }
        } else {
            this.node_ready.active = false;
            this.node_clock.active = false;
        }
        this.showBtmInfo(0);
        if (action > ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_FAPAI && action < ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_OVER) {
            //如果押注池中没有金币，并且押注池的金币大于0
            if (this.betLayer.childrenCount === 0 && dd.gm_manager.zjhGameData.totalBetMoney > 0) {
                let numList = ZJH_Help.getEveryIntervalNum(dd.gm_manager.zjhGameData.totalBetMoney);
                this.createMoreGold(numList, this.betLayer);
            }
        }
        //在结算阶段，清空跟到底选择
        if (action === ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_OVER) {
            this.toggle_gz.isChecked = false;
        }
    }

    /**
     * 从加注界面返回到表态界面
     * @memberof ZJH_Table
     */
    showAddReturnToState() {
        if (this.node_addLayer.active) {
            this.showBtmInfo(0);
        }
    }
    /**
     * 显示底部界面信息
     * @memberof ZJH_Table
     */
    showBtmInfo(type: number) {
        //动作
        let action = dd.gm_manager.zjhGameData.gameState;
        if (action === ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_READY || action === ZJH_Help.ZJH_Game_State.STATE_TABLE_IDLE) {
            this.removeAllEnemyNode(this.betLayer);
        }
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        //如果在下注阶段
        if (action === ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_BET) {
            //如果自己还未表态 并且 表态座位是自己
            if (mySeat && mySeat.btState === ZJH_Help.ZJH_BT_State.ACT_STATE_WAIT
                && mySeat.seatIndex === dd.gm_manager.zjhGameData.btIndex) {
                this.showBtmLayer(type);
            } else {
                this.node_addLayer.active = false;
                this.node_btnLayer.active = true;
                this.node_mySeat.active = true;
                this.initBetBtn();
            }
        } else {
            this.node_addLayer.active = false;
            this.node_btnLayer.active = true;
            this.node_mySeat.active = true;
            this.initBetBtn();
        }
        //大于发牌小于结算，就可以看牌
        if (action > ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_FAPAI && action < ZJH_Help.ZJH_Game_State.STATE_TABLE_ZJH_OVER) {
            if (mySeat && mySeat.bGamed && mySeat.btState !== ZJH_Help.ZJH_BT_State.ACT_STATE_DROP
                && mySeat.btVal !== ZJH_Help.ZJH_Act_State.BT_VAL_DROP
                && mySeat.btVal !== ZJH_Help.ZJH_Act_State.BT_VAL_COMPARAE) {
                //看牌按钮
                this.btn_state_list[1].interactable = mySeat.looked === 1 ? false : true;
                //弃牌按钮
                this.btn_state_list[0].interactable = mySeat.btState === ZJH_Help.ZJH_BT_State.ACT_STATE_DROP ? false : true;
            }
        }
    }

    /**
     *显示底部界面
     * @param {number} type 0=表态按钮 1=加注按钮
     * @memberof ZJH_Table
     */
    showBtmLayer(type: number) {
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        if (type === 0) {//对表态按钮的状态设置
            this.showStateLayer(mySeat);
        } else {
            this.showAddBetLayer(mySeat);
        }
        this.node_addLayer.active = type === 1 ? true : false;
        this.node_btnLayer.active = type === 0 ? true : false;
        this.node_mySeat.active = type === 0 ? true : false;
    }

    /**
     * 显示表态界面信息
     * @param {SeatVo} mySeat 
     * @memberof ZJH_Table
     */
    showStateLayer(mySeat: SeatVo) {
        //如果在弃牌、比牌状态
        if (mySeat.btState === ZJH_Help.ZJH_BT_State.ACT_STATE_DROP
            || mySeat.btVal === ZJH_Help.ZJH_Act_State.BT_VAL_DROP
            || mySeat.btVal === ZJH_Help.ZJH_Act_State.BT_VAL_COMPARAE) {
            this.initBetBtn();
        } else {
            //自己当前 跟注 时，应该下注的金额
            let showMoney = mySeat.looked === 1 ? dd.gm_manager.zjhGameData.lookBetMoney : dd.gm_manager.zjhGameData.unLookBetMoney;
            let prevSeat = ZJH_Help.getSeatBySeatId(dd.gm_manager.zjhGameData.prevSeatIndex);

            if ((prevSeat && prevSeat.btVal === ZJH_Help.ZJH_Act_State.BT_VAL_BETALL) //如果上一家的表态是全下，那么只能 弃牌、比牌上一家
                || (mySeat.money <= showMoney)) {                                   //如果自己身上的钱不足 应该下注的钱，就只能 比牌下一家
                this.btn_state_list[2].interactable = true;//比牌

                this.btn_state_list[3].interactable = false;//全押
                this.btn_state_list[4].interactable = false;//跟注
                this.btn_state_list[5].interactable = false;//加注
                //如果上一个人表态全押，这里就不能跟到底了
                this.toggle_gz.isChecked = false;
            } else {
                //如果设置了跟到底的选项，就直接发送 跟到底的消息
                if (this.toggle_gz.isChecked) {
                    let showMoney = mySeat.looked === 1 ? dd.gm_manager.zjhGameData.lookBetMoney : dd.gm_manager.zjhGameData.unLookBetMoney;
                    this._canvansScript.sendBetInfo(ZJH_Help.ZJH_Act_State.BT_VAL_BETSAME, showMoney);
                }

                //如果应该下注金额大于 单注上限
                if (showMoney > dd.gm_manager.zjhGameData.onceMax) showMoney = dd.gm_manager.zjhGameData.onceMax;
                //如果自己的金额大于应该下注的金额
                if (mySeat.money >= showMoney) {
                    //如果应该下注的金额，大于等于 单注上限
                    if (showMoney >= dd.gm_manager.zjhGameData.onceMax) {
                        //只显示跟注、比牌，不能加注
                        this.btn_state_list[4].interactable = true;//跟注
                        this.btn_state_list[5].interactable = false;//加注
                    } else {
                        //如果应该下注的金额，还在单注上限内，那么可以加注
                        this.btn_state_list[4].interactable = true;//跟注
                        this.btn_state_list[5].interactable = true;//加注
                    }
                    //钱足够上一个人下注金额，就可以比牌
                    //轮数大于2 才能比牌
                    if (dd.gm_manager.zjhGameData.roundNum > 2) {
                        this.btn_state_list[2].interactable = true;//比牌
                    } else {
                        this.btn_state_list[2].interactable = false;//比牌
                    }
                } else {
                    //如果自己的钱不足，跟注了之后，就取消跟到底的选项
                    this.toggle_gz.isChecked = false;
                    //如果自己的钱不够，就只能 弃牌 
                    this.btn_state_list[2].interactable = false;//比牌
                    this.btn_state_list[4].interactable = false;//跟注
                    this.btn_state_list[5].interactable = false;//加注
                }

                //当前正在游戏中的玩家列表
                let seatList = ZJH_Help.getNowPlayer();
                //如果玩家数量是2，并且 轮数大于1
                if (seatList.length === 2 && dd.gm_manager.zjhGameData.roundNum > 1) {
                    this.btn_state_list[3].interactable = true;//全押
                } else {
                    this.btn_state_list[3].interactable = false;//全押
                }
            }
        }
    }
    /**
     * 显示加注界面信息
     * @param {SeatVo} mySeat 
     * @memberof ZJH_Table
     */
    showAddBetLayer(mySeat: SeatVo) {
        let minScore = dd.gm_manager.zjhGameData.baseScore;
        let maxScore = dd.gm_manager.zjhGameData.onceMax;
        //自己当前 跟注 时，应该下注的金额
        let showMoney = mySeat.looked === 1 ? dd.gm_manager.zjhGameData.lookBetMoney : dd.gm_manager.zjhGameData.unLookBetMoney;
        //如果应该下注金额大于 单注上限
        if (showMoney > dd.gm_manager.zjhGameData.onceMax) showMoney = dd.gm_manager.zjhGameData.onceMax;
        let maxBet = maxScore - showMoney;
        if (maxBet < this.lblAddBetList.length) {
            for (var i = 0; i < this.lblAddBetList.length; i++) {
                if (i < maxBet) {
                    this.lblAddBetList[i].node.parent.active = true;
                    this.lblAddBetList[i].string = (i + 1) + '';
                    let line = cc.find('line', this.lblAddBetList[i].node.parent);
                    if (line) {
                        if (i === maxBet - 1) {
                            line.active = false;
                        } else {
                            line.active = true;
                        }
                    }
                } else {
                    this.lblAddBetList[i].node.parent.active = false;
                }
            }
        } else {
            //设置加注按钮的金额
            this.lblAddBetList[this.lblAddBetList.length - 1].node.parent.active = true;
            this.lblAddBetList[this.lblAddBetList.length - 1].string = dd.utils.getShowNumberString(maxBet);
            let one = maxBet / this.lblAddBetList.length;
            for (var i = 0; i < this.lblAddBetList.length - 1; i++) {
                this.lblAddBetList[i].node.parent.active = true;
                this.lblAddBetList[i].string = Math.round(dd.utils.getShowNumberString(one * (i + 1))) + '';
            }
        }
        //设置加注按钮是否可以点击
        for (var i = 0; i < this.lblAddBetList.length; i++) {
            let betMoney = Number(this.lblAddBetList[i].string);
            if (betMoney > mySeat.money) {
                this.btn_addBet_list[i].interactable = false;
            } else {
                this.btn_addBet_list[i].interactable = true;
            }
        }
    }
    /**
     * 表态按钮的事件
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof ZJH_Table
     */
    click_btn_state(event, type: string) {
        dd.mp_manager.playButton();
        cc.log('click_btn_state--+' + type);
        if (!this.btn_state_list[Number(type)].interactable) return;
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        if (!mySeat) return;
        switch (type) {
            case '0'://弃牌
                this._canvansScript.sendBetInfo(ZJH_Help.ZJH_Act_State.BT_VAL_DROP, 0);
                break;
            case '1'://看牌
                this._canvansScript.sendBetInfo(ZJH_Help.ZJH_Act_State.BT_VAL_LOOCK, mySeat.seatIndex);
                break;
            case '2'://比牌
                this._canvansScript.showChooseBPAction();
                break;
            case '3'://全押
                //当前正在游戏中的玩家列表
                let seatList = ZJH_Help.getNowPlayer();
                let list = [];
                for (var i = 0; i < seatList.length; i++) {
                    let m = seatList[i].money + seatList[i].betMoney;
                    list.push(m);
                }
                list.sort((a, b) => {
                    return a - b;
                });
                let allinMoney = list[0];
                this._canvansScript.sendBetInfo(ZJH_Help.ZJH_Act_State.BT_VAL_BETALL, allinMoney);
                break;
            case '4'://跟注
                //自己当前 跟注 时，应该下注的金额
                let showMoney = mySeat.looked === 1 ? dd.gm_manager.zjhGameData.lookBetMoney : dd.gm_manager.zjhGameData.unLookBetMoney;
                this._canvansScript.sendBetInfo(ZJH_Help.ZJH_Act_State.BT_VAL_BETSAME, showMoney);
                break;
            case '5'://加注
                this.showBtmLayer(1);
                break;
            default:
        }
    }

    /**
     * 加注按钮
     * @param {any} event 
     * @param {string} type 
     * @returns 
     * @memberof ZJH_Table
     */
    click_btn_addBet(event, type: string) {
        dd.mp_manager.playButton();
        let index = Number(type);
        if (!this.btn_addBet_list[index].interactable) return;
        let addBet = Number(this.lblAddBetList[index].string);
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        if (!mySeat) return;
        let showMoney = mySeat.looked === 1 ? dd.gm_manager.zjhGameData.lookBetMoney : dd.gm_manager.zjhGameData.unLookBetMoney;
        let betMoeny = addBet + showMoney;
        cc.log('---加注---+' + addBet);
        this._canvansScript.sendBetInfo(ZJH_Help.ZJH_Act_State.BT_VAL_BETADD, betMoeny);
        this.showBtmLayer(0);
    }
    /**
     * 退出游戏
     * @memberof ZJH_Table
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.showAlert('您确定退出游戏吗？', '温馨提示', {
            lbl_name: '确定',
            callback: () => {
                this._canvansScript.sendOutGame();
            }
        },
            {
                lbl_name: '取消',
                callback: () => {
                }
            }, 1);
    }
    /**
     * 准备游戏
     * @memberof ZJH_Table
     */
    click_btn_ready() {
        dd.mp_manager.playButton();
        this._canvansScript.sendReadyGame();
    }

    /**
     * 播放下注动作
     * @param {number} pos 坐标点
     * @param {number} num 数字
     * @memberof ZJH_Table
     */
    playGoldAction(pos: cc.Vec2, num: number) {
        let numList = ZJH_Help.getEveryIntervalNum(num);
        let fz = cc.v2(this.betLayer.width, this.betLayer.height);
        this.playGoldMoveToPool(pos, numList, fz, dd.ui_manager.getRootNode(), this.betLayer);
    }

    /**
     * 播放赢金币的动画
     * @param {cc.Vec2} pos 坐标点
     * @param {any} startCB 第一个金币到达的回调
     * @param {any} endCB   动作做完后的回调
     * @memberof ZJH_Table
     */
    playWinGoldAction(pos: cc.Vec2, startCB: any = null, endCB: any = null) {
        dd.mp_manager.playZJH('coins');
        this.playGoldMoveToPlayer(pos, this.betLayer, startCB, endCB, true, cc.v2(60, 60));
    }
}
