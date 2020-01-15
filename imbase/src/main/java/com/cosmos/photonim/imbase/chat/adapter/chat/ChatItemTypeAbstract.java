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
    private int[] containers = {
            R.id.llVoice,
            R.id.ivPic,
            R.id.llLocation,
            R.id.llFileContainer,
            R.id.flVideo,
            R.id.tvContent};

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
                setVisible(rvViewHolder, R.id.tvContent);

                TextView content = (TextView) rvViewHolder.getView(R.id.tvContent);
                content.setVisibility(View.VISIBLE);
                content.setText(chatData.getContentShow());
                break;
            case PhotonIMMessage.AUDIO:
                setVisible(rvViewHolder, R.id.llVoice);

                ((TextView) rvViewHolder.getView(R.id.tvVoiceDuration)).setText(chatData.getMediaTime() + "");
                ImageLoaderUtils.getInstance().loadImage(view.getContext(), chatData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));

                break;
            case PhotonIMMessage.IMAGE:
                setVisible(rvViewHolder, R.id.ivPic);

                ImageView pic = (ImageView) rvViewHolder.getView(R.id.ivPic);
                pic.setVisibility(View.VISIBLE);
                // TODO: 2019-08-07 chatData.getLocalFile() 判断对方的
                if (!TextUtils.isEmpty(chatData.getLocalFile())) {
                    ImageLoaderUtils.getInstance().loadImage(pic.getContext(), chatData.getLocalFile(), R.drawable.chat_placeholder, pic);
                } else {
                    ImageLoaderUtils.getInstance().loadImage(pic.getContext(), chatData.getFileUrl(), R.drawable.chat_placeholder, pic);
                }
                break;
            case PhotonIMMessage.LOCATION:
                setVisible(rvViewHolder, R.id.llLocation);

                TextView tvLocation = (TextView) rvViewHolder.getView(R.id.tvLocation);
                if (chatData.getLocation() != null) {
                    tvLocation.setText(String.format("%s\n%s", StringUtils.getTextContent(chatData.getLocation().address),
                            StringUtils.getTextContent(chatData.getLocation().detailedAddress)));
                }
                break;
            case PhotonIMMessage.FILE:
                setVisible(rvViewHolder, R.id.llFileContainer);

                TextView fileName = (TextView) rvViewHolder.getView(R.id.tvFileName);
                fileName.setText(chatData.getFileName());
                TextView fileSize = (TextView) rvViewHolder.getView(R.id.tvFileSize);
                fileSize.setText(chatData.getFileSize());
                break;
            case PhotonIMMessage.VIDEO:
                setVisible(rvViewHolder, R.id.flVideo);

                ImageView ivCover = (ImageView) rvViewHolder.getView(R.id.ivCover);
                ivCover.setVisibility(View.VISIBLE);
                // TODO: 2019-08-07 chatData.getLocalFile() 判断对方的
                if (!TextUtils.isEmpty(chatData.getLocalFile())) {
                    ImageLoaderUtils.getInstance().loadImage(ivCover.getContext(), chatData.getLocalFile(), R.drawable.chat_placeholder, ivCover);
                } else {
                    ImageLoaderUtils.getInstance().loadImage(ivCover.getContext(), chatData.getFileUrl(), R.drawable.chat_placeholder, ivCover);
                }
                ((TextView) rvViewHolder.getView(R.id.tvTime)).setText(chatData.getMediaTimeShow());
                break;
        }
        ImageLoaderUtils.getInstance().loadImage(view.getContext(), chatData.getIcon(), R.drawable.head_placeholder, (ImageView) rvViewHolder.getView(R.id.ivIcon));
    }

    private void setVisible(RvViewHolder rvViewHolder, int id) {
        for (int container : containers) {
            if (container == id) {
                rvViewHolder.getView(container).setVisibility(View.VISIBLE);
            } else {
                rvViewHolder.getView(container).setVisibility(View.GONE);
            }
        }
    }
}
