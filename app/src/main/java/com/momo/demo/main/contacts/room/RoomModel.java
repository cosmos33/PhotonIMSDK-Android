package com.momo.demo.main.contacts.room;

import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonRooms;
import com.cosmos.photonim.imbase.utils.task.AsycTaskUtil;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.login.LoginInfo;
import com.momo.demo.main.contacts.room.adapter.RoomData;
import com.momo.demo.main.contacts.room.iroom.IRoomModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RoomModel extends IRoomModel {
    private OnRoomListener onRoomListener;

    @Override
    public void setRoomListener(OnRoomListener onLoadNearbyGroup) {
        onRoomListener = onLoadNearbyGroup;
    }

    @Override
    public void loadRooms() {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                JsonResult jsonResult = HttpUtils.getInstance().loadRooms(LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
                if (!jsonResult.success()) {
                    return null;
                }
                List<JsonRooms.DataBean.ListsBean> lists = ((JsonRooms) jsonResult.get()).getData().getLists();
                ArrayList<RoomData> data = new ArrayList<>(lists.size());
                RoomData temp;
                for (JsonRooms.DataBean.ListsBean list : lists) {
                    temp = new RoomData();
                    temp.setAvatar(list.getAvatar());
                    temp.setName(list.getName());
                    temp.setGid(list.getGid());
                    data.add(temp);
                }
                return data;
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                if (onRoomListener != null) {
                    onRoomListener.onLoadRoom(result != null, (List<RoomData>) result);
                }
            }
        });
    }

    @Override
    public void joinRooms(final String roomId) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return HttpUtils.getInstance().joinRooms(roomId, LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                JsonResult result1 = (JsonResult) result;
                if (onRoomListener != null) {
                    onRoomListener.onJoinRoomResult(result1.success(), roomId);
                }
            }
        });
    }

    @Override
    public void leaveRooms(String roomId) {
        TaskExecutor.getInstance().createAsycTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return HttpUtils.getInstance().leaveRooms(roomId, LoginInfo.getInstance().getSessionId(), LoginInfo.getInstance().getUserId());
            }
        }, new AsycTaskUtil.OnTaskListener() {
            @Override
            public void onTaskFinished(Object result) {
                JsonResult result1 = (JsonResult) result;
                if (onRoomListener != null) {
                    onRoomListener.onLeaveRoomResult(result1.success(), roomId);
                }
            }
        });
    }
}
