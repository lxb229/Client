const { ccclass, property } = cc._decorator;

import MJ_PlayerUI from './MJ_PlayerUI';
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState } from '../../Modules/Protocol';
@ccclass
export default class MJ_Table extends cc.Component {

    /**
     * 游戏配置描述
     * 
     * @type {cc.Label}
     * @memberof MJ_Table
     */
    @property(cc.Label)
    lblTitle: cc.Label = null;

    /**
     * 游戏房间id
     * 
     * @type {cc.Label}
     * @memberof MJ_Table
     */
    @property(cc.Label)
    lblRoomId: cc.Label = null;

    /**
     * 系统时间
     * 
     * @type {cc.Label}
     * @memberof MJ_Table
     */
    @property(cc.Label)
    lblSysTime: cc.Label = null;

    /**
     * 游戏延时
     * 
     * @type {cc.Label}
     * @memberof MJ_Table
     */
    @property(cc.Label)
    lblDelay: cc.Label = null;

    /**
     * 信号图片
     * 
     * @type {cc.Sprite}
     * @memberof MJ_Table
     */
    @property(cc.Sprite)
    imgWifi: cc.Sprite = null;

    /**
     * 电池电量
     * 
     * @type {cc.ProgressBar}
     * @memberof MJ_Table
     */
    @property(cc.ProgressBar)
    pro_Power: cc.ProgressBar = null;

    /**
     * 游戏等待界面
     * 
     * @type {cc.Node}
     * @memberof MJ_Table
     */
    @property(cc.Node)
    table_wait: cc.Node = null;

    /**
     * 退出按钮的节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Table
     */
    @property(cc.Node)
    node_out: cc.Node = null;
    /**
     * 微信邀请按钮
     * @type {cc.Node}
     * @memberof MJ_Table
     */
    @property(cc.Node)
    node_wx_invit: cc.Node = null;
    /**
     * 解散房间按钮的节点
     * 
     * @type {cc.Node}
     * @memberof MJ_Table
     */
    @property(cc.Node)
    node_disband: cc.Node = null;

    /**
     * 玩家节点列表
     * 
     * @type {cc.Node[]}
     * @memberof MJ_Player
     */
    @property([cc.Node])
    playerList: cc.Node[] = [];
    /**
     * 玩家准备节点列表
     * @type {cc.Node[]}
     * @memberof MJ_Table
     */
    @property([cc.Node])
    playerReadyList: cc.Node[] = [];
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

    /**
     * 电量获取的刷新时间
     * 
     * @type {number}
     * @memberof MJ_Table
     */
    _powerTime: number = 30;

    _msTime: number = 1;

    onLoad() {
        if (dd.config.wxState === 0) {
            this.node_wx_invit.active = true;
        } else {
            this.node_wx_invit.active = false;
        }
    }

