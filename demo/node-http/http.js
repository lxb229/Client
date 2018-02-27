var PORT = 8080;

var http = require('http');
var url = require('url');
var fs = require('fs');
var path = require('path');

var server = http.createServer(
    (request, response) => {
        if (request.url !== '/favicon.ico' && request.url !== '/') {
            var pathname = url.parse(request.url).pathname;
            var realPath = path.normalize(pathname);
            if (realPath[0] === '\\') realPath = realPath.substring(1);
            fs.exists(realPath,
                (exists) => {
                    if (!exists) {
                        response.writeHead(404, { 'Content-Type': 'text/plain;charset=utf-8' });
                        response.write("请求文件地址：" + realPath + " 在服务器上未找到！");
                        response.end();
                        console.log("请求文件地址：" + realPath + " 在服务器上未找到！");
                        fs.appendFile('err.log',
                            new Date().toLocaleString() + ' ' + request.connection.remoteAddress + "请求文件地址：" + realPath + " 在服务器上未找到！\r\n", 'utf8',
                            (err) => {
                                if (err) console.log(err.message);
                            })
                    } else {
                        fs.readFile(realPath, "binary",
                            (err, file) => {
                                if (err) {
                                    response.writeHead(500, { 'Content-Type': 'text/plain;charset=utf-8' });
                                    response.write("打开" + realPath + " 失败！");
                                    response.end();
                                    console.log("打开" + realPath + " 失败！");
                                } else {
                                    response.writeHead(200, { 'Content-Type': 'application/force-download' });
                                    response.write(file, "binary");
                                    response.end();
                                    console.log(realPath + " 提供下载成功！");
                                    fs.appendFile('suc.log',
                                        new Date().toLocaleString() + ' ' + request.connection.remoteAddress + "请求文件地址：" + realPath + " 下载成功！\r\n", 'utf8',
                                        (err) => {
                                            if (err) console.log(err.message);
                                        })
                                }
                            });
                    }
                });
        }

    });
server.listen(PORT);
console.log("资源服务器已启动，端口号: " + PORT + " ！");