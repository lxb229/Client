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
    /**
     * 音频配置对象
     * 
     * @type {audioSetting}
     * @memberof MPManager
     */
    audioSetting: audioSetting = null;
    /**
     * 背景音乐播放的id
     * 
     * @private
     * @type {number}
     * @memberof MPManager
     */
    private backgroundID: number = null;

    /**
     * 播放音效的id(警告,按钮,弹出框)
     * 
     * @private
     * @type {number}
     * @memberof MPManager
     */
    private effectID: number = null;

    /**
     * 初始化音频管理
     * 
     * @returns 
     * @memberof MPManager
     */
    initMP() {
        this.initSetting();
        this.playBackGround();
    }
    /**
     * 加载音频文件
     * 
     * @private
     * @param {string} path 音频路径(不带扩展名)
     * @returns {Promise<string>} 返回音频可用播放的路径
     * @memberof MPManager
     */
    private loadFile(path: string): Promise<string> {
        return new Promise<string>((resolve, reject) => {
            cc.loader.loadRes(path, (error: Error, resource: any) => {
                if (error) {
                    cc.error(error.message || error);
                    reject(error.message || error);
                }
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
        let audioStr = db.getItem('audioSetting');
        if (audioStr) {
            this.audioSetting = JSON.parse(audioStr);
        } else {
            this.audioSetting = {
                isMusic: true,
                isEffect: true
            }
            db.setItem('audioSetting', JSON.stringify(this.audioSetting));
        }
    }

    /**
     * 保存音频配置
     * 
     * @memberof MPManager
     */
    saveMPSetting(): void {
        let db = cc.sys.localStorage;
        db.setItem('audioSetting', JSON.stringify(this.audioSetting));
    }



    /**
     * 播放背景音乐
     * 
     * @returns 
     * @memberof MPManager
     */
    async playBackGround() {
        if (this.backgroundID !== null || !this.audioSetting.isMusic) return;
        let path = await this.loadFile('Audio/music');
        this.backgroundID = cc.audioEngine.play(path, true, 1);
    }

    /**
     * 停止背景音乐
     * 
     * @memberof MPManager
     */
    stopBackGround(): void {
        if (this.backgroundID !== null) {
            cc.audioEngine.stop(this.backgroundID);
            this.backgroundID = null;
        }
    }

    /**
     * 停止正在播放的音效
     * 
     * @private
     * @memberof MPManager
     */
    private stopEffect() {
        if (this.effectID) {
            cc.audioEngine.stop(this.effectID);
            this.effectID = null;
        }
    }
    /**
     * 音效播放完毕的回调
     * 
     * @private
     * @memberof MPManager
     */
    private finish(callback?: Function) {
        cc.audioEngine.setFinishCallback(this.effectID, () => {
            this.effectID = null;
            if (callback) {
                callback();
            }
        });
    }
    /**
     * 播放按钮音
     * 
     * @memberof MPManager
     */
    async playButton() {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/click');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish();
        }
    }
    /**
     * 播放发牌音效
     * 
     * @memberof MPManager
     */
    async playFaPai() {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/fapai');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish();
        }
    }
    /**
     * 播放飞金币音效
     * 
     * @memberof MPManager
     */
    async playCoinMove() {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/coinmove');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish();
        }
    }
    /**
     * 播放胜利音效
     * 
     * @memberof MPManager
     */
    async playWin() {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/win');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish();
        }
    }
    /**
     * 播放失败音效
     * 
     * @memberof MPManager
     */
    async playLose() {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/lose');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish();
        }
    }
    /**
     * 播放游戏开始音效
     * 
     * @memberof MPManager
     */
    async playStart(callback?: Function) {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/start');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish(callback);
        }
    }
    /**
     * 播放倒计时音效
     * 
     * @memberof MPManager
     */
    async playTime() {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/time');
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish();
        }
    }
    /**
     * 播放炸金花的音效
     * 
     * @param {string} name 
     * @memberof MPManager
     */
    async playZJH(name: string, callback?: Function) {
        if (this.audioSetting.isEffect) {
            // this.stopEffect();
            let paht = await this.loadFile('Audio/zjh/' + name);
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish(callback);
        }
    }
    /**
     * 播放牛牛的音效
     * 
     * @param {string} name 
     * @memberof MPManager
     */
    async playNN(name: string, callback?: Function) {
        if (this.audioSetting.isEffect) {
            this.stopEffect();
            let paht = await this.loadFile('Audio/nn/' + name);
            this.effectID = cc.audioEngine.play(paht, false, 1);
            this.finish(callback);
        }
    }

    /**
     * 释放销毁
     * 
     * @memberof MPManager
     */
    destroySelf(): void {
        cc.audioEngine.uncacheAll();
        this.effectID = null;
        this.backgroundID = null;
        this.audioSetting = null;
    }
}