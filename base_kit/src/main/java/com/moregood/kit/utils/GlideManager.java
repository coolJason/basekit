package com.moregood.kit.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.moregood.kit.R;


/**
 * Created by Rico on 2018/12/28.
 */
public class GlideManager {
    static boolean isAnimationed;

    static TransitionOptions transitionOptions = GenericTransitionOptions.with(R.animator.glide_alpha);

    public static void displayImage(ImageView imageView, Object url, RequestOptions requestOptions) {
        if (imageView == null) return;
        if (!((Activity) imageView.getContext()).isDestroyed()) {
            requestOptions = requestOptions.override(imageView.getWidth(), imageView.getHeight());
            if (!isAnimationed)
                Glide.with(imageView.getContext()).load(url).apply(requestOptions).into(imageView);
            else
                Glide.with(imageView.getContext()).load(url).apply(requestOptions)
                        .transition(transitionOptions).into(imageView);
        }
    }

    public static void displayImage(ImageView imageView, Object url) {
        if (imageView == null) return;
        if (!((Activity) imageView.getContext()).isDestroyed()) {
            RequestOptions mOptions = new RequestOptions().override(imageView.getWidth(), imageView.getHeight());
            if (!isAnimationed)
                Glide.with(imageView.getContext()).load(url).apply(mOptions).into(imageView);
            else
                Glide.with(imageView.getContext()).load(url).apply(mOptions)
                        .transition(transitionOptions).into(imageView);
        }
    }
    //图片下载并缓存在磁盘
    public static void displayImageCache(ImageView imageView, Object url,RequestOptions requestOptions) {
        if (imageView == null) return;
        if (imageView.getContext() instanceof Activity && ((Activity) imageView.getContext()).isDestroyed()) {
            return;
        }
        if (requestOptions != null) {
            Glide.with(imageView.getContext()).load(url).apply(requestOptions).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        } else {
            Glide.with(imageView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }

    public static void openAnimation(boolean isOpened) {
        isAnimationed = isOpened;
    }

}
