package com.cosmos.photonim.imbase.utils.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

public class ImageLoaderUtils implements IImageLoader {
    private IImageLoader imageLoader;

    private ImageLoaderUtils() {
        init();
    }

    private void init() {
        imageLoader = new GlideIImageLoader();
    }

    private static class ImageLoaderUtilsHolder {
        public static ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils();
    }


    public static ImageLoaderUtils getInstance() {
        return ImageLoaderUtilsHolder.imageLoaderUtils;
    }

    @Override
    public void loadImage(Context context, String url, int placeHolderResId, ImageView imageView) {
        imageLoader.loadImage(context, url, placeHolderResId, imageView);
    }

    @Override
    public void loadImageUri(Context context, Uri uri, int placeHolderResId, ImageView imageView) {
        imageLoader.loadImageUri(context, uri, placeHolderResId, imageView);
    }

    @Override
    public void loadResImage(Context context, int resId, ImageView imageView) {
        imageLoader.loadResImage(context, resId, imageView);
    }

    @Override
    public void downloadImage(Context context, String url, OnDownloadImageListener onDownloadImageListener) {
        imageLoader.downloadImage(context, url, onDownloadImageListener);
    }
}
