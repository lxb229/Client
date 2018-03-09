const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';

const RT: number = 5;
@ccclass
export default class Club extends cc.Component {
    /**
     * 俱乐部loading
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    club_loading: cc.Node = null;
    /**
     * 自己从来没有加入过任何俱乐部
     * 
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_empty: cc.Node = null;

    /**
     * 自己加入或创建的俱乐部列表界面
     * 
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_club: cc.Node = null;
    /**
     * 俱乐部信息界面(俱乐部桌子界面)
     * 
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_table: cc.Node = null;
    /**
     * 添加成员
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_addMember: cc.Node = null;
    /**
     * 战绩查询
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_record: cc.Node = null;

    /**
     * 俱乐部创建
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_create_club: cc.Node = null;

    /**
     * 申请加入俱乐部
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_join_club: cc.Node = null;
    /**
     * 俱乐部角色信息
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_Club_Role: cc.Node = null;
    /**
     * 成员申请列表
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    board_Club_Apply: cc.Node = null;
    /**
     * 名称
     * 
     * @type {cc.Label}
     * @memberof Club
     */
    @property(cc.Label)
    lblName: cc.Label = null;

    /**
     * 房卡
     * 
     * @type {cc.Label}
     * @memberof Club
     */
    @property(cc.Label)
    lblRoomCard: cc.Label = null;
    /**
     * 记录提示
     * 
     * @type {cc.Label}
     * @memberof Club
     */
    @property(cc.Label)
    lblRecordTip: cc.Label = null;
    /**
     * 俱乐部详细界面，成员申请的数量
     * @type {cc.Label}
     * @memberof Club
     */
    @property(cc.Label)
    lblApplyNum: cc.Label = null;

    /**
     * 头像
     * 
     * @type {cc.Sprite}
     * @memberof Club
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 创建俱乐部的输入名称
     * @type {cc.EditBox}
     * @memberof Club
     */
    @property(cc.EditBox)
    edit_club_name: cc.EditBox = null;
    /**
     * 创建俱乐部的错误提示
     * @type {cc.Label}
     * @memberof Club
     */
    @property(cc.Label)
    lblCCMsg: cc.Label = null;
    /**
     * 俱乐部状态
     * @type {cc.Label}
     * @memberof Club
     */
    @property(cc.Label)
    lblClubState: cc.Label = null;
    /**
     * 俱乐部列表
     * 
     * @type {cc.ScrollView}
     * @memberof Club
     */
    @property(cc.ScrollView)
    svNode_club: cc.ScrollView = null;

    /**
     * 俱乐部会员列表
     * 
     * @type {cc.ScrollView}
     * @memberof Club
     */
    @property(cc.ScrollView)
    svNode_club_member: cc.ScrollView = null;

    /**
     * 俱乐部在线桌子列表
     * 
     * @type {cc.ScrollView}
     * @memberof Club
     */
    @property(cc.ScrollView)
    svNode_club_table: cc.ScrollView = null;
    /**
     * 俱乐部战绩列表
     * 
     * @type {cc.ScrollView}
     * @memberof Club
     */
    @property(cc.ScrollView)
    svNode_club_record: cc.ScrollView = null;
    /**
     * 角色预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    role_prefab: cc.Prefab = null;

    /**
     * 商城预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    store_prefab: cc.Prefab = null;
    /**
     * 俱乐部预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    club_item_prefab: cc.Prefab = null;

    /**
     * 俱乐部会员预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    club_member_item_prefab: cc.Prefab = null;

    /**
     * 俱乐部房间预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    club_table_item_prefab: cc.Prefab = null;
    /**
     * 俱乐部记录预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    club_record_item_prefab: cc.Prefab = null;

    /**
     * 俱乐部创建房间预设
     * 
     * @type {cc.Prefab}
     * @memberof Club
     */
    @property(cc.Prefab)
    room_create_prefab: cc.Prefab = null;
    /**
     * 俱乐部房间的解散按钮的说明，如果是群主就是解散房间按钮 如果是成员则是退出俱乐部
     * @type {cc.Button}
     * @memberof Club
     */
    @property(cc.Label)
    lbl_disband_or_out: cc.Label = null;
    /**
     * 成员申请的按钮
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    btn_apply_join: cc.Node = null;
    /**
     * 邀请成员的按钮
     * @type {cc.Node}
     * @memberof Club
     */
    @property(cc.Node)
    btn_add_member: cc.Node = null;
    /**
     * 俱乐部房卡开关的图片列表(0=房主开 1=房主关 2=非房主开 3非房主关)
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof Club
     */
    @property([cc.SpriteFrame])
    club_use_on_off_list: cc.SpriteFrame[] = [];
    /**
     * 桌子状态图片列表
     * @type {cc.SpriteFrame[]}
     * @memberof Club
     */
    @property([cc.SpriteFrame])
    table_state_list: cc.SpriteFrame[] = [];
    /**
     * 显示索引
     * 
     * @type {number}
     * @memberof Club
     */
    _showIndex: number = 0;

