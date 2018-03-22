
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

- (id)parseJSON:(NSString *)jsonString
{
    NSData *jsonData= [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    if (jsonData == nil){
        NSLog(@"Error: NSString->NSData错误：%@",jsonString);
        return nil;
    }
    NSError *error = nil;
    id jsonObject = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:&error];
    if (jsonObject != nil && error == nil) {
        return jsonObject;
    } else {
        NSLog(@"Error: json解析错误：%@",error);
        return nil;
    }
}

-(void)initIAP{
    // 设置IAP事件回调
    [[IAPApi Instance] setMessageHandler:^(int code, NSString *params) {
        NSLog(@"IAPEvent %d",code);
        NSLog(@"%@",params);
        switch (code) {
            case IAPPAY_SUCCESS:
            {
                /**
                 * 支付成功，返回一个json字符串
                 {
                 productIdentifier:"xxx",
                 billNO:"xxxx",
                 receipt:"xxxxxxx"
                 }
                 */
                // 建议服务器做收据校验，如果你想用客户端来验证收据（如单机游戏app），请调用以下方法, 第二个参数表示是否用沙箱验证，此处填NO
                NSDictionary *dic = [self parseJSON:params];
                self.receipt = dic[@"receipt"];
                [[IAPApi Instance] verifyReceipt:self.receipt andDebug:NO];
                break;
            }
            case LIST_AVALIABLE:{
                /**
                 * itunestore产品列表，返回一个json字符串数组
                 [
                 {
                 title:"xxx",
                 description:"xxxx",
                 price:"xxxxxxx",
                 productid:"xxxxxxx"
                 }
                 ]
                 */
                self.receipt = nil;
                NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.getProducts('%@')",params];
                [self callToJS:suc_func];
                break;
            }
            case VERIFY_RECEIPT_RESULT:
            {
                /***iap收据验证结果,  请参考Apple开发文档
                 https://developer.apple.com/library/content/releasenotes/General/ValidateAppStoreReceipt/Chapters/ValidateRemotely.html
                 **/
                NSDictionary *dic = [self parseJSON:params];
                int status = [[dic valueForKey:@"status"] intValue];
                if (status == 0){
                    //验证成功, 处理发货
                    NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.iapBack(0)"];
                    [self callToJS:suc_func];
                }
                else if (status == 21007){
                    [[IAPApi Instance] verifyReceipt:self.receipt andDebug:YES];
                }else{
                    NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.iapBack(-4)"];
                    [self callToJS:suc_func];
                }
                self.receipt = nil;
                break;
            }
            case ErrorPaymentError:
            case ErrorPaymentNotAllowed:
            case ErrorPaymentInvalid:{
                self.receipt = nil;
                NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.iapBack(-2)"];
                [self callToJS:suc_func];
                break;
            }
            case ErrorStoreProductNotAvailable:
            {
                self.receipt = nil;
                NSLog(@" error: %@ ！",params);
                NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.getProducts()"];
                [self callToJS:suc_func];
                break;
            }
            case ErrorVerifyReceipt:{
                self.receipt = nil;
                NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.iapBack(-4)"];
                [self callToJS:suc_func];
                break;
            }
            case ErrorPaymentCancelled:{
                self.receipt = nil;
                NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.iapBack(-1)"];
                [self callToJS:suc_func];
                break;
            }
            default:
                break;
        }
    }];
}
//请求内购列表
+(void) getProducts:(NSString *) ids{
    NSArray * pids = [ids componentsSeparatedByString:@","];
    [[IAPApi Instance] getCanBuyProductList:pids];
}
//购买指定商品
+(void) buyProduct:(NSString *) pid bid:(NSString *) bid{
    [[IAPApi Instance] buy:pid billNo:bid];
}

