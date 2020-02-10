package com.cosmos.photonim.imbase.chat.preview;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.ChatData;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.image.IImageLoader;
import com.cosmos.photonim.imbase.utils.image.ImageLoaderUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class ImageFragment extends BaseFragment {
    @BindView(R2.id.photoView)
    PhotoView photoView;
    @BindView(R2.id.tvPreviewOrigin)
    TextView tvPreviewOrigin;
    @BindView(R2.id.tvDown)
    TextView tvDown;

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
            url = chatData.getThumbnailUrl();
        }
        if (url != null && url.startsWith("http")) {
            ImageLoaderUtils.getInstance().loadImage(view.getContext(), url, R.drawable.head_placeholder, photoView);
            setVisible(View.VISIBLE);
        } else {
            ImageLoaderUtils.getInstance().loadImageUri(view.getContext(), Uri.fromFile(new File(url)), R.drawable.head_placeholder, photoView);
            setVisible(View.GONE);
        }
    }

    private void setVisible(int visible) {
        tvDown.setVisibility(visible);
        tvPreviewOrigin.setVisibility(visible);
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

    @OnClick(R2.id.tvPreviewOrigin)
    public void onCheckOriginClick() {
        ImageLoaderUtils.getInstance().loadImage(getContext(), chatData.getFileUrl(), R.drawable.head_placeholder, photoView);
    }

    @OnClick(R2.id.tvDown)
    public void onDownClick() {
        ImageLoaderUtils.getInstance().downloadImage(getContext(), chatData.getFileUrl(), new IImageLoader.OnDownloadImageListener() {
            @Override
            public void onDownload(String path) {
                ToastUtils.showText(String.format("保存位置：%s", path));
            }
        });
    }
}
