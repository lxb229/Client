
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Notice extends cc.Component {
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Notice
     */
    @property(cc.EditBox)
    edit_notice: cc.EditBox = null;

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
     * @memberof Club_Notice
     */
    initData(clubId: string, target) {
        this._clubId = clubId;
        this._parentTarget = target;
    }
    /**
     * 发送转让明星号
     * @param {string} notice 
     * @memberof Club_Notice
     */
    sendSetNotice(notice: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubId, 'notice': notice };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MODFIY_NOTICE, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    //刷新列表
                    dd.ui_manager.showTip('设置公告成功成功！');
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
     * 设置公告
     * @returns 
     * @memberof Club_Search
     */
    click_btn_notice() {
        dd.mp_manager.playButton();
        let noticeStr = this.edit_notice.string.trim();
        if (noticeStr === '' || noticeStr.length === 0) {
            dd.ui_manager.showTip('*公告不能为空,请重新输入！');
            return;
        }
        this.sendSetNotice(noticeStr);
    }
    /**
     * 退出
     * 
     * @memberof Club_Notice
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
