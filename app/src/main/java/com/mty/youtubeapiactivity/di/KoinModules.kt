package com.mty.youtubeapiactivity.di

import com.mty.youtubeapiactivity.Prefs.prefModule
import com.mty.youtubeapiactivity.remote.networkModule
import com.mty.youtubeapiactivity.remote.remoteDataSource

val koinModules = listOf(
    repoModules,
    viewModules,
    networkModule,
    remoteDataSource,
    prefModule
)