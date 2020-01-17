package com.cosmos.photonim.imbase.chat.album.preview;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.chat.album.AlbumPresenter;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewFragment extends BaseFragment {
    @BindView(R2.id.cbCheck)
    CheckBox cbCheck;
    @BindView(R2.id.tvSend)
    TextView tvSend;
    @BindView(R2.id.photoView)
    PhotoView photoView;

    private CategoryFile categoryFile;
    private ImagePreviewActivity.OnCheckStatusListener onCheckStatusListener;
    private int keepCheckedSize = -1;

    public static ImagePreviewFragment getInstance(CategoryFile categoryFile) {
        ImagePreviewFragment imageFragment = new ImagePreviewFragment();
        imageFragment.categoryFile = categoryFile;
        return imageFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_image_preview;
    }

    @Override
    protected void initView(View view) {
//        ImageLoaderUtils.getInstance().loadImage(getContext(), categoryFile.mPath, R.drawable.chat_placeholder, view.findViewById(R.id.ivImage));
        photoView.setImageURI(Uri.parse(categoryFile.mPath));
        cbCheck.setChecked(categoryFile.checked);
        if (keepCheckedSize != -1) {
            tvSend.setText(String.format("发送 %d/%d", keepCheckedSize, AlbumPresenter.MAX_CHECKED));
        }
    }

    @OnClick(R2.id.cbCheck)
    public void onCheckBoxClick() {
        if (onCheckStatusListener != null) {
            categoryFile.checked = cbCheck.isChecked();
            boolean result = onCheckStatusListener.onCheckStatusChange(categoryFile);
            if (!result) {
                cbCheck.setChecked(!cbCheck.isChecked());
            }
        }
    }

    @OnClick(R2.id.tvSend)
    public void onSendClick() {
        if (onCheckStatusListener != null) {
            onCheckStatusListener.finish(true);
        }
    }


    public void setOnCheckStatusListener(ImagePreviewActivity.OnCheckStatusListener onCheckStatusListener) {
        this.onCheckStatusListener = onCheckStatusListener;
    }

    public void setCheckedCount(int checkedSize) {
        if (tvSend != null) {
            tvSend.setText(String.format("发送 %d/%d", checkedSize, AlbumPresenter.MAX_CHECKED));
        }
    }

    public void initCheckedSize(int checkedSize) {
        keepCheckedSize = checkedSize;
    }
}
