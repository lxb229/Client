import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Record_Item extends cc.Component {

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
     * 大赢家
     * 
     * @type {cc.Node[]}
     * @memberof Club_Record_Item
     */
    @property([cc.Node])
    node_player_list: cc.Node[] = [];

    _itemData: RecordItemVo = null;   //数据
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

        this.lblGameTime.string = '对战时间:  ' + dd.utils.getDateStringByTimestamp(data.recordTime, 3);
        this.lblRoomId.string = '房间号:' + data.tableId;

        let mIndex = 0;
        let maxScore = data.seats[0].score;
        for (var i = 0; i < this.node_player_list.length; i++) {
            let node_player = this.node_player_list[i];
            if (node_player && data.seats[i]) {
                node_player.active = true;
                let lblName = node_player.getChildByName('lblName');
                let lblScore = node_player.getChildByName('lblScore');
                if (lblName) lblName.getComponent(cc.Label).string = data.seats[i].nick;
                if (lblScore) lblScore.getComponent(cc.Label).string = data.seats[i].score.toString();
                //如果是自己
                if (data.seats[i].accountId === dd.ud_manager.mineData.accountId) {
                    if (lblScore) lblScore.color = cc.Color.YELLOW;
                    if (lblName) lblName.color = cc.Color.YELLOW;
                }
                if (data.seats[i].score > maxScore) {
                    maxScore = data.seats[i].score;
                    mIndex = i;
                }

            } else {
                node_player.active = false;
            }
        }
        //如果是大赢家,并且不是自己
        if (this.node_player_list[mIndex] && data.seats[mIndex].accountId !== dd.ud_manager.mineData.accountId) {
            let lblScore = this.node_player_list[mIndex].getChildByName('lblScore');
            let img_win = this.node_player_list[mIndex].getChildByName('img_win');
            if (lblScore) lblScore.color = cc.Color.GREEN;
            if (img_win) img_win.active = true;
        }
    }

}
