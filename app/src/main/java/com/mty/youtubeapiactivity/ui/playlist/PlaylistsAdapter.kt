package com.mty.youtubeapiactivity.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mty.youtubeapiactivity.R

import com.mty.youtubeapiactivity.databinding.PlaylistItemBinding
import com.mty.youtubeapiactivity.extension.Glide
import com.mty.youtubeapiactivity.extension.load
import com.mty.youtubeapiactivity.model.Item
import kotlin.reflect.KFunction2

class PlaylistsAdapter(
    private var data: ArrayList<Item>,
    private val onItemClick: (item: String, String, String) -> Unit?
) : RecyclerView.Adapter<PlaylistsAdapter.PlayListsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListsViewHolder {
        val binding =
            PlaylistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        return PlayListsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PlayListsViewHolder(var binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            if (item.snippet.thumbnails.default.url != null) {
                binding.ivVideos.Glide(item.snippet.thumbnails.default.url)
            } else if (item.snippet.thumbnails.medium.url != null) {
                binding.ivVideos.Glide(item.snippet.thumbnails.medium.url)
            } else if (item.snippet.thumbnails.high.url != null) {
                binding.ivVideos.Glide(item.snippet.thumbnails.high.url)
            } else if (item.snippet.thumbnails.standard.url != null) {
                binding.ivVideos.Glide(item.snippet.thumbnails.standard.url)
            } else if (item.snippet.thumbnails.maxres.url != null) {
                binding.ivVideos.Glide(item.snippet.thumbnails.maxres.url)
            }
            binding.tvName.text = item.snippet.title
            binding.tvDesc.text = String.format(
                "${item.contentDetails.itemCount} ${
                    itemView.context.getString(
                        R.string.playlist_video_series
                    )
                }"
            )
            binding.root.setOnClickListener() {
                onItemClick(item.snippet.title, item.snippet.description, item.id)
            }
        }
    }
}


