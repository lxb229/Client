const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class EmailItem extends cc.Component {
    /**
     * 用户头像
     * 
     * @type {cc.Sprite}
     * @memberof EmailItem
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;
    /**
     * 显示申购信息的节点
     * 
     * @type {cc.RichText}
     * @memberof EmailItem
     */
    @property(cc.RichText)
    msg: cc.RichText = null;
    /**
     * 模版数据
     * 
     * @type {string}
     * @memberof EmailItem
     */
    modelMsg: string = '玩家:<color=#D3AE6C><b><玩家昵称></b></c>,<br/>申购:<color=#D3AE6C><b><积分数量></b></c>积分,<br/>您是否同意?';
    /**
     * 申购条目对象
     * 
     * @type {DzpkerOrderItem}
     * @memberof EmailItem
     */
    data: DzpkerOrderItem = null;
    /**
     * 按钮点击的回调
     * 
     * @memberof EmailItem
     */
    callback: (bt: number, id: string, node: cc.Node) => void = null;//1是同意,2是拒绝

    async init(data: DzpkerOrderItem, callback: (bt: number, id: string, node: cc.Node) => void) {
        this.data = data;
        this.callback = callback;
        this.headImg.spriteFrame = await dd.img_manager.loadURLImage(data.headImg);
        this.msg.string = this.modelMsg.replace('<玩家昵称>', dd.utils.getStringBySize(data.nick, 12)).replace('<积分数量>', data.chipNum);
    }
    /**
     * 点击同意
     * 
     * @memberof EmailItem
     */
    click_yes() {
        this.callback(1, this.data.itemId, this.node);
    }
    /**
     * 点击忽略
     * 
     * @memberof EmailItem
     */
    click_no() {
        this.callback(2, this.data.itemId, this.node);
    }

}
