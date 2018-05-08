const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Game_Over extends cc.Component {

    /**
     * 标题图片
     * 
     * @type {cc.RichText}
     * @memberof Game_Over
     */
    @property(cc.RichText)
    lblTitle: cc.RichText = null;

    /**
     * 标题描述
     * 
     * @type {cc.Label}
     * @memberof Game_Over
     */
    @property(cc.Label)
    lbl_over_des: cc.Label = null;
    @property(cc.Node)
    btn_next: cc.Node = null;
    @property(cc.Node)
    btn_look_result: cc.Node = null;
    /**
     * 单个人结算的预设
     * 
     * @type {cc.Prefab}
     * @memberof Game_Over
     */
    @property(cc.Prefab)
    game_over_prefab: cc.Prefab = null;

    /**
     * 预设容器的父节点
     * 
     * @type {cc.Node}
     * @memberof Game_Over
     */
    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);
    }

    /**
     * 初始化数据
     * 
     * @memberof Game_Over
     */
    initData(sov: SettlementOnceVo[]) {
        if (!sov) return;
        this.svNode.content.removeAllChildren();
        let isLiuJu = true;
        let mySeatInfo: SettlementOnceVo = null;
        for (var i = 0; i < sov.length; i++) {
            let goItem = cc.instantiate(this.game_over_prefab);
            let goItemScript = goItem.getComponent('Game_Over_Item');
            goItemScript.updateItem(i, sov[i]);
            goItem.parent = this.svNode.content;
            //如果有一家胡牌了，就不算流局
            if (sov[i].huPaiIndex !== 0) {
                isLiuJu = false;
            }
            if (sov[i].accountId === dd.ud_manager.mineData.accountId) {
                mySeatInfo = sov[i];
            }
        }

        if (isLiuJu) {
            this.lblTitle.string = '<b>流局</b>';
        } else {
            if (mySeatInfo && mySeatInfo.score >= 0) {
                this.lblTitle.string = '<b>你赢了</b>';
                this.lblTitle.node.color = cc.Color.GREEN;
            } else {
                this.lblTitle.string = '<b>你输了</b>';
                this.lblTitle.node.color = cc.Color.RED;
            }
        }
        this.btn_next.active = dd.gm_manager.mjGameData.tableBaseVo.nextGame === 1 ? true : false;
        this.btn_look_result.active = dd.gm_manager.mjGameData.tableBaseVo.nextGame === 1 ? false : true;

        this.lbl_over_des.string = dd.gm_manager.mjGameData.tableBaseVo.ruleShowDesc;
        /********自动进入下一局 */
        let outTime = dd.gm_manager.getDiffTime(dd.gm_manager.mjGameData.tableBaseVo.svrTime, dd.gm_manager.mjGameData.tableBaseVo.actTime);
        outTime = (outTime - 1) * 1000;
        setTimeout(() => {
            if (this.node && this.node.isValid) {
                dd.js_call_native.phoneVibration();
            }
        }, outTime);
    }

    /**
     * 点击下一局游戏按钮
     * 
     * @memberof Game_Over
     */
    click_btn_next() {
        dd.mp_manager.playButton();
        this.sendNextGame();
    }
    /**
    * 发送下一局
    * 
    * @param {number[]} cardIds 
    * @memberof Game_Over
    */
    sendNextGame() {
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_NEXT_GAME, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.hideLoading();
                    dd.gm_manager.setTableData(content as MJGameData, true, 0);

                    this.node.removeFromParent(true);
                    this.node.destroy();
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    cc.log(content);
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }
}
