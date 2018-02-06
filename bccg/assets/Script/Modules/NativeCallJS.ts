import * as Config from './Config';

/**
 * 微信登录-获取access_token
 * 
 * @export
 * @param {string} code 登录授权获取到的code
 */
export async function getAccessToken(code: string) {
    try {
        let url = 'https://api.weixin.qq.com/sns/oauth2/access_token?appid=' + Config.app_id + '&secret=' + Config.secret + '&code=' + code + '&grant_type=authorization_code';
        let response = await fetch(url);
        if (response.ok) {
            let data: TokenInfo = await response.json();
            if (data.access_token) {
                let db = cc.sys.localStorage;
                db.setItem('TokenInfo', JSON.stringify(data));
                let url_userInfo = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + data.access_token + '&openid=' + data.openid;
                let response_userInfo = await fetch(url_userInfo);
                if (response_userInfo.ok) {
                    let userInfo = await response_userInfo.json();
                    cc.systemEvent.emit('cb_login', { flag: 1, data: userInfo });
                } else {
                    cc.systemEvent.emit('cb_login', { flag: 0, data: '获取用户信息失败' });
                }
            } else {
                cc.systemEvent.emit('cb_login', { flag: 0, data: '获取token失败' });
            }
        } else {
            cc.systemEvent.emit('cb_login', { flag: 0, data: '请求token失败' });
        }
    } catch (err) {
        cc.systemEvent.emit('cb_login', { flag: 0, data: 'http请求异常' });
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