//
//  IOSHelper.h
//  MoYiMo
//
//  Created by zhaohong on 16/11/10.
//  Copyright © 2016年 zhaohong. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "ScriptingCore.h"
#import "cocos/scripting/js-bindings/jswrapper/SeApi.h"
#import "cocos2d.h"
#import "WXApi.h"
#import "GVoice.h"
#import "IAPDefine.h"
#import "IAPApi.h"
@interface IOSHelper : NSObject<WXApiDelegate,GVGCloudVoiceDelegate>

@property(strong, nonatomic) NSString *roomId;
@property(strong, nonatomic) NSString *openId;
@property(strong, nonatomic) NSString *voiceId;
@property(strong, nonatomic) NSString *voiceKey;
@property(assign, nonatomic) int myMemberId;
@property(assign, nonatomic) bool isResume;

@property(strong, nonatomic) NSString *receipt;

+ (instancetype) shareInstance;
-(void)changeState:(bool) isResume;
-(void)initIAP;
@end

