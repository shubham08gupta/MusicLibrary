package com.strings.attached.musiclibrary.ui.album.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strings.attached.musiclibrary.R
import com.strings.attached.musiclibrary.databinding.ItemAlbumViewBinding
import com.strings.attached.musiclibrary.model.album.Track

/***
 * Recycler View to show a list of tracks in a Album
 */
class AlbumTrackAdapter(
    private val onTrackClicked: (Track) -> Unit
) : ListAdapter<Track, AlbumTrackAdapter.TrackViewHolder>(TRACK_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemAlbumViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, onTrackClicked)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackItem = getItem(holder.bindingAdapterPosition)
        trackItem?.let { holder.bind(it) }
    }

    inner class TrackViewHolder(
        private val binding: ItemAlbumViewBinding,
        private val onClicked: (Track) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.apply {
                ivAlbumImage.load(R.drawable.ic_baseline_play_circle_filled_24)
                tvAlbumName.text = track.name
                val durationInMinutes = track.duration.toInt().div(60)
                val durationInSeconds = track.duration.toInt().rem(60)
                tvPlayCount.text = if (durationInMinutes == 0) {
                    "${durationInSeconds}s"
                } else {
                    "${durationInMinutes}m ${durationInSeconds}s"
                }
                root.setOnClickListener { onClicked.invoke(track) }
            }
        }
    }

    companion object {
        private val TRACK_COMPARATOR = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Track, newItem: Track) =
                oldItem == newItem
        }
    }
}