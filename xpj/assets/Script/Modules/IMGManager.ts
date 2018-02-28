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
     * 头像数组集合
     * 
     * @type {cc.SpriteFrame[]}
     * @memberof IMGManager
     */
    headList: cc.SpriteFrame[] = [];
    /**
     * 扑克牌图集
     * 
     * @type {cc.SpriteFrame[][]}
     * @memberof IMGManager
     */
    pokerList: cc.SpriteFrame[][] = [];


    async initIMG(): Promise<void> {
        for (let i = 0; i < 5; i++) {
            this.pokerList.push(new Array(13));
        }
        await this.initSystemHead();
        await this.initPoker();
    }

    /**
     * 初始化，动态加载系统头像
     * 
     * @returns {Promise<void>} 
     * @memberof IMGManager
     */
    private initSystemHead(): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            cc.loader.loadRes("Atlas/head", cc.SpriteAtlas, (err, atlas: cc.SpriteAtlas) => {
                if (err) {
                    reject(err.message);
                }
                else {
                    atlas.getSpriteFrames().forEach((spriteFrame: cc.SpriteFrame) => {
                        let num = parseInt(spriteFrame.name) - 1;
                        this.headList[num] = spriteFrame;
                    }, this);
                    resolve();
                }
            });
        });
    }
    /**
     * 初始化扑克牌
     * 
     * @returns {Promise<void>} 
     * @memberof IMGManager
     */
    private initPoker(): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            cc.loader.loadRes("Atlas/poker", cc.SpriteAtlas, (err, atlas: cc.SpriteAtlas) => {
                if (err) {
                    reject(err.message);
                }
                else {
                    atlas.getSpriteFrames().forEach((spriteFrame: cc.SpriteFrame) => {
                        let num = parseInt(spriteFrame.name);
                        if (num < 2) {
                            this.pokerList[0][num] = spriteFrame;
                        } else if (num >= 2 && num < 15) {
                            this.pokerList[1][num - 2] = spriteFrame;
                        } else if (num >= 15 && num < 28) {
                            this.pokerList[2][num - 15] = spriteFrame;
                        } else if (num >= 28 && num < 41) {
                            this.pokerList[3][num - 28] = spriteFrame;
                        } else if (num >= 41 && num < 54) {
                            this.pokerList[4][num - 41] = spriteFrame;
                        } else {
                            cc.error('poker index error');
                        }
                    }, this);
                    resolve();
                }
            });
        });
    }
    /**
     * 根据id取头像图集
     * 
     * @param {number} id 
     * @returns 
     * @memberof IMGManager
     */
    getHeadById(id: number): cc.SpriteFrame {
        if (this.headList.length > id) {
            return this.headList[id];
        } else {
            return null;
        }
    }
    /**
     * 根据id获取扑克牌图集
     * 
     * @param {number} id 
     * @returns 
     * @memberof IMGManager
     */
    getCardSpriteFrameById(id: number) {
        if (id < 2) {
            return this.pokerList[0][id];
        } else if (id >= 2 && id < 15) {
            return this.pokerList[1][id - 2];
        } else if (id >= 15 && id < 28) {
            return this.pokerList[2][id - 15];
        } else if (id >= 28 && id < 41) {
            return this.pokerList[3][id - 28];
        } else if (id >= 41 && id < 54) {
            return this.pokerList[4][id - 41];
        } else {
            return null;
        }
    }
    /**
     * 释放图片管理器的资源
     * 
     * @memberof IMGManager
     */
    destroySelf(): void {
        while (this.headList.length > 0) {
            this.release(this.headList.pop());
        }
        while (this.pokerList.length > 0) {
            let list = this.pokerList.pop();
            while (list.length > 0) {
                this.release(list.pop());
            }
        }
        IMGManager._instance = null;
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