const { ccclass, property } = cc._decorator;

@ccclass
export default class Mail_Item extends cc.Component {
    @property(cc.Node)
    node_odd: cc.Node = null;

    /**
     * 邮件标题
     * 
     * @type {cc.Label}
     * @memberof Mail_Item
     */
    @property(cc.Label)
    lblTitle: cc.Label = null;

    /**
     * 邮件内容
     * 
     * @type {cc.Label}
     * @memberof Mail_Item
     */
    @property(cc.Label)
    lblContent: cc.Label = null;

    /**
     * 邮件数据
     * 
     * @type {MailVo}
     * @memberof Mail_Item
     */
    _mailItem: MailVo = null;
    /**
     * 点击邮件回调函数
     * 
     * @memberof Mail_Item
     */
    _cb = null;

    onLoad() {
        this.node.on('touchend', (event) => {
            if (this._cb) {
                this._cb(this._mailItem);
            }
            event.stopPropagation();
        }, this);
    }

    updateItem(index: number, data, cb) {
        this._mailItem = data;
        this._cb = cb;
        this.node_odd.active = index % 2 ? false : true;
        this.lblTitle.string = this._mailItem.title;
        this.lblContent.string = this._mailItem.content;
    }
}
