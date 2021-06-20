package com.strings.attached.musiclibrary.ui.home

import androidx.lifecycle.ViewModel
import com.strings.attached.musiclibrary.data.album.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    albumRepository: AlbumRepository
) : ViewModel() {

    val locallySavedAlbumsFlow = albumRepository.getLocallySavedAlbums()
}