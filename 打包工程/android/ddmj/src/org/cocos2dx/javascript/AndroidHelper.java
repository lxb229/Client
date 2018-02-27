package org.cocos2dx.javascript;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.Toast;

import com.gs.ddmj.R;
import com.tencent.gcloud.voice.GCloudVoiceErrno;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.tencent.gcloud.voice.GCloudVoiceEngine;
import com.tencent.gcloud.voice.IGCloudVoiceNotify;


public class AndroidHelper {
    //管理类单例对象
    private static AndroidHelper helper = null;

    private AndroidHelper() {
    }

    public static AndroidHelper getHelper() {
        if (helper == null) {
            helper = new AndroidHelper();
        }
        return helper;
    }

    //AppActivity实例对象
    private AppActivity activity = null;
    //表示当前activity是否处于激活状态
    private boolean isResume = false;

    //activity激活状态改变时触发的方法
    public void activityLifecycle(boolean isResume) {
        this.isResume = isResume;
        if (isResume) {
            if (engine != null) {
                engine.Resume();
            }
        } else {
            if (engine != null) {
                engine.Pause();
            }
        }
    }

    /**
     * 调用JS方法
     *
     * @param msg js执行方法的字符串
     */
    public void callToJS(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (helper.isResume) {
                        activity.runOnGLThread(new Runnable() {
                            @Override
                            public void run() {
                                Cocos2dxJavascriptJavaBridge.evalString(msg);
                            }
                        });
                        break;
                    }
                }
            }
        }).start();
    }

    //微信管理对象
    private IWXAPI wxAPI = null;

    public static IWXAPI getWX() {
        return helper.wxAPI;
    }

    /**
     * 初始化activity和微信对象
     *
     * @param activity
     */
    public void init(AppActivity activity) {
        this.activity = activity;
        GCloudVoiceEngine.getInstance().init(activity.getApplicationContext(), activity);
    }

    /**
     * 初始化微信对象
     */
    public static int initWX(final String app_id, final  String app_key) {
        helper.wxAPI = WXAPIFactory.createWXAPI(helper.activity, app_id, false);
        helper.wxAPI.registerApp(app_id);
		if (!helper.wxAPI.isWXAppInstalled()) {
            return -1;
        }
        return 0;
    }

    /**
     * 调用系统振动
     */
    public static void phoneVibration() {
        Vibrator vibrator = (Vibrator) helper.activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
    }

    /**
     * 跳转进入对应的网页
     *
     * @param url 网页地址
     */
    public static void openBrowser(final String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        helper.activity.startActivity(intent);
    }

    /**
     * 获取对应的app版本号
     *
     * @return app版本号
     */
    public static String getAppVersion() {
        //获取包管理器
        PackageManager pm = helper.activity.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(helper.activity.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 微信登录
     */
    public static void wxLogin() {
        if (!helper.wxAPI.isWXAppInstalled()) {
            Toast.makeText(helper.activity, "没有安装微信，请先安装微信!", Toast.LENGTH_LONG).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";  //这里不能改
        req.state = "ddmj";  //这里改成你自己的数据
        helper.wxAPI.sendReq(req);
    }

    /**
     * 微信分享
     *
     * @param url   网页地址
     * @param title 标题
     * @param des   说明
     */
    public static void wxShare(final String url, final String title, final String des) {
        //封装一个链接，点击跳转到指定网址
        WXWebpageObject webpag = new WXWebpageObject();
        webpag.webpageUrl = url;
        //封装游戏图标
        Bitmap bitmap = BitmapFactory.decodeResource(helper.activity.getResources(), R.mipmap.ic_launcher);
        //封装分享内容
        WXMediaMessage msg = new WXMediaMessage(webpag);
        msg.thumbData = helper.bmpToByteArrary(bitmap, true, true);
        msg.title = title;  //这个是标题
        msg.description = des;  //这个是描述
        //封装请求
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = helper.buildTransaction("webpag");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        //发送请求
        helper.wxAPI.sendReq(req);
    }

    /**
     * 分享战绩
     *
     * @param filePath 图片绝对路径
     */
    public static void wxShareRecord(final String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        WXImageObject imgObj = new WXImageObject(bitmap);

        final WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        msg.thumbData = helper.bmpToByteArrary(bitmap, true, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = helper.buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        helper.wxAPI.sendReq(req);
    }

    /**
     * 把文本保存到系统剪切板
     *
     * @param text 需要保存的文本
     */
    public static void copyToClipboard(final String text) {
        try {
            helper.activity.runOnUiThread(new Runnable() {
                public void run() {
                    ClipboardManager clipboard = (ClipboardManager) helper.activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Text", text);
                    clipboard.setPrimaryClip(clip);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取电量信息
     *
     * @return 电量值（百分制）
     */
    public static int getBatteryLevel() {
        Intent batteryIntent = helper.activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int currLevel = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int total = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        int percent = currLevel * 100 / total;
        return percent;
    }

    /**
     * 将图片解析成一个二进制数组
     *
     * @param bitmap    图片对象
     * @param isRecycle 是否需要重绘
     * @return 字节数组
     */
    private byte[] bmpToByteArrary(final Bitmap bitmap, final boolean isCompress, final boolean isRecycle) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        if (isCompress) {
            int options = 100;
            while (outputStream.toByteArray().length > 32768) {
                outputStream.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
                if (options > 10) {
                    options -= 10;
                } else {
                    if (options > 5) {
                        options -= 5;
                    } else {
                        if (options > 1) {
                            options -= 1;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if (isRecycle) {
            bitmap.recycle();
        }
        byte[] result = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成一个微信唯一请求标识符
     *
     * @param type 标识符
     * @return 唯一标识符
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //音频管理对象
    private GCloudVoiceEngine engine = null;
    private String roomId = null;
    private int myMemberId = -1;
    private Notify notify = new Notify();
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            engine.Poll();
            super.handleMessage(msg);
        }
    };

    class Notify implements IGCloudVoiceNotify {
        @Override
        public void OnJoinRoom(int code, String roomName, int memberID) {
            if (code == GCloudVoiceCompleteCode.GV_ON_JOINROOM_SUCC) {
                helper.myMemberId = memberID;
                helper.callToJS("dd.native_call_js.voiceLoginCallback(0)");
            } else {
                helper.roomId = null;
                helper.myMemberId = -1;
                helper.callToJS("dd.native_call_js.voiceLoginCallback(-1)");
            }
        }

        @Override
        public void OnStatusUpdate(int status, String roomName, int memberID) {
            if (status == GCloudVoiceCompleteCode.GV_ON_ROOM_OFFLINE) {
                if (memberID == helper.myMemberId) {
                    helper.engine.JoinTeamRoom(roomName, 10000);
                }
            }
        }

        @Override
        public void OnQuitRoom(int code, String roomName) {
            if (code == GCloudVoiceCompleteCode.GV_ON_QUITROOM_SUCC) {
                helper.roomId = null;
                helper.myMemberId = -1;
                helper.callToJS("dd.native_call_js.voiceQuitCallback(0)");
            } else {
                helper.callToJS("dd.native_call_js.voiceQuitCallback(-1)");
            }
        }

        @Override
        public void OnMemberVoice(int[] members, int count) {

        }

        @Override
        public void OnUploadFile(int code, String filePath, String fileID) {

        }

        @Override
        public void OnDownloadFile(int code, String filePath, String fileID) {

        }

        @Override
        public void OnPlayRecordedFile(int code, String filePath) {

        }

        @Override
        public void OnApplyMessageKey(int code) {

        }

        @Override
        public void OnSpeechToText(int code, String fileID, String result) {

        }

        @Override
        public void OnRecording(char[] pAudioData, int nDataLength) {

        }

        @Override
        public void OnStreamSpeechToText(int i, int i1, String s) {

        }
    }

    /**
     * 初始化
     *
     * @param openID 用户ID
     * @return
     */
    public static int initVoice(final String openID,final String app_id, final String app_key) {
        helper.engine = GCloudVoiceEngine.getInstance();
        int result = helper.engine.SetAppInfo(app_id, app_key, openID);
        if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
        result = helper.engine.Init();
        if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
        result = helper.engine.SetMode(GCloudVoiceEngine.Mode.RealTime);
        if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
        result = helper.engine.SetNotify(helper.notify);
        if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
        TimerTask task = new TimerTask() {
            public void run() {
                Message message = new Message();
                helper.handler.sendMessage(message);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 500, 500);
        return 0;
    }

    /**
     * 进入指定房间
     *
     * @param roomId 房间ID
     */
    public static int joinTeamRoom(final String roomId) {
        if (helper.roomId != null) return -1;
        int result = helper.engine.JoinTeamRoom(roomId, 10000);
        if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
        helper.roomId = roomId;
        return 0;
    }

    /**
     * 退出房间
     */
    public static int quitRoom() {
        if (helper.roomId == null) return -1;
        int result = helper.engine.QuitRoom(helper.roomId, 10000);
        if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
        return 0;
    }

    /**
     * 设置麦克风和扬声器的开关
     *
     * @param isOpen 开关控制
     * @return
     */
    public static int setState(boolean isOpen) {
        if (helper.roomId == null) return -1;
        if (isOpen) {
            int result = helper.engine.OpenMic();
            if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
            result = helper.engine.OpenSpeaker();
            if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
            return 0;
        } else {
            int result = helper.engine.CloseMic();
            if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
            result = helper.engine.CloseSpeaker();
            if (result != GCloudVoiceErrno.GCLOUD_VOICE_SUCC) return -1;
            return 0;
        }
    }
}