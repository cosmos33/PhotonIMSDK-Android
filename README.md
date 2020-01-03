### 概述

该项目是基于PhotonIM sdk的demo，它提供了一些通用的 UI 组件，例如会话列表、聊天界面等，开发者可根据实际业务需求通过该demo快速地搭建自定义 IM 应用。Demo的组件在实现 UI 功能的同时，调用 IM SDK 相应的接口实现 IM 相关逻辑和数据的处理，因而开发者在使用 Demo 时只需关注自身业务或个性化扩展即可。

工程目录结构介绍：
- app module包含注册、登录、联系人、转发、群组信息、个人资料等功能；
- imbase module包含会话以及聊天界面；
- opuslib module包含录制以及播放音频功能。

#### 效果图展示
会话界面

![avatar](readme_pics/session.png)

聊天界面

![avatar](readme_pics/chat.png)

通讯录

![avatar](readme_pics/contacts.png)

个人信息

![avatar](readme_pics/myinfo.png)


#### 快速集成会话以及聊天界面

目前支持module的形式集成
```
implementation project(':imbase')
```
imbase module依赖opuslib module,不要忘记把opuslib module导入工程。



##### 初始化

在 Application 的 onCreate 中初始化：

```
ImBaseBridge.Builder builder = new ImBaseBridge.Builder()
                .application(this)
                .appId(APP_ID)
                .addListener(getListener());

ImBaseBridge.getInstance().init(builder);

public interface BusinessListener {
    //转发
    void onRelayClick(Activity activity, ChatData chatData);
    //获取用户icon
    void getUserIcon(String userId, OnGetUserIconListener onGetUserIconListener);
    //群聊@成员
    void onAtListener(Activity activity, String gid);
    //收到服务器踢人
    void onKickUser(Activity activity);
    //获取群组信息
    void onGroupInfoClick(Activity activity, String gId);
    //获取他人信息
    JsonResult getOthersInfo(String[] ids);
    //获取群组信息呢
    JsonResult getGroupProfile(String groupId);
    //获取最近联系人
    JsonContactRecent getRecentUser();
    //设置勿扰状态
    JsonResult setIgnoreStatus(String remoteId, boolean igoreAlert);
    //获取勿扰状态
    JsonResult getIgnoreStatus(String otherId);
    //上传语音文件
    JsonResult sendVoiceFile(String localFile);
    //上传图片
    JsonResult sendPic(String localFile);
    //返回用户id
    String getUserId();
    //返回tokenId
    public String getTokenId();
}
        
```

- 获取到token之后
```
ImBaseBridge.getInstance().startIm();
```
ImBaseBridge.getInstance().startIm();调用的结果会通过EventBus post IMStatus回调业务方auth的结果
### 注意事项：
- 使用的第三方库EventBus/okhttp3/butterknife/glide/gson/room





http包下的逻辑比如获取头像需要接入方根据自己服务器的逻辑实现。




cosmos中demo app的下载地址为：app/apks下的apk在github中的地址;
apks下apk如何生成：build