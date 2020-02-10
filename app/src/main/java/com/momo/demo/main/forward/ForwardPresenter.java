package com.momo.demo.main.forward;

import android.text.TextUtils;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.chat.ChatModel;
import com.cosmos.photonim.imbase.chat.ichat.IChatModel;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.event.ChatDataWrapper;
import com.cosmos.photonim.imbase.utils.http.HttpUtils;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonResult;
import com.cosmos.photonim.imbase.utils.http.jsons.JsonUploadImage;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.MyApplication;
import com.momo.demo.main.forward.iforward.IForwardPresenter;
import com.momo.demo.main.forward.iforward.IForwardView;

import org.greenrobot.eventbus.EventBus;

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
            case PhotonIMMessage.FILE:
            case PhotonIMMessage.VIDEO:
                if (TextUtils.isEmpty(chatData.getFileUrl())) {
                    List<ChatData> sendData = getChatData(chatData, selectedData, null);
                    if (!CollectionUtils.isEmpty(sendData)) {
                        for (ChatData sendDatum : sendData) {
                            sendFileMsg(sendDatum);
                        }
                    }
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

    private void sendFileMsg(ChatData sendDatum) {
        EventBus.getDefault().post(new ChatDataWrapper(sendDatum, PhotonIMMessage.SENDING, null));
        if (TextUtils.isEmpty(sendDatum.getFileUrl())) {// TODO: 2020-02-05 maybe judge file is exist?
            getiModel().uploadFile(sendDatum, new IChatModel.OnFileUploadListener() {// TODO: 2020-02-07 文件不需要重复上传
                @Override
                public void onFileUpload(boolean success, ChatData chatData, PhotonIMMessage photonIMMessage) {
                    if (success) {
                        getiModel().updateAndsendMsg(chatData, null);
                    } else {
                        getiModel().updateStatus(chatData.getChatType(), chatData.getChatWith(), chatData.getMsgId(), PhotonIMMessage.SEND_FAILED);
                        EventBus.getDefault().post(new ChatDataWrapper(chatData, ChatModel.MSG_ERROR_CODE_UPLOAD_PIC_FAILED, "上传失败"));
                    }
                }

                @Override
                public void onProgress(ChatData chatData, int progress) {

                }
            });
        } else {
            getiModel().updateAndsendMsg(sendDatum, null);
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

                    .videoCover(chatData.getVideoCover())
                    .videoTime(chatData.getVideoTimeL())
                    .videowhRatio(chatData.getVideowhRatio())
                    .build());
        }
        return result;
    }

    @Override
    public IChatModel generateIModel() {
        return new ChatModel();
    }
}
