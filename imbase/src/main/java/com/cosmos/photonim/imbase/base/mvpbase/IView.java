package com.cosmos.photonim.imbase.base.mvpbase;

public interface IView<P extends IPresenter> {
    P getIPresenter();
}
