package com.mty.youtubeapiactivity.di

import com.mty.youtubeapiactivity.ui.playlist.PlayListViewModel
import com.mty.youtubeapiactivity.ui.playlistvideo.PlayListVideoViewModel
import com.mty.youtubeapiactivity.ui.videoplayer.VideoPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModules: Module = module {
    viewModel { PlayListViewModel(get(), get()) }
    viewModel { PlayListVideoViewModel(get()) }
    viewModel { VideoPlayerViewModel(get()) }
}