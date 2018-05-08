
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Join extends cc.Component {
    /**
     * 加入理由输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Join
     */
    @property(cc.EditBox)
    edit_reason: cc.EditBox = null;

    _parentTarget = null;
    _clubId: string = '0';
    _joinType: number = 0;
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
     * @param {number} type 0=在推荐中申请 1=搜索申请
     * @memberof Club_Join
     */
    initData(clubId: string, type: number, target) {
        this._clubId = clubId;
        this._joinType = type;
        this._parentTarget = target;
    }
    /**
     *  查找俱乐部
     * 
     * @param {string} corpsName 俱乐部名称
     * @memberof Club_Join
     */
    sendJoinClub(resonStr: string) {
        /**
         * 申请加入帮会
         * corpsId [String] 帮会Id
         * reson [String] 加入理由
         */
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubId, 'reson': resonStr };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_QUEST_JOIN, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    //刷新列表
                    dd.ui_manager.showTip('已发送申请，请等待！');
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
        let resonStr = this.edit_reason.string.trim();
        if (resonStr === '' || resonStr.length === 0) {
            dd.ui_manager.showTip('*加入理由不能为空,请重新输入！');
            return;
        }
        this.sendJoinClub(resonStr);
    }
    /**
     * 退出
     * 
     * @memberof Club_Join
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (this._joinType === 0 && this._parentTarget) {
            this._parentTarget.updateClubs();
        }
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
