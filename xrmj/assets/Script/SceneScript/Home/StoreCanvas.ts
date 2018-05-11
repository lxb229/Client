
import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class StoreCanvas extends cc.Component {
    /**
     * 房卡
     * @type {cc.Label}
     * @memberof StoreCanvas
     */
    @property(cc.Label)
    lblMoney: cc.Label = null;
    /**
     * ios的商城界面
     * @type {cc.ScrollView}
     * @memberof StoreCanvas
     */
    @property(cc.ScrollView)
    svNode_ios: cc.ScrollView = null;
    /**
     * android商城界面
     * @type {cc.ScrollView}
     * @memberof StoreCanvas
     */
    @property(cc.ScrollView)
    svNode_android: cc.ScrollView = null;
    /**
     * 商品预设
     * 
     * @type {cc.Prefab}
     * @memberof StoreCanvas
     */
    @property(cc.Prefab)
    store_item_prefab: cc.Prefab = null;
    /**
     * 代理预设
     * 
     * @type {cc.Prefab}
     * @memberof StoreCanvas
     */
    @property(cc.Prefab)
    proxy_item_prefab: cc.Prefab = null;
    /**
     * 商品图片
     * @type {cc.SpriteFrame[]}
     * @memberof StoreCanvas
     */
    @property([cc.SpriteFrame])
    imgGoodsList: cc.SpriteFrame[] = [];
    /**
     * 商品数据
     * @type {Product[]}
     * @memberof StoreCanvas
     */
    _products: Product[] = null;
    /**
     *代理数据
     * @type {MallProxyItem[]}
     * @memberof StoreCanvas
     */
    _proxys: MallProxyItem[] = null;
    /**
     * 购买的商品
     * @type {Product}
     * @memberof StoreCanvas
     */
    _selectProduct: Product = null;
    /**
     * 获取ios内购列表的回调
     * 
     * @memberof HomeCanvas
     */
    cb_getProducts: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        if (event.detail) {
            let products: Product[] = JSON.parse(event.detail);
            this.showStore(products);
        } else {
            dd.ui_manager.showAlert('获取商品信息失败', '错误提示', null, null, 1);
            dd.ui_manager.hideLoading();
        }
    };

    cb_iapBack: (event: cc.Event.EventCustom) => void = (event: cc.Event.EventCustom) => {
        let state: number = event.detail;
        switch (state) {
            case 0://支付成功，校验成功
                let time = Date.now().toString();
                let obj = {
                    'num': parseInt(this._selectProduct.title),
                    'pay_price': this._selectProduct.price,
                    'pay_time': time,
                    'order_no': dd.ud_manager.mineData.accountId + '-' + time
                };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.MALL_ITEM_BUY_OK, msg, (flag: number, content?: any) => {
                    dd.ui_manager.hideLoading();
                    if (flag === 0) {//成功
                        dd.ui_manager.showAlert('购买成功!', '温馨提示', null, null, 1);
                    } else if (flag === -1) {//超时
                        dd.ui_manager.showTip('消息超时！');
                    } else {//失败,content是一个字符串
                        dd.ui_manager.showTip(content);
                    }
                });
                break;
            case -1://用户取消购买
                dd.ui_manager.hideLoading();
                dd.ui_manager.showTip('取消购买!');
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
    };

    qyiap() {
        this.showStore();
    }

    onLoad() {
        cc.systemEvent.on('cb_iapBack', this.cb_iapBack, this);
        cc.systemEvent.on('cb_getProducts', this.cb_getProducts, this);
        cc.systemEvent.on('qyiap', this.qyiap, this);

        this.svNode_ios.node.active = false;
        this.svNode_android.node.active = false;
        if (dd.ui_manager.showLoading()) {
            if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {//手机IOS端
                dd.js_call_native.getProducts(dd.config.productids);
            } else {
                this.showStore();
            }
        }
    }

    onDestroy() {
        cc.systemEvent.off('cb_getProducts', this.cb_getProducts, this);
        cc.systemEvent.off('qyiap', this.qyiap, this);
        cc.systemEvent.off('cb_iapBack', this.cb_iapBack, this);
    }
    update(dt: number) {
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblMoney.string = dd.ud_manager.mineData.wallet.roomCard.toString();
        }
    }
    /**
     * 显示商城
     * 
     * @memberof HomeCanvas
     */
    showStore(products?: Product[]) {
        dd.ws_manager.sendMsg(dd.protocol.MALL_ITEMLIST, '', (flag: number, content?: any) => {
            dd.ui_manager.hideLoading();
            if (flag === 0) {//成功
                let data = content as StoreGoods;
                this._proxys = data.proxyItems;
                this._products = products;
                // this._products = data.mallItems;
                //如果商品存在
                if (this._products) {
                    this.showStoreIos();
                } else {
                    this.showStoreAndroid();
                }
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                dd.ui_manager.showTip(content);
            }
        });
    }

    /**
     * 显示android的商城
     * @memberof StoreCanvas
     */
    showStoreAndroid() {
        this.svNode_ios.node.active = false;
        this.svNode_android.node.active = true;
        this.svNode_android.content.removeAllChildren(true);
        if (this._proxys) {
            this._proxys.forEach((proxy: MallProxyItem) => {
                let proxyNode = cc.instantiate(this.proxy_item_prefab);
                let lblDes = proxyNode.getChildByName('lblDes');
                if (lblDes) {
                    let str = '<color=#FFFF00><name>:</c><br/><color=#0000FF>    <desc></c><color=#ff0000>[<wxNO>]</c>';
                    str = str.replace('<name>', proxy.proxyType);
                    str = str.replace('<desc>', proxy.proxyDesc);
                    str = str.replace('<wxNO>', proxy.wxNO);
                    lblDes.getComponent(cc.RichText).string = str;
                }
                let btn_copy = proxyNode.getChildByName('btn_copy');
                if (btn_copy) {
                    btn_copy.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                        dd.js_call_native.copyToClipboard(proxy.wxNO);
                        dd.ui_manager.showTip('复制成功!');
                    });
                }
                proxyNode.parent = this.svNode_android.content;
            });
        }
    }
    /**
     * 显示ios的商城
     * @memberof StoreCanvas
     */
    showStoreIos() {
        this.svNode_ios.node.active = true;
        this.svNode_android.node.active = false;
        this.svNode_ios.content.removeAllChildren(true);
        if (this._products) {
            this._products.forEach((product: Product, i: number) => {
                let productNode = cc.instantiate(this.store_item_prefab);
                let lblCost = productNode.getChildByName('lblCost');
                if (lblCost) lblCost.getComponent(cc.Label).string = parseInt(product.price) + '元';
                let lblNum = cc.find('lblNum', productNode);
                if (lblNum) lblNum.getComponent(cc.Label).string = product.title;
                let goods_icon = productNode.getChildByName('goods_icon');
                if (goods_icon) goods_icon.getComponent(cc.Sprite).spriteFrame = this.imgGoodsList[i];
                let btn_buy = productNode.getChildByName('btn_buy');
                if (btn_buy) {
                    btn_buy.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
                        this._selectProduct = product;
                        cc.log('正在向苹果请求交易');
                        dd.ui_manager.showLoading('正在向苹果请求交易，请稍后');
                        dd.js_call_native.buyProduct(product.productid, dd.ud_manager.mineData.accountId + '-' + Date.now());
                    });
                }
                productNode.parent = this.svNode_ios.content;
            });
        }
    }

    /**
     * 退出商城
     * 
     * @memberof StoreCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
