
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class TaskCanvas extends cc.Component {
    /**
     * 当前进度
     * @type {cc.RichText}
     * @memberof TaskCanvas
     */
    @property(cc.RichText)
    lblCurPro: cc.RichText = null;
    /**
     * 任务进度
     * @type {cc.ProgressBar}
     * @memberof TaskCanvas
     */
    @property(cc.ProgressBar)
    task_pro: cc.ProgressBar = null;
    /**
     * 任务列表
     * @type {cc.ScrollView}
     * @memberof TaskCanvas
     */
    @property(cc.ScrollView)
    svNode_task: cc.ScrollView = null;

    @property(cc.Prefab)
    task_item: cc.Prefab = null;

    @property(cc.Prefab)
    task_get: cc.Prefab = null;
    onLoad() {
        this.sendGetTaskList();
    }
    /**
     * 获取列表
     * @memberof TaskCanvas
     */
    sendGetTaskList() {
        dd.ui_manager.showLoading();
        //获取任务表
        dd.ws_manager.sendMsg(dd.protocol.TASK_GET_DAYTASK_LIST, '', (flag: number, content?: any) => {
            if (flag === 0) {//成功
                this.initData(content as welfareVo);
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
            }
            dd.ui_manager.hideLoading();
            cc.log(content);
        });

    }
    /**
     * 数据
     * @param {TaskItemInner[]} data 
     * @memberof TaskTask
     */
    initData(data: welfareVo) {
        this.svNode_task.content.removeAllChildren();
        if (data && data.dayTaskList) {
            this.lblCurPro.string = data.dayLuckValue + '%';
            this.task_pro.progress = data.dayLuckValue / 100;
            this.task_pro.node.getComponent(cc.Slider).progress = data.dayLuckValue / 100;
            data.dayTaskList.forEach((taskData: TaskItemInner) => {
                let taskNode = cc.instantiate(this.task_item);
                let taskItemScript = taskNode.getComponent('Task_Item');
                taskItemScript.updateItem(taskData, this);
                taskNode.parent = this.svNode_task.content;
            });
        }
    }
    /**
     * 显示领取成功
     * @memberof TaskCanvas
     */
    showTaskReward(data: TaskItemInner) {
        let taskGet = cc.instantiate(this.task_get);
        let lblReward = taskGet.getChildByName('lblReward');
        if (lblReward) lblReward.getComponent(cc.Label).string = data.rewardDesc;
        taskGet.parent = this.node;
        taskGet.runAction(cc.sequence(cc.delayTime(1), cc.fadeOut(1), cc.callFunc(() => {
            taskGet.removeFromParent(true);
            taskGet.destroy();
        }, this)))
    }
    /**
     * 退出
     * @memberof TaskCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
