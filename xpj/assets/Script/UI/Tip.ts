const { ccclass, property } = cc._decorator;

@ccclass
export default class Tip extends cc.Component {

    @property(cc.Label)
    lbl_msg: cc.Label = null;

    /**
     * 显示漂浮框
     * 
     * @param {string} msg 提示信息内容
     * @param {number} sTime 前段动画时间 
     * @param {number} mTime 悬浮时间
     * @param {number} eTime 后端动画时间
     * @memberof Tip
     */
    showTip(msg: string, sTime: number, mTime: number, eTime: number): void {
        this.lbl_msg.string = msg;
        let topY = Math.floor(cc.director.getVisibleSize().height / 5);
        let bottomY = -topY;
        this.node.y = bottomY;
        this.node.scale = 1.2;
        let action1 = cc.spawn(cc.moveTo(sTime, this.node.x, 0), cc.scaleTo(sTime, 1), cc.fadeIn(sTime));
        let action2 = cc.delayTime(mTime);
        let action3 = cc.spawn(cc.moveTo(eTime, this.node.x, topY), cc.scaleTo(eTime, 0.1), cc.fadeOut(eTime));
        let action4 = cc.callFunc(() => {
            this.node.removeFromParent(true);
            this.node.destroy();
        }, this);
        let seq = cc.sequence(action1, action2, action3, action4);
        this.node.runAction(seq);
    }
}
