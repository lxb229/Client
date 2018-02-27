
var WebSocketServer = require('ws').Server,
    wss = new WebSocketServer({ port: 8181 });
var crypto = require('crypto');
let key = crypto.createHash('md5').update('123456').digest('hex').substr(0, 16);
let iv = crypto.createHash('md5').update(key).digest('hex').substr(0, 16);
wss.on('connection', function (ws) {
    console.log('client connected');
    ws.on('message', function (message) {
        let buffer = new Buffer(message);
        let utf16le = buffer.toString('utf16le');
        let utf8 = new Buffer(utf16le, 'utf8').toString();
        console.log(utf8);
        let obj = JSON.parse(utf8);
        let data = obj.data;
        let hmacMd5 = crypto.createHmac('md5', obj.pid + '').update(data).digest('hex');
        if (hmacMd5 === obj.md5) {
            console.log('对了！');
        }

        let res = aesutil.decryption(data, key, iv);
        console.log(res);
        console.log(new Date().getTime() - obj.time);
    });
    let data = aesutil.encryption('中时间大佛寺24ALFJ倒垃圾上都！@**%', key, iv);
    let obj = {
        flag: 1,
        pid: 20001,
        time: new Date().getTime(),
        md5: crypto.createHmac('md5', '20001').update(data).digest('hex'),
        data: data
    }
    ws.send(new Buffer(JSON.stringify(obj), 'utf16le'));
});



var aesutil = {};

/**
 * aes加密
 * @param data 待加密内容
 * @param key 必须为32位私钥
 * @returns {string}
 */
aesutil.encryption = function (data, key, iv) {
    iv = iv || "";
    var clearEncoding = 'utf8';
    var cipherEncoding = 'base64';
    var cipherChunks = [];
    var cipher = crypto.createCipheriv('aes-128-cbc', key, iv);
    cipher.setAutoPadding(true);
    cipherChunks.push(cipher.update(data, clearEncoding, cipherEncoding));
    cipherChunks.push(cipher.final(cipherEncoding));
    return cipherChunks.join('');
}

/**
 * aes解密
 * @param data 待解密内容
 * @param key 必须为32位私钥
 * @returns {string}
 */
aesutil.decryption = function (data, key, iv) {
    if (!data) {
        return "";
    }
    iv = iv || "";
    var clearEncoding = 'utf8';
    var cipherEncoding = 'base64';
    var cipherChunks = [];
    var decipher = crypto.createDecipheriv('aes-128-cbc', key, iv);
    decipher.setAutoPadding(true);
    cipherChunks.push(decipher.update(data, cipherEncoding, clearEncoding));
    cipherChunks.push(decipher.final(clearEncoding));
    return cipherChunks.join('');
}