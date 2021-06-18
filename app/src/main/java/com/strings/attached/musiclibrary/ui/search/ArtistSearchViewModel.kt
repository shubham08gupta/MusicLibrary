package com.strings.attached.musiclibrary.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.strings.attached.musiclibrary.data.artist.ArtistSearchRepository
import com.strings.attached.musiclibrary.model.artist.Artist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistSearchViewModel @Inject constructor(
    private val artistSearchRepository: ArtistSearchRepository,
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

    val searchQueryResult: Flow<PagingData<Artist>> =
        searchQueryStateFlow.flatMapLatest { searchQuery ->
            // no need to check duplicate search query as StateFlow automatically filters duplicate values
            artistSearchRepository.searchArtistsByName(searchQuery).cachedIn(viewModelScope)
        }

    companion object {
        private const val KEY_SEARCH_QUERY = "query"
    }
}