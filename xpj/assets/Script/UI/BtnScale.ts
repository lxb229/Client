const { ccclass, property } = cc._decorator;

@ccclass
export default class BtnScale extends cc.Component {

    onTouchStart = (event: cc.Event.EventTouch) => {
        this.playAction(1.1);
        event.stopPropagation();
    };
    onTouchMove = (event: cc.Event.EventTouch) => {
        event.stopPropagation();
    };
    onTouchEnd = (event: cc.Event.EventTouch) => {
        this.playAction(1);
        event.stopPropagation();
    };
    onTouchCancel = (event: cc.Event.EventTouch) => {
        this.playAction(1);
        event.stopPropagation();
    };
    onLoad() {
        this.node.on("touchstart", this.onTouchStart, this);

        this.node.on("touchmove", this.onTouchMove, this);

        this.node.on("touchend", this.onTouchEnd, this);

        this.node.on("touchcancel", this.onTouchCancel, this);
    }
    /**
     * 播放动画
     * 
     * @memberof BtnScale
     */
    playAction(scale: number) {
        let btn = this.node.getComponent(cc.Button);
        if (btn) {
            if (btn.interactable) {
                this.node.stopAllActions();
                let action = cc.scaleTo(0.08, scale);
                this.node.runAction(action);
            }
        }
    }

    onDestroy() {
        this.node.off("touchstart", this.onTouchStart, this);
        this.node.off("touchmove", this.onTouchMove, this);
        this.node.off("touchend", this.onTouchEnd, this);
        this.node.off("touchcancel", this.onTouchCancel, this);
    }
}
