package com.momo.demo.main.contacts.single.userinfo;

import com.cosmos.photonim.imbase.utils.dbhelper.DBHelperUtils;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.single.userinfo.iuserinfo.IUserInfoModel;

public class UserInfoModel extends IUserInfoModel {

    @Override
    public void getUserInfo(String id, IUserInfoModel.OnUserInfoListener onUserInfoListener) {
        TaskExecutor.getInstance().createAsycTask(
                () -> {

                    // TODO: 2019-09-25 先从数据库读取
//                    Profile profile = DBHelperUtils.getInstance().findProfile(id);
//                    if (profile != null) {
//                        return profile;
//                    }
                    JsonResult otherInfo = HttpUtils.getInstance().getOtherInfo(LoginInfo.getInstance().getSessionId()
                            , id);
                    if (otherInfo.success()) {
                        JsonOtherInfo other = (JsonOtherInfo) otherInfo.get();
                        DBHelperUtils.getInstance().saveProfile(other.getData().getProfile().getUserId(),
                                other.getData().getProfile().getAvatar(), other.getData().getProfile().getNickname());
                    }
                    return otherInfo;
                }, result -> {
                    if (onUserInfoListener != null) {
                        JsonResult jsonResult = (JsonResult) result;
                        onUserInfoListener.onGetUserInfo((JsonOtherInfo) jsonResult.get());
                    }
                });
    }
}
