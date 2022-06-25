package com.mty.youtubeapiactivity.ui.playlistvideo

import androidx.lifecycle.LiveData
import com.mty.youtubeapiactivity.base.ViewModel
import com.mty.youtubeapiactivity.model.Item
import com.mty.youtubeapiactivity.model.Playlist
import com.mty.youtubeapiactivity.repository.Repository
import com.mty.youtubeapiactivity.result.Resource

class PlayListVideoViewModel (private val repository: Repository): ViewModel() {

    fun getPlaylistItems(playlistId: String): LiveData<Resource<Item>> {
        return repository.getPlaylistItems(playlistId)
    }

}