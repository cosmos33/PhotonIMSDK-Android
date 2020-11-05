package com.cosmos.photonim.imbase.chat.preview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.chat.ChatData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ImageCheckActivity extends BaseActivity {
    private static final String EXTRA_CHATDATA = "EXTRA_CHATDATA";
    private static final String EXTRA_CHATDATA_CURRENT = "EXTRA_CHATDATA_CURRENT";
    @BindView(R2.id.viewPager)
    ViewPager viewPager;

    private ImageCheckFragmentAdapter adapter;
    private List<Fragment> fragments;
    private List<ChatData> chatDataList;
    private int currentPosition;

    public static void startActivity(Activity context, ArrayList<ChatData> chatDatas, int currentPosition) {
        Intent intent = new Intent(context, ImageCheckActivity.class);
        intent.putExtra(EXTRA_CHATDATA, chatDatas);
        intent.putExtra(EXTRA_CHATDATA_CURRENT, currentPosition);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagecheck);

        initView();
    }

    private void initView() {
        chatDataList = (List<ChatData>) getIntent().getExtras().get(EXTRA_CHATDATA);
//        current = (ChatData) getIntent().getExtras().get(EXTRA_CHATDATA_CURRENT);
        currentPosition = (int) getIntent().getIntExtra(EXTRA_CHATDATA_CURRENT, 0);
        fragments = new ArrayList<>();
        for (int i = 0; i < chatDataList.size(); i++) {
            ImageFragment imageFragment = ImageFragment.getInstance(chatDataList.get(i));
            fragments.add(imageFragment);
        }
        adapter = new ImageCheckFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
    }
//
//    @OnClick(R2.id.ivClose)
//    public void onCloseClick() {
//        finish();
//    }
}
