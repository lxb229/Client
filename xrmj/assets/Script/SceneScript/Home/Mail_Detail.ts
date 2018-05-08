import * as dd from '../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Mail_Detail extends cc.Component {
    /**
     * 标题
     * @type {cc.RichText}
     * @memberof Mail_Detail
     */
    @property(cc.RichText)
    lbl_title: cc.RichText = null;
    /**
     * 警示框文本
     * 
     * @type {cc.RichText}
     * @memberof Mail_Detail
     */
    @property(cc.RichText)
    lbl_msg: cc.RichText = null;
    /**
     * 同意按钮
     * 
     * @type {cc.Node}
     * @memberof Mail_Detail
     */
    @property(cc.Node)
    btn_yes: cc.Node = null;
    /**
     * 拒绝按钮
     * 
     * @type {cc.Node}
     * @memberof Mail_Detail
     */
    @property(cc.Node)
    btn_no: cc.Node = null;
    /**
     * 已领取节点
     * @type {cc.Node}
     * @memberof Mail_Detail
     */
    @property(cc.Node)
    node_recive: cc.Node = null;
    /**
     * 领取按钮
     * @type {cc.Node}
     * @memberof Mail_Detail
     */
    @property(cc.Node)
    btn_get: cc.Node = null;

    _parentTarget = null;
    _mail_detail: MailView = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }
    /**
     * 显示邮件详情
     * @param {any} data 
     * @param {string} title 
     * @param {any} target 
     * @param {number} [ha=0] 
     * @memberof Mail_Detail
     */
    showMail_Detail(data: MailView, title: string, target, ha: number = 0) {
        this._mail_detail = data;
        this._parentTarget = target;
        let isHave = false;
        if (data.attachment && data.attachment.length > 0 && data.attachmentGetState === 0) isHave = true;
        this.btn_no.active = isHave;
        this.btn_get.active = isHave;
        this.btn_yes.active = !isHave;
        if (!data.attachment || data.attachmentGetState === 0) {
            this.node_recive.active = false;
        } else {
            this.node_recive.active = true;
        }

        this.lbl_title.string = title;
        this.lbl_msg.string = data.content;
        switch (ha) {
            case 0:
                this.lbl_msg.horizontalAlign = cc.TextAlignment.LEFT;
                break;
            case 1:
                this.lbl_msg.horizontalAlign = cc.TextAlignment.CENTER;
                break;
            case 2:
                this.lbl_msg.horizontalAlign = cc.TextAlignment.RIGHT;
                break;
            default:
                break;
        }
    }

    /**
     * 按钮点击事件
     * 
     * @param {cc.Event.EventTouch} event 
     * @param {string} msg 
     * @memberof Mail_Detail
     */
    click_mail_detail(event: cc.Event.EventTouch, type: string) {
        dd.mp_manager.playButton();

        switch (type) {
            case '0'://取消

                break;
            case '1'://确定

                break;
            case '2'://领取
                this._parentTarget.showMailRecive(this._mail_detail);
                break;
            default:
                break;
        }
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
