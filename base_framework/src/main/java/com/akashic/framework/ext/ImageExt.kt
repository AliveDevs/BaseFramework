package com.akashic.framework.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

/**
 *  author: akashic
 *
 *  date: 2021-01-11 10:58 AM
 *
 *  desc: ImageView 扩展
 */


fun RequestBuilder<Drawable>.applyCorner(roundingRadius: Int): RequestBuilder<Drawable> {
    return this.apply(RequestOptions().transform(CenterCrop(), RoundedCorners(roundingRadius)))
}


fun ImageView.bind(
    src: String?,
    placeHolderRes: Int = 0,
    roundingRadius: Int = 0,
    overrideWidth:Int? = null,
    skipMemoryCache:Boolean = false
) {
    try {
        if (src.isNullOrEmpty() && placeHolderRes>0) {
            setImageResource(placeHolderRes)
            return
        }
        var builder = Glide.with(this).load(src).skipMemoryCache(skipMemoryCache).apply {

            if (roundingRadius > 0) {
                applyCorner(roundingRadius)
            }
        }

        if (placeHolderRes > 0) {
            builder=builder.placeholder(placeHolderRes)
        }

        if(overrideWidth!=null){
           builder= builder.override(overrideWidth,SIZE_ORIGINAL)
        }

        builder.transition(DrawableTransitionOptions().crossFade()).into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }

}


fun ImageView.bind(resId: Int?, placeHolderRes: Int = 0, roundingRadius: Int = 0) {
    try {
        if (resId == null) return
        var builder = Glide.with(this).load(resId).apply {
            if (roundingRadius > 0) {
                applyCorner(roundingRadius)
            }
        }.transition(DrawableTransitionOptions().crossFade())

        if (placeHolderRes > 0) {
            builder=builder.placeholder(placeHolderRes)
        }

        builder.into(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

