package com.cosmos.photonim.imbase.chat.file.ifile;

import com.cosmos.photonim.imbase.base.mvp.IRVBaseFragmentView;

public abstract class IFileView extends IRVBaseFragmentView<IFilePresenter> {
    public abstract void progress(boolean show);

    public abstract void notifyData();
}
