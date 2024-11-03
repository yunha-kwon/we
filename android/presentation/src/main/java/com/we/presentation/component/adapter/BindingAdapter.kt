package com.we.presentation.component.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.we.presentation.util.addComma

@SuppressLint("SetTextI18n")
@BindingAdapter("setPrice")
fun setPrice(textView: TextView, price: Long) {
    textView.text = "${price.addComma()} 원"
}


@BindingAdapter("setImage")
fun setImage(imageView: ImageView, image: String?) {
    Glide.with(imageView.context)
        .load(image)
        .into(imageView)
}