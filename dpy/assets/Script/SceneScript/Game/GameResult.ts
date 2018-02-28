
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class NewClass extends cc.Component {
    /**
     * 参加局数
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lbl_join: cc.Label = null;
    /**
     * 胜率
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lbl_winRate: cc.Label = null;
    /**
     * 最大押注
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lbl_maxBet: cc.Label = null;
    /**
     * 盈利
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lbl_profit: cc.Label = null;
    /**
     * 申请积分
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lbl_applyMoney: cc.Label = null;
    /**
     * 结算
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lbl_settlement: cc.Label = null;

    onLoad() {
        dd.ui_manager.fixiPhoneX();
        this.lbl_join.string = '';
        this.lbl_maxBet.string = '';
        this.lbl_winRate.string = '';
        this.lbl_applyMoney.string = '';
        this.lbl_profit.string = '';
        this.lbl_settlement.string = '';
    }

    updateData(data: SettlementVo) {
        this.lbl_join.string = data.gameNum;
        this.lbl_maxBet.string = data.maxBetMoney;
        this.lbl_winRate.string = Number(data.winRate) * 100 + '%';
        this.lbl_applyMoney.string = data.buyTotalMoney;
        this.lbl_profit.string = data.maxWinMoney;
        this.lbl_settlement.string = data.winMoney;
    }
    /**
     * 返回到大厅
     * @memberof NewClass
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
