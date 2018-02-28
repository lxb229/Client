const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Create extends cc.Component {
    /**
     * 房间名称
     * 
     * @type {cc.Label}
     * @memberof Create
     */
    @property(cc.Label)
    lab_name: cc.Label = null;
    /**
     * 大小盲注
     * 
     * @type {cc.Label}
     * @memberof Create
     */
    @property(cc.Label)
    lab_blind: cc.Label = null;
    /**
     * 买入积分
     * 
     * @type {cc.Label}
     * @memberof Create
     */
    @property(cc.Label)
    lab_bet: cc.Label = null;
    /**
     * 进度条
     * 
     * @type {cc.Sprite}
     * @memberof Create
     */
    @property(cc.Sprite)
    spr_bar: cc.Sprite = null;
    /**
     * 拖动按钮
     * 
     * @type {cc.Node}
     * @memberof Create
     */
    @property(cc.Node)
    nod_btn: cc.Node = null;
    /**
     * 时间单选容器
     * 
     * @type {cc.Node}
     * @memberof Create
     */
    @property(cc.Node)
    nod_time: cc.Node = null;
    /**
     * 时间单选项
     * 
     * @type {cc.Prefab}
     * @memberof Create
     */
    @property(cc.Prefab)
    pre_toggle: cc.Prefab = null;
    /**
     * 保险开关
     * 
     * @type {cc.Toggle}
     * @memberof Create
     */
    @property(cc.Toggle)
    tog_safe: cc.Toggle = null;
    /**
     * straddie开关
     * 
     * @type {cc.Toggle}
     * @memberof Create
     */
    @property(cc.Toggle)
    tog_straddie: cc.Toggle = null;
    /**
     * 单次购入上限
     * 
     * @type {cc.Label}
     * @memberof Create
     */
    @property(cc.Label)
    lab_limit: cc.Label = null;

    /**
     * 创建配置信息
     * 
     * @type {CreateCfg}
     * @memberof Create
     */
    cfgData: CreateCfg = null;
    /**
     * 选中的时间
     * 
     * @type {number}
     * @memberof Create
     */
    selectTime: number = null;
    /**
     * 选中的配置对象
     * 
     * @type {ChipAttrib}
     * @memberof Create
     */
    selectChip: ChipAttrib = null;
    /**
     * 初始化数据
     * 
     * @param {CreateCfg} cfg 
     * @memberof Create
     */
    init(cfg: CreateCfg) {
        this.cfgData = cfg;
    }

    onLoad() {
        this.lab_name.string = dd.utils.getStringBySize(dd.ud_manager.account_mine.roleAttribVo.nick, 12) + '的房间';
        this.updateBet(0);
        this.updatTime();
        this.updateLimit(0);

        this.spr_bar.node.parent.on(cc.Node.EventType.TOUCH_END, (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            let pos = this.spr_bar.node.convertToNodeSpaceAR(event.touch.getLocation());
            if (this.cfgData.chips.length < 2) {
                this.spr_bar.fillRange = 1;
                this.nod_btn.x = this.spr_bar.node.width;
                this.updateBet(0);
                return;
            }
            let index = dd.utils.getClosestIndex(this.spr_bar.node.width, this.cfgData.chips.length - 1, pos.x);
            pos.x = dd.utils.getClosestNumber(this.spr_bar.node.width, this.cfgData.chips.length - 1, pos.x);
            this.spr_bar.fillRange = pos.x / this.spr_bar.node.width;
            this.nod_btn.x = pos.x;
            this.updateBet(index);
        }, this);
        this.nod_btn.on(cc.Node.EventType.TOUCH_MOVE, (event: cc.Event.EventTouch) => {
            let target = event.getCurrentTarget();
            let pos = target.parent.convertToNodeSpaceAR(event.touch.getLocation());
            if (pos.x < 0) pos.x = 0;
            if (pos.x > target.parent.width) pos.x = target.parent.width;
            target.x = pos.x;
            this.spr_bar.fillRange = pos.x / target.parent.width;
            let index = dd.utils.getClosestIndex(target.parent.width, this.cfgData.chips.length - 1, pos.x);
            this.updateBet(index);
            event.stopPropagation();
        }, this);
        this.nod_btn.on(cc.Node.EventType.TOUCH_END, this.touch_fun, this);
        this.nod_btn.on(cc.Node.EventType.TOUCH_CANCEL, this.touch_fun, this);
        dd.ui_manager.hideLoading();
    }
    /**
     * 拖动条按钮点击结束和取消事件
     * 
     * @param {cc.Event.EventTouch} event 
     * @returns 
     * @memberof Create
     */
    touch_fun(event: cc.Event.EventTouch) {
        dd.mp_manager.playButton();
        let target = event.getCurrentTarget();
        let pos = target.parent.convertToNodeSpaceAR(event.touch.getLocation());
        if (this.cfgData.chips.length < 2) {
            this.spr_bar.fillRange = 1;
            this.nod_btn.x = target.parent.width;
            this.updateBet(0);
            return;
        }
        let index = dd.utils.getClosestIndex(target.parent.width, this.cfgData.chips.length - 1, pos.x);
        pos.x = dd.utils.getClosestNumber(target.parent.width, this.cfgData.chips.length - 1, pos.x);
        this.spr_bar.fillRange = pos.x / target.parent.width;
        target.x = pos.x;
        this.updateBet(index);
        event.stopPropagation();
    }

    /**
     * 更新下注积分
     * 
     * @param {number} index 
     * @returns 
     * @memberof Create
     */
    updateBet(index: number) {
        if (index < 0 || index > this.cfgData.chips.length - 1) return;
        let chip: ChipAttrib = this.cfgData.chips[index];
        if (chip) {
            this.lab_blind.string = chip.small + '/' + chip.big;
            this.lab_bet.string = chip.join.toString();
            this.selectChip = chip;
            this.lab_limit.string = this.lab_bet.string;
            this.lab_limit.node.tag = 0;
        } else {
            cc.log(index);
            cc.log(this.cfgData);
            cc.error('updateBet:Error');
        }
    }
    /**
     * 更新时间
     * 
     * @memberof Create
     */
    updatTime() {
        this.cfgData.vaildTimes.forEach((time: number, index: number) => {
            let node = cc.instantiate(this.pre_toggle);
            node.tag = time;
            if (index === 0) {
                node.getComponent(cc.Toggle).check();
                this.selectTime = time;
            }
            let lab_time = cc.find('time', node).getComponent(cc.Label);
            if (time < 60) {
                lab_time.string = time + 'M';
            } else {
                lab_time.string = Math.floor(time / 60) + 'H';
            }
            node.on('toggle', (event: cc.Event.EventCustom) => {
                dd.mp_manager.playButton();
                let toggle: cc.Toggle = event.detail;
                this.selectTime = toggle.node.tag;
            }, this);
            node.parent = this.nod_time;
        }, this);
    }
    /**
     * 更新单次购入上限
     * 
     * @param {number} index 
     * @memberof Create
     */
    updateLimit(index: number) {
        if (index < 0) index = 0;
        if (index > 9) index = 9;
        this.lab_limit.string = (Number(this.lab_bet.string) * (index + 1)).toString();
        this.lab_limit.node.tag = index;
    }
    /**
     * 点击toggle按钮播放音效
     * 
     * @returns 
     * @memberof Create
     */
    click_toggle() {
        dd.mp_manager.playButton();
    }
    /**
     * 点击增加上限
     * 
     * @memberof Create
     */
    upLimit() {
        dd.mp_manager.playButton();
        let index = this.lab_limit.node.tag;
        if (index < 9) {
            index++;
            this.updateLimit(index);
        }
    }
    /**
     * 点击减小上限
     * 
     * @memberof Create
     */
    downLimit() {
        dd.mp_manager.playButton();
        let index = this.lab_limit.node.tag;
        if (index > 0) {
            index--;
            this.updateLimit(index);
        }
    }
    /**
     * 点击关闭按钮
     * 
     * @memberof Create
     */
    click_out() {
        dd.mp_manager.playButton();
        this.node.destroy();
    }
    /**
     * 点击创建房间
     * 
     * @memberof Create
     */
    click_create() {
        if (!dd.ui_manager.showLoading('正在创建房间,请稍后')) return;
        dd.mp_manager.playButton();
        let obj = {
            tableName: this.lab_name.string,
            small: this.selectChip.small,
            big: this.selectChip.big,
            minJoin: this.selectChip.join,
            vaildTime: this.selectTime,
            insurance: this.tog_safe.isChecked ? 1 : 0,
            straddle: this.tog_straddie.isChecked ? 1 : 0,
            buyMax: Number(this.lab_limit.string)
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_CREATE, msg, (flag: number, content?: any) => {
            if (flag === 0) {
                dd.gm_manager.setTableData(content as TableData, 1);
                cc.director.loadScene('GameScene', () => {
                    dd.ui_manager.showTip('创建房间成功');
                });
            } else if (flag === -1) {
                dd.ui_manager.showTip('创建房间消息发送超时');
            } else {
                dd.ui_manager.showTip(content);
            }
        });
    }

}
