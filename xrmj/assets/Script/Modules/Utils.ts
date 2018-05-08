/**
 * 修改微信获取的头像地址
 * 
 * @export
 * @param {string} url 原始头像地址
 * @returns {string} 修改为96尺寸的地址
 */
export function getHeadImgUrl(url: string): string {
    if (url === '' || url === '/0') return '';
    let arrayList: string[] = url.split('/');
    arrayList.pop();
    return arrayList.join('/') + '/96';
}
/**
 * 返回指定长度的字符串
 * 
 * @export
 * @param {string} str 
 * @param {number} size 字节长度 汉字 = 字节 * 2
 * @returns 
 */
export function getStringBySize(str: string, size: number): string {
    let len = 0;
    let vaule = '';
    for (let i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) > 255) {
            len += 2;
        } else {
            len += 1;
        }
        if (len > size) {
            break;
        } else {
            vaule += str.charAt(i);
        }
    }
    return vaule;
}
/**
 * 截图保存到本地
 * 
 * @export
 * @param {cc.Node} node 需要截图的节点
 * @param {string} saveName 需要保存图片的名字
 * @param {Function} callback 保存成功后的回调
 * @return {boolean} 是否保存
 */
export function captureScreen(node: cc.Node, saveName: string, callback: Function): void {
    if (cc.sys.isNative && cc.sys.isMobile) {
        let renderTexture = cc.RenderTexture.create(node.width, node.height, cc.Texture2D.PixelFormat.RGBA8888, gl.DEPTH24_STENCIL8_OES);
        node.parent._sgNode.addChild(renderTexture);
        renderTexture.setVisible(false);
        renderTexture.begin();
        node._sgNode.visit();
        renderTexture.end();
        renderTexture.saveToFile(saveName, cc.ImageFormat.PNG, true, (rt, path) => {
            renderTexture.removeFromParent(true);
            let interval = 0;
            let timeId = setInterval(() => {
                if (interval > 10000) {
                    interval = null;
                    clearInterval(timeId);
                    callback();
                }
                if (jsb.fileUtils.isFileExist(path)) {
                    interval = null;
                    clearInterval(timeId);
                    callback(path);
                }
                interval += 100;
            }, 100);
        });
    } else {
        callback();
    }
}

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
 * 数组乱序
 * 
 * @export
 * @param {any[]} arr 需要乱序的数组
 */
export function shuffle(arr: any[]) {
    if (arr && arr.length > 1) {
        for (let j, x, i = arr.length; i; j = Math.floor(Math.random() * i), x = arr[--i], arr[i] = arr[j], arr[j] = x);
    }
}

/**
 * 手机号验证
 * @param {string} sMobile 
 * @returns 
 * @memberof LoginCanvas
 */
export function checkMobile(sMobile: string) {
    if (!(/^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/.test(sMobile))) {
        return false;
    }
    return true;
}
/**
 * 获取带单位的数字
 * @export
 * @param {number} num 
 */
export function getShowUnitNumber(num: number): string {
    let tempStr = '';
    if (num > 9999) {
        tempStr = this.getEffectiveNumbers(num / 10000, 4);
        tempStr = tempStr + "万";
    } else {
        tempStr = num.toString();
    }
    return tempStr;
}
/**
 * 返回len位有效数字的数字，向下取整
 * @export
 * @param {number} num 
 * @param {number} [len=4] 
 * @returns 
 */
export function getEffectiveNumbers(num: number, len: number = 4) {
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