
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Club_Item extends cc.Component {

    @property(cc.Label)
    lblClubName: cc.Label = null;

    @property(cc.Label)
    lblMemberNum: cc.Label = null;

    @property(cc.Label)
    lblCreatorWXNo: cc.Label = null;

    @property(cc.Node)
    btn_out: cc.Node = null;

    @property(cc.Node)
    btn_apply_join: cc.Node = null;

    @property(cc.Node)
    btn_admin: cc.Node = null;
    /**
     * 俱乐部信息
     * @type {CorpsVo}
     * @memberof Club_Item
     */
    _clubData: CorpsVo = null;

    _parentTarget: any = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            if (this._clubData.joinFlag === 1) {
                dd.ud_manager.openClubData = this._clubData;
                dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(1, this._clubData.corpsId);
            } else {//申请加入
                this._parentTarget.showClubJoin(this._clubData.corpsId, 0);
            }
            event.stopPropagation();
        }, this);
    }

    updateItem(data: CorpsVo, target?: any) {
        this._clubData = data;
        this._parentTarget = target;
        this.lblClubName.string = data.corpsName;
        this.lblMemberNum.string = data.memberNum.toString();
        this.lblCreatorWXNo.string = data.wxNO;
        if (data.createPlayer === dd.ud_manager.mineData.accountId) {
            this.btn_admin.active = true;
            this.btn_out.active = false;
            this.btn_apply_join.active = false;
        } else {
            this.btn_admin.active = false;
            this.btn_apply_join.active = data.joinFlag === 0 ? true : false;
            this.btn_out.active = data.joinFlag === 1 ? true : false;
        }
    }
    /**
     * item的点击事件
     * @param {any} event 
     * @param {string} type 
     * @memberof Club_Item
     */
    click_item_btn(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://管理
                dd.ud_manager.openClubData = this._clubData;
                dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(4, this._clubData.corpsId);
                break;
            case '1'://申请加入
                this._parentTarget.showClubJoin(this._clubData.corpsId, 0);
                break;
            case '2'://退出
                dd.ui_manager.showAlert('你确定退出[ ' + this._clubData.corpsName + ' ]吗？', '温馨提示', {
                    lbl_name: '确定',
                    callback: () => {
                        if (dd.ui_manager.showLoading()) {
                            let obj = { 'corpsId': this._clubData.corpsId };
                            let msg = JSON.stringify(obj);
                            dd.ws_manager.sendMsg(dd.protocol.CORPS_EXIT, msg, (flag: number, content?: any) => {
                                dd.ui_manager.hideLoading();
                                if (flag === 0) {//成功
                                    //重新获取俱乐部列表
                                    this._parentTarget.sendGetClubs();
                                    dd.ui_manager.showTip('退出成功');
                                } else if (flag === -1) {//超时
                                } else {//失败,content是一个字符串
                                    dd.ui_manager.showAlert(content, '温馨提示');
                                }
                            });
                        }
                    }
                }, {
                        lbl_name: '取消',
                        callback: () => { }
                    }, 1);
                break;
            default:
                break;
        }
    }


}
