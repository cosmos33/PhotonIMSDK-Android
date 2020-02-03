package com.cosmos.photonim.imbase.chat.album;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.chat.album.adapter.CategoryFile;

import java.util.ArrayList;

public class AlbumPicUtils {

    private static final char UNIX_SEPARATOR = '/';

    public static String getPathFromFilepath(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            int pos = filepath.lastIndexOf(UNIX_SEPARATOR);
            if (pos != -1) {
                return filepath.substring(0, pos);
            }
        }
        return "";
    }

    public static String getNameFromFilepath(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            int pos = filepath.lastIndexOf(UNIX_SEPARATOR);
            if (pos != -1) {
                return filepath.substring(pos + 1);
            }
        }
        return "";
    }

    public static ArrayList<CategoryFile> queryCategoryFilesSync() {
        ArrayList<CategoryFile> files = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        if (uri != null) {
            String[] projection = new String[]{MediaStore.Files.FileColumns._ID, // id
                    MediaStore.Files.FileColumns.DATA, // 文件路径
                    MediaStore.Files.FileColumns.SIZE, // 文件大小
                    MediaStore.Files.FileColumns.DATE_MODIFIED}; // 修改日期
            Cursor cursor = ImBaseBridge.getInstance().getApplication().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        final int pathIdx = cursor
                                .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                        final int sizeIdx = cursor
                                .getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                        final int modifyIdx = cursor
                                .getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                        int index = 0;
                        do {
                            String path = cursor.getString(pathIdx);
                            CategoryFile file = new CategoryFile();
                            file.mPath = path;
                            file.mParent = getPathFromFilepath(file.mPath);
                            file.mName = getNameFromFilepath(file.mPath);
                            file.mSize = cursor.getLong(sizeIdx);
                            file.mLastModifyTime = cursor.getLong(modifyIdx);
                            file.position = index;
                            files.add(file);
                            index++;
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }
            }
        }
        return files;
    }
}
