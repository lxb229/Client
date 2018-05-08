const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import { Protocol } from '../../Modules/Protocol';

//船X坐标的每帧移动
const shipMoveX: number = 0.3;
// 船Y坐标的每帧移动
const shipMoveY: number = 0.05;
//猫X坐标的每帧移动
const catMoveX: number = 0.4;

@ccclass
export default class LoadCanvas extends cc.Component {
    /**
    * 加载提示消息
    * 
    * @type {cc.Label}
    * @memberof Loading
    */
    @property(cc.Label)
    lbl_msg: cc.Label = null;
    /**
     * 船
     * 
     * @type {cc.Node}
     * @memberof Loading
     */
    @property(cc.Node)
    load_ship: cc.Node = null;
    /**
     * 诗
     * @type {cc.Sprite}
     * @memberof LoadCanvas
     */
    @property(cc.Sprite)
    imgPoetry: cc.Sprite = null;
    /**
     * 猫节点
     * @type {cc.Node}
     * @memberof LoadCanvas
     */
    @property(cc.Node)
    nodeCat: cc.Node = null;
    /**
     * 热更本地资源配置文件
     * 
     * @memberof LoadCanvas
     */
    @property({ url: cc.RawAsset })
    manifestUrl = null;
    /**
     * 加载空闲时间
     * @type {number}
     * @memberof LoadCanvas
     */
    @property(Number)
    loadTime: number = 3;
    /**
     * 诗动画每帧变化
     * @type {number}
     * @memberof LoadCanvas
     */
    @property(Number)
    poetryTime: number = 0.003;
    /**
     * 是否需要热更新
     * 
     * @type {boolean}
     * @memberof LoadCanvas
     */
    _needUpdate: boolean = false;
    /**
     * 热更新管理器
     * 
     * @type {any}
     * @memberof LoadCanvas
     */
    _am: any = null;//
    /**
     * 热更新，检测状态监听
     * 
     * @type {any}
     * @memberof LoadCanvas
     */
    _checkListener: any = null;
    /**
     * 热更新，下载状态监听
     * 
     * @type {any}
     * @memberof LoadCanvas
     */
    _updateListener: any = null;
    /**
     * 热更新文件下载失败次数
     * 
     * @type {number}
     * @memberof LoadCanvas
     */
    _failCount: number = 0;
    /**
     * 加载进度
     * @type {number}
     * @memberof LoadCanvas
     */
    _loadPro: number = 0;
    /**
     *船移动的目标y坐标
     * @type {number}
     * @memberof LoadCanvas
     */
    _shipY: number = 0;
    /**
     * 船Y移动的帧数
     * @type {number}
     * @memberof LoadCanvas
     */
    _shipMoveY: number = 0;
    /**
     * 猫路线
     * @type {number}
     * @memberof LoadCanvas
     */
    _catWay: number = 0;
    private exit: btn_obj = {
        lbl_name: '确定',
        callback: () => {
            cc.game.end();
        }
    }

    async onLoad() {
        //设置全局对象
        (<any>window).dd = dd;
        dd.init();
        dd.config.wxState = dd.js_call_native.initWX(dd.config.app_id, dd.config.secret);
        this.showLoadingPro(0.1, '开始初始化图像资源！');
        let index = 0;
        try {
            await dd.img_manager.initSystemHead();
            index++;
            await dd.img_manager.initChat();
            index++;
            this.showLoadingPro(0.2, '开始初始化UI资源！');
            await dd.ui_manager.initUI();
            index++;
            this.showLoadingPro(0.5, '开始初始化MP资源！');
            await dd.mp_manager.initMP();
            index++;
            this.showLoadingPro(0.8, '开始检测APP版本信息！');
            await this.checkAppVersion();
        } catch (errMsg) {
            if (index < 4) {
                this.showLoadingPro(1, '资源初始化异常，请重启或卸载重装', true);
            } else {
                this.showLoadingPro(1, 'app版本或资源版本检测异常,请确认您的网络是否通畅，重启游戏！', true);
                dd.ui_manager.showAlert('app版本或资源版本检测异常,请确认您的网络是否通畅，重启游戏！', '错误提示', this.exit);
            }
        }
    }
    /**
     * 显示加载进度
     * 
     * @param {number} pro (0-1之间的数值，表示进度
     * @param {string} msg 需要显示的提示语
     * @param {boolean} [isErr=false] 是否是错误消息
     * @memberof LoadCanvas
     */
    showLoadingPro(pro: number, msg: string, isErr = false): void {
        // this.loading_pro.progress = 1 - pro;
        this.lbl_msg.string = msg;
        if (isErr) {
            this._loadPro = 0;
            this.lbl_msg.node.color = cc.Color.RED;
        } else {
            this._loadPro = pro;
        }
    }

