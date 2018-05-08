import * as Config from './Config';
import UDManager from './UDManager';

/**
 * 微信登录-获取access_token
 * 
 * @export
 * @param {string} code 登录授权获取到的code
 */
export async function getAccessToken(code: string) {
    try {
        let url = 'https://api.weixin.qq.com/sns/oauth2/access_token?appid=' + Config.app_id + '&secret=' + Config.secret + '&code=' + code + '&grant_type=authorization_code';
        let tokenStr = await UDManager.getInstance().xmlHttp(url);
        let data: TokenInfo = JSON.parse(tokenStr);
        if (data.access_token) {
            let db = cc.sys.localStorage;
            db.setItem('TokenInfo', tokenStr);
            let url_userInfo = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + data.access_token + '&openid=' + data.openid;
            let infoStr = await UDManager.getInstance().xmlHttp(url_userInfo);
            let userInfo = JSON.parse(infoStr);
            cc.systemEvent.emit('cb_login', { flag: 1, data: userInfo });
        } else {
            cc.systemEvent.emit('cb_login', { flag: 0, data: '获取token失败' + tokenStr });
        }
    } catch (errMsg) {
        cc.systemEvent.emit('cb_login', { flag: 0, data: 'http请求异常' + errMsg });
    }
}

/**
 * 微信登录-授权失败
 * 
 * @export
 */
export function loginError(): void {
    cc.systemEvent.emit('cb_login', { flag: 0, data: '授权失败' });
}

/**
 * 微信分享-结果回调
 * 
 * @export
 * @param {number} result 分享结果,0为成功
 */
export function shareCallback(result: number): void {
    cc.systemEvent.emit('cb_share', result);
}

/**
 * 实时语音登录回调
 * 
 * @export
 * @param {number} result 返回0为成功,其他失败
 */
export function voiceLoginCallback(result: number) {
    cc.systemEvent.emit('cb_voiceLogin', result);
}

/**
 * 退出房间回调
 * 
 * @export
 * @param {number} result 返回0为成功,其他失败
 */
export function voiceQuitCallback(result: number) {
    cc.systemEvent.emit('cb_voiceQuit', result);
}

export function getProducts(products: string) {
    cc.systemEvent.emit('cb_getProducts', products);
}

export function iapBack(state: number) {
    cc.systemEvent.emit('cb_iapBack', state);
}

export function qyiap() {
    cc.systemEvent.emit('qyiap');
}
