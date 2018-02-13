
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import Role from './Role';

@ccclass
export default class HomeCanvas extends cc.Component {

    /**
     * 玩家名称
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblName: cc.Label = null;

    /**
     * 房卡数量
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblGems: cc.Label = null;

    /**
     * 微信公众号
     * 
     * @type {cc.RichText}
     * @memberof HomeCanvas
     */
    @property(cc.RichText)
    lblwxgzh: cc.RichText = null;
    /**
     * 玩家头像
     * 
     * @type {cc.Sprite}
     * @memberof HomeCanvas
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;

    /**
     * 公告父节点
     * 
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    noticeNode: cc.Node = null;
    /**
     * 公告界面
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    noticeLayer: cc.Node = null;
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
     * 角色信息预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    role_prefab: cc.Prefab = null;
    /**
     * 设置界面预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    setting_prefab: cc.Prefab = null;

    /**
     * 认证界面预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    auth_prefab: cc.Prefab = null;

    /**
     * 手机绑定界面预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    auth_phone_prefab: cc.Prefab = null;
    /**
     * 战绩界面预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    record_prefab: cc.Prefab = null;

    /**
     * 创建房间的预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    room_create_prefab: cc.Prefab = null;

    /**
     * 加入普通房间的预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    room_join_normal_prefab: cc.Prefab = null;
    /**
     * 商城的预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    store_prefab: cc.Prefab = null;

    /**
     * 帮助的预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    help_prefab: cc.Prefab = null;

    /**
     * 邮件消息的预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    email_prefab: cc.Prefab = null;

    /**
     * 赠送房卡的预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    gift_prefab: cc.Prefab = null;

    /**
     * 提示列表
     * 
     * @type {cc.Node[]}
     * @memberof HomeCanvas
     */
    @property([cc.Node])
    hotTipList: cc.Node[] = [];
    /**
     * 实名认证和手机绑定
     * 
     * @type {cc.Sprite[]}
     * @memberof HomeCanvas
     */
    @property([cc.Sprite])
    imgAuthList: cc.Sprite[] = [];
    /**
     * 认证和已认证图片
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof HomeCanvas
     */
    @property([cc.SpriteFrame])
    auth_on_off: cc.SpriteFrame[] = [];
    /**
     * 是否在跑公告
     * 
     * @type {boolean}
     * @memberof HomeCanvas
     */
    _isRunNotice: boolean = false;


    /**
     * 
     * 弹框节点
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    _role: cc.Node = null;
    _room_create: cc.Node = null;
    _room_join: cc.Node = null;
    _email: cc.Node = null;
    _help: cc.Node = null;
    _setting: cc.Node = null;
    _auth: cc.Node = null;
    _auth_phone: cc.Node = null;
    _record: cc.Node = null;
    _gift: cc.Node = null;
    _noticeStr: string = '';
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
        dd.ui_manager.hideLoading();
        this.showInfo();
        this.showHead();
        cc.systemEvent.on('cb_getProducts', this.cb_getProducts, this);
        cc.systemEvent.on('qyiap', this.qyiap, this);
        dd.mp_manager.playBackGround();
    }

    qyiap() {
        this.showStore();
    }

    onDestroy() {
        cc.systemEvent.off('cb_getProducts', this.cb_getProducts, this);
        cc.systemEvent.off('qyiap', this.qyiap, this);
    }

    start() {
        //拉回桌子
        if (dd.ud_manager && dd.ud_manager.mineData && dd.ud_manager.mineData.tableId !== 0) {
            if (dd.ui_manager.showLoading('正在重新进入未完成的游戏')) {
                let obj = { 'tableId': dd.ud_manager.mineData.tableId };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                    if (flag === 0) {//成功
                        dd.gm_manager.mjGameData = content as MJGameData;
                        dd.gm_manager.replayMJ = 0;
                        switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
                            case 1:
                                cc.director.loadScene('MJScene');
                                break;
                            case 2:
                            case 3:
                                cc.director.loadScene('SRMJScene');
                                break;
                            case 4:
                                cc.director.loadScene('LRMJScene');
                                break;
                            case 5:
                                cc.director.loadScene('MYMJScene');
                                break;
                            default:
                        }
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
    }
    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof HomeCanvas
     */
    update(dt: number) {
        this.checkPos();
        this.showInfo();
    }

