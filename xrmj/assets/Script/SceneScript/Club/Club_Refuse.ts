
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Refuse extends cc.Component {
    /**
     * 加入理由输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Refuse
     */
    @property(cc.EditBox)
    edit_reason: cc.EditBox = null;

    _parentTarget = null;
    _callBack: any = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    initData(target, cb: any) {
        this._parentTarget = target;
        this._callBack = cb;
    }
    /**
     * 拒绝俱乐部
     * @returns 
     * @memberof Club_Search
     */
    click_btn_refuse() {
        dd.mp_manager.playButton();
        let resonStr = this.edit_reason.string.trim();
        if (resonStr === '' || resonStr.length === 0) {
            dd.ui_manager.showTip('*拒绝理由不能为空,请重新输入！');
            return;
        }
        if (this._callBack) this._callBack(resonStr);
        this.node.removeFromParent(true);
        this.node.destroy();
    }
    /**
     * 退出
     * 
     * @memberof Club_Refuse
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
