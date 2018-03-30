const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Game_Result_Item extends cc.Component {
    /**
     * 自己
     * 
     * @type {cc.Node}
     * @memberof Game_Result_Item
     */
    @property(cc.Node)
    node_mine: cc.Node = null;
    /**
     * 头像
     * 
     * @type {cc.Sprite}
     * @memberof Game_Result_Item
     */
    @property(cc.Sprite)
    imgHead: cc.Sprite = null;

    /**
     * 名字
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblName: cc.Label = null;
    /**
     * 玩家id
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblID: cc.Label = null;

    /**
     * 分数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblScore: cc.Label = null;
    /**
     * 自摸次数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblZM: cc.Label = null;
    /**
     * 接炮次数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblJP: cc.Label = null;
    /**
     * 点炮次数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblDP: cc.Label = null;
    /**
     * 暗杠次数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblAG: cc.Label = null;
    /**
     * 明杠次数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblMG: cc.Label = null;
    /**
     * 查叫次数
     * 
     * @type {cc.Label}
     * @memberof Game_Result_Item
     */
    @property(cc.Label)
    lblCJ: cc.Label = null;
    /**
     * 最佳炮手节点
     * 
     * @type {cc.Node}
     * @memberof Game_Result_Item
     */
    @property(cc.Node)
    node_zjps: cc.Node = null;
    /**
     * 大赢家节点
     * 
     * @type {cc.Node}
     * @memberof Game_Result_Item
     */
    @property(cc.Node)
    node_dyj: cc.Node = null;       //大赢家

    @property(cc.Node)
    node_total_lable: cc.Node = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);
    }

    /**
     * 刷新item信息
     * 
     * @memberof Game_Result_Item
     */
    async updateItem(data: SettlementAllVo, maxScore: number, maxDP: number) {
        this.lblName.string = data.nick;
        this.lblID.string = 'ID:' + data.starNO;
        this.lblZM.string = data.zimo + '';
        this.lblAG.string = data.angang + '';
        this.lblCJ.string = data.chajiao + '';
        this.lblDP.string = data.dianPao + '';
        this.lblJP.string = data.otherHuPai + '';
        this.lblMG.string = data.bagang + '';

        // if (data.accountId === dd.ud_manager.mineData.accountId) {
        //     this.node_mine.active = true;
        // } else {
        //     this.node_mine.active = false;
        // }
        if (maxScore > 0) {
            this.node_dyj.active = data.score === maxScore ? true : false;
            this.node_total_lable.active = data.score === maxScore ? false : true;
            this.lblScore.node.color = data.score === maxScore ? cc.Color.GREEN : cc.color(255, 248, 61);
        } else {
            this.node_dyj.active = false;
            this.node_total_lable.active = true;
            this.lblScore.node.color = cc.color(255, 248, 61);
        }

        if (data.score > 0) {
            this.lblScore.string = '+' + data.score;
        } else {
            this.lblScore.string = data.score + '';
        }

        if (maxDP > 0) {
            this.node_zjps.active = data.dianPao === maxDP ? true : false;
        } else {
            this.node_zjps.active = false;
        }

        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(data.headImg);
        } catch (error) {
            cc.log('获取头像错误');
        }
        this.imgHead.spriteFrame = headSF;
    }
}
