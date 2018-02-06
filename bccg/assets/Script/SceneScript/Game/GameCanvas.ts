const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class GameCanvans extends cc.Component {
    /**
     * 存放信息的父节点
     * 
     * @type {cc.Node}
     * @memberof GameCanvans
     */
    @property(cc.Node)
    info: cc.Node = null;
    /**
     * 押注池预设
     * @type {cc.Prefab}
     * @memberof GameCanvans
     */
    @property(cc.Prefab)
    pool_prefab: cc.Prefab = null;
    /**
     * 下注池节点
     * @type {cc.Node}
     * @memberof GameCanvans
     */
    @property(cc.Node)
    pool: cc.Node = null;
    /**
     * 边池节点列表
     * @type {cc.Node}
     * @memberof GameCanvans
     */
    @property([cc.Node])
    sidePoolList: cc.Node[] = [];
    /**
     * 倒计时展示标签
     * 
     * @type {cc.Label}
     * @memberof GameCanvans
     */
    @property(cc.Label)
    lba_time: cc.Label = null;
    /**
     * 底池
     * @type {cc.Label}
     * @memberof GameCanvans
     */
    @property(cc.Label)
    lbl_poolMoneys: cc.Label = null;
    /**
     * 保险预设
     * @type {cc.Prefab}
     * @memberof GameCanvans
     */
    @property(cc.Prefab)
    safe_prefab: cc.Prefab = null;

    _safe: cc.Node = null;
    onLoad() {
        cc.systemEvent.on('updateGame', this.showGameInfo, this);
        this.showInfo();
    }

    onDestroy() {
        dd.gm_manager.clean();
        cc.systemEvent.off('updateGame', this.showPool, this);
    }
    /**
     * 显示桌子信息
     * 
     * @memberof GameCanvans
     */
    showInfo() {
        if (dd.gm_manager && dd.gm_manager.getTableData()) {
            let tableData = dd.gm_manager.getTableData();
            cc.find('layout2/name', this.info).getComponent(cc.Label).string = tableData.tableName;
            cc.find('layout2/id', this.info).getComponent(cc.Label).string = tableData.tableId.toString();
            cc.find('layout2/small', this.info).getComponent(cc.Label).string = tableData.smallBlind.toString();
            cc.find('layout2/big', this.info).getComponent(cc.Label).string = tableData.bigBlind.toString();
            this.showGameInfo();
        }
    }
    /**
     * 显示游戏信息
     * @memberof GameCanvans
     */
    showGameInfo() {
        this.showSafe();
        if (dd.gm_manager && dd.gm_manager.getTableData()) {
            let tableData = dd.gm_manager.getTableData();
            //如果是 新一轮积分结算 阶段，播放所有人的飞积分动作
            if (tableData.gameState === dd.game_state.STATE_TABLE_NEW_ROUND_BET) {
                //这里判断是否需要飞积分的动作
                let seats = tableData.seats;
                let index = 0;
                for (let i = 0; i < seats.length; i++) {
                    let seat = seats[i];
                    if (seat.bGamed === 1 && Number(seat.betMoney) > 0) {
                        if (dd.gm_manager.playerScript) {
                            dd.gm_manager.playerScript.showFlyBetAction(seat, () => {
                                if (index === 0) {
                                    this.showPool(tableData.pools);
                                }
                            });
                        }
                    }
                }
            } else {
                if (tableData.gameState > dd.game_state.STATE_TABLE_BETBLIND && tableData.gameState < dd.game_state.STATE_TABLE_OVER_ONCE) {
                    if (tableData.gameState !== dd.game_state.STATE_TABLE_BET_BT_1
                        && tableData.gameState !== dd.game_state.STATE_TABLE_BET_BT_2
                        && tableData.gameState !== dd.game_state.STATE_TABLE_BET_BT_3
                        && tableData.gameState !== dd.game_state.STATE_TABLE_BET_BT_4) {
                        this.showPool(tableData.pools);
                    }
                } else {
                    this.sidePoolList.forEach(sidePool => {
                        sidePool.removeAllChildren();
                        sidePool.active = false;
                    });
                }
            }
        }
    }
    /**
     * 显示下注池
     * @memberof GameCanvans
     */
    showPool(pools: string[]) {
        if (!pools || !pools[0]) return;
        this.sidePoolList.forEach(sidePool => {
            sidePool.removeAllChildren();
        });
        pools.forEach((bet: string, index: number) => {
            if (index < 4) {
                this.sidePoolList[0].active = true;
                this.creatSidePool(bet, this.sidePoolList[0]);
            } else if (index >= 4 && index < 8) {
                this.sidePoolList[1].active = true;
                this.creatSidePool(bet, this.sidePoolList[1]);
            } else {
                this.sidePoolList[2].active = true;
                this.creatSidePool(bet, this.sidePoolList[2]);
            }
        });
    }

    creatSidePool(bet: string, parentNode: cc.Node) {
        let sPool = cc.instantiate(this.pool_prefab);
        cc.find('lblBet', sPool).getComponent(cc.Label).string = bet;
        sPool.parent = parentNode;
    }
    /**
     * 显示保险
     * @memberof GameCanvans
     */
    showSafe() {
        let tableData = dd.gm_manager.getTableData();
        if (tableData.gameState === dd.game_state.STATE_TABLE_BUY_INSURANCE) {
            if (!this._safe || !this._safe.isValid) {
                this._safe = cc.instantiate(this.safe_prefab);
                this._safe.parent = this.node;
            }
        } else {
            if (this._safe && this._safe.isValid) {
                this._safe.removeFromParent();
                this._safe.destroy();
            }
        }

    }
    update(dt: number) {
        if (dd && dd.gm_manager) {
            //倒计时标签控制
            if (dd.gm_manager.countDownTime > 0) {
                dd.gm_manager.countDownTime -= dt * 1000;
            }
            if (dd.gm_manager.countDownTime < 0) {
                cc.log('房间时间到了,系统回收房间');
                dd.gm_manager.countDownTime = 0;
            }
            this.lba_time.string = dd.utils.getCountDownString(dd.gm_manager.countDownTime);

            if (dd.gm_manager.getTableData()) {
                if (Number(dd.gm_manager.getTableData().poolMoneys) > 0) {
                    this.lbl_poolMoneys.node.parent.active = true;
                    this.lbl_poolMoneys.string = dd.gm_manager.getTableData().poolMoneys;
                } else {
                    this.lbl_poolMoneys.node.parent.active = false;
                }
            }
        }
    }
}
