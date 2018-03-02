const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Game_Result extends cc.Component {

    /**
     * 单个人结算的预设
     * 
     * @type {cc.Prefab}
     * @memberof Game_Result
     */
    @property(cc.Prefab)
    game_result_prefab: cc.Prefab = null;

    /**
     * 预设容器的父节点
     * 
     * @type {cc.Node}
     * @memberof Game_Result
     */
    @property(cc.Node)
    listLayout: cc.Node = null;
    /**
     * 微信分享 按钮
     * @type {cc.Node}
     * @memberof MJ_Table
     */
    @property(cc.Node)
    node_wx_share: cc.Node = null;
    /**
     * 是否点击按钮
     * 
     * @type {boolean}
     * @memberof Game_Result
     */
    _isTouch: boolean = false;

    /**
    * 微信分享回调
    * 
    * @memberof MJ_Table
    */
    wxShareCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        if (data === 0) {
            dd.ui_manager.showTip('战绩分享成功！');
        } else {
            dd.ui_manager.showTip('战绩分享失败！');
        }
    };
    onLoad() {
        dd.ui_manager.fixIPoneX(this.node);
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);

        if (dd.config.wxState === 0) {
            this.node_wx_share.active = true;
        } else {
            this.node_wx_share.active = false;
        }

        this.showGameResult();
        cc.systemEvent.on('cb_share', this.wxShareCallBack, this);
    }
    onDestroy() {
        cc.systemEvent.off('cb_share', this.wxShareCallBack, this);
    }

    /**
     * 初始化数据
     * 
     * @memberof Game_Result
     */
    showGameResult() {
        if (dd.gm_manager && dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.settlementAll) {
            let sav = dd.gm_manager.mjGameData.settlementAll;
            let maxScore = sav[0].score;
            let maxDP = sav[0].dianPao;
            for (var i = 1; i < sav.length; i++) {
                if (sav[i].score > maxScore) {
                    maxScore = sav[i].score;
                }
                if (sav[i].dianPao > maxDP) {
                    maxDP = sav[i].dianPao;
                }
            }
            this.listLayout.removeAllChildren();
            for (var i = 0; i < sav.length; i++) {
                let rsItem = cc.instantiate(this.game_result_prefab);
                let rsItemScript = rsItem.getComponent('Game_Result_Item');
                rsItemScript.updateItem(sav[i], maxScore, maxDP);
                rsItem.parent = this.listLayout;
            }
            if (sav.length < 4) {
                this.listLayout.getComponent(cc.Layout).spacingX = 100;
            }
        }
    }

    /**
     * 点击退出按钮
     * 
     * @memberof Game_Result
     */
    click_btn_out() {
        if (this._isTouch) {
            return;
        }
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            dd.ud_manager.mineData.tableId = 0;
            if (dd.gm_manager.mjGameData && dd.gm_manager.mjGameData.tableBaseVo.corpsId !== '0') {
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
     * 点击分享按钮
     * 
     * @memberof Game_Result
     */
    click_btn_share() {
        if (this._isTouch) {
            return;
        }
        this._isTouch = true;
        dd.mp_manager.playButton();
        dd.utils.captureScreen(this.node, 'jt.png', (filePath) => {
            if (filePath) {
                dd.js_call_native.wxShareRecord(filePath);
                this._isTouch = false;
            } else {
                dd.ui_manager.showTip('截图失败!');
                this._isTouch = false;
            }
            dd.ui_manager.hideLoading();
        });
    }
}
