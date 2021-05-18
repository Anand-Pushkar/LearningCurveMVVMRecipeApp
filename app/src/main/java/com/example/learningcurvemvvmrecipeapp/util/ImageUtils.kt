package com.example.learningcurvemvvmrecipeapp.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AmbientContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.learningcurvemvvmrecipeapp.R

const val DEFAULT_RECIPE_IMAGE = R.drawable.empty_plate

@Composable
fun loadPicture(
    url: String,
    @DrawableRes defaultImage: Int
): MutableState<Bitmap?>{

    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

    // show default image while the image loads
    Glide.with(AmbientContext.current)
        .asBitmap()
        .load(defaultImage)
        .into(object: CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    // test it
    // loadPicture(drawable = defaultImage)

    // get network image
    Glide.with(AmbientContext.current)
        .asBitmap()
        .load(url)
        .into(object: CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    return bitmapState
}

@Composable
fun loadPicture(
    @DrawableRes drawable: Int
): MutableState<Bitmap?>{

    val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

    Glide.with(AmbientContext.current)
        .asBitmap()
        .load(drawable)
        .into(object: CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmapState.value = resource
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    return bitmapState
}