package com.mty.youtubeapiactivity.remote


import android.net.Uri

import com.mty.youtubeapiactivity.BuildConfig
import com.mty.youtubeapiactivity.base.BaseDataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.mty.youtubeapiactivity.BuildConfig.API_KEY

import com.mty.youtubeapiactivity.`object`.Constant
import org.koin.dsl.module

val remoteDataSource = module {
    factory { RemoteDataSource(get()) }
}

class RemoteDataSource(private val apiService: PlayListApi) : BaseDataSource() {
    suspend fun getPlaylists() = getResult {
        apiService.getPlaylists(
            Constant.part,
            Constant.channelId,
            BuildConfig.API_KEY,
            Constant.maxResult
        )
    }

    suspend fun getPlaylistItems(playlistId: String) = getResult {
        apiService.getPlaylistItems(
            Constant.part,
            playlistId,
            BuildConfig.API_KEY,
            Constant.maxResult
        )
    }
}