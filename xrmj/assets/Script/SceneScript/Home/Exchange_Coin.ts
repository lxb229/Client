
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

const eTime: number = 0.1;
@ccclass
export default class Exchange_Coin extends cc.Component {

    /**
     * 所有礼品界面
     * @type {cc.Node}
     * @memberof Exchange_Coin
     */
    @property(cc.Node)
    board_gifts: cc.Node = null;
    /**
     * 结果
     * @type {cc.Node}
     * @memberof Exchange_Coin
     */
    @property(cc.Node)
    board_result: cc.Node = null;
    /**
     * 银币界面银币
     * @type {cc.Label}
     * @memberof Exchange_Coin
     */
    @property(cc.Label)
    coin_lblCoin: cc.Label = null;
    /**
     * 银币界面刷新消耗
     * @type {cc.Label}
     * @memberof Exchange_Coin
     */
    @property(cc.Label)
    coin_lblCostUpdate: cc.Label = null;
    /**
     * 银币界面抽奖消耗
     * @type {cc.Label}
     * @memberof Exchange_Coin
     */
    @property(cc.Label)
    coin_lblCost: cc.Label = null;
    /**
     * 奖品列表
     * @type {cc.Label}
     * @memberof Exchange_Coin
     */
    @property(cc.Label)
    coin_lblGift: cc.Label = null;
    /**
     * 所有奖品列表
     * @type {cc.ScrollView}
     * @memberof Exchange_Coin
     */
    @property(cc.ScrollView)
    coin_gift_svNode: cc.ScrollView = null;
    /**
     * 奖品item预设
     * @type {cc.Prefab}
     * @memberof Exchange_Coin
     */
    @property(cc.Prefab)
    coin_gift_prefab: cc.Prefab = null;
    /**
     * 牌列表
     * @type {cc.Node[]}
     * @memberof Exchange_Coin
     */
    @property([cc.Node])
    silver_list: cc.Node[] = [];
    /**
     * 牌图片
     * @type {cc.SpriteFrame[]}
     * @memberof Exchange_Coin
     */
    @property([cc.SpriteFrame])
    silver_img_list: cc.SpriteFrame[] = [];
    /**
     * 结果图片节点
     * @type {cc.Sprite}
     * @memberof Exchange_Coin
     */
    @property(cc.Sprite)
    img_result: cc.Sprite = null;
    /**
     * 结果图片
     * @type {cc.SpriteFrame[]}
     * @memberof Exchange_Coin
     */
    @property([cc.SpriteFrame])
    result_img_list: cc.SpriteFrame[] = [];
    /**
     * 是否动作
     * @type {boolean}
     * @memberof Exchange_Coin
     */
    _isAct: boolean = false;
    /**
     * 抽奖编号
     * @type {number}
     * @memberof Exchange_Coin
     */
    _luckIndex: number = -1;

    _coinData: CoinVo = null;
    _dataList = [];

