import { MJ_Game_Type } from "./Protocol";

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
     * 打开创建房间的方式 0=大厅 1=俱乐部
     * @type {number}
     * @memberof UDManager
     */
    openCreateRoom: number = 0;
    /**
     * 打开创建房间的俱乐部数据 null=从大厅进入  非null = 从创建房间返回
     * @type {string}
     * @memberof UDManager
     */
    openClubData: CorpsVo = null;
    /**
     * 发送app地址请求
     * @param {string} appUrl 
     * @returns {Promise<any>} 
     * @memberof LoadCanvas
     */
    xmlHttp(appUrl: string): Promise<any> {
        return new Promise<any>((resolve, reject) => {
            let xhr = new XMLHttpRequest();
            xhr.timeout = 15000;
            xhr.ontimeout = () => {
                reject('超时');
            }
            xhr.onerror = () => {
                reject('发生错误');
            }
            xhr.onload = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        resolve(xhr.responseText);
                    } else {
                        reject('连接服务器失败');
                    }
                }
            }
            xhr.open("GET", appUrl, true);
            xhr.send();
        });
    }

    /**
     * 获取uuid，第一次运行创建uuid
     * 
     * @returns 
     * @memberof LoginCanvas
     */
    getGuestAccount(): string {
        let db = cc.sys.localStorage;
        let uuid = db.getItem('uuid');
        if (uuid && uuid.length === 32) {
            return uuid;
        } else {
            uuid = this.createUUID(32, 16);
            db.setItem('uuid', uuid);
            return uuid;
        }
    }
    /**
     * 创建UUID
     * 
     * @param {number} len UUID长度
     * @param {number} radix 输出的进制（2,8,10,16）
     * @returns {string} 返回对应进制下制定长度的字符串
     * @memberof LoginCanvas
     */
    createUUID(len: number, radix: number): string {
        let chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        let uuid = [], i;
        radix = radix || chars.length;
        if (len) {
            for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
        } else {
            let r;
            uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
            uuid[14] = '4';
            for (i = 0; i < 36; i++) {
                if (!uuid[i]) {
                    r = 0 | Math.random() * 16;
                    uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                }
            }
        }
        return uuid.join('');
    }
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