package com.strings.attached.musiclibrary.ui.album.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.strings.attached.musiclibrary.data.album.AlbumRepository
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel @AssistedInject constructor(
    albumRepository: AlbumRepository,
    @Assisted("artistName") private val artistName: String,
    @Assisted("albumName") private val albumName: String,
) : ViewModel() {

    private val _albumDetailUiState: MutableStateFlow<AlbumDetailUiState> =
        MutableStateFlow(AlbumDetailUiState.Loading)
    val albumDetailUiState: StateFlow<AlbumDetailUiState> get() = _albumDetailUiState

    init {
        viewModelScope.launch {
            val result = albumRepository.getAlbumDetail(artistName, albumName)
            result.fold({
                _albumDetailUiState.value = AlbumDetailUiState.Success(it)
            }, {
                _albumDetailUiState.value = AlbumDetailUiState.Error(it)
            })
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
    data class Success(val album: AlbumDetail) : AlbumDetailUiState()
    data class Error(val throwable: Throwable) : AlbumDetailUiState()
}
