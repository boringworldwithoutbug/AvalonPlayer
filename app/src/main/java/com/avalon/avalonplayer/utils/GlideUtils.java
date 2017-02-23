package com.avalon.avalonplayer.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class GlideUtils {

    //set bitmap from url
    public static void setNormalPic(Context context, String url, ImageView img, int waiting, int error) {
        Glide.with(context).load(url).placeholder(waiting).error(error).into(img);
    }

    //set circlr from url
    public static void setCirclePic(Context context, String url, ImageView img, int waiting, int error) {
        Glide.with(context).load(url).placeholder(waiting).error(error).transform(new GlideCircleTransform(context)).into(img);
    }

    //set bitmap into other view
    public static void setBackGroud(Context context, String url, final View img, int waiting, int error) {
        Glide.with(context).load(url).placeholder(waiting).error(error).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                img.setBackgroundDrawable(resource.getCurrent());
            }
        });
    }
}
