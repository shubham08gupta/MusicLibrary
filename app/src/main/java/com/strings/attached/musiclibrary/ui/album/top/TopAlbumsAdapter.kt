package com.strings.attached.musiclibrary.ui.album.top

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.strings.attached.musiclibrary.R
import com.strings.attached.musiclibrary.databinding.ItemAlbumViewBinding
import com.strings.attached.musiclibrary.model.album.Album

class TopAlbumsAdapter(
    private val onAlbumClicked: (Album) -> Unit
) : PagingDataAdapter<Album, TopAlbumsAdapter.TopAlbumsViewHolder>(ALBUM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAlbumsViewHolder {
        val binding = ItemAlbumViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopAlbumsViewHolder(binding, onAlbumClicked)
    }

    override fun onBindViewHolder(holder: TopAlbumsViewHolder, position: Int) {
        val artistItem = getItem(holder.bindingAdapterPosition)
        artistItem?.let { holder.bind(it) }
    }

    inner class TopAlbumsViewHolder(
        private val binding: ItemAlbumViewBinding,
        private val onClicked: (Album) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.apply {
                ivAlbumImage.load(album.image.find { it.size == "medium" }?.text) {
                    transformations(CircleCropTransformation())
                    crossfade(true)
                    error(R.drawable.drawable_placeholder)
                    placeholder(R.drawable.drawable_placeholder)
                }
                tvAlbumName.text = album.name
                tvPlayCount.apply {
                    text = resources.getQuantityString(
                        R.plurals.play_count,
                        album.playCount,
                        album.playCount
                    )
                }
                root.setOnClickListener { onClicked.invoke(album) }
            }
        }
    }

    companion object {
        private val ALBUM_COMPARATOR = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album) =
                oldItem.mbid == newItem.mbid

            override fun areContentsTheSame(oldItem: Album, newItem: Album) =
                oldItem == newItem
        }
    }
}