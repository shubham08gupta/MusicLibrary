package com.strings.attached.musiclibrary.api.album

import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApiService {

    @GET("?method=artist.gettopalbums")
    suspend fun getTopAlbumsByArtistName(
        @Query("artist") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): TopAlbumsResponse

    @GET("?method=album.getinfo")
    suspend fun getAlbumDetails(
        @Query("artist") artistName: String,
        @Query("album") albumName: String
    ): AlbumDetailResponse
}
