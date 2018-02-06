
const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Auth extends cc.Component {

    /**
     * 实名认证的提示
     * 
     * @type {cc.Label}
     * @memberof Auth
     */
    @property(cc.Label)
    lblMsg: cc.Label = null;
    /**
     * 名称输入框
     * 
     * @type {cc.EditBox}
     * @memberof Auth
     */
    @property(cc.EditBox)
    eb_name: cc.EditBox = null;

    /**
     * 身份证输入框
     * 
     * @type {cc.EditBox}
     * @memberof Auth
     */
    @property(cc.EditBox)
    eb_id: cc.EditBox = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        this.lblMsg.string = '';
        if (dd.ud_manager.mineData.authenticationFlag && dd.ud_manager.mineData.authenticationFlag === 1) {
            this.showMsg(1, '您已经实名认证过了哦！');
        } else {
            this.showMsg(0, '', 0);
        }
    }

    /**
     *  玩家实名认证
     * 
     * @param {string} name 姓名
     * @param {string} cardId 身份证
     * @memberof Auth
     */
    sendAuth(name: string, cardId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'name': name, 'cardId': cardId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_REALNAME_AUTHENTICATION, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ud_manager.mineData.authenticationFlag = 1;
                    this.showMsg(1, '实名认证成功！');
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                }
            });
            dd.ui_manager.hideLoading();
        }
    }

    /**
     * 显示提示信息
     * 
     * @param {number} type  0未实名 1已实名
     * @param {number} errType 0名字 1身份证
     * @param {string} msg 
     * @memberof Auth
     */
    showMsg(type: number, msg: string, errType: number = 0) {
        this.lblMsg.node.opacity = 255;
        this.lblMsg.node.stopAllActions();
        this.eb_name.node.parent.active = type === 0 ? true : false;
        this.eb_id.node.parent.active = type === 0 ? true : false;
        this.lblMsg.string = msg;
        this.lblMsg.node.color = type === 0 ? cc.Color.RED : cc.Color.WHITE;
        this.lblMsg.fontSize = type === 0 ? 30 : 40;
        // let msgWidget = this.lblMsg.node.getComponent(cc.Widget);
        // msgWidget.verticalCenter = type === 0 ? -Math.abs(msgWidget.verticalCenter) : Math.abs(msgWidget.verticalCenter);
        if (type === 0) {
            this.lblMsg.node.setAnchorPoint(0, 0.5);
            this.lblMsg.node.setPositionX(-120);
            let pos = errType === 0 ? this.eb_name.node.parent.getPosition() : this.eb_id.node.parent.getPosition();
            this.lblMsg.node.setPositionY(pos.y - 50);
            let action = cc.sequence(cc.delayTime(2), cc.fadeOut(1), cc.callFunc((target: cc.Node, data?: any) => {
                target.opacity = 255;
                target.getComponent(cc.Label).string = '';
            }, this));
            this.lblMsg.node.runAction(action);
        } else {
            this.lblMsg.node.setAnchorPoint(0.5, 0.5);
            this.lblMsg.node.setPosition(cc.p(0, 30));
        }
    }
    /**
     * 认证按钮点击事件
     * 
     * @memberof Auth
     */
    click_btn_auth() {
        dd.mp_manager.playButton();
        //如果实名认证了，就退出
        if (dd.ud_manager.mineData.authenticationFlag && dd.ud_manager.mineData.authenticationFlag === 1) {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
        } else {
            let nameStr = this.eb_name.string.trim();
            let idStr = this.eb_id.string.trim();
            if (nameStr === '' || nameStr.length === 0) {
                this.showMsg(0, '*姓名不能为空,请重新输入！', 0);
                return;
            }
            if (idStr === '' || idStr.length !== 18) {
                this.showMsg(0, '*请输入有效的身份证号码！', 1);
                return;
            }
            this.sendAuth(nameStr, idStr);
        }
    }

    /**
     * 退出
     * 
     * @memberof Auth
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
