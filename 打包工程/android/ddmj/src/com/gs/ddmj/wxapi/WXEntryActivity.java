package com.gs.ddmj.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.cocos2dx.javascript.AndroidHelper;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidHelper.getWX().handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AndroidHelper.getWX().handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登录成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                final String code = sendResp.code;
                AndroidHelper.getHelper().callToJS("dd.native_call_js.getAccessToken('" + code + "')");
            }
            if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享成功
                AndroidHelper.getHelper().callToJS("dd.native_call_js.shareCallback(0)");
            }
        } else {
            if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                AndroidHelper.getHelper().callToJS("dd.native_call_js.loginError()");
            }
            if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                AndroidHelper.getHelper().callToJS("dd.native_call_js.shareCallback(-1)");
            }
        }
        finish();
    }
}