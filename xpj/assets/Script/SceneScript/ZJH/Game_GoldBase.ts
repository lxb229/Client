import { mp_manager } from './../../Modules/ModuleManager';

const { ccclass, property } = cc._decorator;

const gatherGoldTime: number = 0.5;//聚集金币的时间
const flyGoldTimes: number = 10;   //飞金币的次数
const flyBaseTime: number = 0.12;     //飞金币的基本时间
const everyEnemyActionTime: number = 0.6; //每个对象动作时间

@ccclass
export default class Game_GoldBase extends cc.Component {

    @property({
        type: cc.Prefab,
        tooltip: '对象预设'
    })
    enemyPrefab: cc.Prefab = null;

    @property({
        type: [cc.SpriteFrame],
        tooltip: '对象图片列表'
    })
    goldImgList: cc.SpriteFrame[] = [];

    /**
     * 对象池
     * @type {cc.NodePool}
     * @memberof Game_GoldBase
     */
    _enemyPool: cc.NodePool = new cc.NodePool('Game_Gold');

    onLoad() {
    }

    /**
     * 是否释放对象
     * @param {cc.Node} target  目标节点
     * @param {*} [cb]          回调函数
     * @param {boolean} [isRelease=true]  是否释放
     * @param {cc.Node} [parentNode=null] 父节点
     * @param {boolean} [isDestory=true]  是否销毁目标节点
     * @memberof Game_GoldBase
     */
    removeAllEnemyNode(target: cc.Node, cb?: any, isRelease: boolean = true, parentNode: cc.Node = null, isDestory: boolean = false) {
        while (target.childrenCount > 0) {
            let enemy = target.getChildByName('Game_Gold');
            //放到对象池中
            if (isRelease) {
                this._enemyPool.put(enemy);
            } else {
                //更改父节点为父节点
                enemy.parent = parentNode;
            }
            //如果没有了子节点
            if (target.childrenCount <= 0) {
                if (cb) {
                    cb();
                }
                if (isDestory) {
                    target.destroy();
                }
            }
        }
    }

    /**
     * 创建动作节点
     * @param {cc.Vec2} pos   坐标点
     * @param {cc.Node} parentNode 父节点
     * @param {cc.Vec2} acSize   动作节点大小
     * @param {*} [initCB]    创建完成的回调
     * @memberof Game_GoldBase
     */
    createAcNode(pos: cc.Vec2, parentNode: cc.Node, acSize: cc.Vec2, initCB?: any) {
        let AcNode = new cc.Node('AcNode');
        AcNode.setPosition(pos);
        AcNode.width = acSize.x;
        AcNode.height = acSize.y;
        AcNode.parent = parentNode;

        //创建完成的回调方法
        if (initCB) {
            initCB(AcNode);
        }
    }

    /**
     * 创建对象
     * @param {number} enemyType 对象类型
     * @param {cc.Node} parentNode 父节点
     * @param {*} [initCB]       回调函数
     * @memberof Game_GoldBase
     */
    createEnemyNode(enemyType: number, parentNode: cc.Node, initCB?: any) {
        let enemy = null;
        if (this._enemyPool.size() > 0) { // 通过 size 接口判断对象池中是否有空闲的对象
            enemy = this._enemyPool.get();
        } else { // 如果没有空闲对象，也就是对象池中备用对象不够时，我们就用 cc.instantiate 重新创建
            enemy = cc.instantiate(this.enemyPrefab);
        }
        let script = enemy.getComponent('Game_Gold');
        script.initData(this.goldImgList[enemyType], parentNode);
        //对象池的回调方法
        if (initCB) {
            initCB(enemy);
        }
    }

    /**
     *创建对象集
     * @param {number} enemyType 对象类型
     * @param {cc.Node} parentNode 父节点
     * @memberof Game_GoldBase
     */
    createChip(enemyType: number, parentNode: cc.Node) {
        this.createEnemyNode(enemyType, parentNode, (enemy) => {
            // enemy.rotation = (Math.random() * 10000) % 360;
            let lr = Math.floor(Math.random() * 100 + 100) % 2;
            let betX = Math.floor(Math.random() * (parentNode.width - enemy.width) * 0.5);
            if (lr === 0) {
                betX = 0 - betX;
            }
            let betY = Math.floor(Math.random() * (parentNode.height - enemy.height) * 0.5);
            lr = Math.floor(Math.random() * 100 + 100) % 2;
            if (lr === 0) {
                betY = 0 - betY;
            }
            enemy.setPosition(cc.p(betX, betY));
        });
    }

