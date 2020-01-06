package com.cosmos.photonim.imbase.chat.media;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.media.itakephoto.ITakePhotoView;
import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public class TakePhotoActivity extends ITakePhotoView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_takephoto);
    }

    @Override
    public IPresenter getIPresenter() {
        return new TakePhotoPresenter(this);
    }
}