    async checkAppVersion(): Promise<void> {
        let ver = dd.js_call_native.getAppVersion();
        if (ver.length > 0) {//获取app版本，空字符串说明是web
            dd.ud_manager.xmlHttp(dd.config.checkUrl + ver).then((data: any) => {
                let json: checkData = JSON.parse(data);
                dd.config.cd = json;
                let yes: btn_obj = {
                    lbl_name: '确定下载',
                    callback: () => {
                        switch (cc.sys.os) {
                            case cc.sys.OS_ANDROID:
                                dd.js_call_native.openBrowser(json.apkUrl);
                                break;
                            case cc.sys.OS_IOS:
                                dd.js_call_native.openBrowser(json.ipaUrl);
                                break;
                            default:
                                dd.js_call_native.openBrowser(json.apkUrl);
                                break;
                        }
                    }
                }
                let no: btn_obj = {
                    lbl_name: '继续游戏',
                    callback: () => {
                        this.showLoadingPro(0.8, '开始检测资源版本信息！');
                        this.checkResVersion();
                    }
                }
                if (json.type < 0) {
                    this.showLoadingPro(1, json.msg, true);
                    dd.ui_manager.showAlert(json.msg, '错误提示', yes);
                } else if (json.type === 1) {
                    this.showLoadingPro(1, json.msg, true);
                    dd.ui_manager.showAlert(json.msg, '温馨提示', yes, no);
                } else {
                    this.showLoadingPro(0.8, '开始检测资源版本信息！');
                    this.checkResVersion();
                }
            }).catch((error: string) => {
                cc.log(error);
                this.showLoadingPro(1, '检测APP版本信息服务器响应失败，请确认您的网络通畅后，重启游戏！', true);
                dd.ui_manager.showAlert('检测APP版本信息服务器响应失败，请确认您的网络通畅后，重启游戏！', '错误提示', this.exit);
            });
        } else {
            this.showLoadingPro(1, '初始化完毕，准备登录！');
        }
    }

    /**
     * 资源版本检测，web跳过
     * 
     * @memberof LoadCanvas
     */
    checkResVersion(): void {
        if (cc.sys.isNative && cc.sys.isMobile) {//手机端
            let storagePath = ((jsb.fileUtils ? jsb.fileUtils.getWritablePath() : '/') + 'platform-remote-asset');
            this._am = new jsb.AssetsManager(this.manifestUrl, storagePath);
            this._am.retain();
            if (this._am.getLocalManifest().isLoaded()) {
                this._checkListener = new jsb.EventListenerAssetsManager(this._am, this.checkCb.bind(this));
                cc.eventManager.addListener(this._checkListener, 1);
                this._am.checkUpdate();
            } else {
                this.showLoadingPro(1, '获取本地资源配置失败，请卸载重装', true);
                dd.ui_manager.showAlert('获取本地资源配置失败，请卸载重装', '错误提示', this.exit);
            }
        } else {
            this.showLoadingPro(1, '初始化完毕，准备登录！');
        }
    }

    /**
     * 资源版本检测的回调方法
     * 
     * @param {any} event 
     * @memberof LoadCanvas
     */
    checkCb(event) {
        switch (event.getEventCode()) {
            case jsb.EventAssetsManager.ERROR_NO_LOCAL_MANIFEST:
                cc.log("没有发现本地的资源配置文件，热更新失败！");
                this.showLoadingPro(1, '没有发现本地的资源配置文件，请卸载重装', true);
                dd.ui_manager.showAlert('没有发现本地的资源配置文件，请卸载重装', '错误提示', this.exit);
                cc.eventManager.removeListener(this._checkListener);
                break;
            case jsb.EventAssetsManager.ERROR_DOWNLOAD_MANIFEST:
            case jsb.EventAssetsManager.ERROR_PARSE_MANIFEST:
                cc.log("下载服务端资源配置文件失败，热更新失败！");
                this.showLoadingPro(1, '下载服务端资源配置文件失败，请检查网络！', true);
                dd.ui_manager.showAlert('下载服务端资源配置文件失败，请检查网络！', '错误提示', this.exit);
                cc.eventManager.removeListener(this._checkListener);
                break;
            case jsb.EventAssetsManager.ALREADY_UP_TO_DATE:
                cc.log("当前已经是最新版本，跳过热更新！");
                cc.eventManager.removeListener(this._checkListener);
                this.showLoadingPro(1, '初始化完毕，准备登录！');
                break;
            case jsb.EventAssetsManager.NEW_VERSION_FOUND:
                cc.log("开始准备热更新！");
                this.showLoadingPro(0.8, '发现新版本资源，开始准备更新！');
                this._needUpdate = true;
                cc.eventManager.removeListener(this._checkListener);
                break;
            default:
                break;
        }
    }

