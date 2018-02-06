
const { ccclass, property } = cc._decorator;
import { utils } from '../../Modules/ModuleManager';
@ccclass
export default class NewClass extends cc.Component {

    /**
     * 赠送时间
     * 
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lblTime: cc.Label = null;

    /**
     * 索引
     * 
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lblIndex: cc.Label = null;

    /**
     * 赠送内容
     * 
     * @type {cc.Label}
     * @memberof NewClass
     */
    @property(cc.Label)
    lblContent: cc.Label = null;

    @property(cc.Node)
    node_choose: cc.Node = null;
    onLoad() {

    }

    /**
     * 显示item信息
     * 
     * @param {number} index 
     * @param {WalletGiveInner} data 
     * @memberof NewClass
     */
    updateItem(index: number, data: WalletGiveInner) {
        this.node_choose.active = index % 2 === 0 ? false : true;
        this.lblIndex.string = (index + 1) + '';
        this.lblTime.string = utils.getDateStringByTimestamp(data.giveTime, 3);
        this.lblContent.string = data.content;
    }
}
