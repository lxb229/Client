
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Club_Apply_Join extends cc.Component {

    @property(cc.Label)
    lblTip: cc.Label = null;

    @property(cc.ScrollView)
    svNode_apply: cc.ScrollView = null;

    @property(cc.Prefab)
    apply_prefab: cc.Prefab = null;
    /**
     * 俱乐部信息
     * 
     * @type {CorpsVoInner}
     * @memberof Club
     */
    _clubInfo: CorpsVoInner = null;

    _applyList = [];
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this.node.active = false;
            event.stopPropagation();
        }, this);

    }
    /**
     * 初始化数据
     * @param {CorpsVoInner} clubInfo 
     * @memberof Club_Apply_Join
     */
    initData(clubInfo: CorpsVoInner) {
        this._clubInfo = clubInfo;
        this.sendGetApplyInfo();
    }
    /**
     * 获取申请列表
     * @memberof Club_Apply_Join
     */
    sendGetApplyInfo() {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubInfo.corpsId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_GET_QUESTJOIN_LIST, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.showApplyList(content);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
            });
        }
    }

    /**
     * 发送申请回复
     * @param {string} starNO 玩家Id,'0'就表示全部
     * @param {number} bt  (0=拒绝,1=同意)
     * @memberof Club_Apply_Join
     */
    sendApplyAnwser(starNO: string, bt: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'corpsId': this._clubInfo.corpsId,
                'starNO': starNO,
                'bt': bt
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_QUEST_JOIN_BT, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.sendGetApplyInfo();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
            });
        }
    }
    /**
     * 显示申请列表
     * @param {any} data 
     * @memberof Club_Apply_Join
     */
    showApplyList(data) {
        this.svNode_apply.content.removeAllChildren();
        if (data && data.items) {
            this._applyList = data.items;
            this.lblTip.node.active = false;
            for (var i = 0; i < data.items.length; i++) {
                let apply = cc.instantiate(this.apply_prefab);
                apply.parent = this.svNode_apply.content;
                let apply_script = apply.getComponent('Club_Apply_Join_Item');
                apply_script.updateItem(data.items[i], this);
            }
        } else {
            this.lblTip.node.active = true;
        }
    }
    /**
     * 全部拒绝
     * @memberof Club_Apply_Join
     */
    click_btn_refuseAll() {
        dd.mp_manager.playButton();
        if (this._applyList.length === 0) return;
        this.sendApplyAnwser('0', 0);
    }
    /**
     * 全部通过
     * @memberof Club_Apply_Join
     */
    click_btn_agreeAll() {
        dd.mp_manager.playButton();
        if (this._applyList.length === 0) return;
        this.sendApplyAnwser('0', 1);
    }
}
