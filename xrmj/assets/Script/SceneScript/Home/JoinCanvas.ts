
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class JoinCanvas extends cc.Component {

    /**
     * 加入密码房间的预设
     * @type {cc.Prefab}
     * @memberof CreateCanvas
     */
    @property(cc.Prefab)
    join_pwd_prefab: cc.Prefab = null;

    @property([cc.Label])
    lbl_id_list: cc.Label[] = [];
    /**
     * 当前输入第几个id
     * @type {number}
     * @memberof JoinCanvas
     */
    _curInput: number = 0;

    _join_pwd: cc.Node = null;
    onLoad() {
        this.lbl_id_list.forEach((lbl_id: cc.Label) => {
            lbl_id.string = '';
        });
    }

    /**
     * 获取房间是否有密码
     * @memberof JoinCanvas
     */
    sendGetRoomPwd() {
        let tableId = this.getInputTableId();
        if (dd.ui_manager.showLoading()) {
            let obj = { 'tableId': tableId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_PASSWORD_STATE, msg, (flag: number, content?: any) => {
                if (flag === 0) {//成功
                    if (content.password === 0) {//无密码
                        this.sendJoinRoom(content.tableId);
                    } else {
                        //显示输入密码界面
                        this.showJoinPwd(content.tableId);
                        dd.ui_manager.hideLoading();
                    }
                } else if (flag === -1) {//超时
                    dd.ui_manager.hideLoading();
                } else {//失败,content是一个字符串
                    dd.ui_manager.showAlert(content, '温馨提示');
                    dd.ui_manager.hideLoading();
                }
                cc.log(content);
            });
        }
    }
    /**
     * 显示加入房间输入密码界面
     * @param {string} tableId 
     * @memberof JoinCanvas
     */
    showJoinPwd(tableId: string) {
        if (!this._join_pwd || !this._join_pwd.isValid) {
            this._join_pwd = cc.instantiate(this.join_pwd_prefab);
            let jPwdScript = this._join_pwd.getComponent('Room_Join_Pwd');
            jPwdScript.initData(tableId, this);
            this._join_pwd.parent = dd.ui_manager.getRootNode();
        }
    }
    /**
     * 发送加入房间的数据
     * 
     * @memberof Room_Join_Normal
     */
    sendJoinRoom(tableId: number) {
        let obj = { 'tableId': tableId, 'password': '0' };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
                cc.log('加入成功');
                dd.gm_manager.turnToGameScene(content as MJGameData, 0);
            } else if (flag === -1) {//超时
                dd.ui_manager.hideLoading();
            } else {//失败,content是一个字符串
                dd.ui_manager.showAlert(content, '温馨提示');
                dd.ui_manager.hideLoading();
            }
            cc.log(content);
        });
    }

    /**
     * 获取输入的tableId
     * @returns 
     * @memberof JoinCanvas
     */
    getInputTableId() {
        let tableId = '';
        for (var i = 0; i < this.lbl_id_list.length; i++) {
            tableId += this.lbl_id_list[i].string;
        }
        return tableId;
    }
    /**
     * 按钮的点击事件
     * @memberof JoinCanvas
     */
    click_btn_number(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '10'://回删
                if (this._curInput > 0) {
                    this._curInput--;
                    this.lbl_id_list[this._curInput].string = '';
                }
                break;
            case '11'://清空
                this.lbl_id_list.forEach((lbl_id: cc.Label) => {
                    lbl_id.string = '';
                });
                this._curInput = 0;
                break;
            default://输入
                if (this._curInput < this.lbl_id_list.length) {
                    this.lbl_id_list[this._curInput].string = type;
                    this._curInput++;
                    if (this._curInput === this.lbl_id_list.length) {
                        cc.log('输入完成');
                        this.sendGetRoomPwd();
                    }
                }
                break;
        }
    }
    /**
     * 退出
     * @memberof JoinCanvas
     */
    click_btn_out() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading()) {
            cc.director.loadScene('HomeScene');
        }
    }
}