    _resultTime: number = 0;
    onLoad() {
        this._dataList = [
            { id: 1, des: '壹号牌' },
            { id: 2, des: '贰号牌' },
            { id: 3, des: '叁号牌' },
            { id: 4, des: '肆号牌' },
            { id: 5, des: '伍号牌' },
            { id: 6, des: '陆号牌' },
            { id: 7, des: '柒号牌' }];
        this.showStartAction();
        this.board_gifts.active = false;
        this.board_result.active = false;
    }
    update(dt) {
        if (this._resultTime > 0) {
            this._resultTime -= dt;
            if (this._resultTime < 0) {
                if (this._luckIndex > this.silver_list.length) {
                    this.board_result.active = true;
                    this.img_result.spriteFrame = this._coinData.winning === 1 ? this.result_img_list[0] : this.result_img_list[1];
                } else {
                    this.showRemainingSilver(() => {
                        this._luckIndex = this.silver_list.length + 1;
                        this._resultTime = 1;
                    });
                }
            }
        }
        if (dd.ud_manager && dd.ud_manager.mineData) {
            this.coin_lblCoin.string = dd.ud_manager.mineData.wallet.silverMoney.toString();
        }
    }
    initData(data: CoinVo) {
        this._coinData = data;
        this.coin_lblCostUpdate.string = data.refshCost + '银币';
        this.coin_lblCost.string = data.drawCost + '银币';
        let str = '';
        if (data.rewareList) {
            for (let i = 0; i < data.rewareList.length; i++) {
                let rewareDes = this.getDesById(i + 1) + '：' + data.rewareList[i].itemName;
                if (i !== data.rewareList.length - 1) rewareDes += '\n';
                str += rewareDes;
            }
        }
        this.coin_lblGift.string = str;
    }
    /**
     * 根据id获取好牌名称
     * @param {number} id 
     * @memberof Exchange_Coin
     */
    getDesById(id: number) {
        let str = '';
        switch (id) {
            case 1:
                str = '壹号牌';
                break;
            case 2:
                str = '贰号牌';
                break;
            case 3:
                str = '叁号牌';
                break;
            case 4:
                str = '肆号牌';
                break;
            case 5:
                str = '伍号牌';
                break;
            case 6:
                str = '陆号牌';
                break;
            case 7:
                str = '柒号牌';
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 播放开始时的动作
     * @param {boolean} isUpdateData 是否刷新数据
     * @memberof Exchange_Coin
     */
    showStartAction(isUpdateData: boolean = false) {
        this._luckIndex = -1;
        this._isAct = true;
        dd.utils.shuffle(this._dataList);
        this.showSilverFPAction(0, this.silver_list, eTime * 2, () => {
            this.showSilverMoveAction(0.5, 0, () => {
                this.showSilverMoveAction(1, 1, () => {
                    this._isAct = false;
                    if (isUpdateData) this.initData(this._coinData);
                });
            });
        });
    }
    /**
     * 设置牌数据
     * @param {number} index 
     * @memberof Exchange_Coin
     */
    setSilverInfo(index: number, data: any) {
        let tempList = [];
        let target = null;
        this._dataList.forEach(obj => {
            if (obj.id !== data) {
                tempList.push(obj);
            } else {
                target = obj;
            }
        });
        dd.utils.shuffle(tempList);
        let tIndex = 0;
        this.silver_list.forEach((silver: cc.Node, i: number) => {
            let lblwerfar = silver.getChildByName('lblwerfar');
            if (lblwerfar) {
                if (index === i) {
                    lblwerfar.getComponent(cc.Label).string = target.des;
                } else {
                    lblwerfar.getComponent(cc.Label).string = tempList[tIndex].des;
                    tIndex++;
                }
            }
        });
    }
    /**
     * 做一张张翻牌的动作
     * @param {number} act 0=关闭 1=翻开
     * @memberof Exchange_Coin
     */
    showSilverFPAction(act: number, cNodeList: cc.Node[], interval: number, cb?: any) {
        let count = 0;
        this.schedule(() => {
            //计算第几个位置
            let index = count % cNodeList.length;
            let cNode = cNodeList[index];
            this.playSilverFP(act, cNode, eTime, count, (eIndex: number) => {
                if (eIndex === cNodeList.length - 1) {
                    cc.log('动作完成');
                    if (cb) cb();
                }
            });
            count++;
        }, interval, cNodeList.length - 1);
    }

    /**
     * 播放翻牌动作
     * @param {number} act 0=关闭 1=翻开
     * @param {cc.Node} silver 牌节点
     * @param {number} eTime 动作一半的时间
     * @param {number} index 当前第几个牌的动作
     * @param {*} [cb] 回调
     * @param {*} [data] 牌数据
     * @memberof Exchange_Coin
     */
    playSilverFP(act: number, silver: cc.Node, eTime: number, index: number, cb?: any, data?: any) {
        if (silver) {
            let cSkewY = act === 0 ? -15 : 15;
            let spawn1 = cc.spawn(cc.scaleTo(eTime, 0, 1), cc.skewTo(eTime, 0, cSkewY));
            let finished1 = cc.callFunc((target, opt) => {
                silver.skewY = -cSkewY;
                silver.getComponent(cc.Sprite).spriteFrame = act === 0 ? this.silver_img_list[0] : this.silver_img_list[1];
                let lblwerfar = silver.getChildByName('lblwerfar');
                if (lblwerfar) {
                    lblwerfar.active = act === 0 ? false : true;
                    if (data) lblwerfar.getComponent(cc.Label).string = data;
                }
            }, this);
            let spawn2 = cc.spawn(cc.scaleTo(eTime, 1, 1), cc.skewTo(eTime, 0, 0));
            let finished2 = cc.callFunc((target, opt) => {
                cc.log('动作完成' + index);
                if (cb) cb(index);
            }, this);
            let myAction = cc.sequence(spawn1, finished1, spawn2, finished2);
            silver.runAction(myAction);
        }
    }

    /**
     * 位移动作
     * @param {number} mTime 动作时间
     * @param {number} act 动作类型 0=聚合 1=散开
     * @param {*} [cb] 动作全部结束回调
     * @memberof Exchange_Coin
     */
    showSilverMoveAction(mTime: number, act: number, cb?: any) {
        this.silver_list.forEach((silver: cc.Node, i: number) => {
            if (act === 0) {
                let move = cc.moveTo(mTime, cc.p(0, 0)).easing(cc.easeIn(0.5));
                let action = cc.sequence(cc.moveTo(mTime, cc.p(0, 0)), cc.callFunc((target, opt) => {
                    if (opt.index === this.silver_list.length - 1) {
                        if (cb) cb();
                    }
                }, this, { index: i }));
                silver.runAction(action);
            } else {
                let dx = (i - 3) * (silver.width + 8);
                let move = cc.moveTo(mTime, cc.p(dx, 0)).easing(cc.easeOut(0.5));
                let action = cc.sequence(move, cc.callFunc((target, opt) => {
                    if (opt.index === this.silver_list.length - 1) {
                        if (cb) cb();
                    }
                }, this, { index: i }));
                silver.runAction(action);
            }
        });
    }

    /**
     * 退出按钮
     * @param {any} event 
     * @param {string} type 
     * @memberof Exchange_Coin
     */
    click_btn_out(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0':
                this.node.removeFromParent(true);
                this.node.destroy();
                break;
            case '1':
                this.board_gifts.active = false;
                break;
            case '2':
                this.board_result.active = false;
                this.showStartAction(true);
                break;
            default:
                break;
        }
    }
    /**
     * 刷新银币
     * @memberof Exchange_Coin
     */
    click_btn_updateCoin() {
        if (!this._coinData) return cc.log('无数据');
        if (this._isAct) return cc.log('正在播放动作');
        if (this._resultTime > 0) return cc.log('播放结果中...');
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.TASK_SILVER_REWARE_REFSH, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    this.initData(content as CoinVo);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 查看所有的礼品列表
     * @memberof Exchange_Coin
     */
    click_btn_lookAllGift() {
        if (!this._coinData) return cc.log('无数据');
        if (this._resultTime > 0) return cc.log('播放结果中...');
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            dd.ws_manager.sendMsg(dd.protocol.TASK_SILVER_REWARE_ALL_ITEM, '', (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log(content);
                    this.showAllGifts(content);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showTip(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
    /**
     * 显示所有奖品
     * @memberof Exchange_Coin
     */
    showAllGifts(data: string[]) {
        this.coin_gift_svNode.content.removeAllChildren();
        if (data) {
            data.forEach((name: string) => {
                let coin_gift = cc.instantiate(this.coin_gift_prefab);
                let lblName = coin_gift.getChildByName('lblName');
                if (lblName) lblName.getComponent(cc.Label).string = name;
                coin_gift.parent = this.coin_gift_svNode.content;
            });
        }
        this.board_gifts.active = true;
    }
    /**
     * 显示剩下的牌的动作
     * @memberof Exchange_Coin
     */
    showRemainingSilver(cb?: any) {
        this._isAct = true;
        let cNodeList = [];
        let tempList = this._dataList.slice().splice(this._luckIndex, 1);
        this.silver_list.forEach((silver, i) => {
            if (i !== this._luckIndex) {
                cNodeList.push(silver);
            }
        });
        this.showSilverFPAction(1, cNodeList, 0, () => {
            this._isAct = false;
            if (cb) cb();
        });
    }
    /**
     * 银币抽奖
     * @memberof Exchange_Coin
     */
    click_btn_luckDraw(event, type: string) {
        if (!this._coinData) return cc.log('无数据');
        if (this._isAct) return cc.log('正在播放动作');
        let index = Number(type);
        if (this._luckIndex > -1) {
            return cc.log('已经抽奖了');
        }
        dd.mp_manager.playButton();
        this._isAct = true;
        this._luckIndex = index;
        if (dd.ui_manager.showLoading()) {
            let obj = { 'accountId': dd.ud_manager.mineData.accountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.TASK_SILVER_REWARE_GET, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    cc.log('======抽奖结果-----');
                    cc.log(content);
                    this._coinData = content;
                    this.setSilverInfo(index, content.rewareIndex);
                    this.playSilverFP(1, this.silver_list[index], eTime, index, () => {
                        this._isAct = false;
                        this._resultTime = 1;
                    });
                } else if (flag === -1) {//超时
                    this._isAct = false;
                    this._luckIndex = -1;
                } else {//失败,content是一个字符串
                    this._isAct = false;
                    this._luckIndex = -1;
                    dd.ui_manager.showTip(content);
                }
                dd.ui_manager.hideLoading();
            });
        }
    }
}
