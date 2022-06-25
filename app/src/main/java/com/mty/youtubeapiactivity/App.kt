package com.mty.youtubeapiactivity

import android.app.Application
import com.mty.youtubeapiactivity.di.koinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(koinModules)
        }
    }
}