/**
 * 用户管理类
 * 
 * @export
 * @class UDManager
 */
export default class UDManager {
    private static _instance: UDManager = null;
    private constructor() { }
    /**
     * 获取UDManager单例对象
     * 
     * @static
     * @returns {UDManager} 
     * @memberof UDManager
     */
    static getInstance(): UDManager {
        if (UDManager._instance === null) {
            UDManager._instance = new UDManager();
        }
        return UDManager._instance;
    }

    /**
     * 客户端自身基本信息
     * 
     * @type {UserData}
     * @memberof UDManager
     */
    mineData: UserData = null;

    /**
     * 大厅红点提示数据
     * 
     * @type {HotTip}
     * @memberof UDManager
     */
    hotTip: HotTip = null;

    /**
     * 公告列表
     * 
     * @type {NoticeNotify}
     * @memberof UDManager
     */
    noticeList: NoticeNotify[] = [];

    /**
     * 清空单例对象
     * 
     * @memberof UDManager
     */
    destroySelf(): void {
        this.mineData = null;
        this.hotTip = null;
        this.noticeList.length = 0;

        UDManager._instance = null;
    }
}