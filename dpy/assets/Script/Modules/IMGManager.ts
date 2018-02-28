import ENCManager from './ENCManager';
/**
 * 图片动态加载管理类
 * 
 * @export
 * @class IMGManager
 */
export default class IMGManager {
    private static _instance: IMGManager = null;
    private constructor() { }
    /**
     * 获取IMGManager单例对象
     * 
     * @static
     * @returns {IMGManager} 
     * @memberof IMGManager
     */
    static getInstance(): IMGManager {
        if (IMGManager._instance === null) {
            IMGManager._instance = new IMGManager();
        }
        return IMGManager._instance;
    }

    /**
     * 系统头像
     * 
     * @private
     * @type {cc.SpriteFrame}
     * @memberof IMGManager
     */
    private headSpriteFrame: cc.SpriteFrame = null;

    /**
     * 表情集合
     * 
     * @private
     * @type {cc.SpriteFrame[]}
     * @memberof IMGManager
     */
    bqSpriteFrames: cc.SpriteFrame[] = [];

    /**
     * 扑克牌集合
     * 
     * @private
     * @type {cc.SpriteFrame[]}
     * @memberof IMGManager
     */
    private pokerSpriteFrames: cc.SpriteFrame[] = [];

    /**
     * 存放加载过的头像纹理
     * 
     * @private
     * @type {cc.SpriteFrame[]}
     * @memberof IMGManager
     */
    private spriteFrames: cc.SpriteFrame[] = [];

