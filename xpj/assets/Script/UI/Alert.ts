import { mp_manager } from './../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Alert extends cc.Component {
    /**
     * 警示框文本
     * 
     * @type {cc.RichText}
     * @memberof Alert
     */
    @property(cc.RichText)
    lbl_msg: cc.RichText = null;
    /**
     * 警示框标题
     * 
     * @type {cc.Label}
     * @memberof Alert
     */
    @property(cc.Label)
    lbl_title: cc.Label = null;
    /**
     * 同意按钮
     * 
     * @type {cc.Node}
     * @memberof Alert
     */
    @property(cc.Node)
    btn_yes: cc.Node = null;
    /**
     * 拒绝按钮
     * 
     * @type {cc.Node}
     * @memberof Alert
     */
    @property(cc.Node)
    btn_no: cc.Node = null;
    /**
     * 确定需要显示的文本
     * 
     * @type {cc.Label}
     * @memberof Alert
     */
    @property(cc.Label)
    lbl_yes: cc.Label = null;
    /**
     * 否定需要显示的文本
     * 
     * @type {cc.Label}
     * @memberof Alert
     */
    @property(cc.Label)
    lbl_no: cc.Label = null;
    /**
     * 点击同意按钮事件回调
     * 
     * @type {Function}
     * @memberof Alert
     */
    cb_y: Function = null;
    /**
     * 点击拒绝按钮事件回调
     * 
     * @type {Function}
     * @memberof Alert
     */
    cb_n: Function = null;
    /**
     * 显示警示框
     * 
     * @param {string} msg 具体信息内容
     * @param {string} title 标题
     * @param {Function} [cb_yes] 点击同意按钮事件回调
     * @param {Function} [cb_no] 点击拒绝按钮事件回调
     * @param {number} [ha=0] 文字对齐方式 0=左对齐 1=居中 2=右对齐
     * @memberof Alert
     */
    showAlert(msg: string, title: string, obj_y?: btn_obj, obj_n?: btn_obj, ha: number = 0) {
        this.lbl_title.string = title;
        this.lbl_msg.string = msg;
        if (obj_y) {
            this.cb_y = obj_y.callback;
            this.lbl_yes.string = obj_y.lbl_name;
        } else {
            this.lbl_yes.string = '确定';
        }
        if (obj_n) {
            this.btn_no.active = true;
            this.cb_n = obj_n.callback;
            this.lbl_no.string = obj_n.lbl_name;
        } else {
            this.btn_no.active = false;
        }
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
     * @memberof Alert
     */
    click(event: cc.Event.EventTouch, msg: string) {
        mp_manager.playButton();
        if (msg === 'yes') {//点击了同意按钮
            if (this.cb_y) {
                this.cb_y(event);
            }
        } else {//点击了拒绝按钮
            if (this.cb_n) {
                this.cb_n(event);
            }
        }
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
