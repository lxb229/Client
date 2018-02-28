const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class NNRoomCanvas extends cc.Component {
    /**
     * 玩家名称
     * 
     * @type {cc.Label}
     * @memberof 
     */
    @property(cc.Label)
    lblName: cc.Label = null;
    /**
     * 玩家金币
     * 
     * @type {cc.Label}
     * @memberof 
     */
    @property(cc.Label)
    lblGold: cc.Label = null;
    /**
     * 玩家ID
     * 
     * @type {cc.Label}
     * @memberof 
     */
    @property(cc.Label)
    lblStarNo: cc.Label = null;
    /**
     * 玩家头像
     * 
     * @type {cc.Sprite}
     * @memberof 
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 房间类型
     * 
     * @type {cc.Node[]}
     * @memberof NNRoomCanvas
     */
    @property([cc.Node])
    typeNodes: cc.Node[] = [];
    /**
     * 房间等级
     * 
     * @type {cc.Sprite}
     * @memberof NNRoomCanvas
     */
    @property(cc.Sprite)
    roomTitle: cc.Sprite = null;
    /**
     * 房间等级图集
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof NNRoomCanvas
     */
    @property([cc.SpriteFrame])
    roomSpriteFrame: cc.SpriteFrame[] = [];
    /**
     * 桌子图片(空,1,2)
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof NNRoomCanvas
     */
    @property([cc.SpriteFrame])
    tableSpriteFrame: cc.SpriteFrame[] = [];
    /**
     * 桌子预制节点
     * 
     * @type {cc.Prefab}
     * @memberof NNRoomCanvas
     */
    @property(cc.Prefab)
    tablePrefab: cc.Prefab = null;
    /**
     * 桌子列表层
     * 
     * @type {cc.Node}
     * @memberof NNRoomCanvas
     */
    @property(cc.Node)
    tableLayer: cc.Node = null;
    /**
     * 是否需要等待,停止响应后续的点击事件
     * 
     * @type {boolean}
     * @memberof NNRoomCanvas
     */
    needWait: boolean = false;
    /**
     * 当前展示的桌子等级
     * 
     * @type {number}
     * @memberof NNRoomCanvas
     */
    showType: number = null;

    onLoad() {
        this.tableLayer.active = false;
        dd.ui_manager.getRootNode().active = true;
    }

    init(items: RoomCfgItem[]) {
        for (let i = 0; i < items.length; i++) {
            if (i > this.typeNodes.length - 1) {
                break;
            }
            let node = this.typeNodes[i];
            let item = items[i];
            node.tag = item.cfgId;
            cc.find('layout/limit', node).getComponent(cc.Label).string = item.joinLimit + '';
        }
        for (let i = items.length; i < this.typeNodes.length; i++) {
            this.typeNodes[i].active = false;
        }
    }

    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof 
     */
    update(dt: number) {
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblGold.string = dd.utils.getShowNumberString(dd.ud_manager.mineData.roomCard);
            this.lblStarNo.string = '  (ID:' + dd.ud_manager.mineData.starNO + ')';
            this.headImg.spriteFrame = dd.img_manager.getHeadById(Number(dd.ud_manager.mineData.headImg));
        }
    }
    /**
     * 点击房间,进入桌子列表
     * 
     * @param {cc.Button} sender 
     * @param {string} type 
     * @returns 
     * @memberof NNRoomCanvas
     */
    clickRoom(event: cc.Event.EventTouch) {
        if (this.needWait) return;
        if (dd.ui_manager.showLoading()) {
            this.needWait = true;
            dd.mp_manager.playButton();
            this.getTables(event.getCurrentTarget().tag);
        }
    }
    /**
     * 根据类型iD获取桌子集合
     * 
     * @param {number} cfgId 
     * @memberof NNRoomCanvas
     */
    getTables(cfgId: number) {
        let obj = { 'cfgId': cfgId };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_GET_TABLE_LIST, msg, (flag: number, content?: any) => {
            this.needWait = false;
            if (flag === 0) {//成功
                this.showType = obj.cfgId;
                this.openTable(content.items as TableCfgItem[]);
            } else if (flag === -1) {//超时
                dd.ui_manager.showTip('获取房间列表失败,请重试!');
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
            }
        });
    }
    /**
     * 展现桌子列表
     * 
     * @param {TableCfgItem[]} tables 
     * @memberof NNRoomCanvas
     */
    openTable(tables: TableCfgItem[]) {
        this.tableLayer.active = true;
        dd.ui_manager.getRootNode().active = false;
        this.roomTitle.spriteFrame = this.roomSpriteFrame[this.showType - 1];
        let content = cc.find('sv', this.tableLayer).getComponent(cc.ScrollView).content;
        content.removeAllChildren();
        tables.sort((a, b) => {
            return a.tableId - b.tableId;
        });
        tables.forEach((table: TableCfgItem, index: number) => {
            let node = cc.instantiate(this.tablePrefab);
            cc.find('img', node).getComponent(cc.Sprite).spriteFrame = this.tableSpriteFrame[table.playerNum];
            cc.find('id', node).getComponent(cc.Label).string = '- ' + (index + 1) + ' -';
            node.tag = table.tableId;
            node.parent = content;
            node.on(cc.Node.EventType.TOUCH_END, this.joinTable, this);
        }, this);
        dd.ui_manager.hideLoading();
    }
    /**
     * 点击桌子响应的方法
     * 
     * @memberof NNRoomCanvas
     */
    joinTable = (event: cc.Event.EventTouch) => {
        if (this.needWait) return;
        if (dd.ui_manager.showLoading()) {
            this.needWait = true;
            dd.mp_manager.playButton();
            let obj = { 'tableId': event.getCurrentTarget().tag, 'type': 0 };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_JION_TABLEID, msg, (flag: number, content?: any) => {
                this.needWait = false;
                if (flag === 0) {//成功
                    dd.gm_manager.nnGameData = content as GameData;
                    cc.director.loadScene('NNScene');
                } else if (flag === -1) {//超时
                    dd.ui_manager.showTip('加入房间消息超时,请重试!');
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                    this.getTables(this.showType);
                }
            });
        }
    };
    /**
     * 关闭桌子列表
     * 
     * @memberof NNRoomCanvas
     */
    closeTable() {
        if (this.needWait) return;
        dd.mp_manager.playButton();
        this.showType = null;
        let sv = cc.find('sv', this.tableLayer).getComponent(cc.ScrollView);
        sv.stopAutoScroll();
        sv.scrollToTop();
        sv.content.removeAllChildren();
        this.tableLayer.active = false;
        dd.ui_manager.getRootNode().active = true;
    }
    /**
     * 点击返回按钮,返回大厅
     * 
     * @returns 
     * @memberof NNRoomCanvas
     */
    clickBack() {
        if (this.needWait) return;
        if (dd.ui_manager.showLoading()) {
            this.needWait = true;
            dd.mp_manager.playButton();
            cc.director.loadScene('HomeScene');
        }
    }
    /**
     * 快速匹配
     * 
     * @returns 
     * @memberof NNRoomCanvas
     */
    quickJoin() {
        if (this.needWait || !this.showType) return;
        if (dd.ui_manager.showLoading()) {
            this.needWait = true;
            dd.mp_manager.playButton();
            let obj = { 'cfgId': this.showType };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_QUICK_JOIN, msg, (flag: number, content?: any) => {
                this.needWait = false;
                if (flag === 0) {//成功
                    dd.gm_manager.nnGameData = content as GameData;
                    cc.director.loadScene('NNScene');
                } else if (flag === -1) {//超时
                    dd.ui_manager.showTip('快速匹配消息超时,请重试!');
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                    this.getTables(this.showType);
                }
            });
        }
    }
}