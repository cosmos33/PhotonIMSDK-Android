package com.cosmos.photonim.imbase.chat.album.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.cosmos.photonim.imbase.utils.Constants;
import com.cosmos.photonim.imbase.utils.recycleadapter.ItemData;

public class CategoryFile implements ItemData, Parcelable {
    public String mPath;
    public String mParent;
    public String mName;
    public long mSize;
    public long mLastModifyTime;
    public boolean checked;
    public int position;

    @Override
    public int getItemType() {
        return Constants.ITEM_TYPE_ALBUM;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPath);
        dest.writeString(this.mParent);
        dest.writeString(this.mName);
        dest.writeLong(this.mSize);
        dest.writeLong(this.mLastModifyTime);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.position);
    }

    public CategoryFile() {
    }

    protected CategoryFile(Parcel in) {
        this.mPath = in.readString();
        this.mParent = in.readString();
        this.mName = in.readString();
        this.mSize = in.readLong();
        this.mLastModifyTime = in.readLong();
        this.checked = in.readByte() != 0;
        this.position = in.readInt();
    }

    public static final Creator<CategoryFile> CREATOR = new Creator<CategoryFile>() {
        @Override
        public CategoryFile createFromParcel(Parcel source) {
            return new CategoryFile(source);
        }

        @Override
        public CategoryFile[] newArray(int size) {
            return new CategoryFile[size];
        }
    };

}
