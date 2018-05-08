
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Attorn extends cc.Component {
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Attorn
     */
    @property(cc.EditBox)
    edit_id: cc.EditBox = null;

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
     * @memberof Club_Attorn
     */
    initData(clubId: string, target) {
        this._clubId = clubId;
        this._parentTarget = target;
    }
    /**
     * 发送转让明星号
     * @param {string} btStarNO 
     * @memberof Club_Attorn
     */
    sendAttorn(btStarNO: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubId, 'btStarNO': btStarNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_ZHUANRANG_PLAYER, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    let data = content as ClubPushMsg;
                    if (dd.ud_manager.openClubData.corpsId === data.corpsId) dd.ud_manager.openClubData.createPlayer = data.createPlayer;
                    //刷新列表
                    dd.ui_manager.showTip('转让成功！');
                    dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(1, this._clubId);
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
    click_btn_attorn() {
        dd.mp_manager.playButton();
        let idStr = this.edit_id.string.trim();
        if (idStr === '' || idStr.length === 0) {
            dd.ui_manager.showTip('*被转让人ID不能为空,请重新输入！');
            return;
        }
        this.sendAttorn(idStr);
    }
    /**
     * 退出
     * 
     * @memberof Club_Attorn
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
