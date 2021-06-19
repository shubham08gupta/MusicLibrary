package com.strings.attached.musiclibrary.ui.album.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.strings.attached.musiclibrary.databinding.FragmentAlbumDetailBinding
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}