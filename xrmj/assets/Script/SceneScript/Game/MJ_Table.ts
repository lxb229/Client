const { ccclass, property } = cc._decorator;

import MJ_PlayerUI from './MJ_PlayerUI';
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState } from '../../Modules/Protocol';
@ccclass
export default class MJ_Table extends cc.Component {

    @property(cc.Prefab)
    club_room_detail: cc.Prefab = null;
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
     * 语音按钮的图片
     * @type {cc.Sprite}
     * @memberof MJ_Table
     */
    @property(cc.Sprite)
    btn_voice: cc.Sprite = null;
    /**
     * 语音图片
     * @type {cc.SpriteFrame[]}
     * @memberof MJ_Table
     */
    @property([cc.SpriteFrame])
    img_voice: cc.SpriteFrame[] = [];
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

    _msTime: number = 0;
    /**
     * 语音房语音加载次数
     * 
     * @type {number}
     * @memberof MJCanvas
     */
    _voiceTimes: number = 0;
    /**
     * 是否开启语音
     * @type {boolean}
     * @memberof MJ_Table
     */
    _isOpenVoice: boolean = false;
    /**
     * 是否初始化语音房
     * @type {boolean}
     * @memberof MJ_Table
     */
    _isInitVoice: boolean = false;

    _club_room_detail: cc.Node = null;

