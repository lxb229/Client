import { config, ui_manager } from './ModuleManager';
/**
 * 初始化微信
 * 
 * @export
 * @returns {number} 
 */
export function initWX(app_id: string, app_key: string): number {
    let res = -1;
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        res = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "initWX", "(Ljava/lang/String;Ljava/lang/String;)I", app_id, app_key);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        res = jsb.reflection.callStaticMethod("IOSHelper", "initWX:key:", app_id, app_key);
    } else {
        cc.log("该方法只支持原生平台");
        res = -1;
    }
    return res;
}

/**
 * 初始化实时语音
 * 
 * @export
 * @param {string} openId 
 * @returns {number} 
 */
export function initVoice(openId: string, app_id: string, app_key: string): number {
    let result = -1;
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        result = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "initVoice", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I", openId, app_id, app_key);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        result = jsb.reflection.callStaticMethod("IOSHelper", "initVoice:app_id:app_key:", openId, app_id, app_key);
    }
    else {
        cc.log("该方法只支持原生平台");
        result = 0;
    }
    return result;
}

/**
 * 获取app版本号
 * 
 * @export
 * @returns {string} 
 */
export function getAppVersion(): string {
    let ver = "";
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        ver = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "getAppVersion", "()Ljava/lang/String;");
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        ver = jsb.reflection.callStaticMethod("IOSHelper", "getAppVersion");
    }
    else {
        cc.log("该方法只支持原生平台");
    }
    return ver;
}

/**
 * 用默认浏览器打开指定url
 * 
 * @export
 * @param {string} url url地址
 */
export function openBrowser(url: string): void {
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "openBrowser", "(Ljava/lang/String;)V", url);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "openBrowser:", url);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 获取电量百分比0<x<=100
 * 
 * @export
 * @returns {number} 
 */
export function getBatteryLevel(): number {
    let level = 0;
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        level = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "getBatteryLevel", "()I");
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        level = jsb.reflection.callStaticMethod("IOSHelper", "getBatteryLevel");
    }
    else {
        cc.log("该方法只支持原生平台");
    }
    return level;
}

/**
 * 微信登录
 * 
 * @export
 */
export function wxLogin(): void {
    if (config.wxState !== 0) {
        ui_manager.showTip('请先安装微信客户端，使用微信登录!');
        ui_manager.hideLoading();
        return;
    }
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "wxLogin", "()V");
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "wxLogin");
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 微信分享
 * 
 * @export
 * @param {string} url 网页地址
 * @param {string} title 标题
 * @param {string} des 说明
 */
export function wxShare(url: string, title: string, des: string): void {
    if (config.wxState !== 0) {
        ui_manager.showTip('请先安装微信客户端!');
        ui_manager.hideLoading();
        return;
    }
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "wxShare", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", url, title, des);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "wxShare:title:des:", url, title, des);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 战绩分享-图片分享
 * 
 * @export
 * @param {string} filePath 截图文件路径
 */
export function wxShareRecord(filePath: string): void {
    if (config.wxState !== 0) {
        ui_manager.showTip('请先安装微信客户端，使用微信分享!');
        ui_manager.hideLoading();
        return;
    }
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "wxShareRecord", "(Ljava/lang/String;)V", filePath);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "wxShareRecord:", filePath);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 复制文本到剪切板
 * 
 * @export
 * @param {string} text 需要复制的文本
 */
export function copyToClipboard(text: string): void {
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "copyToClipboard", "(Ljava/lang/String;)V", text);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "copyToClipboard:", text);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 调用手机震动
 * 
 * @export
 */
export function phoneVibration(): void {
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "phoneVibration", "()V");
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "phoneVibration");
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 加入语音房间
 * 
 * @export
 */
export function joinTeamRoom(roomId: string): number {
    let result = -1;
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        result = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "joinTeamRoom", "(Ljava/lang/String;)I", roomId);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        result = jsb.reflection.callStaticMethod("IOSHelper", "joinTeamRoom:", roomId);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
    return result;
}

/**
 * 离开语音房间
 * 
 * @export
 */
export function quitRoom(): number {
    let result = -1;
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        result = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "quitRoom", "()I");
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        result = jsb.reflection.callStaticMethod("IOSHelper", "quitRoom");
    }
    else {
        cc.log("该方法只支持原生平台");
    }
    return result;
}

/**
 * 开关音频
 * 
 * @export
 * @param {boolean} isOpen 是否需要开启音频
 */
export function setState(isOpen: boolean): number {
    let result = -1;
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        result = jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AndroidHelper", "setState", "(Z)I", isOpen);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        result = jsb.reflection.callStaticMethod("IOSHelper", "setState:", isOpen);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
    return result;
}
/**
 * 请求ios内购列表
 * @param ids 苹果网站上配置的商品id，多个用‘,’隔开
 */
export function getProducts(ids: string): void {
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "getProducts:", ids);
    } else {
        cc.log("该方法只支持IOS原生平台");
    }
}
/**
 * 购买指定商品
 * 
 * @export
 * @param {string} pid 商品id
 * @param {string} bid 订单id
 */
export function buyProduct(pid: string, bid: string) {
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("IOSHelper", "buyProduct:bid:", pid, bid);
    } else {
        cc.log("该方法只支持IOS原生平台");
    }
}