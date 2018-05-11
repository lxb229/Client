let fs = require('fs');
let MIN_VER = '1.0.0';
let MAX_VER = '1.0.0';
let APK_URL = '';
let IPA_URL = '';
let DD_URL = '';
fs.readFile('./setting.json', { encoding: 'utf8' },
    (err, data) => {
        if (err) {
            console.log('加载配置文件失败:' + err.message);
        } else {
            data = JSON.parse(data);
            MIN_VER = data.MIN_VER;
            MAX_VER = data.MAX_VER;
            APK_URL = data.APK_URL;
            IPA_URL = data.IPA_URL;
            DD_URL = data.DD_URL;
            console.log('初始化配置完毕!');
        }
    });
let path = require('path');
let express = require('express');
let app = express();
app.use('/ppq', express.static('ppq'));
let PORT = 8080;
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
// 允许所有的请求形式
app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
    next();
});
app.listen(PORT, function () {
    console.log('http服务器启动成功，端口号：' + PORT);
});

let vType = {
    err: -2,//大于最高版本
    less: -1,//小于最低版本
    mid: 1,//在最低版本和最高版本之间
    max: 2//最高版本
};

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
//检查版本号
app.get('/checkVer', function (req, res) {
    let type = checkVer(req.query.ver);
    let data = {
        type: type,
        apkUrl: APK_URL,
        ipaUrl: IPA_URL,
        ddUrl: DD_URL
    };
    switch (type) {
        case 2: data.msg = '当前已经是最新客户端!'; break;
        case 1: data.msg = '发现新版本,是否前往下载最新客户端?'; break;
        default: data.msg = '当前版本过低,请前往下载最新客户端!'; break;
    }
    res.send(JSON.stringify(data));
});

function saveLog(status, ip, filePath) {
    let logName = status ? 'suc.log' : 'err.log';
    fs.appendFile('./logs/' + logName,
        new Date().toLocaleString() + '|' + ip + '|请求文件地址：' + filePath + '\r\n', 'utf8',
        (err) => {
            if (err) console.log(err.message);
        });
}
//热更新下载
app.get('/ddmj/*', function (req, res) {
    let filePath = path.join(__dirname, 'ddmj', path.normalize(req.params[0]));
    let ext = path.extname(filePath);
    if (ext === '') {
        res.status(404).end();
        saveLog(false, req.connection.remoteAddress, filePath);
        return;
    }
    fs.open(filePath, 'r', (err, fd) => {
        if (err) {
            res.status(404).end();
            saveLog(false, req.connection.remoteAddress, filePath);
        } else {
            res.sendFile(filePath, (err) => {
                if (err) {
                    res.status(404).end();
                    saveLog(false, req.connection.remoteAddress, filePath);
                }
                else {
                    saveLog(true, req.connection.remoteAddress, filePath);
                }
            });
            fs.close(fd, (err) => {
                if (err) {
                    console.log('文件关闭失败');
                } else {
                    console.log('文件关闭成功');
                }
            });
        }
    });
});

app.get('/setting', function (req, res) {
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.post('/setting', function (req, res) {
    let ischange = false;
    if (req.body.min_ver) {
        ischange = true;
        MIN_VER = req.body.min_ver;
    }
    if (req.body.max_ver) {
        ischange = true;
        MAX_VER = req.body.max_ver;
    }
    if (req.body.apk_url) {
        ischange = true;
        APK_URL = req.body.apk_url;
    }
    if (req.body.ipa_url) {
        ischange = true;
        IPA_URL = req.body.ipa_url;
    }
    if (req.body.dd_url) {
        DD_URL = req.body.dd_url;
        ischange = true;
    }
    if (ischange) {
        let obj = {
            "MIN_VER": MIN_VER,
            "MAX_VER": MAX_VER,
            "APK_URL": APK_URL,
            "IPA_URL": IPA_URL,
            "DD_URL": DD_URL
        }
        fs.writeFile('./setting.json', JSON.stringify(obj), { encoding: 'utf8' },
            (err, data) => {
                if (err) console.log(err.message);
                else {
                    console.log('保存配置成功');
                }
            });
    }
    res.send('ok');
});

app.get('/file/*', function (req, res) {
    let fileName = req.params[0];
    fs.readFile('./files/' + fileName, { encoding: 'utf8' }, (err, data) => {
        if (err) {
            console.log(err.message);
            res.send('');
        } else {
            let objs = { 'datas': [] };
            let list = data.split('\r\n');
            list.forEach((item) => {
                if (item && item.length > 0) {
                    objs.datas.push(JSON.parse(item));
                }
            }, this);
            res.send(objs);
        }
    });
});

let datas = [];
let isWork = false;
app.post('/', function (req, res) {
    datas.push(req.body);
    res.send('ok');
});

setInterval(() => {
    if (datas.length > 0 && !isWork) {
        isWork = true;
        let data = datas.shift();
        fs.appendFile('./files/' + data.fileName,
            data.frameData + '\r\n',
            'utf8',
            (err) => {
                if (err) console.log(err.message);
                isWork = false;
                fs.appendFile('./logs/' + new Date().toLocaleDateString() + '.log',
                    new Date().toLocaleString() + '|' + JSON.stringify(data) + '\r\n',
                    'utf8',
                    (err) => {
                        if (err) console.log(err.message);
                    });
            });
    }
}, 1000 / 60);

