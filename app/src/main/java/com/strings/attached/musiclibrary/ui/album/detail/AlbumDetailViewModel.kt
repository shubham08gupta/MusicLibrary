package com.strings.attached.musiclibrary.ui.album.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.strings.attached.musiclibrary.data.album.AlbumRepository
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel @AssistedInject constructor(
    private val albumRepository: AlbumRepository,
    @Assisted("artistName") private val artistName: String,
    @Assisted("albumName") private val albumName: String,
) : ViewModel() {

    private val _albumDetailUiState: MutableStateFlow<AlbumDetailUiState> =
        MutableStateFlow(AlbumDetailUiState.Loading)
    val albumDetailUiState: StateFlow<AlbumDetailUiState> get() = _albumDetailUiState

    private val _albumAvailabilityState: MutableStateFlow<AlbumAvailabilityState> =
        MutableStateFlow(AlbumAvailabilityState.NOT_AVAILABLE_LOCALLY)
    val albumAvailabilityState: StateFlow<AlbumAvailabilityState> get() = _albumAvailabilityState

    init {
        viewModelScope.launch {
            val result = albumRepository.getAlbumDetail(artistName, albumName)
            result.fold({
                _albumDetailUiState.value = AlbumDetailUiState.Success(it)
            }, {
                _albumDetailUiState.value = AlbumDetailUiState.Error(it)
            })
        }
        viewModelScope.launch {
            val isAlbumAvailableLocally = albumRepository.isAlbumAvailableLocally(
                albumName = albumName,
                artistName = artistName
            )
            _albumAvailabilityState.value = if (isAlbumAvailableLocally) {
                AlbumAvailabilityState.AVAILABLE_LOCALLY
            } else {
                AlbumAvailabilityState.NOT_AVAILABLE_LOCALLY
            }
        }
    }

    fun saveOrDeleteAlbum() = viewModelScope.launch {
        if (albumDetailUiState.value is AlbumDetailUiState.Success) {
            val albumWithTracks =
                (albumDetailUiState.value as AlbumDetailUiState.Success).albumWithTracks

            // update the UI after saving or deleting the album
            _albumAvailabilityState.value =
                if (albumAvailabilityState.value == AlbumAvailabilityState.NOT_AVAILABLE_LOCALLY) {
                    albumRepository.saveAlbumWithTracksLocally(albumWithTracks)
                    AlbumAvailabilityState.AVAILABLE_LOCALLY
                } else {
                    albumRepository.deleteLocalAlbum(albumWithTracks)
                    AlbumAvailabilityState.NOT_AVAILABLE_LOCALLY
                }
        }
    }

    /***
     * Use an assisted factory to inject manual parameters
     */
    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(
            @Assisted("artistName") artistName: String,
            @Assisted("albumName") albumName: String,
        ): AlbumDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            artistName: String,
            albumName: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(artistName, albumName) as T
            }
        }
    }
}

sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()
    data class Success(val albumWithTracks: AlbumWithTracks) : AlbumDetailUiState()
    data class Error(val throwable: Throwable) : AlbumDetailUiState()
}

enum class AlbumAvailabilityState {
    AVAILABLE_LOCALLY, // the album is available in the local DB
    NOT_AVAILABLE_LOCALLY // the album is not available in the local DB
}
