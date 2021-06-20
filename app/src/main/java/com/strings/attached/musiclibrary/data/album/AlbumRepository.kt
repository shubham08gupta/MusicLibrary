package com.strings.attached.musiclibrary.data.album

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.strings.attached.musiclibrary.api.album.AlbumApiService
import com.strings.attached.musiclibrary.data.album.TopAlbumsPagingSource.Companion.NETWORK_PAGE_SIZE
import com.strings.attached.musiclibrary.db.AlbumDao
import com.strings.attached.musiclibrary.db.TrackDao
import com.strings.attached.musiclibrary.model.album.Album
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface AlbumRepository {

    /***
     * returns a [Flow] of [PagingData] of top [Album]s by an artist name
     */
    fun getTopAlbumsByArtistName(artistName: String): Flow<PagingData<Album>>

    /***
     * returns an Album with all its tracks either from the DB or from the network.
     */
    suspend fun getAlbumDetail(artistName: String, albumName: String): Result<AlbumWithTracks>

    /***
     * saves an album with all its tracks in the local DB
     */
    suspend fun saveAlbumWithTracksLocally(albumWithTracks: AlbumWithTracks)

    /***
     * deletes an album with all its tracks in the local DB
     */
    suspend fun deleteLocalAlbum(albumWithTracks: AlbumWithTracks)

    /***
     * A method to check if an album is available in the local DB or not
     */
    suspend fun isAlbumAvailableLocally(albumName: String, artistName: String): Boolean

    /***
     * returns a [Flow] of all the locally saved albums
     */
    fun getLocallySavedAlbums(): Flow<List<AlbumDetail>>
}

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val albumService: AlbumApiService,
    private val albumDao: AlbumDao,
    private val trackDao: TrackDao,
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
    ): Result<AlbumWithTracks> {
        // get data from the DB
        val dbAlbum = albumDao.getAlbumWithTracksDetail(
            albumName = albumName,
            artistName = artistName
        )
        return try {
            // get data from the network
            val response = albumService.getAlbumDetails(
                artistName = artistName,
                albumName = albumName
            )
            if (response.album != null) {
                val album = response.album.copy(
                    // use the large sized image
                    albumImageUrl = response.album.image.find { it.size == "large" }?.text
                )
                val tracks = response.album.tracks?.tracks ?: emptyList()
                val albumWithTracks = AlbumWithTracks(album = album, tracks = tracks)
                Result.success(albumWithTracks)
            } else if (!response.error.isNullOrEmpty()) {
                Result.failure(IOException(response.message ?: "Some error occurred"))
            } else {
                Result.failure(UnknownError("Some error occurred"))
            }
        } catch (e: Exception) {
            // send DB data if network is not available
            if (dbAlbum == null) {
                Result.failure(e)
            } else {
                Result.success(dbAlbum)
            }
        }
    }

    override suspend fun saveAlbumWithTracksLocally(albumWithTracks: AlbumWithTracks) {
        // insert the album to create a primary key
        albumDao.insert(albumWithTracks.album)
        // fetch the album to get the primary key
        albumDao.getAlbumDetail(
            albumName = albumWithTracks.album.albumName,
            artistName = albumWithTracks.album.artistName
        )?.let { album ->
            albumWithTracks.tracks.forEach { track ->
                // create a foreign key and then save
                trackDao.insert(track.copy(trackAlbumId = album.albumId))
            }
        }
    }

    override suspend fun deleteLocalAlbum(albumWithTracks: AlbumWithTracks) {
        // find the album and delete it
        albumDao.getAlbumDetail(
            albumName = albumWithTracks.album.albumName,
            artistName = albumWithTracks.album.artistName
        )?.let { album ->
            // deleting the album will delete all its tracks also because of the foreign key constraint
            albumDao.delete(album)
        }
    }

    override suspend fun isAlbumAvailableLocally(albumName: String, artistName: String): Boolean {
        return albumDao.getAlbumWithTracksDetail(albumName, artistName) != null
    }

    override fun getLocallySavedAlbums(): Flow<List<AlbumDetail>> {
        return albumDao.getAllAlbums()
    }
}
