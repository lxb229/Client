const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState } from '../../Modules/Protocol';
@ccclass
export default class Game_Setting extends cc.Component {

    /**
     * 报牌音列表
     * 
     * @type {cc.Toggle[]}
     * @memberof Game_Setting
     */
    @property([cc.Toggle])
    toggle_bp_list: cc.Toggle[] = [];

    /**
     * 注销游戏
     * 
     * @type {cc.Node}
     * @memberof Game_Setting
     */
    @property(cc.Node)
    btn_logout: cc.Node = null;

    /**
     * 解散房间
     * 
     * @type {cc.Node}
     * @memberof Game_Setting
     */
    @property(cc.Node)
    btn_disband: cc.Node = null;

    /**
     * 解散房间
     * 
     * @type {cc.Node}
     * @memberof Game_Setting
     */
    @property(cc.Node)
    btn_return: cc.Node = null;

    @property(cc.Sprite)
    img_effect: cc.Sprite = null;

    @property(cc.Sprite)
    img_music: cc.Sprite = null;

    @property([cc.SpriteFrame])
    on_off_list: cc.SpriteFrame[] = [];
    /**
     * 显示类型
     * 
     * @type {number} -1大厅 0聊天房 1普通房
     * @memberof Game_Setting
     */
    _showType: number = 0;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
            dd.ui_manager.isShowPopup = true;
            if (this._showType === -1) {
                this.node.removeFromParent(true);
                this.node.destroy();
            } else {
                this.node.active = false;
            }
        }, this);
    }

    update(dt) {
        if (dd.ud_manager && dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.tableBaseVo) {
            if (dd.gm_manager.mjGameData.tableBaseVo.currGameNum > 1
                || dd.gm_manager.mjGameData.tableBaseVo.gameState > MJ_GameState.STATE_TABLE_IDLE) {
                this.btn_disband.active = true;
                this.btn_return.active = false;
            } else {
                if (dd.ud_manager.mineData.accountId === dd.gm_manager.mjGameData.tableBaseVo.createPlayer) {
                    this.btn_disband.active = true;
                    this.btn_return.active = false;
                } else {
                    this.btn_disband.active = false;
                    this.btn_return.active = true;
                }
            }
        }
    }
    /**
     * 初始化显示
     * 
     * @param {number} type 
     * @memberof Game_Setting
     */
    initData(type: number) {
        this._showType = type;
        this.img_effect.spriteFrame = dd.mp_manager.audioSetting.isEffect === true ? this.on_off_list[0] : this.on_off_list[1];
        this.img_music.spriteFrame = dd.mp_manager.audioSetting.isMusic === true ? this.on_off_list[0] : this.on_off_list[1];
        switch (this._showType) {
            case -1:
                this.btn_disband.active = false;
                this.btn_logout.active = true;
                this.btn_return.active = false;
                break;
            case 0:
                this.btn_disband.active = true;
                this.btn_logout.active = false;
                this.btn_return.active = false;
                break;
            case 1:
                this.btn_disband.active = true;
                this.btn_logout.active = false;
                this.btn_return.active = false;
                break;
            default:
                break;
        }
        for (var i = 0; i < this.toggle_bp_list.length; i++) {
            if (dd.mp_manager.audioSetting.language === i) {
                this.toggle_bp_list[i].isChecked = true;
            } else {
                this.toggle_bp_list[i].isChecked = false;
            }
        }
    }

    /**
     * 点击音效按钮
     * 
     * @memberof Game_Setting
     */
    click_btn_effect() {
        dd.mp_manager.playButton();
        dd.mp_manager.audioSetting.isEffect = !dd.mp_manager.audioSetting.isEffect;
        this.img_effect.spriteFrame = dd.mp_manager.audioSetting.isEffect === true ? this.on_off_list[0] : this.on_off_list[1];
        dd.mp_manager.saveMPSetting();
    }

    /**
     * 点击音乐按钮
     * 
     * @memberof Game_Setting
     */
    click_btn_music() {
        dd.mp_manager.playButton();
        dd.mp_manager.audioSetting.isMusic = !dd.mp_manager.audioSetting.isMusic;
        dd.mp_manager.saveMPSetting();
        this.img_music.spriteFrame = dd.mp_manager.audioSetting.isMusic === true ? this.on_off_list[0] : this.on_off_list[1];
        if (!dd.mp_manager.audioSetting.isMusic) {
            dd.mp_manager.stopBackGround();
        } else {
            dd.mp_manager.playBackGround();
        }
    }

    /**
     * 点击报牌音
     * 
     * @memberof Game_Setting
     */
    click_toggle_bp(event, type: string) {
        dd.mp_manager.playButton();
        cc.log('---报牌音--' + type);
        dd.mp_manager.audioSetting.language = Number(type);
        dd.mp_manager.saveMPSetting();
    }

    /**
    * 点击退出游戏
    * 
    * @memberof PT_Setting
    */
    click_btn_logout() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading('正在注销，请稍后')) {
            let obj = { 'accountId': dd.ud_manager.mineData.accountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_LOGIN_OUT, msg, (flag: number, content?: any) => {
                dd.ws_manager.disconnect(() => {
                    dd.destroy();
                    let db = cc.sys.localStorage;
                    if (db.getItem('TokenInfo')) {//验证是否有授权过
                        db.removeItem('TokenInfo');
                    }
                    cc.sys.garbageCollect();
                    cc.game.restart();
                });
            });
        }
    }

    /**
     * 点击解散游戏
     * 
     * @memberof Game_Setting
     */
    click_btn_disband() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_QUEST_DELETE, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    dd.ui_manager.isShowPopup = true;
                    if (this._showType === -1) {
                        this.node.removeFromParent(true);
                        this.node.destroy();
                    } else {
                        this.node.active = false;
                    }
                    //如果在空闲等待阶段解散房间
                    if (dd.gm_manager.mjGameData.tableBaseVo.gameState === MJ_GameState.STATE_TABLE_IDLE) {
                        this.quitGame();
                    }
                } else if (flag === -1) {//超时
                    cc.log(content);
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '错误提示', null, null, 1);
                }
            });
        }
    }

    /**
    * 退出游戏房间，跳转到大厅
    * 
    * @memberof Game_Setting
    */
    quitGame() {
        if (dd.ui_manager.showLoading()) {
            let canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
            if (canvasTarget) {
                canvasTarget._isLoad = true;
            }
            dd.ud_manager.mineData.tableId = 0;

            //如果是语音房间，退出的时候，要退出语音
            if (dd.gm_manager && dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.tableBaseVo
                && dd.gm_manager.mjGameData.tableBaseVo.tableChatType === 1) {
                let b = dd.js_call_native.quitRoom();
                if (b === 0) {
                    // '离开房间成功';
                } else {
                    // '离开房间失败';
                }
            }
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
        }
    }
    /**
     * 退出游戏
     * @memberof Game_Setting
     */
    click_btn_outGame() {
        let canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        if (canvasTarget) {
            dd.mp_manager.playButton();
            canvasTarget.sendOutGame();
        }
    }
    /**点击退出按钮
     * 
     * 
     * @memberof Game_Setting
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        if (this._showType === -1) {
            this.node.removeFromParent(true);
            this.node.destroy();
        } else {
            this.node.active = false;
        }
    }
}
