const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class HomeCanvas extends cc.Component {

    /**
     * 玩家名称
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblName: cc.Label = null;

    /**
     * 玩家金币
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblGold: cc.Label = null;
    /**
     * 玩家ID
     * 
     * @type {cc.Label}
     * @memberof HomeCanvas
     */
    @property(cc.Label)
    lblStarNo: cc.Label = null;

    /**
     * 玩家头像
     * 
     * @type {cc.Sprite}
     * @memberof HomeCanvas
     */
    @property(cc.Sprite)
    headImg: cc.Sprite = null;


    /**
     * 设置界面预设
     * 
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    setting_prefab: cc.Prefab = null;
    /**
     * 客服预设
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    service_prefab: cc.Prefab = null;

    /**
     * 充值预设
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    recharge_prefab: cc.Prefab = null;

    /**
     * 兑换预设
     * @type {cc.Prefab}
     * @memberof HomeCanvas
     */
    @property(cc.Prefab)
    exchange_prefab: cc.Prefab = null;

    _setting: cc.Node = null;
    _service: cc.Node = null;
    _recharge: cc.Node = null;
    _exchange: cc.Node = null;

    needWait: boolean = false;

    start() {
        //拉回桌子
        if (dd.ud_manager && dd.ud_manager.mineData && dd.ud_manager.mineData.tableId !== 0) {
            if (dd.ui_manager.showLoading('正在重新进入未完成的游戏')) {
                let obj = { 'tableId': dd.ud_manager.mineData.tableId, 'type': 0 };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.ZJH_JION_TABLEID, msg, (flag: number, content?: any) => {
                    if (flag === 0) {//成功
                        switch (content.gameType) {
                            case 1:
                                dd.gm_manager.nnGameData = content as GameData;
                                cc.director.loadScene('NNScene');
                                break;
                            case 2:
                                dd.gm_manager.zjhGameData = content as GameData;
                                cc.director.loadScene('ZJHScene');
                                break;
                            default: break;
                        }
                    } else if (flag === -1) {//超时
                        dd.ui_manager.showTip('获取桌子信息超时!');
                    } else {//失败,content是一个字符串
                        dd.ui_manager.showTip(content);
                    }
                });
            }
        }
    }

    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof HomeCanvas
     */
    update(dt: number) {
        //刷新玩家信息
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.lblName.string = dd.ud_manager.mineData.nick;
            this.lblGold.string = dd.utils.getShowNumberString(dd.ud_manager.mineData.roomCard);
            this.lblStarNo.string = '  (ID:' + dd.ud_manager.mineData.starNO + ')';
            this.headImg.spriteFrame = dd.img_manager.getHeadById(Number(dd.ud_manager.mineData.headImg));
        }
    }

    /**
     * 进入游戏
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof HomeCanvas
     */
    click_btn_game(event, type: string) {
        if (this.needWait) return;
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading('正在获取房间列表,请稍后')) {
            this.needWait = true;
            let obj = { 'gameType': type === '0' ? 1 : 2 };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ZJH_GET_ROOM_LIST, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    let items = content.items as RoomCfgItem[];
                    if (type === '0') {
                        cc.director.loadScene('NNRoomScene', () => {
                            dd.ui_manager.getCanvasNode().getComponent('NNRoomCanvas').init(items);
                        });
                    } else {
                        cc.director.loadScene('ZJHRoomScene', () => {
                            dd.ui_manager.getCanvasNode().getComponent('ZJH_RoomCanvas').init(items);
                        });
                    }
                } else if (flag === -1) {//超时
                    dd.ui_manager.showTip('获取房间列表失败,请重试!');
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }

    /**
     * 点击设置
     * 
     * @memberof HomeCanvas
     */
    click_btn_setting() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.isShowPopup) {
            if (dd.ui_manager.showLoading()) {
                if (!this._setting || !this._setting.isValid) {
                    dd.ui_manager.isShowPopup = false;
                    this._setting = cc.instantiate(this.setting_prefab);
                    this._setting.parent = this.node;
                    dd.ui_manager.hideLoading();
                }
            }
        }
    }
    /**
     * 点击充值
     * 
     * @memberof HomeCanvas
     */
    click_btn_recharge() {
        dd.mp_manager.playButton();
        //获取兑换比例
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.ORDER_GET_EXCHANAGE_PERCENT, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    if (!this._recharge || !this._recharge.isValid) {
                        dd.ui_manager.isShowPopup = false;
                        this._recharge = cc.instantiate(this.recharge_prefab);
                        this._recharge.parent = this.node;
                        let rechargeScript = this._recharge.getComponent('Recharge');
                        rechargeScript.initData(content);
                        dd.ui_manager.hideLoading();
                    }
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }
    /**
     * 兑换
     * @memberof HomeCanvas
     */
    click_btn_exchange() {
        dd.mp_manager.playButton();
        //获取兑换比例
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.ORDER_GET_EXCHANAGE_PERCENT, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    if (!this._exchange || !this._exchange.isValid) {
                        dd.ui_manager.isShowPopup = false;
                        this._exchange = cc.instantiate(this.exchange_prefab);
                        this._exchange.parent = this.node;
                        let exchangeScript = this._exchange.getComponent('Exchange');
                        exchangeScript.initData(content);
                        dd.ui_manager.hideLoading();
                    }
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.hideLoading();
                }
            });
        }
    }
    /**
     * 客服
     * 
     * @memberof HomeCanvas
     */
    click_btn_service() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_CUSTOMER_SERVICE, '', (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    if (dd.ui_manager.isShowPopup) {
                        if (!this._service || !this._service.isValid) {
                            dd.ui_manager.isShowPopup = false;
                            this._service = cc.instantiate(this.service_prefab);
                            let service_script = this._service.getComponent('Service');
                            service_script.initData(content);
                            this._service.parent = this.node;
                        }
                    }
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                }
            });
        }
    }
}
