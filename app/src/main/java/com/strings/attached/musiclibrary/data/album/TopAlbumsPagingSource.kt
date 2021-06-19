package com.strings.attached.musiclibrary.data.album

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.strings.attached.musiclibrary.api.album.AlbumApiService
import com.strings.attached.musiclibrary.model.album.Album
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

/***
 * Returns a paginated list of top albums of an artist. As the user scrolls, more albums are loaded.
 */
class TopAlbumsPagingSource(
    private val apiService: AlbumApiService,
    private val artistName: String
) : PagingSource<Int, Album>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response =
                apiService.getTopAlbumsByArtistName(artistName, position, params.loadSize)
            if (!response.error.isNullOrEmpty()) {
                return LoadResult.Error(IOException(response.message))
            }
            val topAlbums = response.topAlbums?.albums ?: emptyList()
            val nextKey = if (topAlbums.isEmpty()) {
                null
            } else {
                // By default, the initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = topAlbums,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}