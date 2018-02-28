/**
 * 创建UUID
 * 
 * @param {number} len UUID长度
 * @param {number} radix 输出的进制（2,8,10,16）
 * @returns {string} 返回对应进制下制定长度的字符串
 * @memberof LoginCanvas
 */
export function createUUID(len: number, radix: number): string {
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
            renderTexture.removeFromParent();
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
export function getThousandString(num): string {
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
 * @param {string} str 
 * @returns 
 */
export function getBackNumString(str: string): string {
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
export function getPointNumString(str: string): number[] {
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
export function getNumberList(num: number): number[] {
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
export function getDateStringByTimestamp(timestamp: string, type: number = 0): string {
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
export function getDateStringByDate(nowDate: Date, connector: string): string {
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
export function getTimeStringByDate(nowDate, connector): string {
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
        return hourStr + connector + minuteStr /*+ connector + secondStr*/;
    }
    return '';
}

/**
 * 获取倒计时字符串
 * 
 * @param {number} time 倒计时毫秒数
 * @returns {string}
 */
export function getCountDownString(time: number): string {
    if (time < 0) return '00:00:00';
    let s = Math.floor(time / 1000);
    let hour = Math.floor(s / 3600);
    let minute = Math.floor((s - hour * 3600) / 60);
    let second = s - hour * 3600 - minute * 60;
    let str = '';
    if (hour > 9) str += hour + ':';
    else str += '0' + hour + ':';
    if (minute > 9) str += minute + ':';
    else str += '0' + minute + ':';
    if (second > 9) str += second;
    else str += '0' + second;
    return str;

}

/**
 * 返回指定长度的字符串
 * 
 * @export
 * @param {string} str 
 * @param {number} size 
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
 * 一段距离,被分割成几份,传入一个位置,返回这个位置最接近点的值
 * 
 * @export
 * @param {number} maxLen 线段总长
 * @param {number} sections 需要被分为几段
 * @param {number} point 传入值
 * @returns {number} 
 */
export function getClosestNumber(maxLen: number, sections: number, point: number): number {
    if (point < 0) return 0;
    if (point > maxLen) return maxLen;
    if (sections < 2) return maxLen;
    let secLen = maxLen / sections;
    let residue = point % secLen;
    let count = Math.floor(point / secLen);
    if (residue < secLen / 2) {
        return count * secLen;
    } else {
        return (count + 1) * secLen;
    }
}
/**
 * 一段距离,被分割成几份,传入一个位置,返回这个位置最接近点的下标
 * 
 * @export
 * @param {number} maxLen 线段总长
 * @param {number} sections 需要被分为几段
 * @param {number} point 传入值
 * @returns {number} 
 */
export function getClosestIndex(maxLen: number, sections: number, point: number): number {
    if (point < 0) return 0;
    if (point > maxLen) return sections;
    if (sections < 2) return maxLen;
    let secLen = maxLen / sections;
    let residue = point % secLen;
    let count = Math.floor(point / secLen);
    if (residue < secLen / 2) {
        return count;
    } else {
        return count + 1;
    }
}