    /**
     * 显示基本信息
     * 
     * @memberof HomeCanvas
     */
    showInfo() {
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblGems.string = dd.ud_manager.mineData.roomCard + '';
            // this.lblwxgzh.string = '微信公众号：<u> 888888 </u>';
            this.lblwxgzh.node.active = false;
            //显示手机和实名认证的状态
            if (dd.ud_manager.mineData.authenticationFlag && dd.ud_manager.mineData.authenticationFlag === 1) {
                this.imgAuthList[0].node.active = true;
                this.imgAuthList[0].spriteFrame = this.auth_on_off[0];
            } else {
                this.imgAuthList[0].node.active = false;
            }
            if (dd.ud_manager.mineData.phone && dd.ud_manager.mineData.phone !== '') {
                this.imgAuthList[1].node.active = true;
                this.imgAuthList[1].spriteFrame = this.auth_on_off[1];
            } else {
                this.imgAuthList[1].node.active = false;
            }
        }
        //如果存在红点提示
        for (var i = 0; i < this.hotTipList.length; i++) {
            if (dd.ud_manager && dd.ud_manager.hotTip && dd.ud_manager.hotTip[i] && dd.ud_manager.hotTip[i].hotVal === 1) {
                this.hotTipList[i].active = true;
            } else {
                this.hotTipList[i].active = false;
            }
        }
    }

    async showHead() {
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblGems.string = dd.ud_manager.mineData.roomCard + '';

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
     * 更新公告显示位置（跑灯效果）
     * 
     * @memberof HomeCanvas
     */
    checkPos() {
        if (this._isRunNotice) {
            let widget = this.msgLayout.getComponent(cc.Widget);
            if (widget.left < 0 && Math.abs(widget.left) >= this.msgLayout.width) {
                this.msgLayout.removeAllChildren();
                widget.left = this.noticeNode.width;
                this._isRunNotice = false;
                this.noticeNode.active = false;
            } else {
                widget.left -= 1;
            }
        } else {
            if (dd.ud_manager && dd.ud_manager.noticeList && dd.ud_manager.noticeList.length > 0) {
                this.createNotice(dd.ud_manager.noticeList.shift());
            } else {
                let np: NoticeParamAttrib = {
                    content: '抵制不良游戏, 拒绝盗版游戏。 注意自我保护, 谨防受骗上当。 适度游戏益脑, 沉迷游戏伤身。 合理安排时间, 享受健康生活！',
                    type: 2,
                    color: [255, 255, 255]
                };

                let notice: NoticeNotify = {
                    msgId: '0',
                    contents: [np],
                };
                this.createNotice(notice);
            }
        }
    }
    /**
     * 创建公告消息
     * 
     * @memberof HomeCanvas
     */
    createNotice(data: NoticeNotify) {
        this.msgLayout.removeAllChildren();
        for (let i = 0; i < data.contents.length; i++) {
            let msgData = data.contents[i];
            let tNode = new cc.Node('lblNotice');
            tNode.color = new cc.Color(msgData.color[0], msgData.color[1], msgData.color[2]);
            let lbl = tNode.addComponent(cc.Label);
            lbl.fontSize = 30;
            lbl.lineHeight = 30;
            lbl.overflow = cc.Label.Overflow.NONE;
            lbl.horizontalAlign = cc.Label.HorizontalAlign.CENTER;
            lbl.verticalAlign = cc.Label.VerticalAlign.CENTER;
            lbl.string = msgData.content;
            // lbl.string = '大王叫我来巡山，我来牌馆转一转。胡着我的牌，数着赢的钱，生活充满节奏感。';
            this.msgLayout.addChild(tNode);
            this._noticeStr = msgData.content;
            //如果公告
            if (this.noticeLayer.active) {
                this.lblNoticeCT.node.color = new cc.Color(msgData.color[0], msgData.color[1], msgData.color[2]);
                this.lblNoticeCT.string = msgData.content;
            }
        }
        this._isRunNotice = true;
        this.noticeNode.active = true;
    }

    /**
     * 点击头像，获取玩家信息
     * 
     * @memberof HomeCanvas
     */
    click_btn_head() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                dd.mp_manager.playAlert();
                let obj = { 'accountId': dd.ud_manager.mineData.accountId };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_ACCOUNTID, msg, (flag: number, content?: any) => {
                    if (flag === 0) {//成功
                        let roleInfo = content as UserData;
                        if (!this._role || !this._role.isValid) {
                            dd.ui_manager.isShowPopup = false;
                            this._role = cc.instantiate(this.role_prefab);
                            let roleScript: Role = this._role.getComponent('Role');
                            roleScript.showInfo(roleInfo);
                            this._role.parent = this.node;
                        }
                    } else if (flag === -1) {//超时
                    } else {//失败,content是一个字符串
                        cc.log(content);
                    }
                    dd.ui_manager.hideLoading();
                });
            }
        }
    }
    /**
     * 点击公告
     * @memberof HomeCanvas
     */
    click_btn_notice() {
        this.noticeLayer.active = !this.noticeLayer.active;
        if (this.noticeLayer.active) {
            dd.mp_manager.playAlert();
            this.lblNoticeCT.string = this._noticeStr;
        }
    }

    /**
     * 点击购买
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

    /**
     * 点击创建房间
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_creatRoom(event, type: string) {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (!this._room_create || !this._room_create.isValid) {
                dd.ui_manager.isShowPopup = false;
                dd.mp_manager.playAlert();
                this._room_create = cc.instantiate(this.room_create_prefab);
                this._room_create.parent = this.node;
                dd.ui_manager.hideLoading();
                let room_create_script = this._room_create.getComponent('Room_Create');
                room_create_script.showCreateMode();
            }
        }
    }

    /**
     * 点击加入房间
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_joinRoom(event, type: string) {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (!this._room_join || !this._room_join.isValid) {
                dd.ui_manager.isShowPopup = false;
                dd.mp_manager.playAlert();
                this._room_join = cc.instantiate(this.room_join_normal_prefab);
                this._room_join.parent = this.node;
            }
        }
    }
    /**
     * 获取俱乐部列表
     * 
     * @memberof Room_Join_Club
     */
    sendGetClubs(cb) {
        dd.ws_manager.sendMsg(dd.protocol.CORPS_GET_CORPS_LIST, '', (flag: number, content?: any) => {
            if (flag === 0) {//成功
                let clubsList = content.items as CorpsVoInner[];
                if (clubsList && clubsList.length > 0) {
                    if (cb) cb(clubsList);
                } else {
                    dd.ui_manager.showAlert('您还没有加入俱乐部,是否前往创建俱乐部？'
                        , '温馨提示',
                        {
                            lbl_name: '确定',
                            callback: () => {
                                if (dd.ui_manager.showLoading()) {
                                    cc.director.loadScene('ClubScene');
                                }
                            }
                        },
                        {
                            lbl_name: '取消',
                            callback: () => {
                            }
                        }
                        , 1);
                    dd.ui_manager.hideLoading();
                }
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                dd.ui_manager.showAlert(content, '温馨提示');
                dd.ui_manager.hideLoading();
            }
        });
    }

    /**
     * 点击俱乐部
     * 
     * @memberof HomeCanvas
     */
    click_btn_club() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('ClubScene');
        }
    }
    /**
     * 点击信息
     * 
     * @memberof HomeCanvas
     */
    click_btn_email() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._email || !this._email.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._email = cc.instantiate(this.email_prefab);
                    this._email.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }
    /**
     * 点击帮助
     * 
     * @memberof HomeCanvas
     */
    click_btn_help() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._help || !this._help.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._help = cc.instantiate(this.help_prefab);
                    this._help.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }

    /**
     * 点击设置
     * 
     * @memberof HomeCanvas
     */
    click_btn_setting() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._setting || !this._setting.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._setting = cc.instantiate(this.setting_prefab);
                    let sets = this._setting.getComponent('Setting');
                    sets.initData(-1);
                    this._setting.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }

    /**
     * 点击实名认证
     * 
     * @memberof HomeCanvas
     */
    click_btn_realAuth() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._auth || !this._auth.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._auth = cc.instantiate(this.auth_prefab);
                    this._auth.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }
    /**
     * 点击手机绑定
     * 
     * @memberof HomeCanvas
     */
    click_btn_phoneBind() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._auth_phone || !this._auth_phone.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._auth_phone = cc.instantiate(this.auth_phone_prefab);
                    this._auth_phone.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }

    /**
     * 点击战绩
     * 
     * @memberof HomeCanvas
     */
    click_btn_record() {
        dd.mp_manager.playButton();
        this.showRecord('0');
    }

    /**
     * 显示战绩界面
     * 
     * @memberof HomeCanvas
     */
    showRecord(recordId?: string) {
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._record || !this._record.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._record = cc.instantiate(this.record_prefab);
                    this._record.parent = this.node;
                    if (recordId !== '0') {
                        dd.ui_manager.hideLoading();
                        let recordScript = this._record.getComponent('Record');
                        recordScript.sendGetRecordDetailed(recordId);
                    }
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }

    /**
     * 赠送房卡
     * 
     * @memberof HomeCanvas
     */
    click_btn_gift_room_card() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._gift || !this._gift.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    dd.mp_manager.playAlert();
                    this._gift = cc.instantiate(this.gift_prefab);
                    this._gift.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }

    /**
     * 点击分享按钮
     * 
     * @memberof HomeCanvas
     */
    click_btn_share() {
        dd.mp_manager.playButton();
        dd.utils.captureScreen(this.node, 'jt.png', (filePath) => {
            if (filePath) {
                dd.ui_manager.showTip('截图成功,开始分享');
                dd.js_call_native.wxShareRecord(filePath);
            } else {
                dd.ui_manager.showTip('截图失败!');
            }
        });
    }
}
