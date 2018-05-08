import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ClubAminLayer extends cc.Component {
    /**
     * 无记录消息提示
     * @type {cc.Label}
     * @memberof ClubAminLayer
     */
    @property(cc.Label)
    lblMsg: cc.Label = null;
    /**
     * 开张 和 关门
     * @type {cc.RichText}
     * @memberof ClubAminLayer
     */
    @property(cc.RichText)
    lblOpen: cc.RichText = null;
    /**
     * 公开和隐藏
     * @type {cc.RichText}
     * @memberof ClubAminLayer
     */
    @property(cc.RichText)
    lblPublic: cc.RichText = null;
    /**
    * 俱乐部记录列表
    * @type {cc.ScrollView}
    * @memberof ClubCanvas
    */
    @property(cc.ScrollView)
    svNode_record: cc.ScrollView = null;
    /**
     * 俱乐部记录列表的预设
     * @type {cc.Prefab}
     * @memberof ClubCanvas
     */
    @property(cc.Prefab)
    club_record_prefab: cc.Prefab = null;

    /**
     * 修改俱乐部微信号
     * @type {cc.Prefab}
     * @memberof ClubCanvasf
     */
    @property(cc.Prefab)
    club_modify_prefab: cc.Prefab = null;

    /**
     * 转让俱乐部微信号
     * @type {cc.Prefab}
     * @memberof ClubCanvasf
     */
    @property(cc.Prefab)
    club_attorn_prefab: cc.Prefab = null;
    /**
     * 邀请
     * @type {cc.Prefab}
     * @memberof ClubAminLayer
     */
    @property(cc.Prefab)
    club_invite_prefab: cc.Prefab = null;
    /**
     * 公告
     * @type {cc.Prefab}
     * @memberof ClubAminLayer
     */
    @property(cc.Prefab)
    club_notice_prefab: cc.Prefab = null;

    _corpsId: string = '0';

    _modify: cc.Node = null;
    _attorn: cc.Node = null;
    _invite: cc.Node = null;
    _notice: cc.Node = null;

    onLoad() {

    }
    /**
     * 刷新数据
     * @param {string} corpsId 
     * @memberof ClubAminLayer
     */
    initData(corpsId: string) {
        this._corpsId = corpsId;
        this.lblOpen.string = dd.ud_manager.openClubData.roomCardState === 0 ? '<b>开馆<b/>' : '<b>关馆<b/>';
        this.lblPublic.string = dd.ud_manager.openClubData.hidde === 0 ? '<b>隐藏<b/>' : '<b>公开<b/>';
        this.sendGetClubRecord(corpsId);
    }
    /**
     * 获取俱乐部游戏记录
     * @memberof ClubTableLayer
     */
    sendGetClubRecord(corpsId: string, type: number = 1) {
        if (corpsId === '0') return;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'type': 2, 'query': corpsId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_QUERY_RECORD, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showRecordList(content as RecordVo);
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
     * 获取俱乐部状态
     * @param {string} corpsId 
     * @param {number} state 0=关闭,1=打开
     * @returns 
     * @memberof ClubAminLayer
     */
    sendsetClubState(corpsId: string, state: number) {
        this._corpsId = corpsId;
        if (corpsId === '0') return;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': corpsId, 'state': state };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_SET_ROOMCARD_STATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let data = content as ClubState;
                    dd.ud_manager.openClubData.roomCardState = data.roomCardState;
                    dd.ud_manager.openClubData.hidde = data.hidde;
                    this.lblOpen.string = data.roomCardState === 0 ? '<b>开馆<b/>' : '<b>关馆<b/>';
                    this.lblPublic.string = data.hidde === 0 ? '<b>隐藏<b/>' : '<b>公开<b/>';

                    let str = data.roomCardState === 0 ? '关馆成功' : '开馆成功';
                    dd.ui_manager.showTip(str);
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
     * 设置俱乐部可见状态
     * @param {string} corpsId 
     * @param {number} hidde 
     * @returns 
     * @memberof ClubAminLayer
     */
    sendsetClubHidde(corpsId: string, hidde: number) {
        this._corpsId = corpsId;
        if (corpsId === '0') return;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': corpsId, 'hidde': hidde };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MODFIY_HIDDE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let data = content as ClubState;
                    dd.ud_manager.openClubData.roomCardState = data.roomCardState;
                    dd.ud_manager.openClubData.hidde = data.hidde;
                    this.lblOpen.string = data.roomCardState === 0 ? '<b>开馆<b/>' : '<b>关馆<b/>';
                    this.lblPublic.string = data.hidde === 0 ? '<b>隐藏<b/>' : '<b>公开<b/>';
                    let str = data.hidde === 1 ? '隐藏成功' : '公开成功';
                    dd.ui_manager.showTip(str);
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
     * 显示排行榜列表信息
     * @param {number} type 
     * @param {CorpsRankVoItem[]} content 
     * @memberof ClubRankLayer
     */
    showRecordList(recordData: RecordVo) {
        this.svNode_record.content.removeAllChildren(true);
        if (!recordData.items || recordData.items.length === 0) {
            this.svNode_record.node.active = false;
            this.lblMsg.node.active = true;
            return;
        }
        this.svNode_record.node.active = true;
        this.lblMsg.node.active = false;
        recordData.items.forEach((record: RecordItemVo, i: number) => {
            let recordItem = cc.instantiate(this.club_record_prefab);
            let recordScript = recordItem.getComponent('Club_Record_Item');
            recordScript.updateItem(i + 1, record, dd.ud_manager.openClubData.createPlayer, () => {
                this.sendGetClubRecord(this._corpsId);
            }, this);
            recordItem.parent = this.svNode_record.content;
        }, this);
    }
    /**
     * 管理俱乐部的按钮点击事件
     * @memberof ClubAminLayer
     */
    click_btn_admin(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://关馆
                let state = dd.ud_manager.openClubData.roomCardState === 0 ? 1 : 0;
                let stateStr = '';
                if (dd.ud_manager.openClubData.roomCardState === 0) {//如果当前是闭馆状态
                    stateStr = '开启麻将馆，您的麻将馆中的成员将可以消耗共享房卡创建游戏。<br/>您确定开馆吗？';
                } else {
                    stateStr = '关闭麻将馆，您的麻将馆中的成员将不可以消耗共享房卡创建游戏。<br/>您确定关馆吗？';
                }
                dd.ui_manager.showAlert(stateStr, '温馨提示', {
                    lbl_name: '确定',
                    callback: () => {
                        this.sendsetClubState(this._corpsId, state);
                    }
                }, {
                        lbl_name: '取消',
                        callback: () => { }
                    }, 1);
                break;
            case '1'://转让
                if (!this._attorn || !this._attorn.isValid) {
                    this._attorn = cc.instantiate(this.club_attorn_prefab);
                    let attornScript = this._attorn.getComponent('Club_Attorn');
                    attornScript.initData(this._corpsId, this);
                    this._attorn.parent = dd.ui_manager.getRootNode();
                }
                break;
            case '2'://解散
                dd.ui_manager.showAlert('麻将馆解散后，房卡不退回，且无法恢复，是否确认要解散？', '解散麻将馆', {
                    lbl_name: '确定',
                    callback: () => {
                        if (dd.ui_manager.showLoading()) {
                            let obj = { 'corpsId': this._corpsId };
                            let msg = JSON.stringify(obj);
                            dd.ws_manager.sendMsg(dd.protocol.CORPS_DESTORY, msg, (flag: number, content?: any) => {
                                dd.ui_manager.hideLoading();
                                if (flag === 0) {//成功
                                    //重新获取俱乐部列表
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
                break;
            case '3'://公告
                if (!this._notice || !this._notice.isValid) {
                    this._notice = cc.instantiate(this.club_notice_prefab);
                    let noticeScript = this._notice.getComponent('Club_Notice');
                    noticeScript.initData(this._corpsId, this);
                    this._notice.parent = dd.ui_manager.getRootNode();
                }
                break;
            case '4'://修改群号
                if (!this._modify || !this._modify.isValid) {
                    this._modify = cc.instantiate(this.club_modify_prefab);
                    let modifyScript = this._modify.getComponent('Club_Modify');
                    modifyScript.initData(this._corpsId, this);
                    this._modify.parent = dd.ui_manager.getRootNode();
                }
                break;
            case '5'://公开麻将馆
                let hidde = dd.ud_manager.openClubData.hidde === 0 ? 1 : 0;
                let hiddeStr = '';
                if (dd.ud_manager.openClubData.hidde === 1) {//如果当前是隐藏状态
                    hiddeStr = '公开麻将馆，您的麻将馆将将出现在列表中，其他玩家可通过列表申请加入。您确定公开吗？';
                } else {
                    hiddeStr = '隐藏麻将馆，您的麻将馆将不会出现在列表中，其他玩家只可通过搜索加入。您确定隐藏吗？';
                }
                dd.ui_manager.showAlert(hiddeStr, '温馨提示', {
                    lbl_name: '确定',
                    callback: () => {
                        this.sendsetClubHidde(this._corpsId, hidde);
                    }
                }, {
                        lbl_name: '取消',
                        callback: () => { }
                    }, 1);
                break;
            case '6'://邀请
                if (!this._invite || !this._invite.isValid) {
                    this._invite = cc.instantiate(this.club_invite_prefab);
                    let inviteScript = this._invite.getComponent('Club_Invite');
                    inviteScript.initData(this._corpsId, this);
                    this._invite.parent = dd.ui_manager.getRootNode();
                }
                break;
            default:
                break;
        }
    }
    /**
     * 退出
     * @memberof ClubTableLayer
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(1, dd.ud_manager.openClubData.corpsId);
    }
}
