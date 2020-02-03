package com.cosmos.photonim.imbase.utils.image;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.cosmos.photonim.imbase.ImBaseBridge;

public class GlideIImageLoader implements IImageLoader {
    private InternalCacheDiskCacheFactory diskLruCacheFactory = new InternalCacheDiskCacheFactory(
            ImBaseBridge.getInstance().getApplication());

    public GlideIImageLoader() {
        Glide.init(ImBaseBridge.getInstance().getApplication(), new GlideBuilder().setDiskCache(diskLruCacheFactory));
    }

    @Override
    public void loadImage(Context context, String url, int placeHolderResId, ImageView imageView) {
//        Glide.with(context).load(url).placeholder(placeHolderResId).into(imageView);
        Glide.with(context).load(url == null ? "" : Uri.parse(url)).placeholder(placeHolderResId).into(imageView);
    }

    @Override
    public void loadResImage(Context context, int resid, ImageView imageView) {
        Glide.with(context).load(resid).into(imageView);
    }

}
