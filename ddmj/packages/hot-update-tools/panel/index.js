'use strict';

const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const Electron = require('electron');

const Bagpipe = require(Editor.url('packages://hot-update-tools/panel/Bagpipe.js'));
const rootPath = Editor.url('packages://hot-update-tools/panel/hotUpdateConfig.json');
Editor.Panel.extend({
    style: fs.readFileSync(Editor.url('packages://hot-update-tools/panel/index.css', 'utf8')) + "",
    template: fs.readFileSync(Editor.url('packages://hot-update-tools/panel/index.html', 'utf8')) + "",
    $: {},
    ready() {
        // 初始化vue面板
        new window.Vue({
            el: this.shadowRoot,
            created: function () {
                this._initPluginCfg();
            },
            init: function () {
            },
            data: {
                version: "",
                genManifestDir: "",
                serverRootDir: "",
                resourceRootDir: "",
                localServerPath: "",
                logView: "",

                copyProgress: 0,
                totalNum: 0,// 操作文件总数
                curNum: 0,// 当前操作的次数
            },
            methods: {
                _addLog(str) {
                    var time = new Date();
                    this.logView = "[" + time.toLocaleString() + "]: " + str + "\n" + this.logView;
                },
                _pathExist(path) {
                    try {
                        fs.accessSync(path, fs.constants.F_OK);
                    } catch (e) {
                        return false;
                    }
                    return true;
                },
                _getAppCfgPath() {
                    // return path.join(Electron.remote.app.getPath('userData'), 'hotUpdateConfig.json');
                    cc.log(rootPath);
                    return rootPath;
                },
                onCleanAPPCfg() {
                    fs.unlink(this._getAppCfgPath());
                },
                _saveConfig() {
                    let configFilePath = this._getAppCfgPath();
                    var data = {
                        version: this.version,
                        serverRootDir: this.serverRootDir,
                        resourceRootDir: this.resourceRootDir,
                        genManifestDir: this.genManifestDir,
                        localServerPath: this.localServerPath,
                    };
                    fs.writeFile(configFilePath, JSON.stringify(data), (err) => {
                        if (err) throw err;
                    });
                },
                _initPluginCfg() {
                    let configFilePath = this._getAppCfgPath();
                    var b = this._pathExist(configFilePath);
                    if (b) {
                        var data = fs.readFileSync(configFilePath, 'utf-8');
                        var saveData = JSON.parse(data);
                        this.version = saveData.version;
                        this.serverRootDir = saveData.serverRootDir;
                        this.resourceRootDir = saveData.resourceRootDir;
                        this.genManifestDir = saveData.genManifestDir;
                        this.localServerPath = saveData.localServerPath;
                    } else {
                        this._saveConfig();
                    }
                },
                //检查是否满足三段式版本号格式
                checkVer() {
                    if (!this.version || this.version.length <= 0) {
                        this._addLog("版本号未填写");
                        return false;
                    }
                    let list = this.version.split('.');
                    if (list.length !== 3) {
                        return false;
                    }
                    if (isNaN(list[0]) || isNaN(list[1]) || isNaN(list[2])) {
                        return false;
                    }
                    return true;
                },
                checkUrl() {
                    if (this.serverRootDir.length <= 7) {
                        this._addLog("服务器地址未填写");
                        return false;
                    }
                    let httpStr = this.serverRootDir.slice(0, 7);
                    if (httpStr !== "http://") {
                        this._addLog("服务器地址格式不正确");
                        return false;
                    }
                    return true;
                },
                trimData() {
                    this.version = this.delSpace(this.version);
                    this.serverRootDir = this.delSpace(this.serverRootDir);
                    this.resourceRootDir = this.delSpace(this.resourceRootDir);
                    this.genManifestDir = this.delSpace(this.genManifestDir);
                    this.localServerPath = this.delSpace(this.localServerPath);
                },
                //删除前后的空格
                delSpace(path) {
                    let first = 0, end = path.length - 1;
                    while (path[first] === ' ') {
                        first++;
                    }
                    while (path[end] === ' ') {
                        end--;
                    }
                    return path.slice(first, end + 1);
                },

                checkData() {
                    this.trimData();
                    if (!this.checkVer()) {
                        this._addLog("版本号格式不正确，请输入三段式版本号，例如：1.0.0");
                        return false;
                    }
                    if (!this.checkUrl()) {
                        return false;
                    }
                    if (!this.resourceRootDir || this.resourceRootDir.length <= 0) {
                        this._addLog("资源路径未填写");
                        return false;
                    }
                    if (!fs.existsSync(this.resourceRootDir)) {
                        this._addLog("资源目录不存在: " + this.resourceRootDir + ", 请选择构建后的目录");
                        return false;
                    } else {
                        var srcPath = path.join(this.resourceRootDir, 'src');
                        if (!fs.existsSync(srcPath)) {
                            this._addLog(this.resourceRootDir + "不存在src目录");
                            return false;
                        }
                        var resPath = path.join(this.resourceRootDir, 'res');
                        if (!fs.existsSync(resPath)) {
                            this._addLog(this.resourceRootDir + "不存在res目录");
                            return false;
                        }
                    }
                    if (!this.genManifestDir || this.genManifestDir.length <= 0) {
                        this._addLog("manifest文件生成地址未填写");
                        return false;
                    }
                    if (!fs.existsSync(this.genManifestDir)) {
                        this._addLog("manifest存储目录不存在: " + this.genManifestDir);
                        return false;
                    }
                    if (!this.localServerPath || this.localServerPath.length <= 3) {
                        this._addLog("部署路径未填写，且不能是磁盘根目录");
                        return false;
                    }
                    this._makeDir(this.localServerPath);
                    if (!fs.existsSync(this.localServerPath)) {
                        this._addLog("部署路径无法创建: " + this.localServerPath);
                        return false;
                    }
                    return true;
                },
                onClickGenCfg(event) {
                    if (this.checkData()) {
                        this._addLog("开始生成manifest配置文件....");
                        this._saveConfig();
                        this._genVersion(this.version, this.serverRootDir, this.resourceRootDir, this.genManifestDir);
                    }
                },

                onClearLog() {
                    this.logView = '';
                },

                // serverUrl 必须以/结尾
                // genManifestDir 建议在assets目录下
                // buildResourceDir 默认为 build/jsb-default/
                // -v 10.1.1 -u http://192.168.191.1//cocos/remote-assets/  -s build/jsb-default/ -d assets
                _genVersion(version, serverUrl, buildResourceDir, genManifestDir) {
                    var manifest = {
                        packageUrl: 'http://localhost/cocosCreaterHotUpdate/remote-assets/',
                        remoteManifestUrl: 'http://localhost/cocosCreaterHotUpdate/remote-assets/project.manifest',
                        remoteVersionUrl: 'http://localhost/cocosCreaterHotUpdate/remote-assets/version.manifest',
                        version: '1.0.0',
                        assets: {},
                        searchPaths: []
                    };

                    manifest.version = version;

                    if (serverUrl[serverUrl.length - 1] === "/") {
                        manifest.packageUrl = serverUrl;
                        manifest.remoteManifestUrl = serverUrl + 'project.manifest';
                        manifest.remoteVersionUrl = serverUrl + 'version.manifest';

                    } else {
                        manifest.packageUrl = serverUrl + '/';
                        manifest.remoteManifestUrl = serverUrl + '/project.manifest';
                        manifest.remoteVersionUrl = serverUrl + '/version.manifest';
                    }
                    var dest = genManifestDir;
                    var src = buildResourceDir;

                    var readDir = function (dir, obj) {
                        var stat = fs.statSync(dir);
                        if (!stat.isDirectory()) {
                            return;
                        }
                        var subpaths = fs.readdirSync(dir), subpath, size, md5, compressed, relative;
                        for (var i = 0; i < subpaths.length; ++i) {
                            if (subpaths[i][0] === '.') {
                                continue;
                            }
                            subpath = path.join(dir, subpaths[i]);
                            stat = fs.statSync(subpath);
                            if (stat.isDirectory()) {
                                readDir(subpath, obj);
                            }
                            else if (stat.isFile()) {
                                // Size in Bytes
                                size = stat['size'];
                                md5 = crypto.createHash('md5').update(fs.readFileSync(subpath, 'utf8')).digest('hex');
                                compressed = path.extname(subpath).toLowerCase() === '.zip';
                                relative = path.relative(src, subpath);
                                relative = relative.replace(/\\/g, '/');
                                relative = encodeURI(relative);
                                obj[relative] = {
                                    'size': size,
                                    'md5': md5
                                };
                                if (compressed) {
                                    obj[relative].compressed = true;
                                }
                            }
                        }
                    };

                    // Iterate res and src folder
                    readDir(path.join(src, 'src'), manifest.assets);
                    readDir(path.join(src, 'res'), manifest.assets);

                    var destManifest = path.join(dest, 'project.manifest');
                    var destVersion = path.join(dest, 'version.manifest');

                    this._makeDir(dest);

                    // 生成project.manifest
                    fs.writeFileSync(destManifest, JSON.stringify(manifest));
                    this._addLog("生成 project.manifest成功");
                    // 复制project.manifest
                    var copyPath = path.join(src, '/res/raw-assets/project.manifest');
                    fs.writeFileSync(copyPath, JSON.stringify(manifest));
                    this._addLog('复制 project.manifest成功');
                    // 生成version.manifest
                    delete manifest.assets;
                    delete manifest.searchPaths;
                    fs.writeFileSync(destVersion, JSON.stringify(manifest));
                    this._addLog("生成 version.manifest成功");
                    this._copyFileToLocalServer();
                },

                _makeDir(path) {
                    try {
                        fs.mkdirSync(path);
                    } catch (e) {
                        if (e.code !== 'EEXIST') {
                            this._addLog("创建文件夹错误：" + path);
                            throw e;
                        }
                    }
                },

                // 拷贝文件到测试服务器
                _copyFileToLocalServer() {
                    this._addLog("开始拷贝文件到:" + this.localServerPath);
                    this.curNum = 0;
                    this.copyProgress = 0;
                    this.totalNum = this._getTotalNum();
                    let bagpipe = new Bagpipe(100);
                    this._delDir(this.localServerPath);
                    this._copyDir(path.join(this.resourceRootDir, "src"), path.join(this.localServerPath, "src"), bagpipe);
                    this._copyDir(path.join(this.resourceRootDir, "res"), path.join(this.localServerPath, "res"), bagpipe);
                    this._copyFile(path.join(this.genManifestDir, "project.manifest"), this.localServerPath);
                    this._copyFile(path.join(this.genManifestDir, "version.manifest"), this.localServerPath);
                },

                // 获取要操作的文件总数量
                _getTotalNum() {
                    var delNum = this._getFileNum(this.localServerPath);
                    this._addLog("需要删除文件总数据: " + delNum);
                    var srcNum = this._getFileNum(path.join(this.resourceRootDir, "src"));
                    var resNum = this._getFileNum(path.join(this.resourceRootDir, "res"));
                    var copyNum = srcNum + resNum + 2 + 2;// 2个manifest,2个目录(src, res)
                    this._addLog("需要拷贝文件总数据: " + copyNum);
                    return delNum + copyNum;
                },
                addProgress() {
                    this.curNum++;
                    var p = this.curNum / this.totalNum;
                    p = p ? p : 0;
                    this.copyProgress = p * 100;
                    if (p >= 1) {
                        this._addLog("拷贝完成");
                    }
                },
                // 获取文件个数
                _getFileNum(url) {
                    var i = 0;
                    var lookDir = function (fileUrl) {
                        var files = fs.readdirSync(fileUrl);//读取该文件夹
                        for (var k in files) {
                            i++;
                            var filePath = path.join(fileUrl, files[k]);
                            var stats = fs.statSync(filePath);
                            if (stats.isDirectory()) {
                                lookDir(filePath);
                            }
                        }
                    };
                    lookDir(url);
                    return i;
                },
                _delDir(rootFile) {
                    var self = this;
                    //删除所有的文件(将所有文件夹置空)
                    var emptyDir = function (fileUrl) {
                        var files = fs.readdirSync(fileUrl);//读取该文件夹
                        for (var k in files) {
                            var filePath = path.join(fileUrl, files[k]);
                            var stats = fs.statSync(filePath);
                            if (stats.isDirectory()) {
                                emptyDir(filePath);
                            } else {
                                fs.unlinkSync(filePath);
                                self.addProgress();
                            }
                        }
                    };
                    //删除所有的空文件夹
                    var rmEmptyDir = function (fileUrl) {
                        var files = fs.readdirSync(fileUrl);
                        if (files.length > 0) {
                            for (var k in files) {
                                var rmDir = path.join(fileUrl, files[k]);
                                rmEmptyDir(rmDir);
                            }
                            if (fileUrl != rootFile) {// 不删除根目录
                                fs.rmdirSync(fileUrl);
                                self.addProgress();
                            }
                        } else {
                            if (fileUrl != rootFile) {// 不删除根目录
                                fs.rmdirSync(fileUrl);
                                self.addProgress();
                            }
                        }
                    };
                    emptyDir(rootFile);
                    rmEmptyDir(rootFile);
                },
                // 拷贝文件到目录
                _copyFile(src, des) {
                    if (this._pathExist(src) && this._pathExist(des)) {
                        let readable = fs.createReadStream(src);// 创建读取流
                        let fileName = path.basename(src);
                        des = path.join(des, fileName);
                        let writable = fs.createWriteStream(des);// 创建写入流
                        readable.pipe(writable);// 通过管道来传输流
                        this.addProgress();
                    } else {
                        this._addLog('复制：' + src + '到：' + des + '失败');
                    }
                },
                // 拷贝文件夹
                _copyDir(src, des, bagpipe) {
                    if (!fs.existsSync(des)) {
                        fs.mkdirSync(des);
                        this.addProgress();
                    }
                    bagpipe.push(fs.readdir, src, (err, files) => {
                        if (err) {
                            throw err;
                        }
                        files.forEach((file) => {
                            let _src = path.join(src, file);
                            bagpipe.push(fs.stat, _src, (err, st) => {
                                if (err) {
                                    throw err;
                                }
                                if (st.isFile()) {// 判断是否为文件
                                    this._copyFile(_src, des);
                                } else if (st.isDirectory()) {// 如果是目录则递归调用自身
                                    let _des = path.join(des, file);
                                    this._copyDir(_src, _des, bagpipe);
                                }
                            });
                        });
                    });
                },
                // 选择projManifest文件
                onOpenResourceDir() {
                    if (!fs.existsSync(this.resourceRootDir)) {
                        this._addLog("目录不存在：" + this.resourceRootDir);
                        return;
                    }
                    Electron.shell.showItemInFolder(this.resourceRootDir);
                    Electron.shell.beep();
                },
                onOpenManifestDir() {
                    if (!fs.existsSync(this.genManifestDir)) {
                        this._addLog("目录不存在：" + this.genManifestDir);
                        return;
                    }
                    Electron.shell.showItemInFolder(this.genManifestDir);
                    Electron.shell.beep();
                },
                onOpenLocalServerPath() {
                    if (!fs.existsSync(this.localServerPath)) {
                        this._addLog("目录不存在：" + this.localServerPath);
                        return;
                    }
                    Electron.shell.showItemInFolder(this.localServerPath);
                    Electron.shell.beep();
                },
                // 选择生成Manifest的目录
                onSelectGenManifestDir() {
                    let res = Editor.Dialog.openFile({
                        title: "选择生成Manifest目录",
                        defaultPath: Editor.projectInfo.path,
                        properties: ['openDirectory'],
                    });
                    if (res != -1) {
                        this.genManifestDir = res[0];
                        this._saveConfig();
                    }
                },
                onSelectResourceRootDir() {
                    let res = Editor.Dialog.openFile({
                        title: "选择构建后的根目录",
                        defaultPath: Editor.projectInfo.path,
                        properties: ['openDirectory'],
                    });
                    if (res != -1) {
                        this.resourceRootDir = res[0];
                        this._saveConfig();
                    }
                },
                // 选择物理server路径
                onSelectLocalServerPath(event) {
                    let res = Editor.Dialog.openFile({
                        title: "选择本地测试服务器目录",
                        defaultPath: Editor.projectInfo.path,
                        properties: ['openDirectory'],
                    });
                    if (res != -1) {
                        this.localServerPath = res[0];
                        this._saveConfig();
                    }
                },
            }
        });
    },
});