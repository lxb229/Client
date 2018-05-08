import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Record_Item extends cc.Component {

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
    /**
     * 删除按钮节点
     * @type {cc.Node}
     * @memberof Club_Record_Item
     */
    @property(cc.Node)
    btn_delete: cc.Node = null;

    _itemData: RecordItemVo = null;   //数据
    _cb = null;             //item点击回调
    _target = null;

    onLoad() {
    }

    /**
     * 
     * @param {number} index 
     * @param {RecordItemVo} data 记录数据
     * @param {string} createPlayer 俱乐部创建人id
     * @param {any} cb 回调
     * @param {any} target 目标
     * @memberof Club_Record_Item
     */
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
                if (lblScore) {
                    lblScore.getComponent(cc.Label).string = data.seats[i].score.toString();
                    //如果是自己
                    if (data.seats[i].accountId === dd.ud_manager.mineData.accountId) {
                        lblScore.color = cc.Color.GREEN;
                    }
                }
                if (data.seats[i].score > maxScore) {
                    maxScore = data.seats[i].score;
                    mIndex = i;
                }

            } else {
                node_player.active = false;
            }
        }
        //如果是大赢家
        if (this.node_player_list[mIndex]) {
            let lblScore = this.node_player_list[mIndex].getChildByName('lblScore');
            let img_win = this.node_player_list[mIndex].getChildByName('img_win');
            if (lblScore) lblScore.color = cc.Color.RED;
            if (img_win) img_win.active = true;
        }
        this.btn_delete.active = createPlayer === dd.ud_manager.mineData.accountId ? true : false;
    }
    /**
     * 俱乐部群主删除记录
     * @memberof Club_Record_Item
     */
    click_btn_deleteRecord() {
        dd.mp_manager.playButton();
        dd.ui_manager.showAlert('你确定删除当前记录吗？', '温馨提示', {
            lbl_name: '确定',
            callback: () => {
                if (dd.ui_manager.showLoading()) {
                    let obj = { 'recordId': this._itemData.recordId };
                    let msg = JSON.stringify(obj);
                    dd.ws_manager.sendMsg(dd.protocol.REPLAY_DELETE_RECORD, msg, (flag: number, content?: any) => {
                        dd.ui_manager.hideLoading();
                        if (flag === 0) {//成功
                            //重新获取俱乐部列表
                            if (this._cb) this._cb();
                            dd.ui_manager.showTip('记录删除成功');
                        } else if (flag === -1) {//超时
                        } else {//失败,content是一个字符串
                            dd.ui_manager.showAlert(content, '温馨提示');
                        }
                    });
                }
            }
        }, {
                lbl_name: '取消',
                callback: () => { }
            }, 1);
    }
}
