package com.mty.youtubeapiactivity.Prefs

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
val prefModule = module {
    single {Prefs(androidContext()) }
}

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("youtubeApi", Context.MODE_PRIVATE)
    var onBoard: Boolean
        get() = prefs.getBoolean("onBoard", false)
        set(value) =  prefs.edit().putBoolean("onBoard", value).apply()
}