const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_Chat_Show extends cc.Component {
    @property(cc.Label)
    nodeChat: cc.Label = null;

    @property(cc.Label)
    lblChat: cc.Label = null;

    onLoad() {
        // init logic
    }

    /**
     * 显示聊天信息
     * 
     * @param {string} msg 
     * @memberof Game_Chat_Show
     */
    showChat(msg: string, fx: number) {
        this.nodeChat.string = msg;
        this.lblChat.string = msg;
        this.lblChat.node.scaleX = fx;
    }
}
