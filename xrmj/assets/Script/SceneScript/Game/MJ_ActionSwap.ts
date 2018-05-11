import * as dd from "../../Modules/ModuleManager";
import { MJ_Act_State, MJ_Game_Type } from "../../Modules/Protocol";

const { ccclass, property } = cc._decorator;

@ccclass
export default class MJ_ActionSwap extends cc.Component {

    @property([cc.Node])
    node_card_list: cc.Node[] = [];

    _isAct: boolean = false;
    onLoad() {

    }

    /**
     * 显示换三张牌
     * @param {MJGameData} data 
     * @param {number} cfgId 游戏号
     * @memberof MJ_ActionSwap
     */
    showSwapCard(data: MJGameData, cfgId: number) {
        if (this._isAct) return;
        this._isAct = data.tableBaseVo.swapCardType >= 0 ? true : false;
        let sNode = [];
        switch (cfgId) {
            case MJ_Game_Type.GAME_TYPE_SRLF:
            case MJ_Game_Type.GAME_TYPE_SRSF:
                this.node_card_list.forEach((cNode, i) => {
                    if (i !== 2) {//顶部不显示
                        sNode.push(cNode);
                    } else {
                        cNode.active = false;
                    }
                });
                break;
            case MJ_Game_Type.GAME_TYPE_LRLF:
                this.node_card_list.forEach((cNode, i) => {
                    if (i !== 1 && i !== 3) {//左右不显示
                        sNode.push(cNode);
                    } else {
                        cNode.active = false;
                    }
                });
                break;
            default:
                sNode = this.node_card_list;
                break;
        }

        for (var i = 0; i < data.seats.length; i++) {
            let seat: SeatVo = data.seats[i];
            if (seat.btState !== MJ_Act_State.ACT_STATE_WAIT) {
                sNode[i].active = true;
            } else {
                sNode[i].active = false;
            }
        }

        if (this._isAct) {
            let anim = this.node.getComponent(cc.Animation);
            // cc.log('swapCardType:' + data.tableBaseVo.swapCardType);
            switch (cfgId) {
                case MJ_Game_Type.GAME_TYPE_SRLF:
                case MJ_Game_Type.GAME_TYPE_SRSF:
                    if (data.tableBaseVo.swapCardType === 0) {//逆时针
                        anim.play('mj_swap_r3');
                    } else {
                        anim.play('mj_swap_l3');
                    }
                    break;
                case MJ_Game_Type.GAME_TYPE_LRLF:
                    anim.play('mj_swap_d2');
                    break;
                default: {
                    switch (data.tableBaseVo.swapCardType) {
                        case 0://逆时针
                            anim.play('mj_swap_r');
                            break;
                        case 1://顺时针
                            anim.play('mj_swap_l');
                            break;
                        case 2://对面
                            anim.play('mj_swap_d');
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }
        }
    }

    /**
     * 打出牌，显示牌的动作结束
     * 
     * @memberof MJ_Action
     */
    swapActEnd() {
        this._isAct = false;
        if (dd.gm_manager._gmScript) dd.gm_manager._gmScript.showMJInfo();
        this.node.removeFromParent(true);
        this.node.destroy();
    }
    /**
     * 显示动作后，不进行刷新
     * @memberof MJ_ActionSwap
     */
    swapActEnd2() {
        this._isAct = false;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
