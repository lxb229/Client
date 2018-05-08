import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Club_Rank_Item extends cc.Component {

    @property(cc.Label)
    lblName: cc.Label = null;
    @property(cc.Label)
    lblRank: cc.Label = null;

    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 数据显示节点列表
     * @type {cc.Label[]}
     * @memberof Club_Rank_Item
     */
    @property([cc.Label])
    lblDataList: cc.Label[] = [];

    _itemData: CorpsRankVoItem = null;
    _parentTarget: any = null;
    onLoad() {

    }
    /**
     * 刷新数据
     * @param {number} type 1=名册 2=申请 3=黑名单
     * @param {CorpsMemberVoItem} data 
     * @memberof Club_Rank_Item
     */
    updateItem(type: number, data: CorpsRankVoItem, target: any) {
        this._itemData = data;
        this._parentTarget = target;
        this.lblName.string = data.nick;

        this.lblRank.string = data.rank.toString();
        switch (type) {
            case 1://活跃
                this.lblDataList[0].string = dd.utils.getShowUnitNumber(data.activeValue);
                this.lblDataList[1].string = data.gameRoundNum.toString();
                this.lblDataList[2].node.active = false;
                break;
            case 2://战绩
                this.lblDataList[0].string = data.winMaxRate.toString() + '%';
                this.lblDataList[1].string = data.gameRoundNum.toString();
                this.lblDataList[2].string = data.score > 0 ? '+' + data.score.toString() : data.score.toString();
                break;
            case 3://捐献
                this.lblDataList[0].string = data.giveCardNum.toString();
                this.lblDataList[1].node.active = false;
                this.lblDataList[2].node.active = false;
                break;
            default:
                break;
        }
        this.showHead(this.headImg, data.headImg);
    }

    /**
     * 显示玩家头像
     * @param {cc.Sprite} headImg 
     * @param {string} headURL 
     * @memberof Club_Table
     */
    async showHead(headImg: cc.Sprite, headURL: string) {
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(headURL);
        } catch (error) {
            cc.log('获取头像错误');
        }
        headImg.spriteFrame = headSF;
    }
}
