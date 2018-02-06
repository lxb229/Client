/**
 * 检测app版本号的请求地址
 */
export const checkUrl: string = 'http://27.50.49.181:8080/checkVer?ver=';
/**
 * ws连接地址
 */
// export const wsUrl: string = 'ws://27.50.49.181:30000';
// export const wsUrl: string = 'ws://192.168.12.156:30000';
/**
 * 微信id和key
 */
export const app_id: string = 'wx80fecf07fad576ad';
export const secret: string = 'e669f8c640bfcaab88640d0a7fecae05';
/**
 * 微信初始化状态 0 = 成功 其它失败
 */
export let wxState: number = -1;

/**
 * 资源版本号
 */
export let version: string = '1.0.0';

/**
 * 服务器获取的版本信息
 */
export let cd: checkData = null;
