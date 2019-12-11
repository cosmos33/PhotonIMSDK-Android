### 本lib包含功能
聊天会话(fragment)以及聊天(activity)

### 接入方式：
- gradle中依赖
```
implementation project(":imbase")
```
- application中调用
```
ImBaseBridge.Builder builder = new ImBaseBridge.Builder()
        .application(this)
        .appId(APP_ID)
        .iAtListener(getAtListener())//at 回调
        .iGetUserIconListener(getUserIconListener())//获取头像回调
        .onGroupInfoClickListener(getGroupInfoListener())//获取群组信息回调
        .onKickUserListener(getKickListener()) //获取踢人回调 
        .onRelayClickListener(getRelayClickListener()); // 获取转发回调

ImBaseBridge.getInstance().init(builder);
```
- 业务端登录成功之后调用
```
ImBaseBridge.getInstance().setLoginInfo(,,);
```
- 获取到token之后
```
ImBaseBridge.getInstance().setTokenId(token);
ImBaseBridge.getInstance().startIm(this);
```

### 注意事项：
- 使用的第三方库EventBus/okhttp3/butterknife/glide/gson/room

