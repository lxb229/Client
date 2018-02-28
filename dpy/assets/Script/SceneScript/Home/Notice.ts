const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Notice extends cc.Component {
    /**
     * 存放公告的一级容器
     * 
     * @type {cc.ScrollView}
     * @memberof Notice
     */
    @property(cc.ScrollView)
    sv1: cc.ScrollView = null;
    /**
     * 公告展示的容器
     * 
     * @type {cc.ScrollView}
     * @memberof Notice
     */
    @property(cc.ScrollView)
    sv2: cc.ScrollView = null;
    /**
     * 公告数据集
     * 
     * @type {ActivityItemAttrib[]}
     * @memberof Notice
     */
    dataList: ActivityItemAttrib[] = null;


    init(datas: ActivityItemAttrib[]) {
        this.dataList = datas;
    }

    onLoad() {
        this.sv1.node.active = true;
        this.sv2.node.active = false;
        if (this.dataList && this.dataList.length > 0) {
            this.dataList.forEach(async (data: ActivityItemAttrib, index: number) => {
                let sf1 = await dd.img_manager.loadURLImage(data.currUrl);
                let node_out = new cc.Node();
                node_out.tag = index;
                node_out.addComponent(cc.Sprite).spriteFrame = sf1;
                node_out.on(cc.Node.EventType.TOUCH_END, async (event: cc.Event.EventTouch) => {
                    dd.mp_manager.playButton();
                    let traget = event.getCurrentTarget();
                    let item = this.dataList[traget.tag];
                    let sf2 = await dd.img_manager.loadURLImage(item.openUrl);
                    let node_in = new cc.Node();
                    node_in.addComponent(cc.Sprite).spriteFrame = sf2;
                    this.sv1.node.active = false;
                    this.sv2.node.active = true;
                    this.sv2.content.addChild(node_in);
                }, this);
                this.sv1.content.addChild(node_out);
            }, this);
            dd.ui_manager.hideLoading();
        } else {
            dd.ui_manager.showTip('当前没有公告消息');
        }
    }
    /**
     * 点击关闭按钮
     * 
     * @memberof Notice
     */
    click_out() {
        dd.mp_manager.playButton();
        if (this.sv2.node.active) {
            this.sv2.scrollToTop();
            this.sv2.content.removeAllChildren();
            this.sv2.node.active = false;
            this.sv1.node.active = true;
        } else {
            this.node.destroy();
        }
    }
}
