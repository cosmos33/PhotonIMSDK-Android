package com.cosmos.photonim.imbase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.emoji.EmojiUtils;
import com.cosmos.photonim.imbase.utils.LocalRestoreUtils;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

public class ImBaseBridge {
    private Application application;
    private OnRelayClickListener onRelayClickListener;
    private IGetUserIconListener iGetUserIconListener;
    private OnKickUserListener onKickUserListener;
    private OnGroupInfoClickListener onGroupInfoClickListener;
    private String appId;

    private IAtListener iAtListener;
    private List<String> joinedGids;

    private ImBaseBridge(Builder builder) {
        appId = builder.appId;
    }

    public void setMyIcon(String avatar) {
        LoginInfo.getInstance().setMyIcon(avatar);
    }

    public void setTokenId(String token) {
        LoginInfo.getInstance().setTokenId(token);
    }


    public String getUserId() {
        checkinit();
        return LoginInfo.getInstance().getUserId();
    }

    private void checkinit() {
        if (application == null) {
            throw new IllegalStateException("init should call frist");
        }
    }

    public String getSessenId() {
        return LoginInfo.getInstance().getSessenId();
    }

    public void setLoginInfo(String sessionId, String userId) {
        LoginInfo.getInstance().setSessenId(sessionId);
        LoginInfo.getInstance().setUserId(userId);
    }

    public OnKickUserListener getOnStickListener() {
        return onKickUserListener;
    }

    public IAtListener getiAtListener() {
        return iAtListener;
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
        onRelayClickListener = builder.onRelayClickListener;
        iGetUserIconListener = builder.iGetUserIconListener;
        onKickUserListener = builder.onKickUserListener;
        onGroupInfoClickListener = builder.onGroupInfoClickListener;
        iAtListener = builder.iAtListener;
        PhotonIMClient.getInstance().supportGroup();
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

    public OnRelayClickListener getOnRelayClickListener() {
        return onRelayClickListener;
    }

    public IGetUserIconListener getiGetUserIconListener() {
        return iGetUserIconListener;
    }

    public OnGroupInfoClickListener getOnGroupInfoClickListener() {
        return onGroupInfoClickListener;
    }

    public void startIm() {
        IMReceiveHelper.getInstance().start();
    }

    public void stopIm(Context context) {
        IMReceiveHelper.getInstance().stop();
    }

    public interface OnRelayClickListener {
        void onRelayClick(Activity activity, ChatData chatData);
    }

    public interface IGetUserIconListener {
        void getUserIcon(String userId, OnGetUserIconListener onGetUserIconListener);
    }

    public interface IAtListener {
        void onAtListener(Activity activity, String gid);
    }

    public interface OnGetUserIconListener {
        void onGetUserIcon(String iconUrl, String name);
    }

    public interface OnKickUserListener {
        void onKickUser(Activity activity);
    }

    public interface OnGroupInfoClickListener {
        void onGroupInfoClick(Activity activity, String gId);
    }

    public static final class Builder {
        private Application application;
        private OnRelayClickListener onRelayClickListener;
        private IGetUserIconListener iGetUserIconListener;
        private OnKickUserListener onKickUserListener;
        private OnGroupInfoClickListener onGroupInfoClickListener;
        private IAtListener iAtListener;
        private String appId;

        public Builder() {
        }

        public Builder application(Application val) {
            application = val;
            return this;
        }

        public Builder onRelayClickListener(OnRelayClickListener val) {
            onRelayClickListener = val;
            return this;
        }

        public Builder iGetUserIconListener(IGetUserIconListener val) {
            iGetUserIconListener = val;
            return this;
        }

        public Builder onKickUserListener(OnKickUserListener val) {
            onKickUserListener = val;
            return this;
        }

        public Builder onGroupInfoClickListener(OnGroupInfoClickListener val) {
            onGroupInfoClickListener = val;
            return this;
        }

        public Builder iAtListener(IAtListener val) {
            iAtListener = val;
            return this;
        }

        public Builder appId(String val) {
            appId = val;
            return this;
        }
    }
}
