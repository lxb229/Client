
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Club_Room_Detail extends cc.Component {

    @property(cc.Node)
    btn_join: cc.Node = null;

    @property(cc.Node)
    btn_ok: cc.Node = null;
    /**
     * 俱乐部列表
     * @type {cc.ScrollView}
     * @memberof Club_Room_Detail
     */
    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;

    _parentTarget = null;
    _roomData: CorpsTableItem = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
    }

    initData(type: number, data: CorpsTableItem, target) {
        this._roomData = data;
        this._parentTarget = target;
        this.btn_join.active = type === 0 ? true : false;
        this.btn_ok.active = type === 1 ? true : false;
        this.svNode.content.removeAllChildren(true);
        let arr = data.ruleShowDesc.split(' ');
        cc.log(arr);
        arr.forEach((rule: string, i: number) => {
            this.createRule(rule, this.svNode.content);
        });
    }
    /**
     * 参加单个规则
     * @param {string} rule 
     * @param {cc.Node} parentNode 
     * @memberof Club_Room_Detail
     */
    createRule(rule: string, parentNode: cc.Node) {
        let node = new cc.Node();
        node.color = cc.Color.BLACK;
        let lable = node.addComponent(cc.Label);
        lable.fontSize = 30;
        lable.lineHeight = 40;
        lable.overflow = cc.Label.Overflow.SHRINK;
        lable.enableWrapText = false;
        lable.horizontalAlign = cc.Label.HorizontalAlign.LEFT;
        lable.string = rule;
        node.width = 240;
        node.height = 40;
        node.parent = parentNode;
    }
    /**
     * 加入房间
     * @memberof Club_Room_Detail
     */
    click_btn_join_room() {
        dd.mp_manager.playButton();
        if (this._roomData.password === 0) {
            cc.log('无密码');
            this._parentTarget.sendJoinRoom(this._roomData.tableId);
        } else {
            cc.log('请输入密码');
            this._parentTarget.showJoinPwd(this._roomData.tableId);
        }
    }

    /**
     * 加入房间
     * @memberof Club_Room_Detail
     */
    click_btn_ok() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
