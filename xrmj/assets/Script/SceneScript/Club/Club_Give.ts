
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Give extends cc.Component {
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Give
     */
    @property(cc.EditBox)
    edit_input: cc.EditBox = null;

    @property(cc.Label)
    lblMineCardNum: cc.Label = null;

    _parentTarget = null;
    _clubId: string = '0';
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    /**
     * 初始化数据
     * @param {string} clubId 
     * @memberof Club_Give
     */
    initData(clubId: string, target) {
        this._clubId = clubId;
        this._parentTarget = target;
        this.edit_input.string = '';
        this.lblMineCardNum.string = dd.ud_manager.mineData.wallet.roomCard.toString();
    }
    /**
     * 输入结束
     * @param {any} event 
     * @param {string} value 
     * @memberof Club_Give
     */
    edit_end(event, value: string) {
        let cardNum = this.edit_input.string.trim();
        this.edit_input.string = Math.floor(Number(cardNum)).toString();
    }
    /**
     *  捐赠房卡
     *
     * @memberof Club_Give
     */
    sendGiveGoldToClub(cardNum: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubId, 'cardNum': cardNum };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_CORPS_GIVECARD, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    //刷新列表
                    dd.ui_manager.showTip('捐赠成功！');
                    this.node.removeFromParent(true);
                    this.node.destroy();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }
    /**
     * 捐赠房卡
     * @returns 
     * @memberof Club_Search
     */
    click_btn_give() {
        dd.mp_manager.playButton();
        let cardNumStr = this.edit_input.string.trim();
        if (cardNumStr === '') {
            dd.ui_manager.showTip('*捐赠房卡不能为空,请重新输入！');
            return;
        }
        let cardNum = Math.floor(Number(cardNumStr));
        if (cardNum <= 0) {
            dd.ui_manager.showTip('*捐赠房卡必须大于0,请重新输入！');
            return;
        }
        this.sendGiveGoldToClub(cardNum);
    }
    /**
     * 退出
     * 
     * @memberof Club_Give
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
