
/**
 * 用户管理类
 * 
 * @export
 * @class NodeManager
 */
export default class NodeManager {
    private static _instance: NodeManager = null;
    private constructor() { }
    /**
     * 获取NodeManager单例对象
     * 
     * @static
     * @returns {NodeManager} 
     * @memberof NodeManager
     */
    static getInstance(): NodeManager {
        if (NodeManager._instance === null) {
            NodeManager._instance = new NodeManager();
            NodeManager._instance.__dispatchEvent__ = cc.Node.prototype.dispatchEvent;
            cc.Node.prototype.dispatchEvent = NodeManager._instance.dispatchEvent;
        }
        return NodeManager._instance;
    }

    private canTouch: boolean = true;
    private maxTouchNum: number = 1;
    private touchNum: number = 0;
    private __dispatchEvent__: (event: cc.Event) => void = null;

    private dispatchEvent(event: cc.Event): void {
        //这里的this指向的是cc.Node
        switch (event.type) {
            case 'touchstart':
                if (NodeManager._instance.touchNum < NodeManager._instance.maxTouchNum) {
                    NodeManager._instance.touchNum++;
                    NodeManager._instance.canTouch = true;
                    NodeManager._instance.__dispatchEvent__.call(this, event);
                }
                break;
            case 'touchmove':
                if (!NodeManager._instance.canTouch && NodeManager._instance.touchNum < NodeManager._instance.maxTouchNum) {
                    NodeManager._instance.canTouch = true;
                    NodeManager._instance.touchNum++;
                }

                if (NodeManager._instance.canTouch) {
                    NodeManager._instance.__dispatchEvent__.call(this, event);
                }

                break;
            case 'touchend':
                if (NodeManager._instance.canTouch) {
                    NodeManager._instance.canTouch = false;
                    NodeManager._instance.touchNum--;
                    NodeManager._instance.__dispatchEvent__.call(this, event);
                }
                break;
            case 'touchcancel':
                if (NodeManager._instance.canTouch) {
                    NodeManager._instance.canTouch = false;
                    NodeManager._instance.touchNum--;
                    NodeManager._instance.__dispatchEvent__.call(this, event);
                }
                break;
            default:
                NodeManager._instance.__dispatchEvent__.call(this, event);
        }
    }


    /**
     * 清空单例对象
     * 
     * @memberof NodeManager
     */
    destroySelf(): void {
        this.canTouch = true;
        this.maxTouchNum = 1;
        this.touchNum = 0;
        cc.Node.prototype.dispatchEvent = this.__dispatchEvent__;
        this.__dispatchEvent__ = null;

        NodeManager._instance = null;
    }
}