    _isTouch: boolean = false;
    /**
     * 跳转类型 0 = 正常跳转 1 = 结算界面跳转
     * @type {number}
     * @memberof MJ_Table
     */
    _loadType: number = 0;
    /**
     * 
     * 推送消息(房间已解散通知) 回调函数
     * @memberof MJCanvas
     */
    MJ_OutPush = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        //如果在空闲等待阶段解散房间
        if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_IDLE
            && dd.gm_manager.mjGameData.tableBaseVo.createPlayer !== dd.ud_manager.mineData.accountId) {
            dd.ui_manager.showAlert('房主解散了房间！'
                , '温馨提示',
                {
                    lbl_name: '确定',
                    callback: () => {
                        this.quitGame();
                    }
                }, null, 1);
        } else {
            this.quitGame();
        }
    };
    onLoad() {
        if (dd.config.wxState === 0) {
            this.node_wx_invit.active = true;
        } else {
            this.node_wx_invit.active = false;
        }
        this.btn_voice.spriteFrame = this.img_voice[1];

        cc.systemEvent.on('cb_voiceLogin', this.cb_voiceInit, this);
        //推送消息(房间已解散通知)
        cc.systemEvent.on('MJ_OutPush', this.MJ_OutPush, this);
        this.initVoice();
    }

    onDestroy() {
        cc.systemEvent.off('cb_voiceLogin', this.cb_voiceInit, this);
        //推送消息(房间已解散通知)
        cc.systemEvent.off('MJ_OutPush', this.MJ_OutPush, this);
        cc.systemEvent.off('cb_voiceQuit', this.cb_quitRoom, this);
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
            this.showWifiMS();
        }
    }

    /**
     * 退出游戏房间，跳转到大厅
     * 
     * @memberof MJCanvas
     */
    quitGame(loadType: number = 0) {
        this._loadType = loadType;
        if (dd.ui_manager.showLoading()) {
            dd.ud_manager.mineData.tableId = 0;
            //如果初始化语音房
            if (this._isInitVoice) {
                this.setVoiceState(false);
                this._voiceTimes = 0;
                let b = dd.js_call_native.quitRoom();
            } else {
                if (loadType === 0) {
                    if (dd.gm_manager.mjGameData.tableBaseVo.corpsId !== '0') {
                        cc.director.loadScene('ClubScene', () => {
                            dd.gm_manager.destroySelf();
                            cc.sys.garbageCollect();
                        });
                    } else {
                        cc.director.loadScene('HomeScene', () => {
                            dd.gm_manager.destroySelf();
                            cc.sys.garbageCollect();
                        });
                    }
                } else {
                    cc.director.loadScene('GameResult');
                }
            }
        }
    }
    /**
     * 显示桌子信息和界面
     * 
     * @memberof MJ_Table
     */
    showTableInfo() {
        this._cdTime = 1;
        this.lblRoomId.string = '房间号:' + dd.gm_manager.mjGameData.tableBaseVo.tableId;
        this.lblSysTime.string = dd.utils.getDateStringByTimestamp(dd.gm_manager.mjGameData.tableBaseVo.svrTime, 2);
        this.pro_Power.progress = dd.js_call_native.getBatteryLevel() / 100;
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
        if (ms < 60) {
            this.imgWifi.node.color = cc.Color.GREEN;
        } else if (ms >= 60 && ms < 90) {
            this.imgWifi.node.color = cc.Color.YELLOW;
        } else {
            this.imgWifi.node.color = cc.Color.RED;
        }
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
                    this.quitGame();
                } else if (flag === -1) {//超时
                    cc.log(content);
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '错误提示');
                }
            });
        }
    }

    /**
     * 显示房间详细规则
     * @memberof ClubTableLayer
     */
    showRoomDetail() {
        let data: CorpsTableItem = {
            tableId: dd.gm_manager.mjGameData.tableBaseVo.tableId,
            ruleShowDesc: dd.gm_manager.mjGameData.tableBaseVo.ruleShowDesc,
            gameName: '',
            password: 0,
            seats: [],
        };
        if (!this._club_room_detail || !this._club_room_detail.isValid) {
            this._club_room_detail = cc.instantiate(this.club_room_detail);
            let detailScript = this._club_room_detail.getComponent('Club_Room_Detail');
            detailScript.initData(1, data, this);
            this._club_room_detail.parent = dd.ui_manager.getRootNode();
        }
    }

    /**
     * 点击解散游戏
     * 
     * @memberof Game_Setting
     */
    sendDisband() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_QUEST_DELETE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                } else if (flag === -1) {//超时
                    cc.log(content);
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '错误提示', null, null, 1);
                }
                dd.ui_manager.hideLoading();
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
                    this.sendDisband();

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
        dd.js_call_native.wxShare(dd.config.cd.ddUrl, '贤人麻将馆', '我在贤人麻将馆' + dd.gm_manager.mjGameData.tableBaseVo.tableId + '号桌子，快来一起游戏吧！');
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
        dd.gm_manager.getGMTarget().showChat();
    }
    /**
     * 设置
     * 
     * @memberof MJ_Table
     */
    click_btn_setting() {
        if (dd.gm_manager.touchTarget) return;
        dd.mp_manager.playButton();
        dd.gm_manager.getGMTarget().showSetting();
    }
    /**
     * 语音
     * 
     * @memberof MJ_Table
     */
    click_btn_voice() {
        dd.mp_manager.playButton();
        if (dd.config.voiceState === 0) {
            if (this._isInitVoice) {
                this.setVoiceState(!this._isOpenVoice);
            } else {
                this._isTouch = true;
                this.initVoice((isOpen: boolean) => {
                    if (!isOpen) dd.ui_manager.showTip('开启语音失败！');
                });
            }
        } else {
            dd.ui_manager.showTip('您未授权该应用使用语音设备，请先授权');
        }
    }

    click_btn_rule() {
        dd.mp_manager.playButton();
        this.showRoomDetail();
    }


    /**
     * 语音房初始化语音回调
     * 
     * @memberof MJCanvas
     */
    cb_voiceInit = (event: cc.Event.EventCustom) => {
        if (event.detail === 0) {
            cc.systemEvent.on('cb_voiceQuit', this.cb_quitRoom, this);
            // '进入房间成功';
            this._voiceTimes = 0;
            this._isInitVoice = true;
            this.setVoiceState(this._isTouch);

        } else {
            // '进入房间失败';
        }
    };
    /**
     * 退出语音房回调
     * @memberof MJ_Table
     */
    cb_quitRoom = (event: cc.Event.EventCustom) => {
        if (event.detail === 0) {
            if (this._loadType === 0) {
                if (dd.gm_manager.mjGameData.tableBaseVo.corpsId !== '0') {
                    cc.director.loadScene('ClubScene', () => {
                        dd.gm_manager.destroySelf();
                        cc.sys.garbageCollect();
                    });
                } else {
                    cc.director.loadScene('HomeScene', () => {
                        dd.gm_manager.destroySelf();
                        cc.sys.garbageCollect();
                    });
                }
            } else {
                cc.director.loadScene('GameResult');
            }
        } else {
            this._voiceTimes++;
            if (this._voiceTimes < 3) {
                dd.js_call_native.quitRoom();
            } else {
                dd.ui_manager.showAlert('当前网络异常，请重新启动游戏！', '温馨提示', {
                    lbl_name: '确定',
                    callback: () => {
                        cc.game.end();
                    }
                }, null, 1);
            }
        }
    }
    /**
     * 初始化语音房的语音
     * 
     * @memberof MJCanvas
     */
    initVoice(callBack?: Function) {
        if (dd.config.voiceState === 0) {
            let a = dd.js_call_native.joinTeamRoom(dd.gm_manager.mjGameData.tableBaseVo.tableId.toString());
            if (a === 0) {
                //'进入房间成功';
                if (callBack)
                    callBack(true);
            } else {
                //'进入房间失败';
                this._voiceTimes++;
                if (this._voiceTimes < 3) {
                    this.initVoice(callBack);
                } else {
                    this._voiceTimes = 0;
                    if (callBack) callBack(false);
                }
            }
        }
    }

    /**
     * 开启音频
     * 
     * @memberof MJCanvas
     */
    setVoiceState(isOpen: boolean) {
        let b = dd.js_call_native.setState(isOpen);
        if (b === 0) {
            this._isOpenVoice = isOpen;
            // '开启音频成功';
            this.btn_voice.spriteFrame = isOpen === true ? this.img_voice[0] : this.img_voice[1];
        } else {
            // '开启音频失败';
            let str = isOpen === true ? '语音房语音音频开启失败!' : '语音房语音音频关闭失败!'
            dd.ui_manager.showTip(str);
        }
    }

}
