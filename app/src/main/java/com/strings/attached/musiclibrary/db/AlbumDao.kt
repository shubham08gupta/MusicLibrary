package com.strings.attached.musiclibrary.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao : BaseDao<AlbumDetail> {

    @Query("SELECT * FROM AlbumDetail")
    fun getAllAlbums(): Flow<List<AlbumDetail>>

    @Query("SELECT * FROM AlbumDetail WHERE albumName = :albumName AND artistName = :artistName")
    suspend fun getAlbumDetail(albumName: String, artistName: String): AlbumDetail?

    @Transaction
    @Query("SELECT * FROM AlbumDetail WHERE albumName = :albumName AND artistName = :artistName")
    suspend fun getAlbumWithTracksDetail(albumName: String, artistName: String): AlbumWithTracks?
}