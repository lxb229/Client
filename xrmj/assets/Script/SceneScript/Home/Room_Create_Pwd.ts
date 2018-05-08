
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Room_Create_Pwd extends cc.Component {
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Room_Create_Pwd
     */
    @property(cc.EditBox)
    edit_input: cc.EditBox = null;

    _parentTarget = null;
    _ruleCfg: SendRuleCfg = null;
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
     * @memberof Room_Create_Pwd
     */
    initData(data: SendRuleCfg, target) {
        this._ruleCfg = data;
        this._parentTarget = target;
        this.edit_input.string = '';
    }
    /**
     * 发送消息创建房间
     * 
     * @param {SendRuleCfg} obj 创建房间对象
     * @memberof Room_Create
     */
    sendCreatRoom(obj: SendRuleCfg) {
        if (dd.ui_manager.showLoading()) {
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_CREATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    //存储本地
                    this._parentTarget.saveLoacalRuleCfg(obj.roomItemId);
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
    }

    /**
     * 捐赠房卡
     * @returns 
     * @memberof Club_Search
     */
    click_btn_create() {
        dd.mp_manager.playButton();
        let pwdStr = this.edit_input.string.trim();
        if (pwdStr === '' || pwdStr.length !== 6) {
            dd.ui_manager.showTip('*请输入6位数密码！');
            return;
        }
        this._ruleCfg.password = pwdStr;
        this.sendCreatRoom(this._ruleCfg);
    }
    /**
     * 退出
     * 
     * @memberof Room_Create_Pwd
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
