
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Modify extends cc.Component {
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Modify
     */
    @property(cc.EditBox)
    edit_id: cc.EditBox = null;

    @property(cc.RichText)
    lblWXNode: cc.RichText = null;

    _clubId: string = '0';
    _parentTarget = null;
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
     * @memberof Club_Modify
     */
    initData(clubId: string, target) {
        this._clubId = clubId;
        this._parentTarget = target;
        this.lblWXNode.string = '当前微信号:' + dd.ud_manager.openClubData.wxNO;
    }
    /**
     * 发送修改微信号
     * @param {string} wxno 
     * @memberof Club_Modify
     */
    sendModifyWXNo(wxno: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubId, 'wxno': wxno };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MODFIY_WXNO, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    dd.ud_manager.openClubData.wxNO = wxno;
                    //刷新列表
                    dd.ui_manager.showTip('修改成功！');
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
     * 加入俱乐部
     * @returns 
     * @memberof Club_Search
     */
    click_btn_join() {
        dd.mp_manager.playButton();
        let idStr = this.edit_id.string.trim();
        if (idStr === '' || idStr.length === 0) {
            dd.ui_manager.showTip('*微信号不能为空,请重新输入！');
            return;
        }
        this.sendModifyWXNo(idStr);
    }
    /**
     * 退出
     * 
     * @memberof Club_Modify
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
