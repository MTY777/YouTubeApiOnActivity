package com.mty.youtubeapiactivity.ui.playlistvideo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mty.youtubeapiactivity.R
import com.mty.youtubeapiactivity.databinding.PlaylistVideoItemBinding
import com.mty.youtubeapiactivity.extension.Glide

import com.mty.youtubeapiactivity.model.Item

class PlayListVideoAdapter(
    private val list: ArrayList<Item>,
    private val onItemClick: (itemsId: String, String, String) -> Unit?
) : RecyclerView.Adapter<PlayListVideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PlaylistVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: PlaylistVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(items: Item) {
//            Glide.with(binding.root).load(items.snippet.thumbnails.default.url).into(binding.imageEv)

            if (items.snippet.title == "Private video" || items.snippet.title == "Deleted video") {
                Glide.with(binding.root).load(R.drawable.not_found).into(binding.imageEv)
            } else {
                if (items.snippet.thumbnails.default.url != null) {
                    binding.imageEv.Glide(items.snippet.thumbnails.default.url)
                } else if (items.snippet.thumbnails.medium.url != null) {
                    binding.imageEv.Glide(items.snippet.thumbnails.medium.url)
                } else if (items.snippet.thumbnails.high.url != null) {
                    binding.imageEv.Glide(items.snippet.thumbnails.high.url)
                } else if (items.snippet.thumbnails.standard.url != null) {
                    binding.imageEv.Glide(items.snippet.thumbnails.standard.url)
                } else if (items.snippet.thumbnails.maxres.url != null) {
                    binding.imageEv.Glide(items.snippet.thumbnails.maxres.url)
                }
            }
            binding.playlistNameTv.text = items.snippet.title
            binding.timeTv.text = items.snippet.publishedAt.dropLast(10)
            itemView.setOnClickListener {
                onItemClick(items.snippet.videoId, items.snippet.title, items.snippet.description)
            }
        }
    }
}