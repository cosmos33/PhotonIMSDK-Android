package com.momo.demo.main.forward;

import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadImage;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadVoice;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.MyApplication;
import com.momo.demo.main.forward.iforward.IForwardPresenter;
import com.momo.demo.main.forward.iforward.IForwardView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ForwardPresenter extends IForwardPresenter<IForwardView, IChatModel> {
    private ForwardModel forwardModel;

    public ForwardPresenter(IForwardView iView) {
        super(iView);
        forwardModel = new ForwardModel();
    }

    @Override
    public void sendMsgToMulti(final ChatData chatData, Map<String, ForwardData> selectedData) {
        if (selectedData.size() == 0) {
            return;
        }
        switch (chatData.getMsgType()) {
            case PhotonIMMessage.IMAGE:
                if (TextUtils.isEmpty(chatData.getFileUrl())) {
                    TaskExecutor.getInstance().createAsycTask(() ->
                                    HttpUtils.getInstance().sendPic(chatData.getLocalFile(),
                                            com.momo.demo.login.LoginInfo.getInstance().getSessionId(), com.momo.demo.login.LoginInfo.getInstance().getUserId())
                            , result -> {
                                if (((JsonResult) result).success()) {
                                    getiModel().sendMsgMulti(getChatData(chatData, selectedData, ((JsonUploadImage) ((JsonResult) result).get()).getData().getUrl()), null);
                                } else {
                                    ToastUtils.showText(MyApplication.getApplication(), "发送失败");
                                }
                            });
                } else {
                    getiModel().sendMsgMulti(getChatData(chatData, selectedData, null), null);
                }
                break;
            case PhotonIMMessage.AUDIO:
                if (TextUtils.isEmpty(chatData.getFileUrl())) {
                    TaskExecutor.getInstance().createAsycTask(() ->
                                    HttpUtils.getInstance().sendVoiceFile(chatData.getLocalFile(),
                                            com.momo.demo.login.LoginInfo.getInstance().getSessionId(), com.momo.demo.login.LoginInfo.getInstance().getUserId())
                            , result -> {
                                if (((JsonResult) result).success()) {
                                    getiModel().sendMsgMulti(getChatData(chatData, selectedData, ((JsonUploadVoice) ((JsonResult) result).get()).getData().getUrl()), null);
                                } else {
                                    ToastUtils.showText(MyApplication.getApplication(), "发送失败");
                                }
                            });
                } else {
                    getiModel().sendMsgMulti(getChatData(chatData, selectedData, null), null);
                }
                break;
            case PhotonIMMessage.LOCATION:
            case PhotonIMMessage.TEXT:
                getiModel().sendMsgMulti(getChatData(chatData, selectedData, null), null);
                break;
        }
    }

    @Override
    public void loadContacts() {
        forwardModel.loadContacts(Constants.ITEM_TYPE_FORWARD, contactsData -> {
            if (contactsData == null || contactsData.size() == 0) {
                getIView().showContactsEmptyView();
            } else {
                getIView().loadContacts(contactsData);
            }
        });
    }

    @Override
    public void getOthersInfo(String userId, ForwardData forwardData) {
        forwardModel.getOtherInfo(userId, forwardData, new ForwardModel.OnGetOtherInfoListener() {
            @Override
            public void onGetOtherInfo(JsonResult result, ForwardData forwardData) {
                getIView().updateOtherInfo(result, forwardData);
            }
        });
    }


    private List<ChatData> getChatData(ChatData chatData, Map<String, ForwardData> selectedData, String fileUrl) {
        Iterator<ForwardData> iterator = selectedData.values().iterator();
        List<ChatData> result = new ArrayList<>(selectedData.size());
        String toId;
        ForwardData temp;
        ChatData.Location location;
        while (iterator.hasNext()) {
            temp = iterator.next();
            toId = temp.getUserId();
            location = chatData.getLocation();
            result.add(new ChatData.Builder()
                    .msgStatus(PhotonIMMessage.SENDING)
                    .itemType(Constants.ITEM_TYPE_CHAT_NORMAL_RIGHT)
                    .icon(ImBaseBridge.getInstance().getMyIcon())
                    .voiceDuration(chatData.getMediaTime())
                    .msgType(chatData.getMsgType())
                    .chatType(temp.getChatType())
                    .chatWith(toId)
                    .content(chatData.getContent())
                    .from(com.momo.demo.login.LoginInfo.getInstance().getUserId())
                    .to(toId)
                    .fileUrl(fileUrl == null ? chatData.getFileUrl() : fileUrl)
                    .time(System.currentTimeMillis())
                    .localFile(chatData.getLocalFile())
                    .msgId(UUID.randomUUID().toString())
                    .detailAddress(location == null ? null : location.detailedAddress)
                    .address(location == null ? null : location.address)
                    .lat(location == null ? 0 : location.lat)
                    .lng(location == null ? 0 : location.lng)
                    .build());
        }
        return result;
    }

    @Override
    public IChatModel generateIModel() {
        return new ChatModel();
    }
}
