
#import "IOSHelper.h"
#import <AudioToolbox/AudioToolbox.h>

@implementation IOSHelper

static id _instace;

+ (id)allocWithZone:(struct _NSZone *)zone
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instace = [super allocWithZone:zone];
    });
    return _instace;
}

+ (instancetype) shareInstance
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instace = [[self alloc] init];
        [UIDevice currentDevice].batteryMonitoringEnabled = YES;
    });
    return _instace;
}

- (id)copyWithZone:(NSZone *)zone
{
    return _instace;
}

//调用系统振动
+(void) phoneVibration{
    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
}
//打开制定网页
+ (void)openBrowser:(NSString *)url
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}
//获取到app的版本号
+ (NSString *)getAppVersion{
    NSDictionary *tmpDic=[NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Info" ofType:@"plist"]];
    NSString *version = [tmpDic valueForKey:@"CFBundleShortVersionString"];
    return version;
}
//复制文本到剪切板
+(void)copyToClipboard:(NSString *)text
{
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = text;
}
//获取电量信息
+(int)getBatteryLevel{
    return (int)([[UIDevice currentDevice] batteryLevel] * 100);
}
//打开微信授权登录
+(void)wxLogin{
    if ([WXApi isWXAppInstalled]) {
        SendAuthReq *req = [[[SendAuthReq alloc] init] autorelease];
        req.scope = @"snsapi_userinfo";
        req.state = @"ddmj";
        [WXApi sendReq:req];
    }
    else {
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"温馨提示" message:@"请先安装微信客户端!" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *actionConfirm = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[WXApi getWXAppInstallUrl]]];
        }];
        [alert addAction:actionConfirm];
        [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:alert animated:YES completion:nil];
    }
}
+(void) wxShare:(NSString *)url title:(NSString *)title des:(NSString *)des{
    //创建分享内容对象
    WXMediaMessage *msg = [WXMediaMessage message];
    msg.title = title;//分享标题
    msg.description = des;//分享描述
    NSDictionary *infoPlist = [[NSBundle mainBundle] infoDictionary];
    NSString *icon = [[infoPlist valueForKeyPath:@"CFBundleIcons.CFBundlePrimaryIcon.CFBundleIconFiles"]lastObject];
    UIImage *img = [UIImage imageNamed:icon];
    [msg setThumbImage:[self compressImage:img toByte:32*1024]];
    //创建多媒体对象
    WXWebpageObject *webObj = [WXWebpageObject object];
    webObj.webpageUrl = url;//分享链接
    //完成发送对象实例
    msg.mediaObject = webObj;
    //创建发送对象实例
    SendMessageToWXReq *req = [[[SendMessageToWXReq alloc] init] autorelease];
    req.bText = NO;//不使用文本信息
    req.scene = WXSceneSession;
    req.message = msg;
    //发送分享信息
    [WXApi sendReq:req];
}
//战绩分享
+(void) wxShareRecord:(NSString *) filePath{
    NSData *imgData = [NSData dataWithContentsOfFile:filePath];
    UIImage *img = [UIImage imageWithData:imgData];
    WXMediaMessage * msg = [WXMediaMessage message];
    [msg setThumbImage:[self compressImage:img toByte:32*1024]];
    WXImageObject * msgObj = [WXImageObject object];
    msgObj.imageData = imgData;
    msg.mediaObject = msgObj;
    SendMessageToWXReq * req = [[[SendMessageToWXReq alloc] init] autorelease];
    req.bText = NO;
    req.message = msg;
    req.scene = WXSceneSession;
    [WXApi sendReq:req];
}
//压缩图片到32k以内
+ (UIImage *)compressImage:(UIImage *)image toByte:(NSUInteger)maxLength {
    CGFloat compression = 1;
    NSData *data = UIImageJPEGRepresentation(image, compression);
    if (data.length < maxLength) return image;
    CGFloat max = 1;
    CGFloat min = 0;
    for (int i = 0; i < 6; ++i) {
        compression = (max + min) / 2;
        data = UIImageJPEGRepresentation(image, compression);
        if (data.length < maxLength * 0.9) {
            min = compression;
        } else if (data.length > maxLength) {
            max = compression;
        } else {
            break;
        }
    }
    UIImage *resultImage = [UIImage imageWithData:data];
    if (data.length < maxLength) return resultImage;
    NSUInteger lastDataLength = 0;
    while (data.length > maxLength && data.length != lastDataLength) {
        lastDataLength = data.length;
        CGFloat ratio = (CGFloat)maxLength / data.length;
        CGSize size = CGSizeMake((NSUInteger)(resultImage.size.width * sqrtf(ratio)),
                                 (NSUInteger)(resultImage.size.height * sqrtf(ratio)));
        UIGraphicsBeginImageContext(size);
        [resultImage drawInRect:CGRectMake(0, 0, size.width, size.height)];
        resultImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        data = UIImageJPEGRepresentation(resultImage, compression);
    }
    return resultImage;
}

//调用js方法
-(void) callToJS:(NSString *)str{
    std::string newStr = [str UTF8String];
    if (std::this_thread::get_id() == cocos2d::Director::getInstance()->getCocos2dThreadId())
    {
        se::ScriptEngine::getInstance()->evalString(newStr.c_str());
    }
    else
    {
        cocos2d::Director::getInstance()->getScheduler()->performFunctionInCocosThread([=](){
            se::ScriptEngine::getInstance()->evalString(newStr.c_str());
        });
    }
}

+(int) initWX:(NSString *)wx_id key:(NSString *)wx_key{
    //向微信注册
    [WXApi registerApp:wx_id enableMTA:YES];
    //向微信注册支持的文件类型
    UInt64 typeFlag = MMAPP_SUPPORT_TEXT | MMAPP_SUPPORT_PICTURE | MMAPP_SUPPORT_LOCATION | MMAPP_SUPPORT_VIDEO |MMAPP_SUPPORT_AUDIO | MMAPP_SUPPORT_WEBPAGE | MMAPP_SUPPORT_DOC | MMAPP_SUPPORT_DOCX | MMAPP_SUPPORT_PPT | MMAPP_SUPPORT_PPTX | MMAPP_SUPPORT_XLS | MMAPP_SUPPORT_XLSX | MMAPP_SUPPORT_PDF;
    [WXApi registerAppSupportContentFlag:typeFlag];
    if ([WXApi isWXAppInstalled]) {
        return 0;
    }
    return -1;
}

- (void)onResp:(BaseResp *)resp {
    if ([resp isKindOfClass:[SendMessageToWXResp class]]) {
        SendMessageToWXResp *messageResp = (SendMessageToWXResp *)resp;
        if(messageResp.errCode == WXSuccess){
            NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.shareCallback(0)"];
            [self callToJS:suc_func];
        }else{
            NSString *err_func = [NSString stringWithFormat:@"dd.native_call_js.shareCallback(-1)"];
            [self callToJS:err_func];
        }
        
    } else if ([resp isKindOfClass:[SendAuthResp class]]) {
        SendAuthResp *authResp = (SendAuthResp *)resp;
        if(authResp.errCode == WXSuccess){
            NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.getAccessToken('%@')",authResp.code];
            [self callToJS:suc_func];
        }else{
            NSString *err_func = [NSString stringWithFormat:@"dd.native_call_js.loginError()"];
            [self callToJS:err_func];
        }
    }else{
        //没有监听的回调
    }
}

- (void)onReq:(BaseReq *)req {
    
}
@end

