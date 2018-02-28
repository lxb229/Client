const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';

@ccclass
export default class Mine extends cc.Component {
    /**
     * 滚动节点
     * 
     * @type {cc.Node}
     * @memberof Mine
     */
    @property(cc.Node)
    svNode: cc.Node = null;
    /**
     * 我的牌局对象数据集
     * 
     * @type {JoinedTableItem[]}
     * @memberof Mine
     */
    dataList: JoinedTableItem[] = [];

    init(datas: JoinedTableItem[]) {
        this.dataList = datas;
    }

    onLoad() {
        if (this.dataList && this.dataList.length > 0) {
            this.svNode.getComponent('SVScript').init(this.dataList, (data: JoinedTableItem) => {
                dd.ui_manager.showLoading('正在进入房间');
                dd.mp_manager.playButton();
                let obj = { tableId: data.tableId };
                let msg = JSON.stringify(obj);
                dd.ws_manager.sendMsg(dd.protocol.DZPKER_TABLE_JOIN, msg, (flag: number, content?: any) => {
                    if (flag === 0) {
                        dd.gm_manager.setTableData(content as TableData, 1);
                        cc.director.loadScene('GameScene', () => {
                            dd.ui_manager.showTip('加入房间成功');
                        });
                    } else if (flag === -1) {
                        dd.ui_manager.showTip('加入房间消息发送超时');
                    } else {
                        dd.ui_manager.showTip(content);
                    }
                });
            });
            dd.ui_manager.hideLoading();
        } else {
            dd.ui_manager.showTip('当前没有您可以继续的牌局!');
        }
    }
    /**
     * 点击关闭按钮
     * 
     * @memberof Mine
     */
    click_out() {
        dd.mp_manager.playButton();
        this.node.destroy();
    }
}
