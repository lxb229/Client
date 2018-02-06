"use strict";
cc._RF.push(module, '859b3izbDlPmbhqfmMpAYn1', 'NodeManager');
// Script/Modules/NodeManager.ts

Object.defineProperty(exports, "__esModule", { value: true });
/**
 * 用户管理类
 *
 * @export
 * @class NodeManager
 */
var NodeManager = /** @class */ (function () {
    function NodeManager() {
        this.canTouch = true;
        this.maxTouchNum = 1;
        this.touchNum = 0;
        this.__dispatchEvent__ = null;
    }
    /**
     * 获取NodeManager单例对象
     *
     * @static
     * @returns {NodeManager}
     * @memberof NodeManager
     */
    NodeManager.getInstance = function () {
        if (NodeManager._instance === null) {
            NodeManager._instance = new NodeManager();
            NodeManager._instance.__dispatchEvent__ = cc.Node.prototype.dispatchEvent;
            cc.Node.prototype.dispatchEvent = NodeManager._instance.dispatchEvent;
        }
        return NodeManager._instance;
    };
    NodeManager.prototype.dispatchEvent = function (event) {
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
    };
    /**
     * 清空单例对象
     *
     * @memberof NodeManager
     */
    NodeManager.prototype.destroySelf = function () {
        this.canTouch = true;
        this.maxTouchNum = 1;
        this.touchNum = 0;
        cc.Node.prototype.dispatchEvent = this.__dispatchEvent__;
        this.__dispatchEvent__ = null;
        NodeManager._instance = null;
    };
    NodeManager._instance = null;
    return NodeManager;
}());
exports.default = NodeManager;

cc._RF.pop();