package com.mty.youtubeapiactivity.ui.videoplayer

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.mty.youtubeapiactivity.BuildConfig
import com.mty.youtubeapiactivity.Prefs.Prefs
import com.mty.youtubeapiactivity.R
import com.mty.youtubeapiactivity.base.BaseActivity
import com.mty.youtubeapiactivity.databinding.ActivityVideoPlayerBinding
import com.mty.youtubeapiactivity.ui.playlistvideo.PlayListVideoActivity
import com.mty.youtubeapiactivity.utils.NetworkStatus
import com.mty.youtubeapiactivity.utils.NetworkStatusHelper
import org.koin.android.ext.android.inject

class VideoPlayerActivity :
    BaseActivity<ActivityVideoPlayerBinding, VideoPlayerViewModel>(), Player.Listener {

    private var videoId: String? = null
    private lateinit var player: ExoPlayer
    private lateinit var videoSource: ProgressiveMediaSource
    private lateinit var audioSource: ProgressiveMediaSource
    private val prefs: Prefs by inject()

    override val viewModel: VideoPlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("SeekTime", player.currentPosition)
        outState.putInt("mediaItem", player.currentMediaItemIndex)
    }

    fun initListener(savedInstanceState: Bundle?) {
        super.initListener()
        Log.d("prefs", prefs.onBoard.toString())
        binding.tvBack.setOnClickListener {
            onBackPressed()
        }
        downloadVideo(savedInstanceState)
//        binding.downloadButton.setOnClickListener {
//            showDialog("Select video quality")
//        }

        binding.downloadButton.setOnClickListener {
            getYoutubeDownloadUrl(BuildConfig.YOUTUBE_BASE + videoId)
        }
    }

    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val downloadBtn = dialog.findViewById(R.id.materialButton) as Button
        downloadBtn.setOnClickListener {
//            Intent(this, SampleDownloadActivity::class.java).apply {
//                putExtra(Intent.EXTRA_TEXT, BuildConfig.YOUTUBE_BASE + videoId)
//                startActivity(this)
//            }
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("StaticFieldLeak")
    private fun downloadVideo(savedInstanceState: Bundle?) {
        Log.e("Video", "${BuildConfig.YOUTUBE_BASE + videoId}")

        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    val videoTag = 133
                    val audioTag = 140
                    val videoUrl = ytFiles[videoTag].url
                    val audioUrl = ytFiles[audioTag].url
                    setupPlayer(videoUrl, audioUrl)
                    if (savedInstanceState != null) {
                        savedInstanceState.getInt("mediaItem").let { restoredMedia ->
                            val seekTime = savedInstanceState.getLong("SeekTime")
                            player.seekTo(restoredMedia, seekTime)
                            player.play()
                        }
                    }
                }
            }
        }.extract(BuildConfig.YOUTUBE_BASE + videoId)
    }

    override fun initView() {
        getDataIntent()
        binding.networkLayout.root.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.downloadLayout.visibility = View.INVISIBLE
    }

    private fun getDataIntent() {
        videoId = intent.getStringExtra(PlayListVideoActivity.idPdaVa).toString()
        binding.videoTitle.text = intent.getStringExtra(PlayListVideoActivity.titlePdaVa).toString()
        binding.videoDesc.text = intent.getStringExtra(PlayListVideoActivity.descPdaVa).toString()
    }

    private fun setupPlayer(videoUrl: String, audioUrl: String) {
        buildMediaSource(videoUrl, audioUrl)
        player = ExoPlayer.Builder(this).build()
        binding.videoView.player = player
        player.setMediaSource(MergingMediaSource(videoSource, audioSource))
        player.addListener(this)
        player.prepare()
    }

    private fun buildMediaSource(videoUrl: String, audioUrl: String) {
        videoSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(videoUrl))

        audioSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(audioUrl))
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_BUFFERING -> binding.videoProgressBar.visibility = View.VISIBLE
            Player.STATE_READY -> binding.videoProgressBar.visibility = View.INVISIBLE
            Player.STATE_ENDED -> {}
            Player.STATE_IDLE -> {}
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityVideoPlayerBinding {
        return ActivityVideoPlayerBinding.inflate(inflater)
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    override fun initViewModel() {
        viewModel.loading.observe(this) {
            binding.progressBar.isVisible = it
        }
    }

    private fun checkConnection() {
        NetworkStatusHelper(this).observe(this) {
            if (it == NetworkStatus.Available) {
                binding.main.visibility = View.VISIBLE
                binding.networkLayout.root.visibility = View.GONE
            } else {
                binding.main.visibility = View.GONE
                binding.networkLayout.root.visibility = View.VISIBLE
            }
        }
    }

    override fun checkInternet() {
        checkConnection()

        binding.networkLayout.btnTryAgain.setOnClickListener {
            checkConnection()
        }
    }

    private fun getYoutubeDownloadUrl(youtubeLink: String?) {
        object : YouTubeExtractor(this) {
            @SuppressLint("StaticFieldLeak")
            public override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                vMeta: VideoMeta?
            ) {
                if (ytFiles == null) {
                    Toast.makeText(this@VideoPlayerActivity, "Error", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
                var i = 0
                var itag: Int
                while (i < ytFiles.size()) {
                    itag = ytFiles.keyAt(i)
                    val ytFile = ytFiles[itag]

                    if (ytFile.format.height == -1 || ytFile.format.height >= 360) {
                        Log.e("itag is ", itag.toString())
                        addButtonToMainLayout(vMeta!!.title, ytFile)
                    }
                    i++
                }
            }
        }.extract(youtubeLink)
    }

    private fun addButtonToMainLayout(videoTitle: String, ytfile: YtFile) {
        var btnText = if (ytfile.format.height == -1) "Audio " +
                ytfile.format.audioBitrate + " kbit/s" else ytfile.format.height.toString() + "p"
        btnText += if (ytfile.format.isDashContainer) " dash" else ""
        val btn = RadioButton(this)
//        radio.text = btnText
        btn.text = btnText
        btn.setOnClickListener {
            var filename: String
            filename = if (videoTitle.length > 55) {
                videoTitle.substring(0, 55) + "." + ytfile.format.ext
            } else {
                videoTitle + "." + ytfile.format.ext
            }
            filename = filename.replace("[\\\\><\"|*?%:#/]".toRegex(), "")
            downloadFromUrl(ytfile.url, videoTitle, filename)
            //показать описание и скрыть выбор при выборе
            binding.mainDetails.visibility = View.VISIBLE
            binding.downloadLayout.visibility = View.INVISIBLE
            finish()
        }

        binding.mainDetails.visibility = View.GONE
        binding.downloadLayout.visibility = View.VISIBLE

        binding.cancelDownloading.setOnClickListener {
            binding.mainDetails.visibility = View.VISIBLE
            binding.downloadLayout.visibility = View.INVISIBLE
        }
        binding.downloadLayout!!.addView(btn)
    }

    private fun downloadFromUrl(youtubeDlUrl: String, downloadTitle: String, fileName: String) {
        val uri = Uri.parse(youtubeDlUrl)
        val request = DownloadManager.Request(uri)
        request.setTitle(downloadTitle)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }


}