    update(dt) {
        if (this._am && this._needUpdate) {
            this._needUpdate = false;
            this._updateListener = new jsb.EventListenerAssetsManager(this._am, this.updateCb.bind(this));
            cc.eventManager.addListener(this._updateListener, 1);
            this._failCount = 0;
            this._am.update();
        }

        //船的动作
        this.load_ship.x -= shipMoveX;
        if (this._shipY <= 0) {
            let targetY = (Math.random() * 10000 % 5 + 3) * (-10);
            cc.log('移动到目标点为：' + targetY);
            this._shipMoveY = targetY > this.load_ship.y ? -shipMoveY : shipMoveY;
            this._shipY = Math.abs(Math.abs(targetY) - Math.abs(this.load_ship.y));
        } else {
            this._shipY -= Math.abs(this._shipMoveY);
            this.load_ship.y -= this._shipMoveY;
        }

        //猫的动作
        if (this._catWay === 0) {
            this.nodeCat.x += catMoveX;
            if (this.nodeCat.x >= 620) {
                this._catWay = 1;
                this.nodeCat.scaleX = -1;
            }
        } else {
            this.nodeCat.x -= catMoveX;
            if (this.nodeCat.x <= 150) {
                this._catWay = 0;
                this.nodeCat.scaleX = 1;
            }
        }

        //场景的加载进度跳转场景的判断
        this.loadTime -= dt;
        //加载完成
        if (this._loadPro === 1 && this.loadTime < 0) {
            this.imgPoetry.fillStart += this.poetryTime;
            if (this.imgPoetry.fillStart === 1) {
                this._loadPro = 2;
                cc.director.loadScene('LoginScene');
            }
        }
    }

    updateCb(event) {
        let needRestart = false;
        let failed = false;
        switch (event.getEventCode()) {
            case jsb.EventAssetsManager.ERROR_NO_LOCAL_MANIFEST:
                cc.log("没有发现本地的资源配置文件，热更新失败！");
                failed = true;
                break;
            case jsb.EventAssetsManager.UPDATE_PROGRESSION:
                let percent = 0.5 + event.getPercentByFile() * 0.5;
                cc.log('正在更新，下载进度：(' + (percent * 100).toFixed(2) + '%)');
                this.showLoadingPro(percent, '正在更新，下载进度：(' + (percent * 100).toFixed(2) + '%)');
                break;
            case jsb.EventAssetsManager.ERROR_DOWNLOAD_MANIFEST:
            case jsb.EventAssetsManager.ERROR_PARSE_MANIFEST:
                cc.log("下载服务端资源配置文件失败，热更新失败！");
                failed = true;
                break;
            case jsb.EventAssetsManager.ALREADY_UP_TO_DATE:
                cc.log("当前已经是最新版本，跳过热更新！");
                failed = true;
                break;
            case jsb.EventAssetsManager.UPDATE_FINISHED:
                cc.log('热更新完毕：' + event.getMessage());
                needRestart = true;
                break;
            case jsb.EventAssetsManager.UPDATE_FAILED:
                cc.log('文件下载失败：' + event.getMessage());
                this._failCount++;
                if (this._failCount < 5) {
                    this._am.downloadFailedAssets();
                }
                else {
                    cc.log('太多文件下载失败，退出热更新！');
                    this._failCount = 0;
                    failed = true;
                }
                break;
            case jsb.EventAssetsManager.ERROR_UPDATING:
                cc.log('Asset update error: ' + event.getAssetId() + ', ' + event.getMessage());
                break;
            case jsb.EventAssetsManager.ERROR_DECOMPRESS:
                cc.log(event.getMessage());
                break;
            default:
                break;
        };
        if (failed) {
            //提示用户热更失败，让用户重新启动尝试或者跳过
            cc.eventManager.removeListener(this._updateListener);
            this.showLoadingPro(1, '更新资源失败，请确认您的网络通畅后，重启游戏！', true);
            dd.ui_manager.showAlert('更新资源失败，请确认您的网络通畅后，重启游戏！', '错误提示', this.exit);
        }
        if (needRestart) {
            cc.eventManager.removeListener(this._updateListener);
            let searchPaths = jsb.fileUtils.getSearchPaths();
            let newPaths = this._am.getLocalManifest().getSearchPaths();
            Array.prototype.unshift(searchPaths, newPaths);
            cc.sys.localStorage.setItem('HotUpdateSearchPaths', JSON.stringify(searchPaths));
            jsb.fileUtils.setSearchPaths(searchPaths);
            this.showLoadingPro(1, '资源更新完毕，正在重启游戏！', true);
            setTimeout(() => {
                dd.destroy();
                cc.sys.garbageCollect();
                cc.game.restart();
            }, 1000);
        }
    }
    onDestroy(): void {
        this._am && this._am.release();
    }
}
