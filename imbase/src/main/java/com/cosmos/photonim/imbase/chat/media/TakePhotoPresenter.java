package com.cosmos.photonim.imbase.chat.media;

import com.cosmos.photonim.imbase.chat.media.itakephoto.ITakePhotoPresenter;
import com.cosmos.photonim.imbase.chat.media.itakephoto.ITakePhotoView;
import com.cosmos.photonim.imbase.utils.mvpbase.IModel;

public class TakePhotoPresenter extends ITakePhotoPresenter {
    public TakePhotoPresenter(ITakePhotoView iView) {
        super(iView);
    }

    @Override
    public IModel generateIModel() {
        return new TakePhotoModel();
    }
}
