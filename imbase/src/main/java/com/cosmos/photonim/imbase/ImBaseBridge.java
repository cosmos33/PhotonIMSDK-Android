package com.cosmos.photonim.imbase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.emoji.EmojiUtils;
import com.cosmos.photonim.imbase.utils.LocalRestoreUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactRecent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

public class ImBaseBridge {
    private Application application;
    private BusinessListener businessListener;
    private String avatar;

    private List<String> joinedGids;

    private ImBaseBridge(Builder builder) {

    }

    public void setMyIcon(String avatar) {
        this.avatar = avatar;
    }

    public String getMyIcon() {
        return avatar;
    }

    public void setGids(List<String> joinedGids) {
        this.joinedGids = joinedGids;
    }

    public List<String> getJoinedGids() {
        return joinedGids;
    }

    public void addJoindGId(String groupID) {
        if (joinedGids == null) {
            joinedGids = new ArrayList<>();
        }
        if (!joinedGids.contains(groupID)) {
            joinedGids.add(groupID);
        }
    }

    public static class ImBaseInitHolder {
        public static ImBaseBridge imBaseBridge = new ImBaseBridge();
    }

    public static ImBaseBridge getInstance() {
        return ImBaseInitHolder.imBaseBridge;
    }

    private ImBaseBridge() {
    }

    public void init(Builder builder) {
        application = builder.application;
//        PhotonIMClient.getInstance().supportGroup();
        businessListener = builder.businessListener;
        PhotonIMClient.getInstance().openDebugLog();
        PhotonIMClient.getInstance().init(application, builder.appId);
        // TODO: 2019-08-18 maybe should change position
        EmojiUtils.getInstance().init();
    }

    public void logout() {
        LocalRestoreUtils.removeAuth();
        TaskExecutor.getInstance().createAsycTask(() -> {
                    PhotonIMClient.getInstance().logout();
                    PhotonIMClient.getInstance().detachUserId();
                    return null;
                }
        );
    }

    public Application getApplication() {
        return application;
    }

    public void startIm() {
        IMReceiveHelper.getInstance().start();
    }

    public void stopIm(Context context) {
        IMReceiveHelper.getInstance().stop();
    }

    public BusinessListener getBusinessListener() {
        return businessListener;
    }

    public interface OnGetUserIconListener {
        void onGetUserIcon(String iconUrl, String name);
    }

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
        String getTokenId();
    }


    public static final class Builder {
        private Application application;
        private String appId;
        private BusinessListener businessListener;

        public Builder() {
        }

        public Builder application(Application val) {
            application = val;
            return this;
        }

        public Builder appId(String val) {
            appId = val;
            return this;
        }

        public Builder addListener(BusinessListener listener) {
            this.businessListener = listener;
            return this;
        }
    }
}
