import Alert from '../UI/Alert';
import Loading from '../UI/Loading';
import Tip from '../UI/Tip';
/**
 * 管理公共UI的类
 * 
 * @export
 * @class UIManager
 */
export default class UIManager {
    private static _instance: UIManager = null;
    private constructor() { }
    /**
     * 获取WSManager单例对象
     * 
     * @static
     * @returns {UIManager} 
     * @memberof UIManager
     */
    static getInstance(): UIManager {
        if (UIManager._instance === null) {
            UIManager._instance = new UIManager();
        }
        return UIManager._instance;
    }
    /**
     * loading框预制
     * 
     * @private
     * @type {cc.Prefab}
     * @memberof UIManager
     */
    private p_loading: cc.Prefab = null;
    /**
     * Loading框是否显示
     */
    private isShowLoading: boolean = false;
    /**
     * 警示框预制
     * 
     * @private
     * @type {cc.Prefab}
     * @memberof UIManager
     */
    private p_alert: cc.Prefab = null;
    /**
     * 漂浮框预制
     * 
     * @private
     * @type {cc.Prefab}
     * @memberof UIManager
     */
    private p_tip: cc.Prefab = null;
    /**
     * 场景切换前的回调
     * 
     * @memberof UIManager
     */
    private cb_before_scene_loading: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        this.isShowLoading = false;
    }
    /**
     * 游戏每帧刷新前的回调
     * 
     * @memberof UIManager
     */
    private cb_befor_update: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {

    }
    /**
     * app切换到后台的回调
     * 
     * @memberof UIManager
     */
    private cb_app_hide: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        if (!cc.game.isPaused()) {
            cc.game.pause();
        }
    }
    /**
     * app切换到前台的回调
     * 
     * @memberof UIManager
     */
    private cb_app_show: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        if (cc.game.isPaused()) {
            cc.game.resume();
        }
    }
    /**
     * 初始化
     * 
     * @memberof UIManager
     */
    async initUI(): Promise<void> {
        //注册全局事件
        cc.director.on(cc.Director.EVENT_BEFORE_SCENE_LOADING, this.cb_before_scene_loading, this);
        cc.director.on(cc.Director.EVENT_BEFORE_UPDATE, this.cb_befor_update, this);
        cc.game.on(cc.game.EVENT_HIDE, this.cb_app_hide, this);
        cc.game.on(cc.game.EVENT_SHOW, this.cb_app_show, this);

        await this.initLoading();
        await this.initAlert();
        await this.initTip();
    }
    /**
     * 加载Loading预制
     * 
     * @returns {Promise<void>} 
     * @memberof UIManager
     */
    initLoading(): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            cc.loader.loadRes(
                "Prefab/Loading",
                cc.Prefab,
                (err, prefab) => {
                    if (err) {
                        reject(err.message);
                        return;
                    }
                    this.p_loading = prefab;
                    resolve();
                });
        });
    }
    /**
     * 加载Alert预制
     * 
     * @returns {Promise<void>} 
     * @memberof UIManager
     */
    initAlert(): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            cc.loader.loadRes(
                "Prefab/Alert",
                cc.Prefab,
                (err, prefab) => {
                    if (err) {
                        reject(err.message);
                        return;
                    }
                    this.p_alert = prefab;
                    resolve();
                });
        });
    }
    /**
     * 加载Tip预制
     * 
     * @returns {Promise<void>} 
     * @memberof UIManager
     */
    initTip(): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            cc.loader.loadRes(
                "Prefab/Tip",
                cc.Prefab,
                (err, prefab) => {
                    if (err) {
                        reject(err.message);
                        return;
                    }
                    this.p_tip = prefab;
                    resolve();
                });
        });
    }
    /**
     * 获取当前显示的Canvas节点
     * 
     * @returns {cc.Node} 
     * @memberof UIManager
     */
    getCanvasNode(): cc.Node {
        return cc.director.getScene().getChildByName('Canvas');
    }
    /**
     * 获取当前场景下的RootNode节点
     * 
     * @returns {cc.Node} 
     * @memberof UIManager
     */
    getRootNode(): cc.Node {
        return cc.find('RootNode', this.getCanvasNode());
    }
    /**
     * 显示Loading框
     * 
     * @param {string} [msg] 提示信息内容
     * @return {boolean} 是否显示成功
     * @memberof UIManager
     */
    showLoading(msg: string = '正在加载，请稍后'): boolean {
        if (this.isShowLoading) return false;
        this.isShowLoading = true;
        let node = cc.instantiate(this.p_loading);
        node.zIndex = 99;
        node.parent = this.getCanvasNode();
        let loading: Loading = node.getComponent('Loading') as Loading;
        loading.setMsg(msg);
        return true;
    }
    /**
     * 隐藏Loading框
     * 
     * @memberof UIManager
     */
    hideLoading() {
        if (this.isShowLoading) {
            this.isShowLoading = false;
            let node = cc.find('Loading', this.getCanvasNode());
            if (node && node.isValid) {
                node.removeFromParent(true);
                node.destroy();
            }
        }
    }
    /**
     * 显示警示框
     * 
     * @param {string} msg 具体信息内容
     * @param {string} title 标题
     * @param {btn_obj} [obj_yes] 点击同意按钮事件回调
     * @param {btn_obj} [obj_no] 点击拒绝按钮事件回调
     * @param {number} [ha=0] 文字对齐方式 0=左对齐 1=居中 2=右对齐
     * @memberof UIManager
     */
    showAlert(msg: string, title: string, obj_yes?: btn_obj, obj_no?: btn_obj, ha: number = 1) {
        this.hideLoading();
        let node = cc.find('Alert', this.getCanvasNode());
        if (node && node.isValid) {
            node.removeFromParent(true);
            node.destroy();
        }
        node = cc.instantiate(this.p_alert);
        node.parent = this.getCanvasNode();
        let alert: Alert = node.getComponent('Alert') as Alert;
        alert.showAlert(msg, title, obj_yes, obj_no, ha);
    }
    /**
     * 显示漂浮框
     * 
     * @param {string} msg 提示信息内容
     * @param {number} [sTime=1] 前段动画时间 
     * @param {number} [mTime=2] 悬浮时间
     * @param {number} [eTime=1] 后端动画时间
     * @memberof UIManager
     */
    showTip(msg: string, sTime: number = 1, mTime: number = 2, eTime: number = 1) {
        let node = cc.instantiate(this.p_tip);
        node.zIndex = 99;
        node.parent = this.getCanvasNode();
        let tip: Tip = node.getComponent('Tip') as Tip;
        tip.showTip(msg, sTime, mTime, eTime);
    }
    /**
     * 清理销毁
     * 
     * @memberof WSManager
     */
    destroySelf(): void {
        //注销全局事件
        cc.director.off(cc.Director.EVENT_BEFORE_SCENE_LOADING, this.cb_before_scene_loading, this);
        cc.director.off(cc.Director.EVENT_BEFORE_UPDATE, this.cb_befor_update, this);
        cc.game.off(cc.game.EVENT_HIDE, this.cb_app_hide, this);
        cc.game.off(cc.game.EVENT_SHOW, this.cb_app_show, this);
        this.isShowLoading = false;
        //释放loading预制
        this.release(this.p_loading);
        this.release(this.p_alert);
        this.release(this.p_tip);


        UIManager._instance = null;
    }
    /**
     * 释放资源及其所有的引用
     * 
     * @private
     * @param {(cc.Asset | cc.RawAsset | string)} owner 需要释放的资源
     * @memberof IMGManager
     */
    private release(owner: cc.Asset | cc.RawAsset | string): void {
        let deps = cc.loader.getDependsRecursively(owner);
        cc.loader.release(deps);
    }
}