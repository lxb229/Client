/**
 * 检测app版本号的请求地址
 */
export const checkUrl: string = 'http://118.31.66.39:8080/checkVer?ver=';
/**
 * ws连接地址
 */
export const wsUrl: string = 'ws://ws.wolfsgame.com:40000';
// export const wsUrl: string = 'ws://192.168.12.156:40000';
// export const wsUrl: string = 'ws://118.31.66.39:40000';
/**
 * 战绩录像数据获取的请求地址
 */
export const replayUrl: string = 'http://118.31.66.39:8080/file/';
/**
 * 微信id和key
 */
export const app_id: string = 'wx19e1237d774e5763';
export const secret: string = '57ff16852f286c0e571f375b425269d0';
/**
 * 语音id和key
 */
export const voice_id: string = '1126068785';
export const voice_key: string = 'd926363c87ed6be99a87b11b41c42c91';
/**
 * 语音初始化状态 0=成功  其他=失败
 */
export let voiceState: number = -1;
/**
 * 微信初始化状态 0 = 成功 其它失败
 */
export let wxState: number = -1;
/**
 * ios内购，商品id集合，用‘,’隔开
 */
export let productids: string = 'p1,p2,p3';
/**
 * node返回的信息
 */
export let cd: checkData = null;