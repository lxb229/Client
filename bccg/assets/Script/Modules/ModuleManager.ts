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
import * as Enum from './Protocol';
import NodeManager from './NodeManager';

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
export const protocol = Enum.Protocol;
export const hot_key = Enum.HotKey;
export const card_type = Enum.CardType;
export const game_state = Enum.GameState;
export const suit = Enum.Suit;
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
 * 节点管理类
 */
export let node_manager: NodeManager = null;

export function errAlert() {
    ui_manager.showAlert(2, '连接服务器失败，请重新启动游戏！', () => {
        cc.game.end();
    });
    gm_manager.destroySelf();
    ud_manager.destroySelf();
    ws_manager.destroySelf();
    gm_manager = null;
    ud_manager = null;
    ws_manager = null;
}
/**
 * 意外断线通知回调
 */
export let cb_diconnect: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
    //0意外,1心跳超时,2未知
    let accountId = null;
    if (ud_manager && ud_manager.account_mine) {
        accountId = ud_manager && ud_manager.account_mine.accountId;
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
                ws_manager.sendMsg(
                    protocol.ACCOUNT_LOGIN_ACCOUNTID,
                    JSON.stringify({ accountId: accountId }),
                    (flag: number, content?: any) => {
                        if (flag === 0) {//登录服务器成功
                            ud_manager.account_mine = content as AccountData;
                            ws_manager.setLoginState(true);
                            //判断是否在战斗中
                            if (ud_manager.account_mine && ud_manager.account_mine.gameDataAttribVo && ud_manager.account_mine.gameDataAttribVo.tableId !== 0) {
                                //获取当前最新的桌子数据
                                ws_manager.sendMsg(
                                    protocol.DZPKER_TABLE_GET_TABLE_INFO,
                                    JSON.stringify({ tableId: ud_manager.account_mine.gameDataAttribVo.tableId }),
                                    (flag: number, content?: any) => {
                                        if (flag === 0) {
                                            gm_manager.setTableData(content as TableData);
                                            if (sceneName !== 'GameScene') {
                                                cc.director.loadScene('GameScene', () => {
                                                    ui_manager.showTip('重连成功!');
                                                });
                                            } else {
                                                ui_manager.showTip('重连成功!');
                                            }
                                        } else {
                                            errAlert();
                                        }
                                    });
                            } else {
                                if (sceneName === 'GameScene') {
                                    cc.director.loadScene('HomeScene', () => {
                                        ui_manager.showTip('重连成功!');
                                    });
                                } else {
                                    ui_manager.showTip('重连成功!');
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
    node_manager = NodeManager.getInstance();

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
    node_manager.destroySelf();

    img_manager = null;
    ws_manager = null;
    ui_manager = null;
    ud_manager = null;
    gm_manager = null;
    mp_manager = null;
    node_manager = null;

    cc.systemEvent.off('cb_diconnect', cb_diconnect, this);
}
