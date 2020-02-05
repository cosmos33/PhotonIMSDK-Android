package com.cosmos.photonim.imbase.chat.file.adapter;

import android.support.annotation.Nullable;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.chat.SizeUtils;
import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.TimeUtils;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class FileItemData implements ItemData, Serializable {
    private File file;
    private int childSize;
    private String time;
    private String size;
    private long sizeL;
    private String fileName;
    private String filePath;
    private boolean directory;
    private boolean checked;
    private int position;
    private int resId;

    private FileItemData(Builder builder) {
        file = builder.file;
        childSize = builder.childSize;
        time = builder.time;
        size = builder.size;
        fileName = builder.fileName;
        filePath = builder.filePath;
        directory = builder.directory;
        resId = builder.resId;
        sizeL = builder.sizeL;
    }

    public int getChildSize() {
        return childSize;
    }

    public String getTime() {
        return time;
    }

    public String getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isDirectory() {
        return directory;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_FILE;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getResId() {
        return resId;
    }

    public long getSizeL() {
        return sizeL;
    }

    public static final class Builder {
        private File file;
        private int childSize;
        private String time;
        private String size;
        private long sizeL;
        private String fileName;
        private String filePath;
        private boolean directory;
        private int resId;

        public Builder() {
        }

        public Builder file(File val) {
            file = val;
            return this;
        }

        public FileItemData build() {
            if (file != null) {
                this.fileName = file.getName();
                this.time = TimeUtils.getDateFormat().format(new Date(file.lastModified()));
                this.directory = file.isDirectory();
                if (directory) {
                    childSize = file.listFiles().length;
                    resId = R.drawable.file_directory;
                } else {
                    this.size = SizeUtils.getSize(sizeL = file.length());
                    resId = R.drawable.file_file;
                }
                this.filePath = file.getAbsolutePath();
            }
            return new FileItemData(this);
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof FileItemData)) {
            return false;
        }
        if (((FileItemData) obj).file == null) {
            return false;
        }
        return ((FileItemData) obj).file.getAbsolutePath().equals(file.getAbsolutePath());
    }
}
