import { ZJH_Act_State } from './ZJH_Help';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ZJH_PokerShake extends cc.Component {

    _seatId: number = 0;
    _canvansScript = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
            this._canvansScript.sendBetInfo(ZJH_Act_State.BT_VAL_COMPARAE, this._seatId);
        }, this);
    }
    initData(seatId, target) {
        this._seatId = seatId;
        this._canvansScript = target;
    }
}
