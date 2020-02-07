package com.cosmos.photonim.imbase.chat.image;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class ImageFragment extends BaseFragment {
    @BindView(R2.id.photoView)
    PhotoView photoView;
    private ChatData chatData;

    public static ImageFragment getInstance(ChatData imageUrl) {
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.chatData = imageUrl;
        return imageFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_image;
    }

    @Override
    protected void initView(View view) {
//        ImageLoaderUtils.getInstance().loadImage(getContext(), chatData, R.drawable.chat_placeholder, view.findViewById(R.id.ivImage));
        String url;
        if (!TextUtils.isEmpty(chatData.getLocalFile())) {
            url = chatData.getLocalFile();
        } else {
            url = Constants.getFlieUrl(chatData.getFileUrl());
        }
        if (url != null && url.startsWith("http")) {
            ImageLoaderUtils.getInstance().loadImage(view.getContext(), url, R.drawable.head_placeholder, photoView);
        } else {
            ImageLoaderUtils.getInstance().loadImageUri(view.getContext(), Uri.fromFile(new File(url)), R.drawable.head_placeholder, photoView);
        }
    }

    @OnClick(R2.id.ivClose)
    public void onCloseClick() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @OnClick(R2.id.tvForward)
    public void onForwardClick() {
        ImBaseBridge.getInstance().onForwardClick(getActivity(), chatData);
    }
}
