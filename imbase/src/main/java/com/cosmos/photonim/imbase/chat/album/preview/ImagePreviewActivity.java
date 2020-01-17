package com.cosmos.photonim.imbase.chat.album.preview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.chat.album.AlbumPresenter;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.cosmos.photonim.imbase.chat.image.ImageCheckFragmentAdapter;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewActivity extends BaseActivity {
    public static final String RESULT_SEND = "RESULT_SEND";
    public static final String RESULT_DATA = "RESULT_DATA";
    private static final String EXTRA_FILES = "EXTRA_FILES";
    private static final String EXTRA_FILE_CURRENT = "EXTRA_FILE_CURRENT";

    @BindView(R2.id.viewPager)
    ViewPager viewPager;

    private ImageCheckFragmentAdapter adapter;
    private List<Fragment> fragments;
    private ImagePreviewFragment currentFragment;
    private ArrayList<CategoryFile> categoryFiles;
    private int currentPosition;
    private boolean send;

    public static void start(Activity context, ArrayList<CategoryFile> files, int currentPosition) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(EXTRA_FILES, files);
        intent.putExtra(EXTRA_FILE_CURRENT, currentPosition);
        context.startActivityForResult(intent, Constants.REQUEST_PREVIEW_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        initView();
    }

    private void initView() {
        categoryFiles = (ArrayList<CategoryFile>) getIntent().getExtras().get(EXTRA_FILES);
        currentPosition = (int) getIntent().getIntExtra(EXTRA_FILE_CURRENT, 0);
        fragments = new ArrayList<>();
        for (int i = 0; i < categoryFiles.size(); i++) {
            ImagePreviewFragment imageFragment = ImagePreviewFragment.getInstance(categoryFiles.get(i));
            imageFragment.setOnCheckStatusListener(onCheckStatusListener);
            fragments.add(imageFragment);
        }
        adapter = new ImageCheckFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentFragment = (ImagePreviewFragment) fragments.get(i);
                currentFragment.setCheckedCount(getCheckedSize());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setCurrentItem(currentPosition);
        currentFragment = (ImagePreviewFragment) fragments.get(currentPosition);
        currentFragment.initCheckedSize(getCheckedSize());
    }

    private OnCheckStatusListener onCheckStatusListener = new OnCheckStatusListener() {
        @Override
        public boolean onCheckStatusChange(CategoryFile categoryFile) {
            if (categoryFile.checked) {
                if (getCheckedSize() > AlbumPresenter.MAX_CHECKED) {
                    ToastUtils.showText("超过9张了");
                    categoryFile.checked = false;
                    currentFragment.setCheckedCount(getCheckedSize());
                    return false;
                }
            }
            currentFragment.setCheckedCount(getCheckedSize());
            return true;
        }

        @Override
        public void finish(boolean send) {
            ImagePreviewActivity.this.send = send;
            ImagePreviewActivity.this.finish();
        }
    };

    private int getCheckedSize() {
        int i = 0;
        for (CategoryFile categoryFile : categoryFiles) {
            if (categoryFile.checked) {
                i++;
            }
        }
        return i;
    }

    @OnClick(R2.id.ivClose)
    public void onCloseClick() {
        finish();
    }

    interface OnCheckStatusListener {
        boolean onCheckStatusChange(CategoryFile categoryFile);

        void finish(boolean send);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(RESULT_SEND, send);
        data.putExtra(RESULT_DATA, categoryFiles);
        setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}
