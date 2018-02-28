import { mp_manager } from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_TimeDown extends cc.Component {
    /**
     * 倒计时图片
     * @type {cc.Sprite}
     * @memberof Game_TimeDown
     */
    @property(cc.Sprite)
    img_td: cc.Sprite = null;
    /**
     * 倒计时时间总数
     * @memberof Game_TimeDown
     */
    _timeCount = 0;
    /**
     * 当前时间
     * @memberof Game_TimeDown
     */
    _curTime = 0;
    /**
     * 是否播放音效
     * @memberof Game_TimeDown
     */
    _isPlayEfc = false;
    /**
     * 回调函数
     * @memberof Game_TimeDown
     */
    _cb = null;
    /**
     * 播放音效的时间
     * @type {number}
     * @memberof Game_TimeDown
     */
    _playTime: number = 0;
    onLoad() {

    }

    /**
     * 初始化数据
     * @param {number} curTime 当前时间
     * @param {number} timeCount 总共时间
     * @param {boolean} isPlayEfc 是否播放音效
     * @param {cc.Node} parentNode 父节点
     * @param {any} cb            回调函数
     * @memberof Game_TimeDown
     */
    initData(curTime: number, timeCount: number, isPlayEfc: boolean, parentNode: cc.Node, cb?: any) {
        this._timeCount = timeCount;
        this._curTime = curTime;
        this._isPlayEfc = isPlayEfc;
        this.node.parent = parentNode;
        this.img_td.fillRange = 1;
        this._cb = cb;
    }
    /**
     * 播放anim动画，
     * @param {any} startTime 开始位子
     * @param {any} pos       坐标点
     * @param {any} parentNode 父节点
     * @memberof Game_TimeDown
     */
    initDataWithAnim(startTime: number, parentNode: cc.Node) {
        this.node.parent = parentNode;
        let anim = this.node.getComponent(cc.Animation);
        anim.play("game_djsAction", startTime);
    }

    update(dt) {
        if (this._curTime <= this._timeCount) {
            this._curTime += dt;
            let pro = this._curTime / this._timeCount;
            if (pro < 0) {
                pro = 0;
            } else if (pro > 1) {
                pro = 1;
            } else {
                pro = pro;
            }
            this.img_td.fillRange = 1 - pro;

            if (pro < 0.33) {
                this.img_td.node.color = new cc.Color(31, 255, 0);
            } else if (pro > 0.33 && pro < 0.66) {
                this.img_td.node.color = new cc.Color(50, 160, 255);
            } else {
                this.img_td.node.color = new cc.Color(255, 43, 43);
            }
            if (this._isPlayEfc) {
                if (pro >= 0.5) {
                    this._playTime -= dt;
                    if (this._playTime <= 0) {
                        this._playTime = 0.8;
                        mp_manager.playTime();
                    }
                }
            }
        } else {
            this.img_td.fillRange = 0;
            if (this._cb) {
                this._cb();
                //执行一次就清除，防止循环请求
                this._cb = null;
            }
        }
    }
}
