package com.cosmos.photonim.imbase.chat.album;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.R2;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.album.adapter.AlbumAdapter;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.cosmos.photonim.imbase.chat.album.ialbum.IAlbumView;
import com.cosmos.photonim.imbase.chat.album.preview.ImagePreviewActivity;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.ToastUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.OnClick;

public class AlbumFragment extends IAlbumView {
    public static final String ALBUM = "ALBUM";
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        presenter.getAlbum();
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (albumAdapter == null) {
            albumAdapter = new AlbumAdapter(presenter.initData());
            albumAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    if (view.getId() == R.id.cbCheck) {
                        presenter.changeCheckStatus((CategoryFile) data);
                    } else if (view.getId() == R.id.ivPic) {
                        ImagePreviewActivity.start(getActivity(), presenter.getData(), position);
                    }
                }
            });
        }
        return albumAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 4);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.white));
        return dividerItemDecoration;
    }

    @Override
    public void notifyDataSetChanged() {
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int position) {
        albumAdapter.notifyItemChanged(position);
    }

    @Override
    public IPresenter getIPresenter() {
        return new AlbumPresenter(this);
    }

    @OnClick(R2.id.tvPreview)
    public void onPreviewClick() {
        ArrayList<CategoryFile> checkPics = presenter.getCheckPics();
        if (CollectionUtils.isEmpty(checkPics)) {
            ToastUtils.showText("未选中图片");
            return;
        }
        ImagePreviewActivity.start(getActivity(), checkPics, 0);
    }

    @OnClick(R2.id.cbCheck)
    public void onCheckClick() {

    }

    @OnClick(R2.id.tvSend)
    public void onSendClick() {
        ArrayList<CategoryFile> checkedPics = presenter.getCheckPics();
        if (CollectionUtils.isEmpty(checkedPics)) {
            toast("未选中图片");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(ALBUM, checkedPics);
        Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, intent);
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<CategoryFile> resultData = data.getParcelableArrayListExtra(ImagePreviewActivity.RESULT_DATA);
            presenter.updateData(resultData);
            boolean send = data.getBooleanExtra(ImagePreviewActivity.RESULT_SEND, false);
            if (send) {
                onSendClick();
            }
        }
    }
}
