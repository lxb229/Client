import * as dd from './../../Modules/ModuleManager';

const { ccclass, property } = cc._decorator;

@ccclass
export default class Safe extends cc.Component {
    @property(cc.Label)
    lbl_time: cc.Label = null;
    /**
     * allin玩家的节点列表
     * @type {cc.Node[]}
     * @memberof Safe
     */
    @property([cc.Node])
    allin_node_list: cc.Node[] = [];

    /**
     * 公共牌的节点列表
     * @type {cc.Node[]}
     * @memberof Safe
     */
    @property([cc.Node])
    pCard_node_list: cc.Node[] = [];
    /**
     * allin玩家的自己颜色
     * @type {cc.Color[]}
     * @memberof Safe
     */
    @property([cc.Color])
    allin_color_list: cc.Color[] = [];
    /**
     * 已选择
     * @type {cc.Label}
     * @memberof Safe
     */
    @property(cc.Label)
    lbl_choose: cc.Label = null;
    /**
     * 赔率
     * @type {cc.Label}
     * @memberof Safe
     */
    @property(cc.Label)
    lbl_odds: cc.Label = null;
    /**
     * 主池
     * @type {cc.Label}
     * @memberof Safe
     */
    @property(cc.Label)
    lbl_pool: cc.Label = null;
    /**
     * 投保额
     * @type {cc.Label}
     * @memberof Safe
     */
    @property(cc.Label)
    lbl_tbe: cc.Label = null;
    /**
     * 押注
     * @type {cc.Label}
     * @memberof Safe
     */
    @property(cc.Label)
    lbl_bet: cc.Label = null;
    /**
     * 赔付额
     * @type {cc.Label}
     * @memberof Safe
     */
    @property(cc.Label)
    lbl_pfe: cc.Label = null;
    /**
     * 进度条
     * @type {cc.ProgressBar}
     * @memberof Safe
     */
    @property(cc.ProgressBar)
    safe_pro: cc.ProgressBar = null;
    /**
     * 选择所有
     * @type {cc.Toggle}
     * @memberof Safe
     */
    @property(cc.Toggle)
    toggle_choose_all: cc.Toggle = null;
    /**
     * 反超牌的牌的父节点
     * @type {cc.Node}
     * @memberof Safe
     */
    @property(cc.ScrollView)
    fcp_svNode: cc.ScrollView = null;
    /**
     * 非自己购买保险
     * @type {cc.RichText}
     * @memberof Safe
     */
    @property(cc.RichText)
    lbl_other: cc.RichText = null;
    /**
     * 如果是自己购买保险
     * @type {cc.Node}
     * @memberof Safe
     */
    @property(cc.Node)
    mineNode: cc.Node = null;
    /**
     * 如果是自己购买保险，反超牌的节点
     * @type {cc.Node}
     * @memberof Safe
     */
    @property(cc.Node)
    fcp_mineNode: cc.Node = null;
    /**
     * 亮图
     * @type {cc.SpriteFrame}
     * @memberof Safe
     */
    @property(cc.SpriteFrame)
    imgLight: cc.SpriteFrame = null;

    /**
     * 牌节点预设
     * @type {cc.Prefab}
     * @memberof Safe
     */
    @property(cc.Prefab)
    safe_card_prefab: cc.Prefab = null;
    /**
     * allin玩家的预设
     * @type {cc.Prefab}
     * @memberof Safe
     */
    @property(cc.Prefab)
    safe_allin_prefab: cc.Prefab = null;
    /**
     * 选中的牌数量
     * @type {number}
     * @memberof Safe
     */
    _chooseCard: number = 0;
    /**
     * 赔率
     * @type {InsuranceCfgAttrib}
     * @memberof Safe
     */
    _rate: number = 0;
    /**
     * 倒计时
     * @type {number}
     * @memberof Safe
     */
    _timeDown: number = 0;
    onLoad() {
        this.updateData();
    }

