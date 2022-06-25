package com.mty.youtubeapiactivity.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mty.youtubeapiactivity.model.Item
import com.mty.youtubeapiactivity.model.Playlist
import com.mty.youtubeapiactivity.remote.RemoteDataSource
import com.mty.youtubeapiactivity.result.Resource
import com.mty.youtubeapiactivity.result.Resource.Companion.loading
import kotlinx.coroutines.Dispatchers

class Repository(private val dataSourse: RemoteDataSource) {

    fun getPlaylists(): LiveData<Resource<Playlist>> = liveData(Dispatchers.IO) {
        emit(loading())
        val response = dataSourse.getPlaylists()
        emit(response)
    }

    fun getPlaylistItems(playlistId: String): LiveData<Resource<Item>> = liveData(
        Dispatchers.IO) {
        emit(loading())
        val response = dataSourse.getPlaylistItems(playlistId)
        emit(response)
    }
}