    /**
     * 创建押注池显示金币
     * @param {number[]} numList  数字列表
     * @param {cc.Node} parentNode 父节点
     * @memberof Game_GoldBase
     */
    createMoreGold(numList: number[], parentNode: cc.Node) {
        for (var i = 0; i < numList.length; i++) {
            let num = numList[i];
            let enemyType = this.goldImgList.length - 1 - i;
            for (var j = 0; j < num; j++) {
                this.createChip(enemyType, parentNode);
            }
        }
    }

    /**
     *  删除多余的
     * @param {cc.Node} target 目标节点
     * @param {number} [max=20] 最多存在
     * @memberof Game_GoldBase
     */
    deleteSpilth(target: cc.Node, max: number = 150) {
        if (target.childrenCount > max) {
            let i = 0;
            let die = target.childrenCount - max;
            while (target.childrenCount > 0) {
                let enemy = target.getChildByName('Game_Gold');
                //放到对象池中
                this._enemyPool.put(enemy);
                i++;
                if (i >= die) {
                    break;
                }
            }
        }
    }
    /**
     * 建金币并飞向下注池
     * @param {cc.Vec2} startP     起始点
     * @param {number[]} numList   数字列表 对应的是 图片列表 ，长度相等
     * @param {cc.Vec2} acSize     动作节点的大小
     * @param {cc.Node} parentNode 动作播放父节点
     * @param {cc.Node} targetNode 动作完成后金币父节点(目标节点)
     * @param {*} [cb]             回调
     * @memberof Game_GoldBase
     */
    playGoldMoveToPool(startP: cc.Vec2, numList: number[], acSize: cc.Vec2, parentNode: cc.Node, targetNode: cc.Node, cb?: any) {
        this.deleteSpilth(targetNode);
        this.createAcNode(startP, parentNode, acSize, (AcNode) => {
            mp_manager.playCoinMove();
            for (var i = 0; i < numList.length; i++) {
                let num = numList[i];
                let enemyType = this.goldImgList.length - 1 - i;
                for (var j = 0; j < num; j++) {
                    this.createChip(enemyType, AcNode);
                }
            }
            AcNode.scale = 0.1;
            let endP = this.getWorldPos(targetNode.parent, targetNode.getPosition());
            let moveTo = cc.moveTo(everyEnemyActionTime, endP);
            let action = moveTo.easing(cc.easeExponentialOut());
            let spawn = cc.spawn(cc.scaleTo(0.1, 1), action);
            let finished = cc.callFunc(() => {
                this.removeAllEnemyNode(AcNode, cb, false, targetNode, false);
            }, this);
            let seq = cc.sequence(spawn, finished);
            AcNode.runAction(seq);
        });
    }

    /**
     * 获取世界坐标
     * @param {cc.Node} node 父节点
     * @param {cc.Vec2} pos  节点坐标
     * @returns 
     * @memberof Game_GoldBase
     */
    getWorldPos(node: cc.Node, pos: cc.Vec2) {
        let targetP = node.convertToWorldSpaceAR(pos);
        //依然是以屏幕左下角为起点,所以要减去一半
        targetP.x = targetP.x - this.node.width / 2;
        targetP.y = targetP.y - this.node.height / 2;
        return targetP;
    }

    /**
     * 玩家赢取所有的金币 （只有一个节点在做动作，同时到达）
     * @param {boolean} isAct      是否先播放动作，再清除对象
     * @param { cc.Vec2} endP      动作结束坐标点
     * @param {cc.Node} targetNode  目标节点
     * @param {cc.Node} parentNode  动作节点
     */
    playAllGoldMoveToPlayer(isAct: boolean, endP: cc.Vec2, targetNode: cc.Node, parentNode: cc.Node) {
        if (isAct) {//如果先播放动作，再清除对象
            mp_manager.playZJH('coins');
            //根据目标节点，复制一个临时节点
            let tempNode = cc.instantiate(targetNode);
            let targetP = this.getWorldPos(targetNode.parent, targetNode.getPosition());
            tempNode.setPosition(targetP);
            tempNode.parent = parentNode;
            let finished = cc.callFunc(() => {
                this.removeAllEnemyNode(tempNode, null, true, null, true);
            }, this);
            let moveTo = cc.moveTo(everyEnemyActionTime, endP);
            let action = moveTo.easing(cc.easeExponentialIn());
            let spawn = cc.spawn(cc.scaleTo(everyEnemyActionTime, 0.5), action);
            let seq = cc.sequence(spawn, finished);
            tempNode.runAction(seq);
            this.removeAllEnemyNode(targetNode);
        } else {
            this.removeAllEnemyNode(targetNode);
        }
    }

