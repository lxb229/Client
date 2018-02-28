const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
import * as ZJH_Help from './ZJH_Help';
@ccclass
export default class ZJH_ActionPK extends cc.Component {
    /**
     * 头像列表
     * 
     * @type {cc.Sprite[]}
     * @memberof ZJH_ActionPK
     */
    @property([cc.Sprite])
    headImgList: cc.Sprite[] = [];
    /**
     * 金币列表
     * 
     * @type {cc.Label[]}
     * @memberof ZJH_ActionPK
     */
    @property([cc.Label])
    lblMoneyList: cc.Label[] = [];
    /**
     * 名字列表
     * 
     * @type {cc.Label[]}
     * @memberof ZJH_ActionPK
     */
    @property([cc.Label])
    lblNameList: cc.Label[] = [];
    /**
     * 庄节点表
     * 
     * @type {cc.Node[]}
     * @memberof ZJH_ActionPK
     */
    @property([cc.Node])
    dImgList: cc.Node[] = [];
    /**
     * canvas脚本
     * 
     * @memberof ZJH_ActionPK
     */
    _canvansScript = null;

    onLoad() {
        this.node.on('touchend', (event) => {
            event.stopPropagation();
        }, this);
        this._canvansScript = dd.ui_manager.getCanvasNode().getComponent('ZJHCanvas');
        this.initData();
    }

    /**
     * 初始化数据
     * 
     * @memberof ZJH_ActionPK
     */
    initData() {
        let seatList: SeatVo[] = [];
        //比牌发起座位
        let compareSrcSeat = ZJH_Help.getSeatBySeatId(dd.gm_manager.zjhGameData.compareSrcIndex);
        let compareDstSeat = ZJH_Help.getSeatBySeatId(dd.gm_manager.zjhGameData.compareDstIndex);
        seatList.push(compareSrcSeat);
        seatList.push(compareDstSeat);

        for (var i = 0; i < seatList.length; i++) {
            let seat = seatList[i];
            if (seat) {
                this.headImgList[i].spriteFrame = dd.img_manager.getHeadById(Number(seat.headImg));
                this.dImgList[i].active = seat.accountId === dd.gm_manager.zjhGameData.bankerId ? true : false;
                this.lblMoneyList[i].string = dd.utils.getShowNumberString(seat.money);
                this.lblNameList[i].string = seat.nick;
            }
        }
    }

    /**
     * 比牌结果，显示赢家和比牌失败的特效
     * 
     * @memberof ZJH_ActionPK
     */
    bpResult() {
        let kdNode = null;
        if (dd.gm_manager.zjhGameData.compareResult === 1) {
            kdNode = this.dImgList[1].parent;
        } else {
            kdNode = this.dImgList[0].parent;
        }
        if (kdNode) {
            this._canvansScript.showBPSBAction(cc.p(0, 0), kdNode);
        }
    }
    /**
     * 比牌开始
     * 
     * @memberof ZJH_ActionPK
     */
    showBPStart() {
        this.dImgList[0].parent.runAction(cc.fadeIn(0.5));
        this.dImgList[1].parent.runAction(cc.fadeIn(0.5));
    }

    /**
     * 显示比牌失败，失败的一方渐隐
     * 
     * @memberof ZJH_ActionPK
     */
    showBPSB() {
        let action = cc.fadeOut(0.5);
        if (dd.gm_manager.zjhGameData.compareResult === 1) {
            this.dImgList[1].parent.runAction(action);
        } else {
            this.dImgList[0].parent.runAction(action);
        }
        
        let mySeat = ZJH_Help.getSeatById(dd.ud_manager.mineData.accountId);
        if (mySeat) {
            //如果自己是发起比牌的人
            if (mySeat.seatIndex === dd.gm_manager.zjhGameData.compareSrcIndex) {
                if (dd.gm_manager.zjhGameData.compareResult === 1) {
                    dd.mp_manager.playZJH('bp_win');
                } else {
                    dd.mp_manager.playZJH('bp_lose');
                }
            }
            //如果自己是目标比牌的人
            if (mySeat.seatIndex === dd.gm_manager.zjhGameData.compareSrcIndex) {
                if (dd.gm_manager.zjhGameData.compareResult === 1) {
                    dd.mp_manager.playZJH('bp_lose');
                } else {
                    dd.mp_manager.playZJH('bp_win');
                }
            }
        }
    }
    /**
     * 显示比牌失败的效果
     * 
     * @memberof ZJH_ActionPK
     */
    showBPWin() {
        let action = cc.fadeOut(0.5);
        let kdNode = null;
        if (dd.gm_manager.zjhGameData.compareResult === 1) {
            kdNode = this.dImgList[0].parent;
        } else {
            kdNode = this.dImgList[1].parent;
        }
        kdNode.runAction(action);
    }

    /**
     * 比牌动作结束回调
     * 
     * @memberof ZJH_ActionPK
     */
    actionEnd() {
        let loseSeatId = dd.gm_manager.zjhGameData.compareSrcIndex;
        if (dd.gm_manager.zjhGameData.compareResult === 1) {
            loseSeatId = dd.gm_manager.zjhGameData.compareDstIndex
        }
        this._canvansScript.showQPAction(loseSeatId);
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}

