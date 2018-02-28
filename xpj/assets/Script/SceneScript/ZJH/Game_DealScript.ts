const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class NewClass extends cc.Component {

    @property(cc.SpriteFrame)
    card_sf: cc.SpriteFrame = null;

    onLoad() {

    }

    createNode(pos) {//pos为创建节点坐标
        let node = new cc.Node('card_back');
        let sprite = node.addComponent(cc.Sprite);
        sprite.spriteFrame = this.card_sf;
        node.parent = this.node;
        node.setPosition(pos);
        node.scale = 0;
        return node;
    }
    /**
     * 间隔调用动画
     * @param {cc.Vec2[]} posList 终点坐标点列表
     * @param {any} cb            每次动作结束的回调
     * @param {number} [repeat=1]  重复次数
     * @param {number} [interval=0.4] 以秒为单位的时间间隔
     * @param {number} [delay=0] 开始延时
     * @memberof NewClass
     */
    showDeal(posList: cc.Vec2[], cb, repeat: number = 1, interval: number = 0.4, delay: number = 0) {
        // 重复次数
        repeat = posList.length * repeat - 1;
        let i = 0;
        this.schedule(() => {
            let index = i % posList.length;
            if (cb) {
                let data = null;
                cb(index);
            }
            i++;
        }, interval, repeat, delay);
    }

    /**
     * 发牌动画
     * 
     * @param {cc.Vec2} startP 起始的坐标点
     * @param {cc.Vec2[]} posList 终点坐标点列表
     * @param {any} eachCB 每一个动作结束的回调
     * @param {any} endCB  所有动作结束的回调
     * @param {number} [repeat=1] 重复次数
     * @param {number} [interval=0.2] 以秒为单位的时间间隔
     * @param {number} [delay=0] 开始延时
     * @memberof NewClass
     */
    showDealFP(startP: cc.Vec2, posList: cc.Vec2[], eachCB, endCB, repeat: number = 1, interval: number = 0.2, delay: number = 0) {
        if (posList === null || posList === undefined) return;
        //计算一轮的长度
        let rCount = posList.length;
        //计算需要重复多少次(循环是从0开始，所以要减1)
        repeat = posList.length * repeat - 1;
        let count = 0;
        this.schedule(() => {
            if (dd.mp_manager)
                dd.mp_manager.playFaPai();
            let cNode = this.createNode(startP);
            //计算第几个位置
            let index = count % posList.length;
            if (posList[index]) {
                let finished = cc.callFunc((target, opt) => {
                    if (eachCB) {
                        //计算第几轮
                        let round = Math.floor(opt.count / rCount);
                        eachCB(opt.index, round);
                    }
                    if (opt.count === repeat) {//动画执行完毕
                        if (endCB) {
                            endCB();
                        }
                    }
                    cNode.destroy();
                }, this, { 'index': index, "count": count });
                cNode.opacity = 100;
                let spawn = cc.spawn(cc.scaleTo(0.5, 0.8, 0.8), cc.rotateBy(0.5, 540), cc.moveTo(0.5, posList[index]), cc.fadeIn(0.5));
                let myAction = cc.sequence(spawn, finished);
                cNode.runAction(myAction);
                count++;
            }
        }, interval, repeat, delay);
    }

    /**
     * 弃牌动作
     * @param {cc.Vec2} startP 起始坐标
     * @param {any} endCB  结束回调
     * @param {cc.Vec2} endP   终点坐标
     */
    showDealDiscard(startP: cc.Vec2, endCB?: any, endP: cc.Vec2 = cc.v2(0, 0)) {//弃牌动画
        dd.mp_manager.playFaPai();
        let cNode = this.createNode(startP);
        cNode.scale = 0.8;
        let finished = cc.callFunc(() => {
            if (endCB) {
                endCB();
            }
            //动画执行完毕，显示遮罩
            cNode.destroy();
        }, this);
        let spawn = cc.spawn(cc.scaleTo(0.5, 0), cc.rotateBy(0.5, 540), cc.moveTo(0.5, endP));
        let myAction = cc.sequence(spawn, finished);
        cNode.runAction(myAction);
    }
}
