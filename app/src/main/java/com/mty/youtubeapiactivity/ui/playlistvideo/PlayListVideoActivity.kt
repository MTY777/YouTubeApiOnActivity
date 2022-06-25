package com.mty.youtubeapiactivity.ui.playlistvideo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.mty.youtubeapiactivity.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.mty.youtubeapiactivity.base.BaseActivity
import com.mty.youtubeapiactivity.databinding.ActivityPlayListVideoBinding
import com.mty.youtubeapiactivity.model.Item
import com.mty.youtubeapiactivity.result.Status
import com.mty.youtubeapiactivity.ui.playlist.PlayListActivity
import com.mty.youtubeapiactivity.ui.videoplayer.VideoPlayerActivity
import com.mty.youtubeapiactivity.utils.NetworkStatus
import com.mty.youtubeapiactivity.utils.NetworkStatusHelper

class PlayListVideoActivity : BaseActivity<ActivityPlayListVideoBinding, PlayListVideoViewModel>() {

    private var playlistId: String? = null

    override val viewModel: PlayListVideoViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityPlayListVideoBinding {
        return ActivityPlayListVideoBinding.inflate(inflater)
    }

    override fun initView() {
        playlistId = intent.getStringExtra(PlayListActivity.FIRST_KEY).toString()
        Toast.makeText(this, playlistId, Toast.LENGTH_SHORT).show()
        binding.playlistTitle.text = intent.getStringExtra(PlayListActivity.SECOND_KEY).toString()
        binding.playlistDescription.text = intent.getStringExtra(PlayListActivity.THIRD_KEY).toString()
    }

    override fun initViewModel() {
        viewModel.loading.observe(this) {
            binding.progressBar.isVisible = it
        }
        initVM()
    }

    override fun initListener() {
        binding.tvBack.setOnClickListener{
            onBackPressed()
        }
    }

    private fun initRecyclerView(playlistsList: ArrayList<Item>) {
        binding.videosRecyclerView.adapter = PlayListVideoAdapter(playlistsList, this::onItemClick)
        binding.videosRecyclerView.visibility = View.VISIBLE
    }

    private fun onItemClick(videoId: String, videoTitle: String, videoDesc: String) {
        Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra(idPdaVa, videoId)
            putExtra(titlePdaVa, videoTitle)
            putExtra(descPdaVa, videoDesc)
            startActivity(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initVM() {
        playlistId?.let {
            viewModel.getPlaylistItems(it).observe(this) { it ->
                when(it.status) {
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            viewModel.loading.postValue(false)
                            initRecyclerView(it.data.items as ArrayList<Item>)

                            binding.seriesTv.text = it.data.items.size.toString() + " video series"
                        } else {
                            Log.e("Error1", "error 1")
                        }
                    }
                    Status.ERROR -> {
                        viewModel.loading.postValue(false)
                        Log.e("Error2", "error 2")
                        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        viewModel.loading.postValue(true)
                    }
                }
            }
        }
    }

    private fun checkConnection() {
        NetworkStatusHelper(this).observe(this) {
            if (it == NetworkStatus.Available) {
                binding.detailsMain.visibility = View.VISIBLE
                binding.networkLayout.root.visibility = View.GONE
                initVM()
            } else {
                binding.detailsMain.visibility = View.GONE
                binding.networkLayout.root.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val idPdaVa = "idPdaVa"
        const val titlePdaVa = "titlePdaVa"
        const val descPdaVa = "descPdaVa"
    }

    override fun checkInternet() {
        checkConnection()


        binding.networkLayout.btnTryAgain.setOnClickListener {
            checkConnection()
        }
    }

}