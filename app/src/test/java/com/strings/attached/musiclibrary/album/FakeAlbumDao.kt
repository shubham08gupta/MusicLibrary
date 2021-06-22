package com.strings.attached.musiclibrary.album

import com.strings.attached.musiclibrary.db.AlbumDao
import com.strings.attached.musiclibrary.model.album.AlbumDetail
import com.strings.attached.musiclibrary.model.album.AlbumWithTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAlbumDao(
    private val albumList: MutableList<AlbumDetail> = mutableListOf()
) : AlbumDao {
    override fun getAllAlbums(): Flow<List<AlbumDetail>> = flow {
        emit(albumList)
    }

    override suspend fun getAlbumDetail(albumName: String, artistName: String): AlbumDetail? {
        return albumList.find {
            it.albumName == albumName && it.artistName == artistName
        }
    }

    override suspend fun getAlbumWithTracksDetail(
        albumName: String,
        artistName: String
    ): AlbumWithTracks? {
        val album = getAlbumDetail(albumName, artistName)
        return if (album == null) null
        else AlbumWithTracks(
            album = album,
            tracks = album.tracks?.tracks ?: emptyList()
        )
    }

    override suspend fun insert(obj: AlbumDetail) {
        albumList.add(obj)
    }

    override suspend fun insert(obj: List<AlbumDetail>) {
        albumList.addAll(obj)
    }

    override suspend fun insertOrIgnore(obj: AlbumDetail): Long {
        val sameObj = albumList.find {
            it.albumId == obj.albumId
        }
        return if (sameObj == null) {
            albumList.add(obj)
            (albumList.size - 1).toLong()
        } else {
            -1L
        }
    }

    override suspend fun update(obj: AlbumDetail) {
        val target = albumList.find {
            it.albumId == obj.albumId
        }
        target?.let {
            albumList.remove(it)
            albumList.add(obj)
        }
    }

    override suspend fun delete(obj: AlbumDetail) {
        albumList.remove(obj)
    }
}