
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class NewClass extends cc.Component {

    @property(cc.EditBox)
    edit_clubId: cc.EditBox = null;

    @property(cc.Node)
    node_club_info: cc.Node = null;

    @property(cc.Label)
    lblClubName: cc.Label = null;

    @property(cc.Label)
    lblClubNum: cc.Label = null;

    @property(cc.Label)
    lblErr: cc.Label = null;

    @property(cc.Label)
    lblBtnName1: cc.Label = null;

    @property(cc.Label)
    lblBtnName2: cc.Label = null;

    _clubInfo: CorpsDetailed = null;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this.node.active = false;
            event.stopPropagation();
        }, this);
        this.showLayer(0);
    }

    showLayer(type: number) {
        this.edit_clubId.string = '';
        this.lblErr.string = '';
        this.edit_clubId.node.active = type === 0 ? true : false;
        this.node_club_info.active = type === 1 ? true : false;
        this.lblBtnName1.string = type === 0 ? '搜索俱乐部' : '确认加入';
        this.lblBtnName2.string = type === 0 ? '取消搜索' : '取消加入';
    }
    /**
      * 根据俱乐部id获取俱乐部信息
      * 
      * @memberof Room_Join_Club
      */
    sendGetClubByClubId(clubId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': clubId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_GET_CORPS_DETAILED, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    let data = content as CorpsDetailed;
                    if (data) {
                        this._clubInfo = data;
                        this.showLayer(1);
                        this.showClubInfo(data);
                    }
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    this.lblErr.string = '*' + content;
                }
            });
        }
    }
    /**
     * 发送加入俱乐部申请
     * @memberof NewClass
     */
    sendJoinClub() {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubInfo.corpsId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_QUEST_JOIN, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    dd.ui_manager.showTip('已提交您的申请');
                    this.node.active = false;
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
            });
        }
    }
    /**
     * 显示俱乐部信息
     * 
     * @param {CorpsDetailed} data 
     * @memberof NewClass
     */
    showClubInfo(data: CorpsDetailed) {
        this.lblClubName.string = data.corpsName;
        if (data.members) {
            this.lblClubNum.string = data.members.length + '';
        } else {
            this.lblClubNum.string = '0';
        }
    }

    click_btn_ok() {
        dd.mp_manager.playButton();
        if (this.node_club_info.active) {
            this.sendJoinClub();
        } else {
            let clubId = this.edit_clubId.string.trim();
            if (clubId === '') {
                this.lblErr.string = '*请输入俱乐部ID';
                return;
            }
            this.sendGetClubByClubId(clubId);
        }
    }

    click_btn_cancel() {
        dd.mp_manager.playButton();
        if (this.node_club_info.active) {
            this.showLayer(0);
        } else {
            this.node.active = false;
        }
    }
}
