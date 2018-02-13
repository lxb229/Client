import * as dd from "../../Modules/ModuleManager";
import { MJ_Act_State } from "../../Modules/Protocol";

const { ccclass, property } = cc._decorator;

@ccclass
export default class MJ_ActionSwap extends cc.Component {

    @property([cc.Node])
    node_card_list: cc.Node[] = [];
    onLoad() {

    }

    /**
     * 显示换三张牌
     * @param {MJGameData} data 
     * @param {number} cfgId 游戏号
     * @memberof MJ_ActionSwap
     */
    showSwapCard(data: MJGameData, cfgId: number) {
        let isAct = true;
        let sNode = [];
        switch (cfgId) {
            case 1:
                sNode = this.node_card_list;
                break;
            case 2:
            case 3:
                this.node_card_list.forEach((cNode, i) => {
                    if (i !== 2) {//顶部不显示
                        sNode.push(cNode);
                    } else {
                        cNode.active = false;
                    }
                });
                break;
            case 4:
                this.node_card_list.forEach((cNode, i) => {
                    if (i !== 1 && i !== 3) {//左右不显示
                        sNode.push(cNode);
                    } else {
                        cNode.active = false;
                    }
                });
                break;
            default:
        }

        for (var i = 0; i < data.seats.length; i++) {
            let seat: SeatVo = data.seats[i];
            if (seat.btState !== MJ_Act_State.ACT_STATE_WAIT) {
                sNode[i].active = true;
            } else {
                isAct = false;
                sNode[i].active = false;
            }
        }

        if (cfgId === 2 || cfgId === 3 || cfgId === 4) {
            isAct = false;
        }

        if (isAct) {
            let anim = this.node.getComponent(cc.Animation);
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
        }
    }

    /**
     * 打出牌，显示牌的动作结束
     * 
     * @memberof MJ_Action
     */
    swapActEnd() {
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
