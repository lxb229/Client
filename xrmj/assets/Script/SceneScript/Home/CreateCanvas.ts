
const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import { MJ_Game_Type } from '../../Modules/Protocol';

export interface RuleToggle {
    toggle: cc.Toggle,
    ruleContentItemAttrib: RuleContentItemAttrib,
}

@ccclass
export default class CreateCanvas extends cc.Component {
    /**
     * 游戏列表
     * @type {cc.ScrollView}
     * @memberof CreateCanvas
     */
    @property(cc.ScrollView)
    svNode_game: cc.ScrollView = null;
    /**
     * 配置列表
     * @type {cc.ScrollView}
     * @memberof CreateCanvas
     */
    @property(cc.ScrollView)
    svNode_rules: cc.ScrollView = null;
    /**
     * 一级游戏类型
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    game_item: cc.Prefab = null;
    /**
     * 二级游戏
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    game_item2: cc.Prefab = null;
    /**
     * 配置
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    rule_item: cc.Prefab = null;
    /**
     * 组
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    toggle_group_item: cc.Prefab = null;
    /**
     * 单选
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    toggle_label1: cc.Prefab = null;
    /**
     * 复选
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    toggle_label2: cc.Prefab = null;
    /**
     * 密码创建按钮节点
     * @type {cc.Node}
     * @memberof CreateCanvas
     */
    @property(cc.Node)
    btn_pwd_create: cc.Node = null;
    /**
     * 创建密码房间的预设
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    create_pwd_prefab: cc.Prefab = null;
    /**
     *  游戏规则配置
     * 
     * @type {SendRuleCfg}
     * @memberof Room_Create
     */
    _sendRuleCfg: SendRuleCfg = {
        corpsId: '0',
        roomItemId: 0,
        password: '0',
        rules: [],
    };
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
    _game_item_list: cc.Node[] = [];
    _game_item2_list: cc.Node[] = [];

