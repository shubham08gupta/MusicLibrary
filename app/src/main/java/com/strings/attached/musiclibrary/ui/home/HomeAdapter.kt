package com.strings.attached.musiclibrary.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.strings.attached.musiclibrary.R
import com.strings.attached.musiclibrary.databinding.ItemAlbumViewBinding
import com.strings.attached.musiclibrary.model.album.AlbumDetail

/***
 * An adapter to show all the locally saved [AlbumDetail].
 */
class HomeAdapter(
    private val onAlbumClicked: (AlbumDetail) -> Unit
) : ListAdapter<AlbumDetail, HomeAdapter.AlbumsViewHolder>(ALBUM_DETAIL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val binding = ItemAlbumViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumsViewHolder(binding, onAlbumClicked)
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        val albumDetailItem = getItem(holder.bindingAdapterPosition)
        albumDetailItem?.let { holder.bind(it) }
    }

    inner class AlbumsViewHolder(
        private val binding: ItemAlbumViewBinding,
        private val onClicked: (AlbumDetail) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumDetail) {
            binding.apply {
                ivAlbumImage.load(album.albumImageUrl) {
                    transformations(CircleCropTransformation())
                    crossfade(true)
                    error(R.drawable.drawable_placeholder)
                    placeholder(R.drawable.drawable_placeholder)
                }
                tvAlbumName.text = album.albumName
                tvPlayCount.text = album.artistName
                root.setOnClickListener { onClicked.invoke(album) }
            }
        }
    }

    companion object {
        private val ALBUM_DETAIL_COMPARATOR = object : DiffUtil.ItemCallback<AlbumDetail>() {
            override fun areItemsTheSame(oldItem: AlbumDetail, newItem: AlbumDetail) =
                oldItem.albumId == newItem.albumId

            override fun areContentsTheSame(oldItem: AlbumDetail, newItem: AlbumDetail) =
                oldItem == newItem
        }
    }
}