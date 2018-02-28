const { ccclass, property } = cc._decorator;

@ccclass
export default class Toggle_AddLabel extends cc.Component {

    /**
     * 名称
     * 
     * @type {cc.Label}
     * @memberof Toggle_AddLabel
     */
    @property(cc.Label)
    lblName: cc.Label = null;
    /**
     * 文字label描述的节点，用作选中或未选中当前选项的时候，颜色的变化
     * 
     * @type {cc.Label}
     * @memberof Toggle_custom
     */
    @property(cc.Node)
    lblDes: cc.Node = null;

    /**
     * 文字label的描边
     * 
     * @type {cc.LabelOutline}
     * @memberof Toggle_AddLabel
     */
    @property({
        type: cc.LabelOutline,
        tooltip: '文字label的描边,可以不存在'
    })
    lblOL: cc.LabelOutline = null;

    /**
     * 选项节点
     * 
     * @type {cc.Toggle}
     * @memberof Toggle_AddLabel
     */
    @property(cc.Toggle)
    toggle: cc.Toggle = null;

    /**
     * 选中和未选中这个选项的时候，lable的颜色 0选中 1未选中
     * 
     * @type {[cc.Color]}
     * @memberof Toggle_custom
     */
    @property({
        // default: [],
        type: [cc.Color],
        tooltip: '选中和未选中这个选项的时候，lable的颜色\n 0:选中\n 1:未选中'
    })
    check_color: cc.Color[] = [];

    /**
     * label节点颜色改变的类型 0只改变node颜色 1只改变node的labelOutline组件(如果存在的话)颜色 2前两个都改变
     * 
     * @memberof Toggle_AddLabel
     */
    @property({
        // default: 0,
        type: cc.Integer,
        tooltip: 'label节点颜色改变的类型\n 0:只改变node颜色\n 1:只改变node的labelOutline组件(如果存在的话)颜色\n 2:前两个都改变'
    })
    color_type = 0;

    onLoad() {
        // this.toggle.node.on('click', (event: cc.Event.EventCustom) => {
        //     let isCheck = this.toggle.isChecked;
        //     this.checkChangeState(!isCheck);
        // }, this);
    }

    updateItem(name: string) {
        this.lblName.string = name;
    }
    update(dt) {
        let isCheck = this.toggle.isChecked;
        this.checkChangeState(isCheck);
    }

    /**
     * 选项状态改变的时候
     * 
     * @memberof Toggle_AddLabel
     */
    checkChangeState(isCheck: boolean) {

        switch (this.color_type) {
            case 0:
                this.lblDes.color = isCheck === true ? this.check_color[0] : this.check_color[1];
                break;
            case 1:
                if (this.lblOL) {
                    this.lblOL.color = isCheck === true ? this.check_color[0] : this.check_color[1];
                }
                break;
            case 2:
                this.lblDes.color = isCheck === true ? this.check_color[0] : this.check_color[1];
                if (this.lblOL) {
                    this.lblOL.color = isCheck === true ? this.check_color[0] : this.check_color[1];
                }
                break
            default:
                break;
        }
    }
}