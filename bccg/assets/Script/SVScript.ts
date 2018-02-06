const { ccclass, property } = cc._decorator;

@ccclass
export default class SVScript extends cc.Component {
    /**
     * item的预制,需要绑定item脚本,且和prefab同名
     * 
     * @type {cc.Prefab}
     * @memberof SVScript
     */
    @property(cc.Prefab)
    itemPrefab: cc.Prefab = null;
    /**
     * 两个item之间的间隔
     * 
     * @type {number}
     * @memberof SVScript
     */
    @property(cc.Integer)
    spacing: number = 0;
    /**
     * 刷新间隔
     * 
     * @type {number}
     * @memberof SVScript
     */
    @property(cc.Integer)
    updateInterval: number = 0.2;
    /**
     * 存放item的数组
     * 
     * @type {cc.Node[]}
     * @memberof SVScript
     */
    itemNodes: cc.Node[] = [];
    /**
     * 用于创建item的数据集合
     * 
     * @type {any[]}
     * @memberof SVScript
     */
    datas: any[] = null;
    /**
     * item的尺寸
     * 
     * @type {cc.Size}
     * @memberof SVScript
     */
    itemSize: cc.Size = cc.size(0, 0);
    /**
     * item的回调函数
     * 
     * @type {Function}
     * @memberof SVScript
     */
    callback: Function = null;
    /**
     * 计时器时间
     * 
     * @type {number}
     * @memberof SVScript
     */
    updateTimer: number = 0;
    /**
     * 上一次content坐标
     * 
     * @type {number}
     * @memberof SVScript
     */
    lastContentPosY: number = 0;
    /**
     * scrollview组件
     * 
     * @type {cc.ScrollView}
     * @memberof SVScript
     */
    sv: cc.ScrollView = null;
    /**
     * 实例化滚动控件
     * 
     * @param {any[]} dataList 数据集合

     * @param {Function} cb 回调函数，可用于点击item回调，item通知sv数据变化等
     * @memberof SVScript
     */
    init(dataList: any[], cb?: Function) {
        if (!dataList || dataList.length < 1) return;
        if (!this.itemPrefab) return;
        this.sv = this.node.getComponent(cc.ScrollView);
        this.itemNodes.length = 0;
        if (this.updateInterval < 0) this.updateInterval = 0.2;
        this.datas = dataList;
        this.callback = cb;
        let tempItem = cc.instantiate(this.itemPrefab);
        this.itemSize = tempItem.getContentSize();
        tempItem.destroy();
        this.sv.content.height = this.datas.length * (this.itemSize.height + this.spacing);
        let count = Math.ceil(this.node.height / (this.itemSize.height + this.spacing)) * 2;
        for (let i = 0; i < count; i++) {
            if (i < this.datas.length) {
                let itemNode = cc.instantiate(this.itemPrefab);
                itemNode.tag = i;
                itemNode.setPosition(0, -itemNode.height * (0.5 + i) - this.spacing * (i + 1));
                itemNode.getComponent(this.itemPrefab.name).updateItem(this.datas[i], this.callback);
                this.sv.content.addChild(itemNode);
                this.itemNodes.push(itemNode);
            } else {
                break;
            }
        }
    }

    //获取item以滚动控件为坐标系的坐标
    getPositionInView(item) {
        let worldPos = item.parent.convertToWorldSpaceAR(item.position);
        let viewPos = this.sv.content.parent.convertToNodeSpaceAR(worldPos);
        return viewPos;
    }

    update(dt: number) {
        if (!this.datas || this.datas.length < 1) return;
        this.updateTimer += dt;
        if (this.updateTimer < this.updateInterval) return;
        this.updateTimer = 0;
        //判断滑动方向。
        let diff = this.sv.content.y - this.lastContentPosY;
        if (diff === 0) return;
        let offset = (this.itemSize.height + this.spacing) * this.itemNodes.length;
        this.itemNodes.forEach((item) => {
            let viewPos = this.getPositionInView(item);
            let script = item.getComponent(this.itemPrefab.name);
            if (diff < 0) {//向下滑动
                if (viewPos.y < -offset / 2 && item.y + offset < 0) {
                    item.setPositionY(item.y + offset);
                    item.tag = item.tag - this.itemNodes.length;
                    script.updateItem(this.datas[item.tag], this.callback);
                }
            } else {//向上滑动
                if (viewPos.y > offset / 2 && item.y - offset > -this.sv.content.height) {
                    item.setPositionY(item.y - offset);
                    item.tag = item.tag + this.itemNodes.length;
                    script.updateItem(this.datas[item.tag], this.callback);
                }
            }
        }, this);
        // 更新上次记录的Y坐标
        this.lastContentPosY = this.sv.content.y;
    }
}
