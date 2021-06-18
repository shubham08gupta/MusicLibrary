package com.strings.attached.musiclibrary.data.artist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.strings.attached.musiclibrary.api.artist.ArtistApiService
import com.strings.attached.musiclibrary.model.artist.Artist
import retrofit2.HttpException
import java.io.IOException

// Last FM starting index is 1
private const val STARTING_PAGE_INDEX = 1

/***
 * Use a [PagingSource] to fetch a list of [Artist]
 */
class ArtistPagingSource(
    private val apiService: ArtistApiService,
    private val query: String
) : PagingSource<Int, Artist>() {
    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = apiService.searchArtistsByName(query, position, params.loadSize)
            if (!response.error.isNullOrEmpty()) {
                return LoadResult.Error(IOException(response.message))
            }
            val artists = response.results?.artistMatches?.artist ?: emptyList()
            val nextKey = if (artists.isEmpty()) {
                null
            } else {
                // By default, the initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = artists,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}