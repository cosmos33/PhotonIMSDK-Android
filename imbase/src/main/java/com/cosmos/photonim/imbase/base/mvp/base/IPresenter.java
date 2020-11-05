package com.cosmos.photonim.imbase.base.mvp.base;

import com.cosmos.photonim.imbase.utils.LogUtils;

import java.lang.ref.WeakReference;

public abstract class IPresenter<V extends IView, M extends IModel> {
    private static final String TAG = "IPresenter";
    protected WeakReference<V> iView;
    private M iModel;
    private V emptyView;

    public IPresenter(V iView) {
        M iModel = generateIModel();
        if (iModel == null) {
            throw new IllegalStateException("iMode is null");
        }
        init(iView, iModel);
        emptyView = getEmptyView();
        if (emptyView == null) {
            LogUtils.log(TAG, "may cause null pointer!!");
        }
    }

    //    @Override
    protected void init(V iView, M iModel) {
        this.iView = new WeakReference((V) iView);
        this.iModel = iModel;
    }

    //    @Override
    public V getIView() {
        return iView.get() == null ? emptyView : iView.get();
    }

    //    @Override
    public M getiModel() {
        return iModel;
    }

    //    protected abstract void init(V iView, M iModel);
//
    public abstract M generateIModel();
//    public abstract V getIView();

    public abstract V getEmptyView();
}
