package com.cosmos.photonim.imbase.chat;

import android.content.Intent;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

import java.util.ArrayList;

public class ChatGroupActivity extends ChatBaseActivity {
    public static final int RESULT_AT = 250;
    public static final String EXTRA_RESULT_NAME = "EXTRA_RESULT_NAME";
    public static final String EXTRA_RESULT_ID = "EXTRA_RESULT_ID";
    public static final String EXTRA_ALL = "EXTRA_ALL";

    @Override
    protected void onInfoClick() {
        ImBaseBridge.getInstance().onGroupInfoClick(this, chatWith);
    }

    @Override
    protected void onAtCharacterInput() {
        ImBaseBridge.getInstance().onAtListener(this, chatWith);
    }

    @Override
    protected boolean isGroup() {
        return true;
    }

    @Override
    public IPresenter getIPresenter() {
        return new ChatGroupPresenter(this);
    }

    @Override
    protected String getChatIcon(PhotonIMMessage msg) {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_AT) {
            ArrayList<String> resultNames = data.getStringArrayListExtra(EXTRA_RESULT_NAME);
            if (CollectionUtils.isEmpty(resultNames)) {
                return;
            }
            if (data.getBooleanExtra(EXTRA_ALL, false)) {
                extraFragment.addAtContent(null, AT_ALL_CONTENT);
                return;
            }
            ArrayList<String> resultIds = data.getStringArrayListExtra(EXTRA_RESULT_ID);
            for (int i = 0; i < resultNames.size(); i++) {
                extraFragment.addAtContent(resultIds.get(i), resultNames.get(i) + " ");
            }
        }
    }
}