    update(dt) {
        if (this._timeDown > 0) {
            this._timeDown -= dt;
            this.lbl_time.string = Math.floor(this._timeDown) + 's';
        } else {
            this.lbl_time.string = '0s';
            this._timeDown = 0;
        }
    }
    /**
     * 刷新数据
     * @memberof Safe
     */
    updateData() {
        let tableData = dd.gm_manager.getTableData();
        if (tableData && tableData.insuranceStateAttrib) {
            this._timeDown = (Number(tableData.actTime) - Number(tableData.svrTime)) / 1000;
            this.lbl_time.string = this._timeDown + 's';
            let allin_list = tableData.insuranceStateAttrib.insuranceSeatList;
            //购买保险的玩家
            let safeSeat: InsuranceSeatAttrib = null;
            for (let i = 0; i < allin_list.length; i++) {
                this.showAllInSeat(allin_list[i], tableData.insuranceStateAttrib, this.allin_node_list[i]);
                //如果是购买保险的玩家
                if (tableData.insuranceStateAttrib.accountId === allin_list[i].accountId) {
                    safeSeat = allin_list[i];
                }
            }

            let pCards = tableData.insuranceStateAttrib.tableHandCards;
            for (let i = 0; i < this.pCard_node_list.length; i++) {
                if (i < pCards.length) {
                    this.showSafeCard(pCards[i], this.pCard_node_list[i]);
                } else {
                    if (i === pCards.length) {
                        this.showLight(this.pCard_node_list[i]);
                    }
                    this.showSafeCard(0, this.pCard_node_list[i]);
                }
            }

            this.fcp_svNode.content.removeAllChildren();
            let winCardList = tableData.insuranceStateAttrib.winCardList;
            for (let i = 0; i < winCardList.length; i++) {
                this.showSafeCard(winCardList[i], this.fcp_svNode.content, false, (cardNode: cc.Node) => {
                    if (tableData.insuranceStateAttrib.accountId === dd.ud_manager.account_mine.accountId) {
                        cardNode.tag = i;
                        cardNode.on(cc.Node.EventType.TOUCH_END, this.touch_fcp_end, this);
                    }
                });
            }

            if (tableData.insuranceStateAttrib.accountId === dd.ud_manager.account_mine.accountId) {
                this.mineNode.active = true;
                this.fcp_mineNode.active = true;
                this.lbl_other.node.active = false;
            } else {
                this.mineNode.active = false;
                this.fcp_mineNode.active = false;
                if (safeSeat) {
                    this.lbl_other.node.active = true;
                    this.lbl_other.string = '请稍等,玩家[ <color=#FFC103>' + safeSeat.nick + '</c> ]正在购买保险';
                } else {
                    this.lbl_other.node.active = false;
                }
            }

            this.lbl_bet.string = tableData.insuranceStateAttrib.betMoney;
            let pm = Number(tableData.insuranceStateAttrib.poolMoney) - Number(tableData.insuranceStateAttrib.buyedNum);
            this.lbl_pool.string = pm.toString();
            this.lbl_pfe.string = '0';
            this.lbl_tbe.string = '0';
            this.lbl_choose.string = '0';
            this.lbl_odds.string = '0';
            this.toggle_choose_all.isChecked = false;
            this._chooseCard = 0;
            this._rate = 0;
            this.safe_pro.progress = 0;
            this.safe_pro.node.getComponent(cc.Slider).progress = 0;
        }
    }
    /**
     * 点击反超牌
     * @param {cc.Event.EventTouch} event 
     * @memberof Safe
     */
    touch_fcp_end(event: cc.Event.EventTouch) {
        let cardNode = event.getCurrentTarget();
        cardNode.getChildByName('choose').active = !cardNode.getChildByName('choose').active;
        if (cardNode.getChildByName('choose').active) {
            this._chooseCard++;
        } else {
            this._chooseCard--;
        }
        this.showFCPInfo();
    }
    /**
     * 显示一张亮的图片
     * @param {cc.Node} parentNode 
     * @memberof Safe
     */
    showLight(parentNode: cc.Node) {
        let light = new cc.Node();
        let sp = light.addComponent(cc.Sprite);
        sp.spriteFrame = this.imgLight;
        light.parent = parentNode;
    }
    /**
     * 显示反超牌的信息
     * @memberof Safe
     */
    showFCPInfo() {
        let tableData = dd.gm_manager.getTableData();
        if (tableData && tableData.insuranceStateAttrib) {
            this.lbl_choose.string = this._chooseCard + '';

            let rateList = tableData.insuranceStateAttrib.insuranceRateList;
            if (this._chooseCard >= rateList.length) {
                this._rate = Number(rateList[rateList.length - 1].rate);
                this.lbl_odds.string = rateList[rateList.length - 1].rate;
            } else if (this._chooseCard <= 0) {
                this._rate = 0;
                this.lbl_odds.string = '0';
            } else {
                for (let i = 0; i < rateList.length; i++) {
                    if (rateList[i].cardNum === this._chooseCard) {
                        this.lbl_odds.string = rateList[i].rate;
                        this._rate = Number(rateList[i].rate);
                        break;
                    }
                }
            }
        }
        this.showTBInfo(0);
    }

