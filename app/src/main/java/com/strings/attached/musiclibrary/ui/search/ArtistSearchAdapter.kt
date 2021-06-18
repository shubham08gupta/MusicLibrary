package com.strings.attached.musiclibrary.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.strings.attached.musiclibrary.R
import com.strings.attached.musiclibrary.model.artist.Artist
import com.strings.attached.musiclibrary.databinding.ItemArtistViewBinding

class ArtistSearchAdapter(
    private val onArtistClicked: (Artist) -> Unit
) : PagingDataAdapter<Artist, ArtistSearchAdapter.SearchResultViewHolder>(ARTIST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemArtistViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding, onArtistClicked)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val artistItem = getItem(holder.bindingAdapterPosition)
        artistItem?.let { holder.bind(it) }
    }

    inner class SearchResultViewHolder(
        private val binding: ItemArtistViewBinding,
        private val onClicked: (Artist) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            binding.apply {
                imageViewArtistPhoto.load(artist.image.find { it.size == "medium" }?.text) {
                    transformations(CircleCropTransformation())
                    crossfade(true)
                    error(R.drawable.drawable_placeholder)
                    placeholder(R.drawable.drawable_placeholder)
                }
                textViewUserName.text = artist.name
                root.setOnClickListener { onClicked.invoke(artist) }
            }
        }
    }

    companion object {
        private val ARTIST_COMPARATOR = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist) =
                oldItem.mbid == newItem.mbid

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist) =
                oldItem == newItem
        }
    }
}