    update(dt) {
        if (dd.gm_manager && dd.gm_manager.mjGameData && !dd.gm_manager.isReplayPause) {
            this._cdTime -= dt;
            if (this._cdTime < 0) {
                this._cdTime = 1;
                dd.gm_manager.mjGameData.tableBaseVo.svrTime = Number(dd.gm_manager.mjGameData.tableBaseVo.svrTime) + 1000 + '';
                this.lblSysTime.string = dd.utils.getDateStringByTimestamp(dd.gm_manager.mjGameData.tableBaseVo.svrTime, 2);
            }
        }
        //电量获取刷新
        this._powerTime -= dt;
        if (this._powerTime < 0) {
            this._powerTime = 30;
            this.pro_Power.progress = dd.js_call_native.getBatteryLevel() / 100;
        }

        this._msTime -= dt;
        if (dd.ws_manager && this._msTime <= 0) {
            this._msTime = 1;
            // this.showWifiMS();
            this.lblDelay.string = dd.ws_manager.getDelayTime() + 'ms';
        }
    }
    /**
     * 显示桌子信息和界面
     * 
     * @memberof MJ_Table
     */
    showTableInfo() {
        this._cdTime = 1;
        this.lblTitle.string = dd.gm_manager.mjGameData.tableBaseVo.ruleShowDesc;
        this.lblRoomId.string = '房间号:' + dd.gm_manager.mjGameData.tableBaseVo.tableId;
        this.lblSysTime.string = dd.utils.getDateStringByTimestamp(dd.gm_manager.mjGameData.tableBaseVo.svrTime, 2);
        this.pro_Power.progress = dd.js_call_native.getBatteryLevel() / 100;
        this.lblDelay.string = dd.ws_manager.getDelayTime() + 'ms';
        //如果在空闲状态
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_IDLE) {
            this.table_wait.active = true;
            if (dd.ud_manager.mineData.accountId === dd.gm_manager.mjGameData.tableBaseVo.createPlayer) {
                this.node_disband.active = true;
                this.node_out.active = false;
            } else {
                this.node_disband.active = false;
                this.node_out.active = true;
            }
        } else {
            this.table_wait.active = false;
            this.node_disband.active = false;
            this.node_out.active = false;
        }
        this.showPlayerInfo();
    }


    /**
     * 显示玩家信息
     * 
     * @memberof MJ_Player
     */
    showPlayerInfo() {
        if (dd.gm_manager.mjGameData.seats) {
            let gameState = dd.gm_manager.mjGameData.tableBaseVo.gameState;
            for (var i = 0; i < dd.gm_manager.mjGameData.seats.length; i++) {
                let seat: SeatVo = dd.gm_manager.mjGameData.seats[i];
                let player: cc.Node = this.playerList[i];
                let playerReady = this.playerReadyList[i];
                if (seat && seat.accountId !== null && seat.accountId !== '0') {
                    player.active = true;
                    let playerScript: MJ_PlayerUI = player.getComponent('MJ_PlayerUI');
                    playerScript.showInfo(seat);
                    //如果在准备阶段或空闲阶段
                    if (gameState === MJ_GameState.STATE_TABLE_READY || gameState === MJ_GameState.STATE_TABLE_IDLE) {
                        if (seat.btState === 1) {
                            playerReady.active = true;
                        } else {
                            playerReady.active = false;
                        }
                    } else {
                        playerReady.active = false;
                    }
                } else {
                    player.active = false;
                    playerReady.active = false;
                }
            }
        }
    }

    /**
     * 根据玩家id返回玩家的座位坐标
     * 
     * @param {string} accountId 
     * @returns 
     * @memberof MJ_Player
     */
    getPlayPosById(accountId: string) {
        let index = dd.gm_manager.getIndexBySeatId(-1, accountId);
        let player = this.playerList[index];
        let pos = player.getPosition();
        return pos;
    }

    /**
     * 显示信号延时
     * 
     * @memberof MJ_Table
     */
    showWifiMS() {
        let ms = dd.ws_manager.getDelayTime();
        let type = 0;
        if (ms < 60) {
            type = 0;
            this.lblDelay.node.color = cc.Color.GREEN;
            this.imgWifi.node.color = cc.Color.GREEN;
        } else if (ms >= 60 && ms < 90) {
            type = 1;
            this.lblDelay.node.color = cc.Color.YELLOW;
            this.imgWifi.node.color = cc.Color.YELLOW;
        } else {
            type = 2;
            this.lblDelay.node.color = cc.Color.RED;
            this.imgWifi.node.color = cc.Color.RED;
        }
        this.lblDelay.string = ms + 'ms';
    }


    /**
     * 退出桌子
     * 
     * @memberof MJ_Table
     */
    sendOutGame() {
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_LEAV, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    dd.gm_manager._gmScript.quitGame();
                } else if (flag === -1) {//超时
                    cc.log(content);
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '错误提示');
                }
            });
        }
    }
    /**
     * 返回大厅
     * 
     * @memberof MJ_Table
     */
    click_btn_return() {
        dd.mp_manager.playButton();
        this.sendOutGame();
    }
    /**
     * 解散
     * 
     * @memberof MJ_Table
     */
    click_btn_disband() {
        dd.mp_manager.playButton();
        dd.ui_manager.showAlert('您确定解散房间吗？'
            , '温馨提示',
            {
                lbl_name: '确定',
                callback: () => {
                    this.sendOutGame();
                }
            }, {
                lbl_name: '取消',
                callback: () => {
                }
            }, 1);
    }

    /**
     * 邀请
     * 
     * @memberof MJ_Table
     */
    click_btn_invite() {
        dd.mp_manager.playButton();
        dd.js_call_native.wxShare(dd.config.cd.ddUrl, '豆豆麻将', '我在豆豆麻将' + dd.gm_manager.mjGameData.tableBaseVo.tableId + '号桌子，快来一起游戏吧！');
    }
    /**
     * 复制
     * 
     * @memberof MJ_Table
     */
    click_btn_copy() {
        dd.mp_manager.playButton();
        dd.js_call_native.copyToClipboard(dd.gm_manager.mjGameData.tableBaseVo.tableId.toString());
        dd.ui_manager.showTip('复制成功！');
    }
    /**
     * 准备
     * 
     * @memberof MJ_Table
     */
    click_btn_ready() {
        dd.mp_manager.playButton();
    }

    /**
     * 聊天
     * 
     * @memberof MJ_Table
     */
    click_btn_chat() {
        if (dd.gm_manager.touchTarget) return;
        dd.mp_manager.playButton();
        dd.gm_manager._gmScript.showChat();
    }
    /**
     * 设置
     * 
     * @memberof MJ_Table
     */
    click_btn_setting() {
        if (dd.gm_manager.touchTarget) return;
        dd.mp_manager.playButton();
        dd.gm_manager._gmScript.showSetting();
    }
    /**
     * 语音
     * 
     * @memberof MJ_Table
     */
    click_btn_voice() {
        dd.mp_manager.playButton();
    }

}
