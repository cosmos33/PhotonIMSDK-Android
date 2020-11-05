package com.cosmos.photonim.imbase.chat.emoji;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;

import java.util.List;

public class EmojiFragment extends RvBaseFragment {
    private RecyclerView recyclerView;
    private EmojiRecyclerAdapter emojiRecyclerAdapter;
    private List<EmojiBean> emojiBeans;
    private EmojiContainerFragment.OnEmojiClickListener onEmojiClickListener;
    private RecyclerView.RecycledViewPool recycledViewPool;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_emoji_emoji;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRv();
        recyclerView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(this.getContext(), 4);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (emojiRecyclerAdapter == null) {
            emojiRecyclerAdapter = new EmojiRecyclerAdapter(emojiBeans);
            emojiRecyclerAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    EmojiBean bean = (EmojiBean) data;
                    if (onEmojiClickListener != null) {
                        onEmojiClickListener.onEmojiClick(bean.emojiContent);
                    }
                }
            });
        }
        return emojiRecyclerAdapter;
    }

    public void setOnEmojiClickListener(EmojiContainerFragment.OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    public void setEmojiBeans(List<EmojiBean> emojiBeans) {
        this.emojiBeans = emojiBeans;
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool recycledViewPool) {
        this.recycledViewPool = recycledViewPool;
    }
}
