package com.mty.youtubeapiactivity.ui.playlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mty.youtubeapiactivity.base.BaseActivity
import com.mty.youtubeapiactivity.databinding.ActivityMainBinding
import com.mty.youtubeapiactivity.model.Item
import com.mty.youtubeapiactivity.result.Status
import com.mty.youtubeapiactivity.utils.NetworkStatus
import com.mty.youtubeapiactivity.utils.NetworkStatusHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.mty.youtubeapiactivity.ui.playlistvideo.PlayListVideoActivity


class PlayListActivity  : BaseActivity<ActivityMainBinding, PlayListViewModel>() {

    override val viewModel: PlayListViewModel by viewModel()

    override fun initViewModel() {

        viewModel.loading.observe(this) {
            binding.progressBar.isVisible = it
        }
        viewModel.setOnBoard(true)
        initVM()
    }

    private fun initRecyclerView(playlistsList: List<Item>) {
        binding.recyclerView.adapter = PlaylistsAdapter(playlistsList as ArrayList<Item>, this::onItemClick)
    }

    private fun onItemClick(channelId: String, playlistTitle: String, playlistDescription: String) {
        Intent(this, PlayListVideoActivity::class.java).apply {
            putExtra(FIRST_KEY, channelId)
            putExtra(SECOND_KEY, playlistTitle)
            putExtra(THIRD_KEY, playlistDescription)
            startActivity(this)
        }
    }

    private fun initVM() {
        viewModel.getPlaylists().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        viewModel.loading.postValue(false)
                        initRecyclerView(it.data.items)
                    }
                }
                Status.ERROR -> {
                    viewModel.loading.postValue(false)
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    viewModel.loading.postValue(true)
                }
            }
        }
    }

    private fun checkConnection() {
        NetworkStatusHelper(this).observe(this) {
            if (it == NetworkStatus.Available) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.networkLayout.root.visibility = View.GONE
                initVM()
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.networkLayout.root.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val FIRST_KEY = "one_key"
        const val SECOND_KEY = "two_key"
        const val THIRD_KEY = "third_key"

    }

    override fun checkInternet() {
        checkConnection()

        binding.networkLayout.btnTryAgain.setOnClickListener {
            checkConnection()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initView() {

    }
}