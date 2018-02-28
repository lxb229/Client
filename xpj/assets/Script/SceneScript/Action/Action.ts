const { ccclass, property } = cc._decorator;

@ccclass
export default class Action extends cc.Component {

    onLoad() {
        
    }

    /**
     * 动作结束回调
     * 
     * @memberof ZJH_ActionPK
     */
    actionEnd() {
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
