
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Recharge extends cc.Component {
    /**
     * 充值金额输入框
     * @type {cc.EditBox}
     * @memberof Recharge
     */
    @property(cc.EditBox)
    edt_buy: cc.EditBox = null;
    /**
     * 充值比例
     * @type {cc.Label}
     * @memberof Recharge
     */
    @property(cc.Label)
    lblRatio: cc.Label = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
            this.outRecharge();
        }, this);
    }

    initData(data) {
        this.lblRatio.string = '1元=' + data.rmb2goldMoney + '金币';
    }
    /**
     * 获取充值金币
     * @memberof Recharge
     */
    getBuyGold(money: number, payType: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'rmb': money, 'payType': payType };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ORDER_CHARGE_RMB2GOLDMONEY, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    // dd.ui_manager.showTip('充值请求已发送!');
                    this.edt_buy.string = '';
                    dd.utils.openBrowser(content);
                    this.outRecharge();
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
                inputStr = inputStr.substring(0, inputStr.length -1);
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
     * 选择充值方式
     * @memberof Recharge
     */
    click_btn_storeBuy(event, type: string) {
        let moneyStr = this.edt_buy.string.trim();
        if (moneyStr === '' || moneyStr === null) {
            dd.ui_manager.showTip('充值金额不为空');
            return;
        }
        let money = Number(moneyStr);
        if (isNaN(money)) {
            this.edt_buy.string = '';
            dd.ui_manager.showTip('请输入有效的充值金额');
            return;
        }
        if (Math.floor(money) !== money) {
            this.edt_buy.string = '';
            dd.ui_manager.showTip('请输入整数的充值金额');
            return;
        }
        if (money > 0 && money <= 3000) {
            this.getBuyGold(Number(money), Number(type));
        } else {
            dd.ui_manager.showTip('充值金额范围为1~3000，请重新输入');
        }
    }
    /**
     * 退出充值
     * @memberof Recharge
     */
    outRecharge() {
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
