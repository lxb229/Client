const { ccclass, property } = cc._decorator;


@ccclass
export default class Room_Create_Rule extends cc.Component {

    /**
     * 
     * 
     * @type {cc.Label}
     * @memberof Room_Create_Rule
     */
    @property(cc.Label)
    lblRuleName: cc.Label = null;

    /**
     * 配置信息的父节点
     * 
     * @type {cc.Node}
     * @memberof Room_Create_Rule
     */
    @property(cc.Node)
    node_layout: cc.Node = null;

    _sy: number = 0;
    onLoad() {
        this._sy = this.node_layout.getComponent(cc.Layout).spacingY;
    }

    updateItem(ruleName: string) {
        this.lblRuleName.string = ruleName + ':';
    }
    lateUpdate(): void {
        if (this.node.height !== this.node_layout.height + this._sy) {
            this.node.height = this.node_layout.height + this._sy;
        }
    }
}
