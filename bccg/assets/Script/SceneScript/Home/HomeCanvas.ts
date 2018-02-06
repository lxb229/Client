const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class HomeCanvas extends cc.Component {
    /**
     * 头像
     * 
     * @type {cc.Sprite}
     * @memberof HomeCanvas
     */
    @property(cc.Sprite)
    spr_head: cc.Sprite = null;
    /**
     * 用户昵称
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lab_name: cc.Label = null;
    /**
     * 玩家id
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lab_id: cc.Label = null;
    /**
     * 设置界面
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    pre_setting: cc.Prefab = null;
    /**
     * 创建界面
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
    */
    @property(cc.Prefab)
    pre_create: cc.Prefab = null;
    /**
     * 加入房间
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    pre_join: cc.Prefab = null;

    /**
     * 公告父节点
     * 
     * @type {cc.Node}
     * @memberof HomeCanvas
     */
    @property(cc.Node)
    noticeNode: cc.Node = null;
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
     * 是否在跑公告
     * 
     * @type {boolean}
     * @memberof HomeCanvas
     */
    _isRunNotice: boolean = false;

    onLoad() {
        cc.log(dd.ud_manager.account_mine);
    }

    async update(dt: number) {
        if (dd.ud_manager && dd.ud_manager.account_mine) {
            if (dd.ud_manager.account_mine.roleAttribVo) {
                this.lab_name.string = dd.utils.getStringBySize(dd.ud_manager.account_mine.roleAttribVo.nick, 12);
                this.lab_id.string = dd.ud_manager.account_mine.roleAttribVo.starNO;
                try {
                    this.spr_head.spriteFrame = await dd.img_manager.loadURLImage(dd.ud_manager.account_mine.roleAttribVo.headImg);
                } catch (error) {
                    cc.log('获取头像错误');
                }
            }
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
                    color: [150, 150, 150]
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
            let tNode = new cc.Node('notice');
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
        }
        this._isRunNotice = true;
        this.noticeNode.active = true;
    }

    /**
     * 点击设置按钮
     * 
     * @memberof HomeCanvas
     */
    click_setting() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        let setNode = cc.instantiate(this.pre_setting);
        setNode.parent = dd.ui_manager.getRootNode();
    }
    /**
     * 点击公告按钮
     * 
     * @memberof HomeCanvas
     */
    click_notice() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();

    }
    /**
     * 点击生涯按钮
     * 
     * @memberof HomeCanvas
     */
    click_career() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
    }
    /**
     * 点击商店按钮
     * 
     * @memberof HomeCanvas
     */
    click_store() {

    }
    /**
     * 点击创建房间
     * 
     * @memberof HomeCanvas
     */
    click_create() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        let createNode = cc.instantiate(this.pre_create);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_GET_CFG, '', (flag: number, content?: any) => {
            if (flag === 0) {
                let cfg: CreateCfg = content as CreateCfg;
                createNode.getComponent('Create').init(cfg);
                createNode.parent = dd.ui_manager.getRootNode();
            } else if (flag === -1) {
                dd.ui_manager.showTip('获取房间配置消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 点击加入房间
     * 
     * @memberof HomeCanvas
     */
    click_join() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        let joinNode = cc.instantiate(this.pre_join);
        joinNode.parent = dd.ui_manager.getRootNode();
    }
    /**
     * 点击我的房间
     * 
     * @memberof HomeCanvas
     */
    click_mine() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();

    }
}
