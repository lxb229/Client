
const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_Gold extends cc.Component {

    onLoad() {

    }

    unuse() {
        this.node.stopAllActions();
        this.node.getComponent(cc.Sprite).spriteFrame = null;
        this.node.x = 0;
        this.node.y = 0;
        this.node.rotation = 0;
    }
    /**
     * 
     * @param {cc.SpriteFrame} sf 图片
     * @param {cc.Node} parent 父节点 
     * @memberof Game_Gold
     */
    initData(sf: cc.SpriteFrame, parent: cc.Node) {
        this.node.getComponent(cc.Sprite).spriteFrame = sf;
        this.node.parent = parent;
    }
}
