var PORT = 8080;
var MIN_VER = '1.0.0';
var MAX_VER = '1.0.0';
var http = require('http');
var url = require('url');

let vType = {
    err: -2,//大于最高版本
    less: -1,//小于最低版本
    mid: 1,//在最低版本和最高版本之间
    max: 2//最高版本
};

var server = http.createServer(
    (request, response) => {
        if (request.url !== '/favicon.ico' && request.url !== '/') {
            var pathname = url.parse(request.url).pathname;
            if (pathname === '/checkVer') {
                let obj = url.parse(request.url, true).query;
                let type = checkVer(obj.ver);
                let data = {
                    type: type,
                    apkUrl: 'http://123.123.123.123',
                    ipaUrl: 'http://123.123.123.123'
                }
                response.writeHead(200, {
                    'Content-Type': 'text/plain;charset=utf-8',
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Headers": "X-Requested-With",
                    "Access-Control-Allow-Methods": "PUT,POST,GET,DELETE,OPTIONS"
                });
                response.write(JSON.stringify(data));
                response.end();
            }
        }
    });

function checkVer(ver) {
    if (ver === '' || ver === undefined || ver === null) {
        return vType.err;
    }
    let arr1 = ver.split('.').map((val) => {
        return Number(val);
    }, this);
    let a1 = arr1.length > 0 ? arr1[0] : 0,
        a2 = arr1.length > 1 ? arr1[1] : 0,
        a3 = arr1.length > 2 ? arr1[2] : 0;
    let arr2 = MIN_VER.split('.').map((val) => {
        return Number(val);
    }, this);
    let b1 = arr2.length > 0 ? arr2[0] : 0,
        b2 = arr2.length > 0 ? arr2[1] : 0,
        b3 = arr2.length > 0 ? arr2[2] : 0;
    let arr3 = MAX_VER.split('.').map((val) => {
        return Number(val);
    }, this);
    let c1 = arr3.length > 0 ? arr3[0] : 0,
        c2 = arr3.length > 0 ? arr3[1] : 0,
        c3 = arr3.length > 0 ? arr3[2] : 0;
    let v1 = a1 * 100 + a2 * 10 + a3;
    let v2 = b1 * 100 + b2 * 10 + b3;
    let v3 = c1 * 100 + c2 * 10 + c3;
    if (v1 < v2) return vType.less;
    if (v1 > v3) return vType.err;
    if (v1 === v3) return vType.max;
    if (v1 >= v2 && v1 < v3) return vType.mid;
}
server.listen(PORT);
console.log("版本检测服务器开启: " + PORT + " ！");