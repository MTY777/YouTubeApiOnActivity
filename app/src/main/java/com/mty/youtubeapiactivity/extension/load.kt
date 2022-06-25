package com.mty.youtubeapiactivity.extension

import android.net.Uri
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide


fun CardView.load(url: String) {
    Glide.with(this).load(url).into(this)
}

private fun Any.into(cardView: CardView) {

}
fun ImageView.Glide(url:String){
    Glide.with(this).load(url).into(this)
}

