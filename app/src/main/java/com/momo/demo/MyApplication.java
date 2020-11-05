package com.momo.demo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.cosmos.photon.push.PhotonPushManager;
import com.cosmos.photon.push.PushMessageReceiver;
import com.cosmos.photon.push.log.LogUtil;
import com.cosmos.photon.push.msg.MoMessage;
import com.cosmos.photon.push.notification.MoNotify;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.AppContext;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.SharedPrefUtil;
import com.cosmos.photonim.imbase.utils.ContextHolder;
import com.immomo.media_cosmos.RecorderFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyApplication extends Application {
//    public static final String APP_ID = "280f8ef2cec41cde3bed705236ab9bc4";
    public static final String APP_ID = "9122fba3a09654f2972c0fde0ad19f96";
    private static final String APP_ID_HW = "326a7a61d5e8f170957f9bf6591a7c9b";
    private static MyApplication myApplication;


    public static final String NOTIFICATION_CHANNEL_ID_DEFAULT = "notification.default";
    public static final String NOTIFICATION_CHANNEL_ID_MSG = "notification.msg";
    public static final String NOTIFICATION_CHANNEL_ID_OTHERS = "notification.others";
    private static String pushToken;
    String appid = APP_ID;

    @Override
    public void onCreate() {
        myApplication = this;
        super.onCreate();
        AppContext.init(this);
        pushInit();
        mediaInit();
        imInit();
        ContextHolder.init(this);
        initLifeCycle();
    }

    private void initLifeCycle() {
        registerActivityLifecycleCallbacks(new ActivityRecorder());
    }

    private void imInit() {
        ImBaseBridge.Builder builder = new ImBaseBridge.Builder()
                .application(this)
                .appId(appid)
                .addListener(getListener());

        ImBaseBridge.getInstance().init(builder);
    }

    private void mediaInit() {
        RecorderFactory.init(this, appid);
    }

    private void pushInit() {
        {
            LogUtil.init(this, new File(getFilesDir(), "MDLOG.log").getAbsolutePath());
            LogUtil.setLogOpen(true);
        }
        SharedPrefUtil.saveServerType(appid == APP_ID?0:1);
//        int serverType = SharedPrefUtil.getServerType(0);
//        if(serverType == 1){
//            appid = APP_ID_HW;
//        }
        Log.e("PIM","当前APPID:"+appid);
        // push初始化
        // 如果targetSdkVersion >= 26, 则CHANNEL_MODE必须设置为true
        PhotonPushManager.CHANNEL_MODE = true;

        // 如果targetSdkVersion >= 26，客户端需创建NotificationChannel
        createDefaultChannel(this);
        createMSGChannel(this);
        createOtherChannel(this);

        PhotonPushManager.getInstance().init(this, appid, new PushMessageReceiver() {
            // 通知栏展示前调用
            @Override
            public boolean onNotificationShow(MoNotify notify) {
                Log.i("DEBUG", "通知栏展示调用前");
//                showToast("通知栏展示调用前");
                if (TextUtils.isEmpty(notify.channelId)) {
                    notify.channelId = NOTIFICATION_CHANNEL_ID_DEFAULT;
                }
                return super.onNotificationShow(notify);
            }

            // 通知栏被电击调用
            @Override
            public boolean onNotificationMessageClicked(MoNotify notify) {
                Log.i("DEBUG", "通知栏被电击");
                return super.onNotificationMessageClicked(notify);
            }

            // 收到透传消息调用
            @Override
            public void onCommand(int type, int result, String message) {
                if (result == 0) {
                    LogUtils.log("DEBUG","操作成功");
                }
            }

            @Override
            public void onReceivePassThroughMessage(MoMessage moMessage){
                LogUtils.log("DEBUG","收到透传消息");
            }

            @Override
            public void onToken(int result, String token, String message) {
                MyApplication.pushToken = token;
                for (PushTokenObserver pushTokenObserver : observers) {
                    pushTokenObserver.onReceiveToken(token);
                }
                if (result == 0) {
//                    showToast("push注册成功");
                } else {
//                    showToast(message);
                }
            }

            @Override
            public boolean isMiPushOpen() {
                return false;
            }

            @Override
            public boolean isHuaweiPushOpen() {
                return false;
            }

            @Override
            public boolean isVivoPushOpen() {
                return false;
            }

            @Override
            public boolean isOppoPushOpen() {
                return false;
            }

            @Override
            public boolean isMeizuPushOpen() {
                return false;
            }

//            @Override
//            public int getServerType(){
//                if(SharedPrefUtil.getServerType(0) == 1){
//                    return PushMessageReceiver.SERVER_OVERSEA;
//                }
//                return PushMessageReceiver.SERVER_INLAND;
//            }
        });
    }

    private ImBaseBridge.BusinessListener getListener() {
        return new BusinessCallback();
    }

    public static MyApplication getApplication() {
        return myApplication;
    }

//    public String getTokenId() {
//        return tokenId;
//    }
//
//    public void setTokenId(String tokenId) {
//        this.tokenId = tokenId;
//    }

//    public String getInfo() {
//        return icon;
//    }

    public interface PushTokenObserver {
        void onReceiveToken(String token);

        void onReceiveMessage(MoMessage moMessage);
    }

    private final static List<PushTokenObserver> observers = new CopyOnWriteArrayList<>();

    public static void registerPushTokenObserver(PushTokenObserver observer) {
        observers.add(observer);
    }

    public static void unregisterPushTokenObserver(PushTokenObserver observer) {
        observers.remove(observer);
    }

    public static String getPushToken() {
        return pushToken;
    }

    public static void createDefaultChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = nm.getNotificationChannel(NOTIFICATION_CHANNEL_ID_DEFAULT);
        if (null == notificationChannel) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_DEFAULT, "默认通知", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("消息推送默认通知类别");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{50, 100});

            nm.createNotificationChannel(notificationChannel);
        }
    }

    void createMSGChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = nm.getNotificationChannel(NOTIFICATION_CHANNEL_ID_MSG);
        if (null == notificationChannel) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_MSG, "新消息通知", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("收到新消息时使用的通知类别");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{50, 100});

            notificationChannel.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ms2), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build());


            nm.createNotificationChannel(notificationChannel);
        }
    }

    void createOtherChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = nm.getNotificationChannel(NOTIFICATION_CHANNEL_ID_OTHERS);
        if (null == notificationChannel) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_OTHERS, "其他通知", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription("其他不重要消息的通知类别");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);

            nm.createNotificationChannel(notificationChannel);
        }
    }
}