    /**
     * 俱乐部列表
     * 
     * @type {CorpsVoInner}
     * @memberof Club
     */
    _clubsList: CorpsVoInner[] = [];

    /**
     * 俱乐部信息
     * 
     * @type {CorpsVoInner}
     * @memberof Club
     */
    _clubInfo: CorpsVoInner = null;

    _role: cc.Node = null;
    _room_create: cc.Node = null;
    /**
     * 数据刷新时间
     * @type {number}
     * @memberof Club
     */
    _refreshTime: number = 15;
    /**
     * 推送消息(俱乐部成员添加通知)
     * 
     * @memberof Club
     */
    Club_Add_Member_Push = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        if (data.createPlayer && data.createPlayer === dd.ud_manager.mineData.accountId) {
            this.sendGetClubByClubId(this._clubInfo.corpsId);
        } else {
            this.sendGetClubs();
        }
    }
    /**
     * 推送消息(俱乐部成员移除通知)
     * 
     * @memberof Club
     */
    Club_Kik_Member_Push = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        if (data.createPlayer && data.createPlayer === dd.ud_manager.mineData.accountId) {
            this.sendGetClubByClubId(this._clubInfo.corpsId);
        } else {
            this.sendGetClubs();
        }
    }
    /**
     * 推送消息(俱乐部解散通知)
     * 
     * @memberof Club
     */
    Club_Destory_Push = (event: cc.Event.EventCustom) => {
        let data = event.detail;
        this.sendGetClubs();
    }

    /**
    * 获取ios内购列表的回调
    * 
    * @memberof HomeCanvas
    */
    cb_getProducts: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        if (event.detail) {
            let products: Product[] = JSON.parse(event.detail);
            this.showStore(products);
        } else {
            dd.ui_manager.showAlert('获取商品信息失败', '错误提示', null, null, 1);
        }
    };

    onLoad() {
        dd.ui_manager.fixIPoneX(this.node);
        this.bindOnEvent();
        this.showInfo();
        this.sendGetClubs();
    }

    onDestroy() {
        this.bindOffEvent();
    }

    bindOnEvent() {
        //推送消息(俱乐部成员添加通知)
        cc.systemEvent.on('Club_Add_Member_Push', this.Club_Add_Member_Push, this);
        //  推送消息(俱乐部成员移除通知)
        cc.systemEvent.on('Club_Kik_Member_Push', this.Club_Kik_Member_Push, this);
        // 推送消息(俱乐部解散通知)
        cc.systemEvent.on('Club_Destory_Push', this.Club_Destory_Push, this);
        cc.systemEvent.on('cb_getProducts', this.cb_getProducts, this);
    }

    bindOffEvent() {
        //推送消息(俱乐部成员添加通知)
        cc.systemEvent.off('Club_Add_Member_Push', this.Club_Add_Member_Push, this);
        //  推送消息(俱乐部成员移除通知)
        cc.systemEvent.off('Club_Kik_Member_Push', this.Club_Kik_Member_Push, this);
        // 推送消息(俱乐部解散通知)
        cc.systemEvent.off('Club_Destory_Push', this.Club_Destory_Push, this);
        cc.systemEvent.off('cb_getProducts', this.cb_getProducts, this);
    }
    update(dt: number) {
        //刷新房卡
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblRoomCard.string = dd.ud_manager.mineData.roomCard + '';
        }
        //如果在俱乐部详细界面
        if (this._showIndex === 2) {
            //红点提示，成员申请数量
            if (dd.ud_manager && dd.ud_manager.hotTip && dd.ud_manager.hotTip[1] && dd.ud_manager.hotTip[1].hotVal > 0) {
                this.lblApplyNum.node.parent.active = true;
                this.lblApplyNum.string = dd.ud_manager.hotTip[1].hotVal;
            } else {
                this.lblApplyNum.node.parent.active = false;
            }
        }

        if (this._refreshTime > 0) {
            this._refreshTime -= dt;
            if (this._refreshTime < 0) {
                this._refreshTime = RT;
                switch (this._showIndex) {
                    case 1://刷新俱乐部列表
                        this.sendGetClubs();
                        cc.log('刷新俱乐部列表');
                        break;
                    case 2://刷新俱乐部详细信息(桌子列表)
                        this.sendGetClubByClubId(this._clubInfo.corpsId);
                        cc.log('刷新俱乐部桌子');
                        break;
                    default:
                }

            }
        }
    }
    /**
        * 显示基本信息
        * 
        * @memberof HomeCanvas
        */
    async showInfo() {
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblRoomCard.string = dd.ud_manager.mineData.roomCard + '';
            let headSF = null;
            try {
                headSF = await dd.img_manager.loadURLImage(dd.ud_manager.mineData.headImg);
            } catch (error) {
                cc.log('获取头像错误');
            }
            this.headImg.spriteFrame = headSF;
        }
    }
    /**
     * 获取俱乐部列表
     * 
     * @memberof Room_Join_Club
     */
    sendGetClubs() {
        if (!this.club_loading.active) {
            this.club_loading.active = true;
            this._refreshTime = RT;
            dd.ws_manager.sendMsg(dd.protocol.CORPS_GET_CORPS_LIST, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this._clubsList = content.items as CorpsVoInner[];
                    if (this._clubsList && this._clubsList.length > 0) {
                        this.showClub(1);
                    } else {
                        this.showClub(0);
                    }
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                this.club_loading.active = false;
            });
        }
    }

    /**
    * 根据俱乐部id获取俱乐部信息
    * 
    * @memberof Room_Join_Club
    */
    sendGetClubByClubId(clubId: string) {
        if (clubId === null || clubId === undefined || clubId.length === 0) return;
        if (!this.club_loading.active) {
            this.club_loading.active = true;
            this._refreshTime = RT;
            let obj = { 'corpsId': clubId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_GET_CORPS_DETAILED, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let data = content as CorpsDetailed;
                    this.showClub(2, false);
                    this.showClubMembers(data.members);
                    this.showClubTables(data);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                this.club_loading.active = false;
            });
        }
    }


    /**
     * 获取俱乐部所有成员
     * 
     * @memberof Room_Join_Club
     */
    sendGetClubMember(clubId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': clubId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MEMBER_LIST, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showClubMembers(content.members as CorpsMemberVoInner[]);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 获取俱乐部战绩列表
     * 
     * @memberof Room_Join_Club
     */
    sendGetClubRecord(clubId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'type': 2, 'query': clubId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_QUERY_RECORD, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showClub(3);
                    this.showClubRecord(content as RecordVo);
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
     * 群主删除战绩
     * 
     * @memberof Club
     */
    sendDeleteRecord(recordId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'recordId': recordId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.REPLAY_DELETE_RECORD, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.hideLoading();
                    this.sendGetClubRecord(this._clubInfo.corpsId);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }

    /**
     * 发送使用房卡
     * 
     * @param {string} clubId 俱乐部id
     * @param {number} state 使用状态 0关1开
     * @memberof Club
     */
    sendUseRoomCard(clubId: string, state: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': clubId, 'state': state };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_SET_ROOMCARD_STATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.hideLoading();
                    this.sendGetClubs();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }

    /**
     * 解散俱乐部
     * 
     * @param {string} clubId 
     * @memberof Club
     */
    sendDestoryClub(clubId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': clubId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_DESTORY, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.hideLoading();
                    this.showClub(1);
                    this.sendGetClubs();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }
    /**
     * 退出俱乐部
     * 
     * @param {string} clubId 
     * @memberof Club
     */
    sendExitClub(clubId: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': clubId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_EXIT, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.hideLoading();
                    this.showClub(1);
                    this.sendGetClubs();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }
    /**
     * 踢出成员
     * 
     * @param {string} clubId 
     * @param {string} starNO 
     * @memberof Club
     */
    sendKikMember(starNO: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': this._clubInfo.corpsId, 'starNO': starNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_KICK_MEMBER, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    this.board_Club_Role.active = false;
                    dd.ui_manager.showAlert('请离成功！', '温馨提示', null, null, 1);
                    this.showClub(2);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
            });
        }
    }
    /**
     *  
     * 显示俱乐部界面 0没有俱乐部 1俱乐部列表 2俱乐部详情(桌子) 3战绩查询  
     * @param {number} index 0没有俱乐部 1俱乐部列表 2俱乐部详情(桌子) 3战绩查询
     * @memberof Club
     */
    showClub(index: number, isGetData: boolean = true) {
        if (index === null || index === undefined) index = 0;
        this._showIndex = index;
        this.board_empty.active = this._showIndex === 0 ? true : false;
        this.board_club.active = this._showIndex === 1 ? true : false;
        this.board_table.active = this._showIndex === 2 ? true : false;
        this.board_record.active = this._showIndex === 3 ? true : false;
        this.club_loading.active = false;
        switch (this._showIndex) {
            case 1:
                this.showClubsList();
                break;
            case 2:
                //如果俱乐部是自己创建的
                if (this._clubInfo.createPlayer === dd.ud_manager.mineData.accountId) {
                    this.lbl_disband_or_out.string = '解散俱乐部';
                    this.btn_apply_join.active = true;
                    this.btn_add_member.active = true;
                } else {
                    this.lbl_disband_or_out.string = '退出俱乐部';
                    this.btn_apply_join.active = false;
                    this.btn_add_member.active = false;
                }
                if (isGetData) {
                    this.sendGetClubByClubId(this._clubInfo.corpsId);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示俱乐部列表
     * 
     * @memberof Club
     */
    showClubsList() {
        this.svNode_club.content.removeAllChildren();
        for (var i = 0; i < this._clubsList.length; i++) {
            let club_item = cc.instantiate(this.club_item_prefab);
            let club_script = club_item.getComponent('Club_Item');
            club_script.updateItem(i, this._clubsList[i], (clubData: CorpsVoInner) => {
                this._clubInfo = clubData;
                this.sendGetClubByClubId(clubData.corpsId);
            }, this);
            club_item.parent = this.svNode_club.content;
        }
    }

    /**
     *  显示俱乐部信息
     * 
     * @memberof Club
     */
    showClubMembers(clubmembers: CorpsMemberVoInner[]) {
        this.svNode_club_member.content.removeAllChildren();
        if (!clubmembers) return;
        for (var i = 0; i < clubmembers.length; i++) {
            let club_member_item = cc.instantiate(this.club_member_item_prefab);
            let member_script = club_member_item.getComponent('Club_Member_Item');
            member_script.updateItem(clubmembers[i], this._clubInfo, (memberInfo: CorpsMemberVoInner) => {
                this.sendGetRoleInfo(memberInfo.starNO, 1);
            }, this);
            club_member_item.parent = this.svNode_club_member.content;
        }
    }

    /**
     *  显示俱乐部桌子信息
     * 
     * @memberof Club
     */
    showClubTables(data: CorpsDetailed) {
        this.lblClubState.string = data.corpsState === 0 ? '' : '俱乐部已被冻结';
        this.svNode_club_table.content.removeAllChildren();
        let clubtables: CorpsTableInner[] = data.tables;
        if (clubtables && data.corpsState === 0) {
            for (var i = 0; i < clubtables.length; i++) {
                let club_table_item = cc.instantiate(this.club_table_item_prefab);
                let table_script = club_table_item.getComponent('Club_Table_Item');
                table_script.updateItem(clubtables[i], (clubTableData: CorpsTableInner) => {
                    this.sendJoinRoom(clubTableData.tableId);
                }, this);
                club_table_item.parent = this.svNode_club_table.content;
            }
        }
    }
    /**
     * 加入房间
     * @param {string} tableId 
     * @memberof Club
     */
    sendJoinRoom(tableId: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'tableId': tableId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.gm_manager.mjGameData = content as MJGameData;
                    dd.gm_manager.replayMJ = 0;
                    dd.gm_manager.turnToGameScene();
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.hideLoading();
                    dd.ui_manager.showAlert(content, '温馨提示', null, null, 1);
                }
            });
        }
    }
    /**
    *  显示俱乐部战绩记录信息
    * 
    * @memberof Club
    */
    showClubRecord(clubRecords: RecordVo) {
        this.svNode_club_record.content.removeAllChildren();
        if (!clubRecords) return;
        if (clubRecords.items) {
            this.lblRecordTip.node.active = false;
            for (var i = 0; i < clubRecords.items.length; i++) {
                let club_record_item = cc.instantiate(this.club_record_item_prefab);
                let record_script = club_record_item.getComponent('Club_Record_Item');
                record_script.updateItem(i + 1, clubRecords.items[i], this._clubInfo.createPlayer, (clubData: RecordItemVo) => {

                }, this);
                club_record_item.parent = this.svNode_club_record.content;
            }
        } else {
            this.lblRecordTip.node.active = true;
        }
    }

    /**
     *创建俱乐部
     * 
     * @memberof Club
     */
    click_btn_creatClub() {
        dd.mp_manager.playButton();
        let clubName = this.edit_club_name.string.trim();
        if (clubName === '' || clubName === null || clubName === undefined) {
            this.lblCCMsg.string = '*请输入俱乐部名称';
            return;
        }
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsName': clubName };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_CREATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this._clubsList = content.items as CorpsVoInner[];
                    this.board_create_club.active = false;
                    this.showClub(1);
                    dd.ui_manager.showAlert('俱乐部创建成功!', '温馨提示', null, null, 1);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    // dd.ui_manager.showAlert(content, '温馨提示');
                    this.lblCCMsg.string = '*' + content;
                }
                dd.ui_manager.hideLoading();
            });
        }
    }

    click_btn_into_create() {
        dd.mp_manager.playButton();
        this.edit_club_name.string = '';
        this.lblCCMsg.string = '';
        this.board_create_club.active = true;
    }

    click_btn_out_create() {
        dd.mp_manager.playButton();
        this.edit_club_name.string = '';
        this.lblCCMsg.string = '';
        this.board_create_club.active = false;
    }
    /**
     * 加入俱乐部
     * 
     * @memberof Club
     */
    click_btn_joinClub() {
        dd.mp_manager.playButton();
        this.board_join_club.active = true;
        let jc = this.board_join_club.getComponent('Club_Join');
        jc.showLayer(0);
        // dd.ui_manager.showAlert('请找所在微信群的群主申请加入俱乐部', '加入俱乐部');
    }

    /**
     * 俱乐部详细信息的按钮选择
     * 
     * @param {any} event 
     * @param {string} type 0俱乐部成员列表 1俱乐部战绩查询
     * @memberof Club
     */
    click_club_check(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0':// 0俱乐部成员列表 
                this.sendGetClubMember(this._clubInfo.corpsId);
                break;
            case '1':// 1俱乐部战绩查询
                this.sendGetClubRecord(this._clubInfo.corpsId);
                break;
            default:
                break;
        }
    }
    /**
     * 创建俱乐部房间
     * @memberof Club
     */
    click_club_create_room() {
        this.showCreateRoom();
    }

    /**
     *  显示创建房间
     * @memberof HomeCanvas
     */
    showCreateRoom() {
        if (dd.ui_manager.isShowPopup) {
            if (!this._room_create || !this._room_create.isValid) {
                dd.ui_manager.isShowPopup = false;
                dd.mp_manager.playAlert();
                this._room_create = cc.instantiate(this.room_create_prefab);
                this._room_create.parent = this.node;
                dd.ui_manager.hideLoading();
                let room_create_script = this._room_create.getComponent('Room_Create');
                room_create_script.showCreateMode(this._clubInfo.corpsId);
            }
        }
    }

    /**
     * 俱乐部的操作按钮点击事件
     * 
     * @memberof Club
     */
    click_club_deale(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://解散俱乐部

                let des = '';
                let title = '';
                //如果俱乐部是自己创建的
                if (this._clubInfo.createPlayer === dd.ud_manager.mineData.accountId) {
                    title = '解散俱乐部';
                    des = '是否解散俱乐部 <color=#FF0000>' + this._clubInfo.corpsName + '</c>？<br/>解散后将无法恢复！';
                } else {
                    title = '退出俱乐部';
                    des = '是否退出俱乐部 <color=#FF0000>' + this._clubInfo.corpsName + '</c>？<br/>退出后将不能再使用此群主房卡进行游戏！';
                }
                dd.ui_manager.showAlert(des, title,
                    {
                        lbl_name: '确定',
                        callback: () => {
                            //如果俱乐部是自己创建的
                            if (this._clubInfo.createPlayer === dd.ud_manager.mineData.accountId) {
                                this.sendDestoryClub(this._clubInfo.corpsId);
                            } else {
                                this.sendExitClub(this._clubInfo.corpsId);
                            }
                        }
                    }, {
                        lbl_name: '再想想',
                        callback: () => {
                        }
                    }
                    , 1);
                break;
            case '1'://申请成员
                //如果俱乐部是自己创建的
                if (this._clubInfo.createPlayer === dd.ud_manager.mineData.accountId) {
                    this.board_Club_Apply.active = true;
                    let apply_script = this.board_Club_Apply.getComponent('Club_Apply_Join');
                    apply_script.initData(this._clubInfo);
                } else {
                    dd.ui_manager.showAlert('您不是俱乐部群主，不能查看！', '温馨提示', null, null, 1);
                }
                break;
            case '2'://添加成员
                this.board_addMember.active = true;
                let am = this.board_addMember.getComponent('Club_Add_Member');
                am.initData(this._clubInfo, this);
                break;
            case '3'://退出俱乐部
                dd.ui_manager.showAlert('退出 <color=#FFFF00>俱乐部' + this._clubInfo.corpsId + '</c><br/>将不能再用此群主房卡进行游戏，<br/>确认退出俱乐部？'
                    , '退出俱乐部',
                    {
                        lbl_name: '确定',
                        callback: () => {
                            this.sendExitClub(this._clubInfo.corpsId);
                        }
                    }, {
                        lbl_name: '再想想',
                        callback: () => {

                        }
                    }
                );
                break;
            default:
                break;
        }
    }

    /**
     * 退出按钮
     * 
     * @memberof Club
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        switch (this._showIndex) {
            case 0: {
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('HomeScene');
                }
                break;
            }
            case 1: {
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('HomeScene');
                }
                break;
            }
            case 2: {
                this.sendGetClubs();
                break;
            }
            case 3: {//退出俱乐部战绩界面
                this.showClub(2, false);
                break;
            }
            default:

        }
    }

    /**
 * 点击头像，获取玩家信息
 * 
 * @memberof HomeCanvas
 */
    click_btn_head() {
        dd.mp_manager.playButton();
        this.sendGetRoleInfo(dd.ud_manager.mineData.starNO);
    }
    /**
     * 获取角色信息
     * @memberof Club
     */
    sendGetRoleInfo(starNO: string, type: number = 0) {
        if (dd.ui_manager.showLoading()) {
            dd.mp_manager.playAlert();
            let obj = { 'starNO': starNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_STARNO, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let roleInfo = content as UserData;
                    if (type === 0) {
                        if (!this._role || !this._role.isValid) {
                            this._role = cc.instantiate(this.role_prefab);
                            let roleScript = this._role.getComponent('Role');
                            roleScript.showInfo(roleInfo);
                            this._role.parent = this.node;
                        }
                    } else {
                        this.board_Club_Role.active = true;
                        let club_role = this.board_Club_Role.getComponent('Club_Role');
                        club_role.showInfo(roleInfo, this._clubInfo, this);
                    }
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }

    /**
     * 点击购买
     * 
     * @memberof HomeCanvas
     */
    click_btn_buy() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {//手机IOS端
                    dd.js_call_native.getProducts(dd.config.productids);
                } else {
                    this.showStore();
                }
            }
        }
    }

    /**
    * 显示商城
    * 
    * @memberof HomeCanvas
    */
    showStore(products?: Product[]) {
        let store = cc.instantiate(this.store_prefab);
        dd.ws_manager.sendMsg(dd.protocol.MALL_ITEMLIST, '', (flag: number, content?: any) => {
            dd.ui_manager.hideLoading();
            if (flag === 0) {//成功
                let data = content as StoreGoods;
                store.getComponent('Store').init(data.proxyItems, products);
                store.parent = this.node;
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
            }
        });
    }
}
