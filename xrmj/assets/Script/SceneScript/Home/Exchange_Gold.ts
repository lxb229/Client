
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

const eTime: number = 0.1;
@ccclass
export default class Exchange_Gold extends cc.Component {

    /**
     * 商品描述
     * @type {cc.Node}
     * @memberof Exchange_Gold
     */
    @property(cc.Node)
    board_goods_detail: cc.Node = null;
    /**
     * 商品兑换
     * @type {cc.Node}
     * @memberof Exchange_Gold
     */
    @property(cc.Node)
    board_goods_exchange: cc.Node = null;
    /**
     * 列表节点
     * @type {cc.ScrollView}
     * @memberof Exchange_Gold
     */
    @property(cc.ScrollView)
    goods_svNode: cc.ScrollView = null;
    /**
     * 金币界面金币
     * @type {cc.Label}
     * @memberof Exchange_Gold
     */
    @property(cc.Label)
    gold_lblGold: cc.Label = null;
    /**
     * 商品
     * @type {cc.Prefab}
     * @memberof Exchange_Gold
     */
    @property(cc.Prefab)
    goods_prefab: cc.Prefab = null;

    @property(cc.Label)
    lbl_goods_name: cc.Label = null;
    @property(cc.Label)
    lbl_goods_sell: cc.Label = null;
    @property(cc.Label)
    lbl_goods_gold: cc.Label = null;
    @property(cc.Label)
    lbl_goods_num: cc.Label = null;
    @property(cc.Sprite)
    img_goods: cc.Sprite = null;
    @property(cc.ScrollView)
    detail_svNode: cc.ScrollView = null;
    @property(cc.EditBox)
    edit_name: cc.EditBox = null;
    @property(cc.EditBox)
    edit_phone: cc.EditBox = null;
    @property(cc.EditBox)
    edit_addr: cc.EditBox = null;
    /**
     * 商品详情
     * @type {RewareGoldItemInfo}
     * @memberof Exchange_Gold
     */
    _goodsDetail: RewareGoldItemInfo = null;
    onLoad() {
        // if (dd.ui_manager.showLoading()) {
        //     dd.ws_manager.sendMsg(dd.protocol.TASK_GOLD_REWARE_INFO, '', (flag: number, content?: any) => {
        //         if (flag === 0) {//成功
        //             this.initData(content as RewareGoldMoneyItem[]);
        //         } else if (flag === -1) {//超时
        //         } else {//失败,content是一个字符串
        //             dd.ui_manager.showAlert(content, '温馨提示');
        //         }
        //         dd.ui_manager.hideLoading();
        //     });
        // }
    }

