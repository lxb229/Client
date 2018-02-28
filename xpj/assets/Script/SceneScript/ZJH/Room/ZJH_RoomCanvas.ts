const { ccclass, property } = cc._decorator;
import * as dd from './../../../Modules/ModuleManager';
@ccclass
export default class ZJH_RoomCanvas extends cc.Component {

    /**
     * 玩家名称
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblName: cc.Label = null;

    /**
     * 玩家金币
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblGold: cc.Label = null;
    /**
     * 玩家ID
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblStarNo: cc.Label = null;

    /**
     * 玩家头像
     * 
     * @type {cc.Sprite}
     * @memberof HomeCanvas
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * item列表容器
     * 
     * @type {cc.ScrollView}
     * @memberof ZJH_RoomCanvas
     */
    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;
    /**
     * 房间item预设
     * 
     * @type {cc.Prefab}
     * @memberof ZJH_RoomCanvas
     */
    @property(cc.Prefab)
    room_item_prefab: cc.Prefab = null;
    /**
     * 底板图片列表
     * @type {cc.SpriteFrame[]}
     * @memberof ZJH_RoomCanvas
     */
    @property(cc.SpriteFrame)
    board_img_list: cc.SpriteFrame[] = [];

    @property(cc.Sprite)
    imgTitle: cc.Sprite = null;

    @property([cc.SpriteFrame])
    title_img_list: cc.SpriteFrame[] = [];

    @property(cc.Node)
    node_board1: cc.Node = null;

    @property(cc.Node)
    node_board2: cc.Node = null;

    @property(cc.Node)
    layer1: cc.Node = null;

    @property(cc.Node)
    layer2: cc.Node = null;

    @property([cc.Label])
    lblInputList: cc.Label[] = [];
    /**
     * 当前输入的数字索引
     * 
     * @type {number}
     * @memberof Room_Join_Normal
     */
    _curIndex: number = 0;
    /**
     * 等级场数据
     * @type {RoomCfgItem}
     * @memberof ZJH_RoomCanvas
     */
    _cfgData: RoomCfgItem = null;
    onLoad() {

    }
    /**
     * 初始化输入数据
     * 
     * @memberof Room_Join_Normal
     */
    initInput(): void {
        this._curIndex = 0;
        for (var i = 0; i < this.lblInputList.length; i++) {
            this.lblInputList[i].string = '';
        }
    }

    /**
     * 显示房间界面
     * @param {number} type 0=列表界面 1=等级场界面
     * @param {any} itemData 
     * @memberof ZJH_RoomCanvas
     */
    showRoomBoard(type: number, index: number = 0, itemData?: RoomCfgItem) {
        this.node_board1.active = type === 0 ? true : false;
        this.node_board2.active = type === 1 ? true : false;
        if (type === 0) {

        } else {
            this.layer1.active = true;
            this.layer2.active = false;
            this.initInput();
            this._cfgData = itemData;
            this.imgTitle.spriteFrame = this.title_img_list[index];
        }
    }

