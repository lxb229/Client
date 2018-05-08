
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class ClubCanvas extends cc.Component {
    /**
     * 俱乐部列表界面
     * @type {cc.Node}
     * @memberof ClubCanvas
     */
    @property(cc.Node)
    board_club_list: cc.Node = null;
    /**
     * 俱乐部成员
     * @type {cc.Node}
     * @memberof ClubCanvas
     */
    @property(cc.Node)
    board_member: cc.Node = null;
    /**
     * 俱乐部排行
     * @type {cc.Node}
     * @memberof ClubCanvas
     */
    @property(cc.Node)
    board_ranking: cc.Node = null;
    /**
     * 俱乐部管理
     * @type {cc.Node}
     * @memberof ClubCanvas
     */
    @property(cc.Node)
    board_admin: cc.Node = null;
    /**
     * 俱乐部游戏房间
     * @type {cc.Node}
     * @memberof ClubCanvas
     */
    @property(cc.Node)
    board_table: cc.Node = null;
    /**
     * 显示索引
     * @type {number}
     * @memberof ClubCanvas
     */
    _showIndex: number = 0;

    /**
     * 推送消息(俱乐部解散和移除成员的通知)
     * @memberof ClubTableLayer
     */
    ClubPushCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubPushMsg;
        //如果不在俱乐部列表界面
        if (this._showIndex !== 0) {
            dd.ui_manager.showAlert(data.content, '温馨提示', {
                lbl_name: '确定',
                callback: () => {
                    this.showClubLayer(0);
                }
            }, null, 1);
        } else {
            let clubLayer = this.board_club_list.getComponent('ClubListLayer');
            if (clubLayer._showIndex === 1) clubLayer.sendGetClubs();
            dd.ui_manager.showTip(data.content);
        }
    };
    /**
     * 推送消息(帮会转让通知)
     * @memberof ClubCanvas
     */
    ClubAttornPushCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubPushMsg;
        if (dd.ud_manager.openClubData && dd.ud_manager.openClubData.corpsId === data.corpsId) {
            dd.ud_manager.openClubData.createPlayer = data.createPlayer;
        }
        switch (this._showIndex) {
            case 0:
                let clubLayer = this.board_club_list.getComponent('ClubListLayer');
                clubLayer.updateClubs();
                break;
            case 1:
                if (dd.ud_manager.openClubData && dd.ud_manager.openClubData.corpsId === data.corpsId) {
                    let tableLayer = this.board_table.getComponent('ClubTableLayer');
                    tableLayer.initData(dd.ud_manager.openClubData.corpsId);
                }
                break;
            case 2://俱乐部成员(名册)
                if (dd.ud_manager.openClubData && dd.ud_manager.openClubData.corpsId === data.corpsId) {
                    let memberLayer = this.board_member.getComponent('ClubMemberLayer');
                    memberLayer.initData(data.corpsId);
                }
                break;
            default:
                break;
        }
    };

    /**
     * 推送消息(帮会微信公告修改通知)
     * @memberof ClubCanvas
     */
    ModifyWXPushCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubModifyMsg;
        if (dd.ud_manager.openClubData && dd.ud_manager.openClubData.corpsId === data.corpsId) {
            dd.ud_manager.openClubData.wxNO = data.wxNO;
        }
        switch (this._showIndex) {
            case 0:
                let clubLayer = this.board_club_list.getComponent('ClubListLayer');
                clubLayer.updateClubs();
                break;
            case 1:
                if (dd.ud_manager.openClubData && dd.ud_manager.openClubData.corpsId === data.corpsId) {
                    let tableLayer = this.board_table.getComponent('ClubTableLayer');
                    tableLayer.setNoticeData(data);
                }
                break;
            default:
                break;
        }
    };
    onLoad() {
        //如果打开创建房间的俱乐部id不为0，则为从创建房间返回的
        if (dd.ud_manager.openClubData) {
            this.showClubLayer(1, dd.ud_manager.openClubData.corpsId);
        } else {
            this.showClubLayer(0);
        }
        cc.systemEvent.on('Club_Kik_Member_Push', this.ClubPushCallBack);//移除
        cc.systemEvent.on('Club_Destory_Push', this.ClubPushCallBack);//解散
        cc.systemEvent.on('Club_Attorn_Push', this.ClubAttornPushCallBack);//转会
        cc.systemEvent.on('Club_ModifyWX_Push', this.ModifyWXPushCallBack);//修改群主微信号
    }
    onDestroy() {
        cc.systemEvent.off('Club_Kik_Member_Push', this.ClubPushCallBack);
        cc.systemEvent.off('Club_Destory_Push', this.ClubPushCallBack);
        cc.systemEvent.off('Club_Attorn_Push', this.ClubAttornPushCallBack);//转会
        cc.systemEvent.off('Club_ModifyWX_Push', this.ModifyWXPushCallBack);//修改群主微信号
    }
    /**
     * 显示俱乐部界面
     * @memberof ClubCanvas
     */
    showClubLayer(id: number, corpsId: string = '0') {
        this._showIndex = id;
        this.board_club_list.active = id === 0 ? true : false;
        this.board_table.active = id === 1 ? true : false;
        this.board_member.active = id === 2 ? true : false;
        this.board_ranking.active = id === 3 ? true : false;
        this.board_admin.active = id === 4 ? true : false;

        switch (id) {
            case 0://俱乐部列表
                let clubLayer = this.board_club_list.getComponent('ClubListLayer');
                clubLayer.sendGetClubs();
                break;
            case 1://俱乐部桌子
                let tableLayer = this.board_table.getComponent('ClubTableLayer');
                tableLayer.initData(corpsId);
                break;
            case 2://俱乐部成员(名册)
                let memberLayer = this.board_member.getComponent('ClubMemberLayer');
                memberLayer.initData(corpsId);
                break;
            case 3://俱乐部排行
                let rankLayer = this.board_ranking.getComponent('ClubRankLayer');
                rankLayer.sendGetClubRanking(corpsId);
                break;
            case 4://俱乐部管理
                let adminLayer = this.board_admin.getComponent('ClubAminLayer');
                adminLayer.initData(corpsId);
                break;
            default:
                break;
        }
    }
}
