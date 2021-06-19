package com.strings.attached.musiclibrary.ui.album.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.strings.attached.musiclibrary.data.album.AlbumRepository
import com.strings.attached.musiclibrary.model.album.Album
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class TopAlbumsViewModel @AssistedInject constructor(
    albumRepository: AlbumRepository,
    @Assisted("artistName") private val artistName: String,
) : ViewModel() {

    val topAlbumsFlow: Flow<PagingData<Album>> =
        albumRepository.getTopAlbumsByArtistName(artistName)
            .cachedIn(viewModelScope)

    /***
     * Use an assisted factory to inject manual parameters
     */
    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(
            @Assisted("artistName") artistName: String,
        ): TopAlbumsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            artistName: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(artistName) as T
            }
        }
    }
}