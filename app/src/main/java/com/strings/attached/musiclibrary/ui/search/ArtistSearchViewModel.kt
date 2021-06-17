package com.strings.attached.musiclibrary.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistSearchViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val searchQueryStateFlow: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            savedStateHandle.getLiveData<String>(KEY_SEARCH_QUERY).asFlow().collectLatest {
                searchQueryStateFlow.value = it
            }
        }
    }

    fun setSearchQuery(query: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "query"
    }
}