/**
 * 检测app版本号的请求地址
 */
export const checkUrl: string = 'http://xrmj.weichengshu.cn:8080/checkVer?ver=';
/**
 * ws连接地址
 */
// export const wsUrl: string = 'ws://ws.wolfsgame.com:40000';
// export const wsUrl: string = 'ws://192.168.12.156:40000';
export const wsUrl: string = 'ws://118.31.66.39:50000';
/**
 * 战绩录像数据获取的请求地址
 */
export const replayUrl: string = 'http://xrmj.weichengshu.cn:8080/file/';
/**
 * 微信id和key
 */
export const app_id: string = 'wx0207e6109e6d86b0';
export const secret: string = 'd310812836f273eda3a00a592dd100f4';
/**
 * 语音id和key
 */
export const voice_id: string = '1466765226';
export const voice_key: string = '6b98c1e23a77d16c95f622bbcc419e5f';
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
export let productids: string = 'x1,x2,x3';
/**
 * node返回的信息
 */
export let cd: checkData = null;