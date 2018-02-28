
import * as Utils from './Utils';
import * as Config from './Config';
import WSManager from './WSManager';
import IMGManager from './IMGManager';
import UDManager from './UDManager';
import GMManager from './GMManager';
import UIManager from './UIManager';
import ENCManger from './ENCManager';
import MPManager from './MPManager';
import CardManager from './CardManager';
import { Protocol } from './Protocol';

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
 * 扑克牌管理类
 */
export let card_manager: CardManager = null;
/**
 * 意外断线通知回调
 */
export let cb_diconnect: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
    let yes: btn_obj = {
        lbl_name: '确定',
        callback: () => {
            destroy();
            cc.sys.garbageCollect();
            cc.game.restart();
        }
    }
    ui_manager.showAlert('连接断开，请确认您的网络后点击确定按钮重新连接！', '错误提示', yes);
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
    card_manager = CardManager.getInstance();
    cc.systemEvent.on('cb_diconnect', cb_diconnect);
}
/**
 * 释放所有单例对象
 * 
 * @export
 */
export function destroy() {
    img_manager.destroySelf();
    ud_manager.destroySelf();
    ws_manager.destroySelf();
    ui_manager.destroySelf();
    ud_manager.destroySelf();
    gm_manager.destroySelf();
    mp_manager.destroySelf();
    card_manager.destroySelf();

    img_manager = null;
    ud_manager = null;
    ws_manager = null;
    ui_manager = null;
    ud_manager = null;
    gm_manager = null;
    mp_manager = null;
    card_manager = null;

    cc.systemEvent.off('cb_diconnect', cb_diconnect);
}
