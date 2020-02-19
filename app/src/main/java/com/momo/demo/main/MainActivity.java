package com.momo.demo.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photon.im.PhotonIMMessage;
import com.cosmos.photon.im.messagebody.PhotonIMCustomBody;
import com.cosmos.photonim.imbase.base.BaseActivity;
import com.cosmos.photonim.imbase.base.BaseFragmentPagerAdapter;
import com.cosmos.photonim.imbase.session.SessionFragment;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.PermissionUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.event.AllUnReadCount;
import com.cosmos.photonim.imbase.utils.event.IMStatus;
import com.cosmos.photonim.imbase.view.TitleBar;
import com.momo.demo.MyApplication;
import com.momo.demo.R;
import com.momo.demo.main.contacts.ContactsFragment;
import com.momo.demo.main.me.MeFragmentView;
import com.momo.demo.main.sessiontest.SessionTestViewFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final int TAB_MSG = 1;
    private static final int TAB_CONTACT = 2;
    private static final int TAB_ME = 3;
    private static final String TAG = "MainActivityTAG";
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tvMessageUnread)
    TextView tvMessageUnread;

    private BaseFragmentPagerAdapter baseFragmentPagerAdapter;
    private final int Permission_RequestCode = 1001;
    private int lastAllUnRead = 0;
    private int currentPage;
    private String lastTitleMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintab);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PermissionUtils.checkPersmission(this)) {
            requestPermissions(PermissionUtils.permissions, 101);
        }
    }

    private void initView() {
        tabLayout.setSelectedTabIndicatorHeight(0);
        titleBar.setTitle("消息", new View.OnClickListener() {
            private final int count = 2;
            private int num;
            private long lastTime;

            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastTime < 500) {
                    num++;
                } else {
                    num = 0;
                }
                lastTime = System.currentTimeMillis();
                if (num >= count) {
                    num = 0;
                    String versionName = Utils.getAppVersionName(MyApplication.getApplication());
                    ToastUtils.showText(String.format("当前版本号：%s", versionName));
                }
            }
        });
//        ImageView ivIcon = null;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            switch (i) {
                case 0:
                    tab.setCustomView(R.layout.main_layout_message);
//                    ivIcon = tab.getCustomView().findViewById(R.id.ivIcon);
                    break;
                case 1:
                    tab.setCustomView(R.layout.main_layout_contacts);
//                    ivIcon = tab.getCustomView().findViewById(R.id.ivIcon);
                    break;
                case 2:
                    tab.setCustomView(R.layout.main_layout_me);
//                    ivIcon = tab.getCustomView().findViewById(R.id.ivIcon);
                    break;
                case 3:
                    tab.setCustomView(R.layout.main_layout_test);
//                    ivIcon = tab.getCustomView().findViewById(R.id.ivIcon);
                    break;
            }
//            ivIcon.setImageDrawable(Utils.tintDrawable(ivIcon.getDrawable(), getResources().getColorStateList(R.color.selector_tab)));
        }
        initAdapter();
        viewPager.setAdapter(baseFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
                switch (i) {
                    case 0:
                        if (lastTitleMsg != null) {
                            titleBar.setTitle(lastTitleMsg);
                        } else {
                            onGetAllUnReadCount(new AllUnReadCount(lastAllUnRead));
                        }
                        break;
                    case 1:
                        titleBar.setTitle(getResources().getString(R.string.main_contact_title));
                        break;
                    case 2:
                        titleBar.setTitle(getResources().getString(R.string.msg_me_title));
                        break;
                    case 3:
                        titleBar.setTitle("测试");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initAdapter() {
        ArrayList<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new SessionFragment());
        fragments.add(new ContactsFragment());
        fragments.add(new MeFragmentView());
        fragments.add(new SessionTestViewFragment());
        baseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImStatusChange(IMStatus status) {
        switch (status.status) {
            case PhotonIMClient.IM_STATE_CONNECTING:
                lastTitleMsg = "连接中...";
                if (currentPage == 0) {
                    titleBar.setTitle(lastTitleMsg);
                }
                break;
            case PhotonIMClient.IM_STATE_AUTH_SUCCESS:
                lastTitleMsg = null;
                onGetAllUnReadCount(new AllUnReadCount(lastAllUnRead));
                break;
            case PhotonIMClient.IM_STATE_AUTH_FAILED:
                break;
            case PhotonIMClient.IM_STATE_NET_UNAVAILABLE:
                lastTitleMsg = "消息(未连接)";
                if (currentPage == 0) {
                    titleBar.setTitle(lastTitleMsg);
                }
                break;
        }
        LogUtils.log(TAG, "state:" + status.status);
//        ToastUtils.showText(this, status.statusMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(PhotonIMMessage msg) {
        if (msg.chatType == PhotonIMMessage.CUSTOMMSG) {
            PhotonIMCustomBody body = (PhotonIMCustomBody) msg.body;
            ToastUtils.showText(String.format("收到自定义消息：customArg1:%d,customArg2:%d", body.arg1, body.arg2));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetAllUnReadCount(AllUnReadCount allUnReadCount) {
        if (allUnReadCount.allUnReadCount != 0) {
            String unReadCount;
            tvMessageUnread.setVisibility(View.VISIBLE);
            if (allUnReadCount.allUnReadCount > 99) {
                unReadCount = "消息(99+)";
                tvMessageUnread.setText("99+");
            } else {
                unReadCount = String.format("消息(%d)", allUnReadCount.allUnReadCount);
                tvMessageUnread.setText(allUnReadCount.allUnReadCount + "");
            }
            if (currentPage == 0) {
                titleBar.setTitle(unReadCount);
            }
        } else {
            if (currentPage == 0) {
                titleBar.setTitle("消息");
            }
            tvMessageUnread.setVisibility(View.GONE);
        }
        lastAllUnRead = allUnReadCount.allUnReadCount;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission_RequestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    ToastUtils.showText("没有给权限啊好难🤯");
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent intent = new Intent();// 创建Intent对象
            intent.setAction(Intent.ACTION_MAIN);// 设置Intent动作
            intent.addCategory(Intent.CATEGORY_HOME);// 设置Intent种类
            startActivity(intent);// 将Intent传递给Activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
