package com.cosmos.photonim.imbase.session;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public class SessionItem extends ItemTypeAbstract {
    private static final int UNREAD_MAX_SHOW = 99;
    private final UpdateOtherInfoListener updateOtherInfoListener;
    private ImageView iconView;
    private SessionData sessionData;

    public SessionItem(UpdateOtherInfoListener updateOtherInfoListener) {
        this.updateOtherInfoListener = updateOtherInfoListener;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean openLongClick() {
        return true;
    }

    @Override
    public int getType() {
        return Constants.ITEM_TYPE_MSG;
    }

    @Override
    public int getLayout() {
        return R.layout.item_msg;
    }

    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        sessionData = (SessionData) data;
        boolean isSendFromMe = false;
        if (sessionData.getLastMsgFr() != null) {
            isSendFromMe = sessionData.getLastMsgFr().equals(ImBaseBridge.getInstance().getUserId());
        }
        sessionData.setItemPosition(position);
        if (!TextUtils.isEmpty(sessionData.getNickName())) {
            ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(sessionData.getNickName());
        } else {
            ((TextView) rvViewHolder.getView(R.id.tvNickName)).setText(sessionData.getChatWith());
        }

        if (sessionData.isIgnoreAlert()) {
            rvViewHolder.getView(R.id.ivBan).setVisibility(View.VISIBLE);
        } else {
            rvViewHolder.getView(R.id.ivBan).setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(sessionData.getTimeContent())) {
            ((TextView) rvViewHolder.getView(R.id.tvTime)).setText("");
        } else {
            ((TextView) rvViewHolder.getView(R.id.tvTime)).setText(sessionData.getTimeContent());
        }
//       Z if (sessionData.isSticky()) {
//            rvViewHolder.getView(R.id.ivTop).setVisibility(View.VISIBLE);
//        } else {
        rvViewHolder.getView(R.id.ivTop).setVisibility(View.GONE);
//        }
        ((TextView) rvViewHolder.getView(R.id.tvMsgContent)).setText(getMsgContent(sessionData));
        if (sessionData.getUnreadCount() > 0) {
            TextView tvTemp = (TextView) rvViewHolder.getView(R.id.tvUnRead);
            tvTemp.setVisibility(View.VISIBLE);
//            if (sessionData.getUnreadCount() > UNREAD_MAX_SHOW) {
//                tvTemp.setText(UNREAD_MAX_SHOW + "+");
//            } else {
            tvTemp.setText(sessionData.getUnreadCount() + "");
//            }
        } else {
            rvViewHolder.getView(R.id.tvUnRead).setVisibility(View.GONE);
        }
        iconView = (ImageView) rvViewHolder.getView(R.id.ivIcon);
        ImageLoaderUtils.getInstance().loadImage(iconView.getContext(), sessionData.getIcon(), R.drawable.head_placeholder, iconView);

        if (updateOtherInfoListener != null) {
            if (sessionData.getNickName() == null || shouldUpdateFromName(isSendFromMe)) {
                updateOtherInfoListener.onUpdateOtherInfo(sessionData);
            }
        }

    }

    protected CharSequence getMsgContent(SessionData sessionData) {
        if (sessionData.isShowAtTip()) {
            return sessionData.getAtMsg();
        } else {
            if (TextUtils.isEmpty(sessionData.getLastMsgFrName())) {
                return sessionData.getLastMsgContent();
            } else {
                return String.format("%s:%s", sessionData.getLastMsgFrName(), sessionData.getLastMsgContent());
            }
        }
    }

    private boolean shouldUpdateFromName(boolean isSendFromMe) {
        return (sessionData.getChatType() == PhotonIMMessage.GROUP && sessionData.isUpdateFromInfo() && !isSendFromMe);
    }

    @Override
    public int[] getOnClickViews() {
        return new int[]{R.id.item_msg_llRoot};
    }

    @Override
    public int[] getOnLongClickViews() {
        return new int[]{R.id.item_msg_llRoot};
    }

    public interface UpdateOtherInfoListener {
        void onUpdateOtherInfo(SessionData sessionData);
    }
}
