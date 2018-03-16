import * as JSCallNative from './JSCallNative';
import * as NativeCallJS from './NativeCallJS';
import * as Utils from './Utils';
import * as Config from './Config';
import WSManager from './WSManager';
import IMGManager from './IMGManager';
import UDManager from './UDManager';
import GMManager from './GMManager';
import UIManager from './UIManager';
import ENCManger from './ENCManager';
import MPManager from './MPManager';
import { Protocol, MJ_Game_Type } from './Protocol';

/**
 * js调用native管理对象
 */
export const js_call_native = JSCallNative;
/**
 * native调用js管理对象
 */
export const native_call_js = NativeCallJS;
/**
 * 常用方法管理对象
 */
export const utils = Utils;
/**
 * 配置管理对象
 */
export const config = Config;
/**
 * 协议号枚举
 */
export const protocol = Protocol;
/**
 * ws管理单例
 */
export let ws_manager: WSManager = null;
/**
 * 图片管理单例
 */
export let img_manager: IMGManager = null;
/**
 * 用户管理单例
 */
export let ud_manager: UDManager = null;
/**
 * 游戏管理单例
 */
export let gm_manager: GMManager = null;
/**
 * UI管理单例
 */
export let ui_manager: UIManager = null;
/**
 * 加密管理单例
 */
export let enc_manager: ENCManger = null;
/**
 * 音频管理类
 */
export let mp_manager: MPManager = null;
/**
 * 意外断线通知回调
 */
export let cb_diconnect: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
    // //0意外,1心跳超时,2未知
    let accountId = null;
    if (ud_manager && ud_manager.mineData) {
        accountId = ud_manager.mineData.accountId;
    }

    gm_manager.destroySelf();
    ud_manager.destroySelf();
    gm_manager = null;
    ud_manager = null;

    ui_manager.hideLoading();
    ui_manager.showLoading('正在重连,请稍后');
    setTimeout(async () => {
        ws_manager = WSManager.getInstance();
        gm_manager = GMManager.getInstance();
        ud_manager = UDManager.getInstance();
        try {
            let sceneName = cc.director.getScene().name;
            await ws_manager.connect(config.wsUrl);//连接服务器
            if (sceneName !== 'LoginScene' && accountId) {
                //通过accountId获取用户信息
                let obj: any = {};
                obj.accountId = accountId;
                ws_manager.sendMsg(
                    protocol.ACCOUNT_LOGIN_ACCOUNTID,
                    JSON.stringify(obj),
                    (flag: number, content?: any) => {
                        if (flag === 0) {//登录服务器成功
                            ud_manager.mineData = content as UserData;
                            ws_manager.setLoginState(true);
                            //判断是否在战斗中
                            if (ud_manager && ud_manager.mineData && ud_manager.mineData.tableId !== 0) {
                                let obj = { 'tableId': ud_manager.mineData.tableId };
                                let msg = JSON.stringify(obj);
                                ws_manager.sendMsg(protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                                    if (flag === 0) {//成功
                                        gm_manager.mjGameData = content as MJGameData;
                                        gm_manager.replayMJ = 0;
                                        switch (gm_manager.mjGameData.tableBaseVo.cfgId) {
                                            case MJ_Game_Type.GAME_TYPE_XZDD:
                                                if (sceneName !== 'MJScene') {
                                                    cc.director.loadScene('MJScene');
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            case MJ_Game_Type.GAME_TYPE_SRLF:
                                            case MJ_Game_Type.GAME_TYPE_SRSF:
                                                if (sceneName !== 'SRMJScene') {
                                                    cc.director.loadScene('SRMJScene');
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            case MJ_Game_Type.GAME_TYPE_LRLF:
                                                if (sceneName !== 'LRMJScene') {
                                                    cc.director.loadScene('LRMJScene');
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            case MJ_Game_Type.GAME_TYPE_MYMJ:
                                                if (sceneName !== 'MYMJScene') {
                                                    cc.director.loadScene('MYMJScene');
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            case MJ_Game_Type.GAME_TYPE_ZGMJ:
                                                if (sceneName !== 'ZGMJScene') {
                                                    if (gm_manager.mjGameData.seats.length === 3) {
                                                        cc.director.loadScene('ZG3MJScene');
                                                    } else if (gm_manager.mjGameData.seats.length === 4) {
                                                        cc.director.loadScene('ZG4MJScene');
                                                    } else { }
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            case MJ_Game_Type.GAME_TYPE_LSMJ:
                                                if (sceneName !== 'LSMJScene') {
                                                    cc.director.loadScene('LSMJScene');
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            case MJ_Game_Type.GAME_TYPE_NCMJ:
                                                if (sceneName !== 'NCMJScene') {
                                                    cc.director.loadScene('NCMJScene');
                                                } else {
                                                    ui_manager.getCanvasNode().emit('diconnect_update');
                                                    ui_manager.hideLoading();
                                                }
                                                break;
                                            default:
                                                break;
                                        }

                                    }
                                    else if (flag === -1) {//超时
                                        errAlert();
                                    } else {//失败,content是一个字符串
                                        if (sceneName !== 'HomeScene') {
                                            cc.director.loadScene('HomeScene', () => {
                                                ui_manager.showTip('桌子已解散!');
                                            });
                                        } else {
                                            ui_manager.hideLoading();
                                        }
                                    }
                                });
                            } else {
                                let sceneName = cc.director.getScene().name;
                                if (sceneName === 'MJScene' || sceneName === 'SRMJScene' || sceneName === 'LRMJScene'
                                    || sceneName === 'MYMJScene' || sceneName === 'ZG3MJScene' || sceneName === 'ZG4MJScene') {
                                    cc.director.loadScene('HomeScene', () => {
                                        ui_manager.showTip('桌子已解散!');
                                    });
                                } else {
                                    ui_manager.hideLoading();
                                }
                            }
                        } else {//登录服务器失败
                            errAlert();
                        }
                    });
            } else {
                ui_manager.hideLoading();
            }
        } catch (err) {
            errAlert();
        }
    }, 100);
}

export function errAlert() {
    let yes: btn_obj = {
        lbl_name: '确定',
        callback: () => {
            cc.game.end();
        }
    }
    ui_manager.showAlert('连接服务器失败，请重新启动游戏！', '错误提示', yes);
    gm_manager.destroySelf();
    ud_manager.destroySelf();
    ws_manager.destroySelf();
    gm_manager = null;
    ud_manager = null;
    ws_manager = null;
}

/**
 * 初始化单例对象
 * 
 * @export
 */
export function init(): void {
    ws_manager = WSManager.getInstance();
    img_manager = IMGManager.getInstance();
    ud_manager = UDManager.getInstance();
    ui_manager = UIManager.getInstance();
    enc_manager = ENCManger.getInstance();
    gm_manager = GMManager.getInstance();
    mp_manager = MPManager.getInstance();

    cc.systemEvent.on('cb_diconnect', cb_diconnect, this);
}
/**
 * 释放所有单例对象
 * 
 * @export
 */
export function destroy() {
    img_manager.destroySelf();
    ws_manager.destroySelf();
    ui_manager.destroySelf();
    ud_manager.destroySelf();
    gm_manager.destroySelf();
    mp_manager.destroySelf();

    img_manager = null;
    ws_manager = null;
    ui_manager = null;
    ud_manager = null;
    gm_manager = null;
    mp_manager = null;

    cc.systemEvent.off('cb_diconnect', cb_diconnect, this);
}
