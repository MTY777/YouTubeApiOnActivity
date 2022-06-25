package com.mty.youtubeapiactivity.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mty.youtubeapiactivity.BuildConfig.API_KEY
import com.mty.youtubeapiactivity.Prefs.Prefs
import com.mty.youtubeapiactivity.`object`.Constant
import com.mty.youtubeapiactivity.base.ViewModel
import com.mty.youtubeapiactivity.model.Playlist

import com.mty.youtubeapiactivity.repository.Repository
import com.mty.youtubeapiactivity.result.Resource

class PlayListViewModel(private val repository: Repository, private val prefs: Prefs): ViewModel() {
    fun getPlaylists(): LiveData<Resource<Playlist>> {
        return repository.getPlaylists()
    }

    fun setOnBoard(onBoard: Boolean) {
        prefs.onBoard = onBoard
    }
}