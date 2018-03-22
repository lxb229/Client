const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class MJ_Action extends cc.Component {

    onLoad() {
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