    /** 
     * 把加载成功的纹理存放到数组，待释放的时候使用
     * 
     * @param {cc.SpriteFrame} spriteFrame  添加纹理
     * @memberof IMGManager
     */
    private addSpriteFrame(spriteFrame: cc.SpriteFrame): void {
        let bool: boolean = this.spriteFrames.some((sf) => {
            return sf === spriteFrame;
        });
        if (!bool) this.spriteFrames.push(spriteFrame);
    }
    /**
     * 初始化加载图片资源
     * 
     * @returns {Promise<void>} 
     * @memberof IMGManager
     */
    async initIMG(): Promise<void> {
        this.headSpriteFrame = await this.initSystemHead();
        this.bqSpriteFrames.length = 0;
        this.bqSpriteFrames = await this.initAtlas('Atlas/bq');
        this.pokerSpriteFrames.length = 0;
        this.pokerSpriteFrames = await this.initAtlas('Atlas/poker');
    }
    /**
     * 根据资源名排序
     * 
     * @param {cc.SpriteFrame[]} spriteFrames 
     * @memberof IMGManager
     */
    private sortByName(spriteFrames: cc.SpriteFrame[]) {
        if (spriteFrames && spriteFrames.length > 1) {
            spriteFrames.sort((a: cc.SpriteFrame, b: cc.SpriteFrame) => {
                return Number(a.name) - Number(b.name);
            })
        }
    }
    /**
     * 根据id获取扑克图片
     * 
     * @param {number} id 
     * @returns {cc.SpriteFrame} 
     * @memberof IMGManager
     */
    getPokerSpriteFrameById(id: number): cc.SpriteFrame {
        let res: cc.SpriteFrame = null;
        if (this.pokerSpriteFrames && this.pokerSpriteFrames.length > 0) {
            for (let i = 0; i < this.pokerSpriteFrames.length; i++) {
                let spriteFrame = this.pokerSpriteFrames[i];
                if (Number(spriteFrame.name) === id) {
                    res = spriteFrame;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 初始化，动态加载系统头像
     * 
     * @returns {Promise<void>} 
     * @memberof IMGManager
     */
    private initSystemHead(): Promise<cc.SpriteFrame> {
        return new Promise<cc.SpriteFrame>((resolve, reject) => {
            cc.loader.loadRes("Texture/systemHead", cc.SpriteFrame, (err: Error, spriteFrame: cc.SpriteFrame) => {
                if (err) {
                    reject(err.message);
                }
                else {
                    resolve(spriteFrame);
                }
            });
        });
    }

    private initAtlas(path: string): Promise<cc.SpriteFrame[]> {
        return new Promise<cc.SpriteFrame[]>((resolve, reject) => {
            cc.loader.loadRes(path, cc.SpriteAtlas, (err: Error, spriteAtlas: cc.SpriteAtlas) => {
                if (err) {
                    reject(err.message);
                }
                else {
                    let spriteFrames: cc.SpriteFrame[] = spriteAtlas.getSpriteFrames();
                    this.sortByName(spriteFrames);
                    cc.loader.release(spriteAtlas);
                    resolve(spriteFrames);
                }
            });
        });
    }


    /**
     * 获取图片目录
     * 
     * @returns {string} 
     * @memberof IMGManager
     */
    private getDirPath(): string {
        //图片保存目录
        let dirpath = jsb.fileUtils.getWritablePath() + 'img/';
        //验证路径是否存在，如果不存在则创建路径
        if (!jsb.fileUtils.isDirectoryExist(dirpath)) {
            jsb.fileUtils.createDirectory(dirpath);
        }
        return dirpath;
    }

    /**
     * 根据url地址获取本地路径
     * 
     * @param {string} url 
     * @returns {string} 
     * @memberof IMGManager
     */
    private getFilePath(url: string): string {
        return this.getDirPath() + ENCManager.getInstance().MD5(url) + '.jpg';
    }

    /**
     * 根据路径加载本地存储的图片
     * 
     * @param {string} filePath 图片路径
     * @returns {Promise<cc.SpriteFrame>} 
     * @memberof IMGManager
     */
    private loadLocalImg(filePath: string): Promise<cc.SpriteFrame> {
        return new Promise<cc.SpriteFrame>((resolve, reject) => {
            cc.loader.load(filePath, (err, tex) => {
                if (err) {
                    reject(err);
                } else {
                    let spriteFrame = new cc.SpriteFrame(tex);
                    this.addSpriteFrame(spriteFrame);
                    resolve(spriteFrame);
                }
            });
        });
    }

    /**
     * 根据url加载图片，长度为0取系统头像，大于0取微信头像
     * 
     * @param {string} url 头像地址
     * @returns {Promise<cc.SpriteFrame>} 
     * @memberof IMGManager
     */
    async loadURLImage(url: string): Promise<cc.SpriteFrame> {
        if (url.length > 0) {//取微信头像
            if (cc.sys.isNative) {
                let filePath = this.getFilePath(url);
                if (jsb.fileUtils.isFileExist(filePath)) {
                    return this.loadLocalImg(filePath);
                } else {
                    let buffer = await new Promise<ArrayBuffer>((resolve, reject) => {
                        let xhr = new XMLHttpRequest()
                        xhr.responseType = 'arraybuffer';
                        xhr.timeout = 10000;
                        xhr.onload = () => {
                            if (xhr.status === 200) {
                                resolve(xhr.response);
                            } else {
                                reject(new TypeError('Network response failed'));
                            }
                        }
                        xhr.onerror = () => {
                            reject(new TypeError('Network request error'));
                        }
                        xhr.ontimeout = () => {
                            reject(new TypeError('Network request timeout'));
                        }
                        xhr.open("GET", url, true);
                        xhr.send();
                    });
                    jsb.fileUtils.writeDataToFile(new Uint8Array(buffer), filePath);
                    return this.loadLocalImg(filePath);
                }
            } else {
                return new Promise<cc.SpriteFrame>((resolve, reject) => {
                    cc.loader.load({ url: url, type: 'jpg' }, (err, tex) => {
                        if (err) {
                            reject(err);
                        } else {
                            let spriteFrame = new cc.SpriteFrame(tex);
                            this.addSpriteFrame(spriteFrame);
                            resolve(spriteFrame);
                        }
                    });
                });
            }
        } else {//取系统头像
            return this.headSpriteFrame;
        }
    }

    /**
     * 释放图片管理器的资源
     * 
     * @memberof IMGManager
     */
    destroySelf(): void {
        this.destrySpriteFrames(this.spriteFrames);
        this.destrySpriteFrames(this.bqSpriteFrames);
        this.destrySpriteFrames(this.pokerSpriteFrames);
        this.release(this.headSpriteFrame);
        this.headSpriteFrame = null;
        IMGManager._instance = null;
    }
    /**
     * 释放图集
     * 
     * @private
     * @param {cc.SpriteFrame[]} spriteFrames 
     * @memberof IMGManager
     */
    private destrySpriteFrames(spriteFrames: cc.SpriteFrame[]) {
        spriteFrames.forEach((spriteFrame: cc.SpriteFrame) => {
            this.release(spriteFrame);
        }, this);
        spriteFrames.length = 0;
    }

    /**
     * 释放资源及其所有的引用
     * 
     * @private
     * @param {(cc.Asset | cc.RawAsset | string)} owner 需要释放的资源
     * @memberof IMGManager
     */
    private release(owner: cc.Asset | cc.RawAsset | string): void {
        let deps = cc.loader.getDependsRecursively(owner);
        cc.loader.release(deps);
    }
}
    //http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0