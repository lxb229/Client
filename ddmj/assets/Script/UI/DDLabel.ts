const { ccclass, property } = cc._decorator;

@ccclass
export default class DDLabel extends cc.Component {
    lbl: cc.Label = null;
    vaule: string = null;
    onLoad() {
        this.lbl = this.node.getComponent(cc.Label);
    }

    update(dt: number) {
        if (this.lbl && this.vaule !== this.lbl.string) {
            let len = 0;
            this.vaule = '';
            for (let i = 0; i < this.lbl.string.length; i++) {
                if (this.lbl.string.charCodeAt(i) > 255) {
                    len += 2;
                } else {
                    len += 1;
                }
                if (len > 8) {
                    break;
                } else {
                    this.vaule += this.lbl.string.charAt(i);
                }
            }
            this.lbl.string = this.vaule;
        }
    }
}
