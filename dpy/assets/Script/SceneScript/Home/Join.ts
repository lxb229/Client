const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Join extends cc.Component {
    /**
     * 展示点击数字的标签数组
     * 
     * @type {cc.Label[]}
     * @memberof Join
     */
    nums: cc.Label[] = [];
    /**
     * 当前输入的数字对应的下标
     * 
     * @type {number}
     * @memberof Join
     */
    currentIndex: number = 0;

    onLoad() {
        this.nums.length = 0;
        this.currentIndex = 0;
        cc.find('layout', this.node).children.forEach((node: cc.Node) => {
            let num = cc.find('num', node).getComponent(cc.Label);
            this.nums.push(num);
        }, this);
        this.nums.sort((a: cc.Label, b: cc.Label) => {
            return Number(a.node.parent.name) - Number(b.node.parent.name);
        });
        dd.ui_manager.hideLoading();
    }
    /**
     * 点击关闭按钮
     * 
     * @memberof Join
     */
    click_out() {
        dd.mp_manager.playButton();
        this.node.destroy();
    }
    /**
     * 点击按钮
     * 
     * @param {cc.Event.EventTouch} event 
     * @param {string} [data] 
     * @memberof Join
     */
    click_btn(event: cc.Event.EventTouch, data: string) {
        dd.mp_manager.playButton();
        let index = Number(data);
        if (index === 10) {//删除
            if (this.currentIndex > 0) {
                this.currentIndex--;
                this.nums[this.currentIndex].string = '';
            } else {
                this.nums[0].string = '';
            }
        } else if (index === 11) {//清空
            this.nums.forEach((label: cc.Label) => {
                label.string = '';
            }, this);
            this.currentIndex = 0;
        } else {//0-9的数字
            if (this.currentIndex < this.nums.length) {
                this.nums[this.currentIndex].string = data;
                this.currentIndex++;
                if (this.currentIndex === this.nums.length) {
                    if (!dd.ui_manager.showLoading('正在加入房间,请稍后')) return;
                    let obj = { tableId: this.getNumber() };
                    let msg = JSON.stringify(obj);
                    dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_JOIN, msg, (flag: number, content?: any) => {
                        if (flag === 0) {
                            dd.gm_manager.setTableData(content as TableData, 1);
                            cc.director.loadScene('GameScene', () => {
                                dd.ui_manager.showTip('加入房间成功');
                            });
                        } else if (flag === -1) {
                            dd.ui_manager.showTip('加入房间消息发送超时');
                        } else {
                            dd.ui_manager.showTip(content);
                        }
                    });
                }
            }
        }
    }
    /**
     * 获取所有标签所组成的数字
     * 
     * @returns 
     * @memberof Join
     */
    getNumber() {
        let numStr = '';
        for (let i = 0; i < this.nums.length; i++) {
            numStr += this.nums[i].string;
        }
        return Number(numStr);
    }
}
