/**
 * num是传入的正整数,返回千分位逗号分隔的字符串
 * 
 * @export
 * @param {any} num 
 * @returns 
 */
export function getThousandString(num) {
    if (isNaN(num)) num = 0;
    let numString = num.toString();
    let result = "";
    while (numString.length > 3) {
        result = "," + numString.slice(-3) + result;
        numString = numString.slice(0, numString.length - 3);
    }
    if (numString) { result = numString + result; }
    return result;
}

/**
 * 传入千分位的字符串，返回去除逗号的字符串
 * 
 * @export
 * @param {any} str 
 * @returns 
 */
export function getBackNumString(str: string) {
    let list = str.split(",");
    str = "";
    for (let i = 0; i < list.length; i++) {
        str += list[i];
    }
    return str;
}

/**
 * 传入带小数点的数字字符串，返回去除小数点的数组
 * @export
 * @param {string} str 
 * @returns 
 */
export function getPointNumString(str: string) {
    let list = str.split(".");
    let numList = [];
    for (let i = 0; i < list.length; i++) {
        numList.push(Number(list[i]));
    }
    return numList;
}

/**
 * 传入数字，返回每位数字的组成的数组
 * @export
 * @param {number} num 
 * @returns 
 */
export function getNumberList(num: number) {
    let numList = num.toString().split("").map((i) => {
        return Number(i);
    });
    return numList;
}

/**
 * 根据时间戳，返回对应时间格式的字符串
 * 
 * @param {string} timestamp  时间戳(字符串类型)
 * @param {number} type 1是只获取日期，2是只获取时间,默认都获取（数字类型）
 * @returns {string} 
 */
export function getDateStringByTimestamp(timestamp: string, type: number) {
    let num = Number(timestamp);
    if (isNaN(num)) return '';
    else {
        let timeDate = new Date();
        timeDate.setTime(num);
        let timeDateString = this.getDateStringByDate(timeDate);
        let timeString = this.getTimeStringByDate(timeDate);
        if (type === 1) {
            return timeDateString;
        } else if (type === 2) {
            return timeString;
        } else {
            return timeDateString + ' ' + timeString;
        }
    }
}
/**
 * 根据时间对象获取日期
 * 
 * @param {Date} nowDate 时间对象
 * @param {string} connector 分隔符
 * @returns {string}
 */
export function getDateStringByDate(nowDate: Date, connector: string) {
    if (nowDate instanceof Date) {
        if (!connector) {
            connector = "-";
        }
        let year = nowDate.getFullYear() + '';
        let month = nowDate.getMonth() + 1;
        let monthStr = month + '';
        if (month < 10) monthStr = "0" + month;
        let day = nowDate.getDate();
        let dayStr = day + '';
        if (day < 10) dayStr = "0" + day;
        return year + connector + monthStr + connector + dayStr;
    }
    return '';
}
/**
 * 根据时间对象获取时间
 * 
 * @param {Date} nowDate 时间对象
 * @param {string} connector 分隔符
 * @returns {string}
 */
export function getTimeStringByDate(nowDate, connector) {
    if (nowDate instanceof Date) {
        if (!connector) {
            connector = ":";
        }
        let hour = nowDate.getHours();
        let hourStr = hour + '';
        if (hour < 10) hourStr = "0" + hour;
        let minute = nowDate.getMinutes();
        let minuteStr = minute + '';
        if (minute < 10) minuteStr = "0" + minute;
        // let second = nowDate.getSeconds();
        // let secondStr = second + '';
        // if (second < 10) secondStr = "0" + second;
        return hourStr + connector + minuteStr /*+ connector + secondStr*/;
    }
    return '';
}

/**
 * 获取倒计时字符串
 * 
 * @param {number} time 倒计时毫秒数
 * @param {number} type 返回类型(默认空返回时分秒，1返回分秒)
 * @returns {string}
 */
export function getCountDownString(time: number, type: number) {
    let obj = this.getCountDownObj(time);
    if (obj === null) return '';
    else {
        let str = '';
        if (type === 1) {
            obj.minute += obj.hour * 60;
        } else {
            if (obj.hour > 9) str += obj.hour + ':';
            else str += '0' + obj.hour + ':';
        }

        if (obj.minute > 9) str += obj.minute + ':';
        else str += '0' + obj.minute + ':';
        if (obj.second > 9) str += obj.second;
        else str += '0' + obj.second;
        return str;
    }
}

/**
 * 返回len位有效数字的数字，向下取整
 * @export
 * @param {number} num 数字
 * @param {number} len 最长的长度
 * @returns 
 */
export function getEffectiveNumbers(num: number, len: number) {//
    let integerNum = Math.floor(num);
    let stringNum = num.toString();
    if (num === integerNum) {//判断为没有小数点
        if (stringNum.length < len + 1) {//判断位数是否超过4位
            return num;
        } else {
            return Number(stringNum.slice(0, len));
        }
    } else {
        if (stringNum.length < len + 2) {//判断位数是否超过5位，有一位是小数点
            return num;
        } else {
            return Number(stringNum.slice(0, len + 1));
        }
    }
}
/**
 * 传入一个数字，返回带单位的4位有效数字和单位，有小数点
 * @export
 * @param {number} num 
 * @param {boolean} [noSpace=true] 没有空格
 * @returns 
 */
export function getShowNumberString(num: number, noSpace: boolean = true) {
    if (num >= 10000) {
        let numStr = this.getEffectiveNumbers(num / 10000, 4);
        numStr += "万";
        return numStr;
    } else {
        return num + '';
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
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity", "copyToClipboard", "(Ljava/lang/String;)V", text);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("AppController", "copyToClipboard:", text);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}

/**
 * 用默认浏览器打开指定url
 * 
 * @export
 * @param {string} url url地址
 */
export function openBrowser(url: string): void {
    if (cc.sys.isNative && cc.sys.os === cc.sys.OS_ANDROID) {
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity", "openBrowser", "(Ljava/lang/String;)V", url);
    } else if (cc.sys.isNative && cc.sys.os === cc.sys.OS_IOS) {
        jsb.reflection.callStaticMethod("AppController", "openBrowser:", url);
    }
    else {
        cc.log("该方法只支持原生平台");
    }
}
