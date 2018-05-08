import * as dd from './../../Modules/ModuleManager';

const { ccclass, property } = cc._decorator;

@ccclass
export default class Task_Item extends cc.Component {
    /**
     * 任务描述
     * @type {cc.Label}
     * @memberof Task_Item
     */
    @property(cc.Label)
    lblTask: cc.Label = null;
    /**
     * 任务奖励
     * @type {cc.Label}
     * @memberof Task_Item
     */
    @property(cc.Label)
    lblReward: cc.Label = null;
    /**
     * 任务进度
     * @type {cc.Label}
     * @memberof Task_Item
     */
    @property(cc.Label)
    lblPro: cc.Label = null;
    /**
     * 任务状态
     * @type {cc.Label}
     * @memberof Task_Item
     */
    @property(cc.Label)
    lblState: cc.Label = null;
    /**
     * 获取按钮
     * @type {cc.Node}
     * @memberof Task_Item
     */
    @property(cc.Node)
    btn_get: cc.Node = null;

    _target: any = null;
    _itemData: TaskItemInner = null;
    onLoad() {

    }

    updateItem(data: TaskItemInner, target: any) {
        this._itemData = data;
        this._target = target;
        this.lblTask.string = data.finshDesc;
        this.lblReward.string = data.rewardDesc;
        this.lblPro.string = data.complete + '/' + data.total;
        this.lblState.node.active = data.state === 1 ? false : true;
        this.btn_get.active = data.state === 1 ? true : false;
        this.lblState.string = data.state === 2 ? '未达成' : '已领取';
    }

    /**
     * 获取奖励按钮
     * @memberof Task_Item
     */
    click_btn_get() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            let obj = { 'taskId': this._itemData.taskId };
            let msg = JSON.stringify(obj);
            //获取红包福利列表
            dd.ws_manager.sendMsg(dd.protocol.TASK_GET_TASK_REWARE, msg, (flag: number, content?: any) => {
                dd.ui_manager.hideLoading();
                if (flag === 0) {//成功
                    let data = content as RewardItems;
                    this._target.sendGetTaskList();
                    this._target.showTaskReward(this._itemData);
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                }
                cc.log(content);
            });
        }
    }
}
