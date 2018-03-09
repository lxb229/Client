import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Member_Item extends cc.Component {

    /**
     * 名称
     * 
     * @type {cc.Label}
     * @memberof Club_Member_Item
     */
    @property(cc.Label)
    lblName: cc.Label = null;

    /**
     * 游戏状态
     * 
     * @type {cc.Label}
     * @memberof Club_Member_Item
     */
    @property(cc.Label)
    lblState: cc.Label = null;
    /**
     * 头像
     * @type {cc.Sprite}
     * @memberof Club_Member_Item
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 在线状态
     * @type {cc.Toggle}
     * @memberof Club_Member_Item
     */
    @property(cc.Toggle)
    toggle_online: cc.Toggle = null;

    _itemData: CorpsMemberVoInner = null;                            //俱乐部信息数据
    _cb = null;                             //item点击回调
    _target = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            if (this._cb) {
                this._cb(this._itemData);
            }
            event.stopPropagation();
        }, this);
    }

    updateItem(data: CorpsMemberVoInner, clubInfo: CorpsVoInner, cb, target) {
        this._itemData = data;
        this._cb = cb;
        this._target = target;

        this.lblName.string = dd.utils.getStringBySize(data.nick,8);
        this.showHead();
        switch (data.state) {
            case 0:
                this.lblState.string = '';
                this.lblState.node.color = cc.Color.WHITE;
                this.toggle_online.isChecked = false;
                break;
            case 1:
                this.lblState.string = '[空闲]';
                this.lblState.node.color = cc.Color.GRAY;
                this.toggle_online.isChecked = true;
                break;
            case 2:
                this.lblState.string = '[落座中]';
                this.lblState.node.color = cc.Color.RED;
                this.toggle_online.isChecked = true;
                break;
            default:
                break;
        }
    }

    async showHead() {
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(this._itemData.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.headImg.spriteFrame = headSF;
    }

    /**
     * 删除按钮
     * 
     * @memberof Club_Member_Item
     */
    click_btn_delete() {
        dd.mp_manager.playButton();
        dd.ui_manager.showAlert('确定移除成员 <color=#FFFF00>' + this._itemData.starNO + '</c>？'
            , '移除成员',
            {
                lbl_name: '确定',
                callback: () => {
                    this._target.sendKikMember(this._itemData.starNO);
                }
            },
            {
                lbl_name: '再想想',
                callback: () => {
                }
            }
            , 1);
    }

}
