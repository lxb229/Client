import { ui_manager } from './ModuleManager';
import { Protocol } from './Protocol';
import ENCManager from './ENCManager';
import UDManager from './UDManager';
import UIManager from './UIManager';
import GMManager from './GMManager';

/**
 * ws网络管理类
 * 
 * @export
 * @class WSManager
 */
export default class WSManager {
    private static _instance: WSManager = null;
    private constructor() { }
    /**
     * 获取WSManager单例对象
     * 
     * @static
     * @returns {WSManager} 
     * @memberof WSManager
     */
    static getInstance(): WSManager {
        if (WSManager._instance === null) {
            WSManager._instance = new WSManager();
        }
        return WSManager._instance;
    }
    /**
     * 心跳间隔时间，单位毫秒
     * 
     * @type {number}
     * @memberof WSManager
     */
    private readonly heartbeatInterval: number = 5000;
    /**
     * 心跳发送后清零，之后累加
     * 
     * @private
     * @type {number}
     * @memberof WSManager
     */
    private intervalTime: number = 0;
    /**
     * 消息超时时长
     * 
     * @type {number}
     * @memberof WSManager
     */
    private readonly timeoutTime: number = 10000;
    /**
     * 心跳最大间隔时间，超过则掉线
     * 
     * @type {number}
     * @memberof WSManager
     */
    private readonly timeoutMax: number = 30000;
    /**
     * 心跳最早一次发送的时间
     * 
     * @private
     * @type {number}
     * @memberof WSManager
     */
    private heartbeatStart: number = 0;
    /**
     * 心跳最后一次收到的时间
     * 
     * @private
     * @type {number}
     * @memberof WSManager
     */
    private heartbeatEnd: number = 0;
    /**
    * 时间调度器id（setInterval）
    * 
    * @private
    * @type {number}
    * @memberof WSManager
    */
    private timeID: number = null;
    /**
     * ws连接地址
     * 
     * @private
     * @type {string}
     * @memberof WSManager
     */
    private url: string = null;
    /**
     * WebSocket对象
     * 
     * @private
     * @type {WebSocket}
     * @memberof WSManager
     */
    private ws: WebSocket = null;
    /**
     * 当前连接状态
     * 
     * @private
     * @type {number}
     * @memberof WSManager
     */
    private readyState: number = null;
    /**
     * 延迟时间，单位毫秒
     * 
     * @private
     * @type {number}
     * @memberof WSManager
     */
    private delayTime: number = 0;
    /**
     * 存放发送数据的数组，用于判断消息超时
     * 
     * @private
     * @type {SendData[]}
     * @memberof WSManager
     */
    private sendDataArray: SendData[] = [];
    /**
     * 主动断开时发送过来的回调
     * 
     * @private
     * @type {Function}
     * @memberof WSManager
     */
    private cb_close: Function = null;
    /**
     * 是否登录游戏服
     * 
     * @private
     * @type {boolean}
     * @memberof WSManager
     */
    private isLogin: boolean = false;
    /**
     * 修改登录状态
     * 
     * @param {boolean} state 
     * @memberof WSManager
     */
    setLoginState(state: boolean) {
        this.isLogin = state;
    }
    /**
     * 连接ws服务器
     * 
     * @param {string} url 连接地址
     * @memberof WSManager
     */
    connect(url: string): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            if (this.ws || url.length < 1 || this.readyState) {
                this.destroySelf();
                reject('参数错误');
            }
            this.readyState = WebSocket.CONNECTING;
            this.ws = new WebSocket(url);
            this.ws.binaryType = "arraybuffer";
            /**
             * ws连接上时触发
             */
            this.ws.onopen = (ev: Event) => {
                this.readyState = WebSocket.OPEN;
                this.url = url;
                this.checkTimeOut();
                resolve();
            };
            /**
             * ws断开连接时触发
             */
            this.ws.onclose = (ev: CloseEvent) => {
                switch (this.readyState) {
                    case WebSocket.CONNECTING://连接失败
                        reject('连接失败');
                        break;
                    case WebSocket.OPEN://意外断开连接
                        cc.log('意外断开连接');
                        cc.systemEvent.emit('cb_diconnect');
                        break;
                    case WebSocket.CLOSING://客户端主动断开连接
                        cc.log('客户端主动断开连接');
                        if (this.cb_close) { this.cb_close(); }
                        break;
                    case WebSocket.CLOSED://心跳超时
                        cc.log('心跳超时');
                        cc.systemEvent.emit('cb_diconnect');
                        break;
                    default:
                        cc.log('onclose：未知状态！');
                        cc.systemEvent.emit('cb_diconnect');
                        break;
                }
                this.destroySelf();
            };
            /**
             * 收到服务器消息时触发
             */
            this.ws.onmessage = (ev: MessageEvent) => {
                let msgId = this.getMessageId(ev.data, 12);
                let msgBody = this.getMessageBody(ev.data, 32);
                let s_data: S_Data = JSON.parse(msgBody);
                if (msgId > 10000) {
                    //服务器的推送消息
                    if (s_data.code === 0) {
                        this.doPush(msgId, s_data.content);
                    } else {
                        cc.log('推送了错误消息过来');
                    }
                } else {
                    //客户端请求返回的数据
                    let sendData = this.getSendData(msgId);
                    if (sendData) {
                        this.delayTime = Date.now() - sendData.sendTime;
                        sendData.callback(s_data.code, s_data.content);
                        cc.js.array.remove(this.sendDataArray, sendData);
                    } else {
                        if (msgId !== Protocol.ACCOUNT_PING)
                            cc.log('onmessage:协议号' + msgId + '超时或未知');
                    }
                }
            };
        });
    }
    /**
     * 检查消息超时
     * 
     * @memberof WSManager
     */
    private checkTimeOut(): void {
        let dateTime = Date.now();
        this.heartbeatStart = dateTime;
        this.heartbeatEnd = dateTime;
        this.intervalTime = 0;
        this.sendDataArray.length = 0;
        this.timeID = setInterval(() => {
            if (this.readyState !== WebSocket.OPEN) return;
            this.checkHeartbeat();
            let tempRemove: SendData[] = [];
            this.sendDataArray.forEach((item: SendData) => {
                if (Date.now() - item.sendTime >= this.timeoutTime) {
                    ui_manager.showTip('消息超时');
                    ui_manager.hideLoading();
                    item.callback(-1);
                    tempRemove.push(item);
                }
            });
            cc.js.array.removeArray(this.sendDataArray, tempRemove);
        }, 1000);
    }
    /**
     * 停止心跳检测
     * 
     * @memberof WSManager
     */
    stopCheck(): void {
        if (this.timeID) {
            clearInterval(this.timeID);
            this.timeID = null;
        }
    }
    /**
     * 心跳检测
     * 
     * @memberof WSManager
     */
    private checkHeartbeat() {
        if (this.intervalTime === 0) {
            this.heartbeatStart = Date.now();
            //发送心跳包
            this.sendMsg(Protocol.ACCOUNT_HEART, '', (flag: number, content?: any) => {
                if (flag === 0) {
                    this.heartbeatEnd = Date.now();
                    this.delayTime = this.heartbeatEnd - this.heartbeatStart;
                    if (this.isLogin) {
                        let obj = { ping: this.delayTime };
                        this.sendMsg(Protocol.ACCOUNT_PING, JSON.stringify(obj), null);
                    }
                } else {
                    cc.log('心跳返回错误了！');
                }
            });
        }
        this.intervalTime += 1000;
        if (this.intervalTime >= this.heartbeatInterval) {
            this.intervalTime = 0;
        }
        if (this.delayTime >= this.timeoutMax) {
            //掉线了
            this.readyState = WebSocket.CLOSED;
            this.ws.close();
        }
    }
    /**
     * 处理服务器推送消息
     * 
     * @param {Protocol} msgId 协议号
     * @param {any} content 数据明文对象
     * @memberof WSManager
     */
    private doPush(msgId: Protocol, content: any): void {
        switch (msgId) {
            case Protocol.ACCOUNT_NOTIFY_HOT_PROMPT://红点推送消息
                UDManager.getInstance().hotTip = content as HotTip;
                break;
            case Protocol.MESSAGE_NOTICE_NOTIFY: {//公告推送消息
                let isAdd = true;
                let noticeData = content as NoticeNotify;
                for (let i = 0; i < UDManager.getInstance().noticeList.length; i++) {
                    let notice = UDManager.getInstance().noticeList[i];
                    if (notice.msgId === noticeData.msgId) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    UDManager.getInstance().noticeList.push(noticeData);
                }
                break;
            }
            case Protocol.ACCOUNT_NOTIFY_KICK_ACCOUNT://推送消息(帐号被踢下线通知)
                this.disconnect(() => {
                    cc.director.loadScene('LoginScene', () => {
                        ui_manager.showAlert('当前账号已在其他设备登录！', '温馨提示', null, null, 1);
                    });
                });
                break;
            case Protocol.ACCOUNT_NOTIFY_WALLET://玩家房卡数据
                let wallet = content as Wallet;
                UDManager.getInstance().mineData.roomCard = wallet.roomCard;
                break;
            case Protocol.MAJIANG_ROOM_GAMESTATE_NOTIFY://推送消息(游戏状态变化)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.MAJIANG_ROOM_SEAT_NOTIFY://推送消息(座位数据变化)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.MAJIANG_ROOM_DINQUE_NOTIFY://推送消息(定缺数据通知)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.MAJIANG_ROOM_SWAPCARD_NOTIFY://推送消息(换牌数据通知)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.MAJIANG_ROOM_OUTCARD_NOTIFY://推送消息(出牌数据通知)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.MAJIANG_ROOM_BREAKCARD_NOTIFY://推送消息(胡杠碰吃过表态通知)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.MAJIANG_ROOM_BAOJIAO_BT_NOTIFY: //推送消息(座位报叫表态通知)
                GMManager.getInstance().setBaoJiaoData(content);
                break;
            case Protocol.CHAT_SEND_NOTIFY://推送消息(聊天信息通知)
                cc.systemEvent.emit('MJ_ChatPush', content);
                break;
            case Protocol.MAJIANG_ROOM_DESTORY_NOTIFY:// 推送消息(房间已解散通知)
                cc.systemEvent.emit('MJ_OutPush', content);
                break;
            case Protocol.MAJIANG_ROOM_DESTORY_BT_NOTIFY:// 推送消息(桌子解散表态通知)
                GMManager.getInstance().setTableData(content as MJGameData);
                break;
            case Protocol.CORPS_ADD_MEMBER_NOTIFY://推送消息(俱乐部成员添加通知)
                ui_manager.showAlert(content.content, '俱乐部', null, null, 1);
                cc.systemEvent.emit('Club_Add_Member_Push', content);
                break;
            case Protocol.CORPS_KIC_KMEMBER_NOTIFY:// 推送消息(俱乐部成员移除通知)
                ui_manager.showAlert(content.content, '俱乐部', null, null, 1);
                cc.systemEvent.emit('Club_Kik_Member_Push', content);
                break;
            case Protocol.CORPS_DESTORY_NOTIFY:// 推送消息(俱乐部解散通知)
                ui_manager.showAlert(content.content, '俱乐部', null, null, 1);
                cc.systemEvent.emit('Club_Destory_Push', content);
                break;
            default: break;
        }
    }
    /**
     * 根据协议号查找对象
     * 
     * @param {Protocol} msgId 协议号
     * @returns {SendData} 发送数据对象
     * @memberof WSManager
     */
    private getSendData(msgId: Protocol): SendData {
        for (let i = 0, len = this.sendDataArray.length; i < len; i++) {
            let item: SendData = this.sendDataArray[i];
            if (item.msgId === msgId) {
                return item;
            }
        }
        return null;
    }
    /**
     * 客户端主动断开连接
     * 
     * @memberof WSManager
     */
    disconnect(callback: Function): void {
        if (this.ws && this.ws.readyState === WebSocket.OPEN && this.readyState === WebSocket.OPEN) {
            this.readyState = WebSocket.CLOSING;
            this.cb_close = callback;
            this.ws.close();
        }
    }
    /**
     * 向服务器发送消息
     * 
     * @param {Protocol} msgId 协议号
     * @param {string} msgBody 消息明文
     * @param {(flag: number, content?: string) => void} [callback] 回调函数，flag0为成功，负数失败，-1为超时
     * @returns {boolean} 返回发送结果，成功或失败
     * @memberof WSManager
     */
    sendMsg(msgId: Protocol, msgBody: string, callback?: (flag: number, content?: string) => void): boolean {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            if (callback) {
                let sendData: SendData = this.getSendData(msgId);
                if (sendData) {
                    cc.js.array.remove(this.sendDataArray, sendData);
                }
                sendData = {
                    msgId: msgId,
                    sendTime: Date.now(),
                    callback: callback
                };
                this.sendDataArray.push(sendData);
            }
            this.ws.send(this.createMessage(msgId, msgBody));
            return true;
        }
        return false;
    }
    /**
     * 解析消息号
     * 
     * @param {ArrayBuffer} buf 服务器传送过来的数据
     * @param {number} offset 偏移量
     * @returns {number} 返回协议号
     * @memberof WSManager
     */
    private getMessageId(buf: ArrayBuffer, offset: number): number {
        let array = Array.apply([], new Uint8Array(buf));
        let msgId: number = (
            ((array[offset] & 0xFF) << 24) |
            ((array[offset + 1] & 0xFF) << 16) |
            ((array[offset + 2] & 0xFF) << 8) |
            (array[offset + 3] & 0xFF)
        );
        return msgId;
    }
    /**
     * 解析消息内容
     * 
     * @param {ArrayBuffer} buf 服务器传送过来的数据
     * @param {number} offset 偏移量
     * @returns {string} 解析后的字符串数据
     * @memberof WSManager
     */
    private getMessageBody(buf: ArrayBuffer, offset: number): string {
        let bodyBuffer = buf.slice(offset);
        return this.ArrayBufferToString(bodyBuffer);
    }
    /**
     * 创建消息
     * 
     * @param {number} msgId 消息号
     * @param {string} msgBody 消息内容
     * @returns {ArrayBuffer} 待发送数据
     * @memberof WSManager
     */
    private createMessage(msgId: number, msgBody: string): ArrayBuffer {
        let bodyArray = this.getArrayNumberFromString(msgBody);
        let bodyLen = bodyArray.length;
        let msgHeader: MessageHeader = {
            hcheck: 0,
            code: 0,
            flag: 0,
            id: 0,
            timestamp: 0,
            bcheck: 0,
            blen: 0,
            alen: 0,
        };
        msgHeader.id = msgId;
        msgHeader.timestamp = Date.now() / 1000;
        msgHeader.blen = bodyLen;
        msgHeader.bcheck = this.checkSum(bodyArray, 0, bodyLen);
        let headArray = this.getHeaderToArrayNumber(msgHeader);
        msgHeader.hcheck = this.checkSum(headArray, 4, 28);
        headArray = this.getHeaderToArrayNumber(msgHeader);
        let totalArray = headArray.concat(bodyArray);
        return new Uint8Array(totalArray).buffer;
    }
    /**
     * 把消息头转换成int32类型的字节数组
     * 
     * @param {MessageHeader} msgHeader 字节头对象
     * @returns {Array<number>} 数组
     * @memberof WSManager
     */
    private getHeaderToArrayNumber(msgHeader: MessageHeader): Array<number> {//消息头转字节数组
        let headArray = this.numberToArrayNumber(msgHeader.hcheck);
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.code));
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.flag));
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.id));
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.timestamp));
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.bcheck));
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.blen));
        headArray = headArray.concat(this.numberToArrayNumber(msgHeader.alen));
        return headArray;
    }
    /**
     * 把数字转换为int32表示的字节数组
     * 
     * @param {number} number 需要转换的数字
     * @returns {Array<number>} 数组
     * @memberof WSManager
     */
    private numberToArrayNumber(number: number): Array<number> {
        let array = [];
        array[0] = ((number >> 24) & 0xFF);
        array[1] = ((number >> 16) & 0xFF);
        array[2] = ((number >> 8) & 0xFF);
        array[3] = (number & 0xFF);
        return array;
    }
    /**
     * 把字符串转换为字节数组
     * 
     * @param {string} str 待转换的字符串
     * @returns {Array<number>} 数组
     * @memberof WSManager
     */
    private getArrayNumberFromString(str: string): Array<number> {
        let arrayNumber: Array<number> = Array.apply([], this.StringToArrayBuffer(str));
        return arrayNumber;
    }
    /**
     * 消息数据效验
     * 
     * @param {Array<number>} arrayNumber 待校验数组
     * @param {number} offset 偏移量
     * @param {number} length 长度
     * @returns {number} 校验后生成的数字
     * @memberof WSManager
     */
    private checkSum(arrayNumber: Array<number>, offset: number, length: number): number {
        let hash = 0;
        for (let i = offset; i < offset + length; i++) {
            hash = hash << 7 ^ arrayNumber[i];
        }
        return hash;
    }
    /**
     * ArrayBuffer转为字符串
     * 
     * @param {ArrayBuffer} buf ArrayBuffer对象
     * @returns {string} 转换出来的字符串
     * @memberof WSManager
     */
    private ArrayBufferToString(buf: ArrayBuffer): string {
        let str = String.fromCharCode.apply(null, new Uint8Array(buf));
        str = ENCManager.getInstance().Utf8Decode(str);
        return str;
    }
    /**
     * 字符串转为ArrayBuffer对象，参数为字符串
     * 
     * @param {string} str 待转换的字符串
     * @returns {Uint8Array} 转换后的字节码
     * @memberof WSManager
     */
    private StringToArrayBuffer(str: string): Uint8Array {
        str = ENCManager.getInstance().Utf8Encode(str);
        var buf = new ArrayBuffer(str.length);
        var bufView = new Uint8Array(buf);
        for (var i = 0, strLen = str.length; i < strLen; i++) {
            bufView[i] = str.charCodeAt(i);
        }
        return bufView;
    }
    /**
     * 获取延迟时间
     * 
     * @returns {number} 
     * @memberof WSManager
     */
    getDelayTime(): number {
        return this.delayTime;
    }
    /**
     * 清理销毁
     * 
     * @memberof WSManager
     */
    destroySelf(): void {
        this.stopCheck();
        this.url = null;
        this.readyState = null;
        this.ws = null;
        this.sendDataArray.length = 0;
        this.heartbeatEnd = 0;
        this.heartbeatStart = 0;
        this.intervalTime = 0;
        this.delayTime = 0;
        this.cb_close = null;
        this.isLogin = false;

        WSManager._instance = null;
    }
}