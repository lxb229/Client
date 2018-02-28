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
     * 我的牌局
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    pre_mine: cc.Prefab = null;
    /**
     * 公告
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    pre_notice: cc.Prefab = null;
    /**
     * 生涯
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    pre_career: cc.Prefab = null;

    onLoad() {
        dd.ui_manager.fixiPhoneX();
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
        let noticeNode = cc.instantiate(this.pre_notice);
        dd.ws_manager.sendMsg(dd.protocol.ACTIVITY_GET_ACTIVITY_LIST, '', (flag: number, content?: any) => {
            if (flag === 0) {
                let datas: ActivityItemAttrib[] = content.items as ActivityItemAttrib[];
                noticeNode.getComponent('Notice').init(datas);
                noticeNode.parent = dd.ui_manager.getRootNode();
            } else if (flag === -1) {
                dd.ui_manager.showTip('获取房间配置消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 点击生涯按钮
     * 
     * @memberof HomeCanvas
     */
    click_career() {
        dd.ui_manager.showLoading();
        dd.mp_manager.playButton();
        let careerNode = cc.instantiate(this.pre_career);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_GET_CAREE_INFO, '', (flag: number, content?: any) => {
            if (flag === 0) {
                let data: CareerData = content as CareerData;
                careerNode.getComponent('Career').init(data);
                careerNode.parent = dd.ui_manager.getRootNode();
            } else if (flag === -1) {
                dd.ui_manager.showTip('获取房间配置消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
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
        let mineNode = cc.instantiate(this.pre_mine);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_GET_FIGHTED_TABLE_LIST, '', (flag: number, content?: any) => {
            if (flag === 0) {
                let datas: JoinedTableItem[] = content.items as JoinedTableItem[];
                mineNode.getComponent('Mine').init(datas);
                mineNode.parent = dd.ui_manager.getRootNode();
            } else if (flag === -1) {
                dd.ui_manager.showTip('获取我的牌局消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }
}
