package com.cosmos.photonim.imbase.chat.album;

import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;
import com.cosmos.photonim.imbase.chat.album.ialbum.IAlbumModel;
import com.cosmos.photonim.imbase.chat.album.ialbum.IAlbumPresenter;
import com.cosmos.photonim.imbase.chat.album.ialbum.IAlbumView;
import com.cosmos.photonim.imbase.utils.CollectionUtils;

import java.util.ArrayList;

public class AlbumPresenter extends IAlbumPresenter {
    public static final int MAX_CHECKED = 9;
    private ArrayList<CategoryFile> data;
    private ArrayList<CategoryFile> checked;

    public AlbumPresenter(IAlbumView iView) {
        super(iView);
    }

    @Override
    public ArrayList<CategoryFile> initData() {
        data = new ArrayList<>();
        return data;
    }

    @Override
    public void getAlbum() {
        getiModel().getAlbum(new IAlbumModel.OnGetAlbumListener() {
            @Override
            public void onGetAlbum(ArrayList<CategoryFile> files) {
                if (!CollectionUtils.isEmpty(files)) {
                    data.addAll(files);
                    getIView().notifyDataSetChanged();
                } else {
                    getIView().toast("没有图片");
                }
            }
        });
    }

    @Override
    public ArrayList<CategoryFile> getCheckPics() {
        return checked;
    }

    @Override
    public ArrayList<CategoryFile> getData() {
        return data;
    }

    @Override
    public void changeCheckStatus(CategoryFile data) {
        initCheckedData();
        data.checked = !data.checked;
        if (data.checked) {
            if (checked.size() >= MAX_CHECKED) {
                data.checked = false;
                getIView().toast("超过9张了");
                getIView().notifyItemChanged(data.position);
                return;
            } else {
                checked.add(data);
            }
        } else {
            checked.remove(data);
        }
    }

    @Override
    public void updateData(ArrayList<CategoryFile> data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        for (CategoryFile datum : data) {
            if (this.data.get(datum.position).checked != datum.checked) {
                this.data.get(datum.position).checked = datum.checked;
                getIView().notifyItemChanged(datum.position);
                initCheckedData();
                if (datum.checked) {
                    checked.add(this.data.get(datum.position));
                } else {
                    checked.remove(this.data.get(datum.position));
                }
            }
        }
    }

    private void initCheckedData() {
        if (checked == null) {
            checked = new ArrayList<>(MAX_CHECKED);
        }
    }

    @Override
    public IAlbumModel generateIModel() {
        return new AlbumModel();
    }
}
