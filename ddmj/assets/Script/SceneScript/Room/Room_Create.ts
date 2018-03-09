
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
import Room_Create_Rule from './Room_Create_Rule';

export interface RuleToggle {
    toggle: cc.Toggle,
    ruleContentItemAttrib: RuleContentItemAttrib,
}
export interface SendRuleCfg {
    /**
     * 俱乐部id
     * 
     * @type {string}
     * @memberof Room_Create
     */
    corpsId: string;
    /**
     * 游戏id
     * 
     * @type {number}
     * @memberof Room_Create
     */
    roomItemId: number;
    /**
     * 游戏规则配置列表
     * 
     * @type {number[]}
     * @memberof Room_Create
     */
    rules: number[];
}
@ccclass
export default class Room_Create extends cc.Component {

    /**
    * 创建房间的配置父节点
    * 
    * @type {cc.Node}
    * @memberof Room_Create
    */
    @property(cc.Node)
    node_config: cc.Node = null;

    /**
     * 游戏列表的父节点
     * 
     * @type {cc.Node}
     * @memberof Room_Create
     */
    @property(cc.Node)
    node_game: cc.Node = null;

    /**
     * 创建房间游戏列表的item预设
     * 
     * @type {cc.Prefab}
     * @memberof Room_Create
     */
    @property(cc.Prefab)
    room_item_game_prefab: cc.Prefab = null;
    /**
     * 创建房间配置参数的item预设
     * 
     * @type {cc.Prefab}
     * @memberof Room_Create
     */
    @property(cc.Prefab)
    room_item_cf_prefab: cc.Prefab = null;

    /**
     * 创建房间配置参数的复选框的集合item预设
     * 
     * @type {cc.Prefab}
     * @memberof Room_Create
     */
    @property(cc.Prefab)
    room_item_toggleGroup_prefab: cc.Prefab = null;

    /**
     * 创建房间配置参数的单个复选框的item预设
     * 
     * @type {cc.Prefab}
     * @memberof Room_Create
     */
    @property(cc.Prefab)
    room_item_toggle_prefab: cc.Prefab = null;

    /**
     * 创建房间配置参数的单个复选框的item预设
     * 
     * @type {cc.Prefab}
     * @memberof Room_Create
     */
    @property(cc.Prefab)
    room_item_toggle_prefab2: cc.Prefab = null;
    /**
     * 游戏名称图片列表
     * @type {cc.SpriteFrame[]}
     * @memberof Room_Create
     */
    @property([cc.SpriteFrame])
    game_img_list: cc.SpriteFrame[] = [];
    /**
     * 房间模式 0:俱乐部模式 1:普通模式
     * 
     * @type {string}
     * @memberof Room_Create
     */
    _roomMode: string = '0';

    /**
     * 房间游戏类型 0:血战到底 1:三人血战
     * 
     * @type {string}
     * @memberof Room_Create
     */
    _roomGame: string = '0';

    /**
     * 配置参数的复选框列表
     * 
     * @type {cc.Toggle[]}
     * @memberof Room_Create
     */
    _toggleList: RuleToggle[] = [];
    /**
     * 游戏配置数据列表
     * 
     * @type {RuleCfgVo[]}
     * @memberof Room_Create
     */
    _ruleCfgVo: RuleCfgVo[] = [];

