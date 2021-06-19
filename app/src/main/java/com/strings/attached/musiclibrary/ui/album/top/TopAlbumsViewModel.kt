package com.strings.attached.musiclibrary.ui.album.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class TopAlbumsViewModel @AssistedInject constructor(
    @Assisted("artistName") private val artistName: String,
) : ViewModel() {

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