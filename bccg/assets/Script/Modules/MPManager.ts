/**
 * 音频管理类
 * 
 * @export
 * @class MPManager
 */
export default class MPManager {
    private static _instance: MPManager = null;
    private constructor() { }
    /**
     * 获取WSManager单例对象
     * 
     * @static
     * @returns {MPManager} 
     * @memberof MPManager
     */
    static getInstance(): MPManager {
        if (MPManager._instance === null) {
            MPManager._instance = new MPManager();
        }
        return MPManager._instance;
    }

    private url_allin: string = null;
    private url_bet: string = null;
    private url_call: string = null;
    private url_check: string = null;
    private url_deal: string = null;
    private url_flop: string = null;
    private url_fold: string = null;
    private url_join: string = null;
    private url_msg: string = null;
    private url_over: string = null;
    private url_raise: string = null;
    private url_start: string = null;
    private url_straddle: string = null;
    private url_time: string = null;
    private url_button: string = null;

    /**
     * 音效开关
     * 
     * @private
     * @type {boolean}
     * @memberof MPManager
     */
    sw: boolean = true;

    /**
     * 初始化音频管理
     * 
     * @returns {Promise<void>} 
     * @memberof MPManager
     */
    async initMP(): Promise<void> {
        this.initSetting();
        this.url_allin = await this.loadFile('allin');
        this.url_bet = await this.loadFile('bet');
        this.url_call = await this.loadFile('call');
        this.url_check = await this.loadFile('check');
        this.url_deal = await this.loadFile('deal');
        this.url_flop = await this.loadFile('flop');
        this.url_fold = await this.loadFile('fold');
        this.url_join = await this.loadFile('join');
        this.url_msg = await this.loadFile('msg');
        this.url_over = await this.loadFile('over');
        this.url_raise = await this.loadFile('raise');
        this.url_start = await this.loadFile('start');
        this.url_straddle = await this.loadFile('straddle');
        this.url_time = await this.loadFile('time');
        this.url_button = await this.loadFile('button');
    }

    /**
     * 加载音频文件
     * 
     * @private
     * @param {string} name 音频名称(不带扩展名)
     * @returns {Promise<string>} 返回音频可用播放的路径
     * @memberof MPManager
     */
    private loadFile(name: string): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            let path = 'Audio/' + name;
            cc.loader.loadRes(path, (error: Error, resource: string) => {
                if (error) {
                    cc.error(error.message || error);
                    reject(error.message || error);
                }
                cc.loader.setAutoRelease(resource, false);
                resolve(resource);
            });
        });
    }

    /**
     * 初始化音频配置
     * 
     * @memberof MPManager
     */
    private initSetting(): void {
        let db = cc.sys.localStorage;
        let sw = db.getItem('sw');
        if (sw) {
            this.sw = JSON.parse(sw).sw;
        } else {
            db.setItem('sw', JSON.stringify({ sw: this.sw }));
        }
    }
    /**
     * 播放allin音效
     * 
     * @memberof MPManager
     */
    playAllin() {
        if (this.sw && this.url_allin) {
            cc.audioEngine.play(this.url_allin, false, 1);
        }
    }
    /**
     * 播放提示当前表态玩家的音效
     * 
     * @memberof MPManager
     */
    playBet() {
        if (this.sw && this.url_bet) {
            cc.audioEngine.play(this.url_bet, false, 1);
        }
    }
    /**
     * 播放跟住音效
     * 
     * @memberof MPManager
     */
    playCall() {
        if (this.sw && this.url_call) {
            cc.audioEngine.play(this.url_call, false, 1);
        }
    }
    /**
     * 播放看牌(过牌)音效
     * 
     * @memberof MPManager
     */
    playCheck() {
        if (this.sw && this.url_check) {
            cc.audioEngine.play(this.url_check, false, 1);
        }
    }
    /**
     * 播放发牌音效
     * 
     * @memberof MPManager
     */
    playDeal() {
        if (this.sw && this.url_deal) {
            cc.audioEngine.play(this.url_deal, false, 1);
        }
    }
    /**
     * 播放翻盘音效
     * 
     * @memberof MPManager
     */
    playFlop() {
        if (this.sw && this.url_flop) {
            cc.audioEngine.play(this.url_flop, false, 1);
        }
    }
    /**
     * 播放弃牌音效
     * 
     * @memberof MPManager
     */
    playFold() {
        if (this.sw && this.url_fold) {
            cc.audioEngine.play(this.url_fold, false, 1);
        }
    }
    /**
     * 播放入座音效
     * 
     * @memberof MPManager
     */
    playJoin() {
        if (this.sw && this.url_join) {
            cc.audioEngine.play(this.url_join, false, 1);
        }
    }
    /**
     * 播放新消息音效
     * 
     * @memberof MPManager
     */
    playMsg() {
        if (this.sw && this.url_msg) {
            cc.audioEngine.play(this.url_msg, false, 1);
        }
    }
    /**
     * 播放游戏结束的音效
     * 
     * @memberof MPManager
     */
    playOver() {
        if (this.sw && this.url_over) {
            cc.audioEngine.play(this.url_over, false, 1);
        }
    }
    /**
     * 播放加注音效
     * @memberof MPManager
     */
    playRaise() {
        if (this.sw && this.url_raise) {
            cc.audioEngine.play(this.url_raise, false, 1);
        }
    }
    /**
     * 播放游戏开始音效
     * 
     * @memberof MPManager
     */
    playStart() {
        if (this.sw && this.url_start) {
            cc.audioEngine.play(this.url_start, false, 1);
        }
    }
    /**
     * 播放闭眼盲音效
     * 
     * @memberof MPManager
     */
    playStraddle() {
        if (this.sw && this.url_straddle) {
            cc.audioEngine.play(this.url_straddle, false, 1);
        }
    }
    /**
     * 播放时间到的音效
     * 
     * @memberof MPManager
     */
    playTime() {
        if (this.sw && this.url_time) {
            cc.audioEngine.play(this.url_time, false, 1);
        }
    }
    /**
     * 播放按钮音效
     * 
     * @memberof MPManager
     */
    playButton() {
        if (this.sw && this.url_button) {
            cc.audioEngine.play(this.url_button, false, 1);
        }
    }
    /**
     * 释放销毁
     * 
     * @memberof MPManager
     */
    destroySelf(): void {
        cc.loader.release(this.url_allin);
        cc.loader.release(this.url_bet);
        cc.loader.release(this.url_call);
        cc.loader.release(this.url_check);
        cc.loader.release(this.url_deal);
        cc.loader.release(this.url_flop);
        cc.loader.release(this.url_fold);
        cc.loader.release(this.url_join);
        cc.loader.release(this.url_msg);
        cc.loader.release(this.url_over);
        cc.loader.release(this.url_raise);
        cc.loader.release(this.url_start);
        cc.loader.release(this.url_straddle);
        cc.loader.release(this.url_time);
        cc.loader.release(this.url_button);
        cc.audioEngine.uncacheAll();
        this.url_allin = null;
        this.url_bet = null;
        this.url_call = null;
        this.url_check = null;
        this.url_deal = null;
        this.url_flop = null;
        this.url_fold = null;
        this.url_join = null;
        this.url_msg = null;
        this.url_over = null;
        this.url_raise = null;
        this.url_start = null;
        this.url_straddle = null;
        this.url_time = null;
        this.url_button = null;
    }
}