package com.cosmos.photonim.imbase.utils.http;

import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonAuth;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactOnline;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonContactRecent;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGetGroupIgnoreInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGetIgnoreInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupJoin;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupMembers;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroupProfile;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonGroups;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonLogin;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonMyInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonNoData;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonOtherInfoMulti;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRegist;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRooms;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveGroupIgnoreInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSaveIgnoreInfo;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonSetNickName;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadFile;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    private static final HttpUtils ourInstance = new HttpUtils();

    public static HttpUtils getInstance() {
        return ourInstance;
    }

    private IHttpHelper iHttpHelper;

    private HttpUtils() {
        iHttpHelper = new OkHttpHelper();
    }

    public JsonResult login(String userName, String passWord) {
        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", passWord);
        return iHttpHelper.post(HttpContants.URL_LOGIN, map, JsonLogin.class);
    }

    public JsonResult getRecentUser(String sessionId, String userId) {
        return iHttpHelper.post(HttpContants.URL_RECENTUSER, (HashMap) null, getHeadMap(sessionId, userId), JsonContactRecent.class);
    }

    public JsonResult regist(String userName, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", pwd);
        return iHttpHelper.post(HttpContants.URL_REGIST, map, JsonRegist.class);
    }

    public JsonResult setIgnoreStatus(String remoteId, boolean open, String sessionId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("remoteid", remoteId);
        map.put("switch", open ? "0" : "1");//0（开启勿扰）1(关闭勿扰）
        return iHttpHelper.post(HttpContants.URL_SET_IGNORE, map, getHeadMap(sessionId, userId), JsonSaveIgnoreInfo.class);
    }

    public JsonResult getOnLineUsers(String sessionId, String userId) {
        return iHttpHelper.post(HttpContants.URL_CONTACT_ONLINE, (HashMap) null, getHeadMap(sessionId, userId), JsonContactOnline.class);
    }

    public JsonResult getAuth(String sessionId, String userId) {
        return iHttpHelper.post(HttpContants.URL_AUTH_, (HashMap) null, getHeadMap(sessionId, userId), JsonAuth.class);
    }

    public Object logout() {
        return null;
    }

    public JsonResult changeNickName(String nickName, String sessionId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("nickname", nickName);
        return iHttpHelper.post(HttpContants.URL_SET_NICKNAME, map, getHeadMap(sessionId, userId), JsonSetNickName.class);
    }

    public JsonResult getOtherInfo(String sessionId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("remoteid", id);
        return iHttpHelper.post(HttpContants.URL_OTHER_INFO, null, map, getHeadMap(sessionId, null), JsonOtherInfo.class);
    }

    public Object getMyInfo(String sessionId, String userId) {
        return iHttpHelper.post(HttpContants.URL_My_INFO, (HashMap) null, getHeadMap(sessionId, userId), JsonMyInfo.class);
    }

    public Object sendPic(String absolutePath, String sessionId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("fileUpload", absolutePath);
        return iHttpHelper.post(HttpContants.URL_UPLOAD_IMG_, absolutePath, map, getHeadMap(sessionId, userId), JsonUploadFile.class);
    }

    public Object sendVoiceFile(String absolutePath, String sessionId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("fileUpload", absolutePath);
        return iHttpHelper.post(HttpContants.URL_UPLOAD_AUDIO_, absolutePath, map, getHeadMap(sessionId, userId), JsonUploadFile.class);
    }

    public Object getOthersInfo(String[] otherId, String sessionId, String userId) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < otherId.length; i++) {
            if (i != 0) {
                buffer.append(",");
            }
            buffer.append(otherId[i]);
        }
        Map<String, String> map = new HashMap<>();
        map.put("remoteids", buffer.toString());
        return iHttpHelper.post(HttpContants.URL_OTHERS_INFO_MULTI, map, getHeadMap(sessionId, userId), JsonOtherInfoMulti.class);
    }

    public Object getFile(String fileUrl, String saveFileUrl, IChatModel.OnGetFileListener onGetFileListener) {
        return iHttpHelper.getFile(fileUrl, saveFileUrl, onGetFileListener);
    }

    public Object getIgnoreStatus(String sessionId, String userId, String remoteId) {
        Map<String, String> map = new HashMap<>();
        map.put("remoteid", remoteId);
        return iHttpHelper.post(HttpContants.URL_GET_IGNORE_INFO_, map, getHeadMap(sessionId, userId), JsonGetIgnoreInfo.class);
    }

    // 群
    public JsonResult getGroupIgnoreStatus(String sessionId, String userId, String gid) {
        Map<String,String> map = new HashMap<>();
        map.put("gid",gid);

        return iHttpHelper.post(HttpContants.URL_GROUP_GET_IGNORE_INFO_, map, getHeadMap(sessionId, userId), JsonGetGroupIgnoreInfo.class);
    }
