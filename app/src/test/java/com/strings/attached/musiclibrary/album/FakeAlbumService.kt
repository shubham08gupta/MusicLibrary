package com.strings.attached.musiclibrary.album

import com.strings.attached.musiclibrary.api.album.AlbumApiService
import com.strings.attached.musiclibrary.api.album.AlbumDetailResponse
import com.strings.attached.musiclibrary.api.album.TopAlbums
import com.strings.attached.musiclibrary.api.album.TopAlbumsResponse
import com.strings.attached.musiclibrary.model.album.Album
import com.strings.attached.musiclibrary.model.album.AlbumDetail

class FakeAlbumService : AlbumApiService {

    private var topAlbumList: MutableList<Album> = mutableListOf()
    var albumDetail: AlbumDetail? = null
    var error: String? = null
    var errorMessage: String? = null
    var exception: Exception? = null

    override suspend fun getTopAlbumsByArtistName(
        name: String,
        page: Int,
        limit: Int
    ): TopAlbumsResponse {
        if (exception != null) throw exception as Exception
        return TopAlbumsResponse(
            if (topAlbumList.isEmpty()) null else TopAlbums(topAlbumList),
            error,
            errorMessage
        )
    }

    override suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): AlbumDetailResponse {
        if (exception != null) throw exception as Exception
        return AlbumDetailResponse(
            albumDetail,
            error,
            errorMessage
        )
    }
}