    /**
     * 显示allin座位的玩家
     * @param {InsuranceSeatAttrib} seat 
     * @memberof Safe
     */
    showAllInSeat(seat: InsuranceSeatAttrib, safeData: InsuranceStateAttrib, parentNode: cc.Node) {
        let allinNode = cc.instantiate(this.safe_allin_prefab);
        for (let i = 0; i < seat.handCards.length; i++) {
            let card = allinNode.getChildByName('card' + (i + 1));
            this.showSafeCard(seat.handCards[i], card);
        }
        let des = allinNode.getChildByName('des').getComponent(cc.Label);
        if (seat.accountId === safeData.accountId) {
            des.node.color = this.allin_color_list[0];
            des.string = '购买保险';
        } else {
            des.node.color = this.allin_color_list[1];
            des.string = seat.cardNum + '个outs';
        }
        allinNode.getChildByName('name').getComponent(cc.Label).string = dd.utils.getStringBySize(seat.nick, 12);;
        allinNode.parent = parentNode;
    }

    /**
     * 显示牌
     * @param {number} cardId  牌id
     * @param {cc.Node} parentNode 父节点
     * @param {boolean} [isShowChoose=false] 是否选中
     * @param {*} [initCB=null] 回调函数
     * @memberof Safe
     */
    showSafeCard(cardId: number, parentNode: cc.Node, isShowChoose: boolean = false, initCB: any = null) {
        let safe_card = cc.instantiate(this.safe_card_prefab);
        safe_card.getChildByName('img').getComponent(cc.Sprite).spriteFrame = dd.img_manager.getPokerSpriteFrameById(cardId);
        safe_card.getChildByName('choose').active = isShowChoose;
        safe_card.parent = parentNode;
        if (initCB) {
            initCB(safe_card);
        }
    }
    /**
     * 选中所有
     * @memberof Safe
     */
    click_toggle_chooseAll() {
        dd.mp_manager.playButton();
        this.fcp_svNode.content.children.forEach((cardNode: cc.Node, index: number) => {
            cardNode.getChildByName('choose').active = this.toggle_choose_all.isChecked;
        });
        this._chooseCard = this.toggle_choose_all.isChecked ? this.fcp_svNode.content.childrenCount : 0;
        this.showFCPInfo();
    }
    /**
     * 进度条的bar
     * @memberof Safe
     */
    click_pro_bar() {
        let pro = this.safe_pro.node.getComponent(cc.Slider).progress;
        this.showTBInfo(pro);
    }
    /**
     * 显示投保信息
     * @memberof Safe
     */
    showTBInfo(pro: number) {
        let tableData = dd.gm_manager.getTableData();
        if (tableData && tableData.insuranceStateAttrib) {
            if (this._rate !== 0) {
                let maxSafe = (Number(tableData.insuranceStateAttrib.poolMoney) - Number(tableData.insuranceStateAttrib.buyedNum)) / this._rate;
                let curSafe = maxSafe * pro;
                if (curSafe <= 0) {
                    curSafe = 1;
                }
                let tbe = Math.ceil(Math.ceil(curSafe) / tableData.smallBlind) * tableData.smallBlind;
                this.lbl_tbe.string = tbe.toString();
                let pfe = Math.floor(Math.floor(tbe * this._rate) / tableData.smallBlind) * tableData.smallBlind;
                if (pfe > Number(tableData.insuranceStateAttrib.poolMoney)) {
                    pfe = Number(tableData.insuranceStateAttrib.poolMoney);
                }
                this.lbl_pfe.string = pfe.toString();
            } else {
                this.lbl_pfe.string = '0';
                this.lbl_tbe.string = '0';
            }
            this.safe_pro.progress = pro;
            this.safe_pro.node.getComponent(cc.Slider).progress = pro;
        }
    }
    /**
     * 购买
     * @memberof Safe
     */
    click_btn_buy() {
        dd.mp_manager.playButton();
        if (this._rate !== 0) {
            this.sendBuySafe(1);
            this.node.removeFromParent();
            this.node.destroy();
        } else {
            dd.ui_manager.showTip('请先选择反超牌', 200, 0.3, 0.5, 0.2);
        }
    }
    /**
     * 不买
     * @memberof Safe
     */
    click_btn_nobuy() {
        dd.mp_manager.playButton();
        this.sendBuySafe(0);
        this.node.removeFromParent();
        this.node.destroy();
    }
    /**
     * 购买保险
     * @param {number} bt 
     * @memberof Safe
     */
    sendBuySafe(bt: number) {
        let tableData = dd.gm_manager.getTableData();
        if (tableData && tableData.insuranceStateAttrib) {
            let winCardList = tableData.insuranceStateAttrib.winCardList;
            let buyCards = [];
            this.fcp_svNode.content.children.forEach((cardNode: cc.Node, index: number) => {
                let cardId = winCardList[cardNode.tag];
                if (cardNode.getChildByName('choose').active) {
                    buyCards.push(cardId);
                }
            });
            let obj = {
                tableId: tableData.tableId,
                bt: bt,
                buyCards: buyCards,
                buyMoney: Number(this.lbl_tbe.string),
                payMoney: Number(this.lbl_pfe.string),
            };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_BUY_INSURANCE, msg, (flag: number, content?: any) => {
                if (flag !== 0) {
                    dd.ui_manager.showTip(content);
                }
            });
        }
    }
    /**
     * 保本
     * @memberof Safe
     */
    click_btn_baoben() {
        dd.mp_manager.playButton();
        if (this._rate !== 0) {
            let tableData = dd.gm_manager.getTableData();
            if (tableData && tableData.insuranceStateAttrib) {
                let maxSafe = (Number(tableData.insuranceStateAttrib.poolMoney) - Number(tableData.insuranceStateAttrib.buyedNum)) / this._rate;
                let tbe = Math.ceil(Number(tableData.insuranceStateAttrib.betMoney) / this._rate);
                if (tbe > maxSafe) {
                    dd.ui_manager.showTip('可投保额不足以保本');
                    return;
                }
                tbe = Math.ceil(tbe / tableData.smallBlind) * tableData.smallBlind;
                this.lbl_tbe.string = tbe.toString();
                let pfe = Math.floor(Math.floor(tbe * this._rate) / tableData.smallBlind) * tableData.smallBlind;
                if (pfe > Number(tableData.insuranceStateAttrib.poolMoney)) {
                    pfe = Number(tableData.insuranceStateAttrib.poolMoney);
                }
                this.lbl_pfe.string = pfe.toString();

                let pro = tbe / (maxSafe - 1);
                this.safe_pro.progress = pro;
                this.safe_pro.node.getComponent(cc.Slider).progress = pro;
            }
        } else {
            this.lbl_pfe.string = '0';
            this.lbl_tbe.string = '0';
            this.safe_pro.progress = 0;
            this.safe_pro.node.getComponent(cc.Slider).progress = 0;
            dd.ui_manager.showTip('请先选择反超牌', 200, 0.3, 0.5, 0.2);
        }
    }
    /**
     * 等利
     * @memberof Safe
     */
    click_btn_dengli() {
        dd.mp_manager.playButton();
        if (this._rate !== 0) {
            let tableData = dd.gm_manager.getTableData();
            if (tableData && tableData.insuranceStateAttrib) {
                let dm = Number(tableData.insuranceStateAttrib.poolMoney) - Number(tableData.insuranceStateAttrib.buyedNum);
                let maxSafe = dm / this._rate;

                let tbe = Math.ceil((Math.ceil(maxSafe) / tableData.smallBlind)) * tableData.smallBlind;
                this.lbl_tbe.string = tbe.toString();
                let pfe = Math.floor(Math.floor(tbe * this._rate) / tableData.smallBlind) * tableData.smallBlind;
                if (pfe > Number(tableData.insuranceStateAttrib.poolMoney)) {
                    pfe = Number(tableData.insuranceStateAttrib.poolMoney);
                }
                this.lbl_pfe.string = pfe.toString();

                this.safe_pro.progress = 1;
                this.safe_pro.node.getComponent(cc.Slider).progress = 1;
            }
        } else {
            this.lbl_pfe.string = '0';
            this.lbl_tbe.string = '0';
            this.safe_pro.progress = 0;
            this.safe_pro.node.getComponent(cc.Slider).progress = 0;
            dd.ui_manager.showTip('请先选择反超牌', 200, 0.3, 0.5, 0.2);
        }
    }
    /**
     * 退出
     * @memberof Safe
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        this.node.removeFromParent();
        this.node.destroy();
    }
}
