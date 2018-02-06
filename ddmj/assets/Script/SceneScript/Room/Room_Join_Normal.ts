const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Room_Join_Normal extends cc.Component {

    /**
     * 显示房间号Id的数字列表
     * 
     * @type {cc.Label[]}
     * @memberof Room_Join_Normal
     */
    @property([cc.Label])
    lblInputList: cc.Label[] = [];

    /**
     * 当前输入的数字索引
     * 
     * @type {number}
     * @memberof Room_Join_Normal
     */
    _curIndex: number = 0;
    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        this.init();
        dd.ui_manager.hideLoading();
    }

    /**
     * 初始化数据
     * 
     * @memberof Room_Join_Normal
     */
    init(): void {
        this._curIndex = 0;
        for (var i = 0; i < this.lblInputList.length; i++) {
            this.lblInputList[i].string = '';
        }
    }


    /**
     * 发送加入房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendJoinRoom() {
        let tableId = '';
        for (var i = 0; i < this.lblInputList.length; i++) {
            tableId += this.lblInputList[i].string;
        }
        if (dd.ui_manager.showLoading()) {
            let obj = { 'tableId': Number(tableId) };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    dd.gm_manager.mjGameData = content as MJGameData;
                    dd.gm_manager.replayMJ = 0;
                    switch (dd.gm_manager.mjGameData.tableBaseVo.cfgId) {
                        case 1:
                            cc.director.loadScene('MJScene');
                            break;
                        case 2:
                        case 3:
                            cc.director.loadScene('SRMJScene');
                            break;
                        case 4:
                            cc.director.loadScene('LRMJScene');
                            break;
                        default:
                    }
                } else if (flag === -1) {//超时
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
                cc.log(content);
            });
        }
    }

    /**
     * 输入数字的点击事件
     * 
     * @param {cc.Event.EventTouch} event 
     * @param {string} num 
     * @memberof Room_Join_Normal
     */
    click_btn_input(event: cc.Event.EventTouch, num: string): void {
        dd.mp_manager.playButton();
        switch (num) {
            case '10'://重输
                this.init();
                break;
            case '11'://删除
                this.deleteLastInput();
                break;
            default:
                this.showInputNum(num);
                break;
        }
    }

    /**
     *删除上一个输入的数字 
     * 
     * @memberof Room_Join_Normal
     */
    deleteLastInput(): void {
        if (this._curIndex <= 0) return;
        this.lblInputList[this._curIndex - 1].string = '';
        if (this._curIndex > 0)
            this._curIndex--;
    }

    /**
     * 显示当前输入的数字
     * 
     * @param {string} num 
     * @memberof Room_Join_Normal
     */
    showInputNum(num: string): void {
        if (this._curIndex < this.lblInputList.length) {
            this.lblInputList[this._curIndex].string = num;
            if (this._curIndex < this.lblInputList.length) {
                this._curIndex++;
                if (this._curIndex === this.lblInputList.length) {
                    cc.log('输入完毕');
                    this.sendJoinRoom();
                }
            }
        }
    }

    /**
     * 退出加入房间界面
     * 
     * @memberof Room_Join_Normal
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        dd.ui_manager.isShowPopup = true;
        this.node.removeFromParent(true);
        this.node.destroy();
    }
}
