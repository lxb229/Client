(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/Modules/GMManager.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, 'bdcb9ku/dJF5qeEBjkJyvaY', 'GMManager', __filename);
// Script/Modules/GMManager.ts

Object.defineProperty(exports, "__esModule", { value: true });
var Protocol_1 = require("./Protocol");
var UDManager_1 = require("./UDManager");
var MPManager_1 = require("./MPManager");
/**
 * 游戏管理类
 *
 * @export
 * @class GMManager
 */
var GMManager = /** @class */ (function () {
    function GMManager() {
        /**
         * 桌子数据对象
         *
         * @type {TableData}
         * @memberof GMManager
         */
        this.tableData = null;
        /**
         * 保险数据对象
         *
         * @type {SafeData}
         * @memberof GMManager
         */
        this.safeData = null;
        /**
         * 当前正下方节点所代表的坐位号
         *
         * @type {number}
         * @memberof PlayerLayer
         */
        this.startIndex = 0;
        /**
         * 倒计时(毫秒数)
         *
         * @type {number}
         * @memberof GMManager
         */
        this.countDownTime = 0;
        /**
         * 房主获取积分申购订单数量(热点消息获取)
         *
         * @type {number}
         * @memberof GMManager
         */
        this.orderCount = -1;
        /**
         * 已经创建的公共牌数量
         * @type {number}
         * @memberof GMManager
         */
        this.publicCardNum = 0;
        /**
         * 是否已经创建自己的手牌
         * @type {boolean}
         * @memberof GMManager
         */
        this.isCreateMineCard = false;
        /**
         *是否显示游戏单局结算
         * @type {boolean}
         * @memberof GMManager
         */
        this.isShowGameOver = false;
        /**
         * 按钮层脚本
         * @type {ButtonLayer}
         * @memberof GMManager
         */
        this.btnScript = null;
        /**
         * 玩家层脚本
         * @type {PlayerLayer}
         * @memberof GMManager
         */
        this.playerScript = null;
        /**
         * 大盲倍数的底池
         * @type {number}
         * @memberof GMManager
         */
        this.poolMoneys = 0;
    }
    /**
     * 获取GMManager单例对象
     *
     * @static
     * @returns {GMManager}
     * @memberof GMManager
     */
    GMManager.getInstance = function () {
        if (GMManager._instance === null) {
            GMManager._instance = new GMManager();
        }
        return GMManager._instance;
    };
    GMManager.prototype.getTableData = function () {
        return this.tableData;
    };
    /**
     * 刷新游戏桌子对象
     *
     * @param {TableData} tableData
     * @param {number} [type] 没值代表是请求返回,1是代表状态切换,2是代表坐位变化,3是有人表态
     * @memberof GMManager
     */
    GMManager.prototype.setTableData = function (tableData, type) {
        var oldCount = -1;
        if (this.tableData) {
            oldCount = this.getSeatDownCount();
        }
        this.tableData = tableData;
        //当前这轮已下注的人下注最大值
        var r_max = Math.ceil(Number(tableData.maxBetMoney) / tableData.bigBlind) * tableData.bigBlind;
        if (r_max === 0)
            r_max = tableData.bigBlind;
        this.poolMoneys = r_max;
        var newCount = this.getSeatDownCount();
        if (oldCount > -1 && newCount > oldCount) {
            MPManager_1.default.getInstance().playJoin();
        }
        cc.log('游戏数据++++++++gameState==' + tableData.gameState);
        cc.log(tableData);
        this.countDownTime = Number(tableData.vaildTime) - Number(tableData.svrTime);
        //表态下注
        cc.systemEvent.emit('betBT');
        cc.systemEvent.emit('updateGame');
        switch (type) {
            case 1://1是代表状态切换
                cc.systemEvent.emit('updatePlayerUI');
                switch (tableData.gameState) {
                    case Protocol_1.GameState.STATE_TABLE_IDLE://空闲状态
                        this.publicCardNum = 0;
                        break;
                    case Protocol_1.GameState.STATE_TABLE_READY://准备状态
                        this.publicCardNum = 0;
                        MPManager_1.default.getInstance().playStart();
                        break;
                    case Protocol_1.GameState.STATE_TABLE_OUTCARD_1://发牌状态
                        cc.systemEvent.emit('doFPAction');
                        break;
                    case Protocol_1.GameState.STATE_TABLE_BUY_INSURANCE://保险购买状态
                        cc.systemEvent.emit('safe');
                        break;
                    default:
                        break;
                }
                break;
            case 2://2是代表坐位变化
                cc.systemEvent.emit('updatePlayer');
                break;
            case 3://3是有人表态
                cc.systemEvent.emit('updatePlayerUI');
                cc.systemEvent.emit('btResult');
                break;
            default://没值代表是请求返回
                break;
        }
        //小于结算，都赋值
        if (tableData.gameState < Protocol_1.GameState.STATE_TABLE_OVER_ONCE) {
            this.isShowGameOver = false;
        }
    };
    /**
     * 获取自己的座位信息(如果没有返回null,表示自己没有坐下)
     *
     * @returns
     * @memberof GMManager
     */
    GMManager.prototype.getMineSeat = function () {
        var account = UDManager_1.default.getInstance().account_mine;
        return this.getSeatDataByAccount(account.accountId);
    };
    /**
     * 判断是否是自己
     *
     * @param {SeatData} seatData
     * @returns {boolean}
     * @memberof GMManager
     */
    GMManager.prototype.isMineSeat = function (seatData) {
        if (seatData.accountId === UDManager_1.default.getInstance().account_mine.accountId)
            return true;
        return false;
    };
    /**
     * 获取我是否是房主
     *
     * @returns
     * @memberof GMManager
     */
    GMManager.prototype.isMineCreater = function () {
        if (this.tableData.createPlayer === UDManager_1.default.getInstance().account_mine.accountId) {
            return true;
        }
        else {
            return false;
        }
    };
    /**
     * 判断当前自己是否可以站起或离开
     *
     * @param {SeatData} mineSeat
     * @returns
     * @memberof GMManager
     */
    GMManager.prototype.canStandOrLeave = function () {
        var mineSeat = this.getMineSeat();
        if (mineSeat) {
            if (this.tableData.gameState > Protocol_1.GameState.STATE_TABLE_READY && this.tableData.gameState < Protocol_1.GameState.STATE_TABLE_OVER_ONCE) {
                if (mineSeat.btResult === 1 || mineSeat.bGamed !== 1) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    };
    /**
     * 根据用户id获取用户所在座位对象
     *
     * @param {string} acountId
     * @returns
     * @memberof GMManager
     */
    GMManager.prototype.getSeatDataByAccount = function (acountId) {
        var seatData = null;
        if (this.tableData && this.tableData.seats) {
            this.tableData.seats.forEach(function (seat) {
                if (seat.accountId === acountId) {
                    seatData = seat;
                }
            }, this);
        }
        return seatData;
    };
    /**
     * 根据下标获取坐位对象
     *
     * @param {number} index
     * @returns {SeatData}
     * @memberof GMManager
     */
    GMManager.prototype.getSeatDataByIndex = function (index) {
        if (index < 0 || index > 8)
            return null;
        for (var i = 0; i < this.tableData.seats.length; i++) {
            var seat = this.tableData.seats[i];
            if (index === seat.seatIndex) {
                return seat;
            }
        }
    };
    /**
     * 获取坐下的数量
     *
     * @memberof GMManager
     */
    GMManager.prototype.getSeatDownCount = function () {
        var count = 0;
        if (this.tableData && this.tableData.seats) {
            this.tableData.seats.forEach(function (seat) {
                if (seat.accountId !== '0') {
                    count++;
                }
            }, this);
        }
        return count;
    };
    /**
     * 获取牌型
     * @param {number} cType
     * @memberof PlayerUI
     */
    GMManager.prototype.getCardType = function (cType) {
        var str = '';
        switch (cType) {
            case Protocol_1.CardType.TYPE_CARD_NONE:
                str = '单   牌';
                break;
            case Protocol_1.CardType.TYPE_CARD_ONE_DOUBLE:
                str = '一   对';
                break;
            case Protocol_1.CardType.TYPE_CARD_TWO_DOUBLE:
                str = '两   对';
                break;
            case Protocol_1.CardType.TYPE_CARD_SAME_THREE:
                str = '三   条';
                break;
            case Protocol_1.CardType.TYPE_CARD_SHUN:
                str = '顺   子';
                break;
            case Protocol_1.CardType.TYPE_CARD_SAME_SUIT:
                str = '同   花';
                break;
            case Protocol_1.CardType.TYPE_CARD_GOURD:
                str = '葫   芦';
                break;
            case Protocol_1.CardType.TYPE_CARD_SAME_FOUR:
                str = '四   条';
                break;
            case Protocol_1.CardType.TYPE_CARD_SAME_SUIT_SHUN:
                str = '同 花 顺';
                break;
            case Protocol_1.CardType.TYPE_CARD_GOLD_SAME_SUIT_SHUN:
                str = '皇家同花顺';
                break;
            default:
                break;
        }
        return str;
    };
    /**
     * 清空单例对象
     *
     * @memberof GMManager
     */
    GMManager.prototype.destroySelf = function () {
        this.clean();
        GMManager._instance = null;
    };
    /**
     * 清空数据
     *
     * @memberof GMManager
     */
    GMManager.prototype.clean = function () {
        this.tableData = null;
        this.safeData = null;
        this.startIndex = 0;
        this.countDownTime = 0;
        this.orderCount = -1;
        this.publicCardNum = 0;
        this.isCreateMineCard = false;
        this.isShowGameOver = false;
        this.btnScript = null;
        this.playerScript = null;
    };
    GMManager._instance = null;
    return GMManager;
}());
exports.default = GMManager;

cc._RF.pop();
        }
        if (CC_EDITOR) {
            __define(__module.exports, __require, __module);
        }
        else {
            cc.registerModuleFunc(__filename, function () {
                __define(__module.exports, __require, __module);
            });
        }
        })();
        //# sourceMappingURL=GMManager.js.map
        