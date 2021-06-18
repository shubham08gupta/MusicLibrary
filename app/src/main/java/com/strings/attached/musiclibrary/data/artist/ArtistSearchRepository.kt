package com.strings.attached.musiclibrary.data.artist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.strings.attached.musiclibrary.api.artist.ArtistApiService
import com.strings.attached.musiclibrary.data.artist.ArtistPagingSource.Companion.NETWORK_PAGE_SIZE
import com.strings.attached.musiclibrary.model.artist.Artist
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ArtistSearchRepository {

    fun searchArtistsByName(name: String): Flow<PagingData<Artist>>
}

/***
 * Use pagination to fetch the artists list
 */
@Singleton
class ArtistSearchRepositoryImpl @Inject constructor(
    private val apiService: ArtistApiService,
) : ArtistSearchRepository {

    override fun searchArtistsByName(name: String): Flow<PagingData<Artist>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            initialKey = null,
            pagingSourceFactory = { ArtistPagingSource(apiService, name) }
        ).flow
    }

}