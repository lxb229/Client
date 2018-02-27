const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Record extends cc.Component {

    /**
     * 没有任何记录
     * 
     * @type {cc.Node}
     * @memberof Record
     */
    @property(cc.Node)
    lblNoRecord: cc.Node = null;

    /**
     * 
     * 战绩界面
     * @type {cc.Node}
     * @memberof Record
     */
    @property(cc.Node)
    node_record: cc.Node = null;

    /**
     * 
     * 详细战绩界面
     * @type {cc.Node}
     * @memberof Record
     */
    @property(cc.Node)
    node_record_detail: cc.Node = null;

    /**
     * 桌子id
     * 
     * @type {cc.Label}
     * @memberof Record
     */
    @property(cc.Label)
    lblRoomId: cc.Label = null;
    /**
     * 玩家列表
     * 
     * @type {cc.Label[]}
     * @memberof Record
     */
    @property([cc.Label])
    lblNameList: cc.Label[] = [];
    /**
     * 俱乐部战绩列表
     * 
     * @type {cc.ScrollView}
     * @memberof Club
     */
    @property(cc.ScrollView)
    svNode_record: cc.ScrollView = null;

    /**
     * 俱乐部战绩详细列表
     * 
     * @type {cc.ScrollView}
     * @memberof Club
     */
    @property(cc.ScrollView)
    svNode_record_detail: cc.ScrollView = null;

    /**
     * 俱乐部记录预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    club_record_item_prefab: cc.Prefab = null;

    /**
     * 俱乐部详细记录预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    record_detail_item_prefab: cc.Prefab = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        dd.ui_manager.hideLoading();
        this.lblNoRecord.active = true;
        this.sendGetRecord();
    }

    /**
     *查询战绩列表
     * 
     * @memberof Room_Join_Club
     */
    sendGetRecord() {
        this.node_record.active = true;
        this.node_record_detail.active = false;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'type': 1, 'query': dd.ud_manager.mineData.accountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_QUERY_RECORD, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showRecordInfo(content as RecordVo);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }

    /**
     *查询详细战绩数据
     * 
     * @memberof Room_Join_Club
     */
    sendGetRecordDetailed(recordId: string) {
        this.node_record.active = false;
        this.node_record_detail.active = true;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'recordId': recordId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_QUERY_DETAILED_RECORD, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showRecordDetailed(content as RecordDetailedVo, recordId);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }

    /**
    *  显示战绩记录信息
    * 
    * @memberof Club
    */
    showRecordInfo(Records: RecordVo) {
        this.svNode_record.content.removeAllChildren();
        if (!Records) return;
        if (Records.items) {
            this.lblNoRecord.active = false;
            for (var i = 0; i < Records.items.length; i++) {
                let club_record_item = cc.instantiate(this.club_record_item_prefab);
                let record_script = club_record_item.getComponent('Club_Record_Item');
                record_script.updateItem(i + 1, Records.items[i], '0', (recordData: RecordItemVo) => {
                    this.sendGetRecordDetailed(recordData.recordId);
                }, this);
                club_record_item.parent = this.svNode_record.content;
            }
        }
    }

    /**
     * 显示详细战绩数据
     * 
     * @param {RecordDetailedVo} data 
     * @memberof Record
     */
    showRecordDetailed(data: RecordDetailedVo, recordId: string) {
        this.svNode_record_detail.content.removeAllChildren();
        if (!data) return;
        this.lblRoomId.string = '房间号:' + data.tableId;
        for (var i = 0; i < data.nicks.length; i++) {
            this.lblNameList[i].string = data.nicks[i];
        }
        if (data.items) {
            for (var i = 0; i < data.items.length; i++) {
                let record_detail_item = cc.instantiate(this.record_detail_item_prefab);
                let record_detail_script = record_detail_item.getComponent('Record_Detail');
                record_detail_script.updateItem(data.items[i], (recordData: RecordDetailedItemVo) => {
                    this.sendGetVedio(recordData.recordFile, recordId);
                }, this);
                record_detail_item.parent = this.svNode_record_detail.content;
            }
        }
    }
    /**
     * 退出
     * 
     * @memberof Record
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (this.node_record.active) {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
        }
        if (this.node_record_detail.active) {
            this.sendGetRecord();
        }
    }


    /**
     * 战绩录像数据获取接口
     * 
     * @param {string} filName 
     * @memberof Record
     */
    sendGetVedio(filName: string, recordId: string) {
        if (dd.ui_manager.showLoading()) {
            let url = dd.config.replayUrl + filName;
            cc.log(url);
            fetch(url, {

            }).then((response) => {
                if (response.ok) {
                    response.json().then((json) => {
                        cc.log(JSON.stringify(json).length);
                        dd.gm_manager.replayMJ = 1;
                        dd.gm_manager.replayDataList = json.datas as ReplayData[];
                        dd.gm_manager.replayRecordId = recordId;
                        dd.gm_manager.mjGameData = dd.gm_manager.replayDataList[0].frameData;
                        dd.gm_manager.turnToGameScene();
                    });
                } else {
                    dd.ui_manager.showTip('获取录像失败');
                    dd.ui_manager.hideLoading();
                }
            }).catch((errMsg) => {
                cc.log(errMsg);
                dd.ui_manager.showAlert('服务器响应失败，请确认您的网络通畅后，重试！', '温馨提示');
                dd.ui_manager.hideLoading();
            });
        }
    }
}
