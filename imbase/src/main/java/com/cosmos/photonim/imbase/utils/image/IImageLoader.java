package com.cosmos.photonim.imbase.utils.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

public interface IImageLoader {
    void loadImage(Context context, String url, int placeHolderResId, ImageView imageView);

    void loadImageUri(Context context, Uri uri, int placeHolderResId, ImageView imageView);

    void loadResImage(Context context, int resid, ImageView imageView);

    void downloadImage(Context context, String url, OnDownloadImageListener onDownloadImageListener);


    interface OnDownloadImageListener {
        void onDownload(String path);
    }
}
