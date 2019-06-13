package com.example.dell.cloudreaderapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.cloudreaderapp.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by jingbin on 2016/11/30.
 * 首页轮播图
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.shape_bg_loading)
                .error(R.drawable.shape_bg_loading);
        Glide.with(context).load(url)
                .apply(options)
                .into(imageView);
    }
}
