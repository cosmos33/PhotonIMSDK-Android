package com.momo.demo.main.contacts.single;

import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactOnline;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.single.ionline.IOnlineUserModel;

import java.util.ArrayList;
import java.util.List;

public class OnlineUserModel extends IOnlineUserModel {
    private static final String TAG = "OnlineUserModel";

    @Override
    public void loadContacts(int itemType, OnLoadContactListener onLoadContactListener) {
        TaskExecutor.getInstance().createAsycTask(() -> getOnLineUsers(itemType),
                result -> {
                    if (onLoadContactListener != null) {
                        onLoadContactListener.onLoadContact((List<OnlineUserData>) result);
                    }
                });
    }

    public static Object getOnLineUsers(int itemType) {
        JsonResult jsonResult = HttpUtils.getInstance().getOnLineUsers(
                LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
        if (!jsonResult.success()) {
            return null;
        }
        JsonContactOnline jsonContactOnline = (JsonContactOnline) jsonResult.get();
        if (!jsonContactOnline.success()) {
//            if (onLoadContactListener != null) {
//                onLoadContactListener.onLoadContact(null);
//            }
            return null;
        }
        List<OnlineUserData> onlineUserData = new ArrayList<>(jsonContactOnline.getData().getLists().size());
        List<JsonContactOnline.DataBean.ListsBean> lists = jsonContactOnline.getData().getLists();
        List<Profile> profiles = new ArrayList<>(lists.size());
        OnlineUserData temp;
        Profile profile;
        for (JsonContactOnline.DataBean.ListsBean list : lists) {
            temp = new OnlineUserData.Builder()
                    .icon(list.getAvatar())
                    .msgId(list.getUserId())
                    .nickName(list.getNickname())
                    .type(list.getType())
                    .itemType(itemType)
                    .build();
            onlineUserData.add(temp);

            profile = new Profile();
            profile.setName(list.getNickname());
            profile.setIcon(list.getAvatar());
            profile.setUserId(list.getUserId());
            profiles.add(profile);
        }
        DBHelperUtils.getInstance().saveProfiles(profiles);
        return onlineUserData;
    }
}
