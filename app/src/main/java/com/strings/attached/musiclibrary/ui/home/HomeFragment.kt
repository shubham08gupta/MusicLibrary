package com.strings.attached.musiclibrary.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.strings.attached.musiclibrary.R
import com.strings.attached.musiclibrary.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private var albumDetailJob: Job? = null

    private val adapter = HomeAdapter {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(
                artistName = it.artistName,
                albumName = it.albumName
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        albumDetailJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            homeViewModel.locallySavedAlbumsFlow.collect { albumList ->
                adapter.submitList(albumList)
                binding.emptyList.isVisible = albumList.isEmpty()
                binding.tvTitleLocalAlbums.isVisible = albumList.isNotEmpty()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                    true
                }
                else -> false
            }
        }
        binding.albumsRv.adapter = adapter
    }

    override fun onStop() {
        albumDetailJob?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}