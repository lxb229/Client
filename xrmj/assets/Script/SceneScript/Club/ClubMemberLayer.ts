import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ClubMemberLayer extends cc.Component {
    /**
     * 红点
     * @type {cc.Node}
     * @memberof ClubMemberLayer
     */
    @property(cc.Node)
    hotNode: cc.Node = null;
    /**
    * 俱乐部成员列表
    * @type {cc.ScrollView}
    * @memberof ClubCanvas
    */
    @property(cc.ScrollView)
    svNode_member: cc.ScrollView = null;
    /**
     * 俱乐部列表的预设
     * @type {cc.Prefab}
     * @memberof ClubCanvas
     */
    @property(cc.Prefab)
    club_member_prefab: cc.Prefab = null;
    /**
     * 名册
     * @type {cc.Toggle}
     * @memberof ClubMemberLayer
     */
    @property(cc.Toggle)
    toggle_mc: cc.Toggle = null;
    /**
     * 申请
     * @type {cc.Toggle}
     * @memberof ClubMemberLayer
     */
    @property(cc.Toggle)
    toggle_apply: cc.Toggle = null;
    /**
     * 黑名单
     * @type {cc.Toggle}
     * @memberof ClubMemberLayer
     */
    @property(cc.Toggle)
    toggle_balcklist: cc.Toggle = null;

    /**
     * 俱乐部拒绝
     * @type {cc.Prefab}
     * @memberof ClubCanvasf
     */
    @property(cc.Prefab)
    club_refuse_prefab: cc.Prefab = null;

    _refuse: cc.Node = null;
    _corpsId: string = '0';
    _showIndex: number = 1;
    onLoad() {

    }

    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof HomeCanvas
     */
    update(dt: number) {
        if (dd.ud_manager.hotTip && dd.ud_manager.hotTip[1] && dd.ud_manager.hotTip[1].hotVal > 0) {
            this.hotNode.active = true;
        } else {
            this.hotNode.active = false;
        }
    }
    /**
     * 初始化数据
     * @param {CorpsVo} clubData 
     * @returns 
     * @memberof ClubMemberLayer
     */
    initData(corpsId: string) {
        if (dd.ud_manager.openClubData) {
            this.toggle_apply.node.active = dd.ud_manager.openClubData.createPlayer === dd.ud_manager.mineData.accountId ? true : false;
            this.toggle_balcklist.node.active = dd.ud_manager.openClubData.createPlayer === dd.ud_manager.mineData.accountId ? true : false;
        }
        this._corpsId = corpsId;
        this.sendGetClubMember(corpsId);
    }
    /**
     * 获取俱乐部游戏桌子
     * @memberof ClubTableLayer
     */
    sendGetClubMember(corpsId: string, type: number = 1) {
        this._showIndex = type;
        this.toggle_mc.isChecked = type === 1 ? true : false;
        this.toggle_apply.isChecked = type === 2 ? true : false;
        this.toggle_balcklist.isChecked = type === 3 ? true : false;
        if (corpsId === '0') return;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': corpsId, 'type': type };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MEMBER_LIST, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showMemberList(this._showIndex, content.members);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 踢出或拉黑俱乐部成员
     * @param {number} type  0=不加入黑名单,1=加入黑名单
     * @param {string} kickAccountId 
     * @memberof ClubMemberLayer
     */
    sendKikiMember(type: number, kickAccountId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._corpsId, 'type': type, 'kickAccountId': kickAccountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_KICK_MEMBER, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showMemberList(this._showIndex, content.members);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 解锁黑名单玩家
     * @param {string} btAccountId 
     * @memberof ClubMemberLayer
     */
    sendRelieveMember(btAccountId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._corpsId, 'btAccountId': btAccountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MEMBER_BLACKLIST_UNLOCK, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showMemberList(this._showIndex, content.members);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 申请 同意或拒绝 加入俱乐部
     * @param {number} bt 表态(0=拒绝,1=同意)
     * @param {string} btAccountId 
     * @memberof ClubMemberLayer
     */
    sendApplyMember(bt: number, btAccountId: string, reson: string = '') {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._corpsId, 'bt': bt, 'btAccountId': btAccountId, 'reson': reson };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_QUEST_JOIN_BT, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showMemberList(this._showIndex, content.members);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 显示填写拒绝理由的界面
     * @memberof ClubMemberLayer
     */
    showRefuseReson(kickAccountId: string) {
        if (!this._refuse || !this._refuse.isValid) {
            this._refuse = cc.instantiate(this.club_refuse_prefab);
            let refuseScript = this._refuse.getComponent('Club_Refuse');
            refuseScript.initData(this, (reson: string) => {
                this.sendApplyMember(0, kickAccountId, reson);
            });
            this._refuse.parent = this.node;
        }
    }
    /**
     * 显示桌子列表信息
     * @returns 
     * @memberof ClubTableLayer
     */
    showMemberList(type: number, clubMemberList: CorpsMemberVoItem[]) {
        this.svNode_member.content.removeAllChildren(true);
        if (clubMemberList) {
            clubMemberList.forEach((member: CorpsMemberVoItem, i: number) => {
                let memberItem = cc.instantiate(this.club_member_prefab);
                let memberScript = memberItem.getComponent('Club_Member_Item');
                memberScript.updateItem(type, member, this);
                memberItem.parent = this.svNode_member.content;
            }, this);
        }
    }

    /**
     * 切换选项
     * @param {any} event 
     * @param {string} type 
     * @memberof ClubCanvas
     */
    click_btn_choice(event, type: string) {
        dd.mp_manager.playButton();
        this.sendGetClubMember(this._corpsId, Number(type) + 1);
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
