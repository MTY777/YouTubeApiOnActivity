package com.mty.youtubeapiactivity.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
open class ViewModel : ViewModel() {
    val loading = MutableLiveData<Boolean>()
}