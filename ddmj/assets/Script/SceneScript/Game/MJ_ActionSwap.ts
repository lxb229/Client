import { gm_manager } from "../../Modules/ModuleManager";
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
     * 
     * @memberof MJ_ActionSwap
     */
    showSwapCard(data: MJGameData) {
        let isAct = true;
        for (var i = 0; i < data.seats.length; i++) {
            let seat: SeatVo = data.seats[i];
            if (seat.btState !== MJ_Act_State.ACT_STATE_WAIT) {
                this.node_card_list[i].active = true;
            } else {
                isAct = false;
                this.node_card_list[i].active = false;
            }
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
