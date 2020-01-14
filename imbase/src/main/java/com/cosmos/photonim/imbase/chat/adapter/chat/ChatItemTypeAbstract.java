package com.cosmos.photonim.imbase.chat.adapter.chat;

import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.StringUtils;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemTypeAbstract;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvViewHolder;

public abstract class ChatItemTypeAbstract extends ItemTypeAbstract {
    protected ChatData chatData;
    private String illegalContent;
    private String sendStatsRead;
    private String sendStatsSent;

    @CallSuper
    @Override
    public void fillContent(RvViewHolder rvViewHolder, int position, ItemData data) {
        chatData = (ChatData) data;
        chatData.setListPostion(position);
        TextView view = (TextView) rvViewHolder.getView(R.id.tvTime);
        if (chatData.getTimeContent() != null) {
            view.setVisibility(View.VISIBLE);
            view.setText(chatData.getTimeContent());
        } else {
            view.setVisibility(View.GONE);
        }
    }

    protected void fillMsgContent(RvViewHolder rvViewHolder) {
        View view = rvViewHolder.getView(R.id.tvSysInfo);
        view.setVisibility(View.GONE);
        rvViewHolder.getView(R.id.llMsgRoot).setVisibility(View.VISIBLE);
        switch (chatData.getMsgType()) {
            case PhotonIMMessage.TEXT:
                rvViewHolder.getView(R.id.llVoice).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.ivPic).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llLocation).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llFileContainer).setVisibility(View.GONE);

                TextView content = (TextView) rvViewHolder.getView(R.id.tvContent);
                content.setVisibility(View.VISIBLE);
                content.setText(chatData.getContentShow());
                ImageLoaderUtils.getInstance().loadImage(view.getContext(), chatData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));
                break;
            case PhotonIMMessage.AUDIO:
                rvViewHolder.getView(R.id.tvContent).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.ivPic).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llLocation).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llFileContainer).setVisibility(View.GONE);

                rvViewHolder.getView(R.id.llVoice).setVisibility(View.VISIBLE);
                ((TextView) rvViewHolder.getView(R.id.tvVoiceDuration)).setText(chatData.getMediaTime() + "");
                ImageLoaderUtils.getInstance().loadImage(view.getContext(), chatData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));

                break;
            case PhotonIMMessage.IMAGE:
                rvViewHolder.getView(R.id.tvContent).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llVoice).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llLocation).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llFileContainer).setVisibility(View.GONE);

                ImageView pic = (ImageView) rvViewHolder.getView(R.id.ivPic);
                pic.setVisibility(View.VISIBLE);
                // TODO: 2019-08-07 chatData.getLocalFile() 判断对方的
                if (!TextUtils.isEmpty(chatData.getLocalFile())) {
                    ImageLoaderUtils.getInstance().loadImage(pic.getContext(), chatData.getLocalFile(), R.drawable.chat_placeholder, pic);
                } else {
                    ImageLoaderUtils.getInstance().loadImage(pic.getContext(), chatData.getFileUrl(), R.drawable.chat_placeholder, pic);
                }
                ImageLoaderUtils.getInstance().loadImage(view.getContext(), chatData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));
                break;
            case PhotonIMMessage.LOCATION:
                rvViewHolder.getView(R.id.tvContent).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.ivPic).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llVoice).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llFileContainer).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llLocation).setVisibility(View.VISIBLE);

                TextView tvLocation = (TextView) rvViewHolder.getView(R.id.tvLocation);
                tvLocation.setText(String.format("%s\n%s", StringUtils.getTextContent(chatData.getLocation().address),
                        StringUtils.getTextContent(chatData.getLocation().detailedAddress)));
                ImageLoaderUtils.getInstance().loadImage(view.getContext(), chatData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));
                break;
            case PhotonIMMessage.FILE:
                rvViewHolder.getView(R.id.tvContent).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.ivPic).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llVoice).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llLocation).setVisibility(View.GONE);
                rvViewHolder.getView(R.id.llFileContainer).setVisibility(View.VISIBLE);

                TextView fileName = (TextView) rvViewHolder.getView(R.id.tvFileName);
                fileName.setText(chatData.getFileName());
                TextView fileSize = (TextView) rvViewHolder.getView(R.id.tvFileSize);
                fileSize.setText(chatData.getFileSize());
                break;

        }
    }
}
