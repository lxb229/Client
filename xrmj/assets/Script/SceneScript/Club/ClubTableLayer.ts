import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ClubTableLayer extends cc.Component {

    @property(cc.ScrollView)
    svNode_table: cc.ScrollView = null;

    @property(cc.ScrollView)
    svNode_info: cc.ScrollView = null;

    @property(cc.Prefab)
    club_table_prefab: cc.Prefab = null;

    @property(cc.Prefab)
    club_msg_prefab: cc.Prefab = null;

    @property(cc.Label)
    lblClubGold: cc.Label = null;

    @property(cc.Node)
    node_hot: cc.Node = null;
    /**
     * 公告内容
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblNoticeCT: cc.Label = null;
    /**
     * 跑马灯节点
     * 
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    msgLayout: cc.Node = null;
    /**
     * 公告父节点
     * 
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    noticeNode: cc.Node = null;
    /**
     * 管理按钮
     * @type {cc.Node}
     * @memberof ClubTableLayer
     */
    @property(cc.Node)
    btnAdmin: cc.Node = null;

    @property(cc.Layout)
    btnLayout: cc.Layout = null;
    /**
     * 游戏桌子详细规则预设
     * @type {cc.Prefab}
     * @memberof ClubTableLayer
     */
    @property(cc.Prefab)
    club_room_detail: cc.Prefab = null;
    /**
     * 捐赠房卡预设
     * @type {cc.Prefab}
     * @memberof ClubTableLayer
     */
    @property(cc.Prefab)
    club_give_prefab: cc.Prefab = null;

    /**
     * 加入密码房间的预设
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    join_pwd_prefab: cc.Prefab = null;
    /**
     * 俱乐部桌子信息
     * @type {CorpsTable}
     * @memberof ClubTableLayer
     */
    _clubTable: CorpsTable = null;
    /**
     * 
     * @type {ClubTableMsg[]}
     * @memberof ClubTableLayer
     */
    _clubMsgList: ClubTableMsg[] = [];

    _club_room_detail: cc.Node = null;
    _club_give: cc.Node = null;
    _join_pwd: cc.Node = null;
    /**
     * 推送消息(帮会房间创建)
     * @memberof ClubTableLayer
     */
    ClubMsgCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubTableMsg;
        //如果推送的消息是这个打开的俱乐部
        if (data.corpsId === dd.ud_manager.openClubData.corpsId) {
            if (this._clubMsgList.length >= 30) {
                this._clubMsgList.pop();
            }
            this._clubMsgList.unshift(data);
            this.showTableMsgList();
        }
    };
    /**
     * 俱乐部钱包变化回调
     * @memberof ClubTableLayer
     */
    ClubWalletCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubWalletPush;
        //如果推送的消息是这个打开的俱乐部
        if (data.corpsId === dd.ud_manager.openClubData.corpsId) {
            this._clubTable.cardNum = data.roomCard;
        }
    };
    /**
     * 俱乐部桌子数据改变推送回调
     * @memberof ClubTableLayer
     */
    ClubTableCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubTableChange;
        if (this._clubTable) {
            if (!this._clubTable.tables) this._clubTable.tables = [];
            switch (data.type) {
                case 1://增加
                    this._clubTable.tables.push(data.table);
                    break;
                case 2://修改
                    for (let i = 0; i < this._clubTable.tables.length; i++) {
                        if (this._clubTable.tables[i].tableId === data.table.tableId) {
                            this._clubTable.tables[i] = data.table;
                            break;
                        }
                    }
                    break;
                case 3://删除
                    for (let i = 0; i < this._clubTable.tables.length; i++) {
                        if (this._clubTable.tables[i].tableId === data.table.tableId) {
                            this._clubTable.tables.splice(i, 1);
                            break;
                        }
                    }
                    for (let i = 0; i < this._clubMsgList.length; i++) {
                        if (this._clubMsgList[i].tableId === data.table.tableId) {
                            this._clubMsgList.splice(i, 1);
                            break;
                        }
                    }
                    this.showTableMsgList();
                    break;
                default:
                    break;
            }
            this.showTableList(this._clubTable);
        }
    };

    onLoad() {
        cc.systemEvent.on('Club_Msg_Push', this.ClubMsgCallBack);//游戏房间创建的消息
        cc.systemEvent.on('Club_Table_Push', this.ClubTableCallBack);//游戏房间数据变化
        cc.systemEvent.on('Club_Wallet_Push', this.ClubWalletCallBack);//游戏房间钱包数据变化
    }
    onDestroy() {
        cc.systemEvent.off('Club_Msg_Push', this.ClubMsgCallBack);
        cc.systemEvent.off('Club_Table_Push', this.ClubTableCallBack);
        cc.systemEvent.off('Club_Wallet_Push', this.ClubWalletCallBack);//游戏房间钱包数据变化
    }
    /**
     * 刷新数据
     * @param {string} corpsId 
     * @memberof ClubTableLayer
     */
    initData(corpsId: string) {
        this.btnAdmin.active = dd.ud_manager.openClubData.createPlayer === dd.ud_manager.mineData.accountId ? true : false;
        this.btnLayout.cellSize.width = dd.ud_manager.openClubData.createPlayer === dd.ud_manager.mineData.accountId ? Math.round(this.node.width / 4) : Math.round(this.node.width / 3);
        this._clubMsgList.length = 0;
        this.showTableMsgList();
        this.sendGetClubTables(corpsId);
    }
    /**
     * 公告数据变化
     * @param {ClubModifyMsg} data 
     * @memberof ClubTableLayer
     */
    setNoticeData(data: ClubModifyMsg) {
        if (this._clubTable) this._clubTable.corpsNotice = data.corpsNotice;
    }
    /**
     * 获取俱乐部游戏桌子
     * @memberof ClubTableLayer
     */
    sendGetClubTables(corpsId: string) {
        if (corpsId === '0') return;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': corpsId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_TABLE_LIST, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showTableList(content as CorpsTable);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }


    /**
     * 发送加入房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendJoinRoom(tableId: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'tableId': tableId, 'password': '0' };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log('加入成功');
                    dd.gm_manager.turnToGameScene(content as MJGameData, 0);
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
                cc.log(content);
            });
        }
    }

    /**
     * 显示桌子列表信息
     * @returns 
     * @memberof ClubTableLayer
     */
    showTableList(clubTable: CorpsTable) {
        this._clubTable = clubTable;
        this.svNode_table.content.removeAllChildren(true);
        if (clubTable.tables) {
            clubTable.tables.forEach((table: CorpsTableItem, i: number) => {
                let tableItem = cc.instantiate(this.club_table_prefab);
                let tableScript = tableItem.getComponent('Club_Table_Item');
                tableScript.updateItem(table, this);
                tableItem.parent = this.svNode_table.content;
            }, this);
        }
    }
    /**
     * 显示消息列表
     * @memberof ClubTableLayer
     */
    showTableMsgList() {
        this.svNode_info.content.removeAllChildren(true);
        if (this._clubMsgList.length > 0) {
            this._clubMsgList.forEach((clubMsg: ClubTableMsg, i: number) => {
                let msgNode = cc.instantiate(this.club_msg_prefab);
                let lblMsg = msgNode.getChildByName('lblMsg');
                if (lblMsg) {
                    let str = '<color=#a56000e>' + clubMsg.nick + '</c>创建了一局“' + clubMsg.gameName + '”牌局，小伙伴们速速来战！';
                    lblMsg.getComponent(cc.RichText).string = str;
                }
                msgNode.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                    if (clubMsg.password === 1) {
                        cc.log('请输入密码');
                        this.showJoinPwd(clubMsg.tableId);
                    } else {
                        cc.log('无密码');
                        this.sendJoinRoom(clubMsg.tableId);
                    }
                    event.stopPropagation();
                });
                msgNode.parent = this.svNode_info.content;
            });
        }
    }
    /**
    * 界面刷新
    * 
    * @param {number} dt 
    * @memberof HomeCanvas
    */
    update(dt: number) {
        if (this._clubTable) {
            this.lblClubGold.string = this._clubTable.cardNum;
            let temp = this._clubTable.corpsNotice.replace(/\n/g, ' ');
            this.lblNoticeCT.string = temp;
            // this.lblNoticeCT.string = '大王叫我来巡山，我来牌馆转一转。胡着我的牌，数着赢的钱，生活充满节奏感。'
            let widget = this.msgLayout.getComponent(cc.Widget);
            if (widget.left < 0 && Math.abs(widget.left) >= this.msgLayout.width) {
                widget.left = this.noticeNode.width;
            } else {
                widget.left -= 1;
            }
        }
        //如果这个打开的俱乐部时自己创建的，并且有红点，就显示红点
        if (dd.ud_manager.openClubData.createPlayer === dd.ud_manager.mineData.accountId
            && dd.ud_manager.hotTip && dd.ud_manager.hotTip[1] && dd.ud_manager.hotTip[1].hotVal > 0) {
            this.node_hot.active = true;
        } else {
            this.node_hot.active = false;
        }
    }
    /**
     * 显示房间详细规则
     * @memberof ClubTableLayer
     */
    showRoomDetail(data: CorpsTableItem) {
        if (!this._club_room_detail || !this._club_room_detail.isValid) {
            this._club_room_detail = cc.instantiate(this.club_room_detail);
            let detailScript = this._club_room_detail.getComponent('Club_Room_Detail');
            detailScript.initData(0, data, this);
            this._club_room_detail.parent = dd.ui_manager.getRootNode();
        }
    }
    /**
     * 显示加入房间输入密码界面
     * @param {number} tableId 
     * @memberof JoinCanvas
     */
    showJoinPwd(tableId: number) {
        if (!this._join_pwd || !this._join_pwd.isValid) {
            this._join_pwd = cc.instantiate(this.join_pwd_prefab);
            let jPwdScript = this._join_pwd.getComponent('Room_Join_Pwd');
            jPwdScript.initData(tableId, this);
            this._join_pwd.parent = dd.ui_manager.getRootNode();
        }
    }
    /**
     * 捐赠房卡的按钮
     * @memberof ClubTableLayer
     */
    click_btn_give() {
        dd.mp_manager.playButton();
        if (!this._club_give || !this._club_give.isValid) {
            this._club_give = cc.instantiate(this.club_give_prefab);
            let giveScript = this._club_give.getComponent('Club_Give');
            giveScript.initData(this._clubTable.corpsId, this);
            this._club_give.parent = dd.ui_manager.getRootNode();
        }
    }
    /**
     * 俱乐部详情 的按钮点击事件
     * @param {any} event 
     * @param {string} type 
     * @memberof ClubTableLayer
     */
    click_btn_table(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://名册
                dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(2, this._clubTable.corpsId);
                break;
            case '1'://排行
                dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(3, this._clubTable.corpsId);
                break;
            case '2'://管理
                dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(4, this._clubTable.corpsId);
                break;
            case '3'://创建房间
                if (dd.ui_manager.showLoading()) {
                    dd.ud_manager.openCreateRoom = 1;
                    cc.director.loadScene('CreateScene');
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
        dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(0);
    }
}
