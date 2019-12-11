package com.cosmos.photonim.imbase.utils.image;

import android.content.Context;
import android.widget.ImageView;

public interface IImageLoader {
    void loadImage(Context context, String url, int placeHolderResId, ImageView imageView);

    void loadResImage(Context context, int resid, ImageView imageView);
}
