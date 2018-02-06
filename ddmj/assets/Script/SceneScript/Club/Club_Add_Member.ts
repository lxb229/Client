import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Add_Member extends cc.Component {

    /**
     * 输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Add_Member
     */
    @property(cc.EditBox)
    edit_starNo: cc.EditBox = null;

    /**
     * 信息提示
     * 
     * @type {cc.Label}
     * @memberof Club_Add_Member
     */
    @property(cc.Label)
    lblMsg: cc.Label = null;

    @property(cc.Node)
    node_player: cc.Node = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;

    @property(cc.Label)
    lblName: cc.Label = null;

    @property(cc.Label)
    lblId: cc.Label = null;

    @property(cc.Label)
    lblIP: cc.Label = null;

    @property(cc.Label)
    lblBtnName1: cc.Label = null;

    @property(cc.Label)
    lblBtnName2: cc.Label = null;
    /**
     * 俱乐部信息
     * 
     * @type {CorpsVoInner}
     * @memberof Club_Add_Member
     */
    _clubInfo: CorpsVoInner = null;
    /**
     * 脚本
     * 
     * @memberof Club_Add_Member
     */
    _canvasTarget = null;

    _roleInfo: UserData = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this.node.active = false;
            event.stopPropagation();
        }, this);
        this.lblMsg.string = '';
        this.edit_starNo.string = '';
    }
    initData(clubInfo: CorpsVoInner, target) {
        this._canvasTarget = target;
        this._clubInfo = clubInfo;
        this.showAddLayer(0);
    }

    showAddLayer(type: number = 0) {
        this.edit_starNo.string = '';
        this.lblMsg.string = '';
        if (type === 0) {
            this.edit_starNo.node.active = true;
            this.node_player.active = false;
            this.lblBtnName1.string = '搜索好友';
            this.lblBtnName2.string = '取消搜索';
        } else {
            this.edit_starNo.node.active = false;
            this.node_player.active = true;
            this.lblBtnName1.string = '邀请好友';
            this.lblBtnName2.string = '取消邀请';
        }
    }
    /**
     * 显示提示信息
     * 
     * @param {number} type 
     * @param {string} msg 
     * @memberof Club_Add_Member
     */
    showMsg(type: number, msg: string) {
        this.lblMsg.node.color = type === 0 ? cc.Color.RED : cc.Color.GREEN;
        this.lblMsg.string = msg;
    }

    /**
     * 添加成员到俱乐部
     * 
     * @param {string} starNO 
     * @memberof Club
     */
    sendAddMember(starNO: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubInfo.corpsId, 'starNO': starNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_ADD_MEMBER, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this._canvasTarget.showClub(2);
                    this.showAddLayer(0);
                    dd.ui_manager.showTip('添加成功，可以添加继续添加下一位');
                    this.showMsg(1, '添加成功，可以添加继续添加下一位');
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }
    /**
     * 获取玩家信息
     * @param {string} starNode 
     * @memberof Club_Add_Member
     */
    sendGetRoleInfo(starNode: string) {
        if (dd.ui_manager.showLoading()) {
            dd.mp_manager.playAlert();
            let obj = { 'starNO': starNode };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_STARNO, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.showAddLayer(1);
                    let roleInfo = content as UserData;
                    this.showHead(roleInfo);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    this.showMsg(0, content);
                }
            });
        }
    }

    async showHead(roleInfo: UserData) {
        this._roleInfo = roleInfo;
        this.lblId.string = 'ID:' + roleInfo.starNO;
        this.lblIP.string = 'IP: ' + roleInfo.clientIP;
        this.lblName.string = roleInfo.nick;
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(roleInfo.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.headImg.spriteFrame = headSF;
    }

    /**
     * 添加成员
     * 
     * @returns 
     * @memberof Club_Add_Member
     */
    click_btn_add() {
        dd.mp_manager.playButton();
        if (this.edit_starNo.node.active) {
            let starNO = this.edit_starNo.string.trim();
            if (starNO === '' || starNO.length === 0) {
                this.showMsg(0, '*玩家ID不能为空,请重新输入！');
                return;
            }
            this.sendGetRoleInfo(starNO);
        } else {
            if (this._roleInfo) {
                let starNO = this._roleInfo.starNO;
                this.sendAddMember(starNO);
            }
        }
    }

    /**
     * 退出
     * 
     * @memberof Club_Add_Member
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (this.edit_starNo.node.active) {
            this.node.active = false;
        } else {
            this.showAddLayer(0);
        }
    }
}
