const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import mail_item from './Mail_Item';
@ccclass
export default class Email extends cc.Component {

    /**
     * 列表容器
     * 
     * @type {cc.ScrollView}
     * @memberof Email
     */
    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;

    /**
     * 没有邮件时显示的节点
     * 
     * @type {cc.Node}
     * @memberof Email
     */
    @property(cc.Node)
    lblNoMail: cc.Node = null;

    /**
     * 邮件的item预设
     * 
     * @type {cc.Prefab}
     * @memberof Email
     */
    @property(cc.Prefab)
    mail_item_prefab: cc.Prefab = null;
    /**
     * 邮件列表数据
     * 
     * @type {MailVo[]}
     * @memberof Email
     */
    _mailList: MailVo[] = [];
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);

        // let obj = {};
        // let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAIL_MAILLIST, '', (flag: number, content?: any) => {
            if (flag === 0) {//成功
                this._mailList = content as MailVo[];
                this.showMailInfo();
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                cc.log(content);
            }
        });
    }

    /**
     * 读取邮件
     * 
     * @param {number} mailId 
     * @memberof Email
     */
    readMail(mailId: number) {
        let obj = { mailId: mailId };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAIL_MAILVIEW, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
                this._mailList = content as MailVo[];
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                cc.log(content);
            }
        });
    }
    /**
     * 显示邮件列表信息
     * 
     * @memberof Email
     */
    showMailInfo(): void {
        if (this._mailList) {
            this.svNode.node.active = true;
            this.lblNoMail.active = false;

            this.svNode.content.removeAllChildren();
            for (var i = 0; i < this._mailList.length; i++) {
                this.createMailItem(i, this._mailList[i]);
            }
        } else {
            this.svNode.node.active = false;
            this.lblNoMail.active = true;
        }
    }

    /**
     * 创建邮件item
     * 
     * @memberof Email
     */
    createMailItem(index: number, data: MailVo) {
        let mail_item = cc.instantiate(this.mail_item_prefab);
        let mail_item_script: mail_item = mail_item.getComponent('Mail_Item');
        mail_item_script.updateItem(index, data, (itemData: MailVo) => {
            this.readMail(itemData.mailId);
            let str = '   ' + itemData.content + '<br/>    ' + dd.utils.getDateStringByTimestamp(itemData.recvTime, 3);
            dd.ui_manager.showAlert(str, itemData.title, {
                lbl_name: '确定',
                callback: () => {
                    this.showMailInfo();
                }
            }, null, 0);
        });
        mail_item.parent = this.svNode.content;
    }
    /**
     * 退出
     * 
     * @memberof Email
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
