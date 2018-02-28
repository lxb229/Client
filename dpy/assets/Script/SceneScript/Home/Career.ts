const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Career extends cc.Component {
    /**
     * 进度条数组
     * 
     * @type {cc.Sprite[]}
     * @memberof Career
     */
    @property([cc.Sprite])
    proList: cc.Sprite[] = [];
    /**
     * 指针数组
     * 
     * @type {cc.Node[]}
     * @memberof Career
     */
    @property([cc.Node])
    pointList: cc.Node[] = [];
    /**
     * 百分比数组
     * 
     * @type {cc.Label[]}
     * @memberof Career
     */
    @property([cc.Label])
    perList: cc.Label[] = [];
    /**
     * 战绩统计
     * 
     * @type {cc.Node}
     * @memberof Career
     */
    @property(cc.Node)
    layer1: cc.Node = null;
    /**
     * 历史记录
     * 
     * @type {cc.Node}
     * @memberof Career
     */
    @property(cc.Node)
    layer2: cc.Node = null;
    /**
     * 房间明细界面
     * @type {cc.Node}
     * @memberof Career
     */
    @property(cc.Node)
    detailLayer: cc.Node = null;
    /**
     * 房间明细的列表
     * @type {cc.Node}
     * @memberof Career
     */
    @property(cc.Node)
    svNode_detail: cc.Node = null;
    /**
     * 生涯数据
     * 
     * @type {CareerData}
     * @memberof Career
     */
    data: CareerData = null;

    init(data: CareerData) {
        this.data = data;
    }

    onLoad() {
        this.layer1.active = true;
        this.layer2.active = false;
        this.detailLayer.active = false;
        let nums = [
            Number(this.data.winRate),
            Number(this.data.seatDown),
            Number(this.data.showCard),
            Number(this.data.addChip),
            Number(this.data.dropCards),
            Number(this.data.fullBet)
        ];
        nums.forEach((num: number, index: number) => {
            this.proList[index].fillRange = -(num / 2);
            this.pointList[index].rotation = -74 + num * 148;
            this.perList[index].string = Math.round(num * 100) + '%';
        }, this);
        dd.ui_manager.hideLoading();
    }
    /**
     * 点击退出
     * 
     * @memberof Career
     */
    click_out() {
        dd.mp_manager.playButton();
        this.node.destroy();
    }
    /**
     * 点击下方按钮切换展示界面
     * 
     * @param {cc.Event.EventCustom} event 
     * @param {*} [data] 
     * @memberof Career
     */
    click_toggle(event: cc.Event.EventCustom, data?: any) {
        dd.mp_manager.playButton();
        if (Number(data) === 1) {
            let sv = this.layer2.getComponent(cc.ScrollView);
            sv.scrollToTop();
            sv.content.destroyAllChildren();
            this.layer2.active = false;
            this.layer1.active = true;
            this.detailLayer.active = false;
        } else {
            this.layer1.active = false;
            this.layer2.active = true;
            this.detailLayer.active = false;
            if (this.data.historyList && this.data.historyList.length > 0) {
                this.layer2.getComponent('SVScript').init(this.data.historyList, (data: CareerHistory) => {
                    this.getRoomDetailInfo(data.recordId);
                });
            } else {
                dd.ui_manager.showTip('您当前没有历史记录!');
            }
        }
    }
    /**
     * 获取房间明细
     * @param {string} recordId 
     * @memberof Career
     */
    getRoomDetailInfo(recordId: string) {
        let obj = {
            recordId: recordId,
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_GET_ALL_WIN_SCORE_INFO, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                let data: AllUser = content;
                if (data.items && data.items.length > 0) {
                    this.detailLayer.active = true;
                    let detailList = [];
                    data.items.forEach((item: TablePlayerListItem, index: number) => {
                        let obj = {
                            index: index,
                            item: item
                        };
                        detailList.push(obj);
                    }, this);
                    this.svNode_detail.getComponent(cc.ScrollView).content.removeAllChildren();
                    this.svNode_detail.getComponent('SVScript').init(detailList);
                }
            } else if (flag === -1) {
                dd.ui_manager.showTip('获取房间明细消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 退出房间明细
     * @memberof Career
     */
    click_btn_outDetail() {
        dd.mp_manager.playButton();
        this.detailLayer.active = false;
    }
}
