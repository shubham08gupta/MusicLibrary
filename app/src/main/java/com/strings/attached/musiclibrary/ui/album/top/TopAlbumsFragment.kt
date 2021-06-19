package com.strings.attached.musiclibrary.ui.album.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.strings.attached.musiclibrary.databinding.FragmentTopAlbumsBinding
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }
}