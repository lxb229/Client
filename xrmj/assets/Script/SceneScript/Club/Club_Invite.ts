
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Invite extends cc.Component {

    @property(cc.Node)
    board_input: cc.Node = null;

    @property(cc.Node)
    board_role: cc.Node = null;
    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Invite
     */
    @property(cc.EditBox)
    edit_id: cc.EditBox = null;

    @property(cc.Label)
    lblName: cc.Label = null;

    @property(cc.Label)
    lblIP: cc.Label = null;

    @property(cc.Label)
    lblSex: cc.Label = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;

    _parentTarget = null;
    _clubId: string = '0';
    _roleInfo: UserData = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    /**
     * 初始化数据
     * @param {string} clubId 
     * @memberof Club_Invite
     */
    initData(clubId: string, target) {
        this._clubId = clubId;
        this._parentTarget = target;
        this.board_input.active = true;
        this.board_role.active = false;
    }
    /**
     * 获取角色信息
     * @param {string} starNO 
     * @memberof Club_Invite
     */
    sendGetRoleInfo(starNO: string) {
        if (dd.ui_manager.showLoading()) {
            dd.mp_manager.playAlert();
            let obj = { 'starNO': starNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_STARNO, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.board_input.active = false;
                    this.board_role.active = true;
                    this.showRoleInfo(content);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 发送转让明星号
     * @param {string} starNO 
     * @memberof Club_Invite
     */
    sendInvite(starNO: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubId, 'btStarNO': starNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_YAOQING_JOIN, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    //刷新列表
                    dd.ui_manager.showTip('邀请成功！');
                    this.node.removeFromParent(true);
                    this.node.destroy();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }

    /**
     * 显示信息
     * 
     * @memberof Role
     */
    async showRoleInfo(roleInfo: UserData) {
        this._roleInfo = roleInfo;
        this.lblName.string = roleInfo.nick;
        this.lblIP.string = 'IP: ' + roleInfo.clientIP;
        switch (roleInfo.sex) {
            case 1:
                this.lblSex.string = '性别:男';
                break;
            case 2:
                this.lblSex.string = '性别:女';
                break;
            default:
                this.lblSex.string = '性别:保密';
                break;
        }
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(roleInfo.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.headImg.spriteFrame = headSF;
    }
    /**
     * 确定
     * @returns 
     * @memberof Club_Search
     */
    click_btn_role() {
        dd.mp_manager.playButton();
        let idStr = this.edit_id.string.trim();
        if (idStr === '' || idStr.length === 0) {
            dd.ui_manager.showTip('*邀请ID不能为空,请重新输入！');
            return;
        }
        this.sendGetRoleInfo(idStr);
    }
    /**
     * 邀请
     * @returns 
     * @memberof Club_Invite
     */
    click_btn_invite() {
        dd.mp_manager.playButton();
        this.sendInvite(this._roleInfo.starNO);
    }
    /**
     * 退出
     * 
     * @memberof Club_Invite
     */
    click_btn_out(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0':
                this.node.removeFromParent(true);
                this.node.destroy();
                break;
            case '1':
                this.board_input.active = true;
                this.board_role.active = false;
                this.edit_id.string = '';
                break;
            default:
                break;
        }
    }

}
