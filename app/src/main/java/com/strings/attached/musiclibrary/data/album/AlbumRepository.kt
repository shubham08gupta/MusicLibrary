package com.strings.attached.musiclibrary.data.album

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.strings.attached.musiclibrary.api.album.AlbumApiService
import com.strings.attached.musiclibrary.data.album.TopAlbumsPagingSource.Companion.NETWORK_PAGE_SIZE
import com.strings.attached.musiclibrary.model.album.Album
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface AlbumRepository {

    /***
     * returns a [Flow] of [PagingData] of top [Album]s by an artist name
     */
    fun getTopAlbumsByArtistName(artistName: String): Flow<PagingData<Album>>

    suspend fun getAlbumDetail(artistName: String, albumName: String): Result<AlbumDetail>
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

    override suspend fun getAlbumDetail(
        artistName: String,
        albumName: String
    ): Result<AlbumDetail> {
        return try {
            val response = albumService.getAlbumDetails(
                artistName = artistName,
                albumName = albumName
            )
            if (response.album != null) {
                Result.success(response.album)
            } else if (!response.error.isNullOrEmpty()) {
                Result.failure(IOException(response.message ?: "Some error occurred"))
            } else {
                Result.failure(UnknownError("Some error occurred"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}
