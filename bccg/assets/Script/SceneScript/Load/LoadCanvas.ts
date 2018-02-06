const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class LoadCanvas extends cc.Component {

    /**
     * 热更本地资源配置文件
     * 
     * @memberof LoadCanvas
     */
    @property({ url: cc.RawAsset })
    manifestUrl = null;
    /**
     * 显示初始化错误信息的标签
     * 
     * @type {cc.Label}
     * @memberof LoadCanvas
     */
    @property(cc.Label)
    lbl_err: cc.Label = null;
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
     * 下载最新安装包
     * 
     * @memberof LoadCanvas
     */
    download: (data: any) => void = (json: any) => {
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
    /**
     * 显示错误信息
     * 
     * @memberof LoadCanvas
     */
    error: (msg: string) => void = (msg: string) => {
        dd.ui_manager.hideLoading();
        this.lbl_err.string = msg;
    }

    async onLoad() {
        // //设置全局对象
        (<any>window).dd = dd;
        dd.config.wxState = dd.js_call_native.initWX(dd.config.app_id, dd.config.secret);
        dd.init();
        //根据数值判断是网络问题还是包体问题
        try {
            await dd.ui_manager.initUI();
            dd.ui_manager.showLoading('正在初始化游戏资源,请稍后');
            await dd.img_manager.initIMG();
            await dd.mp_manager.initMP();
        }
        catch (errMsg) {
            this.error(errMsg + ',请重启或卸载重装!');
            return;
        }
        await this.checkAppVersion();
    }

    /**
     * 原生平台检测app版本号，web跳过
     * 
     * @memberof LoadCanvas
     */
    async checkAppVersion(): Promise<void> {
        let ver = dd.js_call_native.getAppVersion();
        if (ver.length > 0) {
            let response: Response = await fetch(dd.config.checkUrl + ver);
            if (response.ok) {
                let json: checkData = await response.json();
                dd.config.cd = json;
                if (json.type < 0) {
                    dd.ui_manager.showAlert(2, '当前版本过低,请更新至最新版本再继续游戏!', () => {
                        this.download(json);
                    });
                } else if (json.type === 1) {
                    dd.ui_manager.showAlert(1, '发现新版本,是否前往下载?', () => {
                        this.download(json);
                    }, () => {
                        this.checkResVersion();
                    });
                } else {
                    this.checkResVersion();
                }
            } else {
                this.error('检测APP版本信息服务器响应失败，请确认您的网络通畅后，重启游戏!');
            }
        } else {
            this.jumpScene();
        }
    }

    /**
     * 资源版本检测，web跳过
     * 
     * @memberof LoadCanvas
     */
    checkResVersion(): void {
        let storagePath = ((jsb.fileUtils ? jsb.fileUtils.getWritablePath() : '/') + 'platform-remote-asset');
        this._am = new jsb.AssetsManager(this.manifestUrl, storagePath);
        this._am.retain();
        if (this._am.getLocalManifest().isLoaded()) {
            this._checkListener = new jsb.EventListenerAssetsManager(this._am, this.checkCb.bind(this));
            cc.eventManager.addListener(this._checkListener, 1);
            this._am.checkUpdate();
        } else {
            this.error('获取本地资源配置失败，请卸载重装!');
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
                this.error('没有发现本地的资源配置文件，热更新失败！');
                cc.eventManager.removeListener(this._checkListener);
                break;
            case jsb.EventAssetsManager.ERROR_DOWNLOAD_MANIFEST:
            case jsb.EventAssetsManager.ERROR_PARSE_MANIFEST:
                dd.ui_manager.showAlert(2, '下载服务端资源配置文件失败，请检查网络！', () => {
                    cc.game.end();
                })
                cc.eventManager.removeListener(this._checkListener);
                break;
            case jsb.EventAssetsManager.ALREADY_UP_TO_DATE:
                cc.log("当前已经是最新版本，跳过热更新！");
                cc.eventManager.removeListener(this._checkListener);
                this.jumpScene();
                break;
            case jsb.EventAssetsManager.NEW_VERSION_FOUND:
                dd.ui_manager.showLoading('发现新版本资源，开始准备更新！');
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
                dd.ui_manager.showLoading('正在更新，下载进度：(' + (event.getPercentByFile() * 100).toFixed(2) + '%)');
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
            dd.ui_manager.showAlert(2, '更新资源失败，请确认您的网络通畅后，重启游戏！', () => {
                cc.game.end();
            })
        }
        if (needRestart) {
            cc.eventManager.removeListener(this._updateListener);
            let searchPaths = jsb.fileUtils.getSearchPaths();
            let newPaths = this._am.getLocalManifest().getSearchPaths();
            Array.prototype.unshift(searchPaths, newPaths);
            cc.sys.localStorage.setItem('HotUpdateSearchPaths', JSON.stringify(searchPaths));
            jsb.fileUtils.setSearchPaths(searchPaths);
            dd.ui_manager.showLoading('资源更新完毕，正在重启游戏！');
            setTimeout(() => {
                dd.destroy();
                cc.sys.garbageCollect();
                cc.game.restart();
            }, 1000);
        }
    }

    /**
     * 跳转到登陆场景
     * 
     * @memberof LoadCanvas
     */
    jumpScene(): void {
        setTimeout(() => {
            cc.director.loadScene('LoginScene');
        }, 1000);
    }

    onDestroy(): void {
        this._am && this._am.release();
    }
}
