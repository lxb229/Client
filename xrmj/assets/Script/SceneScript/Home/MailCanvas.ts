
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class MailCanvas extends cc.Component {
    /**
     * 邮件列表
     * @type {cc.ScrollView}
     * @memberof MailCanvas
     */
    @property(cc.ScrollView)
    svNode_mail: cc.ScrollView = null;

    @property(cc.Toggle)
    toggle_chooseAll: cc.Toggle = null;

    @property(cc.Prefab)
    mail_item: cc.Prefab = null;

    @property(cc.Prefab)
    mail_detail_prefab: cc.Prefab = null;

    @property(cc.Prefab)
    mail_recive_prefab: cc.Prefab = null;
    /**
     * 删除按钮
     * @type {cc.Node}
     * @memberof MailCanvas
     */
    @property(cc.Node)
    btn_delete: cc.Node = null;
    /**
     * 选择删除界面的按钮节点
     * @type {cc.Node}
     * @memberof MailCanvas
     */
    @property(cc.Node)
    choose_delete: cc.Node = null;

    @property(cc.Label)
    lblMsg: cc.Label = null;
    /**
     * 当前选中
     * @type {number}
     * @memberof MailCanvas
     */
    _curChoose: number = 0;

    _mailList: MailVoItem[]
    onLoad() {
        this.svNode_mail.content.removeAllChildren();
        this.getMailList();
    }
    /**
     * 获取邮件列表
     * @memberof MailCanvas
     */
    getMailList() {
        // if (dd.ui_manager.showLoading()) {
        dd.ui_manager.showLoading()
        //获取任务表
        dd.ws_manager.sendMsg(dd.protocol.MAIL_MAILLIST, '', (flag: number, content?: any) => {
            if (flag === 0) {//成功
                this.initData(content.mails as MailVoItem[]);
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
            }
            dd.ui_manager.hideLoading();
            cc.log(content);
        });
        // }
    }
    /**
     * 数据
     * @param {MailVoItem[]} data 
     * @memberof MailCanvas
     */
    initData(data: MailVoItem[]) {
        this._mailList = data;
        if (!data || data.length === 0) {
            this.btn_delete.active = false;
            this.choose_delete.active = false;
            this.toggle_chooseAll.node.active = false;
            this.svNode_mail.node.active = false;
            this.lblMsg.node.active = true;
            this.lblMsg.string = '暂无邮件记录';
            return;
        }
        this.lblMsg.node.active = false;
        this.svNode_mail.node.active = true;
        this.btn_delete.active = true;
        this.choose_delete.active = false;
        this.toggle_chooseAll.node.active = false;
        this.svNode_mail.content.removeAllChildren();
        if (data) {
            data.forEach((mailVo: MailVoItem, index: number) => {
                let mailNode = cc.instantiate(this.mail_item);
                let lblTitle = cc.find('layout/lblTitle', mailNode);
                if (lblTitle) lblTitle.getComponent(cc.Label).string = mailVo.title;
                let lblTime = mailNode.getChildByName('lblTime');
                let read = mailNode.getChildByName('read');
                if (read) read.active = mailVo.read === 0 ? false : true;
                if (lblTime) lblTime.getComponent(cc.Label).string = dd.utils.getDateStringByTimestamp(mailVo.recvTime, 3);
                let toggleNode = cc.find('layout/toggle_choose', mailNode);
                if (toggleNode) {
                    toggleNode.getComponent(cc.Toggle).uncheck();
                    toggleNode.active = false;
                }
                mailNode.tag = mailVo.mailId;
                mailNode.parent = this.svNode_mail.content;
                mailNode.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                    if (toggleNode && toggleNode.active) {
                        let toggle_choose = toggleNode.getComponent(cc.Toggle);
                        toggle_choose.isChecked = !toggle_choose.isChecked;
                        if (toggle_choose.isChecked) {
                            this._curChoose++;
                        } else {
                            this._curChoose--;
                        }
                        //是否是全选
                        if (this._curChoose >= data.length) {
                            this.toggle_chooseAll.isChecked = true;
                        } else {
                            this.toggle_chooseAll.isChecked = false;
                        }
                    } else {
                        this.getMailInfo(mailVo, read, index);
                    }
                });
            });
        }
    }
    /**
     * 根据邮件id查看邮件
     * @param {number} mailId 
     * @memberof MailCanvas
     */
    getMailInfo(mailVo: MailVoItem, readNode: cc.Node, index: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'mailId': mailVo.mailId };
            let msg = JSON.stringify(obj);
            //获取任务表
            dd.ws_manager.sendMsg(dd.protocol.MAIL_MAILVIEW, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    if (this._mailList[index]) this._mailList[index].read = 1;
                    if (readNode) readNode.active = true;
                    this.showMailInfo(content as MailView, mailVo.title);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }
    /**
     * 显示邮件信息
     * @memberof MailCanvas
     */
    showMailInfo(data: MailView, title: string) {
        let mail_detail = cc.instantiate(this.mail_detail_prefab);
        let ds = mail_detail.getComponent('Mail_Detail');
        ds.showMail_Detail(data, '<b>' + title + '<b/>', this, 0);
        mail_detail.parent = dd.ui_manager.getRootNode();
    }
    /**
     * 显示邮件奖品领取
     * @param {any} data 
     * @memberof MailCanvas
     */
    showMailRecive(data: MailView) {
        let mail_recive = cc.instantiate(this.mail_recive_prefab);
        let rs = mail_recive.getComponent('Mail_Recive');
        rs.initData(data, this);
        mail_recive.parent = dd.ui_manager.getRootNode();
    }
    /**
     * 是否显示选择删除
     * @param {boolean} isShow 
     * @memberof MailCanvas
     */
    showChooseMail(isShow: boolean) {
        this.btn_delete.active = !isShow;
        this.choose_delete.active = isShow;
        this.toggle_chooseAll.node.active = isShow;
        this.toggle_chooseAll.isChecked = false;
        this.svNode_mail.content.children.forEach((mailNode: cc.Node) => {
            let toggle_choose = cc.find('layout/toggle_choose', mailNode);
            if (toggle_choose) {
                toggle_choose.getComponent(cc.Toggle).uncheck()
                toggle_choose.active = isShow;
            }
        });
    }
    /**
     * 获取选择的邮件
     * @memberof MailCanvas
     */
    getChooseMail() {
        let mailIds = [];
        this.svNode_mail.content.children.forEach((mailNode: cc.Node) => {
            let toggle_choose = cc.find('layout/toggle_choose', mailNode);
            if (toggle_choose) {
                let isChoose = toggle_choose.getComponent(cc.Toggle).isChecked;
                if (isChoose) mailIds.push(mailNode.tag);
            }
        });
        return mailIds;
    }
    /**
     * 获取是否有未读邮件
     * @param {number[]} mailIds 
     * @memberof MailCanvas
     */
    getIsUnRead(mailIds: number[]): number {
        let isUnRead = 0;
        for (let i = 0; i < this._mailList.length; i++) {
            for (let j = 0; j < mailIds.length; j++) {
                if (this._mailList[i].mailId === mailIds[j]) {
                    if (this._mailList[i].read === 0) {//未读
                        isUnRead = 1;
                        break;
                    }
                    if (this._mailList[i].attachmentGetState === 0) {//未领取
                        isUnRead = 2;
                        break;
                    }
                }
            }
            if (isUnRead > 0) {
                break;
            }
        }
        return isUnRead;
    }

    /**
     * 发送删除邮件
     * @memberof MailCanvas
     */
    sendDeleteMail(mailIds: number[]) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'mailIds': mailIds };
            let msg = JSON.stringify(obj);
            //获取任务表
            dd.ws_manager.sendMsg(dd.protocol.MAIL_MAILDELETE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.initData(content.mails as MailVoItem[]);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }
    /**
     * 删除选中邮件
     * @returns 
     * @memberof MailCanvas
     */
    deleteChooseMails() {
        let mailIds = this.getChooseMail();
        if (mailIds.length === 0) return dd.ui_manager.showTip('请选择删除的邮件');

        let isUnRead = this.getIsUnRead(mailIds);
        if (isUnRead > 0) {
            let str = isUnRead === 1 ? '你有未读邮件，确认删除?' : '你有未领取奖励邮件，确认删除?';
            dd.ui_manager.showAlert(str, '温馨提示', {
                lbl_name: '确定',
                callback: () => {
                    this.sendDeleteMail(mailIds);
                }
            }, {
                    lbl_name: '取消',
                    callback: () => { }
                }, 1);
        } else {
            this.sendDeleteMail(mailIds);
        }
    }
    /**
     * 全选
     * @memberof MailCanvas
     */
    click_toggle_chooseAll() {
        dd.mp_manager.playButton();
        this._curChoose = this.toggle_chooseAll.isChecked === false ? 0 : this._mailList.length;

        this.svNode_mail.content.children.forEach((mailNode: cc.Node) => {
            let toggle_choose = cc.find('layout/toggle_choose', mailNode);
            if (toggle_choose) {
                toggle_choose.getComponent(cc.Toggle).isChecked = this.toggle_chooseAll.isChecked;
            }
        });
    }
    /**
     * 删除按钮
     * @memberof MailCanvas
     */
    click_btn_delete(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://进入选择删除
                this._curChoose = 0;
                this.showChooseMail(true);
                break;
            case '1'://确认删除
                this.deleteChooseMails();
                break;
            case '2'://退出选择删除
                this.showChooseMail(false);
                break;
            default:
                break;
        }
    }
    /**
     * 退出
     * @memberof MailCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
