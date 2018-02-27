const { ccclass, property } = cc._decorator;

@ccclass
export default class ChildClass extends cc.Component {
    @property(cc.Label)
    label: cc.Label;

    @property(cc.String)
    text: string = 'hello';

    @property(cc.EditBox)
    edit: cc.EditBox;

    _ws: WebSocket = null;

    _encStr: string = '';

    onLoad() {
        // init logic

        this._ws = new WebSocket('ws://127.0.0.1:8181');
        this._ws.binaryType = "arraybuffer";
        this._ws.onopen = (event) => {
            this._ws.onmessage = (event) => {
                let msg = this.getStringFromArrayBuffer(event.data);
                let obj = JSON.parse(msg);
                if (CryptoJS.HmacMD5(obj.data, obj.pid + '').toString() === obj.md5) {
                    this.label.string = this.decrypt(obj.data);
                }
            };
        };
        this._ws.onclose = (event) => {
            cc.log('onclose');
        };

        this._ws.onerror = (event) => {
            cc.log('Error');
        }

    }

    encrypt(word) {
        let key = CryptoJS.enc.Utf8.parse(CryptoJS.MD5('123456').toString().substr(0, 16));
        cc.log(key.toString());
        let iv = CryptoJS.enc.Utf8.parse(CryptoJS.MD5(key).toString().substr(0, 16));
        cc.log(iv.toString());
        let srcs = CryptoJS.enc.Utf8.parse(word);
        let encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
        return encrypted.toString();
    }
    decrypt(word) {
        let key = CryptoJS.enc.Utf8.parse(CryptoJS.MD5('123456').toString().substr(0, 16));
        let iv = CryptoJS.enc.Utf8.parse(CryptoJS.MD5(key).toString().substr(0, 16));
        let decrypt = CryptoJS.AES.decrypt(word, key, { iv: iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7 });
        return CryptoJS.enc.Utf8.stringify(decrypt).toString();
    }

    getStringFromArrayBuffer(buf: ArrayBuffer): string {
        let utf16le = CryptoJS.lib.WordArray.create(buf).toString(CryptoJS.enc.Utf16LE);
        let utf8 = CryptoJS.enc.Utf8.parse(utf16le).toString(CryptoJS.enc.Utf8);
        return utf8;
    }

    getArrayBufferFromString(str: string): ArrayBuffer {
        let words = CryptoJS.enc.Utf16LE.parse(str);
        str = CryptoJS.enc.Utf16LE.stringify(words);
        let buf = new ArrayBuffer(str.length * 2);
        let bufView = new Uint16Array(buf);
        for (let i = 0, strLen = str.length; i < strLen; i++) {
            bufView[i] = str.charCodeAt(i);
        }
        return buf;
    }

    onClick(event, data) {

        if (data === '1') {
            let obj = {
                pid: 10001,
                time: new Date().getTime().toString(),
                md5: CryptoJS.HmacMD5(this._encStr, '10001').toString(),
                data: this._encStr
            }

            this._ws.send(this.getArrayBufferFromString(JSON.stringify(obj)));
        }
        else if (data === '2') {
            let obj = {
                name: 'jerry-wjasdfasdfasdfasdfasfasdfasfsfsdfwereesadasfsadfasdffasfsafasdf',
                age: 28,
                sex: 1
            }
            let msg = JSON.stringify(obj);
            this._encStr = this.encrypt(msg);
            this.label.string = this._encStr;
            cc.log(msg.length);
            cc.log(this._encStr);
        }
        else
            this.edit.string = this.decrypt(this.label.string);
    }
}
