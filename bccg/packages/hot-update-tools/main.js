'use strict';

module.exports = {
    load () {
        // 当 package 被正确加载的时候执行
    },

    unload () {
        // 当 package 被正确卸载的时候执行
    },

    messages: {
        'test'(event, args){
            Editor.log(args);
        },
        'showPanel'(){
            Editor.Panel.open('hot-update-tools');
        }
    },
};