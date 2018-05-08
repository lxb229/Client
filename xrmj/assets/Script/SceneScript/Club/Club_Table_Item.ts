import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Table extends cc.Component {
    /**
     * 游戏名称 + id
     * @type {cc.Label}
     * @memberof Club_Table
     */
    @property(cc.Label)
    lblGameInfo: cc.Label = null;
    /**
     * 玩家节点列表
     * @type {cc.Node[]}
     * @memberof Club_Table
     */
    @property(cc.Node)
    headNode_list: cc.Node[] = [];

    _itemData: CorpsTableItem = null;
    _parentTarget = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            //加入房间
            if (this._itemData.password === 0) {
                cc.log('进入游戏房间');
                this._parentTarget.sendJoinRoom(this._itemData.tableId);
            } else {
                cc.log('该房间有密码');
                this._parentTarget.showJoinPwd(this._itemData.tableId);
            }
            event.stopPropagation();
        }, this);
    }
    /**
     * 刷新table
     * @param {CorpsTableItem} data 
     * @memberof Club_Table
     */
    updateItem(data: CorpsTableItem, target) {
        this._itemData = data;
        this._parentTarget = target;
        this.lblGameInfo.string = data.gameName + '  ID:' + data.tableId;

        let showList = [];
        if (data.seats) {
            //根据游戏人数分组
            switch (data.seats.length) {
                case 4:
                    showList = this.headNode_list;
                    break;
                case 3:
                    this.headNode_list.forEach((headNode: cc.Node, i: number) => {
                        if (i !== 2) {
                            showList.push(headNode);
                        } else {
                            headNode.active = false;
                        }
                    });
                    break;
                case 2:
                    this.headNode_list.forEach((headNode: cc.Node, i: number) => {
                        if (i === 0 || i === 2) {
                            showList.push(headNode);
                        } else {
                            headNode.active = false;
                        }
                    });
                    break;
                default:
                    break;
            }
        } else {
            this.headNode_list.forEach((headNode: cc.Node, i: number) => {
                headNode.active = false;
            });
        }
        showList.forEach((headNode: cc.Node, i: number) => {
            let headImg = cc.find('mask/headImg', headNode);
            let empty = cc.find('empty', headNode);
            //如果玩家存在，就显示玩家头像
            if (data.seats[i].accountId !== '0') {
                headImg.active = true;
                empty.active = false;
                this.showHead(headImg, data.seats[i].headImg)
            } else {
                headImg.active = false;
                empty.active = true;
            }
        });
    }
    /**
     * 显示玩家头像
     * @param {cc.Sprite} headImg 
     * @param {string} headURL 
     * @memberof Club_Table
     */
    async showHead(headNode: cc.Node, headURL: string) {
        if (!headNode || !headNode.isValid) return;
        let headImg = headNode.getComponent(cc.Sprite);
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(headURL);
        } catch (error) {
            cc.log('获取头像错误');
        }
        headImg.spriteFrame = headSF;
    }

    /**
     * 游戏配置详情
     * @memberof Club_Table
     */
    click_btn_GMInfo() {
        dd.mp_manager.playButton();
        if (this._parentTarget) this._parentTarget.showRoomDetail(this._itemData);
    }
}