//    public static final String URL_GROUP_MEMBERS = DEFAULT_API_HOST+"/group/remote/members";
//    public static final String URL_GROUP_OTHER_INFO = DEFAULT_API_HOST+"/group/remote/profile";
//    public static final String URL_GROUP_JOIN = DEFAULT_API_HOST+"/group/remote/join";
//    public static final String URL_GROUP_LIST = DEFAULT_API_HOST+"/contact/groups";
//
public JsonResult setGroupIgnoreStatus(String sessionId, String userId, String gid, int switchX) {
    Map<String, String> map = new HashMap<>();
    map.put("gid", gid);
    map.put("switch", switchX + "");
    return iHttpHelper.post(HttpContants.URL_GROUP_SET_IGNORE, map, getHeadMap(sessionId, userId), JsonSaveGroupIgnoreInfo.class);
}

    public JsonResult getGroupMembers(String sessionId, String userId, String gid) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        return iHttpHelper.post(HttpContants.URL_GROUP_MEMBERS, map, getHeadMap(sessionId, userId), JsonGroupMembers.class);
    }

    public JsonResult getGroupProfile(String sessionId, String userId, String gid) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        return iHttpHelper.post(HttpContants.URL_GROUP_PROFILE, map, getHeadMap(sessionId, userId), JsonGroupProfile.class);
    }

    public JsonResult joinGroup(String sessionId, String userId, String gid) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        return iHttpHelper.post(HttpContants.URL_GROUP_JOIN, map, getHeadMap(sessionId, userId), JsonGroupJoin.class);
    }

    public JsonResult getGroups(String sessionId, String userId) {
        return iHttpHelper.post(HttpContants.URL_GROUP_LIST, (HashMap) null, getHeadMap(sessionId, userId), JsonGroups.class);
    }

    public JsonResult loadRooms(String sessionId, String userId) {
        return iHttpHelper.post(HttpContants.URL_ROOM_LIST, (Map<String, String>) null, getHeadMap(sessionId, userId), JsonRooms.class);
    }

    public JsonResult joinRooms(String roomId, String sessionId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", roomId);
        return iHttpHelper.post(HttpContants.URL_ROOM_JOIN, map, getHeadMap(sessionId, userId), JsonNoData.class);
    }

    public JsonResult leaveRooms(String roomId, String sessionId, String userId) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", roomId);
        return iHttpHelper.post(HttpContants.URL_ROOM_LEAVE, map, getHeadMap(sessionId, userId), JsonNoData.class);
    }

    public JsonResult roomMember(String sessionId, String userId, String roomId) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", roomId);
        return iHttpHelper.post(HttpContants.URL_ROOM_MEMEBERS, map, getHeadMap(sessionId, userId), JsonGroupMembers.class);
    }

    private Map<String, String> getHeadMap(String sessionId, String userId) {
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Cookie", String.format("sessionId=%s;userId=%s", sessionId, userId));
        return headMap;
    }
}
