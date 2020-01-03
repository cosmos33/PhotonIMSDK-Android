package com.cosmos.photonim.imbase.chat.emoji;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.RvBaseFragment;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;

import java.util.List;

import butterknife.ButterKnife;

public class EmojiFragment extends RvBaseFragment {
    private RecyclerView recyclerView;
    private EmojiRecyclerAdapter emojiRecyclerAdapter;
    private List<EmojiBean> emojiBeans;
    private EmojiContainerFragment.OnEmojiClickListener onEmojiClickListener;
    private RecyclerView.RecycledViewPool recycledViewPool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_emoji_emoji, null);
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
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
