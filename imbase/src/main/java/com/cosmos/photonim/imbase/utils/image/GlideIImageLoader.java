package com.cosmos.photonim.imbase.utils.image;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.FileUtils;

import java.io.File;

public class GlideIImageLoader implements IImageLoader {
    private InternalCacheDiskCacheFactory diskLruCacheFactory = new InternalCacheDiskCacheFactory(
            ImBaseBridge.getInstance().getApplication());

    public GlideIImageLoader() {
        Glide.init(ImBaseBridge.getInstance().getApplication(), new GlideBuilder().setDiskCache(diskLruCacheFactory));
    }

    @Override
    public void loadImageUri(Context context, Uri uri, int placeHolderResId, ImageView imageView) {
        Glide.with(context).load(uri).placeholder(placeHolderResId).into(imageView);
    }

    @Override
    public void loadImage(Context context, String url, int placeHolderResId, ImageView imageView) {
        Glide.with(context).load(url).placeholder(placeHolderResId).into(imageView);
    }

    @Override
    public void loadResImage(Context context, int resid, ImageView imageView) {
        Glide.with(context).load(resid).into(imageView);
    }

    @Override
    public void downloadImage(Context context, String url, OnDownloadImageListener onDownloadImageListener) {
        RequestManager mRequestManager = Glide.with(context);

        RequestBuilder<File> mRequestBuilder = mRequestManager.downloadOnly().load(url).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                File file;
                FileUtils.copy(resource, file = new File(FileUtils.getImageOriginPath(), resource.getName()));
                if (onDownloadImageListener != null) {
                    onDownloadImageListener.onDownload(file.getAbsolutePath());
                }
                return false;
            }
        });

        mRequestBuilder.preload();
    }


}
