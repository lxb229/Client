
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Room_Join_Pwd extends cc.Component {
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Room_Join_Pwd
     */
    @property(cc.EditBox)
    edit_input: cc.EditBox = null;

    _parentTarget = null;
    _tableId: number = 0;
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
     * @memberof Room_Join_Pwd
     */
    initData(tableId: number, target) {
        this._tableId = tableId;
        this._parentTarget = target;
        this.edit_input.string = '';
    }
    /**
     * 发送加入房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendJoinRoom(password: string) {
        let obj = { 'tableId': this._tableId, 'password': password };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
                cc.log('加入成功');
                dd.gm_manager.turnToGameScene(content as MJGameData, 0);
            } else if (flag === -1) {//超时
                dd.ui_manager.hideLoading();
            } else {//失败,content是一个字符串
                dd.ui_manager.showAlert(content, '温馨提示');
                dd.ui_manager.hideLoading();
            }
            cc.log(content);
        });
    }
    /**
     * 加入
     * @returns 
     * @memberof Club_Search
     */
    click_btn_join() {
        dd.mp_manager.playButton();
        let pwdStr = this.edit_input.string.trim();
        if (pwdStr === '' || pwdStr.length !== 6) {
            dd.ui_manager.showTip('*请输入6位数密码！');
            return;
        }
        this.sendJoinRoom(pwdStr);
    }
    /**
     * 退出
     * 
     * @memberof Room_Join_Pwd
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
