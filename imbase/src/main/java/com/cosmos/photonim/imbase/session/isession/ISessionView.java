package com.cosmos.photonim.imbase.session.isession;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.cosmos.photonim.imbase.base.IRvBaseFragmentView;

public abstract class ISessionView extends IRvBaseFragmentView<ISessionPresenter> {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
    }

    public abstract void notifyItemInserted(int position);

    public abstract void notifyItemChanged(int position);

    public abstract void notifyDataSetChanged();

    public abstract void setNoMsgViewVisibility(boolean b);

    public abstract void dismissSessionDialog();
}