    /**
     *  游戏规则配置
     * 
     * @type {SendRuleCfg}
     * @memberof Room_Create
     */
    _sendRuleCfg: SendRuleCfg = {
        corpsId: '0',
        roomItemId: 0,
        rules: [],
    };

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);

        this.node_config.removeAllChildren();
    }
    /**
     * 显示创建房间的模式 0=普通 1俱乐部
     * 
     * @param {number} type 0=普通 1俱乐部
     * @memberof Room_Create
     */
    /**
     * 显示创建房间的模式 如果corpsId不为0，就是俱乐部房间
     * @param {string} [corpsId='0']  俱乐部id
     * @memberof Room_Create
     */
    showCreateMode(corpsId: string = '0') {
        this._sendRuleCfg.corpsId = corpsId;
        //获取游戏房间的配置
        this.getRoomConfig();
    }

    /**
     * 获取房间配置
     * 
     * @memberof Room_Create
     */
    getRoomConfig() {
        if (dd.ui_manager.showLoading('正在获取配置，请稍后')) {
            let obj = {};
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_GET_RULECFG, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this._ruleCfgVo = content as RuleCfgVo[];
                    this.showConfigInfo();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }

    /**
     * 发送消息创建房间
     * 
     * @param {SendRuleCfg} obj 创建房间对象
     * @memberof Room_Create
     */
    sendCreatRoom(obj: SendRuleCfg) {
        if (dd.ui_manager.showLoading()) {
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_CREATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.gm_manager.mjGameData = content as MJGameData;
                    dd.gm_manager.replayMJ = 0;
                    dd.gm_manager.isReplayPause = false;
                    dd.gm_manager.turnToGameScene();

                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
                cc.log(content);
            });

            //存储本地的游戏配置目录
            let ruleCfgVo = [];
            for (let i = 0; i < this._ruleCfgVo.length; i++) {
                if (this._ruleCfgVo[i].itemId === obj.roomItemId) {
                    ruleCfgVo.unshift(this._ruleCfgVo[i]);
                } else {
                    ruleCfgVo.push(this._ruleCfgVo[i]);
                }
            }
            let db = cc.sys.localStorage;
            db.setItem('ruleCfgVo', JSON.stringify(ruleCfgVo));
        }
    }


    /**
     * 房间模式选择
     * 
     * @param {cc.Toggle} event 
     * @param {string} type 0=普通 1=俱乐部
     * @memberof Room_Create
     */
    click_toggle_mode(event: cc.Toggle, type: string): void {
        dd.mp_manager.playButton();
        this._roomMode = type;
    }

    /**
     * 房间游戏类型选择
     * 
     * @param {cc.Toggle} event 
     * @param {string} type 
     * @memberof Room_Create
     */
    click_toggle_game(event: cc.Toggle, type: string): void {
        dd.mp_manager.playButton();
        this._roomGame = type;
    }

    /**
     * 显示房间配置信息
     * 
     * @memberof Room_Create
     */
    showConfigInfo() {
        this.node_game.removeAllChildren();
        this.node_game.opacity = 0;

        let db = cc.sys.localStorage;
        let ruleCfgVo = db.getItem('ruleCfgVo');
        if (ruleCfgVo) {
            ruleCfgVo = JSON.parse(ruleCfgVo);
            this._ruleCfgVo.forEach((data: RuleCfgVo) => {
                let index = -1;
                for (let i = 0; i < ruleCfgVo.length; i++) {
                    if (data.itemId === ruleCfgVo[i].itemId) {
                        index = i;
                        //更新本地配置
                        ruleCfgVo[i] = data;
                        break;
                    }
                }
                if (index === -1) {
                    ruleCfgVo.push(data);
                }
            });
            ruleCfgVo.forEach((data: RuleCfgVo, d: number) => {
                let index = -1;
                for (let i = 0; i < this._ruleCfgVo.length; i++) {
                    if (data.itemId === this._ruleCfgVo[i].itemId) {
                        index = i;
                        break;
                    }
                }
                //如果不存在就移除
                if (index === -1) {
                    ruleCfgVo.splice(d, 1);
                }
            });
            this._ruleCfgVo = ruleCfgVo;
        }

        for (var i = 0; i < this._ruleCfgVo.length; i++) {
            this.create_game_item(this._ruleCfgVo[i]);
        }
        //创建第一个游戏的配置信息显示
        this._sendRuleCfg.roomItemId = this._ruleCfgVo[0].itemId;
        this.showGameRuleInfo(this._ruleCfgVo[0]);

        //这里做一个延时的刷新，不然会闪一下
        setTimeout(() => {
            this.node_game.opacity = 255;
            this.node_config.opacity = 255;
        }, 100);
    }

    /**
     * 显示游戏的配置信息
     * 
     * @param {RuleCfgVo} data 
     * @memberof Room_Create
     */
    showGameRuleInfo(data: RuleCfgVo) {
        this.node_config.opacity = 0;
        this.node_config.removeAllChildren();
        this._toggleList.length = 0;
        for (var i = 0; i < data.ruleContents.length; i++) {
            this.create_rule_item(data.ruleContents[i]);
        }
        // this.createTipNode(this.node_config);
        //这里做一个延时的刷新，不然会闪一下
        setTimeout(() => {
            this.node_config.opacity = 255;
        }, 100);
    }
    /**
     * 创建提示节点
     * 
     * @memberof Room_Create
     */
    createTipNode(parentNode: cc.Node, tipStr: string = '注：房卡在游戏开始时扣除') {
        let tipNode = new cc.Node('tip');
        tipNode.height = 30;
        tipNode.color = new cc.Color(200, 200, 200);
        tipNode.parent = parentNode;
        let lbl = tipNode.addComponent(cc.Label);
        lbl.fontSize = 30;
        lbl.lineHeight = 30;
        lbl.string = tipStr;
        let widget = tipNode.addComponent(cc.Widget);
        widget.isAlignLeft = true;
        widget.left = 80;
    }

    /**
     * 创建游戏列表
     * 
     * @param {RuleCfgVo} data 
     * @memberof Room_Create
     */
    create_game_item(data: RuleCfgVo): void {
        let room_item_game: cc.Node = cc.instantiate(this.room_item_game_prefab);
        let bg = room_item_game.getChildByName('Background').getComponent(cc.Sprite).spriteFrame = this.game_img_list[data.itemId - 1];
        let toggle = room_item_game.getComponent(cc.Toggle);
        //如果存在多个游戏，需要单选并且要添加点击事件进行切换游戏
        if (this._ruleCfgVo.length > 1) {
            toggle.interactable = true;
            room_item_game.on('touchend', () => {
                //如果已经是当前的这个游戏
                if (this._sendRuleCfg.roomItemId === data.itemId) return;
                cc.log(data.itemId);
                dd.mp_manager.playButton();
                this._sendRuleCfg.roomItemId = data.itemId;
                let rule: RuleCfgVo = this.getGameRule(data.itemId);
                this.showGameRuleInfo(rule);
            }, this);
        } else {
            toggle.interactable = false;
        }
        room_item_game.parent = this.node_game;
    }

    /**
     * 获取游戏配置
     * 
     * @param {number} ruleId 游戏id
     * @returns {RuleCfgVo} 
     * @memberof Room_Create
     */
    getGameRule(ruleId: number): RuleCfgVo {
        let rule: RuleCfgVo = null;
        for (var i = 0; i < this._ruleCfgVo.length; i++) {
            if (this._ruleCfgVo[i].itemId === ruleId) {
                rule = this._ruleCfgVo[i];
                break;
            }
        }
        return rule;
    };
    /**
     * 创建配置参数的item
     * 
     * @param {RuleContent} data 
     * @memberof Room_Create
     */
    create_rule_item(data: RuleContent): void {
        let room_item_cf = cc.instantiate(this.room_item_cf_prefab);
        room_item_cf.parent = this.node_config;
        let lblcfName = cc.find('lblCFName', room_item_cf).getComponent(cc.Label);
        lblcfName.string = data.ruleName;
        let ruleScript: Room_Create_Rule = room_item_cf.getComponent('Room_Create_Rule');
        ruleScript.updateItem(data.ruleName);
        let cf_layout = ruleScript.node_layout;

        for (var i = 0; i < data.ruleContentItems.length; i++) {
            let room_tg = this.create_item_toggleGroup(data.ruleContentItems[i], cf_layout);
            this.create_item_toggle(data.ruleContentItems[i], room_tg);
        }
    }

    /**
     * 创建复选框集合的item
     * 
     * @param {cc.Node} parentNode 
     * @returns {cc.Node} 
     * @memberof Room_Create
     */
    create_item_toggleGroup(data: RuleContentItem, parentNode: cc.Node): cc.Node {
        let room_item_toggleGroup = cc.instantiate(this.room_item_toggleGroup_prefab);
        if (data.ridio === 0) {//如果是单选
            let tg = room_item_toggleGroup.addComponent(cc.ToggleContainer);
        }
        room_item_toggleGroup.parent = parentNode;
        return room_item_toggleGroup;
    }

    /**
     * 创建复选框的item
     * 
     * @param {RuleContentItem} data 选项数据
     * @param {cc.Node} parentNode 父节点
     * @memberof Room_Create
     */
    create_item_toggle(data: RuleContentItem, parentNode: cc.Node): void {
        for (var i = 0; i < data.ruleContentItemAttribs.length; i++) {
            let rci: RuleContentItemAttrib = data.ruleContentItemAttribs[i];
            let room_item_toggle = null;
            if (data.ridio === 1) {//如果是多选
                room_item_toggle = cc.instantiate(this.room_item_toggle_prefab2);
            } else {
                room_item_toggle = cc.instantiate(this.room_item_toggle_prefab);
            }
            let toggle = room_item_toggle.getComponent(cc.Toggle);
            toggle.isChecked = rci.state === 0 ? false : true;
            let ruleToggle: RuleToggle = {
                toggle: toggle,
                ruleContentItemAttrib: data.ruleContentItemAttribs[i]
            };
            this._toggleList.push(ruleToggle);
            room_item_toggle.parent = parentNode;
            let toggle_addLable = room_item_toggle.getComponent('Toggle_AddLabel');
            toggle_addLable.updateItem(data.ruleContentItemAttribs[i].ruleName);
        }
    }


    /**
     * 创建房间的按钮
     * 
     * @memberof Room_Create
     */
    click_btn_create() {
        dd.mp_manager.playButton();
        this._sendRuleCfg.rules.length = 0;
        for (var i = 0; i < this._toggleList.length; i++) {
            if (this._toggleList[i].toggle.isChecked === true) {
                this._sendRuleCfg.rules.push(this._toggleList[i].ruleContentItemAttrib.ruleId);
            }
        }
        cc.log(this._sendRuleCfg);
        this.sendCreatRoom(this._sendRuleCfg);
    }

    /**
     * 创建俱乐部
     * 
     * @memberof Room_Join_Club
     */
    click_btn_create_club() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            // let obj = { 'tableId': 123456 };
            // let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_CREATE, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.director.loadScene('ClubScene');
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }

    /**
     * 加入俱乐部
     * 
     * @memberof Room_Join_Club
     */
    click_btn_create_join() {
        dd.mp_manager.playButton();
        dd.ui_manager.showAlert('请找所在微信群的群主申请加入俱乐部', '加入俱乐部');
    }
    /**
     * 退出
     * 
     * @memberof Room_Create
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
