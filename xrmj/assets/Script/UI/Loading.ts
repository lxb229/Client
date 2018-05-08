const { ccclass, property } = cc._decorator;

@ccclass
export default class Loading extends cc.Component {
    /**
     * 动画圆圈
     * 
     * @type {cc.Node}
     * @memberof Loading
     */
    @property(cc.Node)
    sp_circle: cc.Node = null;
    /**
     * 提示信息
     * 
     * @type {cc.Label}
     * @memberof Loading
     */
    @property(cc.Label)
    lbl_msg: cc.Label = null;
    /**
     * 文本最后的小圆点
     * 
     * @type {cc.Label}
     * @memberof Loading
     */
    @property(cc.Label)
    lbl_dd: cc.Label = null;

    /**
     * 设置需要显示的提示信息
     * 
     * @param {string} msg 
     * @memberof Loading
     */
    setMsg(msg: string): void {
        this.lbl_msg.string = msg;
        this.lbl_dd.node.x = this.lbl_msg.node.width / 2 + 10;
    }
    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof Loading
     */
    update(dt: number) {
        let now = Date.now();

        this.sp_circle.rotation += 2;
        if (this.sp_circle.rotation >= 360) {
            this.sp_circle.rotation = 0;
        }
        let count = Math.floor(now / 1000) % 4;
        this.lbl_dd.string = '';
        while (count > 0) {
            count--;
            this.lbl_dd.string += '。';
        }
    }
}
