const { ccclass, property } = cc._decorator;
import MJCanvas from './MJCanvas';
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class MJ_Action extends cc.Component {

    /**
     * canvas脚本
     * 
     * @memberof MJ_Table
     */
    _canvasTarget: MJCanvas = null;

    onLoad() {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
    }

    /**
     * 所有的特效动作结束回调
     * 
     * @memberof MJ_Action
     */
    actionEnd() {
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
