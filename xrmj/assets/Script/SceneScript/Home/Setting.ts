
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import { MJ_GameState } from '../../Modules/Protocol';

@ccclass
export default class Setting extends cc.Component {
    /**
     * 解散按钮
     * @type {cc.Node}
     * @memberof Setting
     */
    @property(cc.Node)
    btn_disband: cc.Node = null;
    /**
     * 注销按钮
     * @type {cc.Node}
     * @memberof Setting
     */
    @property(cc.Node)
    btn_logout: cc.Node = null;
    /**
     * 前往官网
     * @type {cc.Node}
     * @memberof Setting
     */
    @property(cc.Node)
    btn_url: cc.Node = null;
    /**
     * 爆牌音选择节点列表
     * @type {cc.Toggle[]}
     * @memberof Setting
     */
    @property([cc.Toggle])
    toggle_bp_list: cc.Toggle[] = [];

    @property(cc.Sprite)
    switch_effect: cc.Sprite = null;

    @property(cc.Sprite)
    switch_music: cc.Sprite = null;

    @property([cc.SpriteFrame])
    on_off_list: cc.SpriteFrame[] = [];
    onLoad() {
        this.switch_effect.spriteFrame = dd.mp_manager.audioSetting.isEffect === true ? this.on_off_list[0] : this.on_off_list[1];
        this.switch_music.spriteFrame = dd.mp_manager.audioSetting.isMusic === true ? this.on_off_list[0] : this.on_off_list[1];
        for (var i = 0; i < this.toggle_bp_list.length; i++) {
            if (dd.mp_manager.audioSetting.language === i) {
                this.toggle_bp_list[i].isChecked = true;
            } else {
                this.toggle_bp_list[i].isChecked = false;
            }
        }

        if (cc.director.getScene().name === 'SettingScene') {
            if (this.btn_disband) this.btn_disband.active = false;
            this.btn_logout.active = true;
            if (cc.sys.isNative && cc.sys.isMobile) {
                this.btn_url.active = true;
            } else {
                this.btn_url.active = false;
            }
        } else {
            if (this.btn_disband) {
                //如果是第一局,并且在准备阶段
                if (dd.gm_manager.mjGameData.tableBaseVo.currGameNum === 1
                    && dd.gm_manager.mjGameData.tableBaseVo.gameState <= MJ_GameState.STATE_TABLE_FAPAI) {
                    this.btn_disband.active = false;
                } else {
                    this.btn_disband.active = true;
                }
            }
            if (this.btn_url) this.btn_url.active = false;
            this.btn_logout.active = false;
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
     * 点击音效按钮
     * 
     * @memberof Game_Setting
     */
    click_btn_effect() {
        dd.mp_manager.playButton();
        dd.mp_manager.audioSetting.isEffect = !dd.mp_manager.audioSetting.isEffect;
        this.switch_effect.spriteFrame = dd.mp_manager.audioSetting.isEffect === true ? this.on_off_list[0] : this.on_off_list[1];
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
        this.switch_music.spriteFrame = dd.mp_manager.audioSetting.isMusic === true ? this.on_off_list[0] : this.on_off_list[1];
        if (!dd.mp_manager.audioSetting.isMusic) {
            dd.mp_manager.stopBackGround();
        } else {
            dd.mp_manager.playBackGround();
        }
    }

    /**
     * 注销
     * @memberof Setting
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
     * 官网
     * @memberof Setting
     */
    click_btn_netUrl() {
        dd.mp_manager.playButton();
        dd.js_call_native.openBrowser(dd.config.cd.ddUrl);
    }
    /**
     * 退出游戏
     * @memberof Setting
     */
    click_btn_gameout() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_LEAV, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.director.loadScene('HomeScene', () => {
                        dd.gm_manager.destroySelf();
                        dd.ud_manager.mineData.tableId = 0;
                        cc.sys.garbageCollect();
                    });
                } else if (flag === -1) {//超时
                    cc.log(content);
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '错误提示');
                    dd.ui_manager.hideLoading();
                }
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
                if (flag === 0) {//成功
                    this.node.removeFromParent(true);
                    this.node.destroy();
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
     * 退出
     * @memberof Setting
     */
    click_btn_out(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0':
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('HomeScene');
                }
                break;
            case '1':
                this.node.removeFromParent(true);
                this.node.destroy();
                break;
            default:
                break;
        }
    }
}
