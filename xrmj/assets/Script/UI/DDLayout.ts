const { ccclass, property } = cc._decorator;


@ccclass
export default class DDLayout extends cc.Component {

    /**
     * 配置信息的父节点
     * 
     * @type {cc.Node}
     * @memberof DDLayout
     */
    @property(cc.Node)
    node_layout: cc.Node = null;
    onLoad() {
    }

    lateUpdate(): void {
        if (this.node.height !== this.node_layout.height) {
            this.node.height = this.node_layout.height;
        }
    }
}
