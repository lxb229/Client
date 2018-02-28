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
     * 是否处于场景切换中
     * 
     * @type {boolean}
     * @memberof UIManager
     */
    isLoadingScence: boolean = false;

    /**
     * loading 节点
     * 
     * @private
     * @type {cc.Node}
     * @memberof UIManager
     */
    private loadingNode: cc.Node = null;
    /**
     * tip 节点
     * 
     * @private
     * @type {cc.Node}
     * @memberof UIManager
     */
    private tipNode: cc.Node = null;
    /**
     * alert 节点
     * 
     * @private
     * @type {cc.Node}
     * @memberof UIManager
     */
    private alertNode: cc.Node = null;
    /**
     * alert节点里的确定按钮点击回调事件
     * 
     * @private
     * @memberof UIManager
     */
    private cb_yes: () => void = null;
    /**
     * alert节点里的取消按钮点击回调事件
     * 
     * @private
     * @memberof UIManager
     */
    private cb_no: () => void = null;
    /**
     * 场景切换前的回调
     * 
     * @memberof UIManager
     */
    private cb_before_scene_loading: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        this.isLoadingScence = true;
        this.hideLoading();
    }


    private cb_after_scene_loading: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        this.isLoadingScence = false;
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
        cc.director.on(cc.Director.EVENT_AFTER_SCENE_LAUNCH, this.cb_after_scene_loading, this);
        cc.director.on(cc.Director.EVENT_BEFORE_UPDATE, this.cb_befor_update, this);
        cc.game.on(cc.game.EVENT_HIDE, this.cb_app_hide, this);
        cc.game.on(cc.game.EVENT_SHOW, this.cb_app_show, this);
        this.loadingNode = await this.loadPrefabToNode('Loading');
        this.tipNode = await this.loadPrefabToNode('Tip');
        this.alertNode = await this.loadPrefabToNode('Alert');
    }
    /**
     * 加载prefab,返回实例化节点Node
     * 
     * @param {string} name 
     * @returns {Promise<cc.Node>} 
     * @memberof UIManager
     */
    loadPrefabToNode(name: string): Promise<cc.Node> {
        let path = 'Prefab/' + name;
        return new Promise<cc.Node>((resolve, reject) => {
            cc.loader.loadRes(path, cc.Prefab, (err: Error, prefab: cc.Prefab) => {
                if (err) {
                    reject(err.message);
                } else {
                    let node = cc.instantiate(prefab);
                    cc.loader.release(prefab);
                    resolve(node);
                }
            });
        });
    }
    /**
     * 显示Loading框
     * 
     * @param {string} [msg='正在加载,请稍后'] 
     * @memberof UIManager
     */
    showLoading(msg: string = '正在加载,请稍后'): boolean {
        if (this.isLoadingScence) return false;
        if (this.loadingNode && this.loadingNode.isValid) {
            this.loadingNode.parent = this.getCanvasNode();
            this.loadingNode.setLocalZOrder(this.loadingNode.parent.childrenCount - 1);
            this.loadingNode.getComponent(cc.Animation).setCurrentTime(0);
            cc.find('layout/lbl_msg', this.loadingNode).getComponent(cc.Label).string = msg;
        } else {
            cc.error('loadingNode不存在');
        }
        return true;
    }
    /**
     * 隐藏loading框
     * 
     * @memberof UIManager
     */
    hideLoading() {
        if (this.loadingNode && this.loadingNode.parent) {
            this.loadingNode.removeFromParent();
        }
    }
    /**
     * 显示飘框提示
     * 
     * @param {string} msg 提示内容
     * @param {number} [distance=400] 飘框需要移动的距离
     * @param {number} [time1=0.5] 
     * @param {number} [time2=1] 
     * @param {number} [time3=0.5] 
     * @memberof UIManager
     */
    showTip(msg: string, distance: number = 400, time1: number = 0.5, time2: number = 1, time3: number = 0.5) {
        this.hideLoading();
        if (this.tipNode) {
            let tip = cc.instantiate(this.tipNode);
            tip.parent = this.getCanvasNode();
            tip.setLocalZOrder(tip.parent.childrenCount - 1);
            cc.find('layout/lbl_msg', tip).getComponent(cc.Label).string = msg;
            let layout = cc.find('layout', tip);
            layout.setPosition(0, -distance * 0.5);
            let action1 = cc.spawn(cc.moveTo(time1, cc.p(0, 0)), cc.fadeIn(time1));
            let action3 = cc.spawn(cc.moveTo(time3, cc.p(0, distance)), cc.fadeOut(time3));
            let endAction = cc.callFunc(() => {
                tip.removeFromParent();
                tip.destroy();
            }, this);
            layout.runAction(cc.sequence(action1, cc.delayTime(1), action3, endAction));
        } else {
            cc.error('tipNode不存在');
        }
    }
    /**
     * 显示弹出框
     * 
     * @param {number} type 1是温馨提示,2是错误提示
     * @param {string} msg 文本内容(富文本)
     * @param {Function} [yes] 点击确定后的回调方法
     * @param {Function} [no] 点击取消后的回调方法
     * @memberof UIManager
     */
    showAlert(type: number, msg: string, yes?: () => void, no?: () => void) {
        this.hideLoading();
        this.hideAlert();
        if (this.alertNode) {
            if (type === 1) {
                cc.find('box/wxts', this.alertNode).active = true;
                cc.find('box/cwts', this.alertNode).active = false;
            } else {
                cc.find('box/wxts', this.alertNode).active = false;
                cc.find('box/cwts', this.alertNode).active = true;
            }
            cc.find('box/board_msg/rt_msg', this.alertNode).getComponent(cc.RichText).string = msg;
            let btn_yes = cc.find('box/layout/btn_yes', this.alertNode);
            btn_yes.active = true;
            btn_yes.on(cc.Node.EventType.TOUCH_END, this.cb_click, this);
            if (yes) this.cb_yes = yes;
            let btn_no = cc.find('box/layout/btn_no', this.alertNode);
            if (no) {
                this.cb_no = no;
                btn_no.active = true;
                btn_no.on(cc.Node.EventType.TOUCH_END, this.cb_click, this);
            } else {
                btn_no.active = false;
            }
            this.alertNode.parent = this.getCanvasNode();
            this.alertNode.setLocalZOrder(this.alertNode.parent.childrenCount - 1);
        } else {
            cc.error('alertNode不存在');
        }
    }
    /**
     * alert框按钮点击事件
     * 
     * @private
     * @param {cc.Event.EventTouch} event 
     * @memberof UIManager
     */
    private cb_click(event: cc.Event.EventTouch) {
        let btn_yes = cc.find('box/layout/btn_yes', this.alertNode);
        let btn_no = cc.find('box/layout/btn_no', this.alertNode);
        if (btn_yes === event.currentTarget && this.cb_yes) {
            this.cb_yes();
        }
        if (btn_no === event.currentTarget && this.cb_no) {
            this.cb_no();
        }
        this.hideAlert();
    }
    /**
     * 隐藏Alert框
     * 
     * @memberof UIManager
     */
    hideAlert() {
        if (this.alertNode && this.alertNode.parent) {
            this.alertNode.removeFromParent();
        }
        cc.find('box/wxts', this.alertNode).active = false;
        cc.find('box/cwts', this.alertNode).active = false;
        cc.find('box/board_msg/rt_msg', this.alertNode).getComponent(cc.RichText).string = '';
        let btn_yes = cc.find('box/layout/btn_yes', this.alertNode);
        btn_yes.off(cc.Node.EventType.TOUCH_END, this.cb_click, this);
        this.cb_yes = null;
        btn_yes.active = false;
        let btn_no = cc.find('box/layout/btn_no', this.alertNode);
        btn_no.off(cc.Node.EventType.TOUCH_END, this.cb_click, this);
        btn_no.active = false;
        this.cb_no = null;
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
     * 适配iPhone X
     * 
     * @memberof UIManager
     */
    fixiPhoneX() {
        if (cc.sys.isNative && cc.sys.platform == cc.sys.IPHONE) {
            let size = cc.view.getFrameSize();
            let isIphoneX = (size.width == 2436 && size.height == 1125)
                || (size.width == 1125 && size.height == 2436);
            if (isIphoneX) {
                let cvs = this.getCanvasNode().getComponent(cc.Canvas);
                cvs.fitHeight = true;
                cvs.fitWidth = true;
            }
        }
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
        //清除loading框
        if (this.loadingNode) {
            this.hideLoading();
            this.loadingNode.destroy();
            this.loadingNode = null;
        }
        //清除tip框
        if (this.tipNode) {
            this.tipNode.destroy();
            this.tipNode = null;
        }
        //清除alert框
        if (this.alertNode) {
            this.hideAlert();
            this.alertNode.destroy();
            this.alertNode = null;
        }

        UIManager._instance = null;
    }
}