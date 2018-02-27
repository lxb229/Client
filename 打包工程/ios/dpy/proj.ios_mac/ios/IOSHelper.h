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
@interface IOSHelper : NSObject<WXApiDelegate>

+ (instancetype) shareInstance;
@end