    update(dt) {
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.gold_lblGold.string = dd.ud_manager.mineData.wallet.goldMoney.toString();
        }
    }

    initData(data: RewareGoldMoneyItem[]) {
        this.goods_svNode.content.removeAllChildren();
        if (data) {
            data.forEach((rewareGold: RewareGoldMoneyItem) => {
                let goodsNode = cc.instantiate(this.goods_prefab);
                let lblName = goodsNode.getChildByName('lbl_goods_name');
                let img_goods = cc.find('goods/img_goods', goodsNode);
                let lblCost = goodsNode.getChildByName('lblCost');

                if (lblName) lblName.getComponent(cc.Label).string = rewareGold.itemName;
                if (lblCost) lblCost.getComponent(cc.Label).string = rewareGold.goldMoney.toString();
                this.loadGoodsIcon(img_goods, rewareGold.icon);
                goodsNode.parent = this.goods_svNode.content;
                goodsNode.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                    this.getGoodsInfoById(rewareGold.itemId);
                }, this);
            });
        }
    }

    /**
     * 加载商品图片
     * @param {cc.Node} goods 
     * @param {string} url 
     * @memberof Exchange_Gold
     */
    async loadGoodsIcon(goods: cc.Node, url: string) {
        if (goods) {
            let imgSF = null;
            try {
                imgSF = await dd.img_manager.loadURLImage(url);
            } catch (error) {
                cc.log('获取头像错误');
            }
            goods.getComponent(cc.Sprite).spriteFrame = imgSF;
        }
    }
    /**
     * 根据id获取商品详情
     * @param {number} itemId 
     * @memberof Exchange_Gold
     */
    getGoodsInfoById(itemId: number) {
        if (dd.ui_manager.showLoading()) {
            let obj = { 'itemId': itemId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.TASK_GOLD_REWARE_ITEM_INFO, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.showGoodsDetail(content);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 显示商品详情
     * @memberof Exchange_Gold
     */
    showGoodsDetail(data: RewareGoldItemInfo) {
        this._goodsDetail = data;
        if (data) {
            this.lbl_goods_name.string = data.itemName;
            this.lbl_goods_sell.string = data.price.toString();
            this.lbl_goods_gold.string = data.goldMoney.toString();
            this.lbl_goods_num.string = data.num.toString();
            this.loadGoodsIcon(this.img_goods.node, data.icon);
            this.detail_svNode.content.removeAllChildren();
            let node_detail = new cc.Node();
            node_detail.width = this.detail_svNode.content.width;
            node_detail.color = cc.Color.BLACK;
            let lbl_detail = node_detail.addComponent(cc.Label);
            lbl_detail.horizontalAlign = cc.Label.HorizontalAlign.LEFT;
            lbl_detail.fontSize = 30;
            lbl_detail.lineHeight = 40;
            lbl_detail.overflow = cc.Label.Overflow.RESIZE_HEIGHT;
            lbl_detail.string = data.description;
            node_detail.parent = this.detail_svNode.content;
            if (data.descIcon) {
                data.descIcon.forEach(iconUrl => {
                    let node_detail_img = new cc.Node();
                    let img_detail = node_detail_img.addComponent(cc.Sprite);
                    this.loadGoodsIcon(node_detail_img, iconUrl);
                    node_detail_img.parent = this.detail_svNode.content;
                });

            }
        }
        this.board_goods_detail.active = true;
    }

    /**
     * 兑换商品
     * @param {number} itemId 商品id
     * @param {string} [itemName='0'] 商品名称
     * @param {string} [phone='0'] 兑换号码
     * @param {string} [addr='0'] 兑换地址
     * @memberof Exchange_Gold
     */
    exchangeGoods(itemId: number, itemName: string = 'name', phone: string = 'phone', addr: string = 'addr') {
        if (dd.ui_manager.showLoading()) {
            let obj = {
                'itemId': this._goodsDetail.itemId,
                'name': itemName,
                'phone': phone,
                'addr': addr,
            };
            cc.log('----兑换-----');
            cc.log(obj);
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.TASK_GOLD_REWARE_GET, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.ui_manager.showTip('兑换成功，奖品信息已发送邮箱，请注意查收。');
                    this.edit_name.string = '';
                    this.edit_phone.string = '';
                    this.edit_addr.string = '';
                    this.board_goods_exchange.active = false;
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 商品详情的按钮
     * @memberof Exchange_Gold
     */
    click_btn_goods_detail(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://兑换
                if (dd.ud_manager.mineData.wallet.goldMoney >= this._goodsDetail.goldMoney) {
                    if (this._goodsDetail.num > 0) {//库存数量
                        if (this._goodsDetail.type === 'ENTITY') {//如果是实物
                            this.board_goods_exchange.active = true;
                            this.edit_name.string = '';
                            this.edit_phone.string = '';
                            this.edit_addr.string = '';
                            this.board_goods_detail.active = false;
                        } else {
                            dd.ui_manager.showAlert('你确定兑换[<color=#FF0000> ' + this._goodsDetail.itemName + '</c> ]吗？', '温馨提示', {
                                lbl_name: '确定',
                                callback: () => {
                                    this.exchangeGoods(this._goodsDetail.itemId);
                                }
                            }, {
                                    lbl_name: '取消',
                                    callback: () => { }
                                }, 1);
                        }
                    } else {
                        dd.ui_manager.showTip('库存不足');
                    }
                } else {
                    dd.ui_manager.showTip('兑换金币壕礼金币不足');
                }
                break;
            case '1'://去网店
                dd.js_call_native.openBrowser(this._goodsDetail.jumpUrl);
                break;
            default:
                break;
        }
    }
    /**
     * 兑换按钮
     * @memberof Exchange_Gold
     */
    click_btn_exchange() {
        dd.mp_manager.playButton();
        let name = this.edit_name.string.trim();
        if (name.length === 0) return dd.ui_manager.showTip('请输入姓名');
        let phone = this.edit_phone.string.trim();
        if (!dd.utils.checkMobile(phone)) return dd.ui_manager.showTip('请输入有效手机号');
        let addr = this.edit_addr.string.trim();
        if (addr.length === 0) return dd.ui_manager.showTip('请输入详细地址');
        this.exchangeGoods(this._goodsDetail.itemId, name, phone, addr);
    }
    /**
     * 退出按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof Exchange_Gold
     */
    click_btn_out(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0':
                this.node.removeFromParent(true);
                this.node.destroy();
                break;
            case '1':
                this.board_goods_detail.active = false;
                break;
            case '2':
                this.edit_name.string = '';
                this.edit_phone.string = '';
                this.edit_addr.string = '';
                this.board_goods_exchange.active = false;
                break;
            default:
                break;
        }
    }
}
