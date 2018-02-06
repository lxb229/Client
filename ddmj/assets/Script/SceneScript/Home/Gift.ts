import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Gift extends cc.Component {

    @property(cc.Node)
    node_board1: cc.Node = null;

    @property(cc.Node)
    node_board2: cc.Node = null;

    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;

    @property(cc.Prefab)
    gift_item_prefab: cc.Prefab = null;

    /**
     * 输入数量
     * 
     * @type {cc.EditBox}
     * @memberof Gift
     */
    @property(cc.EditBox)
    edit_giftNum: cc.EditBox = null;
    /**
     * 输入id
     * 
     * @type {cc.EditBox}
     * @memberof Gift
     */
    @property(cc.EditBox)
    edit_id: cc.EditBox = null;

    /**
     * 赠送玩家头像
     * 
     * @type {cc.Sprite}
     * @memberof Gift
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 赠送玩家昵称
     * 
     * @type {cc.Label}
     * @memberof Gift
     */
    @property(cc.Label)
    lblName: cc.Label = null;
    /**
     * 赠送玩家id
     * 
     * @type {cc.Label}
     * @memberof Gift
     */
    @property(cc.Label)
    lblID: cc.Label = null;
    /**
     * 赠送玩家数量
     * 
     * @type {cc.Label}
     * @memberof Gift
     */
    @property(cc.Label)
    lblGiftNum: cc.Label = null;
    onLoad() {
        this.node.on('touchend', (event) => {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        this.showGiftLayer(0);
    }

    /**
     * 获取赠送记录
     * 
     * @memberof Gift
     */
    sendGetRecordList() {
        dd.ui_manager.hideLoading();
        if (dd.ui_manager.showLoading()) {
            this.svNode.content.removeAllChildren();
            // let obj = {  };
            // let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.WALLET_ROOMCARD_RECORD, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showRecordList(content.items as WalletGiveInner[]);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }

    /**
     * 赠送房卡
     * 
     * @memberof Gift
     */
    sendGiveGift(givePlayer: string, giveNum: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'givePlayer': givePlayer, 'giveNum': giveNum };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.WALLET_ROOMCARD_GIVE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.showTip('赠送成功');
                    dd.ui_manager.isShowPopup = true;
                    this.node.removeFromParent(true);
                    this.node.destroy();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    this.showGiftLayer(0);
                    dd.ui_manager.showTip(content);
                }
                dd.ui_manager.hideLoading();
                cc.log(content);
            });
        }
    }

    /**
     * 获取玩家信息
     * 
     * @param {string} starNO 
     * @memberof Gift
     */
    sendGetRoleInfo(starNO: string, giftNum: string) {
        if (dd.ui_manager.showLoading()) {
            dd.mp_manager.playAlert();
            let obj = { 'starNO': starNO };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_ROLE_STARNO, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let roleInfo = content as UserData;
                    this.showRoleInfo(roleInfo, giftNum);
                    this.showGiftLayer(1);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content, 0.5, 1, 0.5);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }



    /**
     * 显示玩家信息
     * 
     * @param {UserData} data 
     * @memberof Gift
     */
    async showRoleInfo(data: UserData, giftNum: string) {
        this.lblName.string = data.nick;
        this.lblID.string = data.starNO;
        this.lblGiftNum.string = giftNum;
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(data.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.headImg.spriteFrame = headSF;
    }

    /**
     * 显示赠送界面 0=输入界面 1=确定赠送界面
     * 
     * @param {number} [type=0] 
     * @memberof Gift
     */
    showGiftLayer(type: number = 0) {
        this.node_board1.active = type === 0 ? true : false;
        this.node_board2.active = type === 1 ? true : false;
        if (type === 0) {
            this.sendGetRecordList();
        }
    }

    /**
     * 显示赠送列表
     * 
     * @param {WalletGiveInner[]} data 
     * @memberof Gift
     */
    showRecordList(data: WalletGiveInner[]) {
        if (!data) data = [];
        this.svNode.content.removeAllChildren();
        for (var i = 0; i < data.length; i++) {
            let gift_item = cc.instantiate(this.gift_item_prefab);
            let gift_item_script = gift_item.getComponent('Gift_Item');
            gift_item_script.updateItem(i, data[i]);
            gift_item.parent = this.svNode.content;
        }
    }
    /**
     * 赠送按钮
     * 
     * @memberof Gift
     */
    click_btn_give() {
        let idStr = this.edit_id.string.trim();
        if (idStr === '') {
            dd.ui_manager.showTip('ID不能为空');
            return;
        }
        let numStr = this.edit_giftNum.string.trim();
        if (numStr === '') {
            dd.ui_manager.showTip('请输入赠送数量');
            return;
        }
        if (Number(numStr) === 0) {
            dd.ui_manager.showTip('赠送数量不能为0');
            return;
        }
        this.sendGetRoleInfo(idStr, numStr);
    }

    /**
     * 确定赠送
     * 
     * @memberof Gift
     */
    click_btn_sure() {
        this.sendGiveGift(this.lblID.string, this.lblGiftNum.string);
    }
    /**
     * 取消赠送
     * 
     * @memberof Gift
     */
    click_btn_cancel() {
        this.showGiftLayer(0);
    }

    /**
     * 退出
     * 
     * @memberof Gift
     */
    click_btn_out() {
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
