package com.momo.demo.main.contacts.single;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cosmos.photon.im.PhotonIMClient;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.momo.demo.R;
import com.momo.demo.UserInfoHelper;
import com.momo.demo.login.LoginPresenter;
import com.momo.demo.login.ilogin.ILoginPresenter;
import com.momo.demo.login.ilogin.ILoginView;
import com.momo.demo.main.contacts.single.ionline.IOnlineUserView;
import com.momo.demo.main.contacts.single.userinfo.UserInfoActivity;
import com.momo.demo.main.me.SwitchAccoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OnlineUserFragment extends IOnlineUserView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llNoMsg)
    LinearLayout llNoMsg;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private OnlineUserAdapter adapter;
    private List<OnlineUserData> baseDataList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_onlineusers;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        refreshLayout.setOnRefreshListener(() -> presenter.loadContacts());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && presenter != null) {
            presenter.loadContacts();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 2019-08-05 修改加载时机
        presenter.loadContacts();
    }

    @Override
    public void loadContacts(List<OnlineUserData> onlineUserData) {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        baseDataList.clear();
        if(getActivity() instanceof SwitchAccoutActivity){
            onlineUserData.clear();
            for (String name:UserInfoHelper.INSTANCE.getUserNameList()){
                onlineUserData.add(new OnlineUserData.Builder()
                        .msgId(name)
                        .nickName(name)
                        .build());
            }
        }
        baseDataList.addAll(onlineUserData);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showContactsEmptyView() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        llNoMsg.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @OnClick(R.id.llNoMsg)
    public void onOnNoMsgClick() {
        presenter.loadContacts();
    }

    @Override
    public IPresenter getIPresenter() {
        return new OnlineUserPresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (adapter == null) {
            baseDataList = new ArrayList<>();
            adapter = new OnlineUserAdapter(baseDataList);
            adapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    OnlineUserData onlineUserData = (OnlineUserData) data;
                    if(getActivity() instanceof SwitchAccoutActivity){
                        getLoginPreSenter(onlineUserData.getUserId(), UserInfoHelper.INSTANCE.getPassWd(onlineUserData.getUserId()));
                    }else{
                        UserInfoActivity.startActivity(getActivity(), onlineUserData.getUserId(), onlineUserData.getIcon(), onlineUserData.getNickName());
                    }
                }
            });
        }
        return adapter;
    }


    private void getLoginPreSenter(String userName,String pwd){
        TaskExecutor.getInstance().createAsycTask(()->{
            PhotonIMClient.getInstance().logout();
            LoginPresenter loginPresenter =  new LoginPresenter(new ILoginView() {
                @Override
                public void showDialog() {
                }

                @Override
                public void hideDialog() {

                }

                @Override
                public ILoginPresenter getIPresenter() {
                    return new LoginPresenter(this);
                }
            });
            loginPresenter.onLoginClick(userName,pwd);
            getActivity().finish();
            return null;

        });




    }
}
