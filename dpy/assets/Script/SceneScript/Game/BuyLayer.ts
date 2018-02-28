
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class BuyLayer extends cc.Component {
    /**
     * 最小带入
     * 
     * @type {cc.Label}
     * @memberof BuyLayer
     */
    @property(cc.Label)
    lab_min: cc.Label = null;
    /**
     * 最大购入
     * 
     * @type {cc.Label}
     * @memberof BuyLayer
     */
    @property(cc.Label)
    lab_max: cc.Label = null;
    /**
     * 显示当前选择的购买量
     * 
     * @type {cc.Label}
     * @memberof BuyLayer
     */
    @property(cc.Label)
    lab_buy: cc.Label = null;
    /**
     * 加减购买量用的数组下标
     * 
     * @type {number}
     * @memberof BuyLayer
     */
    maxIndex: number = 0;
    /**
     * 记录桌子上的最小带入
     * 
     * @type {number}
     * @memberof BuyLayer
     */
    min: number = 0;

    onLoad() {
        let tableData = dd.gm_manager.getTableData();
        this.min = tableData.joinChip;
        this.maxIndex = Math.floor(tableData.buyMaxChip / tableData.joinChip);
        this.lab_min.string = tableData.joinChip.toString();
        this.lab_max.string = tableData.buyMaxChip.toString();
        this.lab_buy.string = this.lab_min.string;
        this.lab_buy.node.tag = 0;
    }
    /**
     * 点击面板
     * 
     * @memberof BuyLayer
     */
    click_board() {
        dd.mp_manager.playButton();
        this.node.destroy();
    }
    /**
     * 点击加号
     * 
     * @memberof BuyLayer
     */
    click_add() {
        dd.mp_manager.playButton();
        let index = this.lab_buy.node.tag;
        if (index < this.maxIndex - 1) {
            index++;
            this.lab_buy.string = (this.min * (index + 1)).toString();
            this.lab_buy.node.tag = index;
        }
    }
    /**
     * 点击减号
     * 
     * @memberof BuyLayer
     */
    click_reduce() {
        dd.mp_manager.playButton();
        let index = this.lab_buy.node.tag;
        if (index > 0) {
            index--;
            this.lab_buy.string = (this.min * (index + 1)).toString();
            this.lab_buy.node.tag = index;
        }
    }
    /**
     * 点击发送申请
     * 
     * @memberof BuyLayer
     */
    click_send() {
        dd.ui_manager.showLoading('正在发送购买申请');
        dd.mp_manager.playButton();
        let obj = {
            tableId: dd.gm_manager.getTableData().tableId,
            chipNum: Number(this.lab_buy.string)
        }
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_BUY_CHIP, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                if (dd.gm_manager.getTableData().createPlayer === dd.ud_manager.account_mine.accountId) {
                    dd.ui_manager.hideLoading();
                } else {
                    dd.ui_manager.showTip('申购消息已经发送给房主,请等候房主处理!');
                }
            } else if (flag === -1) {
                dd.ui_manager.showTip('申购消息发送超时!');
            } else {
                dd.ui_manager.showTip(content);
            }
            this.node.destroy();
        });
    }
}
