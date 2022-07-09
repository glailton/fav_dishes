package io.github.glailton.favdish.ui.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}


fun ImageView.loadImage(url: String ){
    Glide.with(this)
        .load(url)
        .into(this)
}

fun ImageView.loadImage(bitmap: Bitmap){
    Glide.with(this)
        .load(bitmap)
        .centerCrop()
        .into(this)
}

fun Dialog.showDialog() {
    if (!this.isShowing) this.show()
}

fun Dialog.hideDialog() {
    if (this.isShowing) this.dismiss()
}