let jimp = require("jimp");
let async = require('async');
let config = require('./config');

let paths = config.getFilePaths();

let j = new jimp(config.S_WIDTH * config.XCOUNT, config.S_HEIGHT * config.YCOUNT, function (err, big_image) {
    let q = async.queue(function (filePath, callback) {
        jimp.read(filePath, function (err, small_image) {
            if (err) callback(err);
            else {
                let index = paths.indexOf(filePath);
                let offsetX = (config.S_WIDTH - small_image.bitmap.width) / 2;
                let offsetY = (config.S_HEIGHT - small_image.bitmap.height) / 2;
                big_image.blit(small_image,
                    (index % config.XCOUNT) * config.S_WIDTH + offsetX,
                    Math.floor(index / config.XCOUNT) * config.S_HEIGHT + offsetY,
                    function (err, com_image) {
                        if (err) callback(err);
                        else {
                            callback(null, filePath);
                        }
                    });
            }
        });
    }, 10);

    q.drain = function () {
        big_image.rgba(true);
        big_image.filterType(jimp.PNG_FILTER_NONE);
        big_image.deflateLevel(9);
        big_image.deflateStrategy(0);
        big_image.write(config.OUT_PATH + config.FileName + '.png', function (err, save_image) {
            if (err) console.log(err);
            else {
                console.log(config.OUT_PATH + config.FileName + '.png saved');
                config.saveFnt();
            }
        });
    };

    q.push(paths, function (err, filePath) {
        if (err) console.log(err);
        else {
            console.log(filePath + '---OK!');
        }
    });
});





