import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class ClubRankLayer extends cc.Component {

    /**
    * 俱乐部成员列表
    * @type {cc.ScrollView}
    * @memberof ClubCanvas
    */
    @property(cc.ScrollView)
    svNode_rank: cc.ScrollView = null;
    /**
     * 俱乐部列表的预设
     * @type {cc.Prefab}
     * @memberof ClubCanvas
     */
    @property(cc.Prefab)
    club_rank_prefab: cc.Prefab = null;
    /**
     * 标题节点列表
     * @type {cc.Node[]}
     * @memberof ClubRankLayer
     */
    @property([cc.Node])
    title_node_list: cc.Node[] = [];
    /**
     * 按钮
     * @type {cc.Toggle[]}
     * @memberof ClubRankLayer
     */
    @property([cc.Toggle])
    toggle_list: cc.Toggle[] = [];
    _corpsId: string = '0';
    onLoad() {

    }

    /**
     * 获取俱乐部游戏桌子
     * @memberof ClubTableLayer
     */
    sendGetClubRanking(corpsId: string, type: number = 1) {
        this._corpsId = corpsId;
        this.title_node_list.forEach((titleNode: cc.Node, i: number) => {
            titleNode.active = type === (i + 1) ? true : false;
        });
        this.toggle_list.forEach((toggle_btn: cc.Toggle, i: number) => {
            toggle_btn.isChecked = type === (i + 1) ? true : false;
        });
        if (corpsId === '0') return;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'corpsId': corpsId, 'type': type };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.CORPS_MEMBER_RANKINFO, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showRankingList(type, content.ranks as CorpsRankVoItem[]);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }

    /**
     * 显示排行榜列表信息
     * @param {number} type 
     * @param {CorpsRankVoItem[]} content 
     * @memberof ClubRankLayer
     */
    showRankingList(type: number, clubRankList: CorpsRankVoItem[]) {
        this.svNode_rank.content.removeAllChildren(true);
        if (clubRankList) {
            clubRankList.forEach((rank: CorpsRankVoItem, i: number) => {
                let rankItem = cc.instantiate(this.club_rank_prefab);
                let rankScript = rankItem.getComponent('Club_Rank_Item');
                rankScript.updateItem(type, rank, this);
                rankItem.parent = this.svNode_rank.content;
            }, this);
        }
    }

    /**
     * 切换选项
     * @param {any} event 
     * @param {string} type 
     * @memberof ClubCanvas
     */
    click_btn_chooseRank(event, type: string) {
        dd.mp_manager.playButton();
        this.sendGetClubRanking(this._corpsId, Number(type) + 1);
    }

    /**
     * 退出
     * @memberof ClubTableLayer
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.getCanvasNode().getComponent('ClubCanvas').showClubLayer(1, dd.ud_manager.openClubData.corpsId);
    }
}
