import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Record_Item extends cc.Component {

    /**
     *索引
     * 
     * @type {cc.Label}
     * @memberof Club_Record_Item
     */
    @property(cc.Label)
    lblIndex: cc.Label = null;

    /**
     * 房间id
     * 
     * @type {cc.Label}
     * @memberof Club_Record_Item
     */
    @property(cc.Label)
    lblRoomId: cc.Label = null;

    /**
     * 对战时间
     * 
     * @type {cc.Label}
     * @memberof Club_Record_Item
     */
    @property(cc.Label)
    lblGameTime: cc.Label = null;

    /**
     * 删除按钮
     * 
     * @type {cc.Node}
     * @memberof Club_Record_Item
     */
    @property(cc.Node)
    node_delete: cc.Node = null;
    /**
     * 大赢家
     * 
     * @type {cc.Node[]}
     * @memberof Club_Record_Item
     */
    @property([cc.Node])
    node_win_list: cc.Node[] = [];

    /**
     * 名字
     * 
     * @type {cc.Label[]}
     * @memberof Club_Record_Item
     */
    @property([cc.Label])
    lblNameList: cc.Label[] = [];

    /**
     * 分数
     * 
     * @type {cc.Label[]}
     * @memberof Club_Record_Item
     */
    @property([cc.Label])
    lblScoreList: cc.Label[] = [];

    _itemData: RecordItemVo = null;                            //俱乐部信息数据
    _cb = null;             //item点击回调
    _target = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            if (this._cb) {
                this._cb(this._itemData);
            }
            event.stopPropagation();
        }, this);
    }

    updateItem(index: number, data: RecordItemVo, createPlayer: string, cb, target) {
        this._itemData = data;
        this._cb = cb;
        this._target = target;

        this.lblIndex.string = index + '';
        this.lblGameTime.string = '对战时间:  ' + dd.utils.getDateStringByTimestamp(data.recordTime, 3);
        this.lblRoomId.string = '房间号:' + data.tableId;

        let mIndex = 0;
        let maxScore = data.seats[0].score;
        for (var i = 0; i < this.lblNameList.length; i++) {
            this.node_win_list[i].active = false;
            this.lblScoreList[i].node.color = cc.Color.WHITE;
            if (data.seats[i]) {
                this.lblNameList[i].node.active = true;
                this.lblNameList[i].string = data.seats[i].nick;
                this.lblScoreList[i].string = data.seats[i].score + '';
                if (data.seats[i].score > maxScore) {
                    maxScore = data.seats[i].score;
                    mIndex = i;
                }
                //如果是自己
                if (data.seats[i].accountId === dd.ud_manager.mineData.accountId) {
                    this.lblScoreList[i].node.color = cc.color(77, 203, 235);
                }
            } else {
                this.lblNameList[i].node.active = false;
            }
        }
        if (this.node_win_list[mIndex]) {
            this.node_win_list[mIndex].active = true;
            this.lblScoreList[mIndex].node.color = cc.color(245, 152, 92);
        }
        if (this.node_delete) {
            //如果创建人是自己
            if (createPlayer === dd.ud_manager.mineData.accountId) {
                this.node_delete.active = true;
            } else {
                this.node_delete.active = false;
            }
        }
    }

    /**
     * 删除按钮
     * 
     * @memberof Club_Member_Item
     */
    click_btn_delete() {
        dd.mp_manager.playButton();
        dd.ui_manager.showAlert('确定删除当前战绩？'
            , '战绩删除',
            {
                lbl_name: '确定',
                callback: () => {
                    this._target.sendDeleteRecord(this._itemData.recordId);
                }
            },
            {
                lbl_name: '再想想',
                callback: () => {
                }
            }
            , 1);
    }
}
