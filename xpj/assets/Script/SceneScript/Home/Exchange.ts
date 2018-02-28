
import * as dd from './../../Modules/ModuleManager';

const { ccclass, property } = cc._decorator;

@ccclass
export default class Exchange extends cc.Component {
    @property(cc.Node)
    node_input: cc.Node = null;

    @property(cc.Node)
    node_order: cc.Node = null;

    @property(cc.RichText)
    lblOrder: cc.RichText = null;

    @property(cc.EditBox)
    edit_dh: cc.EditBox = null;

    /**
    * 充值比例
    * @type {cc.Label}
    * @memberof Recharge
    */
    @property(cc.Label)
    lblRatio: cc.Label = null;

    _ratio: number = 0;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
        }, this);
        this.node_order.active = false;
        this.node_input.active = false;
        dd.ui_manager.hideLoading();
        this.getOrderInfo();
    }
    initData(data) {
        this._ratio = data.goldMoney2Rmb;
        this.lblRatio.string = '兑换比例' + data.goldMoney2Rmb + '金币 = 1礼券';
    }
    /**
     * 显示兑换信息
     * @memberof Exchange
     */
    showExchange(data) {
        if (data) {
            this.node_order.active = true;
            this.node_input.active = false;
            let orderStr = '';
            for (var i = 0; i < data.length; i++) {
                if (i > 1) {
                    orderStr += '<br/>订单号' + (i + 1) + ':<b>' + data[i] + '<b/>';
                } else {
                    if (data.length > 1) {
                        orderStr += '订单号' + (i + 1) + ':<b>' + data[i] + '<b/>';
                    } else {
                        orderStr += '<b>' + data[i] + '<b/>';
                    }
                }
            }
            this.lblOrder.string = orderStr;
        } else {
            this.node_order.active = false;
            this.node_input.active = true;
        }
    }
    /**
     * 获取兑换订单号信息
     * @memberof Recharge
     */
    getOrderInfo() {
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.ORDER_CHARGE_GOLDMONEY2RMB_QUERY, '', (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.showExchange(content);
                } else if (flag === -1) {//超时
                    dd.ui_manager.showTip('消息超时!');
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }
    /**
     * 兑换
     * @param {number} money 
     * @memberof Exchange
     */
    getExchange(money: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'goldMoney': money };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ORDER_CHARGE_GOLDMONEY2RMB, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.showExchange(content);
                } else if (flag === -1) {//超时
                    dd.ui_manager.showTip('消息超时!');
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }

    /**
     * 
     * @memberof Recharge
     */
    edit_change(inputStr: string, editBox: cc.EditBox) {
        //如果是特殊符号
        if (isNaN(Number(inputStr))) {
            if (inputStr.length > 1) {
                inputStr = inputStr.substring(0, inputStr.length - 1);
                editBox.string = inputStr;
            } else {
                editBox.string = '';
            }
        } else {
            let money = Number(inputStr);
            editBox.string = money + '';
        }
    }
    /**
     * 兑换按钮
     * @memberof Exchange
     */
    click_btn_exchange() {
        dd.mp_manager.playButton();
        let moneyStr = this.edit_dh.string.trim();
        if (moneyStr === '' || moneyStr === null) {
            dd.ui_manager.showTip('兑换金额不为空');
            return;
        }
        let money = Number(moneyStr);
        if (isNaN(money)) {
            this.edit_dh.string = '';
            dd.ui_manager.showTip('请输入有效的兑换金额');
            return;
        }
        if (Math.floor(money) !== money) {
            this.edit_dh.string = '';
            dd.ui_manager.showTip('请输入整数的兑换金额');
            return;
        }
        if (money % this._ratio !== 0) {
            dd.ui_manager.showTip('请输入兑换比例的整数倍金币');
            return;
        }
        this.getExchange(money);
    }
}
