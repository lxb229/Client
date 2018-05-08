
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Club_Create extends cc.Component {
    /**
     * 名称输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Create
     */
    @property(cc.EditBox)
    edit_name: cc.EditBox = null;
    /**
     * 群主微信输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Create
     */
    @property(cc.EditBox)
    edit_wx: cc.EditBox = null;

    _parentTarget = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    initData(target: any) {
        this._parentTarget = target;
    }

    /**
     *  创建俱乐部
     * 
     * @param {string} corpsName 俱乐部名称
     * @param {string} wx 微信
     * @memberof Club_Create
     */
    sendCreateClub(corpsName: string, wx: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsName': corpsName, 'wxNO': wx };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_CREATE, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    dd.ui_manager.showTip('创建成功!');
                    //刷新列表
                    if (this._parentTarget) this._parentTarget.updateClubs();
                    this.node.removeFromParent(true);
                    this.node.destroy();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }
    /**
     * 创建按钮点击事件
     * 
     * @memberof Club_Create
     */
    click_btn_create() {
        dd.mp_manager.playButton();
        let nameStr = this.edit_name.string;
        let wxStr = this.edit_wx.string.trim();
        if (nameStr === '' || nameStr.length === 0) {
            dd.ui_manager.showTip('*创建名称不能为空,请重新输入！');
            return;
        }
        let index = nameStr.indexOf(' ');
        if (index !== -1) return dd.ui_manager.showTip('*创建名称不能有空格！');
        this.sendCreateClub(nameStr, wxStr);
    }

    /**
     * 退出
     * 
     * @memberof Club_Create
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
