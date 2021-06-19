package com.strings.attached.musiclibrary.data.album

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.strings.attached.musiclibrary.api.album.AlbumApiService
import com.strings.attached.musiclibrary.data.album.TopAlbumsPagingSource.Companion.NETWORK_PAGE_SIZE
import com.strings.attached.musiclibrary.model.album.Album
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface AlbumRepository {

    /***
     * returns a [Flow] of [PagingData] of top [Album]s by an artist name
     */
    fun getTopAlbumsByArtistName(artistName: String): Flow<PagingData<Album>>
}

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val albumService: AlbumApiService,
) : AlbumRepository {
    override fun getTopAlbumsByArtistName(artistName: String): Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            initialKey = null,
            pagingSourceFactory = { TopAlbumsPagingSource(albumService, artistName) }
        ).flow
    }

}
