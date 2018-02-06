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
     * 快速发言集合
     * 
     * @type {quicklySpeak[]}
     * @memberof MPManager
     */
    quicklyList: quicklySpeak[] = [];
    /**
     * 背景音乐路径
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private backgroundPath: string = null;
    /**
     * 按钮音路径
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private buttonPath: string = null;
    /**
     * 警告音路径
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private warnPath: string = null;
    /**
     * 弹出音路径
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private alertPath: string = null;
    /**
     * 选中牌音效
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private selectPath: string = null;
    /**
     * 出牌音效
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private outPath: string = null;
    /**
     * 结算音效
     * 
     * @private
     * @type {string}
     * @memberof MPManager
     */
    private overPath: string = null;
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
     * 游戏中加载过的音效对象
     * 
     * @private
     * @type {gameAudioURL[]}
     * @memberof MPManager
     */
    private gameAudios: gameAudioURL[] = [];

    /**
     * 初始化音频管理
     * 
     * @returns {Promise<void>} 
     * @memberof MPManager
     */
    async initMP(): Promise<void> {
        this.initSetting();
        this.initQuickly();
        await this.initBackGround();
    }
    /**
     * 初始化背景音乐
     * 
     * @returns {Promise<void>} 
     * @memberof MPManager
     */
    private async initBackGround(): Promise<void> {
        this.backgroundPath = await this.loadFile('Audio/background');
        this.buttonPath = await this.loadFile('Audio/button');
        this.warnPath = await this.loadFile('Audio/warn');
        this.alertPath = await this.loadFile('Audio/alert');
        this.selectPath = await this.loadFile('Audio/select');
        this.outPath = await this.loadFile('Audio/out');
        this.overPath = await this.loadFile('Audio/over');
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
                language: 1,
                isMusic: true,
                isEffect: true,
                isSound: true
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
     * 初始化快速发言
     * 
     * @memberof MPManager
     */
    private initQuickly(): void {
        let quickly1: quicklySpeak = { id: 1, msg: '不好意思，我要离开一会' };
        let quickly2: quicklySpeak = { id: 2, msg: '不要走，决战到天亮' };
        let quickly3: quicklySpeak = { id: 3, msg: '打一个来碰噻' };
        let quickly4: quicklySpeak = { id: 4, msg: '大家好很高兴见到各位' };
        let quickly5: quicklySpeak = { id: 5, msg: '哈哈，上碰下自摸' };
        let quickly6: quicklySpeak = { id: 6, msg: '呵呵' };
        let quickly7: quicklySpeak = { id: 7, msg: '和你合作真是太愉快了' };
        let quickly8: quicklySpeak = { id: 8, msg: '快点吧，我等到花儿都谢了' };
        let quickly9: quicklySpeak = { id: 9, msg: '你的牌打得太好了' };
        let quickly10: quicklySpeak = { id: 10, msg: '下次再玩吧，我要走了' };
        let quickly11: quicklySpeak = { id: 11, msg: '又断线了，网络怎么这么差啊' };
        this.quicklyList = [quickly1, quickly2, quickly3, quickly4, quickly5, quickly6, quickly7, quickly8, quickly9, quickly10, quickly11];
    }
    /**
     * 播放背景音乐
     * 
     * @returns 
     * @memberof MPManager
     */
    playBackGround(): void {
        if (this.backgroundID !== null || this.backgroundPath === null || !this.audioSetting.isMusic) return;
        this.backgroundID = cc.audioEngine.play(this.backgroundPath, true, 0.5);
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
    private finish() {
        cc.audioEngine.setFinishCallback(this.effectID, () => {
            this.effectID = null;
        });
    }
    /**
     * 播放按钮音
     * 
     * @memberof MPManager
     */
    playButton() {
        if (this.audioSetting.isEffect && this.buttonPath) {
            this.stopEffect();
            this.effectID = cc.audioEngine.play(this.buttonPath, false, 1);
            this.finish();
        }
    }
    /**
     * 播放警告音
     * 
     * @memberof MPManager
     */
    playWarn() {
        if (this.audioSetting.isEffect && this.warnPath) {
            this.stopEffect();
            this.effectID = cc.audioEngine.play(this.warnPath, false, 1);
            this.finish();
        }
    }
    /**
     * 播放弹出框音
     * 
     * @memberof MPManager
     */
    playAlert() {
        if (this.audioSetting.isEffect && this.alertPath) {
            this.stopEffect();
            this.effectID = cc.audioEngine.play(this.alertPath, false, 1);
            this.finish();
        }
    }
    /**
     * 播放出牌音效
     * 
     * @memberof MPManager
     */
    playOut() {
        if (this.audioSetting.isEffect && this.outPath) {
            this.stopEffect();
            this.effectID = cc.audioEngine.play(this.outPath, false, 1);
            this.finish();
        }
    }
    /**
     * 播放选中牌音效
     * 
     * @memberof MPManager
     */
    playSelect() {
        if (this.audioSetting.isEffect && this.selectPath) {
            this.stopEffect();
            this.effectID = cc.audioEngine.play(this.selectPath, false, 1);
            this.finish();
        }
    }
    /**
     * 播放结算音效
     * 
     * @memberof MPManager
     */
    playOver() {
        if (this.audioSetting.isEffect && this.overPath) {
            this.stopEffect();
            this.effectID = cc.audioEngine.play(this.overPath, false, 1);
            this.finish();
        }
    }
    /**
     * 播放快速发言
     * 
     * @param {number} sex 1是男,2是女
     * @param {number} id 1-11的数字
     * @returns {Promise<void>} 
     * @memberof MPManager
     */
    async playQuicklySound(sex: number, id: number): Promise<void> {
        if (sex < 0 || sex > 2) return;
        if (id < 0 || id > 11) return;
        if (this.audioSetting.language > 0) {//0表示不播放报牌音
            let path = 'Audio/' + 3 + '/' + (sex === 0 ? 2 : sex) + '/' + id;
            this.playGameAudioByPath(path);
        }
    }
    /**
     * 播放报牌音(短音频没有停止)
     * 
     * @param {number} type 1是四川话,2是普通话
     * @param {number} suit 1是万,2是筒,3是条,4是表态类型
     * @param {number} sex 1是男,2是女
     * @param {number} point 1-9点数(suit为1-3时)或1-4(suit为4时)1胡2杠3碰4自摸
     * @returns {Promise<void>} 
     * @memberof MPManager
     */
    async playPokerSound(type: number, suit: number, sex: number, point: number): Promise<void> {
        if (type < 1 || type > 2) return;
        if (sex < 0 || sex > 2) return;
        if (point < 1 || point > 9) return;
        if (suit < 1 || suit > 4) return;
        if (suit === 4 && point > 4) return;
        if (this.audioSetting.language > 0) {//0表示不播放报牌音
            let path = 'Audio/' + type + '/' + suit + '/' + (sex === 0 ? 2 : sex) + '/' + point;
            this.playGameAudioByPath(path);
        }
    }
    /**
     * 根据拼接路径播放音效
     * 
     * @param {string} path 
     * @memberof MPManager
     */
    async playGameAudioByPath(path: string) {
        let url = this.getURLByPath(path);
        if (!url) {
            url = await this.loadFile(path);
            this.gameAudios.push({
                path: path,
                url: url
            });
        }
        cc.audioEngine.play(url, false, 1);
    }
    /**
     * 根据拼接的路径查找需要播放的音效
     * 
     * @param {string} path 
     * @returns 
     * @memberof MPManager
     */
    getURLByPath(path: string) {
        let url = null;
        for (let index = 0; index < this.gameAudios.length; index++) {
            let element = this.gameAudios[index];
            if (element.path === path) {
                url = element.url;
                break;
            }
        }
        return url;
    }
    /**
     * 离开战斗场景后释放游戏场景中的音效
     * 
     * @memberof MPManager
     */
    destroyGame() {
        while (this.gameAudios.length > 0) {
            cc.audioEngine.uncache(this.gameAudios.shift().url);
        }
    }
    /**
     * 释放销毁
     * 
     * @memberof MPManager
     */
    destroySelf(): void {
        cc.audioEngine.uncacheAll();
        this.backgroundPath = null;
        this.buttonPath = null;
        this.warnPath = null;
        this.alertPath = null;
        this.outPath = null;
        this.selectPath = null;
        this.overPath = null;
        this.effectID = null;
        this.backgroundID = null;
        this.audioSetting = null;
        this.quicklyList.length = 0;
        this.gameAudios.length = 0;
    }
}