    /**
     * 创建多个对象，并从起始点动作到目标点
     * @param {cc.Vec2} startP   动作起始点
     * @param {cc.Vec2} endP     动作结束点
     * @param {number[]} numList    数字列表 对应的是 图片列表 ，长度相等
     * @param {cc.Vec2} acSize   动作节点大小
     * @param { cc.Node} parentNode  动作父节点
     * @param {any} cb       动作完成回调
     */
    playGoldPosToPos(startP: cc.Vec2, endP: cc.Vec2, numList: number[], acSize: cc.Vec2, parentNode: cc.Node, cb) {
        this.createAcNode(startP, parentNode, acSize, (AcNode) => {
            for (var i = 0; i < numList.length; i++) {
                let num = numList[i];
                let enemyType = this.goldImgList.length - 1 - i;
                for (var j = 0; j < num; j++) {
                    this.createChip(enemyType, AcNode);
                }
            }

            let finished = cc.callFunc(() => {
                this.removeAllEnemyNode(AcNode, cb, true);
            }, this);
            let action = cc.moveTo(everyEnemyActionTime, endP);
            let spawn = cc.spawn(cc.scaleTo(everyEnemyActionTime, 0.5), action);
            let seq = cc.sequence(spawn, finished);
            AcNode.runAction(seq);
        });
    }

    /**
     *  玩家赢取所有的金币 （每个金币都在做动作，不同时到达）
     * @param {cc.Vec2} endP        结束位置
     * @param {cc.Node} targetNode  目标节点
     * @param {any} startCB         第一个动作结束时的回调
     * @param {any} endCB           最后一个动作结束时的回调
     * @param {boolean} [isTogather=false]  是否聚拢
     * @param {cc.Vec2} [scopeSize=cc.v2(80, 80)] 聚拢的范围
     * @memberof Game_GoldBase
     */
    playGoldMoveToPlayer(endP: cc.Vec2, targetNode: cc.Node, startCB, endCB, isTogather: boolean = false, scopeSize: cc.Vec2 = cc.v2(60, 60)) {
        if (isTogather) {
            let nodeList = targetNode.children;
            let finished = cc.callFunc(() => {
                this.playFlyAllGoldAction(endP, targetNode, startCB, endCB);
            }, this);
            for (var i = 0; i < nodeList.length; i++) {
                let enemy = nodeList[i];
                let action = null;
                if (i !== nodeList.length - 1) {
                    if (Math.abs(enemy.x) > scopeSize.x || Math.abs(enemy.y) > scopeSize.y) {
                        action = cc.moveTo(gatherGoldTime, cc.p(Math.random() * scopeSize.x, Math.random() * scopeSize.y));
                    }
                } else {
                    action = cc.sequence(cc.moveTo(gatherGoldTime, cc.p(Math.random() * scopeSize.x, Math.random() * scopeSize.y)), finished);
                }
                if (action) {
                    enemy.runAction(action);
                }
            }
        } else {
            this.playFlyAllGoldAction(endP, targetNode, startCB, endCB);
        }
    }
    /**
     * 播放从目标节点中取出所有的 对象，飞到目标点，这个动作是异步的(不同时到达目标点)
     * 
     * @param {cc.Vec2} endP 
     * @param {cc.Node} targetNode 
     * @param {any} startCB 
     * @param {any} endCB 
     * @memberof Game_GoldBase
     */
    private playFlyAllGoldAction(endP: cc.Vec2, targetNode: cc.Node, startCB, endCB) {
        let count = targetNode.childrenCount;
        //每次可以做动作的数量
        let num = Math.round(count / flyGoldTimes);
        if (num < 1) {
            num = 1;
        }
        let isStart = true;
        let nodeList = targetNode.children;
        let finished = cc.callFunc((enemy) => {
            //动作完成,放到对象池中
            this._enemyPool.put(enemy);
            //如果是开始
            if (isStart) {
                if (startCB) {
                    startCB();
                }
                isStart = false;
                cc.log('开始回调' + isStart);
            }
            //如果是结束
            if (targetNode.childrenCount <= 0) {
                if (endCB) {
                    endCB();
                }
                cc.log('结束回调');
            }
        }, this);
        for (var i = 0; i < nodeList.length; i++) {
            let enemy = nodeList[i];
            let moveTo = cc.moveTo(flyBaseTime * (i / num + 1), endP);
            // let action = moveTo.easing(cc.easeExponentialIn());
            let seq = cc.sequence(moveTo, finished);
            enemy.runAction(seq);
        }
    }
}
