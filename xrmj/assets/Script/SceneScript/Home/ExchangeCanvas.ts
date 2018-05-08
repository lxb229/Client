
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Exchange extends cc.Component {
    /**
     * 银币
     * @type {cc.Label}
     * @memberof Exchange
     */
    @property(cc.Label)
    lblCoin: cc.Label = null;
    /**
     * 金币
     * @type {cc.Label}
     * @memberof Exchange
     */
    @property(cc.Label)
    lblGold: cc.Label = null;
    /**
     * 银币抽奖界面
     * @type {cc.Prefab}
     * @memberof Exchange
     */
    @property(cc.Prefab)
    eCoin_prefab: cc.Prefab = null;
    /**
     * 金币兑奖界面
     * @type {cc.Prefab}
     * @memberof Exchange
     */
    @property(cc.Prefab)
    eGold_prefab: cc.Prefab = null;

    _eCoin: cc.Node = null;
    _eGold: cc.Node = null;
    onLoad() {

    }

    update(dt) {
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblCoin.string = '当前银币：' + dd.ud_manager.mineData.wallet.silverMoney.toString();
            this.lblGold.string = '当前金币：' + dd.ud_manager.mineData.wallet.goldMoney.toString();
        }
    }
    /**
     * 选择兑奖界面
     * @param {any} event 
     * @param {string} type 
     * @memberof Exchange
     */
    click_btn_choose(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '1':
                if (dd.ui_manager.showLoading()) {
                    dd.ws_manager.sendMsg(dd.protocol.TASK_SILVER_REWARE_INFO, '', (flag: number, content?: any) => {
                        if (flag === 0) {//成功
                            if (!this._eCoin || !this._eCoin.isValid) {
                                this._eCoin = cc.instantiate(this.eCoin_prefab);
                                let coinScript = this._eCoin.getComponent('Exchange_Coin');
                                coinScript.initData(content as CoinVo);
                                this._eCoin.parent = this.node;
                            }
                        } else if (flag === -1) {//超时
                        } else {//失败,content是一个字符串
                            dd.ui_manager.showAlert(content, '温馨提示');
                        }
                        dd.ui_manager.hideLoading();
                    });
                }
                break;
            case '2':
                if (dd.ui_manager.showLoading()) {
                    dd.ws_manager.sendMsg(dd.protocol.TASK_GOLD_REWARE_INFO, '', (flag: number, content?: any) => {
                        if (flag === 0) {//成功
                            if (!this._eGold || !this._eGold.isValid) {
                                this._eGold = cc.instantiate(this.eGold_prefab);
                                let goldScript = this._eGold.getComponent('Exchange_Gold');
                                goldScript.initData(content.itemList as RewareGoldMoneyItem[]);
                                this._eGold.parent = this.node;
                            }
                        } else if (flag === -1) {//超时
                        } else {//失败,content是一个字符串
                            dd.ui_manager.showAlert(content, '温馨提示');
                        }
                        dd.ui_manager.hideLoading();
                    });
                }
                break;
            default:
                break;
        }
    }
    /**
     * 退出按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof Exchange
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
