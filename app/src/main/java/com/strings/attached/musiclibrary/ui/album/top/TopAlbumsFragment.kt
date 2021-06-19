package com.strings.attached.musiclibrary.ui.album.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.strings.attached.musiclibrary.databinding.FragmentTopAlbumsBinding
import com.strings.attached.musiclibrary.ui.util.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TopAlbumsFragment : Fragment() {

    private val args: TopAlbumsFragmentArgs by navArgs()

    private var _binding: FragmentTopAlbumsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var topAlbumsViewModelFactory: TopAlbumsViewModel.AssistedFactory
    private val topAlbumsViewModel: TopAlbumsViewModel by viewModels {
        TopAlbumsViewModel.provideFactory(topAlbumsViewModelFactory, args.artistName)
    }

    private var topAlbumSearchJob: Job? = null

    private val adapter = TopAlbumsAdapter {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        topAlbumSearchJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topAlbumsViewModel.topAlbumsFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.toolbar.apply {
            title = args.artistName
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun initAdapter() {
        binding.topAlbumsRv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter { adapter.retry() },
            footer = LoadingStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            binding.topAlbumsRv.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            errorState?.let {
                if (context != null)
                    Toast.makeText(context, "${it.error}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun showEmptyList(show: Boolean) {
        binding.emptyList.isVisible = show
        binding.topAlbumsRv.isVisible = !show
    }

    override fun onStop() {
        topAlbumSearchJob?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}