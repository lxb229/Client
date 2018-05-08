import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Member_Item extends cc.Component {

    @property(cc.Label)
    lblName: cc.Label = null;

    @property(cc.Label)
    lblReson: cc.Label = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 成员按钮节点
     * @type {cc.Node}
     * @memberof Club_Member_Item
     */
    @property(cc.Node)
    btnNode_mc: cc.Node = null;
    /**
     * 申请按钮节点
     * @type {cc.Node}
     * @memberof Club_Member_Item
     */
    @property(cc.Node)
    btnNode_apply: cc.Node = null;
    /**
     * 解除按钮
     * @type {cc.Node}
     * @memberof Club_Member_Item
     */
    @property(cc.Node)
    btn_relieve: cc.Node = null;

    _itemData: CorpsMemberVoItem = null;
    _parentTarget: any = null;
    onLoad() {

    }
    /**
     * 刷新数据
     * @param {number} type 1=名册 2=申请 3=黑名单
     * @param {CorpsMemberVoItem} data 
     * @memberof Club_Member_Item
     */
    updateItem(type: number, data: CorpsMemberVoItem, target: any) {
        this._itemData = data;
        this._parentTarget = target;
        this.btnNode_mc.active = type === 1 ? true : false;
        this.btnNode_apply.active = type === 2 ? true : false;
        this.btn_relieve.active = type === 3 ? true : false;
        this.lblName.string = data.nick;
        let reson = data.reson === '' ? '空' : data.reson;
        this.lblReson.string = '理由:' + reson;
        if (type === 2 && data.isOwner === 0) {
            this.lblReson.node.active = true;
        } else {
            this.lblReson.node.active = false;
        }
        this.showHead(this.headImg, data.headImg);
    }

    /**
     * 显示玩家头像
     * @param {cc.Sprite} headImg 
     * @param {string} headURL 
     * @memberof Club_Table
     */
    async showHead(headImg: cc.Sprite, headURL: string) {
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(headURL);
        } catch (error) {
            cc.log('获取头像错误');
        }
        headImg.spriteFrame = headSF;
    }

    /**
     * item 按钮点击事件
     * @param {any} event 
     * @param {string} type 
     * @memberof Club_Member_Item
     */
    click_btn_member(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://拉黑
                dd.ui_manager.showAlert('你确定拉黑[ ' + this._itemData.nick + ' ]吗？', '温馨提示', {
                    lbl_name: '确定',
                    callback: () => {
                        this._parentTarget.sendKikiMember(1, this._itemData.accountId);
                    }
                }, {
                        lbl_name: '取消',
                        callback: () => { }
                    }, 1);
                break;
            case '1'://请离
                dd.ui_manager.showAlert('你确定请离[ ' + this._itemData.nick + ' ]吗？', '温馨提示', {
                    lbl_name: '确定',
                    callback: () => {
                        this._parentTarget.sendKikiMember(0, this._itemData.accountId);
                    }
                }, {
                        lbl_name: '取消',
                        callback: () => { }
                    }, 1);
                break;
            case '2'://同意
                this._parentTarget.sendApplyMember(1, this._itemData.accountId);
                break;
            case '3'://拒绝
                this._parentTarget.showRefuseReson(this._itemData.accountId);
                break;
            case '4'://解除
                this._parentTarget.sendRelieveMember(this._itemData.accountId);
                break;
            default:
                break;
        }
    }
}
