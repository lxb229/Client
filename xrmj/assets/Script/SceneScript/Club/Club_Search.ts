
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Club_Search extends cc.Component {
    /**
     * 俱乐部id输入框
     * 
     * @type {cc.EditBox}
     * @memberof Club_Search
     */
    @property(cc.EditBox)
    edit_id: cc.EditBox = null;
    /**
     * 俱乐部名称
     * @type {cc.Label}
     * @memberof Club_Search
     */
    @property(cc.Label)
    lblClubName: cc.Label = null;
    /**
     * 俱乐部人数
     * @type {cc.Label}
     * @memberof Club_Search
     */
    @property(cc.Label)
    lblClubNum: cc.Label = null;
    /**
     * 搜索节点
     * @type {cc.Node}
     * @memberof Club_Search
     */
    @property(cc.Node)
    board_search: cc.Node = null;
    /**
     * 搜索结果节点
     * @type {cc.Node}
     * @memberof Club_Search
     */
    @property(cc.Node)
    board_res: cc.Node = null;

    _searchClubData: SearchClubVo = null;
    _parentTarget = null;
    onLoad() {
        this.node.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        this.board_search.active = true;
        this.board_res.active = false;
    }
    initData(target: any) {
        this._parentTarget = target;
    }

    /**
     *  查找俱乐部
     * 
     * @param {string} corpsName 俱乐部名称
     * @memberof Club_Search
     */
    sendSearchClub(idStr: string) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'query': idStr };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_CORPS_SEARCH, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this._searchClubData = content as SearchClubVo;
                    this.board_search.active = false;
                    this.board_res.active = true;
                    this.lblClubName.string = this._searchClubData.corpsName;
                    this.lblClubNum.string = this._searchClubData.memberNum.toString();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                    dd.ui_manager.showTip(content);
                }
            });
            dd.ui_manager.hideLoading();
        }
    }
    /**
     * 搜索按钮点击事件
     * 
     * @memberof Club_Search
     */
    click_btn_search() {
        dd.mp_manager.playButton();
        let idStr = this.edit_id.string.trim();
        if (idStr === '' || idStr.length === 0) {
            dd.ui_manager.showTip('*麻将馆名称不能为空,请重新输入！');
            return;
        }
        this.sendSearchClub(idStr);
    }
    /**
     * 加入俱乐部
     * @returns 
     * @memberof Club_Search
     */
    click_btn_join() {
        dd.mp_manager.playButton();
        if (this._parentTarget) this._parentTarget.showClubJoin(this._searchClubData.corpsId, 1);
        this.node.removeFromParent(true);
        this.node.destroy();
    }
    /**
     * 退出
     * 
     * @memberof Club_Search
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    }

}
