
import * as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;
import MJCanvas from './MJCanvas';
import { MJ_Act_State } from '../../Modules/Protocol';

@ccclass
export default class Game_Disband extends cc.Component {

    /**
     * 解散房间的描述
     * 
     * @type {cc.Label}
     * @memberof Game_Disband
     */
    @property(cc.Label)
    lblDes: cc.Label = null;

    /**
     * 解散房间的提示
     * 
     * @type {cc.Label}
     * @memberof Game_Disband
     */
    @property(cc.Label)
    lblTip: cc.Label = null;
    /**
     * 其他玩家节点列表
     * @type {cc.Node[]}
     * @memberof Game_Disband
     */
    @property(cc.Node)
    playerNodeList: cc.Node[] = [];
    /**
     * 玩家名字列表
     * 
     * @type {cc.Label[]}
     * @memberof Game_Disband
     */
    @property(cc.Label)
    lblNameList: cc.Label[] = [];

    /**
     * 头像列表
     * 
     * @type {cc.Sprite[]}
     * @memberof Game_Disband
     */
    @property(cc.Sprite)
    headImgList: cc.Sprite[] = [];
    /**
     * 玩家的表态列表
     * 
     * @type {[cc.Sprite]}
     * @memberof Game_Disband
     */
    @property([cc.Sprite])
    stateList: cc.Sprite[] = [];

    /**
     * 玩家的表态图片列表
     * 
     * @type {[cc.SpriteFrame]} 0=同意 1=拒绝 2=等待
     * @memberof Game_Disband
     */
    @property([cc.SpriteFrame])
    stateImgList: cc.SpriteFrame[] = [];
    /**
     * 解散房间倒计时
     * 
     * @type {cc.Label}
     * @memberof Game_Disband
     */
    @property(cc.Label)
    lblTime: cc.Label = null;

    /**
     * 确定按钮
     * 
     * @type {cc.Node}
     * @memberof Game_Disband
     */
    @property(cc.Node)
    btn_ok: cc.Node = null;

    /**
     * 
     * 拒绝按钮
     * @type {cc.Node}
     * @memberof Game_Disband
     */
    @property(cc.Node)
    btn_refuse: cc.Node = null;  //拒绝

    /**
     * 倒计时的cd
     * 
     * @type {number}
     * @memberof Game_Disband
     */
    _cd: number = 1;
    /**
     * 倒计时
     * 
     * @type {number}
     * @memberof Game_Disband
     */
    _downTime: number = 3;

    /**
     * canvas脚本
     * 
     * @memberof MJ_Table
     */
    _canvasTarget: MJCanvas = null;

    /**
     * 房间信息获取时间
     * 
     * @type {number}
     * @memberof Game_Disband
     */
    _rTime: number = 10;

    onLoad() {
        this._canvasTarget = dd.ui_manager.getCanvasNode().getComponent('MJCanvas');
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);
    }

    initData() {
        let creatorSeat = dd.gm_manager.getSeatById(dd.gm_manager.mjGameData.tableBaseVo.destoryQuestPlayer);
        this.lblDes.string = '玩家【' + creatorSeat.nick + '】申请解散房间，是否同意？';
        this._downTime = dd.gm_manager.getDiffTime(dd.gm_manager.mjGameData.tableBaseVo.svrTime, dd.gm_manager.mjGameData.tableBaseVo.actTime);
        this.lblTime.string = '倒计时' + this._downTime + 's';

        let btSeats: SeatVo[] = [];
        let seats = dd.gm_manager.mjGameData.seats;

        for (var i = 0; i < seats.length; i++) {
            if (seats[i] && seats[i].accountId !== '0') {
                //如果这个玩家不是房主
                if (seats[i].accountId !== dd.gm_manager.mjGameData.tableBaseVo.destoryQuestPlayer) {
                    btSeats.push(seats[i]);
                }
                //如果是自己，查看自己是否表态了，要不要显示表态那妞
                if (seats[i].accountId === dd.ud_manager.mineData.accountId) {
                    if (seats[i].btState !== MJ_Act_State.ACT_STATE_WAIT) {
                        this.btn_ok.active = false;
                        this.btn_refuse.active = false;
                    } else {
                        this.btn_ok.active = true;
                        this.btn_refuse.active = true;
                    }
                }
            }
        }

        for (let i = 0; i < this.playerNodeList.length; i++) {
            let playerNode = this.playerNodeList[i];
            if (i < btSeats.length) {
                playerNode.active = true;
                let state = this.stateList[i];
                state.node.stopAllActions();
                state.node.rotation = 0;
                //如果座位状态已经表态了
                if (btSeats[i].btState !== MJ_Act_State.ACT_STATE_WAIT) {
                    state.spriteFrame = btSeats[i].btState === 1 ? this.stateImgList[0] : this.stateImgList[1];
                } else {
                    state.spriteFrame = this.stateImgList[2];
                    let action = cc.repeatForever(cc.sequence(cc.rotateTo(1, 180), cc.rotateTo(1, 360)));
                    state.node.runAction(action);
                }
                this.lblNameList[i].string = btSeats[i].nick;
                this.showHead(i, btSeats[i].headImg);
            } else {
                playerNode.active = false;
            }
        }
    }

    async showHead(index: number, sf: string) {
        //刷新玩家信息
        let headSF = null;
        try {
            headSF = await dd.img_manager.loadURLImage(sf);
        } catch (error) {
            cc.log('获取头像错误');
        }
        if (this.headImgList[index]) {
            this.headImgList[index].spriteFrame = headSF;
        }
    }
    /**
     * 点击确定
     * 
     * @memberof Game_Disband
     */
    click_btn_ok() {
        dd.mp_manager.playButton();
        this._canvasTarget.sendDisband(2);
    }

    /**
     * 点击拒绝
     * 
     * @memberof Game_Disband
     */
    click_btn_refuse() {
        dd.mp_manager.playButton();
        this._canvasTarget.sendDisband(1);
    }

    /**
     * 获取游戏房间信息，这里用于判断玩家在 房主放出解散房间的请求后，玩家是否已经退出房间，还是数据丢包
     * 
     * @memberof Game_Disband
     */
    sendGetGameInfo() {
        let obj = { 'tableId': Number(dd.gm_manager.mjGameData.tableBaseVo.tableId) };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.MAJIANG_ROOM_JOIN, msg, (flag: number, content?: any) => {
            cc.log('flag=' + flag + ';content=' + content);
            if (flag === 0) {//成功
            } else if (flag === -1) {//超时
            } else {//失败,content是一个字符串
                this._canvasTarget.quitGame();
            }
        });
    }
    /**
     * 界面刷新
     * 
     * @param {number} dt 
     * @memberof Game_Disband
     */
    update(dt: number) {
        if (this._downTime > 0) {
            this._cd -= dt;
            if (this._cd <= 0) {
                this._cd = 1;
                this._downTime--;
                this.lblTime.string = '倒计时' + this._downTime + 's';
                if (this._downTime <= 0) {
                    cc.log('结束倒计时');
                }
            }
        }

        if (!this.btn_ok.active) {
            this._rTime -= dt;
            if (this._rTime < 0) {
                this._rTime = 10;
                this.sendGetGameInfo();
            }
        }
    }
}
