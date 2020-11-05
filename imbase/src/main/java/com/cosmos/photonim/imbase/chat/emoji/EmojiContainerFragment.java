package com.cosmos.photonim.imbase.chat.emoji;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseFragment;
import com.cosmos.photonim.imbase.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EmojiContainerFragment extends BaseFragment {
    private static final int EMOJI_PAGE_MAX = 40;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    private EmojiViewFragmentPagerAdapter adapter;
    private OnSendListener onSendListener;
    private OnDelListener onDelListener;
    private List<Fragment> fragments;
    //    private OnEmojiClickListener onEmojiClickListener;
    private RecyclerView.RecycledViewPool recycledViewPool;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_emoji;
    }

    @Override
    protected void initView(View view) {
        recycledViewPool = new RecyclerView.RecycledViewPool();

        List<EmojiJson.EmojiBean> allEmoji = EmojiUtils.getInstance().getEmojiBeans();
        fragments = new ArrayList<>();
        int size = allEmoji.size();
        int fragmentSize = size % EMOJI_PAGE_MAX == 0 ? size / EMOJI_PAGE_MAX : size / EMOJI_PAGE_MAX + 1;
        List<EmojiBean> emojiBeans;
        EmojiBean beanTemp;
        int j = 0;
        for (int i = 0; i < fragmentSize; i++) {
            emojiBeans = new ArrayList<>(EMOJI_PAGE_MAX);
            j = i * EMOJI_PAGE_MAX;
            for (; j < size; j++) {
                beanTemp = new EmojiBean();
                beanTemp.emojiContent = allEmoji.get(j).getCredentialName();
                beanTemp.emojiId = Utils.getDrawableByName(allEmoji.get(j).getResId());
                emojiBeans.add(beanTemp);
                if (emojiBeans.size() == EMOJI_PAGE_MAX) {
                    break;
                }
            }
            EmojiFragment tempFragment = new EmojiFragment();
            tempFragment.setEmojiBeans(emojiBeans);
//            tempFragment.setOnEmojiClickListener(onEmojiClickListener);
            tempFragment.setRecycledViewPool(recycledViewPool);
            fragments.add(tempFragment);
        }
        adapter = new EmojiViewFragmentPagerAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    @OnClick(R2.id.ivDel)
    public void onDelClick() {
        if (onDelListener != null) {
            onDelListener.onDelClick();
        }
    }

    @OnClick(R2.id.tvEmojiSend)
    public void onSendClick() {
        if (onSendListener != null) {
            onSendListener.onEmojiSend();
        }
    }


    public interface OnSendListener {
        void onEmojiSend();
    }

    public interface OnDelListener {
        void onDelClick();
    }

    public interface OnEmojiClickListener {
        void onEmojiClick(String content);
    }

    public void setOnSendListener(OnSendListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    public void setOnDelListener(OnDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }

    public void setOnEmojiClickListener(OnEmojiClickListener onEmojiClickListener) {
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                ((EmojiFragment) fragment).setOnEmojiClickListener(onEmojiClickListener);
            }
        }
//        this.onEmojiClickListener = onEmojiClickListener;
    }
}
