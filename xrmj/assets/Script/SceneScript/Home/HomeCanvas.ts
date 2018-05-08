
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import { Reward_Type } from '../../Modules/Protocol';

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
     * 绑定房卡数量
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblBDFK: cc.Label = null;
    /**
     * 房卡数量
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblFK: cc.Label = null;
    /**
     * 玩家头像
     * 
     * @type {cc.Sprite}
     * @memberof HomeCanvas
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;

    /**
     * 猫节点
     * @type {cc.Node}
     * @memberof LoadCanvas
     */
    @property(cc.Node)
    nodeCat: cc.Node = null;
    /**
     * 公告父节点
     * 
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    noticeNode: cc.Node = null;
    /**
     * 跑马灯节点
     * 
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    msgLayout: cc.Node = null;
    /**
     * 更多按钮
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    btn_more: cc.Node = null;
    /**
     * 展开的界面
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    board_more: cc.Node = null;
    /**
     * 茁壮度
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblStrong: cc.Label = null;
    /**
     * 动作图片
     * @type {cc.Sprite}
     * @memberof HomeCanvas
     */
    @property([cc.SpriteFrame])
    icon_act_list: cc.SpriteFrame[] = [];
    /**
     * 福袋列表 
     * @type {cc.Node[]}
     * @memberof HomeCanvas
     */
    @property([cc.Node])
    lucky_bag_list: cc.Node[] = [];
    /**
     * 福袋说明
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    luckyBag_prefab: cc.Prefab = null;
    /**
     * 角色信息预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    role_prefab: cc.Prefab = null;
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
    @property([cc.Node])
    imgAuthList: cc.Node[] = [];
    /**
     * 是否在跑公告
     * 
     * @type {boolean}
     * @memberof HomeCanvas
     */
    _isRunNotice: boolean = false;

    /**
     * 猫路线
     * @type {number}
     * @memberof LoadCanvas
     */
    _catWay: number = 0;
    /**
     * 福袋数据
     * @type {TaskItemInner[]}
     * @memberof HomeCanvas
     */
    _welfareData: TaskItemInner[] = [];
    /**
     * 
     * 弹框节点
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    _role: cc.Node = null;
    _auth: cc.Node = null;
    _auth_phone: cc.Node = null;
    _luckyBag: cc.Node = null;

    onLoad() {
        dd.ui_manager.hideLoading();
        this.showHead();
        dd.mp_manager.playBackGround();
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            if (this.board_more) {
                this.board_more.active = false;
                this.btn_more.active = true;
            }
        });

        this.getWelfareList();
    }

    start() {
        //拉回桌子
        if (dd.ud_manager && dd.ud_manager.mineData && dd.ud_manager.mineData.tableId !== 0) {
            if (dd.ui_manager.showLoading('正在重新进入未完成的游戏')) {
                let obj = { 'tableId': dd.ud_manager.mineData.tableId, 'password': '0' };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                    if (flag === 0) {//成功
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
    }
    /**
     * 获取福利列表
     * @memberof HomeCanvas
     */
    getWelfareList() {
        //获取红包福利列表
        dd.ws_manager.sendMsg(dd.protocol.TASK_GET_WELFARE_LIST, '', (flag: number, content?: any) => {
            if (flag === 0) {//成功
                let data = content as welfareVo;
                this.lblStrong.string = data.dayLuckValue + '%';
                this.showWelfare(data.welfareList);
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
            }
            cc.log(content);
        });
    }

    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof HomeCanvas
     */
    update(dt: number) {
        this.checkPos();
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.utils.getStringBySize(dd.ud_manager.mineData.nick, 10);
            this.lblBDFK.string = dd.ud_manager.mineData.wallet.roomCard.toString();
            this.lblFK.string = dd.ud_manager.mineData.wallet.replaceCard.toString();

            let isShowMoreTip = false;
            //显示手机和实名认证的状态
            if (dd.ud_manager.mineData.authenticationFlag && dd.ud_manager.mineData.authenticationFlag === 1) {
                this.imgAuthList[0].active = true;
                this.hotTipList[2].active = false;
            } else {
                this.imgAuthList[0].active = false;
                this.hotTipList[2].active = true;
                isShowMoreTip = true;
            }
            if (dd.ud_manager.mineData.phone && dd.ud_manager.mineData.phone !== '') {
                this.imgAuthList[1].active = true;
                this.hotTipList[3].active = false;
            } else {
                this.imgAuthList[1].active = false;
                this.hotTipList[3].active = true;
                isShowMoreTip = true;
            }
            this.hotTipList[1].active = isShowMoreTip;

            if (dd.ud_manager.hotTip && dd.ud_manager.hotTip[0] && dd.ud_manager.hotTip[0].hotVal > 0) {
                this.hotTipList[0].active = true;
            } else {
                this.hotTipList[0].active = false;
            }

            if (dd.ud_manager.hotTip && dd.ud_manager.hotTip[2] && dd.ud_manager.hotTip[2].hotVal > 0) {
                this.hotTipList[4].active = true;
            } else {
                this.hotTipList[4].active = false;
            }
        }

        //猫的动作
        if (this._catWay === 0) {
            this.nodeCat.x += 0.3;
            if (this.nodeCat.x >= 300) {
                this._catWay = 1;
                this.nodeCat.scaleX = -1;
            }
        } else {
            this.nodeCat.x -= 0.3;
            if (this.nodeCat.x <= -300) {
                this._catWay = 0;
                this.nodeCat.scaleX = 1;
            }
        }
    }

    async showHead() {
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblBDFK.string = dd.ud_manager.mineData.wallet.roomCard.toString();
            this.lblFK.string = dd.ud_manager.mineData.wallet.replaceCard.toString();

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
                // this.noticeNode.active = false;
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
                    color: [0, 0, 0],
                };
                // let np2: NoticeParamAttrib = {
                //     content: '',
                //     type: 1,//头像
                //     color: [255, 255, 255],
                // };
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
            let tNode = new cc.Node('Notice');
            this.msgLayout.addChild(tNode);
            switch (msgData.type) {
                case 1://玩家对象
                    let img = tNode.addComponent(cc.Sprite);
                    img.sizeMode = cc.Sprite.SizeMode.CUSTOM;
                    let headSF = this.getPicByUrl(msgData.content, img);
                    tNode.width = 38;
                    tNode.height = 38;
                    let tempNode = new cc.Node();
                    tempNode.width = 20;
                    tempNode.height = 38;
                    tempNode.parent = this.msgLayout;
                    break;
                case 0://不变参数
                case 2://字符串
                    tNode.color = new cc.Color(msgData.color[0], msgData.color[1], msgData.color[2]);
                    let lbl = tNode.addComponent(cc.Label);
                    lbl.fontSize = 30;
                    lbl.lineHeight = 30;
                    lbl.overflow = cc.Label.Overflow.NONE;
                    lbl.horizontalAlign = cc.Label.HorizontalAlign.CENTER;
                    lbl.verticalAlign = cc.Label.VerticalAlign.CENTER;
                    lbl.string = msgData.content;
                    // lbl.string = '大王叫我来巡山，我来牌馆转一转。胡着我的牌，数着赢的钱，生活充满节奏感。';
                    break;
                case 3://物品

                    break;
                default:
                    break;
            }
        }
        this._isRunNotice = true;
        this.noticeNode.active = true;
    }
    /**
     * 获取网络图片
     * @param {string} url 
     * @memberof HomeCanvas
     */
    async getPicByUrl(url: string, imgSF: cc.Sprite) {
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(url);
        } catch (error) {
            cc.log('获取头像错误');
        }
        imgSF.spriteFrame = headSF;
        return headSF;
    }
    /**
     * 显示商城
     * 
     * @memberof HomeCanvas
     */
    showStore(products?: Product[]) {
        dd.ws_manager.sendMsg(dd.protocol.MALL_ITEMLIST, '', (flag: number, content?: any) => {
            dd.ui_manager.hideLoading();
            if (flag === 0) {//成功
                let data = content as StoreGoods;
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 显示福袋信息
     * @memberof HomeCanvas
     */
    showWelfare(data: TaskItemInner[]) {
        this._welfareData = data;
        this.lucky_bag_list.forEach((luckyBag: cc.Node, i: number) => {
            if (data && data[i]) {
                luckyBag.active = true;
                let light = luckyBag.getChildByName('light');
                let lblTask = luckyBag.getChildByName('lblTask');
                let count = data[i].total;
                let curComplete = data[i].complete;
                let state = data[i].state;
                luckyBag.tag = data[i].taskId;
                let str = '';
                //完成了未领取
                if (curComplete === count && state === 1) {
                    lblTask.getComponent(cc.LabelOutline).color = cc.Color.YELLOW;
                    light.active = true;
                    luckyBag.getComponent(cc.Button).interactable = true;
                    str = '可领取';

                    // let seq = cc.sequence(cc.scaleTo(0.8, 0.95), cc.scaleTo(0.6, 1).easing(cc.easeBounceOut()),cc.delayTime(0.5));
                    let seq2 = cc.sequence(cc.rotateTo(0.1, 2), cc.rotateTo(0.1, 0), cc.rotateTo(0.1, -2), cc.rotateTo(0.5, 0).easing(cc.easeBounceOut()), cc.delayTime(1));
                    let action = cc.repeatForever(seq2);
                    luckyBag.runAction(action);
                } else {
                    luckyBag.stopAllActions();
                    lblTask.getComponent(cc.LabelOutline).color = cc.Color.BLACK;
                    light.active = false;
                    luckyBag.getComponent(cc.Button).interactable = state === 3 ? false : true;
                    str = state === 3 ? '已领取' : '等待中';
                }
                lblTask.getComponent(cc.Label).string = str + curComplete + '/' + count;
            } else {
                luckyBag.active = false;
            }
        });
    }
    /**
     * 点击头像，获取玩家信息
     * 
     * @memberof HomeCanvas
     */
    click_btn_head() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            dd.mp_manager.playAlert();
            let obj = { 'accountId': dd.ud_manager.mineData.accountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_ACCOUNTID, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let roleInfo = content as UserData;
                    if (!this._role || !this._role.isValid) {
                        this._role = cc.instantiate(this.role_prefab);
                        let roleScript = this._role.getComponent('Role');
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

    /**
     * 点击购买
     * @memberof HomeCanvas
     */
    click_btn_buy() {
        dd.mp_manager.playButton();
        cc.log('进入商城');
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('StoreScene');
        }
    }

    /**
     * 大厅游戏按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_game(event, type: string) {
        dd.mp_manager.playButton();
        cc.log('game' + type);
        switch (type) {
            case '1'://贤人壕礼
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('ExchangeScene');
                }
                break;
            case '2'://麻将馆
                if (dd.ui_manager.showLoading()) {
                    dd.ud_manager.openClubData = null;
                    cc.director.loadScene('ClubScene');
                }
                break;
            case '3'://创建游戏
                if (dd.ui_manager.showLoading()) {
                    dd.ud_manager.openCreateRoom = 0;
                    cc.director.loadScene('CreateScene');
                }
                break;
            case '4'://加入游戏
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('JoinScene');
                }
                break;
            default:
                break;
        }
    }
    /**
     * 大厅的按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_hall(event, type: string) {
        dd.mp_manager.playButton();
        cc.log('hall' + type);
        switch (type) {
            case '1'://消息
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('MailScene');
                }
                break;
            case '2'://战绩
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('RecordScene');
                }
                break;
            case '3'://设置
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('SettingScene');
                }
                break;
            case '4'://更多
                this.board_more.active = true;
                this.btn_more.active = false;
                break;
            default:
                break;
        }
        if (type !== '4') {
            this.board_more.active = false;
            this.btn_more.active = true;
        }
    }
    /**
     * 更多按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_more(event, type: string) {
        dd.mp_manager.playButton();
        cc.log('more' + type);
        switch (type) {
            case '1'://帮助
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('HelpScene');
                }
                break;
            case '2'://绑定
                //如果已经绑定手机号
                if (dd.ud_manager.mineData.phone && dd.ud_manager.mineData.phone !== '') {
                    let str = '您已完成手机绑定<br/>绑定手机:<color=#FF0000>' + dd.ud_manager.mineData.phone + '</c><br/>如需更改，请联系客服！';
                    dd.ui_manager.showAlert(str, '温馨提示');
                } else {
                    if (!this._auth_phone || !this._auth_phone.isValid) {
                        this._auth_phone = cc.instantiate(this.auth_phone_prefab);
                        this._auth_phone.parent = this.node;
                    }
                }
                break;
            case '3'://认证
                if (dd.ud_manager.mineData.authenticationFlag && dd.ud_manager.mineData.authenticationFlag === 1) {
                    dd.ui_manager.showAlert('您已经实名认证过了哦！', '温馨提示');
                } else {
                    if (!this._auth || !this._auth.isValid) {
                        this._auth = cc.instantiate(this.auth_prefab);
                        this._auth.parent = this.node;
                    }
                }
                break;
            case '4'://收起

                break;
            default:
                break;
        }
        this.board_more.active = false;
        this.btn_more.active = true;
    }
    /**
     * 左侧按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_left(event, type: string) {
        dd.mp_manager.playButton();
        cc.log('left' + type);
        switch (type) {
            case '1'://每日任务
                if (dd.ui_manager.showLoading()) {
                    cc.director.loadScene('TaskScene');
                }
                break;
            case '2'://福袋说明
                if (!this._luckyBag || !this._luckyBag.isValid) {
                    this._luckyBag = cc.instantiate(this.luckyBag_prefab);
                    let btn_out = cc.find('board/btn_out', this._luckyBag);
                    if (btn_out) {
                        btn_out.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                            this._luckyBag.removeFromParent(true);
                            this._luckyBag.destroy();
                            this._luckyBag = null;
                        });
                    }
                    this._luckyBag.parent = this.node;
                    // let board = this._luckyBag.getChildByName('board');
                    // if (board) {
                    //     board.scale = 0;
                    //     let action = cc.scaleTo(0.5, 1).easing(cc.easeElasticOut(0.8));
                    //     board.runAction(action);
                    // }
                }
                break;
            default:
                break;
        }
        this.board_more.active = false;
        this.btn_more.active = true;
    }
    /**
     * 领取福袋
     * @memberof HomeCanvas
     */
    click_btn_reward(event, type: string) {
        dd.mp_manager.playButton();
        dd.ui_manager.showLoading();
        cc.log('领取福袋' + type);
        let luckyBag = this.lucky_bag_list[Number(type)];
        let obj = { 'taskId': luckyBag.tag };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.TASK_GET_WELFARE_REWARE, msg, (flag: number, content?: any) => {
            dd.ui_manager.hideLoading();
            if (flag === 0) {//成功
                cc.log(content);
                let data = content.rewardItems as RewardItems[];
                let str = '';
                data.forEach((reward: RewardItems, i: number) => {
                    let pos = this.getWorldPos(luckyBag.parent, luckyBag.getPosition());
                    switch (reward.code) {
                        case Reward_Type.GOLD://金币
                            str = reward.amount + '个金币';
                            this.showMoreGoldAction(0, pos, reward.amount);
                            break;
                        case Reward_Type.SILVERCOIN://银币
                            str = reward.amount + '个银币';
                            this.showMoreGoldAction(1, pos, reward.amount);
                            break;
                        default:
                            break;
                    }
                });
                str = '恭喜你，获得' + str;
                dd.ui_manager.showTip(str);
                this.getWelfareList();
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                cc.log(content);
                let welfare = this._welfareData[Number(type)];
                dd.ui_manager.showAlert('任务描述：' + welfare.finshDesc + '\n' + '任务奖励：' + welfare.rewardDesc, '福袋描述', null, null, 0);
            }
        });

        this.board_more.active = false;
        this.btn_more.active = true;
    }

    /**
     *获取世界坐标
     * 获取世界坐标都是以屏幕左下角为原点(0，0) ，AR是节点描点为原点转换 不带AR的是以节点左下角为原点
     * @param {cc.Node} node  父节点
     * @param {cc.Vec2} pos 坐标
     * @returns 
     * @memberof HomeCanvas
     */
    getWorldPos(node: cc.Node, pos: cc.Vec2) {
        let targetP = node.convertToWorldSpaceAR(pos);
        //依然是以屏幕左下角为起点,所以要减去一半
        targetP.x = targetP.x - this.node.width / 2;
        targetP.y = targetP.y - this.node.height / 2;
        return targetP;
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

    /**
     * 金币动作
     * @memberof HomeCanvas
     */
    showMoreGoldAction(type: number, sPos: cc.Vec2, num: number = 5) {
        if (num > 5) num = 5;
        for (let i = 0; i < num; i++) {
            let radom = Math.floor(Math.random() * 10000) % 200;
            let p1 = cc.p(sPos.x + radom, -300);
            this.showGoldAction(type, sPos, p1);
        }
    }
    /**
     * 金币动作
     * @param {cc.Vec2} sPos 
     * @memberof HomeCanvas
     */
    showGoldAction(type: number, sPos: cc.Vec2, p1: cc.Vec2) {
        let goldNode = new cc.Node('gold');
        let icon = goldNode.addComponent(cc.Sprite);
        icon.spriteFrame = this.icon_act_list[type];
        goldNode.rotation = -90;
        goldNode.scale = Math.floor(Math.random() * 1000) % 50 / 100 + 0.4;
        goldNode.setPosition(cc.p(sPos.x, sPos.y + 50));
        goldNode.parent = dd.ui_manager.getRootNode();

        let speed = 300;
        let p2 = cc.p(-250, -300);
        let p3 = cc.p(50, 10);
        //掉落的点 到 楼梯的点 的距离
        let dx = cc.pDistance(p1, p2);
        if (p1.x > p2.x) dx = 0;
        //从上往下掉的时间
        let dt1 = cc.pDistance(cc.p(sPos.x, sPos.y + 50), p1) / (speed + 200);
        let action = cc.spawn(cc.scaleTo(dt1, 1), cc.rotateTo(dt1, 0), cc.jumpTo(dt1, p1, 100, 1));

        let seq = null;
        if (dx < 50) {
            //直接跳到终点
            let jump2 = cc.jumpTo(cc.pDistance(p1, p3) / speed, p3, 50, 7);
            seq = cc.sequence(action, jump2, cc.fadeOut(0.5), cc.callFunc(() => {
                goldNode.removeFromParent(true);
                goldNode.destroy();
            }, this));
        } else {
            //掉落的点 跳到 楼梯的点 需要跳多少次
            let dj = Math.floor(dx / 50);
            if (dj < 1) dj = 1;
            let jump1 = cc.jumpTo(dx / speed, p2, 30, dj);
            let jump2 = cc.jumpTo(cc.pDistance(p2, p3) / speed, p3, 50, 7);
            seq = cc.sequence(action, jump1, jump2, cc.fadeOut(0.3), cc.callFunc(() => {
                goldNode.removeFromParent(true);
                goldNode.destroy();
            }, this));
        }
        goldNode.runAction(seq);
    }
}
