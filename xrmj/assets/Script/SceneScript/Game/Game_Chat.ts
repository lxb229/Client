import *as dd from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_Chat extends cc.Component {

    /**
     * 聊天的文字列表容器
     * 
     * @type {cc.ScrollView}
     * @memberof Game_Chat
     */
    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;

    @property(cc.Prefab)
    chat_item: cc.Prefab = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            dd.mp_manager.playButton();
            this.node.removeFromParent(true);
            this.node.destroy();
            event.stopPropagation();
        }, this);
        this.showChat();
    }

    click_quickInfo = (event: cc.Event.EventTouch) => {
        dd.mp_manager.playButton();
        let chatNode: cc.Node = event.currentTarget;
        cc.log(chatNode.tag);
        this.sendChatInfo(0, chatNode.tag);
        dd.mp_manager.playButton();
        this.node.removeFromParent(true);
        this.node.destroy();
    };

    /**
     * 发送聊天信息
     * 
     * @param {number} type 0文字 1表情
     * @param {number} msg 消息
     * @memberof MJCanvas
     */
    sendChatInfo(type: number, content: number) {
        let obj = {
            'tableId': dd.gm_manager.mjGameData.tableBaseVo.tableId,
            'type': type,
            'content': content,
        };
        let msg = JSON.stringify(obj);
        dd.ws_manager.sendMsg(dd.protocol.CHAT_SEND, msg, (flag: number, content?: any) => {
            if (flag === 0) {//成功
            } else if (flag === -1) {//超时
                cc.log(content);
            } else {//失败,content是一个字符串
            }
        });
    }

    showChat() {
        let quickList = dd.mp_manager.quicklyList;
        for (var i = 0; i < quickList.length; i++) {
            let quickInfo = quickList[i];
            let chatNode = cc.instantiate(this.chat_item);
            chatNode.tag = quickInfo.id;
            chatNode.getComponent(cc.Label).string = quickInfo.msg;
            chatNode.parent = this.svNode.content;
            chatNode.on(cc.Node.EventType.TOUCH_END, this.click_quickInfo, this);
        }
    }
}
