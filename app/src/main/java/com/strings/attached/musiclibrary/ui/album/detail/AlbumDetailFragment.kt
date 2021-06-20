package com.strings.attached.musiclibrary.ui.album.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.strings.attached.musiclibrary.R
import com.strings.attached.musiclibrary.databinding.FragmentAlbumDetailBinding
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AlbumDetailFragment : Fragment() {

    private val args: AlbumDetailFragmentArgs by navArgs()

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var albumDetailModelFactory: AlbumDetailViewModel.AssistedFactory
    private val albumDetailViewModel: AlbumDetailViewModel by viewModels {
        AlbumDetailViewModel.provideFactory(
            albumDetailModelFactory,
            args.artistName,
            args.albumName
        )
    }

    private var uiStateJob: Job? = null
    private var albumAvailabilityJob: Job? = null

    private val trackAdapter = AlbumTrackAdapter {
        activity?.apply {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.url)))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        uiStateJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            albumDetailViewModel.albumDetailUiState.collect { state ->
                when (state) {
                    is AlbumDetailUiState.Error -> {
                        binding.progressBar.isVisible = false
                        Snackbar.make(
                            binding.container,
                            state.throwable.message ?: "Some error occurred",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is AlbumDetailUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is AlbumDetailUiState.Success -> {
                        binding.progressBar.isVisible = false
                        showAlbumDetail(state.albumWithTracks)
                    }
                }
            }
        }
        albumAvailabilityJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            albumDetailViewModel.albumAvailabilityState.collectLatest {
                when (it) {
                    AlbumAvailabilityState.AVAILABLE_LOCALLY -> {
                        binding.fab.setImageResource(R.drawable.ic_baseline_delete_24)
                    }
                    AlbumAvailabilityState.NOT_AVAILABLE_LOCALLY -> {
                        binding.fab.setImageResource(R.drawable.ic_baseline_cloud_download_24)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.content.tracksRv.adapter = trackAdapter
        binding.fab.setOnClickListener {
            albumDetailViewModel.saveOrDeleteAlbum()
        }
    }

    private fun showAlbumDetail(albumWithTracks: AlbumWithTracks) {
        binding.apply {
            ivAlbumImage.load(albumWithTracks.album.albumImageUrl) {
                crossfade(true)
                error(R.drawable.drawable_placeholder)
                placeholder(R.drawable.drawable_placeholder)
            }
            toolbar.title = albumWithTracks.album.artistName
            content.tvAlbumName.text = albumWithTracks.album.albumName
            content.tvNumListeners.apply {
                text = resources.getQuantityString(
                    R.plurals.listeners_count,
                    albumWithTracks.album.listeners.toIntOrNull() ?: 0,
                    albumWithTracks.album.listeners
                )
            }
            content.tvNumTracks.apply {
                text = resources.getQuantityString(
                    R.plurals.tracks_count,
                    albumWithTracks.tracks.size,
                    albumWithTracks.tracks.size
                )
            }
            trackAdapter.submitList(albumWithTracks.tracks)
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        albumAvailabilityJob?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}