-(void)changeState:(bool) isResume
{
    self.isResume = isResume;
    if(isResume){
        if(self.openId)
            [[GVGCloudVoice sharedInstance] resume];
    }else{
        if(self.openId)
            [[GVGCloudVoice sharedInstance] pause];
    }
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
    if(!self.isResume){
        [self performSelectorInBackground:@selector(threadAction:) withObject:str];
    }else{
        [self runJS:str];
    }
}
//执行调用
-(void)runJS:(NSString *)str{
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
//开启新线程等待
-(void)threadAction:(NSString *)str{
    @autoreleasepool {
        while (![IOSHelper shareInstance].isResume) {
            [NSThread sleepForTimeInterval:0.2];
        }
        [self performSelectorOnMainThread:@selector(runJS:) withObject:str waitUntilDone:YES];
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



//初始化实时语音
+(int)initVoice:(NSString *)openId app_id:(NSString *)app_id app_key:(NSString *)app_key{
    [IOSHelper shareInstance].openId = openId;
    [IOSHelper shareInstance].voiceId = app_id;
    [IOSHelper shareInstance].voiceKey = app_key;
    [IOSHelper shareInstance].myMemberId = -1;
    [IOSHelper shareInstance].roomId = nil;
    [GVGCloudVoice sharedInstance].delegate = [IOSHelper shareInstance];
    int result = [[GVGCloudVoice sharedInstance] setAppInfo:[app_id UTF8String] withKey:[app_key UTF8String] andOpenID:[openId cStringUsingEncoding:NSUTF8StringEncoding]];
    if(result != GCLOUD_VOICE_SUCC) return -1;
    result = [[GVGCloudVoice sharedInstance] initEngine];
    if(result != GCLOUD_VOICE_SUCC) return -1;
    [NSTimer scheduledTimerWithTimeInterval:1.000/15 repeats:YES block:^(NSTimer * _Nonnull timer) {
        [[GVGCloudVoice sharedInstance] poll];
    }];
    return 0;
}
//加入指定房间
+(int)joinTeamRoom:(NSString *)roomId{
    if([IOSHelper shareInstance].roomId != nil||[IOSHelper shareInstance].openId == nil) return -1;
    int result = [[GVGCloudVoice sharedInstance] setAppInfo:[[IOSHelper shareInstance].voiceId UTF8String] withKey:[[IOSHelper shareInstance].voiceKey UTF8String] andOpenID:[[IOSHelper shareInstance].openId cStringUsingEncoding:NSUTF8StringEncoding]];
    if(result != GCLOUD_VOICE_SUCC) return -1;
    result = [[GVGCloudVoice sharedInstance] setMode:RealTime];
    if(result != GCLOUD_VOICE_SUCC) return -1;
    result = [[GVGCloudVoice sharedInstance] joinTeamRoom:[roomId cStringUsingEncoding:NSUTF8StringEncoding] timeout:10000];
    if(result != GCLOUD_VOICE_SUCC) return -1;
    [IOSHelper shareInstance].roomId = roomId;
    return 0;
}
//退出房间
+(int)quitRoom{
    if([IOSHelper shareInstance].roomId == nil||[IOSHelper shareInstance].openId == nil) return -1;
    int result = GCLOUD_VOICE_SUCC;
    result = [[GVGCloudVoice sharedInstance] quitRoom:[[IOSHelper shareInstance].roomId UTF8String] timeout:10000];
    if(result != GCLOUD_VOICE_SUCC) return -1;
    return 0;
}
//设置麦克风和扬声器的开关
+(int)setState:(BOOL) isOpen{
    if([IOSHelper shareInstance].roomId == nil) return -1;
    int result = GCLOUD_VOICE_SUCC;
    if(isOpen){
        result = [[GVGCloudVoice sharedInstance] openMic];
        if(result != GCLOUD_VOICE_SUCC) return -1;
        result = [[GVGCloudVoice sharedInstance] openSpeaker];
        if(result != GCLOUD_VOICE_SUCC) return -1;
        return 0;
    }else{
        result = [[GVGCloudVoice sharedInstance] closeMic];
        if(result != GCLOUD_VOICE_SUCC) return -1;
        result = [[GVGCloudVoice sharedInstance] closeSpeaker];
        if(result != GCLOUD_VOICE_SUCC) return -1;
        return 0;
    }
}

#pragma mark delegate

- (void) onJoinRoom:(enum GCloudVoiceCompleteCode) code withRoomName: (const char * _Nullable)roomName andMemberID:(int) memberID {
    if (GV_ON_JOINROOM_SUCC == code) {
        self.myMemberId = memberID;
        NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.voiceLoginCallback(0)"];
        [self callToJS:suc_func];
    } else {
        self.roomId = nil;
        self.myMemberId = -1;
        NSString *err_func = [NSString stringWithFormat:@"dd.native_call_js.voiceLoginCallback(-1)"];
        [self callToJS:err_func];
    }
}

- (void) onStatusUpdate:(enum GCloudVoiceCompleteCode) status withRoomName: (const char * _Nullable)roomName andMemberID:(int) memberID {
    if(status == GV_ON_ROOM_OFFLINE){
        if(memberID == self.myMemberId){
            [[GVGCloudVoice sharedInstance] joinTeamRoom:roomName timeout:10000];
        }
    }
    
}

- (void) onQuitRoom:(enum GCloudVoiceCompleteCode) code withRoomName: (const char * _Nullable)roomName {
    if (GV_ON_QUITROOM_SUCC == code) {
        self.roomId = nil;
        self.myMemberId = -1;
        NSString *suc_func = [NSString stringWithFormat:@"dd.native_call_js.voiceQuitCallback(0)"];
        [self callToJS:suc_func];
    }else{
        NSString *err_func = [NSString stringWithFormat:@"dd.native_call_js.voiceQuitCallback(-1)"];
        [self callToJS:err_func];
    }
    
}

- (void) onMemberVoice:(const unsigned int * _Nullable)members withCount: (int) count {
    
}

- (void) onUploadFile: (enum GCloudVoiceCompleteCode) code withFilePath: (const char * _Nullable)filePath andFileID:(const char * _Nullable)fileID  {
    
}

- (void) onDownloadFile: (enum GCloudVoiceCompleteCode) code  withFilePath: (const char * _Nullable)filePath andFileID:(const char * _Nullable)fileID {
    
}

- (void) onPlayRecordedFile:(enum GCloudVoiceCompleteCode) code withFilePath: (const char * _Nullable)filePath {
    
}

- (void) onApplyMessageKey:(enum GCloudVoiceCompleteCode) code {
    
}

- (void) onSpeechToText:(enum GCloudVoiceCompleteCode) code withFileID:(const char * _Nullable)fileID andResult:( const char * _Nullable)result {
    
}

- (void) onRecording:(const unsigned char* _Nullable) pAudioData withLength: (unsigned int) nDataLength {
    
}


- (void) onStreamSpeechToText:(enum GCloudVoiceCompleteCode) code withError:(int) error andResult:(const char *_Nullable)result {
    
}

@end

