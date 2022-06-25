package com.mty.youtubeapiactivity.di

import com.mty.youtubeapiactivity.repository.Repository
import org.koin.core.module.Module
import org.koin.dsl.module

val repoModules: Module = module {
    single { Repository(get()) }
}