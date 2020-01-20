package com.cosmos.photonim.imbase.chat.file;

import android.text.TextUtils;

import com.cosmos.photonim.imbase.chat.SizeUtils;
import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;
import com.cosmos.photonim.imbase.chat.file.ifile.IFileModel;
import com.cosmos.photonim.imbase.chat.file.ifile.IFilePresenter;
import com.cosmos.photonim.imbase.chat.file.ifile.IFileView;

import java.io.File;
import java.util.ArrayList;

public class FilePresenter extends IFilePresenter {
    private static final int CHECKED_COUNT = 9;
    private ArrayList<FileItemData> data;
    private ArrayList<FileItemData> checkedData;
    private File currentPath;
    private FileFragment.OnFileCheckedListener onFileCheckedListener;

    public FilePresenter(IFileView iView) {
        super(iView);

    }

    @Override
    public ArrayList<FileItemData> init() {
        data = new ArrayList<>();
        checkedData = new ArrayList<>(CHECKED_COUNT);
        return data;
    }

    @Override
    public void getFileData(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        getIView().progress(true);
        getiModel().getFileData(path, new IFileModel.OnGetFileListener() {
            @Override
            public void onProcessing(FileItemData fileItemData) {
                fileItemData.setChecked(checkedData.indexOf(fileItemData) != -1);
            }

            @Override
            public void onGetFileListener(ArrayList<FileItemData> fileItemData) {
                data.clear();
                if (fileItemData != null) {
                    data.addAll(fileItemData);
                }
                currentPath = new File(path);
                getIView().progress(false);
                getIView().notifyData();
            }
        });
    }

    @Override
    public ArrayList<FileItemData> getSelectedFiles() {
        return checkedData;
    }

    @Override
    public boolean checkData(FileItemData file) {
        if (!file.isChecked() && checkedData.size() >= CHECKED_COUNT) {
            getIView().toast(String.format("超过%d了", CHECKED_COUNT));
            return false;
        }
        file.setChecked(!file.isChecked());
        if (file.isChecked()) {
            checkedData.add(file);
        } else {
            checkedData.remove(file);
        }
        if (onFileCheckedListener != null) {
            onFileCheckedListener.onFileCheckStatusChanged(getAllCheckedSize());
        }
        return true;
    }

    private String getAllCheckedSize() {
        long size = 0;
        for (FileItemData checkedDatum : checkedData) {
            size += checkedDatum.getSizeL();
        }
        return SizeUtils.getSize(size);
    }

    @Override
    public String getCurrentPath() {
        return currentPath.getAbsolutePath();
    }

    @Override
    public boolean popFileData() {
        if (currentPath.getParentFile() == null) {
            return false;
        }
        getFileData(currentPath.getParentFile().getAbsolutePath());
        return true;
    }

    @Override
    public void setOnFileCheckedListener(FileFragment.OnFileCheckedListener onFileCheckedListener) {
        this.onFileCheckedListener = onFileCheckedListener;
    }

    @Override
    public IFileModel generateIModel() {
        return new FileModel();
    }
}