    _create_pwd: cc.Node = null;
    onLoad() {
        this.btn_pwd_create.active = dd.ud_manager.openCreateRoom === 1 ? true : false;
        let corpsId = dd.ud_manager.openCreateRoom === 1 ? dd.ud_manager.openClubData.corpsId : '0';
        this.showCreateMode(corpsId);
    }
    /**
     * 显示创建房间的模式 如果corpsId不为0，就是俱乐部房间
     * @param {string} [corpsId='0']  俱乐部id
     * @memberof Room_Create
     */
    showCreateMode(corpsId: string = '0') {
        this._sendRuleCfg.corpsId = corpsId;
        //获取游戏房间的配置
        if (dd.ui_manager.showLoading('正在获取配置，请稍后')) {
            let obj = { corpsId: corpsId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_GET_RULECFG, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.updateLocalRuleCfg(content as RuleCfgVo[]);
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
                    //存储本地
                    this.saveLoacalRuleCfg(obj.roomItemId);
                    dd.gm_manager.turnToGameScene(content as MJGameData, 0);
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.hideLoading();
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                cc.log(content);
            });
        }
    }
    /**
     * 存储本地的游戏配置目录
     * @memberof CreateCanvas
     */
    saveLoacalRuleCfg(roomItemId: number) {
        //存储本地的游戏配置目录
        let ruleCfgVo = [];
        for (let i = 0; i < this._ruleCfgVo.length; i++) {
            if (this._ruleCfgVo[i].itemId === roomItemId) {
                ruleCfgVo.unshift(this._ruleCfgVo[i]);
            } else {
                ruleCfgVo.push(this._ruleCfgVo[i]);
            }
        }
        let db = cc.sys.localStorage;
        db.setItem('ruleCfgVo', JSON.stringify(ruleCfgVo));
    }
    /**
     * 刷新本地数据，(数据的顺序为本地顺序,而不是服务器顺序，记录上一次的游戏)
     * @memberof CreateCanvas
     */
    updateLocalRuleCfg(data: RuleCfgVo[]) {
        this._ruleCfgVo = data;
        if (!data) return;
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
        this.showConfigInfo();
    }
    /**
     * 显示房间配置信息
     *  
     * @memberof Room_Create
     */
    showConfigInfo() {
        this.svNode_game.content.removeAllChildren(true);
        this.svNode_game.node.opacity = 0;
        let xzddNode: cc.Node = null;
        //对游戏进行分类
        this._ruleCfgVo.forEach((ruleCfg: RuleCfgVo) => {
            switch (ruleCfg.itemId) {
                case MJ_Game_Type.GAME_TYPE_XZDD:
                case MJ_Game_Type.GAME_TYPE_SRLF:
                case MJ_Game_Type.GAME_TYPE_SRSF:
                case MJ_Game_Type.GAME_TYPE_LRLF:
                    {
                        if (!xzddNode || !xzddNode.isValid) {
                            xzddNode = this.createMoreGame(ruleCfg, this.svNode_game.content);
                            this._game_item_list.push(xzddNode);
                        }
                        this.createGameItem2(ruleCfg, xzddNode);
                        break;
                    }
                default: {
                    this.createGameItem(ruleCfg, this.svNode_game.content);
                    break;
                }
            }
        });
        //这里做一个延时的刷新，不然会闪一下
        setTimeout(() => {
            this.svNode_game.node.opacity = 255;
        }, 100);
        this.showGameRuleInfo(this._ruleCfgVo[0]);
    }
    /**
     * 显示游戏规则
     * @param {RuleContent[]} rules 
     * @memberof CreateCanvas
     */
    showGameRuleInfo(ruleCfg: RuleCfgVo) {
        this.svNode_rules.content.removeAllChildren(true);
        this._toggleList.length = 0;
        if (!ruleCfg) return;
        this.svNode_rules.node.opacity = 0;
        this._sendRuleCfg.roomItemId = ruleCfg.itemId;
        if (ruleCfg.ruleContents) {
            ruleCfg.ruleContents.forEach((rule: RuleContent) => {
                let ruleNode = cc.instantiate(this.rule_item);
                let lblRuleName = ruleNode.getChildByName('lblRuleName');
                if (lblRuleName) lblRuleName.getComponent(cc.Label).string = rule.ruleName + '：';
                let rule_layout = ruleNode.getChildByName('rule_layout');
                if (rule_layout && rule.ruleContentItems) {
                    rule.ruleContentItems.forEach((data: RuleContentItem) => {
                        this.createRuleGroup(data, rule_layout);
                    });
                }
                ruleNode.parent = this.svNode_rules.content;
            });
        }
        //这里做一个延时的刷新，不然会闪一下
        setTimeout(() => {
            this.svNode_rules.node.opacity = 255;
        }, 100);
    }
    /**
     * 创建规则组
     * @memberof CreateCanvas
     */
    createRuleGroup(data: RuleContentItem, parentNode: cc.Node) {
        let groupNode = cc.instantiate(this.toggle_group_item);
        if (data.ridio === 0) {//如果是单选
            groupNode.addComponent(cc.ToggleContainer);
        }
        if (data.ruleContentItemAttribs) {
            data.ruleContentItemAttribs.forEach((ruleDes: RuleContentItemAttrib) => {
                let toggleNode: cc.Node = null;
                if (data.ridio === 0) {//如果是单选
                    toggleNode = cc.instantiate(this.toggle_label1);
                } else {
                    toggleNode = cc.instantiate(this.toggle_label2);
                }
                let lblDes = toggleNode.getChildByName('lblDes');
                if (lblDes) lblDes.getComponent(cc.Label).string = ruleDes.ruleName;
                let toggleRule = toggleNode.getComponent(cc.Toggle);
                toggleRule.isChecked = ruleDes.state === 0 ? false : true;
                toggleNode.parent = groupNode;
                let ruleToggle: RuleToggle = {
                    toggle: toggleRule,
                    ruleContentItemAttrib: ruleDes
                };
                this._toggleList.push(ruleToggle);
            });
        }
        groupNode.parent = parentNode;
    }
    /**
     * 创建游戏item
     * @param {RuleCfgVo} ruleCfg 
     * @param {cc.Node} parentNode 
     * @memberof CreateCanvas
     */
    createGameItem(ruleCfg: RuleCfgVo, parentNode: cc.Node) {
        let gNode = cc.instantiate(this.game_item);
        let lblGameName = gNode.getChildByName('lblGameName');
        if (lblGameName) lblGameName.getComponent(cc.RichText).string = '<b>' + ruleCfg.itemName + '<b/>'
        if (ruleCfg.itemId === this._ruleCfgVo[0].itemId) {
            gNode.getComponent(cc.Sprite).enabled = true;
        } else {
            gNode.getComponent(cc.Sprite).enabled = false;
        }
        gNode.parent = parentNode;
        this._game_item_list.push(gNode);
        gNode.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            //如果已经是当前的这个游戏
            if (this._sendRuleCfg.roomItemId === ruleCfg.itemId) return;
            cc.log(ruleCfg.itemId);
            dd.mp_manager.playButton();
            this.showGameRuleInfo(ruleCfg);

            this._game_item_list.forEach((gameItem: cc.Node) => {
                if (gameItem !== gNode) {
                    gameItem.getComponent(cc.Sprite).enabled = false;
                }
            });
            this._game_item2_list.forEach((gameItem2: cc.Node) => {
                let img_choose2 = gameItem2.getChildByName('img_choose');
                if (img_choose2) img_choose2.active = false;
                let lblGameName2 = gameItem2.getChildByName('lblGameName');
                if (lblGameName2) lblGameName2.color = new cc.Color(0, 0, 0);
            });
            gNode.getComponent(cc.Sprite).enabled = true;
        }, this);
    }
    /**
     * 创建游戏item2
     * @param {RuleCfgVo} ruleCfg 
     * @param {cc.Node} parentNode 
     * @memberof CreateCanvas
     */
    createGameItem2(ruleCfg: RuleCfgVo, parentNode: cc.Node) {
        let gItem2 = cc.instantiate(this.game_item2);
        let lblGameName = gItem2.getChildByName('lblGameName');
        if (lblGameName) lblGameName.getComponent(cc.RichText).string = ruleCfg.itemName;
        let img_choose = gItem2.getChildByName('img_choose');
        if (ruleCfg.itemId === this._ruleCfgVo[0].itemId) {
            if (img_choose) img_choose.active = true;
            parentNode.getComponent(cc.Sprite).enabled = true;
            if (lblGameName) lblGameName.color = new cc.Color(86, 0, 14);
        } else {
            if (img_choose) img_choose.active = false;
            if (lblGameName) lblGameName.color = new cc.Color(0, 0, 0);
        }
        gItem2.parent = parentNode;
        this._game_item2_list.push(gItem2);

        gItem2.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            //如果已经是当前的这个游戏
            if (this._sendRuleCfg.roomItemId === ruleCfg.itemId) return;
            cc.log(ruleCfg.itemId);
            dd.mp_manager.playButton();
            this.showGameRuleInfo(ruleCfg);

            this._game_item_list.forEach((gameItem: cc.Node) => {
                gameItem.getComponent(cc.Sprite).enabled = false;
            });
            this._game_item2_list.forEach((gameItem2: cc.Node) => {
                if (gameItem2 !== gItem2) {
                    let img_choose2 = gameItem2.getChildByName('img_choose');
                    if (img_choose2) img_choose2.active = false;
                    let lblGameName2 = gameItem2.getChildByName('lblGameName');
                    if (lblGameName2) lblGameName2.color = new cc.Color(0, 0, 0);
                }
            });
            if (lblGameName) lblGameName.color = new cc.Color(86, 0, 14);
            if (img_choose) img_choose.active = true;
            parentNode.getComponent(cc.Sprite).enabled = true;

        }, this);
    }
    /**
     * 创建同一类型多玩法的节点
     * @memberof CreateCanvas
     */
    createMoreGame(ruleCfg: RuleCfgVo, parentNode: cc.Node): cc.Node {
        let gNode = cc.instantiate(this.game_item);
        let layout = gNode.addComponent(cc.Layout);
        layout.type = cc.Layout.Type.VERTICAL;
        layout.resizeMode = cc.Layout.ResizeMode.CONTAINER;
        layout.paddingTop = 20;
        layout.paddingBottom = 12;
        layout.padding = 8;
        let lblGameName = gNode.getChildByName('lblGameName');
        if (lblGameName) {
            if (ruleCfg.itemId === 1)
                lblGameName.getComponent(cc.RichText).string = '<b>血战到底<b/>'
        }
        gNode.parent = parentNode;
        return gNode;
    }
    /**
     * 创建房间
     * @memberof CreateCanvas
     */
    click_btn_create(event, type: string) {
        dd.mp_manager.playButton();
        this._sendRuleCfg.rules.length = 0;
        for (var i = 0; i < this._toggleList.length; i++) {
            if (this._toggleList[i].toggle.isChecked === true) {
                this._sendRuleCfg.rules.push(this._toggleList[i].ruleContentItemAttrib.ruleId);
            }
        }
        cc.log(this._sendRuleCfg);
        switch (type) {
            case '0'://无密码创建
                this.sendCreatRoom(this._sendRuleCfg);
                break;
            case '1'://密码创建
                if (!this._create_pwd || !this._create_pwd.isValid) {
                    this._create_pwd = cc.instantiate(this.create_pwd_prefab);
                    let cPwdScript = this._create_pwd.getComponent('Room_Create_Pwd');
                    cPwdScript.initData(this._sendRuleCfg, this);
                    this._create_pwd.parent = dd.ui_manager.getRootNode();
                }
                break;
            default:
                break;
        }

    }
    /**
     * 退出
     * @memberof TaskCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            if (dd.ud_manager.openCreateRoom === 0) {
                cc.director.loadScene('HomeScene');
            } else {
                cc.director.loadScene('ClubScene');
            }
        }
    }
}
