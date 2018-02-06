(function() {"use strict";var __module = CC_EDITOR ? module : {exports:{}};var __filename = 'preview-scripts/assets/Script/Modules/Utils.js';var __require = CC_EDITOR ? function (request) {return cc.require(request, require);} : function (request) {return cc.require(request, __filename);};function __define (exports, require, module) {"use strict";
cc._RF.push(module, '4819cix6s1DCpLtqwUYzk74', 'Utils', __filename);
// Script/Modules/Utils.ts

Object.defineProperty(exports, "__esModule", { value: true });
/**
 * 创建UUID
 *
 * @param {number} len UUID长度
 * @param {number} radix 输出的进制（2,8,10,16）
 * @returns {string} 返回对应进制下制定长度的字符串
 * @memberof LoginCanvas
 */
function createUUID(len, radix) {
    var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var uuid = [], i;
    radix = radix || chars.length;
    if (len) {
        for (i = 0; i < len; i++)
            uuid[i] = chars[0 | Math.random() * radix];
    }
    else {
        var r = void 0;
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
exports.createUUID = createUUID;
/**
 * 修改微信获取的头像地址
 *
 * @export
 * @param {string} url 原始头像地址
 * @returns {string} 修改为96尺寸的地址
 */
function getHeadImgUrl(url) {
    if (url === '' || url === '/0')
        return '';
    var arrayList = url.split('/');
    arrayList.pop();
    return arrayList.join('/') + '/96';
}
exports.getHeadImgUrl = getHeadImgUrl;
/**
 * 截图保存到本地
 *
 * @export
 * @param {cc.Node} node 需要截图的节点
 * @param {string} saveName 需要保存图片的名字
 * @param {Function} callback 保存成功后的回调
 * @return {boolean} 是否保存
 */
function captureScreen(node, saveName, callback) {
    if (cc.sys.isNative && cc.sys.isMobile) {
        var renderTexture_1 = cc.RenderTexture.create(node.width, node.height, cc.Texture2D.PixelFormat.RGBA8888, gl.DEPTH24_STENCIL8_OES);
        node.parent._sgNode.addChild(renderTexture_1);
        renderTexture_1.setVisible(false);
        renderTexture_1.begin();
        node._sgNode.visit();
        renderTexture_1.end();
        renderTexture_1.saveToFile(saveName, cc.ImageFormat.PNG, true, function (rt, path) {
            renderTexture_1.removeFromParent();
            var interval = 0;
            var timeId = setInterval(function () {
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
    }
    else {
        callback();
    }
}
exports.captureScreen = captureScreen;
/**
 * num是传入的正整数,返回千分位逗号分隔的字符串
 *
 * @export
 * @param {any} num
 * @returns
 */
function getThousandString(num) {
    if (isNaN(num))
        num = 0;
    var numString = num.toString();
    var result = "";
    while (numString.length > 3) {
        result = "," + numString.slice(-3) + result;
        numString = numString.slice(0, numString.length - 3);
    }
    if (numString) {
        result = numString + result;
    }
    return result;
}
exports.getThousandString = getThousandString;
/**
 * 传入千分位的字符串，返回去除逗号的字符串
 *
 * @export
 * @param {string} str
 * @returns
 */
function getBackNumString(str) {
    var list = str.split(",");
    str = "";
    for (var i = 0; i < list.length; i++) {
        str += list[i];
    }
    return str;
}
exports.getBackNumString = getBackNumString;
/**
 * 传入带小数点的数字字符串，返回去除小数点的数组
 * @export
 * @param {string} str
 * @returns
 */
function getPointNumString(str) {
    var list = str.split(".");
    var numList = [];
    for (var i = 0; i < list.length; i++) {
        numList.push(Number(list[i]));
    }
    return numList;
}
exports.getPointNumString = getPointNumString;
/**
 * 传入数字，返回每位数字的组成的数组
 * @export
 * @param {number} num
 * @returns
 */
function getNumberList(num) {
    var numList = num.toString().split("").map(function (i) {
        return Number(i);
    });
    return numList;
}
exports.getNumberList = getNumberList;
/**
 * 根据时间戳，返回对应时间格式的字符串
 *
 * @param {string} timestamp  时间戳(字符串类型)
 * @param {number} type 1是只获取日期，2是只获取时间,默认都获取（数字类型）
 * @returns {string}
 */
function getDateStringByTimestamp(timestamp, type) {
    if (type === void 0) { type = 0; }
    var num = Number(timestamp);
    if (isNaN(num))
        return '';
    else {
        var timeDate = new Date();
        timeDate.setTime(num);
        var timeDateString = this.getDateStringByDate(timeDate);
        var timeString = this.getTimeStringByDate(timeDate);
        if (type === 1) {
            return timeDateString;
        }
        else if (type === 2) {
            return timeString;
        }
        else {
            return timeDateString + ' ' + timeString;
        }
    }
}
exports.getDateStringByTimestamp = getDateStringByTimestamp;
/**
 * 根据时间对象获取日期
 *
 * @param {Date} nowDate 时间对象
 * @param {string} connector 分隔符
 * @returns {string}
 */
function getDateStringByDate(nowDate, connector) {
    if (nowDate instanceof Date) {
        if (!connector) {
            connector = "-";
        }
        var year = nowDate.getFullYear() + '';
        var month = nowDate.getMonth() + 1;
        var monthStr = month + '';
        if (month < 10)
            monthStr = "0" + month;
        var day = nowDate.getDate();
        var dayStr = day + '';
        if (day < 10)
            dayStr = "0" + day;
        return year + connector + monthStr + connector + dayStr;
    }
    return '';
}
exports.getDateStringByDate = getDateStringByDate;
/**
 * 根据时间对象获取时间
 *
 * @param {Date} nowDate 时间对象
 * @param {string} connector 分隔符
 * @returns {string}
 */
function getTimeStringByDate(nowDate, connector) {
    if (nowDate instanceof Date) {
        if (!connector) {
            connector = ":";
        }
        var hour = nowDate.getHours();
        var hourStr = hour + '';
        if (hour < 10)
            hourStr = "0" + hour;
        var minute = nowDate.getMinutes();
        var minuteStr = minute + '';
        if (minute < 10)
            minuteStr = "0" + minute;
        return hourStr + connector + minuteStr /*+ connector + secondStr*/;
    }
    return '';
}
exports.getTimeStringByDate = getTimeStringByDate;
/**
 * 获取倒计时字符串
 *
 * @param {number} time 倒计时毫秒数
 * @returns {string}
 */
function getCountDownString(time) {
    if (time < 0)
        return '00:00:00';
    var s = Math.floor(time / 1000);
    var hour = Math.floor(s / 3600);
    var minute = Math.floor((s - hour * 3600) / 60);
    var second = s - hour * 3600 - minute * 60;
    var str = '';
    if (hour > 9)
        str += hour + ':';
    else
        str += '0' + hour + ':';
    if (minute > 9)
        str += minute + ':';
    else
        str += '0' + minute + ':';
    if (second > 9)
        str += second;
    else
        str += '0' + second;
    return str;
}
exports.getCountDownString = getCountDownString;
/**
 * 返回指定长度的字符串
 *
 * @export
 * @param {string} str
 * @param {number} size
 * @returns
 */
function getStringBySize(str, size) {
    if (str.length > size) {
        var len = 0;
        var vaule = '';
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 255) {
                len += 2;
            }
            else {
                len += 1;
            }
            if (len > size) {
                break;
            }
            else {
                vaule += str.charAt(i);
            }
        }
        return vaule;
    }
    else {
        return str;
    }
}
exports.getStringBySize = getStringBySize;
/**
 * 一段距离,被分割成几份,传入一个位置,返回这个位置最接近点的值
 *
 * @export
 * @param {number} maxLen 线段总长
 * @param {number} sections 需要被分为几段
 * @param {number} point 传入值
 * @returns {number}
 */
function getClosestNumber(maxLen, sections, point) {
    if (point < 0)
        return 0;
    if (point > maxLen)
        return maxLen;
    if (sections < 2)
        return maxLen;
    var secLen = maxLen / sections;
    var residue = point % secLen;
    var count = Math.floor(point / secLen);
    if (residue < secLen / 2) {
        return count * secLen;
    }
    else {
        return (count + 1) * secLen;
    }
}
exports.getClosestNumber = getClosestNumber;
/**
 * 一段距离,被分割成几份,传入一个位置,返回这个位置最接近点的下标
 *
 * @export
 * @param {number} maxLen 线段总长
 * @param {number} sections 需要被分为几段
 * @param {number} point 传入值
 * @returns {number}
 */
function getClosestIndex(maxLen, sections, point) {
    if (point < 0)
        return 0;
    if (point > maxLen)
        return sections;
    if (sections < 2)
        return maxLen;
    var secLen = maxLen / sections;
    var residue = point % secLen;
    var count = Math.floor(point / secLen);
    if (residue < secLen / 2) {
        return count;
    }
    else {
        return count + 1;
    }
}
exports.getClosestIndex = getClosestIndex;

cc._RF.pop();
        }
        if (CC_EDITOR) {
            __define(__module.exports, __require, __module);
        }
        else {
            cc.registerModuleFunc(__filename, function () {
                __define(__module.exports, __require, __module);
            });
        }
        })();
        //# sourceMappingURL=Utils.js.map
        