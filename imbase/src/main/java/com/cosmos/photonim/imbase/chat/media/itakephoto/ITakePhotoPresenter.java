package com.cosmos.photonim.imbase.chat.media.itakephoto;


import com.cosmos.photonim.imbase.utils.mvpbase.IPresenter;

public abstract class ITakePhotoPresenter<V extends ITakePhotoView, M extends ITakePhotoModel> extends IPresenter<V, M> {

    public ITakePhotoPresenter(V iView) {
        super(iView);
    }

    @Override
    public V getEmptyView() {
        return (V) new ITakePhotoView() {

            @Override
            public IPresenter getIPresenter() {
                return null;
            }
        };
    }
}
