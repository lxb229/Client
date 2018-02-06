
import * as dd from './../../Modules/ModuleManager';
import { productids } from '../../Modules/Config';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Store extends cc.Component {
    /**
     * ios的商城界面
     * @type {cc.Node}
     * @memberof Store
     */
    @property(cc.Node)
    node_ios: cc.Node = null;

    /**
     * android的商城界面
     * @type {cc.Node}
     * @memberof Store
     */
    @property(cc.Node)
    node_android: cc.Node = null;
    /**
     * ios游戏代理咨询名称
     * 
     * @type {cc.Label}
     * @memberof Store
     */
    @property(cc.Label)
    lbl_ios_desName: cc.Label = null;
    /**
     * ios 游戏代理咨询联系
     * @type {cc.Label}
     * @memberof Store
     */
    @property(cc.Label)
    lbl_ios_desContent: cc.Label = null;

    /**
     * android游戏代理咨询名称
     * 
     * @type {[cc.Label]}
     * @memberof Store
     */
    @property([cc.Label])
    lbl_android_desName: cc.Label[] = [];
    /**
     * android游戏代理咨询联系
     * @type {[cc.Label]}
     * @memberof Store
     */
    @property([cc.Label])
    lbl_android_desContent: cc.Label[] = [];
    /**
     * 商品列表
     * 
     * @type {cc.Node}
     * @memberof Store
     */
    @property(cc.Node)
    itemBoard: cc.Node = null;

    items: cc.Node[] = [];

    selectProduct: Product = null;

    /**
     * 商品预设
     * 
     * @type {cc.Prefab}
     * @memberof Store
     */
    @property(cc.Prefab)
    store_item_prefab: cc.Prefab = null;

    @property([cc.SpriteFrame])
    imgGoodsList: cc.SpriteFrame[] = [];

    products: Product[] = null;
    proxys: MallProxyItem[] = null;

    /**
     * 商品节点列表
     * 
     * @type {cc.Node[]}
     * @memberof Store
     */
    _node_goods_list: cc.Node[] = [];

    cb_iapBack: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        let state: number = event.detail;
        switch (state) {
            case 0://支付成功，校验成功
                let obj = { 'num': parseInt(this.selectProduct.title) };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.MALL_ITEM_BUY_OK, msg, (flag: number, content?: any) => {
                    if (flag === 0) {//成功
                        dd.ui_manager.showAlert('购买成功!', '温馨提示', null, null, 1);
                    } else if (flag === -1) {//超时
                    } else {//失败,content是一个字符串
                        dd.ui_manager.showTip(content);
                    }
                });
                break;
            case -2://支付失败
                dd.ui_manager.showAlert('支付失败!', '温馨提示', null, null, 1);
                break;
            case -4://支付成功，校验失败
                dd.ui_manager.showAlert('支付成功，校验失败!', '温馨提示', null, null, 1);
                break;
            default:
                break;
        }
        this.node.removeFromParent(true);
        this.node.destroy();
    };

    onLoad() {
        cc.systemEvent.on('cb_iapBack', this.cb_iapBack, this);
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);

        if (this.products) {//手机IOS端
            this.node_android.active = false;
            this.node_ios.active = true;
            //显示商品列表
            this.showGoodsList();
            //显示ios游戏代理
            this.showIosPoxy();
        } else {
            this.node_android.active = true;
            this.node_ios.active = false;
            //显示android的游戏代理列表
            this.showAndroidPoxy();
        }
    }

    onDestroy() {
        cc.systemEvent.off('cb_iapBack', this.cb_iapBack, this);
    }

    init(proxyItems: MallProxyItem[], products?: Product[]) {
        this.proxys = proxyItems;
        this.products = products;
    }

    /**
     * 显示android的代理商
     * @memberof Store
     */
    showAndroidPoxy() {
        for (var i = 0; i < this.lbl_android_desContent.length; i++) {
            if (this.proxys && this.proxys[i]) {
                this.lbl_android_desContent[i].node.parent.active = true;
                this.lbl_android_desName[i].string = this.proxys[i].proxyType;
                this.lbl_android_desContent[i].string = this.proxys[i].wxNO + '   [' + this.proxys[i].proxyDesc + ']';
            } else {
                this.lbl_android_desContent[i].node.parent.active = false;
            }
        }
    }
    /**
     * 显示ios的代理商 
     * @memberof Store
     */
    showIosPoxy() {
        if (this.proxys && this.proxys[0]) {
            this.lbl_ios_desContent.node.parent.active = true;
            this.lbl_ios_desName.string = this.proxys[0].proxyType;
            this.lbl_ios_desContent.string = this.proxys[0].wxNO + '   [' + this.proxys[0].proxyDesc + ']';
        } else {
            this.lbl_ios_desContent.node.parent.active = false;
        }
    }
    /**
     * 显示商品列表
     * 
     * @memberof Store
     */
    showGoodsList(): void {
        this.itemBoard.removeAllChildren();
        this.items = [];
        if (this.products) {
            for (var i = 0; i < this.products.length; i++) {
                let store_item = cc.instantiate(this.store_item_prefab);
                store_item.tag = i;
                cc.find('img', store_item).getComponent(cc.Sprite).spriteFrame = this.imgGoodsList[i];
                cc.find('priceLayout/price', store_item).getComponent(cc.Label).string = parseInt(this.products[i].price).toString();
                cc.find('countLayout/count', store_item).getComponent(cc.Label).string = this.products[i].title;
                let select = cc.find('select', store_item);
                if (i === 0) {
                    select.active = true;
                    this.selectProduct = this.products[0];
                } else {
                    select.active = false;
                }
                store_item.on("touchend", (event: cc.Event.EventTouch) => {
                    dd.mp_manager.playButton();
                    this.items.forEach((item: cc.Node) => {
                        let select = cc.find('select', item);
                        if (item === event.currentTarget) {
                            select.active = true;
                            this.selectProduct = this.products[item.tag];
                        } else {
                            select.active = false;
                        }
                    }, this);
                }, this);
                store_item.parent = this.itemBoard;
                this.items.push(store_item);
            }
        }
    }
    /**
     * 购买商品
     * 
     * @memberof Store
     */
    click_btn_buy(): void {
        dd.mp_manager.playButton();
        if (this.selectProduct) {
            dd.ui_manager.showLoading('正在向苹果请求交易，请稍后');
            dd.js_call_native.buyProduct(this.selectProduct.productid, dd.ud_manager.mineData.accountId + Date.now);
        }
    }
    /**
     * ios的复制按钮
     * @memberof Store
     */
    click_btn_copy_ios(): void {
        dd.mp_manager.playButton();
        if (this.proxys && this.proxys[0]) {
            let copyStr = this.proxys[0].wxNO;
            dd.js_call_native.copyToClipboard(copyStr);
            dd.ui_manager.showTip('复制成功!');
        }
    }

    /**
     * android的复制按钮
     * @memberof Store
     */
    click_btn_copy_android(event, type: string): void {
        dd.mp_manager.playButton();
        if (this.proxys && this.proxys[Number(type)]) {
            let copyStr = this.proxys[Number(type)].wxNO;
            dd.js_call_native.copyToClipboard(copyStr);
            dd.ui_manager.showTip('复制成功!');
        }
    }

    /**
     * 退出商城
     * 
     * @memberof Store
     */
    click_btn_out(): void {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
