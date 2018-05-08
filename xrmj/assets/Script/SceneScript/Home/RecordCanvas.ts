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
     * @type {cc.ScrollView}
     * @memberof Record
     */
    @property(cc.ScrollView)
    svNode_record: cc.ScrollView = null;

    /**
     * 
     * 详细战绩界面
     * @type {cc.ScrollView}
     * @memberof Record
     */
    @property(cc.ScrollView)
    svNode_record_detail: cc.ScrollView = null;
    /**
     * 详细战绩界面 - 桌子id
     * 
     * @type {cc.RichText}
     * @memberof Record
     */
    @property(cc.RichText)
    lblRoomId: cc.RichText = null;
    /**
     * 详细战绩界面 - 玩家列表
     * 
     * @type {cc.Label[]}
     * @memberof Record
     */
    @property([cc.Label])
    lblNameList: cc.Label[] = [];

    /**
     * 俱乐部记录预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    record_item_prefab: cc.Prefab = null;
    /**
     * 俱乐部详细记录预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    record_detail_item_prefab: cc.Prefab = null;

    @property(cc.Sprite)
    img_title: cc.Sprite = null;

    @property([cc.SpriteFrame])
    img_title_list: cc.SpriteFrame[] = [];
    onLoad() {
        this.lblNoRecord.active = true;
        this.sendGetRecord();
    }
    /**
     *查询战绩列表
     * 
     * @memberof Room_Join_Club
     */
    sendGetRecord() {
        this.img_title.spriteFrame = this.img_title_list[0];
        this.svNode_record.node.active = true;
        this.svNode_record_detail.node.parent.active = false;
        // if (dd.ui_manager.showLoading()) {
        dd.ui_manager.showLoading();
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
        // }
    }

    /**
     *查询详细战绩数据
     * 
     * @memberof Room_Join_Club
     */
    sendGetRecordDetailed(recordId: string) {
        this.img_title.spriteFrame = this.img_title_list[1];
        this.svNode_record.node.active = false;
        this.svNode_record_detail.node.parent.active = true;
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
                let club_record_item = cc.instantiate(this.record_item_prefab);
                let record_script = club_record_item.getComponent('Record_Item');
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
        this.lblRoomId.string = '<b>房间号:' + data.tableId + '<b/>';
        this.lblNameList.forEach((lblName: cc.Label, i: number) => {
            if (data.nicks && data.nicks[i]) {
                lblName.node.active = true;
                lblName.string = data.nicks[i];
            } else {
                lblName.node.active = false;
            }
        });

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
        if (this.svNode_record.node.active || this.lblNoRecord.active) {
            dd.mp_manager.playButton();
            if (dd.ui_manager.showLoading()) {
                cc.director.loadScene('HomeScene');
            }
        }
        if (this.svNode_record_detail.node.active) {
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

            dd.ud_manager.xmlHttp(url).then((data: any) => {
                cc.log(data);
                let json = JSON.parse(data);
                dd.gm_manager.replayDataList = json.datas as ReplayData[];
                dd.gm_manager.replayRecordId = recordId;
                dd.gm_manager.turnToGameScene(dd.gm_manager.replayDataList[0].frameData, 1);
            }).catch((error: string) => {
                cc.log(error);
                dd.ui_manager.showAlert('服务器响应失败，请确认您的网络通畅后，重试！', '温馨提示');
                dd.ui_manager.hideLoading();
            });
        }
    }
}
