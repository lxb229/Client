
const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import { Reward_Type } from '../../Modules/Protocol';
@ccclass
export default class Mail_Recive extends cc.Component {

    /**
     * 手机号输入框
     * 
     * @type {cc.EditBox}
     * @memberof Mail_Recive
     */
    @property(cc.EditBox)
    edit_phone: cc.EditBox = null;

    /**
     * 微信号输入框
     * 
     * @type {cc.EditBox}
     * @memberof Mail_Recive
     */
    @property(cc.EditBox)
    edit_wxNo: cc.EditBox = null;

    /**
     * 优酷账号输入框
     * 
     * @type {cc.EditBox}
     * @memberof Mail_Recive
     */
    @property(cc.EditBox)
    edit_ykNo: cc.EditBox = null;

    /**
     * 爱奇艺账号输入框
     * 
     * @type {cc.EditBox}
     * @memberof Mail_Recive
     */
    @property(cc.EditBox)
    edit_aqyNo: cc.EditBox = null;

    _reciveData: MailView = null;
    _parentTarget = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    initData(data: MailView, target) {
        this._reciveData = data;
        this._parentTarget = target;
        this.edit_phone.node.parent.active = false;
        this.edit_wxNo.node.parent.active = false;
        this.edit_ykNo.node.parent.active = false;
        this.edit_aqyNo.node.parent.active = false;
        if (data.attachment && data.attachment.length > 0) {
            switch (data.attachment[0].code) {
                case Reward_Type.ENTITY://实物

                    break;
                case Reward_Type.CHARGECARD://话费
                    this.edit_phone.node.parent.active = true;
                    break;
                case Reward_Type.WXREWARD://微信红包
                    this.edit_wxNo.node.parent.active = true;
                    break;
                case Reward_Type.QQREWARD://QQ红包

                    break;
                case Reward_Type.ZFBREWARD://支付宝红包

                    break;
                case Reward_Type.VIP_TENCENT://腾讯会员

                    break;
                case Reward_Type.VIP_AIQIYI://爱奇艺会员
                    this.edit_aqyNo.node.parent.active = false;
                    break;
                case Reward_Type.VIP_YOUKU://优酷会员
                    this.edit_ykNo.node.parent.active = false;
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 发送领取奖励
     * @param {any} mailId 
     * @memberof Mail_Recive
     */
    sendReciveReward(paramStr: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'mailId': this._reciveData.mailId, 'param': paramStr };
            let msg = JSON.stringify(obj);
            //获取任务表
            dd.ws_manager.sendMsg(dd.protocol.MAIL_GET_ATTACH, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this._parentTarget.getMailList();
                    this.node.removeFromParent(true);
                    this.node.destroy();
                    dd.ui_manager.showTip('领取成功');
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }
    /**
     * 确定按钮
     * @memberof Mail_Recive
     */
    click_btn_ok() {
        dd.mp_manager.playButton();
        let type = 0;
        let sendStr = '';
        switch (type) {
            case 0://手机号
                sendStr = this.edit_phone.string.trim();
                if (!dd.utils.checkMobile(sendStr))
                    return dd.ui_manager.showTip('*请输入有效手机号');
                break;
            case 1://微信号
                sendStr = this.edit_wxNo.string.trim();
                if (sendStr === '' || sendStr.length === 0)
                    return dd.ui_manager.showTip('*微信号不能为空,请输入微信号');
                break;
            case 0://手机号
                sendStr = this.edit_ykNo.string.trim();
                if (sendStr === '' || sendStr.length === 0)
                    return dd.ui_manager.showTip('*优酷账号不能为空,请输入优酷账号');
                break;
            case 0://手机号
                sendStr = this.edit_aqyNo.string.trim();
                if (!dd.utils.checkMobile(sendStr))
                    return dd.ui_manager.showTip('*爱奇艺账号不能为空,请输入爱奇艺账号');
                break;
            default:
                break;
        }
        this.sendReciveReward(sendStr);
    }

    /**
     * 退出
     * 
     * @memberof Mail_Recive
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