    /**
     * 发送加入房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendJoinRoom(tableId: string, type: number) {
        let obj = { 'tableId': tableId, 'type': type };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.ZJH_JION_TABLEID, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
                dd.gm_manager.zjhGameData = content as GameData;
                cc.director.loadScene('ZJHScene');
            } else if (flag === -1) {//超时
                dd.ui_manager.hideLoading();
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
                dd.ui_manager.hideLoading();
            }
            cc.log(content);
        });
    }

    /**
     * 发送匹配房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendQuickRoom() {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'cfgId': this._cfgData.cfgId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_QUICK_JOIN, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.gm_manager.zjhGameData = content as GameData;
                    cc.director.loadScene('ZJHScene');
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                    dd.ui_manager.hideLoading();
                }
                cc.log(content);
            });
        }
    }


    /**
     * 发送创建房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendCreateRoom() {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'cfgId': this._cfgData.cfgId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_TABLE_CREATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.gm_manager.zjhGameData = content as GameData;
                    cc.director.loadScene('ZJHScene');
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }
    /**
     * 返回游戏
     * 
     * @memberof ZJH_RoomCanvas
     */
    returnToGame() {
        //拉回桌子
        if (dd.ud_manager && dd.ud_manager.mineData && dd.ud_manager.mineData.tableId !== 0) {
            if (dd.ui_manager.showLoading('正在重新进入未完成的游戏')) {
                this.sendJoinRoom(dd.ud_manager.mineData.tableId + '', 0);
            }
        } else {
            this.showRoomBoard(0);
        }
    }
    /**
        * 界面刷新
        * 
        * @param {number} dt 
        * @memberof HomeCanvas
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
     * 显示房间列表
     * @memberof ZJH_RoomCanvas
     */
    init(data: RoomCfgItem[]) {
        this.svNode.content.removeAllChildren();
        if (!data) data = [];
        for (var i = 0; i < data.length; i++) {
            let index = i;
            let room_item = cc.instantiate(this.room_item_prefab);
            let room_item_script = room_item.getComponent('ZJH_Room_Item');
            room_item_script.updateItem(data[i], i, this, (itemData: RoomCfgItem) => {
                this.showRoomBoard(1, index, itemData);
            });
            room_item.parent = this.svNode.content;
        }
        this.returnToGame();
    }
    /**
     * 房间等级场的按钮点击 
     * @param {any} event 
     * @param {string} type 0=加入房间 1=创建私人房间 2=随机匹配
     * @memberof ZJH_RoomCanvas
     */
    click_btn_grade(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://加入房间
                this.layer1.active = false;
                this.layer2.active = true;
                break;
            case '1'://创建私人房间
                this.sendCreateRoom();
                break;
            case '2'://随机匹配
                this.sendQuickRoom();
                break;
            default:
        }
    }

    /**
     * 输入数字的点击事件
     * 
     * @param {cc.Event.EventTouch} event 
     * @param {string} num 
     * @memberof Room_Join_Normal
     */
    click_btn_input(event: cc.Event.EventTouch, num: string): void {
        dd.mp_manager.playButton();
        switch (num) {
            case '10'://删除
                this.deleteLastInput();
                break;
            case '11'://加入
                this.joinRoom();
                break;
            default:
                this.showInputNum(num);
                break;
        }
    }

    /**
     *删除上一个输入的数字 
     * 
     * @memberof Room_Join_Normal
     */
    deleteLastInput(): void {
        if (this._curIndex <= 0) return;
        this.lblInputList[this._curIndex - 1].string = '';
        if (this._curIndex > 0)
            this._curIndex--;
    }
    /**
     * 加入房间
     * 
     * @memberof ZJH_RoomCanvas
     */
    joinRoom() {
        if (this._curIndex === this.lblInputList.length) {
            cc.log('输入完毕');
            let tableId = '';
            for (var i = 0; i < this.lblInputList.length; i++) {
                tableId += this.lblInputList[i].string;
            }
            if (dd.ui_manager.showLoading()) {
                this.sendJoinRoom(tableId, 1);
            }
        } else {
            dd.ui_manager.showTip('请输入6位房间号');
        }
    }

    /**
     * 显示当前输入的数字
     * 
     * @param {string} num 
     * @memberof Room_Join_Normal
     */
    showInputNum(num: string): void {
        dd.mp_manager.playButton();
        if (this._curIndex < this.lblInputList.length) {
            this.lblInputList[this._curIndex].string = num;
            if (this._curIndex < this.lblInputList.length) {
                this._curIndex++;
            }
        }
    }

    /**
     * 退出
     * 
     * @memberof ZJH_RoomCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (this.node_board1.active) {
            if (dd.ui_manager.showLoading())
                cc.director.loadScene('HomeScene');
        } else {
            if (this.layer1.active) {
                this.showRoomBoard(0);
            } else {
                this.initInput();
                this.layer1.active = true;
                this.layer2.active = false;
            }
        }
    }
}
