package com.cosmos.photonim.imbase.utils.image;

import android.content.Context;
import android.widget.ImageView;

public class ImageLoaderUtils {
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

    public void loadImage(Context context, String url, int placeHolderResId, ImageView imageView) {
        imageLoader.loadImage(context, url, placeHolderResId, imageView);
    }

    public void loadResImage(Context context, int resId, ImageView imageView) {
        imageLoader.loadResImage(context, resId, imageView);
    }
}
