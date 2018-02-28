/**
 * 游戏管理类
 * 
 * @export
 * @class GMManager
 */
export default class GMManager {
    private static _instance: GMManager = null;
    private constructor() { }
    /**
     * 获取GMManager单例对象
     * 
     * @static
     * @returns {GMManager} 
     * @memberof GMManager
     */
    static getInstance(): GMManager {
        if (GMManager._instance === null) {
            GMManager._instance = new GMManager();
        }
        return GMManager._instance;
    }

    /**
     * 扎金花游戏数据
     * 
     * @type {GameData}
     * @memberof GMManager
     */
    zjhGameData: GameData = null;

    nnGameData: GameData = null;

    /**
     * 清空单例对象
     * 
     * @memberof GMManager
     */
    destroySelf(): void {
        this.zjhGameData = null;
    }
}
