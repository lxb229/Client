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
     * 用户信息
     * 
     * @type {AccountData}
     * @memberof UDManager
     */
    account_mine: AccountData = null;
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
        this.account_mine = null;
        this.noticeList.length = 0;
        UDManager._instance = null;
    }
}