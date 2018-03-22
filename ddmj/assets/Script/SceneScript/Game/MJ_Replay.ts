
const { ccclass, property } = cc._decorator;

import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class MJ_Replay extends cc.Component {

    @property(cc.Node)
    btn_pause: cc.Node = null;

    @property(cc.Node)
    btn_play: cc.Node = null;

    /**
     * 帧时间
     * 
     * @type {number}
     * @memberof MJ_Replay
     */
    _frameTime: number = 0;

    /**
     * 数据索引
     * 
     * @type {number}
     * @memberof MJ_Replay
     */
    _replayIndex: number = 0;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
        }, this);

        //如果是重播，就先暂停所有的刷新
        if (dd.gm_manager.replayMJ === 1) {
            dd.gm_manager.isReplayPause = true;
            this.btn_pause.active = false;
            this.btn_play.active = true;
            this._replayIndex = 0;
            this.showReplayInfo();
        } else {
            dd.gm_manager.isReplayPause = false;
        }
    }

    update(dt) {
        if (!dd.gm_manager.isReplayPause) {
            if (this._frameTime >= 0) {
                this._frameTime -= dt;
                if (this._frameTime <= 0) {
                    this._replayIndex++;
                    this.showReplayInfo();
                }
            }
        }
    }

    /**
     * 设置播放游戏记录的数据
     * 
     * @memberof MJ_Replay
     */
    showReplayInfo() {
        if (this._replayIndex > dd.gm_manager.replayDataList.length - 1) {
            dd.gm_manager.isReplayPause = true;
            this.btn_pause.active = false;
            this.btn_play.active = true;
            this._replayIndex = dd.gm_manager.replayDataList.length;

            dd.ui_manager.showAlert('游戏记录播放完毕,是否重新播放？'
                , '温馨提示',
                {
                    lbl_name: '确定',
                    callback: () => {
                        //移除结算面板
                        if (dd.gm_manager._gmScript._game_over && dd.gm_manager._gmScript._game_over.isValid) {
                            dd.gm_manager._gmScript._game_over.removeFromParent(true);
                            dd.gm_manager._gmScript._game_over.destroy();
                        }
                        this._replayIndex = 0;
                        this.showReplayInfo();
                    }
                },
                {
                    lbl_name: '退出',
                    callback: () => {
                        if (dd.ui_manager.showLoading()) {
                            cc.director.loadScene('HomeScene', () => {
                                let canvasScript = dd.ui_manager.getCanvasNode().getComponent('HomeCanvas');
                                if (canvasScript) {
                                    canvasScript.showRecord(dd.gm_manager.replayRecordId);
                                }
                                dd.gm_manager.destroySelf();
                            });
                        }
                    }
                }
                , 1);
            return;
        }
        let nowReplay = dd.gm_manager.replayDataList[this._replayIndex];
        //刷新下一局的游戏数据
        dd.gm_manager.setTableData(nowReplay.frameData as MJGameData, true, 1);
        if (this._replayIndex + 1 < dd.gm_manager.replayDataList.length) {
            let nextReplay = dd.gm_manager.replayDataList[this._replayIndex + 1];
            let ft = dd.gm_manager.getDiffTime(nowReplay.startTime, nextReplay.startTime);
            this._frameTime = (Number(nextReplay.startTime) - Number(nowReplay.startTime)) / 1000;
        }
    }

    /**
     * 回放按钮点击事件
     * 
     * @param {any} event 
     * @param {string} type 
     * @memberof MJ_Table
     */
    click_btn_replay(event, type: string) {
        dd.mp_manager.playButton();
        switch (type) {
            case '0'://退出
                dd.ui_manager.showAlert('您确定退出播放战绩吗？'
                    , '温馨提示',
                    {
                        lbl_name: '确定',
                        callback: () => {
                            if (dd.ui_manager.showLoading()) {
                                cc.director.loadScene('HomeScene', () => {
                                    let canvasScript = dd.ui_manager.getCanvasNode().getComponent('HomeCanvas');
                                    if (canvasScript) {
                                        canvasScript.showRecord(dd.gm_manager.replayRecordId);
                                    }
                                    dd.gm_manager.destroySelf();
                                });
                            }
                        }
                    },
                    {
                        lbl_name: '取消',
                        callback: () => {
                        }
                    }
                    , 1);

                break;
            case '1'://播放
                dd.gm_manager.isReplayPause = false;
                this.btn_pause.active = true;
                this.btn_play.active = false;
                break;
            case '2'://暂停
                dd.gm_manager.isReplayPause = true;
                this.btn_pause.active = false;
                this.btn_play.active = true;
                break;
            case '3'://进
                this._replayIndex++;
                this.showReplayInfo();
                dd.gm_manager.isReplayPause = true;
                this.btn_pause.active = false;
                this.btn_play.active = true;
                break;
            case '4'://退
                if (this._replayIndex > 0) {
                    this._replayIndex--;
                }
                this.showReplayInfo();
                dd.gm_manager.isReplayPause = true;
                this.btn_pause.active = false;
                this.btn_play.active = true;
                break;
            default:
                break;
        }
    }
}
