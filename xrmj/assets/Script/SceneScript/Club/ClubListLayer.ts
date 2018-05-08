import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ClubLayer extends cc.Component {
    @property(cc.Label)
    lblMsg: cc.Label = null;
    /**
     * 俱乐部列表
     * @type {cc.ScrollView}
     * @memberof ClubCanvas
     */
    @property(cc.ScrollView)
    svNode_clubs: cc.ScrollView = null;
    /**
     * 俱乐部列表的预设
     * @type {cc.Prefab}
     * @memberof ClubCanvas
     */
    @property(cc.Prefab)
    club_item_prefab: cc.Prefab = null;
    /**
     * 俱乐部创建
     * @type {cc.Prefab}
     * @memberof ClubLayer
     */
    @property(cc.Prefab)
    club_create_prefab: cc.Prefab = null;
    /**
     * 俱乐部搜索
     * @type {cc.Prefab}
     * @memberof ClubLayer
     */
    @property(cc.Prefab)
    club_search_prefab: cc.Prefab = null;

    /**
     * 俱乐部加入
     * @type {cc.Prefab}
     * @memberof ClubCanvasf
     */
    @property(cc.Prefab)
    club_join_prefab: cc.Prefab = null;

    /**
     * 按钮
     * @type {cc.Toggle[]}
     * @memberof ClubRankLayer
     */
    @property([cc.Toggle])
    toggle_list: cc.Toggle[] = [];
    /**
     * 显示界面
     * @type {number}
     * @memberof ClubLayer
     */
    _showIndex: number = 1;

    _create: cc.Node = null;
    _search: cc.Node = null;
    _join: cc.Node = null;
    /**
     * 推送消息(俱乐部成员添加通知)
     * @memberof ClubLayer
     */
    ClubAddCallBack = (event: cc.Event.EventCustom) => {
        let data = event.detail as ClubPushMsg;
        if (this._showIndex !== 2) {//如果刚好正在 已加入 界面，就需要刷新界面
            dd.ui_manager.showTip(data.content);
            this.updateClubs();
        }
    };
    onLoad() {
        cc.systemEvent.on('Club_Add_Member_Push', this.ClubAddCallBack);//添加
    }

    onDestroy() {
        cc.systemEvent.off('Club_Add_Member_Push', this.ClubAddCallBack);
    }

    updateClubs() {
        this.sendGetClubs(this._showIndex);
    }
    /**
     *  获取俱乐部列表
     * @param {number} type 1=已加入 2=推荐 3=所有
     * @memberof ClubLayer
     */
    sendGetClubs(type: number = 1) {
        this._showIndex = type;
        this.toggle_list.forEach((toggle_btn: cc.Toggle, i: number) => {
            toggle_btn.isChecked = type === (i + 1) ? true : false;
        });
        if (dd.ui_manager.showLoading()) {
            let obj = { 'type': type };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_GET_CORPS_LIST, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showClubList(content.items as CorpsVo[], type);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 显示俱乐部列表
     * @param {CorpsVo[]} clubsList 
     * @memberof ClubLayer
     */
    showClubList(clubsList: CorpsVo[], type: number) {
        this.svNode_clubs.content.removeAllChildren(true);
        if (!clubsList || clubsList.length === 0) {
            this.svNode_clubs.node.active = false;
            this.lblMsg.node.active = type === 1 ? true : false;
            return;
        }
        this.lblMsg.node.active = false;
        this.svNode_clubs.node.active = true;
        clubsList.forEach((club: CorpsVo, i: number) => {
            let clubItem = cc.instantiate(this.club_item_prefab);
            let clubScript = clubItem.getComponent('Club_Item');
            clubScript.updateItem(club, this);
            clubItem.parent = this.svNode_clubs.content;
        }, this);
    }
    /**
     * 显示加入俱乐部
     * @param {number} clubId 要加入的俱乐部id
     * @param {number} type 0=在推荐中申请 1=搜索申请
     * @memberof ClubCanvas
     */
    showClubJoin(clubId: number, type: number) {
        if (!this._join || !this._join.isValid) {
            this._join = cc.instantiate(this.club_join_prefab);
            let joinScript = this._join.getComponent('Club_Join');
            joinScript.initData(clubId, type, this);
            this._join.parent = dd.ui_manager.getRootNode();
        }
    }

    /**
     * 切换选项
     * @param {any} event 
     * @param {string} type 
     * @memberof ClubCanvas
     */
    click_btn_choose(event, type: string) {
        dd.mp_manager.playButton();
        this.sendGetClubs(Number(type) + 1);
    }
    /**
     * 俱乐部列表按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof ClubLayer
     */
    click_btn_club(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://创建
                if (!this._create || !this._create.isValid) {
                    this._create = cc.instantiate(this.club_create_prefab);
                    let createScript = this._create.getComponent('Club_Create');
                    createScript.initData(this);
                    this._create.parent = dd.ui_manager.getRootNode();
                }
                break;
            case '1'://搜索
                if (!this._search || !this._search.isValid) {
                    this._search = cc.instantiate(this.club_search_prefab);
                    let searchScript = this._search.getComponent('Club_Search');
                    searchScript.initData(this);
                    this._search.parent = dd.ui_manager.getRootNode();
                }
                break;
            default:
                break;
        }
    }
    /**
     * 退出按钮
     * @memberof ClubCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
