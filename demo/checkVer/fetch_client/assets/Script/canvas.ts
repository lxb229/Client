const { ccclass, property } = cc._decorator;

@ccclass
export default class NewClass extends cc.Component {

    @property(cc.Label)
    label: cc.Label;

    async onLoad() {
        try {
            let response = await fetch('http://127.0.0.1:8080/checkVer?ver=1.4.2');
            if (response.ok) {
                let json = await response.json();
                if (json.type < 0) {
                    this.label.string = '版本过低！\n android客户端请前往：\n' + json.apkUrl + '\n ios客户端请前往：\n' + json.ipaUrl + '\n下载最新客户端！';
                } else if (json.type === 1) {
                    this.label.string = '发现有新客户端，是否下载？';
                } else {
                    this.label.string = '客户端已经是最新版本！';
                }
            } else {
                this.label.string = '数据获取失败！';
            }
        } catch (err) {
            this.label.string = '请求失败！';
        }
    }
}
