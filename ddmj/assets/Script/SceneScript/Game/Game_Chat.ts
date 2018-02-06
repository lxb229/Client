import { mp_manager, ui_manager } from './../../Modules/ModuleManager';
const { ccclass, property } = cc._decorator;

@ccclass
export default class Game_Chat extends cc.Component {

    @property(cc.Node)
    chat_info: cc.Node = null;

    /**
     * 聊天的文字列表容器
     * 
     * @type {cc.ScrollView}
     * @memberof Game_Chat
     */
    @property(cc.ScrollView)
    svNode: cc.ScrollView = null;

    /**
     * 聊天表情的列表容器
     * 
     * @type {cc.ScrollView}
     * @memberof Game_Chat
     */
    @property(cc.ScrollView)
    bqNode: cc.ScrollView = null;

    @property(cc.Toggle)
    toggle_chat: cc.Toggle = null;

    @property(cc.Toggle)
    toggle_bq: cc.Toggle = null;
    /**
     * 表情节点列表
     * 
     * @type {cc.Node[]}
     * @memberof Game_Chat
     */
    _bqList: cc.Node[] = [];

    /**
     * canvas脚本
     * 
     * @memberof Game_Chat
     */
    _canvasTarget = null;
    onLoad() {
        this._canvasTarget = ui_manager.getCanvasNode().getComponent('MJCanvas');
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            this.exitChat();
            event.stopPropagation();
        }, this);
        this.initData();
        this.showChatLayer(0);
        this.bindEvent();
    }

    /**
     * 退出聊天
     * 
     * @memberof Game_Chat
     */
    exitChat() {
        this.node.active = false;
    }

    click_quickInfo = (event: cc.Event.EventTouch) => {
        mp_manager.playButton();
        let chatNode: cc.Node = event.currentTarget;
        cc.log(chatNode.tag);
        this._canvasTarget.sendChatInfo(0, chatNode.tag);
        this.exitChat();
    };

    initData() {
        let quickList = mp_manager.quicklyList;
        for (var i = 0; i < quickList.length; i++) {
            let quickInfo = quickList[i];
            if (i === 0) {
                this.chat_info.tag = quickInfo.id;
                this.chat_info.getComponent(cc.Label).string = quickInfo.msg;
                this.chat_info.on(cc.Node.EventType.TOUCH_END, this.click_quickInfo, this);
            } else {
                let chatNode = cc.instantiate(this.chat_info);
                chatNode.tag = quickInfo.id;
                chatNode.getComponent(cc.Label).string = quickInfo.msg;
                chatNode.parent = this.svNode.content;
                chatNode.on(cc.Node.EventType.TOUCH_END, this.click_quickInfo, this);
            }
        }
    }

    /**
     * 绑定事件
     * 
     * @memberof Game_Chat
     */
    bindEvent() {
        let childLen = this.bqNode.content.childrenCount;
        for (var i = 0; i < childLen; i++) {
            let bqName = 'biaoqing_' + (i + 1);
            let bqNode: cc.Node = cc.find(bqName, this.bqNode.content);
            bqNode.tag = i;
            if (bqNode) {
                bqNode.on(cc.Node.EventType.TOUCH_END, () => {
                    this._canvasTarget.sendChatInfo(1, bqNode.tag);
                    this.exitChat();
                }, this);
            }
        }
    }

    /**
     * 显示聊天界面
     * 
     * @param {number} type 
     * @memberof Game_Chat
     */
    showChatLayer(type: number) {
        this.svNode.node.active = type === 0 ? true : false;
        this.bqNode.node.active = type === 0 ? false : true;
        this.toggle_chat.isChecked = type === 0 ? true : false;
        this.toggle_bq.isChecked = type === 0 ? false : true;
    }

    /**
     * 复选框的点击事件
     * 
     * @param {any} event 
     * @param {string} type 0=聊天 1=表情
     * @memberof Game_Chat
     */
    click_btn_chat(event, type: string) {
        mp_manager.playButton();
        this.svNode.node.active = type === '0' ? true : false;
        this.bqNode.node.active = type === '0' ? false : true;
    }
}
