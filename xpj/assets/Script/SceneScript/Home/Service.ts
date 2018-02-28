
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Service extends cc.Component {

    @property(cc.Label)
    lblService: cc.Label = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
        }, this);
    }

    initData(data: string) {
        this.lblService.string = data;
    }
    /**
     * 复制
     * @memberof Service
     */
    click_btn_copy() {
        dd.mp_manager.playButton();
        dd.utils.copyToClipboard(this.lblService.string);
        dd.ui_manager.showTip('复制成功');
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
